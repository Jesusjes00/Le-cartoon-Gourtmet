package persistence;

import persistence.oracle.OracleCustomerDao;
import persistence.oracle.OracleOrderDao;
import persistence.oracle.OracleTarjetaDao;
import persistence.couchbase.CouchbasePedidoDocDao;

/**
 * Factory: el único lugar que decide qué implementación de DAO usa la app.
 * LeCartoon trabaja solo con Oracle; Restaurant y las pantallas solo conocen las interfaces.
 */
public final class DaoFactory {

    private DaoFactory() { }   // solo métodos estáticos

    public static CustomerDao clientes() {
        return new OracleCustomerDao();
    }

    public static OrderDao pedidos() {
        return new OracleOrderDao();
    }
    
    public static PedidoDocDao documentos() {
        return new CouchbasePedidoDocDao();
    }

    public static TarjetaDao tarjetas() {
        return new OracleTarjetaDao();
    }
}
