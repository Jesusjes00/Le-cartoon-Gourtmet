package Restaurante_Completo.LeCartoon_Original;

import java.awt.Color;

import ui.theme.Theme;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Sent implements State{
	//Attributos
	private Order order; // Referencia al pedido en cuestión  
	private Color color; // Color normal relacionado con el estado específico
	private Color hover;  // Color del hover dependiendo el estado específico
	private String image; // String de la ruta de la imagen en la carpeta correspondiente y cambia dependiendo del estado
	
	public Sent(Order order) {
		this.order = order; 
		color = Theme.TINTA;   // colores de la paleta (antes rojo/cian/verde puros)
		hover = new Color(0x3B3B3B);
		image = "/Restaurante_Completo/LeCartoon_Original/GIF/sent1.gif";
		
	}

	public void preparing() {
		System.out.println("**El pedido ya ha sido enviado**");
		
	}

	public void sent() {
		System.out.println("**El pedido ya ha sido enviado**");
		
	}

	//Si el pedido ha sido enviado, el siguiente paso es entregarlo al destinatario
	public void delivered() {
		System.out.println("**El pedido ha sido entregado**");		
		
	}

	public String actualState() {
		return "Enviado";
		
	}

	@Override
	public String message() {
		
		return "Tu repartidor está en camino. ¡Prepárate para recibir tu pedido!";
	}
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String getImage() {
		// TODO Auto-generated method stub
		return image;
	}

	@Override
	public void changeState() {
		order.setState(new Delivered(order));
	}

	@Override
	public Color getHover() {
		// TODO Auto-generated method stub
		return hover;
	}
	
	

}
