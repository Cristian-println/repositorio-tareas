package vista;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
 
/**
 * US3 - Calificar Tareas (GUI Principal - Cristian)
 * Panel para que el docente revise entregas y asigne calificaciones.
 */
public class PanelCalificarTareas extends JPanel {
 
    private JTable tablaEntregas;
    private DefaultTableModel modeloTabla;
    private JTextField txtNota;
    private JTextArea txtComentario;
    private JButton btnGuardar;
    private JLabel lblEstadoPlazo;
 
    private final String[] columnas = {"Estudiante", "Archivo", "Fecha entrega", "Estado plazo"};
 
    public PanelCalificarTareas() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        inicializarComponentes();
    }
 
    private void inicializarComponentes() {
        // --- Título ---
        JLabel lblTitulo = new JLabel("Revisar y calificar entregas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 100, 100));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);
 
        // --- Tabla de entregas ---
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        cargarEntregasEjemplo();
 
        tablaEntregas = new JTable(modeloTabla);
        tablaEntregas.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaEntregas.setRowHeight(28);
        tablaEntregas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaEntregas.getTableHeader().setBackground(new Color(0, 120, 120));
        tablaEntregas.getTableHeader().setForeground(Color.WHITE);
        tablaEntregas.setSelectionBackground(new Color(200, 230, 230));
 
        // Colorear filas según estado de plazo
        tablaEntregas.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                String estado = (String) modeloTabla.getValueAt(row, 3);
                if (!sel) {
                    c.setBackground("Fuera de plazo".equals(estado)
                        ? new Color(255, 230, 225) : new Color(230, 255, 235));
                }
                return c;
            }
        });
 
        tablaEntregas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) mostrarEstadoPlazo();
        });
 
        JScrollPane scrollTabla = new JScrollPane(tablaEntregas);
        scrollTabla.setPreferredSize(new Dimension(0, 250));
        add(scrollTabla, BorderLayout.CENTER);
 
        // --- Panel de calificación ---
        JPanel panelCalif = new JPanel(new GridBagLayout());
        panelCalif.setBackground(new Color(248, 248, 248));
        panelCalif.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            "Asignar calificación", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 13), new Color(60, 60, 60)));
 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
 
        // Estado de plazo
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelCalif.add(crearLabel("Estado:"), gbc);
        lblEstadoPlazo = new JLabel("—");
        lblEstadoPlazo.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridx = 1; gbc.weightx = 1;
        panelCalif.add(lblEstadoPlazo, gbc);
 
        // Nota
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelCalif.add(crearLabel("Nota (0–100):"), gbc);
        txtNota = new JTextField(10);
        estilizarCampo(txtNota);
        gbc.gridx = 1; gbc.weightx = 1;
        panelCalif.add(txtNota, gbc);
 
        // Comentario
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        panelCalif.add(crearLabel("Comentarios:"), gbc);
        txtComentario = new JTextArea(3, 30);
        txtComentario.setFont(new Font("Arial", Font.PLAIN, 13));
        txtComentario.setLineWrap(true);
        txtComentario.setWrapStyleWord(true);
        txtComentario.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        gbc.gridx = 1; gbc.weightx = 1; gbc.anchor = GridBagConstraints.CENTER;
        panelCalif.add(new JScrollPane(txtComentario), gbc);
 
        // Botón guardar
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        btnGuardar = crearBoton("✔  Guardar calificación", new Color(0, 150, 100));
        btnGuardar.setPreferredSize(new Dimension(200, 38));
        panelCalif.add(btnGuardar, gbc);
 
        add(panelCalif, BorderLayout.SOUTH);
 
        // --- Acción botón guardar ---
        btnGuardar.addActionListener(e -> guardarCalificacion());
    }
 
    private void cargarEntregasEjemplo() {
        modeloTabla.addRow(new Object[]{"Juan Pérez",   "tarea1_juan.pdf",  "10/06/2025 09:30", "A tiempo"});
        modeloTabla.addRow(new Object[]{"Ana López",    "entrega_ana.docx", "12/06/2025 23:55", "Fuera de plazo"});
        modeloTabla.addRow(new Object[]{"Carlos Ruiz",  "carlos_act1.pdf",  "10/06/2025 14:00", "A tiempo"});
    }
 
    private void mostrarEstadoPlazo() {
        int fila = tablaEntregas.getSelectedRow();
        if (fila >= 0) {
            String estado = (String) modeloTabla.getValueAt(fila, 3);
            lblEstadoPlazo.setText(estado);
            lblEstadoPlazo.setForeground("Fuera de plazo".equals(estado)
                ? new Color(200, 50, 50) : new Color(0, 130, 60));
        }
    }
 
    private void guardarCalificacion() {
        int fila = tablaEntregas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione una entrega de la lista.", "Sin selección",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        String notaStr = txtNota.getText().trim();
        if (notaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Ingrese una nota antes de guardar.", "Campo vacío",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            double nota = Double.parseDouble(notaStr);
            if (nota < 0 || nota > 100) throw new NumberFormatException();
            String estudiante = (String) modeloTabla.getValueAt(fila, 0);
            JOptionPane.showMessageDialog(this,
                "✔  Calificación de " + nota + " guardada para " + estudiante + ".",
                "Guardado", JOptionPane.INFORMATION_MESSAGE);
            txtNota.setText("");
            txtComentario.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "La nota debe ser un número entre 0 y 100.", "Nota inválida",
                JOptionPane.ERROR_MESSAGE);
        }
    }
 
    private JLabel crearLabel(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Arial", Font.BOLD, 13));
        l.setForeground(new Color(60, 60, 60));
        return l;
    }
 
    private void estilizarCampo(JTextField c) {
        c.setFont(new Font("Arial", Font.PLAIN, 13));
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }
 
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
