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
}
