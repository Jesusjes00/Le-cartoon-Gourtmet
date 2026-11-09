package persistence.couchbase;

import java.time.Duration;

import com.couchbase.client.core.error.CouchbaseException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.Collection;

import persistence.DaoException;

/**
 * Conexión única a Couchbase Capella. Se abre la primera vez que se pide (perezosa)
 * y se reutiliza: conectar a Capella tarda segundos y Cluster es thread-safe.
 */
public final class CouchbaseFactory {

    private static Cluster cluster;
    private static Collection pedidos;

    private CouchbaseFactory() { }

    /** Collection donde viven los documentos del pedido. */
    public static synchronized Collection pedidos() {
        if (pedidos == null) {
            String url      = obligatorio("LECARTOON_CB_URL");
            String usuario  = obligatorio("LECARTOON_CB_USER");
            String password = obligatorio("LECARTOON_CB_PASSWORD");
            String bucket   = obligatorio("LECARTOON_CB_BUCKET");
            String scope      = opcional("LECARTOON_CB_SCOPE", "_default");
            String collection = opcional("LECARTOON_CB_COLLECTION", "_default");
            try {
                Cluster c = Cluster.connect(url, ClusterOptions.clusterOptions(usuario, password)
                        .environment(env -> env.applyProfile("wan-development")));
                Bucket b = c.bucket(bucket);
                b.waitUntilReady(Duration.ofSeconds(20));   // si algo está mal, falla AQUÍ y no después
                cluster = c;
                pedidos = b.scope(scope).collection(collection);
            } catch (CouchbaseException e) {
                throw new DaoException("No se pudo conectar a Couchbase (" + url + ")", e);
            }
        }
        return pedidos;
    }

    /** Cierra la conexión; se llama al salir de la app. */
    public static synchronized void cerrar() {
        if (cluster != null) {
            cluster.disconnect();
            cluster = null;
            pedidos = null;
        }
    }

    // Orden: -D  >  variable de entorno (igual que ConnectionFactory)
    private static String opcional(String clave, String porDefecto) {
        String v = System.getProperty(clave);
        if (v == null) v = System.getenv(clave);
        return (v == null || v.isBlank()) ? porDefecto : v.trim();
    }

    private static String obligatorio(String clave) {
        String v = opcional(clave, null);
        if (v == null) throw new DaoException("Falta definir " + clave, null);
        return v;
    }
}