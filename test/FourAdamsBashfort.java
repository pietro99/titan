public class FourAdamsBashfort {
    double timeStep;
    //previous value
    Vector wi;

    //f(t-1, w-1), ...
    Vector f;
    Vector f1;
    Vector f2;
    Vector f3;

    public FourAdamsBashfort(double timeStep, Vector wi, Vector f, Vector f1, Vector f2, Vector f3){
        this.timeStep = timeStep;
        this.wi = wi;
        this.f = f;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
    }


    public Vector getNext() {
        //calculate next step
        if(f == null)
            return null;
        wi = wi.sum(f.multiply(55).sum(f1.multiply(-59)).sum(f2.multiply(37)).sum(f3.multiply(-9)).multiply(timeStep / 12));
        //update values
        f3 = f2;
        f2 = f1;
        f1 = f;
        f = null;
        return wi;
    }

/* Implicit method  -> TODO compare
    public Vector getNext(Vector state) {
        w1 = w1.add(state.multiply(251).add(f.multiply(646)).add(f1.multiply(-264)).add(f2.multiply(106)).add(f3.multiply(-19)).multiply(timeStep / 720));
        f3 = f2;
        f2 = f1;
        f1 = f;
        f = state;
        return w1;
    }
*/

    public void setNext(Vector state) {
        f = state;
    }
}
