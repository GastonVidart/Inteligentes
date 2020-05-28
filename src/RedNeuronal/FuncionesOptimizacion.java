package RedNeuronal;

// @author guido
public class FuncionesOptimizacion {

    static void gradientDescent(RedNeuronal red, double[][] matrizEntrada,
            double[][] matrizSalida, double learningRate, int funcion) {

        for (int e = 0; e < matrizEntrada.length; e++) {
            //Forward
            for (int i = 0; i < red.capaEntrada.length; i++) {
                for (Arco arco : red.capaEntrada[i]) {
                    arco.setPatron(matrizEntrada[e][i]);
                }
            }

            for (Nodo[] capa : red.capasOcultas) {
                for (Nodo nodo : capa) {
                    try {
                        nodo.pasarSalida(nodo.calcularEntradaNeta(), funcion);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }
            double[] salidas = new double[matrizSalida[e].length];
            for (int i = 0; i < red.capaSalida.length; i++) {
                try {
                    salidas[i] = red.capaSalida[i].obtenerSalida(red.capaSalida[i].calcularEntradaNeta(), funcion);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
            //Backward
        }
    }

}
