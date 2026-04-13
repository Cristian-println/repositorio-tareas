package modelo;

public class Fecha {
	private int dia;
    private int mes;
    private int anio;
    private int hora;
    private int minuto;

    public Fecha(int dia, int mes, int anio, int hora, int minuto) {
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
        this.hora = hora;
        this.minuto = minuto;
    }

    public boolean estaAntesDeLaFechaAsignada(Fecha fecha) {
        if (this.anio < fecha.getAnio()) {
            return true;
        } else if (this.anio > fecha.getAnio()) {
            return false;
        }

        if (this.mes < fecha.getMes()) {
            return true;
        } else if (this.mes >fecha.getMes()) {
            return false;
        }

        if (this.dia < fecha.getDia()) {
            return true;
        } else if (this.dia > fecha.getDia()) {
            return false;
        }

        if (this.hora < fecha.getHora()) {
            return true;
        } else if (this.hora > fecha.getHora()) {
            return false;
        }

        if (this.minuto < fecha.getMinuto()) {
            return true;
        } else {
            return false;
        }
    }

    
    public int getDia() { 
        return dia; 
    }
    public int getMes() { 
        return mes; 
    }
    public int getAnio() {
        return anio;
     }
    public int getHora() { 
        return hora; 
    }
    public int getMinuto() { 
        return minuto; 
    }
    public void validarCalendario() {
        if (this.mes < 1 || this.mes > 12) {
            throw new IllegalArgumentException("Error: El mes debe estar entre 1 y 12.");
        }
        if (this.dia < 1 || this.dia > 31) {
            throw new IllegalArgumentException("Error: El día debe estar entre 1 y 31.");
        }
        if (this.hora < 0 || this.hora > 23 || this.minuto < 0 || this.minuto > 59) {
            throw new IllegalArgumentException("Error: Formato de tiempo inválido (Horas 0-23, Minutos 0-59).");
        }
    }

}
