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
                     "SELECT USER, SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA'), "
                   + "       TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') FROM DUAL")) {

            rs.next();
            System.out.println("OK en " + (System.currentTimeMillis() - inicio) + " ms");
            System.out.println("Usuario: " + rs.getString(1) + " · Esquema de las tablas: " + rs.getString(2)
                               + " · Hora del servidor: " + rs.getString(3));
            permisos(con);

            DatabaseMetaData meta = con.getMetaData();
            System.out.println("Servidor: " + meta.getDatabaseProductVersion().split("\n")[0]);
            System.out.println("Driver:   " + meta.getDriverName() + " " + meta.getDriverVersion());

        } catch (SQLException e) {
            System.err.println("FALLÓ: " + e.getMessage());
            System.err.println("Código: " + e.getErrorCode() + " · SQLState: " + e.getSQLState());
        }
    }

    /**
     * Toca cada tabla que usa la app con un SELECT que no regresa filas (WHERE 1 = 0):
     * si el usuario no tiene permiso, Oracle responde ORA-00942 o ORA-01031 en esa tabla.
     */
    private static void permisos(Connection con) {
        String[] tablas = { "pais", "estado", "ciudad", "municipio", "codigo_postal", "colonia", "calle",
                "direccion", "persona", "telefono", "cliente", "persona_direccion", "tarjeta_cliente",
                "sucursal", "repartidor", "platillo", "pedido", "detalles_pedido" };
        System.out.println("Permisos de lectura de " + ConnectionFactory.usuario() + ":");
        for (String t : tablas) {
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT 1 FROM " + t + " WHERE 1 = 0")) {
                System.out.println("  OK     " + t);
            } catch (SQLException e) {
                System.out.println("  FALLA  " + t + " -> ORA-" + String.format("%05d", e.getErrorCode()));
            }
        }
    }
}