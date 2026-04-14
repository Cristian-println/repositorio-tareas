package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tarea {

    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int           id;
    private String        titulo;
    private String        descripcion;
    private LocalDateTime fechaLimite;
    private double        calificacionMaxima;
    private String        archivoAdjunto;
    private int           materiaId;
    private String        materiaNombre;
    private int           docenteId;
    private String        docenteNombre;
    private LocalDateTime fechaCreacion;

    public Tarea() {}

    public Tarea(String titulo, String descripcion, LocalDateTime fechaLimite,
                 double calificacionMaxima, String archivoAdjunto,
                 int materiaId, int docenteId) {
        this.titulo             = titulo;
        this.descripcion        = descripcion;
        this.fechaLimite        = fechaLimite;
        this.calificacionMaxima = calificacionMaxima;
        this.archivoAdjunto     = archivoAdjunto;
        this.materiaId          = materiaId;
        this.docenteId          = docenteId;
    }

    public int           getId()                  { return id; }
    public void          setId(int id)             { this.id = id; }

    public String        getTitulo()               { return titulo; }
    public void          setTitulo(String t)        { this.titulo = t; }

    public String        getDescripcion()           { return descripcion; }
    public void          setDescripcion(String d)   { this.descripcion = d; }

    public LocalDateTime getFechaLimite()            { return fechaLimite; }
    public void          setFechaLimite(LocalDateTime f) { this.fechaLimite = f; }

    public double        getCalificacionMaxima()     { return calificacionMaxima; }
    public void          setCalificacionMaxima(double c) { this.calificacionMaxima = c; }

    public String        getArchivoAdjunto()         { return archivoAdjunto; }
    public void          setArchivoAdjunto(String a)  { this.archivoAdjunto = a; }

    public int           getMateriaId()              { return materiaId; }
    public void          setMateriaId(int m)          { this.materiaId = m; }

    public String        getMateriaNombre()           { return materiaNombre; }
    public void          setMateriaNombre(String m)   { this.materiaNombre = m; }

    public int           getDocenteId()              { return docenteId; }
    public void          setDocenteId(int d)          { this.docenteId = d; }

    public String        getDocenteNombre()           { return docenteNombre; }
    public void          setDocenteNombre(String d)   { this.docenteNombre = d; }

    public LocalDateTime getFechaCreacion()           { return fechaCreacion; }
    public void          setFechaCreacion(LocalDateTime f) { this.fechaCreacion = f; }

    public boolean estaVencida() {
        return LocalDateTime.now().isAfter(fechaLimite);
    }

    public String getFechaLimiteFormateada() {
        return fechaLimite != null ? fechaLimite.format(FMT) : "";
    }

    @Override
    public String toString() { return titulo; }
}
