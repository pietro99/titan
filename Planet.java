import java.util.ArrayList;

public class Planet extends Body{
	private String name;
	double distanceAtmosphere;
	double atmosphericPressureComparedToEarthPressure;

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

	public Planet(String name, Vector initialPos, Vector initialVelocity, double radius, double mass) {    //distanceAtmosphere for titan = 600  and atmosphericPressureComparedToEarthPressure for titan = 1.5 (km)
		this(name, initialPos, initialVelocity, radius, mass, 0, 0);
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

	public void setNextDataFirst(Vector VelocityDayMinus3, Vector VelocityDayMinus2, Vector VelocityDayMinus1, Vector VelocityActualDay, Vector AccelerationDayMinus3, Vector AccelerationDayMinus2, Vector AccelerationDayMinus1, Vector AccelerationActualDay,  Vector PositionActualDay){
		// ArrayList<Vector> nextData = new ArrayList<>();

		// nextVelocity = new FourAdamsBashfort(SolarSystem.TIME, velocity4, acceleration1, acceleration2, acceleration3, acceleration4 );
		//nextData.add(nextVelocity.getNext());
		nextVelocity = new FourAdamsBashfort(SolarSystem.TIME, VelocityActualDay, AccelerationActualDay, AccelerationDayMinus1, AccelerationDayMinus2,AccelerationDayMinus3);
		 nextVelocity.getNext();
		 //nextData.add(nextVelocity.getNext());

		// nextPosition = new FourAdamsBashfort(SolarSystem.TIME, lastPosition, velocity1, velocity2, velocity3,  velocity4);
		//nextData.add(nextPosition.getNext());
		nextPosition = new FourAdamsBashfort(SolarSystem.TIME, PositionActualDay, VelocityActualDay, VelocityDayMinus1,VelocityDayMinus2,VelocityDayMinus3);
		nextPosition.getNext();

	}
}
