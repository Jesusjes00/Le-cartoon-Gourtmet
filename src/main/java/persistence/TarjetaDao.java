package persistence;

import java.time.YearMonth;

import Restaurante_Completo.LeCartoon_Original.MetodoPago;

/** Tarjetas guardadas del cliente (TARJETA_CLIENTE). */
public interface TarjetaDao {

	/** La tarjeta más reciente del cliente, o null si no tiene ninguna. */
	MetodoPago tarjetaDe(int idCliente);

	/** Guarda una tarjeta nueva del cliente y regresa el id que le dio Oracle. */
	int registrar(int idCliente, String numero, YearMonth vence, String cvv);
}
