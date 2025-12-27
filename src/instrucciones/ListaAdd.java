package instrucciones;

import Simbolo.Arbol;
import Simbolo.Simbolo;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import abstracto.Instruccion;
import excepciones.Errores;
import java.util.LinkedList;

public class ListaAdd extends Instruccion {

    private String id;
    private Instruccion valor;

    public ListaAdd(String id, Instruccion valor, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.id = id;
        this.valor = valor;
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

        Object val = valor.interpretar(arbol, tabla);
        if (val instanceof Errores) return val;

        Tipo sub = s.getTipo().getSubtipo();
        tipoDato base = (sub != null) ? sub.getTipo() : tipoDato.VOID;

        // Si la lista aún es list<void> (por venir de list()), tipamos por primer add
        if (base == tipoDato.VOID) {
            s.getTipo().setSubtipo(new Tipo(valor.tipo.getTipo()));
            base = valor.tipo.getTipo();
        }

        if (valor.tipo.getTipo() != base) {
            return new Errores(
                    "SEMANTICO",
                    "Tipos erroneos en add() de '" + id + "': se esperaba " + base + " y viene " + valor.tipo.getTipo(),
                    this.linea, this.col
            );
        }

        lista.add(val);
        s.setValor(lista);
        return null;
    }
}
