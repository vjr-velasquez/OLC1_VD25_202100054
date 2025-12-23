package Simbolo;

import java.util.LinkedList;

public class NodoAST {
    public String etiqueta;
    public LinkedList<NodoAST> hijos;

    public NodoAST(String etiqueta) {
        this.etiqueta = etiqueta;
        this.hijos = new LinkedList<>();
    }

    public void agregarHijo(NodoAST hijo) {
        this.hijos.add(hijo);
    }
}
