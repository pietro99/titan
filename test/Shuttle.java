public class Shuttle extends Body{
    private static final double epsilon = 1E-10;
    private Vector[] direction; //object coordinate system
    private Vector angularSpeed;

    private double innerRadius;    //shuttle as a sphere shell, radius in body class

    public Vector init; //initial velocity

    private double inertia; //moment of inertia / mass

    private double mainEngineForce;   //acceleration / time of the engines
    private double lateralEngineForce;
    private double mainEngineMass;  //mass / time consumed by the engines
    private double lateralEngineMass;

    private double minMass;

    private double parachute;

    public Shuttle(Vector velocity, double mass, double minMass){
      /*  //set initial acceleration
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
        this.mainEngineForce = 0;
        this.mainEngineMass = 0;
        this.lateralEngineForce = 0;
        this.lateralEngineMass = 0;
        this.radius = 0;
        this.innerRadius = 0;

        this.minMass = minMass;

        this.parachute = .2;*/
        this(velocity, mass, mass, 0, 0, 0, 0, 0, 0, SolarSystem.getPlanets()[3]);
    }

    public Shuttle(Vector velocity, double mass, double minMass, double innerRadius, double radius, double mainEngineForce, double mainEngineMass, double lateralEngineForce, double lateralEngineMass, Body starting) {
        //set initial acceleration
        this.acceleration = new Vector(0, 0, 0);

        //initialiaze coordinate system
        this.direction = new Vector[3];
        this.direction[2] = velocity.normalize();   //main engine and lateral
        if(this.direction[2].isNaN()) {
            this.direction[2] = new Vector(0, 0, 1);
        }
        this.direction[0] = this.direction[2].cross(new Vector(0, 0, 1)).normalize();   //lateral engine
        if(this.direction[0].isNaN()) {
            this.direction[0] = new Vector(1, 0, 0);
        }
        this.direction[1] = this.direction[0].cross(this.direction[2]);                 //lateral engine
        if(this.direction[1].isNaN()) {
            this.direction[1] = new Vector(0, 1, 0);
        }
        //set physical data
        this.velocity = velocity;
        this.mass = mass;
        this.angularSpeed = new Vector(0, 0,0);
        this.init = velocity;

        this.minMass = minMass;

        this.parachute = .2;

        this.position = starting.getPosition().sum(velocity.normalize().multiply(starting.getRadius()));

        this.innerRadius = innerRadius;
        this.radius = radius;
        this.inertia = 0.4 * (Math.pow(this.radius, 5) - Math.pow(this.innerRadius, 5)) / (Math.pow(this.radius, 3) - Math.pow(this.innerRadius, 3)); //mass simplified

        this.mainEngineMass = mainEngineMass;
        this.mainEngineForce = mainEngineForce;
        this.lateralEngineMass = lateralEngineMass;
        this.lateralEngineForce = lateralEngineForce;
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
        if(-deltaMass < mass && mass > minMass) {
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
        if(angularSpeed.squareLength() > epsilon) {
            for(int i = 0; i < 3; i++) {
                direction[i] = direction[i].rotate(angularSpeed, angularSpeed.length() * deltaT);    //rotate directions -> rotate engines   //TODO integral
            }
        }
    }

    public Vector getDirection(int axis) {
        return direction[axis];
    }

    public void mainEngine(double time) {
        addAcceleration(direction[2].multiply(mainEngineForce * time / mass), Vector.ZERO, -mainEngineMass * time);
    }

    public void lateralEngine(double time, boolean positive, int axisMove, int axisRot, double error) {
        int sign = 1;
        if(!positive)
            sign = -1;
        addAcceleration(direction[axisMove].multiply(lateralEngineForce * time * sign * (1 + Math.random() * error / mass)),
                direction[axisRot].multiply(radius * (1 + error * Math.random())),
                -lateralEngineMass * time);
    }

    public void lateralEngine(double time, boolean positive, int axisMove, int axisRot) {
        lateralEngine(time, positive, axisMove, axisRot, 0);
    }

    public void applyForce(Vector force, Vector position) {
        addAcceleration(force.multiply(1 / mass), position, 0);
    }

    public Vector getInitialVelocity() {
        return init;
    }

    public double getDragArea() {
        //half of the surface
        return 2 * Math.PI * radius * radius;
    }

    public void useParachute() {
        double atmosphere = SolarSystem.planets[10].getAtmosphericPressureComparedToEarthPressure();
        acceleration = acceleration.sum(velocity.multiply(-parachute * atmosphere));
    }

    public void stopRotation(double tolerance, double timeStep) {   //timeStep: step of the simulation, lateral engines can work with smaller time step
        //TODO tolerance? lateralEngine bug
        Vector time = angularSpeed.multiply(mass / lateralEngineForce);
        for(int i = 0; i < 3; i++){
            if(angularSpeed.get(i) > tolerance)
                lateralEngine(time.get(i), angularSpeed.get(i) < 0, i == 2 ? 1 : 2, i, 0);
        }
    }

    public void alignTo(Vector axis, boolean sameDirection, double timeStep, double tolerance) {
        //TODO direction

        //assume it is not rotating
        //for velocity, planet-shuttle, ...

        //select the engine that approximate best the alignment (max dot product), repeat until it is done (rotAxis < tolerance)

        //Vector rotAxis = direction[2].cross(axis);
        double totTime = 0;

        /*while(totTime < timeStep && rotAxis.squareLength() > tolerance * tolerance) {
            int ax = -1;
            double dot = -Double.MAX_VALUE;
            double tmp = 0;

            //select the axis with the maximum dot product with the axis -> best approximation of the rotation using only a single engine
            for(int i = 0; i < direction.length; i++) {
                tmp = direction[i].dot(rotAxis);
                if(tmp > dot) {
                    dot = tmp;
                    ax = i;
                }
            }

            //constant acceleration
            //a = v / t, angle = arccos((u.dot(v) / v)) = 0.5 a * t^2   -> t = sqrt(angle / 0.5a)
            //half angle for the acceleration phase, half angle for the deceleration part
            //dot < 0 : counter-clock rotation
            double angle = Math.acos(dot / Math.sqrt(direction[ax].squareLength() * rotAxis.length()));
            if(dot > 0)
                angle *= -1;

            double time = Math.sqrt(angle / (0.5 * lateralEngineForce / mass));
            totTime += time;

            //perform rotation
            System.out.println("Axis: " + ax + ", Angle: " + angle * 180 / Math.PI);
            for(int i = 0; i < direction.length; i++) {
                if(i != ax) {
                    direction[i] = direction[i].rotate(direction[ax], 2*angle);
                }
            }
            //accelarate
            addAcceleration(direction[ax == 2 ? 1 : 2].multiply(lateralEngineForce * time / (2 * mass)), Vector.ZERO, lateralEngineMass * time);
            addAcceleration(direction[ax == 2 ? 1 : 2].multiply(lateralEngineForce * time / (-2 * mass)), Vector.ZERO, lateralEngineMass * time);

            //new rotation axis
            rotAxis = direction[ax].cross(axis).normalize();
        }*/

        //local x-y plane
        Vector horizontal = axis.subtract(axis.project(direction[2]));
        double dot = direction[1].dot(horizontal) / (Math.sqrt(direction[1].squareLength() * horizontal.squareLength()));
        double angle = Math.acos(dot);
        //a = v / t, angle = arccos((u.dot(v) / v)) = 0.5 a * t^2   -> t = sqrt(angle / 0.5a)
        //half angle for the acceleration phase, half angle for the deceleration part
        //dot < 0 : counter-clock rotation
        double time = Math.sqrt(2 * angle * mass / lateralEngineForce);
        if(dot < 0) //TODO check sign
            angle *= -1;

        direction[0] = direction[0].rotate(direction[2], angle);
        direction[1] = direction[1].rotate(direction[2], angle);
        //x2 rotation, acceleration and deceleration
        addAcceleration(direction[1].multiply(lateralEngineForce * time / mass), Vector.ZERO, lateralEngineMass * time / 2);
        addAcceleration(direction[1].multiply(-lateralEngineForce * time /  mass), Vector.ZERO, lateralEngineMass * time / 2);


        dot = direction[2].dot(axis) / (Math.sqrt(direction[2].squareLength() * axis.squareLength()));
        angle = Math.acos(dot);
        time = Math.sqrt(2 * angle * mass / lateralEngineForce);
        if(dot < 0) //TODO check sign
            angle *= -1;
        direction[2] = direction[2].rotate(direction[0], angle);
        direction[1] = direction[1].rotate(direction[0], angle);
        addAcceleration(direction[2].multiply(lateralEngineForce * time / (2 * mass)), Vector.ZERO, lateralEngineMass * time / 2);
        addAcceleration(direction[2].multiply(-lateralEngineForce * time / (2 * mass)), Vector.ZERO, lateralEngineMass * time / 2);
        addAcceleration(direction[0].multiply(lateralEngineForce * time / (2 * mass)), Vector.ZERO, lateralEngineMass * time / 2);
        addAcceleration(direction[0].multiply(lateralEngineForce * time / (2 * mass)), Vector.ZERO, lateralEngineMass * time / 2);
    }

    public void brake(double targetVelocity, double tolerance, double timeStep) {
        //TODO time < time step
        //assumet that it has the right direction
        double vel = velocity.length();
        double time = 0;
        if(Math.abs(vel - targetVelocity) > tolerance) {
            time = Math.abs(Math.max((vel - targetVelocity) * mass / mainEngineForce, timeStep));
            mainEngine(time);
        }
    }

    public void land(Planet planet, double timeStep) {
        Vector dist = position.subtract(planet.getPosition());
        double sDist = dist.squareLength();
        if(sDist > (planet.getDistanceAtmosphere() + planet.getRadius())) {
            stopRotation(0.1, timeStep);
            alignTo(velocity, false, timeStep, 0.1);
            brake(2555, 50, timeStep);  //less than escape velocity
        }else if(sDist > 50){
            stopRotation(0.1, timeStep);
            alignTo(velocity.subtract(dist), false, timeStep, 0.1);
            //TODO  wind parachute, drag?
            useParachute();
            brake(200, 30, timeStep);
        }else {
            stopRotation(0.1, timeStep);
            alignTo(dist, true, timeStep, 10);
            //TODO  wind parachute, drag?
            useParachute();
            brake(0.1, 0.1, timeStep);
        }
    }

    public static void main(String[] args){
        if(args[0].equals("align")) {
            Planet p = new Planet("p", Vector.ZERO, Vector.ZERO, 0, 0);
            Shuttle s = new Shuttle(new Vector(0, 0, 0), 20000, 500, 10, 3, 100, -100, 250, -10, p);
            Vector axis = new Vector(1, 1, 0);
            System.out.println("Z: " + s.getDirection(2) + ", Dot: " + s.getDirection(2).dot(axis) / (s.getDirection(2).length() * axis.length()));
            System.out.println("Acc: " + s.getAcceleration() + ", Mass: " + s.getMass());
            for(int i = 0; i < 10; i++) {
                s.update(100);
                s.alignTo(axis, true, 100, .1);
                System.out.println("Z: " + s.getDirection(2) + ", Dot: " + s.getDirection(2).dot(axis) / (s.getDirection(2).length() * axis.length()));
                System.out.println("Acc: " + s.getAcceleration() + ", Mass: " + s.getMass());
            }
        }

        if(args[0].equals("brake")) {
            Planet p = new Planet("p", Vector.ZERO, Vector.ZERO, 0, 0);
            Shuttle s = new Shuttle(new Vector(0, 0, 10000), 2000, 500, 10, 3, 100, 100, 80, 80, p);
            for (int i = 0; i < 50; i++) {
                s.update(100);
                s.brake(0, 50, 100);
                System.out.println(s.getVelocity());
            }
        }
    }
}
