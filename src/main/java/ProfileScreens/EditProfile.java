package ProfileScreens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Restaurante_Completo.LeCartoon_Original.Buttons;
import Restaurante_Completo.LeCartoon_Original.Costumer;
import Restaurante_Completo.LeCartoon_Original.Restaurant;
import persistence.DaoException;
import ui.Toast;
import ui.theme.Theme;

/**
 * Formulario para cambiar nombre, apellido y teléfono.
 * Los campos ya traen los datos actuales (antes salían vacíos con un texto gris que había que borrar).
 */
public class EditProfile extends JPanel {

	private static final long serialVersionUID = 1L;
	private final Costumer costumer;
	private JTextField nameField;
	private JTextField lnameField;
	private JTextField phoneField;

	public EditProfile(Costumer costumer) {
		this.costumer = costumer;
		setOpaque(false);
		setLayout(new BorderLayout());

		nameField  = campo(costumer.getName(),     "Tu nombre");
		lnameField = campo(costumer.getLastName(), "Tus apellidos");
		phoneField = campo(costumer.getPhone(),    "10 dígitos");

		JPanel form = new JPanel(new GridBagLayout());
		form.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.weightx = 1;

		int fila = 0;
		for (Object[] par : new Object[][] { {"NOMBRE", nameField}, {"APELLIDOS", lnameField}, {"TELÉFONO", phoneField} }) {
			c.gridy = fila++;
			c.insets = new Insets(fila == 1 ? 0 : 18, 0, 6, 0);
			form.add(Theme.etiqueta((String) par[0], Theme.mono(12), Theme.GRAFITO), c);
			c.gridy = fila++;
			c.insets = new Insets(0, 0, 0, 0);
			form.add((JTextField) par[1], c);
		}

		// Correo: no se edita aquí (es el usuario para entrar)
		c.gridy = fila++;
		c.insets = new Insets(18, 0, 6, 0);
		form.add(Theme.etiqueta("CORREO (NO SE PUEDE CAMBIAR)", Theme.mono(12), Theme.GRAFITO), c);
		c.gridy = fila++;
		c.insets = new Insets(0, 0, 0, 0);
		JTextField correo = campo(costumer.getEmail(), "");
		correo.setEnabled(false);
		form.add(correo, c);

		Buttons editBtn = Buttons.primario("Guardar cambios");
		editBtn.setPreferredSize(new Dimension(200, 46 + Theme.SOMBRA));
		editBtn.addActionListener(e -> guardar());

		Buttons cancelar = Buttons.secundario("Cancelar");
		cancelar.setPreferredSize(new Dimension(140, 46 + Theme.SOMBRA));
		cancelar.addActionListener(e -> volverAInfo());

		c.gridy = fila;
		c.insets = new Insets(28, 0, 0, 0);
		c.fill = GridBagConstraints.NONE;
		form.add(ProfileScreen.fila(14, editBtn, cancelar), c);

		// El formulario no pasa de 560 px de ancho: los campos larguísimos cuestan leerlos
		JPanel limite = new JPanel(new BorderLayout()) {
			@Override public Dimension getPreferredSize() {
				return new Dimension(Math.min(560, super.getPreferredSize().width), super.getPreferredSize().height);
			}
		};
		limite.setOpaque(false);
		limite.add(form, BorderLayout.CENTER);
		JPanel izquierda = new JPanel(new BorderLayout());
		izquierda.setOpaque(false);
		izquierda.add(limite, BorderLayout.WEST);
		JPanel arriba = new JPanel(new BorderLayout());
		arriba.setOpaque(false);
		arriba.add(izquierda, BorderLayout.NORTH);

		add(ProfileScreen.seccion("Editar perfil", "user.PNG", arriba), BorderLayout.CENTER);
	}

	private void guardar() {
		marcarErrores();
		if (!canEdit()) {
			Toast.aviso(this, "Completa los campos marcados");
			return;
		}
		if (JOptionPane.showConfirmDialog(this, "¿Tus datos son correctos?", "Guardar cambios",
				JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
			return;
		}
		Costumer cambios = new Costumer(costumer.getId(), nameField.getText().trim(), lnameField.getText().trim(),
		        phoneField.getText().trim(), costumer.getEmail(), costumer.getPassword(), costumer.getAddress());
		try {
		    Restaurant.getInstance().actualizarPerfil(cambios);
		    costumer.setName(cambios.getName());            // solo si la BD aceptó
		    costumer.setLastName(cambios.getLastName());
		    costumer.setPhone(cambios.getPhone());
		    Toast.exito(this, "Se actualizó tu perfil");
		    ProfileScreen.costumerChanged();
		    volverAInfo();
		} catch (DaoException ex) {
		    System.err.println("[DAO] " + ex.getMessage() + " -> " + ex.getCause());
		    JOptionPane.showMessageDialog(this, "No se pudo actualizar tu perfil. Intenta de nuevo.");
		}
	}

	private void volverAInfo() {
		ProfileScreen.changePanel(new Personalinfo());
		Personalinfo.costumerChanged(costumer);
	}

	/** Campo con el estilo del tema: texto actual + placeholder de FlatLaf cuando está vacío. */
	private static JTextField campo(String valor, String placeholder) {
		JTextField t = new JTextField(valor);
		t.setFont(Theme.texto(17));
		t.putClientProperty("JTextField.placeholderText", placeholder);
		t.setPreferredSize(new Dimension(420, 44));
		return t;
	}

	/** Borde ladrillo en los campos vacíos (FlatLaf: JComponent.outline = "error"). */
	private void marcarErrores() {
		for (JTextField t : new JTextField[] { nameField, lnameField, phoneField }) {
			t.putClientProperty("JComponent.outline", t.getText().trim().isEmpty() ? "error" : null);
		}
	}

	public boolean canEdit() {
		return !nameField.getText().trim().isEmpty()
			&& !lnameField.getText().trim().isEmpty()
			&& !phoneField.getText().trim().isEmpty();
	}

	/** Regresa los campos a los datos actuales del cliente. */
	public void resetPH() {
		nameField.setText(costumer.getName());
		lnameField.setText(costumer.getLastName());
		phoneField.setText(costumer.getPhone());
		marcarErrores();
	}
}
