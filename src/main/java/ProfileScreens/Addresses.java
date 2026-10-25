package ProfileScreens;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import Restaurante_Completo.LeCartoon_Original.Buttons;
import ui.theme.Theme;

/** Direcciones de entrega: la del registro (principal) y una guardada de ejemplo. */
public class Addresses extends JPanel {

	private static final long serialVersionUID = 1L;

	public Addresses(String address1) {
		setOpaque(false);
		setLayout(new BorderLayout());

		JPanel lista = ProfileScreen.columna(14,
				direccion(address1, true),
				direccion("Av. Benjamín Franklin 45, Colonia Condesa, Cuauhtémoc, 06140 Ciudad de México, CDMX", false));

		add(ProfileScreen.seccion("Mis direcciones", "location1.png", lista), BorderLayout.CENTER);
	}

	/** Recuadro blanco con borde: ícono + dirección (+ chip "PRINCIPAL"). */
	private static JPanel direccion(String texto, boolean principal) {
		JPanel caja = new JPanel(new BorderLayout(14, 0));
		caja.setBackground(Theme.BLANCO);
		caja.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Theme.TINTA, 2),
				BorderFactory.createEmptyBorder(14, 16, 14, 16)));

		JLabel ico = new JLabel(Theme.icono("localizacion.png", 26, Theme.TERRACOTA));
		ico.setVerticalAlignment(SwingConstants.TOP);

		JTextArea txt = new JTextArea(texto);
		txt.setLineWrap(true);
		txt.setWrapStyleWord(true);
		txt.setEditable(false);
		txt.setFocusable(false);
		txt.setOpaque(false);
		txt.setFont(Theme.titulo(18));
		txt.setForeground(Theme.TINTA);
		txt.setColumns(30);

		caja.add(ico, BorderLayout.WEST);
		caja.add(txt, BorderLayout.CENTER);
		if (principal) {
			Buttons chip = Buttons.chip("PRINCIPAL");
			chip.setPreferredSize(new Dimension(110, 28));
			JPanel der = new JPanel(new BorderLayout());
			der.setOpaque(false);
			der.add(chip, BorderLayout.NORTH);
			caja.add(der, BorderLayout.EAST);
		}
		return caja;
	}
}
