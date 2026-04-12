package modelo;

import java.util.ArrayList;
import java.util.List;

public class Estudiante {

    private String nombre;
    private String apellido;
    private List<Tarea> tareasPendientes;
    private List<Entrega> entregas;

    public Estudiante(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.tareasPendientes = new ArrayList<>();
        this.entregas = new ArrayList<>();
    }

    public List<Tarea> verMisTareas() {
        return new ArrayList<>(tareasPendientes);
    }

    public void agregarTareaPendiente(Tarea tarea) {
        tareasPendientes.add(tarea);
    }

    public void agregarEntrega(Entrega entrega) {
        entregas.add(entrega);
    }
    
 
    public List<Entrega> obtenerEntregasCalificadas() {
        List<Entrega> calificadas = new ArrayList<>();
        for (Entrega e : entregas) {
            if (e.getCalificacion() != null) {
                calificadas.add(e);
            }
        }
        return calificadas;
    }

   
    public double obtenerNota(Entrega entrega) {
        Calificacion calificacion = entrega.getCalificacion();
        if (calificacion != null) {
            return calificacion.obtenerNota();
        }
        throw new IllegalArgumentException("La entrega no tiene calificacion");
    }
    
    public String obtenerComentario(Entrega entrega) {
        Calificacion calificacion = entrega.getCalificacion();
        if (calificacion != null) {
            return calificacion.obtenerComentario();
        }
        throw new IllegalArgumentException("La entrega no tiene comentario");
    }
    public List<Entrega> getEntregas() {
        return new ArrayList<>(entregas); 
    }

    public String getNombre() { 
        return nombre; 
    }

    public String getApellido() {
        return apellido; 
    }

    public List<Tarea> getTareasPendientes() {
        return new ArrayList<>(tareasPendientes); 
    }
}