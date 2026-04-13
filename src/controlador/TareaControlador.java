package controlador;

import modelo.Tarea;
import modelo.dao.TareaDAO;
import validaciones.Validaciones;

import java.util.List;

/**
 * Controlador de operaciones sobre Tarea.
 * Responsable: Danner (T30)
 */
public class TareaControlador {

    private final TareaDAO tareaDAO = new TareaDAO();

    /**
     * Crea una nueva tarea.
     * @return null si todo está bien, mensaje de error en caso contrario
     */
    public String crearTarea(Tarea tarea) {
        String error = Validaciones.validarTarea(tarea);
        if (error != null) return error;

        int id = tareaDAO.guardar(tarea);
        if (id < 0) return "Error al guardar la tarea en la base de datos.";
        tarea.setId(id);
        return null;
    }

    /** Devuelve las tareas creadas por un docente. */
    public List<Tarea> obtenerTareasDocente(int docenteId) {
        return tareaDAO.obtenerPorDocente(docenteId);
    }

    /** Devuelve las tareas asignadas a un estudiante. */
    public List<Tarea> obtenerTareasEstudiante(int estudianteId) {
        return tareaDAO.obtenerPorEstudiante(estudianteId);
    }

    /** Devuelve las tareas de una materia. */
    public List<Tarea> obtenerTareasPorMateria(int materiaId) {
        return tareaDAO.obtenerPorMateria(materiaId);
    }

    public Tarea obtenerPorId(int id) {
        return tareaDAO.obtenerPorId(id);
    }
}
