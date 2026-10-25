package ProfileScreens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Restaurante_Completo.LeCartoon_Original.Buttons;
import Restaurante_Completo.LeCartoon_Original.Main_Frame;
import Restaurante_Completo.LeCartoon_Original.MainFramePanel;
import Restaurante_Completo.LeCartoon_Original.Order;
import ui.theme.Theme;

/** Lista de pedidos del cliente, del más reciente al más viejo. */
public class OrderHistory extends JPanel {

	private static final long serialVersionUID = 1L;

	public OrderHistory(ArrayList<Order> orders) {
		setOpaque(false);
		setLayout(new BorderLayout());

		JPanel contenido;
		if (orders.isEmpty()) {
			Buttons explorar = Buttons.primario("Explorar cocinas →");
			explorar.setPreferredSize(new Dimension(240, 46 + Theme.SOMBRA));
			explorar.addActionListener(e -> Main_Frame.changeDynamicPanel(new MainFramePanel()));
			contenido = ProfileScreen.columna(16,
					Theme.etiqueta("Aún no tienes pedidos.", Theme.titulo(20), Theme.TINTA),
					ProfileScreen.fila(0, explorar));
		} else {
			JPanel lista = new JPanel();
			lista.setOpaque(false);
			lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
			for (Order order : orders) {
				OrderPanel fila = new OrderPanel(order, lista);
				fila.setAlignmentX(LEFT_ALIGNMENT);
				lista.add(fila);
				lista.add(Box.createVerticalStrut(10));
			}
			JPanel arriba = new JPanel(new BorderLayout());
			arriba.setOpaque(false);
			arriba.add(lista, BorderLayout.NORTH);
			JScrollPane scroll = Theme.scroll(arriba);
			scroll.getViewport().setBackground(Theme.BLANCO);
			contenido = new JPanel(new BorderLayout());
			contenido.setOpaque(false);
			contenido.add(scroll, BorderLayout.CENTER);
		}
		add(ProfileScreen.seccion("Historial de pedidos", "pedido.png", contenido), BorderLayout.CENTER);
	}
}
