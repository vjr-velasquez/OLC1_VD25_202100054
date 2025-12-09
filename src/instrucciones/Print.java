
package instrucciones;
import abstracto.Instruccion;
import Simbolo.*;


public class Print extends Instruccion {
    private Instruccion expresion;

    public Print(Instruccion expresion, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.expresion = expresion;
    }
    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla){
        var resultado = this.expresion.interpretar(arbol, tabla);
        arbol.Print(resultado.toString());
        return null;
    }
}
