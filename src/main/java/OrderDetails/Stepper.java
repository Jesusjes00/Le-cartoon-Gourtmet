package OrderDetails;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.Timer;

import ui.theme.Theme;

/**
 * Barra de progreso del pedido: Preparando ── Enviado ── Entregado (t19).
 * Pasos ya cumplidos = cuadro relleno con ✓; paso actual = relleno con su número;
 * pasos que faltan = cuadro vacío. Se dibuja a mano con Graphics2D.
 * Con animarA() la barra se va llenando hasta el paso nuevo y el cuadro se pinta al llegar.
 */
public class Stepper extends JComponent {

    private static final String[] PASOS = { "Preparando", "Enviado", "Entregado" };
    private static final int LADO = 44;     // tamaño del cuadro de cada paso

    private int actual = 1;                 // 1, 2 o 3 (igual que id_state)
    private float avance = 1f;              // posición dibujada de la barra (1.0 … 3.0); se anima
    private Color color = Theme.TERRACOTA;
    private Timer reloj;

    public Stepper() {
        setPreferredSize(new Dimension(600, LADO + 34));
    }

    /** Cambia de estado de golpe (al abrir la pantalla). */
    public void setEstado(int idEstado, Color colorEstado) {
        detener();
        actual = idEstado;
        avance = idEstado;
        color = colorEstado;
        repaint();
    }

    /** Llena la barra poco a poco hasta el estado nuevo (mismo ease-out que PanelSwipe). */
    public void animarA(int idEstado, Color colorEstado, int duracionMs) {
        detener();
        float desde = avance;
        actual = idEstado;
        color = colorEstado;
        long inicio = System.currentTimeMillis();
        reloj = new Timer(15, e -> {
            float x = Math.min(1f, (System.currentTimeMillis() - inicio) / (float) duracionMs);
            avance = desde + (idEstado - desde) * (1f - (float) Math.pow(1 - x, 3));
            if (x >= 1f) {
                avance = idEstado;
                detener();
            }
            repaint();
        });
        reloj.start();
    }

    private void detener() {
        if (reloj != null) reloj.stop();
        reloj = null;
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int n = PASOS.length;
        int margen = 50;                                        // deja espacio para centrar las etiquetas
        int paso = (getWidth() - 2 * margen) / (n - 1);         // distancia entre centros
        int y = 0;
        int alcanzado = (int) Math.floor(avance + 0.001f);      // pasos a los que ya llegó la barra

        // 1. Líneas entre pasos (gruesas y rellenas si ese tramo ya se recorrió)
        for (int i = 0; i < n - 1; i++) {
            int x1 = margen + i * paso + LADO / 2, x2 = margen + (i + 1) * paso - LADO / 2;
            g.setColor(Theme.TINTA);
            g.fillRect(x1, y + LADO / 2 - 4, x2 - x1, 8);
            g.setColor(Theme.BLANCO);
            g.fillRect(x1, y + LADO / 2 - 2, x2 - x1, 4);
            float lleno = Math.max(0f, Math.min(1f, avance - (i + 1)));  // 0 = vacío, 1 = completo
            if (lleno > 0f) {
                g.setColor(color);
                g.fillRect(x1, y + LADO / 2 - 2, Math.round((x2 - x1) * lleno), 4);
            }
        }

        // 2. Cuadros y etiquetas
        for (int i = 0; i < n; i++) {
            int num = i + 1;
            int cx = margen + i * paso, x = cx - LADO / 2;
            boolean hecho = num < alcanzado || (num == alcanzado && alcanzado == n);
            boolean activo = num == alcanzado;

            g.setColor(Theme.TINTA);
            g.fillRect(x + (activo ? 4 : 0), y + (activo ? 4 : 0), LADO, LADO);      // sombra dura en el actual
            g.fillRect(x, y, LADO, LADO);                                           // borde
            g.setColor(num <= alcanzado ? color : Theme.BLANCO);
            g.fillRect(x + Theme.BORDE, y + Theme.BORDE, LADO - 2 * Theme.BORDE, LADO - 2 * Theme.BORDE);

            String marca = hecho ? "✓" : String.valueOf(num);
            g.setFont(Theme.mono(18));
            g.setColor(num <= alcanzado ? Theme.textoSobre(color) : Theme.TINTA);
            FontMetrics fm = g.getFontMetrics();
            g.drawString(marca, cx - fm.stringWidth(marca) / 2, y + (LADO + fm.getAscent()) / 2 - 3);

            g.setFont(activo ? Theme.titulo(15) : Theme.texto(15));
            g.setColor(num <= alcanzado ? Theme.TINTA : Theme.GRAFITO);
            fm = g.getFontMetrics();
            g.drawString(PASOS[i], cx - fm.stringWidth(PASOS[i]) / 2, y + LADO + 24);
        }
        g.dispose();
    }
}
