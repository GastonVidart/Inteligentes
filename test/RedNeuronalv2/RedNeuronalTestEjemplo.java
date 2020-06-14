package RedNeuronalv2;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Gastón
 */
public class RedNeuronalTestEjemplo {

    private static RedNeuronal redNeuronal;

    public RedNeuronalTestEjemplo() {
    }

    @BeforeClass
    public static void setUpClass() {
        redNeuronal = new RedNeuronal(new int[]{2, 3, 2});
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of gradiantDescent method, of class RedNeuronal.
     */
    @Test
    public void testRed() {
        assertEquals(2, redNeuronal.capas.length);

        assertEquals(2, redNeuronal.capas[0].w[0].length); //arcos capa1
        assertEquals(3, redNeuronal.capas[0].w.length); //nodos capa1

        assertEquals(3, redNeuronal.capas[1].w[0].length); //arcos capa2
        assertEquals(2, redNeuronal.capas[1].w.length); //nodos capa2

    }

    @Test
    public void testGradiantDescent() {
        String[][] datosTraining = LectorArchivos.leerDatosEjemploTraining();
        //redNeuronal.gradientDescent(0.3, datosTraining,1); 
    }

}
