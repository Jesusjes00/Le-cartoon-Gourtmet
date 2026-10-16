package persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public final class ConnectionFactory {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;


    static {
        Properties props = new Properties();
        try (InputStream in = ConnectionFactory.class.getResourceAsStream("/db.properties")) {
            if (in == null) {
                throw new IllegalStateException("No se encontró db.properties en src/main/resources");
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer db.properties", e);
        }
        URL      = leer(props, "db.url");
        USER     = leer(props, "db.user");
        PASSWORD = leer(props, "db.password");
        if (URL.isEmpty()) {
            throw new IllegalStateException("db.url está vacío en db.properties");
        }
    }

    private ConnectionFactory() { }  

    // Un -Ddb.password=... al arrancar gana sobre el archivo
    private static String leer(Properties props, String clave) {
        return System.getProperty(clave, props.getProperty(clave, "")).trim();
    }


    public static Connection getConnection() throws SQLException {
        Connection c = USER.isEmpty()
                ? DriverManager.getConnection(URL)
                : DriverManager.getConnection(URL, USER, PASSWORD);

        if (URL.startsWith("jdbc:sqlite:")) {          // solo SQLite lo necesita
            try (Statement s = c.createStatement()) {
                s.execute("PRAGMA foreign_keys = ON");
            }
        }
        return c;
    }

 
    public static String describir() {
        return URL;
    }
}