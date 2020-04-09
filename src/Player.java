
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

    private static boolean[] ataco = new boolean[]{false, false, false};
    private static int[] attack = new int[]{5, 5, 5};

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

            int idRelativo = 0;
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
                            shipAux.idRelativo=idRelativo;
                            idRelativo++;
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

        //--Frente
        posAux = getCasilleroRelativo(barco.posActual, barco.orientacion, 1);//REVISAR OFFSET
        heurAux = heurMove(posAux, barco, true);

        //System.err.println("En: " + getPosString(posAux) + " da " + heurAux);
        if (heurAux >= heur) {
            accion = MOVE + " " + getPosString(getCasilleroRelativo(posAux, barco.orientacion, 2));
            heur = heurAux;
        }

        //--Rotación
        for (int i = -1; i < 2; i = i + 2) {
            int[] posAux1 = getCasilleroRelativo(barco.posActual, (barco.orientacion + 6 + i) % 6, 1);
            posAux = getCasilleroRelativo(posAux1, barco.orientacion, barco.velocidad);
            heurAux = heurMove(posAux, barco, false);

            //System.err.println("En: " + getPosString(posAux) + " da " + heurAux);
            if (heurAux >= heur) {
                accion = MOVE + " " + getPosString(posAux);//ROTAR
                heur = heurAux;
            }
        }

        //--Frenar
        if (barco.velocidad > 0) {
            for (int i = -1; i < 2; i = i + 2) {
                int[] posAux1 = getCasilleroRelativo(barco.posActual, (barco.orientacion + 6 + i) % 6, 1);
                posAux = getCasilleroRelativo(posAux1, barco.orientacion, barco.velocidad - 1);
                heurAux = heurMove(posAux, barco, false);

                //System.err.println("En: " + getPosString(posAux) + " da " + heurAux);
                if (heurAux >= heur) {
                    accion = SLOWER;
                    heur = heurAux;
                }
            }
        }
        
        //FIRE
        

        //WAIT
        heur = heurWait(barco);
        accion = WAIT;

        //MOVE
        int heurAux;
        int[] posAux;

        //--Frente
        posAux = getCasilleroRelativo(barco.posActual, barco.orientacion, 1);//REVISAR OFFSET
        heurAux = heurMove(posAux, barco, true);

        //System.err.println("En: " + getPosString(posAux) + " da " + heurAux);
        if (heurAux >= heur) {
            accion = MOVE + " " + getPosString(getCasilleroRelativo(posAux, barco.orientacion, 2));
            heur = heurAux;
        }

        //--Rotación
        for (int i = -1; i < 2; i = i + 2) {
            int[] posAux1 = getCasilleroRelativo(barco.posActual, (barco.orientacion + 6 + i) % 6, 1);
            posAux = getCasilleroRelativo(posAux1, barco.orientacion, barco.velocidad);
            heurAux = heurMove(posAux, barco, false);

            //System.err.println("En: " + getPosString(posAux) + " da " + heurAux);
            if (heurAux >= heur) {
                accion = MOVE + " " + getPosString(posAux);//ROTAR
                heur = heurAux;
            }
        }

        //--Frenar
        if (barco.velocidad > 0) {
            for (int i = -1; i < 2; i = i + 2) {
                int[] posAux1 = getCasilleroRelativo(barco.posActual, (barco.orientacion + 6 + i) % 6, 1);
                posAux = getCasilleroRelativo(posAux1, barco.orientacion, barco.velocidad - 1);
                heurAux = heurMove(posAux, barco, false);

                //System.err.println("En: " + getPosString(posAux) + " da " + heurAux);
                if (heurAux >= heur) {
                    accion = SLOWER;
                    heur = heurAux;
                }
            }
        }

        //FIRE
        Ship enemigo;
        int[] xyAtaque, posAtaque;
        enemigo = enemigoCercano(barco);
        if (enemigo != null) {
            int valorDist = distancia(enemigo.posActual, barco.posActual);
            heurAux = heurFire(barco, enemigo, valorDist, idBarco);
            System.err.println("Ataque "+heurAux+ " Ship "+idBarco);
            if (heurAux >= heur) {
                ataco[idBarco] = true;
                int offset = (1 + Math.round((float) valorDist / 3));
                posAtaque = calculaAtaque(enemigo, offset, barco.posActual);
                xyAtaque = cube_to_oddr(posAtaque);
                accion = FIRE + " " + xyAtaque[0] + " " + xyAtaque[1];
                ataco[idBarco] = true;
                //System.err.println("Dist " + valorDist + " Tc " + offset);
                //System.err.println("Barco pos X " + barco.posActual[0] + " Y " + barco.posActual[1] + " Z " + barco.posActual[2]);
                //System.err.println("Ataque pos X " + posAtaque[0] + " Y " + posAtaque[1] + " Z " + posAtaque[2]);
                //System.err.println("xy Ataque X" + xyAtaque[0] + " Y " + xyAtaque[1]);            
            } else {
                ataco[idBarco] = false;
            }
        }
        return accion;
    }

