package modelo;

public class Tarea {
    private String titulo;
    private Fecha fecha;
    private String descripcion;
    private double calificacionMaxima;
    private Archivo archivo;
    private String materia;

    public Tarea(String titulo, Fecha fecha, String descripcion, double calificacionMaxima, Archivo archivo, String materia) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.calificacionMaxima = calificacionMaxima;
        this.archivo = archivo;
        this.materia = materia;
    }

    public boolean emptyCampos() {
        return titulo == null || titulo.isEmpty()
            || descripcion == null || descripcion.isEmpty()
            || fecha == null
            || materia == null || materia.isEmpty();
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
    	return calificacionMaxima; 
    	}
    public Archivo getArchivo() {
    	return archivo; 
    }
    public String getMateria() {
    	return materia;
    	}
}