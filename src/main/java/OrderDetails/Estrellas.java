package OrderDetails;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.IntConsumer;
import javax.swing.JComponent;

import ui.theme.Theme;

/**
 * Cinco estrellas dibujadas a mano. Al pasar el mouse se previsualiza la calificación;
 * con clic se fija. También funciona con el teclado (← →). El valor es un int de 0 a 5
 * (0 = todavía no califica).
 */
public class Estrellas extends JComponent {

    private static final int LADO = 40, HUECO = 10;

    private int valor = 0;            // calificación elegida
    private int hover = 0;            // la que se ve mientras el mouse está encima
    private boolean editable = true;
    private IntConsumer alCambiar = v -> { };

    public Estrellas() {
        setPreferredSize(new Dimension(5 * LADO + 4 * HUECO + 4, LADO + 4));
        setFocusable(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        MouseAdapter m = new MouseAdapter() {
            @Override public void mouseMoved(MouseEvent e)   { if (editable) { hover = estrellaEn(e.getX()); repaint(); } }
            @Override public void mouseExited(MouseEvent e)  { hover = 0; repaint(); }
            @Override public void mouseClicked(MouseEvent e) { if (editable) { setValor(estrellaEn(e.getX())); requestFocusInWindow(); } }
        };
        addMouseListener(m);
        addMouseMotionListener(m);
        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (!editable) return;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) setValor(Math.min(5, valor + 1));
                if (e.getKeyCode() == KeyEvent.VK_LEFT)  setValor(Math.max(1, valor - 1));
            }
        });
    }

    /** Qué estrella (1..5) queda bajo la coordenada x. */
    private int estrellaEn(int x) {
        int i = x / (LADO + HUECO) + 1;
        return Math.max(1, Math.min(5, i));
    }

    public int getValor() { return valor; }

    public void setValor(int v) {
        valor = v;
        alCambiar.accept(v);
        repaint();
    }

    public void setAlCambiar(IntConsumer c) { alCambiar = c; }

    public void setEditable(boolean e) {
        editable = e;
        hover = 0;
        setCursor(e ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
        setFocusable(e);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int mostrar = hover > 0 ? hover : valor;
        for (int i = 0; i < 5; i++) {
            Polygon p = estrella(2 + i * (LADO + HUECO), 2, LADO);
            g.setColor(i < mostrar ? Theme.TERRACOTA : Theme.BLANCO);
            g.fillPolygon(p);
            g.setColor(Theme.TINTA);
            g.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.drawPolygon(p);
        }
        if (isFocusOwner() && editable) {           // aviso de foco para el teclado
            g.setColor(Theme.TERRACOTA);
            g.fillRect(0, getHeight() - 2, getWidth(), 2);
        }
        g.dispose();
    }

    /** Estrella de 5 puntas: alterna radio exterior e interior cada 36°. */
    private static Polygon estrella(int x, int y, int lado) {
        Polygon p = new Polygon();
        double cx = x + lado / 2.0, cy = y + lado / 2.0 + 2;
        double rExt = lado / 2.0, rInt = rExt * 0.45;
        for (int k = 0; k < 10; k++) {
            double r = (k % 2 == 0) ? rExt : rInt;
            double ang = Math.toRadians(-90 + k * 36);
            p.addPoint((int) Math.round(cx + r * Math.cos(ang)), (int) Math.round(cy + r * Math.sin(ang)));
        }
        return p;
    }
}
