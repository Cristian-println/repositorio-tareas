package controlador;

import modelo.Calificacion;
import modelo.Entrega;
import modelo.dao.CalificacionDAO;
import modelo.dao.EntregaDAO;
import validaciones.Validaciones;

public class CalificacionControlador {

    private final CalificacionDAO calificacionDAO = new CalificacionDAO();
    private final EntregaDAO      entregaDAO      = new EntregaDAO();

    /**
     * Asigna o actualiza la calificación de una entrega.
     * @param entregaId          ID de la entrega
     * @param notaTexto          Nota ingresada (como texto)
     * @param comentario         Comentario del docente
     * @param calificacionMaxima Nota máxima de la tarea
     * @return null si fue exitoso, mensaje de error si no
     */
    public String calificar(int entregaId, String notaTexto,
                            String comentario, double calificacionMaxima) {
      
        String errorNota = Validaciones.validarNotaTexto(notaTexto, calificacionMaxima);
        if (errorNota != null) return errorNota;

        double nota = Double.parseDouble(notaTexto.trim().replace(",", "."));
        Calificacion cal = new Calificacion(entregaId, nota, comentario);
        if (!calificacionDAO.guardarOActualizar(cal))
            return "Error al guardar la calificación.";
        return null;
    }

    public double obtenerPromedio(int estudianteId) {
        return calificacionDAO.calcularPromedio(estudianteId);
    }

    public int contarCalificadas(int estudianteId) {
        return calificacionDAO.contarCalificadas(estudianteId);
    }

    public Calificacion obtenerPorEntrega(int entregaId) {
        return calificacionDAO.obtenerPorEntrega(entregaId);
    }
}
