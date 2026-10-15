package ui.theme;

import java.awt.Graphics;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/** Panel neo-brutalista: borde grueso + sombra dura sin desenfoque. */
public class Tarjeta extends JPanel {

    public Tarjeta(LayoutManager layout) {
        super(layout);
        setOpaque(false);                 // las esquinas sin sombra quedan transparentes
        setBackground(Theme.CREMA);
        int p = 12;                       // margen interior
        setBorder(BorderFactory.createEmptyBorder(
            Theme.BORDE + p, Theme.BORDE + p,
            Theme.BORDE + Theme.SOMBRA + p, Theme.BORDE + Theme.SOMBRA + p));
    }

    @Override
    protected void paintComponent(Graphics g) {
        int w = getWidth() - Theme.SOMBRA, h = getHeight() - Theme.SOMBRA;
        g.setColor(Theme.TINTA);
        g.fillRect(Theme.SOMBRA, Theme.SOMBRA, w, h);   // 1. sombra desplazada
        g.fillRect(0, 0, w, h);                         // 2. caja negra (será el borde)
        g.setColor(getBackground());
        g.fillRect(Theme.BORDE, Theme.BORDE,            // 3. interior encima
                   w - 2 * Theme.BORDE, h - 2 * Theme.BORDE);
    }
}