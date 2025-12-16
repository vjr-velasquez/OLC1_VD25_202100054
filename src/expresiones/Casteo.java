package expresiones;

import abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.tipoDato;
import Simbolo.tablaSimbolos;
import excepciones.Errores;

public class Casteo extends Instruccion {

    private Tipo tipoDestino;
    private Instruccion expresion;

    public Casteo(Tipo tipoDestino, Instruccion expresion, int linea, int col) {
        // iniciamos con VOID y luego lo ajustamos
        super(new Tipo(tipoDato.VOID), linea, col);
        this.tipoDestino = tipoDestino;
        this.expresion = expresion;
        // el tipo del resultado será el tipo destino
        this.tipo.setTipo(tipoDestino.getTipo());
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        Object val = expresion.interpretar(arbol, tabla);
        if (val instanceof Errores) {
            return val;
        }

        tipoDato origen  = expresion.tipo.getTipo();
        tipoDato destino = tipoDestino.getTipo();

        switch (destino) {
            case ENTERO -> {
                switch (origen) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) val;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return ((Double) val).intValue();
                    }
                    case CARACTER -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) ((Character) val).charValue();
                    }
                    default -> {
                        return new Errores(
                            "SEMANTICO",
                            "No se puede castear de " + origen + " a ENTERO",
                            this.linea, this.col
                        );
                    }
                }
            }

            case DECIMAL -> {
                switch (origen) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return ((Integer) val).doubleValue();
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (Double) val;
                    }
                    case CARACTER -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) ((Character) val).charValue();
                    }
                    default -> {
                        return new Errores(
                            "SEMANTICO",
                            "No se puede castear de " + origen + " a DECIMAL",
                            this.linea, this.col
                        );
                    }
                }
            }

            case CARACTER -> {
                switch (origen) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.CARACTER);
                        return (char) ((int) val);
                    }
                    case CARACTER -> {
                        this.tipo.setTipo(tipoDato.CARACTER);
                        return val;
                    }
                    default -> {
                        return new Errores(
                            "SEMANTICO",
                            "No se puede castear de " + origen + " a CARACTER",
                            this.linea, this.col
                        );
                    }
                }
            }

            default -> {
                return new Errores(
                    "SEMANTICO",
                    "Tipo destino inválido en casteo: " + destino,
                    this.linea, this.col
                );
            }
        }
    }
}
