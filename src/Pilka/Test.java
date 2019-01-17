package Pilka;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        FirstOrderDifferentialEquations freeFall= new FreeFall();
        FirstOrderIntegrator rkIntegrator = new ClassicalRungeKuttaIntegrator(0.01);
        BouncingBall bouncingBall=new BouncingBall();
        rkIntegrator.addStepHandler(bouncingBall);

        TurnignPoint turnignPoint=new TurnignPoint();
        rkIntegrator.addEventHandler(turnignPoint, 0.1, 0.001, 2000);
        //pierwszy parametr to 0.1 musi byc wiekszy od dlugosci kroku calkowania i mamy to gdzies
        //kolejny to v1 okresla dokladnosc , musi byc duza dosc zeby zauwazył zderzenie
        //ostatni to maksymalna ilosc prob jaka mozna dokonac znajdujac miejsce zerowe, wiec nie ma znaczenia byle duze

        double [] x=new double[]{1,0};

        rkIntegrator.integrate(freeFall,0,x,0.5,x);

        System.out.println(Arrays.toString(x));
        saveData("dane", bouncingBall);
    }

    public static void saveData(String name, BouncingBall bb) {
        //utworzenie zmiennych pomocniczych
        String t = "";
        String h = "";
        String v = "";
        String output = "";

        //utworzenie obiektu PrintWriter
        PrintWriter save;
        try { //otoczony blokiem try i catch zapis do pliku
            save = new PrintWriter(name); //przypisanie referencji do obiektu

            for (int i = 0; i < bb.getHeight().size(); i++) {
                //pętla każdorazowo pobiera wartosci z list tablicowych
                t = bb.getTime().get(i).toString();
                h = bb.getHeight().get(i).toString();
                v = bb.getVelocity().get(i).toString();

                //z pobranych wartosci tworzony jest jeden string ( odpowiednio sformatowany)
                output = t + ";" + h + ";" + v ;
                save.println(output); //utworzony string zapisywany jest w kolejnej linii w pliku

            }
            save.close(); //zamniecie zapisu

        } catch (FileNotFoundException e1) { //obsługa wyjątków
            e1.printStackTrace();
        }
    }
}
