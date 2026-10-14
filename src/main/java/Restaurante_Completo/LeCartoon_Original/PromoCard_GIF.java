package Restaurante_Completo.LeCartoon_Original;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PromoCard_GIF extends JPanel {

    private int arc = 30; 
    private boolean isHovered = false;
    private JLabel labelMultimedia; // El contenedor real para el GIF animado
    
    // Paletas de colores customizables
    private Color colorBordeNormal = new Color(255, 255, 255);
    private Color colorBordeHover = new Color(220, 220, 220); 

    public PromoCard_GIF(String rutaImagen, int ancho, int alto) {
        setPreferredSize(new Dimension(ancho, alto));
        setOpaque(false); 
        setLayout(new java.awt.BorderLayout(0, 0)); // Usamos BorderLayout para que el label ocupe el 100%
        setBorder(new EmptyBorder(5, 5, 5, 5));

        // 1. Crear el JLabel que reproducirá el GIF de forma nativa
        labelMultimedia = new JLabel();
        labelMultimedia.setHorizontalAlignment(JLabel.CENTER);
        labelMultimedia.setVerticalAlignment(JLabel.CENTER);
        add(labelMultimedia, java.awt.BorderLayout.CENTER);

        // 2. Cargar el recurso sin usar getScaledInstance() para no matar la animación
        URL imgURL = PromoCard.class.getResource(rutaImagen);
        if (imgURL != null) {
            ImageIcon icono = new ImageIcon(imgURL);
            labelMultimedia.setIcon(icono);
        } else {
            System.out.println("No se encontró el recurso multimedia en: " + rutaImagen);
        }

        // 3. Listeners del mouse
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint(); 
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint(); 
            }
        });
    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int demeritoBorde = 1; 
        int ancho = getWidth() - demeritoBorde;
        int alto = getHeight() - demeritoBorde;

        // EL TRUCO CLAVE: Recortamos el lienzo de los componentes hijos (el JLabel con el GIF) 
        // para forzarlos a adoptar la forma elíptica redondeada en tiempo real.
        g2d.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, ancho, alto, arc, arc));
        super.paintChildren(g2d);
        g2d.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int demeritoBorde = 1; 
        int ancho = getWidth() - demeritoBorde;
        int alto = getHeight() - demeritoBorde;

        // Dibujar el contorno interactivo por encima del GIF animado
        if (isHovered) {
            g2d.setColor(colorBordeHover);
            g2d.setStroke(new java.awt.BasicStroke(3f)); 
        } else {
            g2d.setColor(colorBordeNormal);
            g2d.setStroke(new java.awt.BasicStroke(1f)); 
        }
        
        g2d.drawRoundRect(0, 0, ancho, alto, arc, arc);
        g2d.dispose();
    }
    
    public void changeImage(String route) {
    	ImageIcon icono = new ImageIcon(PromoCard.class.getResource(route));
        labelMultimedia.setIcon(icono);
    }
}