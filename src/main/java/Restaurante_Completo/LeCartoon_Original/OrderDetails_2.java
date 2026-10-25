package Restaurante_Completo.LeCartoon_Original;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import Dishes.Dishes;
import Franchises.Franchises;
import ui.Toast;
import ui.theme.Imagen;
import ui.theme.Lienzo;
import ui.theme.Theme;

/** Detalle de un platillo: foto, origen, descripción y barra para elegir cantidad y agregar. */
public class OrderDetails_2 extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final Locale MX = Locale.forLanguageTag("es-MX");

    private int cantidadActual = 1;
    private double precioUnitario;
    private JLabel lblCantidad;
    private JLabel lblTotalNumerico;

    public OrderDetails_2(Franchises franquicia, Dishes platillo, JPanel panelAnterior) {
        this.precioUnitario = platillo.getPrice();
        Color fondo = FranchiseDetails.colorDe(franquicia);

        setLayout(new BorderLayout());
        setBackground(fondo);

        // ══════ Contenido ══════
        Lienzo contenido = new Lienzo(new BorderLayout(0, 20));
        contenido.setBackground(fondo);
        contenido.setBorder(BorderFactory.createEmptyBorder(20, Theme.MARGEN, 32, Theme.MARGEN));

        Buttons btnVolver = Buttons.secundario("← Volver");
        btnVolver.setPreferredSize(new Dimension(140, 42 + Theme.SOMBRA));
        btnVolver.addActionListener(e -> Main_Frame.changeDynamicPanel(panelAnterior));
        JPanel filaVolver = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filaVolver.setOpaque(false);
        filaVolver.add(btnVolver);
        contenido.add(filaVolver, BorderLayout.NORTH);

        // Dos columnas: foto (45 %) | información (55 %)
        JPanel columnas = new JPanel(new GridBagLayout());
        columnas.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTH;

        PromoCard foto = new PromoCard(platillo.getImage(), 640, 346);
        foto.setInteractiva(false);
        JPanel marcoFoto = new JPanel(new BorderLayout()) {        // mantiene la proporción 640×340
            @Override public Dimension getPreferredSize() {
                int w = Math.max(getWidth(), 360);
                return new Dimension(w, (int) ((w - Theme.SOMBRA) / 1.88) + Theme.SOMBRA);
            }
        };
        marcoFoto.setOpaque(false);
        marcoFoto.add(foto, BorderLayout.CENTER);
        JPanel envolturaFoto = new JPanel(new BorderLayout());
        envolturaFoto.setOpaque(false);
        envolturaFoto.add(marcoFoto, BorderLayout.NORTH);
        envolturaFoto.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentResized(java.awt.event.ComponentEvent e) { marcoFoto.revalidate(); }
        });
        c.gridx = 0;
        c.weightx = 0.45;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 0, 40);
        columnas.add(envolturaFoto, c);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        info.add(chipOrigen(franquicia));
        info.add(Box.createVerticalStrut(16));

        JTextArea titulo = textoLargo(platillo.getName(), Theme.titulo(40), Theme.TINTA);
        info.add(titulo);
        info.add(Box.createVerticalStrut(8));

        JLabel lblPrecio = Theme.etiqueta(dinero(precioUnitario), Theme.mono(30), Theme.TINTA);
        lblPrecio.setAlignmentX(LEFT_ALIGNMENT);
        info.add(lblPrecio);
        info.add(Box.createVerticalStrut(20));

        // La descripción trae "Cantidad" y "Calorías" en líneas aparte: se respetan los \n
        JTextArea descripcion = textoLargo(platillo.getDescription(), Theme.texto(18), Theme.GRAFITO);
        info.add(descripcion);

        JPanel envolturaInfo = new JPanel(new BorderLayout());   // NORTH: que el texto no se estire a lo alto
        envolturaInfo.setOpaque(false);
        envolturaInfo.add(info, BorderLayout.NORTH);
        c.gridx = 1;
        c.weightx = 0.55;
        c.insets = new Insets(0, 0, 0, 0);
        columnas.add(envolturaInfo, c);
        contenido.add(columnas, BorderLayout.CENTER);

        add(Theme.scroll(contenido), BorderLayout.CENTER);
        ((javax.swing.JScrollPane) getComponent(0)).getViewport().setBackground(fondo);

        // ══════ Barra inferior fija: total + cantidad + agregar ══════
        add(crearBarraInferior(platillo), BorderLayout.SOUTH);
    }

    private JPanel crearBarraInferior(Dishes platillo) {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(Theme.BLANCO);
        barra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(Theme.BORDE, 0, 0, 0, Theme.TINTA),
                BorderFactory.createEmptyBorder(14, Theme.MARGEN, 14, Theme.MARGEN)));

        JPanel totales = new JPanel();
        totales.setOpaque(false);
        totales.setLayout(new BoxLayout(totales, BoxLayout.Y_AXIS));
        totales.add(Theme.etiqueta("TOTAL ESTIMADO", Theme.mono(13), Theme.GRAFITO));
        lblTotalNumerico = Theme.etiqueta(dinero(precioUnitario * cantidadActual), Theme.mono(30), Theme.TINTA);
        totales.add(lblTotalNumerico);
        barra.add(totales, BorderLayout.WEST);

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controles.setOpaque(false);

        Buttons btnMenos = Buttons.secundario("−");
        btnMenos.setFont(Theme.titulo(22));
        btnMenos.setPreferredSize(new Dimension(52 + Theme.SOMBRA, 52 + Theme.SOMBRA));
        btnMenos.setToolTipText("Quitar uno");
        btnMenos.addActionListener(e -> {
            if (cantidadActual > 1) {
                cantidadActual--;
                actualizarComponentes();
            }
        });

        lblCantidad = Theme.etiqueta(String.valueOf(cantidadActual), Theme.mono(26), Theme.TINTA);
        lblCantidad.setHorizontalAlignment(SwingConstants.CENTER);
        lblCantidad.setPreferredSize(new Dimension(56, 52));

        Buttons btnMas = Buttons.secundario("+");
        btnMas.setFont(Theme.titulo(22));
        btnMas.setPreferredSize(new Dimension(52 + Theme.SOMBRA, 52 + Theme.SOMBRA));
        btnMas.setToolTipText("Agregar uno");
        btnMas.addActionListener(e -> {
            cantidadActual++;
            actualizarComponentes();
        });

        Buttons btnAñadir = Buttons.acento("Añadir al carrito");
        btnAñadir.setFont(Theme.titulo(18));
        btnAñadir.setPreferredSize(new Dimension(260 + Theme.SOMBRA, 52 + Theme.SOMBRA));
        btnAñadir.addActionListener(e -> {
            Carrito.agregarAlCarrito(platillo, cantidadActual);
            Main_Frame.actualizarCarrito();                       // el header muestra "Carrito (n)" al momento
            Toast.exito(OrderDetails_2.this, "Se agregaron " + cantidadActual + "× " + platillo.getName() + " al carrito");
        });

        controles.add(btnMenos);
        controles.add(lblCantidad);
        controles.add(btnMas);
        controles.add(Box.createHorizontalStrut(16));
        controles.add(btnAñadir);
        barra.add(controles, BorderLayout.EAST);
        return barra;
    }

    /** "ORIGEN · Los Simpson" con el logo de la serie, en un recuadro con borde. */
    private JPanel chipOrigen(Franchises franquicia) {
        JPanel chip = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        chip.setBackground(Theme.BLANCO);
        chip.setBorder(BorderFactory.createCompoundBorder(Theme.borde(), BorderFactory.createEmptyBorder(8, 0, 8, 8)));
        Imagen logo = new Imagen(franquicia.getLogo(), false);
        logo.setPreferredSize(new Dimension(44, 44));
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(Theme.etiqueta("ORIGEN DE LA SERIE", Theme.mono(12), Theme.GRAFITO));
        textos.add(Theme.etiqueta(franquicia.getName(), Theme.titulo(18), Theme.TINTA));
        chip.add(logo);
        chip.add(textos);
        chip.setAlignmentX(LEFT_ALIGNMENT);
        chip.setMaximumSize(chip.getPreferredSize());
        return chip;
    }

    private static JTextArea textoLargo(String texto, java.awt.Font fuente, Color color) {
        JTextArea t = new JTextArea(texto);
        t.setLineWrap(true);
        t.setWrapStyleWord(true);
        t.setEditable(false);
        t.setFocusable(false);
        t.setOpaque(false);
        t.setBorder(null);
        t.setFont(fuente);
        t.setForeground(color);
        t.setAlignmentX(LEFT_ALIGNMENT);
        return t;
    }

    private void actualizarComponentes() {
        lblCantidad.setText(String.valueOf(cantidadActual));
        lblTotalNumerico.setText(dinero(precioUnitario * cantidadActual));
    }

    private static String dinero(double monto) {
        return String.format(MX, "$%,.2f", monto);
    }
}
