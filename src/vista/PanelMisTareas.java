package vista;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Comparator;
import java.util.Vector;
 
/**
 * US6 - Ver Tareas Asignadas (GUI Principal - Cristian)
 * Panel para que el estudiante vea sus tareas con estado, fechas y filtros.
 */
public class PanelMisTareas extends JPanel {
 
    private JTable tablaTareas;
    private DefaultTableModel modeloTabla;
    private JLabel lblMensaje;
    private JComboBox<String> cmbMateria;
    private JButton btnOrdenarFecha;
    private JButton btnOrdenarEstado;
 
    private final String[] columnas = {"Título", "Descripción", "Fecha entrega", "Materia", "Estado"};
 
    private final Object[][] tareas = {
        {"Actividad 1 - SQL",      "Consultas SELECT con JOIN",         "15/06/2025", "Programación BD",      "Pendiente"},
        {"Tarea 2 - Procesos",     "Simulación de planificador MLFQ",   "10/06/2025", "Sistemas Operativos",  "Entregado"},
        {"Práctica 3 - Redes",     "Configurar protocolo TCP",          "20/06/2025", "Redes de Computadoras","Pendiente"},
        {"Actividad 2 - BD",       "Procedimientos almacenados",        "25/06/2025", "Programación BD",      "Pendiente"},
        {"Tarea 3 - Semáforos",    "Sincronización con JBACI",          "08/06/2025", "Sistemas Operativos",  "Entregado"},
    };
 
    public PanelMisTareas() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        inicializarComponentes();
    }
 
    private void inicializarComponentes() {
        // --- Título ---
        JLabel lblTitulo = new JLabel("Mis tareas asignadas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 100, 100));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);
 
        // --- Panel de controles ---
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        panelControles.setBackground(new Color(245, 248, 248));
        panelControles.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 210, 210)));
 
        JLabel lblFiltro = new JLabel("Filtrar por materia:");
        lblFiltro.setFont(new Font("Arial", Font.BOLD, 13));
 
        cmbMateria = new JComboBox<>(new String[]{
            "Todas", "Programación BD", "Sistemas Operativos", "Redes de Computadoras"
        });
        cmbMateria.setFont(new Font("Arial", Font.PLAIN, 13));
        cmbMateria.setPreferredSize(new Dimension(200, 32));
 
        btnOrdenarFecha   = crearBoton("↕ Ordenar por fecha",  new Color(70, 130, 180));
        btnOrdenarEstado  = crearBoton("↕ Ordenar por estado", new Color(100, 100, 180));
 
        panelControles.add(lblFiltro);
        panelControles.add(cmbMateria);
        panelControles.add(Box.createHorizontalStrut(10));
        panelControles.add(btnOrdenarFecha);
        panelControles.add(btnOrdenarEstado);
 
        // --- Tabla de tareas ---
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        cargarTodasLasTareas();
 
        tablaTareas = new JTable(modeloTabla);
        tablaTareas.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaTareas.setRowHeight(28);
        tablaTareas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaTareas.getTableHeader().setBackground(new Color(0, 120, 120));
        tablaTareas.getTableHeader().setForeground(Color.WHITE);
        tablaTareas.setSelectionBackground(new Color(200, 235, 235));
        tablaTareas.setGridColor(new Color(220, 220, 220));
 
        // Colorear filas según estado
        tablaTareas.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setHorizontalAlignment(CENTER);
                setFont(new Font("Arial", Font.BOLD, 12));
                if (!sel) {
                    boolean pendiente = "Pendiente".equals(v);
                    c.setBackground(pendiente ? new Color(255, 245, 210) : new Color(220, 255, 225));
                    setForeground(pendiente ? new Color(160, 100, 0) : new Color(0, 120, 60));
                }
                return c;
            }
        });
 
        JScrollPane scrollTabla = new JScrollPane(tablaTareas);
 
        // Mensaje de estado vacío
        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("Arial", Font.ITALIC, 13));
        lblMensaje.setForeground(Color.GRAY);
        lblMensaje.setBorder(new EmptyBorder(4, 4, 0, 0));
 
        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.setBackground(Color.WHITE);
        centro.add(panelControles, BorderLayout.NORTH);
        centro.add(scrollTabla, BorderLayout.CENTER);
        centro.add(lblMensaje, BorderLayout.SOUTH);
 
        add(centro, BorderLayout.CENTER);
 
        // --- Acciones ---
        cmbMateria.addActionListener(e -> filtrarPorMateria());
        btnOrdenarFecha.addActionListener(e -> ordenarPorFecha());
        btnOrdenarEstado.addActionListener(e -> ordenarPorEstado());
    }
 
    private void cargarTodasLasTareas() {
        modeloTabla.setRowCount(0);
        for (Object[] fila : tareas) modeloTabla.addRow(fila);
        actualizarMensaje();
    }
 
    private void filtrarPorMateria() {
        String seleccion = (String) cmbMateria.getSelectedItem();
        modeloTabla.setRowCount(0);
        for (Object[] fila : tareas) {
            if ("Todas".equals(seleccion) || fila[3].equals(seleccion))
                modeloTabla.addRow(fila);
        }
        actualizarMensaje();
    }
 
    private void ordenarPorFecha() {
        Vector<Vector> datos = modeloTabla.getDataVector();
        datos.sort(Comparator.comparing(v -> v.get(2).toString()));
        tablaTareas.repaint();
    }
 
    private void ordenarPorEstado() {
        Vector<Vector> datos = modeloTabla.getDataVector();
        datos.sort(Comparator.comparing(v -> v.get(4).toString()));
        tablaTareas.repaint();
    }
 
    private void actualizarMensaje() {
        if (modeloTabla.getRowCount() == 0) {
            lblMensaje.setText("No hay tareas asignadas para la materia seleccionada.");
            lblMensaje.setForeground(new Color(180, 80, 0));
        } else {
            lblMensaje.setText(" ");
        }
    }
 
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
