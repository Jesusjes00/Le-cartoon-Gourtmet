package persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
        if (!URL.startsWith("jdbc:oracle:")) {
            throw new IllegalStateException("LeCartoon solo trabaja con Oracle: db.url debe empezar con jdbc:oracle: (llegó \"" + URL + "\")");
        }
    }

    private ConnectionFactory() { }  

 // Orden: -D  >  variable de entorno LECARTOON_*  >  db.properties
    private static String leer(Properties props, String clave) {
    	String sistema = System.getProperty(clave);
    	if(sistema != null)return sistema.trim();
    	
    	String env = System.getenv("LECARTOON_" + clave.replace('.', '_').toUpperCase());
    	if (env != null) return env.trim();
    	return props.getProperty(clave,"").trim();
        
    }


    public static Connection getConnection() throws SQLException {
        if (USER.isEmpty()) {
            throw new SQLException("Faltan las credenciales de Oracle: define LECARTOON_DB_USER y LECARTOON_DB_PASSWORD");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

 
    public static String describir() {
        return URL;
    }
}