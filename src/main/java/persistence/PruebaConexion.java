package persistence;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** Prueba de humo: ¿la app llega a la base de datos configurada? Se corre sola (tiene main). */
public final class PruebaConexion {

    public static void main(String[] args) {
        DriverManager.setLoginTimeout(10);    // si no hay red, falla en 10 s en vez de quedarse colgado
        System.out.println("Conectando a " + ConnectionFactory.describir() + " ...");
        long inicio = System.currentTimeMillis();

        try (Connection con = ConnectionFactory.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT USER, TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') FROM DUAL")) {

            rs.next();
            System.out.println("OK en " + (System.currentTimeMillis() - inicio) + " ms");
            System.out.println("Usuario: " + rs.getString(1) + " · Hora del servidor: " + rs.getString(2));

            DatabaseMetaData meta = con.getMetaData();
            System.out.println("Servidor: " + meta.getDatabaseProductVersion().split("\n")[0]);
            System.out.println("Driver:   " + meta.getDriverName() + " " + meta.getDriverVersion());

        } catch (SQLException e) {
            System.err.println("FALLÓ: " + e.getMessage());
            System.err.println("Código: " + e.getErrorCode() + " · SQLState: " + e.getSQLState());
        }
    }
}