package vista;

import modelo.Estudiante;
import modelo.Fecha;
import modelo.Tarea;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ListaTareasPanel extends JPanel {

    private static final Color PRIMARY    = new Color(37, 99, 235);
    private static final Color BG         = new Color(248, 250, 252);
    private static final Color CARD       = Color.WHITE;
    private static final Color BORDER_COL = new Color(226, 232, 240);
    private static final Color TEXT_MAIN  = new Color(15, 23, 42);
    private static final Color TEXT_SUB   = new Color(100, 116, 139);
    private static final Color WARN       = new Color(245, 158, 11);
    private static final Color SUCCESS    = new Color(34, 197, 94);

    private Estudiante estudiante;
    private JComboBox<String> cmbMateria;
    private JComboBox<String> cmbOrden;
    private JPanel panelLista;

    public ListaTareasPanel(Estudiante estudiante) {
        this.estudiante = estudiante;
        setBackground(BG);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(24, 24, 24, 24));
        buildUI();
    }

    private void buildUI() {
        JLabel lblHeader = new JLabel("📋  Lista de Tareas");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeader.setForeground(TEXT_MAIN);

        JPanel barraFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        barraFiltros.setBackground(BG);

        JLabel lblFiltro = new JLabel("Materia:");
        lblFiltro.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblFiltro.setForeground(TEXT_MAIN);

        cmbMateria = new JComboBox<>();
        cmbMateria.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbMateria.setPreferredSize(new Dimension(160, 34));
        actualizarMaterias();

        JLabel lblOrden = new JLabel("Ordenar por:");
        lblOrden.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblOrden.setForeground(TEXT_MAIN);

        cmbOrden = new JComboBox<>(new String[]{
            "Fecha límite (↑)", "Fecha límite (↓)", "Nombre (A-Z)", "Nombre (Z-A)"
        });
        cmbOrden.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbOrden.setPreferredSize(new Dimension(180, 34));

        cmbMateria.addActionListener(e -> refrescarLista());
        cmbOrden.addActionListener(e -> refrescarLista());

        JButton btnAplicar = new JButton("🔍  Aplicar");
        btnAplicar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAplicar.setForeground(Color.WHITE);
        btnAplicar.setBackground(PRIMARY);
        btnAplicar.setBorder(new EmptyBorder(7, 14, 7, 14));
        btnAplicar.setFocusPainted(false);
        btnAplicar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAplicar.addActionListener(e -> refrescarLista());

        barraFiltros.add(lblFiltro);
        barraFiltros.add(cmbMateria);
        barraFiltros.add(lblOrden);
        barraFiltros.add(cmbOrden);
        barraFiltros.add(btnAplicar);

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBackground(BG);
        top.add(lblHeader);
        top.add(Box.createVerticalStrut(12));
        top.add(barraFiltros);
        add(top, BorderLayout.NORTH);

        // Panel lista
        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(BG);

        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        add(scroll, BorderLayout.CENTER);

        refrescarLista();
    }

    private List<Tarea> obtenerTareasFiltradas(String materia, String orden) {
        List<Tarea> lista = estudiante.verMisTareas();
        if (!"Todas".equals(materia)) {
            lista = lista.stream()
                    .filter(t -> t.getMateria().equalsIgnoreCase(materia))
                    .collect(Collectors.toList());
        }

        switch (orden) {
            case "Nombre (A-Z)"    -> lista.sort((a, b) -> a.getTitulo().compareToIgnoreCase(b.getTitulo()));
            case "Nombre (Z-A)"    -> lista.sort((a, b) -> b.getTitulo().compareToIgnoreCase(a.getTitulo()));
            case "Fecha límite (↓)"-> lista.sort((a, b) -> compararFechas(b.getFecha(), a.getFecha()));
            default                -> lista.sort((a, b) -> compararFechas(a.getFecha(), b.getFecha()));
        }
        return lista;
    }

    public void refrescarLista() {
        actualizarMaterias();
        String materia = (String) cmbMateria.getSelectedItem();
        String orden   = (String) cmbOrden.getSelectedItem();
        List<Tarea> tareas = obtenerTareasFiltradas(
                materia == null ? "Todas" : materia,
                orden   == null ? "Fecha límite (↑)" : orden);

        panelLista.removeAll();

        if (tareas.isEmpty()) {
            JPanel empty = new JPanel(new GridBagLayout());
            empty.setBackground(BG);
            empty.setPreferredSize(new Dimension(0, 200));

            JPanel inner = new JPanel();
            inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
            inner.setBackground(BG);

            JLabel ico = new JLabel("📭");
            ico.setFont(new Font("Segoe UI", Font.PLAIN, 38));
            ico.setAlignmentX(CENTER_ALIGNMENT);

            JLabel msg = new JLabel("No hay tareas asignadas para esta materia.");
            msg.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            msg.setForeground(TEXT_SUB);
            msg.setAlignmentX(CENTER_ALIGNMENT);

            inner.add(ico);
            inner.add(Box.createVerticalStrut(10));
            inner.add(msg);
            empty.add(inner);
            panelLista.add(empty);
        } else {
            for (Tarea t : tareas) {
                panelLista.add(buildTareaCard(t));
                panelLista.add(Box.createVerticalStrut(10));
            }
        }

        panelLista.revalidate();
        panelLista.repaint();
    }

    private void actualizarMaterias() {
        String sel = (String) cmbMateria.getSelectedItem();
        cmbMateria.removeAllItems();
        cmbMateria.addItem("Todas");
    
        estudiante.verMisTareas().stream()
                .map(Tarea::getMateria)
                .distinct()
                .sorted()
                .forEach(cmbMateria::addItem);
        if (sel != null) cmbMateria.setSelectedItem(sel);
    }

    private JPanel buildTareaCard(Tarea t) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(14, 16, 14, 16)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 88));
        card.setAlignmentX(LEFT_ALIGNMENT);

        JLabel ico = new JLabel("🕐");
        ico.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        card.add(ico, BorderLayout.WEST);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(CARD);

        JLabel titulo = new JLabel(t.getTitulo());
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titulo.setForeground(TEXT_MAIN);

        String fechaStr = t.getFecha() != null
                ? String.format("%02d/%02d/%04d %02d:%02d",
                    t.getFecha().getDia(), t.getFecha().getMes(), t.getFecha().getAnio(),
                    t.getFecha().getHora(), t.getFecha().getMinuto())
                : "Sin fecha";

        JLabel detalle = new JLabel(t.getMateria() + "  •  Límite: " + fechaStr);
        detalle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detalle.setForeground(TEXT_SUB);

        if (t.getArchivo() != null) {
            JLabel arch = new JLabel("📎 " + t.getArchivo().getNombre());
            arch.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            arch.setForeground(PRIMARY);
            centro.add(arch);
        }

        centro.add(titulo);
        centro.add(Box.createVerticalStrut(3));
        centro.add(detalle);
        card.add(centro, BorderLayout.CENTER);

        JLabel badge = new JLabel("Máx: " + (int) t.getCalificacionEstimada());
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badge.setForeground(Color.WHITE);
        badge.setBackground(PRIMARY);
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(4, 10, 4, 10));
        card.add(badge, BorderLayout.EAST);

        return card;
    }

    private int compararFechas(Fecha a, Fecha b) {
        if (a == null && b == null) return 0;
        if (a == null) return 1;
        if (b == null) return -1;
        if (a.getAnio()   != b.getAnio())   return Integer.compare(a.getAnio(),   b.getAnio());
        if (a.getMes()    != b.getMes())    return Integer.compare(a.getMes(),    b.getMes());
        if (a.getDia()    != b.getDia())    return Integer.compare(a.getDia(),    b.getDia());
        if (a.getHora()   != b.getHora())   return Integer.compare(a.getHora(),   b.getHora());
        return Integer.compare(a.getMinuto(), b.getMinuto());
    }
}
