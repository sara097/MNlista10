package sample;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

import java.util.ArrayList;
import java.util.Arrays;

public class Path implements StepHandler {

    //klasa zapisujaca nam kolejne kroki wycałkowane z rownan różniczkowych

    //atrubuty klasy ( listy tablicowe do których zapisujemy uzyskane wartosci
    private ArrayList<Double> uValues = new ArrayList<>();
    private ArrayList<Double> vValues = new ArrayList<>();
    private ArrayList<Double> times = new ArrayList<>();
    private double time = 0;


    public ArrayList<Double> getTimes() {
        return times;
    }

    public ArrayList<Double> getuValues() {
        return uValues;
    }

    public ArrayList<Double> getvValues() {
        return vValues;
    }


    public double getTime() {
        return time;
    }

    //nieuzywana metoda inicjalizująca
    @Override
    public void init(double v, double[] doubles, double v1) {

    }

    //pobieranie kroku i zapisywanie go do list tablicowych oraz wyswietlanie
    @Override
    public void handleStep(StepInterpolator stepInterpolator, boolean b) throws MaxCountExceededException {

        double t = stepInterpolator.getCurrentTime(); //pobranie obecnego

        double[] u = stepInterpolator.getInterpolatedState();
        time = t;

        uValues.add(u[0]);
        vValues.add(u[1]);
        times.add(time);

        System.out.println("t= " + t + " " + Arrays.toString(u));


    }
}
