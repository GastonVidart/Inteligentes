package RedNeuronal;

// @author guido
import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LectorArchivos {

    public static double[][] leerDatosPokerTraining() {
        BufferedReader br = null;
        ArrayList<double[]> datosLista = new ArrayList<>();
        double[][] datos = null;
        try {
            br = getBuffer("src/RedNeuronal/dataset/poker/poker-hand-training-true.data");
            String st;
            try {
                while ((st = br.readLine()) != null) {
                    String[] valoresString = st.split(",");
                    double[] valoresNumero = new double[valoresString.length];
                    for (int i = 0; i < valoresString.length; i++) {
                        valoresNumero[i] = Double.parseDouble(valoresString[i]);
                    }
                    datosLista.add(valoresNumero);
                }
                br.close();
                datos = new double[datosLista.size()][datosLista.get(0).length];
                for (int i = 0; i < datos.length; i++) {
                    datos[i] = datosLista.get(i);
                }
                return datos;
            } catch (IOException ex) {
                System.err.println("Error leyendo el archivo");
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Error buscando el archivo");
            System.out.println(ex);
        }
        return datos;
    }

    public static double[][] leerDatosPokerTesting() {
        BufferedReader br = null;
        ArrayList<double[]> datosLista = new ArrayList<>();
        double[][] datos = null;
        try {
            br = getBuffer("src/RedNeuronal/dataset/poker/poker-hand-testing.data");
            String st;
            try {
                while ((st = br.readLine()) != null) {
                    String[] valoresString = st.split(",");
                    double[] valoresNumero = new double[valoresString.length];
                    for (int i = 0; i < valoresString.length; i++) {
                        valoresNumero[i] = Double.parseDouble(valoresString[i]);
                    }
                    datosLista.add(valoresNumero);
                }
                br.close();
                datos = new double[datosLista.size()][datosLista.get(0).length];
                for (int i = 0; i < datos.length; i++) {
                    datos[i] = datosLista.get(i);
                }
                return datos;
            } catch (IOException ex) {
                System.err.println("Error leyendo el archivo");
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Error buscando el archivo");
            System.out.println(ex);
        }
        return datos;
    }
    
    public static BufferedReader getBuffer(String fileName) throws FileNotFoundException {
        File file = new File(fileName);

        return new BufferedReader(new FileReader(file));
    }

    public static void main(String[] args) {
        leerDatosPokerTraining();
    }
}
