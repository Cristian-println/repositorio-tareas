package vista;

import modelo.Docente;
import modelo.Estudiante;
import modelo.Materia;
import modelo.dao.DocenteDAO;
import modelo.dao.EstudianteDAO;
import modelo.dao.MateriaDAO;
import vista.paneles.PanelBuscarEstudiante;
import vista.paneles.PanelCalificarTareas;
import vista.paneles.PanelCrearTarea;

import javax.swing.*;
import java.awt.*;

public class PanelDocente extends JPanel {

    private int docenteId = -1;

    private final PanelCrearTarea       panelCrearTarea;
    private final PanelCalificarTareas  panelCalificar;
    private final PanelBuscarEstudiante panelBuscar;
    private final JPanel                panelAdmin;
    private final JTabbedPane           tabs;

    public PanelDocente() {
        setLayout(new BorderLayout());
        setBackground(Estilos.COLOR_FONDO);

        panelCrearTarea = new PanelCrearTarea();
        panelCalificar  = new PanelCalificarTareas();
        panelBuscar     = new PanelBuscarEstudiante();
        panelAdmin      = crearPanelAdministracion();

        tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setFont(Estilos.FUENTE_NEGRITA);
        tabs.setBackground(Estilos.COLOR_FONDO);

        tabs.addTab("     Crear Tarea  ",         panelCrearTarea);
        tabs.addTab("     Calificar Tareas  ",    panelCalificar);
        tabs.addTab("     Buscar Estudiante  ",   panelBuscar);
        tabs.addTab("     Administración  ",       panelAdmin);

        tabs.addChangeListener(e -> {
            if (docenteId < 1) return;
            switch (tabs.getSelectedIndex()) {
                case 0 -> panelCrearTarea.setDocenteId(docenteId);
                case 1 -> panelCalificar.cargarDatos(docenteId);
                case 2 -> panelBuscar.cargarDatos();
            }
        });

        add(tabs, BorderLayout.CENTER);
    }

    public void cargarDatos(int docenteId) {
        this.docenteId = docenteId;
        tabs.setSelectedIndex(0);
        panelCrearTarea.setDocenteId(docenteId);
        panelCalificar.cargarDatos(docenteId);
        panelBuscar.cargarDatos();
    }

