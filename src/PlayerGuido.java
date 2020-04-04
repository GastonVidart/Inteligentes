
import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 *
 */
class PlayerGuido {

    private static final String MOVE = "MOVE", WAIT = "WAIT", SLOWER = "SLOWER";
    
    private static ArrayList<Barco> barcosEnemigos;
    private static ArrayList<Barco> misBarcos;
    private static ArrayList<Barril> barriles;
    
    private static final int CANTNAVES = 1;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        misBarcos = new ArrayList<>();
        for (int i = 0; i < CANTNAVES; i++) {
            misBarcos.add(new Barco());
        }

        // game loop
        while (true) {
            int myShipCount = in.nextInt(); // the number of remaining ships
            int entityCount = in.nextInt(); // the number of entities (e.g. ships, mines or cannonballs)
            barriles = new ArrayList<>();
            barcosEnemigos = new ArrayList<>();

            for (int i = 0; i < entityCount; i++) {
                int k = 0;

                int entityId = in.nextInt();
                String entityType = in.next();
                int x = in.nextInt();
                int y = in.nextInt();
                int arg1 = in.nextInt();
                int arg2 = in.nextInt();
                int arg3 = in.nextInt();
                int arg4 = in.nextInt();

                switch (entityType) {
                    case "SHIP":
                        Barco barco;
                        if (arg4 == 1) {
                            barco = misBarcos.get(k);
                        } else {
                            barco = new Barco();
                            barcosEnemigos.add(barco);
                        }
                        
                        barco.xActual = x;
                        barco.yActual = y;
                        barco.cantRum = arg3;
                        barco.orientacionActual = arg1;
                        barco.velocidad = arg2;
                        break;
                    case "BARREL":
                        Barril barril = new Barril();
                        barril.xActual = x;
                        barril.yActual = y;
                        barriles.add(barril);
                        break;
                    default:
                        break;
                }
            }

            for (int i = 0; i < myShipCount; i++) {
                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");
                hillClimbing(i);
                System.out.println(misBarcos.get(i).ordenActual); // Any valid action, such as "WAIT" or "MOVE x y"
                misBarcos.get(i).xPrevio = misBarcos.get(i).xActual;
                misBarcos.get(i).yPrevio = misBarcos.get(i).yActual;
                misBarcos.get(i).ordenPrevia = misBarcos.get(i).ordenActual;
                misBarcos.get(i).orientacionPrevia = misBarcos.get(i).orientacionActual;
            }
        }
    }

    public static void hillClimbing(int numNave) {
        Barco barco = misBarcos.get(numNave);
        int x;
        int xD = barco.xDestino,
                yD = barco.yDestino;

        //destrabar si sigue en el mismo lugar
        if (barco.orientacionActual == barco.orientacionPrevia && esIgual(new int[]{barco.xPrevio, barco.yPrevio}, new int[]{barco.xActual, barco.yActual})) {
            int[] posD = destrabarBarco(barco);
            xD = posD[0];
            yD = posD[1];
            barco.heurOrden = 400;
            barco.ordenActual = MOVE + " " + getPosString(xD, yD);
        } else {
            //WAIT
            x = heuristica(WAIT, 0, 0, barco);
            if (esIgual(new int[]{barco.xDestino, barco.yDestino}, new int[]{barco.xActual, barco.yActual})
                    || x < barco.heurOrden) {
                barco.heurOrden = x;
                barco.ordenActual = WAIT;
                xD = barco.xActual;
                yD = barco.yActual;
            }

            //MOVE
            for (int i = 0; i < barriles.size(); i++) {
                Barril barril = barriles.get(i);
                x = heuristica(MOVE, barril.xActual, barril.yActual, barco);
                if (x < barco.heurOrden) {
                    barco.heurOrden = x;
                    barco.ordenActual = MOVE + " " + getPosString(barril.xActual, barril.yActual);
                    xD = barril.xActual;
                    yD = barril.yActual;
                }
            }
        }

        barco.xDestino = xD;
        barco.yDestino = yD;
    }

    private static int heuristica(String action, int x, int y, Barco barco) {
        int aux = 400;

        switch (action) {
            case WAIT:
                aux = 100;
                break;
            case MOVE:
                aux = distancia(new int[]{x, y}, new int[]{barco.xActual, barco.yActual});
                break;
            default:
                break;
        }

        return aux;
    }

    private static String getPosString(int x, int y) {
        return x + " " + y;
    }

    private static boolean esIgual(int[] a, int[] b) {
        return a.length == b.length && a[0] == b[0] && a[1] == b[1];
    }

    //Destrabar Barco
    private static int[] destrabarBarco(Barco miBarco) {
        int[] newPos = new int[2];

        int[] pos0 = {miBarco.xActual + 1, miBarco.yActual + 0},
                pos1 = {miBarco.xActual + miBarco.yActual % 2, miBarco.yActual - 1},
                pos2 = {miBarco.xActual + miBarco.yActual % 2 - 1, miBarco.yActual - 1},
                pos3 = {miBarco.xActual - 1, miBarco.yActual + 0},
                pos4 = {miBarco.xActual + miBarco.yActual % 2 - 1, miBarco.yActual + 1},
                pos5 = {miBarco.xActual + miBarco.yActual % 2, miBarco.yActual + 1};

        switch (miBarco.orientacionActual) {
            case 0:
                newPos = pos1;
                if (estaOcupado(newPos) || estaOcupado(pos0)) {
                    newPos = pos5;
                }
                break;
            case 1:
                newPos = pos2;
                if (estaOcupado(newPos) || estaOcupado(pos1)) {
                    newPos = pos0;
                }
                break;
            case 2:
                newPos = pos3;
                if (estaOcupado(newPos) || estaOcupado(pos2)) {
                    newPos = pos1;
                }
                break;
            case 3:
                newPos = pos4;
                if (estaOcupado(newPos) || estaOcupado(pos3)) {
                    newPos = pos2;
                }
                break;
            case 4:
                newPos = pos5;
                if (estaOcupado(newPos) || estaOcupado(pos4)) {
                    newPos = pos3;
                }
                break;
            case 5:
                newPos = pos0;
                if (estaOcupado(newPos) || estaOcupado(pos5)) {
                    newPos = pos4;
                }
                break;
        }

        return newPos;
    }

    private static boolean estaOcupado(int[] pos) {
        //Por un barco enemigo
        return barcosEnemigos.stream().anyMatch((barco) -> (barco.xActual == pos[0] && barco.yActual == pos[1]));
    }

    //Calculo de Distancia
    private static int distancia(int[] a, int[] b) {
        //distancia real en mapa HEX, convierte de OFFSET que usa el mapa a CUBO
        int valor;
        int[] ac, bc;
        ac = offset_to_cube(a);
        bc = offset_to_cube(b);
        return cube_distance(ac, bc);
    }

    private static int[] offset_to_cube(int[] hex) {
        //el mapa usa odd-r
        int[] cubo = new int[3];
        cubo[0] = hex[0] - (hex[1] - (hex[1] & 1)) / 2; //x
        cubo[2] = hex[1]; //z
        cubo[1] = -cubo[0] - cubo[2]; //y
        return cubo;
    }

    private static int cube_distance(int[] a, int[] b) {
        return (Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]) + Math.abs(a[2] - b[2])) / 2;
    }
}

class Barco {

    public int xActual, yActual, xPrevio, yPrevio, xDestino, yDestino;
    public int cantRum, velocidad, orientacionActual, orientacionPrevia = -1, heurOrden = Integer.MAX_VALUE;
    public String ordenActual, ordenPrevia = null;

}

class Barril {

    public int xActual, yActual;
}
