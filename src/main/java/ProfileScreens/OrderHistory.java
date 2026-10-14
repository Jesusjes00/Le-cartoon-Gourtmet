package ProfileScreens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.jgoodies.forms.factories.DefaultComponentFactory;

import Restaurante_Completo.LeCartoon_Original.Buttons;
import Restaurante_Completo.LeCartoon_Original.Order;

import javax.swing.BoxLayout;
import javax.swing.ScrollPaneConstants;
import java.awt.Dimension;

public class OrderHistory extends JPanel {	 

	private static final long serialVersionUID = 1L;
	private static JScrollPane scrollPane;
	

	public OrderHistory(ArrayList<Order> orders) {
		setSize(1357, 672);
		setLayout(new BorderLayout(0, 0));
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(new Color(255, 255, 255));
		add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(1250, 10));
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(51, 108, 1250, 500);		
		panel.setLayout(new BorderLayout(0, 0));
		mainPanel.add(panel);
		
		JPanel historialPanel = new JPanel();				
		historialPanel.setBackground(new Color(255, 255, 255));		
		historialPanel.setLayout(new BoxLayout(historialPanel, BoxLayout.Y_AXIS));
				
		if(orders.size() != 0) {
			for(Order order : orders) {
				historialPanel.add(new OrderPanel(order, historialPanel));
			}
		}
		
		scrollPane = new JScrollPane(historialPanel);		
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);				
		panel.add(scrollPane, BorderLayout.CENTER);		
		
		
		JLabel lblInformacinPersonal = new JLabel("Historial de Pedidos:");
		lblInformacinPersonal.setHorizontalAlignment(SwingConstants.CENTER);
		lblInformacinPersonal.setForeground(Color.WHITE);
		lblInformacinPersonal.setFont(new Font("SansSerif", Font.BOLD, 40));
		lblInformacinPersonal.setBounds(391, 37, 550, 42);
		mainPanel.add(lblInformacinPersonal);
		
		Buttons loginbutton_1_1 = new Buttons("", 70, 70, Color.BLACK, Color.BLACK);
		loginbutton_1_1.setBounds(23, 24, 1309, 623);
		loginbutton_1_1.setForeground(Color.WHITE);
		loginbutton_1_1.setColorFondo(new Color(0, 0, 0));
		mainPanel.add(loginbutton_1_1);
		
		Buttons loginbutton_1 = new Buttons("", 70, 70, Color.white, Color.white);
		loginbutton_1.setBounds(10, 11, 1337, 650);
		loginbutton_1.setForeground(Color.WHITE);
		mainPanel.add(loginbutton_1);
		
		Buttons loginbutton = new Buttons("", 70, 70, Color.black, Color.black);
		loginbutton.setBounds(0, 0, 1357, 672);
		loginbutton.setForeground(Color.white);
		mainPanel.add(loginbutton);			
		

	}	

}
