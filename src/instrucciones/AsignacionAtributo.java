package instrucciones;

import abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.Instancia;
import Simbolo.Simbolo;
import Simbolo.Tipo;
import Simbolo.TipoUtils;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import excepciones.Errores;

public class AsignacionAtributo extends Instruccion {

    private final Instruccion objeto;
    private final String atributo;
    private final Instruccion valor;

    public AsignacionAtributo(Instruccion objeto, String atributo, Instruccion valor, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.objeto = objeto;
        this.atributo = atributo;
        this.valor = valor;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {

        // 1) Evaluar objeto
        Object objVal = objeto.interpretar(arbol, tabla);
        if (objVal instanceof Errores) return objVal;

        if (!(objVal instanceof Instancia inst)) {
            return new Errores(
                    "SEMANTICO",
                    "Se esperaba un objeto/instancia para asignar al atributo '." + atributo + "'",
                    this.linea,
                    this.col
            );
        }

        // 2) Verificar que el atributo exista
        Simbolo atr = inst.getTablaAtributos().getVariable(atributo);
        if (atr == null) {
            return new Errores(
                    "SEMANTICO",
                    "El objeto de clase '" + inst.getClase().getNombre() + "' no tiene el atributo '" + atributo + "'",
                    this.linea,
                    this.col
            );
        }

        // 3) Evaluar valor
        Object nuevoValor = valor.interpretar(arbol, tabla);
        if (nuevoValor instanceof Errores) return nuevoValor;

        Tipo tipoEsperado = atr.getTipo();
        Tipo tipoObtenido = valor.tipo;

        if (!TipoUtils.compatibles(tipoEsperado, tipoObtenido)) {
            return new Errores(
                    "SEMANTICO",
                    "Tipo inválido en asignación a '." + atributo + "'. Esperado=" + tipoEsperado.getTipo()
                            + " y viene=" + (tipoObtenido != null ? tipoObtenido.getTipo() : "null"),
                    this.linea,
                    this.col
            );
        }

        // 4) Actualizar en la tabla del objeto (busca y actualiza en cadena)
        boolean ok = inst.getTablaAtributos().actualizarVariable(atributo, nuevoValor, tipoEsperado);
        if (!ok) {
            return new Errores(
                    "SEMANTICO",
                    "No se pudo actualizar el atributo '." + atributo + "' (no encontrado en la instancia).",
                    this.linea,
                    this.col
            );
        }

        return null;
    }
}
