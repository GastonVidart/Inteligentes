
/**
 *
 * @author Gastón
 */
public class PruebaCubo {

    public static void main(String[] args) {
        int[] a = {3, 4}, b = {3, 3}, at;
        int dist = heuristicaDistancia(a, b);
        System.out.println(dist);

        //direccion(a, b);
        //System.out.println(direccion(a,b));
        at = calculaAtaque(b, 2, dist+1);
        System.out.println("Ax=" + at[0] + " // Ay=" + at[1]);

    }

    private static int heuristicaDistancia(int[] a, int[] b) {
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

    private static void direccion(int[] a, int[] b) {
        int[] ac, bc;
        int direccion, difX, difY, difZ, difQ, difR, difS;
        ac = offset_to_cube(a);
        bc = offset_to_cube(b);
        /*difX = bc[0] - ac[0];
        difY = bc[1] - ac[1];
        difZ = bc[2] - ac[2];*/
        //System.out.println("X/Q " + difX + " || Y/S " + difY + " || Z/R " + difZ);
        //return direccion;        
        difQ = ac[0] - ac[2];
        difR = ac[2] - ac[1];
        difS = ac[1] - ac[0];
        System.out.println("A DQ " + difQ + " || DR " + difR + " || DS " + difS);
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
        System.out.println("A " + direccion);

        difQ = bc[0] - bc[2];
        difR = bc[2] - bc[1];
        difS = bc[1] - bc[0];
        System.out.println("B DQ " + difQ + " || DR " + difR + " || DS " + difS);
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
        System.out.println("B " + direccion);
    }

    /*private static int[] calculaAtaque(int[] enemigo, int rot, int distancia) {
        int paridad = enemigo[1] & 1;
        int[][][] oddr_directions = new int[][][]{
            {{+1, 0}, {0, -1}, {-1, -1}, {-1, 0}, {-1, +1}, {0, +1}},
            {{+1, 0}, {+1, -1}, {0, -1}, {-1, 0}, {0, +1}, {+1, +1}}};
        int[] dir = oddr_directions[paridad][rot], xyAtaque = new int[2];

        if (dir[0] > 0) {
            dir[0] += distancia;
        } else if (dir[0] < 0) {
            dir[0] = -(Math.abs(dir[0]) + distancia);
        }

        if (dir[1] > 0) {
            dir[1] += distancia;
        } else if (dir[1] < 0) {
            dir[1] = -(Math.abs(dir[1]) + distancia);
        }
        xyAtaque[0] = enemigo[0] + dir[0];
        xyAtaque[1] = enemigo[1] + dir[1];
        return xyAtaque;
    }*/
    private static int[] calculaAtaque(int[] enemigo, int rot, int distancia) {
        int[] xyAtaque = new int[2], dir, desp, eneC, ataqueC;

        int[][] cube_directions = new int[][]{
            {+1, -1, 0}, {+1, 0, -1}, {0, +1, -1},
            {-1, +1, 0}, {-1, 0, +1}, {0, -1, +1}};
        int[] sig = cube_directions[rot];

        if (sig[0] > 0) {
            sig[0] += distancia;
        } else if (sig[0] < 0) {
            sig[0] = -(Math.abs(sig[0]) + distancia);
        }

        if (sig[1] > 0) {
            sig[1] += distancia;
        } else if (sig[1] < 0) {
            sig[1] = -(Math.abs(sig[1]) + distancia);
        }

        if (sig[2] > 0) {
            sig[2] += distancia;
        } else if (sig[2] < 0) {
            sig[2] = -(Math.abs(sig[2]) + distancia);
        }
        System.out.println("SIG x:" + sig[0] + " y:" + sig[1] + " z:" + sig[2]);
        eneC = offset_to_cube(enemigo);
        System.out.println("EnemigoC x:" + eneC[0] + " y:" + eneC[1] + " z:" + eneC[2]);
        //sumo la pos y el sig
        ataqueC = new int[]{eneC[0]+sig[0], eneC[1] + sig[1], eneC[2] + sig[2]};
        System.out.println("AtaqueC x:" + ataqueC[0] + " y:" + ataqueC[1] + " z:" + ataqueC[2]);
        xyAtaque = cube_to_oddr(ataqueC);
        System.out.println("ataque x:" + xyAtaque[0] + " y:" + xyAtaque[1]);
        return xyAtaque;
    }

    private static int[] cube_to_oddr(int[] cube) {
        int col = cube[0] + (cube[2] - (cube[2] & 1)) / 2;
        int row = cube[2];
        return new int[]{col, row};
    }

}
