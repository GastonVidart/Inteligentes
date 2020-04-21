
import java.util.ArrayList;
import java.util.Random;

// @author guido
public class MainTest {

    public static boolean[] genotipo1, genotipo2;
    public static int fitness;
    public static ArrayList<Integer> listaPos;
    public static int N = 10;

    public static void main(String[] args) {
        listaPos = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            listaPos.add(i);
        }
        System.out.println("1");
        for (int i = 0; i < 1; i++) {
            genotipo1 = iniGenotipo();
            System.out.println("2");
            genotipo2 = iniGenotipo();
            Ind padre = new Ind(genotipo1);
            Ind madre = new Ind(genotipo2);
            cruzarPadres(padre, madre, new ArrayList<>());
        }
    }

    public static void viejoTest() {
        listaPos = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            listaPos.add(i);
        }
        genotipo1 = iniGenotipo();
        printArray(genotipo1, genotipo1.length);
        printArray(genotipo1, genotipo1.length);
    }

    public static void printArray(boolean[] array, int cantPos) {
        System.out.print("{ ");
        for (int i = 0; i < cantPos; i++) {
            if (array[i]) {
                System.out.print("1 ");
            } else {
                System.out.print("0 ");
            }
        }
        System.out.println(" }");
    }

    public static void printList(ArrayList<Integer> list) {
        System.out.print("{ ");
        list.forEach((list1) -> {
            System.out.print(list1 + " ");
        });
        System.out.println(" }");
    }

    public static boolean[] iniGenotipo() {
        boolean[] genotipo;
        ArrayList<Integer> listaPosClon = clonarLista(listaPos);
        Random r = new Random();
        genotipo = new boolean[N];
        for (int i = 0; i < N / 2; i++) {
            int random = r.nextInt(listaPosClon.size());
            int pos = listaPosClon.get(random);
            genotipo[pos] = true;
            listaPosClon.remove(random);
        }
        return genotipo;
    }

    public static ArrayList<Integer> clonarLista(ArrayList<Integer> lista) {
        ArrayList<Integer> nuevaLista = new ArrayList<>();
        lista.forEach((lista1) -> {
            nuevaLista.add(lista1);
        });
        return nuevaLista;
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

        printArray(padre.genotipo, padre.genotipo.length);
        printArray(madre.genotipo, madre.genotipo.length);

        int balance = 0;
        for (int i = 0; i < N; i++) {
            if (padre.genotipo[i] != madre.genotipo[i]) {
                if (alternador) {
                    //Si el valor es distinto, entonces cada hijo se queda con el valor de uno de los padres
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

        printArray(hijo1.genotipo, hijo1.genotipo.length);
        printArray(hijo2.genotipo, hijo2.genotipo.length);

        hijosN.add(hijo1);
        hijosN.add(hijo2);
    }

}
