import java.util.ArrayList;

public class Planet extends Body{
	private String name;
	double distanceAtmosphere;
	double atmosphericPressureComparedToEarthPressure;
	private static boolean mix = false;

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

	private static double factor = 10000;
	public static Planet makeSun() {
		Planet p = new Planet(    "sun", new Vector(     0,                       0,                    0),                   new Vector(        0,                      0,                     0),                                695700,   1988500E+23, 0, 0);
		Vector velocityDayMinus3 = new Vector(0, 0, 0).multiply(Planet.factor);      		 // on day 1
		Vector velocityDayMinus2 = new Vector(0, 0, 0).multiply(Planet.factor);   		 // on day 2
		Vector velocityDayMinus1 = new Vector(0, 0, 0).multiply(Planet.factor);        		// on day 3
		Vector velocityActualDay = new Vector(0, 0, 0).multiply(Planet.factor);			//on day 4 -> today

		//set acceleration 	-> use 3-points formula
		Vector accelerationDayMinus3 = FourAdamsBashfort.forwardDiff(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, 1);
		Vector accelerationDayMinus2 = FourAdamsBashfort.centredDiff(velocityDayMinus3, velocityDayMinus1, 1);
		Vector accelerationMinus1 = FourAdamsBashfort.centredDiff(velocityDayMinus2, velocityActualDay, 1);
		Vector accelerationActualDay = FourAdamsBashfort.backwardDiff(velocityActualDay, velocityDayMinus1, velocityDayMinus2, 1);

		Vector positionActualDay = new Vector(0, 0, 0); // day 4 --- actual day

		p.setNextDataFirst(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, velocityActualDay, accelerationDayMinus3, accelerationDayMinus2, accelerationMinus1, accelerationActualDay, positionActualDay);
		return p;
	}

	public static Planet makeMercury() {
		Planet p = new Planet("mercury", new Vector(-5.843237462283994E+07,-2.143781663349622E+07,3.608679295141068E+06),     new Vector(6.693497964118796E+00,-4.362708337948559E+01,-4.178969254985038E+00).multiply(Planet.factor),   2440,     3.302E23,0,0);
		Vector velocityDayMinus3 = new Vector(-2.154727579935500E+00, -4.596267183673503E+01, -3.558103468140175E+00).multiply(Planet.factor);      		 // on day 1
		Vector velocityDayMinus2 = new Vector(9.161143773624232E-01, -4.535157722652043E+01, -3.789880472509838E+00).multiply(Planet.factor);   		 // on day 2
		Vector velocityDayMinus1 = new Vector(3.866440257986716E+00, -4.456782968069503E+01, -3.996493507371428E+00).multiply(Planet.factor);        		// on day 3
		Vector velocityActualDay = new Vector(6.693497964118796E+00, -4.362708337948559E+01, -4.178969254985038E+00).multiply(Planet.factor);			//on day 4 -> today

		//set acceleration 	-> use 3-points formula
		Vector accelerationDayMinus3 = FourAdamsBashfort.forwardDiff(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, 1);
		Vector accelerationDayMinus2 = FourAdamsBashfort.centredDiff(velocityDayMinus3, velocityDayMinus1, 1);
		Vector accelerationMinus1 = FourAdamsBashfort.centredDiff(velocityDayMinus2, velocityActualDay, 1);
		Vector accelerationActualDay = FourAdamsBashfort.backwardDiff(velocityActualDay, velocityDayMinus1, velocityDayMinus2, 1);

		Vector positionActualDay = new Vector(-5.843237462283994E+07, -2.143781663349622E+07, 3.608679295141068E+06); // day 4 --- actual day

		p.setNextDataFirst(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, velocityActualDay, accelerationDayMinus3, accelerationDayMinus2, accelerationMinus1, accelerationActualDay, positionActualDay);
		return p;
	}

