package analisis;

public class Token {
    private String tipo;
    private String lexema;
    private int fila;
    private int columna;

    public Token(String tipo, String lexema, int fila, int columna) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.fila = fila;
        this.columna = columna;
    }

    public String getTipo() {
        return tipo;
    }

    public String getLexema() {
        return lexema;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public String toString() {
        return "Token{tipo=" + tipo +
               ", lexema='" + lexema + '\'' +
               ", fila=" + fila +
               ", columna=" + columna +
               '}';
    }
}
