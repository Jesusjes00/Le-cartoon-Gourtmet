package ProfileScreens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.jgoodies.forms.factories.DefaultComponentFactory;

import Restaurante_Completo.LeCartoon_Original.Buttons;
import Restaurante_Completo.LeCartoon_Original.Costumer;

import javax.swing.JTextArea;

public class Payment extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JLabel name;

	public Payment() {
		setSize(1357, 672);
		setLayout(new BorderLayout(0, 0));
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(new Color(255, 255, 255));
		add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(null);
		
		JLabel lblInformacinPersonal = new JLabel("  Método de pago:");
		lblInformacinPersonal.setIcon(new ImageIcon(Payment.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/money1.PNG")));
		lblInformacinPersonal.setHorizontalAlignment(SwingConstants.CENTER);
		lblInformacinPersonal.setForeground(Color.WHITE);
		lblInformacinPersonal.setFont(new Font("SansSerif", Font.BOLD, 40));
		lblInformacinPersonal.setBounds(391, 37, 550, 42);
		mainPanel.add(lblInformacinPersonal);
		
		JLabel lblPruebagmailcom = new JLabel("05/26");
		lblPruebagmailcom.setForeground(Color.WHITE);
		lblPruebagmailcom.setFont(new Font("SansSerif", Font.BOLD, 40));
		lblPruebagmailcom.setBounds(670, 510, 557, 42);
		mainPanel.add(lblPruebagmailcom);
		
		JLabel lblEmail = new JLabel("  Vencimiento:");
		lblEmail.setIcon(new ImageIcon(Payment.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/calendario.png")));
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setFont(new Font("SansSerif", Font.BOLD, 40));
		lblEmail.setBounds(107, 510, 477, 42);
		mainPanel.add(lblEmail);
		
		name = new JLabel("Nombre + Apellido");
		name.setForeground(new Color(255, 255, 255));
		name.setFont(new Font("SansSerif", Font.BOLD, 40));
		name.setBounds(670, 319, 557, 42);
		mainPanel.add(name);
		
		JLabel lblNombreApellido = new JLabel("3784-2938-9302-9403");
		lblNombreApellido.setForeground(Color.WHITE);
		lblNombreApellido.setFont(new Font("SansSerif", Font.BOLD, 40));
		lblNombreApellido.setBounds(670, 163, 557, 42);
		mainPanel.add(lblNombreApellido);
		
		JLabel lblNewLabel = new JLabel("  Titular:");
		lblNewLabel.setIcon(new ImageIcon(Payment.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/user.PNG")));
		lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(107, 319, 477, 42);
		mainPanel.add(lblNewLabel);
		
		JLabel lblNewJgoodiesLabel = DefaultComponentFactory.getInstance().createLabel("  Número de tarjeta:");
		lblNewJgoodiesLabel.setIcon(new ImageIcon(Payment.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/card.png")));
		lblNewJgoodiesLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewJgoodiesLabel.setForeground(new Color(255, 255, 255));
		lblNewJgoodiesLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
		lblNewJgoodiesLabel.setBounds(107, 163, 477, 42);
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
	
	public static void changedCostumer(Costumer costumer) {
		name.setText(costumer.getName() + " " + costumer.getLastName());
	}

}
