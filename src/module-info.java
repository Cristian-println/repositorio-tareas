/**
 * Módulo principal del Sistema Classroom.
 * Responsable: Josué (T48)
 *
 * Si el proyecto en Eclipse da errores con este archivo,
 * puede eliminarlo y configurar el classpath manualmente
 * añadiendo mysql-connector-j-*.jar al Build Path.
 */
module classroom.proyecto {
    requires java.sql;       // JDBC
    requires java.desktop;   // Swing / AWT
    // Si usa MySQL Connector/J >= 8.x como módulo nombrado:
    // requires mysql.connector.j;
}