public class FourAdamsBashfort {
    double timeStep;
    //previous value
    Vector wi; // actual vector at actual time

    //f(t-1, w-1), ...
    Vector f; // function at actual time
    Vector fMinus1; // at time-1
    Vector fMinus2; // at time-2
    Vector fMinus3; // at time-3
    Vector fPlus1;  // at time+1

    public FourAdamsBashfort(double timeStep, Vector wi, Vector f, Vector fMinus1, Vector fMinus2, Vector fMinus3){
        this.timeStep = timeStep;
        this.wi = wi;
        this.f = f;
        this.fMinus1 = fMinus1;
        this.fMinus2 = fMinus2;
        this.fMinus3 = fMinus3;
    }


    // Explicit Adams Bashfort 4steps
    public Vector getNext() {

        //calculate next step
        if(f == null)
            return null;
        wi = wi.sum(f.multiply(55).sum(fMinus1.multiply(-59)).sum(fMinus2.multiply(37)).sum(fMinus3.multiply(-9)).multiply(timeStep / 24));  // /24 or /12?
        //update values
        fMinus3 = fMinus2;
        fMinus2 = fMinus1;
        fMinus1 = f;
        f = null;
        return wi;
    }


    // Adams Moulton Implicit method  -> TODO compare
    public Vector getNext(Vector state) {
        wi = wi.sum((state.multiply(251).sum(f.multiply(646)).sum(fMinus1.multiply(-264)).sum(fMinus2.multiply(106)).sum(fMinus3.multiply(-19))).multiply(timeStep / 720));
        fMinus3 = fMinus2;
        fMinus2 = fMinus1;
        fMinus1 = f;
        f = state;
        return wi;
    }

    public Vector get() { return wi; }

    public void setNext(Vector state) {
        f = state;
    }

    public static Vector centredDiff(Vector pre, Vector next, double h) {
        return next.subtract(pre).multiply(1/h);
    }

    public static Vector forwardDiff(Vector curr, Vector next1, Vector next2, double h) {
        return curr.multiply(-3).sum(next1.multiply(4)).sum(next2.multiply(-1)).multiply(1/h);
    }

    public static Vector backwardDiff(Vector curr, Vector pre1, Vector pre2, double h) {
        return curr.multiply(3).sum(pre1.multiply(-4)).sum(pre2).multiply(1/h);
    }
}
