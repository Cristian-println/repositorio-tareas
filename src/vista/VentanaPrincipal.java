package vista;

import controlador.EstudianteControlador;
import modelo.Docente;
import modelo.Estudiante;
import modelo.dao.DocenteDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private JComboBox<String>  cmbRol;
    private JComboBox<Object>  cmbUsuario;
    private JPanel             panelCentral;
    private CardLayout         cardLayout;
    private PanelDocente       panelDocente;
    private PanelEstudiante    panelEstudiante;
    private JLabel             lblBienvenidaUsuario;

    private final DocenteDAO             docenteDAO    = new DocenteDAO();
    private final EstudianteControlador  estudianteCtrl= new EstudianteControlador();

    private List<Docente>    docentes;
    private List<Estudiante> estudiantes;

    public VentanaPrincipal() {
        super("Sistema Tareas");
        setIconImage(crearIcono());
        construirUI();
        configurarVentana();
        cargarUsuarios();
    }

    private void construirUI() {
        setLayout(new BorderLayout(0, 0));

        add(crearCabecera(),    BorderLayout.NORTH);
        add(crearContenido(),   BorderLayout.CENTER);
        add(crearBarraEstado(), BorderLayout.SOUTH);
    }

    private JPanel crearCabecera() {
        JPanel cab = new JPanel(new BorderLayout());
        cab.setBackground(Estilos.COLOR_CABECERA);
        cab.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblLogo = new JLabel("   SISTEMA_TAREAS");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblLogo.setForeground(Color.WHITE);
        cab.add(lblLogo, BorderLayout.WEST);

        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        selectorPanel.setOpaque(false);

        lblBienvenidaUsuario = new JLabel();
        lblBienvenidaUsuario.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblBienvenidaUsuario.setForeground(new Color(187, 222, 251));

        JLabel lblRol = new JLabel("Acceder como:");
        lblRol.setFont(Estilos.FUENTE_NEGRITA);
        lblRol.setForeground(Color.WHITE);

        cmbRol = new JComboBox<>(new String[]{"-- Seleccione rol --", "Docente", "Estudiante"});
        cmbRol.setFont(Estilos.FUENTE_NORMAL);
        cmbRol.setPreferredSize(new Dimension(160, 30));

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(Estilos.FUENTE_NEGRITA);
        lblUsuario.setForeground(Color.WHITE);

        cmbUsuario = new JComboBox<>();
        cmbUsuario.setFont(Estilos.FUENTE_NORMAL);
        cmbUsuario.setPreferredSize(new Dimension(210, 30));
        cmbUsuario.setEnabled(false);

        selectorPanel.add(lblBienvenidaUsuario);
        selectorPanel.add(Box.createHorizontalStrut(10));
        selectorPanel.add(lblRol);
        selectorPanel.add(cmbRol);
        selectorPanel.add(lblUsuario);
        selectorPanel.add(cmbUsuario);
        cab.add(selectorPanel, BorderLayout.EAST);

        cmbRol.addActionListener(e -> onRolCambiado());
        cmbUsuario.addActionListener(e -> onUsuarioCambiado());

        return cab;
    }

    private JPanel crearContenido() {
        cardLayout     = new CardLayout();
        panelCentral   = new JPanel(cardLayout);
        panelCentral.setBackground(Estilos.COLOR_FONDO);

        panelDocente    = new PanelDocente();
        panelEstudiante = new PanelEstudiante();

        panelCentral.add(crearPanelBienvenida(), "bienvenida");
        panelCentral.add(panelDocente,           "docente");
        panelCentral.add(panelEstudiante,        "estudiante");

        return panelCentral;
    }

    private JPanel crearPanelBienvenida() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(Estilos.COLOR_FONDO);

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Estilos.COLOR_BORDE),
            BorderFactory.createEmptyBorder(50, 70, 50, 70)));

        JLabel ico = new JLabel("📚", SwingConstants.CENTER);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 72));
        ico.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel t1 = new JLabel("Bienvenido al Sistema Tareas", SwingConstants.CENTER);
        t1.setFont(new Font("Segoe UI", Font.BOLD, 22));
        t1.setForeground(Estilos.COLOR_PRIMARIO);
        t1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel t2 = new JLabel("Seleccione su rol y usuario en la barra superior para continuar",
                SwingConstants.CENTER);
        t2.setFont(Estilos.FUENTE_NORMAL);
        t2.setForeground(Estilos.COLOR_TEXTO_GRIS);
        t2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(400, 2));

        JLabel t3 = new JLabel("Gestión de tareas, entregas y calificaciones", SwingConstants.CENTER);
        t3.setFont(Estilos.FUENTE_PEQUEÑA);
        t3.setForeground(Estilos.COLOR_TEXTO_GRIS);
        t3.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(ico);
        card.add(Box.createVerticalStrut(18));
        card.add(t1);
        card.add(Box.createVerticalStrut(10));
        card.add(t2);
        card.add(Box.createVerticalStrut(16));
        card.add(sep);
        card.add(Box.createVerticalStrut(10));
        card.add(t3);

        outer.add(card);
        return outer;
    }

    private JPanel crearBarraEstado() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(new Color(236, 239, 241));
        barra.setBorder(BorderFactory.createEmptyBorder(4, 14, 4, 14));

        JLabel lblInfo = new JLabel("Sistema Tareas  |  Gestión Académica");
        lblInfo.setFont(Estilos.FUENTE_PEQUEÑA);
        lblInfo.setForeground(Estilos.COLOR_TEXTO_GRIS);
        barra.add(lblInfo, BorderLayout.WEST);

        JLabel lblVer = new JLabel("v1.0");
        lblVer.setFont(Estilos.FUENTE_PEQUEÑA);
        lblVer.setForeground(Estilos.COLOR_TEXTO_GRIS);
        barra.add(lblVer, BorderLayout.EAST);
        return barra;
    }

    private void cargarUsuarios() {
        docentes    = docenteDAO.obtenerTodos();
        estudiantes = estudianteCtrl.obtenerTodos();
    }

    private void onRolCambiado() {
    	cargarUsuarios();
        String rol = (String) cmbRol.getSelectedItem();
        cmbUsuario.removeAllItems();
        cmbUsuario.setEnabled(false);
        lblBienvenidaUsuario.setText("");
        cardLayout.show(panelCentral, "bienvenida");

        if ("Docente".equals(rol)) {
            cmbUsuario.setEnabled(true);
            for (Docente d : docentes) cmbUsuario.addItem(d);
        } else if ("Estudiante".equals(rol)) {
            cmbUsuario.setEnabled(true);
            for (Estudiante e : estudiantes) cmbUsuario.addItem(e);
        }
    }

    private void onUsuarioCambiado() {
        Object sel = cmbUsuario.getSelectedItem();
        if (sel == null) return;
        String rol = (String) cmbRol.getSelectedItem();

        if ("Docente".equals(rol) && sel instanceof Docente) {
            Docente d = (Docente) sel;
            lblBienvenidaUsuario.setText("   " + d.getNombre());
            panelDocente.cargarDatos(d.getId());
            cardLayout.show(panelCentral, "docente");

        } else if ("Estudiante".equals(rol) && sel instanceof Estudiante) {
            Estudiante e = (Estudiante) sel;
            lblBienvenidaUsuario.setText("  " + e.getNombre() + " [" + e.getCodigo() + "]");
            panelEstudiante.cargarDatos(e.getId());
            cardLayout.show(panelCentral, "estudiante");
        }
    }

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 770);
        setMinimumSize(new Dimension(950, 620));
        setLocationRelativeTo(null);
    }

    private Image crearIcono() {
       
        java.awt.image.BufferedImage img =
            new java.awt.image.BufferedImage(32, 32, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Estilos.COLOR_PRIMARIO);
        g.fillRoundRect(0, 0, 32, 32, 8, 8);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI", Font.BOLD, 20));
        g.drawString("C", 8, 24);
        g.dispose();
        return img;
    }
}
