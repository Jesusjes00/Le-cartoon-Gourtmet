package Restaurante_Completo.LeCartoon_Original;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

public class Register_Window {

	private JFrame frame;
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtlastname;
	private JTextField txtnumber;
	private JTextField txtemail;
	private JTextField txtpass;
	private JTextField txtadd;
	
	// Variables parra permitir el movimento
	private int mouseX, mouseY;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Register_Window window = new Register_Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Register_Window() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame(); // No lleva Nombre por que se va a quitar la barra de control
		frame.setSize(1000, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		// Quitamos la barra de control ===========================================================>
		frame.setUndecorated(true);
		
		// Panel Principal ================================================================>
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0)); 
		frame.setContentPane(contentPane);
		
		contentPane.setLayout(new GridLayout(1, 2, 0, 0));
		
		// Panel Izquierod=====================================================================>
		JPanel LeftPanel = new JPanel();
		LeftPanel.setBackground(new Color(45, 45, 45));
		contentPane.add(LeftPanel);
		LeftPanel.setLayout(new java.awt.BorderLayout());
		
		JLabel labelGif = new JLabel();
		labelGif.setHorizontalAlignment(SwingConstants.CENTER);
		labelGif.setVerticalAlignment(SwingConstants.CENTER);
		LeftPanel.add(labelGif, java.awt.BorderLayout.CENTER);
		//Gif de la app=================================================================================================>
		URL gifURL = Register_Window.class.getResource("/Restaurante_Completo/LeCartoon_Original/GIF/REG.gif");

		if (gifURL != null) {
			LeftPanel.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					int ancho = LeftPanel.getWidth();
					int alto = LeftPanel.getHeight();
					
					if (ancho > 0 && alto > 0) {
						ImageIcon iconoOriginal = new ImageIcon(gifURL);
						Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_DEFAULT);
						labelGif.setIcon(new ImageIcon(imagenEscalada));
					}
				}
			});
		} else {
		    System.out.println("No se encontró el archivo GIF");
		}
		
		// Panel Derecho=======================================================================>
		JPanel RightPanel = new JPanel();
		RightPanel.setBackground(Color.WHITE);
		contentPane.add(RightPanel);
		RightPanel.setLayout(null); 
		

		//Barra de control Personalizada===========================================================>
		
		// Botón Cerrar------------------------------------------------------------------->
		Buttons btnClose = new Buttons("✕", 0, 0, Color.WHITE, new Color(231, 76, 60));
		btnClose.setBounds(450, 0, 50, 30); // El panel mide 500 de ancho (1000 / 2)
		btnClose.setFont(new Font("SansSerif", Font.PLAIN, 14));
		btnClose.setForeground(new Color(120, 120, 120));
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) { btnClose.setForeground(Color.WHITE); }
			@Override
			public void mouseExited(MouseEvent e) { btnClose.setForeground(new Color(120, 120, 120)); }
		});
		btnClose.addActionListener(e -> System.exit(0)); 
		RightPanel.add(btnClose);

		// Botón Minimizar------------------------------------------------------------------------->
		Buttons btnMinimize = new Buttons("—", 0, 0, Color.WHITE, new Color(235, 235, 235));
		btnMinimize.setBounds(400, 0, 50, 30); 
		btnMinimize.setFont(new Font("SansSerif", Font.PLAIN, 12));
		btnMinimize.setForeground(new Color(120, 120, 120));
		btnMinimize.addActionListener(e -> frame.setState(JFrame.ICONIFIED)); 
		RightPanel.add(btnMinimize);
		
		// Hacemos que la pantalla responda al arrastre del mouse desde el RightPanel------------>
		RightPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX() + 500; // Compensación por el lef Panel
				mouseY = e.getY();
			}
		});
		RightPanel.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				frame.setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
			}
		});
		

		

		//Componentes de la App---------------------------------------------------------------->
		JLabel logo_letters = new JLabel("Le Cartoon");
		logo_letters.setBounds(84, 15, 214, 62);
		try {
		    URL fontURL = Register_Window.class.getResource("/Restaurante_Completo/LeCartoon_Original/FONTS/Baloo2-SemiBold.ttf");
		    if (fontURL != null) {
		        Font font = Font.createFont(Font.TRUETYPE_FONT, new java.io.File(fontURL.toURI()));
		        font = font.deriveFont(20f);
		        logo_letters.setFont(font);
		        logo_letters.setForeground(new Color(0, 0, 0));
		    }
		} catch(Exception e) {
		    System.out.println("Error al cargar el font: " + e.getMessage());
		}
		RightPanel.add(logo_letters);
		
		JLabel labelLogo = new JLabel();
		labelLogo.setBounds(-20, 4, 150, 82);
		labelLogo.setHorizontalAlignment(SwingConstants.CENTER);
		RightPanel.add(labelLogo);
		
		JLabel welcome = new JLabel("Registrate");
		welcome.setFont(new Font("Arial Black", Font.BOLD, 35));
		welcome.setBounds(40, 95, 292, 56);
		RightPanel.add(welcome);

		JLabel Text = new JLabel("Crea tu cuenta para empezar la aventura culinaria.");
		Text.setFont(new Font("SansSerif", Font.PLAIN, 12));
		Text.setBounds(40, 150, 300, 14);
		RightPanel.add(Text);
		
		//Jlabels  y textfields  de la app============================================================================================>
		//Nombres
		JLabel name = new JLabel("Nombres");
		name.setFont(new Font("SansSerif", Font.PLAIN, 12));
		name.setBounds(40, 194, 162, 14);
		RightPanel.add(name);
		
		//Cuadro de texto de los nombres
		txtName = new JTextField(); 
		txtName.setText("👤 Tus Nombres");
		txtName.setForeground(new Color(160, 155, 150)); 
		txtName.setBackground(Color.WHITE);
		txtName.setBounds(40, 214, 200, 40); 
		LineBorder bordeGrisInput = new LineBorder(new Color(225, 220, 215), 1, true);
		txtName.setBorder(new CompoundBorder(bordeGrisInput, new EmptyBorder(0, 10, 0, 10)));
		//Agramos un Listener
		txtName.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override//cuando Empezamos a escirbir
			public void focusGained(java.awt.event.FocusEvent e) {
				if(txtName.getText().equals("👤 Tus Nombres")) {
					txtName.setText("");
					txtName.setForeground(Color.BLACK);
				}
			}
			
			@Override//En caso que no se haya escrito nada
			public void focusLost(java.awt.event.FocusEvent e) {
				if(txtName.getText().isEmpty()) {
					txtName.setText("👤 Tus Nombres");
					txtName.setForeground(new Color(160, 155, 150));
				}
			}
		});
		RightPanel.add(txtName);
		
		//Apellidos
		JLabel lastname = new JLabel("Apellidos");
		lastname.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lastname.setBounds(250, 194, 162, 14);
		RightPanel.add(lastname);
		
		//Cuadro de texto de los Apellidos
		txtlastname = new JTextField();
		txtlastname.setText("👤 Tus Apellidos");
		txtlastname.setForeground(new Color(160, 155, 150)); 
		txtlastname.setBackground(Color.WHITE);
		txtlastname.setBounds(250, 214, 200, 40); 
		txtlastname.setBorder(new CompoundBorder(bordeGrisInput, new EmptyBorder(0, 10, 0, 10)));
		//Agramos un Listener
		txtlastname.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override//cuando Empezamos a escirbir
			public void focusGained(java.awt.event.FocusEvent e) {
				if(txtlastname.getText().equals("👤 Tus Apellidos")) {
					txtlastname.setText("");
					txtlastname.setForeground(Color.BLACK);
				}
			}
			
			@Override//En caso que no se haya escrito nada
			public void focusLost(java.awt.event.FocusEvent e) {
				if(txtlastname.getText().isEmpty()) {
					txtlastname.setText("👤 Tus Apellidos");
					txtlastname.setForeground(new Color(160, 155, 150));
				}
			}
		});
		RightPanel.add(txtlastname);
		
		//Numeros
		JLabel number = new JLabel("Teléfono");
		number.setFont(new Font("SansSerif", Font.PLAIN, 12));
		number.setBounds(40, 265, 162, 14);
		RightPanel.add(number);
		
		//Cuadro de texto de los Numeros
		txtnumber = new JTextField();
		txtnumber.setText(" +1 (5555) 000-0000");
		txtnumber.setForeground(new Color(160, 155, 150)); 
		txtnumber.setBackground(Color.WHITE);
		txtnumber.setBounds(40, 285, 410, 40); 
		txtnumber.setBorder(new CompoundBorder(bordeGrisInput, new EmptyBorder(0, 10, 0, 10)));
		//Agramos un Listener
		txtnumber.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override//cuando Empezamos a escirbir
			public void focusGained(java.awt.event.FocusEvent e) {
				if(txtnumber.getText().equals(" +1 (5555) 000-0000")) {
					txtnumber.setText("");
					txtnumber.setForeground(Color.BLACK);
				}
			}
			
			@Override//En caso que no se haya escrito nada
			public void focusLost(java.awt.event.FocusEvent e) {
				if(txtnumber.getText().isEmpty()) {
					txtnumber.setText(" +1 (5555) 000-0000");
					txtnumber.setForeground(new Color(160, 155, 150));
				}
			}
		});
		RightPanel.add(txtnumber);
		
		// Correo Electronico
		JLabel email = new JLabel("Correo Electronico");
		email.setFont(new Font("SansSerif", Font.PLAIN, 12));
		email.setBounds(40, 335, 162, 14);
		RightPanel.add(email);
		
		//Cuadro de texto de los Email
		txtemail = new JTextField();
		txtemail.setText("✉️ name@mail.com");
		txtemail.setForeground(new Color(160, 155, 150)); 
		txtemail.setBackground(Color.WHITE);
		txtemail.setBounds(40, 355, 410, 40); 
		txtemail.setBorder(new CompoundBorder(bordeGrisInput, new EmptyBorder(0, 10, 0, 10)));
		txtemail.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override//cuando Empezamos a escirbir
			public void focusGained(java.awt.event.FocusEvent e) {
				if(txtemail.getText().equals("✉️ name@mail.com")) {
					txtemail.setText("");
					txtemail.setForeground(Color.BLACK);
				}
			}
			
			@Override//En caso que no se haya escrito nada
			public void focusLost(java.awt.event.FocusEvent e) {
				if(txtemail.getText().isEmpty()) {
					txtemail.setText("✉️ name@mail.com");
					txtemail.setForeground(new Color(160, 155, 150));
				}
			}
		});
		RightPanel.add(txtemail);
		
		//Contraseña
		JLabel password = new JLabel("Contraseña");
		password.setFont(new Font("SansSerif", Font.PLAIN, 12));
		password.setBounds(40, 410, 162, 14);
		RightPanel.add(password);
		
		//Cuadro de texto de los Email
	    txtpass = new JTextField();
		txtpass.setText("🔒 ••••••••");
		txtpass.setForeground(new Color(160, 155, 150)); 
		txtpass.setBackground(Color.WHITE);
		txtpass.setBounds(40, 430, 410, 40); 
		txtpass.setBorder(new CompoundBorder(bordeGrisInput, new EmptyBorder(0, 10, 0, 10)));
		txtpass.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override//cuando Empezamos a escirbir
			public void focusGained(java.awt.event.FocusEvent e) {
				if(txtpass.getText().equals("🔒 ••••••••")) {
					txtpass.setText("");
					txtpass.setForeground(Color.BLACK);
				}
			}
			
			@Override//En caso que no se haya escrito nada
			public void focusLost(java.awt.event.FocusEvent e) {
				if(txtpass.getText().isEmpty()) {
					txtpass.setText("🔒 ••••••••");
					txtpass.setForeground(new Color(160, 155, 150));
				}
			}
		});
		RightPanel.add(txtpass);
		
		// Domicilio
		JLabel address = new JLabel("Domicilio");
		address.setFont(new Font("SansSerif", Font.PLAIN, 12));
		address.setBounds(40, 485, 162, 14);
		RightPanel.add(address);
		
		txtadd = new JTextField();
		txtadd.setText("Calle, Numero, Ciudad");
		txtadd.setForeground(new Color(160, 155, 150)); 
		txtadd.setBackground(Color.WHITE);
		txtadd.setBounds(40, 505, 410, 40); 
		txtadd.setBorder(new CompoundBorder(bordeGrisInput, new EmptyBorder(0, 10, 0, 10)));
		txtadd.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override//cuando Empezamos a escirbir
			public void focusGained(java.awt.event.FocusEvent e) {
				if(txtadd.getText().equals("Calle, Numero, Ciudad")) {
					txtadd.setText("");
					txtadd.setForeground(Color.BLACK);
				}
			}
			
			@Override//En caso que no se haya escrito nada
			public void focusLost(java.awt.event.FocusEvent e) {
				if(txtadd.getText().isEmpty()) {
					txtadd.setText("Calle, Numero, Ciudad");
					txtadd.setForeground(new Color(160, 155, 150));
				}
			}
		});
		RightPanel.add(txtadd);
		
		//Boton de login
		Buttons loginbutton = new Buttons("Crear Cuenta", 30, 30, new Color(0, 0, 0), new Color(40, 40, 40));
		loginbutton.setBounds(40, 580, 410, 40); 
		loginbutton.setColorFondo(new java.awt.Color(0, 0, 0)); 
		loginbutton.setForeground(new java.awt.Color(255, 255, 255));
		//bottonListener
		loginbutton.addActionListener(e->{
			String user = txtName.getText().trim();
			String lastname_tx = txtlastname.getText().trim();
			String password_prev = txtpass.getText().trim();
			String number_prev = txtnumber.getText().trim();
			String Email = txtemail.getText().trim();
			String Add = txtadd.getText().trim();
			if(user.isEmpty() || lastname_tx.isEmpty() || password_prev.isEmpty()
					||password_prev.isEmpty() || Email.isEmpty() || Add.isEmpty() 
					||user.equals("👤 Tus Nombres")||lastname_tx.equals("👤 Tus Apellidos")
					||password_prev.equals("🔒 ••••••••")||number_prev.equals(" +1 (5555) 000-0000")
					||Email.equals("✉️ name@mail.com") || Add.equals("Calle, Numero, Ciudad")) {
				System.out.println("Por favor, rellene todos los campos requeridos.");
				//Se pinta de rojo el borde para indicar un error
				txtemail.setBorder(new CompoundBorder(new LineBorder(Color.RED, 1, true), new EmptyBorder(0, 10, 0, 10)));
				txtpass.setBorder(new CompoundBorder(new LineBorder(Color.RED, 1, true), new EmptyBorder(0, 10, 0, 10)));
				txtName.setBorder(new CompoundBorder(new LineBorder(Color.RED, 1, true), new EmptyBorder(0, 10, 0, 10)));
				txtadd.setBorder(new CompoundBorder(new LineBorder(Color.RED, 1, true), new EmptyBorder(0, 10, 0, 10)));
				txtnumber.setBorder(new CompoundBorder(new LineBorder(Color.RED, 1, true), new EmptyBorder(0, 10, 0, 10)));
				txtpass.setBorder(new CompoundBorder(new LineBorder(Color.RED, 1, true), new EmptyBorder(0, 10, 0, 10)));
				txtlastname.setBorder(new CompoundBorder(new LineBorder(Color.RED, 1, true), new EmptyBorder(0, 10, 0, 10)));
			}
			else
			{
				Data_Base DB = Data_Base.getInstance();
				boolean reg_bo = DB.register(user, lastname_tx, number_prev, Email, password_prev, Add);
				if(reg_bo) {
					
					Costumer Previus_Costumer = DB.login(Email, password_prev);
					//Validamos al costumer
					if(Previus_Costumer != null) {
						Restaurant.changeCostumer(Previus_Costumer);
						// Abriimos la pantalla principal
						Main_Frame principal_window = new Main_Frame();
						principal_window.showmainframe(true,Previus_Costumer);
						// Ocultamos la ventana de login actual y liberamos recursos
						frame.dispose(); 
						}else {
					
							System.out.println("Hubo un error;");
							Login_Window back = new Login_Window();
							back.showloginframe(true);
							frame.dispose();
						}
					
				}
				else {
					//Cerramos el programa como precaucion
					frame.dispose(); 
					
				}

			}
			

		});
		RightPanel.add(loginbutton);
		
		//Logo
		URL logoURL = Register_Window.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/LOGO.png");
		if (logoURL != null) {
			ImageIcon LeCartoon_Logo = new ImageIcon(logoURL);
			Image Logo_Scale = LeCartoon_Logo.getImage().getScaledInstance(labelLogo.getWidth(), labelLogo.getHeight(), Image.SCALE_SMOOTH);
			labelLogo.setIcon(new ImageIcon(Logo_Scale));
		}
	}
	
	//Metodo para abrir la ventana
	public void showregframe(boolean visible) {
		if(this.frame!= null) {
			this.frame.setVisible(visible);
		}
	}
}