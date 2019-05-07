public class Shuttle extends Body{
    private static final double epsilon = 1E-10;
    private Vector[] direction; //object coordiante system
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

        this.mainEngineMass = mainEngineMass;   //per timeStep
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

    public void mainEngine(double timeStepRatio) {
        addAcceleration(direction[2].multiply(mainEngineForce * timeStepRatio / mass), Vector.ZERO, mainEngineMass * timeStepRatio);
    }

    public void lateralEngine(double timeStepRatio, boolean positive, int axisMove, int axisRot, double error) {
        //TODO F * t / m = v and not a
        int sign = 1;
        if(!positive)
            sign = -1;
        addAcceleration(direction[axisMove].multiply(lateralEngineForce * timeStepRatio * sign * (1 + Math.random() * error / mass)),
                direction[axisRot].multiply(radius * (1 + error * Math.random())),
                -lateralEngineMass * timeStepRatio);
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
        //TODO tolerance? lateralEngine bug
        Vector time = angularSpeed.multiply(mass / lateralEngineForce);
        for(int i = 0; i < 3; i++){
            if(angularSpeed.get(i) > tolerance)
                lateralEngine(time.get(i), angularSpeed.get(i) < 0, i == 2 ? 1 : 2, i, 0);
        }
    }

    public void alignTo(Vector axis, boolean sameDirection, double timeStep, double tolerance, double accuracy) {
        //TODO use timeStepRatio instead of complete
        if(!sameDirection)
            axis = axis.multiply(-1);
        double dot = direction[2].dot(axis) / Math.sqrt(direction[2].squareLength() * axis.squareLength());
        //if not aligned
        if((1 - dot) > tolerance && Math.abs(dot) != 1) {
            //local x-y plane
            Vector horizontal = axis.subtract(axis.project(direction[2]));
            dot = direction[1].dot(horizontal) / (Math.sqrt(direction[1].squareLength() * horizontal.squareLength()));
            double angle = Math.acos(dot);
            //a = v / t, angle = arccos((u.dot(v) / v)) = 0.5 a * t^2   -> t = sqrt(angle / 0.5a)
            //half angle for the acceleration phase, half angle for the deceleration part

            double time = Math.sqrt(2 * angle * mass / lateralEngineForce);
            double complete =/* TODO Math.min(1, timeStep / time) */ 1;
            //rotate around direction[2], align direction[1] to horizontal projection
            if (direction[1].cross(horizontal).dot(direction[2]) < 0)
                angle *= -1;

            direction[0] = direction[0].rotate(direction[2], angle * complete);
            direction[1] = direction[1].rotate(direction[2], angle * complete);
            //x2 rotation, acceleration and deceleration
                                            //acc = force * time * % rotation perfomed * accuracy factor                                 radius          mass
            addAcceleration(direction[1].multiply(lateralEngineForce * time * complete * (1 + (Math.random() - .5) * accuracy) / mass), Vector.ZERO, lateralEngineMass * time * complete/ 2);
            addAcceleration(direction[1].multiply(-lateralEngineForce * time * complete * (1 + (Math.random() - .5) * accuracy) / mass), Vector.ZERO, lateralEngineMass * time * complete/ 2);

            //rotate around direction[0], align direction[2] to axis
            dot = direction[2].dot(axis) / (Math.sqrt(direction[2].squareLength() * axis.squareLength()));
            angle = Math.acos(dot);
            time = Math.sqrt(2 * angle * mass / lateralEngineForce);
            complete = /* TODO Math.min(1, timeStep / time)*/ 1;
            if (direction[2].cross(axis).dot(direction[0]) < 0)
                angle *= -1;
            direction[2] = direction[2].rotate(direction[0], angle * complete);
            direction[1] = direction[1].rotate(direction[0], angle * complete);
            addAcceleration(direction[2].multiply(lateralEngineForce * time * complete * (1 + (Math.random() - .5) * accuracy) / (2 * mass)), Vector.ZERO, lateralEngineMass * time * complete / 2);
            addAcceleration(direction[2].multiply(-lateralEngineForce * time * complete * (1 + (Math.random() - .5) * accuracy) / (2 * mass)), Vector.ZERO, lateralEngineMass * time * complete / 2);
            addAcceleration(direction[0].multiply(lateralEngineForce * time * complete * (1 + (Math.random() - .5) * accuracy) / (2 * mass)), Vector.ZERO, lateralEngineMass * time * complete / 2);
            addAcceleration(direction[0].multiply(-lateralEngineForce * time * complete * (1 + (Math.random() - .5) * accuracy) / (2 * mass)), Vector.ZERO, lateralEngineMass * time * complete / 2);
        }else if(Math.abs(dot + 1) < Physics.EPS) {
            double time = Math.sqrt(2 * Math.PI * mass / lateralEngineForce);
            double complete =/* TODO Math.min(1, timeStep / time) */ 1;
            direction[2] = direction[2].rotate(direction[0], Math.PI * complete);
            direction[1] = direction[1].rotate(direction[0], Math.PI * complete);
            addAcceleration(direction[2].multiply(lateralEngineForce * time * complete * (1 + (Math.random() - .5) * accuracy) / mass), Vector.ZERO, lateralEngineMass * time * complete/ 2);
            addAcceleration(direction[2].multiply(-lateralEngineForce * time * complete * (1 + (Math.random() - .5) * accuracy) / mass), Vector.ZERO, lateralEngineMass * time * complete/ 2);
        }
    }

    public void brake(double targetVelocity, double tolerance, double timeStep) {
        //assumet that it has the right direction
        double vel = velocity.length();
        double time = 0;
        System.out.println("Vel: " + velocity + " V: " + vel);
        if(Math.abs(vel - targetVelocity) > tolerance) {
            if(direction[2].dot(velocity) > 0) {
                System.out.println("Wrong alignment");
            }else {
                //TODO mistake in time calculation -> over stimate
                time = Math.abs((vel - targetVelocity) * mass / mainEngineForce);
                System.out.println("Time: " + time + " Step: " + timeStep);
                if (time > timeStep) {
                    //brake during all the timeStep
                    mainEngine(1);
                } else {
                    //less than one time step
                    //assume a constant acceleration
                    Vector acc = direction[2].multiply(mainEngineForce / mass);
                    System.out.println("Delta: " + acc);
                    mass += mainEngineMass * time;
                    position = position.sum(velocity.multiply(time).sum(acc.multiply(0.5 * time * time)));
                    System.out.println(velocity + " > " + velocity.sum(acc.multiply(time)));
                    velocity = velocity.sum(acc.multiply(time));
                }
            }
        }else {
            System.out.println("No brake");
        }
    }

    public void land(Planet planet, double timeStep) {
        Vector dist = position.subtract(planet.getPosition());
        double sDist = dist.squareLength();
        if(sDist > (planet.getDistanceAtmosphere() + planet.getRadius())) {
            stopRotation(0.1, timeStep);
            alignTo(velocity, false, timeStep, 0.1, 0);
            brake(2555, 50, timeStep);  //less than escape velocity
        }else if(sDist > 50){
            stopRotation(0.1, timeStep);
            alignTo(velocity.subtract(dist), false, timeStep, 0.1, 0);
            //TODO  wind parachute, drag?
            useParachute();
            brake(200, 30, timeStep);
        }else {
            stopRotation(0.1, timeStep);
            alignTo(dist, true, timeStep, 10, 0);
            //TODO  wind parachute, drag?
            useParachute();
            brake(0.1, 0.1, timeStep);
        }
    }

    public static void main(String[] args){
        if(args[0].equals("align")) {
            Planet p = new Planet("p", Vector.ZERO, Vector.ZERO, 0, 0);
            Shuttle s = new Shuttle(new Vector(0, 0, 0), 20000, 500, 10, 3, 100 * 10000, -100, 250 * 10000, -10, p);
            Vector axis = new Vector(0, 0, -1);
            System.out.println("Z: " + s.getDirection(2) + ", Dot: " + s.getDirection(2).dot(axis) / (s.getDirection(2).length() * axis.length()));
            System.out.println("Acc: " + s.getAcceleration() + ", Mass: " + s.getMass());
            for(int i = 0; i < 5; i++) {
                s.update(0.05);
                s.alignTo(axis, true, 0.05, 0, 0);
                System.out.println("Z: " + s.getDirection(2) + ", Dot: " + s.getDirection(2).dot(axis) / (s.getDirection(2).length() * axis.length()));
                System.out.println("Acc: " + s.getAcceleration() + ", Mass: " + s.getMass());
            }
        }

        if(args[0].equals("brake")) {
            Planet p = new Planet("p", Vector.ZERO, Vector.ZERO, 0, 0);
            Shuttle s = new Shuttle(new Vector(0, 0, 10000), 2000, 500, 10, 3, 100*10000, -100, 80*10000, -10, p);
            s.alignTo(s.getInitialVelocity().multiply(-1), true, 100, 0, 0);
            System.out.println("Velocity: " + s.getVelocity() + " Acceleration: " + s.getAcceleration());
            for (int i = 0; i < 100; i++) {
                System.out.println("Acc: " + s.getAcceleration());
                s.update(10);
                s.brake(0, 50, 10);
            }
            s.update(10);
            System.out.println("Mass: " + s.getMass());
            System.out.println("Velocity: " + s.getVelocity());
        }
    }
}
