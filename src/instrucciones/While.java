package instrucciones;

import Simbolo.Arbol;
import Simbolo.ReturnValue;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import abstracto.Instruccion;
import excepciones.Errores;
import java.util.LinkedList;

public class While extends Instruccion {
    private Instruccion expresion;
    private LinkedList<Instruccion> instrucciones;

    public While(Instruccion expresion, LinkedList<Instruccion> instrucciones, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.expresion = expresion;
        this.instrucciones = instrucciones;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {

        // condición inicial
        Object condicion = this.expresion.interpretar(arbol, tabla);
        if (condicion instanceof Errores) return condicion;

        // validar boolean
        if (!(condicion instanceof Boolean)) {
            return new Errores("SEMANTICO",
                    "La condición del while debe ser booleana",
                    this.linea, this.col);
        }

        // ciclo
        while ((boolean) condicion) {
            // scope del cuerpo del while
            tablaSimbolos tablaWhile = new tablaSimbolos(tabla);

            for (Instruccion ins : instrucciones) {
                if (ins == null) continue;

                Object resultado = ins.interpretar(arbol, tablaWhile);

                // error
                if (resultado instanceof Errores) return resultado;

                // ✅ return dentro de while: se propaga hacia arriba
                if (resultado instanceof ReturnValue) return resultado;

                // break: salir del while
                if (resultado instanceof Break) {
                    return null;
                }

                // continue: saltar a reevaluar condición
                if (resultado instanceof Continue) {
                    break; // sale del for de instrucciones, pero sigue while (reevalúa condición)
                }
            }

            // reevaluar condición (usa el mismo scope del ciclo o el del padre; aquí usamos tabla del while)
            condicion = this.expresion.interpretar(arbol, tablaWhile);
            if (condicion instanceof Errores) return condicion;

            if (!(condicion instanceof Boolean)) {
                return new Errores("SEMANTICO",
                        "La condición del while debe ser booleana",
                        this.linea, this.col);
            }
        }

        return null;
    }
}
