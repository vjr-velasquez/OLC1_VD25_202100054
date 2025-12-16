package analisis;

import java.io.FileReader;
import java.io.PrintWriter;
import java.util.LinkedList;

import abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.tablaSimbolos;

public class MainPrueba {

    public static void main(String[] args) {
        try {
            // 1. Leer el archivo de entrada
            FileReader fr = new FileReader("entrada2.txt"); 
            // Cambia el nombre si quieres probar otro archivo, por ejemplo:
            // FileReader fr = new FileReader("prueba_switch.txt");

            // 2. Crear scanner y parser
            scanner s = new scanner(fr);
            parser p = new parser(s);

            System.out.println(">>> INICIANDO PARSEO");
            // 3. Parsear y obtener la lista de instrucciones (INICIO)
            @SuppressWarnings("unchecked")
            LinkedList<Instruccion> instrucciones = (LinkedList<Instruccion>) p.parse().value;
            System.out.println(">>> PARSEO TERMINADO");

            System.out.println("? Análisis sintáctico finalizado sin errores.");
            System.out.println("Se generaron " + instrucciones.size() + " instrucciones.");

            // 4. Crear el árbol con las instrucciones
            Arbol arbol = new Arbol(instrucciones);

            // 5. Obtener la tabla global desde el árbol
            tablaSimbolos global = arbol.getTablaGlobal();

            // 6. Interpretar cada instrucción
            for (Instruccion ins : instrucciones) {
                Object res = ins.interpretar(arbol, global);
                // Si quieres ver errores producidos por instrucciones individuales:
                // if (res instanceof Errores) System.out.println(res);
            }

            // 7. Mostrar lo que se imprimió (si Print usa arbol.Print)
            System.out.println("---- SALIDA DEL PROGRAMA ----");
            System.out.println(arbol.getConsolas());

            // 8. Generar archivo DOT del AST
            try (PrintWriter pw = new PrintWriter("ast.dot")) {
                pw.println(arbol.generarDotAST());
            }
            System.out.println("AST en formato DOT generado en el archivo: ast.dot");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
