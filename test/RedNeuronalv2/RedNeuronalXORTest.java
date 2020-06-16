package RedNeuronalv2;

import java.io.ObjectOutput;
import java.util.Random;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Gastón
 */
public class RedNeuronalXORTest {

    public static RedNeuronal redNeuronal;
    public static TraductorDatos traductor;

    @BeforeClass
    public static void setUpClass() {
        System.out.println("Creando la red");
        redNeuronal = new RedNeuronal(new int[]{2, 2, 2});
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void test1() throws Exception {
        System.out.println("Carga los datos");
        double[][] datosTraining = new double[][]{
            new double[]{0, 1, 1},
            new double[]{1, 0, 1},
            new double[]{0, 0, 0},
            new double[]{1, 1, 0}},
                datosTesting = new double[][]{
                    new double[]{0, 1, 1},
                    new double[]{1, 0, 1},
                    new double[]{0, 0, 0},
                    new double[]{1, 1, 0}};               

        System.out.println("Fase de training");
        for (int i = 0; i < 50000; i++) {
            //redNeuronal.gradiantDescent(0.3, datosTrainingTraducidos);
            double[] aleatorio = datosTraining[Aleatorio.intAleatorio(0, 4)];
            double[][] test = new double[][]{aleatorio};
            redNeuronal.gradientDescent(1, test, 1);
        }

        System.out.println("fase de testing");
        double porcentajePosterior = redNeuronal.testRed(datosTesting);
        redNeuronal.toJson("red-binario-XOR-1");
        System.out.println("Posterior: " + porcentajePosterior);
        //assertTrue(porcentajePrevio < porcentajePosterior);
    }
}
