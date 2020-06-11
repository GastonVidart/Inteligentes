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
        System.out.println("Creando la red");
        
        traductor = new TraductorDatos(10);
        for (int i = 0; i < 5; i++) {
            traductor.addEntrada(1, 5);
            traductor.addEntrada(1, 14);
        }
        redNeuronal = new RedNeuronal(new int[]{traductor.getCantEntradas(), 18, 10}, "red-binario-poker-test");
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void test1() throws Exception {
        System.out.println("Carga los datos");
        String[][] datosTraining = LectorArchivos.leerDatosPokerTraining(),
                datosTesting = LectorArchivos.leerDatosPokerTesting(); //TODO: devolver arr object

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

        System.out.println("Primera fase de testing");
        double porcentajePrevio = redNeuronal.testRed(datosTestingTraducidos);

        
            System.out.println("Fase de training");
        for (int i = 0; i < 500; i++) {
            redNeuronal.gradiantDescent(0.1, datosTrainingTraducidos);
        }

        System.out.println("Segunda fase de testing");
        double porcentajePosterior = redNeuronal.testRed(datosTestingTraducidos);

        System.out.println("Previo: " + porcentajePrevio);
        System.out.println("Posterior: " + porcentajePosterior);
        //assertTrue(porcentajePrevio < porcentajePosterior);
    }
}
