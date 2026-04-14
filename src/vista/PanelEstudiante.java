package vista;

import vista.paneles.PanelEntregarTarea;
import vista.paneles.PanelMisNotas;
import vista.paneles.PanelMisTareas;

import javax.swing.*;
import java.awt.*;

public class PanelEstudiante extends JPanel {

    private int estudianteId = -1;

    private final PanelMisTareas     panelMisTareas;
    private final PanelEntregarTarea  panelEntregar;
    private final PanelMisNotas       panelNotas;
    private final JTabbedPane         tabs;

    public PanelEstudiante() {
        setLayout(new BorderLayout());
        setBackground(Estilos.COLOR_FONDO);

        panelMisTareas = new PanelMisTareas();
        panelEntregar  = new PanelEntregarTarea();
        panelNotas     = new PanelMisNotas();

        tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setFont(Estilos.FUENTE_NEGRITA);
        tabs.setBackground(Estilos.COLOR_FONDO);

        tabs.addTab("     Mis Tareas  ",      panelMisTareas);
        tabs.addTab("     Entregar Tarea  ",   panelEntregar);
        tabs.addTab("     Mis Notas  ",        panelNotas);

        tabs.addChangeListener(e -> {
            if (estudianteId < 1) return;
            switch (tabs.getSelectedIndex()) {
                case 0 -> panelMisTareas.cargarDatos(estudianteId);
                case 1 -> panelEntregar.cargarDatos(estudianteId);
                case 2 -> panelNotas.cargarDatos(estudianteId);
            }
        });

        add(tabs, BorderLayout.CENTER);
    }

    public void cargarDatos(int estudianteId) {
        this.estudianteId = estudianteId;
        tabs.setSelectedIndex(0);
        panelMisTareas.cargarDatos(estudianteId);
        panelEntregar.cargarDatos(estudianteId);
        panelNotas.cargarDatos(estudianteId);
    }
}
