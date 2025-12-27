package instrucciones;

import abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.tipoDato;
import Simbolo.tablaSimbolos;
import Simbolo.ReturnValue;
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
        Object valorCond = condicion.interpretar(arbol, tabla);
        if (valorCond instanceof Errores) return valorCond;

        boolean seEjecutoAlguno = false;

        // CASES
        for (CaseSwitch c : casos) {
            Object valorCase = c.getExpresion().interpretar(arbol, tabla);
            if (valorCase instanceof Errores) return valorCase;

            boolean coincide =
                    (valorCond == null && valorCase == null)
                    || (valorCond != null && valorCond.equals(valorCase));

            if (coincide || seEjecutoAlguno) {
                seEjecutoAlguno = true;

                tablaSimbolos local = new tablaSimbolos(tabla);

                for (Instruccion ins : c.getInstrucciones()) {
                    Object res = ins.interpretar(arbol, local);

                    // errores: los agregas pero no paras (tu comportamiento actual)
                    if (res instanceof Errores) {
                        arbol.getErrores().add((Errores) res);
                        continue;
                    }

                    // ✅ PROPAGAR RETURN
                    if (res instanceof ReturnValue) {
                        return res;
                    }

                    // break: termina el switch
                    if (res instanceof Break) {
                        return null;
                    }

                    // continue: se lo devolvemos al while/for exterior
                    if (res instanceof Continue) {
                        return res;
                    }
                }
            }
        }

        // DEFAULT
        if (!seEjecutoAlguno && defecto != null) {
            tablaSimbolos local = new tablaSimbolos(tabla);

            for (Instruccion ins : defecto) {
                Object res = ins.interpretar(arbol, local);

                if (res instanceof Errores) {
                    arbol.getErrores().add((Errores) res);
                    continue;
                }

                // ✅ PROPAGAR RETURN
                if (res instanceof ReturnValue) {
                    return res;
                }

                if (res instanceof Break) {
                    return null;
                }

                if (res instanceof Continue) {
                    return res;
                }
            }
        }

        return null;
    }
}
