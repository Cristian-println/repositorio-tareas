package vista;

import vista.paneles.PanelBuscarEstudiante;
import vista.paneles.PanelCalificarTareas;
import vista.paneles.PanelCrearTarea;

import javax.swing.*;
import java.awt.*;

/**
 * Panel principal para el rol Docente.
 * Contiene las pestañas: Crear Tarea, Calificar Tareas, Buscar Estudiante.
 * Responsable: Danner (T36)
 */
public class PanelDocente extends JPanel {

    private int docenteId = -1;

    private final PanelCrearTarea       panelCrearTarea;
    private final PanelCalificarTareas  panelCalificar;
    private final PanelBuscarEstudiante panelBuscar;
    private final JTabbedPane           tabs;

    public PanelDocente() {
        setLayout(new BorderLayout());
        setBackground(Estilos.COLOR_FONDO);

        panelCrearTarea = new PanelCrearTarea();
        panelCalificar  = new PanelCalificarTareas();
        panelBuscar     = new PanelBuscarEstudiante();

        tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setFont(Estilos.FUENTE_NEGRITA);
        tabs.setBackground(Estilos.COLOR_FONDO);

        tabs.addTab("  ✏  Crear Tarea  ",       panelCrearTarea);
        tabs.addTab("  📋  Calificar Tareas  ",  panelCalificar);
        tabs.addTab("  🔍  Buscar Estudiante  ", panelBuscar);

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

    /** Carga los datos para el docente seleccionado. */
    public void cargarDatos(int docenteId) {
        this.docenteId = docenteId;
        tabs.setSelectedIndex(0);
        panelCrearTarea.setDocenteId(docenteId);
        panelCalificar.cargarDatos(docenteId);
        panelBuscar.cargarDatos();
    }
}
