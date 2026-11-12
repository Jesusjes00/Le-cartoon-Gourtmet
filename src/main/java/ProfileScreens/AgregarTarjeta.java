package ProfileScreens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.YearMonth;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Restaurante_Completo.LeCartoon_Original.Buttons;
import Restaurante_Completo.LeCartoon_Original.Carrito;
import Restaurante_Completo.LeCartoon_Original.CartPanel;
import Restaurante_Completo.LeCartoon_Original.Costumer;
import Restaurante_Completo.LeCartoon_Original.Main_Frame;
import Restaurante_Completo.LeCartoon_Original.MetodoPago;
import Restaurante_Completo.LeCartoon_Original.Restaurant;
import persistence.DaoException;
import ui.Toast;
import ui.theme.Theme;

/**
 * Formulario para guardar una tarjeta nueva en TARJETA_CLIENTE.
 * Valida antes de ir a Oracle (Luhn, MM/AA, CVV) y marca en ladrillo el campo que está mal.
 * El CVV va en un JPasswordField: no se muestra en pantalla mientras se escribe.
 */
public class AgregarTarjeta extends JPanel {

	private static final long serialVersionUID = 1L;
	private final JTextField numeroField;
	private final JTextField venceField;
	private final JPasswordField cvvField;
	private final boolean volverAlCarrito;           // true si llegó aquí desde "Realizar pedido"

	public AgregarTarjeta() {
		this(false);
	}

	public AgregarTarjeta(boolean volverAlCarrito) {
		this.volverAlCarrito = volverAlCarrito;
		setOpaque(false);
		setLayout(new BorderLayout());

		numeroField = campo(new JTextField(), "16 dígitos", 420);
		venceField  = campo(new JTextField(), "MM/AA", 140);
		cvvField    = campo(new JPasswordField(), "3 o 4", 140);

		Costumer c = Restaurant.getInstance().getCliente();
		JTextField titular = campo(new JTextField(c == null ? "" : c.getName() + " " + c.getLastName()), "", 420);
		titular.setEnabled(false);      // el titular es el cliente: no se escribe

		JPanel form = new JPanel(new GridBagLayout());
		form.setOpaque(false);
		GridBagConstraints g = new GridBagConstraints();
		g.anchor = GridBagConstraints.WEST;
		g.fill = GridBagConstraints.HORIZONTAL;
		g.gridx = 0;
		g.gridwidth = 2;
		g.weightx = 1;

		int fila = 0;
		fila = agregarCampo(form, g, fila, "NÚMERO DE TARJETA", numeroField, true);
		fila = agregarCampo(form, g, fila, "TITULAR", titular, false);

		// Vence y CVV en la misma fila (como en una tarjeta de verdad)
		g.gridwidth = 1;
		g.fill = GridBagConstraints.NONE;
		g.gridy = fila;
		g.gridx = 0;
		g.insets = new Insets(18, 0, 6, 0);
		form.add(Theme.etiqueta("VENCE", Theme.mono(12), Theme.GRAFITO), g);
		g.gridx = 1;
		g.insets = new Insets(18, 18, 6, 0);
		form.add(Theme.etiqueta("CVV", Theme.mono(12), Theme.GRAFITO), g);
		g.gridy = ++fila;
		g.gridx = 0;
		g.insets = new Insets(0, 0, 0, 0);
		form.add(venceField, g);
		g.gridx = 1;
		g.insets = new Insets(0, 18, 0, 0);
		form.add(cvvField, g);
		fila++;

		Buttons guardar = Buttons.primario("Guardar tarjeta");
		guardar.setPreferredSize(new Dimension(200, 46 + Theme.SOMBRA));
		guardar.addActionListener(e -> guardar());

		Buttons cancelar = Buttons.secundario("Cancelar");
		cancelar.setPreferredSize(new Dimension(140, 46 + Theme.SOMBRA));
		cancelar.addActionListener(e -> volver());

		g.gridx = 0;
		g.gridwidth = 2;
		g.gridy = fila;
		g.insets = new Insets(28, 0, 0, 0);
		form.add(ProfileScreen.fila(14, guardar, cancelar), g);

		// Mismo acomodo que EditProfile: formulario de máximo 560 px, pegado arriba a la izquierda
		JPanel limite = new JPanel(new BorderLayout()) {
			private static final long serialVersionUID = 1L;
			@Override public Dimension getPreferredSize() {
				return new Dimension(Math.min(560, super.getPreferredSize().width), super.getPreferredSize().height);
			}
		};
		limite.setOpaque(false);
		limite.add(form, BorderLayout.CENTER);
		JPanel izquierda = new JPanel(new BorderLayout());
		izquierda.setOpaque(false);
		izquierda.add(limite, BorderLayout.WEST);
		JPanel arriba = new JPanel(new BorderLayout());
		arriba.setOpaque(false);
		arriba.add(izquierda, BorderLayout.NORTH);

		add(ProfileScreen.seccion("Agregar tarjeta", "card.png", arriba), BorderLayout.CENTER);
	}

	private static int agregarCampo(JPanel form, GridBagConstraints g, int fila, String etiqueta, JComponent campo, boolean primero) {
		g.gridy = fila++;
		g.insets = new Insets(primero ? 0 : 18, 0, 6, 0);
		form.add(Theme.etiqueta(etiqueta, Theme.mono(12), Theme.GRAFITO), g);
		g.gridy = fila++;
		g.insets = new Insets(0, 0, 0, 0);
		form.add(campo, g);
		return fila;
	}

	private void guardar() {
		String numero = numeroField.getText();
		YearMonth vence = MetodoPago.leerVence(venceField.getText());
		String cvv = new String(cvvField.getPassword());

		// Se marcan TODOS los campos con error a la vez y el aviso dice el primero
		boolean numOk   = MetodoPago.numeroValido(numero);
		boolean venceOk = vence != null && !vence.isBefore(YearMonth.now());
		boolean cvvOk   = MetodoPago.cvvValido(cvv);
		marcar(numeroField, numOk);
		marcar(venceField, venceOk);
		marcar(cvvField, cvvOk);
		if (!numOk)   { Toast.aviso(this, "Revisa el número de la tarjeta"); return; }
		if (!venceOk) { Toast.aviso(this, vence == null ? "La fecha va como MM/AA" : "Esa tarjeta ya venció"); return; }
		if (!cvvOk)   { Toast.aviso(this, "El CVV son 3 o 4 dígitos"); return; }

		try {
			MetodoPago nueva = Restaurant.getInstance().agregarTarjeta(numero, vence, cvv);
			Toast.exito(this, volverAlCarrito ? "Listo, ya puedes hacer tu pedido" : "Se guardó tu " + nueva.corto());
			volver();
		} catch (IllegalArgumentException ex) {          // no debería pasar: ya se validó arriba
			Toast.aviso(this, ex.getMessage());
		} catch (DaoException ex) {
			System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
			Toast.aviso(this, "No se pudo guardar la tarjeta. Intenta de nuevo.");
		}
	}

	/** Regresa al carrito si venía de ahí (y tiene algo), si no a "Método de pago". */
	private void volver() {
		if (volverAlCarrito && !Carrito.estaVacio()) {
			Main_Frame.changeDynamicPanel(new CartPanel(Carrito.getPedidos()));
			return;
		}
		ProfileScreen.changePanel(new Payment());
		Payment.changedCostumer(Restaurant.getInstance().getCliente());
	}

	/** Campo con el estilo del tema y placeholder de FlatLaf. */
	private static <T extends JTextField> T campo(T t, String placeholder, int ancho) {
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
