package Simbolo;

public class ReturnValue {
    private final Object valor;
    private final Tipo tipo;

    public ReturnValue(Object valor, Tipo tipo) {
        this.valor = valor;
        this.tipo = tipo;
    }

    public Object getValor() {
        return valor;
    }

    public Tipo getTipo() {
        return tipo;
    }
}
