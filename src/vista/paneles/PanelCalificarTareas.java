package vista.paneles;

import controlador.CalificacionControlador;
import controlador.EntregaControlador;
import controlador.TareaControlador;
import modelo.Entrega;
import modelo.Tarea;
import vista.Estilos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel para que el Docente revise entregas y califique (HU-3).
 * Responsable: Danner (T34)
 */
public class PanelCalificarTareas extends JPanel {

    private int docenteId = -1;

    // Listas
    private DefaultListModel<Tarea>   modeloTareas    = new DefaultListModel<>();
    private DefaultTableModel         modeloEntregas;
    private JList<Tarea>              listaTareas;
    private JTable                    tablaEntregas;

    // Form calificación
    private JLabel       lblEstudiante;
    private JLabel       lblEntregado;
    private JLabel       lblTardio;
    private JTextField   txtNota;
    private JTextArea    txtComentario;
    private JLabel       lblMensaje;
    private JLabel       lblArchivoEntrega;
    private JButton      btnCalificar;

    // Estado
    private List<Entrega> entregasActuales;
    private int           entregaSeleccionadaId  = -1;
    private double        calMaxActual           = 100.0;

    private final TareaControlador        tareaCtrl = new TareaControlador();
    private final EntregaControlador      entregaCtrl = new EntregaControlador();
    private final CalificacionControlador calCtrl = new CalificacionControlador();

    public PanelCalificarTareas() {
        setLayout(new BorderLayout());
        setBackground(Estilos.COLOR_FONDO);
        construirUI();
    }

    private void construirUI() {
        add(Estilos.panelHeader("📋  Calificar Tareas"), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            crearPanelIzquierdo(), crearPanelDerecho());
        split.setDividerLocation(320);
        split.setDividerSize(6);
        split.setBorder(null);
        split.setBackground(Estilos.COLOR_FONDO);
        add(split, BorderLayout.CENTER);
    }

