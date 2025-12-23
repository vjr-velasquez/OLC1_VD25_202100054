package Simbolo;

import abstracto.Instruccion;
import excepciones.Errores;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

    // =========================
    // AST REAL (NodoAST + DOT)
    // =========================

    /** Construye el NodoAST raíz del programa */
    public NodoAST getNodoAST() {
        NodoAST raiz = new NodoAST("INICIO");
        NodoAST insts = new NodoAST("INSTRUCCIONES");
        raiz.agregarHijo(insts);

        if (instrucciones != null) {
            for (Instruccion ins : instrucciones) {
                if (ins != null) {
                    insts.agregarHijo(ins.getNodoAST()); // opción B: siempre hay algo
                }
            }
        }
        return raiz;
    }

    /** Genera el AST en formato DOT recorriendo recursivamente NodoAST */
    public String generarDotAST() {
        NodoAST raiz = getNodoAST();

        StringBuilder sb = new StringBuilder();
        sb.append("digraph AST {\n");
        sb.append("  rankdir=TB;\n");
        sb.append("  node [shape=box, fontname=\"Consolas\"];\n");

        int[] contador = new int[]{0};
        String idRaiz = "n" + contador[0]++;
        sb.append("  ").append(idRaiz).append(" [label=\"").append(escape(raiz.etiqueta)).append("\"];\n");

        recorrerDot(raiz, idRaiz, sb, contador);

        sb.append("}\n");
        return sb.toString();
    }

    /** Recorrido DFS para imprimir nodos e hijos */
    private void recorrerDot(NodoAST actual, String idActual, StringBuilder sb, int[] contador) {
        if (actual.hijos == null) return;

        for (NodoAST hijo : actual.hijos) {
            if (hijo == null) continue;

            String idHijo = "n" + contador[0]++;
            sb.append("  ").append(idHijo)
              .append(" [label=\"").append(escape(hijo.etiqueta)).append("\"];\n");
            sb.append("  ").append(idActual).append(" -> ").append(idHijo).append(";\n");

            recorrerDot(hijo, idHijo, sb, contador);
        }
    }

    /** Escapar caracteres problemáticos en DOT */
    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }
    
        public void generarAstPng(String dotPath, String pngPath) throws IOException, InterruptedException {
        // 1) escribir DOT
        String dot = generarDotAST();
        Files.writeString(Path.of(dotPath), dot, StandardCharsets.UTF_8);

        // 2) ejecutar Graphviz: dot -Tpng ast.dot -o ast.png
        ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", dotPath, "-o", pngPath);
        pb.redirectErrorStream(true);

        Process p = pb.start();
        String salida = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        int code = p.waitFor();

        if (code != 0) {
            throw new IOException("Graphviz falló (code=" + code + "):\n" + salida);
        }
    }

}
