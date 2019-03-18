

public class Planet {
	public static final double g = 6.67408E-11;
	private String name;
	private double mass;
	private Vector position;
	private Vector velocity;
	private double radius; 
	private Vector gravityForce;
	private Vector acceleration;
	
	
	
	//constructor
	public Planet(String name, Vector initialPos, Vector initialVelocity, double radius, double mass) {
		this.setPosition(initialPos);
		this.velocity = initialVelocity;
		this.radius = radius;
		this.mass = mass;
		this.acceleration = new Vector(0,0,0);
	}

	
	
	//calculate the gravitational pull
	public void calculateGravityForce() {
		for(int i=0; i<SolarSystem.getPlanets().length; i++) {
			if(!this.equals(SolarSystem.getPlanets()[i])) {
				Planet p = SolarSystem.getPlanets()[i];
				Vector direction = this.getPosition().subtract(p.getPosition()).normalize();
				double mass = p.getMass();
				double distance = this.position.distance(p.getPosition());
				double force = g*((this.mass*mass)/Math.pow(distance, 2));
				Vector forceVector = direction.multiply(force);
				p.setGravityForce(forceVector);
				p.addAcceleration();
			}
		}
	}
	public void addAcceleration() {
		acceleration = acceleration.sum(gravityForce.multiply(1/mass));
	}

	public void addAcceleration(Vector acc) {
		acceleration = acceleration.sum(acc);
	}

	public void updateVelocityAndPosition(double deltaT) {
		/*Vector oldVel = new Vector(velocity.getX(),velocity.getY(),velocity.getZ());
		velocity = velocity.sum(acceleration.multiply(deltaT));
		Vector averageVel = oldVel.sum(velocity).multiply(0.5);
		position = position.sum((averageVel).multiply(deltaT));
		acceleration = new Vector(0,0,0);*/

		//more compact
		velocity = velocity.sum(velocity.sum(acceleration.multiply(deltaT))).multiply(0.5);
		position = position.sum(position.sum(velocity.multiply(deltaT))).multiply(0.5);
	}

	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}
	
	public double getMass() {
		return mass;
	}
	public void setGravityForce(Vector force) {
		gravityForce = force;
	}
	public void setAcceleration(Vector acc) {
		acceleration = acc;
	}
	public Vector getAcceleration() {
		return acceleration;
	}
}
