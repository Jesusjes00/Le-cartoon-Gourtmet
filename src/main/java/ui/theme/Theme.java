package ui.theme;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;

public final class Theme {
    private Theme() {}   // no se instancia: solo constantes y utilidades

    // ── Paleta principal: NEGRO + TERRACOTA + BLANCO ───
    public static final Color TERRACOTA = new Color(0xC65D3B);  // acento: pestaña activa, hover, acción principal
    // (TINTA es el negro y BLANCO el fondo y las tarjetas; ver abajo)

    // ── Paleta de apoyo ────────────────────────────────
    public static final Color SALVIA   = new Color(0x99A88C);
    public static final Color PIZARRA  = new Color(0x335C67);  // (ya no se usa en pantallas: el primario ahora es negro)
    public static final Color NIEBLA   = new Color(0xE8E8E8);  // gris neutro (relleno de imágenes que no cargan)
    public static final Color MOSTAZA  = new Color(0xE09F3E);  // avisos (Toast.aviso)
    public static final Color LADRILLO = new Color(0x9E2A2B);  // peligro: Quitar, errores
    public static final Color VINO     = new Color(0x540B0E);  // hover de peligro
    public static final Color TINTA    = new Color(0x1B1B1B);  // NEGRO: navbar, botón primario, bordes, sombra y texto
    public static final Color GRAFITO  = new Color(0x4A4A4A);  // texto secundario (descripciones, etiquetas)
    public static final Color BLANCO   = Color.WHITE;          // fondo, tarjetas y texto sobre oscuro
    /** Antes era el beige de las tarjetas (#FFF9D6). Se deja como alias para que nada truene; usa BLANCO. */
    @Deprecated public static final Color CREMA = BLANCO;

    // ── Medidas ────────────────────────────────────────
    public static final int BORDE  = 3;   // grosor del borde
    public static final int SOMBRA = 6;   // desplazamiento de la sombra dura
    public static final int MARGEN = 32;  // margen lateral de las pantallas

    private static final String ICONOS = "/Restaurante_Completo/LeCartoon_Original/IMAGES/icons/";

    // ── Fuentes ────────────────────────────────────────
    public static Font titulo(int size) { return new Font(FlatInterFont.FAMILY, Font.BOLD, size); }
    public static Font texto(int size)  { return new Font(FlatInterFont.FAMILY, Font.PLAIN, size); }
    public static Font mono(int size)   { return new Font(FlatJetBrainsMonoFont.FAMILY, Font.BOLD, size); }

    private static Font baloo;   // se carga una sola vez
    public static Font logo(float size) {
        if (baloo == null) {
            try (InputStream in = Theme.class.getResourceAsStream(
                    "/Restaurante_Completo/LeCartoon_Original/FONTS/Baloo2-SemiBold.ttf")) {
                baloo = Font.createFont(Font.TRUETYPE_FONT, in);
            } catch (Exception e) {
                baloo = titulo(12);   // si falla, cae a Inter y la app no truena
            }
        }
        return baloo.deriveFont(size);
    }

    // ── Bordes ─────────────────────────────────────────
    public static Border borde() {
        return BorderFactory.createLineBorder(TINTA, BORDE);
    }

    // ── Imágenes ───────────────────────────────────────

