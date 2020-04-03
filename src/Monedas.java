
import java.util.Scanner;

public class Monedas{
public static void main(String args[]) {
    System.out.println("hola");
        Scanner in = new Scanner(System.in);
        String cadena="";
        int N = in.nextInt();
        int a[] = new int[9];                                       
        a[0]=N/500;
        N=N-a[0]*500;             
        System.out.println(a[0]);
        if(a[0]!=0){
            cadena = cadena + a[0] + "x500 ";
            }        
        a[1]=N/200;
        N=N-a[1]*200;             
        System.out.println(a[1]);
        if(a[1]!=0){
            cadena = cadena + a[1] + "x200 ";
            }  
        a[2]=N/100;
        N=N-a[2]*100;             
        System.out.println(a[2]);
        if(a[2]!=0){
            cadena = cadena + a[2] + "x100 ";
            }        
        a[3]=N/50;
        N=N-a[3]*50;             
        System.out.println(a[3]);
        if(a[3]!=0){
            cadena = cadena + a[1] + "x50 ";
            }
        a[4]=N/20;
        N=N-a[4]*20;             
        System.out.println(a[4]);
        if(a[4]!=0){
            cadena = cadena + a[4] + "x20 ";
            }
        a[5]=N/10;
        N=N-a[5]*10;             
        System.out.println(a[5]);
        if(a[5]!=0){
            cadena = cadena + a[5] + "x10 ";
            }        
        a[6]=N/5;
        N=N-a[6]*5;             
        System.out.println(a[6]);
        if(a[6]!=0){
            cadena = cadena + a[6] + "x5 ";
            }
        a[7]=N/2;
        N=N-a[7]*2;             
        System.out.println(a[7]);
        if(a[7]!=0){
            cadena = cadena + a[7] + "x2 ";
            }
        a[8]=N/1;
        N=N-a[8]*1;             
        System.out.println(a[8]);
        if(a[8]!=0){
            cadena = cadena + a[8] + "x1";
            }             
        System.out.println(cadena);

        // Write an action using System.out.println()
        // To debug: System.err.println("Debug messages...");

        
    }
}