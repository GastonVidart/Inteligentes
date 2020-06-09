package RedNeuronalv2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.corba.se.impl.orbutil.ObjectWriter;

public class toJson {

    public static void main(String[] args) {
        RedNeuronal red = new RedNeuronal(new int[]{2, 10, 2});
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String  red2=gson.toJson(red);
        System.out.println(red2);
     
                
        

    }

}
