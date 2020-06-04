package RedNeuronalv2;

// @author guido
import static RedNeuronalv2.LectorArchivos.getBuffer;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ExportadorRedNeuronal {

    //exportado    
    public static String exportarRed(RedNeuronal red, String fileN) {
        return null;
    }

    public static RedNeuronal importarRed(String fileN) {
        BufferedReader br = null;
        try {
            br = getBuffer("src/redes/" + fileN);
            String st;
            try {
                ArrayList<Capa> capasLista = new ArrayList<>();
                ArrayList<Double> valoresB = new ArrayList<>();
                ArrayList<Double[]> valoresW = new ArrayList<>();
                while ((st = br.readLine()) != null) {
                    if (st.contains("-")) {
                        //Crear capa y guardar datos previos
                        //TODO: buscar JSON-simple
//                        double[] b = valoresB.toArray(new Double[0]);
//                        double[][] w = valoresW.toArray(new Double[0][]));
                        capasLista.add(new Capa(b, w));
                    } else {
                        //Agrego a colecciones temporales los valores de B y W
                        String[] nodoS = st.split("|");
                        String[] arcosS = nodoS[0].split(",");
                        valoresB.add(Double.parseDouble(nodoS[1]));
                        double[] nodoW = new double[arcosS.length];
                        for (int i = 0; i < arcosS.length; i++) {
                            nodoW[i] = Double.parseDouble(arcosS[i]);
                        }
                        valoresW.add(nodoW);
                    }
                }
                br.close();
                return null;
            } catch (IOException ex) {
                System.err.println("Error leyendo el archivo");
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Error buscando el archivo");
            System.out.println(ex);
        }
        return null;
    }
}
