package instrucciones;

import Simbolo.Arbol;
import Simbolo.Simbolo;
import Simbolo.Tipo;
import Simbolo.NodoAST;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import abstracto.Instruccion;
import excepciones.Errores;

public class Declaracion extends Instruccion {
    public String identificador;
    public Instruccion valor;

    public Declaracion(String identificador, Instruccion valor, Tipo tipo, int linea, int col) {
        super(tipo, linea, col);
        this.identificador = identificador;
        this.valor = valor;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla){

        Object valorInterpretado = null;

        // Si la declaración trae asignación
        if (this.valor != null) {
            valorInterpretado = this.valor.interpretar(arbol, tabla);
            if (valorInterpretado instanceof Errores){
                return valorInterpretado;
            }

            // ✅ Validación de tipo + dimensiones (vector vs no vector)
            if (this.valor.tipo.getTipo() != this.tipo.getTipo()
                || this.valor.tipo.getDimensiones() != this.tipo.getDimensiones()) {
                return new Errores(
                        "SEMANTICO",
                        "Tipos erroneos en la declaración de '" + this.identificador + "'",
                        this.linea, this.col
                );
            }
        }

        // Si NO trae asignación → asignar valor por defecto
        else {
            // ✅ si es vector, valor por defecto: vector vacío
            if (this.tipo.esVector()) {
                valorInterpretado = new Object[0];
            } else {
                // valor por defecto según el tipo
                switch(this.tipo.getTipo()){
                    case ENTERO:   valorInterpretado = 0; break;
                    case DECIMAL:  valorInterpretado = 0.0; break;
                    case BOOLEANO: valorInterpretado = false; break;
                    case CADENA:   valorInterpretado = ""; break;
                    default:       valorInterpretado = null; break;
                }
            }
        }

        Simbolo s = new Simbolo(this.tipo, this.identificador, valorInterpretado);
        boolean creacion = tabla.setVariables(s);

        if(!creacion){
            return new Errores("SEMANTICO", "Variable ya existente", this.linea, this.col);
        }

        return null;
    }

    @Override
    public NodoAST getNodoAST() {
        NodoAST nodo = new NodoAST("DECLARACION");

        // TIPO
        if (this.tipo != null) {
            String t = this.tipo.getTipo().toString();
            if (this.tipo.esVector()) {
                t += "[]";
            }
            nodo.agregarHijo(new NodoAST("TIPO: " + t));
        } else {
            nodo.agregarHijo(new NodoAST("TIPO: null"));
        }

        // ID
        nodo.agregarHijo(new NodoAST("ID: " + this.identificador));

        // ASIGNACION (opcional)
        if (this.valor != null) {
            NodoAST asig = new NodoAST("ASIGNACION");
            NodoAST exp = new NodoAST("EXPRESION");
            exp.agregarHijo(this.valor.getNodoAST());
            asig.agregarHijo(exp);
            nodo.agregarHijo(asig);
        }

        return nodo;
    }
}
