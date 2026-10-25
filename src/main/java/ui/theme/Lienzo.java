package ui.theme;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.Scrollable;

/**
 * Panel para meter dentro de un JScrollPane: se estira al ANCHO del scroll
 * (nunca aparece barra horizontal) y solo crece hacia abajo.
 * Sin esto, un JPanel dentro de un scroll conserva su ancho preferido y se corta.
 */
public class Lienzo extends JPanel implements Scrollable {

    public Lienzo(LayoutManager layout) {
        super(layout);
        setBackground(Theme.BLANCO);
    }

    @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
    @Override public int getScrollableUnitIncrement(Rectangle r, int orientacion, int dir) { return 24; }
    @Override public int getScrollableBlockIncrement(Rectangle r, int orientacion, int dir) { return r.height - 48; }
    @Override public boolean getScrollableTracksViewportWidth() { return true; }    // ← la clave
    @Override public boolean getScrollableTracksViewportHeight() {                 // si sobra alto, llena la vista
        return getParent() != null && getPreferredSize().height < getParent().getHeight();
    }
}