	public static Planet makeVenus() {
		Planet p = new Planet(  "venus", new Vector(-2.580458154996926E+06,-1.087011239119300E+08,-1.342601858592726E+06),    new Vector(3.477728421647656E+01,-9.612123998925466E-01,-2.020103291838695E+00).multiply(Planet.factor),   6051.84,  48.685E23,0,0);
		Vector velocityDayMinus3 = new Vector(3.458768412905381E+01, -3.864346962632811E+00, -2.048996831918141E+00).multiply(Planet.factor);      		 // on day 1
		Vector velocityDayMinus2 = new Vector(3.467766264561461E+01, -2.898233100184624E+00, -2.040932899582631E+00).multiply(Planet.factor);   		 // on day 2
		Vector velocityDayMinus1 = new Vector(3.474087155765323E+01, -1.930275950076760E+00, -2.031298861694798E+00).multiply(Planet.factor);        		// on day 3
		Vector velocityActualDay = new Vector(3.477728421647656E+01, -9.612123998925466E-01, -2.020103291838695E+00).multiply(Planet.factor);			//on day 4 -> today

		//set acceleration 	-> use 3-points formula
		Vector accelerationDayMinus3 = FourAdamsBashfort.forwardDiff(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, 1);
		Vector accelerationDayMinus2 = FourAdamsBashfort.centredDiff(velocityDayMinus3, velocityDayMinus1, 1);
		Vector accelerationMinus1 = FourAdamsBashfort.centredDiff(velocityDayMinus2, velocityActualDay, 1);
		Vector accelerationActualDay = FourAdamsBashfort.backwardDiff(velocityActualDay, velocityDayMinus1, velocityDayMinus2, 1);

		Vector positionActualDay = new Vector(-2.580458154996926E+06, -1.087011239119300E+08, -1.342601858592726E+06); // day 4 --- actual day

		p.setNextDataFirst(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, velocityActualDay, accelerationDayMinus3, accelerationDayMinus2, accelerationMinus1, accelerationActualDay, positionActualDay);
		return p;
	}

	public static Planet makeEarth() {
		Planet p = new Planet(  "earth", new Vector(-1.490108621500159E+08,-2.126396301163715E+06,1.388910094132880E+02),     new Vector(-6.271192280390987E-02,-2.988491242814953E+01,1.101633412416092E-03).multiply(Planet.factor),   6371.01,  5.97219E24,100,1);
		if(!mix) {
			Vector velocityDayMinus3 = new Vector(-1.603258420638997E+00, -2.986817756054223E+01, 3.977429717245684E-04).multiply(Planet.factor);             // on day 1
			Vector velocityDayMinus2 = new Vector(-1.089438525120777E+00, -2.988188966949264E+01, 5.903837769469789E-04).multiply(Planet.factor);         // on day 2
			Vector velocityDayMinus1 = new Vector(-5.759542750580282E-01, -2.988745606682139E+01, 8.316135863939422E-04).multiply(Planet.factor);                // on day 3
			Vector velocityActualDay = new Vector(-6.271192280390987E-02, -2.988491242814953E+01, 1.101633412416092E-03).multiply(Planet.factor);            //on day 4 -> today

			//set acceleration 	-> use 3-points formula
			Vector accelerationDayMinus3 = FourAdamsBashfort.forwardDiff(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, 1);
			Vector accelerationDayMinus2 = FourAdamsBashfort.centredDiff(velocityDayMinus3, velocityDayMinus1, 1);
			Vector accelerationMinus1 = FourAdamsBashfort.centredDiff(velocityDayMinus2, velocityActualDay, 1);
			Vector accelerationActualDay = FourAdamsBashfort.backwardDiff(velocityActualDay, velocityDayMinus1, velocityDayMinus2, 1);

			Vector positionActualDay = new Vector(-1.490108621500159E+08, -2.126396301163715E+06, 1.388910094132880E+02); // day 4 --- actual day

			p.setNextDataFirst(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, velocityActualDay, accelerationDayMinus3, accelerationDayMinus2, accelerationMinus1, accelerationActualDay, positionActualDay);
		}
		return p;
	}

	public static Planet makeMars() {
		Planet p = new Planet(   "mars", new Vector(2.324287216682393E+07,2.314995121129051E+08,4.280415324853942E+06),       new Vector(-2.319279679672309E+01,4.479321568516172E+00,6.629375340168080E-01).multiply(Planet.factor),    3389.92,  6.4171E23,0,0);
		Vector velocityDayMinus3 = new Vector(-2.312089304446956E+01, 5.111877914873205E+00, 6.744282070621321E-01).multiply(Planet.factor);      		 // on day 1
		Vector velocityDayMinus2 = new Vector(-2.314674717938052E+01, 4.900898621242897E+00, 6.706415898380620E-01).multiply(Planet.factor);   		 // on day 2
		Vector velocityDayMinus1 = new Vector(-2.317071268644678E+01, 4.690041372313497E+00, 6.668112007727518E-01).multiply(Planet.factor);        		// on day 3
		Vector velocityActualDay = new Vector(-2.319279681535404E+01, 4.479321597588995E+00, 6.629375352771729E-01).multiply(Planet.factor);			//on day 4 -> today

		//set acceleration 	-> use 3-points formula
		Vector accelerationDayMinus3 = FourAdamsBashfort.forwardDiff(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, 1);
		Vector accelerationDayMinus2 = FourAdamsBashfort.centredDiff(velocityDayMinus3, velocityDayMinus1, 1);
		Vector accelerationMinus1 = FourAdamsBashfort.centredDiff(velocityDayMinus2, velocityActualDay, 1);
		Vector accelerationActualDay = FourAdamsBashfort.backwardDiff(velocityActualDay, velocityDayMinus1, velocityDayMinus2, 1);

		Vector positionActualDay = new Vector(2.324287221167859E+07, 2.314995121135774E+08, 4.280415288364515E+06); // day 4 --- actual day

		p.setNextDataFirst(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, velocityActualDay, accelerationDayMinus3, accelerationDayMinus2, accelerationMinus1, accelerationActualDay, positionActualDay);
		return p;
	}

