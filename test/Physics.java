public class Physics {
    public static final double G = 6.67408E-11;
    public static final double earthDensity = 1;    //TODO check value
    public static final double titanDensity = 0.2;  //TODO check value

    private static final double dragConstant = 0.1; //TODO check value
    private static final double drag = 0.5;          //TODO

    public static Vector toCartesian(double lat, double lon, double radius) {
        return new Vector(radius * Math.cos(lat) * Math.cos(lon), radius * Math.cos(lat) * Math.sin(lon), radius * Math.sin(lat));
    }

    public static void addPerturbation(Body b) {
        Vector direction = new Vector(Math.random(), Math.random(), Math.random()).normalize();
        b.addAcceleration(direction.multiply(Math.random() * 1000 / b.getMass()));
    }

    public static Vector dragAcceleration(double heightMax, double heightMin, double height, Shuttle shuttle, double planetConst) { //TODO exponential?
        //zero - inverse quadratic - constant
        if(height > heightMax)
            return Vector.ZERO;
        else if{height < heightMin}
            return shuttle.getVelocity().multiply(-dragConstant * planetConst / shuttle.getMass());
        else
            return shuttle.getVelocity().multiply(-drag * planetConst * shuttle.getDragArea() / (mass * height * height));
    }
}