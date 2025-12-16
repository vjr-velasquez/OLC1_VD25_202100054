package Simbolo;

import abstracto.Instruccion;
import excepciones.Errores;
import java.util.LinkedList;

public class Arbol {
    private LinkedList<Instruccion> instrucciones;
    private tablaSimbolos tablaGlobal;
    private LinkedList<Errores> errores;
    private String consolas;

    public Arbol(LinkedList<Instruccion> instrucciones) {
        this.instrucciones = instrucciones;
        this.tablaGlobal = new tablaSimbolos();
        this.errores = new LinkedList<>();
        this.consolas = "";
    }

    public LinkedList<Instruccion> getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(LinkedList<Instruccion> instrucciones) {
        this.instrucciones = instrucciones;
    }

    public tablaSimbolos getTablaGlobal() {
        return tablaGlobal;
    }

    public void setTablaGlobal(tablaSimbolos tablaGlobal) {
        this.tablaGlobal = tablaGlobal;
    }

    public LinkedList<Errores> getErrores() {
        return errores;
    }

    public void setErrores(LinkedList<Errores> errores) {
        this.errores = errores;
    }

    public String getConsolas() {
        return consolas;
    }

    public void setConsolas(String consolas) {
        this.consolas = consolas;
    }

    /** Agregar error semántico o léxico/sintáctico al árbol */
    public void addError(Errores e) {
        if (this.errores == null) {
            this.errores = new LinkedList<>();
        }
        this.errores.add(e);
    }

    /** Método usado por las instrucciones Print */
    public void Print(String valor){
        this.consolas += valor + "\n";
    }

    /**
     * Genera un AST muy simple en formato DOT.
     * Nodo raíz: INICIO
     * Hijos directos: una caja por cada Instrucción (clase) en la lista principal.
     *
     * Si luego quieres algo más detallado, habría que agregar métodos específicos
     * en cada Instrucción (por ejemplo, getNodoAST()).
     */
    public String generarDotAST() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph AST {\n");
        sb.append("  node [shape=box, fontname=\"Consolas\"];\n");
        sb.append("  raiz [label=\"INICIO\"];\n");

        int contador = 0;
        if (instrucciones != null) {
            for (Instruccion ins : instrucciones) {
                String id = "n" + (contador++);
                String label = ins.getClass().getSimpleName();
                sb.append("  ").append(id)
                  .append(" [label=\"").append(label).append("\"];\n");
                sb.append("  raiz -> ").append(id).append(";\n");
            }
        }

        sb.append("}\n");
        return sb.toString();
    }
}
