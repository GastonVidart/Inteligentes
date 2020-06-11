package RedNeuronalv2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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
     *
     * @throws java.lang.Exception : si existe una tupla con un numero
     * incorrecto de entradas a las recibidas por la red
     */
    public void gradiantDescent(double learningRate, double[][] datosTraining) throws Exception {
        for (double[] dato : datosTraining) {
            double[][] sumasPonderadas = new double[capas.length][], //sumasPonderadas[capa][nodo]
                    salidasNodos = new double[capas.length + 1][];//salidasNodos[capa][nodo]

            //Forward Pass
            //comienzo con las entradas
            salidasNodos[0] = dato;

            //recorro cada capa
            for (int i = 0; i < capas.length; i++) {
                //calculo las sumas ponderadas
                try {
                    sumasPonderadas[i] = matrizPorVector(capas[i].w, salidasNodos[i], capas[i].b);
                } catch (Exception ex) {
                    throw new Exception("Uno de los valores tiene incorrecto numero de entradas");
                }
                //calculo las funciones sigmoide
                salidasNodos[i + 1] = new double[sumasPonderadas[i].length];
                for (int j = 0; j < salidasNodos[i + 1].length; j++) {
                    salidasNodos[i + 1][j] = funcionSigmoide(sumasPonderadas[i][j]);
                }
            }
            //Back Propagation
            backPropagation(dato, sumasPonderadas, salidasNodos, learningRate);
        }
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
                    * funcionCoste(valorSalida, salidasNodos[ultimaCapa + 1][i]);
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

    /**
     * Multiplica la matriz recibida por el vector, y le adiciona valorIni al
     * resultado
     *
     * @param matriz : matriz de entrada. Su cantidad de columnas debe ser igual
     * a la cantidad de filas de vector y su cantidad de filas debe ser igual a
     * la longitud de valorIni
     *
     * @param vector : vector de valores. Su longitud debe ser igual a la
     * cantidad de columas de matriz
     *
     * @param valorIni : valor a adicionar para cada columna del vector
     * resultado. Su longitud debe ser igual a la cantidad de filas de matriz.
     *
     * @return un vector resultado
     *
     * @throws Exception si no coincide la cantidad de columnas con la longitud
     * del vector o de valorIni
     */
    public void gradiantDescent1(double learningRate, double[][] datosTraining) {
        //las salidas tienen que estar mapeadas de 0 a n
        //obtengo la cant de nodos de la capa de salida
        int cantSalidas = this.capas[this.capas.length - 1].b.length;

        //datosTraining [dato][atributo] -> atributo[entradas...,salida]
        double[][] matrizSalida = generarMatrizSalida(datosTraining, cantSalidas);//TODO: Reemplazar matriz salida

        //Gradiant Descent 
        for (int e = 0; e < datosTraining.length; e++) {

            //sumas[capa][nodo], suma ponderada
            double[][] sumas = new double[capas.length][];

            //forward pass
            //patrones[capa][nodo], incluye la capa de entrada
            double[][] patrones = new double[capas.length + 1][];   //datos de entrada

            /*for (int i = 0; i < datosTraining[e].length-1; i++) {
                aux[i]=datosTraining[e][i]/13.0;   //TODO: cambiar             
            }*/
            patrones[0] = datosTraining[e];

            for (int i = 0; i < this.capas.length; i++) {
                //System.out.println("Se revisa la capa " + i);

                sumas[i] = new double[capas[i].b.length];
                patrones[i + 1] = new double[capas[i].b.length];    //arreglo de a de la capa actual mientras se calcula

                //recorre cada nodo de la capa
                for (int j = 0; j < capas[i].b.length; j++) {
                    //System.out.println("\tSe revisa el nodo " + j + " que tiene bias " + capas[i].b[j]);
                    sumas[i][j] = capas[i].b[j];    //inicializa la suma con el bias

                    //recorre cada arco del nodo
                    for (int k = 0; k < capas[i].w[j].length; k++) {
                        //System.out.println("\t\tSe revisa el arco " + k + " que tiene peso " + capas[i].w[j][k]);
                        //se multiplica cada peso con el valor de la capa anterior
                        //  la cant de arcos de entrada de un nodo es igual a la cantidad de nodos de la capa anterior
                        sumas[i][j] += capas[i].w[j][k] * patrones[i][k];
                    }
                    patrones[i + 1][j] = funcionSigmoide(sumas[i][j]); //se acutalizan las activaciones
                    //System.out.println("\t-> El nodo tiene resultado " + patrones[i + 1][j]);
                }
            }

//            System.out.print("Las salidas fueron ");
//            for (int i = 0; i < capas[capas.length - 1].b.length; i++) {
//                System.out.print(redondearDecimales(patrones[capas.length][i],2) + " | ");
//            }
//            System.out.println();
            //backward pass
            //delta [capa][delta del nodo]
            double[][] deltas = new double[capas.length][];
            int cantNodosUltimaCapa = capas[capas.length - 1].b.length;
            int idUltimaCapa = capas.length - 1;

            //calculo el delta de la ultima capa            
            deltas[idUltimaCapa] = new double[cantNodosUltimaCapa];
            for (int i = 0; i < cantNodosUltimaCapa; i++) {
                //System.out.println(sumas[idUltimaCapa][i]);
                deltas[idUltimaCapa][i] = funcionSigmoideDerivada(sumas[idUltimaCapa][i])
                        * funcionCoste(matrizSalida[e][i], patrones[idUltimaCapa + 1][i]);
//                if (e == 0) {
//                    System.out.println("Salida: " + matrizSalida[e][i] + "Obtenida: " + patrones[idUltimaCapa + 1][i] + "Coste: " + funcionCoste(matrizSalida[e][i], patrones[idUltimaCapa + 1][i]) + " | ");
//                }
                //System.out.print(patrones[idUltimaCapa + 1][i]+" | ");
            }
//            System.out.println("");

            //calculo los deltas de las capas ocultas
            for (int i = capas.length - 2; i >= 0; i--) {

                deltas[i] = new double[capas[i].b.length];
                //recorro los nodos de la capa
                for (int j = 0; j < capas[i].b.length; j++) {

                    double sumatoriaCorreccion = 0;
                    for (int k = 0; k < capas[i + 1].b.length; k++) {
                        //accedo a los pesos que estan afectados por el nodo actual (es j)
                        sumatoriaCorreccion += capas[i + 1].w[k][j] * deltas[i + 1][k];

                    }
                    deltas[i][j] = funcionSigmoideDerivada(sumas[i][j]) * sumatoriaCorreccion;
                }
            }

            //actualizo los pesos y los b
            //recorro todas las capas
            for (int i = 0; i < capas.length; i++) {

                //recorro todos los nodos
                for (int j = 0; j < capas[i].w.length; j++) {
                    capas[i].b[j] += learningRate * deltas[i][j];
                    //recorro todos sus pesos
                    for (int k = 0; k < capas[i].w[j].length; k++) {
                        capas[i].w[j][k] += learningRate * patrones[i][k] * deltas[i][j];
                    }
                }
            }
        }
        this.toJson();
    }

    public static double redondearDecimales(double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado = (resultado - parteEntera) * Math.pow(10, numeroDecimales);
        resultado = Math.round(resultado);
        resultado = (resultado / Math.pow(10, numeroDecimales)) + parteEntera;
        return resultado;
    }

    //Testing
    /**
     * @param datosTesting : los datos para realizar testing
     * @return valor que representa el porcentaje de aciertos de la red
     */
    public double testRed(double[][] datosTesting) throws Exception {
        double aciertos = 0;
        for (double[] dato : datosTesting) {
            double[] salidasNodos = dato;

            for (Capa capa : this.capas) {
                //calculo las sumas ponderadas
                double[] sumasPonderadas;
                try {
                    sumasPonderadas = matrizPorVector(capa.w, salidasNodos, capa.b);
                } catch (Exception ex) {
                    throw new Exception("Uno de los valores tiene incorrecto numero de entradas");
                }
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
        }

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
    private static double funcionSigmoide(double n) {
        return 1 / (1 + Math.exp(-n));
    }

    private static double funcionSigmoideDerivada(double n) {
        return 1 / (Math.exp(n) * Math.pow(1 + Math.exp(-n), 2));
        //return funcionSigmoide(n)*(1.0-funcionSigmoide(n));
    }

    private double funcionCoste(double esperado, double obtenido) {
        return esperado - obtenido;
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

    private double[] matrizPorVector(double[][] matriz, double[] vector, double[] valorIni) throws Exception {
        if (matriz[0].length != vector.length && matriz.length != valorIni.length) {
            throw new Exception("La cantidad de columnas de la matriz no coincide con la longitud del vector");
        }
        double[] res = new double[matriz.length];

        for (int i = 0; i < matriz.length; i++) {
            res[i] = valorIni[i];
            for (int j = 0; j < vector.length; j++) {
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
            }
            b[i] = r.nextDouble();
        }
    }

    Capa(double[] b, double[][] w) {
        this.b = b;
        this.w = w;
    }
}
