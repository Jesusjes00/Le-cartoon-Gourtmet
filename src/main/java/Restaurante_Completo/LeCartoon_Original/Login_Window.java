package Restaurante_Completo.LeCartoon_Original;

import java.awt.EventQueue;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import persistence.DaoException;
import ui.theme.Theme;

public class Login_Window {

	private JFrame frame;
	private JTextField txtEmail;
	private JPasswordField txtpass;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login_Window window = new Login_Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Login_Window() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame("Le Cartoon Gourmet");
		JPanel contenido = Acceso.prepararVentana(frame);

		contenido.add(Acceso.panelIzquierdo("/Restaurante_Completo/LeCartoon_Original/GIF/LOGIN_1.gif",
				"Un mundo de sabores y aventuras te espera."));

		// ── Formulario ──
		JPanel form = Acceso.columna(340);
		Acceso.agregar(form, Theme.etiqueta("Bienvenido de nuevo", Theme.titulo(30), Theme.TINTA));
		form.add(Box.createVerticalStrut(4));
		Acceso.agregar(form, Theme.etiqueta("Entra para pedir tus platillos favoritos.", Theme.texto(15), Theme.GRAFITO));
		form.add(Box.createVerticalStrut(28));

		txtEmail = Acceso.campo("nombre@correo.com", "email.png");
		txtpass  = Acceso.campoContraseña("Tu contraseña");
		Acceso.agregarCampo(form, "Correo electrónico", txtEmail);
		Acceso.agregarCampo(form, "Contraseña", txtpass);

		// Mensaje de error: oculto hasta que falle el login
		JLabel wrong = Theme.etiqueta("Correo o contraseña incorrectos", Theme.titulo(14), Theme.LADRILLO);
		wrong.setVisible(false);
		Acceso.agregar(form, wrong);
		form.add(Box.createVerticalStrut(10));

		Buttons loginbutton = Buttons.primario("Entrar →");
		loginbutton.setPreferredSize(new java.awt.Dimension(340, 48 + Theme.SOMBRA));
		loginbutton.addActionListener(e -> {
			String user = txtEmail.getText().trim();
			String password_prev = new String(txtpass.getPassword());
			wrong.setVisible(false);

			if (!Acceso.validarLlenos(txtEmail, txtpass)) {
				return;                                         // los campos vacíos quedan marcados
			}
			Costumer cliente;
			try {
			    cliente = Restaurant.getInstance().login(user, password_prev);
			} catch (DaoException ex) {
			    System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
			    JOptionPane.showMessageDialog(frame, "No hay conexión con la base de datos. Intenta de nuevo.");
			    return;
			}

			if (cliente != null) {
			    Main_Frame principal_window = new Main_Frame();
			    principal_window.showmainframe(true, cliente);
			    frame.dispose();
			} else {
			    wrong.setVisible(true);
			    Acceso.marcarError(txtEmail, txtpass);
			}
		});
		Acceso.agregar(form, loginbutton);
		frame.getRootPane().setDefaultButton(loginbutton);        // Enter = Entrar

		form.add(Box.createVerticalStrut(28));
		Acceso.agregar(form, Theme.etiqueta("¿Todavía no eres parte de la familia?", Theme.texto(14), Theme.GRAFITO));
		form.add(Box.createVerticalStrut(10));

		Buttons regbutton = Buttons.secundario("Crear una cuenta");
		regbutton.setPreferredSize(new java.awt.Dimension(340, 48 + Theme.SOMBRA));
		regbutton.addActionListener(e -> {
			Register_Window regWind = new Register_Window();
			regWind.showregframe(true);
			frame.dispose();
		});
		Acceso.agregar(form, regbutton);

		contenido.add(Acceso.panelDerecho(frame, form));
	}

	//Metodo para abrir la ventana
	public void showloginframe(boolean visible) {
		if (this.frame != null) {
			this.frame.setVisible(visible);
		}
	}
}
