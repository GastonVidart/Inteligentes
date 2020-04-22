
import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 *
 */
class Solution {

    public static int N;

    static int[] original;
    static final int CANTPOB = 64, CANTIT = 4000, IGUALESHASTAPARAR = 10;
    static int valorAnterior = Integer.MAX_VALUE, contadorValorAnterior, iteracionAcutal;
    static Ind ganador = null;
    static ArrayList<Integer> listaPos;

    public static void main(String args[]) {
        Ind[] poblacion = new Ind[CANTPOB];

        //----
        Scanner in = new Scanner(System.in);
        N = in.nextInt();   //cant
        //----

        original = new int[N];
        for (int i = 0; i < N; i++) { //in
            original[i] = in.nextInt();
        }

        //---------INICIALIZAR POBLACION
        listaPos = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            listaPos.add(i);
        }
        for (int i = 0; i < CANTPOB; i++) {
            poblacion[i] = new Ind();
            poblacion[i].iniGenotipo();
        }

        //---PROCESO DE EVOLUCIÓN y MECANISMO DE CIERE---
        contadorValorAnterior = 0;
        for (iteracionAcutal = 1; iteracionAcutal <= CANTIT; iteracionAcutal++) {

            //--------CALCULAR FITNESS
            int menor = Integer.MAX_VALUE;
            Ind indMenor = null;
            for (int i = 0; i < CANTPOB; i++) {
                poblacion[i].calcFitnes(original);
                if (poblacion[i].fitness == 0) {
                    ganador = poblacion[i];
                    break;
                } else if (menor > poblacion[i].fitness) {
                    menor = poblacion[i].fitness;
                    indMenor = poblacion[i];
                }
            }
            if (menor != valorAnterior) {
                valorAnterior = menor;
                contadorValorAnterior = 0;
            } else {
                contadorValorAnterior++;
                if (contadorValorAnterior == IGUALESHASTAPARAR) {
                    ganador = indMenor;
                    break;
                }
            }

            //-------SELECCION
            ArrayList<Ind> padresE;
            padresE = seleccionRuleta(poblacion, CANTPOB / 2);
//            padresE = seleccionTorneo(poblacion, 4, 2);

            //-------CROSSOVER
            ArrayList<Ind> hijosN;
            hijosN = crossOver(padresE);

            //-------MUTACION
            hijosN = mutacion(hijosN);

            //-------REINSERCION
//            poblacion = reinsercionPura(poblacion, hijosN, padresE);
//            poblacion = reinsercionRancia(hijosN, padresE);
//            poblacion = reinsercionUniforme(hijosN, poblacion);
            poblacion = reinsercionElitista(hijosN, poblacion);
        }

        if (ganador == null) {
            for (int i = 0; i < CANTPOB; i++) {
                poblacion[i].calcFitnes(original);
            }
            ganador = poblacion[0];
            for (int i = 1; i < CANTPOB; i++) {
                if (ganador.fitness > poblacion[i].fitness) {
                    ganador = poblacion[i];
                }
            }
        }

        // Write an answer using System.out.println()
        // To debug: System.err.println("Debug messages...");
        mostrarWinner(ganador);
        System.out.println(ganador.fitness);
    }

    //Selección
    private static ArrayList<Ind> seleccionRuleta(Ind[] poblacion, int cantPadres) {
        //Se invierte el valor de fitnes para que el mejor sea el mayor y se seleccionan desde un segmento
        double[] segmentoSeleccion = new double[CANTPOB];
        double[] fitnessNuevo = new double[CANTPOB];
        boolean[] esSeleccionado = new boolean[CANTPOB];
        double totFitness = 0, totFitnessAux = 0;
        ArrayList<Ind> padresE = new ArrayList<>();

        //Preparacion
        for (int i = 0; i < CANTPOB; i++) {
            totFitnessAux += poblacion[i].fitness;
            esSeleccionado[i] = false;
        }
        for (int i = 0; i < CANTPOB; i++) {
            fitnessNuevo[i] = totFitnessAux - poblacion[i].fitness;
            totFitness += fitnessNuevo[i];
        }

        //Creacion del Segmento
        segmentoSeleccion[0] = ((fitnessNuevo[0] * 100) / totFitness);
        for (int i = 1; i < CANTPOB; i++) {
            segmentoSeleccion[i] = ((fitnessNuevo[i] * 100) / totFitness) + segmentoSeleccion[i - 1];
        }

        //Seleccion de padres
        Random r = new Random();
        int valor;
        for (int i = 0; i < cantPadres; i++) {
            valor = r.nextInt(100);
            for (int j = 0; j < CANTPOB; j++) {
                if (valor < segmentoSeleccion[j]) {
                    if (!esSeleccionado[j]) {
                        padresE.add(poblacion[j]);
                        esSeleccionado[j] = true;
                    } else {
                        i--;
                    }
                    break;
                }
            }
        }

        return padresE;
    }

