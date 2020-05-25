package RedNeuronal;

// @author guido
public class Arco {

    private double w;//Peso sináptico
    private double p;//Patron

    public Arco(double w) {
        this.w = w;
    }

    public void setPatron(double p) {
        this.p = p;
    }

    public double getValor() {
        return p * w;
    }

}
