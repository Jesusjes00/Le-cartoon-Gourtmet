package ProfileScreens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import Restaurante_Completo.LeCartoon_Original.Buttons;
import Restaurante_Completo.LeCartoon_Original.Costumer;
import Restaurante_Completo.LeCartoon_Original.Login_Window;
import Restaurante_Completo.LeCartoon_Original.Order;
import Restaurante_Completo.LeCartoon_Original.Restaurant;
import persistence.DaoException;
import ui.theme.Imagen;
import ui.theme.Tarjeta;
import ui.theme.Theme;

/**
 * Perfil del cliente: tarjeta con sus datos arriba, menú a la izquierda y la sección
 * elegida a la derecha. Todo con layouts (antes eran 3 botones encimados con setBounds a 1920 px).
 */
public class ProfileScreen extends JPanel {

	private static final long serialVersionUID = 1L;
	private Buttons infoBtn;
	private Buttons payBtn;
	private Buttons addressBtn;
	private Buttons ordersBtn;
	private static JPanel dynamicPanel;
	private static JLabel name;
	private static Costumer costumer;
	private static ArrayList<Order> orders = new ArrayList<>();
	private static JPanel mainPanel;
	private static ProfileScreen actual;     // para marcar el menú cuando otra pantalla llama a changePanel

	public ProfileScreen() {
		actual = this;
		costumer = Restaurant.getInstance().getCliente();
		try {
		    orders = new ArrayList<>(Restaurant.getInstance().misPedidos());
		} catch (DaoException ex) {
		    System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
		    JOptionPane.showMessageDialog(null, "No se pudieron cargar tus pedidos");
		    orders = new ArrayList<>();
		}

		setBackground(Theme.BLANCO);
		setLayout(new BorderLayout());

		mainPanel = new JPanel(new BorderLayout(0, 24));
		mainPanel.setOpaque(false);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(24, Theme.MARGEN, 24, Theme.MARGEN));
		add(mainPanel, BorderLayout.CENTER);

		mainPanel.add(crearTarjetaCliente(), BorderLayout.NORTH);

		JPanel cuerpo = new JPanel(new BorderLayout(28, 0));
		cuerpo.setOpaque(false);
		cuerpo.add(crearMenu(), BorderLayout.WEST);

		dynamicPanel = new JPanel(new BorderLayout());
		dynamicPanel.setOpaque(false);
		cuerpo.add(dynamicPanel, BorderLayout.CENTER);
		mainPanel.add(cuerpo, BorderLayout.CENTER);

