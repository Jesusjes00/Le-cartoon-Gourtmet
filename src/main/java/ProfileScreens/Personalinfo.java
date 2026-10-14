package ProfileScreens;

import javax.swing.JPanel;

import Restaurante_Completo.LeCartoon_Original.Buttons;
import Restaurante_Completo.LeCartoon_Original.Costumer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.Font;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

public class Personalinfo extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JLabel name;
	private static JLabel phone;
	private JLabel email;	

	/**
	 * Create the panel.
	 */
	public Personalinfo() {		
		setSize(1357, 672);
		setLayout(new BorderLayout(0, 0));
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(new Color(255, 255, 255));
		add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(null);
		
		JLabel lblInformacinPersonal = new JLabel("Información Personal:");
		lblInformacinPersonal.setHorizontalAlignment(SwingConstants.CENTER);
		lblInformacinPersonal.setForeground(Color.WHITE);
		lblInformacinPersonal.setFont(new Font("SansSerif", Font.BOLD, 40));
		lblInformacinPersonal.setBounds(391, 37, 550, 42);
		mainPanel.add(lblInformacinPersonal);
		
		email = new JLabel("prueba@gmail.com");
		email.setForeground(Color.WHITE);
		email.setFont(new Font("SansSerif", Font.BOLD, 40));
		email.setBounds(670, 500, 557, 52);
		mainPanel.add(email);
		
		JLabel lblEmail = new JLabel("  Email:");
		lblEmail.setIcon(new ImageIcon(Personalinfo.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/email.png")));
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setFont(new Font("SansSerif", Font.BOLD, 40));
		lblEmail.setBounds(107, 510, 243, 42);
		mainPanel.add(lblEmail);
		
		phone = new JLabel("55 xxxxxxxx");
		phone.setForeground(new Color(255, 255, 255));
		phone.setFont(new Font("SansSerif", Font.BOLD, 40));
		phone.setBounds(670, 332, 557, 42);
		mainPanel.add(phone);
		
		name = new JLabel("Nombre + Apellido");
		name.setForeground(Color.WHITE);
		name.setFont(new Font("SansSerif", Font.BOLD, 40));
		name.setBounds(670, 163, 557, 42);
		mainPanel.add(name);
		
		JLabel lblNewLabel = new JLabel("  Teléfono:");
		lblNewLabel.setIcon(new ImageIcon(Personalinfo.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/phone.png")));
		lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(107, 332, 274, 42);
		mainPanel.add(lblNewLabel);
		
		JLabel lblNewJgoodiesLabel = DefaultComponentFactory.getInstance().createLabel("  Nombre:");
		lblNewJgoodiesLabel.setIcon(new ImageIcon(Personalinfo.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/user.PNG")));
		lblNewJgoodiesLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewJgoodiesLabel.setForeground(new Color(255, 255, 255));
		lblNewJgoodiesLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
		lblNewJgoodiesLabel.setBounds(107, 163, 284, 42);
		mainPanel.add(lblNewJgoodiesLabel);
		
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
	
	public static void costumerChanged(Costumer costumer) {		
		name.setText(costumer.getName() + " " + costumer.getLastName()); 
		phone.setText(costumer.getPhone());
	}
}