	public static Planet makeJupiter() {
		Planet p = new Planet("jupiter", new Vector(-2.356728008848499E+08,-7.610014493571992E+08,8.434013543900371E+06),     new Vector(1.233529763939601E+01,-3.252405720855331E+00,-2.624998782087296E-01).multiply(Planet.factor),   69911,    1898.13E24,0,0);
		Vector velocityDayMinus3 = new Vector(1.232023000200009E+01, -3.303897250546632E+00, -2.618959767974396E-01).multiply(Planet.factor);      		 // on day 1
		Vector velocityDayMinus2 = new Vector(1.232329084416636E+01, -3.285486374212564E+00, -2.620819345277658E-01).multiply(Planet.factor);   		 // on day 2
		Vector velocityDayMinus1 = new Vector(1.232974906751424E+01, -3.269422421450925E+00, -2.623017853843566E-01).multiply(Planet.factor);        		// on day 3
		Vector velocityActualDay = new Vector(1.233361263555140E+01, -3.252782848348839E+00, -2.625332120353037E-01).multiply(Planet.factor);			//on day 4 -> today

		//set acceleration 	-> use 3-points formula
		Vector accelerationDayMinus3 = FourAdamsBashfort.forwardDiff(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, 1);
		Vector accelerationDayMinus2 = FourAdamsBashfort.centredDiff(velocityDayMinus3, velocityDayMinus1, 1);
		Vector accelerationMinus1 = FourAdamsBashfort.centredDiff(velocityDayMinus2, velocityActualDay, 1);
		Vector accelerationActualDay = FourAdamsBashfort.backwardDiff(velocityActualDay, velocityDayMinus1, velocityDayMinus2, 1);

		Vector positionActualDay = new Vector(-2.356728458452976E+08, -7.610012694580332E+08, 8.434019057867110E+06); // day 4 --- actual day

		p.setNextDataFirst(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, velocityActualDay, accelerationDayMinus3, accelerationDayMinus2, accelerationMinus1, accelerationActualDay, positionActualDay);
		return p;
	}

	public static Planet makeSaturn() {
		Planet p = new Planet("saturn", new Vector(3.547591201282651E+08,-1.461948963315429E+09,1.129264446065086E+07),       new Vector(8.868556258040849E+00,2.246063396151365E+00,-3.919338649448527E-01).multiply(Planet.factor),    58232,    5.6834E26,0,0);
		if(!mix) {
			Vector velocityDayMinus3 = new Vector(8.872729518092511E+00, 2.232190577514043E+00, -3.923917984791055E-01).multiply(Planet.factor);             // on day 1
			Vector velocityDayMinus2 = new Vector(8.871105488756131E+00, 2.237224594707695E+00, -3.924277079439306E-01).multiply(Planet.factor);         // on day 2
			Vector velocityDayMinus1 = new Vector(8.869479321794495E+00, 2.242198953868266E+00, -3.924325096657389E-01).multiply(Planet.factor);                // on day 3
			Vector velocityActualDay = new Vector(8.867827359240396E+00, 2.247044412940183E+00, -3.923703407460523E-01).multiply(Planet.factor);            //on day 4 -> today

			//set acceleration 	-> use 3-points formula
			Vector accelerationDayMinus3 = FourAdamsBashfort.forwardDiff(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, 1);
			Vector accelerationDayMinus2 = FourAdamsBashfort.centredDiff(velocityDayMinus3, velocityDayMinus1, 1);
			Vector accelerationMinus1 = FourAdamsBashfort.centredDiff(velocityDayMinus2, velocityActualDay, 1);
			Vector accelerationActualDay = FourAdamsBashfort.backwardDiff(velocityActualDay, velocityDayMinus1, velocityDayMinus2, 1);

			Vector positionActualDay = new Vector(3.547593532400821E+08, -1.461948830848272E+09, 1.129255310798091E+07); // day 4 --- actual day

			p.setNextDataFirst(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, velocityActualDay, accelerationDayMinus3, accelerationDayMinus2, accelerationMinus1, accelerationActualDay, positionActualDay);
		}
		return p;
	}

