public class Shuttle extends Body{
    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;
    public static final int Z_AXIS = 2;

    public final double fuelCost = 0.5547615714017767;   //cost euro / kg
    //1.68 euro/gallon / (3.78541 l/gallion * 0.8 kg/l)
    //https://www.indexmundi.com/
    //https://www.indexmundi.com/commodities/?commodity=jet-fuel&months=12&currency=eur
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
    private double initMass;

    private double parachute;

    private double timeStep = 1;

    private boolean landing;
    public boolean allowLanding = true;
    private double costEst;

    private FourAdamsBashfort nextAngle;
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
        this.initMass = mass;
        this.angularSpeed = new Vector(0, 0,0);
        this.init = new Vector(velocity);

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
        //System.out.println("INIT: " + init);
        this.setNextDataFirst(this.init, this.init, this.init, this.init, Vector.ZERO, this.init.multiply(1 / 3), this.init.multiply(2 / 3),this.init, this.position);
    }

    public static Shuttle getStandardShuttle() {
        //return new Shuttle(new Vector(192417.8004932324, -925027.0853926808, -558.466505544255).multiply(0.999999 * 0 + 1.01), 20000, 500, 5, 20, 1000, -80, 500e4, -50, 2000, SolarSystem.planets[3]);
        //return getStandardShuttle(new Vector(192417.8004932324, -925027.0853926808, -558.466505544255));
        //return getStandardShuttle(new Vector(522329.18378110224, -1376412.6059415718, 8723.181875644595));
        //return getStandardShuttle(new Vector(443046.99525267974, -1306664.8064509863, 8872.41117955623).multiply(0.8));
        //return getStandardShuttle(new Vector(1044657.1895703765, -2752825.624261847, 17446.512633296163));
        //return getStandardShuttle(new Vector(381800.2163455189, -1149217.8900735674, 4136.636811081316));     //close titan 6 months
        //return getStandardShuttle(new Vector(381605.51557622006, -1148736.3302332184, 4127.753788215523));    //hit saturn 6 montha
        //return getStandardShuttle(new Vector(176154.29933254703, -831468.409196778, -894.8628493431781));       //close titan 1 year, few orbits
        return getStandardShuttle(new Vector(176148.06943044582, -831464.4838398469, -892.0608999642003).multiply(1));      //63.5 km 1 year
        //return getStandardShuttle(new Vector(176148.0730347496, -831464.488020747, -892.086879038433));       //400m

    }
    public static Shuttle getStandardShuttle(Vector vel) {
        return getStandardShuttle(vel, SolarSystem.planets[3]);
    }

    public static Shuttle getStandardShuttle(Vector vel, Body start) {
        return new Shuttle(vel, 20000, 500, 5, 20, 100e8, -1e6, 500e8, -2e4, 2000, start);
    }

    public static Shuttle getBackShuttle(Shuttle shuttle, Body start) {
        //-11758.832380706273 1087391.3637470687 -111573.91536482116
        //use sun
        return new Shuttle(new Vector(-11758.832380706273, 1087391.3637470687, -111573.91536482116), shuttle.getMass(), shuttle.getMinMass(), shuttle.getInnerRadius(), shuttle.getRadius(), shuttle.getMainEngineForce(), shuttle.getMainEngineMass(), shuttle.getLateralEngineForce(), shuttle.getLateralEngineMass(), shuttle.getParachute(), start);
    }

    public void setNextDataFirst(Vector VelocityDayMinus3, Vector VelocityDayMinus2, Vector VelocityDayMinus1, Vector VelocityActualDay, Vector AccelerationDayMinus3, Vector AccelerationDayMinus2, Vector AccelerationDayMinus1, Vector AccelerationActualDay,  Vector PositionActualDay) {
        super.setNextDataFirst(this.init, this.init, this.init, this.init, Vector.ZERO, this.init.multiply(1 / 3), this.init.multiply(2 / 3), this.init, this.position);
        this.nextAngle = new FourAdamsBashfort(SolarSystem.getTimeStep() / 4, Vector.ZERO, Vector.ZERO, Vector.ZERO, Vector.ZERO, Vector.ZERO);
        this.setTimeStep(SolarSystem.getTimeStep());
    }

    /* ********* Setters ******** */
    public void setTimeStep(double t) {
        if(t >= 0 && t != timeStep) {
            mainEngineForce *= t / timeStep;
            lateralEngineForce *= t / timeStep;
            mainEngineMass *= t / timeStep;
            lateralEngineMass *= t / timeStep;
            timeStep = t;
            nextVelocity.setTimeStep(t/4);
            nextPosition.setTimeStep(t/4);
            nextAngle.setTimeStep(t/4);
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
                Vector tmpAngular = angularSpeed.sum(radius.cross(acc).multiply(timeStep / inertia));
                if(!tmpAngular.isNaN())
                    angularSpeed = tmpAngular;  //TODO integral
            }
        }else {
            System.out.println("No fuel: require " + deltaMass + ", get " + mass);
        }
    }

    public void update(double deltaT) {
        if(!landing) {
            super.update(deltaT);
        }else {
            //position = position.sum(position.sum(velocity.multiply(deltaT))).multiply(0.5);
            if(!acceleration.isNaN()) {
                velocity = velocity.sum(velocity.sum(acceleration.multiply(deltaT))).multiply(0.5);
                position = position.sum(position.sum(velocity.multiply(deltaT))).multiply(0.5);
            }
        }
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
        double atmosphere = SolarSystem.planets[10].getAtmosphericPressureComparedToEarthPressure();
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
                    angularSpeed.set(i, 0);;
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

    /*public void brake(double targetVelocity, double tolerance, double timeStep) {
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
        //}
    //}


    public void brake(Vector target, Vector planetSpeed) {
        Vector delta = target.subtract(velocity);
        Vector main = delta.project(direction[2]);
        Vector lateral = delta.subtract(main);
        double mainTime = main.length() / mainEngineForce;
        double latTime = lateral.length() / lateralEngineForce;
        //System.out.println("Time: " + mainTime + " " + latTime);
        if(mainTime > timeStep) {
            mainTime = timeStep;
            main = main.multiply(timeStep / mainTime);
        }
        if(latTime > timeStep) {
            latTime = timeStep;
            lateral = lateral.multiply(timeStep / latTime);
        }
        if(mass >= -mainEngineMass * mainTime / timeStep) {
            mass += mainEngineMass * mainTime / timeStep;
            velocity = velocity.sum(main);
        }
        if(mass >= -lateralEngineMass * latTime / timeStep) {
            mass += lateralEngineMass * latTime / timeStep;
            velocity = velocity.sum(lateral);
        }
        //velocity = target;
        position = position.sum(velocity.multiply(timeStep));
    }

    public void land(Planet planet, double timeStep) {
        Vector dist = position.subtract(planet.getPosition());
        double d = dist.length();
        if(allowLanding && d < (planet.getDistanceAtmosphere() + planet.getRadius()) + 5e4) {       //start landing
            costEst = costEstimate(planet);
            System.out.println("Landing: " + (d - planet.getRadius()));
            //velocity = dist.normalize().multiply(-velocity.length() + 100e4);
            landing = true;
          //  if(!Runner.simulation)
                SolarSystem.setTimeStep(0.0001);
            setTimeStep(SolarSystem.getTimeStep());
            stopRotation(0, timeStep);
            //brake(planet.getVelocity().normalize().multiply(100e4).sum(planet.getVelocity()), planet.getVelocity());
            if(d > (planet.getDistanceAtmosphere() + planet.getRadius())) {         //in atmosphere
                alignTo(planet.getVelocity().normalize().subtract(dist.normalize()), true, timeStep, 0.1, 0);
                brake(planet.getVelocity().normalize().multiply(50e4).sum(planet.getVelocity()), planet.getVelocity());
            }else {
                alignTo(dist, true, timeStep, 0, 0);

                System.out.println("In atmosphere: " + (d - planet.getRadius()) + " " + planet.getName());
                double dot = dist.normalize().dot(getDirection(2).normalize());
                //roundoff error
                if(dot > 1)
                    dot = 1;
                else if (dot < -1)
                    dot = -1;
                //System.out.println("Angle (deg): " + (180 / Math.PI) * Math.acos(dot) + " " + dot);
                double v = getVelocity().distance(planet.getVelocity());
                //System.out.println("Speed: " + (v * 1e-4));
                useParachute(planet);   //-> we need this to land

                Vector[] p = Physics.wind(this, planet, 1, 0.2, 1);

                acceleration = acceleration.sum(p[0]);
                angularSpeed = angularSpeed.sum(p[1]);
                Vector drag = Physics.dragAcceleration(planet, this);   //-> we need this to land
                acceleration = acceleration.subtract(drag);
                //if(velocity.dot(dist) < 0)
                    //brake(dist.normalize().multiply(-1e4), planet.getVelocity());
                brake(planet.getVelocity().normalize().multiply(0e4).sum(planet.getVelocity()), planet.getVelocity());
            }
        }else {
            landing = false;
            SolarSystem.setTimeStep(0.05);
            setTimeStep(SolarSystem.getTimeStep());
        }
    }

    public double costEstimate(Planet p) {
        double v = velocity.subtract(p.getVelocity()).squareLength() / 1e4;
        double h = position.distance(p.getPosition());
        double a =  v  / (2 * h);
        double steps = Math.ceil(a * mass / mainEngineForce);   //F / Fstep
        return -mainEngineMass * fuelCost * v * mass / (2 * h * mainEngineForce);   //check formula
        //return steps * -mainEngineMass * fuelCost;
      //  return mainEngineForce / a  * fuelCost;
    }

    public double cost() {
        return (initMass - mass) * fuelCost;
    }
    public double getCostEstimate() {
        return  costEst;
    }

    /* ************************* */

    /* ***** Testing ******* */
    /*public void setAngularSpeed(Vector speed) {
        this.angularSpeed = speed;
    }*/
}
