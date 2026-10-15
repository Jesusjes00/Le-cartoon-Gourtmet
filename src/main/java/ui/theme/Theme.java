package ui.theme;

import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;

public final class Theme {
    private Theme() {}   // no se instancia: solo constantes y utilidades

    // ── Paleta ─────────────────────────────────────────
    public static final Color SALVIA   = new Color(0x99A88C);
    public static final Color PIZARRA  = new Color(0x335C67);  // primario: header, botón principal
    public static final Color NIEBLA   = new Color(0xE8E8E8);  // fondo de la app
    public static final Color CREMA    = new Color(0xFFF9D6);  // tarjetas
    public static final Color MOSTAZA  = new Color(0xE09F3E);  // acento: "Realizar pedido", foco
    public static final Color LADRILLO = new Color(0x9E2A2B);  // peligro: Quitar, errores
    public static final Color VINO     = new Color(0x540B0E);  // hover de peligro
    public static final Color TINTA    = new Color(0x1B1B1B);  // bordes, sombra y texto
    public static final Color BLANCO   = Color.WHITE;

    // ── Medidas ────────────────────────────────────────
    public static final int BORDE  = 3;   // grosor del borde
    public static final int SOMBRA = 6;   // desplazamiento de la sombra dura

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

    // ── Instalar todo (se llama UNA vez, antes de abrir ventanas) ──
    public static void install() {
        FlatInterFont.install();
        FlatJetBrainsMonoFont.install();
        FlatLaf.setPreferredFontFamily(FlatInterFont.FAMILY);
        FlatLightLaf.setup();

        UIManager.put("Panel.background", NIEBLA);
        UIManager.put("Component.arc", 0);
        UIManager.put("Button.arc", 0);
        UIManager.put("TextComponent.arc", 0);
        UIManager.put("Component.borderWidth", 2);
        UIManager.put("Component.borderColor", TINTA);
        UIManager.put("Component.focusWidth", 0);
        UIManager.put("Component.focusedBorderColor", MOSTAZA);
        UIManager.put("Button.borderWidth", 2);
        UIManager.put("Button.borderColor", TINTA);
        UIManager.put("Button.background", CREMA);
        UIManager.put("Button.default.background", PIZARRA);
        UIManager.put("Button.default.foreground", BLANCO);
        UIManager.put("TextField.background", BLANCO);
        UIManager.put("ScrollBar.width", 10);
        UIManager.put("ScrollBar.thumb", PIZARRA);
        UIManager.put("ScrollBar.track", NIEBLA);
    }
}