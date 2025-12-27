package instrucciones;

import Simbolo.*;
import abstracto.Instruccion;
import excepciones.Errores;
import java.util.LinkedList;

public class If extends Instruccion {
    private Instruccion expresion;
    private LinkedList<Instruccion> instrucciones;
    private LinkedList<Instruccion> instruccioneselseif;
    private LinkedList<Instruccion> instruccioneselse;

    public If(Instruccion expresion, LinkedList<Instruccion> instrucciones, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.expresion = expresion;
        this.instrucciones = instrucciones;
    }

    public If(Instruccion expresion, LinkedList<Instruccion> instrucciones, LinkedList<Instruccion> instruccioneselse, int linea, int col){
        super(new Tipo(tipoDato.VOID), linea, col);
        this.expresion = expresion;
        this.instrucciones = instrucciones;
        this.instruccioneselse = instruccioneselse;
    }

    public If(Instruccion expresion, LinkedList<Instruccion> instrucciones, LinkedList<Instruccion> instruccioneselseif, LinkedList<Instruccion> instruccioneselse, int linea, int col){
        super(new Tipo(tipoDato.VOID), linea, col);
        this.expresion = expresion;
        this.instrucciones = instrucciones;
        this.instruccioneselseif = instruccioneselseif;
        this.instruccioneselse = instruccioneselse;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla){
        Object condicion = this.expresion.interpretar(arbol, tabla);
        if (condicion instanceof Errores) return condicion;

        if(!(condicion instanceof Boolean)){
            return new Errores("SEMANTICO", "La condicion del if tiene que devolver un valor booleano", this.linea, this.col);
        }

        boolean ejecutarIf = (Boolean) condicion;

        // =========================
        // IF
        // =========================
        if(ejecutarIf){
            tablaSimbolos nuevaTabla = new tablaSimbolos(tabla);

            for (Instruccion inst : instrucciones){
                if (inst == null) continue;

                Object res = inst.interpretar(arbol, nuevaTabla);

                if (res instanceof Errores) return res;

                // ✅ PROPAGAR RETURN / BREAK / CONTINUE
                if (res instanceof ReturnValue) return res;
                if (res instanceof Break) return res;
                if (res instanceof Continue) return res;
            }

            return true; // ejecutó if (usado para elseif)
        }

        // =========================
        // ELSEIF (tu diseño lo maneja como lista de "If" anidados)
        // =========================
        if (instruccioneselseif != null){
            for (Instruccion elseif : instruccioneselseif){
                if (elseif == null) continue;

                Object res = elseif.interpretar(arbol, tabla);

                if (res instanceof Errores) return res;

                // ✅ si el elseif retornó una señal, la propagamos
                if (res instanceof ReturnValue) return res;
                if (res instanceof Break) return res;
                if (res instanceof Continue) return res;

                // si el elseif se ejecutó, corta la cadena
                if (res instanceof Boolean && (Boolean) res){
                    return true;
                }
            }
        }

        // =========================
        // ELSE
        // =========================
        if (instruccioneselse != null){
            tablaSimbolos nuevaTabla = new tablaSimbolos(tabla);

            for (Instruccion instElse : instruccioneselse){
                if (instElse == null) continue;

                Object res = instElse.interpretar(arbol, nuevaTabla);

                if (res instanceof Errores) return res;

                // ✅ PROPAGAR RETURN / BREAK / CONTINUE
                if (res instanceof ReturnValue) return res;
                if (res instanceof Break) return res;
                if (res instanceof Continue) return res;
            }

            return true;
        }

        return false;
    }
}
