package Restaurante_Completo.LeCartoon_Original;


public class Costumer {
	//Atributos
	private int id; // id cliente 
	private String name; // nombre 
	private String lastName; //Apellidp
	private String phone; //telefono
	private String email; // usuario
	private String password; //contraseña
	private String address;
	
	
	//Constructores
	//Constructor para crear un cliente nuevo (que no posee id de cliente) 
	public Costumer(String name, String lastName, String phone, String email, String password, String address) {	
		this.id = -1; // seteamos un id genérico para identificar al cliente nuevo, del obtenido por la base de datos 
		this.name = name; 
		this.lastName = lastName;
		this.phone = phone; 
		this.email = email; 
		this.password = password; 
		this.address = address;
	}
	
	//Constructor utilizado tras obtener al Cliente de la base de datos con su respectivo id 
	public Costumer(int id, String name, String lastName, String phone, String email, String password, String address) {
		this(name, lastName, phone, email, password, address);
		this.id = id; 		
	}

	//Getters Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

	public String getAddress() {
		return address;
	}

	public void setAddress1(String address) {
		this.address = address;
	}
	

	public String toString() {
		return "Cliente [id = " + id + ", " + name + " " + lastName + ", tel: " + phone + ", usuario = "
				+ email + "]";
	}
	
}
