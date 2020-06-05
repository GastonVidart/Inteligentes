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

    public RedNeuronalTestPoker() {
    }

    @BeforeClass
    public static void setUpClass() {
        redNeuronal = new RedNeuronal(new int[]{10, 50, 100, 50, 10});
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
        double[][] datosTraining = LectorArchivos.leerDatosPokerTraining(),
                datosTesting = LectorArchivos.leerDatosPokerTesting();
        
        double porcentajePrevio = redNeuronal.testRed(datosTesting);        
        
        for (int i = 0; i < 50; i++) {
            redNeuronal.gradiantDescent(0.3, datosTraining);            
        }       
        
        double porcentajePosterior = redNeuronal.testRed(datosTesting);
        
        System.out.println("Previo: " + porcentajePrevio);
        System.out.println("Posterior: " + porcentajePosterior);
        assertTrue(porcentajePrevio < porcentajePosterior);
        
    }
    
    

}
