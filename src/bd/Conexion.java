package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión JDBC a la base de datos MySQL.
 * Responsable: Josué (T09)
 */
public class Conexion {

    private static final String URL =
        "jdbc:mysql://localhost:3306/classroom_db" +
        "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USUARIO   = "root";
    private static final String CONTRASENA = "";

    private static Connection conexion = null;

    /** Devuelve (o crea) la conexión singleton. */
    public static Connection obtenerConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("[BD] Driver MySQL no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("[BD] Error de conexión: " + e.getMessage());
        }
        return conexion;
    }

    /** Cierra la conexión si está abierta. */
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                conexion = null;
            }
        } catch (SQLException e) {
            System.err.println("[BD] Error al cerrar conexión: " + e.getMessage());
        }
    }

    /** Prueba si la conexión es posible. */
    public static boolean probarConexion() {
        return obtenerConexion() != null;
    }
}
