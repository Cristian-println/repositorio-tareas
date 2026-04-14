package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Entrega {

    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public enum Estado { ENTREGADA, CALIFICADA }

    private int           id;
    private int           tareaId;
    private String        tareaTitulo;
    private int           estudianteId;
    private String        estudianteNombre;
    private String        archivoRuta;
    private String        comentarioEstudiante;
    private LocalDateTime fechaEntrega;
    private boolean       esTardio;
    private Estado        estado;

    private Double        nota;
    private String        comentarioDocente;
    private double        calificacionMaxima;

    public Entrega() {}

    public Entrega(int tareaId, int estudianteId, String archivoRuta,
                   String comentarioEstudiante, boolean esTardio) {
        this.tareaId              = tareaId;
        this.estudianteId         = estudianteId;
        this.archivoRuta          = archivoRuta;
        this.comentarioEstudiante = comentarioEstudiante;
        this.esTardio             = esTardio;
        this.estado               = Estado.ENTREGADA;
    }

    public int    getId()            { return id; }
    public void   setId(int id)       { this.id = id; }

    public int    getTareaId()        { return tareaId; }
    public void   setTareaId(int t)   { this.tareaId = t; }

    public String getTareaTitulo()    { return tareaTitulo; }
    public void   setTareaTitulo(String t) { this.tareaTitulo = t; }

    public int    getEstudianteId()   { return estudianteId; }
    public void   setEstudianteId(int e) { this.estudianteId = e; }

    public String getEstudianteNombre() { return estudianteNombre; }
    public void   setEstudianteNombre(String e) { this.estudianteNombre = e; }

    public String getArchivoRuta()    { return archivoRuta; }
    public void   setArchivoRuta(String a) { this.archivoRuta = a; }

    public String getComentarioEstudiante() { return comentarioEstudiante; }
    public void   setComentarioEstudiante(String c) { this.comentarioEstudiante = c; }

    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime f) { this.fechaEntrega = f; }

    public boolean isEsTardio()       { return esTardio; }
    public void    setEsTardio(boolean e) { this.esTardio = e; }

    public Estado  getEstado()        { return estado; }
    public void    setEstado(Estado e) { this.estado = e; }

    public Double  getNota()           { return nota; }
    public void    setNota(Double n)   { this.nota = n; }

    public String  getComentarioDocente() { return comentarioDocente; }
    public void    setComentarioDocente(String c) { this.comentarioDocente = c; }

    public double  getCalificacionMaxima() { return calificacionMaxima; }
    public void    setCalificacionMaxima(double c) { this.calificacionMaxima = c; }

    public String  getFechaEntregaFormateada() {
        return fechaEntrega != null ? fechaEntrega.format(FMT) : "";
    }

    public String  getEstadoTexto() {
        if (nota != null) return "Calificada";
        return esTardio ? "Entregada (tardía)" : "Entregada";
    }

    @Override
    public String toString() {
        return "Entrega[" + estudianteNombre + " - " + tareaTitulo + "]";
    }
}
