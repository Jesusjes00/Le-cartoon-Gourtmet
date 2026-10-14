package platillos;


public abstract class Platillos {
	protected String name;
	protected double price;
	protected String description;
	protected int id;

	public Platillos(String name, double price, String description, int id) {
		super();
		this.name = name;
		this.price = price;
		this.description = description;
		this.id = id;
	}

	/*
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/*
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/*
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/*
	 * @return the id
	 */
	public int getId() {
		return id;
	}

}
