package Restaurante_Completo.LeCartoon_Original;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Dishes.Dishes;
import Franchises.Franchises;
import ui.theme.Grilla;
import ui.theme.Imagen;
import ui.theme.Lienzo;
import ui.theme.Tarjeta;
import ui.theme.Theme;

/** Cocina de una franquicia: portada, nombre y cuadrícula de platillos. */
public class FranchiseDetails extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Locale MX = Locale.forLanguageTag("es-MX");
	private static final int ALTO_INFO = 104;   // alto del texto debajo de la foto de cada platillo

	public FranchiseDetails(Franchises franquicia, ArrayList<Dishes> listaPlatillos, JPanel panelAnterior) {
		setLayout(new BorderLayout());
		Color fondo = colorDe(franquicia);
		setBackground(fondo);

		Lienzo lienzo = new Lienzo(null);
		lienzo.setLayout(new BoxLayout(lienzo, BoxLayout.Y_AXIS));
		lienzo.setBackground(fondo);
		lienzo.setBorder(BorderFactory.createEmptyBorder(20, Theme.MARGEN, 48, Theme.MARGEN));

		// ── Volver ──
		Buttons btnVolver = Buttons.secundario("← Volver");
		btnVolver.setPreferredSize(new Dimension(140, 42 + Theme.SOMBRA));
		btnVolver.addActionListener(e -> Main_Frame.changeDynamicPanel(new MainFramePanel()));
		JPanel filaVolver = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		filaVolver.setOpaque(false);
		filaVolver.add(btnVolver);
		agregar(lienzo, filaVolver);
		lienzo.add(Box.createVerticalStrut(16));

		// ── Portada: alto fijo, la imagen se recorta (no se deforma ni ocupa media pantalla) ──
		PromoCard portada = new PromoCard(franquicia.getImage(), 1000, 300);
		portada.setInteractiva(false);
		portada.setPreferredSize(new Dimension(1000, 300));
		portada.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
		agregar(lienzo, portada);
		lienzo.add(Box.createVerticalStrut(24));

		// ── Encabezado: logo + nombre + chip con el número de platillos ──
		JPanel encabezado = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
		encabezado.setOpaque(false);
		Imagen logo = new Imagen(franquicia.getLogo(), true);
		logo.setPreferredSize(new Dimension(64, 64));
		JPanel textos = new JPanel();
		textos.setOpaque(false);
		textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
		textos.add(Theme.etiqueta("COCINA", Theme.mono(13), Theme.GRAFITO));
		textos.add(Theme.etiqueta(franquicia.getName(), Theme.titulo(36), Theme.TINTA));
		int n = listaPlatillos.size();
		Buttons chip = Buttons.chip(n + (n == 1 ? " PLATILLO" : " PLATILLOS"));
		chip.setPreferredSize(new Dimension(140, 32));
		encabezado.add(logo);
		encabezado.add(textos);
		encabezado.add(chip);
		agregar(lienzo, encabezado);
		lienzo.add(Box.createVerticalStrut(24));

		// ── Platillos ──
		JPanel grilla = Grilla.panel(250, 1.88, ALTO_INFO + 30, 5);   // fotos de 640×340
		for (Dishes platillo : listaPlatillos) {
			grilla.add(tarjetaPlatillo(franquicia, platillo));
		}
		agregar(lienzo, grilla);

		JScrollPane scroll = Theme.scroll(lienzo);
		scroll.getViewport().setBackground(fondo);
		add(scroll, BorderLayout.CENTER);
	}

	/** Tarjeta blanca: foto arriba, nombre y precio abajo. Toda la tarjeta es clicable. */
	private Tarjeta tarjetaPlatillo(Franchises franquicia, Dishes platillo) {
		Tarjeta card = new Tarjeta(new BorderLayout(0, 12));

		Imagen foto = new Imagen(platillo.getImage(), true);
		card.add(foto, BorderLayout.CENTER);

		JPanel info = new JPanel(new BorderLayout(0, 6));
		info.setOpaque(false);
		info.setPreferredSize(new Dimension(10, ALTO_INFO));

		JTextArea nombre = new JTextArea(platillo.getName());   // JTextArea para que nombres largos bajen de línea
		nombre.setLineWrap(true);
		nombre.setWrapStyleWord(true);
		nombre.setEditable(false);
		nombre.setFocusable(false);
		nombre.setOpaque(false);
		nombre.setBorder(null);
		nombre.setFont(Theme.titulo(17));
		nombre.setForeground(Theme.TINTA);

		JPanel pie = new JPanel(new BorderLayout());
		pie.setOpaque(false);
		pie.add(Theme.etiqueta(String.format(MX, "$%,.2f", platillo.getPrice()), Theme.mono(18), Theme.TINTA), BorderLayout.WEST);
		pie.add(Theme.etiqueta("Ver →", Theme.titulo(14), Theme.TERRACOTA), BorderLayout.EAST);

		info.add(nombre, BorderLayout.CENTER);
		info.add(pie, BorderLayout.SOUTH);
		card.add(info, BorderLayout.SOUTH);

		// Se pasa FranchiseDetails.this como panel anterior: "Volver" regresa a esta misma cocina
		card.hacerClicable(() -> Main_Frame.changeDynamicPanel(new OrderDetails_2(franquicia, platillo, FranchiseDetails.this)));
		return card;
	}

	static Color colorDe(Franchises franquicia) {
		try {
			return Color.decode(franquicia.getColor());
		} catch (Exception e) {
			return Theme.BLANCO;
		}
	}

	private static void agregar(JPanel lienzo, JComponent c) {
		c.setAlignmentX(Component.LEFT_ALIGNMENT);
		if (!(c instanceof JLabel)) c.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
		lienzo.add(c);
	}
}
