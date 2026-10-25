package ProfileScreens;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import Restaurante_Completo.LeCartoon_Original.Costumer;

/** Datos del cliente (solo lectura). Para cambiarlos está "Editar perfil". */
public class Personalinfo extends JPanel {

	private static final long serialVersionUID = 1L;
	private static JLabel name;
	private static JLabel phone;
	private static JLabel email;

	public Personalinfo() {
		setOpaque(false);
		setLayout(new BorderLayout());

		name  = new JLabel("Nombre + Apellido");
		phone = new JLabel("55 xxxxxxxx");
		email = new JLabel("correo@ejemplo.com");

		JPanel datos = ProfileScreen.columna(28,
				ProfileScreen.dato("Nombre",   "user.PNG",  name),
				ProfileScreen.dato("Teléfono", "phone.png", phone),
				ProfileScreen.dato("Correo",   "email.png", email));

		add(ProfileScreen.seccion("Información personal", "usuario.png", datos), BorderLayout.CENTER);
	}

	public static void costumerChanged(Costumer costumer) {
		name.setText(costumer.getName() + " " + costumer.getLastName());
		phone.setText(costumer.getPhone());
		email.setText(costumer.getEmail());       // antes se quedaba en "prueba@gmail.com"
	}
}
