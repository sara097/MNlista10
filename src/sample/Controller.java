package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;

import java.util.ArrayList;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class Controller {
    //klasa kontrolera

    //elementy GUI

    @FXML
    private LineChart<Number, Number> uChart;

    @FXML
    private NumberAxis xAxisuChart;

    @FXML
    private NumberAxis yAxisUChart;

    @FXML
    private LineChart<Number, Number> vChart;

    @FXML
    private NumberAxis xAxisvChart;

    @FXML
    private NumberAxis yAxisvChart;

    @FXML
    private LineChart<Number, Number> IChart;

    @FXML
    private NumberAxis xAxisiChart;

    @FXML
    private NumberAxis yAxisiChart;

    @FXML
    private TextArea textArea;

    @FXML
    private TextField aParamTxt;

    @FXML
    private TextField bParamTxt;

    @FXML
    private TextField cParamTxt;

    @FXML
    private TextField dParamTxt;

    //parametry klasy
    private double a = 0;
    private double b = 0;
    private double c = 0;
    private double d = 0;


    private TextFormatter format() { //prywatna metoda (może ją użyc tylko metoda z klasy) zwracająca obiekt typu TextFormatter
        //ustawienie formatowania tekstu w polach tekstowych (zeby nie wpisywać niedozwolonych wartości
        Pattern pattern = Pattern.compile("[\\-]?\\d{0,10}([\\.]\\d{0,2})?"); //ustawienie wzoru formatowania tekstu

        //ustawienie formatowania tekstu z użyciem interfejsu UnaryOperator
        // Tworze obiekt klasy TextFormatter, w którego konstruktorze używam operatora lambda
        //jesli wyrazenie wpisywane nie pasuje do wzoru formatowania nie pojawia się w polu tekstowym
        TextFormatter formatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return pattern.matcher(change.getControlNewText()).matches() ? change : null; //wyrazenie lambda zwraca null jesli wpisywany tekst nie pasuje do wzrou formatowania
        });
        return formatter; //metoda zwraca obiekt typu TextFormatter
    }

    //metoda inicjalizująca okno aplikacji
    @FXML
    private void initialize() {
        aParamTxt.setTextFormatter(format());
        bParamTxt.setTextFormatter(format());
        cParamTxt.setTextFormatter(format());
        dParamTxt.setTextFormatter(format());
        aParamTxt.setText("0.02");
        bParamTxt.setText("0.2");
        cParamTxt.setText("-65");
        dParamTxt.setText("2");
        textArea.setEditable(false);
    }

    //metoda po kliknieciu przycisku
    @FXML
    void clicked(ActionEvent event) {

        IChart.getData().removeAll(IChart.getData());
        uChart.getData().removeAll(uChart.getData());
        vChart.getData().removeAll(vChart.getData());

        //ustawienie koloru pól tekstowych
        aParamTxt.setStyle("-fx-background-color: #ffffff;");
        bParamTxt.setStyle("-fx-background-color: #ffffff;");
        cParamTxt.setStyle("-fx-background-color: #ffffff;");
        dParamTxt.setStyle("-fx-background-color: #ffffff;");

        //sprawdzenie czy pola tekstowe nie sa puste
        //jesli nie są to pobranie wartości
        //jesli są to zmiana koloru pól i wyrzucenie błędu
        if (aParamTxt.getText().isEmpty() || bParamTxt.getText().isEmpty() || cParamTxt.getText().isEmpty() || dParamTxt.getText().isEmpty()) {
            aParamTxt.setStyle("-fx-background-color: #e60b00;");
            bParamTxt.setStyle("-fx-background-color: #e60b00;");
            cParamTxt.setStyle("-fx-background-color: #e60b00;");
            dParamTxt.setStyle("-fx-background-color: #e60b00;");
            throw new IllegalArgumentException("Wrong parameters");
        } else {
            a = Double.parseDouble(aParamTxt.getText());
            b = Double.parseDouble(bParamTxt.getText());
            c = Double.parseDouble(cParamTxt.getText());
            d = Double.parseDouble(dParamTxt.getText());
        }

        //ustawienie wartosci początkowych
        double I = 0;
        double v0 = c;
        double u0 = b * v0;

        //utworzenie obiektu klasy ODE
        FirstOrderDifferentialEquations ode = new ODE(a, b, I);
        //utworzenie integratora Runge Kutta
        FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(0.01);
        //utworzneie obiektu klasy Path
        Path path = new Path();
        //dodanie do integratora step Handlera
        integrator.addStepHandler(path);

        //ustawienie czasu poczatkowego i koncowego
        double Tk = 0.001; //dokładnosc z jaka interesuje nasz czas (zeby wyznaczyc kiedy minie 15% pomiaru)
        double te = 50;
        double T15 = (0.15 * te);

        double[] yStart = new double[]{u0, v0}; //warunki początkowe
        double[] yStop = new double[]{0, 1};

        integrator.integrate(ode, 0, yStart, T15, yStop); //całkowanie równania

        //jesli mineło 15% pomiaru ( w granicy dokładnosci)
        if ((path.getTime() < (T15) + Tk) && (path.getTime() > (T15) - Tk)) {
            System.out.println("nowe i");
            //jesli tak to zmiana prądu
            I = 10;
            // ustawienie nowych wartosci początkowych
            v0 = path.getvValues().get(path.getvValues().size() - 1);
            u0 = path.getuValues().get(path.getuValues().size() - 1);

            ode = new ODE(a, b, I);
            yStart = new double[]{u0, v0}; //warunki początkowe
            yStop = new double[]{0, 1};
            //ustawienie event handlera
            MaxPot maxPot = new MaxPot(c, d);
            //dodanie event handlera do integratora
            integrator.addEventHandler(maxPot, 0.1, 0.001, 2000);
            //ponowne całkowanie równania
            integrator.integrate(ode, T15, yStart, te, yStop);
        }

        //pobranie list z wartosciami po całkowaniu
        ArrayList<Double> uValues = path.getuValues();
        ArrayList<Double> vValues = path.getvValues();
        ArrayList<Double> time = path.getTimes();

        //listy na wartosci maksymalne potencjału i odpowiadajace im czasy
        ArrayList<Double> vMax = new ArrayList<>();
        ArrayList<Double> tMax = new ArrayList<>();

        //utowrzenie serii danych
        XYChart.Series<Number, Number> uSeries = new XYChart.Series();
        XYChart.Series<Number, Number> vSeries = new XYChart.Series();
        XYChart.Series<Number, Number> iSeries = new XYChart.Series();
        vSeries.setName("Potencjał błony neuronu");
        uSeries.setName("Zmienna odpwoiadajaca za powrót do stanu spoczynkowego");
        iSeries.setName("Natężenie prądu");


        for (int i = 0; i < uValues.size(); i++) {
//dodanie wartosci do serii danych
            if (time.get(i) > 7.5) iSeries.getData().add(new XYChart.Data<>(time.get(i), I));
            else iSeries.getData().add(new XYChart.Data<>(time.get(i), 0));
            uSeries.getData().add(new XYChart.Data<>(time.get(i), uValues.get(i)));
            vSeries.getData().add(new XYChart.Data<>(time.get(i), vValues.get(i)));

            //jesli wartosci są iglicami(czyli ponad 30 - są niewiele ponad) to dodajemy do list
            if (vValues.get(i) > 30) {
                vMax.add(vValues.get(i));
                tMax.add(time.get(i));
            }

        }

        //zmienne na wartosci ktore wyliczamy
        double sum = 0;
        double mean = 0;
        double max = vMax.get(0);
        double std = 0;
        ArrayList<Double> times = new ArrayList<>();

        for (int i = 0; i < vMax.size(); i++) {

            if (vMax.get(i) > max) max = vMax.get(i); //wyznaczanie wartosci maksymalnej

            sum += vMax.get(i); //oblicxanie sumy do sredniej

            if (i > 0) {
                times.add(tMax.get(i) - tMax.get(i - 1)); //obliczanie czasu pomiedzy iglicami
            }
        }
        mean = sum / vMax.size(); //obliczanie sredniej

        double ssum = 0;
        double stdSum = 0;

        for (int i = 0; i < vMax.size(); i++) {
            stdSum += Math.pow(vMax.get(i) - mean, 2); //obliczanie sumy do odchylenia standardowego

            if (i < vMax.size() - 1) ssum += times.get(i); //obliczanie sumy do czestotliwosci
        }
        std = Math.pow((stdSum / (vMax.size() - 1)), 0.5); //obliczanie odchylenia standardowego

        //obliczanie częstotliwości
        double T1 = te / vMax.size();
        double f1 = 1 / T1;
        double T2 = ssum / times.size();
        double f2 = 1 / T2;

        //tworzenie Stringa do wyswietlenia w TextArea
        String stats = "Częstotliwość gen. iglic (z ilości poków w czasie) = " + f1 + "\n" + "Maksymalny potencjał iglicy = " + max + "\n"
                + "Maksymalny potencjał iglicy wyznaczany ze średniej = " + mean + "\n" +
                "Odchylenie standardowe = " + std + "\n" +
                "Częstotliwość gen. iglic (z czasów pomiedzy iglicami) = " + String.format("%.4f", f2) + "\n"
                + "Czas między maksymalnymi amplitudami = ";

        for (int i = 0; i < times.size(); i++) {
            stats += "\n" + String.format("%.2f", times.get(i));
        }

        textArea.setText(stats); //wyswietelnie w text area

        //dodanie serii do wykresu i opisanie osi
        vChart.getData().add(vSeries);
        uChart.getData().add(uSeries);
        IChart.getData().add(iSeries);

        yAxisiChart.setTickUnit(1);
        yAxisiChart.setAutoRanging(true);
        xAxisiChart.setTickUnit(1);
        xAxisiChart.setAutoRanging(true);
        yAxisiChart.setLabel("I");
        xAxisiChart.setLabel("t");

        yAxisUChart.setTickUnit(1);
        yAxisUChart.setAutoRanging(true);
        xAxisuChart.setTickUnit(1);
        xAxisuChart.setAutoRanging(true);
        yAxisUChart.setLabel("u");
        xAxisuChart.setLabel("t");

        yAxisvChart.setTickUnit(1);
        yAxisvChart.setAutoRanging(true);
        xAxisvChart.setTickUnit(1);
        xAxisvChart.setAutoRanging(true);
        yAxisvChart.setLabel("v");
        xAxisvChart.setLabel("t");

    }


}
