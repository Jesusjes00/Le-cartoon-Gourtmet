package Restaurante_Completo.LeCartoon_Original;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import Dishes.*;
import Franchises.*;
import OrderDetails.Details;
import ProfileScreens.OrderHistory;
import ProfileScreens.ProfileScreen;
import ui.theme.Theme;

/**
 * Ventana principal: header fijo arriba + panel dinámico abajo.
 * El header se acomoda con layouts (no con medidas de 1920 px), así que funciona
 * en cualquier resolución y con el escalado de Windows.
 */
public class Main_Frame {

	private static final int ALTO_HEADER = 72;

	private JFrame frame;
	private static JPanel contentPaneDinamic;
	private int mouseX, mouseY;
	private boolean maximizada = true;

	// Pestañas del header: estáticas para que changeDynamicPanel pueda marcarlas
	private static Buttons btnInicio, btnCocinas, btnPedidos, btnPerfil, btnCarrito;

	/** Cocinas del menú: {nombre visible, clave}. La clave la usa abrirCocina(). */
	private static final String[][] COCINAS = {
		{"Un Show Más",       "Regular"},
		{"Hora de Aventura",  "Adventure"},
		{"Los Simpson",       "Simpson"},
		{"Disney",            "Disney"},
		{"Bob Esponja",       "Sponge"},
	};

