package ProfileScreens;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import Restaurante_Completo.LeCartoon_Original.Buttons;
import Restaurante_Completo.LeCartoon_Original.Direccion;
import Restaurante_Completo.LeCartoon_Original.Restaurant;
import persistence.DaoException;
import ui.theme.Theme;

/**
 * Dirección de entrega del cliente, leída de Oracle. El cliente tiene UNA sola:
 * si no tiene, se le invita a agregarla (la necesita para pedir); si tiene, puede editarla.
 */
public class Addresses extends JPanel {

	private static final long serialVersionUID = 1L;

	public Addresses() {
		setOpaque(false);
		setLayout(new BorderLayout());

		JComponent contenido;
		try {
			Direccion d = Restaurant.getInstance().miDireccion();
			contenido = (d == null) ? sinDireccion() : conDireccion(d);
		} catch (DaoException ex) {
			System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
			contenido = ProfileScreen.columna(18,
					Theme.etiqueta("No se pudo cargar tu dirección. Intenta de nuevo.", Theme.texto(15), Theme.LADRILLO));
		}
		add(ProfileScreen.seccion("Mi dirección", "location1.png", contenido), BorderLayout.CENTER);
	}

	private JComponent conDireccion(Direccion d) {
		JLabel nota = Theme.etiqueta("Tus pedidos se entregan aquí.", Theme.texto(14), Theme.GRAFITO);
		Buttons editar = Buttons.secundario("Editar dirección");
		editar.setPreferredSize(new Dimension(200, 46 + Theme.SOMBRA));
		editar.addActionListener(e -> ProfileScreen.changePanel(new EditarDireccion()));
		return ProfileScreen.columna(18, caja(d), nota, ProfileScreen.fila(0, editar));
	}

	private JComponent sinDireccion() {
		JLabel titulo = Theme.etiqueta("Aún no tienes una dirección de entrega", Theme.titulo(22), Theme.TINTA);
		JLabel texto = Theme.etiqueta("La necesitas para hacer tu primer pedido.", Theme.texto(15), Theme.GRAFITO);
		Buttons agregar = Buttons.primario("Agregar dirección");
		agregar.setPreferredSize(new Dimension(210, 46 + Theme.SOMBRA));
		agregar.addActionListener(e -> ProfileScreen.changePanel(new EditarDireccion()));
		return ProfileScreen.columna(12, titulo, texto, ProfileScreen.fila(0, agregar));
	}

	/** Recuadro blanco con borde: ícono + calle grande + resto de la dirección + chip "ENTREGA". */
	private static JPanel caja(Direccion d) {
		JPanel caja = new JPanel(new BorderLayout(14, 0));
		caja.setBackground(Theme.BLANCO);
		caja.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Theme.TINTA, 2),
				BorderFactory.createEmptyBorder(14, 16, 14, 16)));

		JLabel ico = new JLabel(Theme.icono("localizacion.png", 26, Theme.TERRACOTA));
		ico.setVerticalAlignment(SwingConstants.TOP);

		JPanel textos = new JPanel(new BorderLayout(0, 4));
		textos.setOpaque(false);
		textos.add(Theme.etiqueta(d.calleYNumero(), Theme.titulo(20), Theme.TINTA), BorderLayout.NORTH);

		JTextArea resto = new JTextArea("Col. " + d.getColonia() + ", C.P. " + d.getCp() + "\n"
				+ d.getMunicipio() + ", " + d.getCiudad() + ", " + d.getEstado());
		resto.setLineWrap(true);
		resto.setWrapStyleWord(true);
		resto.setEditable(false);
		resto.setFocusable(false);
		resto.setOpaque(false);
		resto.setFont(Theme.texto(16));
		resto.setForeground(Theme.GRAFITO);
		resto.setColumns(30);
		textos.add(resto, BorderLayout.CENTER);

		Buttons chip = Buttons.chip("ENTREGA");
		chip.setPreferredSize(new Dimension(100, 28));
		JPanel der = new JPanel(new BorderLayout());
		der.setOpaque(false);
		der.add(chip, BorderLayout.NORTH);

		caja.add(ico, BorderLayout.WEST);
		caja.add(textos, BorderLayout.CENTER);
		caja.add(der, BorderLayout.EAST);
		return caja;
	}
}
