public abstract class Body {
    protected Vector position, velocity, acceleration;
    protected double mass, radius;

    public Vector getPosition() { return position; }
    public Vector getVelocity() { return velocity; }
    public Vector getAcceleration() { return acceleration;}

    public double getMass() { return mass; }
    public double getRadius() { return radius; }

    public void setPosition(Vector position) { this.position = position; }
    public void setVelocity(Vector velocity) { this.velocity = velocity; }
    public void setAcceleration(Vector acceleration) { this.acceleration = acceleration; }

    public void setMass(double mass) { this.mass = mass; }
    public void setRadius(double radius) { this.radius = radius; }

    public void addAcceleration(Vector acc) {
        acceleration = acceleration.sum(acc);
    }

    public void update(double deltaT) {
        velocity = velocity.sum(velocity.sum(acceleration.multiply(deltaT))).multiply(0.5);
        position = position.sum(position.sum(velocity.multiply(deltaT))).multiply(0.5);
        acceleration = new Vector(0, 0, 0);
    }
}