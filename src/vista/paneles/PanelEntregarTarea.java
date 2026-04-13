package vista.paneles;

import controlador.EntregaControlador;
import controlador.TareaControlador;
import modelo.Entrega;
import modelo.Tarea;
import validaciones.Validaciones;
import vista.Estilos;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel para que el Estudiante entregue una tarea (HU-2).
 * Responsable: Cristian (T40, T45)
 */
public class PanelEntregarTarea extends JPanel {

    private int estudianteId = -1;

    private JComboBox<Tarea>  cmbTareas;
    private JLabel            lblMateria;
    private JLabel            lblFechaLimite;
    private JLabel            lblDescripcion;
    private JLabel            lblEstadoEntrega;
    private JLabel            lblCalMax;
    private JTextField        txtArchivoNombre;
    private JTextArea         txtComentario;
    private JLabel            lblMensaje;
    private JButton           btnEntregar;

    private File              archivoSeleccionado;
    private List<Tarea>       tareasDisponibles = new ArrayList<>();

    private final TareaControlador    tareaCtrl   = new TareaControlador();
    private final EntregaControlador  entregaCtrl = new EntregaControlador();

    public PanelEntregarTarea() {
        setLayout(new BorderLayout());
        setBackground(Estilos.COLOR_FONDO);
        construirUI();
    }

