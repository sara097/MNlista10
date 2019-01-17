package Pilka;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class BouncingBall implements StepHandler {

    private ArrayList<Double>height=new ArrayList<>();
    private ArrayList<Double>velocity=new ArrayList<>();
    private ArrayList<Double>time=new ArrayList<>();


    public ArrayList<Double> getHeight() {
        return height;
    }

    public void setHeight(ArrayList<Double> height) {
        this.height = height;
    }

    public ArrayList<Double> getVelocity() {
        return velocity;
    }

    public void setVelocity(ArrayList<Double> velocity) {
        this.velocity = velocity;
    }

    public ArrayList<Double> getTime() {
        return time;
    }

    public void setTime(ArrayList<Double> time) {
        this.time = time;
    }

    @Override
    public void init(double v, double[] doubles, double v1) {

    }

    @Override
    public void handleStep(StepInterpolator stepInterpolator, boolean b) throws MaxCountExceededException {

        double t=stepInterpolator.getCurrentTime(); //pobranie obecnego

        double [] x=stepInterpolator.getInterpolatedState();
       time.add(t);

        height.add(x[0]);
        velocity.add(x[1]);
        System.out.println("t= "+t+" "+ Arrays.toString(x));
    }
}
