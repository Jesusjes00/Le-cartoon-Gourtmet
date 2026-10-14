package Dishes;

public class Dishes {
	protected String name;
	protected double price;
	protected String description;
	protected int id;
	protected String image;

	public Dishes(String name, double price, String description, int id, String image) {
		super();
		this.name = name;
		this.price = price;
		this.description = description;
		this.id = id;
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public String getDescription() {
		return description;
	}

	public int getId() {
		return id;
	}

	public String getImage() {
		return image;
	}

}
