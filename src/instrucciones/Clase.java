package instrucciones;

import abstracto.Instruccion;
import Simbolo.Arbol;
import Simbolo.Tipo;
import Simbolo.tablaSimbolos;
import Simbolo.tipoDato;
import excepciones.Errores;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Clase extends Instruccion {

    private final String nombre;
    private final LinkedList<Instruccion> miembros;

    // FASE 1: separación, sin ejecutar
    private LinkedList<Declaracion> atributos;
    private HashMap<String, Funcion> metodos;

    public Clase(String nombre, LinkedList<Instruccion> miembros, int linea, int col) {
        super(new Tipo(tipoDato.VOID), linea, col);
        this.nombre = nombre;
        this.miembros = (miembros == null) ? new LinkedList<>() : miembros;

        this.atributos = new LinkedList<>();
        this.metodos = new HashMap<>();
    }

    public String getNombre() {
        return nombre;
    }

    public LinkedList<Instruccion> getMiembros() {
        return miembros;
    }

    public LinkedList<Declaracion> getAtributos() {
        return atributos;
    }

    public Map<String, Funcion> getMetodos() {
        return metodos;
    }

    public Funcion getMetodo(String id) {
        return (metodos != null) ? metodos.get(id) : null;
    }

    @Override
    public Object interpretar(Arbol arbol, tablaSimbolos tabla) {
        // 1) Registrar definición de clase
        boolean ok = arbol.registrarClase(nombre, this);
        if (!ok) {
            // Arbol ya metió el error; aquí solo cortamos
            return null;
        }

        // 2) Separar miembros (SIN ejecutar)
        this.atributos = new LinkedList<>();
        this.metodos = new HashMap<>();

        for (Instruccion inst : this.miembros) {
            if (inst == null) continue;

            // MÉTODO
            if (inst instanceof Funcion fun) {
                String id = fun.getNombre();

                if (this.metodos.containsKey(id)) {
                    Errores err = new Errores(
                            "SEMANTICO",
                            "Método duplicado '" + id + "' en clase '" + nombre + "'",
                            fun.getLinea(), fun.getCol()
                    );
                    arbol.addError(err);
                    continue;
                }

                this.metodos.put(id, fun);
                continue;
            }

            // ATRIBUTO
            if (inst instanceof Declaracion dec) {
                this.atributos.add(dec);
                continue;
            }

            // MIEMBRO INVALIDO
            Errores err = new Errores(
                    "SEMANTICO",
                    "Miembro inválido en clase '" + nombre + "': " + inst.getClass().getSimpleName(),
                    inst.linea, inst.col
            );
            arbol.addError(err);
        }

        // 3) No ejecutar nada todavía
        return null;
    }
}
