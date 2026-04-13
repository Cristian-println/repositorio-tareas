package modelo.dao;

import bd.Conexion;
import modelo.Entrega;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para la entidad Entrega.
 * Responsable: Joel (T21)
 */
public class EntregaDAO {

    private static final String BASE_SELECT =
        "SELECT e.id, e.tarea_id, t.titulo AS tarea_titulo, " +
        "e.estudiante_id, est.nombre AS estudiante_nombre, " +
        "e.archivo_ruta, e.comentario_estudiante, e.fecha_entrega, e.es_tardio, " +
        "t.calificacion_maxima, " +
        "c.nota, c.comentario AS comentario_docente " +
        "FROM entregas e " +
        "JOIN tareas t ON e.tarea_id = t.id " +
        "JOIN estudiantes est ON e.estudiante_id = est.id " +
        "LEFT JOIN calificaciones c ON e.id = c.entrega_id ";

    /** Guarda una nueva entrega y devuelve su ID (-1 si falla). */
    public int guardar(Entrega entrega) {
        String sql = "INSERT INTO entregas " +
                     "(tarea_id, estudiante_id, archivo_ruta, comentario_estudiante, fecha_entrega, es_tardio) " +
                     "VALUES (?, ?, ?, ?, NOW(), ?)";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entrega.getTareaId());
            ps.setInt(2, entrega.getEstudianteId());
            ps.setString(3, entrega.getArchivoRuta());
            ps.setString(4, entrega.getComentarioEstudiante());
            ps.setBoolean(5, entrega.isEsTardio());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[EntregaDAO] guardar: " + e.getMessage());
        }
        return -1;
    }

    /** Actualiza el archivo de una entrega existente. */
    public boolean actualizar(Entrega entrega) {
        String sql = "UPDATE entregas SET archivo_ruta=?, comentario_estudiante=?, " +
                     "fecha_entrega=NOW(), es_tardio=? WHERE id=?";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, entrega.getArchivoRuta());
            ps.setString(2, entrega.getComentarioEstudiante());
            ps.setBoolean(3, entrega.isEsTardio());
            ps.setInt(4, entrega.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[EntregaDAO] actualizar: " + e.getMessage());
        }
        return false;
    }

    /** Entregas de una tarea específica. */
    public List<Entrega> obtenerPorTarea(int tareaId) {
        return consultar(BASE_SELECT + "WHERE e.tarea_id = ? ORDER BY e.fecha_entrega ASC", tareaId);
    }

    /** Entregas de un estudiante específico. */
    public List<Entrega> obtenerPorEstudiante(int estudianteId) {
        return consultar(BASE_SELECT + "WHERE e.estudiante_id = ? ORDER BY e.fecha_entrega DESC", estudianteId);
    }

    /** Verifica si un estudiante ya entregó una tarea. */
    public Entrega obtenerPorTareaYEstudiante(int tareaId, int estudianteId) {
        String sql = BASE_SELECT + "WHERE e.tarea_id = ? AND e.estudiante_id = ?";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, tareaId);
            ps.setInt(2, estudianteId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[EntregaDAO] obtenerPorTareaYEstudiante: " + e.getMessage());
        }
        return null;
    }

    // ── Helpers ──────────────────────────────────────────────────────
    private List<Entrega> consultar(String sql, int param) {
        List<Entrega> lista = new ArrayList<>();
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[EntregaDAO] consultar: " + e.getMessage());
        }
        return lista;
    }

    private Entrega mapear(ResultSet rs) throws SQLException {
        Entrega e = new Entrega();
        e.setId(rs.getInt("id"));
        e.setTareaId(rs.getInt("tarea_id"));
        e.setTareaTitulo(rs.getString("tarea_titulo"));
        e.setEstudianteId(rs.getInt("estudiante_id"));
        e.setEstudianteNombre(rs.getString("estudiante_nombre"));
        e.setArchivoRuta(rs.getString("archivo_ruta"));
        e.setComentarioEstudiante(rs.getString("comentario_estudiante"));
        Timestamp ts = rs.getTimestamp("fecha_entrega");
        if (ts != null) e.setFechaEntrega(ts.toLocalDateTime());
        e.setEsTardio(rs.getBoolean("es_tardio"));
        e.setCalificacionMaxima(rs.getDouble("calificacion_maxima"));
        double nota = rs.getDouble("nota");
        if (!rs.wasNull()) {
            e.setNota(nota);
            e.setEstado(Entrega.Estado.CALIFICADA);
        } else {
            e.setEstado(Entrega.Estado.ENTREGADA);
        }
        e.setComentarioDocente(rs.getString("comentario_docente"));
        return e;
    }
}
