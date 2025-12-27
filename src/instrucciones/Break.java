package instrucciones;

import abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;

public class Break extends Instruccion {

    public Break(int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Igual que Continue: devolvemos esta instancia para que el ciclo/switch lo detecte
        return this;
    }
}
