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

            // =========================
            // VALIDACIÓN PARA LISTAS
            // =========================
            if (this.tipo.esLista()) {

                if (!(valorInterpretado instanceof java.util.LinkedList)) {
                    return new Errores(
                            "SEMANTICO",
                            "Se esperaba una lista en la declaración de '" + this.identificador + "'",
                            this.linea, this.col
                    );
                }

                // Caso list() vacío: ListaLiteral queda como LISTA<VOID>, tipamos por contexto
                if (this.valor.tipo != null
                        && this.valor.tipo.esLista()
                        && this.valor.tipo.getSubtipo() != null
                        && this.valor.tipo.getSubtipo().getTipo() == tipoDato.VOID) {

                    this.valor.tipo = this.tipo;
                } else {
                    // Validar list<T> vs list<T>
                    if (this.valor.tipo == null
                            || !this.valor.tipo.esLista()
                            || this.valor.tipo.getSubtipo() == null
                            || this.tipo.getSubtipo() == null
                            || this.valor.tipo.getSubtipo().getTipo() != this.tipo.getSubtipo().getTipo()) {

                        return new Errores(
                                "SEMANTICO",
                                "Tipos erroneos en la declaración de '" + this.identificador + "'",
                                this.linea, this.col
                        );
                    }
                }

            }
            // =========================
            // VALIDACIÓN VECTORES / ESCALARES (tu lógica actual)
            // =========================
            else {
                if (this.valor.tipo.getTipo() != this.tipo.getTipo()
                    || this.valor.tipo.getDimensiones() != this.tipo.getDimensiones()) {

                    // excepción: permitir [] cuando es vector declarado
                    if (this.tipo.esVector()
                        && this.valor.tipo.esVector()
                        && this.valor.tipo.getDimensiones() == this.tipo.getDimensiones()
                        && (valorInterpretado instanceof Object[] arr)
                        && arr.length == 0) {

                        this.valor.tipo = this.tipo;

                    } else {
                        return new Errores(
                                "SEMANTICO",
                                "Tipos erroneos en la declaración de '" + this.identificador + "'",
                                this.linea, this.col
                        );
                    }
                }
            }
        }

        // Si NO trae asignación → asignar valor por defecto
        else {

            // default lista: lista vacía
            if (this.tipo.esLista()) {
                valorInterpretado = new java.util.LinkedList<Object>();
            }
            // default vector
            else if (this.tipo.esVector()) {
                valorInterpretado = new Object[0];
            }
            // default escalar
            else {
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
            String t;

            // lista: list<T>
            if (this.tipo.esLista()) {
                String sub = "VOID";
                if (this.tipo.getSubtipo() != null) {
                    sub = this.tipo.getSubtipo().getTipo().toString();
                }
                t = "list<" + sub + ">";
            }
            // vector: tipo[]
            else {
                t = this.tipo.getTipo().toString();
                if (this.tipo.esVector()) {
                    t += "[]";
                }
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
