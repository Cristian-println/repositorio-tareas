package controlador;

import modelo.Entrega;
import modelo.Estudiante;
import modelo.Tarea;
import modelo.dao.EntregaDAO;
import modelo.dao.EstudianteDAO;
import modelo.dao.TareaDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstudianteControlador {

    private final EstudianteDAO estudianteDAO = new EstudianteDAO();
    private final TareaDAO      tareaDAO      = new TareaDAO();
    private final EntregaDAO    entregaDAO    = new EntregaDAO();

    public List<Estudiante> buscar(String termino) {
        if (termino == null || termino.trim().isEmpty())
            return estudianteDAO.obtenerTodos();
        return estudianteDAO.buscar(termino.trim());
    }

    public List<Estudiante> obtenerTodos() {
        return estudianteDAO.obtenerTodos();
    }

    public Estudiante obtenerPorId(int id) {
        return estudianteDAO.obtenerPorId(id);
    }

    public List<Entrega> obtenerEntregas(int estudianteId) {
        return entregaDAO.obtenerPorEstudiante(estudianteId);
    }

    public List<Tarea> obtenerTareas(int estudianteId) {
        return tareaDAO.obtenerPorEstudiante(estudianteId);
    }
}
