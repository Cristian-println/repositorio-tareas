package modelo.dao;

import bd.Conexion;
import modelo.Tarea;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para la entidad Tarea.
 * Responsable: Joel (T20)
 */
public class TareaDAO {

    private static final String BASE_SELECT =
        "SELECT t.id, t.titulo, t.descripcion, t.fecha_limite, " +
        "t.calificacion_maxima, t.archivo_adjunto, " +
        "t.materia_id, m.nombre AS materia_nombre, " +
        "t.docente_id, d.nombre AS docente_nombre, t.fecha_creacion " +
        "FROM tareas t " +
        "JOIN materias m ON t.materia_id = m.id " +
        "JOIN docentes d ON t.docente_id = d.id ";

    /** Guarda una nueva tarea y devuelve su ID generado (-1 si falla). */
    public int guardar(Tarea tarea) {
        String sql = "INSERT INTO tareas (titulo, descripcion, fecha_limite, " +
                     "calificacion_maxima, archivo_adjunto, materia_id, docente_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tarea.getTitulo());
            ps.setString(2, tarea.getDescripcion());
            ps.setTimestamp(3, Timestamp.valueOf(tarea.getFechaLimite()));
            ps.setDouble(4, tarea.getCalificacionMaxima());
            ps.setString(5, tarea.getArchivoAdjunto());
            ps.setInt(6, tarea.getMateriaId());
            ps.setInt(7, tarea.getDocenteId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[TareaDAO] guardar: " + e.getMessage());
        }
        return -1;
    }

    /** Devuelve las tareas de un docente. */
    public List<Tarea> obtenerPorDocente(int docenteId) {
        return ejecutarConsulta(BASE_SELECT + "WHERE t.docente_id = ? ORDER BY t.fecha_limite DESC",
                docenteId);
    }

    /**
     * Devuelve las tareas asignadas a un estudiante
     * (a través de sus inscripciones en materias).
     */
    public List<Tarea> obtenerPorEstudiante(int estudianteId) {
        List<Tarea> lista = new ArrayList<>();
        String sql = BASE_SELECT +
                     "WHERE t.materia_id IN " +
                     "  (SELECT materia_id FROM inscripciones WHERE estudiante_id = ?) " +
                     "ORDER BY t.fecha_limite ASC";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, estudianteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[TareaDAO] obtenerPorEstudiante: " + e.getMessage());
        }
        return lista;
    }

    /** Devuelve las tareas de una materia. */
    public List<Tarea> obtenerPorMateria(int materiaId) {
        return ejecutarConsulta(BASE_SELECT + "WHERE t.materia_id = ? ORDER BY t.fecha_limite DESC",
                materiaId);
    }

    public Tarea obtenerPorId(int id) {
        List<Tarea> r = ejecutarConsulta(BASE_SELECT + "WHERE t.id = ?", id);
        return r.isEmpty() ? null : r.get(0);
    }

    // ── Helpers ──────────────────────────────────────────────────────
    private List<Tarea> ejecutarConsulta(String sql, int param) {
        List<Tarea> lista = new ArrayList<>();
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[TareaDAO] consulta: " + e.getMessage());
        }
        return lista;
    }

    private Tarea mapear(ResultSet rs) throws SQLException {
        Tarea t = new Tarea();
        t.setId(rs.getInt("id"));
        t.setTitulo(rs.getString("titulo"));
        t.setDescripcion(rs.getString("descripcion"));
        Timestamp ts = rs.getTimestamp("fecha_limite");
        if (ts != null) t.setFechaLimite(ts.toLocalDateTime());
        t.setCalificacionMaxima(rs.getDouble("calificacion_maxima"));
        t.setArchivoAdjunto(rs.getString("archivo_adjunto"));
        t.setMateriaId(rs.getInt("materia_id"));
        t.setMateriaNombre(rs.getString("materia_nombre"));
        t.setDocenteId(rs.getInt("docente_id"));
        t.setDocenteNombre(rs.getString("docente_nombre"));
        Timestamp tc = rs.getTimestamp("fecha_creacion");
        if (tc != null) t.setFechaCreacion(tc.toLocalDateTime());
        return t;
    }
}
