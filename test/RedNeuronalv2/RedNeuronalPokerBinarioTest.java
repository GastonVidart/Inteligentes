package RedNeuronalv2;

import java.io.ObjectOutput;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Gastón
 */
public class RedNeuronalPokerBinarioTest {

    public static RedNeuronal redNeuronal;
    public static TraductorDatos traductor;

    @BeforeClass
    public static void setUpClass() {
        traductor = new TraductorDatos(10);
        for (int i = 0; i < 5; i++) {
            traductor.addEntrada(1, 5);
            traductor.addEntrada(1, 14);
        }
        redNeuronal = new RedNeuronal(new int[]{traductor.getCantEntradas(), 35, 10});
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void test1() throws Exception {
        String[][] datosTraining = LectorArchivos.leerDatosPokerTraining(),
                datosTesting = LectorArchivos.leerDatosPokerTesting(); //TODO: devolver arr object

        double[][] datosTrainingTraducidos = new double[datosTraining.length][],
                datosTestingTraducidos = new double[datosTesting.length][];

        for (int j = 0; j < datosTraining.length; j++) {
            datosTrainingTraducidos[j] = traductor.transformarEntradaBinario(datosTraining[j]);
        }
        
        for (int j = 0; j < datosTesting.length; j++) {
            datosTestingTraducidos[j] = traductor.transformarEntradaBinario(datosTesting[j]);
        }

        //double porcentajePrevio = redNeuronal.testRed(datosTestingTraducidos);

        for (int i = 0; i < 10000; i++) {
            redNeuronal.gradiantDescent(0.03, datosTrainingTraducidos);
        }

        double porcentajePosterior = redNeuronal.testRed(datosTestingTraducidos);

        //System.out.println("Previo: " + porcentajePrevio);
        System.out.println("Posterior: " + porcentajePosterior);
        //assertTrue(porcentajePrevio < porcentajePosterior);
    }
}
