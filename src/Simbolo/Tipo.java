package Simbolo;

public class Tipo {
    private tipoDato tipo;
    private int dimensiones; // 0 = normal, 1 = vector (tipo[])

    // NUEVO: subtipo para estructuras genéricas (ej: LISTA<ENTERO>)
    private Tipo subtipo;

    public Tipo(tipoDato tipo) {
        this.tipo = tipo;
        this.dimensiones = 0;
        this.subtipo = null;
    }

    public Tipo(tipoDato tipo, int dimensiones) {
        this.tipo = tipo;
        this.dimensiones = Math.max(0, dimensiones);
        this.subtipo = null;
    }

    // NUEVO: constructor para LISTA<T>
    public Tipo(tipoDato tipo, Tipo subtipo) {
        this.tipo = tipo;
        this.dimensiones = 0;
        this.subtipo = subtipo;
    }

    public tipoDato getTipo() {
        return tipo;
    }

    public void setTipo(tipoDato tipo) {
        this.tipo = tipo;
    }

    public int getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(int dimensiones) {
        this.dimensiones = Math.max(0, dimensiones);
    }

    public boolean esVector() {
        return this.dimensiones > 0;
    }

    // --------- LISTAS ---------

    public boolean esLista() {
        return this.tipo == tipoDato.LISTA;
    }

    public Tipo getSubtipo() {
        return subtipo;
    }

    public void setSubtipo(Tipo subtipo) {
        this.subtipo = subtipo;
    }
}
