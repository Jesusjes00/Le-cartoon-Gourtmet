package Restaurante_Completo.LeCartoon_Original;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import persistence.DaoException;
import ui.Toast;
import ui.theme.Theme;

public class Register_Window {

	private JFrame frame;
	private JTextField txtName;
	private JTextField txtlastname;
	private JTextField txtnumber;
	private JTextField txtemail;
	private JPasswordField txtpass;
	private JTextField txtadd;

	public Register_Window() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame("Le Cartoon Gourmet · Registro");
		JPanel contenido = Acceso.prepararVentana(frame);

		contenido.add(Acceso.panelIzquierdo("/Restaurante_Completo/LeCartoon_Original/GIF/REG.gif",
				"Crea tu cuenta y empieza la aventura culinaria."));

		JPanel form = Acceso.columna(380);
		Acceso.agregar(form, Theme.etiqueta("Regístrate", Theme.titulo(30), Theme.TINTA));
		form.add(Box.createVerticalStrut(4));
		Acceso.agregar(form, Theme.etiqueta("Solo te toma un minuto.", Theme.texto(15), Theme.GRAFITO));
		form.add(Box.createVerticalStrut(22));

		txtName     = Acceso.campo("Tus nombres", "user.PNG");
		txtlastname = Acceso.campo("Tus apellidos", null);
		txtnumber   = Acceso.campo("55 1234 5678", "phone.png");
		txtemail    = Acceso.campo("nombre@correo.com", "email.png");
		txtpass     = Acceso.campoContraseña("Crea una contraseña");
		txtadd      = Acceso.campo("Calle, número, colonia, ciudad", "localizacion.png");

		// Nombres y apellidos en dos columnas
		JPanel nombres = new JPanel(new GridLayout(1, 2, 12, 0));
		nombres.setOpaque(false);
		JPanel colNombre = Acceso.columna(180);
		Acceso.agregarCampo(colNombre, "Nombres", txtName);
		JPanel colApellido = Acceso.columna(180);
		Acceso.agregarCampo(colApellido, "Apellidos", txtlastname);
		nombres.add(colNombre);
		nombres.add(colApellido);
		Acceso.agregar(form, nombres);

		Acceso.agregarCampo(form, "Teléfono", txtnumber);
		Acceso.agregarCampo(form, "Correo electrónico", txtemail);
		Acceso.agregarCampo(form, "Contraseña", txtpass);
		Acceso.agregarCampo(form, "Domicilio", txtadd);
		form.add(Box.createVerticalStrut(6));

		Buttons loginbutton = Buttons.primario("Crear cuenta");
		loginbutton.setPreferredSize(new Dimension(380, 48 + Theme.SOMBRA));
		loginbutton.addActionListener(e -> {
			if (!Acceso.validarLlenos(txtName, txtlastname, txtnumber, txtemail, txtpass, txtadd)) {
				Toast.aviso(frame, "Completa los campos marcados");
				return;
			}
			String user = txtName.getText().trim();
			String lastname_tx = txtlastname.getText().trim();
			String password_prev = new String(txtpass.getPassword()).trim();
			String number_prev = txtnumber.getText().trim();
			String Email = txtemail.getText().trim();
			String Add = txtadd.getText().trim();
			try {
			    Costumer nuevo = new Costumer(user, lastname_tx, number_prev, Email, password_prev, Add);
			    Costumer cliente = Restaurant.getInstance().registrar(nuevo);

			    Main_Frame principal_window = new Main_Frame();
			    principal_window.showmainframe(true, cliente);
			    frame.dispose();
			} catch (DaoException ex) {
			    System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
			    JOptionPane.showMessageDialog(frame, "No se pudo crear la cuenta. Revisa tus datos e intenta de nuevo.");
			    Acceso.marcarError(txtemail);
			}
		});
		Acceso.agregar(form, loginbutton);
		frame.getRootPane().setDefaultButton(loginbutton);
		form.add(Box.createVerticalStrut(12));

		// Antes no había forma de regresar al login sin cerrar la app
		Buttons volver = Buttons.secundario("← Ya tengo cuenta");
		volver.setPreferredSize(new Dimension(380, 44 + Theme.SOMBRA));
		volver.addActionListener(e -> {
			new Login_Window().showloginframe(true);
			frame.dispose();
		});
		Acceso.agregar(form, volver);

		contenido.add(Acceso.panelDerecho(frame, form));
	}

	//Metodo para abrir la ventana
	public void showregframe(boolean visible) {
		if (this.frame != null) {
			this.frame.setVisible(visible);
		}
	}
}
