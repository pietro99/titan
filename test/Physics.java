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

    public static final InertiaSolver NO_INERTIA = new InertiaSolver() {
        public double solve(Vector radius)  {
            return 0;
        }
    };

    public static final InertiaSolver BALL = new InertiaSolver() {
        public double solve(Vector radius) {
            return radius.squareLength() * 0.4;
        }
    };

    //TODO check force not applied on the surface
    public final class SHELL implements InertiaSolver {
        private double inRadius, outRadius;

        public SHELL(double inRadius, double outRadius) {
            this.inRadius = inRadius;
            this.outRadius = outRadius;
        }

        public double solve(Vector radius) {
            return 0.4 * (Math.pow(outRadius, 5) - Math.pow(inRadius, 5)) / (Math.pow(outRadius, 3) - Math.pow(inRadius, 3));
        }
    }

    //TODO check rotation not alligned to faces
    public final class CUBE implements InertiaSolver {
        private double size;
        public CUBE(double size) {
            this.size = size;
        }
        public double solve(Vector radius) {
            return 1/ 6 * Math.pow(size, 2);
        }
    };

}