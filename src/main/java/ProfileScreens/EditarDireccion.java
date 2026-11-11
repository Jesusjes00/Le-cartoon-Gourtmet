package ProfileScreens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Restaurante_Completo.LeCartoon_Original.Buttons;
import Restaurante_Completo.LeCartoon_Original.Carrito;
import Restaurante_Completo.LeCartoon_Original.CartPanel;
import Restaurante_Completo.LeCartoon_Original.Direccion;
import Restaurante_Completo.LeCartoon_Original.Main_Frame;
import Restaurante_Completo.LeCartoon_Original.Restaurant;
import Restaurante_Completo.LeCartoon_Original.ZonaCobertura;
import persistence.DaoException;
import ui.Toast;
import ui.theme.Theme;

/**
 * Alta o cambio de la dirección de entrega, "CP primero":
 * el cliente escribe su código postal → Oracle regresa municipio, ciudad y las colonias de esa zona →
 * elige su colonia, escribe calle y números. Si el CP no está en CODIGO_POSTAL, no entregamos ahí.
 */
public class EditarDireccion extends JPanel {

	private static final long serialVersionUID = 1L;
	private final boolean volverAlCarrito;           // true si llegó aquí desde "Realizar pedido"

	private final JTextField cpField;
	private final JLabel lugar;                      // "Benito Juárez, CDMX" o el aviso de cobertura
	private final JComboBox<ZonaCobertura.Colonia> coloniaCombo;
	private final JTextField calleField;
	private final JTextField extField;
	private final JTextField intField;

	private ZonaCobertura zona;                      // la del CP escrito (null = aún no hay una válida)
	private String cpConsultado = "";                // para no preguntarle a Oracle dos veces por el mismo CP

	public EditarDireccion() {
		this(false);
	}

