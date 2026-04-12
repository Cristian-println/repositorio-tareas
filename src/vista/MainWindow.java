package vista;

import modelo.Estudiante;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private static final Color SIDEBAR_BG  = new Color(15, 23, 42);
    private static final Color SIDEBAR_SEL = new Color(37, 99, 235);
    private static final Color SIDEBAR_HOV = new Color(30, 41, 59);
    private static final Color TEXT_LIGHT  = new Color(226, 232, 240);
    private static final Color TEXT_MID    = new Color(148, 163, 184);

    private JPanel panelContenido;
    private CardLayout cardLayout;
    private Estudiante estudiante;

    private CrearTareaPanel     crearPanel;
    private ListaTareasPanel    listaPanel;
    private SubirTareaPanel     subirPanel;
    private CalificacionesPanel califPanel;

    public MainWindow(Estudiante estudiante) {
        this.estudiante = estudiante;

        setTitle("SistemaTareas – Vista");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 680);
        setMinimumSize(new Dimension(800, 550));
        setLocationRelativeTo(null);

        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        logoPanel.setBackground(SIDEBAR_BG);
        logoPanel.setBorder(new EmptyBorder(24, 0, 24, 0));
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        logoPanel.add(makeLabel("📚", 24, Color.WHITE));
        logoPanel.add(makeLabel("SistemaTareas", 17, Color.WHITE));
        sidebar.add(logoPanel);

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        infoPanel.setBackground(new Color(30, 41, 59));
        infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        infoPanel.add(makeLabel("👤", 22, Color.WHITE));
        JPanel infoText = new JPanel();
        infoText.setLayout(new BoxLayout(infoText, BoxLayout.Y_AXIS));
        infoText.setBackground(new Color(30, 41, 59));
        JLabel nomLbl = new JLabel(estudiante.getNombre() + " " + estudiante.getApellido());
        nomLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nomLbl.setForeground(TEXT_LIGHT);
        infoText.add(nomLbl);
        infoPanel.add(infoText);
        sidebar.add(infoPanel);
        sidebar.add(Box.createVerticalStrut(20));

        sidebar.add(buildSectionLabel("GESTIÓN"));
        JButton btnCrear  = buildNavBtn("✏️", "Crear Tarea",    "crear");
        JButton btnLista  = buildNavBtn("📋", "Lista de Tareas", "lista");
        JButton btnSubir  = buildNavBtn("📤", "Subir Tarea",     "subir");
        JButton btnCalif  = buildNavBtn("🎓", "Calificaciones",  "calificaciones");

        sidebar.add(btnCrear);
        sidebar.add(btnLista);
        sidebar.add(btnSubir);
        sidebar.add(btnCalif);
        sidebar.add(Box.createVerticalGlue());

        JLabel ver = new JLabel("Módulo GUI – Daner");
        ver.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        ver.setForeground(TEXT_MID);
        ver.setBorder(new EmptyBorder(0, 16, 14, 0));
        ver.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(ver);

        add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        crearPanel  = new CrearTareaPanel(estudiante);
        listaPanel  = new ListaTareasPanel(estudiante);
        subirPanel  = new SubirTareaPanel(estudiante);
        califPanel  = new CalificacionesPanel(estudiante);

        panelContenido.add(crearPanel,  "crear");
        panelContenido.add(listaPanel,  "lista");
        panelContenido.add(subirPanel,  "subir");
        panelContenido.add(califPanel,  "calificaciones");

        add(panelContenido, BorderLayout.CENTER);

        cardLayout.show(panelContenido, "crear");
        btnCrear.setBackground(SIDEBAR_SEL);
    }

    private JButton buildNavBtn(String icon, String label, String card) {
        JButton btn = new JButton(icon + "  " + label);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(TEXT_LIGHT);
        btn.setBackground(SIDEBAR_BG);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(11, 18, 11, 18));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!btn.getBackground().equals(SIDEBAR_SEL)) btn.setBackground(SIDEBAR_HOV);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!btn.getBackground().equals(SIDEBAR_SEL)) btn.setBackground(SIDEBAR_BG);
            }
        });
        btn.addActionListener(e -> {
            for (Component c : btn.getParent().getComponents())
                if (c instanceof JButton) c.setBackground(SIDEBAR_BG);
            btn.setBackground(SIDEBAR_SEL);
            if ("lista".equals(card)) listaPanel.refrescarLista();
            cardLayout.show(panelContenido, card);
        });
        return btn;
    }

    private JLabel buildSectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(TEXT_MID);
        lbl.setBorder(new EmptyBorder(4, 18, 6, 18));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        return lbl;
    }

    private JLabel makeLabel(String text, int size, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, size));
        lbl.setForeground(color);
        return lbl;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            Estudiante est = new Estudiante("Daner", "Apellido");
            new MainWindow(est);
        });
    }
}
