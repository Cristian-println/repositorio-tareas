import java.util.ArrayList;

public class Docente {
    private String email;
    private String nombre;
    private GestorTarea gestor;

    public Docente(String email, String nombre) {
        this.email = email;
        this.nombre = nombre;
        this.gestor = new GestorTarea();
    }
    public Tarea crearTarea(String titulo, Fecha fecha, String descripcion,
                            double calificacionEstimada, Archivo archivo) {
        Tarea nuevaTarea = new Tarea(titulo, fecha, descripcion, calificacionEstimada, archivo);

        if (nuevaTarea.emptyCampos()) {
           throw new IllegalArgumentException("Error campos vacios");
           
        }

        gestor.agregar(nuevaTarea);
        
        return nuevaTarea;
    }
    public void asignarTareaAEstudiante(Tarea tarea, Estudiante estudiante) {
        if (tarea == null || estudiante == null) {
           throw new IllegalArgumentException("Error Campos nulos");
        }

        estudiante.getTareasPendientes().add(tarea);
        
    }
    public void asignarTareaAEstudiantes(Tarea tarea, ArrayList<Estudiante> estudiantes) {
        for (Estudiante e : estudiantes) {
            asignarTareaAEstudiante(tarea, e);
        }
    }
 
    public String getEmail() { 
        return email; 
    }
    public String getNombre() { 
        return nombre; 
    }
    public GestorTarea getGestor() { 
        return gestor; 
    }
}