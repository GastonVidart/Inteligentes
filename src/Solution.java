
import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 *
 */
class Solution {

    static int[] original;
    public static int N, esperado = 6;
    static final int CANTPOB = 16, CANTIT = 20;

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
        ArrayList<Integer> listaPos = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            listaPos.add(i);
        }
        for (int i = 0; i < CANTPOB; i++) {
            poblacion[i] = new Ind();
            poblacion[i].iniGenotipo(clonarLista(listaPos));
        }

        for (int j = 0; j < CANTIT; j++) {
            
            //--------CALCULAR FITNESS
            for (int i = 0; i < CANTPOB; i++) {
                poblacion[i].calcFitnes(original);
            }

            //-------SELECCION
            ArrayList<Ind> padresE;
            padresE = seleccionTorneo(poblacion);

            //-------CROSSOVER
            ArrayList<Ind> hijosN;
            hijosN = crossOver(padresE);

            //-------MUTACION
            hijosN = mutacion(hijosN);

            //-------REINSERCION
            poblacion = reinsercionPura(poblacion, hijosN, padresE);

        }

        // Write an answer using System.out.println()
        // To debug: System.err.println("Debug messages...");
        System.out.println("answer");
    }

    public static ArrayList<Integer> clonarLista(ArrayList<Integer> lista) {
        ArrayList<Integer> nuevaLista = new ArrayList<>();
        lista.forEach((lista1) -> {
            nuevaLista.add(lista1);
        });
        return nuevaLista;
    }

    private static ArrayList<Ind> seleccionTorneo(Ind[] poblacion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static ArrayList<Ind> crossOver(ArrayList<Ind> padresE) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static ArrayList<Ind> mutacion(ArrayList<Ind> hijosN) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static Ind[] reinsercionPura(Ind[] poblacion, ArrayList<Ind> hijosN, ArrayList<Ind> padresE) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

class Ind {
    //true si pertenece a A, false si pertenece a B

    public boolean[] genotipo;
    public int fitness;
    
    public void iniGenotipo(ArrayList<Integer> listaPos){
        Random r = new Random();
        genotipo = new boolean[Solution.N];
        for (int i = 0; i < Solution.N / 2; i++) {
            int pos = r.nextInt(listaPos.size());
            genotipo[pos] = true;
            listaPos.remove(pos);
        }
        //Si hace falta, mirar los q no tienen true y poner false
    }

    public void calcFitnes(int[] listaO) {
        int A = 0, B = 1, n = Solution.N;
        for (int i = 0; i < n; i++) {
            if(genotipo[i]){
                A += listaO[i];
            }else {
                B *= listaO[i];
            }
        }
        fitness = Math.abs((int) Math.pow(A, 2) - B);
    }
    
    public void mutacion(int cantMutaciones){
        //TODO: swapear valores de V/F segun la cantMutaciones
    }

}
