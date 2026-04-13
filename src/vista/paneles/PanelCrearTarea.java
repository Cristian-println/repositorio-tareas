package vista.paneles;

import controlador.TareaControlador;
import modelo.Materia;
import modelo.Tarea;
import modelo.dao.MateriaDAO;
import vista.Estilos;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Formulario para que el Docente cree nuevas tareas (HU-1).
 * Responsable: Cristian (T46)
 */
public class PanelCrearTarea extends JPanel {

    private int docenteId = -1;

    private JTextField         txtTitulo;
    private JTextArea          txtDescripcion;
    private JComboBox<Materia> cmbMateria;
    private JSpinner           spnFecha;
    private JSpinner           spnCalMax;
    private JTextField         txtArchivoNombre;
    private File               archivoAdjunto;
    private JLabel             lblMensaje;

    private final TareaControlador ctrl      = new TareaControlador();
    private final MateriaDAO       materiaDAO = new MateriaDAO();

    public PanelCrearTarea() {
        setLayout(new BorderLayout());
        setBackground(Estilos.COLOR_FONDO);
        construirUI();
    }

    private void construirUI() {
        add(Estilos.panelHeader("✏  Crear Nueva Tarea"), BorderLayout.NORTH);

        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setBackground(Estilos.COLOR_FONDO);
        contenedor.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Estilos.COLOR_BORDE),
            BorderFactory.createEmptyBorder(28, 32, 28, 32)));

        GridBagConstraints g = new GridBagConstraints();
        g.insets  = new Insets(9, 6, 9, 6);
        g.anchor  = GridBagConstraints.WEST;

        int row = 0;

        // ── Título ──
        agregarFila(form, g, row++, "Título: *",
            txtTitulo = Estilos.campo(35));

        // ── Descripción ──
        txtDescripcion = Estilos.areaTexto(4, 35);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setPreferredSize(new Dimension(0, 90));
        agregarFila(form, g, row++, "Descripción:", scrollDesc);

        // ── Materia ──
        cmbMateria = new JComboBox<>();
        cmbMateria.setFont(Estilos.FUENTE_NORMAL);
        cargarMaterias();
        agregarFila(form, g, row++, "Materia: *", cmbMateria);

        // ── Fecha límite ──
        spnFecha = new JSpinner(new SpinnerDateModel());
        spnFecha.setEditor(new JSpinner.DateEditor(spnFecha, "dd/MM/yyyy  HH:mm"));
        spnFecha.setFont(Estilos.FUENTE_NORMAL);
        // Valor inicial: mañana
        spnFecha.setValue(Date.from(LocalDateTime.now().plusDays(1)
            .atZone(ZoneId.systemDefault()).toInstant()));
        agregarFila(form, g, row++, "Fecha límite: *", spnFecha);

        // ── Nota máxima ──
        spnCalMax = new JSpinner(new SpinnerNumberModel(100.0, 0.5, 1000.0, 0.5));
        spnCalMax.setFont(Estilos.FUENTE_NORMAL);
        JPanel panelNota = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelNota.setOpaque(false);
        panelNota.add(spnCalMax);
        panelNota.add(Box.createHorizontalStrut(8));
        panelNota.add(Estilos.etiquetaGris("puntos"));
        agregarFila(form, g, row++, "Nota máxima: *", panelNota);

        // ── Archivo adjunto ──
        JPanel panelArchivo = new JPanel(new BorderLayout(6, 0));
        panelArchivo.setOpaque(false);
        txtArchivoNombre = new JTextField("(ninguno seleccionado)");
        txtArchivoNombre.setFont(Estilos.FUENTE_PEQUEÑA);
        txtArchivoNombre.setEditable(false);
        txtArchivoNombre.setBackground(new Color(245, 245, 245));
        JButton btnSeleccionar = new JButton("📎 Seleccionar");
        btnSeleccionar.setFont(Estilos.FUENTE_NORMAL);
        btnSeleccionar.addActionListener(e -> seleccionarArchivoAdjunto());
        panelArchivo.add(txtArchivoNombre, BorderLayout.CENTER);
        panelArchivo.add(btnSeleccionar,   BorderLayout.EAST);
        agregarFila(form, g, row++, "Adjunto (opcional):", panelArchivo);

        // ── Botones ──
        g.gridx = 0; g.gridy = row++; g.gridwidth = 2;
        g.fill = GridBagConstraints.HORIZONTAL;
        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBtns.setOpaque(false);
        JButton btnLimpiar = Estilos.botonSecundario("Limpiar");
        JButton btnCrear   = Estilos.botonPrimario("✔  Crear Tarea");
        btnLimpiar.addActionListener(e -> limpiar());
        btnCrear.addActionListener(e -> crearTarea());
        panelBtns.add(btnLimpiar);
        panelBtns.add(btnCrear);
        form.add(panelBtns, g);

        // ── Mensaje de estado ──
        g.gridy = row; g.insets = new Insets(4, 6, 4, 6);
        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(Estilos.FUENTE_NORMAL);
        form.add(lblMensaje, g);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1; gc.weighty = 1;
        contenedor.add(form, gc);

        add(Estilos.scrollPane(contenedor), BorderLayout.CENTER);
    }

    /** Helper para añadir una fila etiqueta-componente al form. */
    private void agregarFila(JPanel form, GridBagConstraints g,
                              int fila, String etiqueta, Component comp) {
        g.gridx = 0; g.gridy = fila; g.gridwidth = 1;
        g.weightx = 0; g.fill = GridBagConstraints.NONE;
        form.add(Estilos.etiqueta(etiqueta), g);

        g.gridx = 1; g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        form.add(comp, g);
    }

    private void cargarMaterias() {
        cmbMateria.removeAllItems();
        List<Materia> materias = materiaDAO.obtenerTodas();
        for (Materia m : materias) cmbMateria.addItem(m);
    }

    private void seleccionarArchivoAdjunto() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccionar archivo adjunto");
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            archivoAdjunto = fc.getSelectedFile();
            txtArchivoNombre.setText(archivoAdjunto.getName() +
                String.format("  (%.2f MB)", archivoAdjunto.length() / (1024.0 * 1024)));
        }
    }

    private void crearTarea() {
        Materia materia = (Materia) cmbMateria.getSelectedItem();
        if (materia == null) { mostrarError("Seleccione una materia."); return; }
        if (docenteId < 1)   { mostrarError("No se identificó al docente."); return; }

        Date fechaDate = (Date) spnFecha.getValue();
        LocalDateTime fechaLimite = fechaDate.toInstant()
            .atZone(ZoneId.systemDefault()).toLocalDateTime();

        // Copiar adjunto si existe
        String rutaAdjunto = null;
        if (archivoAdjunto != null) {
            try {
                rutaAdjunto = copiarAdjunto(archivoAdjunto);
            } catch (IOException ex) {
                mostrarError("Error al guardar adjunto: " + ex.getMessage());
                return;
            }
        }

        Tarea tarea = new Tarea(
            txtTitulo.getText().trim(),
            txtDescripcion.getText().trim(),
            fechaLimite,
            (Double) spnCalMax.getValue(),
            rutaAdjunto,
            materia.getId(),
            docenteId
        );

        String error = ctrl.crearTarea(tarea);
        if (error != null) {
            mostrarError(error);
        } else {
            mostrarExito("✔  Tarea \"" + tarea.getTitulo() + "\" creada y asignada exitosamente.");
            limpiar();
        }
    }

    private String copiarAdjunto(File origen) throws IOException {
        Path dir = Paths.get("archivos/tareas");
        if (!Files.exists(dir)) Files.createDirectories(dir);
        String ext = "";
        int p = origen.getName().lastIndexOf('.');
        if (p >= 0) ext = origen.getName().substring(p);
        String nombre = "adj_" + System.currentTimeMillis() + ext;
        Path dest = dir.resolve(nombre);
        Files.copy(origen.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
        return dest.toString();
    }

    private void limpiar() {
        txtTitulo.setText("");
        txtDescripcion.setText("");
        spnCalMax.setValue(100.0);
        txtArchivoNombre.setText("(ninguno seleccionado)");
        archivoAdjunto = null;
        lblMensaje.setText(" ");
        txtTitulo.requestFocus();
    }

    private void mostrarError(String msg) {
        lblMensaje.setText("⚠  " + msg);
        lblMensaje.setForeground(Estilos.COLOR_ERROR);
    }

    private void mostrarExito(String msg) {
        lblMensaje.setText(msg);
        lblMensaje.setForeground(Estilos.COLOR_EXITO);
    }

    public void setDocenteId(int docenteId) {
        this.docenteId = docenteId;
        lblMensaje.setText(" ");
    }
}
