package modelo.dao;

import bd.Conexion;
import modelo.Docente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocenteDAO {

    public List<Docente> obtenerTodos() {
        List<Docente> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, email FROM docentes ORDER BY nombre";
        try (Connection cn = Conexion.obtenerConexion();
             Statement  st = cn.createStatement();
             ResultSet  rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[DocenteDAO] obtenerTodos: " + e.getMessage());
        }
        return lista;
    }

    public Docente obtenerPorId(int id) {
        String sql = "SELECT id, nombre, email FROM docentes WHERE id = ?";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[DocenteDAO] obtenerPorId: " + e.getMessage());
        }
        return null;
    }

    private Docente mapear(ResultSet rs) throws SQLException {
        return new Docente(rs.getInt("id"), rs.getString("nombre"), rs.getString("email"));
    }
    
    public int guardar(Docente docente) {
        String sql = "INSERT INTO docentes (nombre, email) VALUES (?, ?)";
        try (Connection cn = Conexion.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, docente.getNombre());
            ps.setString(2, docente.getEmail());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[DocenteDAO] guardar: " + e.getMessage());
        }
        return -1;
    }

}
