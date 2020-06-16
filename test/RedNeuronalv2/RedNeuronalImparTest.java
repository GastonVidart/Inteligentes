package RedNeuronalv2;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author guido
 */
public class RedNeuronalImparTest {

    public static RedNeuronal redNeuronal;
    public static TraductorDatos traductor;
    public static int cantDatos = 2000, cantEntradas = 4;

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Creando la red");

        traductor = new TraductorDatos(4);
        for (int i = 0; i < cantEntradas; i++) {
            traductor.addEntrada(0, 16);
        }
        redNeuronal = new RedNeuronal(new int[]{traductor.getCantEntradas(), 16, 2});
    }

    @Test
    public void test1() throws Exception {
        System.out.println("Carga los datos");
        String[][] datosTraining = CreadorDataset.crearDatasetMatriz(cantEntradas, cantDatos),
                datosTesting = CreadorDataset.crearDatasetMatriz(cantEntradas, cantDatos / 10); //TODO: devolver arr object

        double[][] datosTrainingTraducidos = new double[datosTraining.length][],
                datosTestingTraducidos = new double[datosTesting.length][];

        System.out.println("Traduce los datos de training");
        for (int j = 0; j < datosTraining.length; j++) {
            datosTrainingTraducidos[j] = traductor.transformarEntradaBinario(datosTraining[j]);
        }

        System.out.println("Traduce los datos de testing");
        for (int j = 0; j < datosTesting.length; j++) {
            datosTestingTraducidos[j] = traductor.transformarEntradaBinario(datosTesting[j]);
        }

        System.out.println("Fase de training");
        for (int i = 0; i < 50000; i++) {
            if (i % 100 == 0) {
                System.out.print("->Fase de training: " + i + " -- ");
            }
            redNeuronal.gradientDescent(5, datosTrainingTraducidos, 1);
            if (i % 100 == 0) {
                double porcentaje = redNeuronal.testRed(datosTestingTraducidos);
                System.out.println(" -- Aciertos: " + porcentaje);
            }
        }

        System.out.println("Segunda fase de testing");
        double porcentajePosterior = redNeuronal.testRed(datosTestingTraducidos);
        redNeuronal.toJson("red-binario-poker-test-post");
        System.out.println("Posterior: " + porcentajePosterior);
    }
}
