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
import java.awt.Dimension;
import javax.swing.JTextArea;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class Addresses extends JPanel {

	private static final long serialVersionUID = 1L;

	public Addresses(String address1) {
		setSize(1357, 672);
		setLayout(new BorderLayout(0, 0));
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(new Color(255, 255, 255));
		add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBounds(40, 92, 1271, 538);
		mainPanel.add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{30, 30, 30, 30, 30, 30, 30, 683, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 35, 97, 119, 211, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblMisDirecciones_1 = new JLabel("");
		lblMisDirecciones_1.setIcon(new ImageIcon(Addresses.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/map1.png")));
		lblMisDirecciones_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblMisDirecciones_1.setForeground(Color.WHITE);
		lblMisDirecciones_1.setFont(new Font("SansSerif", Font.BOLD, 25));
		GridBagConstraints gbc_lblMisDirecciones_1 = new GridBagConstraints();
		gbc_lblMisDirecciones_1.gridheight = 2;
		gbc_lblMisDirecciones_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblMisDirecciones_1.gridx = 1;
		gbc_lblMisDirecciones_1.gridy = 2;
		panel.add(lblMisDirecciones_1, gbc_lblMisDirecciones_1);
		
		JTextArea txtrAvCuauhtmoc = new JTextArea();
		txtrAvCuauhtmoc.setWrapStyleWord(true);
		txtrAvCuauhtmoc.setText("- "+address1);
		txtrAvCuauhtmoc.setOpaque(false);
		txtrAvCuauhtmoc.setLineWrap(true);
		txtrAvCuauhtmoc.setForeground(Color.WHITE);
		txtrAvCuauhtmoc.setFont(new Font("SansSerif", Font.BOLD, 25));
		txtrAvCuauhtmoc.setFocusable(false);
		txtrAvCuauhtmoc.setEditable(false);
		txtrAvCuauhtmoc.setBackground(Color.BLACK);
		GridBagConstraints gbc_txtrAvCuauhtmoc = new GridBagConstraints();
		gbc_txtrAvCuauhtmoc.insets = new Insets(0, 0, 5, 5);
		gbc_txtrAvCuauhtmoc.fill = GridBagConstraints.BOTH;
		gbc_txtrAvCuauhtmoc.gridx = 7;
		gbc_txtrAvCuauhtmoc.gridy = 2;
		panel.add(txtrAvCuauhtmoc, gbc_txtrAvCuauhtmoc);
		
		JTextArea txtrAvBenjamn = new JTextArea();
		txtrAvBenjamn.setWrapStyleWord(true);
		txtrAvBenjamn.setText("- "+"Av. Benjamín Franklin 45, Colonia Condesa, Cuauhtémoc, 06140 Ciudad de México, CDMX");
		txtrAvBenjamn.setOpaque(false);
		txtrAvBenjamn.setLineWrap(true);
		txtrAvBenjamn.setForeground(Color.WHITE);
		txtrAvBenjamn.setFont(new Font("SansSerif", Font.BOLD, 25));
		txtrAvBenjamn.setFocusable(false);
		txtrAvBenjamn.setEditable(false);
		txtrAvBenjamn.setBackground(Color.BLACK);
		GridBagConstraints gbc_txtrAvBenjamn = new GridBagConstraints();
		gbc_txtrAvBenjamn.insets = new Insets(0, 0, 5, 5);
		gbc_txtrAvBenjamn.fill = GridBagConstraints.BOTH;
		gbc_txtrAvBenjamn.gridx = 7;
		gbc_txtrAvBenjamn.gridy = 3;
		panel.add(txtrAvBenjamn, gbc_txtrAvBenjamn);
		
		JLabel lblInformacinPersonal = new JLabel("  Mis Direcciones:");
		lblInformacinPersonal.setIcon(new ImageIcon(Addresses.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/location1.png")));
		lblInformacinPersonal.setHorizontalAlignment(SwingConstants.CENTER);
		lblInformacinPersonal.setForeground(Color.WHITE);
		lblInformacinPersonal.setFont(new Font("SansSerif", Font.BOLD, 40));
		lblInformacinPersonal.setBounds(391, 39, 550, 42);
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
