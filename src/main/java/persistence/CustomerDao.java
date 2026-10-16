package persistence;
import Restaurante_Completo.LeCartoon_Original.Costumer;
public interface CustomerDao {

	//Regresa un Costumer al momento de hacer un login // si el login falla regresa null
	Costumer login(String email,String password);
	// Inserta un cliente nuevo dentro de la base y regresa el id de la base de datos
	int registrar(Costumer nuevo);
	// actualiza el perfil del cliente dentro de la base de datos por medio del id
	void actualizarPerfil(Costumer c);
}
