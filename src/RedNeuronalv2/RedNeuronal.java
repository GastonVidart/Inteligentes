package RedNeuronalv2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Random;

public class RedNeuronal {

    Capa[] capas;
    private String name;    

    //Constructor
    /**
     * @param topologia : [entrada,ocultas,...,ocultas,salida]
     * @param name : nombre con el cual se creara la representación de la red
     */
    public RedNeuronal(int[] topologia, String name) {
        int cantCapas = topologia.length - 1;

        this.capas = new Capa[cantCapas];
        for (int i = 0; i < cantCapas; i++) {
            this.capas[i] = new Capa(topologia[i + 1], topologia[i]);
        }
        this.name = name;
    }

    //Optimizacion
    /**
     * Entrena una red mediante gradiant descent con un conjunto de datos de
     * entrenamiento.
     *
     * @param learningRate : valor que indica la potencia de entrenamiento.
     * Valores bajos son mas lentos y precisos, valores altos son mas rápidos y
     * poco precisos
     *
     * @param datosTraining : matriz de datos de entrenamiento. Cada fila es una
     * tupla diferente, las primeras [n - 1] son entradas y la columna [n] es la
     * salida. La salida debe ser un arreglo con valores de 0 a N (con N siendo
     * igual a la cantidad de salidas en la topologia).
     */
    public void gradiantDescent(double learningRate, double[][] datosTraining) {
        int cantSalidas = this.capas[this.capas.length - 1].b.length;
        int ultimaCapa = capas.length - 1;
        double costo = 0.0;
        for (double[] dato : datosTraining) {
            double[][] sumasPonderadas = new double[capas.length][], //sumasPonderadas[capa][nodo]
                    salidasNodos = new double[capas.length + 1][];//salidasNodos[capa][nodo]

            //Forward Pass
            //comienzo con las entradas
            salidasNodos[0] = dato;

            //recorro cada capa
            for (int i = 0; i < capas.length; i++) {
                //calculo las sumas ponderadas
                sumasPonderadas[i] = matrizPorVector(capas[i].w, salidasNodos[i], capas[i].b);
                //calculo las funciones sigmoide
                salidasNodos[i + 1] = new double[sumasPonderadas[i].length];
                for (int j = 0; j < salidasNodos[i + 1].length; j++) {
                    salidasNodos[i + 1][j] = funcionSigmoide(sumasPonderadas[i][j]);
                }
            }
            //calculo el error que tiene la red

            double[] esperado = generarArregloEsperados(dato, cantSalidas);
            costo += funcionCoste(esperado, salidasNodos[ultimaCapa + 1]);

            //Back Propagation
            backPropagation(dato, sumasPonderadas, salidasNodos, learningRate);
        }
        toJson();
        System.out.println("Error de la red: " + costo / datosTraining.length);
    }

    private void backPropagation(double[] dato, double[][] sumasPonderadas, double[][] salidasNodos, double learningRate) {
        int cantSalidas = capas[capas.length - 1].b.length;
        double[][] deltas = new double[capas.length][];//deltas[capa][nodo]

        int ultimaCapa = capas.length - 1;

        //calculo los deltas de la ultima capa            
        deltas[ultimaCapa] = new double[cantSalidas];

        for (int i = 0; i < cantSalidas; i++) {
            double valorSalida = (dato[dato.length - 1] != i) ? 0 : 1;
            deltas[ultimaCapa][i] = funcionSigmoideDerivada(sumasPonderadas[ultimaCapa][i])
                    * funcionCosteDerivada(valorSalida, salidasNodos[ultimaCapa + 1][i]);
        }

        //calculo los deltas de las capas ocultas
        for (int i = capas.length - 2; i >= 0; i--) {
            deltas[i] = new double[capas[i].b.length];

            //recorro los nodos de la capa
            for (int j = 0; j < capas[i].b.length; j++) {
                double sumatoriaCorreccion = 0;

                //recorro los arcos de SALIDA del nodo 
                for (int k = 0; k < capas[i + 1].b.length; k++) {

                    //accedo a los pesos que estan afectados por el nodo actual (el nodo actual es j)
                    sumatoriaCorreccion += capas[i + 1].w[k][j] * deltas[i + 1][k];

                }
                deltas[i][j] = funcionSigmoideDerivada(sumasPonderadas[i][j]) * sumatoriaCorreccion;
            }
        }

        //actualizo los pesos y los b
        for (int i = 0; i < capas.length; i++) {
            //recorro todos los nodos
            for (int j = 0; j < capas[i].w.length; j++) {
                capas[i].b[j] += learningRate * deltas[i][j];
                //recorro todos sus arcos
                for (int k = 0; k < capas[i].w[j].length; k++) {
                    capas[i].w[j][k] += learningRate * salidasNodos[i][k] * deltas[i][j];
                }
            }
        }
    }

