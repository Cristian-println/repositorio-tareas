package modelo;

/**
 * Entidad que representa a un estudiante.
 * Responsable: Joel (T11)
 */
public class Estudiante {

    private int    id;
    private String nombre;
    private String codigo;

    public Estudiante() {}

    public Estudiante(int id, String nombre, String codigo) {
        this.id     = id;
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public Estudiante(String nombre, String codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
    }

    // ── Getters / Setters ────────────────────────────────────────────
    public int    getId()     { return id; }
    public void   setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void   setNombre(String nombre) { this.nombre = nombre; }

    public String getCodigo() { return codigo; }
    public void   setCodigo(String codigo) { this.codigo = codigo; }

    @Override
    public String toString() {
        return nombre + " [" + codigo + "]";
    }
}
