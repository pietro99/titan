public class FourAdamsBashfort {
    public FourAdamsBashfort(double timeStep, Vector wi, Vector wMinus1, Vector wMinus2, Vector wMinus3){
        this.timeStep = timeStep;
        this.wi = wi;
        this.wMinus1 = wMinus1;
        this.wMinus2 = wMinus2;
        this.wMinus3 = wMinus3;
    }

    double timeStep;
    Vector wi;
    Vector wMinus1;
    Vector wMinus2;
    Vector wMinus3;

    private double positionX;
    private double positionY;
    private double positionZ;


    public Vector getVectorPosition(){
        getPositionX();
        getPositionY();
        getPositionZ();

        Vector solution = new Vector(positionX,positionY,positionZ);

        return solution;
    }


    public double getPositionX(){
        double wiX = wi.getX();
        double wMinus1X = wMinus1.getX();
        double wMinus2X = wMinus2.getX();
        double wMinus3X = wMinus3.getX();

        double w = wiX + (1/24)*(timeStep)*(55*wiX) - (59*wMinus1X) + (37*wMinus2X) - (9*wMinus3X);  // Four-stage Adams-Bashforth method equation
                                                                                                    // Local error E = 251*h^5*y^(5)*(τ). Global error O(h4); fourth-order
        positionX = w;
        return w;
    }


    public double getPositionY(){
        double wiY = wi.getY();
        double wMinus1Y = wMinus1.getY();
        double wMinus2Y = wMinus2.getY();
        double wMinus3Y = wMinus3.getY();

        double w = wiY + (1/24)*(timeStep)*(55*wiY) - (59*wMinus1Y) + (37*wMinus2Y) - (9*wMinus3Y);  // Four-stage Adams-Bashforth method equation
                                                                                                    // Local error E = 251*h^5*y^(5)*(τ). Global error O(h4); fourth-order
        positionY = w;
        return w;
    }

    public double getPositionZ(){
        double wiZ = wi.getZ();
        double wMinus1Z = wMinus1.getZ();
        double  wMinus2Z = wMinus2.getZ();
        double wMinus3Z = wMinus3.getZ();

        double w = wiZ + (1/24)*(timeStep)*(55*wiZ) - (59*wMinus1Z) + (37*wMinus2Z) - (9*wMinus3Z);  // Four-stage Adams-Bashforth method equation
                                                                                                    // Local error E = 251*h^5*y^(5)*(τ). Global error O(h4); fourth-order
        positionZ = w;
        return w;
    }

}
