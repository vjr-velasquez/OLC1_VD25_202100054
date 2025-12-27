package Simbolo;

import instrucciones.Clase;

public class Instancia {

    private final Clase clase;
    private final tablaSimbolos tablaAtributos;

    public Instancia(Clase clase, tablaSimbolos tablaAtributos) {
        this.clase = clase;
        this.tablaAtributos = tablaAtributos;
    }

    public Clase getClase() {
        return clase;
    }

    public tablaSimbolos getTablaAtributos() {
        return tablaAtributos;
    }
}
