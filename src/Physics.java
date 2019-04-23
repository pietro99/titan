public class Physics {
    public static final double G = 6.67408E-11;
    public static final double earthDensity = 1;    //TODO check value
    public static final double titanDensity = 1.2;  //TODO check value

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

    public static Vector[] wind(Shuttle shuttle, Planet p, double squareLimit, double scale, double change, double rotScale) {
        double height = shuttle..getPosition().subtract(p.getPostion()).squareLength();
        Vecotr wind = Vector.ZERO;
        Vector rot = Vector.ZERO;

        if(height < squareLimit) {
            Vector wind = shuttle.getDirection(0).multiply(Math.random()).sum(shuttle.getDirection(1).multiply(Math.random()));
            wind = wind.sum(new Vector(change * Math.random() * wing.getX(), change * Math.random() * wing.getY(), change * Math.random() * wind.getZ()));
            wind = wind.multiple(height / (scale * shuttle.getMass()));

            Vector rot = new Vector(Math.random(), Math.random(), Math.random());
            rot = rot.multiply(rotScale);
        }
        return new Vector[]{wind, rot};
    }
}