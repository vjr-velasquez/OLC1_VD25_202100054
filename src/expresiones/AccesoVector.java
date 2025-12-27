package expresiones;

import Simbolo.Arbol;
import Simbolo.Simbolo;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import abstracto.Instruccion;
import excepciones.Errores;

public class AccesoVector extends Instruccion {

    private String id;
    private Instruccion indice;

    public AccesoVector(String id, Instruccion indice, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.id = id;
        this.indice = indice;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {

        Simbolo s = tabla.getVariable(id);
        if (s == null) {
            return new Errores("SEMANTICO", "Vector/Variable '" + id + "' no existe", this.linea, this.col);
        }

        if (!s.getTipo().esVector()) {
            return new Errores("SEMANTICO", "'" + id + "' no es un vector", this.linea, this.col);
        }

        Object rawArr = s.getValor();
        if (!(rawArr instanceof Object[])) {
            return new Errores("SEMANTICO", "Valor interno de '" + id + "' no es vector", this.linea, this.col);
        }
        Object[] arr = (Object[]) rawArr;

        Object idxVal = indice.interpretar(arbol, tabla);
        if (idxVal instanceof Errores) return idxVal;

        if (indice.tipo.getTipo() != tipoDato.ENTERO) {
            return new Errores("SEMANTICO", "Índice de vector debe ser ENTERO", this.linea, this.col);
        }

        int idx = (int) idxVal;

        if (idx < 0 || idx >= arr.length) {
            return new Errores("SEMANTICO", "Índice fuera de rango en '" + id + "': " + idx, this.linea, this.col);
        }

        // tipo del resultado = tipo base del vector
        this.tipo = new Tipo(s.getTipo().getTipo());
        return arr[idx];
    }

}
