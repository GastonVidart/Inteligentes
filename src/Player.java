
import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 *
 */
class Player {

    private static ArrayList<Ship> myBarcos = new ArrayList<Ship>();
    private static ArrayList<Ship> enemies = new ArrayList<Ship>();
    private static ArrayList<Barrel> barrels = new ArrayList<Barrel>();
    private static ArrayList<CannonBall> cannonballs = new ArrayList<CannonBall>();
    private static ArrayList<Mine> mines = new ArrayList<Mine>();
    private static String accionActual;

    private static boolean[] ataco = new boolean[]{false, false, false};
    private static int[] attack = new int[]{5, 5, 5};

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        String entityType;
        int myBarcoCount, entityCount, entityId, x, y, arg1, arg2, arg3, arg4;

        // game loop
        while (true) {
            myBarcos.clear();
            enemies.clear();
            barrels.clear();
            cannonballs.clear();
            mines.clear();
            myBarcoCount = in.nextInt(); // the number of remaining ships
            //System.out.println("cant ships "+myBarcoCount);
            entityCount = in.nextInt(); // the number of entities (e.g. ships, mines or cannonballs)
            //System.out.println("cant entities "+entityCount);
            for (int i = 0; i < entityCount; i++) {
                entityId = in.nextInt();
                //System.out.println("id entity "+entityId);
                entityType = in.next(); //ship o barrel
                //System.out.println("tipo entity "+entityType); 
                x = in.nextInt();
                //System.out.println("pos x entity "+x);
                y = in.nextInt();
                //System.out.println("pos y entity "+y);
                arg1 = in.nextInt();
                //System.out.println("arg 1 "+arg1);
                arg2 = in.nextInt();
                //System.out.println("arg 2 "+arg2);
                arg3 = in.nextInt();
                //System.out.println("arg 3 "+arg3);
                arg4 = in.nextInt();
                //System.out.println("arg 4 "+arg4);

                switch (entityType) {
                    case "BARREL":
                        barrels.add(new Barrel(x, y, arg1));
                        break;
                    case "SHIP":
                        //si lo controlo lo guardo como mi barco, sino enemigo
                        Ship shipAux = new Ship(x, y, arg1, arg2, arg3);
                        if (arg4 == 1) {
                            myBarcos.add(shipAux);
                        } else {
                            enemies.add(shipAux);
                        }
                        break;
                    case "CANNONBALL":
                        cannonballs.add(new CannonBall(x, y, arg1, arg2));
                        break;
                    case "MINE":
                        mines.add(new Mine(x, y));
                        break;
                }
            }

            //System.out.println("WAIT");
            for (int i = 0; i < myBarcoCount; i++) {

                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");
                accionActual = hillClimbing(i);
                System.out.println(accionActual); // Any valid action, such as "WAIT" or "MOVE x y"
            }
        }
    }

    private static String hillClimbing(int idShip) {
        String[] acciones = {"MOVE", "SLOWER", "WAIT", "FIRE", "MINE"};
        String mejorAccion, accionAux = "";
        Barrel barril;
        Mine mina;
        int mejorEstado, estadoAux;
        int[] xyBarril, xyBarco, xyMina, xyAtaque, xyEnemigo, sig;
        Ship shipAux = myBarcos.get(idShip), enemigo;
        xyBarco = shipAux.getXY();

        int h;

        Random r = new Random();
        mejorEstado = 0;
        sig = getSiguiente(xyBarco, r.nextInt(6), 3);
        mejorAccion = acciones[0] + " " + sig[0] + " " + sig[1];

        barril = mejorBarril(xyBarco[0], xyBarco[1], idShip); //busco el barril de menor distancia                
        estadoAux = 0;
        if (barril != null) {
            xyBarril = new int[]{barril.getX(), barril.getY()};
            accionAux = acciones[0] + " " + barril.getX() + " " + barril.getY();
            h = distancia(xyBarril, xyBarco); //entre 1 y 32
            //estadoAux += h + idShip + 5;
            estadoAux += h;
        }

        if (estadoAux > mejorEstado) {
            mejorAccion = accionAux;
            mejorEstado = estadoAux;
        }

        mina = minaCercana(shipAux);
        if (mina != null) {
            xyMina = new int[]{mina.getX(), mina.getY()};                        
            if (r.nextInt(2) == 0) {                
                sig = getSiguiente(xyBarco, shipAux.getRot(), 0);
                accionAux = acciones[0] + " " + (sig[0] + 1) + " " + (sig[1] + 1);
            } else {
                sig = getSiguiente(xyBarco, shipAux.getRot(), -2);
                accionAux = acciones[0] + " " + (sig[0] + 1) + " " + (sig[1] + 1);
            }
            //estadoAux += heuristicaBarril(barril.getX(), barril.getY(), shipAux.getX(), shipAux.getY());
            estadoAux += 100; //suma bastante para que no busque a ningun barril, prioriza esquivar la mina
        }

        if (estadoAux > mejorEstado) {
            mejorAccion = accionAux;
        }

        enemigo = enemigoCercano(shipAux);
        if (!ataco[idShip]) {
            estadoAux = 0;
            if (enemigo != null) {
                xyEnemigo = enemigo.getXY();

                //estadoAux += heuristicaBarril(barril.getX(), barril.getY(), shipAux.getX(), shipAux.getY());
                h = distancia(xyEnemigo, xyBarco);
                xyAtaque = calculaAtaque(enemigo, h - 2, shipAux.getXY());
                accionAux = acciones[3] + " " + xyAtaque[0] + " " + xyAtaque[1];
                /*if (mejorEstado == 0) {
                    estadoAux += h + (h - 3) + idShip * 2 + 6; //mas cerca, mas ataca
                } else {                    
                    estadoAux += h + idShip * 2;
                }*/
                attack[idShip] = 5;
                estadoAux += h + attack[idShip];
            }

            if (estadoAux > mejorEstado) {
                mejorAccion = accionAux;
                mejorEstado = estadoAux;
                ataco[idShip] = true;
            }
        } else {
            ataco[idShip] = false;
            attack[idShip] = -5;
        }

        return mejorAccion;
    }

    
    private static Barrel mejorBarril(int xBarco, int yBarco, int idShip) {
        int cantBarriles = barrels.size(), hBarril = Integer.MAX_VALUE, hBAux;
        int[] xyBarril, xyBarco;
        Barrel barrilAux, mejorB = null;
        for (int i = 0; i < cantBarriles; i++) {
            barrilAux = barrels.get(i);
            if (barrilAux.getRon() >= 10) {
                xyBarril = new int[]{barrilAux.getX(), barrilAux.getY()};
                xyBarco = new int[]{xBarco, yBarco};
                hBAux = distancia(xyBarril, xyBarco);

                //si hB > 10 entonces mas rapido, "FASTER"            
                if (hBAux < hBarril && hBAux > 0 && hBAux % (idShip + 1) == 0) { //si esta mas cerca de ese barril, elije ese                
                    hBarril = hBAux;
                    mejorB = barrilAux;
                }
            }
        }
        return mejorB;
    }

    private static Mine minaCercana(Ship barco) {
        int cantMinas = mines.size(), hMina = Integer.MAX_VALUE, hMAux;
        int[] xyMina, xyBarco = barco.getXY();
        Mine minaAux, peorM = null;
        for (int i = 0; i < cantMinas; i++) {
            minaAux = mines.get(i);
            xyMina = new int[]{minaAux.getX(), minaAux.getY()};
            hMAux = distancia(xyMina, xyBarco);

            if (hMAux < hMina && hMAux <= 3 && enFrente(xyMina, xyBarco, i)) { //si esta a 1 de distancia y esta en frente la elige
                hMina = hMAux;
                peorM = minaAux;
            }
        }
        /*if (peorM != null) {
            xyMina = new int[]{peorM.getX(), peorM.getY()};
            if (enFrente(xyMina, xyBarco, barco.getRot())) {

            }
        }*/
        return peorM;
    }

    private static Ship enemigoCercano(Ship miBarco) {
        int cantEnemigos = enemies.size(), hEnemigo = Integer.MAX_VALUE, hEAux;
        int[] xyEnemigo, xyBarco = miBarco.getXY();
        Ship enemigoAux, mejorE = null;
        for (int i = 0; i < cantEnemigos; i++) {
            enemigoAux = enemies.get(i);
            xyEnemigo = enemigoAux.getXY();
            hEAux = distancia(xyEnemigo, xyBarco);

            if (hEAux < hEnemigo && hEAux <= 7) { //si esta a 1 de distancia la elige
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

    private static int[] getSiguiente(int[] xyBarco, int direccion, int plus) {
        int[] xyPos, barcoC, ataqueC;
        int[][] cube_directions = new int[][]{
            {+1, -1, 0}, {+1, 0, -1}, {0, +1, -1},
            {-1, +1, 0}, {-1, 0, +1}, {0, -1, +1}};
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
        //System.out.println("SIG x:" + sig[0] + " y:" + sig[1] + " z:" + sig[2]);
        barcoC = offset_to_cube(xyBarco);
        //System.out.println("EnemigoC x:" + eneC[0] + " y:" + eneC[1] + " z:" + eneC[2]);
        //sumo la pos y el sig
        ataqueC = new int[]{barcoC[0] + sig[0], barcoC[1] + sig[1], barcoC[2] + sig[2]};
        //System.out.println("AtaqueC x:" + ataqueC[0] + " y:" + ataqueC[1] + " z:" + ataqueC[2]);
        xyPos = cube_to_oddr(ataqueC);
        //System.out.println("ataque x:" + xyAtaque[0] + " y:" + xyAtaque[1]);
        return xyPos;
    }

    private static int[] calculaAtaque(Ship enemigo, int distancia, int[] xyBarco) {
        int[] xyAtaque, xyEnemigo = enemigo.getXY();
        if (enemigo.getVel() > 0) {
            xyAtaque = getSiguiente(enemigo.getXY(), enemigo.getRot(), distancia);
        } else {
            xyAtaque = new int[]{xyEnemigo[0], xyEnemigo[1]};
        }
        if (autoAtaque(xyAtaque, xyBarco)) {
            xyAtaque = new int[]{xyEnemigo[0], xyEnemigo[1]};
        }
        return xyAtaque;
    }

    private static int heuristicaDireccion(int[] xyMina) {
        int direccion, difQ, difR, difS;
        difQ = xyMina[0] - xyMina[2];
        difR = xyMina[2] - xyMina[1];
        difS = xyMina[1] - xyMina[0];
        direccion = Math.max(Math.abs(difQ), Math.max(Math.abs(difR), Math.abs(difS)));
        if (direccion == Math.abs(difQ)) {
            if (difQ > 0) {
                direccion = 1;
            } else {
                direccion = 4;
            }
        } else if (direccion == Math.abs(difR)) {
            if (difR > 0) {
                direccion = 5;
            } else {
                direccion = 2;
            }
        } else if (direccion == Math.abs(difS)) {
            if (difS > 0) {
                direccion = 3;
            } else {
                direccion = 0;
            }
        }
        return direccion;
    }

    private static int distancia(int[] a, int[] b) {
        //distancia real en mapa HEX, convierte de OFFSET que usa el mapa a CUBO        
        int[] ac, bc;
        ac = offset_to_cube(a);
        bc = offset_to_cube(b);
        return cube_distance(ac, bc);
    }

    private static int cube_distance(int[] a, int[] b) {
        return (Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]) + Math.abs(a[2] - b[2])) / 2;
    }

    private static int[] offset_to_cube(int[] hex) {
        //el mapa usa odd-r, convierte a cube desde offset
        int[] cubo = new int[3];
        cubo[0] = hex[0] - (hex[1] - (hex[1] & 1)) / 2; //x
        cubo[2] = hex[1]; //z
        cubo[1] = -cubo[0] - cubo[2]; //y
        return cubo;
    }

    private static int[] cube_to_oddr(int[] cube) {
        int col = cube[0] + (cube[2] - (cube[2] & 1)) / 2;
        int row = cube[2];
        return new int[]{col, row};
    }

    

    private static boolean autoAtaque(int[] xyAtaque, int[] xyBarco) {
        boolean exito = false;
        int[] balaC = offset_to_cube(xyAtaque), barcoC = offset_to_cube(xyBarco);
        if ((balaC[0] == barcoC[0] && balaC[1] == barcoC[1] && balaC[2] == barcoC[2])
                || (balaC[0] + 1 == barcoC[0] + 1 && balaC[1] + 1 == barcoC[1] + 1 && balaC[2] + 1 == barcoC[2] + 1)
                || (balaC[0] - 1 == barcoC[0] - 1 && balaC[1] - 1 == barcoC[1] - 1 && balaC[2] - 1 == barcoC[2] - 1)) {
            exito = true;
        }
        return exito;
    }

    private static boolean enFrente(int[] xyMina, int[] xyBarco, int direccion) {
        boolean exito = false;
        int[] minaC = offset_to_cube(xyMina), barcoC = offset_to_cube(xyBarco);
        switch (direccion) {
            case 0:
            case 3:
                exito = minaC[2] == barcoC[2];     //si el z es igual, esta en la mira            
                break;
            case 1:
            case 4:
                exito = minaC[1] == barcoC[1];     //si el y es igual, esta en la mira            
                break;
            case 2:
            case 5:
                exito = minaC[0] == barcoC[0];     //si el x es igual, esta en la mira 
                break;
        }
        return exito;
    }
}

class Ship {

    private int x, y, rot, vel, ron;

    public Ship(int x, int y, int rotacion, int velocidad, int cant) {
        this.x = x;
        this.y = y;
        this.rot = rotacion;
        this.vel = velocidad;
        this.ron = cant;
    }

    public int[] getXY() {
        return new int[]{this.x, this.y};
    }

    public int getRot() {
        return this.rot;
    }

    public int getRon() {
        return this.ron;
    }

    public int getVel() {
        return this.vel;
    }
}

class Barrel {

    private int x, y, cantRon;

    public Barrel(int x, int y, int cant) {
        this.x = x;
        this.y = y;
        this.cantRon = cant;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getRon() {
        return this.cantRon;
    }
}

class CannonBall {

    private int x, y, duenio, impacto;

    public CannonBall(int x, int y, int duenio, int impacto) {
        this.x = x;
        this.y = y;
        this.duenio = duenio;
        this.impacto = impacto;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getDuenio() {
        return this.duenio;
    }

    public int getImacto() {
        return this.impacto;
    }
}

class Mine {

    private int x, y;

    public Mine(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
