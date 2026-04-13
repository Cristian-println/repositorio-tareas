package vista;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
 
/**
 * US7 - Notificaciones (GUI Principal - Cristian)
 * Panel que muestra las notificaciones del estudiante (tareas asignadas y calificadas).
 */
public class PanelNotificaciones extends JPanel {
 
    private JPanel panelLista;
    private JLabel lblContador;
    private JScrollPane scroll;
 
    private static class Notificacion {
        String tipo, titulo, fecha, hora;
        boolean leida;
        Notificacion(String tipo, String titulo, String fecha, String hora, boolean leida) {
            this.tipo = tipo; this.titulo = titulo;
            this.fecha = fecha; this.hora = hora; this.leida = leida;
        }
    }
 
    private final List<Notificacion> notificaciones = new ArrayList<>();
 
    public PanelNotificaciones() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        cargarNotificacionesEjemplo();
        inicializarComponentes();
    }
 
    private void cargarNotificacionesEjemplo() {
        notificaciones.add(new Notificacion("ASIGNADA",   "Actividad 1 - SQL fue asignada",          "11/06/2025", "08:00", false));
        notificaciones.add(new Notificacion("CALIFICADA", "Tarea 2 - Procesos fue calificada",       "10/06/2025", "15:30", false));
        notificaciones.add(new Notificacion("PLAZO",      "Vence en 24h: Práctica 3 - Redes",        "09/06/2025", "09:00", true));
        notificaciones.add(new Notificacion("ASIGNADA",   "Tarea 3 - Semáforos fue asignada",        "07/06/2025", "10:00", true));
        notificaciones.add(new Notificacion("CALIFICADA", "Actividad 2 - BD fue calificada",         "06/06/2025", "16:45", true));
    }
 
    private void inicializarComponentes() {
        // --- Encabezado con ícono y contador ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(0, 0, 10, 0));
 
        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        izq.setBackground(Color.WHITE);
 
        JLabel lblIcono = new JLabel("🔔");
        lblIcono.setFont(new Font("Arial", Font.PLAIN, 24));
 
        JLabel lblTitulo = new JLabel("Notificaciones");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 100, 100));
 
        long noLeidas = notificaciones.stream().filter(n -> !n.leida).count();
        lblContador = new JLabel(noLeidas > 0 ? "  " + noLeidas + " sin leer" : "");
        lblContador.setFont(new Font("Arial", Font.BOLD, 12));
        lblContador.setForeground(Color.WHITE);
        lblContador.setOpaque(true);
        lblContador.setBackground(new Color(200, 60, 60));
        lblContador.setBorder(new EmptyBorder(2, 7, 2, 7));
 
        izq.add(lblIcono);
        izq.add(lblTitulo);
        izq.add(lblContador);
        header.add(izq, BorderLayout.WEST);
 
        add(header, BorderLayout.NORTH);
 
        // --- Lista de notificaciones ---
        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(Color.WHITE);
 
        if (notificaciones.isEmpty()) {
            JLabel lblVacio = new JLabel("No tienes notificaciones por el momento.");
            lblVacio.setFont(new Font("Arial", Font.ITALIC, 14));
            lblVacio.setForeground(Color.GRAY);
            lblVacio.setBorder(new EmptyBorder(20, 10, 0, 0));
            panelLista.add(lblVacio);
        } else {
            for (Notificacion n : notificaciones) {
                panelLista.add(crearTarjetaNotificacion(n));
                panelLista.add(Box.createVerticalStrut(6));
            }
        }
 
        scroll = new JScrollPane(panelLista);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        add(scroll, BorderLayout.CENTER);
    }
 
    private JPanel crearTarjetaNotificacion(Notificacion n) {
        JPanel card = new JPanel(new BorderLayout(10, 4));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        card.setBackground(n.leida ? new Color(248, 248, 248) : new Color(235, 248, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, obtenerColorTipo(n.tipo)),
            new EmptyBorder(10, 14, 10, 14)));
 
        // Ícono de tipo
        JLabel lblIcono = new JLabel(obtenerIconoTipo(n.tipo));
        lblIcono.setFont(new Font("Arial", Font.PLAIN, 20));
        lblIcono.setPreferredSize(new Dimension(32, 32));
 
        // Contenido
        JPanel contenido = new JPanel(new GridLayout(2, 1, 0, 2));
        contenido.setOpaque(false);
 
        JLabel lblTexto = new JLabel(n.titulo);
        lblTexto.setFont(new Font("Arial", n.leida ? Font.PLAIN : Font.BOLD, 13));
        lblTexto.setForeground(new Color(40, 40, 40));
 
        JLabel lblFecha = new JLabel(n.fecha + "  " + n.hora);
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 11));
        lblFecha.setForeground(Color.GRAY);
 
        contenido.add(lblTexto);
        contenido.add(lblFecha);
 
        // Indicador no leída
        JLabel lblNoLeido = new JLabel(n.leida ? "" : "●");
        lblNoLeido.setFont(new Font("Arial", Font.BOLD, 14));
        lblNoLeido.setForeground(new Color(0, 100, 200));
        lblNoLeido.setPreferredSize(new Dimension(20, 20));
 
        card.add(lblIcono,   BorderLayout.WEST);
        card.add(contenido,  BorderLayout.CENTER);
        card.add(lblNoLeido, BorderLayout.EAST);
 
        return card;
    }
 
    private Color obtenerColorTipo(String tipo) {
        switch (tipo) {
            case "ASIGNADA":   return new Color(70, 130, 180);
            case "CALIFICADA": return new Color(0, 150, 100);
            case "PLAZO":      return new Color(210, 100, 0);
            default:           return Color.GRAY;
        }
    }
 
    private String obtenerIconoTipo(String tipo) {
        switch (tipo) {
            case "ASIGNADA":   return "📋";
            case "CALIFICADA": return "✅";
            case "PLAZO":      return "⏰";
            default:           return "🔔";
        }
    }
}
