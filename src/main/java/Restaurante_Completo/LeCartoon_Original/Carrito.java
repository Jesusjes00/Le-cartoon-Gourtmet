package Restaurante_Completo.LeCartoon_Original;
import java.util.ArrayList;
import Dishes.*;

public class Carrito {
    
    public static class ItemPedido {
        private Dishes platillo;
        private int cantidad;

        public ItemPedido(Dishes platillo, int cantidad) {
            this.platillo = platillo;
            this.cantidad = cantidad;
        }

		public Dishes getPlatillo() {
			return platillo;
		}

		public void setPlatillo(Dishes platillo) {
			this.platillo = platillo;
		}

		public int getCantidad() {
			return cantidad;
		}

		public void setCantidad(int cantidad) {
			this.cantidad = cantidad;
		}
        
        
        
    }

    private static ArrayList<ItemPedido> listaPedidos = new ArrayList<>();

    public static void agregarAlCarrito(Dishes platillo, int cantidad) {
        if (cantidad <= 0) return;
        ItemPedido existente = buscar(platillo.getId());
        if (existente != null) {
            existente.setCantidad(existente.getCantidad() + cantidad);   // fusiona
        } else {
            listaPedidos.add(new ItemPedido(platillo, cantidad));
        }
    }

    private static ItemPedido buscar(int dishId) {
        for (ItemPedido item : listaPedidos) {
            if (item.getPlatillo().getId() == dishId) return item;
        }
        return null;
    }

    public static void quitar(int dishId) {
        listaPedidos.removeIf(item -> item.getPlatillo().getId() == dishId);
    }

    public static void cambiarCantidad(int dishId, int nuevaCantidad) {
        if (nuevaCantidad <= 0) { quitar(dishId); return; }   // llegar a 0 = quitar
        ItemPedido item = buscar(dishId);
        if (item != null) item.setCantidad(nuevaCantidad);
    }

    public static void vaciar() {
        listaPedidos.clear();
    }

    public static boolean estaVacio() {
        return listaPedidos.isEmpty();
    }

    public static int totalUnidades() {               // para el "Carrito (n)" del header
        int unidades = 0;
        for (ItemPedido item : listaPedidos) unidades += item.getCantidad();
        return unidades;
    }

    public static double total() {                    // subtotal, sin envío
        double total = 0;
        for (ItemPedido item : listaPedidos) {
            total += item.getPlatillo().getPrice() * item.getCantidad();
        }
        return total;
    }

    // ── Cobro: reglas de negocio del carrito ───────────────────────────
    public static final double TASA_IVA = 0.16;    // IVA en México
    public static final double ENVIO    = 35.00;   // costo fijo de envío (MXN)

    /** Suma de los platillos tal como se muestran (sus precios YA incluyen IVA). */
    public static double importePlatillos() {
        return redondear(total());
    }

    /** Importe de los platillos sin IVA: precio con IVA / 1.16. */
    public static double subtotal() {
        return redondear(total() / (1 + TASA_IVA));
    }

    /** Parte del importe que es IVA. Se calcula por diferencia para que subtotal + iva cuadre al centavo. */
    public static double iva() {
        return redondear(importePlatillos() - subtotal());
    }

    /** Envío: solo se cobra si hay algo en el carrito. */
    public static double envio() {
        return estaVacio() ? 0 : ENVIO;
    }

    /** Lo que paga el cliente y lo que se guarda como precio del pedido en la BD. */
    public static double totalAPagar() {
        return redondear(importePlatillos() + envio());
    }

    /** Redondea a centavos (2 decimales). */
    private static double redondear(double x) {
        return Math.round(x * 100) / 100.0;
    }

    public static ArrayList<ItemPedido> getPedidos() {
        return listaPedidos;
    }


    
    
}