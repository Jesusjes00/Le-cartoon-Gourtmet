package ui.theme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/** Panel neo-brutalista: borde grueso + sombra dura sin desenfoque. */
public class Tarjeta extends JPanel {

    private Color colorSombra = Theme.TINTA;
    private boolean hundida = false;          // mientras se presiona una tarjeta clicable

    public Tarjeta(LayoutManager layout) {
        super(layout);
        setOpaque(false);                 // las esquinas sin sombra quedan transparentes
        setBackground(Theme.BLANCO);
        int p = 12;                       // margen interior
        setBorder(BorderFactory.createEmptyBorder(
            Theme.BORDE + p, Theme.BORDE + p,
            Theme.BORDE + Theme.SOMBRA + p, Theme.BORDE + Theme.SOMBRA + p));
    }

    @Override
    protected void paintComponent(Graphics g) {
        int w = getWidth() - Theme.SOMBRA, h = getHeight() - Theme.SOMBRA;
        if (!hundida) {
            g.setColor(colorSombra);
            g.fillRect(Theme.SOMBRA, Theme.SOMBRA, w, h);   // 1. sombra desplazada
        }
        g.setColor(Theme.TINTA);
        g.fillRect(0, 0, w, h);                         // 2. caja negra (será el borde)
        g.setColor(getBackground());
        g.fillRect(Theme.BORDE, Theme.BORDE,            // 3. interior encima
                   w - 2 * Theme.BORDE, h - 2 * Theme.BORDE);
    }

    public void setColorSombra(Color c) { colorSombra = c; repaint(); }

    /**
     * Convierte la tarjeta en botón: mano al pasar, sombra terracota en hover
     * (la misma señal que las PromoCard) y la acción al hacer clic.
     * Se registra también en los hijos para que el clic sobre la imagen o el texto cuente.
     */
    public void hacerClicable(Runnable accion) {
        MouseAdapter m = new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { setColorSombra(Theme.TERRACOTA); }
            @Override public void mouseExited(MouseEvent e) {
                if (!contiene(e)) { setColorSombra(Theme.TINTA); hundida = false; repaint(); }
            }
            @Override public void mousePressed(MouseEvent e) { hundida = true; repaint(); }
            @Override public void mouseReleased(MouseEvent e) {
                hundida = false; repaint();
                if (contiene(e)) accion.run();
            }
        };
        registrar(this, m);
    }

    private boolean contiene(MouseEvent e) {   // ¿el mouse sigue dentro de la tarjeta (no solo del hijo)?
        java.awt.Point p = javax.swing.SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this);
        return p.x >= 0 && p.y >= 0 && p.x < getWidth() && p.y < getHeight();
    }

    private static void registrar(Component c, MouseAdapter m) {
        c.addMouseListener(m);
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (c instanceof Container k) for (Component hijo : k.getComponents()) registrar(hijo, m);
    }
}
