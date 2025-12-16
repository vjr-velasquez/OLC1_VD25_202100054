package instrucciones;

import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import abstracto.Instruccion;

public class Continue extends Instruccion {

    public Continue(int linea, int col) {
        // tipo VOID porque no devuelve valor
        super(new Tipo(tipoDato.VOID), linea, col);
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Igual que Break, devolvemos el mismo objeto para que
        // el ciclo (While/For) sepa que debe hacer un continue.
        return this;
    }
}
