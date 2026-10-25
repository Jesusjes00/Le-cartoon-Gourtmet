package Restaurante_Completo.LeCartoon_Original;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import ui.theme.Theme;

/**
 * Botón de LeCartoon con 5 estilos. Los nuevos se crean con las fábricas
 * (primario, acento, nav, control, chip...). El constructor viejo da el estilo CLASICO.
 */
public class Buttons extends JButton {

    /** Cómo se dibuja el botón. CLASICO = el estilo original redondeado. */
    public enum Estilo { CLASICO, ACCION, NAV, CONTROL, CHIP }

    private static final Cursor MANO = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    private Estilo estilo = Estilo.CLASICO;
    private int arcWidth = 20;
    private int arcHeight = 20;
    private Color colorFondo;
    private Color colorHover;
    private Color colorBordeCustom = null;
    private Color colorTexto;          // color del texto en reposo
    private Color colorTextoHover;     // color del texto con el mouse encima (null = no cambia)
    private boolean mouseOver = false;
    private boolean seleccionado = false;   // NAV: pestaña activa

    public Buttons(String texto, int arcWidth, int arcHeight, Color background, Color Hover) {
        super(texto);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(true);
        colorFondo = background;
        colorHover = Hover;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                mouseOver = true;
                if (colorTextoHover != null) cambiarTexto(colorTextoHover);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseOver = false;
                if (colorTextoHover != null) cambiarTexto(colorTexto);
                repaint();
            }
        });
    }

    // ══════════════ Fábricas ══════════════

    /** Acción principal de confirmación (negro). */
    public static Buttons primario(String texto)   { return accion(texto, Theme.TINTA); }
    /** LA acción más importante de la pantalla (terracota). Solo una por pantalla. */
    public static Buttons acento(String texto)     { return accion(texto, Theme.TERRACOTA); }
    /** Acción alternativa (blanco). */
    public static Buttons secundario(String texto) { return accion(texto, Theme.BLANCO); }
    /** Borrar, quitar, salir (ladrillo; hover vino). */
    public static Buttons peligro(String texto) {
        Buttons b = accion(texto, Theme.LADRILLO);
        b.colorHover = Theme.VINO;
        return b;
    }
    /** Acción con el color del estado del pedido (viene del patrón State). */
    public static Buttons estado(String texto, Color colorEstado) { return accion(texto, colorEstado); }

    /** Pestaña de menú: plana; hover = terracota; seleccionada = barra terracota abajo. */
    public static Buttons nav(String texto, Color fondoBarra) {
        Buttons b = new Buttons(texto, 0, 0, fondoBarra, Theme.TERRACOTA);
        b.estilo = Estilo.NAV;
        b.setForeground(textoSobre(fondoBarra));
        b.colorTextoHover = textoSobre(Theme.TERRACOTA);
        b.setFont(Theme.titulo(16));
        b.setCursor(MANO);
        return b;
    }

    /** Minimizar / cerrar ventana: borde 2 px, sin sombra. */
    public static Buttons control(String texto, Color fondoBarra, boolean esCerrar) {
        Color hover = esCerrar ? Theme.LADRILLO : Theme.TERRACOTA;
        Buttons b = new Buttons(texto, 0, 0, fondoBarra, hover);
        b.estilo = Estilo.CONTROL;
        b.setForeground(textoSobre(fondoBarra));
        b.colorTextoHover = textoSobre(hover);
        b.setFont(Theme.titulo(14));
        b.setCursor(MANO);
        return b;
    }

    /** Dato que no se clica (ej. "12 PEDIDOS"): mono, borde 2 px, plano. */
    public static Buttons chip(String texto) {
        Buttons b = new Buttons(texto, 0, 0, Theme.BLANCO, Theme.BLANCO);
        b.estilo = Estilo.CHIP;
        b.setForeground(Theme.TINTA);
        b.setFont(Theme.mono(14));
        b.setFocusable(false);
        return b;
    }

    private static Buttons accion(String texto, Color fondo) {
        Buttons b = new Buttons(texto, 0, 0, fondo, oscurecer(fondo));
        b.estilo = Estilo.ACCION;
        b.setForeground(textoSobre(fondo));
        b.setFont(Theme.titulo(16));
        b.setCursor(MANO);
        return b;
    }

    // ══════════════ Dibujo ══════════════

    @Override
    protected void paintComponent(Graphics g) {
        switch (estilo) {
            case ACCION        -> pintarAccion(g);
            case NAV           -> pintarNav(g);
            case CONTROL, CHIP -> pintarPlano(g);
            default            -> pintarClasico(g);
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (estilo != Estilo.CLASICO) return;          // los estilos nuevos dibujan su borde solos
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color borde = (colorBordeCustom != null)
                ? (mouseOver ? colorHover : colorBordeCustom)
                : (mouseOver ? colorHover : colorFondo);
        g2d.setColor(borde);
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
    }

    /** Estilo original: rectángulo redondeado. */
    private void pintarClasico(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(mouseOver ? colorHover : colorFondo);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
        super.paintComponent(g);
    }

    /** Neo-brutalista: sombra dura + borde grueso + se hunde al presionar. */
    private void pintarAccion(Graphics g) {
        int s = Theme.SOMBRA, b = Theme.BORDE;
        boolean hundido = getModel().isArmed() && getModel().isPressed();
        int off = hundido ? s : 0;
        int w = getWidth() - s, h = getHeight() - s;

        Graphics2D g2 = (Graphics2D) g.create();
        if (!hundido) {                                       // 1. sombra
            g2.setColor(Theme.TINTA);
            g2.fillRect(s, s, w, h);
        }
        g2.setColor(colorBordeCustom != null ? colorBordeCustom : Theme.TINTA);
        g2.fillRect(off, off, w, h);                          // 2. borde
        g2.setColor(mouseOver ? colorHover : colorFondo);
        g2.fillRect(off + b, off + b, w - 2 * b, h - 2 * b);  // 3. cara
        g2.translate(off - s / 2, off - s / 2);               // 4. texto centrado en la cara
        super.paintComponent(g2);
        g2.dispose();
    }

    /** Pestaña: plana, hover terracota, barra terracota si está seleccionada. */
    private void pintarNav(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(mouseOver ? colorHover : colorFondo);
        g2.fillRect(0, 0, getWidth(), getHeight());
        if (seleccionado) {
            g2.setColor(Theme.TERRACOTA);
            g2.fillRect(0, getHeight() - 4, getWidth(), 4);
        }
        g2.dispose();
        super.paintComponent(g);
    }

    /** CONTROL y CHIP: borde de 2 px del color del texto, sin sombra. */
    private void pintarPlano(Graphics g) {
        int b = 2;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(colorBordeCustom != null ? colorBordeCustom : getForeground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        boolean hover = mouseOver && estilo == Estilo.CONTROL;
        g2.setColor(hover ? colorHover : colorFondo);
        g2.fillRect(b, b, getWidth() - 2 * b, getHeight() - 2 * b);
        g2.dispose();
        super.paintComponent(g);
    }

    // ══════════════ Utilidades ══════════════

    private static Color oscurecer(Color c) {      // hover de las acciones: 15 % más oscuro
        if (c.getRed() + c.getGreen() + c.getBlue() < 120) {   // el negro no se puede oscurecer: se aclara
            return new Color(c.getRed() + 40, c.getGreen() + 40, c.getBlue() + 40);
        }
        return new Color((int) (c.getRed() * 0.85), (int) (c.getGreen() * 0.85), (int) (c.getBlue() * 0.85));
    }

    /** Texto legible sobre un fondo: tinta si el fondo es claro, blanco si es oscuro. */
    private static Color textoSobre(Color fondo) {
        return Theme.textoSobre(fondo);          // una sola regla para toda la app
    }

    /** Cambia el color del texto sin perder el color "en reposo" (lo usa el hover). */
    private void cambiarTexto(Color c) {
        super.setForeground(c);
    }

    @Override
    public void setForeground(Color c) {
        colorTexto = c;            // si alguien lo cambia por fuera, ese pasa a ser el de reposo
        super.setForeground(c);
    }

    // ══════════════ Getters / setters (compatibilidad) ══════════════

    public Estilo getEstilo() { return estilo; }

    public boolean isSeleccionado() { return seleccionado; }
    public void setSeleccionado(boolean seleccionado) { this.seleccionado = seleccionado; repaint(); }

    public void setColorFondo(Color color) {
        this.colorFondo = color;
        if (estilo == Estilo.ACCION) {          // en acciones, hover y texto siguen al nuevo fondo
            colorHover = oscurecer(color);
            setForeground(textoSobre(color));
        }
        repaint();
    }

    public void setColorBordeCustom(Color colorBorde) { this.colorBordeCustom = colorBorde; repaint(); }
    public Color getColorBordeCustom() { return colorBordeCustom; }

    public Color getColorHover() { return colorHover; }
    public void setColorHover(Color colorHover) { this.colorHover = colorHover; }

    public int getArcWidth() { return arcWidth; }
    public void setArcWidth(int arcWidth) { this.arcWidth = arcWidth; }
    public int getArcHeight() { return arcHeight; }
    public void setArcHeight(int arcHeight) { this.arcHeight = arcHeight; }

    public boolean isMouseOver() { return mouseOver; }
    public void setMouseOver(boolean mouseOver) { this.mouseOver = mouseOver; }
}