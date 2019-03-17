
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SolarSystem extends Group{
	
	public int counter=0;
	public final double TIME = 0.05;
	
	private static Planet[] planets = new Planet[11];
	private static Circle[] planetCircles = new Circle[11];
	//planets size in pixels
	private final double SUN_SIZE = 6;
	private final double SMALL_SIZE = 1;
	private final double EARTH_SIZE = 2;
	private final double SATURN_SIZE = 4;
	private final double MEDIUM_SIZE = 3;
	private final double LARGE_SIZE = 5.5;
	
	private double scale = 4e6;//scaling factor
	private Vector movingFactor = new Vector(500,525,0);
	
	public SolarSystem() {
		//the coordinates origin is the sun for the planets and titan, the moon uses the earth as origin instead
		
		
		//		 name						 PosX					  PosY					PosZ                                     VelX                   VelY                   VelZ               Radius       Mass
		
		planets[0] = new Planet(    "sun",        new Vector(     0,                       0,                    0),                   new Vector(        0,                      0,                     0),              695700,   1988500E+23);
		planets[1] = new Planet("mercury",    new Vector(-5.843237462283994E+07,-2.143781663349622E+07,3.608679295141068E+06),     new Vector(6.693497964118796E+00*10000,-4.362708337948559E+01*10000,-4.178969254985038E+00*10000),   2440,     3.302E23);
		planets[2] = new Planet(  "venus",      new Vector(-2.580458154996926E+06,-1.087011239119300E+08,-1.342601858592726E+06),    new Vector(3.477728421647656E+01*10000,-9.612123998925466E-01*10000,-2.020103291838695E+00*10000),   6051.84,  48.685E23);
		planets[3] = new Planet(  "earth",      new Vector(-1.490108621500159E+08,-2.126396301163715E+06,1.388910094132880E+02),     new Vector(-6.271192280390987E-02*10000,-2.988491242814953E+01*10000,1.101633412416092E-03*10000),   6371.01,  5.97219E24);
		planets[4] = new Planet(   "mars",       new Vector(2.324287216682393E+07,2.314995121129051E+08,4.280415324853942E+06),       new Vector(-2.319279679672309E+01*10000,4.479321568516172E+00*10000,6.629375340168080E-01*10000),    3389.92,  6.4171E23);
		
		planets[5] = new Planet("jupiter",    new Vector(-2.356728008848499E+08,-7.610014493571992E+08,8.434013543900371E+06),     new Vector(1.233529763939601E+01*10000,-3.252405720855331E+00*10000,-2.624998782087296E-01*10000),   69911,    1898.13E24);
		planets[6] = new Planet( "saturn",     new Vector(3.547591201282651E+08,-1.461948963315429E+09,1.129264446065086E+07),      new Vector(8.868556258040849E+00*10000,2.246063396151365E+00*10000,-3.919338649448527E-01*10000),    58232,    5.6834E26);
		planets[7] = new Planet( "uranus",     new Vector(2.520721627475109E+09,1.570265333451912E+09,-2.681126672206974E+07),      new Vector(-3.638527457446034E+00*10000,5.459445391405725E+00*10000,6.723572113095622E-02*10000),    25362,    86.813E24);
		planets[8] = new Planet("neptune",    new Vector(4.344787259046247E+09,-1.083664330264994E+09,-7.782633401672775E+07),     new Vector(1.292292607032268E+00*10000,5.304279525500773E+00*10000,-1.390977388629209E-01*10000),    24624,    102.413E24);
		
		planets[9] = new Planet(   "moon",       new Vector(-3.518238400980993E+04,-8.598213408503399E+03,3.149044021775210E+03),     new Vector(2.167615778151889E-01*10000,-1.061706350429193E+00*10000,2.083694127112523E-02*10000),     1737.4,   7.349E22);
		planets[10]= new Planet(  "titan",      new Vector(3.537424927743304E+08,-1.462539028125231E+09,1.169787519537956E+07),      new Vector(1.208193089270527E+01*10000,-1.813839579262785E+00*10000,1.381017323560965E+00*10000),    2575.5,   13455.3E19);
		
		
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

			
			// add the circles to the SolarSystem group
			for(int i=0; i<planetCircles.length; i++) {
				getChildren().add(planetCircles[i]);
			}
	        
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
	        planetCircles[9].fillProperty().set(Color.RED);
	        planetCircles[10].fillProperty().set(Color.RED);
	        
	}
	
	
	
	//method for updating solar system
	public void updateSolarSystem() {
		
//	//	accuracy check: get the distance between the earth after an year in the simulation and the earth after an year accordig to the data.
//		counter+=500;
//		if(counter == 60*60*24*365) {
//			System.out.println(getEarth().getPosition().distance(new Vector(-1.490108437771287E+08,-2.895909986831957E+06,2.165455838982249E+03)));
//			System.exit(0);
//		}
		
		
		calculateGravity();
		for(int i=0; i<planets.length; i++) {
        	 planetCircles[i].setLayoutX((planets[i].getPosition().getX()/scale)+movingFactor.getX());
        	 planetCircles[i].setLayoutY((planets[i].getPosition().getY()/scale)+movingFactor.getY());
         }
     }
	
	
	
	
	public void calculateGravity() {
		for(int i=0; i<planets.length; i++) 
			planets[i].calculateGravityForce();
		
		for(int i=0; i<planets.length; i++) 
			planets[i].updateVelocityAndPosition(TIME);
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
	public void setScale(double scale) {
		this.scale = scale;
	}
	public double getScale() {
		return scale;
	}
	public void setMovingFactor(double x, double y) {
		movingFactor = new Vector(x,y,0);
	}
	public Vector getMovingFactor() {
		return movingFactor;
	}
	public static Planet[] getPlanets() {
		return planets;
	}
}
