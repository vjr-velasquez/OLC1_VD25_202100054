
package expresiones;
import abstracto.Instruccion;
import Simbolo.*;
import static Simbolo.tipoDato.CADENA;
import static Simbolo.tipoDato.CARACTER;
import static Simbolo.tipoDato.DECIMAL;
import static Simbolo.tipoDato.ENTERO;
import excepciones.Errores;
import static expresiones.OperadoresAritmeticos.DIVISION;


public class Aritmeticas extends Instruccion {
    private Instruccion operando1;
    private Instruccion operando2;
    private OperadoresAritmeticos operaciones;
    private Instruccion operandoUnico;
    // negacion
    public Aritmeticas(OperadoresAritmeticos operaciones, Instruccion operandoUnico, int linea, int col) {
        super(new Tipo(tipoDato.ENTERO), linea, col);
        this.operaciones = operaciones;
        this.operandoUnico = operandoUnico;
    }
    // aritmetica 
    public Aritmeticas(Instruccion operando1, Instruccion operando2, OperadoresAritmeticos operaciones, int linea, int col) {
        super(new Tipo(tipoDato.ENTERO), linea, col);
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.operaciones = operaciones;
    }
    
    public Object interpretar(Arbol arbol, tablaSimbolos tabla){
        Object opIzq = null, opDer = null, Unico = null;
        if (this.operandoUnico!=null){
            Unico = this.operandoUnico.interpretar(arbol, tabla);
            if (Unico instanceof Errores) {
                return Unico;
            }
        }else{
            opIzq = this.operando1.interpretar(arbol, tabla);
            if (opIzq instanceof Errores){
                return opIzq;
            }
            opDer = this.operando2.interpretar(arbol, tabla);
            if (opDer instanceof Errores){
                return opDer;
            }
        }
        return switch (operaciones){
            case SUMA ->
                this.suma(opIzq, opDer);
            case RESTA ->
                this.resta(opIzq, opDer);
            case MULTIPLICACION ->
                this.multiplicacion(opIzq, opDer);
            case DIVISION ->
                this.division(opIzq, opDer);
            case MODULO ->
                this.modulo(opIzq, opDer);
            case NEGACION ->
                this.negacion(Unico);
            default -> 
                new Errores("ERROR semantico", "operando inexistente", this.linea, this.col);
                 
        };
        
        
    }
    
