package analisis;

import java.io.FileReader;
import java.util.LinkedList;

import abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.tablaSimbolos;

public class MainPrueba {

    public static void main(String[] args) {
        try {
            // 1. Leer el archivo de entrada
            FileReader fr = new FileReader("entrada2.txt"); 
            // Si pusiste el archivo en otro lado, ajusta la ruta

            // 2. Crear scanner y parser
            scanner s = new scanner(fr);
            parser p = new parser(s);

            // 3. Parsear y obtener la lista de instrucciones (INICIO)
            @SuppressWarnings("unchecked")
            LinkedList<Instruccion> instrucciones = (LinkedList<Instruccion>) p.parse().value;

            System.out.println("✅ Análisis sintáctico finalizado sin errores.");
            System.out.println("Se generaron " + instrucciones.size() + " instrucciones.");

            // 4. Crear el árbol con las instrucciones
            Arbol arbol = new Arbol(instrucciones);

            // 5. Obtener la tabla global desde el árbol
            tablaSimbolos global = arbol.getTablaGlobal();

            // 6. Interpretar cada instrucción
            for (Instruccion ins : instrucciones) {
                ins.interpretar(arbol, global);
            }

            // 7. Mostrar lo que se imprimió (si Print usa arbol.Print)
            System.out.println("---- SALIDA DEL PROGRAMA ----");
            System.out.println(arbol.getConsolas());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
