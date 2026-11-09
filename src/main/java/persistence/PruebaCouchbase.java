package persistence;

import java.time.LocalDateTime;

import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;

import persistence.couchbase.CouchbaseFactory;

public class PruebaCouchbase {

    public static void main(String[] args) {
        long t0 = System.currentTimeMillis();
        try {
            Collection col = CouchbaseFactory.pedidos();
            System.out.println("Conectado en " + (System.currentTimeMillis() - t0) + " ms");

            JsonObject doc = JsonObject.create()
                    .put("tipo", "prueba")
                    .put("mensaje", "Hola desde LeCartoon")
                    .put("creado_en", LocalDateTime.now().toString());

            col.upsert("prueba::conexion", doc);                     // guarda (crea o reemplaza)
            System.out.println("Leído: " + col.get("prueba::conexion").contentAsObject());
        } catch (DaoException e) {
            System.out.println("ERROR: " + e.getMessage());
            System.out.println("Causa: " + e.getCause());
        } finally {
            CouchbaseFactory.cerrar();
        }
    }
}