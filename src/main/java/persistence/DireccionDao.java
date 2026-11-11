package persistence;

import Restaurante_Completo.LeCartoon_Original.Direccion;
import Restaurante_Completo.LeCartoon_Original.ZonaCobertura;

/** Dirección de entrega del cliente (DIRECCION + PERSONA_DIRECCION) y la zona de cobertura (catálogo). */
public interface DireccionDao {

	/** La dirección activa del cliente, o null si todavía no tiene. */
	Direccion direccionDe(int idCliente);

	/** Municipio, ciudad, estado y colonias de ese CP; null si no está en la zona de cobertura. */
	ZonaCobertura zonaDe(String cp);

	/**
	 * Guarda una dirección NUEVA y la deja como la única del cliente.
	 * La anterior no se borra: los pedidos viejos siguen apuntando a ella.
	 * Regresa el id de la DIRECCION nueva.
	 */
	int guardar(int idCliente, int idColonia, String calle, String numExterior, String numInterior);
}
