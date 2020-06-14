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
public class RedNeuronalWineTest {

    private static RedNeuronal redNeuronal;

    @BeforeClass
    public static void setUpClass() {
        redNeuronal = new RedNeuronal(new int[]{11, 30, 10});
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Ignore
    @Test
    public void testRed() {
        assertEquals(2, redNeuronal.capas.length);

        assertEquals(11, redNeuronal.capas[0].w[0].length); //arcos capa1
        assertEquals(20, redNeuronal.capas[0].w.length); //nodos capa1

        assertEquals(20, redNeuronal.capas[1].w[0].length); //arcos capa2
        assertEquals(10, redNeuronal.capas[1].w.length); //nodos capa2

    }

    /**
     * Test of gradiantDescent method, of class RedNeuronal.
     */
    @Test
    public void testGradiantDescent() {
        double[][] datosWine = LectorArchivos.leerDatosWine(), datosTest, datosTrain;
        int i;
        datosTrain = new double[3429][];
        for (i = 0; i < datosTrain.length; i++) {
            System.out.println(i);
            datosTrain[i] = datosWine[i];
        }
        System.out.println(datosTrain.length+"--");

        datosTest = new double[1469][];
        for (i = 0; i < 1469; i++) {
            System.out.println(i);
            datosTest[i] = datosWine[i+3428];
        }
        System.out.println(datosTest.length);

        System.out.println("Primera fase de testing");
        double porcentajePrevio = redNeuronal.testRed(datosTest);

        System.out.println("Fase de training");
        for (i = 0; i < 1000; i++) {
            redNeuronal.gradientDescent(0.5, datosTrain, 1);
        }

        System.out.println("Segunda fase de testing");
        double porcentajePosterior = redNeuronal.testRed(datosTest);

        System.out.println("Previo: " + porcentajePrevio);
        System.out.println("Posterior: " + porcentajePosterior);
    }

}
