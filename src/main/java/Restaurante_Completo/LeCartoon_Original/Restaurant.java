package Restaurante_Completo.LeCartoon_Original;

import java.util.ArrayList;
import java.util.List;

import Restaurante_Completo.LeCartoon_Original.Carrito.ItemPedido;
import persistence.CustomerDao;
import persistence.DaoException;
import persistence.DaoFactory;
import persistence.OrderDao;
import persistence.PedidoDocDao;


public final class Restaurant {

    private static final Restaurant INSTANCIA = new Restaurant();

    private final CustomerDao clientes = DaoFactory.clientes();
    private final OrderDao pedidos = DaoFactory.pedidos();
    private final PedidoDocDao documentos = DaoFactory.documentos();

    private Costumer cliente;          // sesión actual (null = nadie ha entrado)

    private Restaurant() { }

    public static Restaurant getInstance() {
        return INSTANCIA;
    }

    // ---------- Sesión ----------

    /** Regresa el cliente y lo deja como sesión; null si email/contraseña no coinciden. */
    public Costumer login(String email, String password) {
        Costumer c = clientes.login(email, password);
        if (c != null) cliente = c;
        return c;
    }

    /** Registra al cliente, le pone el id que dio la BD y lo deja como sesión. */
    public Costumer registrar(Costumer nuevo) {
        int id = clientes.registrar(nuevo);
        nuevo.setId(id);
        cliente = nuevo;
        return nuevo;
    }

    public void cerrarSesion() {
        cliente = null;
    }

    public Costumer getCliente() {
        return cliente;
    }

    public void actualizarPerfil(Costumer c) {
        clientes.actualizarPerfil(c);
    }

    // ---------- Pedidos ----------

    public List<Order> misPedidos() {
        exigirSesion();
        return pedidos.pedidosDe(cliente.getId());
    }

    public List<OrderDetails> detallesDe(int idPedido) {
        return pedidos.detallesDe(idPedido);
    }

    public void cambiarEstado(Order pedido) {
        pedidos.cambiarEstado(pedido.getId(), pedido.getId_state());
    }

    public int registrarPedido(double total, List<ItemPedido> items) {
        exigirSesion();
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío");
        }
        int id = pedidos.registrar(cliente.getId(), total, items);      // 1) Oracle: fuente de verdad
        EstadosVistos.marcar(cliente.getId(), id, 1);                   // el cliente lo vio nacer en "Preparando"
        try {
            documentos.crear(id, cliente, pedidos.sucursalDePedido(id), items);  // 2) copia en Couchbase
        } catch (DaoException e) {
            System.err.println("[Couchbase] El pedido " + id + " quedó solo en Oracle: "
                               + e.getMessage() + " -> " + e.getCause());
        }
        return id;
    }

    // ---------- Reseñas ----------

    /** Reseña guardada del pedido, o null si todavía no lo califican. */
    public Resena resenaDe(int idPedido) {
        return pedidos.resenaDe(idPedido);
    }

    public void calificar(int idPedido, Resena resena) {
        exigirSesion();
        pedidos.calificar(idPedido, resena);                             // 1) Oracle
        try {
            documentos.calificar(idPedido, resena);                      // 2) Couchbase
        } catch (DaoException e) {
            System.err.println("[Couchbase] La reseña del pedido " + idPedido + " quedó solo en Oracle: "
                               + e.getMessage() + " -> " + e.getCause());
        }
    }

    // ---------- Seguimiento: qué estado ya vio el cliente ----------

    /**
     * Pedidos del cliente que avanzaron desde la última vez que los vio en esta computadora
     * (los mueve el vendedor). Lo usa el vigilante de Main_Frame, desde otro hilo.
     */
    public List<Order> pedidosConNovedades() {
        Costumer c = cliente;                           // copia local: otro hilo puede cerrar la sesión
        if (c == null) throw new IllegalStateException("No hay un cliente con sesión iniciada");
        List<Order> cambiados = new ArrayList<>();
        for (Order o : pedidos.pedidosDe(c.getId())) {
            if (o.getId_state() > EstadosVistos.visto(c.getId(), o.getId(), o.getId_state())) {
                cambiados.add(o);
            }
        }
        return cambiados;
    }

    /** Último estado que el cliente vio del pedido (si nunca lo vio aquí, el actual). */
    public int estadoVisto(Order pedido) {
        exigirSesion();
        return EstadosVistos.visto(cliente.getId(), pedido.getId(), pedido.getId_state());
    }

    /** Anota que el cliente ya vio el pedido en ese estado. */
    public void marcarVisto(int idPedido, int estado) {
        exigirSesion();
        EstadosVistos.marcar(cliente.getId(), idPedido, estado);
    }

    private void exigirSesion() {
        if (cliente == null) {
            throw new IllegalStateException("No hay un cliente con sesión iniciada");
        }
    }


}