		changePanel(new Personalinfo());
		Personalinfo.costumerChanged(costumer);
	}

	// ══════════════ Tarjeta del cliente (arriba) ══════════════

	private JPanel crearTarjetaCliente() {
		Tarjeta tarjeta = new Tarjeta(new BorderLayout(20, 0));
		tarjeta.setBackground(Theme.TINTA);

		Imagen avatar = new Imagen("/Restaurante_Completo/LeCartoon_Original/IMAGES/pfp.png", true);
		avatar.setPreferredSize(new Dimension(88, 88));
		tarjeta.add(avatar, BorderLayout.WEST);

		JPanel textos = new JPanel();
		textos.setOpaque(false);
		textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
		textos.add(Theme.etiqueta("CLIENTE", Theme.mono(12), Theme.TERRACOTA));
		name = Theme.etiqueta(costumer.getName() + " " + costumer.getLastName(), Theme.titulo(28), Theme.BLANCO);
		textos.add(name);
		textos.add(Box.createVerticalStrut(8));
		Buttons chipPedidos = Buttons.chip(orders.size() + (orders.size() == 1 ? " PEDIDO" : " PEDIDOS"));
		chipPedidos.setPreferredSize(new Dimension(130, 30));
		chipPedidos.setMaximumSize(chipPedidos.getPreferredSize());
		chipPedidos.setAlignmentX(LEFT_ALIGNMENT);
		textos.add(chipPedidos);
		JPanel centro = new JPanel(new java.awt.GridBagLayout());   // centra el bloque de texto en vertical
		centro.setOpaque(false);
		java.awt.GridBagConstraints c = new java.awt.GridBagConstraints();
		c.anchor = java.awt.GridBagConstraints.WEST;
		c.weightx = 1;
		centro.add(textos, c);
		tarjeta.add(centro, BorderLayout.CENTER);

		Buttons editProfile = Buttons.secundario("Editar perfil");
		editProfile.setPreferredSize(new Dimension(170, 44 + Theme.SOMBRA));
		editProfile.addActionListener(e -> {
			changePanel(new EditProfile(costumer));
			resetButtons();
		});
		JPanel derecha = new JPanel(new java.awt.GridBagLayout());
		derecha.setOpaque(false);
		derecha.add(editProfile);
		tarjeta.add(derecha, BorderLayout.EAST);
		return tarjeta;
	}

	// ══════════════ Menú (izquierda) ══════════════

	private JPanel crearMenu() {
		Tarjeta menu = new Tarjeta(new BorderLayout(0, 16));
		menu.setPreferredSize(new Dimension(300, 10));

		JPanel opciones = new JPanel(new GridLayout(0, 1, 0, 6));
		opciones.setOpaque(false);

		infoBtn    = opcion("Información personal", "user.PNG");
		payBtn     = opcion("Métodos de pago",      "card.png");
		addressBtn = opcion("Mis direcciones",      "localizacion.png");
		ordersBtn  = opcion("Historial de pedidos", "pedido.png");

		infoBtn.addActionListener(e -> {
			changePanel(new Personalinfo());
			Personalinfo.costumerChanged(costumer);
		});
		payBtn.addActionListener(e -> {
			changePanel(new Payment());
			Payment.changedCostumer(costumer);
		});
		addressBtn.addActionListener(e -> changePanel(new Addresses(costumer.getAddress())));
		ordersBtn.addActionListener(e -> changePanel(new OrderHistory(orders)));

		JLabel titulo = Theme.etiqueta("CUENTA", Theme.mono(13), Theme.GRAFITO);
		opciones.add(titulo);
		opciones.add(infoBtn);
		opciones.add(payBtn);
		opciones.add(addressBtn);
		opciones.add(ordersBtn);

		JPanel arriba = new JPanel(new BorderLayout());
		arriba.setOpaque(false);
		arriba.add(opciones, BorderLayout.NORTH);
		menu.add(arriba, BorderLayout.CENTER);

		Buttons logOut = Buttons.peligro("Cerrar sesión");
		logOut.setPreferredSize(new Dimension(10, 44 + Theme.SOMBRA));
		logOut.addActionListener(e -> {
			Restaurant.getInstance().cerrarSesion();
			Login_Window log_window = new Login_Window();
			log_window.showloginframe(true);
			Window ventana = SwingUtilities.getWindowAncestor(ProfileScreen.this);
			if (ventana != null) {
				ventana.dispose();
			}
		});
		menu.add(logOut, BorderLayout.SOUTH);
		return menu;
	}

	/** Opción del menú: plana; hover terracota; seleccionada = negro con texto blanco. */
	private Buttons opcion(String texto, String icono) {
		Buttons b = Buttons.nav(texto, Theme.BLANCO);
		b.setHorizontalAlignment(SwingConstants.LEFT);
		b.setFont(Theme.titulo(15));
		b.setIconTextGap(12);
		b.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
		b.setPreferredSize(new Dimension(10, 46));
		b.putClientProperty("icono", icono);
		pintarOpcion(b, false);
		return b;
	}

	private static void pintarOpcion(Buttons b, boolean activa) {
		Color fondo = activa ? Theme.TINTA : Theme.BLANCO;
		Color texto = activa ? Theme.BLANCO : Theme.TINTA;
		b.setColorFondo(fondo);
		b.setForeground(texto);
		b.setIcon(Theme.icono((String) b.getClientProperty("icono"), 20, texto));
	}

	private void seleccionar(Buttons activa) {
		for (Buttons b : new Buttons[] { infoBtn, payBtn, addressBtn, ordersBtn }) {
			pintarOpcion(b, b == activa);
		}
	}

	/** Marca en el menú la opción que corresponde al panel que se está mostrando. */
	private void seleccionarPor(JPanel panel) {
		if (panel instanceof Personalinfo) seleccionar(infoBtn);
		else if (panel instanceof Payment) seleccionar(payBtn);
		else if (panel instanceof Addresses) seleccionar(addressBtn);
		else if (panel instanceof OrderHistory) seleccionar(ordersBtn);
		else seleccionar(null);   // Editar perfil: ninguna
	}

	public void resetButtons() {
		seleccionar(null);
	}

	public void initOrdersPanel() {
		seleccionar(ordersBtn);
	}

	// ══════════════ API que usan otras pantallas ══════════════

	public static void costumerChanged() {
		name.setText(costumer.getName() + " " + costumer.getLastName());
	}

	public static void changePanel(JPanel panel) {
		dynamicPanel.removeAll();
		dynamicPanel.add(panel, BorderLayout.CENTER);
		dynamicPanel.revalidate();
		dynamicPanel.repaint();
		if (actual != null) actual.seleccionarPor(panel);
	}

	public static void orderHistory() {
		changePanel(new OrderHistory(orders));
	}

	/**
	 * Tarjeta blanca de una sección del perfil: título con ícono, línea gruesa y el contenido.
	 * La usan Personalinfo, Payment, Addresses, EditProfile y OrderHistory para verse iguales.
	 */
	static Tarjeta seccion(String titulo, String icono, JComponent contenido) {
		Tarjeta t = new Tarjeta(new BorderLayout(0, 16));
		JLabel lbl = Theme.etiqueta(titulo, Theme.titulo(26), Theme.TINTA);
		if (icono != null) {
			lbl.setIcon(Theme.icono(icono, 26, Theme.TERRACOTA));
			lbl.setIconTextGap(12);
		}
		JPanel encabezado = new JPanel(new BorderLayout(0, 12));
		encabezado.setOpaque(false);
		encabezado.add(lbl, BorderLayout.CENTER);
		JPanel linea = new JPanel();
		linea.setBackground(Theme.TINTA);
		linea.setPreferredSize(new Dimension(10, Theme.BORDE));
		encabezado.add(linea, BorderLayout.SOUTH);
		t.add(encabezado, BorderLayout.NORTH);
		t.add(contenido, BorderLayout.CENTER);
		return t;
	}

	/** Dato de solo lectura: etiqueta pequeña en mono + valor grande. */
	static JPanel dato(String etiqueta, String icono, JLabel valor) {
		JPanel p = new JPanel(new BorderLayout(14, 2));
		p.setOpaque(false);
		JLabel ico = new JLabel(Theme.icono(icono, 24, Theme.TINTA));
		ico.setVerticalAlignment(SwingConstants.TOP);
		ico.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
		JPanel textos = new JPanel(new BorderLayout(0, 2));
		textos.setOpaque(false);
		textos.add(Theme.etiqueta(etiqueta.toUpperCase(), Theme.mono(12), Theme.GRAFITO), BorderLayout.NORTH);
		valor.setFont(Theme.titulo(22));
		valor.setForeground(Theme.TINTA);
		textos.add(valor, BorderLayout.CENTER);
		p.add(ico, BorderLayout.WEST);
		p.add(textos, BorderLayout.CENTER);
		return p;
	}

	/** Columna de componentes alineados arriba (no se estiran a lo alto). */
	static JPanel columna(int separacion, JComponent... filas) {
		JPanel col = new JPanel();
		col.setOpaque(false);
		col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
		for (int i = 0; i < filas.length; i++) {
			filas[i].setAlignmentX(LEFT_ALIGNMENT);
			filas[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, filas[i].getPreferredSize().height));
			col.add(filas[i]);
			if (i < filas.length - 1) col.add(Box.createVerticalStrut(separacion));
		}
		JPanel envoltura = new JPanel(new BorderLayout());
		envoltura.setOpaque(false);
		envoltura.add(col, BorderLayout.NORTH);
		return envoltura;
	}

	static JPanel fila(int hueco, JComponent... comps) {
		JPanel f = new JPanel(new FlowLayout(FlowLayout.LEFT, hueco, 0));
		f.setOpaque(false);
		for (JComponent c : comps) f.add(c);
		return f;
	}

	public static JPanel getDynamicPanel() {
		return dynamicPanel;
	}

	public static void setDynamicPanel(JPanel dynamicPanel) {
		ProfileScreen.dynamicPanel = dynamicPanel;
	}

	public static Costumer getCostumer() {
		return costumer;
	}

	public static void setCostumer(Costumer costumer) {
		ProfileScreen.costumer = costumer;
	}

	public static ArrayList<Order> getOrders() {
		return orders;
	}

	public static void setOrders(ArrayList<Order> orders) {
		ProfileScreen.orders = orders;
	}

	public static JPanel getMainPanel() {
		return mainPanel;
	}

	public static void setMainPanel(JPanel mainPanel) {
		ProfileScreen.mainPanel = mainPanel;
	}
}
