package vista;

import modelo.Docente;
import modelo.Estudiante;
import modelo.Tarea;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class BusquedaEstudiantesPanel extends JPanel {

    private static final Color PRIMARY    = new Color(37, 99, 235);
    private static final Color SUCCESS    = new Color(34, 197, 94);
    private static final Color WARN_COL   = new Color(245, 158, 11);
    private static final Color WARN_BG    = new Color(255, 251, 235);
    private static final Color DANGER     = new Color(239, 68, 68);
    private static final Color DANGER_BG  = new Color(254, 242, 242);
    private static final Color BG         = new Color(248, 250, 252);
    private static final Color CARD       = Color.WHITE;
    private static final Color BORDER_COL = new Color(226, 232, 240);
    private static final Color TEXT_MAIN  = new Color(15, 23, 42);
    private static final Color TEXT_SUB   = new Color(100, 116, 139);

    private Docente docente;
    private List<Estudiante> estudiantes;

    private JTextField txtNombre;
    private JTextField txtMateria;
    private JPanel panelResultados;

    public BusquedaEstudiantesPanel(Docente docente, List<Estudiante> estudiantes) {
        this.docente = docente;
        this.estudiantes = estudiantes;
        setBackground(BG);
        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(24, 24, 24, 24));
        buildUI();
    }

    private void buildUI() {
        JLabel lblHeader = new JLabel("🔍  Búsqueda de Estudiantes");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeader.setForeground(TEXT_MAIN);
        lblHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(lblHeader, BorderLayout.NORTH);

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(BG);

        JPanel cardBusqueda = new JPanel();
        cardBusqueda.setLayout(new BoxLayout(cardBusqueda, BoxLayout.Y_AXIS));
        cardBusqueda.setBackground(CARD);
        cardBusqueda.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(20, 20, 20, 20)));
        cardBusqueda.setAlignmentX(LEFT_ALIGNMENT);

        cardBusqueda.add(buildLabel("Nombre del estudiante *"));
        txtNombre = buildTextField("Ej: Juan");
        cardBusqueda.add(txtNombre);
        cardBusqueda.add(Box.createVerticalStrut(12));

        cardBusqueda.add(buildLabel("Materia *"));
        txtMateria = buildTextField("Ej: Matemáticas");
        cardBusqueda.add(txtMateria);
        cardBusqueda.add(Box.createVerticalStrut(16));

        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filaBotones.setBackground(CARD);
        filaBotones.setAlignmentX(LEFT_ALIGNMENT);

        JButton btnBuscar = buildBoton("🔍  Buscar estudiante", PRIMARY, new Color(29, 78, 216));
        JButton btnFiltrar = buildBoton("📋  Ver todos por materia", new Color(71, 85, 105), new Color(51, 65, 85));

        btnBuscar.addActionListener(e -> buscarEstudiante());
        btnFiltrar.addActionListener(e -> filtrarPorMateria());

        txtNombre.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) buscarEstudiante();
            }
        });

        filaBotones.add(btnBuscar);
        filaBotones.add(btnFiltrar);
        cardBusqueda.add(filaBotones);

        contenido.add(cardBusqueda);
        contenido.add(Box.createVerticalStrut(20));

        panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        panelResultados.setBackground(BG);
        panelResultados.setAlignmentX(LEFT_ALIGNMENT);
        contenido.add(panelResultados);

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        add(scroll, BorderLayout.CENTER);
    }

    private void buscarEstudiante() {
        String nombre  = txtNombre.getText().trim();
        String materia = txtMateria.getText().trim();

        if (nombre.isEmpty() || nombre.equals("Ej: Juan")) {
            mostrarBanner("⚠  El campo de nombre no puede estar vacío.", DANGER, DANGER_BG);
            return;
        }
        if (materia.isEmpty() || materia.equals("Ej: Matemáticas")) {
            mostrarBanner("⚠  El campo de materia no puede estar vacío.", DANGER, DANGER_BG);
            return;
        }

        String resultado = docente.buscarEstudiantePorNombre(nombre, materia, estudiantes);

        panelResultados.removeAll();

        if (resultado == null) {
        	
            mostrarBanner("📭  No se encontró ningún estudiante con ese nombre en la materia \"" + materia + "\".",
                    WARN_COL, WARN_BG);
        } else {
            JLabel lblTitulo = new JLabel("Resultado de búsqueda:");
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblTitulo.setForeground(TEXT_MAIN);
            lblTitulo.setAlignmentX(LEFT_ALIGNMENT);
            lblTitulo.setBorder(new EmptyBorder(0, 0, 8, 0));
            panelResultados.add(lblTitulo);
            panelResultados.add(buildCardResultado(resultado, materia));
        }

        panelResultados.revalidate();
        panelResultados.repaint();
    }

    private void filtrarPorMateria() {
        String materia = txtMateria.getText().trim();

        if (materia.isEmpty() || materia.equals("Ej: Matemáticas")) {
            mostrarBanner("⚠  Ingresa una materia para filtrar.", DANGER, DANGER_BG);
            return;
        }

        List<Estudiante> filtrados = docente.filtrarEstudiantesPorMateria(materia, estudiantes);

        panelResultados.removeAll();

        if (filtrados.isEmpty()) {
        	
            mostrarBanner("📭  No hay estudiantes registrados en la materia \"" + materia + "\".",
                    WARN_COL, WARN_BG);
        } else {
            JLabel lblTitulo = new JLabel("Estudiantes en \"" + materia + "\" (" + filtrados.size() + "):");
            lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblTitulo.setForeground(TEXT_MAIN);
            lblTitulo.setAlignmentX(LEFT_ALIGNMENT);
            lblTitulo.setBorder(new EmptyBorder(0, 0, 8, 0));
            panelResultados.add(lblTitulo);

            for (Estudiante est : filtrados) {
                String info = "Nombre: " + est.getNombre() + ", Apellido: " + est.getApellido();
                panelResultados.add(buildCardResultado(info, materia));
                panelResultados.add(Box.createVerticalStrut(8));
            }
        }

        panelResultados.revalidate();
        panelResultados.repaint();
    }

    private void mostrarBanner(String msg, Color fg, Color bg) {
        panelResultados.removeAll();
        JPanel banner = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        banner.setBackground(bg);
        banner.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(fg, 1, true),
                new EmptyBorder(12, 16, 12, 16)));
        banner.setAlignmentX(LEFT_ALIGNMENT);
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));
        JLabel lbl = new JLabel(msg);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(fg);
        banner.add(lbl);
        panelResultados.add(banner);
        panelResultados.revalidate();
        panelResultados.repaint();
    }

    private JPanel buildCardResultado(String info, String materia) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(14, 16, 14, 16)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        card.setAlignmentX(LEFT_ALIGNMENT);

        JLabel ico = new JLabel("👤");
        ico.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        card.add(ico, BorderLayout.WEST);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(CARD);

        JLabel lblInfo = new JLabel(info);
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblInfo.setForeground(TEXT_MAIN);

        JLabel lblMateria = new JLabel("📚 Materia: " + materia);
        lblMateria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMateria.setForeground(TEXT_SUB);

        centro.add(lblInfo);
        centro.add(Box.createVerticalStrut(3));
        centro.add(lblMateria);
        card.add(centro, BorderLayout.CENTER);

        return card;
    }

    private JLabel buildLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_MAIN);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(0, 0, 5, 0));
        return lbl;
    }

    private JTextField buildTextField(String placeholder) {
        JTextField tf = new JTextField(placeholder);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setForeground(TEXT_SUB);
        tf.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(8, 10, 8, 10)));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        tf.setAlignmentX(LEFT_ALIGNMENT);
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) { tf.setText(""); tf.setForeground(TEXT_MAIN); }
            }
            public void focusLost(FocusEvent e) {
                if (tf.getText().isBlank()) { tf.setText(placeholder); tf.setForeground(TEXT_SUB); }
            }
        });
        return tf;
    }

    private JButton buildBoton(String text, Color bg, Color bgPress) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bgPress : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
