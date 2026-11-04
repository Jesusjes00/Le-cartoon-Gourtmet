package persistence;

public class PruebaClientes {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CustomerDao dao = new persistence.oracle.OracleCustomerDao();
		System.out.println(dao.login("carlos@email.com","pass123"));
		System.out.println(dao.login("carlos@email.com", "mala"));

	}

}