//    private static ArrayList<Ind> seleccionTorneo(Ind[] poblacion, int cantGrupos, int cantGanadores) {
//        
//    }
    //Mutación
    private static ArrayList<Ind> mutacion(ArrayList<Ind> hijosN) {
        Random r = new Random();
        int maxMutaciones = (CANTIT / iteracionAcutal) + 1;//Siempre toma uno menos, es decir, es hasta maxMutaciones - 1
        hijosN.forEach((hijosN1) -> {
            hijosN1.mutacion(r.nextInt(maxMutaciones));
        });
        return hijosN;
    }

    //Crossover
    private static ArrayList<Ind> crossOver(ArrayList<Ind> padresE) {
        int mitad = padresE.size() / 2;
        ArrayList<Ind> hijosN = new ArrayList<>();

        for (int i = 0; i < mitad; i++) {
            cruzarPadres(padresE.get(i), padresE.get(mitad + i), hijosN);
        }
        return hijosN;
    }

    private static void cruzarPadres(Ind padre, Ind madre, ArrayList<Ind> hijosN) {
        boolean[] geno1 = new boolean[N], geno2 = new boolean[N];
        for (int i = 0; i < N; i++) {
            geno1[i] = padre.genotipo[i];
            geno2[i] = padre.genotipo[i];
        }
        Ind hijo1 = new Ind(geno1),
                hijo2 = new Ind(geno2);
        boolean alternador = true;

        int balance = 0;
        for (int i = 0; i < N; i++) {
            if (padre.genotipo[i] != madre.genotipo[i]) {
                /*Si el valor es distinto, le asigna al primer hijo el mismo valor que el padre y el opuesto al 
                segundo. Luego, en la proxima diferencia, asigna los valores opuestos para balancear el arreglo
                y cambia el alternador, para que (si existe algun otra diferencia) realice el mismo proceso pero 
                esta vez, mirando al segundo hijo
                 */
                if (alternador) {
                    if (balance == 0) {
                        hijo2.genotipo[i] = !padre.genotipo[i];
                        if (padre.genotipo[i]) {
                            balance++;
                        } else {
                            balance--;
                        }
                    } else {
                        if (balance > 0) {
                            hijo1.genotipo[i] = false;
                            hijo2.genotipo[i] = true;
                            balance--;
                        } else {
                            hijo1.genotipo[i] = true;
                            hijo2.genotipo[i] = false;
                            balance++;
                        }
                        alternador = !alternador;
                    }
                } else {
                    if (balance == 0) {
                        hijo1.genotipo[i] = !padre.genotipo[i];
                        if (padre.genotipo[i]) {
                            balance++;
                        } else {
                            balance--;
                        }
                    } else {
                        if (balance > 0) {
                            hijo2.genotipo[i] = false;
                            hijo1.genotipo[i] = true;
                            balance--;
                        } else {
                            hijo2.genotipo[i] = true;
                            hijo1.genotipo[i] = false;
                            balance++;
                        }
                        alternador = !alternador;
                    }
                }
            }
        }
        hijosN.add(hijo1);
        hijosN.add(hijo2);
    }

    //Reinsercion
    private static Ind[] reinsercionUniforme(ArrayList<Ind> hijosN, Ind[] poblacion) {
        ArrayList<Integer> listaPosUniforme = new ArrayList<>();
        for (int i = 0; i < poblacion.length; i++) {
            listaPosUniforme.add(i);
        }
        int random, pos;
        Random r = new Random();
        for (Ind hijosN1 : hijosN) {
            random = r.nextInt(listaPosUniforme.size());
            pos = listaPosUniforme.get(random);
            poblacion[pos] = hijosN1;
        }
        return poblacion;
    }

    private static Ind[] reinsercionPura(Ind[] poblacion, ArrayList<Ind> hijosN, ArrayList<Ind> padresE) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static Ind[] reinsercionRancia(ArrayList<Ind> hijosN, ArrayList<Ind> padresE) {
        int mitad = CANTPOB / 2;
        Ind[] poblacion = new Ind[CANTPOB];
        for (int i = 0; i < mitad; i++) {
            poblacion[i] = padresE.get(i);
            poblacion[mitad + i] = hijosN.get(i);
        }
        return poblacion;
    }

    private static Ind[] reinsercionElitista(ArrayList<Ind> hijosN, Ind[] poblacion) {
        int[] posicionesPeores = buscarPosicionesPeores(poblacion, hijosN.size());
        for (int i = 0; i < hijosN.size(); i++) {
            poblacion[posicionesPeores[i]] = hijosN.get(i);
        }
        return poblacion;
    }

    //Metodos Auxiliares
    private static void mostrarWinner(Ind unInd) {
        System.err.println("El ganador es: ");
        System.err.print("A{ ");
        for (int i = 0; i < N; i++) {
            if (unInd.genotipo[i]) {
                System.err.print(original[i] + " ");
            }
        }
        System.err.print("} ");

        System.err.print("B{ ");
        for (int i = 0; i < N; i++) {
            if (!unInd.genotipo[i]) {
                System.err.print(original[i] + " ");
            }
        }
        System.err.println("}");
        System.err.println("Con un fitness de: " + ganador.fitness);
    }

    private static int[] buscarPosicionesPeores(Ind[] poblacion, int size) {
        //Devuelve los indices de poblacion con peor valor (mas alto) de fitness
        int[] indices = new int[size];
        int pos = 0;
        int mejor = Integer.MAX_VALUE, posMejor = 0;
        for (int i = 0; i < poblacion.length; i++) {
            if (pos < size) {
                //Almacena los primeros [size] poblaciones segun su posicion, denotada con [i]
                indices[pos] = i;
                if (poblacion[i].fitness < mejor) {
                    //Guarda el mejor de los peores
                    mejor = poblacion[i].fitness;
                    posMejor = pos;
                }
                pos++;
            } else {
                //Para los siguientes Ind de poblacion despues de [size]
                if (poblacion[i].fitness > mejor) {
                    //Reemplaza el mejor por el actual y recalcula el mejor
                    indices[posMejor] = i;
                    mejor = poblacion[i].fitness;
                    for (int j = 0; j < indices.length; j++) {
                        if (mejor < poblacion[indices[j]].fitness) {
                            mejor = poblacion[indices[j]].fitness;
                            posMejor = j;
                        }
                    }
                }
            }
        }
        return indices;
    }
}

