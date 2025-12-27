package expresiones;

import Simbolo.Tipo;
import Simbolo.NodoAST;

public class Parametro {
    private final Tipo tipo;
    private final String id;
    private final int linea;
    private final int col;

    public Parametro(Tipo tipo, String id, int linea, int col) {
        this.tipo = tipo;
        this.id = id;
        this.linea = linea;
        this.col = col;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getId() {
        return id;
    }

    public int getLinea() {
        return linea;
    }

    public int getCol() {
        return col;
    }

    public NodoAST getNodoAST() {
        NodoAST n = new NodoAST("PARAM");

        n.agregarHijo(new NodoAST("ID: " + id));
        n.agregarHijo(new NodoAST("TIPO: " + (tipo != null ? tipo.getTipo().toString() : "null")));

        return n;
    }

}
