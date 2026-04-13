package modelo;

public class Entrega {
	private int idEntrega;
    private Archivo archivo;
    private Fecha fechaEntrega;
    private boolean estado;
    private Estudiante estudiante;
    private Tarea tarea;
    private Calificacion calificacion;


    public Entrega(int idEntrega, Archivo archivo, Estudiante estudiante, Tarea tarea) {
        this.idEntrega = idEntrega;
        this.archivo = archivo;
        this.estudiante = estudiante;
        this.tarea = tarea;
        this.estado = false;
    }

    
    public void registrarEntrega(Fecha fecha){
        if (fecha.estaAntesDeLaFechaAsignada(tarea.getFecha())) {
            this.fechaEntrega = fecha;
            this.estado = true;

        } else {
            throw new IllegalArgumentException("No se puede entregar la fecha paso el limite");
        }
    }
    public void setCalificacion(Calificacion calificacion) {
        this.calificacion = calificacion;
    }

    public Calificacion getCalificacion() {
        return calificacion;
    }
 public int getIdEntrega() {
         return idEntrega;
         }
    public Archivo getArchivo() { 
        return archivo; 
    }
    public Fecha getFechaEntrega() {
         return fechaEntrega;
         }
    public boolean getEstado() {
         return estado; 
        }
    public Estudiante getEstudiante() {
         return estudiante; 
        }
    public Tarea getTarea() {
    	return tarea;
    }
 
    public void registrarEntregaValidada(Fecha fechaIntento) {
        if (fechaIntento == null) {
            throw new IllegalArgumentException("US2 Error: Debe proporcionar una fecha de entrega.");
        }
        fechaIntento.validarCalendario(); 
        
        if (this.archivo == null) {
            throw new IllegalArgumentException("US2 Error: La entrega debe contener un archivo (no puede estar vacío).");
        }
        
        java.util.List<String> formatosPermitidos = java.util.Arrays.asList("pdf", "zip", "rar");
        this.archivo.validarArchivo(50.0, formatosPermitidos);
        
        this.registrarEntrega(fechaIntento);
    }
}