	public Main_Frame() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame("Le Cartoon Gourmet");
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(1100, 680));
		maximizar();

		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setBackground(Theme.BLANCO);
		frame.setContentPane(contentPane);

		contentPane.add(crearHeader(), BorderLayout.NORTH);

		contentPaneDinamic = new JPanel(new BorderLayout());
		contentPaneDinamic.setBackground(Theme.BLANCO);
		contentPane.add(contentPaneDinamic, BorderLayout.CENTER);

		changeDynamicPanel(new MainFramePanel());
	}

	// ══════════════ Header ══════════════

	private JPanel crearHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(Theme.TINTA);
		header.setBorder(BorderFactory.createMatteBorder(0, 0, Theme.BORDE, 0, Theme.TERRACOTA));   // línea de acento
		header.setPreferredSize(new Dimension(10, ALTO_HEADER + Theme.BORDE));

		header.add(crearMarca(), BorderLayout.WEST);
		header.add(crearNavegacion(), BorderLayout.CENTER);
		header.add(crearZonaDerecha(), BorderLayout.EAST);

		// Arrastrar la ventana desde el header (solo si no está maximizada)
		// y doble clic para alternar entre maximizada y ventana normal
		MouseAdapter arrastre = new MouseAdapter() {
			@Override public void mousePressed(MouseEvent e) {
				mouseX = e.getXOnScreen() - frame.getX();
				mouseY = e.getYOnScreen() - frame.getY();
			}
			@Override public void mouseDragged(MouseEvent e) {
				if (!maximizada) frame.setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
			}
			@Override public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) alternarTamaño();
			}
		};
		header.addMouseListener(arrastre);
		header.addMouseMotionListener(arrastre);
		return header;
	}

	/** Logo + nombre. Clic = volver al inicio. */
	private JPanel crearMarca() {
		JPanel marca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		marca.setOpaque(false);
		marca.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 24));

		JLabel logo = new JLabel(Theme.recortada("/Restaurante_Completo/LeCartoon_Original/IMAGES/LOGO2.png", 40));
		JLabel nombre = Theme.etiqueta("Le Cartoon Gourmet", Theme.logo(24f), Theme.BLANCO);
		logo.setPreferredSize(new Dimension(logo.getPreferredSize().width, ALTO_HEADER));
		nombre.setPreferredSize(new Dimension(nombre.getPreferredSize().width, ALTO_HEADER));

		MouseAdapter alInicio = new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) { changeDynamicPanel(new MainFramePanel()); }
		};
		logo.addMouseListener(alInicio);
		nombre.addMouseListener(alInicio);
		logo.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
		nombre.setCursor(logo.getCursor());

		marca.add(logo);
		marca.add(nombre);
		return marca;
	}

	/** Pestañas: Inicio · Cocinas ▼ · Mis pedidos · Mi perfil. */
	private JPanel crearNavegacion() {
		JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		nav.setOpaque(false);

		btnInicio  = pestaña("Inicio");
		btnCocinas = pestaña("Cocinas ▼");
		btnPedidos = pestaña("Mis pedidos");
		btnPerfil  = pestaña("Mi perfil");

		btnInicio.addActionListener(e -> changeDynamicPanel(new MainFramePanel()));

		JPopupMenu menuCocinas = crearMenuCocinas();
		btnCocinas.addActionListener(e -> menuCocinas.show(btnCocinas, 0, btnCocinas.getHeight() + Theme.BORDE));

		btnPedidos.addActionListener(e -> irAMisPedidos());

		btnPerfil.addActionListener(e -> changeDynamicPanel(new ProfileScreen()));

		nav.add(btnInicio);
		nav.add(btnCocinas);
		nav.add(btnPedidos);
		nav.add(btnPerfil);
		return nav;
	}

	private Buttons pestaña(String texto) {
		Buttons b = Buttons.nav(texto, Theme.TINTA);
		int ancho = b.getFontMetrics(b.getFont()).stringWidth(texto) + 40;
		b.setPreferredSize(new Dimension(ancho, ALTO_HEADER));
		return b;
	}

	private JPopupMenu crearMenuCocinas() {
		JPopupMenu menu = new JPopupMenu();
		menu.setBorder(Theme.borde());
		for (String[] cocina : COCINAS) {
			String clave = cocina[1];
			JMenuItem item = new JMenuItem(cocina[0]);
			item.setIcon(Theme.imagen(crearFranquicia(clave).getLogo(), 28, 28));
			item.setIconTextGap(12);
			item.setFont(Theme.titulo(15));
			item.setForeground(Theme.TINTA);
			item.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 48));
			item.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
			item.addActionListener(e -> abrirCocina(clave));
			menu.add(item);
		}
		return menu;
	}

	/** Dirección de entrega + Carrito (n) + minimizar/cerrar. */
	private JPanel crearZonaDerecha() {
		JPanel derecha = new JPanel(new GridBagLayout());
		derecha.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;

		// Dirección: etiqueta pequeña arriba + dirección (recortada si es muy larga)
		JPanel entrega = new JPanel();
		entrega.setOpaque(false);
		entrega.setLayout(new BoxLayout(entrega, BoxLayout.Y_AXIS));
		String direccion = Restaurant.getInstance().getCliente().getAddress();
		JLabel lblTitulo = Theme.etiqueta("ENTREGAR EN", Theme.mono(11), Theme.TERRACOTA);
		JLabel lblDireccion = Theme.etiqueta(recortar(direccion, 24), Theme.titulo(14), Theme.BLANCO);
		lblDireccion.setToolTipText(direccion);
		entrega.add(lblTitulo);
		entrega.add(Box.createVerticalStrut(2));
		entrega.add(lblDireccion);
		c.gridx = 0;
		c.insets = new Insets(0, 0, 0, 20);
		derecha.add(entrega, c);

		// Carrito (n): secundario (blanco) para que resalte sobre el negro.
		// No es "acento" porque la terracota se reserva para la acción principal de cada pantalla.
		btnCarrito = Buttons.secundario("Carrito (0)");
		btnCarrito.setPreferredSize(new Dimension(150, 46 + Theme.SOMBRA));
		btnCarrito.addActionListener(e -> changeDynamicPanel(new CartPanel(Carrito.getPedidos())));
		c.gridx = 1;
		c.insets = new Insets(0, 0, 0, 20);
		derecha.add(btnCarrito, c);

		// Controles de ventana: ocupan todo el alto del header
		Buttons btnMinimizar = Buttons.control("—", Theme.TINTA, false);
		btnMinimizar.setToolTipText("Minimizar");
		btnMinimizar.addActionListener(e -> frame.setState(JFrame.ICONIFIED));
		Buttons btnCerrar = Buttons.control("×", Theme.TINTA, true);
		btnCerrar.setFont(Theme.titulo(22));
		btnCerrar.setToolTipText("Cerrar");
		btnCerrar.addActionListener(e -> System.exit(0));
		for (Buttons b : new Buttons[] { btnMinimizar, btnCerrar }) {
			b.setPreferredSize(new Dimension(56, ALTO_HEADER));
		}
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx = 2;
		derecha.add(btnMinimizar, c);
		c.gridx = 3;
		derecha.add(btnCerrar, c);
		return derecha;
	}

	// ══════════════ Ventana ══════════════

	/** Ocupa el área útil de la pantalla (sin tapar la barra de tareas). */
	private void maximizar() {
		Rectangle util = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		frame.setBounds(util);
		maximizada = true;
	}

	private void alternarTamaño() {
		if (maximizada) {
			Rectangle util = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
			int w = Math.max(1100, util.width * 3 / 4), h = Math.max(680, util.height * 3 / 4);
			frame.setBounds(util.x + (util.width - w) / 2, util.y + (util.height - h) / 2, w, h);
			maximizada = false;
		} else {
			maximizar();
		}
		frame.revalidate();
	}

	public void showmainframe(boolean visible, Costumer costumer) {
		if (this.frame != null) {
			this.frame.setVisible(visible);
		}
	}

	// ══════════════ Navegación ══════════════

	public static void changeDynamicPanel(javax.swing.JPanel panel) {
		contentPaneDinamic.removeAll();
		contentPaneDinamic.add(panel, BorderLayout.CENTER);
		contentPaneDinamic.revalidate();
		contentPaneDinamic.repaint();
		marcarSeccion(panel);
		actualizarCarrito();
	}

	/** Perfil abierto directamente en el historial (lo usan la pestaña y el "← Mis pedidos" del detalle). */
	public static void irAMisPedidos() {
		ProfileScreen pfs = new ProfileScreen();
		changeDynamicPanel(pfs);
		ProfileScreen.changePanel(new OrderHistory(ProfileScreen.getOrders()));
		pfs.initOrdersPanel();
		marcar(btnPedidos);
	}

	/** Pone "Carrito (n)" con las unidades actuales. Llamar después de agregar o quitar platillos. */
	public static void actualizarCarrito() {
		if (btnCarrito != null) btnCarrito.setText("Carrito (" + Carrito.totalUnidades() + ")");
	}

	/** Abre la cocina de una franquicia. Único lugar con el catálogo (t22 lo cambiará por DishDao). */
	public static void abrirCocina(String clave) {
		changeDynamicPanel(new FranchiseDetails(crearFranquicia(clave), platillosDe(clave), null));
	}

	// switch clásico con return (no "return switch ... ->"): el compilador de Eclipse (ECJ)
	// generaba bytecode inválido para la expresión switch y la app tronaba con VerifyError al entrar.
	private static Franchises crearFranquicia(String clave) {
		switch (clave) {
			case "Regular":   return new Regular();
			case "Adventure": return new Adventure();
			case "Simpson":   return new Simpson();
			case "Disney":    return new Disney();
			default:          return new Sponge();
		}
	}

	private static ArrayList<Dishes> platillosDe(String clave) {
		ArrayList<Dishes> platos = new ArrayList<>();
		switch (clave) {
			case "Regular" -> {
				platos.add(new Magnificarne()); platos.add(new Sandwich_M());
				platos.add(new Burrito());      platos.add(new Extrahuevordinario());
				platos.add(new Radicola());
			}
			case "Adventure" -> {
				platos.add(new Sandwich_Perfecto()); platos.add(new Hotcakes());
				platos.add(new Pastel_Manzana());    platos.add(new Super_Porp());
			}
			case "Simpson" -> {
				platos.add(new Costiburguer());   platos.add(new Tapa_Arterias());
				platos.add(new Rosquilla_Rosa()); platos.add(new Cerveza_Duff());
				platos.add(new Squishee());
			}
			case "Disney" -> {
				platos.add(new Ratatouille()); platos.add(new Empanadas());
				platos.add(new Buñuelos());    platos.add(new Jugo_Mabel());
			}
			case "Sponge" -> {
				platos.add(new Cangreburguer());      platos.add(new Colorburguer());
				platos.add(new Pizza_Don_Cangrejo()); platos.add(new Limonada());
				platos.add(new Refresco_Algas());
			}
		}
		return platos;
	}

	/** Marca la pestaña que corresponde a la pantalla que se acaba de abrir. */
	private static void marcarSeccion(javax.swing.JPanel panel) {
		if (btnInicio == null) return;
		if (panel instanceof MainFramePanel) marcar(btnInicio);
		else if (panel instanceof FranchiseDetails || panel instanceof OrderDetails_2) marcar(btnCocinas);
		else if (panel instanceof Details) marcar(btnPedidos);
		else if (panel instanceof ProfileScreen) marcar(btnPerfil);
		else marcar(null);   // carrito: ninguna pestaña
	}

	private static void marcar(Buttons activa) {
		for (Buttons b : new Buttons[] { btnInicio, btnCocinas, btnPedidos, btnPerfil }) {
			b.setSeleccionado(b == activa);
		}
	}

	private static String recortar(String s, int max) {
		if (s == null) return "";
		return s.length() <= max ? s : s.substring(0, max - 1) + "…";
	}
}
