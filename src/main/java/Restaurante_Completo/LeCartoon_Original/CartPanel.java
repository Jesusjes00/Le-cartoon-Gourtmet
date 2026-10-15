package Restaurante_Completo.LeCartoon_Original;

import javax.swing.*;
import javax.swing.border.*;

import ProfileScreens.EditProfile;
import Restaurante_Completo.LeCartoon_Original.Carrito.ItemPedido;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.sql.SQLException;

public class CartPanel extends JPanel {
	private ArrayList<ItemPedido> listaPedidos;
	private double total = 0;

    private static final long serialVersionUID = 1L;

    //Paleta
    private static final Color BG= Color.WHITE;
    private static final Color NEGRO = new Color(15, 15, 15);
    private static final Color GRIS_BORDE = new Color(225, 220, 215);
    private static final Color GRIS_TEXTO = new Color(120, 115, 110);
    private static final Color GRIS_CLARO = new Color(248, 248, 248);
    private static final Color NARANJA = new Color(211, 84, 0);    

    public CartPanel(ArrayList<ItemPedido> listaPedidos) {
    	this.listaPedidos = listaPedidos;
    	this.total = 0; 
    	
        setBackground(BG);
        setLayout(new BorderLayout(0, 0));

        // Scroll wrapper del contenido central 
        JPanel contenido = new JPanel();
        contenido.setBackground(BG);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBorder(new EmptyBorder(40, 60, 40, 60));

        // Encabezado
        JLabel titulo = new JLabel("Finalizar Pedido");
        titulo.setFont(new Font("Arial Black", Font.BOLD, 32));
        titulo.setForeground(NEGRO);
        titulo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel("Revisa tu selección inspirada en la gran pantalla antes de confirmar.");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(GRIS_TEXTO);
        subtitulo.setAlignmentX(LEFT_ALIGNMENT);

        contenido.add(titulo);
        contenido.add(Box.createVerticalStrut(6));
        contenido.add(subtitulo);
        contenido.add(Box.createVerticalStrut(30));

        // Fila principal: panel izquierdo + panel derecho
        JPanel filaMain = new JPanel(new BorderLayout(24, 0));
        filaMain.setBackground(BG);
        filaMain.setAlignmentX(LEFT_ALIGNMENT);
        filaMain.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        
        // PANEL IZQUIERDO de  pedido
        
        JPanel panelPedido = crearTarjeta();
        panelPedido.setLayout(new BoxLayout(panelPedido, BoxLayout.Y_AXIS));
        panelPedido.setBorder(new CompoundBorder(
                new LineBorder(GRIS_BORDE, 1, true),
                new EmptyBorder(24, 24, 24, 24)
        ));

        // Encabezado del panel izquierdo
        JPanel headerPedido = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        headerPedido.setBackground(BG);
        headerPedido.setAlignmentX(LEFT_ALIGNMENT);
        headerPedido.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JLabel iconoPedido = new JLabel("✕");
        iconoPedido.setFont(new Font("SansSerif", Font.BOLD, 18));
        iconoPedido.setForeground(NARANJA);

        JLabel lblTuPedido = new JLabel("Tu Pedido");
        lblTuPedido.setFont(new Font("Arial Black", Font.BOLD, 18));
        lblTuPedido.setForeground(NEGRO);

        headerPedido.add(iconoPedido);
        headerPedido.add(lblTuPedido);

        panelPedido.add(headerPedido);
        panelPedido.add(Box.createVerticalStrut(18));

        
        for(ItemPedido item : listaPedidos) {	                
        	
	        //creación de item card
        	        	
	        panelPedido.add(crearItemCard(
	                "",
	                item.getPlatillo().getName(),
	                item.getPlatillo().getDescription(),
	                "$" +(item.getPlatillo().getPrice()*item.getCantidad()),
	                item.getCantidad()
	        ));
	
	        panelPedido.add(Box.createVerticalStrut(4));
	        panelPedido.add(crearSeparadorFino());
	        panelPedido.add(Box.createVerticalStrut(4));
	        
	        total += item.getPlatillo().getPrice()*item.getCantidad();
	        
	        BigDecimal bd = new BigDecimal(total);
	        bd = bd.setScale(2, RoundingMode.HALF_UP);
	        total = bd.doubleValue(); // 30.89
	        
        }

       

      
        // PANEL de Resumen
       
        JPanel panelResumen = crearTarjeta();
        panelResumen.setLayout(new BoxLayout(panelResumen, BoxLayout.Y_AXIS));
        panelResumen.setBorder(new CompoundBorder(
                new LineBorder(GRIS_BORDE, 1, true),
                new EmptyBorder(24, 24, 24, 24)
        ));
        panelResumen.setPreferredSize(new Dimension(320, 0));
        panelResumen.setMaximumSize(new Dimension(320, Integer.MAX_VALUE));
        panelResumen.setMinimumSize(new Dimension(320, 0));

        JLabel lblResumen = new JLabel("Resumen");
        lblResumen.setFont(new Font("Arial Black", Font.BOLD, 16));
        lblResumen.setForeground(NEGRO);
        lblResumen.setAlignmentX(LEFT_ALIGNMENT);
        panelResumen.add(lblResumen);
        panelResumen.add(Box.createVerticalStrut(20));

        // Filas de precios
        panelResumen.add(crearFilaPrecio("Subtotal",    "$" + String.valueOf(total), false));
        panelResumen.add(Box.createVerticalStrut(10));
        panelResumen.add(crearFilaPrecio("Delivery Fee","$2.50",  false));
        panelResumen.add(Box.createVerticalStrut(10));
        panelResumen.add(crearFilaPrecio("iva",       "$16",  false));
        panelResumen.add(Box.createVerticalStrut(14));
        panelResumen.add(crearSeparadorFino());
        panelResumen.add(Box.createVerticalStrut(14));
        panelResumen.add(crearFilaPrecio("Total",       "$" + String.valueOf((total+18.50)), true));  // destacado
        panelResumen.add(Box.createVerticalStrut(24));

        // Dirección de entrega
        panelResumen.add(crearInfoBox("📦", "DIRECCIÓN DE ENTREGA", Restaurant.getCostumer().getAddress()));
        panelResumen.add(Box.createVerticalStrut(10));

        // Método de pago
        panelResumen.add(crearInfoBox("💳", "MÉTODO DE PAGO", "Visa **** 1234"));
        panelResumen.add(Box.createVerticalStrut(28));

        // Botón Realizar Pedido
        Buttons btnRealizarPedido = new Buttons("Realizar Pedido", 30, 30, NARANJA, new Color(180, 60, 0));
        btnRealizarPedido.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				if(listaPedidos.size() != 0) {
					try {
					    int id = Restaurant.registrarPedido(total + 18.5, listaPedidos);
					    JOptionPane.showMessageDialog(null, "Tu pedido #" + id + " fue realizado");
					    Carrito.vaciar();
					    Main_Frame.changeDynamicPanel(new MainFramePanel());
					} catch (SQLException ex) {
					    ex.printStackTrace();
					    JOptionPane.showMessageDialog(null, "No se pudo registrar tu pedido. Intenta de nuevo.");
					}
				}else {
					JOptionPane.showMessageDialog(null, "No has agregado productos a tu carrito");
				}
								
			}
		});
        btnRealizarPedido.setAlignmentX(LEFT_ALIGNMENT);
        btnRealizarPedido.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btnRealizarPedido.setPreferredSize(new Dimension(260, 48));
        btnRealizarPedido.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnRealizarPedido.setForeground(Color.WHITE);
        btnRealizarPedido.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panelResumen.add(btnRealizarPedido);

        panelResumen.add(Box.createVerticalStrut(12));
      

        panelResumen.add(Box.createVerticalStrut(16));

        // Badge pago seguro
        JPanel badgePago = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        badgePago.setBackground(BG);
        badgePago.setAlignmentX(CENTER_ALIGNMENT);
        badgePago.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        JLabel iconoPago = new JLabel("🔒");
        iconoPago.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JLabel lblPagoSeguro = new JLabel("PAGO SEGURO");
        lblPagoSeguro.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblPagoSeguro.setForeground(GRIS_TEXTO);

        badgePago.add(iconoPago);
        badgePago.add(lblPagoSeguro);
        panelResumen.add(badgePago);

        //Ensamblar fila principal
        filaMain.add(panelPedido,  BorderLayout.CENTER);
        filaMain.add(panelResumen, BorderLayout.EAST);
        contenido.add(filaMain);

        // JScrollPane 
        JScrollPane scrollPane = new JScrollPane(contenido) {
            @Override
            public JScrollBar createVerticalScrollBar() {
                JScrollBar bar = super.createVerticalScrollBar();
                bar.setPreferredSize(new Dimension(6, Integer.MAX_VALUE));
                bar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
                    @Override
                    protected void configureScrollBarColors() {
                        this.thumbColor            = new Color(180, 180, 180);
                        this.trackColor            = new Color(245, 245, 245);
                        this.thumbDarkShadowColor  = new Color(0, 0, 0, 0);
                        this.thumbHighlightColor   = new Color(0, 0, 0, 0);
                        this.thumbLightShadowColor = new Color(0, 0, 0, 0);
                    }
                    @Override
                    protected JButton createDecreaseButton(int o) { return botonInvisible(); }
                    @Override
                    protected JButton createIncreaseButton(int o) { return botonInvisible(); }
                    @Override
                    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                        if (r.isEmpty() || !scrollbar.isEnabled()) return;
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(isDragging ? new Color(120, 120, 120) : thumbColor);
                        g2.fillRoundRect(r.x + 1, r.y + 2, r.width - 2, r.height - 4, 8, 8);
                        g2.dispose();
                    }
                    @Override
                    protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(trackColor);
                        g2.fillRect(r.x, r.y, r.width, r.height);
                        g2.dispose();
                    }
                    private JButton botonInvisible() {
                        JButton b = new JButton();
                        b.setPreferredSize(new Dimension(0, 0));
                        b.setMinimumSize(new Dimension(0, 0));
                        b.setMaximumSize(new Dimension(0, 0));
                        return b;
                    }
                });
                return bar;
            }
        };
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setBackground(BG);
        scrollPane.getViewport().setBackground(BG);

        add(scrollPane, BorderLayout.CENTER);
    }

    
    // HELPERS DE CONSTRUCCIÓN
    

    //Tarjeta blanca base 
    private JPanel crearTarjeta() {
        JPanel p = new JPanel();
        p.setBackground(BG);
        return p;
    }

    //Línea divisoria fina gris. 
    private JPanel crearSeparadorFino() {
        JPanel sep = new JPanel();
        sep.setBackground(GRIS_BORDE);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setPreferredSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }
    
    private JPanel crearItemCard(String badge, String nombre, String descripcion,
                                  String precio, int cantidad) {

        JPanel card = new JPanel(new BorderLayout(16, 0));
        card.setBackground(BG);
        card.setAlignmentX(LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setBorder(new EmptyBorder(12, 0, 12, 0));

        // Imagen placeholder 
        JPanel imgBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GRIS_CLARO);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(GRIS_BORDE);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
                // ícono de imagen genérico
                g2.setColor(GRIS_TEXTO);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 22));
                FontMetrics fm = g2.getFontMetrics();
                String ico = "🖼";
                g2.drawString(ico, (getWidth() - fm.stringWidth(ico)) / 2,
                        (getHeight() + fm.getAscent()) / 2 - 2);
                g2.dispose();
            }
        };
        imgBox.setPreferredSize(new Dimension(72, 72));
        imgBox.setMaximumSize(new Dimension(72, 72));
        imgBox.setOpaque(false);

        // Columna de información
        JPanel info = new JPanel();
        info.setBackground(BG);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        // Badge de categoría
        JLabel lblBadge = new JLabel(badge);
        lblBadge.setFont(new Font("SansSerif", Font.BOLD, 9));
        lblBadge.setForeground(NARANJA);
        lblBadge.setBorder(new EmptyBorder(0, 0, 4, 0));
        lblBadge.setAlignmentX(LEFT_ALIGNMENT);

        // Nombre del platillo
        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("Arial Black", Font.BOLD, 14));
        lblNombre.setForeground(NEGRO);
        lblNombre.setAlignmentX(LEFT_ALIGNMENT);

        // Descripción
        JLabel lblDesc = new JLabel(descripcion );
        lblDesc.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblDesc.setForeground(GRIS_TEXTO);
        lblDesc.setAlignmentX(LEFT_ALIGNMENT);

        // Fila de controles: [ — ] cantidad [ + ]   Eliminar
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        controles.setBackground(BG);
        controles.setAlignmentX(LEFT_ALIGNMENT);
        

        // Cantidad
        JLabel lblCantidad = new JLabel("x" + String.valueOf(cantidad));
        lblCantidad.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblCantidad.setForeground(NEGRO);
        lblCantidad.setHorizontalAlignment(SwingConstants.CENTER);
        lblCantidad.setPreferredSize(new Dimension(24, 28));               
               
        controles.add(lblCantidad);      
        controles.add(Box.createHorizontalStrut(8));        

        info.add(lblBadge);
        info.add(lblNombre);
        info.add(Box.createVerticalStrut(3));
        info.add(lblDesc);
        info.add(Box.createVerticalStrut(8));
        info.add(controles);

        // Precio (derecha) 
      
        JLabel lblPrecio = new JLabel(String.valueOf(precio));
        lblPrecio.setFont(new Font("Arial Black", Font.BOLD, 14));
        lblPrecio.setForeground(NEGRO);
        lblPrecio.setVerticalAlignment(SwingConstants.TOP);
        lblPrecio.setBorder(new EmptyBorder(14, 0, 0, 0));

        card.add(imgBox, BorderLayout.WEST);
        card.add(info,   BorderLayout.CENTER);
        card.add(lblPrecio, BorderLayout.EAST);

        return card;
    }

    private JPanel crearFilaPrecio(String label, String valor, boolean destacado) {
        JPanel fila = new JPanel(new BorderLayout(0, 0));
        fila.setBackground(BG);
        fila.setAlignmentX(LEFT_ALIGNMENT);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        Font fuente = destacado
                ? new Font("Arial Black", Font.BOLD, 16)
                : new Font("SansSerif", Font.PLAIN, 13);

        JLabel lbl = new JLabel(label);
        lbl.setFont(fuente);
        lbl.setForeground(destacado ? NEGRO : GRIS_TEXTO);

        JLabel val = new JLabel(valor);
        val.setFont(fuente);
        val.setForeground(destacado ? NARANJA : NEGRO);
        val.setHorizontalAlignment(SwingConstants.RIGHT);

        fila.add(lbl, BorderLayout.WEST);
        fila.add(val, BorderLayout.EAST);
        return fila;
    }

 
    private JPanel crearInfoBox(String icono, String tituloBox, String valor) {
        // Contenedor con borde redondeado pintado a mano
        JPanel box = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GRIS_CLARO);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(GRIS_BORDE);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        box.setOpaque(false);
        box.setLayout(new BorderLayout(12, 0));
        box.setBorder(new EmptyBorder(12, 14, 12, 14));
        box.setAlignmentX(LEFT_ALIGNMENT);
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Ícono
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblIcono.setForeground(NARANJA);

        // Texto (título + valor)
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel lblTitBox = new JLabel(tituloBox);
        lblTitBox.setFont(new Font("SansSerif", Font.BOLD, 9));
        lblTitBox.setForeground(GRIS_TEXTO);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblValor.setForeground(NEGRO);

        textos.add(lblTitBox);
        textos.add(Box.createVerticalStrut(2));
        textos.add(lblValor);

        box.add(lblIcono, BorderLayout.WEST);
        box.add(textos,   BorderLayout.CENTER);
        return box;
    }
}