	public static Planet makeUranus() {
		Planet p = new Planet("uranus", new Vector(2.520721627475109E+09,1.570265333451912E+09,-2.681126672206974E+07),       new Vector(-3.638527457446034E+00,5.459445391405725E+00,6.723572113095622E-02).multiply(Planet.factor),    25362,    86.813E24,0,0);
		Vector velocityDayMinus3 = new Vector(-3.635127613751139E+00, 5.461425186745550E+00, 6.720912295043679E-02).multiply(Planet.factor);      		 // on day 1
		Vector velocityDayMinus2 = new Vector(-3.636267070683341E+00, 5.460736089554170E+00, 6.699582916089963E-02).multiply(Planet.factor);   		 // on day 2
		Vector velocityDayMinus1 = new Vector(-3.637646054398172E+00, 5.460144532539170E+00, 6.711448714035395E-02).multiply(Planet.factor);        		// on day 3
		Vector velocityActualDay = new Vector(-3.638605615637463E+00, 5.459468350572506E+00, 6.727967066481910E-02).multiply(Planet.factor);			//on day 4 -> today

		//set acceleration 	-> use 3-points formula
		Vector accelerationDayMinus3 = FourAdamsBashfort.forwardDiff(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, 1);
		Vector accelerationDayMinus2 = FourAdamsBashfort.centredDiff(velocityDayMinus3, velocityDayMinus1, 1);
		Vector accelerationMinus1 = FourAdamsBashfort.centredDiff(velocityDayMinus2, velocityActualDay, 1);
		Vector accelerationActualDay = FourAdamsBashfort.backwardDiff(velocityActualDay, velocityDayMinus1, velocityDayMinus2, 1);

		Vector positionActualDay = new Vector(2.520721625280142E+09, 1.570265330931762E+09, -2.681128773651946E+07); // day 4 --- actual day

		p.setNextDataFirst(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, velocityActualDay, accelerationDayMinus3, accelerationDayMinus2, accelerationMinus1, accelerationActualDay, positionActualDay);
		return p;
	}

	public static Planet makeNeptune() {
		Planet p = new Planet("neptune", new Vector(4.344787259046247E+09,-1.083664330264994E+09,-7.782633401672775E+07),     new Vector(1.292292607032268E+00,5.304279525500773E+00,-1.390977388629209E-01).multiply(Planet.factor),    24624,    102.413E24,0,0);
		Vector velocityDayMinus3 = new Vector(1.293646901277863E+00, 5.303062647606767E+00, -1.395789056062844E-01).multiply(Planet.factor);      		 // on day 1
		Vector velocityDayMinus2 = new Vector(1.292605243239582E+00, 5.303566890454661E+00, -1.388565292048443E-01).multiply(Planet.factor);   		 // on day 2
		Vector velocityDayMinus1 = new Vector(1.292378533114978E+00, 5.304482887813889E+00, -1.384041102417533E-01).multiply(Planet.factor);        		// on day 3
		Vector velocityActualDay = new Vector(1.292632887654737E+00, 5.305024140488896E+00, -1.386814230827889E-01).multiply(Planet.factor);			//on day 4 -> today

		//set acceleration 	-> use 3-points formula
		Vector accelerationDayMinus3 = FourAdamsBashfort.forwardDiff(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, 1);
		Vector accelerationDayMinus2 = FourAdamsBashfort.centredDiff(velocityDayMinus3, velocityDayMinus1, 1);
		Vector accelerationMinus1 = FourAdamsBashfort.centredDiff(velocityDayMinus2, velocityActualDay, 1);
		Vector accelerationActualDay = FourAdamsBashfort.backwardDiff(velocityActualDay, velocityDayMinus1, velocityDayMinus2, 1);

		Vector positionActualDay = new Vector(4.344787551365745E+09, -1.083664718815018E+09, -7.782629925658041E+07); // day 4 --- actual day

		p.setNextDataFirst(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, velocityActualDay, accelerationDayMinus3, accelerationDayMinus2, accelerationMinus1, accelerationActualDay, positionActualDay);
		return p;
	}

