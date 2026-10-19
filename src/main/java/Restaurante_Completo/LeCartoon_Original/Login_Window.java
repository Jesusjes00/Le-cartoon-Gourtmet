package Restaurante_Completo.LeCartoon_Original;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Font;
import java.awt.TextField;
import java.awt.SystemColor;
import javax.swing.JOptionPane;
import persistence.DaoException;

public class Login_Window {

	private JFrame frame;
	private JPanel contentPane;
	private JTextField txtEmail;
	private javax.swing.JPasswordField txtpass;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login_Window window = new Login_Window();
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
	public Login_Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Le Cartoon");
		frame.setBounds(100, 100, 1200 , 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,750);
		frame.setLocationRelativeTo(null);
		
		//Creamos el panel principal
		contentPane = new JPanel();
		contentPane.setBackground(new Color(254,242,240));
		contentPane.setBorder(new EmptyBorder (5,5,5,5));
		frame.setContentPane(contentPane);
		
		GridBagLayout gbl_contentPane = new GridBagLayout(); // Para centrar la tarjeta del centro
		contentPane.setLayout(gbl_contentPane);
		
		//card
		JPanel HoverCard = new JPanel();
		HoverCard.setBackground(Color.WHITE);
		HoverCard.setPreferredSize(new Dimension(800,530));
		
		
		//Suavizar bordes y efecto sombra
		LineBorder border = new LineBorder(new Color(255,220,215),1,true);
		EmptyBorder margin = new EmptyBorder(0,0,0,0);
		HoverCard.setBorder(new CompoundBorder(border,margin));
		// sombras
		// Card de sombra
		JPanel HoverCardBackground = new JPanel();
		HoverCardBackground.setOpaque(true);
		HoverCardBackground.setBackground(new Color(255,220,215,50));
		HoverCardBackground.setPreferredSize(new Dimension(820,550));
		
		 
		//configuramos el grid con restricciones
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		// Constraints para el segundo layout
		GridBagConstraints gbc_2 = new GridBagConstraints();
		gbc_2.gridx = 0;
		gbc_2.gridy = 0;
		
		contentPane.add(HoverCard,gbc);
		contentPane.add(HoverCardBackground,gbc_2);
		
		//Dividimos la tarjeta en 2 columnas
		HoverCard.setLayout(new GridLayout(1,2,0,0));
		JPanel LeftPanel = new JPanel();
		LeftPanel.setBackground(new Color(45,45,45));
		HoverCard.add(LeftPanel);
		LeftPanel.setLayout(new java.awt.BorderLayout());
		
		// Creamos el JLabel vacío para el GIF
				JLabel labelGif = new JLabel();
				labelGif.setHorizontalAlignment(SwingConstants.CENTER);
				labelGif.setVerticalAlignment(SwingConstants.CENTER);
				LeftPanel.add(labelGif, java.awt.BorderLayout.CENTER);
				
				//Obtenemos al gif
				URL gifURL = Login_Window.class.getResource("/Restaurante_Completo/LeCartoon_Original/GIF/LOGIN_1.gif");

