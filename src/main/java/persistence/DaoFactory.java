package persistence;

import persistence.sqlite.SqliteCustomerDao;
import persistence.sqlite.SqliteOrderDao;


 //lugar que decide qué implementación de DAO usa la app.

public final class DaoFactory {

    private DaoFactory() { }   // solo métodos estáticos

    public static boolean esOracle() {
        return ConnectionFactory.describir().startsWith("jdbc:oracle:");
    }

    public static CustomerDao clientes() {
        if (esOracle()) {
            throw new UnsupportedOperationException("OracleCustomerDao llega en o04");
        }
        return new SqliteCustomerDao();
    }

    public static OrderDao pedidos() {
        if (esOracle()) {
            throw new UnsupportedOperationException("OracleOrderDao llega en o05");
        }
        return new SqliteOrderDao();
    }
}