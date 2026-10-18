package ProfileScreens;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import OrderDetails.Details;
import Restaurante_Completo.LeCartoon_Original.Order;
import Restaurante_Completo.LeCartoon_Original.OrderDetails;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.BorderLayout;
import Restaurante_Completo.LeCartoon_Original.Buttons;
import Restaurante_Completo.LeCartoon_Original.Main_Frame;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import Restaurante_Completo.LeCartoon_Original.Restaurant;
import persistence.DaoException;

public class OrderPanel extends JPanel {
	private Order order;
    private Container container;

	private static final long serialVersionUID = 1L;

	public OrderPanel(Order order, Container container) {
		setBackground(new Color(0, 0, 0));
        this.order = order;
        this.container = container;
        
        setPreferredSize(new Dimension(1250, 100)); // ALTURA FIJA = 100
        setMaximumSize(new Dimension(1250, 100));  
        setMinimumSize(new Dimension(1250, 100));  
        setLayout(new GridLayout(0, 7, 0, 0));
        
        JLabel idLabel = DefaultComponentFactory.getInstance().createLabel("Id de Pedido:");
        idLabel.setOpaque(true);
        idLabel.setBackground(new Color(255, 255, 255));
        idLabel.setHorizontalAlignment(SwingConstants.CENTER);
        idLabel.setFont(new Font("SansSerif", Font.PLAIN, 25));
        add(idLabel);
        
        //Clave del alumno 
        JLabel idValue = new JLabel(String.valueOf(order.getId()));
        idValue.setOpaque(true);
        idValue.setBackground(new Color(255, 255, 255));
        idValue.setFont(new Font("SansSerif", Font.PLAIN, 25));
        idValue.setText(String.valueOf(order.getId()));
        this.add(idValue);
        this.setBorder(BorderFactory.createEtchedBorder());
        
        JLabel priceLabel = new JLabel("Precio:");
        priceLabel.setOpaque(true);
        priceLabel.setBackground(new Color(255, 255, 255));
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 25));
        add(priceLabel);
        
        JLabel priceValue = new JLabel("$ " + order.getTotalPrice());
        priceValue.setOpaque(true);
        priceValue.setBackground(new Color(255, 255, 255));
        priceValue.setFont(new Font("SansSerif", Font.PLAIN, 25));
        add(priceValue);
        
        JLabel stateLabel = new JLabel("Estado:");
        stateLabel.setOpaque(true);
        stateLabel.setBackground(new Color(255, 255, 255));
        stateLabel.setFont(new Font("SansSerif", Font.PLAIN, 25));
        add(stateLabel);
        
        JLabel stateValue = new JLabel(order.actualState());
        stateValue.setOpaque(true);
        stateValue.setBackground(new Color(255, 255, 255));
        stateValue.setFont(new Font("SansSerif", Font.BOLD, 25));        
        stateValue.setForeground(order.getColor());
        add(stateValue);
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        add(panel);
        panel.setLayout(new BorderLayout(0, 0));
        
        //Variables de color 
        Color normal = null, hover =null;
        if(order.getId_state() == 1) {
        	normal = new Color(170, 0, 0);   
        	hover = Color.red;
        }else if(order.getId_state() == 2) {
        	normal = new Color(0, 170, 170);
        	hover = Color.cyan;
        }else if(order.getId_state() == 3){
        	normal = new Color(0, 170, 0);
        	hover = Color.green;
        }
        
        
        Buttons loginbutton = new Buttons("Crear Cuenta", 30, 30, normal, hover);
        loginbutton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginbutton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        		    List<OrderDetails> detalles = Restaurant.getInstance().detallesDe(order.getId());
        		    if (detalles.isEmpty()) {
        		        JOptionPane.showMessageDialog(null, "Este pedido no tiene detalles registrados");
        		    } else {
        		        Main_Frame.changeDynamicPanel(new Details(new ArrayList<>(detalles), order));
        		    }
        		} catch (DaoException ex) {
        		    System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
        		    JOptionPane.showMessageDialog(null, "No se pudieron leer los detalles del pedido");
        		}
        	}
        });        
        loginbutton.setText("Detalles");
        loginbutton.setFont(new Font("SansSerif", Font.BOLD, 22));
        loginbutton.setForeground(Color.WHITE);
        loginbutton.setColorFondo(normal);
        panel.add(loginbutton, BorderLayout.CENTER);
        
        JLabel lblNewLabel = new JLabel(" ");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        panel.add(lblNewLabel, BorderLayout.NORTH);
        
        JLabel lblNewLabel_1 = new JLabel(" ");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
        panel.add(lblNewLabel_1, BorderLayout.SOUTH);
        
        JLabel lblNewLabel_2 = new JLabel("      ");
        panel.add(lblNewLabel_2, BorderLayout.WEST);
        
        JLabel lblNewLabel_3 = new JLabel("          ");
        panel.add(lblNewLabel_3, BorderLayout.EAST);


	}

}
