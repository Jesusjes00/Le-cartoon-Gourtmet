package OrderDetails;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import Restaurante_Completo.LeCartoon_Original.Buttons;
import Restaurante_Completo.LeCartoon_Original.Delivered;
import Restaurante_Completo.LeCartoon_Original.Main_Frame;
import Restaurante_Completo.LeCartoon_Original.Order;
import Restaurante_Completo.LeCartoon_Original.OrderDetails;
import Restaurante_Completo.LeCartoon_Original.Resena;
import Restaurante_Completo.LeCartoon_Original.Restaurant;
import persistence.DaoException;
import ui.PanelSwipe;
import ui.Toast;
import ui.theme.Lienzo;
import ui.theme.Tarjeta;
import ui.theme.Theme;

/**
 * Seguimiento de un pedido: stepper con las 3 etapas, tarjeta del estado (color, GIF, hora
 * estimada y mensaje, todo viene del patrón State) y la lista de platillos.
 * El cliente ya no avanza su pedido: lo avanza el vendedor desde su página. Cuando la app
 * detecta el cambio (al abrir el pedido o con el vigilante de Main_Frame), la tarjeta se
 * desliza al estado nuevo con avanzarA().
 */
public class Details extends JPanel {
	private ArrayList<OrderDetails> orderlist;
	private Order order;

	private static final long serialVersionUID = 1L;
	private static final Locale MX = Locale.forLanguageTag("es-MX");

	private Stepper stepper;
	private JLabel badgeEstado;
	private Tarjeta tarjetaEstado;
	private JLabel gif;
	private JLabel estimacion;
	private JLabel hora;
	private JTextArea textState;
	private PanelSwipe swipe;                // envuelve la tarjeta de estado para animar el cambio
	private int destino;                     // estado al que hay que llegar (el que tiene la BD)
	private boolean animando;
	private int anchoGif;                    // el GIF es el protagonista: crece con la pantalla
	private JPanel huecoResena;              // debajo de la tarjeta de estado; se llena al estar Entregado

	public Details(ArrayList<OrderDetails> orderlist, Order order) {
		this.orderlist = orderlist;
		int real = order.getId_state();
		int visto = estadoVisto(order);
		// Si avanzó mientras el cliente no lo veía, la pantalla arranca donde lo dejó...
		this.order = visto < real
				? new Order(order.getId(), order.getId_costumer(), order.getTotalPrice(), visto)
				: order;
		this.destino = this.order.getId_state();
		initialize();
		// ...y un momento después se desliza hasta el estado real
		if (visto < real) {
			Timer espera = new Timer(700, e -> avanzarA(real));
			espera.setRepeats(false);
			espera.start();
		}
	}

	public void initialize() {
		setLayout(new BorderLayout());
		setBackground(Theme.BLANCO);

		Lienzo lienzo = new Lienzo(new BorderLayout(0, 24));
		lienzo.setBorder(BorderFactory.createEmptyBorder(20, Theme.MARGEN, 32, Theme.MARGEN));

		// ── Arriba: volver + título + estado + stepper ──
		JPanel arriba = new JPanel();
		arriba.setOpaque(false);
		arriba.setLayout(new BoxLayout(arriba, BoxLayout.Y_AXIS));

		Buttons returnBtn = Buttons.secundario("← Mis pedidos");
		returnBtn.setPreferredSize(new Dimension(170, 42 + Theme.SOMBRA));
		returnBtn.addActionListener(e -> Main_Frame.irAMisPedidos());

		badgeEstado = new JLabel("", SwingConstants.CENTER);
		badgeEstado.setOpaque(true);
		badgeEstado.setFont(Theme.mono(14));
		badgeEstado.setBorder(BorderFactory.createLineBorder(Theme.TINTA, 2));
		badgeEstado.setPreferredSize(new Dimension(140, 32));

		JPanel filaTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		filaTitulo.setOpaque(false);
		filaTitulo.add(returnBtn);
		filaTitulo.add(Box.createHorizontalStrut(24));
		filaTitulo.add(Theme.etiqueta("Pedido #" + order.getId(), Theme.titulo(30), Theme.TINTA));
		filaTitulo.add(Box.createHorizontalStrut(16));
		filaTitulo.add(badgeEstado);
		agregar(arriba, filaTitulo);
		arriba.add(Box.createVerticalStrut(20));

		stepper = new Stepper();
		JPanel marcoStepper = new JPanel(new BorderLayout());
		marcoStepper.setOpaque(false);
		stepper.setPreferredSize(new Dimension(640, 80));
		marcoStepper.add(stepper, BorderLayout.WEST);
		agregar(arriba, marcoStepper);
		lienzo.add(arriba, BorderLayout.NORTH);

		// ── Dos columnas: estado | platillos ──
		JPanel columnas = new JPanel(new GridBagLayout());
		columnas.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1;

		// Columna izquierda: tarjeta de estado y, abajo, la reseña (solo si ya se entregó)
		JPanel izquierda = new JPanel();
		izquierda.setOpaque(false);
		izquierda.setLayout(new BoxLayout(izquierda, BoxLayout.Y_AXIS));
		swipe = new PanelSwipe(crearTarjetaEstado());
		JComponent estado = swipe;
		huecoResena = new JPanel(new BorderLayout());
		huecoResena.setOpaque(false);
		estado.setAlignmentX(LEFT_ALIGNMENT);
		huecoResena.setAlignmentX(LEFT_ALIGNMENT);
		izquierda.add(estado);
		izquierda.add(huecoResena);

		c.gridx = 0;
		c.weightx = 0.68;
		c.insets = new Insets(0, 0, 0, 28);
		columnas.add(alArriba(izquierda), c);

		c.gridx = 1;
		c.weightx = 0.32;
		c.insets = new Insets(0, 0, 0, 0);
		columnas.add(alArriba(crearTarjetaPlatillos()), c);
		lienzo.add(columnas, BorderLayout.CENTER);

		add(Theme.scroll(lienzo), BorderLayout.CENTER);
		updateGraphics(order);
	}

