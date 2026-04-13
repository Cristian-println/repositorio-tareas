package vista;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Constantes y métodos de fábrica para el estilo visual de la aplicación.
 * Responsable: Danner (T38)
 */
public class Estilos {

    // ── Colores ───────────────────────────────────────────────────────
    public static final Color COLOR_PRIMARIO   = new Color(21, 101, 192);
    public static final Color COLOR_CABECERA   = new Color(13,  71, 161);
    public static final Color COLOR_EXITO      = new Color(46, 125,  50);
    public static final Color COLOR_ERROR      = new Color(198, 40,  40);
    public static final Color COLOR_ADVERTENCIA= new Color(230, 81,   0);
    public static final Color COLOR_TARDIO     = new Color(245,124,   0);
    public static final Color COLOR_FONDO      = new Color(240, 242, 245);
    public static final Color COLOR_PANEL      = Color.WHITE;
    public static final Color COLOR_TEXTO      = new Color( 33,  33,  33);
    public static final Color COLOR_TEXTO_GRIS = new Color( 97,  97,  97);
    public static final Color COLOR_BORDE      = new Color(207, 216, 220);
    public static final Color COLOR_TABLA_PAR  = new Color(232, 240, 254);
    public static final Color COLOR_SELECCION  = new Color(187, 222, 251);

    // ── Fuentes ───────────────────────────────────────────────────────
    public static final Font FUENTE_TITULO    = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FUENTE_SECCION   = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FUENTE_NEGRITA   = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FUENTE_NORMAL    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FUENTE_PEQUEÑA   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FUENTE_PROMEDIO  = new Font("Segoe UI", Font.BOLD, 48);

    // ── Botones ───────────────────────────────────────────────────────
    public static JButton botonPrimario(String texto) {
        return estilizarBoton(new JButton(texto), COLOR_PRIMARIO, Color.WHITE, 160, 36);
    }

    public static JButton botonExito(String texto) {
        return estilizarBoton(new JButton(texto), COLOR_EXITO, Color.WHITE, 160, 36);
    }

    public static JButton botonSecundario(String texto) {
        return estilizarBoton(new JButton(texto), new Color(224, 224, 224), COLOR_TEXTO, 120, 36);
    }

    public static JButton botonPeligro(String texto) {
        return estilizarBoton(new JButton(texto), new Color(211, 47, 47), Color.WHITE, 140, 36);
    }

    private static JButton estilizarBoton(JButton btn, Color bg, Color fg, int w, int h) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(FUENTE_NEGRITA);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }

    // ── Campos de texto ───────────────────────────────────────────────
    public static JTextField campo(int columnas) {
        JTextField t = new JTextField(columnas);
        t.setFont(FUENTE_NORMAL);
        t.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDE),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return t;
    }

    public static JTextArea areaTexto(int filas, int cols) {
        JTextArea ta = new JTextArea(filas, cols);
        ta.setFont(FUENTE_NORMAL);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        return ta;
    }

    // ── Etiquetas ─────────────────────────────────────────────────────
    public static JLabel etiquetaTitulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FUENTE_TITULO);
        l.setForeground(COLOR_PRIMARIO);
        return l;
    }

    public static JLabel etiquetaSeccion(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FUENTE_SECCION);
        l.setForeground(COLOR_PRIMARIO);
        return l;
    }

    public static JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FUENTE_NEGRITA);
        l.setForeground(COLOR_TEXTO);
        return l;
    }

    public static JLabel etiquetaGris(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FUENTE_NORMAL);
        l.setForeground(COLOR_TEXTO_GRIS);
        return l;
    }

    // ── Paneles ───────────────────────────────────────────────────────
    public static JPanel panelConTitulo(String titulo) {
        JPanel p = new JPanel();
        p.setBackground(COLOR_PANEL);
        p.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_BORDE),
            titulo,
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            FUENTE_NEGRITA, COLOR_PRIMARIO));
        return p;
    }

    public static JPanel panelHeader(String texto) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(COLOR_FONDO);
        p.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));
        p.add(etiquetaTitulo(texto));
        return p;
    }

    /** Tabla con colores alternados y fuente correcta. */
    public static void estilizarTabla(JTable tabla) {
        tabla.setFont(FUENTE_NORMAL);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setFont(FUENTE_NEGRITA);
        tabla.getTableHeader().setBackground(COLOR_PRIMARIO);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setSelectionBackground(COLOR_SELECCION);
        tabla.setSelectionForeground(COLOR_TEXTO);
        tabla.setGridColor(COLOR_BORDE);
        tabla.setShowGrid(true);
        tabla.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) setBackground(row % 2 == 0 ? Color.WHITE : COLOR_TABLA_PAR);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
    }

    public static JScrollPane scrollPane(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(BorderFactory.createLineBorder(COLOR_BORDE));
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    public static Border paddingBorde(int v, int h) {
        return BorderFactory.createEmptyBorder(v, h, v, h);
    }
}
