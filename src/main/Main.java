package main;

import bd.Conexion;
import vista.VentanaPrincipal;

import javax.swing.*;

/**
 * Punto de entrada del sistema Classroom.
 * Responsable: Integración del equipo (T47)
 */
public class Main {

    public static void main(String[] args) {

        // ── Look & Feel del sistema operativo ──
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // ── Personalización global de Swing ──
        UIManager.put("TabbedPane.selected",         new java.awt.Color(232, 240, 254));
        UIManager.put("TabbedPane.focus",            new java.awt.Color(21, 101, 192));
        UIManager.put("Button.arc",                  8);
        UIManager.put("ScrollBar.thumbArc",          999);
        UIManager.put("ScrollBar.thumbInsets",
            new javax.swing.plaf.InsetsUIResource(2, 2, 2, 2));

        // ── Verificar conexión a la BD antes de abrir la ventana ──
        if (!Conexion.probarConexion()) {
            JOptionPane.showMessageDialog(null,
                "   No se pudo conectar a la base de datos.\n\n" +
                "Asegúrese de que:\n" +
                "  • MySQL esté en ejecución\n" +
                "  • La base de datos 'classroom_db' exista\n" +
                "  • El usuario y contraseña en bd/Conexion.java sean correctos\n\n" +
                "Ejecute el script 'database.sql' para crear la base de datos.",
                "Error de conexión",
                JOptionPane.ERROR_MESSAGE);
            // Continuar de todas formas para que se pueda ver la UI
        }

        // ── Lanzar interfaz gráfica en el hilo de eventos de Swing ──
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });

        // ── Cerrar conexión al salir ──
        Runtime.getRuntime().addShutdownHook(new Thread(Conexion::cerrarConexion));
    }
}
