package OrderDetails;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;

import Restaurante_Completo.LeCartoon_Original.Buttons;
import Restaurante_Completo.LeCartoon_Original.Order;
import Restaurante_Completo.LeCartoon_Original.Preparing;
import Restaurante_Completo.LeCartoon_Original.Sent;
import Restaurante_Completo.LeCartoon_Original.Delivered;
import Restaurante_Completo.LeCartoon_Original.Main_Frame;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import Restaurante_Completo.LeCartoon_Original.Restaurant;
import persistence.DaoException;

import java.awt.Font;
import javax.swing.SwingConstants;

import ProfileScreens.OrderHistory;

import ProfileScreens.ProfileScreen;
import Restaurante_Completo.LeCartoon_Original.OrderDetails;
import Restaurante_Completo.LeCartoon_Original.PromoCard;
import Restaurante_Completo.LeCartoon_Original.PromoCard_GIF;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.awt.Cursor;



public class Details extends JPanel {
	private ArrayList<OrderDetails> orderlist;
	private Order order;

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JPanel secMainPanel;
	private Buttons barra;
	private Buttons rightBtn;
	private Buttons returnBtn;
	private Buttons simState;
	private Buttons widget;
	private PromoCard_GIF card;
	private JPanel panelBarra;
	private JTextArea textState;
	private ArrayList<Buttons> btns;
	private JLabel estimacion;
	

	public Details(ArrayList<OrderDetails> orderlist, Order order) {			
		this.orderlist = orderlist;
		this.order = order;
		btns = new ArrayList<Buttons>();
		initialize();
		
		
	}
	
