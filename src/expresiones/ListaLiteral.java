package expresiones;

import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import abstracto.Instruccion;
import excepciones.Errores;
import java.util.LinkedList;

public class ListaLiteral extends Instruccion {

    private LinkedList<Instruccion> elementos;

    public ListaLiteral(LinkedList<Instruccion> elementos, int linea, int col) {
        super(new Tipo(tipoDato.LISTA, new Tipo(tipoDato.VOID)), linea, col);
        this.elementos = (elementos == null) ? new LinkedList<>() : elementos;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {

        LinkedList<Object> lista = new LinkedList<>();

        tipoDato base = null;

        for (Instruccion e : elementos) {
            Object val = e.interpretar(arbol, tabla);
            if (val instanceof Errores) return val;

            tipoDato t = (e.tipo != null) ? e.tipo.getTipo() : tipoDato.VOID;

            if (base == null) base = t;
            else if (t != base) {
                return new Errores(
                        "SEMANTICO",
                        "Lista literal con tipos mezclados: " + base + " y " + t,
                        this.linea, this.col
                );
            }

            lista.add(val);
        }

        if (base == null) base = tipoDato.VOID; // list() vacío

        this.tipo = new Tipo(tipoDato.LISTA, new Tipo(base));
        return lista;
    }
}
