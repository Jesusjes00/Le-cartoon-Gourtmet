package Restaurante_Completo.LeCartoon_Original;
import ui.theme.Theme;

public class App {
	public static void main(String[] args) {
	    Theme.install();
	    Login_Window.main(args);
	    
        // Conecta a Couchbase en otro hilo mientras el usuario inicia sesión
        new Thread(() -> {
            try {
                persistence.couchbase.CouchbaseFactory.pedidos();
            } catch (persistence.DaoException e) {
                System.err.println("[Couchbase] " + e.getMessage() + " -> " + e.getCause());
            }
        }, "couchbase-arranque").start();

        // Al cerrar la app (también con System.exit), cierra Couchbase
        Runtime.getRuntime().addShutdownHook(
                new Thread(persistence.couchbase.CouchbaseFactory::cerrar));
	}
	
	
}
