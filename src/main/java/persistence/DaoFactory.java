package persistence;

import persistence.sqlite.SqliteCustomerDao;
import persistence.sqlite.SqliteOrderDao;
import persistence.oracle.OracleCustomerDao;
import persistence.oracle.OracleOrderDao;


 //lugar que decide qué implementación de DAO usa la app.

public final class DaoFactory {

    private DaoFactory() { }   // solo métodos estáticos

    public static boolean esOracle() {
        return ConnectionFactory.describir().startsWith("jdbc:oracle:");
    }

    public static CustomerDao clientes() {
        return esOracle() ? new OracleCustomerDao() : new SqliteCustomerDao();
    }

    public static OrderDao pedidos() {
        return esOracle() ? new OracleOrderDao() : new SqliteOrderDao();
    }
}