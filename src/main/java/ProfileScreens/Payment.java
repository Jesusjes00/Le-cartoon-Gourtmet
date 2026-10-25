package ProfileScreens;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Restaurante_Completo.LeCartoon_Original.Costumer;
import ui.theme.Tarjeta;
import ui.theme.Theme;

/**
 * Método de pago guardado (por ahora fijo; t21 lo traerá de la BD).
 * El número se muestra enmascarado: una app nunca debe enseñar la tarjeta completa.
 */
public class Payment extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JLabel name;

	public Payment() {
		setOpaque(false);
		setLayout(new BorderLayout());

		// Tarjeta "física" en negro
		Tarjeta plastico = new Tarjeta(null);
		plastico.setBackground(Theme.TINTA);
		plastico.setLayout(new BoxLayout(plastico, BoxLayout.Y_AXIS));
		plastico.setPreferredSize(new Dimension(440, 250));
		plastico.setMaximumSize(new Dimension(440, 250));
		plastico.setBorder(BorderFactory.createCompoundBorder(plastico.getBorder(), BorderFactory.createEmptyBorder(8, 10, 8, 10)));

		JLabel marca = Theme.etiqueta("VISA", Theme.titulo(26), Theme.BLANCO);
		JLabel numero = Theme.etiqueta("•••• •••• •••• 9403", Theme.mono(26), Theme.BLANCO);
		name = Theme.etiqueta("Nombre + Apellido", Theme.titulo(16), Theme.BLANCO);
		JLabel vence = Theme.etiqueta("05/26", Theme.mono(16), Theme.BLANCO);

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

		for (javax.swing.JComponent c : new javax.swing.JComponent[] { marca, numero, pie }) c.setAlignmentX(LEFT_ALIGNMENT);
		pie.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		plastico.add(marca);
		plastico.add(Box.createVerticalGlue());
		plastico.add(numero);
		plastico.add(Box.createVerticalStrut(18));
		plastico.add(pie);

		JLabel nota = Theme.etiqueta("Es la tarjeta con la que se cobran tus pedidos.", Theme.texto(14), Theme.GRAFITO);

		JPanel contenido = ProfileScreen.columna(18, ProfileScreen.fila(0, plastico), nota);
		add(ProfileScreen.seccion("Método de pago", "card.png", contenido), BorderLayout.CENTER);
	}

	public static void changedCostumer(Costumer costumer) {
		name.setText((costumer.getName() + " " + costumer.getLastName()).toUpperCase());
	}
}
