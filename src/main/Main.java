package main;

import bd.Conexion;
import vista.VentanaPrincipal;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        UIManager.put("TabbedPane.selected",         new java.awt.Color(232, 240, 254));
        UIManager.put("TabbedPane.focus",            new java.awt.Color(21, 101, 192));
        UIManager.put("Button.arc",                  8);
        UIManager.put("ScrollBar.thumbArc",          999);
        UIManager.put("ScrollBar.thumbInsets",
            new javax.swing.plaf.InsetsUIResource(2, 2, 2, 2));

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
         
        }

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(Conexion::cerrarConexion));
    }
}
