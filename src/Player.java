
import java.util.*;
import java.io.*;
import java.math.*;

// @author Todos
class Player {

    private static final String MOVE = "MOVE", WAIT = "WAIT",
            SLOWER = "SLOWER", FIRE = "FIRE", MINE = "MINE",
            PORT = "PORT", STARBOARD = "STARBOARD", FASTER = "FASTER";

    private static final String[] ACCIONES = {WAIT, SLOWER, FIRE, MINE, PORT, STARBOARD, FASTER};

    private static ArrayList<Ship> barcos;
    private static ArrayList<Ship> enemigos;
    private static ArrayList<Barrel> barriles;
    private static ArrayList<CannonBall> balas;
    private static ArrayList<Mine> minas;

    private static boolean[] ataco = new boolean[]{false, false, false};

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
                barcos.get(i).idRelativo = i;
                String orden;
                orden = hillClimbing(i);
                //orden = simulated(i);
                mostrarMensaje(barcos.get(i), "--------------------------------------");
                System.out.println(orden); // Any valid action, such as "WAIT" or "MOVE x y"
            }
        }
    }

    private static String hillClimbing(int idBarco) {
        Ship barco = barcos.get(idBarco);
        String orden = WAIT;
        int heuristicaOrdenActual = Integer.MIN_VALUE, heuristicaAux;

        for (String ACCION : ACCIONES) {

            heuristicaAux = heuristica(ACCION, barco);
            if (heuristicaAux >= heuristicaOrdenActual) {
                if (ACCION.equals(FIRE)) {
                    orden = ACCION + " " + posToString(barco.posObjetivo);
                } else {
                    orden = ACCION;
                }
                heuristicaOrdenActual = heuristicaAux;
            }
        }

        return orden;
    }

    private static String simulated(int idBarco) {
        Ship barco = barcos.get(idBarco);
        String orden = WAIT;
        
        return orden;
    }

    private static int heuristica(String accion, Ship barco) {
        switch (accion) {
            case WAIT:
                return hWait(barco);
            case SLOWER:
                return hSlower(barco);
            case FASTER:
                return hFaster(barco);
            case PORT:
                return hPort(barco);
            case STARBOARD:
                return hStarboard(barco);
            case MINE:
                return hMine(barco);
            case FIRE:
                return hFire(barco);
        }
        return 0;
    }

    // Herramientas -----------------------------------
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

    public static int[] posRelativa(int[] pos, int orientacion, int xCasillas) {
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

    private static String posToString(int[] posActual) {
        int[] posOddr = cube_to_oddr(posActual);
        return posOddr[0] + " " + posOddr[1];
    }

    public static void mostrarMensaje(Ship barco, String mensaje) {
        System.err.println(barco.idRelativo + " en: " + posToString(barco.posActual) + " -- " + mensaje);
    }

    public static void mostrarAccion(Ship barco, String accion, int heuristica) {
        mostrarMensaje(barco, accion + ": " + heuristica);
    }

    private static int heurMove(int[] pos, Ship barco, boolean barcoEstaOrientado) {
        int valor = heurCasilleroFijo(pos, barco);
        int hayBalaF, ppBalaF, pnBalaF,
                hayMinaF, ppMinaF, pnMinaF;//Verifica balas o minas en el frente del barco

        hayBalaF = 0;
        ppBalaF = 1;
        pnBalaF = 1;

        hayMinaF = 0;
        ppMinaF = 1;
        pnMinaF = 1;

        if (barcoEstaOrientado) {
            if (!hayBarco(posRelativa(pos, barco.orientacion, 1), barco)) {
                hayBalaF = 0;
                ppBalaF = 1;
                pnBalaF = 1;

                hayMinaF = 0;
                ppMinaF = 1;
                pnMinaF = 1;
            } else {
                valor = 0;
            }
        }

        if (valor != -1 && !hayBarco(pos, barco)) {
            int hayBala, ppBala, pnBala,
                    distanciaEnemigo, ppEnemigo, pnEnemigo,
                    estaOrientado, ppOrientacion, pnOrientacion;

            if (balas.stream().anyMatch((bala) -> (bala.posActual == pos && bala.impacto == 1))) {
                if (!barcoEstaOrientado && barco.velocidad == 0
                        && balas.stream().anyMatch((bala) -> (bala.posActual == barco.posActual && bala.impacto == 1))) {
                    if (barco.ron <= 50) {
                        return 0;//La bala mata
                    } else {
                        hayBala = -200;
                    }
                } else if (barco.ron <= 50) {
                    return 0;//La bala mata
                } else {
                    return 0;
                }
            } else {
                hayBala = 0;
            }
            ppBala = 1;
            pnBala = 1;

            if (barriles.isEmpty()) {
                distanciaEnemigo = distancia(pos, enemigoCercano(barco).posActual);
                mostrarMensaje(barco, posToString(enemigoCercano(barco).posActual));
                if (distanciaEnemigo < 3) {
                    distanciaEnemigo = 0;
                } else {
                    distanciaEnemigo = 34 - distanciaEnemigo;
                }
            } else {
                distanciaEnemigo = 0;
            }
            ppEnemigo = 10;
            pnEnemigo = 1;

            estaOrientado = barcoEstaOrientado ? 1 : 0;
            ppOrientacion = 1;
            pnOrientacion = 1;

            valor += -calculoPond(hayBala, ppBala, pnBala)
                    - calculoPond(hayBalaF, ppBalaF, pnBalaF)
                    - calculoPond(hayMinaF, ppMinaF, pnMinaF)
                    + calculoPond(distanciaEnemigo, ppEnemigo, pnEnemigo)
                    + calculoPond(estaOrientado, ppOrientacion, pnOrientacion);
        } else {
            valor = 0;
        }

        return valor;
    }

    private static Ship enemigoCercano(Ship miBarco) {
        int cantEnemigos = enemigos.size(), distancia = Integer.MAX_VALUE, distanciaAux;
        int[] posBarco = miBarco.posActual;
        Ship enemigoAux, mejorE = null;
        for (int i = 0; i < cantEnemigos; i++) {
            enemigoAux = enemigos.get(i);
            distanciaAux = distancia(enemigoAux.posActual, posBarco);

            if (distanciaAux < distancia) { //si esta a 1 de distancia la elige |||||&& hEAux <= 6
                distancia = distanciaAux;
                mejorE = enemigoAux;
            }
        }
        return mejorE;
    }

    private static int heurCasilleroFijo(int[] pos, Ship barco) {
        int valor;
        int[] posBin = cube_to_oddr(pos);
        if (posBin[1] >= 0 && posBin[1] <= 20
                && posBin[0] >= 0 && posBin[0] <= 22) {

            int hayMina, ppMina, pnMina,
                    distanciaBarril, ppBarril, pnBarril;

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

            valor = -calculoPond(hayMina, ppMina, pnMina)
                    + calculoPond(distanciaBarril, ppBarril, pnBarril);
        } else {
            valor = -1;
        }

        return valor;
    }

    private static boolean hayBarco(int[] pos, Ship barco) {
        return (enemigos.stream().anyMatch((enemigo) -> (posEsIgual(enemigo.posActual, pos)
                || posEsIgual(enemigo.getPopa(), pos)
                || posEsIgual(enemigo.getProa(), pos))))
                || barcos.stream().anyMatch((nave) -> (nave.idBarco != barco.idBarco
                && (posEsIgual(pos, nave.posActual)
                || posEsIgual(pos, nave.getPopa())
                || posEsIgual(pos, nave.getProa()))));
    }

    private static int calculoPond(int valor, int ponderacionPositiva, int ponderacionNegativa) {
        return (valor * ponderacionPositiva) / ponderacionNegativa;
    }

    private static Mine obtenerMinaCercana(int[] miPos) {
        Mine cercana = null;
        int distanciaMina = Integer.MAX_VALUE, distanciaActual;

        for (int i = 0; i < minas.size(); i++) {
            Mine minaAux = minas.get(i);

            distanciaActual = distancia(miPos, minaAux.posActual);
            if (distanciaActual < distanciaMina
                    && !balas.stream().anyMatch((bala) -> (posEsIgual(bala.posActual, minaAux.posActual)))) {

                distanciaMina = distanciaActual;
                cercana = minaAux;
            }
        }
        return cercana;

    }

    private static int[] calculaAtaque(Ship enemigo, int distancia, Ship barco) {
        int[] posAtaque;
        if (enemigo.velocidad > 0) {
            posAtaque = posRelativa(enemigo.posActual, enemigo.orientacion, distancia);
        } else {
            posAtaque = enemigo.posActual;
        }
        if (posEsIgual(posAtaque, barco.posActual) || posEsIgual(posAtaque, barco.getPopa()) || posEsIgual(posAtaque, barco.getProa())) {

            posAtaque = enemigo.posActual;
        }
        return posAtaque;
    }

    // Heuristicas -------------------------------------------------------------------
    private static int hWait(Ship barco) {
        mostrarAccion(barco, WAIT, 1);
        return 1;
    }

    private static int hSlower(Ship barco) {
        int heuristica = 0;
        if (barco.velocidad > 0) {
            int[] posAuxI, posAuxD;

            posAuxI = posRelativa(posRelativa(barco.posActual, (barco.orientacion + 5) % 6, 1),
                    barco.orientacion, barco.velocidad - 1);
            posAuxD = posRelativa(posRelativa(barco.posActual, (barco.orientacion + 7) % 6, 1),
                    barco.orientacion, barco.velocidad - 1);

            heuristica = Math.max(heurMove(posAuxI, barco, false), heurMove(posAuxD, barco, false));
        }
        mostrarAccion(barco, SLOWER, heuristica);
        return heuristica;
    }

    private static int hFaster(Ship barco) {
        int heuristica = heurMove(posRelativa(barco.posActual, barco.orientacion, (barco.velocidad == 0 ? 1 : barco.velocidad)), barco, true);
        mostrarAccion(barco, FASTER, heuristica);
        return heuristica;
    }

    private static int hPort(Ship barco) {
        int heuristica;

        int[] posAux = posRelativa(posRelativa(barco.posActual, (barco.orientacion + 7) % 6, 1),
                barco.orientacion, barco.velocidad);
        heuristica = heurMove(posAux, barco, false);

        if (distancia(enemigoCercano(barco).posActual, barco.posActual) < 7) { //maniobras evasivas
            heuristica += 60 * (barco.velocidad == 0 ? 0 : 1);
        } else {
            heuristica += 0;
        }

        mostrarAccion(barco, PORT, heuristica);
        return heuristica;
    }

    private static int hStarboard(Ship barco) {
        int heuristica;

        int[] posAux = posRelativa(posRelativa(barco.posActual, (barco.orientacion + 5) % 6, 1),
                barco.orientacion, barco.velocidad);
        heuristica = heurMove(posAux, barco, false);

        if (distancia(enemigoCercano(barco).posActual, barco.posActual) < 7) { //maniobras evasivas
            heuristica += 60 * (barco.velocidad == 0 ? 0 : 1);
        } else {
            heuristica += 0;
        }

        mostrarAccion(barco, STARBOARD, heuristica);
        return heuristica;
    }

    private static int hMine(Ship barco) {
        int heuristica = 0;

        mostrarAccion(barco, MINE, heuristica);
        return heuristica;
    }

    private static int hFire(Ship barco) {
        int heuristica = 0;
        Ship enemigo;
        enemigo = enemigoCercano(barco);
        boolean yaDisparo = ataco[barco.idRelativo];

        if (!yaDisparo) {
            int valorDist = distancia(enemigo.posActual, barco.posActual);
            if (valorDist <= 6) {
                //Dispara a un Enemigo
                heuristica = 400;
                ataco[barco.idRelativo] = true;
                int offset = (1 + Math.round((float) valorDist / 3)) * (enemigo.velocidad == 0 ? 1 : enemigo.velocidad);
                int[] posAtaque = calculaAtaque(enemigo, offset, barco);
                barco.posObjetivo = posAtaque;
            } else {
                //Dispara a Mina
                Mine minaCercana;
                minaCercana = obtenerMinaCercana(barco.posActual);
                valorDist = distancia(enemigo.posActual, barco.posActual);
                if (minaCercana != null && valorDist <= 6 && valorDist > 2) {
                    heuristica = 300;
                    ataco[barco.idRelativo] = true;
                    barco.posObjetivo = minaCercana.posActual;
                }
            }
        } else {
            ataco[barco.idRelativo] = false;
        }

        mostrarAccion(barco, FIRE, heuristica);
        return heuristica;
    }

}

// POSICIONES X=0 Y=1 Z=2 -----------------------------------
class Ship {

    public boolean evasion = true;
    public int idBarco, orientacion, velocidad, ron, idRelativo;
    public int[] posActual, posObjetivo;

    public Ship(int idBarco, int orientacion, int velocidad, int ron, int[] posActual) {
        this.idBarco = idBarco;
        this.orientacion = orientacion;
        this.velocidad = velocidad;
        this.ron = ron;
        this.posActual = posActual;
    }

    public int[] getProa() {
        //Frente del barco
        return Player.posRelativa(posActual, orientacion, 1);
    }

    public int[] getPopa() {
        //Parte trasera del barco
        return Player.posRelativa(posActual, orientacion, -1);
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
