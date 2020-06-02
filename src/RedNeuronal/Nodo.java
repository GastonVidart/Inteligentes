package RedNeuronal;

// @author Guido, Gaston y Seba
import java.util.ArrayList;

public class Nodo {

    private double b = 1; //polarizacion (bias)
    private ArrayList<Arco> arcosEntrada = new ArrayList<>(),
            arcosSalida = new ArrayList<>();
    private double n = 0, a = 0;

    public double calcularEntradaNeta() {
        //Calcula la suma ponderada
        n = arcosEntrada.stream().map((arco) -> arco.getValor()).reduce(n, (accumulator, _item) -> accumulator + _item);
        return n + b;
    }

    public double calcularCosteNodo(double[] deltas) {
        double sum = 0;
        for (int i = 0; i < arcosSalida.size(); i++) {
            sum += deltas[i] * arcosSalida.get(i).getPeso();
        }
        return sum;
    }
        
    public void pasarSalida(int funcionActivacion) throws Exception {
        //Pasa la salida a los arcos siguientes
        switch (funcionActivacion) {
            case RedNeuronal.ESCALON:
                a = n >= 0 ? 1 : 0;
                break;
            case RedNeuronal.ESCSIMETRICO:
                a = n >= 0 ? 1 : -1;
                break;
            case RedNeuronal.LINEAL:
                a = n;
                break;
            case RedNeuronal.SIGMOIDE:
                a = funcionSigmoide(n);
                break;
            default:
                throw new Exception("Valor de funcion incorecto");
        }
        arcosSalida.forEach((arco) -> {
            arco.setPatron(a);
        });
    }

    public double obtenerSalida(double n, int funcionActivacion) throws Exception {
        //Obtiene la salida
        double a;
        switch (funcionActivacion) {
            case RedNeuronal.ESCALON:
                a = n >= 0 ? 1 : 0;
                break;
            case RedNeuronal.ESCSIMETRICO:
                a = n >= 0 ? 1 : -1;
                break;
            case RedNeuronal.LINEAL:
                a = n;
                break;
            case RedNeuronal.SIGMOIDE:
                a = funcionSigmoide(n);
                break;
            default:
                throw new Exception("Valor de funcion incorecto");
        }
        return a;
    }

    public double obtenerDerivada(int funcionActivacion) throws Exception {
        //Obtiene la salida
        double a;
        switch (funcionActivacion) {
            case RedNeuronal.LINEAL:
                a = 1;
                break;
            case RedNeuronal.SIGMOIDE:
                a = funcionSigmoideDerivada(n);
                break;
            default:
                throw new Exception("Valor de funcion incorecto");
        }
        return a;
    }

    void corregirPesos(double delta, double learningRate) {
        arcosEntrada.forEach((arco) -> {
            arco.corregirPeso(delta, learningRate);
        });
        b = b + learningRate * delta;
    }
    //funciones de activacion
    private double funcionSigmoide(double n) {
        return 1 / (1 + Math.exp(-n));
    }

    private double funcionSigmoideDerivada(double n) {
        return 1 / (Math.exp(n) * Math.pow(1 + Math.exp(-n), 2));
    }

    void addArcoEntrada(Arco arco) {
        arcosEntrada.add(arco);
    }

    void addArcoSalida(Arco arco) {
        arcosSalida.add(arco);
    }
}
