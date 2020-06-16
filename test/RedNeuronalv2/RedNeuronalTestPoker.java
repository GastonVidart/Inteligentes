package RedNeuronalv2;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Gastón
 */
public class RedNeuronalTestPoker {

    private static RedNeuronal redNeuronal;
    public static TraductorDatos traductor;

    public RedNeuronalTestPoker() {
    }

    @BeforeClass
    public static void setUpClass() {
        redNeuronal = new RedNeuronal(new int[]{10, 18, 10});
        traductor = new TraductorDatos(10);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Ignore
    @Test
    public void testRed() {
        assertEquals(2, redNeuronal.capas.length);

        assertEquals(10, redNeuronal.capas[0].w[0].length); //arcos capa1
        assertEquals(2, redNeuronal.capas[0].w.length); //nodos capa1

        assertEquals(2, redNeuronal.capas[1].w[0].length); //arcos capa2
        assertEquals(10, redNeuronal.capas[1].w.length); //nodos capa2

    }

    /**
     * Test of gradiantDescent method, of class RedNeuronal.
     */
    @Test
    public void testGradiantDescent() {
        String[][] datosTraining = LectorArchivos.leerDatosPokerTraining(),
                datosTesting = LectorArchivos.leerDatosPokerTesting();

        double[][] datosTrainingTraducidos = new double[datosTraining.length][],
                datosTestingTraducidos = new double[datosTesting.length][];
        
        System.out.println("Traduce los datos de training a double");
        for (int j = 0; j < datosTraining.length; j++) {
            datosTrainingTraducidos[j] = traductor.transformarDouble(datosTraining[j]);
        }
        
        System.out.println("Traduce los datos de testing");
        for (int j = 0; j < datosTesting.length; j++) {
            datosTestingTraducidos[j] = traductor.transformarDouble(datosTesting[j]);
        }        

        System.out.println("Primera fase de testing");
        double porcentajePrevio = redNeuronal.testRed(datosTestingTraducidos);

        System.out.println("Fase de training");
        for (int i = 0; i < 2000; i++) {
            redNeuronal.gradientDescent(0.1, datosTrainingTraducidos,10);
        }

        System.out.println("Segunda fase de testing");
        double porcentajePosterior = redNeuronal.testRed(datosTestingTraducidos);

        System.out.println("Previo: " + porcentajePrevio);
        System.out.println("Posterior: " + porcentajePosterior);
    }

}
