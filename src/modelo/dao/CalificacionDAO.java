package modelo.dao;

import bd.Conexion;
import modelo.Calificacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para la entidad Calificacion.
 * Responsable: Joel (T22 + T23 promedio)
 */
public class CalificacionDAO {

    /** Guarda o actualiza la calificación de una entrega. */
    public boolean guardarOActualizar(Calificacion cal) {
        // Si ya existe, actualiza
        String sqlCheck = "SELECT id FROM calificaciones WHERE entrega_id = ?";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sqlCheck)) {
            ps.setInt(1, cal.getEntregaId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return actualizar(cal, cn);
                } else {
                    return insertar(cal, cn);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CalificacionDAO] guardarOActualizar: " + e.getMessage());
        }
        return false;
    }

    private boolean insertar(Calificacion cal, Connection cn) throws SQLException {
        String sql = "INSERT INTO calificaciones (entrega_id, nota, comentario) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, cal.getEntregaId());
            ps.setDouble(2, cal.getNota());
            ps.setString(3, cal.getComentario());
            return ps.executeUpdate() > 0;
        }
    }

    private boolean actualizar(Calificacion cal, Connection cn) throws SQLException {
        String sql = "UPDATE calificaciones SET nota=?, comentario=?, " +
                     "fecha_calificacion=NOW() WHERE entrega_id=?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDouble(1, cal.getNota());
            ps.setString(2, cal.getComentario());
            ps.setInt(3, cal.getEntregaId());
            return ps.executeUpdate() > 0;
        }
    }

    public Calificacion obtenerPorEntrega(int entregaId) {
        String sql = "SELECT id, entrega_id, nota, comentario, fecha_calificacion " +
                     "FROM calificaciones WHERE entrega_id = ?";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, entregaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[CalificacionDAO] obtenerPorEntrega: " + e.getMessage());
        }
        return null;
    }

    /**
     * T23: Calcula el promedio de notas de un estudiante.
     * Solo considera entregas calificadas.
     */
    public double calcularPromedio(int estudianteId) {
        String sql = "SELECT AVG(c.nota) AS promedio " +
                     "FROM calificaciones c " +
                     "JOIN entregas e ON c.entrega_id = e.id " +
                     "WHERE e.estudiante_id = ?";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, estudianteId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double prom = rs.getDouble("promedio");
                    return rs.wasNull() ? -1 : prom;
                }
            }
        } catch (SQLException e) {
            System.err.println("[CalificacionDAO] calcularPromedio: " + e.getMessage());
        }
        return -1;
    }

    /** Cuenta las tareas calificadas de un estudiante. */
    public int contarCalificadas(int estudianteId) {
        String sql = "SELECT COUNT(*) FROM calificaciones c " +
                     "JOIN entregas e ON c.entrega_id = e.id " +
                     "WHERE e.estudiante_id = ?";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, estudianteId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[CalificacionDAO] contarCalificadas: " + e.getMessage());
        }
        return 0;
    }

    /** Calificaciones de un estudiante con información de la tarea. */
    public List<Calificacion> obtenerPorEstudiante(int estudianteId) {
        List<Calificacion> lista = new ArrayList<>();
        String sql = "SELECT c.id, c.entrega_id, c.nota, c.comentario, c.fecha_calificacion " +
                     "FROM calificaciones c " +
                     "JOIN entregas e ON c.entrega_id = e.id " +
                     "WHERE e.estudiante_id = ?";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, estudianteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[CalificacionDAO] obtenerPorEstudiante: " + e.getMessage());
        }
        return lista;
    }

    private Calificacion mapear(ResultSet rs) throws SQLException {
        Calificacion c = new Calificacion();
        c.setId(rs.getInt("id"));
        c.setEntregaId(rs.getInt("entrega_id"));
        c.setNota(rs.getDouble("nota"));
        c.setComentario(rs.getString("comentario"));
        Timestamp ts = rs.getTimestamp("fecha_calificacion");
        if (ts != null) c.setFechaCalificacion(ts.toLocalDateTime());
        return c;
    }
}
