package Restaurante_Completo.LeCartoon_Original;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import Dishes.ComboBikini;
import Dishes.ComboOoo;
import Dishes.ComboParque;
import Dishes.ComboSpringfield;
import Dishes.Dishes;
import Franchises.Adventure;
import Franchises.Franchises;
import Franchises.Regular;
import Franchises.Simpson;
import Franchises.Sponge;
import ui.theme.Grilla;
import ui.theme.Lienzo;
import ui.theme.Theme;

/** Pantalla de inicio: saludo, promociones, cocinas, combos y misión. Se adapta al ancho. */
public class MainFramePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final String IMG = "/Restaurante_Completo/LeCartoon_Original/IMAGES/";
	private static final double PROPORCION = 1.79;   // las imágenes miden ~2752×1536

	public MainFramePanel() {
		setLayout(new BorderLayout());
		setBackground(Theme.BLANCO);

		Lienzo lienzo = new Lienzo(null);
		lienzo.setLayout(new BoxLayout(lienzo, BoxLayout.Y_AXIS));
		lienzo.setBorder(new EmptyBorder(28, Theme.MARGEN, 48, Theme.MARGEN));

		// ── Saludo ──
		Costumer cliente = Restaurant.getInstance().getCliente();
		String nombre = (cliente != null) ? cliente.getName().split(" ")[0] : "";
		JLabel saludo = Theme.etiqueta("Hola, " + nombre + ". ¿Qué se te antoja hoy?", Theme.titulo(30), Theme.TINTA);
		JLabel subtitulo = Theme.etiqueta("Platillos de tus caricaturas favoritas, hechos de verdad.", Theme.texto(16), Theme.GRAFITO);
		agregar(lienzo, saludo);
		lienzo.add(Box.createVerticalStrut(4));
		agregar(lienzo, subtitulo);
		lienzo.add(Box.createVerticalStrut(24));

		// ── Promociones (3 columnas) ──
		JPanel promos = Grilla.panel(300, PROPORCION, 0, 3);
		PromoCard banner = new PromoCard(IMG + "Sabores_2.png", 560, 310);
		banner.setInteractiva(false);                              // es el eslogan, no lleva a ningún lado
		PromoCard promoCombos = new PromoCard(IMG + "Promo_1.png", 560, 310);
		PromoCard promoBogo   = new PromoCard(IMG + "Promo_2.png", 560, 310);
		promos.add(banner);
		promos.add(promoCombos);
		promos.add(promoBogo);
		agregar(lienzo, promos);

		// ── Cocinas ──
		lienzo.add(Box.createVerticalStrut(40));
		agregar(lienzo, Theme.seccion("Franquicias favoritas"));
		lienzo.add(Box.createVerticalStrut(16));
		JPanel cocinas = Grilla.panel(260, PROPORCION, 0, 4);
		cocinas.add(cocina("AdventureTime_Hover.png", "Adventure"));
		cocinas.add(cocina("UnShowMas_Hover.png",     "Regular"));
		cocinas.add(cocina("SpongeBob_Hover.png",     "Sponge"));
		cocinas.add(cocina("LosSimpsons_Hover.jpeg",  "Simpson"));
		agregar(lienzo, cocinas);

		// ── Combos ──
		lienzo.add(Box.createVerticalStrut(40));
		JPanel tituloCombos = Theme.seccion("Combos de película");
		agregar(lienzo, tituloCombos);
		lienzo.add(Box.createVerticalStrut(16));
		JPanel combos = Grilla.panel(260, PROPORCION, 0, 4);
		combos.add(combo("SpongeBob_Combo.png",   new Sponge(),    new ComboBikini()));
		combos.add(combo("UnshowMas_Combo.png",   new Regular(),   new ComboParque()));
		combos.add(combo("AdventureTime_Combo.png", new Adventure(), new ComboOoo()));
		combos.add(combo("Los Simpson_Combo.png", new Simpson(),   new ComboSpringfield()));
		agregar(lienzo, combos);

		// ── Misión ──
		lienzo.add(Box.createVerticalStrut(40));
		agregar(lienzo, Theme.seccion("Nuestra misión"));
		lienzo.add(Box.createVerticalStrut(16));
		JPanel mision = Grilla.panel(380, PROPORCION, 0, 2);
		PromoCard imgMision = new PromoCard(IMG + "Mision.png", 660, 368);
		PromoCard imgDibujo = new PromoCard(IMG + "Dibujo.png", 660, 368);
		imgMision.setInteractiva(false);
		imgDibujo.setInteractiva(false);
		mision.add(imgMision);
		mision.add(imgDibujo);
		agregar(lienzo, mision);

		JScrollPane scroll = Theme.scroll(lienzo);
		add(scroll, BorderLayout.CENTER);

		// Las promos llevan a algún lado (antes no hacían nada al darles clic)
		promoCombos.addActionListener(e -> SwingUtilities.invokeLater(() ->
				lienzo.scrollRectToVisible(new java.awt.Rectangle(0, tituloCombos.getY(), 10, scroll.getViewport().getHeight()))));
		promoBogo.addActionListener(e -> Main_Frame.changeDynamicPanel(
				new OrderDetails_2(new Simpson(), new ComboSpringfield(), new MainFramePanel())));
	}

	private PromoCard cocina(String imagen, String clave) {
		PromoCard card = new PromoCard(IMG + imagen, 460, 257);
		card.addActionListener(e -> Main_Frame.abrirCocina(clave));
		return card;
	}

	private PromoCard combo(String imagen, Franchises franquicia, Dishes platillo) {
		PromoCard card = new PromoCard(IMG + imagen, 460, 257);
		card.addActionListener(e -> Main_Frame.changeDynamicPanel(new OrderDetails_2(franquicia, platillo, new MainFramePanel())));
		return card;
	}

	/** BoxLayout respeta el tamaño máximo; así cada fila se estira a lo ancho y se alinea a la izquierda. */
	private static void agregar(JPanel lienzo, JComponent c) {
		c.setAlignmentX(Component.LEFT_ALIGNMENT);
		if (!(c instanceof JLabel)) c.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
		lienzo.add(c);
	}
}
