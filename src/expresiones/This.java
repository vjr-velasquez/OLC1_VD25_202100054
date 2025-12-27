package expresiones;

import abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.Instancia;
import Simbolo.Simbolo;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import excepciones.Errores;

public class This extends Instruccion {

    public This(int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {

        Simbolo s = tabla.getVariable("this");
        if (s == null) {
            // Esto pasa si llaman THIS fuera de un método o si no se inyectó "this"
            return new Errores(
                    "SEMANTICO",
                    "No existe 'this' en el contexto actual (solo es válido dentro de métodos).",
                    this.linea,
                    this.col
            );
        }

        Object val = s.getValor();
        if (!(val instanceof Instancia)) {
            return new Errores(
                    "SEMANTICO",
                    "El valor de 'this' no es una instancia válida.",
                    this.linea,
                    this.col
            );
        }

        // Si luego creas un tipo de clase real, aquí puedes setearlo.
        this.tipo = new Tipo(tipoDato.VOID);
        return val;
    }
}
