package vista;

import javax.swing.*;
import java.awt.*;
 
public class MainFrame extends JFrame {
 
    private JTabbedPane tabbedPane;
 
    public MainFrame() {
        setTitle("Plataforma Académica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
 
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(new Color(0, 120, 120));
        header.setPreferredSize(new Dimension(900, 50));
        JLabel titulo = new JLabel("  Plataforma Académica");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        header.add(titulo);
        add(header, BorderLayout.NORTH);
 
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 13));
 
        tabbedPane.addTab("US1 - Crear Tarea",     new PanelCrearTarea());
        tabbedPane.addTab("US2 - Entregar Tarea",  new PanelEntregarTarea());
        tabbedPane.addTab("US3 - Calificar",       new PanelCalificarTareas());
        tabbedPane.addTab("US4 - Notas",           new PanelCalificaciones());
        tabbedPane.addTab("US5 - Buscar",          new PanelBuscarEstudiantes());
        tabbedPane.addTab("US6 - Mis Tareas",      new PanelMisTareas());
        tabbedPane.addTab("US7 - Notificaciones",  new PanelNotificaciones());
        tabbedPane.addTab("US8 - Promedio",        new PanelMisNotas());
 
        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
