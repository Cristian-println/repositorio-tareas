package modelo;
import java.util.ArrayList;
import java.util.List;
public class GestorTarea {
	private List<Tarea> tareas = new ArrayList<>();

	   public GestorTarea() {
	   }

	   public void agregar(Tarea tarea) {
	      tareas.add(tarea);
	   }

	   public List<Tarea> obtenerTodas() {
	      return new ArrayList<>(tareas);
	   }

	   public boolean existe(Tarea tarea) {
	      return tareas.contains(tarea);
	   }

}