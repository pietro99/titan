public class Planet extends Body{
	private String name;
	
	//constructor
	public Planet(String name, Vector initialPos, Vector initialVelocity, double radius, double mass) {
		this.position = initialPos;
		this.velocity = initialVelocity;
		this.radius = radius;
		this.mass = mass;
		this.acceleration = new Vector(0,0,0);
	}

	public String getName() {
		return name;
	}
}
