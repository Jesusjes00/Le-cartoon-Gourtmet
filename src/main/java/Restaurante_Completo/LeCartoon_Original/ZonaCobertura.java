package Restaurante_Completo.LeCartoon_Original;

import java.util.List;

/**
 * Lo que la app sabe de un código postal de la zona de cobertura:
 * dónde está (municipio, ciudad, estado, país) y qué colonias tiene.
 * Si el CP no está en CODIGO_POSTAL (o no tiene colonias), no hay zona: no entregamos ahí.
 *
 * Es un record (Java 16+): una clase inmutable cuyo constructor, getters, equals y toString
 * los escribe el compilador. Los "getters" se llaman como el campo: zona.municipio(), zona.colonias().
 */
public record ZonaCobertura(String cp, String municipio, String ciudad, String estado, String pais,
		List<Colonia> colonias) {

	/** Una colonia del catálogo. toString() es lo que muestra el JComboBox. */
	public record Colonia(int id, String nombre) {
		@Override
		public String toString() {
			return nombre;
		}
	}

	/** "Benito Juárez, CDMX" para el texto junto al CP. */
	public String lugar() {
		return municipio + ", " + ciudad;
	}

	/** La colonia con ese nombre (sin importar mayúsculas), o null. Sirve al editar una dirección. */
	public Colonia colonia(String nombre) {
		if (nombre == null) return null;
		for (Colonia c : colonias) {
			if (c.nombre().equalsIgnoreCase(nombre.trim())) return c;
		}
		return null;
	}
}
