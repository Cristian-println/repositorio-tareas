package controlador;

import modelo.Entrega;
import modelo.Tarea;
import modelo.dao.EntregaDAO;
import modelo.dao.TareaDAO;
import validaciones.Validaciones;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador de operaciones sobre Entrega.
 * Responsable: Danner (T31)
 */
public class EntregaControlador {

    private final EntregaDAO entregaDAO = new EntregaDAO();
    private final TareaDAO   tareaDAO   = new TareaDAO();

    // Directorio local donde se guardan los archivos de entrega
    private static final String DIR_ENTREGAS = "archivos/entregas/";

    /**
     * Procesa la entrega de una tarea por un estudiante.
     * @param tareaId        ID de la tarea
     * @param estudianteId   ID del estudiante
     * @param archivo        Archivo a entregar
     * @param comentario     Comentario opcional del estudiante
     * @return null si fue exitoso, mensaje de error si no
     */
    public String entregarTarea(int tareaId, int estudianteId,
                                File archivo, String comentario) {
        // Validar archivo
        String errArchivo = Validaciones.validarArchivo(archivo);
        if (errArchivo != null) return errArchivo;

        // Obtener tarea para verificar fecha
        Tarea tarea = tareaDAO.obtenerPorId(tareaId);
        if (tarea == null) return "La tarea no existe.";

        boolean esTardio = tarea.estaVencida();

        // Copiar archivo al directorio de entregas
        String rutaDestino;
        try {
            rutaDestino = copiarArchivo(archivo, tareaId, estudianteId);
        } catch (IOException e) {
            return "Error al guardar el archivo: " + e.getMessage();
        }

        // Verificar si ya existe entrega previa
        Entrega entregaExistente = entregaDAO.obtenerPorTareaYEstudiante(tareaId, estudianteId);

        if (entregaExistente != null) {
            // Actualizar entrega existente (reentrega)
            entregaExistente.setArchivoRuta(rutaDestino);
            entregaExistente.setComentarioEstudiante(comentario);
            entregaExistente.setEsTardio(esTardio);
            entregaExistente.setFechaEntrega(LocalDateTime.now());
            if (!entregaDAO.actualizar(entregaExistente))
                return "Error al actualizar la entrega.";
        } else {
            // Nueva entrega
            Entrega entrega = new Entrega(tareaId, estudianteId, rutaDestino, comentario, esTardio);
            int id = entregaDAO.guardar(entrega);
            if (id < 0) return "Error al registrar la entrega.";
        }
        return null; // éxito
    }

    public List<Entrega> obtenerEntregasPorTarea(int tareaId) {
        return entregaDAO.obtenerPorTarea(tareaId);
    }

    public List<Entrega> obtenerEntregasPorEstudiante(int estudianteId) {
        return entregaDAO.obtenerPorEstudiante(estudianteId);
    }

    public Entrega obtenerEntrega(int tareaId, int estudianteId) {
        return entregaDAO.obtenerPorTareaYEstudiante(tareaId, estudianteId);
    }

    // ── Helpers ──────────────────────────────────────────────────────
    private String copiarArchivo(File origen, int tareaId, int estudianteId) throws IOException {
        // Crear directorio si no existe
        Path dir = Paths.get(DIR_ENTREGAS);
        if (!Files.exists(dir)) Files.createDirectories(dir);

        String ext = "";
        int punto = origen.getName().lastIndexOf('.');
        if (punto >= 0) ext = origen.getName().substring(punto);

        String nombreDestino = "tarea_" + tareaId + "_est_" + estudianteId +
                               "_" + System.currentTimeMillis() + ext;
        Path destino = dir.resolve(nombreDestino);
        Files.copy(origen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
        return destino.toString();
    }
}
