package instrucciones;

import Simbolo.Arbol;
import Simbolo.tablaSimbolos;
import abstracto.Instruccion;
import Simbolo.Tipo;
import Simbolo.tipoDato;
import excepciones.Errores;
import java.util.LinkedList;

public class Start extends Instruccion {

    private final String id;
    private final LinkedList<Instruccion> args;

    public Start(String id, LinkedList<Instruccion> args, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.id = id;
        this.args = (args != null) ? args : new LinkedList<>();
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {

        // 1) Evaluar args
        LinkedList<Object> valores = new LinkedList<>();
        for (Instruccion e : args) {
            if (e == null) continue;
            Object v = e.interpretar(arbol, tabla);
            if (v instanceof Errores) return v;
            valores.add(v);
        }

        // 2) Ejecutar la función global con ese ID
        Funcion f = arbol.getFuncion(id);
        if (f == null) {
            Errores err = new Errores("SEMANTICO",
                    "No existe la función '" + id + "' para Start().",
                    this.linea, this.col);
            arbol.addError(err);
            return null;
        }

        // 3) Llamar ejecutar (tabla hija de global)
        try {
            tablaSimbolos local = new tablaSimbolos(arbol.getTablaGlobal());
            return f.ejecutar(arbol, local, valores);
        } catch (Exception ex) {
            arbol.addError(new Errores("SEMANTICO",
                    "Excepción ejecutando Start(" + id + "): " + ex.getMessage(),
                    this.linea, this.col));
            return null;
        }
    }
}
