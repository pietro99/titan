import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SolarSystem extends Group{
	
	//planets size in pixel.............
	private final double SUN_SIZE = 8;
	private final double SMALL_SIZE = 1;
	private final double EARTH_SIZE = 2;
	private final double SATURN_SIZE = 4;
	private final double MEDIUM_SIZE = 3;
	private final double LARGE_SIZE = 5.5;
	//...................................
	
	private double scale = 4e6;//scaling factor
	private Vector movingFactor = new Vector(500,525,0);//transaltion on the axis factor
	

	
//the coordinates origin is the sun for the planets and titan, the moon uses the earth as origin instead
	
	
//										 name						 PosX					  PosY					PosZ                                     VelX                   VelY                   VelZ               Radius       Mass
	
	
	private Planet sun = new Planet(    "sun",        new Vector(     0,                       0,                    0),                   new Vector(        0,                      0,                     0),              695700,   1988500E+23);
	private Planet mercury = new Planet("mercury",    new Vector(-5.843237462283994E+07,-2.143781663349622E+07,3.608679295141068E+06),     new Vector(6.693497964118796E+00,-4.362708337948559E+01,-4.178969254985038E+00),   2440,     3.302E23);
	private Planet venus = new Planet(  "venus",      new Vector(-2.580458154996926E+06,-1.087011239119300E+08,-1.342601858592726E+06),    new Vector(3.477728421647656E+01,-9.612123998925466E-01,-2.020103291838695E+00),   6051.84,  48.685E23);
	private Planet earth = new Planet(  "earth",      new Vector(-1.490108621500159E+08,-2.126396301163715E+06,1.388910094132880E+02),     new Vector(-6.271192280390987E-02,-2.988491242814953E+01,1.101633412416092E-03),   6371.01,  5.97219E24);
	private Planet mars = new Planet(   "mars",       new Vector(2.324287216682393E+07,2.314995121129051E+08,4.280415324853942E+06),       new Vector(-2.319279679672309E+01,4.479321568516172E+00,6.629375340168080E-01),    3389.92,  6.4171E23);
	private Planet jupiter = new Planet("jupiter",    new Vector(-2.356728008848499E+08,-7.610014493571992E+08,8.434013543900371E+06),     new Vector(1.233529763939601E+01,-3.252405720855331E+00,-2.624998782087296E-01),   69911,    1898.13E24);
	private Planet saturn = new Planet( "saturn",     new Vector(3.547591201282651E+08,-1.461948963315429E+09,1.129264446065086E+07),      new Vector(8.868556258040849E+00,2.246063396151365E+00,-3.919338649448527E-01),    58232,    5.6834E26);
	private Planet uranus = new Planet( "uranus",     new Vector(2.520721627475109E+09,1.570265333451912E+09,-2.681126672206974E+07),      new Vector(-3.638527457446034E+00,5.459445391405725E+00,6.723572113095622E-02),    25362,    86.813E24);
	private Planet neptune = new Planet("neptune",    new Vector(4.344787259046247E+09,-1.083664330264994E+09,-7.782633401672775E+07),     new Vector(1.292292607032268E+00,5.304279525500773E+00,-1.390977388629209E-01),    24624,    102.413E24);
	private Planet moon = new Planet(   "moon",       new Vector(-3.518238400980993E+05,-8.598213408503399E+04,3.149044021775210E+04),     new Vector(2.167615778151889E-01,1.061706350429193E+00,2.083694127112523E-02),     1737.4,   7.349E22);
	private Planet titan = new Planet(  "titan",      new Vector(3.537424927743304E+08,-1.462539028125231E+09,1.169787519537956E+07),      new Vector(1.208193089270527E+01,-1.813839579262785E+00,1.381017323560965E+00),    2575.5,   13455.3E19);
	
	
	//circles for the GUI
	private Circle sunCircle = new Circle(SUN_SIZE);
	private Circle mercuryCircle = new Circle(EARTH_SIZE);
	private Circle venusCircle = new Circle(EARTH_SIZE);
	private Circle marsCircle = new Circle(EARTH_SIZE);
	private Circle earthCircle = new Circle(EARTH_SIZE);
	private Circle jupiterCircle = new Circle(LARGE_SIZE);
	private Circle saturnCircle = new Circle(SATURN_SIZE);
	private Circle uranusCircle = new Circle(MEDIUM_SIZE);
	private Circle neptuneCircle = new Circle(MEDIUM_SIZE);
	private Circle moonCircle = new Circle(SMALL_SIZE);
	private Circle titanCircle = new Circle(SMALL_SIZE);
	
	//cunstructor
	public SolarSystem() {
			
			//add the moons
			sun.addMoon(mercury);
			sun.addMoon(venus);
			sun.addMoon(earth);
			sun.addMoon(mars);
			sun.addMoon(jupiter);
			sun.addMoon(saturn);
			sun.addMoon(uranus);
			sun.addMoon(neptune);
			earth.addMoon(moon);
			
			// add the circles to the SolarSystem group
	        getChildren().addAll(
	        			titanCircle,moonCircle,sunCircle,mercuryCircle,venusCircle,earthCircle,marsCircle,jupiterCircle ,saturnCircle,uranusCircle,neptuneCircle
	        		);
	        
	        
	        //color the circles
	        mercuryCircle.fillProperty().set(Color.CORNFLOWERBLUE);
	        venusCircle.fillProperty().set(Color.CHOCOLATE);
	        earthCircle.fillProperty().set(Color.GREEN);
	        marsCircle.fillProperty().set(Color.BROWN);
	        jupiterCircle.fillProperty().set(Color.BURLYWOOD);
	        saturnCircle.fillProperty().set(Color.ANTIQUEWHITE);
	        uranusCircle.fillProperty().set(Color.ROYALBLUE);
	        neptuneCircle.fillProperty().set(Color.SLATEBLUE);
	        sunCircle.fillProperty().set(Color.YELLOW);
	        moonCircle.fillProperty().set(Color.GRAY);
	        titanCircle.fillProperty().set(Color.RED);
	        
	        
	}
	
	//method for updating solar system
	public void updateSolarSystem() {
		
        mercuryCircle.setLayoutX((mercury.getPosition().getX()/scale)+movingFactor.getX());
        mercuryCircle.setLayoutY((mercury.getPosition().getY()/scale)+movingFactor.getY());
        venusCircle.setLayoutX((venus.getPosition().getX()/scale)+movingFactor.getX());
        venusCircle.setLayoutY((venus.getPosition().getY()/scale)+movingFactor.getY());
        marsCircle.setLayoutX((mars.getPosition().getX()/scale)+movingFactor.getX());
        marsCircle.setLayoutY((mars.getPosition().getY()/scale)+movingFactor.getY());
        earthCircle.setLayoutX((earth.getPosition().getX()/scale)+movingFactor.getX());
        earthCircle.setLayoutY((earth.getPosition().getY()/scale)+movingFactor.getY());
        jupiterCircle.setLayoutX((jupiter.getPosition().getX()/scale)+movingFactor.getX());
        jupiterCircle.setLayoutY((jupiter.getPosition().getY()/scale)+movingFactor.getY());
        saturnCircle.setLayoutX((saturn.getPosition().getX()/scale)+movingFactor.getX());
        saturnCircle.setLayoutY((saturn.getPosition().getY()/scale)+movingFactor.getY());
        uranusCircle.setLayoutX((uranus.getPosition().getX()/scale)+movingFactor.getX());
        uranusCircle.setLayoutY((uranus.getPosition().getY()/scale)+movingFactor.getY());
        neptuneCircle.setLayoutX((neptune.getPosition().getX()/scale)+movingFactor.getX());
        neptuneCircle.setLayoutY((neptune.getPosition().getY()/scale)+movingFactor.getY());
        titanCircle.setLayoutX((titan.getPosition().getX()/scale)+movingFactor.getX());
        titanCircle.setLayoutY((titan.getPosition().getY()/scale)+movingFactor.getY());
        sunCircle.setLayoutX(sun.getPosition().getX()+movingFactor.getX());
        sunCircle.setLayoutY(sun.getPosition().getY()+movingFactor.getY());
        setMoonLayout();
        
	}
	
	
	//convert the moon coordinate system to the sun coordinate system
	public void setMoonLayout() {
		Vector pos = earth.getPosition().sum(moon.getPosition());
		moonCircle.setLayoutX((pos.getX()/scale)+movingFactor.getX());
		moonCircle.setLayoutY((pos.getY()/scale)+movingFactor.getY());
	}
	
	
	
	//setters and getters
	public Planet getSun() {
		return sun;
	}
	public Planet getMercury() {
		return mercury;
	}
	public Planet getVenus() {
		return venus;
	}
	public Planet getEarth() {
		return earth;
	}
	public Planet getMars() {
		return mars;
	}
	public Planet getJupiter() {
		return jupiter;
	}
	public Planet getSaturn() {
		return saturn;
	}
	public Planet getUranus() {
		return uranus;
	}
	public Planet getNeptune() {
		return neptune;
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
}
