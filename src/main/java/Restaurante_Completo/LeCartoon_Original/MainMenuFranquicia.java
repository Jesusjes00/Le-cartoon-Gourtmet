package Restaurante_Completo.LeCartoon_Original;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import Dishes.*;
import Franchises.*;

public class MainMenuFranquicia {

	public static void main(String[] args) {
		int op = 5;
		
		SwingUtilities.invokeLater(() -> {

			JFrame frame = new JFrame("Prueba Menú Franquicia - 1080p Display");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(1920, 1000);
			frame.setMinimumSize(new Dimension(1280, 720));
			frame.setLocationRelativeTo(null);
			Franchises franquicia = null;
			ArrayList<Dishes> listaPlatillos = new ArrayList<>();
			switch(op) {
			case 1:
				franquicia = new Regular();
				listaPlatillos.add(new Magnificarne());
				listaPlatillos.add(new Sandwich_M());
				listaPlatillos.add(new Burrito());
				listaPlatillos.add(new Extrahuevordinario());
				listaPlatillos.add(new Radicola());
				break;
			case 2:
				franquicia = new Adventure();
				listaPlatillos.add(new Sandwich_Perfecto());
				listaPlatillos.add(new Hotcakes());
				listaPlatillos.add(new Pastel_Manzana());
				listaPlatillos.add(new Super_Porp());
				break;
			case 3:
				franquicia = new Simpson();
				listaPlatillos.add(new Costiburguer());
				listaPlatillos.add(new Tapa_Arterias());
				listaPlatillos.add(new Rosquilla_Rosa());
				listaPlatillos.add(new Cerveza_Duff());
				listaPlatillos.add(new Squishee());
				break;
			case 4:
				franquicia = new Disney();
				listaPlatillos.add(new Ratatouille());
				listaPlatillos.add(new Empanadas());
				listaPlatillos.add(new Buñuelos());
				listaPlatillos.add(new Jugo_Mabel());
				break;
			case 5:
				franquicia = new Sponge();
				listaPlatillos.add(new Cangreburguer());
				listaPlatillos.add(new Colorburguer());
				listaPlatillos.add(new Pizza_Don_Cangrejo());
				listaPlatillos.add(new Limonada());
				listaPlatillos.add(new Refresco_Algas());
				break;
			}

			FranchiseDetails panelMenu = new FranchiseDetails(franquicia, listaPlatillos, null);

			frame.add(panelMenu);
			frame.setVisible(true);
			ArrayList<Carrito.ItemPedido> pedidos = new ArrayList<>();
			System.out.println(pedidos);
			
			
		});
}}
