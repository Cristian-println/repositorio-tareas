import java.util.ArrayList;
import java.util.List;

public class Estudiante {
    private String nombre;
    private String apellido;
    private ArrayList<Tarea> tareasPendientes;

    public Estudiante(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.tareasPendientes = new ArrayList<>();
    }

    public List<Tarea> verMisTareas() {
        return new ArrayList<>(tareasPendientes);
    }

   
    public String getNombre() { 
        return nombre; 
    }
    public String getApellido() {
         return apellido; 
    }
    public ArrayList<Tarea> getTareasPendientes() {
         return tareasPendientes; 
    }
}

