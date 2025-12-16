package expresiones;

import abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import excepciones.Errores;

public class Logicas extends Instruccion {

    private Instruccion cond1;
    private Instruccion cond2; // para NOT será null
    private OperadoresLogicos operador;

    // AND / OR / XOR
    public Logicas(Instruccion cond1, Instruccion cond2,
                   OperadoresLogicos operador,
                   int linea, int col) {
        super(new Tipo(tipoDato.BOOLEANO), linea, col);
        this.cond1 = cond1;
        this.cond2 = cond2;
        this.operador = operador;
    }

    // NOT
    public Logicas(Instruccion cond1,
                   OperadoresLogicos operador,
                   int linea, int col) {
        super(new Tipo(tipoDato.BOOLEANO), linea, col);
        this.cond1 = cond1;
        this.cond2 = null;
        this.operador = operador;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // Primer operando
        Object v1 = cond1.interpretar(arbol, tabla);
        if (v1 instanceof Errores) return v1;

        // -------- NOT --------
        if (operador == OperadoresLogicos.NOT) {
            Boolean b1 = convertirABoolean(v1);
            if (b1 == null) {
                return new Errores(
                    "SEMANTICO",
                    "El operador ! solo se puede aplicar a expresiones logicas (boolean, relacionales, etc.)",
                    this.linea,
                    this.col
                );
            }
            this.tipo.setTipo(tipoDato.BOOLEANO);
            return !b1;
        }

        // -------- AND / OR / XOR --------
        Object v2 = cond2.interpretar(arbol, tabla);
        if (v2 instanceof Errores) return v2;

        Boolean b1 = convertirABoolean(v1);
        Boolean b2 = convertirABoolean(v2);

        if (b1 == null || b2 == null) {
            return new Errores(
                "SEMANTICO",
                "Operadores logicos &&, || y ^ solo admiten expresiones logicas (que se puedan evaluar a true/false)",
                this.linea,
                this.col
            );
        }

        this.tipo.setTipo(tipoDato.BOOLEANO);

        return switch (operador) {
            case AND -> b1 && b2;
            case OR  -> b1 || b2;
            case XOR -> b1 ^  b2;   // XOR logico
            default  -> new Errores(
                            "SEMANTICO",
                            "Operador logico invalido",
                            this.linea,
                            this.col
                        );
        };
    }

    /**
     * Intenta convertir el valor a Boolean:
     * - Boolean  -> se regresa tal cual
     * - Number   -> 0 = false, cualquier otro = true
     * - Character-> '\0' = false, cualquier otro = true
     * - Otros    -> null (indica que NO se puede convertir)
     *
     * Esto hace tu lenguaje más tolerante si alguna expresión
     * relacional o lógica interna no marcó bien el tipo.
     */
    private Boolean convertirABoolean(Object v) {
        if (v instanceof Boolean) {
            return (Boolean) v;
        }
        if (v instanceof Number) {
            double d = ((Number) v).doubleValue();
            return d != 0.0;
        }
        if (v instanceof Character) {
            char c = (Character) v;
            return c != '\0';
        }
        return null; // no se puede convertir
    }
}
