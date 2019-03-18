public class Physics {
    public static final double G = 6.67408E-11;

    public static final InertiaSolver NO_INERTIA = new InertiaSolver() {
        public double solve(Vector radius)  {
            return 0;
        }
    };

    //TODO
    public static final InertiaSolver SPHERE = null;
    public static final InertiaSolver CUBOID = null;
    public static final InertiaSolver SHELL = null;
}