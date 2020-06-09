package RedNeuronal;

import RedNeuronalv2.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.corba.se.impl.orbutil.ObjectWriter;


public class toJson {

    public static void main(String[] args) {
        int cantEntradas = 10;
        int[] cantCapasOcultas = {2, 3, 2};
        int cantSalidas = 10;
        int funcion = RedNeuronalV1.SIGMOIDE;
        String[] salidas = {"Nothing in hand", "One pair", "Two pairs",
        "Three of a kind", "Straight", "Flush", "Full house",
        "Four of a kind", "Straight flush", "Royal flush"};
        RedNeuronalV1 red = new RedNeuronalV1(cantEntradas, cantCapasOcultas, cantSalidas, salidas, funcion);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String  red2=gson.toJson(red);
        System.out.println(red2);
     
                
        

    }

}
