package ProfileScreens;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Restaurante_Completo.LeCartoon_Original.Buttons;
import Restaurante_Completo.LeCartoon_Original.Costumer;
import Restaurante_Completo.LeCartoon_Original.MetodoPago;
import Restaurante_Completo.LeCartoon_Original.Restaurant;
import persistence.DaoException;
import ui.theme.Tarjeta;
import ui.theme.Theme;

/**
 * Método de pago del cliente, leído de Oracle (TARJETA_CLIENTE).
 * Tres casos: tiene tarjeta (se dibuja enmascarada), no tiene (invita a agregar una)
 * u Oracle no respondió (aviso). En los dos primeros hay botón a AgregarTarjeta.
 */
public class Payment extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JLabel name;

	public Payment() {
		setOpaque(false);
		setLayout(new BorderLayout());

		Costumer c = Restaurant.getInstance().getCliente();
		name = Theme.etiqueta(titular(c), Theme.titulo(16), Theme.BLANCO);

		JComponent contenido;
		try {
			MetodoPago mp = Restaurant.getInstance().metodoPago();
			contenido = (mp == null) ? sinTarjeta() : conTarjeta(mp);
		} catch (DaoException ex) {
			System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
			contenido = ProfileScreen.columna(18,
					Theme.etiqueta("No se pudo cargar tu método de pago. Intenta de nuevo.", Theme.texto(15), Theme.LADRILLO));
		}
		add(ProfileScreen.seccion("Método de pago", "card.png", contenido), BorderLayout.CENTER);
	}

	// ══════════════ Con tarjeta ══════════════

	private JComponent conTarjeta(MetodoPago mp) {
		// Tarjeta "física" en negro
		Tarjeta plastico = new Tarjeta(null);
		plastico.setBackground(Theme.TINTA);
		plastico.setLayout(new BoxLayout(plastico, BoxLayout.Y_AXIS));
		plastico.setPreferredSize(new Dimension(440, 250));
		plastico.setMaximumSize(new Dimension(440, 250));
		plastico.setBorder(BorderFactory.createCompoundBorder(plastico.getBorder(), BorderFactory.createEmptyBorder(8, 10, 8, 10)));

		JPanel cabeza = new JPanel(new BorderLayout());
		cabeza.setOpaque(false);
		cabeza.add(Theme.etiqueta(mp.getMarca(), Theme.titulo(26), Theme.BLANCO), BorderLayout.WEST);
		if (mp.vencida()) cabeza.add(sello("VENCIDA"), BorderLayout.EAST);

		JLabel numero = Theme.etiqueta(mp.enmascarado(), Theme.mono(26), Theme.BLANCO);
		JLabel vence = Theme.etiqueta(mp.venceTexto(), Theme.mono(16), mp.vencida() ? Theme.TERRACOTA : Theme.BLANCO);

		JPanel pie = new JPanel(new BorderLayout());
		pie.setOpaque(false);
		JPanel titular = new JPanel(new BorderLayout());
		titular.setOpaque(false);
		titular.add(Theme.etiqueta("TITULAR", Theme.mono(11), Theme.TERRACOTA), BorderLayout.NORTH);
		titular.add(name, BorderLayout.CENTER);
		JPanel vencimiento = new JPanel(new BorderLayout());
		vencimiento.setOpaque(false);
		vencimiento.add(Theme.etiqueta("VENCE", Theme.mono(11), Theme.TERRACOTA), BorderLayout.NORTH);
		vencimiento.add(vence, BorderLayout.CENTER);
		pie.add(titular, BorderLayout.WEST);
		pie.add(vencimiento, BorderLayout.EAST);

		for (JComponent comp : new JComponent[] { cabeza, numero, pie }) comp.setAlignmentX(LEFT_ALIGNMENT);
		cabeza.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		pie.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		plastico.add(cabeza);
		plastico.add(Box.createVerticalGlue());
		plastico.add(numero);
		plastico.add(Box.createVerticalStrut(18));
		plastico.add(pie);

		JLabel nota = mp.vencida()
				? Theme.etiqueta("Esta tarjeta ya venció. Agrega una nueva para seguir pidiendo.", Theme.texto(14), Theme.LADRILLO)
				: Theme.etiqueta("Es la tarjeta con la que se cobran tus pedidos.", Theme.texto(14), Theme.GRAFITO);

		Buttons cambiar = mp.vencida() ? Buttons.primario("Agregar tarjeta nueva") : Buttons.secundario("Cambiar tarjeta");
		cambiar.setPreferredSize(new Dimension(mp.vencida() ? 240 : 190, 46 + Theme.SOMBRA));
		cambiar.addActionListener(e -> ProfileScreen.changePanel(new AgregarTarjeta()));

		return ProfileScreen.columna(18, ProfileScreen.fila(0, plastico), nota, ProfileScreen.fila(0, cambiar));
	}

	/** Sello rectangular (ladrillo, texto blanco) para marcar la tarjeta vencida. */
	private static JLabel sello(String texto) {
		JLabel l = Theme.etiqueta(texto, Theme.mono(13), Theme.BLANCO);
		l.setOpaque(true);
		l.setBackground(Theme.LADRILLO);
		l.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Theme.BLANCO, 2),
				BorderFactory.createEmptyBorder(3, 8, 3, 8)));
		return l;
	}

	// ══════════════ Sin tarjeta ══════════════

	private JComponent sinTarjeta() {
		JLabel titulo = Theme.etiqueta("Aún no tienes una tarjeta guardada", Theme.titulo(22), Theme.TINTA);
		JLabel texto = Theme.etiqueta("Agrégala para que tus pedidos se cobren con ella.", Theme.texto(15), Theme.GRAFITO);
		Buttons agregar = Buttons.primario("Agregar tarjeta");
		agregar.setPreferredSize(new Dimension(190, 46 + Theme.SOMBRA));
		agregar.addActionListener(e -> ProfileScreen.changePanel(new AgregarTarjeta()));
		return ProfileScreen.columna(12, titulo, texto, ProfileScreen.fila(0, agregar));
	}

	private static String titular(Costumer c) {
		return c == null ? "" : (c.getName() + " " + c.getLastName()).toUpperCase();
	}

	/** Lo sigue llamando ProfileScreen después de crear el panel. */
	public static void changedCostumer(Costumer costumer) {
		if (name != null) name.setText(titular(costumer));
	}
}
