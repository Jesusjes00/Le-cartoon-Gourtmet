package persistence.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Restaurante_Completo.LeCartoon_Original.Direccion;
import Restaurante_Completo.LeCartoon_Original.ZonaCobertura;
import persistence.ConnectionFactory;
import persistence.DaoException;
import persistence.DireccionDao;

/**
 * DireccionDao para el esquema de Alan. La dirección está normalizada en 8 tablas:
 * pais → estado → ciudad → municipio → codigo_postal → colonia → calle → direccion,
 * y se liga al cliente por PERSONA_DIRECCION (CLIENTE.ID_PERSONA).
 *
 * Zona de cobertura = las filas de CODIGO_POSTAL + COLONIA que cargó el equipo.
 * El cliente solo escribe la calle (se reutiliza o se crea en CALLE) y los números.
 */
public class OracleDireccionDao implements DireccionDao {

    /** Toda la cadena, de la dirección hasta el país. Se usa con "WHERE c.id_cliente = ?". */
    private static final String SELECT_COMPLETA =
            "SELECT d.id_direccion, d.numexterior, d.numinterior, "
          + "       ca.nombre AS calle, co.nombre AS colonia, cp.codigo AS cp, "
          + "       mu.nombre AS municipio, ci.nombre AS ciudad, es.nombre AS estado, pa.nombre AS pais "
          + "FROM   cliente c "
          + "JOIN   persona_direccion pd ON pd.id_persona   = c.id_persona "
          + "JOIN   direccion d          ON d.id_direccion  = pd.id_direccion "
          + "JOIN   calle ca             ON ca.id_calle     = d.id_calle "
          + "JOIN   colonia co           ON co.id_colonia   = ca.id_colonia "
          + "JOIN   codigo_postal cp     ON cp.id_cp        = co.id_cp "
          + "JOIN   municipio mu         ON mu.id_municipio = cp.id_municipio "
          + "JOIN   ciudad ci            ON ci.id_ciudad    = mu.id_ciudad "
          + "JOIN   estado es            ON es.id_estado    = ci.id_estado "
          + "JOIN   pais pa              ON pa.id_pais      = es.id_pais ";

