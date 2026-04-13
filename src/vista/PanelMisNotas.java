package vista;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
 
/**
 * US8 - Revisar Promedio (GUI Principal - Cristian)
 * Panel que muestra las notas del estudiante y calcula su promedio general.
 */
public class PanelMisNotas extends JPanel {
 
    private JTable tablaNotas;
    private DefaultTableModel modeloTabla;
    private JLabel lblPromedioValor;
    private JLabel lblEstadoPromedio;
    private JProgressBar barraPromedio;
 
    private final String[] columnas = {"Tarea", "Materia", "Nota obtenida", "Máxima", "Porcentaje"};
 
    private final Object[][] notas = {
        {"Actividad 1 - SQL",   "Programación BD",      "85", "100", "85%"},
        {"Tarea 2 - Procesos",  "Sistemas Operativos",  "72", "100", "72%"},
        {"Práctica 3 - Redes",  "Redes",                "90", "100", "90%"},
        {"Actividad 2 - BD",    "Programación BD",      "68", "100", "68%"},
        {"Tarea 3 - Semáforos", "Sistemas Operativos",  "95", "100", "95%"},
    };
 
    public PanelMisNotas() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        inicializarComponentes();
    }
 
    private void inicializarComponentes() {
        // --- Título ---
        JLabel lblTitulo = new JLabel("Mis notas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 100, 100));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);
 
        // --- Tabla de notas ---
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
 
        if (notas.length == 0) {
            JLabel lblVacio = new JLabel("No hay calificaciones registradas aún.");
            lblVacio.setFont(new Font("Arial", Font.ITALIC, 14));
            lblVacio.setForeground(Color.GRAY);
            lblVacio.setHorizontalAlignment(SwingConstants.CENTER);
            add(lblVacio, BorderLayout.CENTER);
        } else {
            for (Object[] fila : notas) modeloTabla.addRow(fila);
 
            tablaNotas = new JTable(modeloTabla);
            tablaNotas.setFont(new Font("Arial", Font.PLAIN, 13));
            tablaNotas.setRowHeight(30);
            tablaNotas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
            tablaNotas.getTableHeader().setBackground(new Color(0, 120, 120));
            tablaNotas.getTableHeader().setForeground(Color.WHITE);
            tablaNotas.setSelectionBackground(new Color(200, 235, 235));
            tablaNotas.setGridColor(new Color(220, 220, 220));
 
            // Renderizador para columna nota con color
            tablaNotas.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable t, Object v,
                        boolean sel, boolean foc, int row, int col) {
                    Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                    setHorizontalAlignment(CENTER);
                    setFont(new Font("Arial", Font.BOLD, 13));
                    if (!sel) {
                        try {
                            double nota = Double.parseDouble(v.toString());
                            c.setBackground(nota >= 70 ? new Color(220, 255, 220)
                                          : nota >= 51 ? new Color(255, 250, 200)
                                          : new Color(255, 220, 220));
                            setForeground(nota >= 70 ? new Color(0, 120, 50)
                                        : nota >= 51 ? new Color(150, 100, 0)
                                        : new Color(180, 30, 30));
                        } catch (Exception e) { c.setBackground(Color.WHITE); }
                    }
                    return c;
                }
            });
 
            // Centrar columna porcentaje
            DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
            centrado.setHorizontalAlignment(SwingConstants.CENTER);
            tablaNotas.getColumnModel().getColumn(4).setCellRenderer(centrado);
 
            JScrollPane scrollTabla = new JScrollPane(tablaNotas);
            add(scrollTabla, BorderLayout.CENTER);
        }
 
        // --- Panel de promedio ---
        JPanel panelPromedio = new JPanel(new GridBagLayout());
        panelPromedio.setBackground(new Color(240, 250, 250));
        panelPromedio.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(180, 220, 220)),
            new EmptyBorder(15, 20, 15, 20)));
 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
 
        double promedio = calcularPromedio();
 
        // Etiqueta
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel lblPromedioTxt = new JLabel("Promedio general:");
        lblPromedioTxt.setFont(new Font("Arial", Font.BOLD, 15));
        lblPromedioTxt.setForeground(new Color(40, 80, 80));
        panelPromedio.add(lblPromedioTxt, gbc);
 
        // Valor numérico
        gbc.gridx = 1;
        lblPromedioValor = new JLabel(notas.length == 0 ? "—" : String.format("%.1f / 100", promedio));
        lblPromedioValor.setFont(new Font("Arial", Font.BOLD, 26));
        lblPromedioValor.setForeground(obtenerColorPromedio(promedio));
        panelPromedio.add(lblPromedioValor, gbc);
 
        // Estado descriptivo
        gbc.gridx = 2;
        lblEstadoPromedio = new JLabel(obtenerEtiquetaPromedio(promedio));
        lblEstadoPromedio.setFont(new Font("Arial", Font.BOLD, 12));
        lblEstadoPromedio.setForeground(Color.WHITE);
        lblEstadoPromedio.setOpaque(true);
        lblEstadoPromedio.setBackground(obtenerColorPromedio(promedio));
        lblEstadoPromedio.setBorder(new EmptyBorder(3, 10, 3, 10));
        panelPromedio.add(lblEstadoPromedio, gbc);
 
        // Barra de progreso
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        barraPromedio = new JProgressBar(0, 100);
        barraPromedio.setValue((int) promedio);
        barraPromedio.setPreferredSize(new Dimension(500, 18));
        barraPromedio.setForeground(obtenerColorPromedio(promedio));
        barraPromedio.setBackground(new Color(220, 220, 220));
        barraPromedio.setStringPainted(false);
        panelPromedio.add(barraPromedio, gbc);
 
        add(panelPromedio, BorderLayout.SOUTH);
    }
 
    private double calcularPromedio() {
        if (notas.length == 0) return 0;
        double suma = 0;
        for (Object[] fila : notas) {
            try { suma += Double.parseDouble(fila[2].toString()); }
            catch (Exception ignored) {}
        }
        return suma / notas.length;
    }
 
    private Color obtenerColorPromedio(double p) {
        if (p >= 80) return new Color(0, 150, 80);
        if (p >= 60) return new Color(180, 130, 0);
        return new Color(190, 50, 50);
    }
 
    private String obtenerEtiquetaPromedio(double p) {
        if (notas.length == 0) return "Sin datos";
        if (p >= 80) return "  Excelente  ";
        if (p >= 60) return "  Regular  ";
        return "  Deficiente  ";
    }
}
