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
        for (int e = 0; e < datosTraining.length; e++) {
            
            //forward pass
            
            //backward pass
            
            
            
            

        }

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
        for (double[] nodo : w) {
            for (double peso : nodo) {
                peso = r.nextDouble();
            }
        }

        for (double bias : b) {
            bias = r.nextDouble();
        }
    }
}
