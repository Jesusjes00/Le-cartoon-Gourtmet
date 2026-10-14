package Restaurante_Completo.LeCartoon_Original;

import java.awt.Color;

public class Order {
	//Atributos
	private int id; // id de pedido (No importa a la hora de crear un pedido, porque la base de datos le asigna el id)
	private int id_costumer; // id de cliente
	private double totalPrice; // precio total 
	private State state; // estado del pedido (PATRÓN DE DISEÑO STATE)
	private int id_state; // id que me permite conocer el estado de un pedido (1 = preparando, 2= enviado, 3 = entregado)  
	//(Al crear un nuevo pedido, por defecto el pedido tendrá id_state = 1)
	
	//Constructores
	//Sin id de estado, lo que implica que el pedido es nuevo 
	public Order(int id_costumer, double totalPrice) {
		this.id = -1; 
		this.id_costumer = id_costumer;
		this.totalPrice = totalPrice; 
		this.id_state = 1;
		
		this.state = new Preparing(this);
	}
	
	//Con id de estado, normalmente se usará tras extraer el pedido de la base de datos 
	public Order(int id, int id_costumer, double totalPrice, int id_state) {  
		this(id_costumer, totalPrice);
		this.id = id; 
		this.id_state = id_state;
		
		if(id_state == 1) {
			//Se está preparando el pedido 
			this.state = new Preparing(this);
			
		}else if(id_state == 2) {
			//Se ha enviado el pedido
			this.state = new Sent(this);
			
		}else {
			//Se entregó el pedido
			this.state = new Delivered(this);
		}
	}

	//Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_costumer() {
		return id_costumer;
	}

	public void setId_costumer(int id_costumer) {
		this.id_costumer = id_costumer;
	}
	
	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public int getId_state() {
		return id_state;
	}

	public void setId_state(int id_state) {
		this.id_state = id_state;
	}


	//Métodos relacionados con el patrón de diseño State
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void preparing() {
		state.preparing();
	}
	
	public void sent() {
		state.sent();			
	}
	
	public void delivered() {
		state.delivered();
	}
	
	public String actualState() {
		return state.actualState();
	}
	
	public String message() {
		return state.message();
	}
	
	public Color getColor() {
		return state.getColor();
	}
	
	public String getImage() {
		return state.getImage();
	}
	
	public void changeState() {
		state.changeState();
	}
	
	public Color getHover() {
		return state.getHover();
	}

	public String toString() {
		//Si el estado se encuentra en la fase de preparación
		if(this.state instanceof Preparing) {
			return "Pedido [id = " + id + ", Cliente = " + id_costumer + ", Precio total: $" + totalPrice + ", Estado: Preparando...] " + id_state;
		}else if(this.state instanceof Sent) {
		//Si el estado se encuentra en la fase de enviado
			return "Pedido [id = " + id + ", Cliente = " + id_costumer + ", Precio total: $" + totalPrice + ", Estado: Enviado] " + id_state;
		}else {
		//Si el estado se encuentra en la fase de entregado 
			return "Pedido [id = " + id + ", Cliente = " + id_costumer + ", Precio total: $" + totalPrice + ", Estado: Entregado] " + id_state;
		}
		
	}
	
	
}
