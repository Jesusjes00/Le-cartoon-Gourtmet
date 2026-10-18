package Restaurante_Completo.LeCartoon_Original;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import ProfileScreens.OrderHistory;
import ProfileScreens.ProfileScreen;

import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import Dishes.*;
import Franchises.*;

public class Main_Frame {

	private JFrame frame;
	private JTextField find;
	private static JPanel contentPaneDinamic;
	private int mouseX, mouseY;
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main_Frame window = new Main_Frame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 */
	public Main_Frame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(0, 0, 1920, 1080);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		
		//Contenedor Principal
		JPanel contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(new java.awt.BorderLayout(0,0));
		frame.setContentPane(contentPane);
		
		//Barra de Navegacion
		JPanel navbar = new JPanel();
		navbar.setBackground(new Color(0, 0, 0)); 
		navbar.setPreferredSize(new Dimension(1920, 80)); 
		navbar.setBorder(new EmptyBorder(0, 30, 0, 0)); 
		contentPane.add(navbar, java.awt.BorderLayout.NORTH); // Posición fija arriba
		
		GridBagLayout gbl_navbar = new GridBagLayout();
		gbl_navbar.columnWidths = new int[]{150, 250, 450, 200, 42, 42};
		navbar.setLayout(gbl_navbar);

		//Letras del logo
		JLabel logo_letters = new JLabel("Le Cartoon Gourmet");
		logo_letters.setPreferredSize(new Dimension(214, 62));
		logo_letters.setHorizontalAlignment(SwingConstants.LEFT); 

