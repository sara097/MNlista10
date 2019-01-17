package Pilka;


import org.apache.commons.math3.ode.events.EventHandler;

public class TurnignPoint implements EventHandler {

    private int sign;

    @Override
    public void init(double t, double[] x, double dxdt) {
        sign=1;
    }

    @Override
    public double g(double t, double[] x) {
        return sign*x[0] ; //w liscie v-30 czyli x[1]-30 u mnie, sign mi zostaje
        //bo tutaj gdzie funkcja g jest rowna 0 tam mamy zdarzenie
    }

    @Override
    public Action eventOccurred(double t, double[] x, boolean b) {

        sign=-sign;
        return Action.RESET_STATE;
    }

    @Override
    public void resetState(double t, double[] x) {

        x[1]=-x[1];

    }
}
