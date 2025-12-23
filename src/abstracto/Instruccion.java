package abstracto;

import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.NodoAST;

public abstract class Instruccion {
    public Tipo tipo;
    public int linea;
    public int col;

    public Instruccion(Tipo tipo, int linea, int col) {
        this.tipo = tipo;
        this.linea = linea;
        this.col = col;
    }

    public abstract Object interpretar(Arbol arbol, tablaSimbolos tabla);

    // ✅ NO abstract: así no te rompe todas las clases de golpe
    public NodoAST getNodoAST() {
        return new NodoAST(this.getClass().getSimpleName());
    }
}