    private void construirUI() {
        add(Estilos.panelHeader("   Entregar Tarea"), BorderLayout.NORTH);

        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setBackground(Estilos.COLOR_FONDO);
        contenedor.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Estilos.COLOR_BORDE),
            BorderFactory.createEmptyBorder(28, 32, 28, 32)));

        GridBagConstraints g = new GridBagConstraints();
        g.insets  = new Insets(8, 6, 8, 6);
        g.anchor  = GridBagConstraints.WEST;
        int row = 0;

        // ── Selección de tarea ──
        g.gridx=0; g.gridy=row; g.weightx=0; g.fill=GridBagConstraints.NONE;
        form.add(Estilos.etiqueta("Tarea: *"), g);
        g.gridx=1; g.weightx=1; g.fill=GridBagConstraints.HORIZONTAL;
        cmbTareas = new JComboBox<>();
        cmbTareas.setFont(Estilos.FUENTE_NORMAL);
        cmbTareas.addActionListener(e -> mostrarDetallesTarea());
        form.add(cmbTareas, g);
        row++;

        // ── Info de la tarea (solo lectura) ──
        g.gridx=0; g.gridy=row; g.weightx=0; g.fill=GridBagConstraints.NONE;
        form.add(Estilos.etiqueta("Materia:"), g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL;
        lblMateria = new JLabel("—");
        lblMateria.setFont(Estilos.FUENTE_NORMAL);
        form.add(lblMateria, g);
        row++;

        g.gridx=0; g.gridy=row; g.fill=GridBagConstraints.NONE;
        form.add(Estilos.etiqueta("Descripción:"), g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL;
        lblDescripcion = new JLabel("—");
        lblDescripcion.setFont(Estilos.FUENTE_NORMAL);
        form.add(lblDescripcion, g);
        row++;

        g.gridx=0; g.gridy=row; g.fill=GridBagConstraints.NONE;
        form.add(Estilos.etiqueta("Fecha límite:"), g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL;
        lblFechaLimite = new JLabel("—");
        lblFechaLimite.setFont(Estilos.FUENTE_NORMAL);
        form.add(lblFechaLimite, g);
        row++;

        g.gridx=0; g.gridy=row; g.fill=GridBagConstraints.NONE;
        form.add(Estilos.etiqueta("Nota máxima:"), g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL;
        lblCalMax = new JLabel("—");
        lblCalMax.setFont(Estilos.FUENTE_NORMAL);
        form.add(lblCalMax, g);
        row++;

        g.gridx=0; g.gridy=row; g.fill=GridBagConstraints.NONE;
        form.add(Estilos.etiqueta("Mi entrega:"), g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL;
        lblEstadoEntrega = new JLabel("—");
        lblEstadoEntrega.setFont(Estilos.FUENTE_NEGRITA);
        form.add(lblEstadoEntrega, g);
        row++;

        // ── Separador ──
        g.gridx=0; g.gridy=row++; g.gridwidth=2; g.fill=GridBagConstraints.HORIZONTAL;
        form.add(new JSeparator(), g);
        g.gridwidth=1;

        // ── Selección de archivo ──
        g.gridx=0; g.gridy=row; g.fill=GridBagConstraints.NONE;
        form.add(Estilos.etiqueta("Archivo: *"), g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL;
        JPanel panelArchivo = new JPanel(new BorderLayout(6, 0));
        panelArchivo.setOpaque(false);
        txtArchivoNombre = Estilos.campo(25);
        txtArchivoNombre.setEditable(false);
        txtArchivoNombre.setBackground(new Color(245, 245, 245));
        txtArchivoNombre.setText("Haga clic en \"Seleccionar archivo\"");
        JButton btnSeleccionar = new JButton("  Seleccionar");
        btnSeleccionar.setFont(Estilos.FUENTE_NORMAL);
        btnSeleccionar.addActionListener(e -> seleccionarArchivo());
        panelArchivo.add(txtArchivoNombre, BorderLayout.CENTER);
        panelArchivo.add(btnSeleccionar,   BorderLayout.EAST);
        form.add(panelArchivo, g);
        row++;

        // ── Nota de tamaños ──
        g.gridx=0; g.gridy=row++; g.gridwidth=2;
        JLabel lblNota = Estilos.etiquetaGris(
            "  ⓘ  Formatos permitidos: PDF, DOC, DOCX, TXT, XLSX, ZIP…  " +
            "| Tamaño: mínimo 10 MB — máximo 1 GB");
        lblNota.setFont(Estilos.FUENTE_PEQUEÑA);
        form.add(lblNota, g);
        g.gridwidth=1;

        // ── Comentario ──
        g.gridx=0; g.gridy=row; g.fill=GridBagConstraints.NONE;
        form.add(Estilos.etiqueta("Comentario:"), g);
        g.gridx=1; g.fill=GridBagConstraints.HORIZONTAL; g.weighty=1;
        txtComentario = Estilos.areaTexto(4, 30);
        JScrollPane scrollCom = new JScrollPane(txtComentario);
        scrollCom.setPreferredSize(new Dimension(0, 90));
        form.add(scrollCom, g);
        row++;
        g.weighty=0;

        // ── Botón entregar ──
        g.gridx=0; g.gridy=row++; g.gridwidth=2; g.fill=GridBagConstraints.HORIZONTAL;
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBtn.setOpaque(false);
        btnEntregar = Estilos.botonExito("   Enviar Entrega");
        btnEntregar.setEnabled(false);
        btnEntregar.addActionListener(e -> enviarEntrega());
        panelBtn.add(btnEntregar);
        form.add(panelBtn, g);

        // ── Mensaje ──
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

    public void cargarDatos(int estudianteId) {
        this.estudianteId = estudianteId;
        cmbTareas.removeAllItems();
        tareasDisponibles = tareaCtrl.obtenerTareasEstudiante(estudianteId);

        for (Tarea t : tareasDisponibles) cmbTareas.addItem(t);

        if (!tareasDisponibles.isEmpty()) {
            mostrarDetallesTarea();
        } else {
            limpiarInfo();
        }
        lblMensaje.setText(" ");
    }

    private void mostrarDetallesTarea() {
        Tarea t = (Tarea) cmbTareas.getSelectedItem();
        if (t == null) { limpiarInfo(); return; }

        lblMateria.setText(t.getMateriaNombre());
        String desc = t.getDescripcion();
        lblDescripcion.setText(desc != null && !desc.isBlank()
            ? (desc.length() > 80 ? desc.substring(0, 80) + "…" : desc) : "(sin descripción)");
        lblCalMax.setText(String.format("%.0f puntos", t.getCalificacionMaxima()));

        if (t.estaVencida()) {
            lblFechaLimite.setText(t.getFechaLimiteFormateada() + "    VENCIDA");
            lblFechaLimite.setForeground(Estilos.COLOR_ERROR);
        } else {
            lblFechaLimite.setText(t.getFechaLimiteFormateada());
            lblFechaLimite.setForeground(Estilos.COLOR_EXITO);
        }

        // Estado de entrega actual
        if (estudianteId > 0) {
            Entrega existente = entregaCtrl.obtenerEntrega(t.getId(), estudianteId);
            if (existente != null) {
                String estadoTxt = existente.getNota() != null
                    ? String.format("Ya calificada (%.1f)", existente.getNota())
                    : (existente.isEsTardio() ? "Ya entregada (tardía)" : "Ya entregada ✔");
                lblEstadoEntrega.setText(estadoTxt);
                lblEstadoEntrega.setForeground(
                    existente.getNota() != null ? Estilos.COLOR_EXITO : Estilos.COLOR_TARDIO);
                btnEntregar.setText("   Volver a entregar");
            } else {
                lblEstadoEntrega.setText("Pendiente");
                lblEstadoEntrega.setForeground(Estilos.COLOR_TEXTO_GRIS);
                btnEntregar.setText("   Enviar Entrega");
            }
        }
        btnEntregar.setEnabled(archivoSeleccionado != null);
    }

    private void seleccionarArchivo() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccionar archivo de entrega");
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = fc.getSelectedFile();
            // Validar formato
            String errFmt = Validaciones.validarFormatoArchivo(archivo);
            if (errFmt != null) {
                mostrarError(errFmt);
                return;
            }
            // Validar tamaño (10MB mín, 1GB máx)
            String errTam = Validaciones.validarTamanoArchivo(archivo);
            if (errTam != null) {
                mostrarError(errTam);
                return;
            }
            archivoSeleccionado = archivo;
            txtArchivoNombre.setText(archivo.getName() +
                String.format("  (%.2f MB)", archivo.length() / (1024.0 * 1024)));
            btnEntregar.setEnabled(true);
            lblMensaje.setText(" ");
        }
    }

    private void enviarEntrega() {
        Tarea t = (Tarea) cmbTareas.getSelectedItem();
        if (t == null)                     { mostrarError("Seleccione una tarea."); return; }
        if (archivoSeleccionado == null)   { mostrarError("Seleccione un archivo."); return; }
        if (estudianteId < 1)              { mostrarError("Estudiante no identificado."); return; }

        // Confirmar si tardía
        if (t.estaVencida()) {
            int conf = JOptionPane.showConfirmDialog(this,
                "La fecha límite ya pasó. ¿Desea enviar la entrega de todas formas?\n" +
                "Quedará marcada como entrega tardía.",
                "Entrega tardía", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (conf != JOptionPane.YES_OPTION) return;
        }

        String error = entregaCtrl.entregarTarea(
            t.getId(), estudianteId,
            archivoSeleccionado,
            txtComentario.getText().trim()
        );

        if (error != null) {
            mostrarError(error);
        } else {
            String msg = t.estaVencida()
                ? "✔  Entrega recibida (tardía). El docente podrá revisarla."
                : "✔  Entrega enviada exitosamente. El docente recibirá tu archivo.";
            mostrarExito(msg);

            // Mostrar diálogo de confirmación (HU-2: debe mostrar confirmación)
            JOptionPane.showMessageDialog(this,
                "✔  ¡Entrega enviada exitosamente!\n\n" +
                "Tarea: " + t.getTitulo() + "\n" +
                "Archivo: " + archivoSeleccionado.getName() + "\n" +
                (t.estaVencida() ? "  Entrega tardía" : "Entrega a tiempo"),
                "Confirmación de Entrega", JOptionPane.INFORMATION_MESSAGE);

            // Refrescar estado
            archivoSeleccionado = null;
            txtArchivoNombre.setText("Haga clic en \"Seleccionar archivo\"");
            txtComentario.setText("");
            btnEntregar.setEnabled(false);
            mostrarDetallesTarea();
        }
    }

    private void limpiarInfo() {
        lblMateria.setText("—");
        lblDescripcion.setText("—");
        lblFechaLimite.setText("—");
        lblFechaLimite.setForeground(Estilos.COLOR_TEXTO);
        lblEstadoEntrega.setText("—");
        lblCalMax.setText("—");
    }

    private void mostrarError(String msg) {
        lblMensaje.setText("   " + msg);
        lblMensaje.setForeground(Estilos.COLOR_ERROR);
    }

    private void mostrarExito(String msg) {
        lblMensaje.setText(msg);
        lblMensaje.setForeground(Estilos.COLOR_EXITO);
    }
}
