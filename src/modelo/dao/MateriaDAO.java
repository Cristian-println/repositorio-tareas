package modelo.dao;

import bd.Conexion;
import modelo.Materia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MateriaDAO {

    public List<Materia> obtenerTodas() {
        List<Materia> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, codigo FROM materias ORDER BY nombre";
        try (Connection cn = Conexion.obtenerConexion();
             Statement  st = cn.createStatement();
             ResultSet  rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[MateriaDAO] obtenerTodas: " + e.getMessage());
        }
        return lista;
    }

    public List<Materia> obtenerPorEstudiante(int estudianteId) {
        List<Materia> lista = new ArrayList<>();
        String sql = "SELECT m.id, m.nombre, m.codigo FROM materias m " +
                     "JOIN inscripciones i ON m.id = i.materia_id " +
                     "WHERE i.estudiante_id = ? ORDER BY m.nombre";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, estudianteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[MateriaDAO] obtenerPorEstudiante: " + e.getMessage());
        }
        return lista;
    }

    public Materia obtenerPorId(int id) {
        String sql = "SELECT id, nombre, codigo FROM materias WHERE id = ?";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[MateriaDAO] obtenerPorId: " + e.getMessage());
        }
        return null;
    }

    private Materia mapear(ResultSet rs) throws SQLException {
        return new Materia(rs.getInt("id"), rs.getString("nombre"), rs.getString("codigo"));
    }
    
    public int guardar(Materia materia) {
        String sql = "INSERT INTO materias (nombre, codigo) VALUES (?, ?)";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, materia.getNombre());
            ps.setString(2, materia.getCodigo());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate"))
                throw new RuntimeException("El código '" + materia.getCodigo() + "' ya existe.");
            System.err.println("[MateriaDAO] guardar: " + e.getMessage());
        }
        return -1;
    }

    public boolean existeCodigo(String codigo) {
        String sql = "SELECT COUNT(*) FROM materias WHERE codigo = ?";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[MateriaDAO] existeCodigo: " + e.getMessage());
        }
        return false;
    }
}
