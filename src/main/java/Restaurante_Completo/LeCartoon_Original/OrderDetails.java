package Restaurante_Completo.LeCartoon_Original;

public class OrderDetails { 
	//La clase de detalles de pedido sólo sirve para conocer los detalles de un pedido ya existente 
	private int idOrder;
	private String dish;
	private int quantity;
	
	
	public OrderDetails(int idOrder, String dish, int quantity) {
		super();
		this.idOrder = idOrder;
		this.dish = dish;
		this.quantity = quantity;
	}
	
	public int getIdOrder() {
		return idOrder;
	}
	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}
	public String getDish() {
		return dish;
	}
	public void setDish(String dish) {
		this.dish = dish;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "OrderDetails [idOrder=" + idOrder + ", dish=" + dish + ", quantity=" + quantity + "]";
	}
	
	
	
	
}
