package modelo;

import java.util.ArrayList;
import java.util.List;

public class Estudiante {

    private String nombre;
    private String apellido;
    private List<Tarea> tareasPendientes;
    private List<Entrega> entregas;
    private int cantidadCalificaciones = 0;

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
    
    public List<Tarea> getTareasPendientes() {
        return new ArrayList<>(tareasPendientes); 
    }
    
    public List<Tarea> filtrarTareasPorMateria(String materia) {
        List<Tarea> resultado = new ArrayList<>();

        for (Tarea t : tareasPendientes) {
            if (t.getMateria().equalsIgnoreCase(materia)) {
                resultado.add(t);
            }
        }

        return resultado;
    }
    public List<Tarea> ordenarTareasPorFecha() {
        List<Tarea> copia = new ArrayList<>(tareasPendientes);

        for (int i = 0; i < copia.size() - 1; i++) {
            for (int j = 0; j < copia.size() - 1 - i; j++) {

                Tarea t1 = copia.get(j);
                Tarea t2 = copia.get(j + 1);

                if (!t1.getFecha().estaAntesDeLaFechaAsignada(t2.getFecha())) {
                    // intercambio
                    copia.set(j, t2);
                    copia.set(j + 1, t1);
                }
            }
        }

        return copia;
    }
    public List<Entrega> ordenarEntregasPorEstado() {
        List<Entrega> copia = new ArrayList<>(entregas);

        for (int i = 0; i < copia.size() - 1; i++) {
            for (int j = 0; j < copia.size() - 1 - i; j++) {

                Entrega e1 = copia.get(j);
                Entrega e2 = copia.get(j + 1);

                // false primero, true después
                if (e1.getEstado() && !e2.getEstado()) {
                    copia.set(j, e2);
                    copia.set(j + 1, e1);
                }
            }
        }

        return copia;
    }
    
    public List<Calificacion> obtenerCalificaciones() {
        List<Calificacion> resultado = new ArrayList<>();

        for (int i = 0; i < entregas.size(); i++) {
            Entrega e = entregas.get(i);

            if (e.getCalificacion() != null) {
                resultado.add(e.getCalificacion());
            }
        }

        return resultado;
    }
    
    public double calcularPromedio() {
        double suma = 0;
        int contador = 0;

        for (int i = 0; i < entregas.size(); i++) {
            Entrega e = entregas.get(i);

            if (e.getCalificacion() != null) {
                suma += e.getCalificacion().obtenerNota();
                contador++;
            }
        }

        if (contador == 0) {
            return 0;
        }

        return suma / contador;
    }
    
    public double actualizarPromedioSiHayNuevaNota() {
        int actuales = 0;

        for (int i = 0; i < entregas.size(); i++) {
            if (entregas.get(i).getCalificacion() != null) {
                actuales++;
            }
        }

        if (actuales > cantidadCalificaciones) {
            cantidadCalificaciones = actuales;
            return calcularPromedio();
        }

        return -1; //no hubo cambios
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
    public java.util.List<Tarea> verMisTareasValidadas() {
        if (this.tareasPendientes == null || this.tareasPendientes.isEmpty()) {
            throw new IllegalStateException("US6 Aviso: No tienes tareas asignadas actualmente.");
        }
        return this.verMisTareas(); 
    }

   
    public java.util.List<Entrega> obtenerEntregasCalificadasValidadas() {
        java.util.List<Entrega> calificadas = this.obtenerEntregasCalificadas(); 
        if (calificadas == null || calificadas.isEmpty()) {
            throw new IllegalStateException("US4 Aviso: No hay tareas calificadas para mostrar.");
        }
        return calificadas;
    }

 
    public double calcularPromedioValidado() {
        boolean tieneNotas = false;
        for (Entrega e : this.entregas) {
            if (e.getCalificacion() != null) {
                tieneNotas = true;
                break;
            }
        }
        if (!tieneNotas) {
            throw new IllegalStateException("US8 Aviso: El estudiante " + this.nombre + " no tiene notas registradas.");
        }
        return this.calcularPromedio();
    }

    
}