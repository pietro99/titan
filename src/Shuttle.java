public class Shuttle extends Body{
    private static final double epsilon = 1E-10;
    private Vector[] direction; //object coordiante system
    private Vector angularSpeed;

    private double innerRadius;    //shuttle as a sphere shell, radius in body class

    public Vector init; //initial velocity

    private double inertia; //moment of inertia / mass

    private double mainEngineAcc;   //acceleration / time of the engines
    private double lateralEngineAcc;
    private double mainEngineMass;  //mass / time consumed by the engines
    private double lateralEngineMass;

    private double minMass;

    private double parachute;

    public Shuttle(Vector velocity, double mass, double minMass){
        //set initial acceleration
        this.acceleration = new Vector(0, 0, 0);

        //set initial position on the surface of the planet     //TODO use different planets
        this.position = SolarSystem.getPlanets()[3].getPosition().sum(velocity.normalize().multiply(SolarSystem.getPlanets()[3].getRadius()));

        //initialiaze coordinate system
        this.direction = new Vector[3];
        this.direction[2] = velocity.normalize();   //main engine and lateral
        this.direction[0] = this.direction[2].cross(new Vector(0, 0, 1)).normalize();   //lateral engine
        this.direction[1] = this.direction[0].cross(this.direction[2]);                 //lateral engine

        //set physical data
        this.velocity = velocity;
        this.mass = mass;
        this.angularSpeed = new Vector(0, 0,0);
        this.init = velocity;

        this.inertia = 0;
        this.mainEngineAcc = 0;
        this.mainEngineMass = 0;
        this.lateralEngineAcc = 0;
        this.lateralEngineMass = 0;
        this.radius = 0;
        this.innerRadius = 0;

        this.minMass = minMass;

        this.parachute = .2;
    }

    /*
    public Shuttle(Vector position, Vector velocity, double mass) {
        this(velocity, mass);
    }*/

    public Shuttle(Vector velocity, double mass, double minMass, double innerRadius, double radius, double mainEngineAcc, double mainEngineMass, double lateralEngineAcc, double lateralEngineMass, Body starting) {
        this(velocity, mass, minMass);
        this.position = starting.getPosition().sum(velocity.normalize().multiply(starting.getRadius()));

        this.innerRadius = innerRadius;
        this.radius = radius;
        this.inertia = 0.4 * (Math.pow(this.radius, 5) - Math.pow(this.innerRadius, 5)) / (Math.pow(this.radius, 3) - Math.pow(this.innerRadius, 3)); //mass simplified

        this.mainEngineMass = mainEngineMass;
        this.mainEngineAcc = mainEngineAcc;
        this.lateralEngineMass = lateralEngineMass;
        this.lateralEngineAcc = lateralEngineAcc;
    }

    public void calculateGravity(Planet[] planets) {
        acceleration.set(0, 0, 0);
        if(mass != 0)
            for(int i = 0; i < planets.length; i++) {
                Vector distance = planets[i].getPosition().subtract(this.getPosition());
                double d = distance.squareLength();
                distance = distance.normalize();
                acceleration = acceleration.sum(distance.multiply(Physics.G * planets[i].getMass() / d));
            }
    }

    public void addAcceleration(Vector acc, Vector radius, double deltaMass) {
        if(deltaMass < mass) {
            //add acceleration
            acceleration = acceleration.sum(acc);

            //update mass
            mass += deltaMass;
            if (radius.squareLength() < epsilon) {
                angularSpeed = angularSpeed.sum(radius.cross(acc).multiply(1 / inertia));  //TODO integral
            }
        }
    }

    public void update(double deltaT) {
        super.update(deltaT);

        //rotation
        if(angularSpeed.squareLength() < epsilon) {
            for(int i = 0; i < 3; i++) {
                direction[i] = direction[i].rotate(angularSpeed, angularSpeed.length() * deltaT);    //rotate directions -> rotate engines   //TODO integral
            }
        }
    }

    public Vector getDirection(int axis) {
        return direction[axis];
    }

    public void mainEngine(double time) {
        addAcceleration(direction[2].multiply(mainEngineAcc * time / mass), Vector.ZERO, -mainEngineMass * time);
    }

    public void lateralEngine(double time, boolean positive, int axisMove, int axisRot, double error) {
        int sign = 1;
        if(!positive)
            sign = -1;
        addAcceleration(direction[axisMove].multiply(lateralEngineAcc * time * sign * (1 + Math.random() * error / mass)),
                        direction[axisRot].multiply(radius * (1 + error * Math.random())),
                        -lateralEngineMass * time);
    }

    public void lateralEngine(double time, boolean positive, int axisMove, int axisRot) {
        lateralEngine(time, positive, axisMove, axisRot, 0);
    }

    public void applyForce(Vector force, Vector position) {
        addAcceleration(force.multiply(1 / mass), position, 0);
    }

    public Vector getInitialVelocity() { return init;}

    public double getDragArea() {
        //half of the surface
        return 2 * Math.PI * radius * radius;
    }

    public void useParachute() {
        double atmosphere = SolarSystem.planets[10].getAtmosphericPressureComparedToEarthPressure();
        acceleration = acceleration.sum(velocity.multiply(-parachute * atmosphere));
    }

    public void stopRotation(double tolerance, double timeStep) {   //timeStep: step of the simulation, lateral engines can work with smaller time step
        //TODO
    }

    public void alignTo(Vector axis, boolean sameDirection) {
        //for velocity, planet-shuttle,
        //TODO
    }

    public void brake(double targetVelocity, double tolerance, double timeStep) {
        //TODO
    }

    public void land(Planet planet, double timeStep) {
        Vector dist = position.subtract(planet.getPosition());
        double sDist = dist.squareLength();
        if(sDist > (planet.getDistanceAtmosphere() + planet.getRadius())) {
            stopRotation(0.1, timeStep);
            alignTo(velocity, false);
            brake(2555, 50, timeStep);  //less than escape velocity
        }else if(sDist > 50){
            stopRotation(0.1, timeStep);
            alignTo(velocity.subtract(dist), false);
            //TODO  wind parachute, drag?
            useParachute();
            brake(200, 30, timeStep);
        }else {
            stopRotation(0.1, timeStep);
            alignTo(dist, true);
            //TODO  wind parachute, drag?
            useParachute();
            brake(0.1, 0.1, timeStep);
        }
    }

}
