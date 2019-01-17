package sample;

import org.apache.commons.math3.ode.events.EventHandler;

public class MaxPot implements EventHandler {

    //metoda obsługująca zdarzenia w bibliotece Apache Commons Math

    //atrubuty klasy
    private int sign;
    private double c;
    private double d;

    //konstrktor klasy
    public MaxPot(double c, double d) {
        this.c = c;
        this.d = d;
    }

    //metoda inicjalizująca event handler.
    @Override
    public void init(double t, double[] u, double dudt) {
        sign = 1;
    }

    // metoda sprawdzajaca czy zaszło zdarzenie
    @Override
    public double g(double t, double[] u) {
        return sign * (u[1] - 30);  //oblicza miejsce zerowe, ktore dla nas jest rowne 30
    }

    //metoda po zajsciu zdarzenia
    @Override
    public Action eventOccurred(double t, double[] x, boolean b) {

        sign = -sign; //zmienia znak
        return Action.RESET_STATE; //resetuje stan, czyli pochodne bedzie liczyc od poczatku
    }

    //metoda zmieniajaca stan
    @Override
    public void resetState(double t, double[] u) {
        //ustawiamy nowe rownania, podane w liscie 10
        u[1] = c;
        u[0] = u[0] + d;
    }
}
