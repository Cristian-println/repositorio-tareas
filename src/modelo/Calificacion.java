package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Calificacion {

    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int           id;
    private int           entregaId;
    private double        nota;
    private String        comentario;
    private LocalDateTime fechaCalificacion;

    public Calificacion() {}

    public Calificacion(int entregaId, double nota, String comentario) {
        this.entregaId  = entregaId;
        this.nota       = nota;
        this.comentario = comentario;
    }

    public int    getId()            { return id; }
    public void   setId(int id)       { this.id = id; }

    public int    getEntregaId()      { return entregaId; }
    public void   setEntregaId(int e) { this.entregaId = e; }

    public double getNota()           { return nota; }
    public void   setNota(double n)   { this.nota = n; }

    public String getComentario()     { return comentario; }
    public void   setComentario(String c) { this.comentario = c; }

    public LocalDateTime getFechaCalificacion() { return fechaCalificacion; }
    public void setFechaCalificacion(LocalDateTime f) { this.fechaCalificacion = f; }

    public String getFechaFormateada() {
        return fechaCalificacion != null ? fechaCalificacion.format(FMT) : "";
    }

    @Override
    public String toString() {
        return String.format("Nota: %.2f", nota);
    }
}
