package vista;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
 
/**
 * US2 - Entregar Tarea (GUI Principal - Cristian)
 * Panel que permite al estudiante subir y confirmar la entrega de su tarea.
 */
public class PanelEntregarTarea extends JPanel {
 
    private JLabel lblArchivoNombre;
    private JLabel lblIconoArchivo;
    private JLabel lblFechaEntrega;
    private JLabel lblAdvertencia;
    private JButton btnSubir;
    private JButton btnEliminar;
    private JButton btnConfirmar;
    private File archivoSeleccionado;
 
    private static final long MIN_BYTES = 10L * 1024 * 1024;   // 10 MB
    private static final long MAX_BYTES = 1024L * 1024 * 1024; // 1 GB
 
    public PanelEntregarTarea() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 30, 20, 30));
        setBackground(Color.WHITE);
        inicializarComponentes();
    }
 
    private void inicializarComponentes() {
        // --- Título ---
        JLabel lblTitulo = new JLabel("Entregar tarea");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 100, 100));
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);
 
        // --- Panel central ---
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(Color.WHITE);
 
        // Sección de archivo
        JPanel panelArchivo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelArchivo.setBackground(new Color(245, 245, 245));
        panelArchivo.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Archivo a entregar", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12), new Color(80, 80, 80)));
        panelArchivo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
 
        lblIconoArchivo = new JLabel("📄");
        lblIconoArchivo.setFont(new Font("Arial", Font.PLAIN, 32));
        lblIconoArchivo.setVisible(false);
 
        lblArchivoNombre = new JLabel("Ningún archivo seleccionado");
        lblArchivoNombre.setFont(new Font("Arial", Font.ITALIC, 13));
        lblArchivoNombre.setForeground(Color.GRAY);
 
        btnSubir    = crearBoton("⬆  Subir archivo",    new Color(70, 130, 180));
        btnEliminar = crearBoton("✖  Eliminar archivo", new Color(200, 60, 60));
        btnEliminar.setEnabled(false);
 
        panelArchivo.add(lblIconoArchivo);
        panelArchivo.add(lblArchivoNombre);
        panelArchivo.add(btnSubir);
        panelArchivo.add(btnEliminar);
 
        // Advertencia de plazo
        lblAdvertencia = new JLabel(" ");
        lblAdvertencia.setFont(new Font("Arial", Font.BOLD, 13));
        lblAdvertencia.setForeground(new Color(200, 80, 0));
        lblAdvertencia.setBorder(new EmptyBorder(8, 5, 0, 0));
 
        // Fecha y hora de entrega
        lblFechaEntrega = new JLabel(" ");
        lblFechaEntrega.setFont(new Font("Arial", Font.PLAIN, 13));
        lblFechaEntrega.setForeground(new Color(60, 60, 60));
        lblFechaEntrega.setBorder(new EmptyBorder(4, 5, 0, 0));
 
        centro.add(panelArchivo);
        centro.add(Box.createVerticalStrut(10));
        centro.add(lblAdvertencia);
        centro.add(lblFechaEntrega);
 
        add(centro, BorderLayout.CENTER);
 
        // --- Panel botón confirmar ---
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.setBackground(Color.WHITE);
        btnConfirmar = crearBoton("✔  Confirmar entrega", new Color(0, 150, 100));
        btnConfirmar.setPreferredSize(new Dimension(180, 40));
        btnConfirmar.setEnabled(false);
        panelBoton.add(btnConfirmar);
        add(panelBoton, BorderLayout.SOUTH);
 
        // --- Acciones ---
        btnSubir.addActionListener(e -> seleccionarArchivo());
        btnEliminar.addActionListener(e -> eliminarArchivo());
        btnConfirmar.addActionListener(e -> confirmarEntrega());
    }
 
    private void seleccionarArchivo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar archivo de entrega");
        int resultado = chooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivoSeleccionado = chooser.getSelectedFile();
            long size = archivoSeleccionado.length();
 
            if (size < MIN_BYTES) {
                JOptionPane.showMessageDialog(this,
                    "El archivo es menor al mínimo permitido (10 MB).",
                    "Tamaño inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (size > MAX_BYTES) {
                JOptionPane.showMessageDialog(this,
                    "El archivo supera el máximo permitido (1 GB).",
                    "Tamaño inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
 
            String nombre = archivoSeleccionado.getName();
            lblArchivoNombre.setText(nombre);
            lblArchivoNombre.setForeground(new Color(30, 100, 30));
            lblIconoArchivo.setText(obtenerIcono(nombre));
            lblIconoArchivo.setVisible(true);
            btnEliminar.setEnabled(true);
            btnConfirmar.setEnabled(true);
            lblAdvertencia.setText("⚠  Verifica que tu entrega esté dentro del plazo establecido.");
        }
    }
 
    private void eliminarArchivo() {
        archivoSeleccionado = null;
        lblArchivoNombre.setText("Ningún archivo seleccionado");
        lblArchivoNombre.setForeground(Color.GRAY);
        lblIconoArchivo.setVisible(false);
        lblFechaEntrega.setText(" ");
        lblAdvertencia.setText(" ");
        btnEliminar.setEnabled(false);
        btnConfirmar.setEnabled(false);
    }
 
    private void confirmarEntrega() {
        String ahora = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        lblFechaEntrega.setText("Entregado el: " + ahora);
 
        JOptionPane.showMessageDialog(this,
            "✔  Entrega confirmada correctamente.\nFecha y hora: " + ahora,
            "Entrega exitosa", JOptionPane.INFORMATION_MESSAGE);
 
        btnConfirmar.setEnabled(false);
        btnSubir.setEnabled(false);
        btnEliminar.setEnabled(false);
        lblAdvertencia.setText(" ");
    }
 
    private String obtenerIcono(String nombre) {
        String n = nombre.toLowerCase();
        if (n.endsWith(".pdf"))  return "📕";
        if (n.endsWith(".doc") || n.endsWith(".docx")) return "📘";
        if (n.endsWith(".zip") || n.endsWith(".rar"))  return "🗜";
        if (n.endsWith(".png") || n.endsWith(".jpg"))  return "🖼";
        return "📄";
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
 
