package persistence.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Restaurante_Completo.LeCartoon_Original.Carrito;
import Restaurante_Completo.LeCartoon_Original.Carrito.ItemPedido;
import Restaurante_Completo.LeCartoon_Original.Order;
import Restaurante_Completo.LeCartoon_Original.OrderDetails;
import Restaurante_Completo.LeCartoon_Original.Repartidor;
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
    
    /**
     * Simulación del vendedor: a cada pedido del cliente que ya va en Enviado (o Entregado) y no tiene
     * repartidor, se le asigna uno de SU sucursal (al azar si hay varios). Regresa id_pedido -> repartidor
     * de los que se asignaron en esta llamada (vacío si no hubo).
     * El "AND id_repartidor IS NULL" del UPDATE evita pisar uno ya asignado (por la página de Alan, p. ej.).
     */
    @Override
    public Map<Integer, Repartidor> asignarRepartidores(int idCliente) {
        String pendientes = "SELECT id_pedido, id_sucursal FROM pedido "
                          + "WHERE id_cliente = ? AND id_status >= 2 AND id_repartidor IS NULL";
        String elegir = "SELECT r.id_repartidor, "
                      + "       pe.nombre || ' ' || pe.apellidopaterno "
                      + "       || NVL2(pe.apellidomaterno, ' ' || pe.apellidomaterno, NULL) AS nombre, "
                      + "       (SELECT MIN(t.telefono) FROM telefono t WHERE t.id_persona = pe.id_persona) AS telefono "
                      + "FROM   repartidor r JOIN persona pe ON pe.id_persona = r.id_persona "
                      + "WHERE  r.id_sucursal = ? "
                      + "ORDER  BY DBMS_RANDOM.VALUE "
                      + "FETCH FIRST 1 ROW ONLY";
        String asignar = "UPDATE pedido SET id_repartidor = ? WHERE id_pedido = ? AND id_repartidor IS NULL";

        Map<Integer, Repartidor> asignados = new LinkedHashMap<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement psPend = con.prepareStatement(pendientes);
             PreparedStatement psElegir = con.prepareStatement(elegir);
             PreparedStatement psAsignar = con.prepareStatement(asignar)) {
            psPend.setInt(1, idCliente);
            try (ResultSet pend = psPend.executeQuery()) {
                while (pend.next()) {
                    int idPedido = pend.getInt("id_pedido");
                    psElegir.setInt(1, pend.getInt("id_sucursal"));
                    try (ResultSet r = psElegir.executeQuery()) {
                        if (!r.next()) continue;                // sucursal sin repartidores: se queda NULL
                        Repartidor rep = new Repartidor(r.getInt("id_repartidor"),
                                                        r.getString("nombre"), r.getString("telefono"));
                        psAsignar.setInt(1, rep.id());
                        psAsignar.setInt(2, idPedido);
                        if (psAsignar.executeUpdate() == 1) asignados.put(idPedido, rep);
                    }
                }
            }
            return asignados;
        } catch (SQLException e) {
            throw new DaoException("No se pudieron asignar repartidores a los pedidos del cliente " + idCliente, e);
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
                int idSucursal = sucursalPara(con, idDireccion);
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

    /**
     * Sucursal que atiende la dirección de entrega: la que está en la MISMA alcaldía.
     * Si en esa alcaldía no hubiera sucursal, la de menor id (no debería pasar: hay una por alcaldía).
     * El CASE dentro del ORDER BY pone primero (0) a las de la misma alcaldía y después (1) al resto.
     */
    private int sucursalPara(Connection con, int idDireccion) throws SQLException {
        String sql = "SELECT s.id_sucursal "
                   + "FROM   sucursal s "
                   + "JOIN   direccion ds      ON ds.id_direccion = s.id_direccion "
                   + "JOIN   calle cs          ON cs.id_calle     = ds.id_calle "
                   + "JOIN   colonia cos       ON cos.id_colonia  = cs.id_colonia "
                   + "JOIN   codigo_postal cps ON cps.id_cp       = cos.id_cp "
                   + "ORDER  BY CASE WHEN cps.id_municipio = ("
                   + "              SELECT cp.id_municipio "                       // alcaldía de la entrega
                   + "              FROM   direccion d "
                   + "              JOIN   calle ca         ON ca.id_calle   = d.id_calle "
                   + "              JOIN   colonia co       ON co.id_colonia = ca.id_colonia "
                   + "              JOIN   codigo_postal cp ON cp.id_cp      = co.id_cp "
                   + "              WHERE  d.id_direccion = ?) "
                   + "            THEN 0 ELSE 1 END, "
                   + "          s.id_sucursal "
                   + "FETCH FIRST 1 ROW ONLY";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDireccion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("No hay sucursales registradas");
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