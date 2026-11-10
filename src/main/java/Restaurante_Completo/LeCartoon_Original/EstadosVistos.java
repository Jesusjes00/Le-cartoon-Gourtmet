package Restaurante_Completo.LeCartoon_Original;

import java.util.prefs.Preferences;

/**
 * Recuerda, en esta computadora, el último estado que el cliente vio de cada pedido.
 * Sirve para saber si un pedido "avanzó" desde la última vez (lo mueve el vendedor).
 *
 * Usa java.util.prefs.Preferences: un almacén clave → valor de Java que en Windows vive en
 * el registro (HKEY_CURRENT_USER\Software\JavaSoft\Prefs\lecartoon\estados-vistos) y
 * sobrevive a cerrar la app. No toca Oracle ni Couchbase.
 *
 * Clave: "idCliente.idPedido"  →  valor: estado visto (1, 2 o 3).
 * Solo la usa Restaurant (por eso no es pública).
 */
final class EstadosVistos {

    private static final Preferences PREFS = Preferences.userRoot().node("lecartoon/estados-vistos");

    private EstadosVistos() { }

    private static String clave(int idCliente, int idPedido) {
        return idCliente + "." + idPedido;
    }

    /**
     * Último estado que el cliente vio del pedido.
     * Si nunca lo vio en esta computadora, o lo guardado ya no cuadra con la BD (por ejemplo,
     * si se volvió a correr el script y los ids se repitieron), toma el estado actual como
     * visto: así no se animan de golpe todos los pedidos viejos.
     */
    static synchronized int visto(int idCliente, int idPedido, int estadoActual) {
        int guardado = PREFS.getInt(clave(idCliente, idPedido), -1);
        if (guardado < 1 || guardado > estadoActual) {
            marcar(idCliente, idPedido, estadoActual);
            return estadoActual;
        }
        return guardado;
    }

    /** Anota que el cliente ya vio el pedido en ese estado. */
    static synchronized void marcar(int idCliente, int idPedido, int estado) {
        PREFS.putInt(clave(idCliente, idPedido), estado);
    }
}
