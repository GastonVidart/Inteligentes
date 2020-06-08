package RedNeuronalv2;

import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Ignore;

/**
 *
 * @author guido
 */
public class TraductorDatosTest {

    static TraductorDatos traductor;

    @BeforeClass
    public static void setUpClass() {
        traductor = new TraductorDatos();
        traductor.addEntrada(new Object[]{1, 2, 3, 4, 5, 6, 7});
        traductor.addEntrada(new Object[]{"uno", "dos", "tres"});
        traductor.addEntrada(1, 5);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test 
    public void testCantidad() {
        assertEquals(7, traductor.getCantEntradas());
    }
    
    @Test
    public void test1() throws Exception {
        Object[] test = {1, "dos", 1};

        double[] esperado = {0, 0, 0, 0, 1, 0, 0}, 
                resultado = traductor.transformarEntrada(test);
        
        printArray(resultado);
        assertArrayEquals(esperado, resultado, 0);
    }

    @Test
    public void test2() throws Exception {
        Object[] test = {4, "tres", 2};

        double[] esperado = {0, 1, 1, 1, 0, 0, 1}, 
                resultado = traductor.transformarEntrada(test);

        printArray(resultado);
        assertArrayEquals(esperado, resultado, 0);
    }

    @Test(expected = Exception.class)
    public void testException() throws Exception {
        Object[] test = {1, "perro", 1};

        traductor.transformarEntrada(test);
    }

    @Test
    public void testTransformarBinario() {
        double[] test = {1, 0, 1, 0},
                segundoTest = {0, 0, 1, 0, 1, 0};
        assertArrayEquals(test, TraductorDatos.transformarBinario(10, 4), 0);
        assertArrayEquals(segundoTest, TraductorDatos.transformarBinario(10, 6), 0);
    }

    private static void printArray(double[] array) {
        System.out.print("[");
        for (double d : array) {
            System.out.print(d + " ");
        }
        System.out.println("]");
    }
}
