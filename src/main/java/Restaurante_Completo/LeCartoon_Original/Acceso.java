package Restaurante_Completo.LeCartoon_Original;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import ui.theme.Theme;

/**
 * Piezas compartidas por Login_Window y Register_Window para que las dos ventanas
 * se vean igual: marco, mitad izquierda con el GIF a todo lo alto, campos con placeholder y controles.
 */
final class Acceso {

    static final int ANCHO = 980, ALTO = 710;

    private Acceso() { }

    /** Ventana sin barra de Windows, centrada, con borde grueso y arrastrable desde cualquier parte. */
    static JPanel prepararVentana(JFrame frame) {
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(ANCHO, ALTO);
        frame.setLocationRelativeTo(null);

        JPanel contenido = new JPanel(new GridLayout(1, 2, 0, 0));
        contenido.setBorder(Theme.borde());
        frame.setContentPane(contenido);

        int[] inicio = new int[2];   // arreglo para poder modificarlo dentro de la clase anónima
        MouseAdapter arrastre = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                inicio[0] = e.getXOnScreen() - frame.getX();
                inicio[1] = e.getYOnScreen() - frame.getY();
            }
            @Override public void mouseDragged(MouseEvent e) {
                frame.setLocation(e.getXOnScreen() - inicio[0], e.getYOnScreen() - inicio[1]);
            }
        };
        contenido.addMouseListener(arrastre);
        contenido.addMouseMotionListener(arrastre);
        return contenido;
    }

    /**
     * Mitad izquierda: el GIF ocupa TODO el panel, como en la versión original (es el protagonista).
     * Se escala para cubrir el panel sin deformarse; lo que sobra se recorta al centro
     * porque el JLabel no pinta fuera de sus límites. SCALE_DEFAULT (no SMOOTH) para que siga animado.
     */
    static JPanel panelIzquierdo(String rutaGif, String frase) {
        JPanel izq = new JPanel(new BorderLayout());
        izq.setBackground(Theme.TINTA);
        izq.setBorder(BorderFactory.createMatteBorder(0, 0, 0, Theme.BORDE, Theme.TINTA));

        JLabel gif = new JLabel();
        gif.setHorizontalAlignment(SwingConstants.CENTER);
        gif.setVerticalAlignment(SwingConstants.CENTER);
        URL url = Acceso.class.getResource(rutaGif);
        if (url != null) {
            ImageIcon original = new ImageIcon(url);
            int panelW = ANCHO / 2 - 2 * Theme.BORDE, panelH = ALTO - 2 * Theme.BORDE;
            double f = Math.max((double) panelW / original.getIconWidth(), (double) panelH / original.getIconHeight());
            int w = (int) Math.ceil(original.getIconWidth() * f), h = (int) Math.ceil(original.getIconHeight() * f);
            gif.setIcon(new ImageIcon(original.getImage().getScaledInstance(w, h, Image.SCALE_DEFAULT)));
        }
        gif.setToolTipText(frase);
        izq.add(gif, BorderLayout.CENTER);
        return izq;
    }

    /** Mitad derecha: fondo blanco, controles − × arriba y el formulario. */
    static JPanel panelDerecho(JFrame frame, JComponent formulario) {
        JPanel der = new JPanel(new BorderLayout());
        der.setBackground(Theme.BLANCO);

        Buttons btnMinimizar = Buttons.control("—", Theme.BLANCO, false);
        btnMinimizar.addActionListener(e -> frame.setState(JFrame.ICONIFIED));
        Buttons btnCerrar = Buttons.control("×", Theme.BLANCO, true);
        btnCerrar.setFont(Theme.titulo(20));
        btnCerrar.addActionListener(e -> System.exit(0));
        for (Buttons b : new Buttons[] { btnMinimizar, btnCerrar }) b.setPreferredSize(new Dimension(48, 36));
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        controles.setOpaque(false);
        controles.add(btnMinimizar);
        controles.add(btnCerrar);

        // Marca arriba a la izquierda (logo negro + nombre), como estaba en el original
        JPanel marca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        marca.setOpaque(false);
        marca.setBorder(BorderFactory.createEmptyBorder(18, 26, 0, 0));
        marca.add(new JLabel(Theme.recortada("/Restaurante_Completo/LeCartoon_Original/IMAGES/LOGO.png", 36)));
        marca.add(Theme.etiqueta("Le Cartoon Gourmet", Theme.logo(24f), Theme.TINTA));

        JPanel arriba = new JPanel(new BorderLayout());
        arriba.setOpaque(false);
        arriba.add(marca, BorderLayout.WEST);
        arriba.add(controles, BorderLayout.EAST);
        der.add(arriba, BorderLayout.NORTH);

        JPanel centro = new JPanel(new java.awt.GridBagLayout());   // centra el formulario
        centro.setOpaque(false);
        centro.add(formulario);
        der.add(centro, BorderLayout.CENTER);
        return der;
    }

    /** Columna vertical alineada a la izquierda, de ancho fijo. */
    static JPanel columna(int ancho) {
        JPanel col = new JPanel() {
            @Override public Dimension getPreferredSize() {
                return new Dimension(ancho, super.getPreferredSize().height);
            }
        };
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        return col;
    }

    static void agregar(JPanel col, JComponent c) {
        c.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
        col.add(c);
    }

    /** Etiqueta en mono sobre el campo + el campo. */
    static void agregarCampo(JPanel col, String etiqueta, JComponent campo) {
        agregar(col, Theme.etiqueta(etiqueta.toUpperCase(), Theme.mono(12), Theme.GRAFITO));
        col.add(Box.createVerticalStrut(6));
        agregar(col, campo);
        col.add(Box.createVerticalStrut(14));
    }


    static JTextField campo(String placeholder, String icono) {
        JTextField t = new JTextField();
        configurar(t, placeholder, icono);
        return t;
    }

    static JPasswordField campoContraseña(String placeholder) {
        JPasswordField t = new JPasswordField();
        configurar(t, placeholder, "candado");
        t.putClientProperty("JPasswordField.showRevealButton", true);   // ojito para ver la contraseña
        return t;
    }

    private static void configurar(JTextComponent t, String placeholder, String icono) {
        t.setFont(Theme.texto(16));
        t.setPreferredSize(new Dimension(300, 44));
        t.putClientProperty("JTextField.placeholderText", placeholder);
        if (icono != null && !icono.equals("candado")) {
            t.putClientProperty("JTextField.leadingIcon", Theme.icono(icono, 18, Theme.GRAFITO));
        }
        // Al escribir se quita la marca de error
        t.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { t.putClientProperty("JComponent.outline", null); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { }
        });
    }

    /** Marca en ladrillo los campos vacíos; true si todos tienen algo. */
    static boolean validarLlenos(JTextComponent... campos) {
        boolean ok = true;
        for (JTextComponent t : campos) {
            String valor = (t instanceof JPasswordField p) ? new String(p.getPassword()) : t.getText();
            boolean vacio = valor.trim().isEmpty();
            t.putClientProperty("JComponent.outline", vacio ? "error" : null);
            ok &= !vacio;
        }
        return ok;
    }

    static void marcarError(JTextComponent... campos) {
        for (JTextComponent t : campos) t.putClientProperty("JComponent.outline", "error");
    }
}
