package sample;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

public class ODE implements FirstOrderDifferentialEquations {

    //klasa całkująca równania różniczkowe

    //atrybuty klasy
    private double a;
    private double b;
    private double I;

    //konstruktor klasy
    public ODE(double a, double b, double i) {
        this.a = a;
        this.b = b;
        I = i;
    }

    //metoda zwracająca rząd równania różniczkowego
    @Override
    public int getDimension() {
        return 2;
    }

    //metoda obliczająca pochodne
    @Override
    public void computeDerivatives(double t, double[] u, double[] dudt) throws MaxCountExceededException, DimensionMismatchException {

        //równania zadane w liście 10
        dudt[1] = 0.04 * (u[1] * u[1]) + (5 * u[1]) + 140 - u[0] + I;
        dudt[0] = a * (b * u[1] - u[0]);
    }
}