	public EditarDireccion(boolean volverAlCarrito) {
		this.volverAlCarrito = volverAlCarrito;
		setOpaque(false);
		setLayout(new BorderLayout());

		cpField    = campo("5 dígitos", 140);
		calleField = campo("Ej. Insurgentes Sur", 420);
		extField   = campo("Ej. 300", 140);
		intField   = campo("Opcional", 140);
		lugar = Theme.etiqueta("Escribe tu código postal", Theme.texto(14), Theme.GRAFITO);

		coloniaCombo = new JComboBox<>();
		coloniaCombo.setFont(Theme.texto(17));
		coloniaCombo.setPreferredSize(new Dimension(420, 44));
		coloniaCombo.setEnabled(false);

		// Cada vez que cambia el CP: si ya son 5 dígitos se consulta la zona
		cpField.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void insertUpdate(DocumentEvent e)  { cpCambio(); }
			@Override public void removeUpdate(DocumentEvent e)  { cpCambio(); }
			@Override public void changedUpdate(DocumentEvent e) { cpCambio(); }
		});

		JPanel form = new JPanel(new GridBagLayout());
		form.setOpaque(false);
		GridBagConstraints g = new GridBagConstraints();
		g.anchor = GridBagConstraints.WEST;
		g.gridx = 0;

		int fila = 0;
		// CP + lugar en la misma fila
		fila = etiqueta(form, g, fila, "CÓDIGO POSTAL", true);
		g.gridy = fila++;
		g.gridwidth = 1;
		g.insets = new Insets(0, 0, 0, 0);
		form.add(cpField, g);
		g.gridx = 1;
		g.insets = new Insets(0, 18, 0, 0);
		form.add(lugar, g);
		g.gridx = 0;

		fila = etiqueta(form, g, fila, "COLONIA", false);
		fila = campoCompleto(form, g, fila, coloniaCombo);
		fila = etiqueta(form, g, fila, "CALLE", false);
		fila = campoCompleto(form, g, fila, calleField);

		// Números en la misma fila
		g.gridy = fila++;
		g.gridwidth = 1;
		g.insets = new Insets(18, 0, 6, 0);
		form.add(Theme.etiqueta("NÚM. EXTERIOR", Theme.mono(12), Theme.GRAFITO), g);
		g.gridx = 1;
		g.insets = new Insets(18, 18, 6, 0);
		form.add(Theme.etiqueta("NÚM. INTERIOR", Theme.mono(12), Theme.GRAFITO), g);
		g.gridy = fila++;
		g.gridx = 0;
		g.insets = new Insets(0, 0, 0, 0);
		form.add(extField, g);
		g.gridx = 1;
		g.insets = new Insets(0, 18, 0, 0);
		form.add(intField, g);
		g.gridx = 0;

		Buttons guardar = Buttons.primario("Guardar dirección");
		guardar.setPreferredSize(new Dimension(210, 46 + Theme.SOMBRA));
		guardar.addActionListener(e -> guardar());
		Buttons cancelar = Buttons.secundario("Cancelar");
		cancelar.setPreferredSize(new Dimension(140, 46 + Theme.SOMBRA));
		cancelar.addActionListener(e -> volver());

		g.gridy = fila;
		g.gridwidth = 2;
		g.insets = new Insets(28, 0, 0, 0);
		form.add(ProfileScreen.fila(14, guardar, cancelar), g);

		// Mismo acomodo que EditProfile: pegado arriba a la izquierda
		JPanel izquierda = new JPanel(new BorderLayout());
		izquierda.setOpaque(false);
		izquierda.add(form, BorderLayout.WEST);
		JPanel arriba = new JPanel(new BorderLayout());
		arriba.setOpaque(false);
		arriba.add(izquierda, BorderLayout.NORTH);

		String titulo = "Agregar dirección";
		try {
			Direccion actual = Restaurant.getInstance().miDireccion();
			if (actual != null) {
				titulo = "Editar dirección";
				precargar(actual);
			}
		} catch (DaoException ex) {
			System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
		}
		add(ProfileScreen.seccion(titulo, "location1.png", arriba), BorderLayout.CENTER);
	}

	// ══════════════ Zona de cobertura ══════════════

	private void cpCambio() {
		String cp = cpField.getText().trim();
		if (cp.equals(cpConsultado)) return;
		cpConsultado = cp;
		zona = null;
		coloniaCombo.removeAllItems();
		coloniaCombo.setEnabled(false);
		marcar(cpField, true);

		if (!cp.matches("\\d{5}")) {
			pintarLugar(cp.isEmpty() ? "Escribe tu código postal" : "Son 5 dígitos", Theme.GRAFITO);
			return;
		}
		try {
			zona = Restaurant.getInstance().zonaDe(cp);
		} catch (DaoException ex) {
			System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
			pintarLugar("No se pudo consultar el código postal", Theme.LADRILLO);
			return;
		}
		if (zona == null) {
			pintarLugar("Aún no entregamos en esa zona", Theme.LADRILLO);
			marcar(cpField, false);
			return;
		}
		for (ZonaCobertura.Colonia c : zona.colonias()) coloniaCombo.addItem(c);
		coloniaCombo.setEnabled(true);
		pintarLugar(zona.lugar(), Theme.TINTA);
	}

	private void pintarLugar(String texto, java.awt.Color color) {
		lugar.setText(texto);
		lugar.setForeground(color);
	}

	/** Al editar: el CP dispara la consulta (DocumentListener) y luego se elige la colonia guardada. */
	private void precargar(Direccion d) {
		cpField.setText(d.getCp());
		if (zona != null) {
			ZonaCobertura.Colonia c = zona.colonia(d.getColonia());
			if (c != null) coloniaCombo.setSelectedItem(c);
		}
		calleField.setText(d.getCalle());
		extField.setText(d.getNumexterior());
		intField.setText(d.getNuminterior() == null ? "" : d.getNuminterior());
	}

	// ══════════════ Guardar ══════════════

	private void guardar() {
		ZonaCobertura.Colonia colonia = (ZonaCobertura.Colonia) coloniaCombo.getSelectedItem();
		boolean cpOk    = zona != null;
		boolean colOk   = colonia != null;
		boolean calleOk = !calleField.getText().isBlank();
		boolean extOk   = !extField.getText().isBlank();
		marcar(cpField, cpOk);
		marcar(coloniaCombo, colOk || !cpOk);       // si el CP está mal, el error es del CP
		marcar(calleField, calleOk);
		marcar(extField, extOk);
		if (!cpOk)    { Toast.aviso(this, "Revisa tu código postal"); return; }
		if (!colOk)   { Toast.aviso(this, "Elige tu colonia"); return; }
		if (!calleOk) { Toast.aviso(this, "Escribe tu calle"); return; }
		if (!extOk)   { Toast.aviso(this, "Escribe tu número exterior"); return; }

		try {
			Restaurant.getInstance().guardarDireccion(colonia, calleField.getText(), extField.getText(), intField.getText());
			Toast.exito(this, volverAlCarrito ? "Listo, ya puedes hacer tu pedido" : "Se guardó tu dirección");
			volver();
		} catch (IllegalArgumentException ex) {          // largo de más, por ejemplo
			Toast.aviso(this, ex.getMessage());
		} catch (DaoException ex) {
			System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
			Toast.aviso(this, "No se pudo guardar tu dirección. Intenta de nuevo.");
		}
	}

	/** Regresa al carrito si venía de ahí (y tiene algo), si no a "Mi dirección". */
	private void volver() {
		if (volverAlCarrito && !Carrito.estaVacio()) {
			Main_Frame.changeDynamicPanel(new CartPanel(Carrito.getPedidos()));
		} else {
			ProfileScreen.changePanel(new Addresses());
		}
	}

	// ══════════════ Helpers de formulario ══════════════

	private static int etiqueta(JPanel form, GridBagConstraints g, int fila, String texto, boolean primero) {
		g.gridy = fila++;
		g.gridwidth = 2;
		g.insets = new Insets(primero ? 0 : 18, 0, 6, 0);
		form.add(Theme.etiqueta(texto, Theme.mono(12), Theme.GRAFITO), g);
		return fila;
	}

	private static int campoCompleto(JPanel form, GridBagConstraints g, int fila, JComponent campo) {
		g.gridy = fila++;
		g.gridwidth = 2;
		g.insets = new Insets(0, 0, 0, 0);
		form.add(campo, g);
		return fila;
	}

	private static JTextField campo(String placeholder, int ancho) {
		JTextField t = new JTextField();
		t.setFont(Theme.texto(17));
		t.putClientProperty("JTextField.placeholderText", placeholder);
		t.setPreferredSize(new Dimension(ancho, 44));
		return t;
	}

	/** Borde ladrillo si está mal (FlatLaf: JComponent.outline = "error"). */
	private static void marcar(JComponent campo, boolean ok) {
		campo.putClientProperty("JComponent.outline", ok ? null : "error");
	}
}
