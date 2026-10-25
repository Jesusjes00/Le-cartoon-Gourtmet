package Restaurante_Completo.LeCartoon_Original;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Dishes.Dishes;
import Restaurante_Completo.LeCartoon_Original.Carrito.ItemPedido;
import persistence.DaoException;
import ui.Toast;
import ui.theme.Imagen;
import ui.theme.Lienzo;
import ui.theme.Tarjeta;
import ui.theme.Theme;

public class CartPanel extends JPanel {
	private ArrayList<ItemPedido> listaPedidos;

    private static final long serialVersionUID = 1L;

    private static final Color LINEA = new Color(0, 0, 0, 45);         // separador fino dentro de las tarjetas
    private static final Locale MX = Locale.forLanguageTag("es-MX");   // $1,234.50

    public CartPanel(ArrayList<ItemPedido> listaPedidos) {
    	this.listaPedidos = listaPedidos;

        setBackground(Theme.BLANCO);
        setLayout(new BorderLayout(0, 0));

        // Contenido que se estira al ancho del scroll
        Lienzo contenido = new Lienzo(null);
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBorder(new EmptyBorder(28, Theme.MARGEN, 40, Theme.MARGEN));

        // Encabezado
        JLabel titulo = Theme.etiqueta("Tu carrito", Theme.titulo(30), Theme.TINTA);
        JLabel subtitulo = Theme.etiqueta("Revisa tu selección inspirada en la gran pantalla antes de confirmar.",
                Theme.texto(16), Theme.GRAFITO);
        agregar(contenido, titulo);
        contenido.add(Box.createVerticalStrut(4));
        agregar(contenido, subtitulo);
        contenido.add(Box.createVerticalStrut(24));

        // Carrito vacío: en vez de un resumen en $0, una invitación a explorar
        if (Carrito.estaVacio()) {
            contenido.add(crearEstadoVacio());
            add(Theme.scroll(contenido), BorderLayout.CENTER);
            return;
        }

        // Fila principal: pedido (se estira) + resumen (ancho fijo)
        JPanel filaMain = new JPanel(new BorderLayout(28, 0));
        filaMain.setOpaque(false);

        // ── PANEL IZQUIERDO: el pedido ──
        Tarjeta panelPedido = new Tarjeta(null);
        panelPedido.setLayout(new BoxLayout(panelPedido, BoxLayout.Y_AXIS));

        // Encabezado: "Tu pedido" + chip de artículos | Vaciar carrito
        JPanel headerPedido = new JPanel(new BorderLayout(8, 0));
        headerPedido.setOpaque(false);

        JPanel tituloPedido = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        tituloPedido.setOpaque(false);
        int unidades = Carrito.totalUnidades();
        Buttons chipArticulos = Buttons.chip(unidades + (unidades == 1 ? " ARTÍCULO" : " ARTÍCULOS"));
        chipArticulos.setPreferredSize(new Dimension(140, 32));
        tituloPedido.add(Theme.etiqueta("Tu pedido", Theme.titulo(20), Theme.TINTA));
        tituloPedido.add(chipArticulos);

        Buttons btnVaciar = Buttons.peligro("Vaciar carrito");
        btnVaciar.setPreferredSize(new Dimension(170, 40 + Theme.SOMBRA));
        btnVaciar.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(CartPanel.this,
                    "¿Quitar todos los platillos del carrito?", "Vaciar carrito",
                    JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                Carrito.vaciar();
                refrescar();
            }
        });

        headerPedido.add(tituloPedido, BorderLayout.WEST);
        headerPedido.add(btnVaciar, BorderLayout.EAST);
        agregar(panelPedido, headerPedido);
        panelPedido.add(Box.createVerticalStrut(12));

        for (ItemPedido item : listaPedidos) {
            agregar(panelPedido, crearSeparadorFino(LINEA, 2));
            agregar(panelPedido, crearItemCard(item));          // la tarjeta trae sus botones − + Quitar
        }

        // ── PANEL DERECHO: resumen ──
        Tarjeta panelResumen = new Tarjeta(null) {
            @Override public Dimension getPreferredSize() {          // ancho fijo, alto el que pida el contenido
                return new Dimension(360, super.getPreferredSize().height);
            }
        };
        panelResumen.setLayout(new BoxLayout(panelResumen, BoxLayout.Y_AXIS));

        agregar(panelResumen, Theme.etiqueta("Resumen", Theme.titulo(20), Theme.TINTA));
        panelResumen.add(Box.createVerticalStrut(18));

        agregar(panelResumen, crearFilaPrecio("Subtotal",   dinero(Carrito.subtotal()), false));
        panelResumen.add(Box.createVerticalStrut(8));
        agregar(panelResumen, crearFilaPrecio("IVA (16 %)", dinero(Carrito.iva()),      false));
        panelResumen.add(Box.createVerticalStrut(8));
        agregar(panelResumen, crearFilaPrecio("Envío",      dinero(Carrito.envio()),    false));
        panelResumen.add(Box.createVerticalStrut(14));
        agregar(panelResumen, crearSeparadorFino(Theme.TINTA, Theme.BORDE));
        panelResumen.add(Box.createVerticalStrut(14));
        agregar(panelResumen, crearFilaPrecio("Total", dinero(Carrito.totalAPagar()), true));  // destacado
        panelResumen.add(Box.createVerticalStrut(4));
        agregar(panelResumen, Theme.etiqueta("Precios con IVA incluido", Theme.texto(12), Theme.GRAFITO));
        panelResumen.add(Box.createVerticalStrut(20));

        agregar(panelResumen, crearInfoBox("localizacion.png", "DIRECCIÓN DE ENTREGA",
                Restaurant.getInstance().getCliente().getAddress()));
        panelResumen.add(Box.createVerticalStrut(10));
        agregar(panelResumen, crearInfoBox("card.png", "MÉTODO DE PAGO", "Visa •••• 1234"));
        panelResumen.add(Box.createVerticalStrut(24));

        // Botón Realizar Pedido: la acción principal de la pantalla (terracota)
        Buttons btnRealizarPedido = Buttons.acento("Realizar pedido");
        btnRealizarPedido.setFont(Theme.titulo(18));
        btnRealizarPedido.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!Carrito.estaVacio()) {
					try {
						int id = Restaurant.getInstance().registrarPedido(Carrito.totalAPagar(), listaPedidos);
					    Toast.exito(CartPanel.this, "Tu pedido #" + id + " fue realizado");
					    Carrito.vaciar();
					    Main_Frame.changeDynamicPanel(new MainFramePanel());
					} catch (DaoException ex) {
					    ex.printStackTrace();
					    JOptionPane.showMessageDialog(null, "No se pudo registrar tu pedido. Intenta de nuevo.");
					}
				}else {
					Toast.aviso(CartPanel.this, "No has agregado productos a tu carrito");
				}
			}
		});
        btnRealizarPedido.setPreferredSize(new Dimension(300, 52 + Theme.SOMBRA));
        agregar(panelResumen, btnRealizarPedido);
        panelResumen.add(Box.createVerticalStrut(12));

        JLabel lblPagoSeguro = Theme.etiqueta("✓ PAGO SEGURO", Theme.mono(12), Theme.GRAFITO);
        lblPagoSeguro.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel badge = new JPanel(new BorderLayout());
        badge.setOpaque(false);
        badge.add(lblPagoSeguro, BorderLayout.CENTER);
        agregar(panelResumen, badge);

        // Ensamblar: el resumen va arriba a la derecha y no se estira a lo alto
        JPanel columnaResumen = new JPanel(new BorderLayout());
        columnaResumen.setOpaque(false);
        columnaResumen.add(panelResumen, BorderLayout.NORTH);
        JPanel columnaPedido = new JPanel(new BorderLayout());
        columnaPedido.setOpaque(false);
        columnaPedido.add(panelPedido, BorderLayout.NORTH);

        filaMain.add(columnaPedido,  BorderLayout.CENTER);
        filaMain.add(columnaResumen, BorderLayout.EAST);
        agregar(contenido, filaMain);

        add(Theme.scroll(contenido), BorderLayout.CENTER);
    }

    // ACCIONES DEL CARRITO

    /** Vuelve a construir la pantalla con el estado actual del carrito (y el contador del header). */
    private void refrescar() {
        Main_Frame.changeDynamicPanel(new CartPanel(Carrito.getPedidos()));
    }

    /** $1,234.50 — siempre dos decimales y separador de miles. */
    private static String dinero(double monto) {
        return String.format(MX, "$%,.2f", monto);
    }

    /** Tarjeta que se muestra cuando no hay platillos. */
    private JPanel crearEstadoVacio() {
        Tarjeta caja = new Tarjeta(null);
        caja.setLayout(new BoxLayout(caja, BoxLayout.Y_AXIS));
        caja.setAlignmentX(LEFT_ALIGNMENT);
        caja.setMaximumSize(new Dimension(560, 240));

        JLabel lblVacio = new JLabel("Tu carrito está vacío");
        lblVacio.setFont(Theme.titulo(24));
        lblVacio.setForeground(Theme.TINTA);
        lblVacio.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblInvitacion = new JLabel("Agrega platillos desde cualquiera de nuestras cocinas.");
        lblInvitacion.setFont(Theme.texto(14));
        lblInvitacion.setForeground(Theme.TINTA);
        lblInvitacion.setAlignmentX(LEFT_ALIGNMENT);

        Buttons btnExplorar = Buttons.primario("Explorar cocinas →");
        btnExplorar.setAlignmentX(LEFT_ALIGNMENT);
        btnExplorar.setMaximumSize(new Dimension(246, 52));
        btnExplorar.addActionListener(e -> Main_Frame.changeDynamicPanel(new MainFramePanel()));

        caja.add(lblVacio);
        caja.add(Box.createVerticalStrut(8));
        caja.add(lblInvitacion);
        caja.add(Box.createVerticalStrut(24));
        caja.add(btnExplorar);
        return caja;
    }

    // HELPERS DE CONSTRUCCIÓN

    /** BoxLayout: alinear a la izquierda y estirar a lo ancho, pero no a lo alto. */
    private static void agregar(JPanel destino, JComponent c) {
        c.setAlignmentX(LEFT_ALIGNMENT);
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
        destino.add(c);
    }

    private JPanel crearSeparadorFino(Color color, int grosor) {
        JPanel sep = new JPanel();
        sep.setBackground(color);
        sep.setPreferredSize(new Dimension(10, grosor));
        return sep;
    }

    /** Un platillo: foto | nombre, descripción y − n + Quitar | importe. */
    private JPanel crearItemCard(ItemPedido item) {
        Dishes platillo = item.getPlatillo();
        int id = platillo.getId();
        int cantidad = item.getCantidad();

        JPanel card = new JPanel(new BorderLayout(18, 0));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(14, 0, 14, 0));

        // Foto real del platillo (antes era un recuadro gris con un emoji)
        Imagen foto = new Imagen(platillo.getImage(), true);
        foto.setPreferredSize(new Dimension(132, 70));
        JPanel marcoFoto = new JPanel(new BorderLayout());
        marcoFoto.setOpaque(false);
        marcoFoto.add(foto, BorderLayout.NORTH);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel lblNombre = Theme.etiqueta(platillo.getName(), Theme.titulo(17), Theme.TINTA);
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Solo la primera línea de la descripción (la cantidad y calorías van en las siguientes)
        String desc = platillo.getDescription().split("\n")[0];
        JLabel lblDesc = Theme.etiqueta(desc, Theme.texto(13), Theme.GRAFITO);
        lblDesc.setToolTipText(platillo.getDescription().replace("\n", " · "));
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDesc.setMinimumSize(new Dimension(50, lblDesc.getPreferredSize().height));
        lblDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, lblDesc.getPreferredSize().height));

        // Fila de controles: [ − ] cantidad [ + ]   [ Quitar ]
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        controles.setOpaque(false);
        controles.setAlignmentX(Component.LEFT_ALIGNMENT);

        Buttons btnMenos = Buttons.secundario("−");
        btnMenos.setPreferredSize(new Dimension(42, 40));
        btnMenos.setToolTipText(cantidad == 1 ? "Quitar del carrito" : "Quitar uno");
        btnMenos.addActionListener(e -> {                   // en 1, bajar = quitar (lo decide Carrito)
            Carrito.cambiarCantidad(id, cantidad - 1);
            refrescar();
        });

        JLabel lblCantidad = Theme.etiqueta(String.valueOf(cantidad), Theme.mono(16), Theme.TINTA);
        lblCantidad.setHorizontalAlignment(SwingConstants.CENTER);
        lblCantidad.setPreferredSize(new Dimension(36, 34));

        Buttons btnMas = Buttons.secundario("+");
        btnMas.setPreferredSize(new Dimension(42, 40));
        btnMas.setToolTipText("Agregar uno");
        btnMas.addActionListener(e -> {
            Carrito.cambiarCantidad(id, cantidad + 1);
            refrescar();
        });

        Buttons btnQuitar = Buttons.peligro("Quitar");
        btnQuitar.setPreferredSize(new Dimension(100, 40));
        btnQuitar.addActionListener(e -> {
            Carrito.quitar(id);
            refrescar();
        });

        controles.add(btnMenos);
        controles.add(lblCantidad);
        controles.add(btnMas);
        controles.add(Box.createHorizontalStrut(12));
        controles.add(btnQuitar);
        controles.setMaximumSize(new Dimension(Integer.MAX_VALUE, controles.getPreferredSize().height));

        info.add(lblNombre);
        info.add(Box.createVerticalStrut(2));
        info.add(lblDesc);
        info.add(Box.createVerticalStrut(10));
        info.add(controles);

        // Importe (derecha): precio × cantidad, y abajo el precio unitario si hay más de uno
        JPanel importe = new JPanel();
        importe.setOpaque(false);
        importe.setLayout(new BoxLayout(importe, BoxLayout.Y_AXIS));
        JLabel lblImporte = Theme.etiqueta(dinero(platillo.getPrice() * cantidad), Theme.mono(17), Theme.TINTA);
        lblImporte.setAlignmentX(Component.RIGHT_ALIGNMENT);
        importe.add(lblImporte);
        if (cantidad > 1) {
            JLabel lblUnitario = Theme.etiqueta(dinero(platillo.getPrice()) + " c/u", Theme.texto(12), Theme.GRAFITO);
            lblUnitario.setAlignmentX(Component.RIGHT_ALIGNMENT);
            importe.add(lblUnitario);
        }

        card.add(marcoFoto, BorderLayout.WEST);
        card.add(info,      BorderLayout.CENTER);
        card.add(importe,   BorderLayout.EAST);
        return card;
    }

    private JPanel crearFilaPrecio(String label, String valor, boolean destacado) {
        JPanel fila = new JPanel(new BorderLayout(0, 0));
        fila.setOpaque(false);

        JLabel lbl = Theme.etiqueta(label, destacado ? Theme.titulo(20) : Theme.texto(15),
                destacado ? Theme.TINTA : Theme.GRAFITO);
        JLabel val = Theme.etiqueta(valor, Theme.mono(destacado ? 22 : 15), Theme.TINTA);
        val.setHorizontalAlignment(SwingConstants.RIGHT);

        fila.add(lbl, BorderLayout.WEST);
        fila.add(val, BorderLayout.EAST);
        return fila;
    }

    /** Recuadro blanco con borde: ícono + título pequeño + valor. */
    private JPanel crearInfoBox(String icono, String tituloBox, String valor) {
        JPanel box = new JPanel(new BorderLayout(12, 0));
        box.setBackground(Theme.BLANCO);
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.TINTA, 2),
                new EmptyBorder(10, 12, 10, 12)));

        JLabel lblIcono = new JLabel(Theme.icono(icono, 22, Theme.TERRACOTA));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(Theme.etiqueta(tituloBox, Theme.mono(11), Theme.GRAFITO));
        textos.add(Box.createVerticalStrut(2));
        JLabel lblValor = Theme.etiqueta(valor, Theme.titulo(14), Theme.TINTA);
        lblValor.setToolTipText(valor);
        textos.add(lblValor);

        box.add(lblIcono, BorderLayout.WEST);
        box.add(textos,   BorderLayout.CENTER);
        return box;
    }
}
