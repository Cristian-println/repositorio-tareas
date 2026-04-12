package vista;

import modelo.Archivo;
import modelo.Calificacion;
import modelo.Docente;
import modelo.Entrega;
import modelo.Estudiante;
import modelo.Fecha;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class RevisionEntregasPanel extends JPanel {

    private static final Color PRIMARY    = new Color(37, 99, 235);
    private static final Color SUCCESS    = new Color(34, 197, 94);
    private static final Color SUCCESS_BG = new Color(240, 253, 244);
    private static final Color WARN_COL   = new Color(245, 158, 11);
    private static final Color WARN_BG    = new Color(255, 251, 235);
    private static final Color DANGER     = new Color(239, 68, 68);
    private static final Color DANGER_BG  = new Color(254, 242, 242);
    private static final Color BG         = new Color(248, 250, 252);
    private static final Color CARD       = Color.WHITE;
    private static final Color BORDER_COL = new Color(226, 232, 240);
    private static final Color TEXT_MAIN  = new Color(15, 23, 42);
    private static final Color TEXT_SUB   = new Color(100, 116, 139);

    private Docente docente;
    private List<Estudiante> estudiantes;
    private JPanel panelEntregas;
    private JLabel lblConfirmacion;

    public RevisionEntregasPanel(Docente docente, List<Estudiante> estudiantes) {
        this.docente = docente;
        this.estudiantes = estudiantes;
        setBackground(BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(24, 24, 24, 24));
        buildUI();
    }

    private void buildUI() {
        JLabel lblHeader = new JLabel("📝  Revisión de Entregas");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeader.setForeground(TEXT_MAIN);
        lblHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(lblHeader, BorderLayout.NORTH);

        panelEntregas = new JPanel();
        panelEntregas.setLayout(new BoxLayout(panelEntregas, BoxLayout.Y_AXIS));
        panelEntregas.setBackground(BG);

        JScrollPane scroll = new JScrollPane(panelEntregas);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        add(scroll, BorderLayout.CENTER);

        cargarEntregas();
    }

    public void cargarEntregas() {
        panelEntregas.removeAll();

        boolean hayEntregas = false;

        for (Estudiante est : estudiantes) {
            for (Entrega entrega : est.getEntregas()) {
                hayEntregas = true;
                panelEntregas.add(buildCardEntrega(entrega, est));
                panelEntregas.add(Box.createVerticalStrut(16));
            }
        }

        if (!hayEntregas) {
            panelEntregas.add(buildMensajeVacio("📭", "No hay entregas pendientes de revisión."));
        }

        panelEntregas.revalidate();
        panelEntregas.repaint();
    }

    private JPanel buildCardEntrega(Entrega entrega, Estudiante est) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(18, 20, 18, 20)));
        card.setAlignmentX(LEFT_ALIGNMENT);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CARD);

        JPanel infoIzq = new JPanel();
        infoIzq.setLayout(new BoxLayout(infoIzq, BoxLayout.Y_AXIS));
        infoIzq.setBackground(CARD);

        JLabel lblEstudiante = new JLabel("👤 " + est.getNombre() + " " + est.getApellido());
        lblEstudiante.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblEstudiante.setForeground(TEXT_MAIN);

        JLabel lblTarea = new JLabel("📋 " + entrega.getTarea().getTitulo()
                + "  •  " + entrega.getTarea().getMateria());
        lblTarea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTarea.setForeground(TEXT_SUB);

        infoIzq.add(lblEstudiante);
        infoIzq.add(Box.createVerticalStrut(3));
        infoIzq.add(lblTarea);
        header.add(infoIzq, BorderLayout.CENTER);

        boolean aTime = entrega.getEstado();
        JLabel badge = new JLabel(aTime ? "✅ A tiempo" : "⚠ Fuera de plazo");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badge.setForeground(aTime ? SUCCESS : WARN_COL);
        badge.setBackground(aTime ? SUCCESS_BG : WARN_BG);
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(5, 12, 5, 12));
        header.add(badge, BorderLayout.EAST);

        card.add(header);
        card.add(Box.createVerticalStrut(12));

        if (entrega.getFechaEntrega() != null) {
            Fecha fe = entrega.getFechaEntrega();
            JLabel lblFecha = new JLabel(String.format("🕐 Entregado el: %02d/%02d/%04d %02d:%02d",
                    fe.getDia(), fe.getMes(), fe.getAnio(), fe.getHora(), fe.getMinuto()));
            lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblFecha.setForeground(TEXT_SUB);
            lblFecha.setAlignmentX(LEFT_ALIGNMENT);
            card.add(lblFecha);
            card.add(Box.createVerticalStrut(8));
        }

        Archivo arch = entrega.getArchivo();
        if (arch != null) {
            JPanel archivoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            archivoPanel.setBackground(new Color(239, 246, 255));
            archivoPanel.setBorder(new CompoundBorder(
                    new LineBorder(new Color(191, 219, 254), 1, true),
                    new EmptyBorder(6, 12, 6, 12)));
            archivoPanel.setAlignmentX(LEFT_ALIGNMENT);
            archivoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            JLabel lblArch = new JLabel("📎  " + arch.getNombre()
                    + "  (" + String.format("%.1f", arch.getTamanio()) + " KB)");
            lblArch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblArch.setForeground(PRIMARY);
            archivoPanel.add(lblArch);
            card.add(archivoPanel);
            card.add(Box.createVerticalStrut(14));
        }

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COL);
        sep.setAlignmentX(LEFT_ALIGNMENT);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        card.add(sep);
        card.add(Box.createVerticalStrut(12));

        if (entrega.getCalificacion() != null) {
            JPanel yaCalif = new JPanel(new FlowLayout(FlowLayout.LEFT));
            yaCalif.setBackground(SUCCESS_BG);
            yaCalif.setBorder(new CompoundBorder(
                    new LineBorder(SUCCESS, 1, true),
                    new EmptyBorder(6, 12, 6, 12)));
            yaCalif.setAlignmentX(LEFT_ALIGNMENT);
            yaCalif.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            JLabel lbl = new JLabel("✅ Ya calificada — Nota: "
                    + entrega.getCalificacion().obtenerNota()
                    + "  |  " + entrega.getCalificacion().obtenerComentario());
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lbl.setForeground(SUCCESS);
            yaCalif.add(lbl);
            card.add(yaCalif);
            return card;
        }

        JPanel filaNota = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filaNota.setBackground(CARD);
        filaNota.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblNota = new JLabel("Calificación (0–100):");
        lblNota.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNota.setForeground(TEXT_MAIN);

        JTextField txtNota = new JTextField();
        txtNota.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNota.setPreferredSize(new Dimension(100, 36));
        txtNota.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(4, 8, 4, 8)));

        filaNota.add(lblNota);
        filaNota.add(txtNota);
        card.add(filaNota);
        card.add(Box.createVerticalStrut(10));

        JLabel lblCom = new JLabel("Comentario al estudiante:");
        lblCom.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCom.setForeground(TEXT_MAIN);
        lblCom.setAlignmentX(LEFT_ALIGNMENT);
        card.add(lblCom);
        card.add(Box.createVerticalStrut(5));

        JTextArea txtComentario = new JTextArea(3, 0);
        txtComentario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtComentario.setLineWrap(true);
        txtComentario.setWrapStyleWord(true);
        txtComentario.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(6, 8, 6, 8)));
        JScrollPane scrollCom = new JScrollPane(txtComentario);
        scrollCom.setBorder(null);
        scrollCom.setAlignmentX(LEFT_ALIGNMENT);
        scrollCom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.add(scrollCom);
        card.add(Box.createVerticalStrut(12));

        JLabel lblMsg = new JLabel();
        lblMsg.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMsg.setAlignmentX(LEFT_ALIGNMENT);
        lblMsg.setVisible(false);
        card.add(lblMsg);
        card.add(Box.createVerticalStrut(6));
        
        JButton btnGuardar = buildBoton("💾  Guardar calificación", PRIMARY, new Color(29, 78, 216));
        btnGuardar.addActionListener(e -> {
            String notaStr = txtNota.getText().trim();
            if (notaStr.isEmpty()) {
                mostrarMsg(lblMsg, "⚠  Ingresa una calificación.", WARN_COL);
                return;
            }
            double nota;
            try {
                nota = Double.parseDouble(notaStr);
                if (nota < 0 || nota > 100) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                mostrarMsg(lblMsg, "⚠  La nota debe ser un número entre 0 y 100.", DANGER);
                return;
            }
            String comentario = txtComentario.getText().trim();
            if (comentario.isEmpty()) comentario = "Sin comentarios.";

            docente.asignarCalificacion(entrega, nota, comentario);

            mostrarMsg(lblMsg, "✅  Calificación guardada correctamente.", SUCCESS);
            txtNota.setEnabled(false);
            txtComentario.setEnabled(false);
            btnGuardar.setEnabled(false);

            est.actualizarPromedioSiHayNuevaNota();
        });
        card.add(btnGuardar);

        return card;
    }

    private void mostrarMsg(JLabel lbl, String msg, Color color) {
        lbl.setText(msg);
        lbl.setForeground(color);
        lbl.setVisible(true);
    }

    private JPanel buildMensajeVacio(String ico, String msg) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG);
        p.setPreferredSize(new Dimension(0, 200));

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(BG);

        JLabel icoLbl = new JLabel(ico);
        icoLbl.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        icoLbl.setAlignmentX(CENTER_ALIGNMENT);

        JLabel msgLbl = new JLabel(msg);
        msgLbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        msgLbl.setForeground(TEXT_SUB);
        msgLbl.setAlignmentX(CENTER_ALIGNMENT);

        inner.add(icoLbl);
        inner.add(Box.createVerticalStrut(10));
        inner.add(msgLbl);
        p.add(inner);
        return p;
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
        btn.setMaximumSize(new Dimension(240, 44));
        return btn;
    }
}
