package Franchises;

public class Franchises {
	protected String name;
	protected String image;
	protected String logo;
	protected String color;

	public Franchises(String name, String image, String logo, String color) {
		this.name = name;
		this.image = image;
		this.logo = logo;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public String getImage() {
		return image;
	}

	public String getLogo() {
		return logo;
	}

	public String getColor() {
		return color;
	}

}
