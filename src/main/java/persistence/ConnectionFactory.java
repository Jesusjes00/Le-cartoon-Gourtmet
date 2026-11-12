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
    /**
     * Dueño de las tablas (CIB500_05, el esquema de Alan). La app entra como CLIENTE (CIB500_14):
     * con "ALTER SESSION SET CURRENT_SCHEMA" los nombres sin prefijo (pedido, cliente…) se buscan en
     * el esquema del dueño, pero los PERMISOS se siguen revisando con el usuario que inició sesión.
     * Vacío = no se cambia (la app entra con el dueño, como en la rama data).
     */
    private static final String SCHEMA;


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
        SCHEMA   = leer(props, "db.schema").toUpperCase();
        // Va pegado dentro de un ALTER SESSION: solo se acepta un nombre de esquema válido (sin inyección)
        if (!SCHEMA.isEmpty() && !SCHEMA.matches("[A-Z][A-Z0-9_$#]{0,127}")) {
            throw new IllegalStateException("db.schema no es un nombre de esquema válido: \"" + SCHEMA + "\"");
        }
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
        Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
        if (!SCHEMA.isEmpty() && !SCHEMA.equalsIgnoreCase(USER)) {
            try (Statement st = con.createStatement()) {
                st.execute("ALTER SESSION SET CURRENT_SCHEMA = " + SCHEMA);
            } catch (SQLException e) {
                con.close();                 // no dejar abierta una conexión que apunta al esquema equivocado
                throw e;
            }
        }
        return con;
    }

    /** Usuario con el que entra la app (para PruebaConexion). */
    public static String usuario() {
        return USER;
    }

    /** Esquema donde se buscan las tablas (vacío = el del propio usuario). */
    public static String esquema() {
        return SCHEMA;
    }

 
    public static String describir() {
        return URL;
    }
}