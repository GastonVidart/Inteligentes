package RedNeuronalv2;

// @author guido
import java.util.Random;

public class CreadorDataset {

    public static String crearDataset(int cantIn, int cantDatos) {
        //Crea un dataset q genera combinaciones de entradas con una salida que indica si es par o no la suma de ellos
        //1: impar, 0: par
        String dataset = "";
        Random r = new Random();
        int acum;
        
        for (int i = 0; i < cantDatos; i++) {
            acum = 0;
            for (int j = 0; j < cantIn; j++) { 
                int valor = r.nextInt(16);
                acum += valor;
                dataset += valor + ",";
            }
            dataset += (acum % 2 == 1) ? 1 : 0;
            dataset += "\n";
        }

        return dataset;
    }
    
    public static void main(String[] args) {
        System.out.println(crearDataset(4, 10));
    }
}
