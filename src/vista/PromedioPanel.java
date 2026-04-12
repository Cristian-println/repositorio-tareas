package vista;

import modelo.Calificacion;
import modelo.Entrega;
import modelo.Estudiante;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class PromedioPanel extends JPanel {

    private static final Color PRIMARY    = new Color(37, 99, 235);
    private static final Color SUCCESS    = new Color(34, 197, 94);
    private static final Color WARN_COL   = new Color(245, 158, 11);
    private static final Color DANGER     = new Color(239, 68, 68);
    private static final Color BG         = new Color(248, 250, 252);
    private static final Color CARD       = Color.WHITE;
    private static final Color BORDER_COL = new Color(226, 232, 240);
    private static final Color TEXT_MAIN  = new Color(15, 23, 42);
    private static final Color TEXT_SUB   = new Color(100, 116, 139);

    private Estudiante estudiante;
    private JPanel panelContenido;
    private Timer timerActualizacion;

    public PromedioPanel(Estudiante estudiante) {
        this.estudiante = estudiante;
        setBackground(BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(24, 24, 24, 24));
        buildUI();
        iniciarActualizacionAutomatica();
    }

    private void buildUI() {
        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setBackground(BG);
        headerRow.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel lblHeader = new JLabel("📊  Promedio y Notas");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeader.setForeground(TEXT_MAIN);
        headerRow.add(lblHeader, BorderLayout.WEST);

        JButton btnActualizar = new JButton("🔄  Actualizar");
        btnActualizar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnActualizar.setForeground(PRIMARY);
        btnActualizar.setBackground(new Color(239, 246, 255));
        btnActualizar.setBorder(new CompoundBorder(
                new LineBorder(new Color(191, 219, 254), 1, true),
                new EmptyBorder(6, 12, 6, 12)));
        btnActualizar.setFocusPainted(false);
        btnActualizar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnActualizar.addActionListener(e -> actualizarVista());
        headerRow.add(btnActualizar, BorderLayout.EAST);

        add(headerRow, BorderLayout.NORTH);

        panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(BG);

        JScrollPane scroll = new JScrollPane(panelContenido);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        add(scroll, BorderLayout.CENTER);

        actualizarVista();
    }

    private void iniciarActualizacionAutomatica() {
    	
        timerActualizacion = new Timer(5000, e -> {
            double resultado = estudiante.actualizarPromedioSiHayNuevaNota();
            if (resultado >= 0) {
                actualizarVista();
            }
        });
        timerActualizacion.start();
    }

    public void actualizarVista() {
        panelContenido.removeAll();

        double promedio = estudiante.calcularPromedio();
        List<Entrega> calificadas = estudiante.obtenerEntregasCalificadas();

        panelContenido.add(buildCardPromedio(promedio, calificadas.size()));
        panelContenido.add(Box.createVerticalStrut(20));

        if (calificadas.isEmpty()) {
            panelContenido.add(buildMensajeVacio());
        } else {
            
            JLabel lblDetalle = new JLabel("Historial de calificaciones");
            lblDetalle.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lblDetalle.setForeground(TEXT_MAIN);
            lblDetalle.setAlignmentX(LEFT_ALIGNMENT);
            lblDetalle.setBorder(new EmptyBorder(0, 0, 10, 0));
            panelContenido.add(lblDetalle);

            for (Entrega e : calificadas) {
                panelContenido.add(buildFilaCalificacion(e));
                panelContenido.add(Box.createVerticalStrut(8));
            }

            panelContenido.add(Box.createVerticalStrut(12));
            JLabel lblActualizado = new JLabel("🕐 Vista actualizada automáticamente cada 5 segundos.");
            lblActualizado.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lblActualizado.setForeground(TEXT_SUB);
            lblActualizado.setAlignmentX(LEFT_ALIGNMENT);
            panelContenido.add(lblActualizado);
        }

        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private JPanel buildCardPromedio(double promedio, int totalCalif) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(24, 28, 24, 28)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setAlignmentX(LEFT_ALIGNMENT);

        JPanel izq = new JPanel();
        izq.setLayout(new BoxLayout(izq, BoxLayout.Y_AXIS));
        izq.setBackground(CARD);
        
        String promedioStr = promedio <= 0 ? "—" : String.format("%.1f", promedio);
        JLabel lblProm = new JLabel(promedioStr);
        lblProm.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblProm.setForeground(colorNota(promedio));
        lblProm.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Promedio general  •  " + totalCalif + " calificación(es)");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(TEXT_SUB);
        lblSub.setAlignmentX(LEFT_ALIGNMENT);

        izq.add(lblProm);
        izq.add(lblSub);
        card.add(izq, BorderLayout.CENTER);

        if (promedio > 0) {
            card.add(buildCirculo(promedio), BorderLayout.EAST);
        }
        return card;
    }

    private JPanel buildCirculo(double promedio) {
        double pct = Math.min(promedio / 100.0, 1.0);
        JPanel circ = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                int r = Math.min(w, h) - 10;
                int x = (w - r) / 2, y = (h - r) / 2;
                g2.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(new Color(226, 232, 240));
                g2.drawArc(x, y, r, r, 90, -360);
                g2.setColor(colorNota(promedio));
                g2.drawArc(x, y, r, r, 90, (int)(-360 * pct));
                String txt = (int) promedio + "%";
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                g2.setColor(TEXT_MAIN);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(txt, w / 2 - fm.stringWidth(txt) / 2, h / 2 + fm.getAscent() / 2 - 2);
                g2.dispose();
            }
        };
        circ.setBackground(CARD);
        circ.setPreferredSize(new Dimension(90, 90));
        return circ;
    }

    private JPanel buildMensajeVacio() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(32, 24, 32, 24)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        card.setAlignmentX(LEFT_ALIGNMENT);

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(CARD);

        JLabel ico = new JLabel("📊");
        ico.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        ico.setAlignmentX(CENTER_ALIGNMENT);

        JLabel msg = new JLabel("No hay notas disponibles aún.");
        msg.setFont(new Font("Segoe UI", Font.BOLD, 15));
        msg.setForeground(TEXT_MAIN);
        msg.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Esta vista se actualizará automáticamente cuando el docente registre calificaciones.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(TEXT_SUB);
        sub.setAlignmentX(CENTER_ALIGNMENT);

        inner.add(ico);
        inner.add(Box.createVerticalStrut(12));
        inner.add(msg);
        inner.add(Box.createVerticalStrut(6));
        inner.add(sub);
        card.add(inner);
        return card;
    }

    private JPanel buildFilaCalificacion(Entrega e) {
        JPanel fila = new JPanel(new BorderLayout(12, 0));
        fila.setBackground(CARD);
        fila.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(12, 16, 12, 16)));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        fila.setAlignmentX(LEFT_ALIGNMENT);

        JPanel izq = new JPanel();
        izq.setLayout(new BoxLayout(izq, BoxLayout.Y_AXIS));
        izq.setBackground(CARD);

        JLabel titulo = new JLabel(e.getTarea().getTitulo());
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titulo.setForeground(TEXT_MAIN);

        String comentario = "";
        try { comentario = estudiante.obtenerComentario(e); } catch (Exception ex) {}
        JLabel detalle = new JLabel(e.getTarea().getMateria()
                + (comentario.isEmpty() ? "" : "  •  \"" + comentario + "\""));
        detalle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detalle.setForeground(TEXT_SUB);

        izq.add(titulo);
        izq.add(Box.createVerticalStrut(3));
        izq.add(detalle);
        fila.add(izq, BorderLayout.CENTER);

        double nota = estudiante.obtenerNota(e);
        JLabel lblNota = new JLabel(String.format("%.1f", nota));
        lblNota.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblNota.setForeground(colorNota(nota));
        fila.add(lblNota, BorderLayout.EAST);

        return fila;
    }

    public void detenerActualizacion() {
        if (timerActualizacion != null) timerActualizacion.stop();
    }

    private Color colorNota(double nota) {
        if (nota >= 90) return SUCCESS;
        if (nota >= 70) return PRIMARY;
        if (nota >= 51) return WARN_COL;
        return DANGER;
    }
}
