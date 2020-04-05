
import java.util.*;
import java.io.*;
import java.math.*;

// @author Todos
class Player {

    private static final String MOVE = "MOVE", WAIT = "WAIT",
            SLOWER = "SLOWER", FIRE = "FIRE", MINE = "MINE",
            PORT = "PORT", STARBOARD = "STARBOARD", FASTER = "FASTER";

    private static ArrayList<Ship> barcos = new ArrayList<>();
    private static ArrayList<Ship> enemigos = new ArrayList<>();
    private static ArrayList<Barrel> barriles = new ArrayList<>();
    private static ArrayList<CannonBall> balas = new ArrayList<>();
    private static ArrayList<Mine> minas = new ArrayList<>();

    private static boolean[] ataco = new boolean[]{false, false, false};
    private static int[] attack = new int[]{5, 5, 5};

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            barcos.clear();
            enemigos.clear();
            barriles.clear();
            balas.clear();
            minas.clear();

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
        String accion = "";
        int heur; //apunta al mejor +
        int valorHeurDist = 330, estadoAux;
        Ship barco = barcos.get(idBarco);
        int[] posEnemigo, posBarco = barco.posActual, posAtaque, xyAtaque;
        Ship enemigo;

        //-----movilidad no pasar-----------------------------------------------
        String accionAux = "";
        Barrel barril;
        int mejorEstado;
        int[] sig, xyBarril;
        Random r = new Random();
        mejorEstado = 0;
        int a;
        if (r.nextInt(2) == 0) { //+1
            a = (barco.orientacion++) % 6;
        } else { //-1
            a = barco.orientacion--;
            if (a < 0) {
                a = 5;
            }
        }
        sig = getSiguiente(barco.posActual, a, 3);
        sig = cube_to_oddr(sig);
        accion = MOVE + " " + sig[0] + " " + sig[1];

        barril = mejorBarril(posBarco, idBarco); //busco el barril de menor distancia                
        estadoAux = 0;
        if (barril != null) {
            xyBarril = cube_to_oddr(barril.posActual);
            accionAux = MOVE + " " + xyBarril[0] + " " + xyBarril[1];
            heur = distancia(barril.posActual, posBarco); //entre 1 y 32
            //estadoAux += h + idShip + 5;
            estadoAux += heur;
        }

        if (estadoAux > mejorEstado) {
            accion = accionAux;
            mejorEstado = estadoAux;
        }

        //---- ataque esto queda -----------------------------------------------
        enemigo = enemigoCercano(barco);

        if (!ataco[idBarco]) {
            estadoAux = 0;
            if (enemigo != null) {
                System.err.println("Id enemigo " + enemigo.idBarco);
                int[] aux = cube_to_oddr(enemigo.posActual);
                System.err.println("Enemigo X " + aux[0] + " Y " + aux[1]);
                //System.err.println("Enemigo X " + enemigo.posActual[0] + " Y " + enemigo.posActual[1] + " Z " + enemigo.posActual[2]);
                posEnemigo = enemigo.posActual;

                //estadoAux += heuristicaBarril(barril.getX(), barril.getY(), shipAux.getX(), shipAux.getY());
                heur = distancia(posEnemigo, posBarco);
                int tC = (1 + Math.round((float) heur / 3));
                System.err.println("Dist " + heur + " Tc " + tC);
                //System.err.println("Barco pos X " + barco.posActual[0] + " Y " + barco.posActual[1] + " Z " + barco.posActual[2]);
                posAtaque = calculaAtaque(enemigo, tC, barco.posActual);
                System.err.println("Ataque pos X " + posAtaque[0] + " Y " + posAtaque[1] + " Z " + posAtaque[2]);
                xyAtaque = cube_to_oddr(posAtaque);
                System.err.println("xy Ataque X" + xyAtaque[0] + " Y " + xyAtaque[1]);
                accionAux = FIRE + " " + xyAtaque[0] + " " + xyAtaque[1];
                /*if (mejorEstado == 0) {
                    estadoAux += h + (h - 3) + idShip * 2 + 6; //mas cerca, mas ataca
                } else {                    
                    estadoAux += h + idShip * 2;
                }*/
                attack[idBarco] = 5;
                estadoAux += heur + attack[idBarco];
            }

            if (estadoAux > mejorEstado) {
                accion = accionAux;
                ataco[idBarco] = true;
            }
        } else {
            ataco[idBarco] = false;
            attack[idBarco] = -5;
        }