    // ── Panel izquierdo: lista de tareas + tabla de entregas ──────────
    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(Estilos.COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 6));

        // ── Tareas ──
        JPanel panelTareas = Estilos.panelConTitulo("Tareas asignadas");
        panelTareas.setLayout(new BorderLayout());

        listaTareas = new JList<>(modeloTareas);
        listaTareas.setFont(Estilos.FUENTE_NORMAL);
        listaTareas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTareas.setCellRenderer(new TareaListCellRenderer());
        listaTareas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarEntregas();
        });
        panelTareas.add(Estilos.scrollPane(listaTareas), BorderLayout.CENTER);
        panelTareas.setPreferredSize(new Dimension(0, 240));

        // ── Entregas de la tarea seleccionada ──
        String[] cols = {"Estudiante", "Entregado", "Estado"};
        modeloEntregas = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaEntregas = new JTable(modeloEntregas);
        Estilos.estilizarTabla(tablaEntregas);
        tablaEntregas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEntregas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) mostrarDetalleEntrega();
        });

        JPanel panelEntregas = Estilos.panelConTitulo("Entregas recibidas");
        panelEntregas.setLayout(new BorderLayout());
        panelEntregas.add(Estilos.scrollPane(tablaEntregas), BorderLayout.CENTER);

        JSplitPane splitV = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            panelTareas, panelEntregas);
        splitV.setDividerLocation(240);
        splitV.setBorder(null);

        panel.add(splitV, BorderLayout.CENTER);
        return panel;
    }

    // ── Panel derecho: formulario de calificación ─────────────────────
    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Estilos.COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 6, 8, 12));

        JPanel form = Estilos.panelConTitulo("Calificar entrega");
        form.setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.insets  = new Insets(9, 8, 9, 8);
        g.anchor  = GridBagConstraints.WEST;
        int row = 0;

        // Estudiante
        g.gridx=0; g.gridy=row; g.weightx=0; g.fill=GridBagConstraints.NONE;
        form.add(Estilos.etiqueta("Estudiante:"), g);
        g.gridx=1; g.weightx=1; g.fill=GridBagConstraints.HORIZONTAL;
        lblEstudiante = new JLabel("—");
        lblEstudiante.setFont(Estilos.FUENTE_NEGRITA);
        form.add(lblEstudiante, g);
        row++;

        // Fecha entrega
        g.gridx=0; g.gridy=row; g.weightx=0; g.fill=GridBagConstraints.NONE;
        form.add(Estilos.etiqueta("Entregado:"), g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL;
        lblEntregado = new JLabel("—");
        lblEntregado.setFont(Estilos.FUENTE_NORMAL);
        form.add(lblEntregado, g);
        row++;

        // ¿Tardío?
        g.gridx=0; g.gridy=row; g.fill=GridBagConstraints.NONE;
        form.add(Estilos.etiqueta("Estado:"), g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL;
        lblTardio = new JLabel("—");
        lblTardio.setFont(Estilos.FUENTE_NORMAL);
        form.add(lblTardio, g);
        row++;

        // Archivo de entrega
        g.gridx=0; g.gridy=row; g.fill=GridBagConstraints.NONE;
        form.add(Estilos.etiqueta("Archivo:"), g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL;
        lblArchivoEntrega = new JLabel("—");
        lblArchivoEntrega.setFont(Estilos.FUENTE_PEQUEÑA);
        lblArchivoEntrega.setForeground(Estilos.COLOR_TEXTO_GRIS);
        form.add(lblArchivoEntrega, g);
        row++;

        // Separador
        g.gridx=0; g.gridy=row++; g.gridwidth=2; g.fill=GridBagConstraints.HORIZONTAL;
        form.add(new JSeparator(), g);
        g.gridwidth=1;

        // Nota
        g.gridx=0; g.gridy=row; g.fill=GridBagConstraints.NONE;
        form.add(Estilos.etiqueta("Nota: *"), g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL;
        JPanel panelNota = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelNota.setOpaque(false);
        txtNota = Estilos.campo(8);
        txtNota.setPreferredSize(new Dimension(90, 30));
        panelNota.add(txtNota);
        panelNota.add(Box.createHorizontalStrut(8));
        JLabel lblMaxNota = new JLabel("/ —");
        lblMaxNota.setFont(Estilos.FUENTE_PEQUEÑA);
        lblMaxNota.setForeground(Estilos.COLOR_TEXTO_GRIS);
        panelNota.add(lblMaxNota);
        form.add(panelNota, g);
        row++;

        // Comentario
        g.gridx=0; g.gridy=row; g.fill=GridBagConstraints.NONE;
        form.add(Estilos.etiqueta("Comentario:"), g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL; g.weighty=1;
        txtComentario = Estilos.areaTexto(5, 25);
        JScrollPane scrollCom = new JScrollPane(txtComentario);
        scrollCom.setPreferredSize(new Dimension(0, 110));
        form.add(scrollCom, g);
        row++;
        g.weighty = 0;

        // Botón calificar
        g.gridx=0; g.gridy=row++; g.gridwidth=2; g.fill=GridBagConstraints.HORIZONTAL;
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBtn.setOpaque(false);
        btnCalificar = Estilos.botonExito("✔  Guardar Calificación");
        btnCalificar.setEnabled(false);
        btnCalificar.addActionListener(e -> guardarCalificacion(lblMaxNota));
        panelBtn.add(btnCalificar);
        form.add(panelBtn, g);

        // Mensaje
        g.gridy = row;
        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(Estilos.FUENTE_NORMAL);
        form.add(lblMensaje, g);

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    // ── Lógica ───────────────────────────────────────────────────────
    public void cargarDatos(int docenteId) {
        this.docenteId = docenteId;
        modeloTareas.clear();
        modeloEntregas.setRowCount(0);
        limpiarFormCalificacion();

        List<Tarea> tareas = tareaCtrl.obtenerTareasDocente(docenteId);
        for (Tarea t : tareas) modeloTareas.addElement(t);
    }

    private void cargarEntregas() {
        Tarea tarea = listaTareas.getSelectedValue();
        modeloEntregas.setRowCount(0);
        limpiarFormCalificacion();
        if (tarea == null) return;

        calMaxActual = tarea.getCalificacionMaxima();
        entregasActuales = entregaCtrl.obtenerEntregasPorTarea(tarea.getId());

        for (Entrega e : entregasActuales) {
            String estado = e.getNota() != null
                ? String.format("Calificada (%.1f)", e.getNota())
                : e.isEsTardio() ? "Tardía" : "A tiempo";
            modeloEntregas.addRow(new Object[]{
                e.getEstudianteNombre(),
                e.getFechaEntregaFormateada(),
                estado
            });
        }
    }

    private void mostrarDetalleEntrega() {
        int fila = tablaEntregas.getSelectedRow();
        if (fila < 0 || entregasActuales == null) return;

        Entrega e = entregasActuales.get(fila);
        entregaSeleccionadaId = e.getId();

        lblEstudiante.setText(e.getEstudianteNombre());
        lblEntregado.setText(e.getFechaEntregaFormateada());

        if (e.isEsTardio()) {
            lblTardio.setText("⚠  Entrega tardía");
            lblTardio.setForeground(Estilos.COLOR_TARDIO);
        } else {
            lblTardio.setText("✔  Entregada a tiempo");
            lblTardio.setForeground(Estilos.COLOR_EXITO);
        }

        String ruta = e.getArchivoRuta();
        lblArchivoEntrega.setText(ruta != null
            ? java.nio.file.Paths.get(ruta).getFileName().toString() : "(sin archivo)");

        // Pre-rellenar nota si ya estaba calificada
        if (e.getNota() != null) {
            txtNota.setText(String.format("%.2f", e.getNota()));
        } else {
            txtNota.setText("");
        }
        txtComentario.setText(e.getComentarioDocente() != null ? e.getComentarioDocente() : "");

        btnCalificar.setEnabled(true);
        lblMensaje.setText(" ");
    }

    private void guardarCalificacion(JLabel lblMaxNota) {
        if (entregaSeleccionadaId < 0) return;

        String error = calCtrl.calificar(
            entregaSeleccionadaId,
            txtNota.getText(),
            txtComentario.getText().trim(),
            calMaxActual
        );
        if (error != null) {
            lblMensaje.setText("⚠  " + error);
            lblMensaje.setForeground(Estilos.COLOR_ERROR);
        } else {
            lblMensaje.setText("✔  Calificación guardada.");
            lblMensaje.setForeground(Estilos.COLOR_EXITO);
            cargarEntregas(); // refrescar tabla
        }
    }

    private void limpiarFormCalificacion() {
        lblEstudiante.setText("—");
        lblEntregado.setText("—");
        lblTardio.setText("—");
        lblTardio.setForeground(Estilos.COLOR_TEXTO);
        lblArchivoEntrega.setText("—");
        txtNota.setText("");
        txtComentario.setText("");
        btnCalificar.setEnabled(false);
        lblMensaje.setText(" ");
        entregaSeleccionadaId = -1;
    }

    // ── Renderer personalizado para la lista de tareas ────────────────
    private static class TareaListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> l, Object v,
                int idx, boolean sel, boolean focus) {
            super.getListCellRendererComponent(l, v, idx, sel, focus);
            if (v instanceof Tarea t) {
                setText("<html><b>" + t.getTitulo() + "</b><br>"
                    + "<small style='color:gray'>" + t.getMateriaNombre()
                    + " · " + t.getFechaLimiteFormateada() + "</small></html>");
                if (t.estaVencida() && !sel)
                    setBackground(new Color(255, 243, 224));
            }
            setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
            return this;
        }
    }
}


