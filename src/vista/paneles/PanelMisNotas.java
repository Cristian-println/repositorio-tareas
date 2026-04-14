package vista.paneles;

import controlador.CalificacionControlador;
import modelo.Entrega;
import modelo.dao.EntregaDAO;
import vista.Estilos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelMisNotas extends JPanel {

    private int estudianteId = -1;

    private JLabel          lblPromedio;
    private JLabel          lblPromedioTexto;
    private JLabel          lblContador;
    private JLabel          lblLetra;

    private DefaultTableModel modeloTabla;
    private JTable            tabla;

    private final EntregaDAO            entregaDAO = new EntregaDAO();
    private final CalificacionControlador calCtrl  = new CalificacionControlador();

    public PanelMisNotas() {
        setLayout(new BorderLayout());
        setBackground(Estilos.COLOR_FONDO);
        construirUI();
    }

    private void construirUI() {
        add(Estilos.panelHeader("   Mis Notas y Promedio"), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            crearPanelResumen(), crearPanelTabla());
        split.setDividerLocation(260);
        split.setDividerSize(6);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    private JPanel crearPanelResumen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Estilos.COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 6));

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Estilos.COLOR_BORDE),
            BorderFactory.createEmptyBorder(30, 20, 30, 20)));

        JLabel lblTitCard = Estilos.etiquetaSeccion("Mi Promedio");
        lblTitCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblPromedio = new JLabel("—", SwingConstants.CENTER);
        lblPromedio.setFont(Estilos.FUENTE_PROMEDIO);
        lblPromedio.setForeground(Estilos.COLOR_PRIMARIO);
        lblPromedio.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblPromedioTexto = new JLabel("Sin calificaciones aún", SwingConstants.CENTER);
        lblPromedioTexto.setFont(Estilos.FUENTE_PEQUEÑA);
        lblPromedioTexto.setForeground(Estilos.COLOR_TEXTO_GRIS);
        lblPromedioTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblLetra = new JLabel(" ", SwingConstants.CENTER);
        lblLetra.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblLetra.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(180, 2));

        lblContador = new JLabel("0 tarea(s) calificada(s)", SwingConstants.CENTER);
        lblContador.setFont(Estilos.FUENTE_PEQUEÑA);
        lblContador.setForeground(Estilos.COLOR_TEXTO_GRIS);
        lblContador.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnRefrescar = Estilos.botonSecundario("  Actualizar");
        btnRefrescar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRefrescar.addActionListener(e -> cargarDatos(estudianteId));

        card.add(lblTitCard);
        card.add(Box.createVerticalStrut(20));
        card.add(lblPromedio);
        card.add(Box.createVerticalStrut(6));
        card.add(lblPromedioTexto);
        card.add(Box.createVerticalStrut(16));
        card.add(lblLetra);
        card.add(Box.createVerticalStrut(12));
        card.add(sep);
        card.add(Box.createVerticalStrut(12));
        card.add(lblContador);
        card.add(Box.createVerticalStrut(24));
        card.add(btnRefrescar);

        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Estilos.COLOR_FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 6, 8, 12));

        String[] cols = {"Tarea", "Materia", "Entregado", "Tardía", "Nota", "Nota máx.", "Comentario docente"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        Estilos.estilizarTabla(tabla);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(180);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(110);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(60);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(60);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(220);

        JPanel panelTabla = Estilos.panelConTitulo("Detalle de tareas calificadas");
        panelTabla.setLayout(new BorderLayout());
        panelTabla.add(Estilos.scrollPane(tabla), BorderLayout.CENTER);

        JLabel leyenda = Estilos.etiquetaGris(
            "     Solo se muestran las tareas que el docente ha calificado");
        leyenda.setFont(Estilos.FUENTE_PEQUEÑA);
        leyenda.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        panelTabla.add(leyenda, BorderLayout.SOUTH);

        panel.add(panelTabla, BorderLayout.CENTER);
        return panel;
    }

    public void cargarDatos(int estudianteId) {
        this.estudianteId = estudianteId;
        modeloTabla.setRowCount(0);

        List<Entrega> entregas = entregaDAO.obtenerPorEstudiante(estudianteId);
        int calificadas = 0;

        for (Entrega e : entregas) {
            if (e.getNota() == null) continue; 
            calificadas++;
            modeloTabla.addRow(new Object[]{
                e.getTareaTitulo(),
                "(ver tarea)",
                e.getFechaEntregaFormateada(),
                e.isEsTardio() ? "Sí" : "No",
                String.format("%.2f", e.getNota()),
                String.format("%.2f", e.getCalificacionMaxima()),
                e.getComentarioDocente() != null ? e.getComentarioDocente() : ""
            });
        }

        double promedio = calCtrl.obtenerPromedio(estudianteId);
        int count = calCtrl.contarCalificadas(estudianteId);

        if (promedio < 0) {
            lblPromedio.setText("—");
            lblPromedio.setForeground(Estilos.COLOR_TEXTO_GRIS);
            lblPromedioTexto.setText("Sin calificaciones aún");
            lblLetra.setText(" ");
            lblContador.setText("0 tarea(s) calificada(s)");
        } else {
            lblPromedio.setText(String.format("%.1f", promedio));
            lblPromedio.setForeground(colorPorNota(promedio, 100));
            lblPromedioTexto.setText("promedio general");
            lblLetra.setText(letraEquivalente(promedio));
            lblLetra.setForeground(colorPorNota(promedio, 100));
            lblContador.setText(count + " tarea(s) calificada(s)");
        }
    }

    private Color colorPorNota(double nota, double max) {
        double pct = nota / max * 100;
        if (pct >= 70) return Estilos.COLOR_EXITO;
        if (pct >= 50) return Estilos.COLOR_TARDIO;
        return Estilos.COLOR_ERROR;
    }

    private String letraEquivalente(double nota) {
        double pct = nota;         
        if (pct >= 90) return "A";
        if (pct >= 80) return "B";
        if (pct >= 70) return "C";
        if (pct >= 60) return "D";
        return "F";
    }
}
