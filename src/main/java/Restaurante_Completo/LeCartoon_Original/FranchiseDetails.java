package Restaurante_Completo.LeCartoon_Original;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import Dishes.*;
import Franchises.*;

public class FranchiseDetails extends JPanel {
	
    public FranchiseDetails(Franchises franquicia, ArrayList<Dishes> listaPlatillos, JPanel panelAnterior) {
        setLayout(new BorderLayout());
        Color colorFondo = Color.WHITE;
        try {
            if (franquicia.getColor() != null && !franquicia.getColor().isEmpty()) {
                colorFondo = Color.decode(franquicia.getColor());
            }
        } catch (NumberFormatException e) {
            System.err.println("Error de color, usando blanco por defecto.");
        }

        JPanel pnlContenedorPrincipal = new JPanel(new BorderLayout());
        pnlContenedorPrincipal.setBackground(colorFondo);

        // Boton de regreso
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTop.setBackground(colorFondo);
        
        JLabel lblBack = new JLabel();
        lblBack.setPreferredSize(new Dimension(60, 60));
        lblBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        java.net.URL urlBack = getClass().getResource("/Icons/back.png");
        if (urlBack != null) {
            ImageIcon backIconOriginal = new ImageIcon(urlBack);
            Image backImg = backIconOriginal.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            lblBack.setIcon(new ImageIcon(backImg));
        } else {
            lblBack.setText("<");
            lblBack.setFont(new Font("SansSerif", Font.BOLD, 40));
            lblBack.setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        lblBack.addMouseListener(new MouseAdapter() {
        	@Override
            public void mouseClicked(MouseEvent e) {
        		Main_Frame.changeDynamicPanel(new MainFramePanel());
            }
        });
        pnlTop.add(lblBack);

        // Imagen
        JLabel lblImagenFranquicia = new JLabel();
        lblImagenFranquicia.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagenFranquicia.setOpaque(true);
        lblImagenFranquicia.setBackground(colorFondo);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int anchoBanner = screenSize.width;
        int altoBanner = (int) (anchoBanner * 0.375);

        lblImagenFranquicia.setPreferredSize(new Dimension(anchoBanner, altoBanner));

        ImageIcon iconoFranquicia = cargarIcono(franquicia.getImage(), anchoBanner, altoBanner);
        if (iconoFranquicia != null) {
            lblImagenFranquicia.setIcon(iconoFranquicia);
        } else {
            lblImagenFranquicia.setText("Aquí va la imagen");
            lblImagenFranquicia.setFont(new Font("SansSerif", Font.PLAIN, 24));
        }

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(colorFondo);
        pnlHeader.add(pnlTop, BorderLayout.NORTH);
        pnlHeader.add(lblImagenFranquicia, BorderLayout.CENTER);

        pnlContenedorPrincipal.add(pnlHeader, BorderLayout.NORTH);

        // Titulo y botones
        JPanel pnlContenidoInferior = new JPanel(new BorderLayout());
        pnlContenidoInferior.setOpaque(false);

        // Título Platillos
        JPanel pnlTituloPlatillos = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 20));
        pnlTituloPlatillos.setOpaque(false);

        JLabel lblTextoPlatillos = new JLabel("Platillos:");
        lblTextoPlatillos.setFont(new Font("SansSerif", Font.BOLD, 42));
        lblTextoPlatillos.setForeground(Color.DARK_GRAY);

        pnlTituloPlatillos.add(lblTextoPlatillos);
        pnlContenidoInferior.add(pnlTituloPlatillos, BorderLayout.NORTH);

