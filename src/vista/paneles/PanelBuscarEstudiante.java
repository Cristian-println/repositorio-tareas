package vista.paneles;

import controlador.EstudianteControlador;
import modelo.Entrega;
import modelo.Estudiante;
import modelo.Tarea;
import vista.Estilos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel para buscar estudiantes y ver su información académica (HU-5).
 * Responsable: Danner (T35)
 */
public class PanelBuscarEstudiante extends JPanel {

    private JTextField          txtBusqueda;
    private DefaultTableModel   modeloResultados;
    private JTable              tablaResultados;
    private List<Estudiante>    estudiantesEncontrados;

    // Detalle del estudiante seleccionado
    private JLabel              lblNombre;
    private JLabel              lblCodigo;
    private DefaultTableModel   modeloTareas;
    private JTable              tablaTareas;

    private final EstudianteControlador ctrl = new EstudianteControlador();

    public PanelBuscarEstudiante() {
        setLayout(new BorderLayout());
        setBackground(Estilos.COLOR_FONDO);
        construirUI();
    }

    private void construirUI() {
        add(Estilos.panelHeader("   Buscar Estudiantes"), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            crearPanelBusqueda(), crearPanelDetalle());
        split.setDividerLocation(380);
        split.setDividerSize(6);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Estilos.COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 6));

        // ── Barra de búsqueda ──
        JPanel barraBusqueda = new JPanel(new BorderLayout(8, 0));
        barraBusqueda.setBackground(Estilos.COLOR_FONDO);
        txtBusqueda = Estilos.campo(20);
        txtBusqueda.setToolTipText("Buscar por nombre o código de estudiante");
        JButton btnBuscar = Estilos.botonPrimario("  Buscar");
        btnBuscar.setPreferredSize(new Dimension(100, 34));
        btnBuscar.addActionListener(e -> buscar());
        // También buscar con Enter
        txtBusqueda.addActionListener(e -> buscar());
        barraBusqueda.add(txtBusqueda, BorderLayout.CENTER);
        barraBusqueda.add(btnBuscar,   BorderLayout.EAST);

        // ── Tabla de resultados ──
        String[] cols = {"Nombre", "Código"};
        modeloResultados = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaResultados = new JTable(modeloResultados);
        Estilos.estilizarTabla(tablaResultados);
        tablaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaResultados.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) mostrarDetalleEstudiante();
        });

        JPanel panelResultados = Estilos.panelConTitulo("Resultados");
        panelResultados.setLayout(new BorderLayout());
        panelResultados.add(Estilos.scrollPane(tablaResultados), BorderLayout.CENTER);

        panel.add(barraBusqueda,   BorderLayout.NORTH);
        panel.add(panelResultados, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelDetalle() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Estilos.COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 6, 8, 12));

        // ── Info básica ──
        JPanel infoPanel = Estilos.panelConTitulo("Información del estudiante");
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 10, 8, 10);
        g.anchor = GridBagConstraints.WEST;

        g.gridx=0; g.gridy=0;
        infoPanel.add(Estilos.etiqueta("Nombre:"), g);
        g.gridx=1; g.weightx=1; g.fill=GridBagConstraints.HORIZONTAL;
        lblNombre = new JLabel("—");
        lblNombre.setFont(Estilos.FUENTE_NEGRITA);
        infoPanel.add(lblNombre, g);

        g.gridx=0; g.gridy=1; g.weightx=0; g.fill=GridBagConstraints.NONE;
        infoPanel.add(Estilos.etiqueta("Código:"), g);
        g.gridx=1;
        lblCodigo = new JLabel("—");
        lblCodigo.setFont(Estilos.FUENTE_NORMAL);
        infoPanel.add(lblCodigo, g);
        infoPanel.setPreferredSize(new Dimension(0, 100));

        // ── Tabla de tareas / notas ──
        String[] cols = {"Tarea", "Materia", "Fecha límite", "Estado", "Nota"};
        modeloTareas = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaTareas = new JTable(modeloTareas);
        Estilos.estilizarTabla(tablaTareas);

        JPanel panelTareas = Estilos.panelConTitulo("Tareas del estudiante");
        panelTareas.setLayout(new BorderLayout());
        panelTareas.add(Estilos.scrollPane(tablaTareas), BorderLayout.CENTER);

        panel.add(infoPanel,   BorderLayout.NORTH);
        panel.add(panelTareas, BorderLayout.CENTER);
        return panel;
    }

    // ── Lógica ───────────────────────────────────────────────────────
    public void cargarDatos() {
        buscarTodos();
    }

    private void buscar() {
        String termino = txtBusqueda.getText().trim();
        modeloResultados.setRowCount(0);
        limpiarDetalle();

        estudiantesEncontrados = ctrl.buscar(termino);
        if (estudiantesEncontrados.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No se encontraron estudiantes con ese criterio.",
                "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        for (Estudiante e : estudiantesEncontrados)
            modeloResultados.addRow(new Object[]{e.getNombre(), e.getCodigo()});
    }

    private void buscarTodos() {
        txtBusqueda.setText("");
        modeloResultados.setRowCount(0);
        limpiarDetalle();
        estudiantesEncontrados = ctrl.obtenerTodos();
        for (Estudiante e : estudiantesEncontrados)
            modeloResultados.addRow(new Object[]{e.getNombre(), e.getCodigo()});
    }

    private void mostrarDetalleEstudiante() {
        int fila = tablaResultados.getSelectedRow();
        if (fila < 0 || estudiantesEncontrados == null) return;

        Estudiante est = estudiantesEncontrados.get(fila);
        lblNombre.setText(est.getNombre());
        lblCodigo.setText(est.getCodigo());

        // Cargar tareas con estado
        modeloTareas.setRowCount(0);
        List<Entrega> entregas = ctrl.obtenerEntregas(est.getId());
        List<Tarea>   tareas   = ctrl.obtenerTareas(est.getId());

        // Crear mapa tarea → entrega
        java.util.Map<Integer, Entrega> mapaEntregas = new java.util.HashMap<>();
        for (Entrega e : entregas) mapaEntregas.put(e.getTareaId(), e);

        for (Tarea t : tareas) {
            Entrega e = mapaEntregas.get(t.getId());
            String estado, nota;
            if (e == null) {
                estado = t.estaVencida() ? "No entregada (vencida)" : "Pendiente";
                nota   = "—";
            } else if (e.getNota() != null) {
                estado = "Calificada";
                nota   = String.format("%.1f / %.1f", e.getNota(), t.getCalificacionMaxima());
            } else {
                estado = e.isEsTardio() ? "Entregada (tardía)" : "Entregada";
                nota   = "—";
            }
            modeloTareas.addRow(new Object[]{
                t.getTitulo(), t.getMateriaNombre(),
                t.getFechaLimiteFormateada(), estado, nota
            });
        }
    }

    private void limpiarDetalle() {
        lblNombre.setText("—");
        lblCodigo.setText("—");
        modeloTareas.setRowCount(0);
    }
}
