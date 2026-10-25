package Restaurante_Completo.LeCartoon_Original;

import java.awt.Color;

import ui.theme.Theme;

import javax.swing.ImageIcon;

public class Delivered implements State{
	//Atribtos
	private Order order; // Referencia al pedido en cuestión  
	private Color color; // Color normal relacionado con el estado específico
	private Color hover;  // Color del hover dependiendo el estado específico
	private String image; // String de la ruta de la imagen en la carpeta correspondiente y cambia dependiendo del estado

	
	public Delivered(Order order) {
		this.order = order;
		color = Theme.SALVIA;   // colores de la paleta (antes rojo/cian/verde puros)
		hover = new Color(0x828F77);
		image = "/Restaurante_Completo/LeCartoon_Original/GIF/delivered1.gif";
	}
	

	//Si el pedido ya se entregó, pues ya concluyó su viaje por el mundo 
	public void preparing() {
		System.out.println("**El pedido ya ha sido entregado**");
		
	}

	public void sent() {
		System.out.println("**El pedido ya ha sido entregado**");
		
	}

	public void delivered() {
		System.out.println("**El pedido ya ha sido entregado**");
		
	}

	public String actualState() {
		return "Entregado";
		
	}


	@Override
	public String message() {		
		return "Tu pedido ha sido entregado. ¡Que lo disfrutes!";
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
	
	public String getImage() {
		// TODO Auto-generated method stub
		return image;
	}

	public void changeState() {
		System.out.println("**Ya se entregó el pedido**");
		
	}
	
	public Color getHover() {
		// TODO Auto-generated method stub
		return hover;
	}

}
