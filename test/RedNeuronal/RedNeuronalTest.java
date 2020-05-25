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

    RedNeuronal red;

    @Test
    public void testPoker() throws Exception {
        int cantEntradas = 10;
        int[] cantCapasOcultas = {2, 3, 2};
        int cantSalidas = 10;
        String[] salidas = {"Nothing in hand", "One pair", "Two pairs",
            "Three of a kind", "Straight", "Flush", "Full house",
            "Four of a kind", "Straight flush", "Royal flush"};
        int funcion = RedNeuronal.SIGMOIDE;
        red = new RedNeuronal(cantEntradas, cantCapasOcultas, cantSalidas, salidas, funcion);
        int indice = red.obtenerSalida(new double[]{1,1,1,13,2,4,2,3,1,12});
        assertEquals(red.traducirSalida(indice), "Nothing in hand");
        
        indice = red.obtenerSalida(new double[]{3,12,3,2,3,11,4,5,2,5});
        assertEquals("One pair", red.traducirSalida(indice));
    }

}
