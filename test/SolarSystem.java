

/**
 * the SolarSystem class is responsible for creating and updating planets and shuttle position with the implementation
 * of gravity
 */
public class SolarSystem {
	private static double timeStep = 0.05;
	private SolarSystemGUI solarSystemGUI;
	public static Planet[] planets = new Planet[11];
	private static boolean done = false;
	private GravityMethod gravityCalculator;
	public Shuttle shuttle;
	public static Vector bestPos, bestTitan;
	public static double bestDistance;
	public static int bestTime = 0;

	//initiate the solar system with the planets and the shuttle initial position
	public SolarSystem() {
		initiatePlanets();
		initiateShuttle();
		/*gravityCalculator = new Euler(this);
		solarSystemGUI = new SolarSystemGUI(this);*/
		setShuttle(shuttle);
		done = false;
	}

	//update solar system
	public void updateSolarSystem() {
		calculateGravity();
		solarSystemGUI.updateGUI();
	}

	//calculate gravity based on the GravityMethod Interface
	public void calculateGravity() {
		//reset force and acceleration
		gravityCalculator.calculateForce();
		if(shuttle != null) {
			//bestDistance = shuttle.getPosition().distance(planets[10].getPosition());
			//bestPos = shuttle.getPosition();
			//bestTitan = planets[10].getPosition();
			//update shuttle position
			shuttle.update(timeStep);
		}
		checkCrush();
		updatePlanetsPosition();
	}

	//update planets new position:
	private void updatePlanetsPosition() {
		for(int i=0; i<planets.length; i++)
			planets[i].update(timeStep);
	}

	//initiate shuttle with initial velocity:
	private void initiateShuttle() {
		/*Vector vel = new Vector(getEarth().getPosition().subtract(planets[10].getPosition().multiply(-1)));
		vel = vel.normalize();
		vel = vel.multiply(80*10000);
		vel = vel.sum(planets[10].velocity);*/
		//shuttle = new Shuttle(new Vector(192417.8004932324, -925027.0853926808, -558.466505544255 ), 100);
		shuttle = Shuttle.getStandardShuttle();
	}

	//check if the shuttle crashed:
	private void checkCrush() {
		if(shuttle!= null) {
			//if crashes on Titan:
			if(/*!Runner.back &&*/ shuttle.getPosition().subtract(planets[10].getPosition()).squareLength() < Math.pow(planets[10].getRadius(), 2)) {
				Vector v = shuttle.getVelocity().subtract(planets[10].getVelocity());
				Vector dist = shuttle.getPosition().subtract(planets[10].getPosition());
				double d = dist.length();

				System.out.println("shuttle initial vector speed: " + shuttle.init);
				System.out.println("shuttle initial speed: " + shuttle.init.length());
				System.out.println("distance shuttle-titan: " + shuttle.getPosition().distance(planets[10].getPosition()));
				System.out.println("Direction Z: " + shuttle.getDirection(2));
				//System.out.println("Angle (deg): " + (180 / Math.PI) * Math.acos(planets[10].getPosition().subtract(shuttle.getPosition()).normalize().dot(shuttle.getDirection(2).normalize())));
				double dot = dist.normalize().dot(shuttle.getDirection(2).normalize());
				//roundoff error
				if(dot > 1)
					dot = 1;
				else if (dot < -1)
					dot = -1;
				//System.out.println("Angle (deg): " + (180 / Math.PI) * Math.acos(dot) + " " + dot);
				System.out.println("Angle (rad): " + (Math.acos(dot)));
				//System.out.println("Angle - velocity: " + (180 / Math.PI) * Math.acos(shuttle.getDirection(2).dot(shuttle.getVelocity()) / (shuttle.getVelocity().length() * shuttle.getDirection(2).length())));
				//System.out.println("Angle - velocity: " + (180 / Math.PI) * Math.acos(shuttle.getDirection(2).dot(v) / (v.length() * shuttle.getDirection(2).length())));
				System.out.println("Speed: " + (v.length() / 1e4));
				//System.out.println("Angular speed (deg): " + shuttle.getAngularSpeed().multiply(180 / Math.PI) + " -> " + shuttle.getAngularSpeed().multiply(180 / Math.PI).length());
				System.out.println("Angular speed (rad): " + shuttle.getAngularSpeed() + " -> " + shuttle.getAngularSpeed().length());
				System.out.println("Mass: " + shuttle.getMass());
				System.out.println("Cost estimate: " + shuttle.getCostEstimate());
				System.out.println("Cost: " + shuttle.cost());
				System.out.println("!!! LANDED ON TITAN !!!");
				done = true;
				//System.exit(0);
			}
			else {//crashes somewhere else:
				for(int j = 0; j < planets.length - 1; j++) {
					if(shuttle!=null && shuttle.getPosition().subtract(planets[j].getPosition()).squareLength() < Math.pow(planets[j].getRadius(), 2)) {
						if(planets[j] != getEarth()) {
							//shuttle = null;
							//System.out.println("Wrong planet");
						}else
							System.out.println("Hit Earth");
					}
				}
			}
		}
	}

	//initiate Planets with initial position:
	private void initiatePlanets() {
		//the coordinates origin is the sun for the planets and titan, the moon uses the earth as origin instead
		//		 name						 PosX					  PosY					PosZ                                     VelX                   VelY                   VelZ             			Radius       Mass
		planets = Planet.make();
	}

	//setters and getters
	public Planet getSun() {
		return planets[0];
	}
	public Planet getMercury() {
		return planets[1];
	}
	public Planet getVenus() {
		return planets[2];
	}
	public Planet getEarth() {
		return planets[3];
	}
	public Planet getMars() {
		return planets[4];
	}
	public Planet getJupiter() {
		return planets[5];
	}
	public Planet getSaturn() {
		return planets[6];
	}
	public Planet getUranus() {
		return planets[7];
	}
	public Planet getNeptune() {
		return planets[8];
	}
	public Planet getMoon() {
		return planets[9];
	}
	public Planet getTitan() {
		return planets[10];
	}
	public Shuttle getShuttle() {
		return shuttle;
	}
	public void setShuttle(Shuttle newShuttle) {
		shuttle = newShuttle;
		gravityCalculator = new Gravity(this);
		solarSystemGUI = new SolarSystemGUI(this);
	}
	public static Planet[] getPlanets() {
		return planets;
	}
	public Planet getPlanet(int i) {
		return planets[i];
	}
	public SolarSystemGUI getGUI(){
		return solarSystemGUI;
	}
	public static void setTimeStep(double t){
		timeStep = t;
	}
	public static double getTimeStep(){
		return timeStep;
	}
	public boolean getDone() {
		return done;
	}
}
