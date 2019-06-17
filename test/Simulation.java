public class Simulation {
    private int target;
    private int start;
    private int count;
    private int limit;
    private int gen;
    private double factor = 2.0;

    private double error;
    private Vector bestPos;
    private Vector bestTarget;

    private SolarSystem system;
    private Vector init;

    public Simulation(SolarSystem s, int start, int target, int limit) {
        this.target = target;
        this.start = start;
        this.limit = limit;

        gen = 0;

        bestPos = s.getShuttle().getPosition();
        bestTarget = s.getPlanets()[target].getPosition();
        error = getError();
        system = s;
        init = system.getShuttle().getInitialVelocity();
    }

    public void addStep(int c) {
        count += c;
    }

    public SolarSystem tryNext() {  //TODO travel back
        if(count >= limit) {
            gen++;
            Shuttle shuttle = system.getShuttle();
            bestPos = shuttle.getPosition();

            bestTarget = system.getPlanets()[target].getPosition();

            double newErr = getError();
            if(newErr > error) {
                factor += 0.1;
            }

            Vector correction = bestTarget.subtract(bestPos);

            System.out.println("Select: " + init);
            System.out.println("Position: " + bestPos);
            System.out.println("Target: " + bestTarget);
            System.out.println("Correction: " + correction);
            System.out.println("Error: " + error);

            Vector newInit = shuttle.getInitialVelocity().sum(correction.multiply(1 / getScaling(newErr)));
            count = 0;
            system = new SolarSystem();
            shuttle = Shuttle.getStandardShuttle(newInit, system.getPlanets()[start]);
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
    }

    public static Simulation getEarthTitan(SolarSystem s) {
        return new Simulation(s, 3, 11, 3600 * 24 * 365 * 2);
    }

    public static Simulation getTitanEarth(SolarSystem s) {
        return new Simulation(s, 11, 11, (3600 * 24 * 365 * 2) * 2);
    }
}