        return accion;
    }
    
//------- Solo para movilidad BORRAR--------------------------------------------
    private static Barrel mejorBarril(int[] posBarco, int idShip) {
        int cantBarriles = barriles.size(), hBarril = Integer.MAX_VALUE, hBAux;
        int[] xyBarril, xyBarco;
        Barrel barrilAux, mejorB = null;
        for (int i = 0; i < cantBarriles; i++) {
            barrilAux = barriles.get(i);
            if (barrilAux.ron >= 10) {
                hBAux = distancia(barrilAux.posActual, posBarco);

                //si hB > 10 entonces mas rapido, "FASTER"            
                if (hBAux < hBarril && hBAux > 0 && hBAux % (idShip + 1) == 0) { //si esta mas cerca de ese barril, elije ese                
                    hBarril = hBAux;
                    mejorB = barrilAux;
                }
            }
        }
        return mejorB;
    }

//------- Necesario Para ataque-------------------------------------------------
    private static int[] getSiguiente(int[] posBarco, int direccion, int plus) {
        int[] posSiguiente;
        int[][] cube_directions = new int[][]{
            {+1, -1, 0}, {+1, 0, -1}, {0, +1, -1},
            {-1, +1, 0}, {-1, 0, +1}, {0, -1, +1}};
        System.err.println("Orientacion " + direccion);
        int[] sig = cube_directions[direccion];

        if (sig[0] > 0) {
            sig[0] += plus;
        } else if (sig[0] < 0) {
            sig[0] = -(Math.abs(sig[0]) + plus);
        }

        if (sig[1] > 0) {
            sig[1] += plus;
        } else if (sig[1] < 0) {
            sig[1] = -(Math.abs(sig[1]) + plus);
        }

        if (sig[2] > 0) {
            sig[2] += plus;
        } else if (sig[2] < 0) {
            sig[2] = -(Math.abs(sig[2]) + plus);
        }
        //System.err.println("SIG x:" + sig[0] + " y:" + sig[1] + " z:" + sig[2]);        
        //System.err.println("Pos Barco x:" + posBarco[0] + " y:" + posBarco[1] + " z:" + posBarco[2]);

        //sumo la pos y el sig
        posSiguiente = new int[]{posBarco[0] + sig[0], posBarco[1] + sig[1], posBarco[2] + sig[2]};
        //System.err.println("Sig x:" + posSiguiente[0] + " y:" + posSiguiente[1] + " z:" + posSiguiente[2]);

        //int[] aux = cube_to_oddr(posSiguiente);
        //System.err.println("Sig X " + aux[0] + " y " + aux[1]);
        return posSiguiente;
    }

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
        int cantEnemigos = enemigos.size(), hEnemigo = Integer.MAX_VALUE, hEAux;
        int[] posEnemigo, posBarco = miBarco.posActual;
        Ship enemigoAux, mejorE = null;
        for (int i = 0; i < cantEnemigos; i++) {
            enemigoAux = enemigos.get(i);
            posEnemigo = enemigoAux.posActual;
            hEAux = distancia(posEnemigo, posBarco);

            if (hEAux < hEnemigo && hEAux <= 6) { //si esta a 1 de distancia la elige |||||&& hEAux <= 7
                hEnemigo = hEAux;
                mejorE = enemigoAux;
            }
        }
        /*if (mejorE != null) {
            xyEnemigo = new int[]{mejorE.getX(), mejorE.getY()};
            if (heuristicaDireccion(xyMina) == miBarco.getRot()) { //////////////
                peorM = null;
            }
        }*/
        return mejorE;
    }

    private static int heuristica(String accion, int[] pos, Ship barco) {
        int heur = 0;

        return heur;
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
