public class Gravity implements GravityMethod {
    Body[] bodies;
    SolarSystem solarSystem;
    Shuttle shuttle;
    public Gravity(SolarSystem solarSystem){
        this.shuttle = solarSystem.getShuttle();
        this.solarSystem = solarSystem;
        Planet[] planets = solarSystem.getPlanets();
        bodies = new Body[planets.length+1];
        for(int i=0; i<planets.length; i++){
            bodies[i] = planets[i];
        }
        bodies[bodies.length-1] = shuttle;
    }
    @Override
    public void calculateForce() {
        resetAcceleration();
        calculateGravity();
    }
    private void resetAcceleration() {
        for(int i = 0; i < bodies.length; i++)
            bodies[i].setAcceleration(new Vector(0,0,0));
    }

    private void calculateGravity() {
        if(!Runner.getOnTitan())
            shuttle.land(solarSystem.getTitan(), SolarSystem.getTimeStep());
        else
            shuttle.land(solarSystem.getEarth(), SolarSystem.getTimeStep());

        for(int i = 0; i < bodies.length; i++) {	//first planet
            for(int j = 1; j < bodies.length - i; j++) {        //other planet
                Vector distance = bodies[i].getPosition().subtract(bodies[i + j].getPosition());	//from j to i
                double d = distance.squareLength();
                if(d < 1000) {
                    d = 1000;
                    distance = distance.normalize().multiply(1000);
                }
                distance = distance.normalize();
                bodies[i + j].addAcceleration(distance.multiply(gravity(bodies[i], d)));
                bodies[i].addAcceleration(distance.multiply(-1 * gravity(bodies[i+j], d)));
            }
        }
    }

    private double gravity( Body p, double d) {
        return (Physics.G * p.getMass())/d;
    }

}
