package expresiones;
import abstracto.Instruccion;
import Simbolo.*;
import excepciones.Errores;
import static expresiones.OperadoresRelacionales.EQUALS;
import static expresiones.OperadoresRelacionales.MENORQ;


public class Relacionales extends Instruccion {
      private Instruccion cond1;
    private Instruccion cond2;
    private OperadoresRelacionales relacional;

    public Relacionales(Instruccion cond1, Instruccion cond2, OperadoresRelacionales relacional, int linea, int col) {
        super(new Tipo(tipoDato.BOOLEANO), linea, col);
        this.cond1 = cond1;
        this.cond2 = cond2;
        this.relacional = relacional;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        var condIzq = this.cond1.interpretar(arbol, tabla);
        if (condIzq instanceof Errores) {
            return condIzq;
        }

        var condDer = this.cond2.interpretar(arbol, tabla);
        if (condDer instanceof Errores) {
            return condDer;
        }

        return switch (relacional) {
            case EQUALS ->
                this.equals(condIzq, condDer);
            case DIFERENTE ->
                this.diferente(condIzq, condDer);
            case MENORQ ->
                this.menor(condIzq, condDer);
            case MENORIGUAL ->
                this.menorIgual(condIzq, condDer);
            case MAYORQ -> 
                this.mayor(condIzq, condDer);
            case MAYORIGUAL  -> 
                this.mayorIgual(condIzq, condDer);
            default ->
                new Errores("SEMANTICO", "Relacional Invalido", this.linea, this.col);
        };
    }

    public Object equals(Object comp1, Object comp2) {
        var comparando1 = this.cond1.tipo.getTipo();
        var comparando2 = this.cond2.tipo.getTipo();

        return switch (comparando1) {
            case ENTERO ->
                switch (comparando2) {
                    case ENTERO ->
                        (int) comp1 == (int) comp2;
                    case DECIMAL ->
                        (int) comp1 == (double) comp2;
                    default ->
                        new Errores("SEMANTICO", "Relacional Invalido", this.linea, this.col);
                };
            case DECIMAL ->
                switch (comparando2) {
                    case ENTERO ->
                        (double) comp1 == (int) comp2;
                    case DECIMAL ->
                        (double) comp1 == (double) comp2;
                    default ->
                        new Errores("SEMANTICO", "Relacional Invalido", this.linea, this.col);
                };
            case CADENA ->
                switch (comparando2) {
                    case CADENA ->
                        comp1.toString().equalsIgnoreCase(comp2.toString());
                    default ->
                        new Errores("SEMANTICO", "Relacional Invalido", this.linea, this.col);
                };
            case BOOLEANO ->
                switch (comparando2) {
                    case BOOLEANO ->
                        (boolean) comp1 == (boolean) comp2;
                    default ->
                        new Errores("SEMANTICO", "Relacional Invalido", this.linea, this.col);
                };
            default ->
                new Errores("SEMANTICO", "Relacional Invalido", this.linea, this.col);
        };
    }

    
    public Object diferente(Object comp1, Object comp2) {
        Object eq = this.equals(comp1, comp2);
        if (eq instanceof Errores) {
            return eq;
        }
        return !((boolean) eq);
    }
    

