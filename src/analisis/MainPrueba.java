package analisis;

import java.io.FileReader;
import java.io.PrintWriter;
import java.util.LinkedList;

import abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.tablaSimbolos;

import excepciones.ControlErrores;
import excepciones.Errores;
import java_cup.runtime.Symbol;

public class MainPrueba {

    public static void main(String[] args) {
        try {
            // Limpia listas de ejecuciones anteriores
            scanner.listaTokens.clear();
            ControlErrores.limpiar();

            // 1. Leer el archivo de entrada
            FileReader fr = new FileReader("entrada2.txt");
            // Cambia el nombre si quieres probar otro archivo, por ejemplo:
            // FileReader fr = new FileReader("prueba_switch.txt");

            // 2. Crear scanner y parser
            scanner s = new scanner(fr);
            parser p = new parser(s);

            System.out.println(">>> INICIANDO PARSEO");

            // 3. Parsear y obtener el resultado
            Symbol resultadoParseo = p.parse();

            System.out.println(">>> PARSEO TERMINADO");

            LinkedList<Instruccion> instrucciones = new LinkedList<>();

            if (resultadoParseo != null && resultadoParseo.value != null) {
                @SuppressWarnings("unchecked")
                LinkedList<Instruccion> tmp =
                        (LinkedList<Instruccion>) resultadoParseo.value;
                instrucciones = tmp;
            }

            System.out.println("? Análisis sintáctico finalizado.");
            System.out.println("Se generaron " + instrucciones.size() + " instrucciones.");

            // 4. Crear el árbol con las instrucciones
            Arbol arbol = new Arbol(instrucciones);

            // 5. Obtener la tabla global desde el árbol
            tablaSimbolos global = arbol.getTablaGlobal();

            // 6. Interpretar cada instrucción
            for (Instruccion ins : instrucciones) {
                Object res = ins.interpretar(arbol, global);

                // Si quisieras ver errores semánticos individuales:
                if (res instanceof Errores) {
                    Errores err = (Errores) res;
                    System.out.println(err);
                    ControlErrores.agregarError(err);
                }
            }

            // 7. Mostrar lo que se imprimió (si Print usa arbol.Print)
            System.out.println("---- SALIDA DEL PROGRAMA ----");
            System.out.println(arbol.getConsolas());

            // 8. Generar archivo DOT del AST
            try (PrintWriter pw = new PrintWriter("ast.dot")) {
                pw.println(arbol.generarDotAST());
            }
            System.out.println("AST en formato DOT generado en el archivo: ast.dot");

            // 9. Generar reportes HTML de tokens y errores
            Token.generarReporteHTML(scanner.listaTokens);
            ControlErrores.generarReporteHTML();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
