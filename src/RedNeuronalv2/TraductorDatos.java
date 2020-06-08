package RedNeuronalv2;

import java.util.ArrayList;
import java.util.HashMap;

// @author guido
public class TraductorDatos {

    ArrayList<Integer> rangos;
    HashMap<Object, double[]> indicesValores;
    int cantEntradas;

    public TraductorDatos() {
        rangos = new ArrayList<>();
        indicesValores = new HashMap<>();
        this.cantEntradas = 0;
    }

    /**
     * Agrega y mapea todas las entradas hacia indices enteros
     *
     * @param valores : arreglo con todos los diferentes valores de entrada
     */
    public void addEntrada(Object[] valores) {
        int cantBit = (int) Math.ceil(Math.log10(valores.length) / Math.log10(2));
        this.cantEntradas += cantBit;
        rangos.add(cantBit);
        for (int i = 0; i < valores.length; i++) {
            //TODO: mejorar la clave
            indicesValores.put(valores[i], transformarBinario(i, cantBit));
        }
    }

    /**
     * Agrega y mapea todas las entradas de una serie continua de enteros
     *
     * @param min : valor minimo del rango de enteros
     * @param max : valor maximo del rango de enteros (se toma hasta el anterior)
     */
    public void addEntrada(int min, int max) {
        int rango = Math.abs(max - min),
                cantBit = (int) Math.ceil(Math.log10(rango) / Math.log10(2));
        this.cantEntradas += cantBit;
        rangos.add(cantBit);
        for (int i = 0; i < rango; i++) {
            indicesValores.put(min + i, transformarBinario(i, cantBit));
        }
    }
    
    public int getCantEntradas(){
        return cantEntradas;
    }
    
    public double[] transformarEntrada(Object[] entradas) throws Exception{
        double[] entradaBin = new double[cantEntradas];
        int cont = 0;
        
        for (Object entrada : entradas) {
            double[] entradaBinAux = indicesValores.get(entrada);//TODO: corregir clave
            if(entradaBinAux == null){
                throw new Exception("La entrada: " + entrada.toString() + " no es valida");
            }
            for (double bit : entradaBinAux) {
                entradaBin[cont] = bit;
                cont++;
            }
        }
        
        return entradaBin;
    }

    public static double[] transformarBinario(int num, int cantBitTotal) {
        double[] numBinario = new double[cantBitTotal];
        int i = 0;
        int numAux = num;

        while (numAux > 0) {
            numBinario[cantBitTotal - 1 - i] = numAux % 2;
            numAux = numAux / 2;
            i++;
        }
        for (int j = i; j < cantBitTotal; j++) {
            //Si es necesario agregar mas 0 debido a la cantidad de bits totales
            numBinario[cantBitTotal - 1 - j] = 0;
        }
        return numBinario;
    }
}
