package instrucciones;

import Simbolo.Arbol;
import Simbolo.Simbolo;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import abstracto.Instruccion;
import excepciones.Errores;

public class AsignacionVector extends Instruccion {

    private String id;
    private Instruccion indice;
    private Instruccion valor;

    public AsignacionVector(String id, Instruccion indice, Instruccion valor, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.id = id;
        this.indice = indice;
        this.valor = valor;
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
        if (!(rawArr instanceof Object[] arr)) {
            return new Errores("SEMANTICO", "Valor interno de '" + id + "' no es vector", this.linea, this.col);
        }

        Object idxVal = indice.interpretar(arbol, tabla);
        if (idxVal instanceof Errores) return idxVal;

        if (indice.tipo.getTipo() != tipoDato.ENTERO) {
            return new Errores("SEMANTICO", "Índice de vector debe ser ENTERO", this.linea, this.col);
        }

        int idx = (int) idxVal;
        if (idx < 0 || idx >= arr.length) {
            return new Errores("SEMANTICO", "Índice fuera de rango en '" + id + "': " + idx, this.linea, this.col);
        }

        Object val = valor.interpretar(arbol, tabla);
        if (val instanceof Errores) return val;

        // validar tipo base
        tipoDato base = s.getTipo().getTipo();
        if (valor.tipo.getTipo() != base) {
            return new Errores(
                    "SEMANTICO",
                    "Tipos erroneos al asignar en vector '" + id + "': se esperaba " + base + " y viene " + valor.tipo.getTipo(),
                    this.linea, this.col
            );
        }

        arr[idx] = val;
        s.setValor(arr); // no es obligatorio, pero queda explícito
        return null;
    }
}
