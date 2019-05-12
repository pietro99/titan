import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SolarSystem extends Group{
	
	private int counter=0;
	private final double TIME = 0.05;
	
	public static Planet[] planets = new Planet[11];
	private static boolean done = false;
	public Shuttle shuttle;
	public static Shuttle best;
	public static Vector bestPos, bestTitan, correction;
	public static double bestDistance;
	public static int time = 0;
	public static int bestTime = 0;

	private static Circle[] planetCircles = new Circle[11];
	private static Circle shuttleCircle = new Circle(1);
	//planets size in pixels
	private final double SUN_SIZE = 6;
	private final double SMALL_SIZE = 1;
	private final double EARTH_SIZE = 2;
	private final double SATURN_SIZE = 4;
	private final double MEDIUM_SIZE = 3;
	private final double LARGE_SIZE = 5.5;
	
	private static double scale = 4e6;//scaling factor
	private static Vector movingFactor = new Vector(500,525,0);
	
	public SolarSystem() {
		//-235301.33181875394 -800572.9273890787 477.10348273478763
		//-235955.3311477492 -800117.3184090039 477.08528139046257
		time = 0;
		
		initiatePlanets();
		initiateCircles();
		initiateShuttle();
	}
	


	//method for updating solar system
	public void updateSolarSystem() {

		calculateGravity();
		
		updateGUI();
		
			
		}
		

	public void calculateGravity() {
		time++;
		
		//reset force and acceleration
		resetAcceleration();
		calculateGravityforPlanets();
		calculateGravityForShuttleAndUpdatePosition();
		checkCrash();
		if(shuttle!=null) {
			bestDistance = shuttle.getPosition().distance(planets[10].getPosition());
			bestPos = shuttle.getPosition();
			bestTitan = planets[10].getPosition();
		}
		updatePlanetsPosition();
	}
	
	


	private void updatePlanetsPosition() {
		for(int i=0; i<planets.length; i++)
			planets[i].update(TIME);
	}



	private void calculateGravityForShuttleAndUpdatePosition() {
		if(shuttle != null) {
			shuttle.calculateGravity(planets);
			shuttle.update(TIME);
		}
	}



	private void calculateGravityforPlanets() {
		for(int i = 0; i < planets.length; i++) {	//first planet
			for(int j = 1; j < planets.length - i; j++) {        //other planet
				Vector distance = planets[i].getPosition().subtract(planets[i + j].getPosition());	//from j to i
				double d = distance.squareLength();
				distance = distance.normalize();
				planets[i + j].addAcceleration(distance.multiply(gravity(planets[i], d)));
				planets[i].addAcceleration(distance.multiply(-1 * gravity(planets[i+j], d)));
			}
		}
	}
	
	private double gravity( Planet p, double d) {
		return (Physics.G * p.getMass())/d;
	}

	private void initiateShuttle() {
		Vector vel = new Vector(planets[3].getPosition().subtract(planets[10].getPosition().multiply(-1)));
		vel = vel.normalize();
		vel = vel.multiply(80*10000);
		vel = vel.sum(planets[10].velocity);
		shuttle = new Shuttle(new Vector(192417.8004932324, -925027.0853926808, -558.466505544255 ), 100);
	//	System.out.prinlnt(shuttle.getVelocity());
		//192417.8004932324, -925027.0853926808, -558.466505544255 landing in 1 year
		
	}

	private void checkCrash() {
		if(shuttle!= null) {
			//if crashes on Titan:
			if(shuttle.getPosition().subtract(planets[10].getPosition()).squareLength() < Math.pow(planets[10].getRadius() + planets[10].getDistanceAtmosphere(), 2)) {
			//if(shuttle.getPosition().subtract(planets[10].getPosition()).squareLength() < Math.pow(planets[10].getRadius(), 2)) {
				System.out.println("shuttle initial vector speed: " + shuttle.init);
				System.out.println("shuttle initial speed: " + shuttle.init.length());
				System.out.println("distance shuttle-titan: " + shuttle.getPosition().distance(planets[10].getPosition()));
				System.out.println("LANDED ON TITAN");
				System.out.println("Direction Z: " + shuttle.getDirection(2));
				System.out.println("Angle: " + Math.acos(planets[10].getPosition().subtract(shuttle.getPosition()).normalize().dot(shuttle.getDirection(2).normalize())));
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
	
	private void updateGUI() {
		for(int i=0; i<planets.length; i++) {
	       	planetCircles[i].setLayoutX((planets[i].getPosition().getX()/scale)+movingFactor.getX());
	        planetCircles[i].setLayoutY((planets[i].getPosition().getY()/scale)+movingFactor.getY());
		}
		if(shuttle != null) {
			shuttleCircle.setLayoutX((shuttle.getPosition().getX() / scale) + movingFactor.getX());
			shuttleCircle.setLayoutY((shuttle.getPosition().getY() / scale) + movingFactor.getY());
		}
		else 
			shuttleCircle.fillProperty().set(Color.BLACK);
		
	}

	private void initiateCircles() {
	//circles for the GUI
		planetCircles[0] = new Circle(SUN_SIZE);
		planetCircles[1] = new Circle(EARTH_SIZE);
		planetCircles[2] = new Circle(EARTH_SIZE);
		planetCircles[3] = new Circle(EARTH_SIZE);
		planetCircles[4] = new Circle(EARTH_SIZE);
		planetCircles[5] = new Circle(LARGE_SIZE);
		planetCircles[6] = new Circle(SATURN_SIZE);
		planetCircles[7] = new Circle(MEDIUM_SIZE);
		planetCircles[8] = new Circle(MEDIUM_SIZE);
		planetCircles[9] = new Circle(SMALL_SIZE);
		planetCircles[10] = new Circle(SMALL_SIZE);
		
		
		
		//color the circles
		planetCircles[0].fillProperty().set(Color.YELLOW);
		planetCircles[1].fillProperty().set(Color.BLUE);
		planetCircles[2].fillProperty().set(Color.CHOCOLATE);
		planetCircles[3].fillProperty().set(Color.GREEN);
		planetCircles[4].fillProperty().set(Color.BROWN);
		planetCircles[5].fillProperty().set(Color.BURLYWOOD);
		planetCircles[6].fillProperty().set(Color.ANTIQUEWHITE);
		planetCircles[7].fillProperty().set(Color.ROYALBLUE);
		planetCircles[8].fillProperty().set(Color.SLATEBLUE);
		planetCircles[9].fillProperty().set(Color.GRAY);
		planetCircles[10].fillProperty().set(Color.GRAY);
		
		shuttleCircle = new Circle(SMALL_SIZE);
		shuttleCircle.fillProperty().set(Color.RED);
		
		
	 // add the circles to the SolarSystem group
		for(int i=0; i<planetCircles.length; i++) 
			getChildren().add(planetCircles[i]);
		
		getChildren().add(shuttleCircle);
		
		
	}

	private void initiatePlanets() {
	//the coordinates origin is the sun for the planets and titan, the moon uses the earth as origin instead
							//		 name						 PosX				PosY				PosZ                                     VelX                              VelY                        VelZ                  Radius      Mass               distance of the atmosphere    Atmospheric pressure compared to earth pressure
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

	private void resetAcceleration() {
		for(int i = 0; i < planets.length; i++) 
			planets[i].setAcceleration(new Vector(0,0,0));
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
	public void setScale(double scale) {
		this.scale = scale;
	}
	public static double getScale() {
		return scale;
	}
	public void setMovingFactor(double x, double y) {
		movingFactor = new Vector(x,y,0);
	}
	public static Vector getMovingFactor() {
		return movingFactor;
	}
	public static Planet[] getPlanets() {
		return planets;
	}
	public static Circle getShuttleCircle() {
		return shuttleCircle;
	}
	public static Circle[] getPlanetCircles() {
		return planetCircles;
	}
	public static Planet getPlanet(int i) {
		return planets[i];
	}
}
