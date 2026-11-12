package Restaurante_Completo.LeCartoon_Original;

/**
 * Repartidor asignado a un pedido: lo mínimo que se copia al documento de Couchbase
 * (patrón Extended Reference, igual que el cliente). Datos de REPARTIDOR + PERSONA + TELEFONO.
 */
public record Repartidor(int id, String nombre, String telefono) { }
