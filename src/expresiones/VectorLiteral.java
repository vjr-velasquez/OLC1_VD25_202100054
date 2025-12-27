package expresiones;

import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import abstracto.Instruccion;
import excepciones.Errores;
import java.util.LinkedList;

public class VectorLiteral extends Instruccion {

    private LinkedList<Instruccion> elementos;

    public VectorLiteral(LinkedList<Instruccion> elementos, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col); // el tipo real se define al interpretar
        this.elementos = elementos;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {

        if (elementos == null) elementos = new LinkedList<>();

        Object[] arr = new Object[elementos.size()];

        // tipo base inferido
        tipoDato base = null;

        for (int i = 0; i < elementos.size(); i++) {

            Instruccion e = elementos.get(i);
            Object val = e.interpretar(arbol, tabla);

            if (val instanceof Errores) return val;

            // si el tipo de la expresión no quedó bien seteado, igual intentamos inferir
            tipoDato tipoElem = e.tipo != null ? e.tipo.getTipo() : tipoDato.VOID;

            if (base == null) {
                base = tipoElem;
            } else {
                if (tipoElem != base) {
                    return new Errores(
                            "SEMANTICO",
                            "Vector literal con tipos mezclados: " + base + " y " + tipoElem,
                            this.linea,
                            this.col
                    );
                }
            }

            arr[i] = val;
        }

        // vector vacío: no se puede inferir tipo -> queda como VOID[]
        if (base == null) base = tipoDato.VOID;

        this.tipo = new Tipo(base, 1);
        return arr;
    }

}
