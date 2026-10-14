package Restaurante_Completo.LeCartoon_Original;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Dishes.Burrito;
import Dishes.Cangreburguer;
import Dishes.Cerveza_Duff;
import Dishes.Colorburguer;
import Dishes.ComboBikini;
import Dishes.ComboOoo;
import Dishes.ComboParque;
import Dishes.ComboSpringfield;
import Dishes.Costiburguer;
import Dishes.Dishes;
import Dishes.Extrahuevordinario;
import Dishes.Hotcakes;
import Dishes.Limonada;
import Dishes.Magnificarne;
import Dishes.Pastel_Manzana;
import Dishes.Pizza_Don_Cangrejo;
import Dishes.Radicola;
import Dishes.Refresco_Algas;
import Dishes.Rosquilla_Rosa;
import Dishes.Sandwich_M;
import Dishes.Sandwich_Perfecto;
import Dishes.Squishee;
import Dishes.Super_Porp;
import Dishes.Tapa_Arterias;
import Franchises.Adventure;
import Franchises.Regular;
import Franchises.Simpson;
import Franchises.Sponge;

import java.awt.Cursor;

public class MainFramePanel extends JPanel {
	

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public MainFramePanel() {
		setPreferredSize(new Dimension(1920, 1000));
		
		setBackground(new Color(255, 255, 255));
		setLayout(new BorderLayout(0, 0));
		
		//Lienzo con scroll
		
		JPanel panelPromos = new JPanel();
		panelPromos.setBackground(Color.WHITE);
		panelPromos.setLayout(new javax.swing.BoxLayout(panelPromos, javax.swing.BoxLayout.Y_AXIS));
		
		// Bordes del JpanelDinamico
		panelPromos.setBorder(new javax.swing.border.EmptyBorder(35, 10, 0, 15));
		
		// Fila de Promociones(1)
		JPanel filaPromos = new JPanel();
		filaPromos.setBackground(Color.WHITE);
		filaPromos.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 10));
		
		JLabel labelBanner = new JLabel();
		labelBanner.setHorizontalAlignment(SwingConstants.CENTER);
		labelBanner.setPreferredSize(new Dimension(560, 310)); 
		
		URL bannerURL = Main_Frame.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/Sabores_2.png");
		if (bannerURL != null) {
			ImageIcon iconoOriginal = new ImageIcon(bannerURL);
			Image imgEscalada = iconoOriginal.getImage().getScaledInstance(560, 310, Image.SCALE_SMOOTH);
			labelBanner.setIcon(new ImageIcon(imgEscalada));
		}
		
		PromoCard promocombos = new PromoCard("/Restaurante_Completo/LeCartoon_Original/IMAGES/Promo_1.png", 560, 310);
		promocombos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		PromoCard promobogo = new PromoCard("/Restaurante_Completo/LeCartoon_Original/IMAGES/Promo_2.png", 560, 310);
		promobogo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		filaPromos.add(javax.swing.Box.createHorizontalStrut(20));
		filaPromos.add(labelBanner);
		filaPromos.add(javax.swing.Box.createHorizontalStrut(40));
		filaPromos.add(promocombos);
		filaPromos.add(promobogo);
		
		// Fila de Franquicias(2)
		JPanel filaFranquicias = new JPanel();
		filaFranquicias.setBackground(Color.WHITE);
		filaFranquicias.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 10)); // Cambiado a LEFT para simetría
		
		PromoCard franchise = new PromoCard("/Restaurante_Completo/LeCartoon_Original/IMAGES/AdventureTime_Hover.png", 460, 210);
		franchise.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		PromoCard franchise_1 = new PromoCard("/Restaurante_Completo/LeCartoon_Original/IMAGES/UnShowMas_Hover.png", 460, 210);
		franchise_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		PromoCard franchise_2 = new PromoCard("/Restaurante_Completo/LeCartoon_Original/IMAGES/SpongeBob_Hover.png", 460, 210);
		franchise_2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		PromoCard franchise_3 = new PromoCard("/Restaurante_Completo/LeCartoon_Original/IMAGES/LosSimpsons_Hover.jpeg", 460, 210);
		franchise_3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		franchise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Dishes> listaPlatillos_oo = new ArrayList<>();
				listaPlatillos_oo.add(new Sandwich_Perfecto());
				listaPlatillos_oo.add(new Hotcakes());
				listaPlatillos_oo.add(new Pastel_Manzana());
				listaPlatillos_oo.add(new Super_Porp());
				Main_Frame.changeDynamicPanel(new FranchiseDetails(new Adventure(), listaPlatillos_oo,null));
			}
		});

		
		filaFranquicias.add(franchise);
		
		franchise_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Dishes> listaPlatillos_r = new ArrayList<>();
				listaPlatillos_r.add(new Magnificarne());
				listaPlatillos_r.add(new Sandwich_M());
				listaPlatillos_r.add(new Burrito());
				listaPlatillos_r.add(new Extrahuevordinario());
				listaPlatillos_r.add(new Radicola());
				Main_Frame.changeDynamicPanel(new FranchiseDetails(new Regular(), listaPlatillos_r,null));
			}
		});

		
		filaFranquicias.add(franchise_1);
		
		franchise_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Dishes> listaPlatillos_sponge = new ArrayList<>();
				listaPlatillos_sponge.add(new Cangreburguer());
				listaPlatillos_sponge.add(new Colorburguer());
				listaPlatillos_sponge.add(new Pizza_Don_Cangrejo());
				listaPlatillos_sponge.add(new Limonada());
				listaPlatillos_sponge.add(new Refresco_Algas());
				Main_Frame.changeDynamicPanel(new FranchiseDetails(new Sponge(), listaPlatillos_sponge,null));
			}
		});
		
		filaFranquicias.add(franchise_2);
		
		franchise_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Dishes> listaPlatillos_simp = new ArrayList<>();
				listaPlatillos_simp.add(new Costiburguer());
				listaPlatillos_simp.add(new Tapa_Arterias());
				listaPlatillos_simp.add(new Rosquilla_Rosa());
				listaPlatillos_simp.add(new Cerveza_Duff());
				listaPlatillos_simp.add(new Squishee());
				Main_Frame.changeDynamicPanel(new FranchiseDetails(new Simpson(), listaPlatillos_simp,null));
			}
		});
		
		filaFranquicias.add(franchise_3);

		// Fila de Combos(3)
		JPanel filaExtra3 = new JPanel();
		filaExtra3.setBackground(Color.WHITE);
		filaExtra3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 10)); // Cambiado a LEFT
		
		PromoCard combo_0 = new PromoCard("/Restaurante_Completo/LeCartoon_Original/IMAGES/SpongeBob_Combo.png", 460, 210);
		combo_0.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		PromoCard combo_1 = new PromoCard("/Restaurante_Completo/LeCartoon_Original/IMAGES/UnshowMas_Combo.png", 460, 210);
		combo_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		PromoCard combo_2 = new PromoCard("/Restaurante_Completo/LeCartoon_Original/IMAGES/AdventureTime_Combo.png", 460, 210);
		combo_2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		PromoCard combo_3 = new PromoCard("/Restaurante_Completo/LeCartoon_Original/IMAGES/Los Simpson_Combo.png", 460, 210);
		combo_3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		combo_0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Main_Frame.changeDynamicPanel(new OrderDetails_2(new Sponge(),new ComboBikini(),new MainFramePanel()));
			}
		});
		
		filaExtra3.add(combo_0);
		
		combo_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Main_Frame.changeDynamicPanel(new OrderDetails_2(new Regular(),new ComboParque(),new MainFramePanel()));
			}
		});
		
		filaExtra3.add(combo_1);
		
		combo_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Main_Frame.changeDynamicPanel(new OrderDetails_2(new Adventure(),new ComboOoo(),new MainFramePanel()));
			}
		});
		
		filaExtra3.add(combo_2);
		
		combo_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Main_Frame.changeDynamicPanel(new OrderDetails_2(new Simpson(),new ComboSpringfield(),new MainFramePanel()));
			}
		});
		
		filaExtra3.add(combo_3);
		
		// Fila de Mision(4)
		JPanel filaExtra4 = new JPanel();
		filaExtra4.setBackground(Color.WHITE);
		filaExtra4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 10));
		
		JLabel labelmision = new JLabel();
		labelmision.setHorizontalAlignment(SwingConstants.CENTER);
		labelmision.setPreferredSize(new Dimension(660, 410)); 
		
		URL misionURL = Main_Frame.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/Mision.png");
		if (misionURL != null) {
			ImageIcon iconoOriginal = new ImageIcon(misionURL);
			Image imgEscalada = iconoOriginal.getImage().getScaledInstance(660, 410, Image.SCALE_SMOOTH);
			labelmision.setIcon(new ImageIcon(imgEscalada));
		}
		
		JLabel labelogod = new JLabel();
		labelogod.setHorizontalAlignment(SwingConstants.CENTER); // Corregido el target de la inicialización
		labelogod.setPreferredSize(new Dimension(660, 410)); 
		
		URL URLLOGO = Main_Frame.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/LogoD.png");
		if (URLLOGO != null) {
			ImageIcon iconoOriginal = new ImageIcon(URLLOGO);
			Image imgEscalada = iconoOriginal.getImage().getScaledInstance(660, 410, Image.SCALE_SMOOTH);
			labelogod.setIcon(new ImageIcon(imgEscalada));
		}
		
		JLabel labelimagen = new JLabel();
		labelimagen.setHorizontalAlignment(SwingConstants.CENTER);
		labelimagen.setPreferredSize(new Dimension(660, 410)); 
		
		URL imgURL = Main_Frame.class.getResource("/Restaurante_Completo/LeCartoon_Original/IMAGES/Dibujo.png");
		if (imgURL != null) {
			ImageIcon iconoOriginal = new ImageIcon(imgURL);
			Image imgEscalada = iconoOriginal.getImage().getScaledInstance(660, 410, Image.SCALE_SMOOTH);
			labelimagen.setIcon(new ImageIcon(imgEscalada));
		}
		
		filaExtra4.add(labelmision);
		filaExtra4.add(labelimagen);
	


		//LineaS Divisorias------------------------------------------------------->
		
		// Promos
		panelPromos.add(filaPromos);
		
		// Franquicias
		panelPromos.add(crearSeparadorSeccion("FRANQUICIAS FAVORITAS"));
		panelPromos.add(filaFranquicias);
		
		// Combos
		panelPromos.add(crearSeparadorSeccion("COMBOS DE PELÍCULA"));
		panelPromos.add(filaExtra3);
		
		// Mision
		panelPromos.add(crearSeparadorSeccion("NUESTRA MISIÓN"));
		panelPromos.add(filaExtra4);
		
		//Acomodo Final
		panelPromos.add(javax.swing.Box.createVerticalGlue());
		
		// ScrollPane
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(panelPromos) {
		    @Override
		    public javax.swing.JScrollBar createVerticalScrollBar() {
		        javax.swing.JScrollBar bar = super.createVerticalScrollBar();
		        bar.setPreferredSize(new java.awt.Dimension(6, Integer.MAX_VALUE));
		        bar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {

		            @Override
		            protected void configureScrollBarColors() {
		                this.thumbColor= new Color(0, 0, 0); // color del thumb
		                this.trackColor= new Color(245, 245, 245); // color del riel
		                this.thumbDarkShadowColor = new Color(0, 0, 0, 0);
		                this.thumbHighlightColor  = new Color(0, 0, 0, 0);
		                this.thumbLightShadowColor = new Color(0, 0, 0, 0);
		            }

		            // Quitar botones de flecha del riel
		            @Override
		            protected javax.swing.JButton createDecreaseButton(int orientation) {
		                return crearBotonInvisible();
		            }

		            @Override
		            protected javax.swing.JButton createIncreaseButton(int orientation) {
		                return crearBotonInvisible();
		            }

		            // Thumb redondeado
		            @Override
		            protected void paintThumb(Graphics g, javax.swing.JComponent c, java.awt.Rectangle thumbBounds) {
		                if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) return;
		                Graphics2D g2 = (Graphics2D) g.create();
		                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		                g2.setColor(isDragging ? new Color(120, 120, 120) : thumbColor);
		                g2.fillRoundRect(thumbBounds.x + 1, thumbBounds.y + 2,
		                                 thumbBounds.width - 2, thumbBounds.height - 4, 8, 8);
		                g2.dispose();
		            }

		            // riel transparente
		            @Override
		            protected void paintTrack(Graphics g, javax.swing.JComponent c, java.awt.Rectangle trackBounds) {
		                Graphics2D g2 = (Graphics2D) g.create();
		                g2.setColor(trackColor);
		                g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
		                g2.dispose();
		            }

		            private javax.swing.JButton crearBotonInvisible() {
		                javax.swing.JButton btn = new javax.swing.JButton();
		                btn.setPreferredSize(new java.awt.Dimension(0, 0));
		                btn.setMinimumSize(new java.awt.Dimension(0, 0));
		                btn.setMaximumSize(new java.awt.Dimension(0, 0));
		                return btn;
		            }
		        });
		        return bar;
		    }
		};
		//Montamos el thumb y el riel
		scrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane.setBackground(Color.WHITE);
		scrollPane.getViewport().setBackground(Color.WHITE);
		//Todo se monta en el mismo contenedor dinamico
		
		this.add(scrollPane, BorderLayout.CENTER);

	}
	
 // Crear separadores con nombre(lineas divisorias)
 
	private JPanel crearSeparadorSeccion(String titulo) {
		JPanel panelSeparador = new JPanel();
		panelSeparador.setBackground(Color.WHITE);
		
		// Padding(Problemas con los bordes del JPane)
		panelSeparador.setBorder(new EmptyBorder(15, 40, 15, 50)); 
		panelSeparador.setLayout(new java.awt.BorderLayout(20, 0));
		
		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblTitulo.setForeground(new Color(40, 40, 40)); 
		panelSeparador.add(lblTitulo, java.awt.BorderLayout.WEST);
		
		JPanel linea = new JPanel();
		linea.setBackground(new Color(230, 225, 220)); 
		linea.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1)); 
		
		JPanel contenedorLinea = new JPanel(new GridBagLayout());
		contenedorLinea.setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		contenedorLinea.add(linea, gbc);
		
		panelSeparador.add(contenedorLinea, java.awt.BorderLayout.CENTER);
		panelSeparador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		
		return panelSeparador;
	}	

}
