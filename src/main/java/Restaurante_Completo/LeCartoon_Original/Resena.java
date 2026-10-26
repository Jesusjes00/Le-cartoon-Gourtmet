package Restaurante_Completo.LeCartoon_Original;

import java.time.LocalDateTime;

/**
 * Reseña de un pedido entregado: calificación entera de 1 a 5 + comentario opcional.
 * Es lo que se guarda en Couchbase dentro del documento del pedido (campo "resena").
 */
public class Resena {

	public static final int MAX_COMENTARIO = 500;

	private final int estrellas;            // 1..5
	private final String comentario;        // puede venir vacío, nunca null
	private final LocalDateTime fecha;

	public Resena(int estrellas, String comentario, LocalDateTime fecha) {
		if (estrellas < 1 || estrellas > 5) {
			throw new IllegalArgumentException("La calificación va de 1 a 5, llegó " + estrellas);
		}
		String limpio = (comentario == null) ? "" : comentario.trim();
		if (limpio.length() > MAX_COMENTARIO) {
			throw new IllegalArgumentException("El comentario pasa de " + MAX_COMENTARIO + " caracteres");
		}
		this.estrellas = estrellas;
		this.comentario = limpio;
		this.fecha = fecha;
	}

	public int getEstrellas() { return estrellas; }
	public String getComentario() { return comentario; }
	public LocalDateTime getFecha() { return fecha; }
}
