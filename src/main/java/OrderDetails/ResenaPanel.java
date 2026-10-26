package OrderDetails;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.Predicate;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import Restaurante_Completo.LeCartoon_Original.Buttons;
import Restaurante_Completo.LeCartoon_Original.Resena;
import ui.theme.Tarjeta;
import ui.theme.Theme;

/**
 * Tarjeta "¿Qué tal estuvo tu pedido?": 5 estrellas + comentario + Enviar.
 * Cuando ya hay reseña se muestra en modo lectura. No sabe nada de bases de datos:
 * quien la crea le pasa qué hacer al enviar (guardar) y si salió bien.
 */
public class ResenaPanel extends Tarjeta {

    private static final long serialVersionUID = 1L;
    private static final String[] TEXTOS = { "Toca una estrella para calificar", "Muy malo", "Malo", "Regular", "Bueno", "¡Excelente!" };
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("d 'de' MMMM, HH:mm", Locale.forLanguageTag("es-MX"));

    private final Predicate<Resena> alEnviar;     // devuelve true si se guardó

    /**
     * @param existente la reseña ya guardada (o null si todavía no califica)
     * @param alEnviar  guarda la reseña; true = se guardó, false = falló (la tarjeta sigue editable)
     */
    public ResenaPanel(Resena existente, Predicate<Resena> alEnviar) {
        super(new BorderLayout());
        this.alEnviar = alEnviar;
        if (existente == null) mostrarFormulario();
        else mostrarLectura(existente);
    }