		try {
		    URL fontURL = Main_Frame.class.getResource("/Restaurante_Completo/LeCartoon_Original/FONTS/Baloo2-SemiBold.ttf");
		    if (fontURL != null) {
		        java.awt.Font font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new java.io.File(fontURL.toURI()));
		        font = font.deriveFont(20f);
		        logo_letters.setFont(font);
		        logo_letters.setForeground(new java.awt.Color(255, 255, 255)); 
		    }
		} catch(Exception e) {
		    System.out.println("Error al cargar el font: " + e.getMessage());
		}

		GridBagConstraints gbc_logo = new GridBagConstraints();
		gbc_logo.anchor = GridBagConstraints.WEST; 
		gbc_logo.insets = new Insets(17, 55, 0, 0); 
		gbc_logo.gridx = 0;
		gbc_logo.gridy = 0;
		navbar.add(logo_letters, gbc_logo);
		
		// Imagen del logo
		JLabel labelLogo = new JLabel();
		labelLogo.setHorizontalAlignment(SwingConstants.CENTER);
		labelLogo.setPreferredSize(new Dimension(180, 125));
		
		URL logoURL = Main_Frame.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/LOGO2.png");
		if (logoURL != null) {
			ImageIcon LeCartoon_Logo = new ImageIcon(logoURL);
			Image Logo_Scale = LeCartoon_Logo.getImage().getScaledInstance(
				labelLogo.getPreferredSize().width, 
				labelLogo.getPreferredSize().height, 
				Image.SCALE_SMOOTH
			);
			labelLogo.setIcon(new ImageIcon(Logo_Scale));
		}
		
		GridBagConstraints gbc_logoImagen = new GridBagConstraints();
		gbc_logoImagen.anchor = GridBagConstraints.WEST; 
		gbc_logoImagen.insets = new Insets(-40, -120, 0, 850); 
		gbc_logoImagen.gridx = 0; 
		gbc_logoImagen.gridy = 0; 
		navbar.add(labelLogo, gbc_logoImagen);
		
		//Carrito
		Buttons btnCart = new Buttons("🛒 Cart", 30, 30, Color.BLACK, new Color(15,15,15));
		btnCart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnCart.setForeground(Color.gray);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnCart.setForeground(Color.white);
			}
		});
		btnCart.setPreferredSize(new Dimension(110, 38));
		btnCart.setFont(new Font("SansSerif", Font.BOLD, 15));
		btnCart.setForeground(Color.WHITE);
		GridBagConstraints gbc_cart = new GridBagConstraints();
		gbc_cart.anchor = GridBagConstraints.EAST;
		gbc_cart.weightx = 1.0; 
		gbc_cart.insets = new Insets(0, 0, 0, 15);
		gbc_cart.gridx = 3;
		gbc_cart.gridy = 0;
		btnCart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeDynamicPanel(new CartPanel(Carrito.getPedidos()));
			}
		});
		navbar.add(btnCart, gbc_cart);
		
		//Home
		Buttons Home = new Buttons("Home", 30, 30, Color.BLACK, new Color(15,15,15));
		Home.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				Home.setForeground(Color.gray);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				Home.setForeground(Color.white);
			}
		});
		Home.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeDynamicPanel(new MainFramePanel());
			}
		});
		Home.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		Home.setPreferredSize(new Dimension(110, 38));
		Home.setFont(new Font("SansSerif", Font.BOLD, 15));
		Home.setForeground(Color.WHITE);
		GridBagConstraints gbc_Home = new GridBagConstraints();
		gbc_Home.anchor = GridBagConstraints.EAST;
		gbc_Home.weightx = 1.0; 
		gbc_Home.insets = new Insets(0, 0, 0, 15);
		gbc_Home.gridx = 1;
		gbc_Home.gridy = 0;
		navbar.add(Home, gbc_Home);
		
		// ── Worlds (dropdown de franquicias) ──────────────────────────────
		Buttons worlds = new Buttons("Worlds ▾", 30, 30, Color.BLACK, new Color(15,15,15));
		worlds.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		worlds.setPreferredSize(new Dimension(120, 38));
		worlds.setFont(new Font("SansSerif", Font.BOLD, 15));
		worlds.setForeground(Color.WHITE);

		// ── Definición de franquicias (nombre → panel) ───────────────────
		String[][] franquicias = {
		    {"🍔  Un Show Más",     "Regular"},
		    {"🗡️  Hora de Aventura", "Adventure"},
		    {"🍩  Los Simpsons",    "Simpson"},
		    {"✨  Disney",          "Disney"},
		    {"🧽  Bob Esponja",     "Sponge"},
		};

		// ── Popup minimalista ────────────────────────────────────────────
		JPopupMenu popup = new JPopupMenu() {
		    @Override
		    protected void paintComponent(Graphics g) {
		        Graphics2D g2 = (Graphics2D) g.create();
		        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		        g2.setColor(new Color(18, 18, 18));
		        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
		        g2.dispose();
		    }
		};
		popup.setOpaque(false);
		popup.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
		popup.setBackground(new Color(0, 0, 0, 0));

		for (String[] entry : franquicias) {
		    String etiqueta  = entry[0];
		    String claseKey  = entry[1];

		    JMenuItem item = new JMenuItem(etiqueta) {
		        @Override
		        protected void paintComponent(Graphics g) {
		            Graphics2D g2 = (Graphics2D) g.create();
		            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		            if (getModel().isArmed()) {           // hover
		                g2.setColor(new Color(40, 40, 40));
		                g2.fillRoundRect(6, 0, getWidth() - 12, getHeight(), 8, 8);
		            }
		            g2.dispose();
		            super.paintComponent(g);
		        }
		    };
		    item.setFont(new Font("SansSerif", Font.PLAIN, 14));
		    item.setForeground(Color.WHITE);
		    item.setOpaque(false);
		    item.setBackground(new Color(0, 0, 0, 0));
		    item.setBorder(new EmptyBorder(10, 20, 10, 40));
		    item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		    item.addActionListener(e -> {
		        ArrayList<Dishes> platos = new ArrayList<>();
		        Franchises franquicia   = null;

		        switch (claseKey) {
		            case "Regular":
		                franquicia = new Regular();
		                platos.add(new Magnificarne()); platos.add(new Sandwich_M());
		                platos.add(new Burrito());      platos.add(new Extrahuevordinario());
		                platos.add(new Radicola());
		                break;
		            case "Adventure":
		                franquicia = new Adventure();
		                platos.add(new Sandwich_Perfecto()); platos.add(new Hotcakes());
		                platos.add(new Pastel_Manzana());    platos.add(new Super_Porp());
		                break;
		            case "Simpson":
		                franquicia = new Simpson();
		                platos.add(new Costiburguer()); platos.add(new Tapa_Arterias());
		                platos.add(new Rosquilla_Rosa()); platos.add(new Cerveza_Duff());
		                platos.add(new Squishee());
		                break;
		            case "Disney":
		                franquicia = new Disney();
		                platos.add(new Ratatouille()); platos.add(new Empanadas());
		                platos.add(new Buñuelos());    platos.add(new Jugo_Mabel());
		                break;
		            case "Sponge":
		                franquicia = new Sponge();
		                platos.add(new Cangreburguer()); platos.add(new Colorburguer());
		                platos.add(new Pizza_Don_Cangrejo()); platos.add(new Limonada());
		                platos.add(new Refresco_Algas());
		                break;
		        }
		        changeDynamicPanel(new FranchiseDetails(franquicia, platos, null));
		    });

		    popup.add(item);

		    // separador fino entre items (excepto después del último)
		    if (!etiqueta.equals(franquicias[franquicias.length - 1][0])) {
		        JSeparator sep = new JSeparator();
		        sep.setForeground(new Color(45, 45, 45));
		        sep.setBackground(new Color(45, 45, 45));
		        popup.add(sep);
		    }
		}

		// Abrir popup alineado debajo del botón
		worlds.addMouseListener(new MouseAdapter() {
		    @Override public void mouseEntered(MouseEvent e) { worlds.setForeground(Color.GRAY); }
		    @Override public void mouseExited(MouseEvent e)  { worlds.setForeground(Color.WHITE); }
		    @Override public void mouseClicked(MouseEvent e) {
		        popup.show(worlds, 0, worlds.getHeight() + 4);
		    }
		});

		GridBagConstraints gbc_worlds = new GridBagConstraints();
		gbc_worlds.anchor = GridBagConstraints.EAST;
		gbc_worlds.weightx = 1.0;
		gbc_worlds.insets = new Insets(0, 0, 0, -80);
		gbc_worlds.gridx = 1;
		gbc_worlds.gridy = 0;
		navbar.add(worlds, gbc_worlds);
		
		
		
		//Orders
		Buttons orders = new Buttons("Ordenes", 30, 30, Color.BLACK, new Color(15,15,15));
		orders.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				orders.setForeground(Color.gray);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				orders.setForeground(Color.white);
			}
		});
		orders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProfileScreen pfs = new ProfileScreen();
				changeDynamicPanel(pfs);
				ProfileScreen.changePanel(new OrderHistory(ProfileScreen.getOrders()));
				pfs.resetButtons();
				pfs.initOrdersPanel();
			}
		});
		orders.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		orders.setPreferredSize(new Dimension(110, 38));
		orders.setFont(new Font("SansSerif", Font.BOLD, 15));
		orders.setForeground(Color.WHITE);
		GridBagConstraints gbc_orders = new GridBagConstraints();
		gbc_orders.anchor = GridBagConstraints.EAST;
		gbc_orders.weightx = 1.0; 
		gbc_orders.insets = new Insets(0, 0, 0,-175);
		gbc_orders.gridx = 1;
		gbc_orders.gridy = 0;
		navbar.add(orders, gbc_orders);
		
		//Direcciones
		Buttons address = new Buttons(Restaurant.getInstance().getCliente().getAddress(), 30, 30, new Color(60,60,60), new Color(90,90,90));
		address.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		address.setPreferredSize(new Dimension(110, 38));
		address.setFont(new Font("SansSerif", Font.BOLD, 12));
		address.setForeground(Color.WHITE);
		GridBagConstraints gbc_address = new GridBagConstraints();
		gbc_address.anchor = GridBagConstraints.EAST;
		gbc_address.weightx = 1.0; 
		gbc_address.insets = new Insets(2, 0, 0,-25);
		gbc_address.gridx = 2;
		gbc_address.gridy = 0;
		navbar.add(address, gbc_address);
		
		// ── Botón Minimizar 
		Buttons btnMinimize = new Buttons("—", 0, 0, Color.BLACK, new Color(70, 70, 70));
		btnMinimize.setPreferredSize(new Dimension(42, 80)); //navbar completa
		btnMinimize.setFont(new Font("SansSerif", Font.BOLD, 14));
		btnMinimize.setForeground(new Color(180, 180, 180));
		btnMinimize.addMouseListener(new MouseAdapter() {
		    @Override public void mouseEntered(MouseEvent e) {
		        btnMinimize.setForeground(Color.WHITE);
		    }
		    @Override public void mouseExited(MouseEvent e) {
		        btnMinimize.setForeground(new Color(180, 180, 180));
		    }
		});
		btnMinimize.addActionListener(e -> frame.setState(JFrame.ICONIFIED));

		GridBagConstraints gbc_min = new GridBagConstraints();
		gbc_min.anchor = GridBagConstraints.EAST;
		gbc_min.fill   = GridBagConstraints.VERTICAL;
		gbc_min.insets = new Insets(0, 0, 0, 0); // sin margen
		gbc_min.gridx  = 4;
		gbc_min.gridy  = 0;
		navbar.add(btnMinimize, gbc_min);

		// ── Botón Cerrar 
		Buttons btnClose = new Buttons("✕", 0, 0, Color.BLACK, new Color(200, 40, 40));
		btnClose.setPreferredSize(new Dimension(42, 80)); 
		btnClose.setFont(new Font("SansSerif", Font.BOLD, 14));
		btnClose.setForeground(new Color(180, 180, 180));
		btnClose.addMouseListener(new MouseAdapter() {
		    @Override public void mouseEntered(MouseEvent e) {
		        btnClose.setForeground(Color.WHITE);
		    }
		    @Override public void mouseExited(MouseEvent e) {
		        btnClose.setForeground(new Color(180, 180, 180));
		    }
		});
		btnClose.addActionListener(e -> System.exit(0));

		GridBagConstraints gbc_close = new GridBagConstraints();
		gbc_close.anchor = GridBagConstraints.EAST;
		gbc_close.fill   = GridBagConstraints.VERTICAL; // ocupa toda la altura de la fila
		gbc_close.insets = new Insets(0, 0, 0, 0);      // sin margen
		gbc_close.gridx  = 5;                           // columna 5, distinta de minimize
		gbc_close.gridy  = 0;
		navbar.add(btnClose, gbc_close);
		
		// ── Arrastre de ventana desde la navbar------------
		navbar.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mousePressed(MouseEvent e) {
		        // Solo permitir arrastre si NO está maximizada
		        if (frame.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
		            mouseX = e.getXOnScreen() - frame.getX();
		            mouseY = e.getYOnScreen() - frame.getY();
		        }
		    }
		});
		navbar.addMouseMotionListener(new MouseAdapter() {
		    @Override
		    public void mouseDragged(MouseEvent e) {
		        if (frame.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
		            frame.setLocation(
		                e.getXOnScreen() - mouseX,
		                e.getYOnScreen() - mouseY
		            );
		        }
		    }
		});
		
		
		//Panedinamico
		contentPaneDinamic = new JPanel();
		contentPaneDinamic.setBackground(Color.WHITE);
		contentPaneDinamic.setLayout(new java.awt.BorderLayout(0,0));
		contentPane.add(contentPaneDinamic,java.awt.BorderLayout.CENTER);
		
		changeDynamicPanel(new MainFramePanel());
	}
	
	//Metodo para abrir la ventana
	public void showmainframe(boolean visible,Costumer costumer) {
		if(this.frame!= null) {
			this.frame.setVisible(visible);
		
		}
	}
	
	public static void changeDynamicPanel(JPanel panel) {			
		contentPaneDinamic.removeAll();
		contentPaneDinamic.add(panel, BorderLayout.CENTER);
		contentPaneDinamic.revalidate();
		contentPaneDinamic.repaint();
	}
}