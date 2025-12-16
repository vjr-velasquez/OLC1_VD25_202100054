package instrucciones;

import abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.tipoDato;
import Simbolo.tablaSimbolos;
import excepciones.Errores;
import java.util.LinkedList;

public class Switch extends Instruccion {

    private Instruccion condicion;
    private LinkedList<CaseSwitch> casos;
    private LinkedList<Instruccion> defecto; // puede ser null

    public Switch(Instruccion condicion,
                  LinkedList<CaseSwitch> casos,
                  LinkedList<Instruccion> defecto,
                  int linea,
                  int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.condicion = condicion;
        this.casos = casos;
        this.defecto = defecto;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Evaluamos la condición del switch
        Object valorCond = condicion.interpretar(arbol, tabla);
        if (valorCond instanceof Errores) {
            return valorCond;
        }

        boolean seEjecutoAlguno = false; // para saber si ya entramos a algún case

        // Recorremos los cases
        for (CaseSwitch c : casos) {
            Object valorCase = c.getExpresion().interpretar(arbol, tabla);
            if (valorCase instanceof Errores) {
                return valorCase;
            }

            boolean coincide =
                (valorCond == null && valorCase == null)
                || (valorCond != null && valorCond.equals(valorCase));

            // Si coincide este case, o ya venimos arrastrando ejecución (fall-through)
            if (coincide || seEjecutoAlguno) {
                seEjecutoAlguno = true;

                // Nuevo entorno para las instrucciones del case
                tablaSimbolos local = new tablaSimbolos(tabla);

                for (Instruccion ins : c.getInstrucciones()) {
                    Object res = ins.interpretar(arbol, local);

                    // si es error, lo guardamos y seguimos
                    if (res instanceof Errores) {
                        arbol.getErrores().add((Errores) res);
                    }

                    // Si viene un break, termina el switch
                    if (res instanceof instrucciones.Break) {
                        return null;
                    }

                    // Si viene un continue, se lo devolvemos al while/for exterior
                    if (res instanceof instrucciones.Continue) {
                        return res;
                    }
                }
            }
        }

        // Si no se ejecutó ningún case y hay default, lo corremos
        if (!seEjecutoAlguno && defecto != null) {
            tablaSimbolos local = new tablaSimbolos(tabla);

            for (Instruccion ins : defecto) {
                Object res = ins.interpretar(arbol, local);

                if (res instanceof Errores) {
                    arbol.getErrores().add((Errores) res);
                }

                if (res instanceof instrucciones.Break) {
                    return null;
                }

                if (res instanceof instrucciones.Continue) {
                    return res;
                }
            }
        }

        return null;
    }
}
