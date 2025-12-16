package instrucciones;

import Simbolo.Arbol;
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

        // 1) Inicialización (asignación/declaración)
        Object resIni = this.asignacion.interpretar(arbol, tablaFor);
        if (resIni instanceof Errores) {
            return resIni;
        }

        // 2) Evaluamos condición por primera vez
        Object cond = this.condicion.interpretar(arbol, tablaFor);
        if (cond instanceof Errores) {
            return cond;
        }

        // Debe ser booleana
        if (this.condicion.tipo.getTipo() != tipoDato.BOOLEANO) {
            Errores err = new Errores(
                    "SEMANTICO",
                    "La condición del ciclo for no es de tipo booleano",
                    this.linea, this.col
            );
            arbol.getErrores().add(err);
            return err;
        }

        // 3) Ciclo principal
        while ((boolean) cond) {

            // Nuevo ámbito para las instrucciones del cuerpo
            tablaSimbolos tablaInstrucciones = new tablaSimbolos(tablaFor);

            for (Instruccion ins : instrucciones) {
                Object resIns = ins.interpretar(arbol, tablaInstrucciones);

                if (resIns instanceof Errores) {
                    return resIns;
                }

                // break -> salimos del for
                if (resIns instanceof Break) {
                    return null;
                }

                // continue -> dejamos de ejecutar el resto de instrucciones
                // y pasamos a la actualización
                if (resIns instanceof Continue) {
                    break;
                }
            }

            // 4) Actualización (i = i + 1, etc.)
            Object resAct = this.actualizacion.interpretar(arbol, tablaFor);
            if (resAct instanceof Errores) {
                return resAct;
            }

            // 5) Re-evaluamos condición
            cond = this.condicion.interpretar(arbol, tablaFor);
            if (cond instanceof Errores) {
                return cond;
            }

            if (this.condicion.tipo.getTipo() != tipoDato.BOOLEANO) {
                Errores err = new Errores(
                        "SEMANTICO",
                        "La condición del ciclo for no es de tipo booleano",
                        this.linea, this.col
                );
                arbol.getErrores().add(err);
                return err;
            }
        }

        return null;
    }
}
