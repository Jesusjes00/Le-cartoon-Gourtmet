package ProfileScreens;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.EventQueue;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import Restaurante_Completo.LeCartoon_Original.Buttons;
import Restaurante_Completo.LeCartoon_Original.Costumer;
import Restaurante_Completo.LeCartoon_Original.Login_Window;
import Restaurante_Completo.LeCartoon_Original.Main_Frame;
import Restaurante_Completo.LeCartoon_Original.Order;
import Restaurante_Completo.LeCartoon_Original.OrderDetails;
import Restaurante_Completo.LeCartoon_Original.Restaurant;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.SpringLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import OrderDetails.Details;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import javax.swing.JOptionPane;
import persistence.DaoException;

public class ProfileScreen extends JPanel {

	private static final long serialVersionUID = 1L;
	private Buttons infoBtn;
	private Buttons payBtn;
	private Buttons addressBtn;
	private Buttons ordersBtn;
	private static JPanel dynamicPanel;
	private static JLabel name;
	private static Costumer costumer;
	private static ArrayList<Order> orders = new ArrayList<>();
	private static JPanel mainPanel;
	
	public ProfileScreen() {
		costumer = Restaurant.getInstance().getCliente();
		try {
		    orders = new ArrayList<>(Restaurant.getInstance().misPedidos());
		} catch (DaoException ex) {
		    System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
		    JOptionPane.showMessageDialog(null, "No se pudieron cargar tus pedidos");
		    orders = new ArrayList<>();
		}
		
		
		setSize(1920, 1000);
		setBackground(new Color(255, 255, 255));
		setLayout(new BorderLayout(0, 0));
		
		mainPanel = new JPanel();
		mainPanel.setBackground(new Color(255, 255, 255));
		add(mainPanel, BorderLayout.CENTER);
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[]{30, 498, 0, 30, 0};
		gbl_mainPanel.rowHeights = new int[]{30, 200, 63, 0, 30, 0};
		gbl_mainPanel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_mainPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		mainPanel.setLayout(gbl_mainPanel);
		
		JPanel profilePanel = new JPanel();
		profilePanel.setBackground(new Color(255, 255, 255));
		profilePanel.setLayout(null);
		GridBagConstraints gbc_profilePanel = new GridBagConstraints();
		gbc_profilePanel.gridwidth = 2;
		gbc_profilePanel.insets = new Insets(0, 0, 5, 5);
		gbc_profilePanel.fill = GridBagConstraints.BOTH;
		gbc_profilePanel.gridx = 1;
		gbc_profilePanel.gridy = 1;
		mainPanel.add(profilePanel, gbc_profilePanel);
		
		Buttons editProfile = Buttons.secundario("Editar perfil");
		editProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {								
				changePanel(new EditProfile(costumer));
				resetButtons();
								
			}
		});
		editProfile.setBounds(1612, 72, 187, 47);
		profilePanel.add(editProfile);
		
		Buttons loginbutton_1 = Buttons.chip(orders.size() + " PEDIDOS");
		loginbutton_1.setBounds(222, 135, 226, 32);
		profilePanel.add(loginbutton_1);
		
		JLabel lblNewLabel_1 = new JLabel("Cliente");
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("SansSerif", Font.PLAIN, 23));
		lblNewLabel_1.setBounds(222, 27, 135, 24);
		profilePanel.add(lblNewLabel_1);
		
		name = new JLabel(costumer.getName() + " " + costumer.getLastName());
		name.setForeground(Color.WHITE);
		name.setFont(new Font("SansSerif", Font.BOLD, 30));
		name.setBounds(222, 74, 344, 33);
		profilePanel.add(name);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(ProfileScreen.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/pfp.png")));
		lblNewLabel.setBounds(47, 11, 144, 173);
		profilePanel.add(lblNewLabel);
		
		Buttons loginbutton = new Buttons("Crear Cuenta", 70, 70, Color.BLACK, Color.BLACK);
		loginbutton.setBackground(new Color(255, 255, 255));
		loginbutton.setText("");
		loginbutton.setForeground(Color.WHITE);
		loginbutton.setBounds(0, 0, 1855, 195);
		profilePanel.add(loginbutton);
		
		JPanel menu = new JPanel();
		menu.setBackground(new Color(255, 255, 255));
		menu.setLayout(null);
		GridBagConstraints gbc_menu = new GridBagConstraints();
		gbc_menu.insets = new Insets(0, 0, 5, 5);
		gbc_menu.fill = GridBagConstraints.BOTH;
		gbc_menu.gridx = 1;
		gbc_menu.gridy = 3;
		mainPanel.add(menu, gbc_menu);
		
		JPanel menuPanel = new JPanel();
		menuPanel.setBounds(0, 0, 493, 672);
		menuPanel.setBackground(Color.WHITE);
		menu.add(menuPanel);
		menuPanel.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("Cuenta");
		lblNewLabel_2.setBounds(0, -12, 439, 42);
		lblNewLabel_2.setForeground(Color.LIGHT_GRAY);
		lblNewLabel_2.setFont(new Font("SansSerif", Font.BOLD, 25));
		menuPanel.add(lblNewLabel_2);
		
		JPanel panelinfo = new JPanel();
		panelinfo.setBounds(0, 35, 439, 47);
		panelinfo.setBackground(Color.WHITE);
		menuPanel.add(panelinfo);
		panelinfo.setLayout(null);
		
		infoBtn = new Buttons("Crear Cuenta", 70, 70, Color.BLACK, Color.LIGHT_GRAY);
		infoBtn.setBounds(0, 0, 439, 47);
		infoBtn.setText("  Información Personal");
		infoBtn.setIcon(new ImageIcon(ProfileScreen.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/user.PNG")));
		infoBtn.setHorizontalAlignment(SwingConstants.LEFT);
		infoBtn.setForeground(Color.WHITE);
		infoBtn.setFont(new Font("SansSerif", Font.PLAIN, 25));
		infoBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		infoBtn.setColorFondo(Color.BLACK);
		panelinfo.add(infoBtn);
		infoBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				resetButtons();
				infoBtn.setColorFondo(Color.black);
				infoBtn.setForeground(Color.white);
				infoBtn.setIcon(new ImageIcon(ProfileScreen.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/user.PNG")));
								
				changePanel(new Personalinfo());	
				Personalinfo.costumerChanged(costumer);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				infoBtn.setForeground(Color.black);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				infoBtn.setForeground(Color.lightGray);
			}
		});
		
		JPanel panelPay = new JPanel();
		panelPay.setBounds(0, 87, 439, 47);
		panelPay.setBackground(Color.WHITE);
		menuPanel.add(panelPay);
		panelPay.setLayout(null);
		
		payBtn = new Buttons("Crear Cuenta", 70, 70, Color.WHITE, Color.LIGHT_GRAY);
		payBtn.setBounds(0, 0, 439, 47);
		payBtn.setText("  Métodos de pago");
		payBtn.setIcon(new ImageIcon(ProfileScreen.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/billete-de-banco.png")));
		payBtn.setHorizontalAlignment(SwingConstants.LEFT);
		payBtn.setForeground(Color.LIGHT_GRAY);
		payBtn.setFont(new Font("SansSerif", Font.PLAIN, 25));
		payBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		payBtn.setColorFondo(Color.WHITE);
		panelPay.add(payBtn);
		payBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				resetButtons();
				payBtn.setColorFondo(Color.black);
				payBtn.setForeground(Color.white);
				payBtn.setIcon(new ImageIcon(ProfileScreen.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/money.png")));
								
				changePanel(new Payment());
				Payment.changedCostumer(costumer);
			}
			
			public void mouseEntered(MouseEvent e) {
				payBtn.setForeground(Color.black);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				payBtn.setForeground(Color.lightGray);
			}
		});
		
		JPanel panelAddress = new JPanel();
		panelAddress.setBounds(0, 139, 439, 50);
		panelAddress.setBackground(Color.WHITE);
		menuPanel.add(panelAddress);
		panelAddress.setLayout(null);
		
		addressBtn = new Buttons("Crear Cuenta", 70, 70, Color.WHITE, Color.LIGHT_GRAY);
		addressBtn.setText("  Mis Direcciones");
		addressBtn.setIcon(new ImageIcon(ProfileScreen.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/localizacion.png")));
		addressBtn.setHorizontalAlignment(SwingConstants.LEFT);
		addressBtn.setForeground(Color.LIGHT_GRAY);
		addressBtn.setFont(new Font("SansSerif", Font.PLAIN, 25));
		addressBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addressBtn.setColorFondo(Color.WHITE);
		addressBtn.setBounds(0, 0, 439, 47);
		panelAddress.add(addressBtn);
		addressBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				resetButtons();
				addressBtn.setColorFondo(Color.black);
				addressBtn.setForeground(Color.white);
				addressBtn.setIcon(new ImageIcon(ProfileScreen.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/location.png")));
								
				changePanel(new Addresses(costumer.getAddress()));
			}
			
			public void mouseEntered(MouseEvent e) {
				addressBtn.setForeground(Color.black);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				addressBtn.setForeground(Color.lightGray);
			}
			
		});
		
		JLabel logOutLabel = new JLabel("Cerrar Sesión");
		logOutLabel.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				resetButtons();
	
				Restaurant.getInstance().cerrarSesion();
				Login_Window log_window = new Login_Window();
				log_window.showloginframe(true);
				// Ocultamos la ventana de login actual y liberamos recursos
				Window ventana = SwingUtilities.getWindowAncestor(ProfileScreen.this);
				if(ventana != null) {
					ventana.dispose();
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				logOutLabel.setIcon(new ImageIcon(ProfileScreen.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/cerrar-sesion.png")));
				logOutLabel.setForeground(new Color(237, 28, 36));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				logOutLabel.setIcon(new ImageIcon(ProfileScreen.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/logOut.PNG")));
				logOutLabel.setForeground(new Color(128, 0, 0));
			}
		});
		logOutLabel.setBounds(0, 640, 439, 32);
		logOutLabel.setIcon(new ImageIcon(ProfileScreen.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/logOut.PNG")));
		logOutLabel.setHorizontalAlignment(SwingConstants.LEFT);
		logOutLabel.setForeground(new Color(128, 0, 0));
		logOutLabel.setFont(new Font("SansSerif", Font.PLAIN, 25));
		logOutLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menuPanel.add(logOutLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 28, 415, 2);
		menuPanel.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 254, 415, 2);
		menuPanel.add(separator_1);
		
		JPanel panelAddress_1 = new JPanel();
		panelAddress_1.setLayout(null);
		panelAddress_1.setBackground(Color.WHITE);
		panelAddress_1.setBounds(0, 193, 439, 50);
		menuPanel.add(panelAddress_1);
		
		ordersBtn = new Buttons("Crear Cuenta", 70, 70, Color.WHITE, Color.LIGHT_GRAY);
		ordersBtn.setText("  Historial de Pedidos");
		ordersBtn.setIcon(new ImageIcon(ProfileScreen.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/pedido.png")));
		ordersBtn.setHorizontalAlignment(SwingConstants.LEFT);
		ordersBtn.setForeground(Color.LIGHT_GRAY);
		ordersBtn.setFont(new Font("SansSerif", Font.PLAIN, 25));
		ordersBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		ordersBtn.setColorFondo(Color.WHITE);
		ordersBtn.setBounds(0, 0, 439, 47);
		
		ordersBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				resetButtons();
				ordersBtn.setColorFondo(Color.black);
				ordersBtn.setForeground(Color.white);
				ordersBtn.setIcon(new ImageIcon(ProfileScreen.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/order.png")));
								
				changePanel(new OrderHistory(orders));
			}
			
			public void mouseEntered(MouseEvent e) {
				ordersBtn.setForeground(Color.black);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				ordersBtn.setForeground(Color.lightGray);
			}
			
		});
		panelAddress_1.add(ordersBtn);
		
		dynamicPanel = new JPanel();
		dynamicPanel.setBackground(new Color(255, 255, 255));
		dynamicPanel.setLayout(null);
		GridBagConstraints gbc_dynamicPanel = new GridBagConstraints();
		gbc_dynamicPanel.insets = new Insets(0, 0, 5, 5);
		gbc_dynamicPanel.fill = GridBagConstraints.BOTH;
		gbc_dynamicPanel.gridx = 2;
		gbc_dynamicPanel.gridy = 3;
		mainPanel.add(dynamicPanel, gbc_dynamicPanel);
		
		changePanel(new Personalinfo());
		Personalinfo.costumerChanged(costumer);
						
		
	}	
	
	public void resetButtons() {
		infoBtn.setColorFondo(Color.white);
		infoBtn.setForeground(Color.LIGHT_GRAY);
		infoBtn.setIcon(new ImageIcon(ProfileScreen.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/usuario.png")));
		payBtn.setColorFondo(Color.white);
		payBtn.setForeground(Color.LIGHT_GRAY);
		payBtn.setIcon(new ImageIcon(ProfileScreen.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/billete-de-banco.png")));
		addressBtn.setColorFondo(Color.white);
		addressBtn.setForeground(Color.LIGHT_GRAY);
		addressBtn.setIcon(new ImageIcon(ProfileScreen.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/localizacion.png")));
		ordersBtn.setColorFondo(Color.white);
		ordersBtn.setForeground(Color.LIGHT_GRAY);
		ordersBtn.setIcon(new ImageIcon(ProfileScreen.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/pedido.png")));
	}
		
	
	public static void costumerChanged() {
		name.setText(costumer.getName() + " " + costumer.getLastName()); 
				
	}
	
	public static void changePanel(JPanel panel) {	
		panel.setBounds(0, 0, 1357, 672);
		dynamicPanel.removeAll();
		dynamicPanel.add(panel, BorderLayout.CENTER);
		dynamicPanel.revalidate();
		dynamicPanel.repaint();
	}
	
	public static void orderHistory() {
		JPanel panel = new OrderHistory(orders);
		panel.setBounds(0, 0, 1357, 672);
		dynamicPanel.removeAll();
		dynamicPanel.add(panel, BorderLayout.CENTER);
		dynamicPanel.revalidate();
		dynamicPanel.repaint();
		
	}

	public static JPanel getDynamicPanel() {
		return dynamicPanel;
	}

	public static void setDynamicPanel(JPanel dynamicPanel) {
		ProfileScreen.dynamicPanel = dynamicPanel;
	}


	public static Costumer getCostumer() {
		return costumer;
	}

	public static void setCostumer(Costumer costumer) {
		ProfileScreen.costumer = costumer;
	}

	public static ArrayList<Order> getOrders() {
		return orders;
	}

	public static void setOrders(ArrayList<Order> orders) {
		ProfileScreen.orders = orders;
	}

	public static JPanel getMainPanel() {
		return mainPanel;
	}

	public static void setMainPanel(JPanel mainPanel) {
		ProfileScreen.mainPanel = mainPanel;
	}	
	
	public void initOrdersPanel() {
		ordersBtn.setColorFondo(Color.black);
		ordersBtn.setForeground(Color.white);
		ordersBtn.setIcon(new ImageIcon(ProfileScreen.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/order.png")));
	}
	
	
		
}