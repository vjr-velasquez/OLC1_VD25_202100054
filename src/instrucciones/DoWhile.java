package instrucciones;

import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import abstracto.Instruccion;
import excepciones.Errores;
import java.util.LinkedList;

public class DoWhile extends Instruccion {

    private Instruccion condicion;
    private LinkedList<Instruccion> instrucciones;

    public DoWhile(Instruccion condicion, LinkedList<Instruccion> instrucciones,
                   int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.condicion = condicion;
        this.instrucciones = instrucciones;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {

        // tabla base para el ciclo (como While / For)
        tablaSimbolos tablaCiclo = new tablaSimbolos(tabla);

        Object cond;

        do {
            // nuevo entorno para el bloque interno
            tablaSimbolos tablaInterna = new tablaSimbolos(tablaCiclo);

            for (Instruccion ins : instrucciones) {
                Object res = ins.interpretar(arbol, tablaInterna);

                if (res instanceof Errores) {
                    arbol.getErrores().add((Errores) res);
                }

                // romper el ciclo completo
                if (res instanceof Break) {
                    return null;
                }

                // pasar directo a evaluar la condición nuevamente
                if (res instanceof Continue) {
                    break;
                }
            }

            // evaluar condición al final (do-while)
            cond = condicion.interpretar(arbol, tablaCiclo);
            if (cond instanceof Errores) {
                return cond;
            }

            if (cond == null || this.condicion.tipo.getTipo() != tipoDato.BOOLEANO) {
                return new Errores(
                    "SEMANTICO",
                    "La condicion de do-while debe ser de tipo booleano",
                    this.linea, this.col
                );
            }

        } while ((boolean) cond);

        return null;
    }
}
