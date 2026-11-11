package persistence.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import Restaurante_Completo.LeCartoon_Original.Carrito;
import Restaurante_Completo.LeCartoon_Original.Carrito.ItemPedido;
import Restaurante_Completo.LeCartoon_Original.Order;
import Restaurante_Completo.LeCartoon_Original.OrderDetails;
import Restaurante_Completo.LeCartoon_Original.Resena;
import persistence.ConnectionFactory;
import persistence.DaoException;
import persistence.OrderDao;

/** OrderDao para el esquema de Alan: PEDIDO + DETALLES_PEDIDO + PLATILLO. */
public class OracleOrderDao implements OrderDao {

    @Override
    public List<Order> pedidosDe(int idCliente) {
        String sql = "SELECT p.id_pedido, p.id_cliente, p.id_status, "
                   + "       NVL((SELECT SUM(d.cantidad * pl.precio) "
                   + "            FROM detalles_pedido d JOIN platillo pl ON pl.id_platillo = d.id_platillo "
                   + "            WHERE d.id_pedido = p.id_pedido), 0) AS importe "
                   + "FROM pedido p WHERE p.id_cliente = ? ORDER BY p.id_pedido DESC";
        List<Order> pedidos = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double total = Math.round((rs.getDouble("importe") + Carrito.ENVIO) * 100) / 100.0;
                    pedidos.add(new Order(rs.getInt("id_pedido"), rs.getInt("id_cliente"),
                                          total, rs.getInt("id_status")));
                }
            }
            return pedidos;
        } catch (SQLException e) {
            throw new DaoException("No se pudieron leer los pedidos del cliente " + idCliente, e);
        }
    }
    
    @Override
    public List<OrderDetails> detallesDe(int idPedido) {
        String sql = "SELECT d.id_pedido, pl.nombre, d.cantidad "
                   + "FROM detalles_pedido d JOIN platillo pl ON pl.id_platillo = d.id_platillo "
                   + "WHERE d.id_pedido = ? ORDER BY pl.nombre";
        List<OrderDetails> detalles = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    detalles.add(new OrderDetails(rs.getInt("id_pedido"),
                                                  rs.getString("nombre"),
                                                  rs.getInt("cantidad")));
                }
            }
            return detalles;
        } catch (SQLException e) {
            throw new DaoException("No se pudieron leer los detalles del pedido " + idPedido, e);
        }
    }

    @Override
    public void cambiarEstado(int idPedido, int estado) {
        String sql = "UPDATE pedido SET id_status = ?, fecha_actualizacion = SYSDATE, "
                   + "       id_repartidor = CASE WHEN ? = 2 AND id_repartidor IS NULL "
                   + "                            THEN (SELECT MIN(r.id_repartidor) FROM repartidor r "
                   + "                                  WHERE r.id_sucursal = pedido.id_sucursal) "
                   + "                            ELSE id_repartidor END "
                   + "WHERE id_pedido = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estado);
            ps.setInt(2, estado);
            ps.setInt(3, idPedido);
            if (ps.executeUpdate() == 0) {
                throw new DaoException("No existe el pedido " + idPedido, null);
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo cambiar el estado del pedido " + idPedido, e);
        }
    }
    
    @Override
    public int sucursalDePedido(int idPedido) {
        String sql = "SELECT id_sucursal FROM pedido WHERE id_pedido = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new DaoException("No existe el pedido " + idPedido, null);
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo leer la sucursal del pedido " + idPedido, e);
        }
    }
    
    @Override
    public int registrar(int idCliente, int idDireccion, double total, List<ItemPedido> items) {
        // 'total' no se guarda: PEDIDO no tiene columna de total, se recalcula en pedidosDe().
        try (Connection con = ConnectionFactory.getConnection()) {
            con.setAutoCommit(false);
            try {
                int idSucursal = sucursalDe(con, idCliente);
                int idPedido = insertarPedido(con, idCliente, idSucursal, idDireccion);
                insertarDetalles(con, idPedido, items);
                con.commit();
                return idPedido;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo registrar el pedido", e);
        }
    }

    /** Sucursal donde está registrado el cliente; la 1 si no tiene. */
    private int sucursalDe(Connection con, int idCliente) throws SQLException {
        String sql = "SELECT NVL(MIN(id_sucursal), 1) FROM sucursal_cliente WHERE id_cliente = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private int insertarPedido(Connection con, int idCliente, int idSucursal, int idDireccion) throws SQLException {
        String sql = "INSERT INTO pedido (id_status, id_sucursal, id_cliente, id_direccion, fecha) VALUES (1, ?, ?, ?, SYSDATE)";
        try (PreparedStatement ps = con.prepareStatement(sql, new String[] { "ID_PEDIDO" })) {
            ps.setInt(1, idSucursal);
            ps.setInt(2, idCliente);
            ps.setInt(3, idDireccion);                     // a dónde se entrega (antes iba NULL)
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new SQLException("Oracle no regresó el id del pedido");
    }

    @Override
    public Resena resenaDe(int idPedido) {
        String sql = "SELECT calificacion, resena, fecha_actualizacion FROM pedido WHERE id_pedido = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;                     // no existe el pedido
                int estrellas = rs.getInt("calificacion");
                if (rs.wasNull()) return null;                   // existe, pero aún no lo califican
                Timestamp fecha = rs.getTimestamp("fecha_actualizacion");
                return new Resena(estrellas, rs.getString("resena"),
                                  fecha == null ? null : fecha.toLocalDateTime());
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo leer la reseña del pedido " + idPedido, e);
        }
    }

    @Override
    public void calificar(int idPedido, Resena resena) {
        // Solo pedidos ENTREGADOS (id_status = 3) y solo una vez (calificacion IS NULL).
        String sql = "UPDATE pedido SET calificacion = ?, resena = ?, fecha_actualizacion = SYSDATE "
                   + "WHERE id_pedido = ? AND id_status = 3 AND calificacion IS NULL";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, resena.getEstrellas());
            if (resena.getComentario().isEmpty()) {
                ps.setNull(2, Types.VARCHAR);                    // sin comentario = NULL
            } else {
                ps.setString(2, resena.getComentario());
            }
            ps.setInt(3, idPedido);
            if (ps.executeUpdate() == 0) {
                throw new DaoException("El pedido " + idPedido + " no existe, no está entregado o ya tiene reseña", null);
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo guardar la reseña del pedido " + idPedido, e);
        }
    }

    private void insertarDetalles(Connection con, int idPedido, List<ItemPedido> items) throws SQLException {
        String sql = "INSERT INTO detalles_pedido (id_pedido, id_platillo, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (ItemPedido item : items) {
                ps.setInt(1, idPedido);
                ps.setInt(2, item.getPlatillo().getId());
                ps.setInt(3, item.getCantidad());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}