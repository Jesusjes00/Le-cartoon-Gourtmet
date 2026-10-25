package OrderDetails;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

import ui.theme.Theme;

/**
 * Barra de progreso del pedido: Preparando ── Enviado ── Entregado (t19).
 * Pasos ya cumplidos = cuadro relleno con ✓; paso actual = relleno con su número;
 * pasos que faltan = cuadro vacío. Se dibuja a mano con Graphics2D.
 */
public class Stepper extends JComponent {

    private static final String[] PASOS = { "Preparando", "Enviado", "Entregado" };
    private static final int LADO = 44;     // tamaño del cuadro de cada paso

    private int actual = 1;                 // 1, 2 o 3 (igual que id_state)
    private Color color = Theme.TERRACOTA;

    public Stepper() {
        setPreferredSize(new Dimension(600, LADO + 34));
    }

    public void setEstado(int idEstado, Color colorEstado) {
        actual = idEstado;
        color = colorEstado;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int n = PASOS.length;
        int margen = 50;                                        // deja espacio para centrar las etiquetas
        int paso = (getWidth() - 2 * margen) / (n - 1);         // distancia entre centros
        int y = 0;

        // 1. Líneas entre pasos (gruesas y rellenas si ese tramo ya se recorrió)
        for (int i = 0; i < n - 1; i++) {
            int x1 = margen + i * paso + LADO / 2, x2 = margen + (i + 1) * paso - LADO / 2;
            g.setColor(Theme.TINTA);
            g.fillRect(x1, y + LADO / 2 - 4, x2 - x1, 8);
            if (i + 1 < actual) {
                g.setColor(color);
                g.fillRect(x1, y + LADO / 2 - 2, x2 - x1, 4);
            } else {
                g.setColor(Theme.BLANCO);
                g.fillRect(x1, y + LADO / 2 - 2, x2 - x1, 4);
            }
        }

        // 2. Cuadros y etiquetas
        for (int i = 0; i < n; i++) {
            int num = i + 1;
            int cx = margen + i * paso, x = cx - LADO / 2;
            boolean hecho = num < actual || (num == actual && actual == n);
            boolean activo = num == actual;

            g.setColor(Theme.TINTA);
            g.fillRect(x + (activo ? 4 : 0), y + (activo ? 4 : 0), LADO, LADO);      // sombra dura en el actual
            g.fillRect(x, y, LADO, LADO);                                           // borde
            g.setColor(num <= actual ? color : Theme.BLANCO);
            g.fillRect(x + Theme.BORDE, y + Theme.BORDE, LADO - 2 * Theme.BORDE, LADO - 2 * Theme.BORDE);

            String marca = hecho ? "✓" : String.valueOf(num);
            g.setFont(Theme.mono(18));
            g.setColor(num <= actual ? Theme.textoSobre(color) : Theme.TINTA);
            FontMetrics fm = g.getFontMetrics();
            g.drawString(marca, cx - fm.stringWidth(marca) / 2, y + (LADO + fm.getAscent()) / 2 - 3);

            g.setFont(activo ? Theme.titulo(15) : Theme.texto(15));
            g.setColor(num <= actual ? Theme.TINTA : Theme.GRAFITO);
            fm = g.getFontMetrics();
            g.drawString(PASOS[i], cx - fm.stringWidth(PASOS[i]) / 2, y + LADO + 24);
        }
        g.dispose();
    }
}
