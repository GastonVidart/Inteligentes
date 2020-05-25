package RedNeuronal;

// @author guido
import java.util.ArrayList;

public class RedNeuronal {

    private ArrayList<Nodo>[] capasOcultas;//TODO: puede ser matriz
    private Arco[] capaEntrada;
    private Arco[] capaSalida;
    private String[] valorSalida;

    public static final int ESCALON = 0, ESCSIMETRICO = 1, LINEAL = 2, SIGMOIDE = 3;
    
    public String obtenerSalida(double[] valoresEntrada, int funcion) throws Exception {
        for (int i = 0; i < capaEntrada.length; i++) {
            capaEntrada[i].setPatron(valoresEntrada[i]);
        }
        for (ArrayList<Nodo> capa : capasOcultas) {
            for (Nodo nodo : capa) {
                nodo.setSalidaArcos(nodo.calcularEntradaNeta(), funcion);
            }
        }
        double max = Integer.MIN_VALUE;
        int indiceMax = -1;
        for (int i = 0; i < capaSalida.length; i++) {
            if(max < capaSalida[i].getValor()){
                indiceMax = i;
                max = capaSalida[i].getValor();
            }
        }
        return valorSalida[indiceMax];
    }
}
