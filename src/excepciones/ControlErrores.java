package excepciones;

import java.io.FileWriter;
import java.util.LinkedList;

public class ControlErrores {

    public static LinkedList<Errores> listaErrores = new LinkedList<>();

    public static void agregarError(Errores err) {
        if (err != null) {
            listaErrores.add(err);
        }
    }

    public static void limpiar() {
        listaErrores.clear();
    }

    public static LinkedList<Errores> getListaErrores() {
        return listaErrores;
    }

    // ====== REPORTE HTML DE ERRORES ======
    public static void generarReporteHTML() {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"es\">\n");
        html.append("<head>\n");
        html.append("  <meta charset=\"UTF-8\">\n");
        html.append("  <title>Reporte de Errores</title>\n");
        html.append("  <style>\n");
        html.append("    table { border-collapse: collapse; width: 100%; }\n");
        html.append("    th, td { border: 1px solid #555; padding: 4px 8px; }\n");
        html.append("    th { background-color: #f8d7da; }\n");
        html.append("  </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("  <h2>Reporte de Errores</h2>\n");

        if (listaErrores.isEmpty()) {
            html.append("  <p>No se encontraron errores.</p>\n");
        } else {
            html.append("  <table>\n");
            html.append("    <tr>\n");
            html.append("      <th>#</th>\n");
            html.append("      <th>Tipo</th>\n");
            html.append("      <th>Descripción</th>\n");
            html.append("      <th>Línea</th>\n");
            html.append("      <th>Columna</th>\n");
            html.append("    </tr>\n");

            int i = 1;
            for (Errores e : listaErrores) {
                html.append("    <tr>\n");
                html.append("      <td>").append(i++).append("</td>\n");
                html.append("      <td>").append(e.getTipo()).append("</td>\n");
                html.append("      <td>").append(escapeHtml(e.getDesc())).append("</td>\n");
                html.append("      <td>").append(e.getLinea()).append("</td>\n");
                html.append("      <td>").append(e.getColumna()).append("</td>\n");
                html.append("    </tr>\n");
            }

            html.append("  </table>\n");
        }

        html.append("</body>\n");
        html.append("</html>\n");

        try (FileWriter fw = new FileWriter("reporte_errores.html")) {
            fw.write(html.toString());
            System.out.println("✅ Reporte de errores generado: reporte_errores.html");
        } catch (Exception ex) {
            System.out.println("❌ Error al generar reporte de errores: " + ex.getMessage());
        }
    }
    
    private static String escapeHtml(String txt) {
        if (txt == null) return "";
        return txt
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;")
                .replace("\n", "<br>")
                .replace("\r", "");
    }

}