				if (gifURL != null) {
					// Listener para adaptar el GIF al tamaño exacto de la mitad de la pantalla de forma fluida
					LeftPanel.addComponentListener(new ComponentAdapter() {
						@Override
						public void componentResized(ComponentEvent e) {
							int ancho = LeftPanel.getWidth();
							int alto = LeftPanel.getHeight();
							
							if (ancho > 0 && alto > 0) {
								ImageIcon iconoOriginal = new ImageIcon(gifURL);
								// Forzar escala para cubrir todo el panel sin dejar bordes grises
								Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_DEFAULT);
								labelGif.setIcon(new ImageIcon(imagenEscalada));
							}
						}
					});
				} else {
				    System.out.println("No se encontró el archivo GIF");
				}
		
		//Layout derecho
		JPanel RightPanel = new JPanel();
		RightPanel.setBackground(Color.WHITE);
		HoverCard.add(RightPanel);
		RightPanel.setLayout(null);
		
		JLabel logo_letters = new JLabel("Le Cartoon");
		logo_letters.setBounds(84, 39, 214, 62);
		try {
		    URL fontURL = Login_Window.class.getResource("/Restaurante_Completo/LeCartoon_Original/FONTS/Baloo2-SemiBold.ttf");
		    
		    if (fontURL != null) {
		        java.awt.Font font = java.awt.Font.createFont(
		            java.awt.Font.TRUETYPE_FONT,
		            new java.io.File(fontURL.toURI())
		        );
		        font = font.deriveFont(20f);
		        logo_letters.setFont(font);
		        logo_letters.setForeground(new java.awt.Color(0, 0, 0));
		    } else {
		        System.out.println("No se encontró el archivo FONT");
		    }
		} catch(Exception e) {
		    System.out.println("Error al cargar el font: " + e.getMessage());
		    e.printStackTrace();
		}
		RightPanel.add(logo_letters);
		
		//Agregamos el el logo de la cocina
		JLabel labelLogo = new JLabel();
		labelLogo.setBounds(-20,25,150,82);
		labelLogo.setHorizontalAlignment(SwingConstants.CENTER);
		RightPanel.add(labelLogo);
		
		//ARIAL para las letras normales
		JLabel Text = new JLabel("Un Mundo de sabores y Aventuras te esperan.");
		Text.setFont(new Font("SansSerif", Font.PLAIN, 11));
		Text.setBounds(40, 150, 281, 14);
		RightPanel.add(Text);
		//Bienvenidos
		JLabel welcome = new JLabel("Bienvenido de Nuevo");
		welcome.setFont(new Font("Arial Black", Font.BOLD, 15));
		welcome.setBounds(40, 120, 214, 14);
		RightPanel.add(welcome);
		
		JLabel email = new JLabel("Correo Electronico");
		email.setFont(new Font("SansSerif", Font.PLAIN, 12));
		email.setBounds(40, 194, 162, 14);
		RightPanel.add(email);
		
		JLabel password = new JLabel("Contraseña");
		password.setFont(new Font("SansSerif", Font.PLAIN, 12));
		password.setBounds(40, 262, 162, 14);
		RightPanel.add(password);
		
		//Incorrecta
		JLabel wrong = new JLabel("Correo o Contraseña Invalida");
		wrong.setFont(new Font("SansSerif", Font.PLAIN, 12));
		wrong.setBounds(40, 100, 162, 14);
		wrong.setForeground(Color.WHITE);
		RightPanel.add(wrong);
		
		//Etiqueta Textfield
		LineBorder bordeGrisInput = new LineBorder(new Color(225, 220, 215), 1, true);
		
				// El JTextField del Email
				txtEmail = new JTextField();
				txtEmail.setText("✉ name@mail.com");
				txtEmail.setForeground(new Color(160, 155, 150)); // Texto de sugerencia
				txtEmail.setBackground(Color.WHITE);
				txtEmail.setBounds(40, 219, 300, 35); // Altura de 45 para que sea robusto como el de la imagen
				txtEmail.setBorder(new CompoundBorder(bordeGrisInput, new EmptyBorder(0, 10, 0, 10)));
				//Agregamos un Listener
				txtEmail.addFocusListener(new java.awt.event.FocusAdapter() {
					@Override //Cuando Empezamos a escribir
					public void focusGained(java.awt.event.FocusEvent e) {
						if (txtEmail.getText().equals("✉ name@mail.com")) {
							txtEmail.setText("");
							txtEmail.setForeground(Color.BLACK); 
						}
					}
					@Override//En caso que no se haya escrito nada
					public void focusLost(java.awt.event.FocusEvent e) {
						if (txtEmail.getText().isEmpty()) {
							txtEmail.setText("✉ name@mail.com");
							txtEmail.setForeground(new Color(160, 155, 150));
						}
					}
				});
				RightPanel.add(txtEmail);
				
				
				// El JTextField del password
				txtpass = new javax.swing.JPasswordField();
				txtpass.setEchoChar((char) 0); // Desactiva los puntos inicialmente para que se lea el texto de sugerencia
				txtpass.setText("🔒 ••••••••");
				txtpass.setForeground(new Color(160, 155, 150)); 
				txtpass.setBackground(Color.WHITE);
				txtpass.setBounds(40, 290, 300, 35); 
				txtpass.setBorder(new CompoundBorder(bordeGrisInput, new EmptyBorder(0, 10, 0, 10)));
				
				txtpass.addFocusListener(new java.awt.event.FocusAdapter() {
					@Override
					public void focusGained(java.awt.event.FocusEvent e) {
						String passStr = new String(txtpass.getPassword());
						if (passStr.equals("🔒 ••••••••")) {
							txtpass.setText("");
							txtpass.setEchoChar('•'); // Activa los puntos de encriptación
							txtpass.setForeground(Color.BLACK);
						}
					}
					@Override
					public void focusLost(java.awt.event.FocusEvent e) {
						String passStr = new String(txtpass.getPassword());
						if (passStr.isEmpty()) {
							txtpass.setEchoChar((char) 0); // Desactiva los puntos para ver el texto de sugerencia
							txtpass.setText("🔒 ••••••••");
							txtpass.setForeground(new Color(160, 155, 150));
						}
					}
				});
				RightPanel.add(txtpass);
				
				//Botones
				Buttons loginbutton = Buttons.primario("Entrar →");
				loginbutton.setBounds(40, 350, 306, 41);   // +6 de ancho y alto la sobra ya esta en el boton
				loginbutton.addActionListener(e ->{
					String user = txtEmail.getText().trim();
					String password_prev = new String(txtpass.getPassword());
					
					//Validaciones 
					
					if (user.isEmpty() || user.equals("✉ name@mail.com") || 
						    password_prev.isEmpty() || password_prev.equals("🔒 ••••••••")) {
							
							System.out.println("Por favor, rellene todos los campos requeridos.");
							//Se pinta de rojo el borde para indicar un error
							txtEmail.setBorder(new CompoundBorder(new LineBorder(Color.RED, 1, true), new EmptyBorder(0, 10, 0, 10)));
							txtpass.setBorder(new CompoundBorder(new LineBorder(Color.RED, 1, true), new EmptyBorder(0, 10, 0, 10)));
						} else {
							//Procedemos a meter la base de datos
							Costumer cliente;
							try {
							    cliente = Restaurant.getInstance().login(user, password_prev);
							} catch (DaoException ex) {
							    System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
							    JOptionPane.showMessageDialog(frame, "No hay conexión con la base de datos. Intenta de nuevo.");
							    return;
							}

							if (cliente != null) {
							    Main_Frame principal_window = new Main_Frame();
							    principal_window.showmainframe(true, cliente);
							    frame.dispose();
							} else {
							    wrong.setForeground(Color.RED);
							    txtEmail.setBorder(new CompoundBorder(new LineBorder(Color.RED, 1, true), new EmptyBorder(0, 10, 0, 10)));
							    txtpass.setBorder(new CompoundBorder(new LineBorder(Color.RED, 1, true), new EmptyBorder(0, 10, 0, 10)));
							}
							
						}
					});
					RightPanel.add(loginbutton);
				
				//Texto plano para registrarse
				JLabel txtregister = new JLabel("¿Todavia no eres parte de la familia?");
				txtregister.setFont(new Font("SansSerif", Font.PLAIN, 11));
				txtregister.setBounds(99, 405, 233, 14);
				RightPanel.add(txtregister);
				
				//Boton para registrarse
				Buttons regbutton = Buttons.secundario("Registrarse");
				regbutton.setBounds(40, 430, 306, 41);
				regbutton.addActionListener(e->{
					Register_Window regWind = new Register_Window();
					regWind.showregframe(true);
					frame.dispose();
				});
				RightPanel.add(regbutton);
				
				
				
		
		
		
		//Cargamos el URL de la imagen
		URL logoURL = Login_Window.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/LOGO.png");

		if (logoURL != null) {
			ImageIcon LeCartoon_Logo = new ImageIcon(logoURL);
			
			
			Image Logo_Scale = LeCartoon_Logo.getImage().getScaledInstance(labelLogo.getWidth(), labelLogo.getHeight(), Image.SCALE_SMOOTH);
			labelLogo.setIcon(new ImageIcon(Logo_Scale));
		} else {
			System.out.println("No se encontró el archivo del logo PNG.");
		}
		
		
	}
	
	//Metodo para abrir la ventana
	public void showloginframe(boolean visible) {
		if(this.frame!= null) {
			this.frame.setVisible(visible);
		}
	}
}
