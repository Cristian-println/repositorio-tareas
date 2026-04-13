package vista;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
 
/**
 * US5 - Buscar Estudiantes (GUI Principal - Cristian)
 * Panel para que el docente busque estudiantes por nombre o ID.
 */
public class PanelBuscarEstudiantes extends JPanel {
 
    private JTextField txtBusqueda;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;
    private JLabel lblMensaje;
    private JButton btnBuscar;
 
    private final String[] columnas = {"ID / CI", "Nombre completo", "Materia", "Estado"};
 
    // Datos simulados de estudiantes inscritos
    private final String[][] estudiantes = {
        {"12345678", "Juan Pérez García",   "Programación BD",      "Inscrito"},
        {"87654321", "Ana López Mendoza",   "Sistemas Operativos",  "Inscrito"},
        {"11223344", "Carlos Ruiz Torres",  "Programación BD",      "Inscrito"},
        {"55667788", "María Flores Quispe", "Redes de Computadoras","Inscrito"},
        {"99001122", "Luis Mamani Condori", "Sistemas Operativos",  "Inscrito"},
    };
 
    public PanelBuscarEstudiantes() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 30, 20, 30));
        setBackground(Color.WHITE);
        inicializarComponentes();
    }
 
    private void inicializarComponentes() {
        // --- Título ---
        JLabel lblTitulo = new JLabel("Buscar estudiantes");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 100, 100));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);
 
        // --- Panel de búsqueda ---
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBusqueda.setBackground(new Color(245, 248, 248));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Criterio de búsqueda", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12), new Color(80, 80, 80)));
 
        JLabel lblCampo = new JLabel("Nombre o ID:");
        lblCampo.setFont(new Font("Arial", Font.BOLD, 13));
 
        txtBusqueda = new JTextField(28);
        txtBusqueda.setFont(new Font("Arial", Font.PLAIN, 13));
        txtBusqueda.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
 
        btnBuscar = crearBoton("🔍  Buscar", new Color(0, 120, 120));
        btnBuscar.setPreferredSize(new Dimension(120, 36));
 
        panelBusqueda.add(lblCampo);
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(btnBuscar);
 
        // --- Tabla de resultados ---
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
 
        tablaResultados = new JTable(modeloTabla);
        tablaResultados.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaResultados.setRowHeight(28);
        tablaResultados.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaResultados.getTableHeader().setBackground(new Color(0, 120, 120));
        tablaResultados.getTableHeader().setForeground(Color.WHITE);
        tablaResultados.setSelectionBackground(new Color(200, 235, 235));
        tablaResultados.setGridColor(new Color(225, 225, 225));
 
        JScrollPane scrollTabla = new JScrollPane(tablaResultados);
 
        // Mensaje de estado
        lblMensaje = new JLabel("Ingrese un nombre o ID para buscar.");
        lblMensaje.setFont(new Font("Arial", Font.ITALIC, 13));
        lblMensaje.setForeground(Color.GRAY);
        lblMensaje.setBorder(new EmptyBorder(6, 4, 0, 0));
 
        // Layout central
        JPanel centro = new JPanel(new BorderLayout(5, 5));
        centro.setBackground(Color.WHITE);
        centro.add(panelBusqueda, BorderLayout.NORTH);
        centro.add(scrollTabla, BorderLayout.CENTER);
        centro.add(lblMensaje, BorderLayout.SOUTH);
 
        add(centro, BorderLayout.CENTER);
 
        // --- Acciones ---
        btnBuscar.addActionListener(e -> buscarEstudiante());
        txtBusqueda.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) buscarEstudiante();
            }
        });
    }
 
    private void buscarEstudiante() {
        String criterio = txtBusqueda.getText().trim();
 
        if (criterio.isEmpty()) {
            lblMensaje.setText("⚠  El campo de búsqueda no puede estar vacío.");
            lblMensaje.setForeground(new Color(200, 80, 0));
            modeloTabla.setRowCount(0);
            return;
        }
 
        modeloTabla.setRowCount(0);
        String criterioLower = criterio.toLowerCase();
        int encontrados = 0;
 
        for (String[] est : estudiantes) {
            if (est[0].contains(criterio) || est[1].toLowerCase().contains(criterioLower)) {
                modeloTabla.addRow(est);
                encontrados++;
            }
        }
 
        if (encontrados == 0) {
            lblMensaje.setText("⚠  No se encontraron estudiantes inscritos con ese criterio.");
            lblMensaje.setForeground(new Color(200, 80, 0));
        } else {
            lblMensaje.setText("✔  Se encontraron " + encontrados + " estudiante(s) inscrito(s).");
            lblMensaje.setForeground(new Color(0, 130, 60));
        }
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
