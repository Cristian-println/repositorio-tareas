package modelo;

public class Archivo {
	private String nombre;
    private String tipo;
    private String ruta;
    private double tamanio;
    public Archivo(String nombre, String tipo, String ruta, double tamanio) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.ruta = ruta;
        this.tamanio = tamanio;
        }
        public String getTipo() {
        return tipo;
        }
        public void setTipo(String tipo) {
        this.tipo = tipo;
        }
        public String getRuta() {
        return ruta;
        }
        public void setRuta(String ruta) {
        this.ruta = ruta;
        }
        public double getTamanio() {
        return tamanio;
        }
        public void setTamanio(double tamanio) {
        this.tamanio = tamanio;
        }
        public String getNombre() {
        return nombre;
        }
       
}
