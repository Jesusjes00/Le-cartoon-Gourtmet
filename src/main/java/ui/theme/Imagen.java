package ui.theme;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * Imagen que llena su espacio SIN deformarse (modo "cover": escala y recorta el sobrante,
 * como object-fit: cover en CSS). Las imágenes se leen una sola vez y se guardan reducidas.
 */
public class Imagen extends JComponent {

    private static final int MAX_LADO = 1400;                    // las originales miden hasta 2816 px
    private static final Map<String, BufferedImage> CACHE = new HashMap<>();

    private final BufferedImage maestra;
    private BufferedImage escalada;                              // copia ya escalada al tamaño actual
    private int anchoCache = -1, altoCache = -1;
    private boolean conBorde;

    public Imagen(String ruta, boolean conBorde) {
        this.maestra = cargar(ruta);
        this.conBorde = conBorde;
        setPreferredSize(new Dimension(320, 180));
    }

    /** Lee la imagen del classpath una sola vez (reducida a 1400 px) y la comparte. */
    public static synchronized BufferedImage cargar(String ruta) {
        if (ruta == null) return null;
        return CACHE.computeIfAbsent(ruta, r -> {
            try {
                URL url = Imagen.class.getResource(r);
                if (url == null) return null;
                BufferedImage img = sinEsquinasRedondas(ImageIO.read(url));
                double f = Math.min(1.0, (double) MAX_LADO / Math.max(img.getWidth(), img.getHeight()));
                return f < 1.0 ? escalar(img, (int) (img.getWidth() * f), (int) (img.getHeight() * f)) : img;
            } catch (Exception e) {
                System.err.println("No se pudo cargar la imagen: " + r);
                return null;
            }
        });
    }

    /**
     * Las fotos de los platillos traen esquinas redondeadas transparentes; dentro de un marco
     * cuadrado se ven como "picos" negros. Se recorta lo justo para quedarse con el rectángulo lleno.
     */
    private static BufferedImage sinEsquinasRedondas(BufferedImage img) {
        if (img.getColorModel().hasAlpha() && (img.getRGB(0, 0) >>> 24) < 10) {
            int d = 0, max = Math.min(img.getWidth(), img.getHeight()) / 4;
            while (d < max && (img.getRGB(d, d) >>> 24) < 250) d++;   // avanza en diagonal hasta pixel opaco
            d += 2;
            return img.getSubimage(d, d, img.getWidth() - 2 * d, img.getHeight() - 2 * d);
        }
        return img;
    }

    /** Escala la imagen para CUBRIR w×h y recorta lo que sobra, centrado. */
    public static BufferedImage cubrir(BufferedImage src, int w, int h) {
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        double f = Math.max((double) w / src.getWidth(), (double) h / src.getHeight());
        int sw = (int) Math.ceil(src.getWidth() * f), sh = (int) Math.ceil(src.getHeight() * f);
        g.drawImage(src, (w - sw) / 2, (h - sh) / 2, sw, sh, null);
        g.dispose();
        return out;
    }

    private static BufferedImage escalar(BufferedImage src, int w, int h) {
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(src, 0, 0, w, h, null);
        g.dispose();
        return out;
    }

    /** Pinta la maestra cubriendo (x, y, w, h); reescala solo si el tamaño cambió. */
    public static BufferedImage enCache(BufferedImage maestra, BufferedImage cache, int w, int h) {
        if (maestra == null || w <= 0 || h <= 0) return null;
        if (cache != null && cache.getWidth() == w && cache.getHeight() == h) return cache;
        return cubrir(maestra, w, h);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int b = conBorde ? Theme.BORDE : 0;
        int w = getWidth() - 2 * b, h = getHeight() - 2 * b;
        if (conBorde) {
            g.setColor(Theme.TINTA);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        if (maestra == null) {                                   // sin imagen: bloque gris con texto
            g.setColor(Theme.NIEBLA);
            g.fillRect(b, b, w, h);
            return;
        }
        if (w != anchoCache || h != altoCache) {
            escalada = enCache(maestra, escalada, w, h);
            anchoCache = w;
            altoCache = h;
        }
        g.drawImage(escalada, b, b, null);
    }

    public void setConBorde(boolean conBorde) { this.conBorde = conBorde; repaint(); }
    public boolean tieneImagen() { return maestra != null; }
}
