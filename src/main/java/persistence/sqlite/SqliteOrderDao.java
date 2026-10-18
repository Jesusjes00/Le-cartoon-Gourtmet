package persistence.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Restaurante_Completo.LeCartoon_Original.Order;
import Restaurante_Completo.LeCartoon_Original.OrderDetails;
import Restaurante_Completo.LeCartoon_Original.Carrito.ItemPedido;
import persistence.ConnectionFactory;
import persistence.DaoException;
import persistence.OrderDao;

/** OrderDao para el esquema SQLite actual (ORDERS, ORDERSDETAILS, DISHES). */
public class SqliteOrderDao implements OrderDao {

    @Override
    public List<Order> pedidosDe(int idCliente) {
        String sql = "SELECT ID, COSTUMER, PRICE, STATE FROM ORDERS "
                   + "WHERE COSTUMER = ? ORDER BY ID DESC";
        List<Order> pedidos = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(new Order(rs.getInt("ID"), rs.getInt("COSTUMER"),
                                          rs.getDouble("PRICE"), rs.getInt("STATE")));
                }
            }
            return pedidos;
        } catch (SQLException e) {
            throw new DaoException("No se pudieron leer los pedidos del cliente " + idCliente, e);
        }
    }

    @Override
    public List<OrderDetails> detallesDe(int idPedido) {
        String sql = "SELECT d.IDORDER, p.NAME, d.QUANTITY "
                   + "FROM ORDERSDETAILS d JOIN DISHES p ON p.ID = d.DISH "
                   + "WHERE d.IDORDER = ?";
        List<OrderDetails> detalles = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    detalles.add(new OrderDetails(rs.getInt("IDORDER"),
                                                  rs.getString("NAME"),
                                                  rs.getInt("QUANTITY")));
                }
            }
            return detalles;
        } catch (SQLException e) {
            throw new DaoException("No se pudieron leer los detalles del pedido " + idPedido, e);
        }
    }

    @Override
    public void cambiarEstado(int idPedido, int estado) {
        String sql = "UPDATE ORDERS SET STATE = ? WHERE ID = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estado);
            ps.setInt(2, idPedido);
            if (ps.executeUpdate() == 0) {
                throw new DaoException("No existe el pedido " + idPedido, null);
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo cambiar el estado del pedido " + idPedido, e);
        }
    }

    @Override
    public int registrar(int idCliente, double total, List<ItemPedido> items) {
        try (Connection con = ConnectionFactory.getConnection()) {
            con.setAutoCommit(false);                 // inicia la transacción
            try {
                int idPedido = insertarPedido(con, idCliente, total);
                insertarDetalles(con, idPedido, items);
                con.commit();                         // todo o nada: se confirma
                return idPedido;
            } catch (SQLException e) {
                con.rollback();                       // algo falló: se deshace todo
                throw e;
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo registrar el pedido", e);
        }
    }

    private int insertarPedido(Connection con, int idCliente, double total) throws SQLException {
        String sql = "INSERT INTO ORDERS (COSTUMER, PRICE, STATE) VALUES (?, ?, 1)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, idCliente);
            ps.setDouble(2, total);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new SQLException("La BD no regresó el id del pedido");
    }

    private void insertarDetalles(Connection con, int idPedido, List<ItemPedido> items) throws SQLException {
        String sql = "INSERT INTO ORDERSDETAILS (IDORDER, DISH, QUANTITY) VALUES (?, ?, ?)";
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