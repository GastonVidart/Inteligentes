package RedNeuronal;

// @author guido
public class Main {

    public static void main(String[] args) {
        double[][] matrizTraining = LectorArchivos.leerDatosPokerTraining(),
                matrizTesting = LectorArchivos.leerDatosPokerTesting();
        RedNeuronal red = new RedNeuronal(10, new int[]{10}, 10,
                new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"}, RedNeuronal.SIGMOIDE);
        
        
        System.arraycopy(matrizTesting, 0, matrizTesting, 0, matrizTesting.length / 2);
        System.out.println(matrizTesting.length);
        red.entrenarRed(matrizTraining, 1, matrizTesting);
    }
}
