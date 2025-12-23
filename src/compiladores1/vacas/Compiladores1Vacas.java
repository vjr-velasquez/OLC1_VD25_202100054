package compiladores1.vacas;

public class Compiladores1Vacas {

    public static void main(String[] args) {
        // Lanzar la ventana principal
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new interfaz().setVisible(true);
            }
        });
    }
}
