package persistence.oracle;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;

import Restaurante_Completo.LeCartoon_Original.MetodoPago;
import persistence.ConnectionFactory;
import persistence.DaoException;
import persistence.TarjetaDao;

/**
 * TarjetaDao para el esquema de Alan: TARJETA_CLIENTE.
 * El número se recorta DENTRO de Oracle (SUBSTR): el número completo y el CVV nunca viajan a la app.
 */
public class OracleTarjetaDao implements TarjetaDao {

    @Override
    public MetodoPago tarjetaDe(int idCliente) {
        String sql = "SELECT id_tarjeta_cliente, "
                   + "       SUBSTR(num_tarjeta, 1, 1) AS primer_digito, "
                   + "       SUBSTR(num_tarjeta, -4)   AS ultimos4, "
                   + "       expiracion "
                   + "FROM   tarjeta_cliente "
                   + "WHERE  id_cliente = ? "
                   + "ORDER  BY id_tarjeta_cliente DESC "     // la más reciente primero
                   + "FETCH FIRST 1 ROW ONLY";                 // se aplica DESPUÉS del ORDER BY (ROWNUM no)
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;            // el cliente no tiene tarjeta
                return new MetodoPago(
                        rs.getInt("id_tarjeta_cliente"),
                        rs.getString("primer_digito"),
                        rs.getString("ultimos4"),
                        YearMonth.from(rs.getDate("expiracion").toLocalDate()));
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo leer la tarjeta del cliente", e);
        }
    }

    @Override
    public int registrar(int idCliente, String numero, YearMonth vence, String cvv) {
        String sql = "INSERT INTO tarjeta_cliente (id_cliente, num_tarjeta, expiracion, cvv) VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[] { "ID_TARJETA_CLIENTE" })) {
            ps.setInt(1, idCliente);
            ps.setString(2, numero);
            ps.setDate(3, Date.valueOf(vence.atDay(1)));   // EXPIRACION es DATE: se guarda el día 1 del mes
            ps.setString(4, cvv);                          // NOT NULL en el esquema (ver matriz de seguridad)
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
            throw new SQLException("Oracle no regresó el id de tarjeta_cliente");
        } catch (SQLException e) {
            throw new DaoException("No se pudo guardar la tarjeta", e);
        }
    }
}
