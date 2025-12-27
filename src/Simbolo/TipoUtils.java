package Simbolo;

public class TipoUtils {

    /** Igualdad/compatibilidad estricta de tipos (incluye vector y lista<T>) */
    public static boolean compatibles(Tipo esperado, Tipo obtenido) {
        if (esperado == null || obtenido == null) return false;

        if (esperado.getTipo() != obtenido.getTipo()) return false;
        if (esperado.getDimensiones() != obtenido.getDimensiones()) return false;

        // Si es LISTA, subtipo debe ser compatible
        if (esperado.esLista()) {
            Tipo subE = esperado.getSubtipo();
            Tipo subO = obtenido.getSubtipo();
            return compatibles(subE, subO);
        }

        return true;
    }
}
