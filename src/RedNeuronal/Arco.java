package RedNeuronal;

// @author guido
public class Arco {

    private double w = 1;//Peso sináptico
    private double p;//Patron

    public Arco(double w) {
        this.w = w;
    }

    public Arco(){
        
    }
    
    public void setPatron(double p) {
        this.p = p;
    }

    public double getValor() {
        return p * w;
    }
    
    public double getPeso(){
        return w;
    }

    void corregirPeso(double delta, double learningRate) {
        w = w + learningRate * delta * p;
    }
}
