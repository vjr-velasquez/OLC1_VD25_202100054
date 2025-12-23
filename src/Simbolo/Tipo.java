package Simbolo;

public class Tipo {
    private tipoDato tipo;
    private int dimensiones; // 0 = normal, 1 = vector (tipo[])

    public Tipo(tipoDato tipo) {
        this.tipo = tipo;
        this.dimensiones = 0;
    }

    public Tipo(tipoDato tipo, int dimensiones) {
        this.tipo = tipo;
        this.dimensiones = Math.max(0, dimensiones);
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
}