        JPanel pnlPlatillos = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10)) {
            @Override
            public Dimension getPreferredSize() {
                int parentWidth = 0;
                Container parent = getParent();
                while (parent != null) {
                    if (parent instanceof JViewport) {
                        parentWidth = parent.getWidth();
                        break;
                    }
                    parent = parent.getParent();
                }

                if (parentWidth > 0) {
                    Insets insets = getInsets();
                    int hgap = ((FlowLayout) getLayout()).getHgap();
                    int vgap = ((FlowLayout) getLayout()).getVgap();

                    int x = insets.left + hgap;
                    int y = insets.top + vgap;
                    int rowHeight = 0;

                    for (Component comp : getComponents()) {
                        if (comp.isVisible()) {
                            Dimension d = comp.getPreferredSize();
                            if (x + d.width > parentWidth - insets.right && x > insets.left + hgap) {
                                x = insets.left + hgap;
                                y += rowHeight + vgap;
                                rowHeight = 0;
                            }
                            x += d.width + hgap;
                            rowHeight = Math.max(rowHeight, d.height);
                        }
                    }
                    return new Dimension(parentWidth, y + rowHeight + insets.bottom + vgap);
                }
                return super.getPreferredSize();
            }
        };
        pnlPlatillos.setOpaque(false);

        for (Dishes platillo : listaPlatillos) {
            JPanel pnlTarjeta = new JPanel(new BorderLayout());
            pnlTarjeta.setPreferredSize(new Dimension(306, 210));
            pnlTarjeta.setBackground(Color.WHITE);
            pnlTarjeta.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));

            JLabel lblImgPlatillo = new JLabel();
            int anchoImg = 306;
            int altoImg = 162;
            lblImgPlatillo.setPreferredSize(new Dimension(anchoImg, altoImg));
            lblImgPlatillo.setHorizontalAlignment(SwingConstants.CENTER);
            ImageIcon iconoPlatillo = cargarIcono(platillo.getImage(), anchoImg, altoImg);
            if (iconoPlatillo != null) {
                lblImgPlatillo.setIcon(iconoPlatillo);
            }
            pnlTarjeta.add(lblImgPlatillo, BorderLayout.NORTH);

            JPanel pnlInfoPlatillo = new JPanel(new BorderLayout(10, 0));
            pnlInfoPlatillo.setBackground(Color.WHITE);
            pnlInfoPlatillo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

            JTextArea txtNombre = new JTextArea(platillo.getName());
            txtNombre.setWrapStyleWord(true);
            txtNombre.setLineWrap(true);
            txtNombre.setOpaque(false);
            txtNombre.setEditable(false);
            txtNombre.setFocusable(false);
            txtNombre.setFont(new Font("SansSerif", Font.BOLD, 16));
            txtNombre.setForeground(Color.DARK_GRAY);
            pnlInfoPlatillo.add(txtNombre, BorderLayout.CENTER);

            JLabel lblPrecio = new JLabel("$" + String.format("%.2f", platillo.getPrice()));
            lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 15));
            lblPrecio.setForeground(new Color(200, 50, 50));
            lblPrecio.setVerticalAlignment(SwingConstants.TOP);
            pnlInfoPlatillo.add(lblPrecio, BorderLayout.EAST);

            pnlTarjeta.add(pnlInfoPlatillo, BorderLayout.CENTER);

            pnlTarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));

            MouseAdapter clickAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Se modificó esta línea para pasar FranchiseDetails.this como el panel anterior
                    OrderDetails_2 panelDetalle = new OrderDetails_2(franquicia, platillo, FranchiseDetails.this);
                    Main_Frame.changeDynamicPanel(panelDetalle);
                }
            };

            pnlTarjeta.addMouseListener(clickAdapter);
            lblImgPlatillo.addMouseListener(clickAdapter);
            pnlInfoPlatillo.addMouseListener(clickAdapter);
            txtNombre.addMouseListener(clickAdapter);
            lblPrecio.addMouseListener(clickAdapter);

            pnlPlatillos.add(pnlTarjeta);
        }

        pnlContenidoInferior.add(pnlPlatillos, BorderLayout.CENTER);
        pnlContenedorPrincipal.add(pnlContenidoInferior, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(pnlContenedorPrincipal);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private ImageIcon cargarIcono(String rutaAbsoluta, int ancho, int alto) {
        if (rutaAbsoluta == null || rutaAbsoluta.isEmpty())
            return null;
        try {
            java.net.URL url = getClass().getResource(rutaAbsoluta);
            if (url != null) {
                ImageIcon iconoOriginal = new ImageIcon(url);
                Image imgEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                return new ImageIcon(imgEscalada);
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen: " + rutaAbsoluta);
        }
        return null;
    }
}