class Ind {
    //true si pertenece a A, false si pertenece a B

    public boolean[] genotipo;
    public int fitness;

    public Ind() {

    }

    public Ind(boolean[] genotipo) {
        this.genotipo = genotipo;
    }

    public void iniGenotipo() {
        ArrayList<Integer> listaPosClon = clonarLista(Solution.listaPos);
        Random r = new Random();
        genotipo = new boolean[Solution.N];
        for (int i = 0; i < Solution.N / 2; i++) {
            int random = r.nextInt(listaPosClon.size());
            int pos = listaPosClon.get(random);
            genotipo[pos] = true;
            listaPosClon.remove(random);
        }
        //Si hace falta, mirar los q no tienen true y poner false
    }

    public void calcFitnes(int[] listaO) {
        int A = 0, B = 1, n = Solution.N;
        for (int i = 0; i < n; i++) {
            if (genotipo[i]) {
                A += listaO[i];
            } else {
                B *= listaO[i];
            }
        }
        fitness = Math.abs((int) Math.pow(A, 2) - B);
    }

    public void mutacion(int cantMutaciones) {
        Random r = new Random();
        for (int i = 0; i < cantMutaciones; i++) {
            ArrayList<Integer> listaPosClon = clonarLista(Solution.listaPos);
            int random1 = r.nextInt(listaPosClon.size()), random2;
            int pos1 = listaPosClon.get(random1), pos2;
            listaPosClon.remove(random1);

            do {
                random2 = r.nextInt(listaPosClon.size());
                pos2 = listaPosClon.get(random2);
                listaPosClon.remove(random2);
            } while (genotipo[pos1] == genotipo[pos2]);

            genotipo[pos1] = !genotipo[pos1];
            genotipo[pos2] = !genotipo[pos2];
        }
    }

    public ArrayList<Integer> clonarLista(ArrayList<Integer> lista) {
        ArrayList<Integer> nuevaLista = new ArrayList<>();
        lista.forEach((lista1) -> {
            nuevaLista.add(lista1);
        });
        return nuevaLista;
    }

    @Override
    public String toString() {
        String s = "{ ";
        for (int i = 0; i < genotipo.length; i++) {
            if (genotipo[i]) {
                s += "1 ";
            } else {
                s += "0 ";
            }
        }
        s += "}";
        return s;
    }
}
