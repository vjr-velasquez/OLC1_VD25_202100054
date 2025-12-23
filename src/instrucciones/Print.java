package instrucciones;

import abstracto.Instruccion;
import Simbolo.*;

public class Print extends Instruccion {
    private Instruccion expresion;

    public Print(Instruccion expresion, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.expresion = expresion;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla){
        var resultado = this.expresion.interpretar(arbol, tabla);
        arbol.Print(String.valueOf(resultado));
        return null;
    }

    @Override
    public NodoAST getNodoAST() {
        NodoAST nodo = new NodoAST("PRINT");

        NodoAST nExp = new NodoAST("EXPRESION");
        if (this.expresion != null) {
            nExp.agregarHijo(this.expresion.getNodoAST()); // si expresión ya tiene AST, se cuelga aquí
        }

        nodo.agregarHijo(nExp);
        return nodo;
    }
}
