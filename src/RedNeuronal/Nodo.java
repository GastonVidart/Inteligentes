package RedNeuronal;

// @author guido
import java.util.ArrayList;

public class Nodo {

    private double b = 0; //polarizacion
    private ArrayList<Arco> arcosEntrada = new ArrayList<>(),
            arcosSalida = new ArrayList<>();

    public double calcularEntradaNeta() {
        double n = 0;
        n = arcosEntrada.stream().map((arco) -> arco.getValor()).reduce(n, (accumulator, _item) -> accumulator + _item);
        return n + b;
    }

    public void setSalidaArcos(double n, int funcionActivacion) throws Exception {
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

    //funciones de activacion
    private double funcionSigmoide(double n) {
        return 1 / (1 + Math.exp(-n));
    }
}
