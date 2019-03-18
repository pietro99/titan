public class Shuttle {
    private static final double epsilon = 1E-10;
    private double mass;
    private Vector acceleration, velocity, position, direction;
    private Vector angularSpeed;
    private InertiaSolver inertia;

    public Shuttle(Vector position, Vector velocity, double mass) {
        //TODO motor force, consume mass, controller, rigid body shape (InertiaSolver)
        this.position = position;
        this.direction = direction;
        this.velocity = velocity;
        this.mass = mass;
        this.angularSpeed = new Vector(0, 0,0);
        this.inertia = Physics.NO_INERTIA;
    }

    public void calculateGravity(Planet[] planets) {
        acceleration.set(0, 0, 0);
        for(int i = 0; i < planets.length; i++) {
            Vector distance = planets[i].getPosition().subtract(this.getPosition());
            double d = distance.squareLength();
            distance = distance.normalize();
            acceleration = acceleration.sum(distance.multiply(Planet.g * planets[i].getMass() / d));
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
        //linear movement
        velocity = velocity.sum(velocity.sum(acceleration.multiply(deltaT))).multiply(0.5);
        position = position.sum(position.sum(velocity.multiply(deltaT))).multiply(0.5);

        //rotation
        if(angularSpeed.squareLength() < epsilon) {
            direction = direction.rotate(angularSpeed, angularSpeed.length() * deltaT);
        }
    }

    public Vector getPosition() { return position; }
    public double getMass() { return mass; }
    public Vector getVelocity() { return velocity; }
    public Vector getAcceleration() { return acceleration; }
    public Vector getDirection() { return direction; }
    public Vector getAngularSpeed() { return direction; }
}