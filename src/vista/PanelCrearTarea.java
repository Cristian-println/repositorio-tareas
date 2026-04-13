package vista;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
 
/**
 * US1 - Crear Tarea (GUI Principal - Cristian)
 * Panel que permite al docente crear y asignar tareas a estudiantes.
 */
public class PanelCrearTarea extends JPanel {
 
    private JTextField txtTitulo;
    private JTextArea txtDescripcion;
    private JSpinner spFecha;
    private JTextField txtCalificacion;
    private JLabel lblArchivo;
    private JButton btnAdjuntar;
    private JButton btnCrear;
    private File archivoAdjunto;
 
    public PanelCrearTarea() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 30, 20, 30));
        setBackground(Color.WHITE);
        inicializarComponentes();
    }
 
    private void inicializarComponentes() {
        // --- Título del panel ---
        JLabel lblTitulo = new JLabel("Crear nueva tarea");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 100, 100));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);
 
        // --- Formulario central ---
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
 
        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formulario.add(crearLabel("Título:"), gbc);
        txtTitulo = new JTextField(30);
        estilizarCampo(txtTitulo);
        gbc.gridx = 1; gbc.weightx = 1;
        formulario.add(txtTitulo, gbc);
 
        // Descripción
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        formulario.add(crearLabel("Descripción:"), gbc);
        txtDescripcion = new JTextArea(4, 30);
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 13));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        gbc.gridx = 1; gbc.weightx = 1; gbc.anchor = GridBagConstraints.CENTER;
        formulario.add(scrollDesc, gbc);
 
        // Fecha de entrega
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formulario.add(crearLabel("Fecha de entrega:"), gbc);
        SpinnerDateModel modelo = new SpinnerDateModel();
        spFecha = new JSpinner(modelo);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spFecha, "dd/MM/yyyy HH:mm");
        spFecha.setEditor(editor);
        spFecha.setFont(new Font("Arial", Font.PLAIN, 13));
        gbc.gridx = 1; gbc.weightx = 1;
        formulario.add(spFecha, gbc);
 
        // Calificación máxima
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formulario.add(crearLabel("Calificación máxima:"), gbc);
        txtCalificacion = new JTextField(10);
        estilizarCampo(txtCalificacion);
        gbc.gridx = 1; gbc.weightx = 1;
        formulario.add(txtCalificacion, gbc);
 
        // Adjuntar archivo
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formulario.add(crearLabel("Archivo adjunto:"), gbc);
 
        JPanel panelArchivo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelArchivo.setBackground(Color.WHITE);
        btnAdjuntar = crearBoton("Seleccionar archivo", new Color(70, 130, 180));
        lblArchivo = new JLabel("Ningún archivo seleccionado");
        lblArchivo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblArchivo.setForeground(Color.GRAY);
        panelArchivo.add(btnAdjuntar);
        panelArchivo.add(lblArchivo);
        gbc.gridx = 1; gbc.weightx = 1;
        formulario.add(panelArchivo, gbc);
 
        add(formulario, BorderLayout.CENTER);
 
        // --- Panel inferior: botón crear ---
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.setBackground(Color.WHITE);
        btnCrear = crearBoton("✔  Crear Tarea", new Color(0, 150, 100));
        btnCrear.setPreferredSize(new Dimension(160, 40));
        panelBoton.add(btnCrear);
        add(panelBoton, BorderLayout.SOUTH);
 
        // --- Acciones ---
        btnAdjuntar.addActionListener(e -> seleccionarArchivo());
        btnCrear.addActionListener(e -> crearTarea());
    }
 
    private void seleccionarArchivo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar archivo adjunto");
        int resultado = chooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivoAdjunto = chooser.getSelectedFile();
            lblArchivo.setText(archivoAdjunto.getName());
            lblArchivo.setForeground(new Color(0, 120, 0));
        }
    }
 
    private void crearTarea() {
        String titulo = txtTitulo.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String calificacion = txtCalificacion.getText().trim();
 
        if (titulo.isEmpty() || descripcion.isEmpty() || calificacion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor complete todos los campos obligatorios.",
                "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        JOptionPane.showMessageDialog(this,
            "✔  Tarea \"" + titulo + "\" creada y asignada correctamente.",
            "Tarea creada", JOptionPane.INFORMATION_MESSAGE);
 
        limpiarFormulario();
    }
 
    private void limpiarFormulario() {
        txtTitulo.setText("");
        txtDescripcion.setText("");
        txtCalificacion.setText("");
        lblArchivo.setText("Ningún archivo seleccionado");
        lblArchivo.setForeground(Color.GRAY);
        archivoAdjunto = null;
    }
 
    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(new Color(60, 60, 60));
        return lbl;
    }
 
    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Arial", Font.PLAIN, 13));
        campo.setBorder(BorderFactory.createCompoundBorder(
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
