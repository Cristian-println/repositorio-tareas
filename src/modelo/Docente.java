package modelo;

import java.util.ArrayList;
import java.util.List;

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
                            double calificacionEstimada, Archivo archivo, String materia) {

        Tarea nuevaTarea = new Tarea(titulo, fecha, descripcion, calificacionEstimada, archivo,materia);

        if (nuevaTarea.emptyCampos()) {
            throw new IllegalArgumentException("Error: algun campo esta vacio");
        }

        gestor.agregar(nuevaTarea);

        return nuevaTarea;
    }

    public void asignarTareaAEstudiante(Tarea tarea, Estudiante estudiante) {
        if (tarea == null || estudiante == null) {
            throw new IllegalArgumentException("Error: campos nulos");
        }

        estudiante.agregarTareaPendiente(tarea); 
    }

    public void asignarTareaAEstudiantes(Tarea tarea, List<Estudiante> estudiantes) {
        if (tarea == null || estudiantes == null) {
            throw new IllegalArgumentException("Error: datos inválidos");
        }

        for (Estudiante e : estudiantes) {
            asignarTareaAEstudiante(tarea, e);
        }
    }
    
    public void asignarCalificacion(Entrega entrega, double nota, String comentario) {
        if (entrega == null) {
            throw new IllegalArgumentException("La entrega no puede ser nula");
        }
        Calificacion calificacion = new Calificacion(nota, comentario);
        entrega.setCalificacion(calificacion);
    }
    
    public String buscarEstudiantePorNombre(String nombre, String materia, List<Estudiante> estudiantes) {

        for (int i = 0; i < estudiantes.size(); i++) {
            Estudiante e = estudiantes.get(i);

            boolean perteneceMateria = false;

            List<Tarea> tareas = e.getTareasPendientes();
            for (int j = 0; j < tareas.size(); j++) {
                if (tareas.get(j).getMateria().equalsIgnoreCase(materia)) {
                    perteneceMateria = true;
                    break;
                }
            }

            if (perteneceMateria && e.getNombre().equalsIgnoreCase(nombre)) {
                return "Nombre: " + e.getNombre() + ", Apellido: " + e.getApellido();
            }
        }

        return null; // no encontrado
    }
    
    public List<Estudiante> filtrarEstudiantesPorMateria(String materia, List<Estudiante> estudiantes) {
        List<Estudiante> resultado = new ArrayList<>();

        for (int i = 0; i < estudiantes.size(); i++) {
            Estudiante e = estudiantes.get(i);

            boolean perteneceMateria = false;

            List<Tarea> tareas = e.getTareasPendientes();
            for (int j = 0; j < tareas.size(); j++) {
                if (tareas.get(j).getMateria().equalsIgnoreCase(materia)) {
                    perteneceMateria = true;
                    break;
                }
            }

            if (perteneceMateria) {
                resultado.add(e);
            }
        }

        return resultado;
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
