package ProfileScreens;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import OrderDetails.Details;
import Restaurante_Completo.LeCartoon_Original.Buttons;
import Restaurante_Completo.LeCartoon_Original.Main_Frame;
import Restaurante_Completo.LeCartoon_Original.Order;
import Restaurante_Completo.LeCartoon_Original.OrderDetails;
import Restaurante_Completo.LeCartoon_Original.Restaurant;
import persistence.DaoException;
import ui.Toast;
import ui.theme.Theme;

/** Una fila del historial: número | total | estado (con su color) | Ver detalles. */
public class OrderPanel extends JPanel {
	private Order order;
    private Container container;

	private static final long serialVersionUID = 1L;
	private static final Locale MX = Locale.forLanguageTag("es-MX");

	public OrderPanel(Order order, Container container) {
        this.order = order;
        this.container = container;

        setBackground(Theme.BLANCO);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createCompoundBorder(
        		BorderFactory.createLineBorder(Theme.TINTA, 2),
        		BorderFactory.createEmptyBorder(10, 18, 10, 12)));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 78));

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.weightx = 0.25;
        add(columna("PEDIDO", "#" + order.getId()), c);

        c.gridx = 1; c.weightx = 0.35;
        add(columna("TOTAL", String.format(MX, "$%,.2f", order.getTotalPrice())), c);

        // Estado: recuadro del color del estado (viene del patrón State)
        JLabel estado = new JLabel(order.actualState().toUpperCase(), SwingConstants.CENTER);
        estado.setOpaque(true);
        estado.setBackground(order.getColor());
        estado.setForeground(Theme.textoSobre(order.getColor()));
        estado.setFont(Theme.mono(13));
        estado.setBorder(BorderFactory.createLineBorder(Theme.TINTA, 2));
        estado.setPreferredSize(new Dimension(130, 30));
        c.gridx = 2; c.weightx = 0.25;
        add(estado, c);

        Buttons verDetalles = Buttons.secundario("Ver detalles →");
        verDetalles.setFont(Theme.titulo(14));
        verDetalles.setPreferredSize(new Dimension(150, 40 + Theme.SOMBRA));
        verDetalles.addActionListener(e -> {
    		try {
    		    List<OrderDetails> detalles = Restaurant.getInstance().detallesDe(order.getId());
    		    if (detalles.isEmpty()) {
    		        Toast.aviso(OrderPanel.this, "Este pedido no tiene detalles registrados");
    		    } else {
    		        Main_Frame.changeDynamicPanel(new Details(new ArrayList<>(detalles), order));
    		    }
    		} catch (DaoException ex) {
    		    System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
    		    JOptionPane.showMessageDialog(null, "No se pudieron leer los detalles del pedido");
    		}
        });
        c.gridx = 3; c.weightx = 0; c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 12, 0, 0);
        add(verDetalles, c);
	}

	private static JPanel columna(String etiqueta, String valor) {
		JPanel p = new JPanel(new BorderLayout(0, 2));
		p.setOpaque(false);
		p.add(Theme.etiqueta(etiqueta, Theme.mono(11), Theme.GRAFITO), BorderLayout.NORTH);
		p.add(Theme.etiqueta(valor, Theme.mono(18), Theme.TINTA), BorderLayout.CENTER);
		return p;
	}
}
