package persistence.couchbase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.couchbase.client.core.error.CouchbaseException;
import com.couchbase.client.core.error.DocumentExistsException;
import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.json.JsonArray;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.MutateInSpec;

import Dishes.Dishes;
import Restaurante_Completo.LeCartoon_Original.Costumer;
import Restaurante_Completo.LeCartoon_Original.Direccion;
import Restaurante_Completo.LeCartoon_Original.Repartidor;
import Restaurante_Completo.LeCartoon_Original.Resena;
import Restaurante_Completo.LeCartoon_Original.Carrito.ItemPedido;
import persistence.DaoException;
import persistence.PedidoDocDao;

/** Documento "pedido::<id>" con el formato de Alan (version 1). */
public class CouchbasePedidoDocDao implements PedidoDocDao {

    // mismo formato que usa la página de Alan: "16-10-2026 14:15:00"
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private static String llave(int idPedido) {
        return "pedido::" + idPedido;
    }

    private static String ahora() {
        return LocalDateTime.now().format(FECHA);
    }

    @Override
    public void crear(int idPedido, Costumer c, int idSucursal, Direccion entrega, List<ItemPedido> items) {
        JsonArray detalles = JsonArray.create();
        double subtotal = 0;
        for (ItemPedido item : items) {
            Dishes p = item.getPlatillo();
            detalles.add(JsonObject.create()
                    .put("id_platillo", p.getId())
                    .put("nombre", p.getName())
                    .put("cantidad", item.getCantidad())
                    .put("precio", p.getPrice())          // precio al momento de comprar
                    .putNull("observaciones"));
            subtotal += p.getPrice() * item.getCantidad();
        }

        JsonObject cliente = JsonObject.create()
                .put("id_cliente", c.getId())
                .put("nombre", (c.getName() + " " + c.getLastName()).trim());
        if (c.getPhone() == null || c.getPhone().isBlank()) cliente.putNull("telefono");
        else cliente.put("telefono", c.getPhone().trim());

        JsonObject doc = JsonObject.create()
                .put("id_pedido", idPedido)
                .put("cliente", cliente)                  // Extended Reference: copia del cliente
                .putNull("repartidor")                    // lo asigna el vendedor al pasar a Enviado
                .put("id_sucursal", idSucursal)
                .put("id_estado", 1)                      // Preparando
                .put("direccion", direccion(entrega))     // Extended Reference: copia de la dirección de entrega
                .put("detalles", detalles)                // Embedding: los renglones van adentro
                .put("subtotal", Math.round(subtotal * 100) / 100.0)
                .put("fechaCreacion", ahora())
                .putNull("fechaActualizacion")
                .put("resena", JsonObject.create().putNull("calificacion").putNull("comentarios"))
                .put("version", 1);                       // Schema Versioning

        try {
            CouchbaseFactory.pedidos().insert(llave(idPedido), doc);
        } catch (DocumentExistsException e) {
            throw new DaoException("Ya existe el documento " + llave(idPedido) + " en Couchbase", e);
        } catch (CouchbaseException e) {
            throw new DaoException("No se pudo crear el documento del pedido " + idPedido, e);
        }
    }

    /** La dirección como la espera Alan: pais, estado, ..., numExterior, numInterior (null si no tiene). */
    private static JsonObject direccion(Direccion d) {
        if (d == null) return null;
        JsonObject o = JsonObject.create()
                .put("pais", d.getPais())
                .put("estado", d.getEstado())
                .put("ciudad", d.getCiudad())
                .put("municipio", d.getMunicipio())
                .put("cp", d.getCp())
                .put("colonia", d.getColonia())
                .put("calle", d.getCalle())
                .put("numExterior", d.getNumexterior());
        if (d.getNuminterior() == null || d.getNuminterior().isBlank()) o.putNull("numInterior");
        else o.put("numInterior", d.getNuminterior());
        return o;
    }

    /** Sub-documento: solo cambia "repartidor" y "fechaActualizacion"; el estado lo pone la página de Alan. */
    @Override
    public void asignarRepartidor(int idPedido, Repartidor r) {
        JsonObject repartidor = JsonObject.create()
                .put("id_repartidor", r.id())
                .put("nombre", r.nombre());
        if (r.telefono() == null) repartidor.putNull("telefono");
        else repartidor.put("telefono", r.telefono());

        try {
            CouchbaseFactory.pedidos().mutateIn(llave(idPedido), List.of(
                    MutateInSpec.upsert("repartidor", repartidor),
                    MutateInSpec.upsert("fechaActualizacion", ahora())));
        } catch (DocumentNotFoundException e) {
            throw new DaoException("No existe el documento " + llave(idPedido) + " en Couchbase", e);
        } catch (CouchbaseException e) {
            throw new DaoException("No se pudo guardar el repartidor en Couchbase del pedido " + idPedido, e);
        }
    }

    @Override
    public void calificar(int idPedido, Resena r) {
        JsonObject resena = JsonObject.create().put("calificacion", r.getEstrellas());
        if (r.getComentario().isEmpty()) resena.putNull("comentarios");
        else resena.put("comentarios", r.getComentario());

        try {
            CouchbaseFactory.pedidos().mutateIn(llave(idPedido), List.of(
                    MutateInSpec.upsert("resena", resena),
                    MutateInSpec.upsert("fechaActualizacion", ahora())));
        } catch (DocumentNotFoundException e) {
            throw new DaoException("No existe el documento " + llave(idPedido) + " en Couchbase", e);
        } catch (CouchbaseException e) {
            throw new DaoException("No se pudo guardar la reseña en Couchbase del pedido " + idPedido, e);
        }
    }
}