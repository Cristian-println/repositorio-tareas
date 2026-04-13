package modelo.dao;

import bd.Conexion;
import modelo.Docente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para la entidad Docente.
 * Responsable: Joel (T18)
 */
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
}
