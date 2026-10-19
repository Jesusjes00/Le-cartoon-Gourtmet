package ProfileScreens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.jgoodies.forms.factories.DefaultComponentFactory;

import Restaurante_Completo.LeCartoon_Original.Buttons;
import Restaurante_Completo.LeCartoon_Original.Costumer;

import javax.swing.JTextField;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import Restaurante_Completo.LeCartoon_Original.Restaurant;
import persistence.DaoException;

public class EditProfile extends JPanel {	
	private String namePH = "Nuevo Nombre";
	private String lnamePH = "Nuevo Apellido";
	private String phonePH = "Nuevo Teléfono";

	private static final long serialVersionUID = 1L;
	private JTextField nameField;
	private JTextField lnameField;
	private JTextField phoneField;

	public EditProfile(Costumer costumer) {
		setSize(1357, 672);
		setLayout(new BorderLayout(0, 0));		
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(new Color(255, 255, 255));
		add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(null);
		
		nameField = new JTextField();
		nameField.setBounds(674, 124, 557, 52);		
		nameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				nameField.setForeground(Color.white);
			}
		});
		nameField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				nameField.setText("");
			}
		});
		
		Buttons editBtn = Buttons.primario("Guardar cambios");
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				if(canEdit()) {
					if(JOptionPane.showConfirmDialog(null, "¿Tus datos son correctos?") == 0) {
						Costumer cambios = new Costumer(costumer.getId(), nameField.getText(), lnameField.getText(),
						        phoneField.getText(), costumer.getEmail(), costumer.getPassword(), costumer.getAddress());
						try {
						    Restaurant.getInstance().actualizarPerfil(cambios);
						    costumer.setName(cambios.getName());            // solo si la BD aceptó
						    costumer.setLastName(cambios.getLastName());
						    costumer.setPhone(cambios.getPhone());
						    JOptionPane.showMessageDialog(null, "Se actualizó correctamente tu perfil");
						    resetPH();
						    ProfileScreen.costumerChanged();
						} catch (DaoException ex) {
						    System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
						    JOptionPane.showMessageDialog(null, "No se pudo actualizar tu perfil. Intenta de nuevo.");
						}					
					}					
				}else {
					JOptionPane.showMessageDialog(null, "Error, Ingrese información válida");
					resetPH();
				}
			}
		});
		editBtn.setBounds(1044, 562, 187, 47);
		mainPanel.add(editBtn);
		
		JSeparator separator_1_1 = new JSeparator();
		separator_1_1.setBounds(674, 523, 557, 2);
		mainPanel.add(separator_1_1);
				
		phoneField = new JTextField();
		phoneField.setText(phonePH);
		phoneField.setForeground(Color.LIGHT_GRAY);
		phoneField.setFont(new Font("SansSerif", Font.BOLD, 40));
		phoneField.setBorder(null);
		phoneField.setBackground(Color.BLACK);
		phoneField.setBounds(674, 460, 557, 52);
		phoneField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				phoneField.setForeground(Color.white);
			}
		});
		phoneField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				phoneField.setText("");
			}
		});
		mainPanel.add(phoneField);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(674, 351, 557, 2);
		mainPanel.add(separator_1);
				
		lnameField = new JTextField();
		lnameField.setText(lnamePH);
		lnameField.setForeground(Color.LIGHT_GRAY);
		lnameField.setFont(new Font("SansSerif", Font.BOLD, 40));
		lnameField.setBorder(null);
		lnameField.setBackground(Color.BLACK);
		lnameField.setBounds(674, 288, 557, 52);
		lnameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				lnameField.setForeground(Color.white);
			}
		});
		lnameField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lnameField.setText("");
			}
		});
		mainPanel.add(lnameField);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(674, 187, 557, 2);
		mainPanel.add(separator);
				
		nameField.setText(namePH);
		nameField.setForeground(Color.LIGHT_GRAY);
		nameField.setFont(new Font("SansSerif", Font.BOLD, 40));
		nameField.setBorder(null);
		nameField.setBackground(new Color(0, 0, 0));
		mainPanel.add(nameField);
		
		JLabel lblInformacinPersonal = new JLabel("Información Personal:");
		lblInformacinPersonal.setHorizontalAlignment(SwingConstants.CENTER);
		lblInformacinPersonal.setForeground(Color.WHITE);
		lblInformacinPersonal.setFont(new Font("SansSerif", Font.BOLD, 40));
		lblInformacinPersonal.setBounds(391, 37, 550, 42);
		mainPanel.add(lblInformacinPersonal);
		
		JLabel lblEmail = new JLabel("  Teléfono:");
		lblEmail.setIcon(new ImageIcon(EditProfile.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/phone.png")));
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setFont(new Font("SansSerif", Font.BOLD, 40));
		lblEmail.setBounds(111, 483, 243, 42);
		mainPanel.add(lblEmail);
		
		JLabel lblNewLabel = new JLabel("  Apellido:");
		lblNewLabel.setIcon(new ImageIcon(EditProfile.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/user.PNG")));
		lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setBounds(111, 311, 274, 42);
		mainPanel.add(lblNewLabel);
		
		JLabel lblNewJgoodiesLabel = DefaultComponentFactory.getInstance().createLabel("  Nombre:");
		lblNewJgoodiesLabel.setIcon(new ImageIcon(Personalinfo.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/user.PNG")));
		lblNewJgoodiesLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewJgoodiesLabel.setForeground(new Color(255, 255, 255));
		lblNewJgoodiesLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
		lblNewJgoodiesLabel.setBounds(111, 136, 284, 42);
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
	
	public boolean canEdit() {
		if(!nameField.getText().isEmpty() && !nameField.getText().equalsIgnoreCase(namePH) &&
				!lnameField.getText().isEmpty() && !lnameField.getText().equalsIgnoreCase(lnamePH) &&
				!phoneField.getText().isEmpty() && !phoneField.getText().equalsIgnoreCase(phonePH) ) {
			//Si los placeholders tienen texto válido, que performe la acción de updatear
			return true;
		}else {
			return false;
		}
	}
	
	public void resetPH() {
		nameField.setText(namePH);
		nameField.setForeground(Color.LIGHT_GRAY);
		lnameField.setText(lnamePH);
		lnameField.setForeground(Color.LIGHT_GRAY);
		phoneField.setText(phonePH);
		phoneField.setForeground(Color.LIGHT_GRAY);
	}
	
	public void changeCostumer() {
		nameField.getText();
	}
}
