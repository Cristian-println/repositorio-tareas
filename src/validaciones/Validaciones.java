package validaciones;

import modelo.Tarea;
import modelo.dao.EstudianteDAO;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Clase de validaciones de datos del sistema.
 * Responsable: Marco (T24-T29)
 */
public class Validaciones {

    // T26: Tamaño de archivo (según historia de usuario)
    public static final long TAMANO_MINIMO_BYTES = 10L * 1024 * 1024;       // 10 MB
    public static final long TAMANO_MAXIMO_BYTES = 1024L * 1024 * 1024;     // 1 GB

    // T27: Formatos permitidos
    private static final List<String> FORMATOS_PERMITIDOS =
        Arrays.asList("pdf", "doc", "docx", "txt", "xlsx", "xls", "pptx", "ppt",
                      "zip", "rar", "png", "jpg", "jpeg");

    private static final EstudianteDAO estudianteDAO = new EstudianteDAO();

    // ── T24: Validar campos obligatorios de tarea ─────────────────────
    /**
     * Valida los datos de una tarea.
     * @return null si es válido, o mensaje de error
     */
    public static String validarTarea(Tarea tarea) {
        if (tarea.getTitulo() == null || tarea.getTitulo().trim().isEmpty())
            return "El título de la tarea es obligatorio.";
        if (tarea.getTitulo().trim().length() > 200)
            return "El título no puede superar 200 caracteres.";
        if (tarea.getFechaLimite() == null)
            return "La fecha de entrega es obligatoria.";
        // T25: La fecha límite no puede ser en el pasado
        String errorFecha = validarFechaLimite(tarea.getFechaLimite());
        if (errorFecha != null) return errorFecha;
        if (tarea.getMateriaId() <= 0)
            return "Debe seleccionar una materia.";
        if (tarea.getDocenteId() <= 0)
            return "No se identificó al docente.";
        if (tarea.getCalificacionMaxima() <= 0)
            return "La calificación máxima debe ser mayor a 0.";
        return null; // válido
    }

    // ── T25: Validar fecha límite ─────────────────────────────────────
    public static String validarFechaLimite(LocalDateTime fecha) {
        if (fecha == null) return "La fecha de entrega es obligatoria.";
        if (fecha.isBefore(LocalDateTime.now()))
            return "La fecha de entrega no puede ser en el pasado.";
        return null;
    }

    // ── T26: Validar tamaño de archivo ────────────────────────────────
    public static String validarTamanoArchivo(File archivo) {
        if (archivo == null) return "Debe seleccionar un archivo.";
        long tamano = archivo.length();
        if (tamano < TAMANO_MINIMO_BYTES)
            return String.format("El archivo es muy pequeño. Mínimo requerido: 10 MB " +
                                 "(el archivo pesa %.2f MB).", tamano / (1024.0 * 1024));
        if (tamano > TAMANO_MAXIMO_BYTES)
            return "El archivo supera el tamaño máximo permitido de 1 GB.";
        return null;
    }

    // ── T27: Validar formato de archivo ───────────────────────────────
    public static String validarFormatoArchivo(File archivo) {
        if (archivo == null) return "Debe seleccionar un archivo.";
        String nombre = archivo.getName().toLowerCase();
        int punto = nombre.lastIndexOf('.');
        if (punto < 0) return "El archivo no tiene extensión reconocible.";
        String ext = nombre.substring(punto + 1);
        if (!FORMATOS_PERMITIDOS.contains(ext))
            return "Formato no permitido (" + ext + "). Formatos válidos: " +
                   String.join(", ", FORMATOS_PERMITIDOS);
        return null;
    }

    /** Valida formato y tamaño en un solo paso. */
    public static String validarArchivo(File archivo) {
        String errFmt = validarFormatoArchivo(archivo);
        if (errFmt != null) return errFmt;
        return validarTamanoArchivo(archivo);
    }

    // ── T28: Validar rango de calificación ────────────────────────────
    /**
     * @param nota              nota a asignar
     * @param calificacionMaxima calificación máxima de la tarea
     */
    public static String validarCalificacion(double nota, double calificacionMaxima) {
        if (nota < 0)
            return "La nota no puede ser negativa.";
        if (nota > calificacionMaxima)
            return String.format("La nota (%.2f) supera la calificación máxima de la tarea (%.2f).",
                    nota, calificacionMaxima);
        return null;
    }

    // ── T29: Validar inscripción ──────────────────────────────────────
    public static String validarInscripcion(int estudianteId, int materiaId) {
        if (!estudianteDAO.estaInscrito(estudianteId, materiaId))
            return "El estudiante no está inscrito en esta materia.";
        return null;
    }

    // ── Utilidades ────────────────────────────────────────────────────
    public static boolean esNuloOVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    public static String validarNotaTexto(String textoNota, double maxima) {
        if (esNuloOVacio(textoNota)) return "Ingrese la nota.";
        try {
            double nota = Double.parseDouble(textoNota.trim().replace(",", "."));
            return validarCalificacion(nota, maxima);
        } catch (NumberFormatException e) {
            return "La nota debe ser un número válido.";
        }
    }
}
