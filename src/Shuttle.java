public class Shuttle extends Body{
    private static final double epsilon = 1E-10;
    private Vector direction;
    private Vector angularSpeed;
    private InertiaSolver inertia;
    public Vector init;

    public Shuttle(Vector velocity, double mass) {
        //TODO motor force, consume mass, controller, rigid body shape (InertiaSolver)
        this.acceleration = new Vector(0, 0, 0);
        this.position = SolarSystem.getPlanets()[3].getPosition().sum(velocity.normalize().multiply(SolarSystem.getPlanets()[3].getRadius()));
        this.direction = velocity.normalize();
        this.velocity = velocity;
        this.mass = mass;
        this.angularSpeed = new Vector(0, 0,0);
        this.inertia = Physics.NO_INERTIA;
        this.init = velocity;
    }

    public Shuttle(Vector position, Vector velocity, double mass, InertiaSolver inertia) {
        this(velocity, mass);
        this.inertia = inertia;
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
        acceleration = acceleration.sum(acc);
        mass += deltaMass;
        if(radius.squareLength() < epsilon) {   //TODO check physics: mass of the object and mass consumed
            angularSpeed.sum(radius.cross(acc).multiply(1 / inertia.solve(radius)));
        }
    }

    public void update(double deltaT) {
        super.update(deltaT);

        //rotation
        if(angularSpeed.squareLength() < epsilon) {
            direction = direction.rotate(angularSpeed, angularSpeed.length() * deltaT);
        }
    }
}