    @Override
    public Direccion direccionDe(int idCliente) {
        String sql = SELECT_COMPLETA
                   + "WHERE c.id_cliente = ? "
                   + "ORDER BY d.id_direccion DESC "      // por si los datos semilla traen más de una
                   + "FETCH FIRST 1 ROW ONLY";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;              // todavía no tiene dirección
                return new Direccion(
                        rs.getInt("id_direccion"),
                        rs.getString("pais"), rs.getString("estado"), rs.getString("ciudad"),
                        rs.getString("municipio"), rs.getString("cp"), rs.getString("colonia"),
                        rs.getString("calle"), rs.getString("numexterior"), rs.getString("numinterior"));
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo leer la dirección del cliente", e);
        }
    }

    @Override
    public ZonaCobertura zonaDe(String cp) {
        // Una fila por colonia; municipio/ciudad/estado/país se repiten y se toman de la primera.
        String sql = "SELECT mu.nombre AS municipio, ci.nombre AS ciudad, es.nombre AS estado, pa.nombre AS pais, "
                   + "       co.id_colonia, co.nombre AS colonia "
                   + "FROM   codigo_postal cp "
                   + "JOIN   colonia co    ON co.id_cp        = cp.id_cp "
                   + "JOIN   municipio mu  ON mu.id_municipio = cp.id_municipio "
                   + "JOIN   ciudad ci     ON ci.id_ciudad    = mu.id_ciudad "
                   + "JOIN   estado es     ON es.id_estado    = ci.id_estado "
                   + "JOIN   pais pa       ON pa.id_pais      = es.id_pais "
                   + "WHERE  cp.codigo = ? "
                   + "ORDER  BY co.nombre";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cp);
            try (ResultSet rs = ps.executeQuery()) {
                List<ZonaCobertura.Colonia> colonias = new ArrayList<>();
                String municipio = null, ciudad = null, estado = null, pais = null;
                while (rs.next()) {
                    if (municipio == null) {
                        municipio = rs.getString("municipio");
                        ciudad    = rs.getString("ciudad");
                        estado    = rs.getString("estado");
                        pais      = rs.getString("pais");
                    }
                    colonias.add(new ZonaCobertura.Colonia(rs.getInt("id_colonia"), rs.getString("colonia")));
                }
                if (colonias.isEmpty()) return null;      // CP fuera de cobertura (o sin colonias)
                return new ZonaCobertura(cp, municipio, ciudad, estado, pais, List.copyOf(colonias));
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo consultar el código postal", e);
        }
    }

    @Override
    public int guardar(int idCliente, int idColonia, String calle, String numExterior, String numInterior) {
        try (Connection con = ConnectionFactory.getConnection()) {
            con.setAutoCommit(false);                     // las 3 escrituras o ninguna
            try {
                int idPersona   = personaDe(con, idCliente);
                int idCalle     = buscarOCrearCalle(con, idColonia, calle);
                int idDireccion = insertarDireccion(con, idCalle, numExterior, numInterior);
                ligar(con, idPersona, idDireccion);
                con.commit();
                return idDireccion;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo guardar la dirección", e);
        }
    }

    private int personaDe(Connection con, int idCliente) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT id_persona FROM cliente WHERE id_cliente = ?")) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("No existe el cliente " + idCliente);
    }

    /**
     * Si en esa colonia ya hay una calle con ese nombre (sin importar mayúsculas ni espacios), se reutiliza;
     * si no, se agrega al catálogo. Así dos vecinos de "Insurgentes Sur" comparten la misma fila de CALLE.
     */
    private int buscarOCrearCalle(Connection con, int idColonia, String calle) throws SQLException {
        String buscar = "SELECT id_calle FROM calle "
                      + "WHERE id_colonia = ? AND UPPER(TRIM(nombre)) = UPPER(TRIM(?)) "
                      + "FETCH FIRST 1 ROW ONLY";
        try (PreparedStatement ps = con.prepareStatement(buscar)) {
            ps.setInt(1, idColonia);
            ps.setString(2, calle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        String insertar = "INSERT INTO calle (id_colonia, nombre) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(insertar, new String[] { "ID_CALLE" })) {
            ps.setInt(1, idColonia);
            ps.setString(2, calle);
            ps.executeUpdate();
            return idGenerado(ps, "calle");
        }
    }

    private int insertarDireccion(Connection con, int idCalle, String numExterior, String numInterior) throws SQLException {
        String sql = "INSERT INTO direccion (id_calle, numexterior, numinterior) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, new String[] { "ID_DIRECCION" })) {
            ps.setInt(1, idCalle);
            ps.setString(2, numExterior);
            ps.setString(3, numInterior);                  // null si no tiene (y '' también es NULL en Oracle)
            ps.executeUpdate();
            return idGenerado(ps, "direccion");
        }
    }

    /**
     * Una sola dirección por cliente: se quita el vínculo anterior (si había) y se pone el nuevo.
     * La DIRECCION vieja se queda en su tabla porque PEDIDO.ID_DIRECCION la sigue usando.
     * El trigger de auditoría deja en PERSONA_DIRECCION_H un 'BORRADO' y un 'INSERT'.
     */
    private void ligar(Connection con, int idPersona, int idDireccion) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM persona_direccion WHERE id_persona = ?")) {
            ps.setInt(1, idPersona);
            ps.executeUpdate();
        }
        try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO persona_direccion (id_persona, id_direccion) VALUES (?, ?)")) {
            ps.setInt(1, idPersona);
            ps.setInt(2, idDireccion);
            ps.executeUpdate();
        }
    }

    private int idGenerado(PreparedStatement ps, String tabla) throws SQLException {
        try (ResultSet keys = ps.getGeneratedKeys()) {
            if (keys.next()) return keys.getInt(1);
        }
        throw new SQLException("Oracle no regresó el id de " + tabla);
    }
}
