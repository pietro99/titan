public class Planet extends Body{
	private String name;
	
	//constructor
	public Planet(String name, Vector initialPos, Vector initialVelocity, double radius, double mass, double distanceAtmosphere, double atmosphericPressureComparedToEarthPressure) {    //distanceAtmosphere for titan = 600  and atmosphericPressureComparedToEarthPressure for titan = 1.5 (km)
		this.position = initialPos;
		this.velocity = initialVelocity;
		this.radius = radius;
		this.mass = mass;
		this.acceleration = new Vector(0,0,0);
		this.distanceAtmosphere = distanceAtmosphere;
		this.atmosphericPressureComparedToEarthPressure = atmosphericPressureComparedToEarthPressure;
	}

	public String getName() {
		return name;
	}

	@Override
	public Vector getPosition() {
		return super.getPosition();
	}

	public double getDistanceAtmosphere(){
		return distanceAtmosphere;
	}

	public double getAtmosphericPressureComparedToEarthPressure(){
		return atmosphericPressureComparedToEarthPressure;
	}
}
