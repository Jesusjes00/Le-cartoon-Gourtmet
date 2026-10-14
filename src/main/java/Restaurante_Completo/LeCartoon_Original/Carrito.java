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
        listaPedidos.add(new ItemPedido(platillo, cantidad));
        System.out.println("Guardado en carrito: " + cantidad + "x " + platillo.getName());
    }

    public static ArrayList<ItemPedido> getPedidos() {
        return listaPedidos;
    }

	public static ArrayList<ItemPedido> getListaPedidos() {
		return listaPedidos;
	}

	public static void setListaPedidos() {
		Carrito.listaPedidos = null;
		Carrito.listaPedidos = new ArrayList<>();
	}
    
    
}