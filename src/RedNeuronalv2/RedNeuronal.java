package RedNeuronalv2;

import java.util.Random;

public class RedNeuronal {

    Capa[] capas;

    //constructor
    // topologia [entrada,ocultas,salida]
    public RedNeuronal(int[] topologia) {
        int cantCapas = topologia.length - 1;

        this.capas = new Capa[cantCapas]; //no representamos la capa de entrada 
        for (int i = 0; i < cantCapas; i++) {
            this.capas[i] = new Capa(topologia[i + 1], topologia[i]);
        }
    }

    //optimizacion
    public void gradiantDescent(double learningRate, double[][] datosTraining) {
        //las salidas tienen que estar mapeadas de 0 a n;
        //obtengo la cant de nodos de la capa de salida
        int cantSalidas = this.capas[this.capas.length - 1].b.length;

        //Separa datosTraining en matrizEntrada y matrizSalida        
        //      datosTraining [dato][atributo] -> atributo[entradas...,salida]
        double[][] matrizEntrada = new double[datosTraining.length][datosTraining[0].length - 1];
        double[][] matrizSalida = new double[datosTraining.length][cantSalidas];

        for (int i = 0; i < matrizSalida.length; i++) {
            System.arraycopy(datosTraining[i], 0, matrizEntrada[i], 0, datosTraining[i].length - 1);
            for (int j = 0; j < cantSalidas; j++) {
                matrizSalida[i][j] = (datosTraining[i][datosTraining[i].length - 1] == j) ? 1 : 0;
            }
        }

        //Gradiant Descent 
        for (int e = 0; e < matrizEntrada.length; e++) {
            /*int e = 0;
        for (int z = 0; z < 100000; z++) {*/

            //sumas[capa][nodo], suma ponderada
            double[][] sumas = new double[capas.length][];

            //forward pass
            //patrones[capa][nodo], incluye la capa de entrada
            double[][] patrones = new double[capas.length + 1][];   //datos de entrada
            patrones[0] = matrizEntrada[e];

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

            System.out.print("Las salidas fueron ");
            for (int i = 0; i < capas[capas.length - 1].b.length; i++) {
                System.out.print(redondearDecimales(patrones[capas.length][i],2) + " | ");
            }
            System.out.println();

            //backward pass
            //delta [capa][delta del nodo]
            double[][] deltas = new double[capas.length][];
            int cantNodosUltimaCapa = capas[capas.length - 1].b.length;
            int idUltimaCapa = capas.length - 1;

            //calculo el delta de la ultima capa            
            deltas[idUltimaCapa] = new double[cantNodosUltimaCapa];
            for (int i = 0; i < cantNodosUltimaCapa; i++) {
                //System.out.println(sumas[idUltimaCapa][i]);
                deltas[idUltimaCapa][i] = funcionSigmoideDerivada(sumas[idUltimaCapa][i]) * funcionCoste(matrizSalida[e][i], patrones[idUltimaCapa + 1][i]);
            }

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

                    //recorro todos sus pesos
                    for (int k = 0; k < capas[i].w[j].length; k++) {
                        capas[i].w[j][k] = capas[i].w[j][k] + learningRate * patrones[i][k] * deltas[i][j];
                    }
                }
            }
        }
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

    //testing
    //exportado    
    //funciones extra
    private static double funcionSigmoide(double n) {
        return 1 / (1 + Math.exp(-n));
    }

    private static double funcionSigmoideDerivada(double n) {
        return 1 / (Math.exp(n) * Math.pow(1 + Math.exp(-n), 2));
    }

    private double funcionCoste(double esperado, double obtenido) {
        return esperado - obtenido;
    }
}

class Capa {

    // w[nodo de la capa][peso del arco] | pesos
    double[][] w;
    // b[nodo de la capa] | bias
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
}
