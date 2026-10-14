package Restaurante_Completo.LeCartoon_Original;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ProfileScreen {

	private JFrame frame;
	private Costumer costumer = new Costumer("dskfm", "dsa", "asd", "dsa", "asd","ddd");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProfileScreen window = new ProfileScreen();
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
	public ProfileScreen() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1247, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(250, 240, 230));
		panel.setBounds(0, 0, 1200, 730);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(null);
		panel_2.setBounds(35, 30, 1124, 159);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		Buttons boton = new Buttons("prueba", 30, 30, new Color(0,0,0), new Color(70,70,70));
		boton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		boton.setText("Editar Perfil");
		boton.setBounds(1638, 55, 153, 47);
		panel_2.add(boton);
		boton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		boton.setForeground(Color.white);
		
		JLabel name = DefaultComponentFactory.getInstance().createLabel("Nombre Apellido");
		name.setText(costumer.getName() + " " + costumer.getLastName());
		name.setFont(new Font("Tahoma", Font.PLAIN, 22));
		name.setBounds(175, 25, 374, 46);
		panel_2.add(name);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(ProfileScreen.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/pfp.png")));
		lblNewLabel_1.setBounds(21, 11, 144, 137);
		panel_2.add(lblNewLabel_1);
		
		Buttons boton_1 = new Buttons("", 40, 40, Color.WHITE, Color.WHITE);
		boton_1.setBounds(0, 0, 1124, 159);
		panel_2.add(boton_1);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(null);
		panel_3.setBounds(35, 235, 700, 400);
		panel.add(panel_3);
		panel_3.setLayout(null);
		
		JLabel name2 = DefaultComponentFactory.getInstance().createLabel("New JGoodies label");
		name2.setText(costumer.getName() + " " + costumer.getLastName());
		name2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		name2.setBounds(82, 133, 342, 29);
		panel_3.add(name2);
		
		JLabel lblNewJgoodiesLabel_4 = DefaultComponentFactory.getInstance().createLabel("Nombre: ");
		lblNewJgoodiesLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewJgoodiesLabel_4.setBounds(61, 98, 92, 14);
		panel_3.add(lblNewJgoodiesLabel_4);
		
		JLabel emailCostumer = DefaultComponentFactory.getInstance().createLabel("New JGoodies label");
		emailCostumer.setText(costumer.getEmail());
		emailCostumer.setFont(new Font("Tahoma", Font.PLAIN, 18));
		emailCostumer.setBounds(82, 323, 342, 29);
		panel_3.add(emailCostumer);
		
		JLabel lblNewJgoodiesLabel_3 = DefaultComponentFactory.getInstance().createLabel("Email:");
		lblNewJgoodiesLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewJgoodiesLabel_3.setBounds(61, 286, 92, 14);
		panel_3.add(lblNewJgoodiesLabel_3);
		
		JLabel telCostumer = DefaultComponentFactory.getInstance().createLabel("New JGoodies label");
		telCostumer.setText(costumer.getPhone());
		telCostumer.setFont(new Font("Tahoma", Font.PLAIN, 18));
		telCostumer.setBounds(82, 227, 231, 22);
		panel_3.add(telCostumer);
		
		JLabel lblNewJgoodiesLabel_2 = DefaultComponentFactory.getInstance().createLabel("Teléfono: ");
		lblNewJgoodiesLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewJgoodiesLabel_2.setBounds(61, 194, 100, 22);
		panel_3.add(lblNewJgoodiesLabel_2);
		
		JLabel lblNewJgoodiesLabel_1 = DefaultComponentFactory.getInstance().createLabel("Información Personal");
		lblNewJgoodiesLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblNewJgoodiesLabel_1.setBounds(245, 26, 211, 45);
		panel_3.add(lblNewJgoodiesLabel_1);
		
//Ignorar botones
		Buttons boton_1_1_1_1 = new Buttons("", 40, 40, Color.WHITE, Color.WHITE);
		boton_1_1_1_1.setBounds(24, 26, 653, 348);
		panel_3.add(boton_1_1_1_1);
		
		Buttons boton_1_1_1 = new Buttons("", 40, 40, new Color(250, 240, 230), new Color(250, 240, 230));
		boton_1_1_1.setBounds(10, 11, 680, 378);
		panel_3.add(boton_1_1_1);
		
		Buttons boton_1_1 = new Buttons("", 40, 40, Color.WHITE, Color.WHITE);
		boton_1_1.setBounds(0, 0, 700, 400);
		panel_3.add(boton_1_1);
		
		Buttons btnsCerrarSesin = new Buttons("prueba", 30, 30, Color.BLACK, new Color(70,70,70));
		btnsCerrarSesin.setText("Cerrar Sesión");
		btnsCerrarSesin.setForeground(Color.WHITE);
		btnsCerrarSesin.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnsCerrarSesin.setBounds(1741, 983, 153, 47);
		panel.add(btnsCerrarSesin);
//Ignorar botones		
		
		
		
		
	}
}
