public class Physics {
    public static final double G = 6.67408E-11; //m^3 * kg^-1 * s^-2
    //public static final double G = 6.67408E-20; //km^3 * kg^-1 * s^-2
    public static final double EPS = 1E-15;

    //private static final double dragConstant = 100;
    public static final double drag = 250;

    public static Vector dragAcceleration(Planet p, Shuttle shuttle) {
        double height = p.getPosition().subtract(shuttle.getPosition()).length() - p.getRadius();
        if(height < p.getDistanceAtmosphere()) {
            return shuttle.getVelocity().multiply(-drag * p.getAtmosphericPressureComparedToEarthPressure() * shuttle.getDragArea() / shuttle.getMass() * Math.sqrt(p.getDistanceAtmosphere()- height));
        }
        return Vector.ZERO;
    }

    public static Vector[] wind(Shuttle shuttle, Planet p, double scale, double change, double rotScale) {
        double height = shuttle.getPosition().subtract(p.getPosition()).length() - p.getRadius();
        double limit = p.getDistanceAtmosphere();
        Vector wind = Vector.ZERO;
        Vector rot = Vector.ZERO;

        if(height < limit) {
            //wind = shuttle.getDirection(0).multiply(Math.random()).sum(shuttle.getDirection(1).multiply(Math.random()));
            wind = shuttle.getPosition().subtract(p.getPosition()).cross(Vector.random()).normalize();
            wind = wind.sum(new Vector(change * Math.random() * Math.max(wind.getX(), .1), change * Math.random() * Math.max(wind.getY(), .1), change * Math.random() * Math.max(wind.getZ(), .1)));

            wind = wind.normalize().multiply((Math.pow(1.01011793482, Math.sqrt(height) - 1 + scale)) / 3600);

            rot = new Vector(Math.random(), Math.random(), Math.random());
            rot = rot.multiply(rotScale * wind.length());
            if(rot.isNaN())
                rot = new Vector(Math.random(), Math.random(), Math.random()).multiply(rotScale);
        }
        return new Vector[]{wind, rot};
    }
}