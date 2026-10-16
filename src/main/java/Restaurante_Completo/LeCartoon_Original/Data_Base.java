package Restaurante_Completo.LeCartoon_Original;
import java.sql.Connection;
import persistence.ConnectionFactory;
import persistence.CustomerDao;
import persistence.DaoException;
import persistence.sqlite.SqliteCustomerDao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Statement;

import javax.swing.JOptionPane;

import Restaurante_Completo.LeCartoon_Original.Carrito.ItemPedido;
public class Data_Base {
	private  static Data_Base db;
	private  Connection connect = null;
	private final CustomerDao clientes = new SqliteCustomerDao();
	
	private Data_Base() {
	    try {
	        connect = ConnectionFactory.getConnection();
	        System.out.println("Conectado a " + ConnectionFactory.describir());
	    } catch (SQLException e) {
	        System.out.println("Error de conexión: " + e.getMessage());
	    }
	}
	
	public Costumer login(String email, String password) {
	    try {
	        return clientes.login(email, password);
	    } catch (DaoException e) {
	        System.err.println("[DAO] " + e.getMessage() + " -> " + e.getCause());
	        return null;
	    }
	}
	public boolean register(String name, String lastname, String phone,
					String email, String password, String address) {
					try {
							Costumer nuevo = new Costumer(name, lastname, phone, email, password, address);
							int id = clientes.registrar(nuevo);
							System.out.println("Cliente registrado con id " + id);
							return true;
						} catch (DaoException e) {
							System.err.println("[DAO] " + e.getMessage() + " -> " + e.getCause());
							return false;
						}
	}
	
	public void Favorites(int ID , ArrayList<Integer> favorites) {
		try {
			
			String sql  = "INSERT INTO FAVORITES (COSTUMER,FAV1,FAV2,FAV3) VALUES (?,?,?,?)";
			PreparedStatement insert = connect.prepareStatement(sql);
			insert.setInt(1,ID);
			for(int i = 0 ; i< favorites.size();i++) {
				insert.setInt(i+2,favorites.get(i));
			}
			insert.executeUpdate();
			insert.close();
			System.out.println("Se inserto con exito los favoritos del cliente");
			
		}catch(SQLException e) {
			System.out.println("Algo no se inserto bien");
			e.printStackTrace();
		}

	}
	
	//Método para obtener los pedidos del cliente 
	public ArrayList<Order> getOrders(int idCostumer) {
		ArrayList<Order> orders = new ArrayList<>();
		String sql = "Select * from ORDERS where COSTUMER = ?";
		try {
			PreparedStatement query = connect.prepareStatement(sql);
			query.setInt(1, idCostumer);
			ResultSet result = query.executeQuery();
			
			while(result.next()) {
				int id; 
				int idcostumer;
				double totalPrice; 				
				int idState;
				
				id = result.getInt("ID");
				idcostumer = result.getInt("COSTUMER");
				totalPrice = result.getDouble("PRICE");
				idState = result.getInt("STATE");
				
				Order aux = new Order(id, idcostumer, totalPrice, idState);
				orders.add(aux);
				
				
			}
			
			result.close();
			query.close();
			if(orders.size() >=1) {
				return orders; //Se regresa la lista de pedidos asociada al cliente
			}
			
		} catch (SQLException e) {
			System.out.println("*Hubo un error al ingresar a la base de datos*");
			JOptionPane.showMessageDialog(null, "Hubo un error al ingresar a la base de datos");			
			
		}
		return null; 
		
		
	}
	
