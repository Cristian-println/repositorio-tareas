package vista;

import modelo.Archivo;
import modelo.Entrega;
import modelo.Estudiante;
import modelo.Fecha;
import modelo.Tarea;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class SubirTareaPanel extends JPanel {

    private static final Color PRIMARY    = new Color(37, 99, 235);
    private static final Color SUCCESS    = new Color(34, 197, 94);
    private static final Color SUCCESS_BG = new Color(240, 253, 244);
    private static final Color WARN_COL   = new Color(245, 158, 11);
    private static final Color WARN_BG    = new Color(255, 251, 235);
    private static final Color DANGER     = new Color(239, 68, 68);
    private static final Color BG         = new Color(248, 250, 252);
    private static final Color CARD       = Color.WHITE;
    private static final Color BORDER_COL = new Color(226, 232, 240);
    private static final Color TEXT_MAIN  = new Color(15, 23, 42);
    private static final Color TEXT_SUB   = new Color(100, 116, 139);

    private Estudiante estudiante;
    private Tarea tareaSeleccionada;
    private Archivo archivoSeleccionado = null;

    private JComboBox<String> cmbTarea;
    private JPanel panelArchivo;
    private JPanel panelEstado;
    private JLabel lblArchivoActual;

    public SubirTareaPanel(Estudiante estudiante) {
        this.estudiante = estudiante;
        setBackground(BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(24, 24, 24, 24));
        buildUI();
    }

    private void buildUI() {
        JLabel lblHeader = new JLabel("📤  Subir Tarea");
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

        card.add(buildLabel("Seleccionar tarea pendiente *"));
        cmbTarea = new JComboBox<>();
        cmbTarea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbTarea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        cmbTarea.setAlignmentX(LEFT_ALIGNMENT);
        actualizarComboTareas();
        cmbTarea.addActionListener(e -> onTareaSeleccionada());
        card.add(cmbTarea);
        card.add(Box.createVerticalStrut(20));

        card.add(buildLabel("Archivo de entrega"));

        JButton btnSubir = buildBoton("⬆  Subir archivo", PRIMARY, new Color(29, 78, 216));
        btnSubir.addActionListener(e -> seleccionarArchivo());
        card.add(btnSubir);
        card.add(Box.createVerticalStrut(10));

        panelArchivo = new JPanel();
        panelArchivo.setLayout(new BoxLayout(panelArchivo, BoxLayout.Y_AXIS));
        panelArchivo.setBackground(CARD);
        panelArchivo.setAlignmentX(LEFT_ALIGNMENT);
        card.add(panelArchivo);
        card.add(Box.createVerticalStrut(24));

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COL);
        sep.setAlignmentX(LEFT_ALIGNMENT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        card.add(sep);
        card.add(Box.createVerticalStrut(16));

        panelEstado = new JPanel();
        panelEstado.setLayout(new BoxLayout(panelEstado, BoxLayout.Y_AXIS));
        panelEstado.setBackground(CARD);
        panelEstado.setAlignmentX(LEFT_ALIGNMENT);
        card.add(panelEstado);
        card.add(Box.createVerticalStrut(16));

        JButton btnConfirmar = buildBoton("✔  Confirmar entrega", SUCCESS, new Color(21, 128, 61));
        btnConfirmar.addActionListener(e -> confirmarEntrega());
        card.add(btnConfirmar);

        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        add(scroll, BorderLayout.CENTER);
    }


    private void actualizarComboTareas() {
        cmbTarea.removeAllItems();
        cmbTarea.addItem("-- Selecciona una tarea --");
        for (Tarea t : estudiante.verMisTareas()) {
            cmbTarea.addItem(t.getTitulo() + " [" + t.getMateria() + "]");
        }
    }

    private void onTareaSeleccionada() {
        int idx = cmbTarea.getSelectedIndex();
        if (idx <= 0) { tareaSeleccionada = null; return; }
        List<Tarea> pendientes = estudiante.verMisTareas();
        if (idx - 1 < pendientes.size()) {
            tareaSeleccionada = pendientes.get(idx - 1);
        }
        archivoSeleccionado = null;
        panelArchivo.removeAll();
        panelArchivo.revalidate();
        panelArchivo.repaint();
        panelEstado.removeAll();
        panelEstado.revalidate();
        panelEstado.repaint();
    }

    private void seleccionarArchivo() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            String ext = obtenerExtension(f.getName());
            archivoSeleccionado = new Archivo(
                    f.getName(), ext,
                    f.getAbsolutePath(),
                    (double) f.length() / 1024.0
            );
            panelArchivo.removeAll();
            panelArchivo.add(buildFilaArchivo(archivoSeleccionado));
            panelArchivo.revalidate();
            panelArchivo.repaint();
        }
    }

    private JPanel buildFilaArchivo(Archivo archivo) {
        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setBackground(new Color(249, 250, 251));
        fila.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(10, 14, 10, 14)));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        fila.setAlignmentX(LEFT_ALIGNMENT);
        String ico = iconoPorTipo(archivo.getTipo(), archivo.getNombre());
        JLabel lbl = new JLabel(ico + "  " + archivo.getNombre()
                + "   (" + String.format("%.1f", archivo.getTamanio()) + " KB)");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(TEXT_MAIN);

        JButton btnElim = new JButton("🗑  Eliminar");
        btnElim.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnElim.setForeground(DANGER);
        btnElim.setBackground(null);
        btnElim.setBorderPainted(false);
        btnElim.setContentAreaFilled(false);
        btnElim.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnElim.addActionListener(e -> {
            archivoSeleccionado = null;
            panelArchivo.removeAll();
            panelArchivo.revalidate();
            panelArchivo.repaint();
        });

        fila.add(lbl, BorderLayout.CENTER);
        fila.add(btnElim, BorderLayout.EAST);
        return fila;
    }

    private void confirmarEntrega() {
        if (tareaSeleccionada == null) {
            mostrarBanner("⚠  Selecciona una tarea primero.", WARN_COL, WARN_BG);
            return;
        }
        if (archivoSeleccionado == null) {
            mostrarBanner("⚠  Debes subir un archivo antes de confirmar.", WARN_COL, WARN_BG);
            return;
        }

        java.time.LocalDateTime ldt = java.time.LocalDateTime.now();
        Fecha ahora = new Fecha(ldt.getDayOfMonth(), ldt.getMonthValue(), ldt.getYear(), ldt.getHour(), ldt.getMinute());

        Entrega entrega = new Entrega(
                (int)(System.currentTimeMillis() % 100000), // id temporal
                archivoSeleccionado,
                estudiante,
                tareaSeleccionada
        );

        boolean fueraDePlayzo;
        try {
            entrega.registrarEntrega(ahora);
            fueraDePlayzo = false;
        } catch (IllegalArgumentException ex) {
            fueraDePlayzo = true;
        }

        estudiante.agregarEntrega(entrega);

        panelEstado.removeAll();

        String fechaStr = String.format("%02d/%02d/%04d %02d:%02d",
                ahora.getDia(), ahora.getMes(), ahora.getAnio(),
                ahora.getHora(), ahora.getMinuto());
        panelEstado.add(buildBanner("📅  Entrega registrada el: " + fechaStr,
                PRIMARY, new Color(239, 246, 255)));
        panelEstado.add(Box.createVerticalStrut(8));

        if (fueraDePlayzo) {
            String limiteStr = tareaSeleccionada.getFecha() != null
                    ? String.format("%02d/%02d/%04d %02d:%02d",
                        tareaSeleccionada.getFecha().getDia(),
                        tareaSeleccionada.getFecha().getMes(),
                        tareaSeleccionada.getFecha().getAnio(),
                        tareaSeleccionada.getFecha().getHora(),
                        tareaSeleccionada.getFecha().getMinuto())
                    : "N/A";
            panelEstado.add(buildBanner(
                    "⚠  Entrega FUERA DE PLAZO. El límite era: " + limiteStr,
                    WARN_COL, WARN_BG));
        } else {
            panelEstado.add(buildBanner("✅  Entrega realizada dentro del plazo.", SUCCESS, SUCCESS_BG));
        }

        panelEstado.revalidate();
        panelEstado.repaint();

        actualizarComboTareas();
        cmbTarea.setSelectedIndex(0);
        tareaSeleccionada = null;
        archivoSeleccionado = null;
        panelArchivo.removeAll();
        panelArchivo.revalidate();
        panelArchivo.repaint();
    }

    private void mostrarBanner(String msg, Color fg, Color bg) {
        panelEstado.removeAll();
        panelEstado.add(buildBanner(msg, fg, bg));
        panelEstado.revalidate();
        panelEstado.repaint();
    }

    private JPanel buildBanner(String msg, Color fg, Color bg) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setBackground(bg);
        p.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(fg, 1, true),
                new EmptyBorder(10, 14, 10, 14)));
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        JLabel lbl = new JLabel(msg);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(fg);
        p.add(lbl);
        return p;
    }

    private String iconoPorTipo(String tipo, String nombre) {
        if (tipo != null) {
            return switch (tipo.toUpperCase()) {
                case "PDF"              -> "📄";
                case "DOC", "DOCX"     -> "📝";
                case "XLS", "XLSX"     -> "📊";
                case "PPT", "PPTX"     -> "📑";
                case "PNG", "JPG", "JPEG", "GIF" -> "🖼";
                case "MP4", "AVI"      -> "🎬";
                case "ZIP", "RAR"      -> "🗜";
                case "JAVA", "PY", "JS"-> "💻";
                default                -> iconoPorNombre(nombre);
            };
        }
        return iconoPorNombre(nombre);
    }

    private String iconoPorNombre(String nombre) {
        String n = nombre.toLowerCase();
        if (n.endsWith(".pdf"))  return "📄";
        if (n.endsWith(".docx") || n.endsWith(".doc")) return "📝";
        if (n.endsWith(".xlsx") || n.endsWith(".xls")) return "📊";
        if (n.endsWith(".png")  || n.endsWith(".jpg")) return "🖼";
        if (n.endsWith(".java") || n.endsWith(".py"))  return "💻";
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
        lbl.setBorder(new EmptyBorder(0, 0, 6, 0));
        return lbl;
    }

    private JButton buildBoton(String text, Color bg, Color bgPress) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bgPress : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 44));
        return btn;
    }
}
