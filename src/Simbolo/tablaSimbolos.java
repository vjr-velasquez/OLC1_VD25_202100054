package Simbolo;

import java.util.HashMap;

public class tablaSimbolos {
    private tablaSimbolos tablaAnterior;
    private HashMap<String, Simbolo> tablaActual;
    private String nombre;

    public tablaSimbolos(){
        this.tablaActual = new HashMap<>();
        this.nombre = "";
    }

    public tablaSimbolos(tablaSimbolos tablaAnterior){
        this.tablaAnterior = tablaAnterior;
        this.tablaActual = new HashMap<>();
        this.nombre = "";
    }

    public tablaSimbolos getTablaAnterior() {
        return tablaAnterior;
    }

    public void setTablaAnterior(tablaSimbolos tablaAnterior) {
        this.tablaAnterior = tablaAnterior;
    }

    public HashMap<String, Simbolo> getTablaActual() {
        return tablaActual;
    }

    public void setTablaActual(HashMap<String, Simbolo> tablaActual) {
        this.tablaActual = tablaActual;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // =========================
    // API ACTUAL (sin romper)
    // =========================

    public boolean setVariables(Simbolo simbolo){
        if (simbolo == null || simbolo.getId() == null) return false;

        Simbolo busqueda = this.tablaActual.get(simbolo.getId().toLowerCase());
        if (busqueda == null){
            this.tablaActual.put(simbolo.getId().toLowerCase(), simbolo);
            return true;
        }
        return false;
    }

    public Simbolo getVariable(String id) {
        if (id == null) return null;

        for (tablaSimbolos i = this; i != null; i = i.getTablaAnterior()) {
            Simbolo busqueda = i.tablaActual.get(id.toLowerCase());
            if (busqueda != null) return busqueda;
        }
        return null;
    }

    // =========================
    // Helpers útiles para funciones
    // =========================

    /** Busca solo en el ámbito actual (sin subir a padres) */
    public Simbolo getVariableEnActual(String id) {
        if (id == null) return null;
        return this.tablaActual.get(id.toLowerCase());
    }

    /** True si existe una variable con ese id solo en el scope actual */
    public boolean existeEnActual(String id) {
        return getVariableEnActual(id) != null;
    }

    /**
     * Actualiza una variable existente buscando desde el scope actual hacia arriba.
     * Retorna true si la encontró y actualizó.
     *
     * OJO: requiere que tu clase Simbolo tenga setValor / setTipo (si no, lo quitamos).
     */
    public boolean actualizarVariable(String id, Object nuevoValor, Tipo nuevoTipo) {
        if (id == null) return false;

        for (tablaSimbolos i = this; i != null; i = i.getTablaAnterior()) {
            Simbolo busqueda = i.tablaActual.get(id.toLowerCase());
            if (busqueda != null) {
                // Ajusta estos setters según tu clase Simbolo real
                busqueda.setValor(nuevoValor);
                if (nuevoTipo != null) busqueda.setTipo(nuevoTipo);
                return true;
            }
        }
        return false;
    }
}
