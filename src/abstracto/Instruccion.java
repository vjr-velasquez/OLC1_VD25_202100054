
package abstracto;
import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;


public abstract class Instruccion {
    public Tipo tipo;
    public int linea;
    public int col;

    public Instruccion(Tipo tipo, int linea, int col) {
        this.tipo = tipo;
        this.linea = linea;
        this.col = col;
    }
    
    public abstract Object interpretar(Arbol arbol, tablaSimbolos tabla);
    
}