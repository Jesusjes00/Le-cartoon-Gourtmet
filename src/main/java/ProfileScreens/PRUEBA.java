package ProfileScreens;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import OrderDetails.Details;
import Restaurante_Completo.LeCartoon_Original.Costumer;
import Restaurante_Completo.LeCartoon_Original.Data_Base;
import Restaurante_Completo.LeCartoon_Original.Order;
import Restaurante_Completo.LeCartoon_Original.OrderDetails;
import Restaurante_Completo.LeCartoon_Original.Restaurant;
import javax.swing.JPanel;

public class PRUEBA {

	private JFrame frame;
	private static JPanel mainPanel;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PRUEBA window = new PRUEBA();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PRUEBA() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Data_Base db = Data_Base.getInstance();
		frame = new JFrame();
		frame.setBounds(100, 100, 1200, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainPanel = new JPanel();		
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		Restaurant r = Restaurant.getInstance(null);
		
		ProfileScreen pfp= new ProfileScreen();
		mainPanel.add(pfp);	
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		
	}
	
	public static void changeMainPanel(JPanel panel) {				
		panel.setBounds(0, 0, 1920, 1000);
		mainPanel.removeAll();
		mainPanel.add(panel, BorderLayout.CENTER);
		mainPanel.revalidate();
		mainPanel.repaint();
	}	
	
}