	//Método para obtener los detalles de pedido en un arraylist
	public ArrayList<OrderDetails> getOrderDetails(int idOrder){
		ArrayList<OrderDetails> details = new ArrayList<>();
		String sql = "Select * from ORDERSDETAILS Where IDORDER = ?";
		boolean found = false;
		try {
			PreparedStatement query = connect.prepareStatement(sql);
			query.setInt(1, idOrder);
			ResultSet result = query.executeQuery();			
			
			while(result.next()) {	
				found = true;
				int id;
				int dish;
				int quantity;				
				id = result.getInt("IDORDER");
				dish = result.getInt("DISH");
				quantity = result.getInt("QUANTITY");
				String plate = dishById(dish);				
				
				OrderDetails aux = new OrderDetails(id, plate, quantity);
				details.add(aux);
			}
			result.close();
			query.close();
			if(found) {
				return details;
			}else {
				JOptionPane.showMessageDialog(null, "No se encontraron detalles de ese pedido");
				return null;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("*No se encontraron detalles de ese pedido*");
			JOptionPane.showMessageDialog(null, "No se encontraron detalles de ese pedido");
			return null;
		}
		
	}
	
	//Método para editar el perfil del usuario en la base de datos
	public void editProfile(Costumer costumer) {
	    try {
	        clientes.actualizarPerfil(costumer);
	        JOptionPane.showMessageDialog(null, "Se actualizó correctamente su perfil");
	    } catch (DaoException e) {
	        System.err.println("[DAO] " + e.getMessage() + " -> " + e.getCause());
	        JOptionPane.showMessageDialog(null, "Hubo un error de actualización");
	    }
	}
	
	//Método para conocer una comida a partir de un id
	public String dishById(int id) { // recibe el id
		String sql = "Select * from DISHES where ID = ?";
		try {
			PreparedStatement query = connect.prepareStatement(sql);
			query.setInt(1, id);
			ResultSet result = query.executeQuery();
			if(result.next()) {
				return result.getString("NAME"); // Regresa el nombre de la comida a partir de un id de platillo
			}			
			result.close();
			query.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("*Hubo un problema*");			
		}
		return null;
		
	}
	
	public void updateOrder(Order order) {
		String sql = "UPDATE ORDERS SET STATE = ? WHERE ID = ?";			
		try {
			PreparedStatement update= connect.prepareStatement(sql);			
			update.setInt(1, order.getId_state());
			update.setInt(2, order.getId());			
			update.executeUpdate();
			System.out.println("**Proceso exitoso");
			update.close();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("*Error*");
		}		
		
	}
	
	public int registrarPedido(int customerId,double price,
								ArrayList<ItemPedido>items)throws SQLException{
		boolean autocommitPrevio = connect.getAutoCommit();
		connect.setAutoCommit(false); //inicia la transaccion
		try {
			int orderId = insertOrder(customerId,price);
			insertOrderDetails(orderId,items);
			connect.commit();  //genera un commit
			return orderId;
		}catch (SQLException e) {
			connect.rollback();   //en caso de error genera un rollback
			throw e;
		}finally {
			connect.setAutoCommit(autocommitPrevio); // restablece la conexión
		}
	}
	
	
	
	private int insertOrder(int customerId,double price)throws SQLException {
		String sql = "INSERT INTO ORDERS (COSTUMER, PRICE , STATE) VALUES (?,?,?)";
		try (PreparedStatement insert =
				connect.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
			insert.setInt(1, customerId);
			insert.setDouble(2, price);
			insert.setInt(3,1);
			insert.executeUpdate();
			try(ResultSet keys = insert.getGeneratedKeys()){
				if (keys.next()) return keys.getInt(1);
			}
		
		}
		throw new SQLException("No se obtuvo el id del pedido");

	}
	
	
	private void insertOrderDetails(int oderId,ArrayList<ItemPedido> items)throws SQLException {
		String sql = "INSERT INTO ORDERSDETAILS (IDORDER,DISH,QUANTITY) VALUES(?,?,?)";
		try(PreparedStatement insert = connect.prepareStatement(sql)){
			for(ItemPedido item:items) {
				insert.setInt(1, oderId);
				insert.setInt(2,item.getPlatillo().getId());
				insert.setInt(3,item.getCantidad());
				insert.addBatch();
			}
			insert.executeBatch();
		}
		
				
	}
	
	
	//Reporte----------------------------------------------------------->
	public ArrayList<String> reporte_ventas() {
		ArrayList<String> fina = new ArrayList<>();
		String sql = "SELECT * FROM ORDERS";
		try {
			PreparedStatement get= connect.prepareStatement(sql);	
			ResultSet result = get.executeQuery();
			while(result.next()) {
				String data = "ID:" + result.getInt("ID") +" "+ "Costumer:" + result.getInt("COSTUMER") +" "+"Price:"+ result.getDouble("PRICE") +
						" " + "STATE:" + result.getInt("STATE") + "\n";
				fina.add(data);
			
			}
			result.close();
			get.close();
			return fina;
		}catch(SQLException e) {
			System.out.println("*Error*");
			return null;
		}
	}
	
	//SIngleton
	public static Data_Base getInstance() {
        if (db == null) {
            db = new Data_Base();
        }
        return db;
    }

}