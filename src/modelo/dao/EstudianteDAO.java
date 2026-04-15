package modelo.dao;

import bd.Conexion;
import modelo.Estudiante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {

    public List<Estudiante> obtenerTodos() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, codigo FROM estudiantes ORDER BY nombre";
        try (Connection cn = Conexion.obtenerConexion();
             Statement  st = cn.createStatement();
             ResultSet  rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[EstudianteDAO] obtenerTodos: " + e.getMessage());
        }
        return lista;
    }

    public List<Estudiante> buscar(String termino) {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, codigo FROM estudiantes " +
                     "WHERE LOWER(nombre) LIKE ? OR LOWER(codigo) LIKE ? ORDER BY nombre";
        try (Connection  cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            String like = "%" + termino.toLowerCase() + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[EstudianteDAO] buscar: " + e.getMessage());
        }
        return lista;
    }

    public List<Estudiante> obtenerPorMateria(int materiaId) {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT e.id, e.nombre, e.codigo FROM estudiantes e " +
                     "JOIN inscripciones i ON e.id = i.estudiante_id " +
                     "WHERE i.materia_id = ? ORDER BY e.nombre";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, materiaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[EstudianteDAO] obtenerPorMateria: " + e.getMessage());
        }
        return lista;
    }

    public Estudiante obtenerPorId(int id) {
        String sql = "SELECT id, nombre, codigo FROM estudiantes WHERE id = ?";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[EstudianteDAO] obtenerPorId: " + e.getMessage());
        }
        return null;
    }

    public boolean estaInscrito(int estudianteId, int materiaId) {
        String sql = "SELECT COUNT(*) FROM inscripciones " +
                     "WHERE estudiante_id = ? AND materia_id = ?";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, estudianteId);
            ps.setInt(2, materiaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[EstudianteDAO] estaInscrito: " + e.getMessage());
        }
        return false;
    }

    private Estudiante mapear(ResultSet rs) throws SQLException {
        return new Estudiante(rs.getInt("id"), rs.getString("nombre"), rs.getString("codigo"));
    }
    
    public int guardar(Estudiante estudiante) {
        String sql = "INSERT INTO estudiantes (nombre, codigo) VALUES (?, ?)";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getCodigo());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            
            if (e.getMessage().contains("Duplicate")) 
                throw new RuntimeException("El código '" + estudiante.getCodigo() + "' ya existe.");
            System.err.println("[EstudianteDAO] guardar: " + e.getMessage());
        }
        return -1;
    }

    public boolean existeCodigo(String codigo) {
        String sql = "SELECT COUNT(*) FROM estudiantes WHERE codigo = ?";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[EstudianteDAO] existeCodigo: " + e.getMessage());
        }
        return false;
    }

}
