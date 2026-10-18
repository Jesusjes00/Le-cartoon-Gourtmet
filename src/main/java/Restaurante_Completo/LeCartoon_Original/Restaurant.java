package Restaurante_Completo.LeCartoon_Original;

import java.util.ArrayList;
import java.util.List;

import Restaurante_Completo.LeCartoon_Original.Carrito.ItemPedido;
import persistence.CustomerDao;
import persistence.DaoFactory;
import persistence.OrderDao;


public final class Restaurant {

    private static final Restaurant INSTANCIA = new Restaurant();

    private final CustomerDao clientes = DaoFactory.clientes();
    private final OrderDao pedidos = DaoFactory.pedidos();

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
        return pedidos.registrar(cliente.getId(), total, items);
    }

    private void exigirSesion() {
        if (cliente == null) {
            throw new IllegalStateException("No hay un cliente con sesión iniciada");
        }
    }


}