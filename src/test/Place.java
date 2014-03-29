package test;



public class Place {
	public String name;
	public double x;
	public double y;
	public double z;

	public Place(String name, double x, double y, double z) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	Place(Place p) {
		name = p.name;
		x = p.x;
		y = p.y;
		z = p.z;
	}

}