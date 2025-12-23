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

        // vector vacío: lo dejamos como vector de VOID (o podrías marcar error)
        if (elementos == null) elementos = new LinkedList<>();

        Object[] arr = new Object[elementos.size()];

        tipoDato base = null;

        for (int i = 0; i < elementos.size(); i++) {
            Instruccion e = elementos.get(i);
            Object val = e.interpretar(arbol, tabla);

            if (val instanceof Errores) return val;

            // determinar tipo base con el primer elemento
            if (base == null) {
                base = e.tipo.getTipo();
            } else {
                // todos deben ser del mismo tipo base
                if (e.tipo.getTipo() != base) {
                    return new Errores(
                            "SEMANTICO",
                            "Vector literal con tipos mezclados: " + base + " y " + e.tipo.getTipo(),
                            this.linea,
                            this.col
                    );
                }
            }

            arr[i] = val;
        }

        // si está vacío, no hay forma de inferir tipo: lo dejamos como VOID[]
        if (base == null) base = tipoDato.VOID;

        this.tipo = new Tipo(base, 1); // vector del tipo base
        return arr;
    }
}
