
import java.util.*;
import java.io.*;
import java.math.*;

// @author Todos
class Player {

    private static final String MOVE = "MOVE", WAIT = "WAIT",
            SLOWER = "SLOWER", FIRE = "FIRE", MINE = "MINE",
            PORT = "PORT", STARBOARD = "STARBOARD", FASTER = "FASTER";

    private static ArrayList<Ship> barcos;
    private static ArrayList<Ship> enemigos;
    private static ArrayList<Barrel> barriles;
    private static ArrayList<CannonBall> balas;
    private static ArrayList<Mine> minas;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            barcos = new ArrayList<>();
            enemigos = new ArrayList<>();
            barriles = new ArrayList<>();
            balas = new ArrayList<>();
            minas = new ArrayList<>();
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
                            System.err.println("TRUE POSITION: " + getPosString(pos));
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
        String accion;
        int heur; //apunta al mejor +
        Ship barco = barcos.get(idBarco);

        //WAIT
        heur = heurWait(barco);
        accion = WAIT;

        //MOVE
        int heurAux;
        int[] posAux;
        for (int i = 0; i < 6; i++) {

            System.err.println(getPosString(getCasilleroRelativo(barco.posActual, i, 1)));

            if (i == barco.orientacion) {
                posAux = getCasilleroRelativo(barco.posActual, i, 2);
                heurAux = heurMove(posAux, barco, true);
                if (heurAux >= heur) {
                    accion = MOVE + " " + getPosString(getCasilleroRelativo(barco.posActual, i, 1));
                    heur = heurAux;
                }
            } else {

                int[] posAux1 = getCasilleroRelativo(barco.posActual, i, 1);
                posAux = getCasilleroRelativo(posAux1, barco.orientacion, barco.velocidad);
                heurAux = heurMove(posAux, barco, false);
                if (heurAux >= heur) {
                    accion = MOVE + " " + getPosString(posAux);
                    heur = heurAux;
                }
            }

            System.err.println(getPosString(posAux) + " da " + heurAux);

        }

        return accion;
    }

    //Heuristicas de los movimientos
    private static int heurWait(Ship barco) {
        return 10;
    }

    private static int heurMove(int[] pos, Ship barco, boolean barcoEstaOrientado) {
        int valor = 0;
        int[] posBin = cube_to_oddr(pos);

        if (posBin[1] >= 0 && posBin[1] <= 21 
                && posBin[0] >= 0 && posBin[0] <= 22
                && !hayEnemigo(pos)) {
            
            int hayBala, ppBala, pnBala,
                    hayMina, ppMina, pnMina,
                    distanciaBarril, ppBarril, pnBarril,
                    distanciaEnemigo, ppEnemigo, pnEnemigo,
                    estaOrientado, ppOrientacion, pnOrientacion;

            hayBala = 0;
            ppBala = 1;
            pnBala = 1;

            hayMina = 0;
            ppMina = 1;
            pnMina = 1;

            distanciaBarril = 34;
            int auxDist;
            for (Barrel barril : barriles) {
                auxDist = distancia(pos, barril.posActual);
                if (distanciaBarril > auxDist) {
                    distanciaBarril = auxDist;
                }
            }
            distanciaBarril = 34 - distanciaBarril;
            ppBarril = 10;
            pnBarril = 1;

            distanciaEnemigo = 0;
            ppEnemigo = 1;
            pnEnemigo = 1;

            estaOrientado = barcoEstaOrientado ? 1 : 0;
            ppOrientacion = 1;
            pnOrientacion = 1;

            valor = -calculoPond(hayBala, ppBala, pnBala)
                    - calculoPond(hayMina, ppMina, pnMina)
                    + calculoPond(distanciaBarril, ppBarril, pnBarril)
                    + calculoPond(distanciaEnemigo, ppEnemigo, pnEnemigo)
                    + calculoPond(estaOrientado, ppOrientacion, pnOrientacion);
        }

        return valor;
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

    private static boolean posEsIgual(int[] a, int[] b) {
        return a.length == b.length && a[0] == b[0] && a[1] == b[1] && a[2] == b[2];
    }

    private static String getPosString(int[] posActual) {
        int[] posOddr = cube_to_oddr(posActual);
        return posOddr[0] + " " + posOddr[1];
    }

    public static int[] getCasilleroRelativo(int[] pos, int orientacion, int xCasillas) {
        xCasillas = Math.abs(xCasillas);
        int[][] cubeDirections = new int[][]{
            {xCasillas, -xCasillas, 0}, {xCasillas, 0, -xCasillas}, {0, xCasillas, -xCasillas},
            {-xCasillas, xCasillas, 0}, {-xCasillas, 0, xCasillas}, {0, -xCasillas, xCasillas}};

        int[] adyacente = new int[3];

        adyacente[0] = pos[0] + cubeDirections[orientacion][0];
        adyacente[1] = pos[1] + cubeDirections[orientacion][1];
        adyacente[2] = pos[2] + cubeDirections[orientacion][2];

        return adyacente;
    }

    private static int calculoPond(int valor, int ponderacionPositiva, int ponderacionNegativa) {
        return (valor * ponderacionPositiva) / ponderacionNegativa;
    }

    private static boolean hayEnemigo(int[] pos) {
        return enemigos.stream().anyMatch((enemigo) -> (posEsIgual(enemigo.posActual, pos)
                || posEsIgual(enemigo.getPopa(), pos)
                || posEsIgual(enemigo.getProa(), pos)));
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

    public int[] getProa() {
        //Frente del barco
        return Player.getCasilleroRelativo(posActual, orientacion, 1);
    }

    public int[] getPopa() {
        //Parte trasera del barco
        return Player.getCasilleroRelativo(posActual, (orientacion + 3) % 6, 1);
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