    private JPanel crearPanelAdministracion() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Estilos.COLOR_FONDO);
        panel.add(Estilos.panelHeader("   Administración del Sistema"), BorderLayout.NORTH);

        JPanel contenido = new JPanel(new GridLayout(1, 3, 14, 0));
        contenido.setBackground(Estilos.COLOR_FONDO);
        contenido.setBorder(BorderFactory.createEmptyBorder(10, 18, 18, 18));

        contenido.add(crearFormDocente());
        contenido.add(crearFormEstudiante());
        contenido.add(crearFormMateria());

        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearFormDocente() {
        DocenteDAO dao = new DocenteDAO();

        JPanel card = Estilos.panelConTitulo("   Nuevo Docente");
        card.setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        card.add(Estilos.etiqueta("Nombre: *"), g);
        g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        JTextField txtNombre = Estilos.campo(18);
        card.add(txtNombre, g);

        g.gridx = 0; g.gridy = 1; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        card.add(Estilos.etiqueta("Email:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        JTextField txtEmail = Estilos.campo(18);
        card.add(txtEmail, g);

        g.gridx = 0; g.gridy = 2; g.gridwidth = 2; g.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblMsg = new JLabel(" ");
        lblMsg.setFont(Estilos.FUENTE_PEQUEÑA);
        card.add(lblMsg, g);

        g.gridy = 3;
        JButton btnGuardar = Estilos.botonPrimario("Guardar Docente");
        card.add(btnGuardar, g);

        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String email  = txtEmail.getText().trim();
            if (nombre.isEmpty()) {
                lblMsg.setText("  El nombre es obligatorio.");
                lblMsg.setForeground(Estilos.COLOR_ERROR);
                return;
            }
            Docente d = new Docente(0, nombre, email.isEmpty() ? null : email);
            int id = dao.guardar(d);
            if (id > 0) {
                lblMsg.setText("  Docente guardado con ID " + id);
                lblMsg.setForeground(Estilos.COLOR_EXITO);
                txtNombre.setText("");
                txtEmail.setText("");
            } else {
                lblMsg.setText("  Error al guardar. Revise la BD.");
                lblMsg.setForeground(Estilos.COLOR_ERROR);
            }
        });

        return card;
    }

    private JPanel crearFormEstudiante() {
        EstudianteDAO dao = new EstudianteDAO();

        JPanel card = Estilos.panelConTitulo("   Nuevo Estudiante");
        card.setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0; g.weightx = 0; 
        g.fill = GridBagConstraints.NONE;
        card.add(Estilos.etiqueta("Nombre: *"), g);
        g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        JTextField txtNombre = Estilos.campo(18);
        card.add(txtNombre, g);
  
        g.gridx = 0; g.gridy = 1; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        card.add(Estilos.etiqueta("Código: *"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        JTextField txtCodigo = Estilos.campo(18);
        card.add(txtCodigo, g);

        g.gridx = 0; g.gridy = 2; g.fill = GridBagConstraints.NONE;
        card.add(Estilos.etiqueta("Inscribir en:"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        JComboBox<String> cmbMateria = new JComboBox<>();
        cmbMateria.setFont(Estilos.FUENTE_NORMAL);
        cmbMateria.addItem("(ninguna por ahora)");
        
        new modelo.dao.MateriaDAO().obtenerTodas()
            .forEach(m -> cmbMateria.addItem(m.getId() + "|" + m.getNombre()));
        
        cmbMateria.setRenderer((list, value, index, sel, foc) -> {
            JLabel lbl = new JLabel(value == null ? "" :
                (value.contains("|") ? value.split("\\|", 2)[1] : value));
            lbl.setFont(Estilos.FUENTE_NORMAL);
            lbl.setOpaque(true);
            lbl.setBackground(sel ? Estilos.COLOR_SELECCION : Color.WHITE);
            return lbl;
        });
        card.add(cmbMateria, g);

        g.gridx = 0; g.gridy = 3; g.gridwidth = 2; g.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblMsg = new JLabel(" ");
        lblMsg.setFont(Estilos.FUENTE_PEQUEÑA);
        card.add(lblMsg, g);

        g.gridy = 4;
        JButton btnGuardar = Estilos.botonPrimario("Guardar Estudiante");
        card.add(btnGuardar, g);

        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String codigo = txtCodigo.getText().trim();
            if (nombre.isEmpty() || codigo.isEmpty()) {
                lblMsg.setText("  Nombre y código son obligatorios.");
                lblMsg.setForeground(Estilos.COLOR_ERROR);
                return;
            }
            if (dao.existeCodigo(codigo)) {
                lblMsg.setText("  El código '" + codigo + "' ya existe.");
                lblMsg.setForeground(Estilos.COLOR_ERROR);
                return;
            }
            Estudiante est = new Estudiante(nombre, codigo);
            int idEst = dao.guardar(est);
            if (idEst < 0) {
                lblMsg.setText("  Error al guardar en la BD.");
                lblMsg.setForeground(Estilos.COLOR_ERROR);
                return;
            }
            
            String selMateria = (String) cmbMateria.getSelectedItem();
            if (selMateria != null && selMateria.contains("|")) {
                int idMateria = Integer.parseInt(selMateria.split("\\|")[0]);
                inscribirEstudiante(idEst, idMateria);
                lblMsg.setText("  Guardado e inscrito en materia (ID " + idEst + ")");
            } else {
                lblMsg.setText("  Estudiante guardado con ID " + idEst);
            }
            lblMsg.setForeground(Estilos.COLOR_EXITO);
            txtNombre.setText("");
            txtCodigo.setText("");
        });

        return card;
    }

    private JPanel crearFormMateria() {
        MateriaDAO dao = new MateriaDAO();

        JPanel card = Estilos.panelConTitulo("   Nueva Materia");
        card.setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        card.add(Estilos.etiqueta("Nombre: *"), g);
        g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        JTextField txtNombre = Estilos.campo(18);
        card.add(txtNombre, g);

        g.gridx = 0; g.gridy = 1; g.weightx = 0; g.fill = GridBagConstraints.NONE;
        card.add(Estilos.etiqueta("Código: *"), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL;
        JTextField txtCodigo = Estilos.campo(18);
        txtCodigo.setToolTipText("Ej: PROG01, BD02, MAT03");
        card.add(txtCodigo, g);

        g.gridx = 0; g.gridy = 2; g.gridwidth = 2; g.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblMsg = new JLabel(" ");
        lblMsg.setFont(Estilos.FUENTE_PEQUEÑA);
        card.add(lblMsg, g);

        g.gridy = 3;
        JButton btnGuardar = Estilos.botonPrimario("Guardar Materia");
        card.add(btnGuardar, g);

        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String codigo = txtCodigo.getText().trim().toUpperCase();
            if (nombre.isEmpty() || codigo.isEmpty()) {
                lblMsg.setText("  Nombre y código son obligatorios.");
                lblMsg.setForeground(Estilos.COLOR_ERROR);
                return;
            }
            if (dao.existeCodigo(codigo)) {
                lblMsg.setText("  El código '" + codigo + "' ya existe.");
                lblMsg.setForeground(Estilos.COLOR_ERROR);
                return;
            }
            Materia m = new Materia(0, nombre, codigo);
            int id = dao.guardar(m);
            if (id > 0) {
                lblMsg.setText("  Materia guardada con ID " + id);
                lblMsg.setForeground(Estilos.COLOR_EXITO);
                txtNombre.setText("");
                txtCodigo.setText("");
            } else {
                lblMsg.setText("  Error al guardar. Revise la BD.");
                lblMsg.setForeground(Estilos.COLOR_ERROR);
            }
        });

        return card;
    }

    private void inscribirEstudiante(int estudianteId, int materiaId) {
        String sql = "INSERT IGNORE INTO inscripciones (estudiante_id, materia_id) VALUES (?, ?)";
        try (java.sql.Connection cn = bd.Conexion.obtenerConexion();
             java.sql.PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, estudianteId);
            ps.setInt(2, materiaId);
            ps.executeUpdate();
        } catch (java.sql.SQLException ex) {
            System.err.println("[PanelDocente] inscribirEstudiante: " + ex.getMessage());
        }
    }
}