	public void initialize() {		
		setBackground(new Color(255, 255, 255));	
		setSize(1920, 1000);
		setLayout(null);		
		
		mainPanel = new JPanel();
		mainPanel.setBackground(new Color(255, 255, 255));
		mainPanel.setBounds(0, 0, 1259, 1000);
		add(mainPanel);
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[]{30, 30, 30, 1130, 30, 0};
		gbl_mainPanel.rowHeights = new int[]{30, 30, 30, 202, 30, 30, 30, 460, 30, 30, 0, 30, 0};
		gbl_mainPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_mainPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		mainPanel.setLayout(gbl_mainPanel);
		
		panelBarra = new JPanel();
		panelBarra.setBackground(new Color(255, 255, 255));
		panelBarra.setLayout(null);
		GridBagConstraints gbc_panelBarra = new GridBagConstraints();
		gbc_panelBarra.insets = new Insets(0, 0, 5, 5);
		gbc_panelBarra.fill = GridBagConstraints.BOTH;
		gbc_panelBarra.gridx = 3;
		gbc_panelBarra.gridy = 3;
		mainPanel.add(panelBarra, gbc_panelBarra);
		
		LocalTime ahora = LocalTime.now().plusMinutes(30);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		String horaFormateada = ahora.format(formatter);
		
		JLabel reloj = new JLabel("");
		reloj.setBounds(104, 22, 128, 128);
		reloj.setIcon(new ImageIcon(Details.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/reloj.png")));
		panelBarra.add(reloj);
		
		estimacion = new JLabel();		
		estimacion.setForeground(new Color(255, 255, 255));
		estimacion.setHorizontalAlignment(SwingConstants.CENTER);
		estimacion.setFont(new Font("SansSerif", Font.BOLD, 30));
		estimacion.setBounds(373, 114, 367, 39);
		panelBarra.add(estimacion);
		
		JLabel lblNewLabel = new JLabel(horaFormateada);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 60));
		lblNewLabel.setBounds(282, 11, 526, 92);
		panelBarra.add(lblNewLabel);
		
		barra = new Buttons("Crear Cuenta", 70, 70, order.getColor(), order.getColor());
		barra.setText("");
		barra.setForeground(Color.WHITE);
		barra.setBackground(order.getColor());
		barra.setBounds(0, 0, 1134, 197);
		panelBarra.add(barra);
		btns.add(barra);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(255, 255, 255));
		panel_3.setLayout(null);
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 5);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 3;
		gbc_panel_3.gridy = 7;		
				
		mainPanel.add(panel_3, gbc_panel_3);
		
		textState = new JTextArea();
		textState.setBounds(585, 189, 500, 179);
		textState.setWrapStyleWord(true);
		textState.setText(order.getState().message());
		textState.setOpaque(false);
		textState.setLineWrap(true);
		textState.setForeground(Color.WHITE);
		textState.setFont(new Font("SansSerif", Font.BOLD, 30));
		textState.setFocusable(false);
		textState.setEditable(false);
		textState.setBackground(Color.BLACK);
		panel_3.add(textState);
		
		JPanel panel_6 = new JPanel();
		panel_6.setOpaque(false);
		panel_6.setBounds(42, 89, 500, 279);		
		
		//Cambia el GIF deacuerdo al estado
		card = new PromoCard_GIF(order.getState().getImage(), 500, 269);
		card.setBounds(0, 0, 500, 269);
		panel_6.add(card);
		panel_3.add(panel_6);
				
		
		widget = new Buttons("Crear Cuenta", 70, 70, order.getColor(), order.getColor());
		widget.setText("");
		widget.setForeground(Color.WHITE);
		widget.setBackground(order.getColor());
		widget.setBounds(0, 0, 1134, 455);
		panel_3.add(widget);
		btns.add(widget);
		
		JLabel label = new JLabel("New label");
		label.setBounds(596, 164, 46, 14);
		panel_3.add(label);
		
		JPanel panel = new JPanel();
		panel.setBounds(585, 89, 500, 89);
		panel_3.add(panel);
		
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(255, 255, 255));
		panel_4.setLayout(null);
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(0, 0, 5, 5);
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 3;
		gbc_panel_4.gridy = 10;
		mainPanel.add(panel_4, gbc_panel_4);
			
		
		returnBtn = Buttons.estado("← Regresar", order.getColor());
		returnBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Main_Frame.changeDynamicPanel(new ProfileScreen());
				ProfileScreen.changePanel(new OrderHistory(ProfileScreen.getOrders()));
			}
		});
		returnBtn.setBounds(0, 0, 216, 63);
		panel_4.add(returnBtn);
		
		
		simState = Buttons.estado("Avanzar estado →", order.getColor());
		simState.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int anterior = order.getId_state();
				if (anterior + 1 <= 3) {
				    order.setId_state(anterior + 1);
				    try {
				        Restaurant.getInstance().cambiarEstado(order);
				        order.changeState();          // el patrón State avanza solo si se guardó
				        updateGraphics(order);
				    } catch (DaoException ex) {
				        order.setId_state(anterior);  // se regresa: la BD no cambió
				        System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
				        JOptionPane.showMessageDialog(null, "No se pudo guardar el nuevo estado del pedido");
				    }
				} else {
				    JOptionPane.showMessageDialog(null, "Este pedido ya ha sido entregado");
				}
				
			}
		});
		simState.setBounds(918, 0, 216, 63);
		panel_4.add(simState);
		
		
		secMainPanel = new JPanel();
		secMainPanel.setBackground(new Color(255, 255, 255));
		secMainPanel.setBounds(1258, 0, 662, 1000);
		add(secMainPanel);
		GridBagLayout gbl_secMainPanel = new GridBagLayout();
		gbl_secMainPanel.columnWidths = new int[]{30, 0, 30, 0};
		gbl_secMainPanel.rowHeights = new int[]{30, 30, 30, 748, 0};
		gbl_secMainPanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_secMainPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		secMainPanel.setLayout(gbl_secMainPanel);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(new Color(255, 255, 255));
		panel_5.setLayout(null);
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.insets = new Insets(0, 0, 0, 5);
		gbc_panel_5.fill = GridBagConstraints.BOTH;
		gbc_panel_5.gridx = 1;
		gbc_panel_5.gridy = 3;
		secMainPanel.add(panel_5, gbc_panel_5);
		
		JPanel detalles = new JPanel();
		detalles.setPreferredSize(new Dimension(540, 10));
		detalles.setBackground(Color.WHITE);
		detalles.setBounds(27, 75, 542, 632);
		panel_5.add(detalles);
		detalles.setLayout(new BorderLayout(0, 0));
		
		JPanel foodList = new JPanel();				
		foodList.setBackground(Color.white);		
		foodList.setLayout(new BoxLayout(foodList, BoxLayout.Y_AXIS));
		
		for(OrderDetails order : orderlist) {
			foodList.add(new DetailsPanelOD(order, foodList));
		}
		
		JScrollPane scrollPane = new JScrollPane(foodList);		
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);				
		detalles.add(scrollPane, BorderLayout.CENTER);	
		
		JLabel lblInformacinPersonal = new JLabel("Detalles del Pedido:");
		lblInformacinPersonal.setHorizontalAlignment(SwingConstants.CENTER);
		lblInformacinPersonal.setForeground(new Color(0, 0, 0));
		lblInformacinPersonal.setFont(new Font("SansSerif", Font.BOLD, 40));
		lblInformacinPersonal.setBounds(27, 22, 542, 42);
		panel_5.add(lblInformacinPersonal);
		
		Buttons loginbutton_1_1 = new Buttons("", 70, 70, Color.WHITE, Color.WHITE);
		loginbutton_1_1.setForeground(Color.WHITE);
		loginbutton_1_1.setBounds(10, 11, 577, 726);
		panel_5.add(loginbutton_1_1);
		
		rightBtn = new Buttons("", 70, 70, order.getColor(), order.getColor());
		rightBtn.setForeground(Color.WHITE);
		rightBtn.setBounds(0, 0, 597, 748);
		panel_5.add(rightBtn);
		btns.add(rightBtn);
		updateGraphics(order);
		
		
	}
	
	
	
	public ArrayList<OrderDetails> getOrderlist() {
		return orderlist;
	}

	public void setOrderlist(ArrayList<OrderDetails> orderlist) {
		this.orderlist = orderlist;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	//Se actualizan los elementos gráficos en función al color asociado al estado en concreto en el que se halle el pedido, tambien cambia el gif dependiendo de lo mismo
	public void updateGraphics(Order order) {
		for(Buttons btn :btns) {
			btn.setColorFondo(order.getColor());
			btn.setColorHover(order.getColor());
			btn.repaint();
			btn.revalidate();
		}
		returnBtn.setColorFondo(order.getColor());
		returnBtn.repaint();
		returnBtn.revalidate();
		simState.setColorFondo(order.getColor());
		simState.repaint();
		simState.revalidate();
		card.changeImage(order.getImage());
		card.repaint();
		card.revalidate();				
		textState.setText(order.message());
		if(order.getState() instanceof Delivered) {
			estimacion.setText("Pedido entregado");
		}else {
			estimacion.setText("Hora estimada de entrega");
		}
		
	}
		
}