	/** Tarjeta del color del estado: GIF a la izquierda; hora y mensaje a la derecha. */
	private JComponent crearTarjetaEstado() {
		tarjetaEstado = new Tarjeta(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;

		int pantalla = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
		anchoGif = pantalla >= 1800 ? 700 : pantalla >= 1500 ? 600 : 520;
		gif = new JLabel();
		gif.setBorder(BorderFactory.createLineBorder(Theme.TINTA, Theme.BORDE));
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(4, 4, 4, 24);
		tarjetaEstado.add(gif, c);

		JPanel textos = new JPanel();
		textos.setOpaque(false);
		textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

		estimacion = Theme.etiqueta("", Theme.mono(13), Theme.TINTA);
		String horaFormateada = LocalTime.now().plusMinutes(30).format(DateTimeFormatter.ofPattern("HH:mm"));
		hora = Theme.etiqueta(horaFormateada, Theme.mono(56), Theme.TINTA);

		textState = new JTextArea();
		textState.setLineWrap(true);
		textState.setWrapStyleWord(true);
		textState.setEditable(false);
		textState.setFocusable(false);
		textState.setOpaque(false);
		textState.setFont(Theme.titulo(22));
		textState.setColumns(16);

		for (JComponent comp : new JComponent[] { estimacion, hora, textState }) comp.setAlignmentX(LEFT_ALIGNMENT);
		textos.add(estimacion);
		textos.add(hora);
		textos.add(Box.createVerticalStrut(12));
		textos.add(textState);

		c.gridx = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(4, 0, 4, 4);
		tarjetaEstado.add(textos, c);
		return tarjetaEstado;
	}

	/** Lista de platillos del pedido + total. */
	private JComponent crearTarjetaPlatillos() {
		Tarjeta t = new Tarjeta(null);
		t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));

		agregar(t, Theme.etiqueta("Detalles del pedido", Theme.titulo(22), Theme.TINTA));
		t.add(Box.createVerticalStrut(8));
		for (OrderDetails od : orderlist) {
			agregar(t, new DetailsPanelOD(od, t));
		}
		t.add(Box.createVerticalStrut(16));

