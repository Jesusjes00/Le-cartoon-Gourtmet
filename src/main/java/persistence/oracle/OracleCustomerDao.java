package persistence.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Restaurante_Completo.LeCartoon_Original.Costumer;
import persistence.ConnectionFactory;
import persistence.CustomerDao;
import persistence.DaoException;

/** CustomerDao para el esquema de Alan: CLIENTE + PERSONA + TELEFONO. */
public class OracleCustomerDao implements CustomerDao {

    // "García López" -> ["García", "López"];  "García" -> ["García", null]
    private static String[] separarApellidos(String apellidos) {
        String limpio = apellidos == null ? "" : apellidos.trim();
        int espacio = limpio.indexOf(' ');
        if (espacio < 0) return new String[] { limpio, null };
        return new String[] { limpio.substring(0, espacio), limpio.substring(espacio + 1).trim() };
    }

    private static String juntarApellidos(String paterno, String materno) {
        return materno == null ? paterno : paterno + " " + materno;
    }

    private static boolean vacio(String s) {
        return s == null || s.isBlank();
    }
    
    @Override
    public Costumer login(String email, String password) {
        String sql = "SELECT c.id_cliente, p.nombre, p.apellidopaterno, p.apellidomaterno, c.email, "
                   + "       (SELECT MIN(t.telefono) FROM telefono t WHERE t.id_persona = p.id_persona) AS telefono "
                   + "FROM cliente c JOIN persona p ON p.id_persona = c.id_persona "
                   + "WHERE c.email = ? AND c.contraseña = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                String tel = rs.getString("telefono");
                return new Costumer(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        juntarApellidos(rs.getString("apellidopaterno"), rs.getString("apellidomaterno")),
                        tel == null ? "" : tel,
                        rs.getString("email"),
                        password,
                        " ");                       // dirección: llega con Direccion/Addresses
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo iniciar sesión", e);
        }
    }
    
    
    @Override
    public int registrar(Costumer nuevo) {
        try (Connection con = ConnectionFactory.getConnection()) {
            con.setAutoCommit(false);
            try {
                int idPersona = insertarPersona(con, nuevo);
                int idCliente = insertarCliente(con, idPersona, nuevo);
                if (!vacio(nuevo.getPhone())) insertarTelefono(con, idPersona, nuevo.getPhone());
                con.commit();
                return idCliente;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {            // ORA-00001: viola UNIQUE (cliente__un = email)
                throw new DaoException("Ese correo ya está registrado", e);
            }
            throw new DaoException("No se pudo registrar al cliente", e);
        }
    }

    private int insertarPersona(Connection con, Costumer c) throws SQLException {
        String[] ap = separarApellidos(c.getLastName());
        String sql = "INSERT INTO persona (nombre, apellidopaterno, apellidomaterno) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, new String[] { "ID_PERSONA" })) {
            ps.setString(1, c.getName());
            ps.setString(2, ap[0]);
            ps.setString(3, ap[1]);                 // null si no hay segundo apellido
            ps.executeUpdate();
            return idGenerado(ps, "persona");
        }
    }

    private int insertarCliente(Connection con, int idPersona, Costumer c) throws SQLException {
        String sql = "INSERT INTO cliente (id_persona, email, contraseña) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, new String[] { "ID_CLIENTE" })) {
            ps.setInt(1, idPersona);
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPassword());
            ps.executeUpdate();
            return idGenerado(ps, "cliente");
        }
    }

    private void insertarTelefono(Connection con, int idPersona, String telefono) throws SQLException {
        String sql = "INSERT INTO telefono (id_persona, telefono) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPersona);
            ps.setString(2, telefono.trim());
            ps.executeUpdate();
        }
    }

    private int idGenerado(PreparedStatement ps, String tabla) throws SQLException {
        try (ResultSet keys = ps.getGeneratedKeys()) {
            if (keys.next()) return keys.getInt(1);
        }
        throw new SQLException("Oracle no regresó el id de " + tabla);
    }
    
    
    @Override
    public void actualizarPerfil(Costumer c) {
        String deCliente = "(SELECT id_persona FROM cliente WHERE id_cliente = ?)";
        String[] ap = separarApellidos(c.getLastName());
        try (Connection con = ConnectionFactory.getConnection()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE persona SET nombre = ?, apellidopaterno = ?, apellidomaterno = ? "
                      + "WHERE id_persona = " + deCliente)) {
                    ps.setString(1, c.getName());
                    ps.setString(2, ap[0]);
                    ps.setString(3, ap[1]);
                    ps.setInt(4, c.getId());
                    if (ps.executeUpdate() == 0) throw new SQLException("No existe el cliente " + c.getId());
                }
                if (!vacio(c.getPhone())) {
                    int filas;
                    try (PreparedStatement ps = con.prepareStatement(
                            "UPDATE telefono SET telefono = ? WHERE id_persona = " + deCliente)) {
                        ps.setString(1, c.getPhone().trim());
                        ps.setInt(2, c.getId());
                        filas = ps.executeUpdate();
                    }
                    if (filas == 0) {                   // no tenía teléfono: se le crea
                        try (PreparedStatement ps = con.prepareStatement(
                                "INSERT INTO telefono (id_persona, telefono) "
                              + "SELECT id_persona, ? FROM cliente WHERE id_cliente = ?")) {
                            ps.setString(1, c.getPhone().trim());
                            ps.setInt(2, c.getId());
                            ps.executeUpdate();
                        }
                    }
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo actualizar el perfil", e);
        }
    }
}
    
    