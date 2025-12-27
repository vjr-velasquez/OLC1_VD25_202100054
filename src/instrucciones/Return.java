package instrucciones;

import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.ReturnValue;
import Simbolo.tablaSimbolos;
import Simbolo.NodoAST;
import abstracto.Instruccion;

public class Return extends Instruccion {

    private final Instruccion expr; // puede ser null

    public Return(Instruccion expr, int linea, int col) {
        super(new Tipo(Simbolo.tipoDato.VOID), linea, col); // tipo interno, se valida en Funcion
        this.expr = expr;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        if (expr == null) {
            return new ReturnValue(null, new Tipo(Simbolo.tipoDato.VOID));
        }

        Object val = expr.interpretar(arbol, tabla);
        // Si la expresión ya devuelve ReturnValue (raro), propagalo
        if (val instanceof ReturnValue) return val;

        // El tipo real está en expr.tipo
        Tipo t = expr.tipo;
        return new ReturnValue(val, t);
    }

    @Override
    public NodoAST getNodoAST() {
        NodoAST n = new NodoAST("RETURN");
        if (expr != null) n.agregarHijo(expr.getNodoAST());
        return n;
    }
}
