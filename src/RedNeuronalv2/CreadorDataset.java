package RedNeuronalv2;

// @author guido
import java.util.Random;

public class CreadorDataset {

    public static String crearDatasetString(int cantEntradas, int cantDatos) {
        //Crea un dataset q genera combinaciones de entradas con una salida que indica si es par o no la suma de ellos
        //1: impar, 0: par
        String dataset = "";
        Random r = new Random();
        int acum;

        for (int i = 0; i < cantDatos; i++) {
            acum = 0;
            for (int j = 0; j < cantEntradas; j++) {
                int valor = r.nextInt(16);
                acum += valor;
                dataset += valor + ",";
            }
            dataset += (acum % 2 == 1) ? 1 : 0;
            dataset += "\n";
        }

        return dataset;
    }

    public static String[][] crearDatasetMatriz(int cantEntradas, int cantDatos) {
        //Crea un dataset q genera combinaciones de entradas con una salida que indica si es par o no la suma de ellos
        //1: impar, 0: par
        String[][] dataset = new String[cantDatos][cantEntradas + 1];
        Random r = new Random();
        int acum;

        for (int i = 0; i < cantDatos; i++) {
            acum = 0;
            for (int j = 0; j < cantEntradas; j++) {
                int valor = r.nextInt(16);
                acum += valor;
                dataset[i][j] = "" + valor;
            }
            dataset[i][dataset[i].length - 1] = (acum % 2 == 1) ? "1" : "0";
        }

        return dataset;
    }

    public static void main(String[] args) {
        int cantDatos = 10,
                cantEntradas = 4;
        
        String[][] dataset = crearDatasetMatriz(cantEntradas, cantDatos);

        for (int i = 0; i < cantDatos; i++) {
            for (int j = 0; j < cantEntradas + 1; j++) {
                System.out.print(dataset[i][j] + " ");
            }
            System.out.println("");
        }

    }
}
