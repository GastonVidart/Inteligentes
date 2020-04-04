import java.util.*;
import java.io.*;
import java.math.*;



 
class playerYaupe {   
    public static  ArrayList<Barrel> theB=new ArrayList<Barrel>();
    public static  ArrayList<int[]> posVecinas=new ArrayList<int[]>();
    public static  ArrayList<int[]> minasAlrededor=new ArrayList<int[]>();
    public static  ArrayList<int[]> enemigos=new ArrayList<int[]>();
public static  ArrayList<int[]> misBarcos=new ArrayList<int[]>();
    
    public static int heuristica;
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
    
        // game loop
        while (true) {
          theB.clear();
          enemigos.clear();
          minasAlrededor.clear();
          posVecinas.clear();
          misBarcos.clear();
            int myShipCount = in.nextInt(); // the number of remaining ships
            int entityCount = in.nextInt(); // the number of entities (e.g. ships, mines or cannonballs)
            int miPosx=0,miPosy=0;
            int miRum=0;
            for (int i = 0; i < entityCount; i++) {
              
                int entityId = in.nextInt();
                String entityType = in.next();
                int x = in.nextInt();
                int y = in.nextInt();
                int arg1 = in.nextInt();
                int arg2 = in.nextInt();
                int arg3 = in.nextInt();
                int arg4 = in.nextInt();
                switch (entityType){
                    case "BARREL":
                     theB.add(new Barrel(arg1,x,y));
                     break;
                     case "SHIP":
                        if(arg4==1){
                            //para guardar la posicion de mi barco
                            int[] barco=new int[3];                            
                             barco[0]=x;
                             barco[1]=y;
                             barco[2]=arg3;
                             misBarcos.add(barco);
                             //misVecinos(miPosx,miPosy);
                             }
                             else{
                             int[] enemigo=new int[6];
                             enemigo[0]=arg1;
                             enemigo[1]=arg2;
                             enemigo[2]=arg3;
                             enemigo[3]=arg4;
                             enemigo[4]=x;
                             enemigo[5]=y;
                             enemigos.add(enemigo);
                                 }
                    break;
                    case "MINE":
                        boolean encontrada=analizarMinaCerca(x,y);
                        if(encontrada){
                            int mina[]=new int[2];
                            mina[0]=x;
                            mina[1]=y;
                            minasAlrededor.add(mina);
                            }
                    break;
                        }                     
            }
            String sigEstado[]=new String[3];
           //   heuristica=100-miRum;
            //sigEstado=hillClimbing(miPosx,miPosy);
            for (int j = 0; j < myShipCount; j++) { 
                            heuristica=100-miRum;
                            sigEstado=hillClimbing((misBarcos.get(j))[0],(misBarcos.get(j))[1],(misBarcos.get(j))[2]);
                            System.out.println(sigEstado[2]+Integer.parseInt(sigEstado[0])+" "+Integer.parseInt(sigEstado[1]));
                         }
          
           /** if(theB.isEmpty()){
               System.out.println("WAIT");  
               }else{
                int[] searchB=analizarBarriles(miPosx,miPosy);
        
                     if(minasAlrededor.isEmpty()){
                         int[] enemigoCerca=obtenerEnemigoCerca(miPosx,miPosy);
                         if(searchB[2]<enemigoCerca[6] || enemigoCerca[6]>10){
                             for (int j = 0; j < myShipCount; j++) { 
                             System.out.println("MOVE "+searchB[0]+" "+searchB[1]);}
                         }else{
                             System.out.println("FIRE "+enemigoCerca[4]+" "+enemigoCerca[5]);
                             }
                             
                      }else{
                          
                          
                            int[] target=esquivar(miPosx,miPosy);
                            System.out.println("MOVE "+Integer.parseInt(target[0])+" "+Integer.(target[1]));
                    }
              }**/
        }
    }
    
    public static String[] hillClimbing(int x,int y,int rum){
        int sigVHeu=0;
        heuristica=100-rum;
        int heuristicaPosible=heuristica, heuristicaPosible2=heuristica;
        String[] sigEstado=new String[3];
        int[] searchB=new int[2];
        if(theB.isEmpty()){
            heuristicaPosible=heuristicaPosible-25;
            }else{
                searchB=analizarBarriles(x,y);
                heuristicaPosible=heuristicaPosible+(25-searchB[2]);
            }
        int[] enemigoCerca=obtenerEnemigoCerca(x,y);
        heuristicaPosible2=heuristicaPosible2+(25-enemigoCerca[6]);
        if(heuristicaPosible2>heuristicaPosible){
            heuristica=heuristicaPosible2;
            int[] posEnemigo=new int[2];
            posEnemigo[0]=enemigoCerca[4];
            posEnemigo[1]=enemigoCerca[5];
           // int[] posicion=getSiguiente(posEnemigo,enemigoCerca[0],3);
            sigEstado[0]=""+enemigoCerca[4];
            sigEstado[1]=""+enemigoCerca[5];
            //sigEstado[0]=""+posicion[0];
            //sigEstado[1]=""+posicion[1];
            sigEstado[2]="FIRE ";
            }else{
            //if(minasAlrededor.isEmpty()){
            heuristica=heuristicaPosible;
            sigEstado[0]=""+searchB[0];
            sigEstado[1]=""+searchB[1];
            sigEstado[2]="MOVE ";
            //}
                 //else{
                 //int[] target=esquivar(x,y);
                 //sigEstado[0]=""+target[0];
                 //sigEstado[1]=""+target[1];
                 //sigEstado[2]="MOVE ";    
                 //} 
                 }
        return sigEstado;
        }
    
    
    public static int[] cube_to_oddr(int[] cubo){
        int col=cubo[0]+(cubo[2]-(cubo[2] & 1)/2);
        int row=cubo[2];
        return new int[]{col,row};  
        }
    public static int[] oddr_to_cube(int[] hex){
        //el mapa usa odd-r, convierte a cube desde offset
        int[] cubo = new int[3];
        cubo[0] = hex[0] - (hex[1] - (hex[1] & 1)) / 2; //x
        cubo[2] = hex[1]; //z
        cubo[1] = -cubo[0] - cubo[2]; //y
        return cubo;
        }     
    public static void misVecinos(int x,int y){
        int[] a,b,c,d,e,f;
        
        a=new int[2];
        a[0]=x-1;
        a[1]=y;
        b=new int[2];
        b[0]=x-1;
        b[1]=y-1;
        c=new int[2];
        c[0]=x;
        c[1]=y-1;
        d=new int[2];
        d[0]=x+1;
        d[0]=y;
        e=new int[2];
        e[0]=x+1;
        e[1]=y+1;
        f=new int[2];
        f[0]=x;
        f[1]=y+1;
        posVecinas.add(a);
        posVecinas.add(b);
        posVecinas.add(c);
        posVecinas.add(d);
        posVecinas.add(e);
        posVecinas.add(f);
        }  
    public static int[] analizarBarriles(int x,int y){
        //x e y marcan la pos de mi barco
        int[] objective=null;
        int xA,yA,xO=0,yO=0,distanceO=0,distanceA=0;
        int[] barril=new int[2];
        int[] posBarco=new int[2];
        Barrel analize;
        barril[0]=0;
        barril[1]=0;
        posBarco[0]=x;
        posBarco[1]=y;
        for (int i=0;i<theB.size();i++){
            analize=theB.get(i);
            if(i==0){
                objective=new int[3];
                xO=analize.getX();
                yO=analize.getY();
                barril[0]=xO;
                barril[1]=yO;
                objective[0]=xO;
                objective[1]=yO;
                distanceO=distancia(posBarco,barril);
                }
            else{
                xA=analize.getX();
                yA=analize.getY();
                barril[0]=xA;
                barril[1]=yA;
               distanceA=distancia(posBarco,barril);
               
               if(distanceA<distanceO){  
                   distanceO=distanceA;
                   objective[0]=xA;
                   objective[1]=yA;
                   objective[2]=distanceO;
               }
                } }
return objective;
        }   
    private static int distancia(int[] a, int[] b) {
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
        barcoC = oddr_to_cube(xyBarco);
        //System.out.println("EnemigoC x:" + eneC[0] + " y:" + eneC[1] + " z:" + eneC[2]);
        //sumo la pos y el sig
        ataqueC = new int[]{barcoC[0] + sig[0], barcoC[1] + sig[1], barcoC[2] + sig[2]};
        //System.out.println("AtaqueC x:" + ataqueC[0] + " y:" + ataqueC[1] + " z:" + ataqueC[2]);
        xyPos = cube_to_oddr(ataqueC);
        //System.out.println("ataque x:" + xyAtaque[0] + " y:" + xyAtaque[1]);
        return xyPos;
    }
    private static int cube_distance(int[] a, int[] b) {
        return (Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]) + Math.abs(a[2] - b[2])) / 2;
    }
    public static boolean analizarMinaCerca(int x,int y){
        //para saber si la mina encontrada es mi vecina
        boolean exito=false;
        for(int i=0;i<posVecinas.size();i++){
            int a=(posVecinas.get(i))[0];
            int b=(posVecinas.get(i))[1];
            if(a==x && b==y){
                exito=true;
                }
           
            
            }
        return exito; 
        }    
    public static int[] esquivar(int x,int y){
        boolean exito=false;
        int xmina, ymina,i=0;
        int[] avanzo=new int[2];
        avanzo[0]=x-1;
        avanzo[1]=y;
        while(!exito && !(i==minasAlrededor.size())){
            xmina=(minasAlrededor.get(i))[0];
            ymina=(minasAlrededor.get(i))[1];
            if(!(xmina==avanzo[0]) && !(ymina==avanzo[1])){
                exito=true;
                }else{
                    avanzo[0]=x-1;
                    avanzo[1]=y-1;
                          if(!(xmina==avanzo[0]) && !(ymina==avanzo[1])){
                                exito=true;
                            }else{
                                    avanzo[0]=x;
                                    avanzo[1]=y-1;
                                    if(!(xmina==avanzo[0]) && !(ymina==avanzo[1])){
                                    exito=true;
                                    }else{
                                             avanzo[0]=x+1;
                                             avanzo[1]=y;
                                             if(!(xmina==avanzo[0]) && !(ymina==avanzo[1])){
                                             exito=true;
                                             }else{
                                                        avanzo[0]=x+1;
                                                        avanzo[1]=y+1;
                                                        if(!(xmina==avanzo[0]) && !(ymina==avanzo[1])){
                                                        exito=true;
                                                        }else{
                                                                    avanzo[0]=x;
                                                                    avanzo[1]=y+1;
                                                                    if(!(xmina==avanzo[0]) && !(ymina==avanzo[1])){
                                                                      exito=true;
                                                                    }
                                                            }
                                                 
                                                 
                                                 }
                                        
                                          }
                                
                                
                                
                                }
                    }
            
            
            i++;}
        
        return avanzo;
        }
    public static int[] obtenerEnemigoCerca(int x,int y){
        //x e y marcan la pos de mi barco
        boolean exito=false;
        int xA,yA,xO=0,yO=0,distanceO=0,distanceA=0;
        int[] enemigo=new int[7];
        int[] posBarco=new int[2];
        int[] posBarcoEnemigo=new int[2];
        int[] analize;
        posBarco[0]=x;
        posBarco[1]=y;
        int i=0;
      while (i<enemigos.size() && !exito){
            analize=enemigos.get(i);
            if(i==0){
                posBarcoEnemigo[0]=analize[4];
                posBarcoEnemigo[1]=analize[5];
                distanceO=distancia(posBarco,posBarcoEnemigo);
                exito=true;
                }
            else{
                posBarcoEnemigo[0]=analize[4];
                posBarcoEnemigo[1]=analize[5];
                distanceA=distancia(posBarco,posBarcoEnemigo);
                     if(distanceA<distanceO){  
                        distanceO=distanceA;
                        exito=true;
                         }  
                } 
                enemigo[0]=analize[0];
                enemigo[1]=analize[1];
                enemigo[2]=analize[2];
                enemigo[3]=analize[3];
                enemigo[4]=analize[4];
                enemigo[5]=analize[5];
                enemigo[6]=distanceO;
                i++;
                }
return enemigo;
        
        }
    
        
}