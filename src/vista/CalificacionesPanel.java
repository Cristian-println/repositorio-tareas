package vista;

import modelo.Entrega;
import modelo.Estudiante;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class CalificacionesPanel extends JPanel {

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

    public CalificacionesPanel(Estudiante estudiante) {
        this.estudiante = estudiante;
        setBackground(BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(24, 24, 24, 24));
        buildUI();
    }

    private void buildUI() {
        JLabel lblHeader = new JLabel("🎓  Calificaciones");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHeader.setForeground(TEXT_MAIN);
        lblHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(lblHeader, BorderLayout.NORTH);

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(BG);

        List<Entrega> calificadas = estudiante.obtenerEntregasCalificadas();

        double promedio = estudiante.calcularPromedio();

        contenido.add(buildCardPromedio(promedio, calificadas.size()));
        contenido.add(Box.createVerticalStrut(20));

        if (calificadas.isEmpty()) {
            contenido.add(buildCardVacia());
        } else {
      
            JLabel lblDet = new JLabel("Detalle por entrega");
            lblDet.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lblDet.setForeground(TEXT_MAIN);
            lblDet.setAlignmentX(LEFT_ALIGNMENT);
            contenido.add(lblDet);
            contenido.add(Box.createVerticalStrut(10));

            for (Entrega e : calificadas) {
                contenido.add(buildFilaEntrega(e));
                contenido.add(Box.createVerticalStrut(8));
            }

            contenido.add(Box.createVerticalStrut(16));
            JLabel lblMat = new JLabel("Promedio por materia");
            lblMat.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lblMat.setForeground(TEXT_MAIN);
            lblMat.setAlignmentX(LEFT_ALIGNMENT);
            contenido.add(lblMat);
            contenido.add(Box.createVerticalStrut(10));

            Map<String, Double> promediosPorMateria = calificadas.stream()
                    .filter(e -> e.getCalificacion() != null)
                    .collect(Collectors.groupingBy(
                            e -> e.getTarea().getMateria(),
                            Collectors.averagingDouble(e -> estudiante.obtenerNota(e))
                    ));

            promediosPorMateria.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        contenido.add(buildFilaMateria(entry.getKey(), entry.getValue()));
                        contenido.add(Box.createVerticalStrut(6));
                    });
        }

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildCardPromedio(double promedio, int totalCalificadas) {
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

        JLabel lblSub = new JLabel("Promedio general  •  " + totalCalificadas + " entrega(s) calificada(s)");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(TEXT_SUB);
        lblSub.setAlignmentX(LEFT_ALIGNMENT);

        izq.add(lblProm);
        izq.add(lblSub);
        card.add(izq, BorderLayout.CENTER);

        if (promedio > 0) {
            JPanel circWrap = new JPanel(new GridBagLayout());
            circWrap.setBackground(CARD);
            circWrap.add(buildCirculo(promedio));
            card.add(circWrap, BorderLayout.EAST);
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

    private JPanel buildCardVacia() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(32, 24, 32, 24)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        card.setAlignmentX(LEFT_ALIGNMENT);

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(CARD);

        JLabel ico = new JLabel("📭");
        ico.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        ico.setAlignmentX(CENTER_ALIGNMENT);

        JLabel msg = new JLabel("Aún no tienes calificaciones registradas.");
        msg.setFont(new Font("Segoe UI", Font.BOLD, 15));
        msg.setForeground(TEXT_MAIN);
        msg.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Cuando el docente califique tus entregas, aparecerán aquí.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(TEXT_SUB);
        sub.setAlignmentX(CENTER_ALIGNMENT);

        inner.add(ico);
        inner.add(Box.createVerticalStrut(12));
        inner.add(msg);
        inner.add(Box.createVerticalStrut(4));
        inner.add(sub);
        card.add(inner);
        return card;
    }


    private JPanel buildFilaEntrega(Entrega e) {
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

        JLabel materia = new JLabel(e.getTarea().getMateria());
        materia.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        materia.setForeground(TEXT_SUB);

        izq.add(titulo);
        izq.add(Box.createVerticalStrut(3));
        izq.add(materia);
        fila.add(izq, BorderLayout.CENTER);

        double nota = estudiante.obtenerNota(e);
        JLabel lblNota = new JLabel(String.format("%.1f", nota));
        lblNota.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblNota.setForeground(colorNota(nota));
        fila.add(lblNota, BorderLayout.EAST);

        return fila;
    }

    private JPanel buildFilaMateria(String materia, double promedio) {
        JPanel fila = new JPanel(new BorderLayout(12, 0));
        fila.setBackground(new Color(249, 250, 251));
        fila.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COL, 1, true),
                new EmptyBorder(10, 16, 10, 16)));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        fila.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblMat = new JLabel("📚 " + materia);
        lblMat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMat.setForeground(TEXT_MAIN);

        JLabel lblProm = new JLabel(String.format("%.1f", promedio));
        lblProm.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblProm.setForeground(colorNota(promedio));

        fila.add(lblMat, BorderLayout.CENTER);
        fila.add(lblProm, BorderLayout.EAST);
        return fila;
    }

    private Color colorNota(double nota) {
        if (nota >= 90) return SUCCESS;
        if (nota >= 70) return PRIMARY;
        if (nota >= 51) return WARN_COL;
        return DANGER;
    }
}