//------- Necesario Para ataque-------------------------------------------------   
    private static int[] calculaAtaque(Ship enemigo, int distancia, int[] posBarco) {
        int[] posAtaque, posEnemigo = enemigo.posActual;
        if (enemigo.velocidad > 0) {
            System.err.println("vel > 0 || = " + enemigo.velocidad);
            posAtaque = getSiguiente(enemigo.posActual, enemigo.orientacion, distancia);
        } else {
            System.err.println("vel = 0");
            posAtaque = new int[]{posEnemigo[0], posEnemigo[1], posEnemigo[2]};
            System.err.println("Atq x:" + posAtaque[0] + " y:" + posAtaque[1] + " z:" + posAtaque[2]);
        }
        if (autoAtaque(posAtaque, posBarco)) {
            System.err.println("auto Atq");
            posAtaque = new int[]{posEnemigo[0], posEnemigo[1], posEnemigo[2]};
        }
        return posAtaque;
    }

    private static boolean autoAtaque(int[] posAtaque, int[] posBarco) {
        boolean exito = false;
        if ((posAtaque[0] == posBarco[0] && posAtaque[1] == posBarco[1] && posAtaque[2] == posBarco[2])
                || (posAtaque[0] + 1 == posBarco[0] + 1 && posAtaque[1] + 1 == posBarco[1] + 1 && posAtaque[2] + 1 == posBarco[2] + 1)
                || (posAtaque[0] - 1 == posBarco[0] - 1 && posAtaque[1] - 1 == posBarco[1] - 1 && posAtaque[2] - 1 == posBarco[2] - 1)) {
            System.err.println("Me ataco a mi mismo");
            exito = true;
        }
        return exito;
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

    //Heuristicas de los movimientos
    private static int heurWait(Ship barco) {
        return 10;
    }

    private static int heurCasilleroFijo(int[] pos, Ship barco) {
        int valor = 0;
        int[] posBin = cube_to_oddr(pos);
        if (posBin[1] >= 0 && posBin[1] <= 21
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
        }

        return valor;
    }

    private static int heurMove(int[] pos, Ship barco, boolean barcoEstaOrientado) {
        int valor = heurCasilleroFijo(pos, barco);
        int hayBalaF, ppBalaF, pnBalaF,
                hayMinaF, ppMinaF, pnMinaF;

        hayBalaF = 0;
        ppBalaF = 1;
        pnBalaF = 1;

        hayMinaF = 0;
        ppMinaF = 1;
        pnMinaF = 1;

        if (barcoEstaOrientado) {
            if (!hayEnemigo(getCasilleroRelativo(pos, barco.orientacion, 1))) {
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

        if (valor != 0 && !hayEnemigo(pos)) {
            int hayBala, ppBala, pnBala,
                    distanciaEnemigo, ppEnemigo, pnEnemigo,
                    estaOrientado, ppOrientacion, pnOrientacion;

            hayBala = 0;
            ppBala = 1;
            pnBala = 1;

            distanciaEnemigo = 0;
            ppEnemigo = 1;
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

    private static int heurFire(Ship barco, Ship enemigo, int valorDist, int idBarco) {
        int valor = 0;        
        if (!ataco[idBarco] && valorDist <= 6) {
            //System.err.println("");
            valor = 350; //+ valorDist; 
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

    public int idBarco, orientacion, velocidad, ron, idRelativo;
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
