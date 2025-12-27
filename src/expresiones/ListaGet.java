package expresiones;

import Simbolo.Arbol;
import Simbolo.Simbolo;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import abstracto.Instruccion;
import excepciones.Errores;
import java.util.LinkedList;

public class ListaGet extends Instruccion {

    private String id;
    private Instruccion indice;

    public ListaGet(String id, Instruccion indice, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.id = id;
        this.indice = indice;
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

        Object idxVal = indice.interpretar(arbol, tabla);
        if (idxVal instanceof Errores) return idxVal;

        if (indice.tipo.getTipo() != tipoDato.ENTERO) {
            return new Errores("SEMANTICO", "Índice de lista debe ser ENTERO", this.linea, this.col);
        }

        int idx = (int) idxVal;
        if (idx < 0 || idx >= lista.size()) {
            return new Errores("SEMANTICO", "Índice fuera de rango en '" + id + "': " + idx, this.linea, this.col);
        }

        // tipo resultante = subtipo de la lista
        Tipo sub = s.getTipo().getSubtipo();
        this.tipo = (sub != null) ? new Tipo(sub.getTipo()) : new Tipo(tipoDato.VOID);

        return lista.get(idx);
    }
}
