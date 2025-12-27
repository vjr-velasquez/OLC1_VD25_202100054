package expresiones;

import Simbolo.Arbol;
import Simbolo.Instancia;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import abstracto.Instruccion;
import excepciones.Errores;
import instrucciones.Clase;
import instrucciones.Declaracion;

public class NewObjeto extends Instruccion {

    private final String nombreClase;

    public NewObjeto(String nombreClase, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.nombreClase = nombreClase;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {

        Clase c = arbol.getClase(nombreClase);
        if (c == null) {
            return new Errores("SEMANTICO",
                    "No existe la clase: " + nombreClase,
                    this.linea, this.col);
        }

        // tabla de atributos del objeto (padre = global)
        tablaSimbolos tablaObj = new tablaSimbolos(arbol.getTablaGlobal());

        // ejecutar solo atributos al instanciar
        for (Declaracion d : c.getAtributos()) {
            Object r = d.interpretar(arbol, tablaObj);
            if (r instanceof Errores) return r;
        }

        return new Instancia(c, tablaObj);
    }
}
