public class Simulation {
    public static int delta = 250;
    private int target;
    private int start;
    private int count;
    private int limit;
    private int gen;
    private double factor = 1;

    private double error;
    private Vector bestPos;
    private Vector bestTarget;

    private SolarSystem system;
    private Shuttle shuttle;
    private Vector init;

    public Simulation(SolarSystem s, int start, int target, int limit) {
        this(s, start, target, limit, s.getShuttle().getInitialVelocity());
    }

    public Simulation(SolarSystem s, int start, int target, int limit, Vector init) {
        this.target = target;
        this.start = start;
        this.limit = limit + delta;

        gen = 0;
        count = 0;

        bestPos = s.getShuttle().getPosition();
        bestTarget = s.getPlanets()[target].getPosition();
        error = getError();
        system = s;
        this.init = init;
        this.shuttle = s.getShuttle();
        System.out.println("Start: " + s.getPlanets()[start].getName());
        System.out.println("Target: " + s.getPlanets()[target].getName());
        System.out.println("init: " + this.init);
    }

    public void addStep(int c) {
        count += c;
    }
    public void addStep() {count += delta; }

    public SolarSystem tryNext() {  //TODO travel back
        if(count > limit) {
            gen++;
            Shuttle shuttle = system.getShuttle();
            bestPos = shuttle.getPosition();

            bestTarget = system.getPlanets()[target].getPosition();

            double newErr = getError();
            if(newErr > error) {
                factor += 0.1;
            }

            Vector correction = bestTarget.subtract(bestPos);

            System.out.println("Start: " + system.getPlanets()[start].getName());
            System.out.println("Target: " + system.getPlanets()[target].getName());
            System.out.println("Select: " + init);
            System.out.println("Position: " + bestPos);
            System.out.println("Target: " + bestTarget);
            System.out.println("Correction: " + correction);
            System.out.println("Error: " + (error - system.getPlanets()[target].getRadius()));

            Vector newInit = init.sum(correction.multiply(1 / getScaling(newErr))); //TODO distinguish between titan-earth init and earth-titan init
            System.out.println("New: " + newInit);

            double mass = system.getShuttle().getMass();
            count = 0;
            system = new SolarSystem();
            shuttle = Shuttle.getStandardShuttle(newInit, system.getPlanets()[start]);
            shuttle.setMass(mass);
            system.setShuttle(shuttle);
            error = newErr;
            init = newInit;
            return system;
        }
        return null;
    }

    public Vector getInit() {
        return init;
    }

    public double getError() {
        return bestPos.distance(bestTarget);
    }

    private double getScaling(double err) {
        return Math.pow(err, (0.32 + (count / 7e8)) * factor);
    }

    public SolarSystem getSystem() {
        return system;
    }   //redundant

    public Shuttle getShuttle() { return shuttle; }

    public double getErrorRadius() { return error - system.getPlanets()[target].getRadius();}

    public int getTarget() { return target; }
    public int getStart() { return start; }

    public static Simulation getEarthTitan(SolarSystem s) {

        return new Simulation(s, 3, 10, 3600 * 24 * 365 * 2 , new Vector(150000, -800000, -1000));
    }

    public static Simulation getTitanEarth(SolarSystem s) {
        final int start = 10;
        final int target = 3;

        return new Simulation(s, 10, 3, (3600 * 24 * 365 * 2), Shuttle.getStandardShuttle().getInitialVelocity().multiply(-1.5));
        //return new Simulation(s, 10, 3, (3600 * 24 * 365 * 2), new Vector(-11758.832380706273, 1087391.3637470687, -111573.91536482116));
        //return new Simulation(s, 10, 3, (3600 * 24 * 365 * 2), new Vector(0, 1e8, 0));
    }
}