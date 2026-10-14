package OrderDetails;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Restaurante_Completo.LeCartoon_Original.OrderDetails;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class DetailsPanelOD extends JPanel {

	private OrderDetails details;
    private Container container;

	private static final long serialVersionUID = 1L;

	public DetailsPanelOD(OrderDetails details, Container container) {
		setBackground(new Color(255, 255, 255));
        this.details = details;
        this.container = container;
        
        setPreferredSize(new Dimension(540, 100)); // ALTURA FIJA = 100
        setMaximumSize(new Dimension(540, 100));  
        setMinimumSize(new Dimension(540, 100));
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{30, 268, 30, 0};
        gridBagLayout.rowHeights = new int[]{96, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);
        this.setBorder(BorderFactory.createEtchedBorder());
        
       
        JLabel dishValue = new JLabel(String.valueOf(details.getQuantity()) + "x  " +details.getDish());
        dishValue.setHorizontalAlignment(SwingConstants.LEFT);
        dishValue.setOpaque(true);
        dishValue.setBackground(new Color(255, 255, 255));
        dishValue.setFont(new Font("SansSerif", Font.BOLD, 24));       
        GridBagConstraints gbc_dishValue = new GridBagConstraints();
        gbc_dishValue.insets = new Insets(0, 0, 0, 5);
        gbc_dishValue.fill = GridBagConstraints.BOTH;
        gbc_dishValue.gridx = 1;
        gbc_dishValue.gridy = 0;
        this.add(dishValue, gbc_dishValue);

	}

}