    public Object menor(Object comp1, Object comp2) {
        var comparando1 = this.cond1.tipo.getTipo();
        var comparando2 = this.cond2.tipo.getTipo();

        return switch (comparando1) {
            case ENTERO ->
                switch (comparando2) {
                    case ENTERO   -> (int) comp1 < (int) comp2;
                    case DECIMAL  -> (int) comp1 < (double) comp2;
                    case CARACTER -> (int) comp1 < (int) ((Character) comp2);
                    default       -> new Errores("SEMANTICO", "Relacional invaldo",
                                                 this.linea, this.col);
                };
            case DECIMAL ->
                switch (comparando2) {
                    case ENTERO   -> (double) comp1 < (int) comp2;
                    case DECIMAL  -> (double) comp1 < (double) comp2;
                    case CARACTER -> (double) comp1 < (int) ((Character) comp2);
                    default       -> new Errores("SEMANTICO", "Relacional invaldo",
                                                 this.linea, this.col);
                };
            case CARACTER ->
                switch (comparando2) {
                    case ENTERO   -> (int) ((Character) comp1) < (int) comp2;
                    case DECIMAL  -> (int) ((Character) comp1) < (double) comp2;
                    case CARACTER -> (int) ((Character) comp1) < (int) ((Character) comp2);
                    default       -> new Errores("SEMANTICO", "Relacional invaldo",
                                                 this.linea, this.col);
                };
            default ->
                new Errores("SEMANTICO", "Relacional invaldo", this.linea, this.col);
        };
    }

    
    public Object mayor(Object comp1, Object comp2) {
        var comparando1 = this.cond1.tipo.getTipo();
        var comparando2 = this.cond2.tipo.getTipo();

        return switch (comparando1) {
            case ENTERO ->
                switch (comparando2) {
                    case ENTERO   -> (int) comp1 > (int) comp2;
                    case DECIMAL  -> (int) comp1 > (double) comp2;
                    case CARACTER -> (int) comp1 > (int) ((Character) comp2);
                    default       -> new Errores("SEMANTICO", "Relacional invalido", this.linea, this.col);
                };
            case DECIMAL ->
                switch (comparando2) {
                    case ENTERO   -> (double) comp1 > (int) comp2;
                    case DECIMAL  -> (double) comp1 > (double) comp2;
                    case CARACTER -> (double) comp1 > (int) ((Character) comp2);
                    default       -> new Errores("SEMANTICO", "Relacional invalido", this.linea, this.col);
                };
            case CARACTER ->
                switch (comparando2) {
                    case ENTERO   -> (int) ((Character) comp1) > (int) comp2;
                    case DECIMAL  -> (int) ((Character) comp1) > (double) comp2;
                    case CARACTER -> (int) ((Character) comp1) > (int) ((Character) comp2);
                    default       -> new Errores("SEMANTICO", "Relacional invalido", this.linea, this.col);
                };
            default ->
                new Errores("SEMANTICO", "Relacional invalido", this.linea, this.col);
        };
    }  

    
    public Object menorIgual(Object comp1, Object comp2) {
        var comparando1 = this.cond1.tipo.getTipo();
        var comparando2 = this.cond2.tipo.getTipo();

        return switch (comparando1) {
            case ENTERO ->
                switch (comparando2) {
                    case ENTERO   -> (int) comp1 <= (int) comp2;
                    case DECIMAL  -> (int) comp1 <= (double) comp2;
                    case CARACTER -> (int) comp1 <= (int) ((Character) comp2);
                    default       -> new Errores("SEMANTICO", "Relacional invalido", this.linea, this.col);
                };
            case DECIMAL ->
                switch (comparando2) {
                    case ENTERO   -> (double) comp1 <= (int) comp2;
                    case DECIMAL  -> (double) comp1 <= (double) comp2;
                    case CARACTER -> (double) comp1 <= (int) ((Character) comp2);
                    default       -> new Errores("SEMANTICO", "Relacional invalido", this.linea, this.col);
                };
            case CARACTER ->
                switch (comparando2) {
                    case ENTERO   -> (int) ((Character) comp1) <= (int) comp2;
                    case DECIMAL  -> (int) ((Character) comp1) <= (double) comp2;
                    case CARACTER -> (int) ((Character) comp1) <= (int) ((Character) comp2);
                    default       -> new Errores("SEMANTICO", "Relacional invalido", this.linea, this.col);
                };
            default ->
                new Errores("SEMANTICO", "Relacional invalido", this.linea, this.col);
        };
    }

    public Object mayorIgual(Object comp1, Object comp2) {
        var comparando1 = this.cond1.tipo.getTipo();
        var comparando2 = this.cond2.tipo.getTipo();

        return switch (comparando1) {
            case ENTERO ->
                switch (comparando2) {
                    case ENTERO   -> (int) comp1 >= (int) comp2;
                    case DECIMAL  -> (int) comp1 >= (double) comp2;
                    case CARACTER -> (int) comp1 >= (int) ((Character) comp2);
                    default       -> new Errores("SEMANTICO", "Relacional invalido", this.linea, this.col);
                };
            case DECIMAL ->
                switch (comparando2) {
                    case ENTERO   -> (double) comp1 >= (int) comp2;
                    case DECIMAL  -> (double) comp1 >= (double) comp2;
                    case CARACTER -> (double) comp1 >= (int) ((Character) comp2);
                    default       -> new Errores("SEMANTICO", "Relacional invalido", this.linea, this.col);
                };
            case CARACTER ->
                switch (comparando2) {
                    case ENTERO   -> (int) ((Character) comp1) >= (int) comp2;
                    case DECIMAL  -> (int) ((Character) comp1) >= (double) comp2;
                    case CARACTER -> (int) ((Character) comp1) >= (int) ((Character) comp2);
                    default       -> new Errores("SEMANTICO", "Relacional invalido", this.linea, this.col);
                };
            default ->
                new Errores("SEMANTICO", "Relacional invalido", this.linea, this.col);
        };
    }
    
    
}
