package Restaurante_Completo.LeCartoon_Original;

import java.awt.Color;

import ui.theme.Theme;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Preparing implements State{
	//Atributos 
	private Order order; // Referencia al pedido en cuestión  
	private Color color; // Color normal relacionado con el estado específico
	private Color hover;  // Color del hover dependiendo el estado específico
	private String image; // String de la ruta de la imagen en la carpeta correspondiente y cambia dependiendo del estado 
	
	//Se crea el estado de preparando y se guarda la referencia del pedido
	public Preparing(Order order) {
		this.order = order; 
		color = Theme.TERRACOTA;   // colores de la paleta (antes rojo/cian/verde puros)
		hover = new Color(0xA84C30);
		image = "/Restaurante_Completo/LeCartoon_Original/GIF/preparing1.gif";
	}
	
	public void preparing() {
		System.out.println("**El pedido ya se haya en preparación**");
		
	}

	//Si el pedido se haya en preparación, el próximo paso es enviarlo al destinatario
	public void sent() {
		System.out.println("**El pedido está listo y ha sido enviado**");
		order.setState(new Sent(this.order));
		
	}

	public void delivered() {
		System.out.println("**El pedido sigue preparándose**");
		
	}

	public String actualState() {
		return "Preparando";
		
	}

	@Override
	public String message() {
		
		return "Tu pedido se encuentra en preparación. ¡Agradecemos tu paciencia!";
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

	@Override
	public void changeState() {
		order.setState(new Sent(this.order));
		
	}

	@Override
	public Color getHover() {
		// TODO Auto-generated method stub
		return hover;
	}

}
