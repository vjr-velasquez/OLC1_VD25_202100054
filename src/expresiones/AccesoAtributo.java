package expresiones;

import abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.Instancia;
import Simbolo.Simbolo;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import excepciones.Errores;

public class AccesoAtributo extends Instruccion {

    private final Instruccion objeto;
    private final String atributo;

    public AccesoAtributo(Instruccion objeto, String atributo, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.objeto = objeto;
        this.atributo = atributo;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {

        // 1) Evaluar objeto
        Object objVal = objeto.interpretar(arbol, tabla);
        if (objVal instanceof Errores) return objVal;

        if (!(objVal instanceof Instancia inst)) {
            return new Errores(
                    "SEMANTICO",
                    "Se esperaba un objeto/instancia para acceder al atributo '." + atributo + "'",
                    this.linea,
                    this.col
            );
        }

        // 2) Buscar atributo en la tabla de atributos del objeto
        Simbolo s = inst.getTablaAtributos().getVariable(atributo);
        if (s == null) {
            return new Errores(
                    "SEMANTICO",
                    "El objeto de clase '" + inst.getClase().getNombre() + "' no tiene el atributo '" + atributo + "'",
                    this.linea,
                    this.col
            );
        }

        // 3) Propagar tipo + retornar valor
        this.tipo = s.getTipo();
        return s.getValor();
    }
}
