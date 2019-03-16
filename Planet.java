import java.util.ArrayList;
import java.util.List;


public class Planet {
	private String name;
	private double mass;
	private Vector position;
	private Vector velocity;
	private double radius; 
	private Vector acceleration;
	private Ellipse orbit;
	private List<Planet> moon = new ArrayList<Planet>();
	
	
	//constructor
	public Planet(String name, Vector initialPos, Vector initialVelocity, double radius, double mass) {
		this.setPosition(initialPos);
		this.velocity = initialVelocity;
		this.radius = radius;
		this.mass = mass;
	}
	
	public void addMoon(Planet moon) {
		this.moon.add(moon);
	}

	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}
}
