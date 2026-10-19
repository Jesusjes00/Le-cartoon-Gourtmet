package Restaurante_Completo.LeCartoon_Original;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import Dishes.*;
import Franchises.*;
import ui.Toast;

public class OrderDetails_2 extends JPanel {
    private int cantidadActual = 1;
    private double precioUnitario;
    private JLabel lblCantidad;
    private JLabel lblTotalNumerico;

    public OrderDetails_2(Franchises franquicia, Dishes platillo, JPanel panelAnterior) {
        this.precioUnitario = platillo.getPrice();
        Color fondo =  Color.decode(franquicia.getColor());
        java.net.URL urlImagen = getClass().getResource(platillo.getImage());
        ImageIcon food1 = new ImageIcon(urlImagen);
        java.net.URL urlImagen1 = getClass().getResource(franquicia.getLogo());
        ImageIcon logo1 = new ImageIcon(urlImagen1);

        setLayout(new BorderLayout());
        setBackground(fondo);
        setPreferredSize(new Dimension(1920, 1000));

        JPanel pnlContenidoSuperior = new JPanel(new BorderLayout(40, 40));
        pnlContenidoSuperior.setBackground(fondo);
        pnlContenidoSuperior.setBorder(BorderFactory.createEmptyBorder(60, 60, 40, 60));

        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTop.setBackground(fondo);
        
        JLabel lblBack = new JLabel();
        lblBack.setPreferredSize(new Dimension(60, 60));
        lblBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        java.net.URL urlBack = getClass().getResource("/Icons/back.png");
        if (urlBack != null) {
            ImageIcon backIconOriginal = new ImageIcon(urlBack);
            Image backImg = backIconOriginal.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            lblBack.setIcon(new ImageIcon(backImg));
        } else {
            lblBack.setText("<");
            lblBack.setFont(new Font("SansSerif", Font.BOLD, 40));
            lblBack.setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        lblBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	Main_Frame.changeDynamicPanel(panelAnterior);
            }
        });
        pnlTop.add(lblBack);
        pnlContenidoSuperior.add(pnlTop, BorderLayout.NORTH);

        JPanel pnlImagenContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlImagenContainer.setBackground(fondo);
        JLabel lblImagen = new JLabel(food1);
        int anchoImagen = 640;
        int altoImagen = 340;
        lblImagen.setPreferredSize(new Dimension(anchoImagen, altoImagen));
        lblImagen.setMinimumSize(new Dimension(anchoImagen, altoImagen));
        lblImagen.setMaximumSize(new Dimension(anchoImagen, altoImagen));
        lblImagen.setOpaque(true);
        lblImagen.setBackground(fondo);
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        pnlImagenContainer.add(lblImagen);
        pnlContenidoSuperior.add(pnlImagenContainer, BorderLayout.WEST);

        JPanel pnlInfo = new JPanel();
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
        pnlInfo.setBackground(fondo);

        JLabel lblTitulo = new JLabel(platillo.getName());
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 46));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPrecio = new JLabel("$" + String.format("%.2f", precioUnitario));
        lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblPrecio.setForeground(new Color(230, 50, 50));
        lblPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel pnlOrigenContenedor = new JPanel();
        pnlOrigenContenedor.setLayout(new BoxLayout(pnlOrigenContenedor, BoxLayout.X_AXIS));
        pnlOrigenContenedor.setBackground(Color.WHITE);
        pnlOrigenContenedor.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 20)));
        pnlOrigenContenedor.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlOrigenContenedor.setMaximumSize(new Dimension(500, 80));

        JLabel lblIconoSerie = new JLabel(logo1);
        int tamañoImagen = 60;
        lblIconoSerie.setPreferredSize(new Dimension(tamañoImagen, tamañoImagen));
        lblIconoSerie.setMinimumSize(new Dimension(tamañoImagen, tamañoImagen));
        lblIconoSerie.setMaximumSize(new Dimension(tamañoImagen, tamañoImagen));
        lblIconoSerie.setOpaque(true);
        lblIconoSerie.setBackground(new Color(150, 200, 150));
        lblIconoSerie.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel pnlTextosOrigen = new JPanel();
        pnlTextosOrigen.setLayout(new BoxLayout(pnlTextosOrigen, BoxLayout.Y_AXIS));
        pnlTextosOrigen.setBackground(Color.WHITE);

        JLabel lblEtiquetaOrigen = new JLabel("Origen de la Serie");
        lblEtiquetaOrigen.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblEtiquetaOrigen.setForeground(new Color(0, 0, 247));
        lblEtiquetaOrigen.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblNombreSerie = new JLabel(franquicia.getName());
        lblNombreSerie.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblNombreSerie.setForeground(Color.BLACK);
        lblNombreSerie.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlTextosOrigen.add(lblEtiquetaOrigen);
        pnlTextosOrigen.add(Box.createVerticalStrut(5));
        pnlTextosOrigen.add(lblNombreSerie);

        pnlOrigenContenedor.add(lblIconoSerie);
        pnlOrigenContenedor.add(Box.createHorizontalStrut(15));
        pnlOrigenContenedor.add(pnlTextosOrigen);

        JTextPane txtDescripcion = new JTextPane();
        txtDescripcion.setText(platillo.getDescription());
        txtDescripcion.setEditable(false);
        txtDescripcion.setOpaque(false);
        txtDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 24));
        txtDescripcion.setMaximumSize(new Dimension(800, 300));
        txtDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        StyledDocument doc = txtDescripcion.getStyledDocument();
        SimpleAttributeSet leftAlign = new SimpleAttributeSet();
        StyleConstants.setAlignment(leftAlign, StyleConstants.ALIGN_LEFT);
        doc.setParagraphAttributes(0, doc.getLength(), leftAlign, false);

        pnlInfo.add(lblTitulo);
        pnlInfo.add(Box.createVerticalStrut(15));
        pnlInfo.add(lblPrecio);
        pnlInfo.add(Box.createVerticalStrut(25));
        pnlInfo.add(pnlOrigenContenedor);
        pnlInfo.add(Box.createVerticalStrut(40));
        pnlInfo.add(txtDescripcion);

        pnlContenidoSuperior.add(pnlInfo, BorderLayout.CENTER);
        
        add(pnlContenidoSuperior, BorderLayout.CENTER);

        JPanel pnlBarraInferior = new JPanel(new BorderLayout());
        pnlBarraInferior.setBackground(Color.WHITE);
        pnlBarraInferior.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(3, 0, 0, 0, Color.BLACK),
                BorderFactory.createEmptyBorder(20, 60, 20, 60)));

        JPanel pnlTotales = new JPanel();
        pnlTotales.setLayout(new BoxLayout(pnlTotales, BoxLayout.Y_AXIS));
        pnlTotales.setBackground(Color.WHITE);

        JLabel lblTextoTotal = new JLabel("TOTAL ESTIMADO");
        lblTextoTotal.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTextoTotal.setForeground(Color.DARK_GRAY);

        lblTotalNumerico = new JLabel("$" + String.format("%.2f", precioUnitario * cantidadActual));
        lblTotalNumerico.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblTotalNumerico.setForeground(Color.RED);

        pnlTotales.add(lblTextoTotal);
        pnlTotales.add(Box.createVerticalStrut(5));
        pnlTotales.add(lblTotalNumerico);

        pnlBarraInferior.add(pnlTotales, BorderLayout.WEST);

        JPanel pnlControles = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 10));
        pnlControles.setBackground(Color.WHITE);

        JButton btnMenos = crearBotonTransparente("-");
        btnMenos.addActionListener(e -> {
            if (cantidadActual > 1) {
                cantidadActual--;
                actualizarComponentes();
            }
        });

        lblCantidad = new JLabel(String.valueOf(cantidadActual));
        lblCantidad.setFont(new Font("SansSerif", Font.BOLD, 28));

        JButton btnMas = crearBotonTransparente("+");
        btnMas.addActionListener(e -> {
            cantidadActual++;
            actualizarComponentes();
        });

        Buttons btnAñadir = Buttons.acento("Añadir al carrito");
        btnAñadir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                Carrito.agregarAlCarrito(platillo, cantidadActual);
                
                Toast.exito(OrderDetails_2.this, "Se agregaron " + cantidadActual + "× " + platillo.getName() + " al carrito");
            }
        });
        btnAñadir.setPreferredSize(new Dimension(286, 61));

        pnlControles.add(btnMenos);
        pnlControles.add(lblCantidad);
        pnlControles.add(btnMas);
        pnlControles.add(Box.createHorizontalStrut(20));
        pnlControles.add(btnAñadir);
        
        pnlBarraInferior.add(pnlControles, BorderLayout.EAST);

        add(pnlBarraInferior, BorderLayout.SOUTH);
    }

    private void actualizarComponentes() {
        lblCantidad.setText(String.valueOf(cantidadActual));
        double total = precioUnitario * cantidadActual;
        lblTotalNumerico.setText("$" + String.format("%.2f", total));
    }

    private JButton crearBotonTransparente(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 36));
        boton.setForeground(Color.BLACK);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}