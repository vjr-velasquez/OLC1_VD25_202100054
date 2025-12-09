package analisis;

public class Generador {
    public static void main(String[] args){
        generarCompilador();
    }

    public static void generarCompilador(){
        try{    
           // carpeta donde están lexico.jflex y sintactico.cup
           String ruta = "src/analisis/";

           // JFlex: genera scanner.java
           String Flex[] = { ruta + "lexico.jflex", "-d", ruta };
           jflex.Main.generate(Flex);

           // CUP: genera parser.java y sym.java
           String Cup[] = { "-destdir", ruta, "-parser", "parser", ruta + "sintactico.cup" };
           java_cup.Main.main(Cup);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
