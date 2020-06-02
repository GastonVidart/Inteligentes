package RedNeuronal;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author guido
 */
public class RedNeuronalTest {

    private static RedNeuronalV1 red;
    private static String[] salidas = {"Nothing in hand", "One pair", "Two pairs",
        "Three of a kind", "Straight", "Flush", "Full house",
        "Four of a kind", "Straight flush", "Royal flush"};

    @BeforeClass
    public static void setUpClass() {
        int cantEntradas = 10;
        int[] cantCapasOcultas = {2, 3, 2};
        int cantSalidas = 10;
        int funcion = RedNeuronalV1.SIGMOIDE;
        red = new RedNeuronalV1(cantEntradas, cantCapasOcultas, cantSalidas, salidas, funcion);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void testPokerDatoNada() throws Exception {
        int indice = red.obtenerSalida(new double[]{1, 1, 1, 13, 2, 4, 2, 3, 1, 12});
        assertEquals(salidas[0], red.traducirSalida(indice));
    }

    @Test
    public void testPokerDatoPar() throws Exception {
        int indice = red.obtenerSalida(new double[]{3, 12, 3, 2, 3, 11, 4, 5, 2, 5});
        assertEquals(salidas[1], red.traducirSalida(indice));
    }

    @Test
    public void testPokerDatoTriple() throws Exception {
        int indice = red.obtenerSalida(new double[]{2, 11, 2, 4, 3, 8, 1, 11, 4, 11});
        assertEquals(salidas[3], red.traducirSalida(indice));
    }

    @Test
    public void testPokerDatoEscaleraReal() throws Exception {
        int indice = red.obtenerSalida(new double[]{1, 1, 1, 7, 1, 2, 1, 6, 1, 5});
        assertEquals(salidas[5], red.traducirSalida(indice));
    }
}
