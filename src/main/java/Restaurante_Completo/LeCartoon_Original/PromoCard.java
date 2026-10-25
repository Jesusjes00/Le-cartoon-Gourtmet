package Restaurante_Completo.LeCartoon_Original;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import ui.theme.Imagen;
import ui.theme.Theme;

/**
 * Tarjeta-imagen neo-brutalista: borde de 3 px + sombra dura.
 * La imagen se dibuja en modo "cover" (sin deformarse) al tamaño que le dé el layout.
 * Hover = sombra terracota; clic = se hunde y dispara los ActionListener.
 */
public class PromoCard extends JPanel {

    private static final long serialVersionUID = 1L;

    private final BufferedImage maestra;
    private BufferedImage escalada;
    private boolean hover = false;
    private boolean presionada = false;
    private boolean interactiva = true;
    private final EventListenerList listenerList = new EventListenerList();

    /** ancho y alto ahora son el tamaño PREFERIDO; el layout puede estirarla o encogerla. */
    public PromoCard(String rutaImagen, int ancho, int alto) {
        setPreferredSize(new Dimension(ancho, alto));
        setOpaque(false);                      // las esquinas de la sombra quedan transparentes
        setLayout(null);
        maestra = Imagen.cargar(rutaImagen);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e)  { hover = interactiva; repaint(); }
            @Override public void mouseExited(MouseEvent e)   { hover = false; presionada = false; repaint(); }
            @Override public void mousePressed(MouseEvent e)  { presionada = interactiva; repaint(); }
            @Override public void mouseReleased(MouseEvent e) {
                boolean disparar = presionada && contains(e.getPoint());
                presionada = false;
                repaint();
                if (disparar) fireActionPerformed();
            }
        });
    }

    /** false = solo decorativa (sin hover, sin mano, sin clic). */
    public void setInteractiva(boolean interactiva) {
        this.interactiva = interactiva;
        setCursor(interactiva ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
    }

    @Override
    protected void paintComponent(Graphics g) {
        int s = Theme.SOMBRA, b = Theme.BORDE;
        int off = presionada ? s : 0;                  // presionada: se "hunde" sobre su sombra
        int w = getWidth() - s, h = getHeight() - s;
        if (w <= 2 * b || h <= 2 * b) return;

        if (!presionada) {                             // 1. sombra (terracota si el mouse está encima)
            g.setColor(hover ? Theme.TERRACOTA : Theme.TINTA);
            g.fillRect(s, s, w, h);
        }
        g.setColor(Theme.TINTA);                       // 2. borde
        g.fillRect(off, off, w, h);

        int iw = w - 2 * b, ih = h - 2 * b;            // 3. imagen (o un bloque si no se encontró)
        escalada = Imagen.enCache(maestra, escalada, iw, ih);
        if (escalada != null) {
            g.drawImage(escalada, off + b, off + b, null);
        } else {
            g.setColor(new Color(0xD0D0D0));
            g.fillRect(off + b, off + b, iw, ih);
        }
    }

    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }

    public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }

    private void fireActionPerformed() {
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "");
        for (ActionListener l : listenerList.getListeners(ActionListener.class)) {
            l.actionPerformed(event);
        }
    }
}
