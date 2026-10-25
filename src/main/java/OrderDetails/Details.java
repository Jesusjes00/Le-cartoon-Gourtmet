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

import Restaurante_Completo.LeCartoon_Original.Buttons;
import Restaurante_Completo.LeCartoon_Original.Delivered;
import Restaurante_Completo.LeCartoon_Original.Main_Frame;
import Restaurante_Completo.LeCartoon_Original.Order;
import Restaurante_Completo.LeCartoon_Original.OrderDetails;
import Restaurante_Completo.LeCartoon_Original.Restaurant;
import persistence.DaoException;
import ui.Toast;
import ui.theme.Lienzo;
import ui.theme.Tarjeta;
import ui.theme.Theme;

/**
 * Seguimiento de un pedido: stepper con las 3 etapas, tarjeta del estado (color, GIF, hora
 * estimada y mensaje, todo viene del patrón State) y la lista de platillos.
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
	private Buttons simState;
	private int anchoGif;                    // el GIF es el protagonista: crece con la pantalla

	public Details(ArrayList<OrderDetails> orderlist, Order order) {
		this.orderlist = orderlist;
		this.order = order;
		initialize();
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

		c.gridx = 0;
		c.weightx = 0.68;
		c.insets = new Insets(0, 0, 0, 28);
		columnas.add(alArriba(crearTarjetaEstado()), c);

		c.gridx = 1;
		c.weightx = 0.32;
		c.insets = new Insets(0, 0, 0, 0);
		columnas.add(alArriba(crearTarjetaPlatillos()), c);
		lienzo.add(columnas, BorderLayout.CENTER);

		add(Theme.scroll(lienzo), BorderLayout.CENTER);
		updateGraphics(order);
	}

	/** Tarjeta del color del estado: GIF a la izquierda; hora, mensaje y botón a la derecha. */
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

		simState = Buttons.secundario("Avanzar estado →");
		simState.setPreferredSize(new Dimension(220, 44 + Theme.SOMBRA));
		simState.setToolTipText("Simula que el restaurante avanza el pedido");
		simState.addActionListener(e -> {
			int anterior = order.getId_state();
			if (anterior + 1 <= 3) {
			    order.setId_state(anterior + 1);
			    try {
			        Restaurant.getInstance().cambiarEstado(order);
			        order.changeState();          // el patrón State avanza solo si se guardó
			        updateGraphics(order);
			        Toast.exito(Details.this, "Pedido actualizado: " + order.actualState());
			    } catch (DaoException ex) {
			        order.setId_state(anterior);  // se regresa: la BD no cambió
			        System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
			        JOptionPane.showMessageDialog(null, "No se pudo guardar el nuevo estado del pedido");
			    }
			} else {
			    Toast.aviso(Details.this, "Este pedido ya fue entregado");
			}
		});

		for (JComponent comp : new JComponent[] { estimacion, hora, textState }) comp.setAlignmentX(LEFT_ALIGNMENT);
		JPanel filaBoton = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		filaBoton.setOpaque(false);
		filaBoton.setAlignmentX(LEFT_ALIGNMENT);
		filaBoton.add(simState);

		textos.add(estimacion);
		textos.add(hora);
		textos.add(Box.createVerticalStrut(12));
		textos.add(textState);
		textos.add(Box.createVerticalStrut(20));
		textos.add(filaBoton);

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

	/** Repinta todo con el color, GIF y mensaje del estado actual (los da el patrón State). */
	public void updateGraphics(Order order) {
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
		simState.setVisible(!entregado);          // ya no hay siguiente estado

		badgeEstado.setText(order.actualState().toUpperCase());
		badgeEstado.setBackground(fondo);
		badgeEstado.setForeground(texto);
		stepper.setEstado(order.getId_state(), fondo);

		revalidate();
		repaint();
	}
}
