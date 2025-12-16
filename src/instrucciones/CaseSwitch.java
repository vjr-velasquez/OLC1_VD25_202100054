package instrucciones;

import java.util.LinkedList;
import abstracto.Instruccion;

/**
 * Representa un solo "case" dentro de un switch:
 *   case EXPRESION: { lista de instrucciones }
 */
public class CaseSwitch {

    private Instruccion expresion;               // la expresión del case
    private LinkedList<Instruccion> instrucciones; // instrucciones dentro del case

    public CaseSwitch(Instruccion expresion, LinkedList<Instruccion> instrucciones) {
        this.expresion = expresion;
        this.instrucciones = instrucciones;
    }

    public Instruccion getExpresion() {
        return expresion;
    }

    public LinkedList<Instruccion> getInstrucciones() {
        return instrucciones;
    }
}
