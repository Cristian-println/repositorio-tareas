package vista;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
 
/**
 * US4 - Revisar Notas (GUI Principal - Cristian)
 * Panel que muestra al estudiante sus tareas calificadas, notas,
 * comentarios del docente y su promedio general.
 */
public class PanelCalificaciones extends JPanel {
 
    private JTable tablaNotas;
    private DefaultTableModel modeloTabla;
    private JLabel lblPromedio;
 
    private final String[] columnas = {"Tarea", "Fecha entrega", "Nota", "Calificación máx.", "Comentario del docente"};
 
    public PanelCalificaciones() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        inicializarComponentes();
    }
 
    private void inicializarComponentes() {
        // --- Título ---
        JLabel lblTitulo = new JLabel("Mis calificaciones");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 100, 100));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);
 
        // --- Tabla de notas ---
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        cargarNotasEjemplo();
 
        tablaNotas = new JTable(modeloTabla);
        tablaNotas.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaNotas.setRowHeight(30);
        tablaNotas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaNotas.getTableHeader().setBackground(new Color(0, 120, 120));
        tablaNotas.getTableHeader().setForeground(Color.WHITE);
        tablaNotas.setSelectionBackground(new Color(200, 235, 235));
        tablaNotas.setGridColor(new Color(220, 220, 220));
 
        // Colorear nota según rendimiento
        tablaNotas.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setHorizontalAlignment(CENTER);
                if (!sel) {
                    try {
                        double nota = Double.parseDouble(v.toString());
                        c.setBackground(nota >= 70 ? new Color(220, 255, 220)
                                      : nota >= 51 ? new Color(255, 250, 200)
                                      : new Color(255, 220, 220));
                    } catch (Exception e) { c.setBackground(Color.WHITE); }
                }
                return c;
            }
        });
 
        // Columna de comentarios con tooltip
        tablaNotas.getColumnModel().getColumn(4).setPreferredWidth(220);
 
        JScrollPane scrollTabla = new JScrollPane(tablaNotas);
        add(scrollTabla, BorderLayout.CENTER);
 
        // --- Panel promedio ---
        JPanel panelPromedio = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        panelPromedio.setBackground(new Color(240, 250, 250));
        panelPromedio.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(180, 220, 220)));
 
        JLabel lblPromedioTxt = new JLabel("Promedio general:");
        lblPromedioTxt.setFont(new Font("Arial", Font.BOLD, 15));
        lblPromedioTxt.setForeground(new Color(40, 80, 80));
 
        lblPromedio = new JLabel(calcularPromedio());
        lblPromedio.setFont(new Font("Arial", Font.BOLD, 22));
        lblPromedio.setForeground(new Color(0, 130, 100));
 
        panelPromedio.add(lblPromedioTxt);
        panelPromedio.add(lblPromedio);
 
        if (modeloTabla.getRowCount() == 0) {
            JLabel lblVacio = new JLabel("  No hay calificaciones registradas aún.");
            lblVacio.setFont(new Font("Arial", Font.ITALIC, 13));
            lblVacio.setForeground(Color.GRAY);
            panelPromedio.add(lblVacio);
        }
 
        add(panelPromedio, BorderLayout.SOUTH);
    }
 
    private void cargarNotasEjemplo() {
        modeloTabla.addRow(new Object[]{"Actividad 1 - Algoritmos", "05/05/2025", "85", "100", "Buen trabajo, revisa la complejidad."});
        modeloTabla.addRow(new Object[]{"Tarea 2 - Bases de datos",  "20/05/2025", "72", "100", "Correcto, faltó normalización."});
        modeloTabla.addRow(new Object[]{"Práctica 3 - Redes",        "10/06/2025", "90", "100", "Excelente, muy completo."});
    }
 
    private String calcularPromedio() {
        if (modeloTabla.getRowCount() == 0) return "—";
        double suma = 0;
        int count = 0;
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            try {
                suma += Double.parseDouble(modeloTabla.getValueAt(i, 2).toString());
                count++;
            } catch (Exception ignored) {}
        }
        return count == 0 ? "—" : String.format("%.1f / 100", suma / count);
    }
}
