package RedNeuronal;

// @author guido
import java.util.ArrayList;

public class Nodo {

    private double b = 1; //polarizacion (bias)
    private ArrayList<Arco> arcosEntrada = new ArrayList<>(),
            arcosSalida = new ArrayList<>();

    
    
    public double calcularEntradaNeta() {
        //Calcula la suma ponderada
        double n = 0;
        n = arcosEntrada.stream().map((arco) -> arco.getValor()).reduce(n, (accumulator, _item) -> accumulator + _item);
        return n + b;
    }

    public void pasarSalida(double n, int funcionActivacion) throws Exception {
        //Pasa la salida a los arcos siguientes
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

    //funciones de activacion
    private double funcionSigmoide(double n) {
        return 1 / (1 + Math.exp(-n));
    }

    void addArcoEntrada(Arco arco) {
        arcosEntrada.add(arco);
    }

    void addArcoSalida(Arco arco) {
        arcosSalida.add(arco);
    }
}