	public static Planet makeMoon() {
		Planet p = new Planet("moon", new Vector(-1.493626859901140E+08,-2.212378435248749E+06,3.162933122716530E+04),        new Vector(1.540496550112790E-01,-3.094661877857872E+01,2.193857468353855E-02).multiply(Planet.factor),     1737.4,   7.349E22,0,0);
		if(!mix) {
			Vector velocityDayMinus3 = new Vector(-2.152328722476906E+00, -3.080930515123942E+01, 7.945582060016498E-02).multiply(Planet.factor);             // on day 1

			Vector velocityDayMinus2 = new Vector(-1.395215285597100E+00, -3.093177379507073E+01, 6.372975559516725E-02).multiply(Planet.factor);         // on day 2
			Vector velocityDayMinus1 = new Vector(-6.195854110189489E-01, -3.097814794574678E+01, 4.402078433207102E-02).multiply(Planet.factor);                // on day 3
			Vector velocityActualDay = new Vector(1.540496550112790E-01, -3.094661877857872E+01, 2.193857468353855E-02).multiply(Planet.factor);            //on day 4 -> today

			//set acceleration 	-> use 3-points formula
			Vector accelerationDayMinus3 = FourAdamsBashfort.forwardDiff(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, 1);
			Vector accelerationDayMinus2 = FourAdamsBashfort.centredDiff(velocityDayMinus3, velocityDayMinus1, 1);
			Vector accelerationMinus1 = FourAdamsBashfort.centredDiff(velocityDayMinus2, velocityActualDay, 1);
			Vector accelerationActualDay = FourAdamsBashfort.backwardDiff(velocityActualDay, velocityDayMinus1, velocityDayMinus2, 1);

			Vector positionActualDay = new Vector(-1.493626859901140E+08, -2.212378435248749E+06, 3.162933122716530E+04); // day 4 --- actual day

			p.setNextDataFirst(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, velocityActualDay, accelerationDayMinus3, accelerationDayMinus2, accelerationMinus1, accelerationActualDay, positionActualDay);
		}
		return p;
	}

	public static Planet makeTitan() {
		Planet p = new Planet("titan", new Vector(3.537424927743304E+08,-1.462539028125231E+09,1.169787519537956E+07),        new Vector(1.208193089270527E+01,-1.813839579262785E+00,1.381017323560965E+00).multiply(Planet.factor),     2575.5 + 8192,   13455.3E19, 600, 1.5);
		if(!mix) {
			Vector velocityDayMinus3 = new Vector(6.178641376271634E+00, -1.833777023610783E+00, 1.971184760009602E+00).multiply(Planet.factor);             // on day 1
			Vector velocityDayMinus2 = new Vector(8.122765730000186E+00, -2.500412938410761E+00, 2.123832640709379E+00).multiply(Planet.factor);         // on day 2
			Vector velocityDayMinus1 = new Vector(1.018081864020847E+01, -2.498253448725608E+00, 1.920443203939330E+00).multiply(Planet.factor);                // on day 3
			Vector velocityActualDay = new Vector(1.208193089270527E+01, -1.813839579262785E+00, 1.381017323560965E+00).multiply(Planet.factor);            //on day 4 -> today

			//set acceleration 	-> use 3-points formula
			Vector accelerationDayMinus3 = FourAdamsBashfort.forwardDiff(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, 1);
			Vector accelerationDayMinus2 = FourAdamsBashfort.centredDiff(velocityDayMinus3, velocityDayMinus1, 1);
			Vector accelerationMinus1 = FourAdamsBashfort.centredDiff(velocityDayMinus2, velocityActualDay, 1);
			Vector accelerationActualDay = FourAdamsBashfort.backwardDiff(velocityActualDay, velocityDayMinus1, velocityDayMinus2, 1);

			Vector positionActualDay = new Vector(3.537424927743304E+08, -1.462539028125231E+09, 1.169787519537956E+07); // day 4 --- actual day

			p.setNextDataFirst(velocityDayMinus3, velocityDayMinus2, velocityDayMinus1, velocityActualDay, accelerationDayMinus3, accelerationDayMinus2, accelerationMinus1, accelerationActualDay, positionActualDay);
		}
		return p;
	}

	public static Planet[] make() {
		return new Planet[]{Planet.makeSun(), Planet.makeMercury(), Planet.makeVenus(), Planet.makeEarth(), Planet.makeMars(), Planet.makeJupiter(), Planet.makeSaturn(), Planet.makeUranus(), Planet.makeNeptune(), Planet.makeMoon(), Planet.makeTitan()};
	}
}
