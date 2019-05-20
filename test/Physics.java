public class Physics {
    public static final double G = 6.67408E-11;
    public static final double EPS = 1E-15;

    private static final double dragConstant = 10;
    private static final double drag = 25;

    public static Vector dragAcceleration(Planet p, Shuttle shuttle) {
        double height = p.getPosition().subtract(shuttle.getPosition()).length() - p.getRadius();
        if(height < p.getDistanceAtmosphere()) {
            return shuttle.getVelocity().multiply(-drag * p.getAtmosphericPressureComparedToEarthPressure() * shuttle.getDragArea() / shuttle.getMass() * Math.sqrt(p.getDistanceAtmosphere()- height));
        }
        return Vector.ZERO;
    }

    public static Vector[] wind(Shuttle shuttle, Planet p, double scale, double change, double rotScale) {
        double height = shuttle.getPosition().subtract(p.getPosition()).squareLength();
        double squareLimit = p.getDistanceAtmosphere() * p.getDistanceAtmosphere() ;

        Vector wind = Vector.ZERO;
        Vector rot = Vector.ZERO;

        if(height < squareLimit) {
            //wind = shuttle.getDirection(0).multiply(Math.random()).sum(shuttle.getDirection(1).multiply(Math.random()));
            wind = shuttle.getPosition().subtract(p.getPosition()).cross(Vector.random()).normalize();
            wind = wind.sum(new Vector(change * Math.random() * Math.max(wind.getX(), .1), change * Math.random() * Math.max(wind.getY(), .1), change * Math.random() * Math.max(wind.getZ(), .1)));
            //wind = wind.multiply(height / (scale * shuttle.getMass()));
            wind = wind.normalize().multiply((Math.pow(1.01011793482, Math.sqrt(height) - 1 + scale)) / 3600);
            //wind = wind.sum(p.getVelocity());
            rot = new Vector(Math.random(), Math.random(), Math.random());
            rot = rot.multiply(rotScale * wind.length());
        }
        return new Vector[]{wind, rot};
    }

    public static void main(String[] args){
        if(args.length >= 1) {
            if(args[0].equals("drag")) {
                Planet p1 = new Planet("titan", new Vector(2000, 2000, 2000), Vector.ZERO, 500, 500, 600, 1.5);
                Planet p2 = new Planet("titan", Vector.ZERO, Vector.ZERO, 100, 100, 600, 1.5);
                Shuttle s = new Shuttle(new Vector(10, 10, 10), 1000, 1000, 5, 10, 10, 10, 6, 6, 0.1, p2);
                Vector acc;
                for(int i = 0; i < 60; i++) {
                    s.update(10);
                    acc = dragAcceleration(p1, s);
                    System.out.println("Drag: " + acc.length() + " Distance: " + p1.getPosition().subtract(s.getPosition()).length() + " Direction: " + acc.dot(s.getVelocity()));
                }
            }else if(args[0].equals("wind")) {
                Planet p1 = new Planet("titan", Vector.ZERO, Vector.ZERO, 500, 500, 1000, 1.5);
                Planet p2 = new Planet("titan", Vector.ZERO, Vector.ZERO, 700, 500, 600, 1.5);
                Shuttle s = new Shuttle(new Vector(0, 0, 1), 1000, 1000, 5, 10, 10, 10, 6, 6, 0.1, p2);
                Vector[] f;
                System.out.println(p1.getPosition().subtract(s.getPosition()).length());
                for(int i = 0; i < 10; i++) {
                    f = wind(s, p1, 10, .1, 1);
                    System.out.println("Wind: " + f[0].length() + " Rot: " + f[1].length());
                }
            }
        }
    }
}