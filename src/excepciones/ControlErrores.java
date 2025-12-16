package excepciones;

import java.util.LinkedList;

public class ControlErrores {

    public static LinkedList<Errores> listaErrores = new LinkedList<>();

    public static void agregarError(Errores err) {
        if (err != null) {
            listaErrores.add(err);
        }
    }

    public static void limpiar() {
        listaErrores.clear();
    }
}
