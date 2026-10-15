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

    public static ArrayList<ItemPedido> getPedidos() {
        return listaPedidos;
    }


    
    
}