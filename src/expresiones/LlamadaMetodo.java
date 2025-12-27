package expresiones;

import abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.Instancia;
import Simbolo.Tipo;
import Simbolo.TipoUtils;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import excepciones.Errores;
import instrucciones.Funcion;
import java.util.LinkedList;

public class LlamadaMetodo extends Instruccion {

    private final Instruccion objeto;
    private final String metodo;
    private final LinkedList<Instruccion> args;

    public LlamadaMetodo(Instruccion objeto, String metodo, LinkedList<Instruccion> args, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.objeto = objeto;
        this.metodo = metodo;
        this.args = (args == null) ? new LinkedList<>() : args;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {

        // 1) Evaluar objeto
        Object objVal = objeto.interpretar(arbol, tabla);
        if (objVal instanceof Errores) return objVal;

        if (!(objVal instanceof Instancia inst)) {
            return new Errores(
                    "SEMANTICO",
                    "Se esperaba un objeto/instancia para llamar ." + metodo + "(), pero viene: "
                            + (objVal == null ? "null" : objVal.getClass().getSimpleName()),
                    this.linea,
                    this.col
            );
        }

        // 2) Buscar método en la clase
        Funcion f = inst.getClase().getMetodo(metodo);
        if (f == null) {
            return new Errores(
                    "SEMANTICO",
                    "La clase '" + inst.getClase().getNombre() + "' no tiene el método '" + metodo + "'.",
                    this.linea,
                    this.col
            );
        }

        // 3) Validar cantidad de args
        int nParams = (f.getParametros() != null) ? f.getParametros().size() : 0;
        int nArgs = (args != null) ? args.size() : 0;

        if (nParams != nArgs) {
            return new Errores(
                    "SEMANTICO",
                    "Cantidad de argumentos inválida en '" + metodo + "'. Esperado=" + nParams + ", recibido=" + nArgs,
                    this.linea,
                    this.col
            );
        }

        // 4) Evaluar args + validar tipos
        LinkedList<Object> valores = new LinkedList<>();

        for (int i = 0; i < nArgs; i++) {
            Instruccion exp = args.get(i);
            Parametro p = f.getParametros().get(i);

            Object val = exp.interpretar(arbol, tabla);
            if (val instanceof Errores) return val;

            Tipo tipoObtenido = exp.tipo;
            Tipo tipoEsperado = p.getTipo();

            if (!TipoUtils.compatibles(tipoEsperado, tipoObtenido)) {
                return new Errores(
                        "SEMANTICO",
                        "Tipo inválido en argumento " + (i + 1) + " de '" + metodo + "'. Esperado="
                                + tipoEsperado.getTipo()
                                + " y viene=" + (tipoObtenido != null ? tipoObtenido.getTipo() : "null"),
                        this.linea,
                        this.col
                );
            }

            valores.add(val);
        }

        // 5) Ejecutar método con this
        Object ret = f.ejecutar(arbol, inst.getTablaAtributos(), valores, inst);

        // 6) Guardar tipo de retorno para uso como expresión
        this.tipo = f.getTipoRetorno();

        return ret;
    }
}