    /** Imagen del classpath escalada a w×h; null si no existe (la pantalla decide qué mostrar). */
    public static ImageIcon imagen(String ruta, int w, int h) {
        if (ruta == null) return null;
        URL url = Theme.class.getResource(ruta);
        if (url == null) return null;
        Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    /**
     * Imagen sin el margen transparente que traen los PNG del logo, escalada a un alto
     * fijo y conservando la proporción. LOGO.png y LOGO2.png son ~65 % aire.
     */
    public static ImageIcon recortada(String ruta, int alto) {
        URL url = Theme.class.getResource(ruta);
        if (url == null) return null;
        try {
            BufferedImage img = javax.imageio.ImageIO.read(url);
            int x0 = img.getWidth(), y0 = img.getHeight(), x1 = -1, y1 = -1;
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    if ((img.getRGB(x, y) >>> 24) > 10) {     // pixel no transparente
                        x0 = Math.min(x0, x); x1 = Math.max(x1, x);
                        y0 = Math.min(y0, y); y1 = Math.max(y1, y);
                    }
                }
            }
            if (x1 < 0) return null;
            BufferedImage util = img.getSubimage(x0, y0, x1 - x0 + 1, y1 - y0 + 1);
            int ancho = util.getWidth() * alto / util.getHeight();
            return new ImageIcon(util.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH));
        } catch (java.io.IOException e) {
            return null;
        }
    }

    /**
     * Ícono de IMAGES/icons pintado de un solo color. Los PNG vienen unos negros y otros
     * blancos; así cualquiera sirve sobre cualquier fondo.
     */
    public static ImageIcon icono(String archivo, int lado, Color color) {
        URL url = Theme.class.getResource(ICONOS + archivo);
        if (url == null) return null;
        BufferedImage lienzo = new BufferedImage(lado, lado, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = lienzo.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(new ImageIcon(url).getImage(), 0, 0, lado, lado, null);   // 1. forma del ícono
        g.setComposite(AlphaComposite.SrcIn);                                  // 2. solo donde ya hay pixeles
        g.setColor(color);
        g.fillRect(0, 0, lado, lado);                                          // 3. se rellenan del color
        g.dispose();
        return new ImageIcon(lienzo);
    }

    /** Texto legible sobre un fondo: tinta si el fondo es claro, blanco si es oscuro (luminancia). */
    public static Color textoSobre(Color fondo) {
        double luz = 0.299 * fondo.getRed() + 0.587 * fondo.getGreen() + 0.114 * fondo.getBlue();
        return luz > 150 ? TINTA : BLANCO;
    }

    // ── Piezas que se repiten en todas las pantallas ───

    public static JLabel etiqueta(String texto, Font fuente, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(fuente);
        l.setForeground(color);
        return l;
    }

    /** Título de sección: "FRANQUICIAS FAVORITAS ━━━━━━━━━━" (texto mono + barra gruesa). */
    public static JPanel seccion(String titulo) {
        JPanel fila = new JPanel(new BorderLayout(16, 0));
        fila.setOpaque(false);
        fila.add(etiqueta(titulo.toUpperCase(), mono(15), TINTA), BorderLayout.WEST);
        JPanel barra = new JPanel();
        barra.setBackground(TINTA);
        barra.setPreferredSize(new Dimension(10, BORDE));
        JPanel centro = new JPanel(new java.awt.GridBagLayout());   // centra la barra en vertical
        centro.setOpaque(false);
        java.awt.GridBagConstraints c = new java.awt.GridBagConstraints();
        c.fill = java.awt.GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        centro.add(barra, c);
        fila.add(centro, BorderLayout.CENTER);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return fila;
    }

    /** Scroll sin borde, con la barra del tema y rueda a velocidad cómoda. */
    public static JScrollPane scroll(Component contenido) {
        JScrollPane sp = new JScrollPane(contenido);
        sp.setBorder(null);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.getVerticalScrollBar().setUnitIncrement(24);
        sp.getViewport().setBackground(BLANCO);
        return sp;
    }

    // ── Instalar todo (se llama UNA vez, antes de abrir ventanas) ──
    public static void install() {
        FlatInterFont.install();
        FlatJetBrainsMonoFont.install();
        FlatLaf.setPreferredFontFamily(FlatInterFont.FAMILY);
        FlatLightLaf.setup();

        UIManager.put("Panel.background", BLANCO);
        UIManager.put("Component.arc", 0);
        UIManager.put("Button.arc", 0);
        UIManager.put("TextComponent.arc", 0);
        UIManager.put("Component.borderWidth", 2);
        UIManager.put("Component.borderColor", TINTA);
        UIManager.put("Component.focusWidth", 0);
        UIManager.put("Component.innerFocusWidth", 0);
        UIManager.put("Component.focusedBorderColor", TERRACOTA);
        UIManager.put("Component.error.borderColor", LADRILLO);
        UIManager.put("Component.error.focusedBorderColor", LADRILLO);
        UIManager.put("Button.borderWidth", 2);
        UIManager.put("Button.borderColor", TINTA);
        UIManager.put("Button.background", BLANCO);
        UIManager.put("Button.default.background", TINTA);
        UIManager.put("Button.default.foreground", BLANCO);
        UIManager.put("TextField.background", BLANCO);
        UIManager.put("PasswordField.background", BLANCO);
        UIManager.put("TextComponent.selectionBackground", TERRACOTA);
        UIManager.put("TextComponent.selectionForeground", BLANCO);
        UIManager.put("TextField.placeholderForeground", GRAFITO);
        UIManager.put("ScrollBar.width", 12);
        UIManager.put("ScrollBar.thumb", TINTA);
        UIManager.put("ScrollBar.hoverThumbColor", TERRACOTA);
        UIManager.put("ScrollBar.track", BLANCO);
        UIManager.put("ScrollBar.showButtons", false);
        UIManager.put("ScrollBar.thumbArc", 0);
        UIManager.put("ScrollBar.thumbInsets", new java.awt.Insets(2, 2, 2, 2));
        UIManager.put("PopupMenu.borderColor", TINTA);
        UIManager.put("PopupMenu.borderInsets", new java.awt.Insets(3, 3, 3, 3));
        UIManager.put("PopupMenu.background", BLANCO);
        UIManager.put("MenuItem.background", BLANCO);
        UIManager.put("MenuItem.selectionBackground", TERRACOTA);
        UIManager.put("MenuItem.selectionForeground", BLANCO);
        UIManager.put("ToolTip.background", BLANCO);
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(TINTA, 2));
    }
}
