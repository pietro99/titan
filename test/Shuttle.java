public class Shuttle extends Body{
    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;
    public static final int Z_AXIS = 2;

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

    private double timeStep = 1;

    private boolean landing;

    /* ******* Constructor ******** */
    public Shuttle(Vector velocity, double mass){
        this(velocity, mass, mass, 0, 0, 0, 0, 0, 0, 0, SolarSystem.getPlanets()[3]);
    }

    public Shuttle(Vector velocity, double mass, double minMass, double innerRadius, double radius, double mainEngineForce, double mainEngineMass, double lateralEngineForce, double lateralEngineMass, double parachute, Body starting) {
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

        this.parachute = parachute;

        this.position = starting.getPosition().sum(velocity.normalize().multiply(starting.getRadius()));

        this.innerRadius = innerRadius;
        this.radius = radius;
        this.inertia = calculateInertia();

        this.mainEngineMass = mainEngineMass;   //per timeStep
        this.mainEngineForce = mainEngineForce;
        this.lateralEngineMass = lateralEngineMass;
        this.lateralEngineForce = lateralEngineForce;

        this.setNextDataFirst(this.init, this.init, this.init, this.init, Vector.ZERO, this.init.multiply(1 / 3), this.init.multiply(2 / 3),this.init, this.position);
    }

    public static Shuttle getStandardShuttle() {
        //return new Shuttle(new Vector(192417.8004932324, -925027.0853926808, -558.466505544255).multiply(0.999999 * 0 + 1.01), 20000, 500, 5, 20, 1000, -80, 500e4, -50, 2000, SolarSystem.planets[3]);
        return getStandardShuttle(new Vector(192417.8004932324, -925027.0853926808, -558.466505544255).multiply(0.999999 * 0 + 1.01));
    }
    public static Shuttle getStandardShuttle(Vector vel) {
        return new Shuttle(vel, 20000, 500, 5, 20, 1000, -80, 500e4, -50, 2000, SolarSystem.planets[3]);
    }
    /* ************************** */

    /* ********* Setters ******** */
    public void setTimeStep(double t) {
        if(t >= 0 && t != timeStep) {
            mainEngineForce *= timeStep / t;
            lateralEngineForce *= timeStep / t;
            timeStep = t;
        }
    }
    public void setMainEngineForce(double f) {
        if(f >= 0)
            mainEngineForce = f;
    }
    public void setMainEngineMass(double m) {
        if(m >= 0)
            mainEngineMass = m;
    }
    public void setLateralEngineForce(double f) {
        if(f >= 0)
            lateralEngineForce = f;
    }
    public void setLateralEngineMass(double m) {
        if(m >= 0)
            lateralEngineMass = m;
    }
    public void setRadius(double r) {
        if(r >= 0 && r >= innerRadius) {
            radius = r;
            inertia = calculateInertia();
        }
    }
    public void setInnerRadius(double r) {
        if(r >= 0 && r <= radius) {
            innerRadius = r;
            inertia = calculateInertia();
        }
    }
    public void setParachute(double p ){
        if(p >= 0)
            parachute = p;
    }

    private double calculateInertia() {
        return 0.4 * (Math.pow(this.radius, 5) - Math.pow(this.innerRadius, 5)) / (Math.pow(this.radius, 3) - Math.pow(this.innerRadius, 3)); //mass simplified
    }

    /* *************************** */

    /* ******* Getters *********** */
    public Vector getDirection(int axis) {
        return direction[axis];
    }
    public Vector getInitialVelocity() { return init;}
    public double getDragArea() {
        //half of the surface
        return 2 * Math.PI * radius * radius;
    }

    public Vector getAngularSpeed() { return angularSpeed; }
    public double getInnerRadius() { return innerRadius; }
    public double getParachute() { return parachute; }
    public double getMinMass() { return minMass; }
    public double getTimeStep() { return timeStep; }
    public double getMainEngineForce() { return  mainEngineForce; }
    public double getMainEngineMass() { return mainEngineMass; }
    public double getLateralEngineForce() { return lateralEngineForce; }
    public double getLateralEngineMass() { return lateralEngineMass; }

    public boolean isLanding() { return landing; }

    public double getAngle2D(Vector z) {
        Vector v = direction[2].normalize();
        Vector u = z.normalize();

        double dot = u.dot(v);      //-> cos angle
        double cross = u.cross(v).length();  //-> sin angle
        return Math.acos(dot) * (cross >= 0 ? +1 : -1);
    }
    /* *************************** */

    /* ****** SolarSyste methods ******* */


    public void addAcceleration(Vector acc, Vector radius, double deltaMass) {
        if((mass > -deltaMass && mass > minMass) || deltaMass == 0) {
            //add acceleration
            acceleration = acceleration.sum(acc);

            //update mass
            mass += deltaMass; //TODO
            if (radius.squareLength() > epsilon) {
                angularSpeed = angularSpeed.sum(radius.cross(acc).multiply(timeStep / inertia));  //TODO integral
            }
        }else {
            System.out.println("No fuel: require " + deltaMass + ", get " + mass);
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

    /* ******************************** */

    /* ******* Engines ************* */
    public void mainEngine(double timeStepRatio) {
        addAcceleration(direction[2].normalize().multiply(mainEngineForce * timeStepRatio / mass), Vector.ZERO, mainEngineMass * timeStepRatio);
        //System.out.println("Delta acc: " + direction[2].multiply(mainEngineForce * timeStepRatio / mass));
        //System.out.println(acceleration);
    }

    public void lateralEngine(double timeStepRatio, boolean positive, int axisMove, int axisRot, double error) {
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

    public void useParachute(Planet p) {
        // TODO make planet changeable
        double atmosphere = SolarSystem.planets[10].getAtmosphericPressureComparedToEarthPressure();
        //System.out.println("Parachute acc: " + velocity.multiply(-parachute * atmosphere));
        //System.out.println("Acc: " + acceleration);

        acceleration = acceleration.sum(velocity.subtract(p.getVelocity()).multiply(-parachute * atmosphere));
    }

    /* ***************************** */

    /* ********* Landing ********* */

    public void stopRotation(double tolerance, double timeStep) {   //timeStep: step of the simulation, lateral engines can work with smaller time step
        Vector time = new Vector(0, 0, 0);
        //from angular speed formula
        time = angularSpeed.abs().multiply(inertia * mass / (radius * lateralEngineForce));
        for(int i = 0; i < 3; i++) {
            if(angularSpeed.get(i) > tolerance) {
                if(time.get(i) > timeStep) {
                    //use lateral engine
                    lateralEngine(time.get(i), angularSpeed.get(i) < 0, i == 2 ? 1 : 2, i, 0);
                }else {
                    //consume mass
                    mass += lateralEngineMass * time.get(i);

                    //angle
                    double angle = angularSpeed.get(i) * time.get(i) * 0.5;    //assume constant change of speed
                    //update rotation
                    for(int j = 0; j < 3; j++) {
                        if(j != i)
                            direction[j] = direction[j].rotate(direction[i], angle);
                    }

                    //add acceleration effect   -> select the right axis
                    velocity = velocity.sum(direction[i == 2 ? 1: 2].multiply(lateralEngineForce / mass * time.get(i)));
                    angularSpeed.set(i, 0);
                }
            }
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

            double time = Math.sqrt(2 * angle * mass / lateralEngineForce) / timeStep;
            //rotate around direction[2], align direction[1] to horizontal projection
            if (direction[1].cross(horizontal).dot(direction[2]) < 0)
                angle *= -1;

            direction[0] = direction[0].rotate(direction[2], angle);
            direction[1] = direction[1].rotate(direction[2], angle);
            //x2 rotation, acceleration and deceleration
            //acc = force * time * % rotation perfomed * accuracy factor                                 radius          mass
            addAcceleration(direction[1].multiply(lateralEngineForce * time * (1 + (Math.random() - .5) * accuracy) / mass), Vector.ZERO, lateralEngineMass * time / 2);
            addAcceleration(direction[1].multiply(-lateralEngineForce * time * (1 + (Math.random() - .5) * accuracy) / mass), Vector.ZERO, lateralEngineMass * time / 2);

            //rotate around direction[0], align direction[2] to axis
            dot = direction[2].dot(axis) / (Math.sqrt(direction[2].squareLength() * axis.squareLength()));
            angle = Math.acos(dot);
            time = Math.sqrt(2 * angle * mass / lateralEngineForce);

            if (direction[2].cross(axis).dot(direction[0]) < 0)
                angle *= -1;
            direction[2] = direction[2].rotate(direction[0], angle);
            direction[1] = direction[1].rotate(direction[0], angle);
            addAcceleration(direction[2].multiply(lateralEngineForce * time * (1 + (Math.random() - .5) * accuracy) / (2 * mass)), Vector.ZERO, lateralEngineMass * time / 2);
            addAcceleration(direction[2].multiply(-lateralEngineForce * time * (1 + (Math.random() - .5) * accuracy) / (2 * mass)), Vector.ZERO, lateralEngineMass * time / 2);
            addAcceleration(direction[0].multiply(lateralEngineForce * time * (1 + (Math.random() - .5) * accuracy) / (2 * mass)), Vector.ZERO, lateralEngineMass * time / 2);
            addAcceleration(direction[0].multiply(-lateralEngineForce * time * (1 + (Math.random() - .5) * accuracy) / (2 * mass)), Vector.ZERO, lateralEngineMass * time / 2);
        }else if(Math.abs(dot + 1) < Physics.EPS) {
            double time = Math.sqrt(2 * Math.PI * mass / lateralEngineForce);
            double complete =/* TODO Math.min(1, timeStep / time) */ 1;
            direction[2] = direction[2].rotate(direction[0], Math.PI * complete);
            direction[1] = direction[1].rotate(direction[0], Math.PI * complete);
            addAcceleration(direction[2].multiply(lateralEngineForce * time * (1 + (Math.random() - .5) * accuracy) / mass), Vector.ZERO, lateralEngineMass * time / 2);
            addAcceleration(direction[2].multiply(-lateralEngineForce * time * (1 + (Math.random() - .5) * accuracy) / mass), Vector.ZERO, lateralEngineMass * time / 2);
        }
    }

    public void brake(double targetVelocity, double tolerance, double timeStep) {
        //assumet that it has the right direction
        double vel = velocity.length();
        double time = 0;
        //System.out.println("Vel: " + velocity + " V: " + vel);
        if(Math.abs(vel - targetVelocity) > tolerance) {
            //if (direction[2].dot(velocity) < 0) {    //make sure that it is the right direction
            //TODO mistake in time calculation -> over stimate
            time = Math.abs((vel - targetVelocity) * mass / mainEngineForce);

            if (time > timeStep) {
                //brake during all the timeStep
                System.out.println("Main engine");
                mainEngine(1);
            } else {
                System.out.println("Almost the right speed: " + vel);
                System.out.println("Distance: " + position.distance(SolarSystem.planets[10].getPosition()));
                System.out.println("Titan: " + SolarSystem.planets[10].getVelocity().length());
                //less than one time step
                //assume a constant acceleration
                Vector acc = direction[2].multiply(mainEngineForce / mass);

                //update mass
                mass += mainEngineMass * time;

                //change of position during the brake
                position = position.sum(velocity.multiply(time).sum(acc.multiply(0.5 * time * time)));

                //update velocity
                velocity = velocity.sum(acc.multiply(time));
            }
           /* }else {
                System.out.println("Wrong direction");
                velocity = velocity.normalize().multiply(targetVelocity);
            }*/
        }
    }
/*
    public void brake(double targetVelocity, double tolerance, double timeStep, Body ref) {
        if(ref == null)
            brake(targetVelocity, tolerance, timeStep);
        else {
            Vector delta = velocity.subtract(ref.getVelocity().multiply(targetVelocity / ref.getVelocity().length()));  //chnge of velocity needed
            //Local z-coomponent    -> main engine can handle it
            Vector z = delta.project(direction[2]);

            //residual
            Vector err = delta.subtract(z);
            brake(z.length(), tolerance, timeStep);
        }

    }*/

    public void land(Planet planet, double timeStep) {
        Vector dist = position.subtract(planet.getPosition());
        double d = dist.length();

        if(d < (planet.getDistanceAtmosphere() + planet.getRadius()) + 1e4) {       //start landing
            landing = true;
            SolarSystem.setTimeStep(0.0001);
            //SolarSystem.TIME /= .5e3;
            setTimeStep(SolarSystem.getTimeStep());
            stopRotation(0, timeStep);
            if(d > (planet.getDistanceAtmosphere() + planet.getRadius())) {         //in atmosphere
                alignTo(planet.getVelocity().normalize().subtract(dist.normalize()), true, timeStep, 0.1, 0);
                if(d < 4000)
                    System.out.println("Near: " + d);
                brake(planet.getVelocity().length(), 10, timeStep);  //less than escape velocity
                //brake(2000, 50, timeStep, planet);
            }else {
                alignTo(dist, true, timeStep, 0, 0);
                System.out.println("In atmosphere: " + (d - planet.getRadius()));
                double dot = dist.normalize().dot(getDirection(2).normalize());
                //roundoff error
                if(dot > 1)
                    dot = 1;
                else if (dot < -1)
                    dot = -1;
                System.out.println("Angle (deg): " + (180 / Math.PI) * Math.acos(dot) + " " + dot);
                double v = getVelocity().distance(planet.getVelocity());
                System.out.println("Speed: " + (v * 1e-4));
                useParachute(planet);   //-> we need this to land

                Vector[] p = Physics.wind(this, planet, 100, 100000, 10);
                acceleration = acceleration.sum(p[0]);
                angularSpeed = angularSpeed.sum(p[1]);

                Vector drag = Physics.dragAcceleration(planet, this);   //-> we need this to land
                acceleration = acceleration.subtract(drag);
                //brake(planet.getVelocity().length(), 0, timeStep);    //->TODO ???    if brake then there is always the same landing speed
                //brake(0, 0, timeStep);    //->TODO ???
                //brake(1e16, 0, timeStep);    //->TODO ???
            }
        }else {
            landing = false;
        }
    }

    /* ************************* */

    /* ***** Testing ******* */
    public void setAngularSpeed(Vector speed) {
        this.angularSpeed = speed;
    }

    public static void main(String[] args){
        if(args.length > 0) {
            if (args[0].equals("stop")) {
                Planet p = new Planet("p", Vector.ZERO, Vector.ZERO, 0, 0);
                Shuttle s = new Shuttle(new Vector(0, 0, 10), 20000, 2000, 5, 20, 1000 * 10000, -100, 250 * 10000, -25, 20, p);
                s.setAngularSpeed(new Vector(2, 3, 1));
                for(int i = 0; i < 5; i++) {
                    s.update(0.05);
                    System.out.println("Speed: " + s.getAngularSpeed());
                    s.stopRotation(0, 0.05);
                }
            }

            if (args[0].equals("align")) {
                Planet p = new Planet("p", Vector.ZERO, Vector.ZERO, 0, 0);
                Shuttle s = new Shuttle(new Vector(0, 0, 0), 20000, 500, 10, 3, 100 * 10000, -100, 250 * 10000, -10, 0.2, p);
                Vector axis = new Vector(0, 0, -1);
                System.out.println("Z: " + s.getDirection(2) + ", Dot: " + s.getDirection(2).dot(axis) / (s.getDirection(2).length() * axis.length()));
                System.out.println("Acc: " + s.getAcceleration() + ", Mass: " + s.getMass());
                for (int i = 0; i < 5; i++) {
                    s.update(0.05);
                    s.alignTo(axis, true, 0.05, 0, 0);
                    System.out.println("Z: " + s.getDirection(2) + ", Dot: " + s.getDirection(2).dot(axis) / (s.getDirection(2).length() * axis.length()));
                    System.out.println("Acc: " + s.getAcceleration() + ", Mass: " + s.getMass());
                }
            }

            if (args[0].equals("brake")) {
                Planet p = new Planet("p", Vector.ZERO, Vector.ZERO, 0, 0);
                Shuttle s = new Shuttle(new Vector(0, 0, 10000), 2000, 500, 5, 20, 1000e4, -100, 250e4, -10, 20, p);
                double timeStep = 0.05;
                //s.setTimeStep(1);
                s.setTimeStep(timeStep);
                s.alignTo(s.getInitialVelocity().multiply(-1), true, timeStep, 0, 0);

                System.out.println("Velocity: " + s.getVelocity() + " Acceleration: " + s.getAcceleration());
                for (int i = 0; i < 100; i++) {
                    System.out.println("Acc: " + s.getAcceleration());
                    s.update(timeStep);
                    s.setAcceleration(new Vector(0, 0, 0));
                    s.brake(0, 1, timeStep);
                }
                s.update(10);
                System.out.println("Mass: " + s.getMass());
                System.out.println("Velocity: " + s.getVelocity());
            }
        } else {
            final String[] commands = new String[]{"stop", "align", "brake"};
            System.out.println("Commands:");
            for(String s: commands) {
                System.out.println("\t" + s);
            }
        }
    }
}
