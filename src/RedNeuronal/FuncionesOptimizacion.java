package RedNeuronal;

// @author guido
public class FuncionesOptimizacion {

    static void gradientDescent(RedNeuronal red, double[][] matrizEntrada,
            double[][] matrizSalida, double learningRate, int funcion, double[][] matrizTesting) {
        System.out.println("Comienza a entrenar");
        for (int e = 0; e < matrizEntrada.length; e++) {
            //Forward
            System.out.println("Hace Forward");
            for (int i = 0; i < red.capaEntrada.length; i++) {
                for (Arco arco : red.capaEntrada[i]) {
                    arco.setPatron(matrizEntrada[e][i]);
                }
            }

            for (Nodo[] capa : red.capasOcultas) {
                for (Nodo nodo : capa) {
                    try {
                        nodo.calcularEntradaNeta();
                        nodo.pasarSalida(funcion);
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
            System.out.println("Hace Backwardying");
            double[][] deltas = new double[red.capasOcultas.length + 1][];
            //calcular delta
            //capa de salida
            deltas[deltas.length - 1] = new double[red.capaSalida.length];
            for (int i = 0; i < red.capaSalida.length; i++) {
                try {
                    deltas[deltas.length - 1][i]
                            = red.capaSalida[i].obtenerDerivada(funcion) * funcionCoste(matrizSalida[e][i], salidas[i]);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
            //capas ocultas
            for (int i = red.capasOcultas.length - 1; i >= 0; i--) {
                deltas[i] = new double[red.capasOcultas[i].length];
                for (int j = 0; j < red.capasOcultas[i].length; j++) {
                    try {
                        deltas[i][j]
                                = red.capaSalida[i].obtenerDerivada(funcion) * red.capaSalida[i].calcularCosteNodo(deltas[i + 1]);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }

            }
            //corregir pesos
            //capa de salida
            for (int i = 0; i < red.capaSalida.length; i++) {
                red.capaSalida[i].corregirPesos(deltas[deltas.length - 1][i], learningRate);
            }
            //capas ocultas
            for (int i = red.capasOcultas.length - 1; i >= 0; i--) {
                for (int j = 0; j < red.capasOcultas[i].length; j++) {
                    red.capasOcultas[i][j].corregirPesos(deltas[i][j], learningRate);
                }
            }

        }
        //Testing
        System.out.println("Hace Testing");
        double aciertos = 0;
        for (double[] tupla : matrizTesting) {
            double[] entradas = new double[tupla.length - 1];
            System.arraycopy(tupla, 0, entradas, 0, entradas.length);
            int index = 0;
            try {
                index = red.obtenerSalida(entradas);
            } catch (Exception ex) {
                System.out.println(ex);
            }
            aciertos += (index == tupla[tupla.length - 1]) ? 1 : 0;
        }
        System.out.println("->El porcentaje de error es de %" + aciertos / matrizTesting.length);
    }

    public static double funcionCoste(double esperado, double obtenido) {
        return esperado - obtenido;
    }
}
