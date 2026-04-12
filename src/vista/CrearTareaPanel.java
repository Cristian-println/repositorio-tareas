package vista;

import modelo.Archivo;
import modelo.Estudiante;
import modelo.Fecha;
import modelo.Tarea;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class CrearTareaPanel extends JPanel {

    private static final Color PRIMARY      = new Color(37, 99, 235);
    private static final Color PRIMARY_DARK = new Color(29, 78, 216);
    private static final Color SUCCESS      = new Color(34, 197, 94);
    private static final Color BG           = new Color(248, 250, 252);
    private static final Color CARD         = Color.WHITE;
    private static final Color BORDER_COL   = new Color(226, 232, 240);
    private static final Color TEXT_MAIN    = new Color(15, 23, 42);
    private static final Color TEXT_SUB     = new Color(100, 116, 139);
    private static final Color DANGER       = new Color(239, 68, 68);

    private JTextField txtTitulo;
    private JTextField txtMateria;
    private JTextArea  txtDescripcion;
    private JTextField txtCalifMax;
    private JTextField txtDia, txtMes, txtAnio, txtHora, txtMin;
    private JLabel     lblArchivoSeleccionado;
    private JLabel     lblConfirmacion;
    private Archivo    archivoAdjunto = null;
    private Estudiante estudiante;

    public CrearTareaPanel(Estudiante estudiante) {
        this.estudiante = estudiante;
        setBackground(BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(24, 24, 24, 24));
        buildUI();
    }

    private void buildUI() {
       
        JLabel lblHeader = new JLabel("✏️  Crear Nueva Tarea");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeader.setForeground(TEXT_MAIN);
        lblHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(lblHeader, BorderLayout.NORTH);

        JPanel card = new JPanel();
        card.setBackground(CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(24, 24, 24, 24)));

        card.add(buildLabel("Título *"));
        txtTitulo = buildTextField("Ej: Ejercicios de álgebra");
        card.add(txtTitulo);
        card.add(Box.createVerticalStrut(12));

        card.add(buildLabel("Materia *"));
        txtMateria = buildTextField("Ej: Matemáticas");
        card.add(txtMateria);
        card.add(Box.createVerticalStrut(12));

        card.add(buildLabel("Descripción *"));
        txtDescripcion = new JTextArea(4, 0);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(8, 10, 8, 10)));
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setBorder(null);
        scrollDesc.setAlignmentX(LEFT_ALIGNMENT);
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.add(scrollDesc);
        card.add(Box.createVerticalStrut(12));

        card.add(buildLabel("Calificación máxima *"));
        txtCalifMax = buildTextField("Ej: 100");
        card.add(txtCalifMax);
        card.add(Box.createVerticalStrut(12));

        card.add(buildLabel("Fecha límite *"));
        JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelFecha.setBackground(CARD);
        panelFecha.setAlignmentX(LEFT_ALIGNMENT);

        txtDia  = buildCampoFecha("DD",   40);
        txtMes  = buildCampoFecha("MM",   40);
        txtAnio = buildCampoFecha("AAAA", 60);
        txtHora = buildCampoFecha("HH",   40);
        txtMin  = buildCampoFecha("MM",   40);

        panelFecha.add(txtDia);
        panelFecha.add(new JLabel("/"));
        panelFecha.add(txtMes);
        panelFecha.add(new JLabel("/"));
        panelFecha.add(txtAnio);
        panelFecha.add(Box.createHorizontalStrut(10));
        panelFecha.add(new JLabel("🕐"));
        panelFecha.add(txtHora);
        panelFecha.add(new JLabel(":"));
        panelFecha.add(txtMin);
        card.add(panelFecha);
        card.add(Box.createVerticalStrut(16));

        card.add(buildLabel("Archivo adjunto"));
        JButton btnAdjuntar = buildSecondaryButton("📎  Adjuntar archivo");
        btnAdjuntar.addActionListener(e -> adjuntarArchivo());
        card.add(btnAdjuntar);
        card.add(Box.createVerticalStrut(6));

        lblArchivoSeleccionado = new JLabel("Ningún archivo seleccionado");
        lblArchivoSeleccionado.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblArchivoSeleccionado.setForeground(TEXT_SUB);
        lblArchivoSeleccionado.setAlignmentX(LEFT_ALIGNMENT);
        card.add(lblArchivoSeleccionado);
        card.add(Box.createVerticalStrut(20));

        lblConfirmacion = new JLabel();
        lblConfirmacion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblConfirmacion.setAlignmentX(LEFT_ALIGNMENT);
        lblConfirmacion.setVisible(false);
        card.add(lblConfirmacion);
        card.add(Box.createVerticalStrut(8));

        JButton btnCrear = buildPrimaryButton("✔  Crear Tarea");
        btnCrear.addActionListener(e -> crearTarea());
        card.add(btnCrear);

        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        add(scroll, BorderLayout.CENTER);
    }

 
    /** US1: adjuntar archivo */
    private void adjuntarArchivo() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            String extension = obtenerExtension(f.getName());
       
            archivoAdjunto = new Archivo(
                    f.getName(),
                    extension,
                    f.getAbsolutePath(),
                    (double) f.length() / 1024.0  // tamaño en KB
            );
            lblArchivoSeleccionado.setText(iconoArchivo(f.getName()) + "  " + f.getName()
                    + "  (" + String.format("%.1f", archivoAdjunto.getTamanio()) + " KB)");
            lblArchivoSeleccionado.setForeground(PRIMARY);
        }
    }

    private void crearTarea() {
        if (txtTitulo.getText().isBlank() || txtTitulo.getText().equals("Ej: Ejercicios de álgebra")) {
            mostrarMensaje("⚠  El título no puede estar vacío.", DANGER);
            return;
        }
        if (txtMateria.getText().isBlank() || txtMateria.getText().equals("Ej: Matemáticas")) {
            mostrarMensaje("⚠  La materia no puede estar vacía.", DANGER);
            return;
        }
        if (txtDescripcion.getText().isBlank()) {
            mostrarMensaje("⚠  La descripción no puede estar vacía.", DANGER);
            return;
        }
        double califMax;
        try {
            califMax = Double.parseDouble(txtCalifMax.getText().trim());
        } catch (NumberFormatException ex) {
            mostrarMensaje("⚠  La calificación máxima debe ser un número.", DANGER);
            return;
        }
        Fecha fechaLimite;
        try {
            int d = Integer.parseInt(txtDia.getText().trim());
            int m = Integer.parseInt(txtMes.getText().trim());
            int a = Integer.parseInt(txtAnio.getText().trim());
            int h = Integer.parseInt(txtHora.getText().trim());
            int min = Integer.parseInt(txtMin.getText().trim());
            fechaLimite = new Fecha(d, m, a, h, min);
        } catch (NumberFormatException ex) {
            mostrarMensaje("⚠  Completa todos los campos de la fecha correctamente.", DANGER);
            return;
        }

        Tarea tarea = new Tarea(
                txtTitulo.getText().trim(),
                fechaLimite,
                txtDescripcion.getText().trim(),
                califMax,
                archivoAdjunto, 
                txtMateria.getText().trim()
        );

        estudiante.agregarTareaPendiente(tarea);

        mostrarMensaje("✅  Tarea \"" + tarea.getTitulo() + "\" creada exitosamente.", SUCCESS);

        txtTitulo.setText("Ej: Ejercicios de álgebra");
        txtTitulo.setForeground(TEXT_SUB);
        txtMateria.setText("Ej: Matemáticas");
        txtMateria.setForeground(TEXT_SUB);
        txtDescripcion.setText("");
        txtCalifMax.setText("Ej: 100");
        txtCalifMax.setForeground(TEXT_SUB);
        txtDia.setText("DD"); txtMes.setText("MM");
        txtAnio.setText("AAAA"); txtHora.setText("HH"); txtMin.setText("MM");
        lblArchivoSeleccionado.setText("Ningún archivo seleccionado");
        lblArchivoSeleccionado.setForeground(TEXT_SUB);
        archivoAdjunto = null;
    }

    private void mostrarMensaje(String msg, Color color) {
        lblConfirmacion.setText(msg);
        lblConfirmacion.setForeground(color);
        lblConfirmacion.setVisible(true);
        Timer t = new Timer(4000, e -> lblConfirmacion.setVisible(false));
        t.setRepeats(false);
        t.start();
    }

    private String iconoArchivo(String nombre) {
        String n = nombre.toLowerCase();
        if (n.endsWith(".pdf"))                         return "📄";
        if (n.endsWith(".doc") || n.endsWith(".docx")) return "📝";
        if (n.endsWith(".xls") || n.endsWith(".xlsx")) return "📊";
        if (n.endsWith(".png") || n.endsWith(".jpg"))  return "🖼";
        if (n.endsWith(".zip") || n.endsWith(".rar"))  return "🗜";
        if (n.endsWith(".java")|| n.endsWith(".py"))   return "💻";
        return "📁";
    }

    private String obtenerExtension(String nombre) {
        int i = nombre.lastIndexOf('.');
        return (i >= 0) ? nombre.substring(i + 1).toUpperCase() : "OTRO";
    }

    private JLabel buildLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_MAIN);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(0, 0, 5, 0));
        return lbl;
    }

    private JTextField buildTextField(String placeholder) {
        JTextField tf = new JTextField(placeholder);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setForeground(TEXT_SUB);
        tf.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(8, 10, 8, 10)));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        tf.setAlignmentX(LEFT_ALIGNMENT);
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) { tf.setText(""); tf.setForeground(TEXT_MAIN); }
            }
            public void focusLost(FocusEvent e) {
                if (tf.getText().isBlank()) { tf.setText(placeholder); tf.setForeground(TEXT_SUB); }
            }
        });
        return tf;
    }

    private JTextField buildCampoFecha(String ph, int width) {
        JTextField tf = new JTextField(ph);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setForeground(TEXT_SUB);
        tf.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(6, 6, 6, 6)));
        tf.setPreferredSize(new Dimension(width, 36));
        tf.setHorizontalAlignment(JTextField.CENTER);
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(ph)) { tf.setText(""); tf.setForeground(TEXT_MAIN); }
            }
            public void focusLost(FocusEvent e) {
                if (tf.getText().isBlank()) { tf.setText(ph); tf.setForeground(TEXT_SUB); }
            }
        });
        return tf;
    }

    private JButton buildPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? PRIMARY_DARK : PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 44));
        return btn;
    }

    private JButton buildSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(PRIMARY);
        btn.setBackground(new Color(239, 246, 255));
        btn.setBorder(new CompoundBorder(
                new LineBorder(new Color(191, 219, 254), 1, true),
                new EmptyBorder(7, 14, 7, 14)));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        return btn;
    }
}
