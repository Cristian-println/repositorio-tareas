public class Tarea {
   private String titulo;
   private Fecha fecha;
   private String descripcion; 
   private double calificacionEstimada;
   private Archivo archivo;

     public Tarea(String titulo, Fecha fecha, String descripcion, double calificacionEstimada, Archivo archivo) {
    this.titulo = titulo;
    this.fecha = fecha;
    this.descripcion = descripcion;
    this.calificacionEstimada = calificacionEstimada;
    this.archivo = archivo;
    }
    public boolean emptyCampos() {
        return titulo == null || titulo.isEmpty()
            || descripcion == null || descripcion.isEmpty()
            || fecha == null;
    }

    public String getTitulo() {
    return titulo;
    }
    public Fecha getFecha() {
    return fecha;
    }
    public String getDescripcion() {
    return descripcion;
    }
    public double getCalificacionEstimada() {
    return calificacionEstimada;
    }
    public Archivo getArchivo() {
    return archivo;
    }
 
    }
   










