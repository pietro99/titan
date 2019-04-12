public class Physics {
    public static final double G = 6.67408E-11;
    public static final double earthDensity = 1;    //TODO check value
    public static final double titanDensity = 0.2;  //TODO check value
    public static final double friction = 0.1;      //TODO check value

    public static Vector toCartesian(double lat, double lon, double radius) {
        return new Vector(radius * Math.cos(lat) * Math.cos(lon), radius * Math.cos(lat) * Math.sin(lon), radius * Math.sin(lat));
    }

    public static Vector getFrictionAcceleration(Vector velocity, double friction, double density, double area) {
        return velocity.multiply( -0.5 * friction * density * velocity.length() * area);
    }

    public static void addPerturbation(Body b) {
        Vector direction = new Vector(Math.random(), Math.random(), Math.random()).normalize();
        b.addAcceleration(direction.multiply(Math.random() * 1000 / b.getMass()));
    }
}