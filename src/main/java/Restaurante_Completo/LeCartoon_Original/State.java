package Restaurante_Completo.LeCartoon_Original;

import java.awt.Color;

import javax.swing.ImageIcon;

public interface State {
	//Estado que tendrán los pedidos, el cuál es dinámico, 
	//Cambian los colores, los gifs y los mensajes dependiendo del estado
	//Los estados son: Preparando, Enviado, Entregado 
	
	public void preparing(); // Estado de preparando pedido
	public void sent(); // Estado de pedido enviado 
	public void delivered(); // Estado de que el pedido ya fue entregado al cliente
	public String actualState(); // Retorna el String del estado del pedido (Preparando, Entregado, etc)
	public String message(); // Mensaje que sale en pantalle en función del estado
	public Color getColor(); // Método para obtener El color dependiendo el estado 
	public Color getHover(); // Método para obtener el color del Hover dependiedo el Estado
	public String getImage(); // Método para obtener el gif asignado al estado correspondiente
	public void changeState(); // Método cambia el estado del pedido al siguiente
	
}
