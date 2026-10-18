package Restaurante_Completo.LeCartoon_Original;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import Restaurante_Completo.LeCartoon_Original.Carrito.ItemPedido;
import persistence.ConnectionFactory;
import persistence.CustomerDao;
import persistence.DaoException;
import persistence.OrderDao;
import persistence.sqlite.SqliteCustomerDao;
import persistence.sqlite.SqliteOrderDao;
public class Data_Base {
	private  static Data_Base db;
	private  Connection connect = null;
	private final CustomerDao clientes = new SqliteCustomerDao();
	private final OrderDao pedidos = new SqliteOrderDao();
	
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
	    try {
	        return new ArrayList<>(pedidos.pedidosDe(idCostumer));
	    } catch (DaoException e) {
	        System.err.println("[DAO] " + e.getMessage() + " -> " + e.getCause());
	        JOptionPane.showMessageDialog(null, "Hubo un error al leer tus pedidos");
	        return new ArrayList<>();
	    }
	}
	
	//Método para obtener los detalles de pedido en un arraylist
	public ArrayList<OrderDetails> getOrderDetails(int idOrder) {
	    try {
	        List<OrderDetails> detalles = pedidos.detallesDe(idOrder);
	        if (detalles.isEmpty()) {
	            JOptionPane.showMessageDialog(null, "No se encontraron detalles de ese pedido");
	            return null;               // OrderPanel revisa null (se limpia en t11)
	        }
	        return new ArrayList<>(detalles);
	    } catch (DaoException e) {
	        System.err.println("[DAO] " + e.getMessage() + " -> " + e.getCause());
	        JOptionPane.showMessageDialog(null, "No se pudieron leer los detalles del pedido");
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
	

	
	public void updateOrder(Order order) {
	    try {
	        pedidos.cambiarEstado(order.getId(), order.getId_state());
	    } catch (DaoException e) {
	        System.err.println("[DAO] " + e.getMessage() + " -> " + e.getCause());
	        JOptionPane.showMessageDialog(null, "No se pudo guardar el nuevo estado del pedido");
	    }
	}
	
	public int registrarPedido(int customerId, double price, ArrayList<ItemPedido> items) {
	    return pedidos.registrar(customerId, price, items);
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