		JPanel total = new JPanel(new BorderLayout());
		total.setOpaque(false);
		total.add(Theme.etiqueta("TOTAL", Theme.mono(14), Theme.GRAFITO), BorderLayout.WEST);
		total.add(Theme.etiqueta(String.format(MX, "$%,.2f", order.getTotalPrice()), Theme.mono(22), Theme.TINTA), BorderLayout.EAST);
		agregar(t, total);
		return t;
	}

	private static JPanel alArriba(JComponent c) {
		JPanel p = new JPanel(new BorderLayout());
		p.setOpaque(false);
		p.add(c, BorderLayout.NORTH);
		return p;
	}

	private static void agregar(JPanel destino, JComponent c) {
		c.setAlignmentX(LEFT_ALIGNMENT);
		c.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
		destino.add(c);
	}

	public ArrayList<OrderDetails> getOrderlist() {
		return orderlist;
	}

	public void setOrderlist(ArrayList<OrderDetails> orderlist) {
		this.orderlist = orderlist;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	/** Solo un pedido entregado se puede calificar. La tarjeta aparece debajo del estado. */
	private void mostrarResena(boolean entregado) {
		if (!entregado || huecoResena.getComponentCount() > 0) return;
		Resena guardada = null;
		try {
			guardada = Restaurant.getInstance().resenaDe(order.getId());   // la reseña vive en PEDIDO (Oracle)
		} catch (DaoException ex) {
			System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
		}
		ResenaPanel panel = new ResenaPanel(guardada, r -> {
			try {
				Restaurant.getInstance().calificar(order.getId(), r);
				Toast.exito(Details.this, "¡Gracias! Tu reseña de " + r.getEstrellas() + (r.getEstrellas() == 1 ? " estrella" : " estrellas") + " se envió");
				return true;                     // la tarjeta pasa a modo lectura
			} catch (DaoException ex) {
				System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
				JOptionPane.showMessageDialog(null, "No se pudo guardar tu reseña. Intenta de nuevo.");
				return false;                    // la tarjeta sigue editable
			}
		});
		huecoResena.setBorder(BorderFactory.createEmptyBorder(24, 0, 0, 0));
		huecoResena.add(panel, BorderLayout.CENTER);
	}

	/** Repinta todo con el color, GIF y mensaje del estado actual (los da el patrón State). */
	public void updateGraphics(Order order) {
		pintarEstado(order, false);
	}

	private void pintarEstado(Order order, boolean animarStepper) {
		Color fondo = order.getColor();
		Color texto = Theme.textoSobre(fondo);

		tarjetaEstado.setBackground(fondo);
		estimacion.setForeground(texto);
		hora.setForeground(texto);
		textState.setForeground(texto);
		textState.setText(order.message());
		ImageIcon original = new ImageIcon(Details.class.getResource(order.getImage()));
		int altoGif = original.getIconHeight() * anchoGif / original.getIconWidth();
		// SCALE_DEFAULT (no SMOOTH): SMOOTH convierte el GIF en una imagen fija y deja de moverse
		gif.setIcon(new ImageIcon(original.getImage().getScaledInstance(anchoGif, altoGif, java.awt.Image.SCALE_DEFAULT)));

		boolean entregado = order.getState() instanceof Delivered;
		estimacion.setText(entregado ? "PEDIDO ENTREGADO" : "HORA ESTIMADA DE ENTREGA");
		mostrarResena(entregado);

		badgeEstado.setText(order.actualState().toUpperCase());
		badgeEstado.setBackground(fondo);
		badgeEstado.setForeground(texto);
		if (animarStepper) stepper.animarA(order.getId_state(), fondo, PanelSwipe.DURACION_MS);
		else stepper.setEstado(order.getId_state(), fondo);

		revalidate();
		repaint();
	}

	// ── Detección del cambio de estado ──

	/**
	 * Lleva la pantalla, paso por paso y con animación, hasta el estado que ya tiene la BD.
	 * La llaman el constructor (cambio que pasó mientras el cliente no veía) y el vigilante
	 * de Main_Frame (cambio en vivo). Si ya va en camino a ese estado, no hace nada.
	 */
	public void avanzarA(int nuevoEstado) {
		if (!animando && nuevoEstado <= order.getId_state()) return;   // ya se muestra (aviso repetido)
		if (nuevoEstado > destino) destino = Math.min(nuevoEstado, 3);
		if (!animando) siguientePaso();
	}

	private void siguientePaso() {
		if (order.getId_state() >= destino) {               // llegó: se marca como visto
			animando = false;
			try {
				Restaurant.getInstance().marcarVisto(order.getId(), order.getId_state());
			} catch (IllegalStateException sinSesion) {
				return;
			}
			Toast.exito(this, "Pedido #" + order.getId() + ": " + order.actualState());
			Main_Frame.revisarPedidos();                    // actualiza "Mis pedidos (n)"
			return;
		}
		animando = true;
		swipe.animar(() -> {
			order.setId_state(order.getId_state() + 1);
			order.changeState();                            // patrón State: Preparing → Sent → Delivered
			pintarEstado(order, true);
		}, this::siguientePaso);
	}

	/** Último estado que el cliente vio de este pedido (si no hay sesión, el actual). */
	private static int estadoVisto(Order o) {
		try {
			return Restaurant.getInstance().estadoVisto(o);
		} catch (IllegalStateException sinSesion) {
			return o.getId_state();
		}
	}
}