    public Object suma(Object op1, Object op2){
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();
        
        switch(tipo1) {
            case ENTERO -> {
                switch (tipo2){
                    case ENTERO ->{
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) op1 + (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (int)op1 + (double) op2;
                    }
                    case CADENA ->{
                        this.tipo.setTipo(tipoDato.CADENA);
                        return op1.toString() + op2.toString();
                      
                    }
                    default -> {
                        return new Errores("ERROR semantico", "suma erronea", this.linea, this.col);
                    }
                }
            }
            case DECIMAL -> {
                switch (tipo2){
                    case ENTERO ->{
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) op1 + (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double)op1 + (double) op2;
                    }
                    case CADENA ->{
                        this.tipo.setTipo(tipoDato.CADENA);
                        return op1.toString() + op2.toString();
                      
                    }
                    default -> {
                        return new Errores("ERROR semantico", "suma erronea", this.linea, this.col);
                    }
                }
            }
            case CARACTER -> {
                switch (tipo2){
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int)((Character) op1) + (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double)((Character) op1) + (double) op2;
                    }
                    case CARACTER -> {
                        this.tipo.setTipo(tipoDato.CADENA);
                        return op1.toString() + op2.toString();
                    }
                    case CADENA -> {
                        this.tipo.setTipo(tipoDato.CADENA);
                        return op1.toString() + op2.toString();
                    }
                    default -> {
                        return new Errores("ERROR semantico", "suma erronea", this.linea, this.col);
                    }
                }
            }
            case CADENA ->{
                this.tipo.setTipo(tipoDato.CADENA);
                return op1.toString() + op2.toString();
            }
            
            default -> {
                return new Errores("ERROR semantico", "suma erronea", this.linea, this.col);
            }
        }
        
        
    }
    
    public Object resta(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        switch (tipo1) {
            case ENTERO -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) op1 - (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return ((int) op1) - ((double) op2);
                    }
                    case CARACTER -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) op1 - (int) ((Character) op2);
                    }
                    default -> {
                        return new Errores(
                            "ERROR semantico",
                            "resta erronea entre " + tipo1 + " y " + tipo2,
                            this.linea,
                            this.col
                        );
                    }
                }
            }
            case DECIMAL -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return ((double) op1) - ((int) op2);
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return ((double) op1) - ((double) op2);
                    }
                    case CARACTER -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return ((double) op1) - (double) ((Character) op2);
                    }
                    default -> {
                        return new Errores(
                            "ERROR semantico",
                            "resta erronea entre " + tipo1 + " y " + tipo2,
                            this.linea,
                            this.col
                        );
                    }
                }
            }
            case CARACTER -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) ((Character) op1) - (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) ((Character) op1) - (double) op2;
                    }
                    case CARACTER -> {
                        // tabla marca esta combinación como inválida (rojo)
                        return new Errores(
                            "ERROR semantico",
                            "resta invalida entre CARACTER y CARACTER",
                            this.linea,
                            this.col
                        );
                    }
                    default -> {
                        return new Errores(
                            "ERROR semantico",
                            "resta erronea entre " + tipo1 + " y " + tipo2,
                            this.linea,
                            this.col
                        );
                    }
                }
            }
            default -> {
                // Cualquier otro tipo (CADENA, BOOLEANO, etc.) es inválido para resta
                return new Errores(
                    "ERROR semantico",
                    "resta erronea entre " + tipo1 + " y " + tipo2,
                    this.linea,
                    this.col
                );
            }
        }
    }

    public Object multiplicacion(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        switch (tipo1) {
            case ENTERO -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) op1 * (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return ((int) op1) * ((double) op2);
                    }
                    case CARACTER -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) op1 * (int) ((Character) op2);
                    }
                    default -> {
                        return new Errores(
                            "ERROR semantico",
                            "multiplicacion erronea entre " + tipo1 + " y " + tipo2,
                            this.linea,
                            this.col
                        );
                    }
                }
            }
            case DECIMAL -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return ((double) op1) * ((int) op2);
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return ((double) op1) * ((double) op2);
                    }
                    case CARACTER -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return ((double) op1) * (double) ((Character) op2);
                    }
                    default -> {
                        return new Errores(
                            "ERROR semantico",
                            "multiplicacion erronea entre " + tipo1 + " y " + tipo2,
                            this.linea,
                            this.col
                        );
                    }
                }
            }
            case CARACTER -> {
                switch (tipo2) {
                    case ENTERO -> {
                        this.tipo.setTipo(tipoDato.ENTERO);
                        return (int) ((Character) op1) * (int) op2;
                    }
                    case DECIMAL -> {
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) ((Character) op1) * (double) op2;
                    }
                    case CARACTER -> {
                        // combinación inválida según la tabla (rojo)
                        return new Errores(
                            "ERROR semantico",
                            "multiplicacion invalida entre CARACTER y CARACTER",
                            this.linea,
                            this.col
                        );
                    }
                    default -> {
                        return new Errores(
                            "ERROR semantico",
                            "multiplicacion erronea entre " + tipo1 + " y " + tipo2,
                            this.linea,
                            this.col
                        );
                    }
                }
            }
            default -> {
                return new Errores(
                    "ERROR semantico",
                    "multiplicacion erronea entre " + tipo1 + " y " + tipo2,
                    this.linea,
                    this.col
                );
            }
        }
    }
    

    public Object division(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        switch (tipo1) {
            case ENTERO -> {
                switch (tipo2) {
                    case ENTERO -> {
                        // ENTERO / ENTERO -> DECIMAL
                        int v1 = (int) op1;
                        int v2 = (int) op2;
                        if (v2 == 0) {
                            return new Errores("ERROR semantico",
                                    "division por cero",
                                    this.linea, this.col);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) v1 / v2;
                    }
                    case DECIMAL -> {
                        // ENTERO / DECIMAL -> DECIMAL
                        int v1 = (int) op1;
                        double v2 = (double) op2;
                        if (v2 == 0.0) {
                            return new Errores("ERROR semantico",
                                    "division por cero",
                                    this.linea, this.col);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return v1 / v2;
                    }
                    case CARACTER -> {
                        // ENTERO / CARACTER -> DECIMAL
                        int v1 = (int) op1;
                        int v2 = (int) ((Character) op2);
                        if (v2 == 0) {
                            return new Errores("ERROR semantico",
                                    "division por cero",
                                    this.linea, this.col);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) v1 / v2;
                    }
                    default -> {
                        return new Errores("ERROR semantico",
                                "division erronea entre " + tipo1 + " y " + tipo2,
                                this.linea, this.col);
                    }
                }
            }

            case DECIMAL -> {
                switch (tipo2) {
                    case ENTERO -> {
                        // DECIMAL / ENTERO -> DECIMAL
                        double v1 = (double) op1;
                        int v2 = (int) op2;
                        if (v2 == 0) {
                            return new Errores("ERROR semantico",
                                    "division por cero",
                                    this.linea, this.col);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return v1 / v2;
                    }
                    case DECIMAL -> {
                        // DECIMAL / DECIMAL -> DECIMAL
                        double v1 = (double) op1;
                        double v2 = (double) op2;
                        if (v2 == 0.0) {
                            return new Errores("ERROR semantico",
                                    "division por cero",
                                    this.linea, this.col);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return v1 / v2;
                    }
                    case CARACTER -> {
                        // DECIMAL / CARACTER -> DECIMAL
                        double v1 = (double) op1;
                        int v2 = (int) ((Character) op2);
                        if (v2 == 0) {
                            return new Errores("ERROR semantico",
                                    "division por cero",
                                    this.linea, this.col);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return v1 / v2;
                    }
                    default -> {
                        return new Errores("ERROR semantico",
                                "division erronea entre " + tipo1 + " y " + tipo2,
                                this.linea, this.col);
                    }
                }
            }

            case CARACTER -> {
                switch (tipo2) {
                    case ENTERO -> {
                        // CARACTER / ENTERO -> DECIMAL
                        int v1 = (int) ((Character) op1);
                        int v2 = (int) op2;
                        if (v2 == 0) {
                            return new Errores("ERROR semantico",
                                    "division por cero",
                                    this.linea, this.col);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) v1 / v2;
                    }
                    case DECIMAL -> {
                        // CARACTER / DECIMAL -> DECIMAL
                        int v1 = (int) ((Character) op1);
                        double v2 = (double) op2;
                        if (v2 == 0.0) {
                            return new Errores("ERROR semantico",
                                    "division por cero",
                                    this.linea, this.col);
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return v1 / v2;
                    }
                    case CARACTER -> {
                        // combinación inválida según tabla
                        return new Errores("ERROR semantico",
                                "division invalida entre CARACTER y CARACTER",
                                this.linea, this.col);
                    }
                    default -> {
                        return new Errores("ERROR semantico",
                                "division erronea entre " + tipo1 + " y " + tipo2,
                                this.linea, this.col);
                    }
                }
            }

            default -> {
                // Cualquier otro tipo (CADENA, BOOLEANO, etc.) es inválido
                return new Errores("ERROR semantico",
                        "division erronea entre " + tipo1 + " y " + tipo2,
                        this.linea, this.col);
            }
        }
    }

    
    public Object potencia(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        switch (tipo1) {
            case ENTERO -> {
                switch (tipo2) {
                    case ENTERO -> {
                        // ENTERO ^ ENTERO -> ENTERO
                        int base = (int) op1;
                        int exp  = (int) op2;
                        this.tipo.setTipo(tipoDato.ENTERO);
                        // pow devuelve double, lo casteamos a int
                        return (int) Math.pow(base, exp);
                    }
                    case DECIMAL -> {
                        // ENTERO ^ DECIMAL -> DECIMAL
                        int base = (int) op1;
                        double exp = (double) op2;
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return Math.pow(base, exp);
                    }
                    default -> {
                        return new Errores(
                            "ERROR semantico",
                            "potencia erronea entre " + tipo1 + " y " + tipo2,
                            this.linea,
                            this.col
                        );
                    }
                }
            }

            case DECIMAL -> {
                switch (tipo2) {
                    case ENTERO -> {
                        // DECIMAL ^ ENTERO -> DECIMAL
                        double base = (double) op1;
                        int exp = (int) op2;
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return Math.pow(base, exp);
                    }
                    case DECIMAL -> {
                        // DECIMAL ^ DECIMAL -> DECIMAL
                        double base = (double) op1;
                        double exp = (double) op2;
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return Math.pow(base, exp);
                    }
                    default -> {
                        return new Errores(
                            "ERROR semantico",
                            "potencia erronea entre " + tipo1 + " y " + tipo2,
                            this.linea,
                            this.col
                        );
                    }
                }
            }

            default -> {
                // Cualquier otro tipo (CARACTER, CADENA, BOOLEANO, etc.) es inválido
                return new Errores(
                    "ERROR semantico",
                    "potencia erronea entre " + tipo1 + " y " + tipo2,
                    this.linea,
                    this.col
                );
            }
        }
    }

    public Object modulo(Object op1, Object op2) {
        var tipo1 = this.operando1.tipo.getTipo();
        var tipo2 = this.operando2.tipo.getTipo();

        switch (tipo1) {
            case ENTERO -> {
                switch (tipo2) {
                    case ENTERO -> {
                        int v1 = (int) op1;
                        int v2 = (int) op2;
                        if (v2 == 0) {
                            return new Errores(
                                "ERROR semantico",
                                "modulo por cero",
                                this.linea, this.col
                            );
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return (double) (v1 % v2);
                    }
                    case DECIMAL -> {
                        int v1 = (int) op1;
                        double v2 = (double) op2;
                        if (v2 == 0.0) {
                            return new Errores(
                                "ERROR semantico",
                                "modulo por cero",
                                this.linea, this.col
                            );
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return v1 % v2;
                    }
                    default -> {
                        return new Errores(
                            "ERROR semantico",
                            "modulo erroneo entre " + tipo1 + " y " + tipo2,
                            this.linea, this.col
                        );
                    }
                }
            }

            case DECIMAL -> {
                switch (tipo2) {
                    case ENTERO -> {
                        double v1 = (double) op1;
                        int v2 = (int) op2;
                        if (v2 == 0) {
                            return new Errores(
                                "ERROR semantico",
                                "modulo por cero",
                                this.linea, this.col
                            );
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return v1 % v2;
                    }
                    case DECIMAL -> {
                        double v1 = (double) op1;
                        double v2 = (double) op2;
                        if (v2 == 0.0) {
                            return new Errores(
                                "ERROR semantico",
                                "modulo por cero",
                                this.linea, this.col
                            );
                        }
                        this.tipo.setTipo(tipoDato.DECIMAL);
                        return v1 % v2;
                    }
                    default -> {
                        return new Errores(
                            "ERROR semantico",
                            "modulo erroneo entre " + tipo1 + " y " + tipo2,
                            this.linea, this.col
                        );
                    }
                }
            }

            default -> {
                // Cualquier otro tipo (CARACTER, CADENA, BOOLEANO, etc.) es inválido
                return new Errores(
                    "ERROR semantico",
                    "modulo erroneo entre " + tipo1 + " y " + tipo2,
                    this.linea, this.col
                );
            }
        }
    }





    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public Object negacion(Object op1){
            var opU = this.operandoUnico.tipo.getTipo();
            switch (opU){
                case ENTERO->{
                    this.tipo.setTipo(tipoDato.ENTERO);
                    return (int) op1 * -1;
                }
                case DECIMAL ->{
                    this.tipo.setTipo(tipoDato.DECIMAL);
                    return (double) op1 * -1;
                }
                default ->{
                   return new Errores("ERROR semantico", "negacion erronea", this.linea, this.col);
                }
            }
        }
}
