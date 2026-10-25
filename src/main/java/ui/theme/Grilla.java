package ui.theme;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;
import javax.swing.JViewport;

/**
 * Cuadrícula adaptable: calcula cuántas columnas caben según el ancho disponible
 * (cada celda mide al menos minAncho) y el alto de cada celda sale de la proporción
 * de la imagen. Así las tarjetas se ven igual en 1366, 1536 o 1920 px.
 */
public class Grilla implements LayoutManager {

    private final int minAncho;
    private final double aspecto;    // ancho / alto de la imagen
    private final int extraAlto;     // alto fijo extra (texto debajo de la imagen)
    private final int maxColumnas;
    private final int hueco = 24;

    public Grilla(int minAncho, double aspecto, int extraAlto, int maxColumnas) {
        this.minAncho = minAncho;
        this.aspecto = aspecto;
        this.extraAlto = extraAlto;
        this.maxColumnas = maxColumnas;
    }

    /** Panel transparente con esta grilla, que se vuelve a acomodar cuando cambia su ancho. */
    public static JPanel panel(int minAncho, double aspecto, int extraAlto, int maxColumnas) {
        JPanel p = new JPanel(new Grilla(minAncho, aspecto, extraAlto, maxColumnas)) {
            // En un BoxLayout vertical no debe estirarse más alto de lo que mide su contenido
            @Override public Dimension getMaximumSize() { return new Dimension(Integer.MAX_VALUE, getPreferredSize().height); }
        };
        p.setOpaque(false);
        p.addComponentListener(new ComponentAdapter() {
            private int anchoPrevio = -1;
            @Override public void componentResized(ComponentEvent e) {
                if (p.getWidth() != anchoPrevio) {   // el alto depende del ancho: hay que recalcular
                    anchoPrevio = p.getWidth();
                    p.revalidate();
                }
            }
        });
        return p;
    }

    private int anchoDisponible(Container c) {
        if (c.getWidth() > 0) return c.getWidth();
        for (Container p = c.getParent(); p != null; p = p.getParent()) {   // primera vez: aún no mide nada
            if (p instanceof JViewport && p.getWidth() > 0) return p.getWidth() - 2 * Theme.MARGEN;
        }
        return 1200;
    }

    private int columnas(int ancho, int n) {
        int cols = Math.max(1, (ancho + hueco) / (minAncho + hueco));
        cols = Math.min(cols, Math.min(n, maxColumnas));
        if (cols > 2 && n % cols == 1) cols--;   // evita una tarjeta huérfana en la última fila
        return Math.max(cols, 1);
    }

    private int altoCelda(int anchoCelda) {
        return (int) Math.round((anchoCelda - Theme.SOMBRA) / aspecto) + Theme.SOMBRA + extraAlto;
    }

    @Override
    public Dimension preferredLayoutSize(Container c) {
        Insets in = c.getInsets();
        int n = c.getComponentCount();
        int ancho = anchoDisponible(c) - in.left - in.right;
        if (n == 0) return new Dimension(ancho, 0);
        int cols = columnas(ancho, n);
        int filas = (n + cols - 1) / cols;
        int celda = (ancho - hueco * (cols - 1)) / cols;
        int alto = filas * altoCelda(celda) + hueco * (filas - 1);
        return new Dimension(ancho + in.left + in.right, alto + in.top + in.bottom);
    }

    @Override
    public void layoutContainer(Container c) {
        Insets in = c.getInsets();
        int n = c.getComponentCount();
        if (n == 0) return;
        int ancho = c.getWidth() - in.left - in.right;
        int cols = columnas(ancho, n);
        int celda = (ancho - hueco * (cols - 1)) / cols;
        int alto = altoCelda(celda);
        for (int i = 0; i < n; i++) {
            Component comp = c.getComponent(i);
            int col = i % cols, fila = i / cols;
            comp.setBounds(in.left + col * (celda + hueco), in.top + fila * (alto + hueco), celda, alto);
        }
    }

    @Override public Dimension minimumLayoutSize(Container c) { return new Dimension(minAncho, 0); }
    @Override public void addLayoutComponent(String nombre, Component comp) { }
    @Override public void removeLayoutComponent(Component comp) { }
}
