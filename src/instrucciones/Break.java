
package instrucciones;
import abstracto.Instruccion;
import Simbolo.*;


public class Break extends Instruccion {

    public Break(int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
    }
    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla){
       return null;
    }
    
}
