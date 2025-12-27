package instrucciones;

import Simbolo.Arbol;
import Simbolo.Instancia;
import Simbolo.ReturnValue;
import Simbolo.Simbolo;
import Simbolo.Tipo;
import Simbolo.TipoUtils;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import Simbolo.NodoAST;
import abstracto.Instruccion;
import excepciones.Errores;
import expresiones.Parametro;
import java.util.LinkedList;

public class Funcion extends Instruccion {

    private final Tipo tipoRetorno;
    private final String nombre;
    private final LinkedList<Parametro> parametros;
    private final LinkedList<Instruccion> cuerpo;

    public Funcion(Tipo tipoRetorno, String nombre, LinkedList<?> paramsHack, LinkedList<Instruccion> cuerpo, int linea, int col) {
        super(tipoRetorno, linea, col);
        this.tipoRetorno = tipoRetorno;
        this.nombre = nombre;

        // Permitimos que CUP te mande LinkedList<Parametro> o LinkedList<Instruccion> si aún lo tienes así
        this.parametros = new LinkedList<>();
        if (paramsHack != null) {
            for (Object o : paramsHack) {
                if (o instanceof Parametro p) this.parametros.add(p);
                // si lo mandaste como Instruccion, no debería pasar; pero evitamos crash
            }
        }

        this.cuerpo = (cuerpo != null) ? cuerpo : new LinkedList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public Tipo getTipoRetorno() {
        return tipoRetorno;
    }

    public LinkedList<Parametro> getParametros() {
        return parametros;
    }

    public int getLinea() {
        return linea;
    }

    public int getCol() {
        return col;
    }

    /**
     * Al interpretar una Funcion (como instrucción del "top level") NO se ejecuta,
     * solo se registra en el Arbol.
     */
    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        arbol.registrarFuncion(this);
        return null;
    }

    /**
     * Wrapper para funciones globales (sin this).
     * Mantiene compatibilidad con LlamadaFuncion y Arbol.ejecutarStart().
     */
    public Object ejecutar(Arbol arbol, tablaSimbolos tablaBase, LinkedList<Object> args) {
        return ejecutar(arbol, tablaBase, args, null);
    }

    /**
     * Ejecuta la función / método.
     * - tablaBase: scope base (global para funciones, tabla de atributos para métodos)
     * - thisRef: null si es función global; Instancia si es método llamado con objeto
     */
    public Object ejecutar(Arbol arbol, tablaSimbolos tablaBase, LinkedList<Object> args, Instancia thisRef) {

        int nParams = (parametros != null) ? parametros.size() : 0;
        int nArgs = (args != null) ? args.size() : 0;

        if (nParams != nArgs) {
            arbol.addError(new Errores("SEMANTICO",
                    "Cantidad de argumentos inválida en llamada a '" + nombre + "'. Esperado=" + nParams + ", recibido=" + nArgs,
                    linea, col));
            return null;
        }

        // scope local hijo de la base (base puede ser tablaGlobal o tablaAtributos del objeto)
        tablaSimbolos local = new tablaSimbolos(tablaBase);

        // ✅ Inyectar this en el scope local (NO en la tabla base)
        if (thisRef != null) {
            // Por ahora VOID es suficiente; si luego creas tipoClase, lo cambias
            Simbolo symThis = new Simbolo(new Tipo(tipoDato.VOID), "this", thisRef);
            local.setVariables(symThis); // aquí no debería chocar porque el local es nuevo
        }

        // bind params
        for (int i = 0; i < nParams; i++) {
            Parametro p = parametros.get(i);
            Object val = args.get(i);

            Simbolo sym = new Simbolo(p.getTipo(), p.getId(), val);

            boolean ok = local.setVariables(sym);
            if (!ok) {
                arbol.addError(new Errores("SEMANTICO",
                        "Parámetro duplicado en función '" + nombre + "': " + p.getId(),
                        p.getLinea(), p.getCol()));
                return null;
            }
        }

        // ejecutar cuerpo
        for (Instruccion ins : cuerpo) {
            if (ins == null) continue;

            Object res = ins.interpretar(arbol, local);

            // Propagación de return
            if (res instanceof ReturnValue rv) {
                return validarRetorno(arbol, rv);
            }
        }

        // Si no hubo return:
        if (tipoRetorno != null && tipoRetorno.getTipo() != tipoDato.VOID) {
            arbol.addError(new Errores("SEMANTICO",
                    "La función '" + nombre + "' debe retornar un valor de tipo " + tipoRetorno.getTipo(),
                    linea, col));
        }

        return null;
    }

    private Object validarRetorno(Arbol arbol, ReturnValue rv) {
        // retorno void
        if (tipoRetorno.getTipo() == tipoDato.VOID) {
            // return; permitido
            if (rv.getTipo() == null || rv.getTipo().getTipo() == tipoDato.VOID) return null;

            // return expr; en void => error
            arbol.addError(new Errores("SEMANTICO",
                    "La función '" + nombre + "' es void y no debe retornar un valor.",
                    linea, col));
            return null;
        }

        // retorno con tipo: debe haber valor y tipo compatible
        if (rv.getTipo() == null || rv.getTipo().getTipo() == tipoDato.VOID) {
            arbol.addError(new Errores("SEMANTICO",
                    "La función '" + nombre + "' debe retornar un valor de tipo " + tipoRetorno.getTipo(),
                    linea, col));
            return null;
        }

        if (!TipoUtils.compatibles(tipoRetorno, rv.getTipo())) {
            arbol.addError(new Errores("SEMANTICO",
                    "Tipo de retorno incorrecto en '" + nombre + "'. Esperado=" + tipoRetorno.getTipo() + " y viene=" + rv.getTipo().getTipo(),
                    linea, col));
            return null;
        }

        return rv.getValor();
    }

    @Override
    public NodoAST getNodoAST() {
        NodoAST n = new NodoAST("FUNCION");

        n.agregarHijo(new NodoAST("NOMBRE: " + nombre));
        n.agregarHijo(new NodoAST("RETORNO: " + (tipoRetorno != null ? tipoRetorno.getTipo().toString() : "null")));

        NodoAST ps = new NodoAST("PARAMS");
        for (Parametro p : parametros) {
            ps.agregarHijo(p.getNodoAST());
        }
        n.agregarHijo(ps);

        NodoAST b = new NodoAST("CUERPO");
        for (Instruccion ins : cuerpo) {
            if (ins != null) b.agregarHijo(ins.getNodoAST());
        }
        n.agregarHijo(b);

        return n;
    }
}
