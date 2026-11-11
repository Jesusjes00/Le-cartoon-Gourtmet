package Restaurante_Completo.LeCartoon_Original;

import java.time.YearMonth;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tarjeta guardada del cliente, ya enmascarada (TARJETA_CLIENTE en Oracle).
 * La app nunca guarda en memoria el número completo ni el CVV: solo la marca y los últimos 4.
 * Las validaciones estáticas se usan antes de mandar una tarjeta nueva a la BD.
 */
public class MetodoPago {

	private final int id;               // TARJETA_CLIENTE.ID_TARJETA_CLIENTE
	private final String marca;         // VISA, MASTERCARD, AMEX o TARJETA
	private final String ultimos4;
	private final YearMonth vence;      // solo importan mes y año

	public MetodoPago(int id, String primerDigito, String ultimos4, YearMonth vence) {
		this.id = id;
		this.marca = marcaDe(primerDigito);
		this.ultimos4 = ultimos4;
		this.vence = vence;
	}

	/** La marca sale del primer dígito del número (así lo hacen las terminales de verdad). */
	private static String marcaDe(String digito) {
		if (digito == null) return "TARJETA";
		return switch (digito) {
			case "4"      -> "VISA";
			case "5", "2" -> "MASTERCARD";
			case "3"      -> "AMEX";
			default       -> "TARJETA";
		};
	}

	public int getId()          { return id; }
	public String getMarca()    { return marca; }
	public String getUltimos4() { return ultimos4; }
	public YearMonth getVence() { return vence; }

	/** Para la tarjeta grande de Perfil → Pago. */
	public String enmascarado() { return "•••• •••• •••• " + ultimos4; }

	/** Para el recuadro del carrito. */
	public String corto()       { return marca + " •••• " + ultimos4; }

	/** "05/26" */
	public String venceTexto() {
		return String.format("%02d/%02d", vence.getMonthValue(), vence.getYear() % 100);
	}

	/** Vence al terminar su mes: 10/26 sirve hasta el 31 de octubre de 2026. */
	public boolean vencida() {
		return vence.isBefore(YearMonth.now());
	}

	// ══════════════ Validaciones para una tarjeta nueva ══════════════

	/** "4111 1111-1111 1111" -> "4111111111111111" (la gente escribe con espacios o guiones). */
	public static String soloDigitos(String texto) {
		return texto == null ? "" : texto.replaceAll("[\\s-]", "");
	}

	/** Entre 13 y 19 dígitos y que pase el algoritmo de Luhn (detecta errores de dedo). */
	public static boolean numeroValido(String numero) {
		String n = soloDigitos(numero);
		return n.matches("\\d{13,19}") && luhn(n);
	}

	/**
	 * Luhn: de derecha a izquierda, duplica uno sí y uno no; si el doble pasa de 9 se le resta 9.
	 * La suma total debe terminar en 0. Todas las tarjetas reales lo cumplen.
	 */
	private static boolean luhn(String n) {
		int suma = 0;
		boolean duplicar = false;
		for (int i = n.length() - 1; i >= 0; i--) {
			int d = n.charAt(i) - '0';
			if (duplicar) {
				d *= 2;
				if (d > 9) d -= 9;
			}
			suma += d;
			duplicar = !duplicar;
		}
		return suma % 10 == 0;
	}

	public static boolean cvvValido(String cvv) {
		return cvv != null && cvv.trim().matches("\\d{3,4}");
	}

	private static final Pattern MM_AA = Pattern.compile("(0[1-9]|1[0-2])\\s*/?\\s*(\\d{2})");

	/** "05/29" o "0529" -> mayo de 2029; null si no tiene ese formato. */
	public static YearMonth leerVence(String texto) {
		if (texto == null) return null;
		Matcher m = MM_AA.matcher(texto.trim());
		if (!m.matches()) return null;
		return YearMonth.of(2000 + Integer.parseInt(m.group(2)), Integer.parseInt(m.group(1)));
	}
}
