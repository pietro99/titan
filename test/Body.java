import javafx.beans.binding.ObjectExpression;

public abstract class Body {
    protected Vector position, velocity, acceleration;
    protected double mass, radius;

    public FourAdamsBashfort nextVelocity = null;
    public FourAdamsBashfort nextPosition = null;

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
        if (nextVelocity == null){
            //  EULER METHOD
            velocity = velocity.sum(velocity.sum(acceleration.multiply(deltaT))).multiply(0.5);
            position = position.sum(position.sum(velocity.multiply(deltaT))).multiply(0.5);
        }else{
            velocity = nextVelocity.getNext(acceleration);
            position = nextPosition.getNext(velocity);
        }

    }

    public void setNextDataFirst(Vector VelocityDayMinus3, Vector VelocityDayMinus2, Vector VelocityDayMinus1, Vector VelocityActualDay, Vector AccelerationDayMinus3, Vector AccelerationDayMinus2, Vector AccelerationDayMinus1, Vector AccelerationActualDay,  Vector PositionActualDay){
        //Adams solvers
        nextVelocity = new FourAdamsBashfort(SolarSystem.getTimeStep(), VelocityActualDay, AccelerationActualDay, AccelerationDayMinus1, AccelerationDayMinus2,AccelerationDayMinus3);
        nextPosition = new FourAdamsBashfort(SolarSystem.getTimeStep(), PositionActualDay, VelocityActualDay, VelocityDayMinus1, VelocityDayMinus2, VelocityDayMinus3);
    }
}