    private void mostrarFormulario() {
        removeAll();
        JPanel col = columna();

        agregar(col, Theme.etiqueta("¿Qué tal estuvo tu pedido?", Theme.titulo(22), Theme.TINTA));
        col.add(Box.createVerticalStrut(4));
        agregar(col, Theme.etiqueta("Tu reseña la leen las cocinas para mejorar.", Theme.texto(14), Theme.GRAFITO));
        col.add(Box.createVerticalStrut(16));

        Estrellas estrellas = new Estrellas();
        JLabel lblValor = Theme.etiqueta(TEXTOS[0], Theme.titulo(16), Theme.GRAFITO);
        JPanel filaEstrellas = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filaEstrellas.setOpaque(false);
        filaEstrellas.add(estrellas);
        filaEstrellas.add(Box.createHorizontalStrut(16));
        filaEstrellas.add(lblValor);
        agregar(col, filaEstrellas);
        col.add(Box.createVerticalStrut(18));

        agregar(col, Theme.etiqueta("COMENTARIO (OPCIONAL)", Theme.mono(12), Theme.GRAFITO));
        col.add(Box.createVerticalStrut(6));
        // FlatLaf no tiene placeholder para JTextArea: se dibuja a mano cuando está vacío
        JTextArea comentario = new JTextArea(4, 30) {
            @Override protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    ((java.awt.Graphics2D) g).setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
                            java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g.setColor(Theme.GRAFITO);
                    g.setFont(getFont());
                    java.awt.Insets in = getInsets();
                    g.drawString("¿Qué te gustó? ¿Qué mejorarías?", in.left, in.top + g.getFontMetrics().getAscent());
                }
            }
        };
        comentario.setLineWrap(true);
        comentario.setWrapStyleWord(true);
        comentario.setFont(Theme.texto(16));
        comentario.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        ((AbstractDocument) comentario.getDocument()).setDocumentFilter(new Limite(Resena.MAX_COMENTARIO));
        JScrollPane scroll = new JScrollPane(comentario);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.TINTA, 2));
        scroll.setPreferredSize(new Dimension(10, 110));
        agregar(col, scroll);
        col.add(Box.createVerticalStrut(6));

        JLabel contador = Theme.etiqueta("0 / " + Resena.MAX_COMENTARIO, Theme.mono(12), Theme.GRAFITO);
        agregar(col, contador);
        col.add(Box.createVerticalStrut(14));

        Buttons enviar = Buttons.acento("Enviar reseña");
        enviar.setPreferredSize(new Dimension(220, 48 + Theme.SOMBRA));
        enviar.setEnabled(false);
        enviar.setToolTipText("Primero elige de 1 a 5 estrellas");
        JPanel filaBoton = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filaBoton.setOpaque(false);
        filaBoton.add(enviar);
        agregar(col, filaBoton);

        // ── Comportamiento ──
        estrellas.setAlCambiar(v -> {
            lblValor.setText(TEXTOS[v]);
            lblValor.setForeground(v > 0 ? Theme.TINTA : Theme.GRAFITO);
            enviar.setEnabled(v > 0);                        // sin estrellas no se puede enviar
            enviar.setToolTipText(v > 0 ? null : "Primero elige de 1 a 5 estrellas");
        });
        comentario.getDocument().addDocumentListener(new DocumentListener() {
            private void contar() { contador.setText(comentario.getText().length() + " / " + Resena.MAX_COMENTARIO); }
            public void insertUpdate(DocumentEvent e)  { contar(); }
            public void removeUpdate(DocumentEvent e)  { contar(); }
            public void changedUpdate(DocumentEvent e) { contar(); }
        });
        enviar.addActionListener(e -> {
            Resena r = new Resena(estrellas.getValor(), comentario.getText(), LocalDateTime.now());
            if (alEnviar.test(r)) mostrarLectura(r);
        });

        add(col, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    /** Ya calificó: estrellas fijas, su comentario y la fecha. */
    private void mostrarLectura(Resena r) {
        removeAll();
        JPanel col = columna();
        agregar(col, Theme.etiqueta("Tu reseña", Theme.titulo(22), Theme.TINTA));
        col.add(Box.createVerticalStrut(12));

        Estrellas estrellas = new Estrellas();
        estrellas.setValor(r.getEstrellas());
        estrellas.setEditable(false);
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fila.setOpaque(false);
        fila.add(estrellas);
        fila.add(Box.createHorizontalStrut(16));
        fila.add(Theme.etiqueta(TEXTOS[r.getEstrellas()], Theme.titulo(16), Theme.TINTA));
        agregar(col, fila);
        col.add(Box.createVerticalStrut(12));

        if (!r.getComentario().isEmpty()) {
            JTextArea txt = new JTextArea("“" + r.getComentario() + "”");
            txt.setLineWrap(true);
            txt.setWrapStyleWord(true);
            txt.setEditable(false);
            txt.setFocusable(false);
            txt.setOpaque(false);
            txt.setFont(Theme.texto(17));
            txt.setForeground(Theme.TINTA);
            agregar(col, txt);
            col.add(Box.createVerticalStrut(10));
        }
        if (r.getFecha() != null) {
            agregar(col, Theme.etiqueta("ENVIADA EL " + r.getFecha().format(FECHA).toUpperCase(), Theme.mono(12), Theme.GRAFITO));
        }
        add(col, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private static JPanel columna() {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        return col;
    }

    private static void agregar(JPanel col, JComponent c) {
        c.setAlignmentX(LEFT_ALIGNMENT);
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
        col.add(c);
    }

    /** No deja escribir (ni pegar) más de N caracteres. */
    private static class Limite extends DocumentFilter {
        private final int max;
        Limite(int max) { this.max = max; }
        @Override public void insertString(FilterBypass fb, int off, String s, AttributeSet a) throws BadLocationException {
            replace(fb, off, 0, s, a);
        }
        @Override public void replace(FilterBypass fb, int off, int len, String s, AttributeSet a) throws BadLocationException {
            if (s == null) s = "";
            int cabe = max - (fb.getDocument().getLength() - len);
            if (cabe <= 0) return;
            super.replace(fb, off, len, s.length() > cabe ? s.substring(0, cabe) : s, a);
        }
    }
}
