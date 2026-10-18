package Restaurante_Completo.LeCartoon_Original;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import Restaurante_Completo.LeCartoon_Original.Carrito.ItemPedido;


public class Restaurant {
	private static Restaurant rest;
	private String Name;
	private static Data_Base DB;
	private static Costumer costumer; 
	private static ArrayList<Order> orders;
	

	private Restaurant(Costumer costumer) {
		super();
		Name = "Le Cartoon";
		DB = Data_Base.getInstance();
		Restaurant.costumer = costumer; 
		Restaurant.orders = getOrders();
		
	}
	

	
	//Creamos Metodos
	
	public Costumer login(String Email , String password ) {
		Costumer Previus_Costumer=DB.login(Email, password);
		if (Previus_Costumer == null) {
			System.out.println("No fue encontrado el cliente");
			JOptionPane.showMessageDialog(null, "Datos incorrectos, inténtelo de nuevo");
			return null;
		}
		else
		{
			System.out.println("Si se encontro el cliente");
			return Previus_Costumer;
		}

	}
	
	public void Register(String name , String lastname , String phone ,String email,String Password,String Address) {
		DB.register(name, lastname, phone, email, Password, Address);
		System.out.println("Todo salio bien");
		
	}
	
	
	public static int registrarPedido(double price, ArrayList<ItemPedido> items) {
	    return DB.registrarPedido(getCostumer().getId(), price, items);
	}
	
	public void generate_report() {
		
		ArrayList<String> fina = DB.reporte_ventas();
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("texto.txt"));
			
			for(String pp:fina) {
				writer.write(pp);
			}
			
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Data_Base getDB() {
		return DB;
	}

	public void setDB(Data_Base dB) {
		DB = dB;
	}

	public static Costumer getCostumer() {
		return costumer;
	}	
		
	
	public static void setCostumer(Costumer costumer) {
		Restaurant.costumer = costumer;
	}

	public static void changeCostumer(Costumer costumer) {		
		getInstance(costumer);
		Restaurant.setCostumer(costumer);
		Restaurant.orders = getOrders();
	}

	public static Restaurant getInstance(Costumer costumer) {
		if(rest == null) {
			rest = new Restaurant(costumer);
		}
		return rest;
	}
	
	public static ArrayList<Order> getOrders() {
		return DB.getOrders(costumer.getId());
	}



	public static void setOrders(ArrayList<Order> orders) {
		Restaurant.orders = orders;
	}
	
	

}