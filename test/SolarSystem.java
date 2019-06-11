

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
		gravityCalculator = new Euler(this);
		solarSystemGUI = new SolarSystemGUI(this);
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
			bestDistance = shuttle.getPosition().distance(planets[10].getPosition());
			bestPos = shuttle.getPosition();
			bestTitan = planets[10].getPosition();
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
		Vector vel = new Vector(planets[3].getPosition().subtract(planets[10].getPosition().multiply(-1)));
		vel = vel.normalize();
		vel = vel.multiply(80*10000);
		vel = vel.sum(planets[10].velocity);
		//shuttle = new Shuttle(new Vector(192417.8004932324, -925027.0853926808, -558.466505544255 ), 100);
		shuttle = Shuttle.getStandardShuttle();

	}

	//check if the shuttle crashed:
	private void checkCrush() {
		if(shuttle!= null) {
			//if crashes on Titan:
			if(shuttle.getPosition().subtract(planets[10].getPosition()).squareLength() < Math.pow(planets[10].getRadius(), 2)) {
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
				System.out.println("Angle (deg): " + (180 / Math.PI) * Math.acos(dot) + " " + dot);
				System.out.println("Angle (rad): " + (Math.acos(dot)));
				//System.out.println("Angle - velocity: " + (180 / Math.PI) * Math.acos(shuttle.getDirection(2).dot(shuttle.getVelocity()) / (shuttle.getVelocity().length() * shuttle.getDirection(2).length())));
				System.out.println("Angle - velocity: " + (180 / Math.PI) * Math.acos(shuttle.getDirection(2).dot(v) / (v.length() * shuttle.getDirection(2).length())));
				System.out.println("Speed: " + (v.length() / 10000));
				System.out.println("Angular speed (deg): " + shuttle.getAngularSpeed().multiply(180 / Math.PI) + " -> " + shuttle.getAngularSpeed().multiply(180 / Math.PI).length());
				System.out.println("Angular speed (rad): " + shuttle.getAngularSpeed() + " -> " + shuttle.getAngularSpeed().length());
				System.out.println("Mass: " + shuttle.getMass());
				System.out.println("!!! LANDED ON TITAN !!!");
				done = true;
				System.exit(0);
			}
			else {//crashes somewhere else:
				for(int j = 0; j < planets.length - 1; j++) {
					if(shuttle!=null && shuttle.getPosition().subtract(planets[j].getPosition()).squareLength() < Math.pow(planets[j].getRadius(), 2))
						shuttle = null;
				}
			}
		}
	}

	//initiate Planets with initial position:
	private void initiatePlanets() {
		//the coordinates origin is the sun for the planets and titan, the moon uses the earth as origin instead
		//		 name						 PosX					  PosY					PosZ                                     VelX                   VelY                   VelZ             			Radius       Mass
		planets[0] = new Planet(    "sun", new Vector(     0,                       0,                    0),                   new Vector(        0,                      0,                     0),                                695700,   1988500E+23, 0, 0);
		planets[1] = new Planet("mercury", new Vector(-5.843237462283994E+07,-2.143781663349622E+07,3.608679295141068E+06),     new Vector(6.693497964118796E+00*10000,-4.362708337948559E+01*10000,-4.178969254985038E+00*10000),   2440,     3.302E23,0,0);
		planets[2] = new Planet(  "venus", new Vector(-2.580458154996926E+06,-1.087011239119300E+08,-1.342601858592726E+06),    new Vector(3.477728421647656E+01*10000,-9.612123998925466E-01*10000,-2.020103291838695E+00*10000),   6051.84,  48.685E23,0,0);
		planets[3] = new Planet(  "earth", new Vector(-1.490108621500159E+08,-2.126396301163715E+06,1.388910094132880E+02),     new Vector(-6.271192280390987E-02*10000,-2.988491242814953E+01*10000,1.101633412416092E-03*10000),   6371.01,  5.97219E24,100,1);
		planets[4] = new Planet(   "mars", new Vector(2.324287216682393E+07,2.314995121129051E+08,4.280415324853942E+06),       new Vector(-2.319279679672309E+01*10000,4.479321568516172E+00*10000,6.629375340168080E-01*10000),    3389.92,  6.4171E23,0,0);

		planets[5] = new Planet("jupiter", new Vector(-2.356728008848499E+08,-7.610014493571992E+08,8.434013543900371E+06),     new Vector(1.233529763939601E+01*10000,-3.252405720855331E+00*10000,-2.624998782087296E-01*10000),   69911,    1898.13E24,0,0);
		planets[6] = new Planet("saturn", new Vector(3.547591201282651E+08,-1.461948963315429E+09,1.129264446065086E+07),       new Vector(8.868556258040849E+00*10000,2.246063396151365E+00*10000,-3.919338649448527E-01*10000),    58232,    5.6834E26,0,0);
		planets[7] = new Planet("uranus", new Vector(2.520721627475109E+09,1.570265333451912E+09,-2.681126672206974E+07),       new Vector(-3.638527457446034E+00*10000,5.459445391405725E+00*10000,6.723572113095622E-02*10000),    25362,    86.813E24,0,0);
		planets[8] = new Planet("neptune", new Vector(4.344787259046247E+09,-1.083664330264994E+09,-7.782633401672775E+07),     new Vector(1.292292607032268E+00*10000,5.304279525500773E+00*10000,-1.390977388629209E-01*10000),    24624,    102.413E24,0,0);

		planets[9] = new Planet("moon", new Vector(-1.493626859901140E+08,-2.212378435248749E+06,3.162933122716530E+04),        new Vector(1.540496550112790E-01*10000,-3.094661877857872E+01*10000,2.193857468353855E-02*10000),     1737.4,   7.349E22,0,0);
		planets[10]= new Planet("titan", new Vector(3.537424927743304E+08,-1.462539028125231E+09,1.169787519537956E+07),        new Vector(1.208193089270527E+01*10000,-1.813839579262785E+00*10000,1.381017323560965E+00*10000),     2575.5,   13455.3E19, 600, 1.5);
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
}
