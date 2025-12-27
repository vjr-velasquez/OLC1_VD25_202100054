package instrucciones;

import Simbolo.Arbol;
import Simbolo.ReturnValue;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import abstracto.Instruccion;
import excepciones.Errores;
import java.util.LinkedList;

public class For extends Instruccion {

    private Instruccion asignacion;      // i = 0
    private Instruccion condicion;       // i < 5
    private Instruccion actualizacion;   // i = i + 1
    private LinkedList<Instruccion> instrucciones;

    public For(Instruccion asignacion,
               Instruccion condicion,
               Instruccion actualizacion,
               LinkedList<Instruccion> instrucciones,
               int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.asignacion = asignacion;
        this.condicion = condicion;
        this.actualizacion = actualizacion;
        this.instrucciones = instrucciones;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {

        // Ámbito donde vive la variable del for
        tablaSimbolos tablaFor = new tablaSimbolos(tabla);

        // 1) Inicialización
        Object resIni = this.asignacion.interpretar(arbol, tablaFor);
        if (resIni instanceof Errores) return resIni;

        // 2) Evaluamos condición por primera vez
        Object cond = this.condicion.interpretar(arbol, tablaFor);
        if (cond instanceof Errores) return cond;

        if (!(cond instanceof Boolean)) {
            Errores err = new Errores("SEMANTICO",
                    "La condición del ciclo for debe ser booleana",
                    this.linea, this.col);
            arbol.getErrores().add(err);
            return err;
        }

        // 3) Ciclo principal
        while ((boolean) cond) {

            // Nuevo ámbito para el cuerpo (hijo del for)
            tablaSimbolos tablaInstrucciones = new tablaSimbolos(tablaFor);

            boolean huboContinue = false;

            for (Instruccion ins : instrucciones) {
                if (ins == null) continue;

                Object resIns = ins.interpretar(arbol, tablaInstrucciones);

                if (resIns instanceof Errores) return resIns;

                // ✅ return dentro del for: se propaga a la función
                if (resIns instanceof ReturnValue) return resIns;

                // break -> salimos del for
                if (resIns instanceof Break) {
                    return null;
                }

                // continue -> saltamos a actualización
                if (resIns instanceof Continue) {
                    huboContinue = true;
                    break;
                }
            }

            // 4) Actualización
            Object resAct = this.actualizacion.interpretar(arbol, tablaFor);
            if (resAct instanceof Errores) return resAct;

            // 5) Re-evaluamos condición
            cond = this.condicion.interpretar(arbol, tablaFor);
            if (cond instanceof Errores) return cond;

            if (!(cond instanceof Boolean)) {
                Errores err = new Errores("SEMANTICO",
                        "La condición del ciclo for debe ser booleana",
                        this.linea, this.col);
                arbol.getErrores().add(err);
                return err;
            }
        }

        return null;
    }
}
