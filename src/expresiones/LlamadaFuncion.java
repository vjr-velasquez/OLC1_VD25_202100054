package expresiones;

import Simbolo.Arbol;
import excepciones.Errores;
import Simbolo.Tipo;
import Simbolo.TipoUtils;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import Simbolo.NodoAST;
import abstracto.Instruccion;
import instrucciones.Funcion;
import java.util.LinkedList;

public class LlamadaFuncion extends Instruccion {

    private final String nombre;
    private final LinkedList<Instruccion> args;

    public LlamadaFuncion(String nombre, LinkedList<Instruccion> args, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col); // se setea al ejecutar
        this.nombre = nombre;
        this.args = (args != null) ? args : new LinkedList<>();
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        Funcion f = arbol.getFuncion(nombre);
        if (f == null) {
            arbol.addError(new Errores("SEMANTICO", "No existe la función: " + nombre, linea, col));
            return null;
        }

        // evaluar args
        LinkedList<Object> valores = new LinkedList<>();

        if (f.getParametros().size() != args.size()) {
            arbol.addError(new Errores("SEMANTICO",
                    "Cantidad de argumentos inválida en '" + nombre + "'. Esperado=" + f.getParametros().size() + ", recibido=" + args.size(),
                    linea, col));
            return null;
        }

        for (int i = 0; i < args.size(); i++) {
            Instruccion exp = args.get(i);
            Parametro p = f.getParametros().get(i);

            Object val = exp.interpretar(arbol, tabla);
            Tipo tipoObtenido = exp.tipo;
            Tipo tipoEsperado = p.getTipo();

            if (!TipoUtils.compatibles(tipoEsperado, tipoObtenido)) {
                arbol.addError(new Errores("SEMANTICO",
                        "Tipo inválido en argumento " + (i+1) + " de '" + nombre + "'. Esperado=" + tipoEsperado.getTipo() + " y viene=" + (tipoObtenido != null ? tipoObtenido.getTipo() : "null"),
                        linea, col));
                return null;
            }

            valores.add(val);
        }

        // ejecutar función: retorna Object (o null si void)
        Object ret = f.ejecutar(arbol, tabla, valores);

        // setear tipo para que pueda usarse como expresión
        this.tipo = f.getTipoRetorno();

        return ret;
    }

    @Override
    public NodoAST getNodoAST() {
        NodoAST n = new NodoAST("LLAMADA_FUNCION");

        n.agregarHijo(new NodoAST("NOMBRE: " + nombre));

        NodoAST a = new NodoAST("ARGS");
        for (Instruccion e : args) {
            if (e != null) a.agregarHijo(e.getNodoAST());
        }
        n.agregarHijo(a);

        return n;
    }

}
