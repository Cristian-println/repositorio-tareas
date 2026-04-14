package main;

import bd.Conexion;
import vista.VentanaPrincipal;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        try {
           
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo aplicar el LookAndFeel");
        }

        UIManager.put("TabbedPane.selected", new java.awt.Color(232, 240, 254));
        UIManager.put("TabbedPane.focus", new java.awt.Color(21, 101, 192));
        UIManager.put("Button.arc", 8);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.thumbInsets",
                new javax.swing.plaf.InsetsUIResource(2, 2, 2, 2));

        System.out.println("Iniciando sistema...");

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

            System.out.println("Error: No se pudo conectar a la base de datos.");
            return; 
        }

        SwingUtilities.invokeLater(() -> {
            try {
                VentanaPrincipal ventana = new VentanaPrincipal();
                ventana.setVisible(true);
                System.out.println("Interfaz iniciada correctamente.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error al iniciar la interfaz: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Conexion.cerrarConexion();
            System.out.println("Conexión a BD cerrada.");
        }));
    }
}