package OrderDetails;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Restaurante_Completo.LeCartoon_Original.OrderDetails;
import ui.theme.Theme;

/** Renglón del detalle: cantidad en un recuadro + nombre del platillo. */
public class DetailsPanelOD extends JPanel {

	private OrderDetails details;
    private Container container;

	private static final long serialVersionUID = 1L;

	public DetailsPanelOD(OrderDetails details, Container container) {
        this.details = details;
        this.container = container;
        setOpaque(false);
        setLayout(new BorderLayout(14, 0));
        setBorder(BorderFactory.createCompoundBorder(
        		BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 0, 0, 45)),
        		BorderFactory.createEmptyBorder(12, 0, 12, 0)));

        JLabel cantidad = new JLabel(details.getQuantity() + "×", SwingConstants.CENTER);
        cantidad.setOpaque(true);
        cantidad.setBackground(Theme.BLANCO);
        cantidad.setFont(Theme.mono(16));
        cantidad.setForeground(Theme.TINTA);
        cantidad.setBorder(BorderFactory.createLineBorder(Theme.TINTA, 2));
        cantidad.setPreferredSize(new Dimension(52, 36));

        JLabel platillo = Theme.etiqueta(details.getDish(), Theme.titulo(18), Theme.TINTA);

        add(cantidad, BorderLayout.WEST);
        add(platillo, BorderLayout.CENTER);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
	}
}
