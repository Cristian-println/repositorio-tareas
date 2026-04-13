package vista.paneles;

import controlador.TareaControlador;
import modelo.Entrega;
import modelo.Materia;
import modelo.Tarea;
import modelo.dao.EntregaDAO;
import modelo.dao.MateriaDAO;
import vista.Estilos;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lista de tareas asignadas al estudiante con estado y filtros (HU-6).
 * Responsable: Cristian (T39, T43)
 */
public class PanelMisTareas extends JPanel {

    private int estudianteId = -1;

    private JComboBox<String>   cmbFiltroMateria;
    private JComboBox<String>   cmbFiltroEstado;
    private DefaultTableModel   modeloTabla;
    private JTable              tabla;
    private JLabel              lblDetalle;

    // Datos cargados
    private List<Tarea>   tareasOriginales = new ArrayList<>();
    private List<Entrega> entregasMap      = new ArrayList<>();

    private final TareaControlador  tareaCtrl   = new TareaControlador();
    private final EntregaDAO        entregaDAO  = new EntregaDAO();
    private final MateriaDAO        materiaDAO  = new MateriaDAO();

    public PanelMisTareas() {
        setLayout(new BorderLayout());
        setBackground(Estilos.COLOR_FONDO);
        construirUI();
    }

    private void construirUI() {
        add(Estilos.panelHeader("📝  Mis Tareas Asignadas"), BorderLayout.NORTH);

        // ── Filtros ──
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        filtros.setBackground(Estilos.COLOR_FONDO);
        filtros.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

        filtros.add(Estilos.etiqueta("Filtrar por materia:"));
        cmbFiltroMateria = new JComboBox<>();
        cmbFiltroMateria.setFont(Estilos.FUENTE_NORMAL);
        cmbFiltroMateria.setPreferredSize(new Dimension(200, 30));
        cmbFiltroMateria.addItem("Todas las materias");
        cmbFiltroMateria.addActionListener(e -> aplicarFiltros());
        filtros.add(cmbFiltroMateria);

        filtros.add(Estilos.etiqueta("Estado:"));
        cmbFiltroEstado = new JComboBox<>(new String[]{
            "Todos", "Pendiente", "Entregada", "Calificada", "Vencida"
        });
        cmbFiltroEstado.setFont(Estilos.FUENTE_NORMAL);
        cmbFiltroEstado.setPreferredSize(new Dimension(140, 30));
        cmbFiltroEstado.addActionListener(e -> aplicarFiltros());
        filtros.add(cmbFiltroEstado);

        JButton btnRefrescar = Estilos.botonSecundario("↺ Actualizar");
        btnRefrescar.addActionListener(e -> cargarDatos(estudianteId));
        filtros.add(btnRefrescar);

        add(filtros, BorderLayout.NORTH);

        // ── Tabla ──
        String[] cols = {"Título", "Materia", "Docente", "Fecha límite", "Estado", "Nota"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        Estilos.estilizarTabla(tabla);
        tabla.setDefaultRenderer(Object.class, new EstadoCellRenderer());
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) mostrarDetalle();
        });

        // Anchos de columna
        tabla.getColumnModel().getColumn(0).setPreferredWidth(220);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(140);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(80);

        // ── Detalle ──
        lblDetalle = new JLabel(" ");
        lblDetalle.setFont(Estilos.FUENTE_PEQUEÑA);
        lblDetalle.setForeground(Estilos.COLOR_TEXTO_GRIS);
        lblDetalle.setBorder(BorderFactory.createEmptyBorder(4, 16, 4, 16));

        JPanel centro = new JPanel(new BorderLayout());
        centro.setBackground(Estilos.COLOR_FONDO);
        centro.setBorder(BorderFactory.createEmptyBorder(4, 12, 8, 12));
        centro.add(Estilos.scrollPane(tabla), BorderLayout.CENTER);
        centro.add(lblDetalle, BorderLayout.SOUTH);

        // Reemplazo del norte después de agregar filtros
        remove(filtros);
        JPanel norte = new JPanel(new BorderLayout());
        norte.setBackground(Estilos.COLOR_FONDO);
        norte.add(Estilos.panelHeader("📝  Mis Tareas Asignadas"), BorderLayout.NORTH);
        norte.add(filtros, BorderLayout.SOUTH);
        add(norte, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
    }

    public void cargarDatos(int estudianteId) {
        this.estudianteId = estudianteId;

        // Recargar materias en filtro
        cmbFiltroMateria.removeAllItems();
        cmbFiltroMateria.addItem("Todas las materias");
        List<Materia> materias = materiaDAO.obtenerPorEstudiante(estudianteId);
        for (Materia m : materias) cmbFiltroMateria.addItem(m.getNombre());

        // Cargar tareas
        tareasOriginales = tareaCtrl.obtenerTareasEstudiante(estudianteId);
        entregasMap      = entregaDAO.obtenerPorEstudiante(estudianteId);

        aplicarFiltros();
    }

    private void aplicarFiltros() {
        if (tareasOriginales == null) return;

        String filtroMateria = (String) cmbFiltroMateria.getSelectedItem();
        String filtroEstado  = (String) cmbFiltroEstado.getSelectedItem();

        modeloTabla.setRowCount(0);
        int count = 0;

        for (Tarea t : tareasOriginales) {
            // Filtro materia
            if (filtroMateria != null && !filtroMateria.equals("Todas las materias")
                && !t.getMateriaNombre().equals(filtroMateria)) continue;

            // Calcular estado
            Entrega entrega = buscarEntrega(t.getId());
            String estado = calcularEstado(t, entrega);
            String nota   = calcularNota(entrega, t);

            // Filtro estado
            if (filtroEstado != null && !filtroEstado.equals("Todos")) {
                if (!estado.contains(filtroEstado)) continue;
            }

            modeloTabla.addRow(new Object[]{
                t.getTitulo(),
                t.getMateriaNombre(),
                t.getDocenteNombre(),
                t.getFechaLimiteFormateada(),
                estado,
                nota
            });
            count++;
        }

        lblDetalle.setText("Mostrando " + count + " tarea(s)  |  Total asignadas: " + tareasOriginales.size());
    }

    private Entrega buscarEntrega(int tareaId) {
        for (Entrega e : entregasMap)
            if (e.getTareaId() == tareaId) return e;
        return null;
    }

    private String calcularEstado(Tarea t, Entrega e) {
        if (e == null) return t.estaVencida() ? "Vencida" : "Pendiente";
        if (e.getNota() != null) return "Calificada";
        return e.isEsTardio() ? "Entregada (tardía)" : "Entregada";
    }

    private String calcularNota(Entrega e, Tarea t) {
        if (e == null || e.getNota() == null) return "—";
        return String.format("%.1f / %.1f", e.getNota(), t.getCalificacionMaxima());
    }

    private void mostrarDetalle() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        String titulo  = (String) modeloTabla.getValueAt(fila, 0);
        String materia = (String) modeloTabla.getValueAt(fila, 1);
        String estado  = (String) modeloTabla.getValueAt(fila, 4);
        String nota    = (String) modeloTabla.getValueAt(fila, 5);
        lblDetalle.setText(String.format("📋 \"%s\"  |  %s  |  Estado: %s  |  Nota: %s",
            titulo, materia, estado, nota));
    }

    // ── Renderer de colores por estado ───────────────────────────────
    private static class EstadoCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v,
                boolean sel, boolean foc, int row, int col) {
            super.getTableCellRendererComponent(t, v, sel, foc, row, col);
            setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
            if (!sel) {
                String estado = (String) t.getValueAt(row, 4);
                if (estado == null) return this;
                if (estado.startsWith("Calificada")) {
                    setBackground(new Color(232, 245, 233));
                } else if (estado.startsWith("Vencida")) {
                    setBackground(new Color(255, 235, 238));
                } else if (estado.startsWith("Entregada")) {
                    setBackground(new Color(255, 248, 225));
                } else {
                    setBackground(row % 2 == 0 ? Color.WHITE : Estilos.COLOR_TABLA_PAR);
                }
            }
            return this;
        }
    }
}
