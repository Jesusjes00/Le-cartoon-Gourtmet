package ui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Envuelve un componente y anima sus cambios con un "swipe": lo de antes sale por la
 * izquierda y lo nuevo entra por la derecha.
 *
 * Funciona con dos fotos: antes de aplicar el cambio se toma una foto (BufferedImage) del
 * contenido, se aplica el cambio de verdad, se toma otra foto, y durante DURACION_MS un
 * javax.swing.Timer pinta las dos fotos desplazándose. Al terminar se vuelve a pintar el
 * contenido real (con su GIF animado y todo).
 *
 * El cambio en sí no lo conoce: lo recibe como Runnable. Así el patrón State sigue igual.
 */
public class PanelSwipe extends JPanel {

    private static final long serialVersionUID = 1L;

    /** Duración del deslizamiento; el Stepper usa la misma para ir parejo. */
    public static final int DURACION_MS = 380;
    private static final int CUADRO_MS = 15;      // ~60 cuadros por segundo

    private BufferedImage antes, despues;
    private float avance = -1f;                   // 0 → 1 mientras anima; -1 = sin animación
    private Timer reloj;
    private Runnable alTerminar;

    public PanelSwipe(JComponent contenido) {
        super(new BorderLayout());
        setOpaque(false);
        add(contenido, BorderLayout.CENTER);
    }

    /**
     * Aplica el cambio con animación. Si el panel no está en pantalla (todavía no se muestra
     * o el usuario ya se fue a otra), lo aplica directo, sin animar.
     *
     * @param cambio     lo que modifica el contenido (por ejemplo, avanzar el estado y repintar)
     * @param alTerminar qué hacer cuando acabe la animación (puede ser null)
     */
    public void animar(Runnable cambio, Runnable alTerminar) {
        detener(false);                            // si había una a medias, se corta sin encadenar nada
        if (!isShowing() || getWidth() <= 0 || getHeight() <= 0) {
            cambio.run();
            if (alTerminar != null) alTerminar.run();
            return;
        }
        this.alTerminar = alTerminar;
        antes = foto();
        cambio.run();
        Window ventana = SwingUtilities.getWindowAncestor(this);
        if (ventana != null) ventana.validate();   // acomoda YA el contenido nuevo para fotografiarlo
        despues = foto();

        long inicio = System.currentTimeMillis();
        avance = 0f;
        reloj = new Timer(CUADRO_MS, e -> {
            float x = (System.currentTimeMillis() - inicio) / (float) DURACION_MS;
            if (x >= 1f) {
                detener(true);
            } else {
                avance = 1f - (float) Math.pow(1 - x, 3);   // ease-out: rápido al inicio, frena al final
                repaint();
            }
        });
        reloj.start();
    }

    /**
     * Detiene la animación (si la hay) y vuelve a pintar el contenido real.
     * @param avisar true = al terminar normal se ejecuta alTerminar; false = se descarta
     */
    private void detener(boolean avisar) {
        if (reloj != null) reloj.stop();
        reloj = null;
        boolean estabaAnimando = avance >= 0f;
        avance = -1f;
        antes = despues = null;
        repaint();
        Runnable siguiente = alTerminar;
        alTerminar = null;                         // se limpia ANTES de llamarlo: puede iniciar otra animación
        if (avisar && estabaAnimando && siguiente != null) siguiente.run();
    }

    /** Foto del contenido tal como se ve ahora. */
    private BufferedImage foto() {
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        super.paint(g);                            // pinta los hijos, no la animación
        g.dispose();
        return img;
    }

    @Override
    public void paint(Graphics g0) {
        if (avance < 0f || antes == null || despues == null) {
            super.paint(g0);
            return;
        }
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.clipRect(0, 0, getWidth(), getHeight());
        int dx = Math.round(getWidth() * avance);
        g.drawImage(antes, -dx, 0, null);              // sale por la izquierda
        g.drawImage(despues, getWidth() - dx, 0, null); // entra por la derecha
        g.dispose();
    }
}
