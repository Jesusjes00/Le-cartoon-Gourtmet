package Restaurante_Completo.LeCartoon_Original;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.EventListenerList;

public class PromoCard extends JPanel {

    private int arc = 30; // 
    private Image imagenPromo;
    private boolean isHovered = false;
    
    // Paleta de colores 
    private Color colorBordeNormal = new Color(255, 255, 255);
    private Color colorBordeHover = new Color(220, 220, 220); //Cambiarle los colores a los bordes 
    private EventListenerList listenerList = new EventListenerList();
    private String actionCommand = "";

    public PromoCard(String rutaImagen, int ancho, int alto) {
        setPreferredSize(new Dimension(ancho, alto));
        setOpaque(false); // esquinas transparentes
        setLayout(null); 

        
        setBorder(new EmptyBorder(5, 5, 5, 5));

        // Cargar la imagen 
        URL imgURL = PromoCard.class.getResource(rutaImagen);
        if (imgURL != null) {
            ImageIcon iconoOriginal = new ImageIcon(imgURL);
            imagenPromo = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        } else {
            System.out.println("No se encontró la imagen promocional en: " + rutaImagen);
        }

        // listener del mouse 
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
            
            @Override
            public void mouseClicked(MouseEvent e) {
                fireActionPerformed();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        //Suavizado de curvas
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int demeritoBorde = 1; 
        int ancho = getWidth() - demeritoBorde;
        int alto = getHeight() - demeritoBorde;

    
        if (imagenPromo != null) {
 
            g2d.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, ancho, alto, arc, arc));
            g2d.drawImage(imagenPromo, 0, 0, getWidth(), getHeight(), this);
            g2d.setClip(null); // Liberamos el recorte
        } else {

            g2d.setColor(new Color(240, 240, 240));
            g2d.fillRoundRect(0, 0, ancho, alto, arc, arc);
        }

 
        if (isHovered) {
            g2d.setColor(colorBordeHover);
            g2d.setStroke(new java.awt.BasicStroke(3f));  
        } else {
            g2d.setColor(colorBordeNormal);
            g2d.setStroke(new java.awt.BasicStroke(1f)); 
        }
        
   
        g2d.drawRoundRect(0, 0, ancho, alto, arc, arc);

        super.paintComponent(g);
    }
    
 
    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }

    public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }

    private void fireActionPerformed() {
        ActionListener[] listeners = listenerList.getListeners(ActionListener.class);
        if (listeners.length == 0) return;
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCommand);
        for (ActionListener l : listeners) {
            l.actionPerformed(event);
        }
    }
}