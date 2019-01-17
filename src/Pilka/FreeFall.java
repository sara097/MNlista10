package Pilka;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

public class FreeFall implements FirstOrderDifferentialEquations {

    private double g;

    public FreeFall() {
        this.g = 9.81;
    }

    public FreeFall(double g) {
        this.g = g;
    }
    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public void computeDerivatives(double v, double[] x, double[] dxdt) throws MaxCountExceededException, DimensionMismatchException {


        dxdt[0]=x[1];
        dxdt[1]=-g;
    }
}
