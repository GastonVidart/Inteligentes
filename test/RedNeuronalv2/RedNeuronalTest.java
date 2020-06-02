package RedNeuronalv2;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Gastón
 */
public class RedNeuronalTest {

    private static RedNeuronal redNeuronal;

    public RedNeuronalTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        redNeuronal = new RedNeuronal(new int[]{10, 2, 10});
    }

    @AfterClass
    public static void tearDownClass() {
    }

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
        double[][] datosTraining = LectorArchivos.leerDatosPokerTraining();
        redNeuronal.gradiantDescent(0.3, datosTraining);       
    }

}