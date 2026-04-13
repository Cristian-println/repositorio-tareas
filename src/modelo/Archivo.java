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
        public void validarArchivo(double maxTamanioMB, java.util.List<String> formatosPermitidos) {
            if (this.tamanio <= 0) {
                throw new IllegalArgumentException("Error: El archivo no puede estar vacío (0 MB).");
            }
            if (this.tamanio > maxTamanioMB) {
                throw new IllegalArgumentException("Error: El tamaño del archivo excede el máximo permitido de " + maxTamanioMB + "MB.");
            }
            if (this.tipo == null || !formatosPermitidos.contains(this.tipo.toLowerCase())) {
                throw new IllegalArgumentException("Error: Formato de archivo no válido. Permitidos: " + formatosPermitidos);
            }
        }
       
}
