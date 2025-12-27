package expresiones;

import Simbolo.Arbol;
import Simbolo.Simbolo;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import abstracto.Instruccion;
import excepciones.Errores;
import java.util.LinkedList;

public class ListaSize extends Instruccion {

    private String id;

    public ListaSize(String id, int linea, int col) {
        super(new Tipo(tipoDato.ENTERO), linea, col);
        this.id = id;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {

        Simbolo s = tabla.getVariable(id);
        if (s == null) return new Errores("SEMANTICO", "Lista '" + id + "' no existe", this.linea, this.col);

        if (!s.getTipo().esLista()) {
            return new Errores("SEMANTICO", "'" + id + "' no es una lista", this.linea, this.col);
        }

        Object raw = s.getValor();
        if (!(raw instanceof LinkedList)) {
            return new Errores("SEMANTICO", "Valor interno de '" + id + "' no es lista", this.linea, this.col);
        }

        @SuppressWarnings("unchecked")
        LinkedList<Object> lista = (LinkedList<Object>) raw;

        this.tipo = new Tipo(tipoDato.ENTERO);
        return lista.size();
    }
}
