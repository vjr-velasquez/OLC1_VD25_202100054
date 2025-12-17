package analisis;

public class Token {
    private String tipo;
    private String lexema;
    private int linea;
    private int columna;

    public Token(String tipo, String lexema, int linea, int columna) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
    }

    public String getTipo() {
        return tipo;
    }

    public String getLexema() {
        return lexema;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    // ---------- REPORTE HTML DE TOKENS ----------
    public static void generarReporteHTML(java.util.List<Token> tokens) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"es\">\n");
        html.append("<head>\n");
        html.append("  <meta charset=\"UTF-8\">\n");
        html.append("  <title>Reporte de Tokens</title>\n");
        html.append("  <style>\n");
        html.append("    table { border-collapse: collapse; width: 100%; }\n");
        html.append("    th, td { border: 1px solid #555; padding: 4px 8px; }\n");
        html.append("    th { background-color: #eee; }\n");
        html.append("  </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("  <h2>Reporte de Tokens</h2>\n");
        html.append("  <table>\n");
        html.append("    <tr>\n");
        html.append("      <th>#</th>\n");
        html.append("      <th>Tipo</th>\n");
        html.append("      <th>Lexema</th>\n");
        html.append("      <th>Línea</th>\n");
        html.append("      <th>Columna</th>\n");
        html.append("    </tr>\n");

        int i = 1;
        for (Token t : tokens) {
            html.append("    <tr>\n");
            html.append("      <td>").append(i++).append("</td>\n");
            html.append("      <td>").append(t.getTipo()).append("</td>\n");
            html.append("      <td>").append(escapeHtml(t.getLexema())).append("</td>\n");
            html.append("      <td>").append(t.getLinea()).append("</td>\n");
            html.append("      <td>").append(t.getColumna()).append("</td>\n");
            html.append("    </tr>\n");
        }

        html.append("  </table>\n");
        html.append("</body>\n");
        html.append("</html>\n");

        try {
            java.io.FileWriter fw = new java.io.FileWriter("reporte_tokens.html");
            fw.write(html.toString());
            fw.close();
            System.out.println("✅ Reporte de tokens generado: reporte_tokens.html");
        } catch (Exception e) {
            System.out.println("❌ Error al generar reporte de tokens: " + e.getMessage());
        }
    }

    private static String escapeHtml(String txt) {
        if (txt == null) return "";
        return txt
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