    //Testing
    /**
     * @param datosTesting : los datos para realizar testing
     * @return valor que representa el porcentaje de aciertos de la red
     */
    public double testRed(double[][] datosTesting) {
        double aciertos = 0;
        int cantSalidas = this.capas[this.capas.length - 1].b.length;
        int ultimaCapa = capas.length - 1;
        double costo = 0.0;
        for (double[] dato : datosTesting) {
            double[] salidasNodos = dato;

            for (Capa capa : this.capas) {
                //calculo las sumas ponderadas
                double[] sumasPonderadas;
                sumasPonderadas = matrizPorVector(capa.w, salidasNodos, capa.b);
                //calculo las funciones sigmoide
                double[] salidasNodosAux = new double[sumasPonderadas.length];
                for (int j = 0; j < salidasNodosAux.length; j++) {
                    salidasNodosAux[j] = funcionSigmoide(sumasPonderadas[j]);
                }
                salidasNodos = salidasNodosAux;
            }

            //verifico si fue un acierto
            double max = -1.0, indiceMax = -1.0;
            for (int i = 0; i < salidasNodos.length; i++) {
                if (max < salidasNodos[i]) {
                    indiceMax = i;
                    max = salidasNodos[i];
                }
            }
            aciertos += (dato[dato.length - 1] == indiceMax) ? 1 : 0;

            //calculo el error que tiene la red
            double[] esperado = generarArregloEsperados(dato, cantSalidas);
            costo += funcionCoste(esperado, salidasNodos);
        }
        System.out.println("Error de la red Testing: " + costo / datosTesting.length);
        return aciertos / datosTesting.length;
    }

    //Conversion
    public void toJson() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String red2 = gson.toJson(this);
        String NOMBRE_ARCHIVO = "src/salidas/" + name + ".txt";
        try (PrintWriter flujoDeSalida = new PrintWriter(new FileOutputStream(NOMBRE_ARCHIVO))) {
            flujoDeSalida.print(red2);
            System.out.println("Se imprimio correctamente en: " + NOMBRE_ARCHIVO);
        } catch (FileNotFoundException ex) {
            System.err.println("Archivo no encontrado");
        }
    }

    //Funciones Extra
    private static double funcionRelu(double n) {
        return (n <= 0) ? 0 : n;
    }

    private static double funcionReluDerivada(double n) {
        return (n <= 0) ? 0 : 1;
        //return funcionSigmoide(n)*(1.0-funcionSigmoide(n));
    }

    private static double funcionSigmoide(double n) {
        return 1 / (1 + Math.exp(-n));
    }

    private static double funcionSigmoideDerivada(double n) {
        return 1 / (Math.exp(n) * Math.pow(1 + Math.exp(-n), 2));
        //return funcionSigmoide(n)*(1.0-funcionSigmoide(n));
    }

    private double funcionCoste(double[] esperado, double[] predicho) {
        double costo = 0.0;
        for (int i = 0; i < predicho.length; i++) {
            costo += Math.pow(predicho[i] - esperado[i], 2);
        }
        return costo;
    }

    private double funcionCosteDerivada(double esperado, double obtenido) {
        return esperado - obtenido;
        //return obtenido-esperado;
    }

    private double[][] generarMatrizSalida(double[][] datos, int cantSalidas) {
        double[][] matrizSalida = new double[datos.length][cantSalidas];
        for (int i = 0; i < matrizSalida.length; i++) {
            for (int j = 0; j < cantSalidas; j++) {
                matrizSalida[i][j] = (datos[i][datos[i].length - 1] == j) ? 1 : 0;
            }
        }
        return matrizSalida;
    }

    private double[] generarArregloEsperados(double[] dato, int cantSalidas) {
        double[] arr = new double[cantSalidas];
        for (int j = 0; j < cantSalidas; j++) {
            arr[j] = (dato[dato.length - 1] == j) ? 1 : 0;
        }
        return arr;
    }

    private double[] matrizPorVector(double[][] matriz, double[] vector, double[] valorIni) {
        double[] res = new double[matriz.length];

        for (int i = 0; i < matriz.length; i++) {
            res[i] = valorIni[i];
            for (int j = 0; j < matriz[i].length; j++) {
                res[i] += matriz[i][j] * vector[j];
            }
        }

        return res;
    }

}

class Capa {

    // w[nodo][peso] | pesos
    double[][] w;
    // b[nodo] | bias
    double[] b;

    Capa(int cantNodos, int cantArcos) {

        this.w = new double[cantNodos][cantArcos];
        this.b = new double[cantNodos];

        //inicializacion random
        Random r = new Random();
        for (int i = 0; i < w.length; i++) {
            for (int j = 0; j < w[i].length; j++) {
                w[i][j] = r.nextDouble();
                //w[i][j] = 0.5;
            }
            b[i] = r.nextDouble();
            //b[i] = 1;
        }
    }

    Capa(double[] b, double[][] w) {
        this.b = b;
        this.w = w;
    }
}
