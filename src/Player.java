
import java.util.*;
import java.io.*;
import java.math.*;

// @author Todos
public class Player {

    private static final String MOVE = "MOVE", WAIT = "WAIT",
            SLOWER = "SLOWER", FIRE = "FIRE", MINE = "MINE",
            PORT = "PORT", STARBOARD = "STARBOARD", FASTER = "FASTER";

    private static ArrayList<Ship> barcos = new ArrayList<>();
    private static ArrayList<Ship> enemigos = new ArrayList<>();
    private static ArrayList<Barrel> barriles = new ArrayList<>();
    private static ArrayList<CannonBall> balas = new ArrayList<>();
    private static ArrayList<Mine> minas = new ArrayList<>();

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            int myShipCount = in.nextInt(); // the number of remaining ships
            int entityCount = in.nextInt(); // the number of entities (e.g. ships, minas or balas)

            for (int i = 0; i < entityCount; i++) {
                int entityId = in.nextInt();
                String entityType = in.next();
                int x = in.nextInt();
                int y = in.nextInt();
                int arg1 = in.nextInt();
                int arg2 = in.nextInt();
                int arg3 = in.nextInt();
                int arg4 = in.nextInt();

                int[] pos = oddr_to_cube(new int[]{x, y});

                switch (entityType) {
                    case "BARREL":
                        barriles.add(new Barrel(arg1, pos));
                        break;
                    case "SHIP":
                        //si lo controlo lo guardo como mi barco, sino enemigo
                        Ship shipAux = new Ship(entityId, arg1, arg2, arg3, pos);
                        if (arg4 == 1) {
                            barcos.add(shipAux);
                        } else {
                            enemigos.add(shipAux);
                        }
                        break;
                    case "CANNONBALL":
                        balas.add(new CannonBall(arg1, arg2, pos));
                        break;
                    case "MINE":
                        minas.add(new Mine(pos));
                        break;
                }
            }

            for (int i = 0; i < myShipCount; i++) {
                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");                
                System.out.println(hillClimbing(i)); // Any valid action, such as "WAIT" or "MOVE x y"
            }
        }
    }

    private static String hillClimbing(int idBarco) {
        String accion="";
        int heur;
        Ship barco = barcos.get(idBarco);
        
        return accion;
    }

    // METODOS PARA CONTROL DE MAPA -----------------------------------
    private static int distancia(int[] a, int[] b) {
        //distancia en cubo entre a y b
        return (Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]) + Math.abs(a[2] - b[2])) / 2;
    }

    private static int[] oddr_to_cube(int[] hex) {
        //convierte de oddr(xy) a cubo(xyz)
        int[] cubo = new int[3];
        cubo[0] = hex[0] - (hex[1] - (hex[1] & 1)) / 2; //x
        cubo[2] = hex[1]; //z
        cubo[1] = -cubo[0] - cubo[2]; //y
        return cubo;
    }

    private static int[] cube_to_oddr(int[] cube) {
        //convierte de cubo(xyz) a oddr(xy) 
        int col = cube[0] + (cube[2] - (cube[2] & 1)) / 2;
        int row = cube[2];
        return new int[]{col, row};
    }

    public static void printMensaje(String msj) {
        //para debug
        System.err.println(msj);
    }
}

// POSICIONES X=0 Y=1 Z=2 -----------------------------------
class Ship {

    public int idBarco, orientacion, velocidad, ron;
    public int[] posActual;

    public Ship(int idBarco, int orientacion, int velocidad, int ron, int[] posActual) {
        this.idBarco = idBarco;
        this.orientacion = orientacion;
        this.velocidad = velocidad;
        this.ron = ron;
        this.posActual = posActual;
    }
}

class Barrel {

    public int ron;
    public int[] posActual;

    public Barrel(int ron, int[] posActual) {
        this.ron = ron;
        this.posActual = posActual;
    }
}

class CannonBall {

    public int duenio, impacto;
    public int[] posActual;

    public CannonBall(int duenio, int impacto, int[] posActual) {
        this.duenio = duenio;
        this.impacto = impacto;
        this.posActual = posActual;
    }
}

class Mine {

    public int[] posActual;

    public Mine(int[] posActual) {
        this.posActual = posActual;
    }
}
