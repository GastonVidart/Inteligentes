package RedNeuronal;

// @author guido
public class RedNeuronal {

    private Nodo[][] capasOcultas;
    private Arco[][] capaEntrada;
    private Nodo[] capaSalida;
    private String[] valoresSalida;
    private int funcion;

    public static final int ESCALON = 0, ESCSIMETRICO = 1, LINEAL = 2, SIGMOIDE = 3;

    public RedNeuronal(int cantEntradas, int[] cantCapasOcultas, int cantSalidas, String[] salidas, int funcion) {
        //Creo nodos en capa oculta 3, [2, 3, 2], 4, 1
        capasOcultas = new Nodo[cantCapasOcultas.length][];
        for (int i = 0; i < cantCapasOcultas.length; i++) {
            capasOcultas[i] = new Nodo[cantCapasOcultas[i]];
            for (int j = 0; j < cantCapasOcultas[i]; j++) {
                capasOcultas[i][j] = new Nodo();
            }
        }
        //Creo arcos de entrada 
        capaEntrada = new Arco[cantEntradas][capasOcultas[0].length];
        for (int i = 0; i < cantEntradas; i++) {
            for (int j = 0; j < capasOcultas[0].length; j++) {
                capaEntrada[i][j] = new Arco();
                capasOcultas[0][j].addArcoEntrada(capaEntrada[i][j]);
            }
        }
        //Creo arcos entre nodos de capa oculta
        for (int i = 0; i < capasOcultas.length - 1; i++) {
            for (Nodo nodoAnterior : capasOcultas[i]) {
                for (Nodo nodoSiguiente : capasOcultas[i + 1]) {
                    Arco arco = new Arco();
                    nodoAnterior.addArcoSalida(arco);
                    nodoSiguiente.addArcoEntrada(arco);
                }
            }
        }
        //Creo arcos de salida
        int ultCapa = capasOcultas.length - 1;
        capaSalida = new Nodo[cantSalidas];
        for (int i = 0; i < capaSalida.length; i++) {
            capaSalida[i] = new Nodo();
            for (Nodo nodoUltCapa : capasOcultas[ultCapa]) {
                Arco arco = new Arco();
                capaSalida[i].addArcoEntrada(arco);
                nodoUltCapa.addArcoSalida(arco);
            }
        }
        //Asigno valores descriptivos a los nodos de salida
        valoresSalida = salidas;
        //Guardo la funcion de activacion usada por los nodos
        this.funcion = funcion;
    }
    
    public void entrenarRed(){
        //ForwardPass para calcular la funcion de costo
        //BackwardPass para ajustar la red
        FuncionesOptimizacion.gradientDescent(this);
    }

    public int obtenerSalida(double[] valoresEntrada) throws Exception {
        for (int i = 0; i < capaEntrada.length; i++) {
            for (Arco arco : capaEntrada[i]) {
                //Cada arco es de salida de la entrada i
                arco.setPatron(valoresEntrada[i]);
            }
        }
        for (Nodo[] capa : capasOcultas) {
            for (Nodo nodo : capa) {
                nodo.pasarSalida(nodo.calcularEntradaNeta(), funcion);
            }
        }
        double max = Integer.MIN_VALUE;
        int indiceMax = -1;
        for (int i = 0; i < capaSalida.length; i++) {
            double valor = capaSalida[i].obtenerSalida(capaSalida[i].calcularEntradaNeta(), funcion);
            if (max < valor) {
                indiceMax = i;
                max = valor;
            }
        }
        return indiceMax;
    }

    public String traducirSalida(int indice) {
        return valoresSalida[indice];
    }
}
