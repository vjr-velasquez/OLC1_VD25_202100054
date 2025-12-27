package Simbolo;

import abstracto.Instruccion;
import excepciones.Errores;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

import instrucciones.Funcion;
import instrucciones.Clase;

public class Arbol {
    private LinkedList<Instruccion> instrucciones;
    private tablaSimbolos tablaGlobal;
    private LinkedList<Errores> errores;
    private String consolas;

    // =========================
    // FUNCIONES (FASE 2)
    // =========================
    // Mapa global de funciones por nombre (sin sobrecarga).
    // Si más adelante quieres sobrecarga, cambiamos la clave a firma: nombre + "(" + tipos + ")"
    private Map<String, Funcion> funciones;

    // =========================
    // CLASES (FASE 2)
    // =========================
    private Map<String, Clase> clases;

    public Arbol(LinkedList<Instruccion> instrucciones) {
        this.instrucciones = instrucciones;
        this.tablaGlobal = new tablaSimbolos();
        this.errores = new LinkedList<>();
        this.consolas = "";

        this.funciones = new HashMap<>();
        this.clases = new HashMap<>();
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
    public void Print(String valor) {
        this.consolas += valor + "\n";
    }

    // =========================
    // FUNCIONES: API
    // =========================

    /** Devuelve el mapa de funciones (por si quieres reportarlo) */
    public Map<String, Funcion> getFunciones() {
        return funciones;
    }

    /** Registrar función en el entorno global */
    public boolean registrarFuncion(Funcion f) {
        if (f == null) return false;

        String nombre = f.getNombre();
        if (nombre == null) return false;

        if (this.funciones.containsKey(nombre)) {
            // error semántico: función ya existe
            Errores err = new Errores(
                    "SEMANTICO",
                    "Función ya declarada: " + nombre,
                    f.getLinea(),
                    f.getCol()
            );
            addError(err);
            return false;
        }

        this.funciones.put(nombre, f);
        return true;
    }

    /** Obtener función por nombre */
    public Funcion getFuncion(String nombre) {
        if (nombre == null) return null;
        return this.funciones.get(nombre);
    }

    /** Verifica si existe Start y cumple firma básica: void Start() */
    public boolean validarStart() {
        Funcion start = getFuncion("Start");
        if (start == null) {
            addError(new Errores("SEMANTICO", "No existe la función Start() como punto de entrada.", 0, 0));
            return false;
        }

        // Debe ser VOID
        if (start.getTipoRetorno() == null || start.getTipoRetorno().getTipo() != tipoDato.VOID) {
            addError(new Errores("SEMANTICO", "Start() debe ser de tipo void.", start.getLinea(), start.getCol()));
            return false;
        }

        // Debe tener 0 parámetros
        if (start.getParametros() != null && !start.getParametros().isEmpty()) {
            addError(new Errores("SEMANTICO", "Start() no debe recibir parámetros.", start.getLinea(), start.getCol()));
            return false;
        }

        return true;
    }

    /**
     * Ejecuta Start() (asumiendo que ya registraste todas las funciones).
     * La ejecución real la va a hacer Funcion.ejecutar(...)
     */
    public Object ejecutarStart() {
        if (!validarStart()) return null;

        Funcion start = getFuncion("Start");
        try {
            // tabla local hija de global (si tu tablaSimbolos soporta padre)
            // Si tu tablaSimbolos aún no tiene constructor con padre, luego lo ajustamos.
            tablaSimbolos local = new tablaSimbolos(this.tablaGlobal);

            // Start sin args
            return start.ejecutar(this, local, new LinkedList<>());
        } catch (Exception ex) {
            addError(new Errores("SEMANTICO", "Excepción ejecutando Start(): " + ex.getMessage(), start.getLinea(), start.getCol()));
            return null;
        }
    }

    // =========================
    // CLASES: API
    // =========================

    public Map<String, Clase> getClases() {
        return clases;
    }

    public boolean existeClase(String nombre) {
        if (nombre == null) return false;
        return this.clases.containsKey(nombre);
    }

    public boolean registrarClase(String nombre, Clase c) {
        if (nombre == null || c == null) return false;

        if (this.clases.containsKey(nombre)) {
            addError(new Errores(
                    "SEMANTICO",
                    "Clase ya declarada: " + nombre,
                    c.linea,
                    c.col
            ));
            return false;
        }

        this.clases.put(nombre, c);
        return true;
    }

    public Clase getClase(String nombre) {
        if (nombre == null) return null;
        return this.clases.get(nombre);
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
                    insts.agregarHijo(ins.getNodoAST());
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
