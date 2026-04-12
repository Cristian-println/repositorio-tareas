package modelo;

public class Calificacion {
		
	    private double nota;
	    private String comentario;
	   

	    public Calificacion( double nota, String comentario ) {
	        
	        this.nota = nota;
	        this.comentario = comentario;
	        
	    }

	    public double obtenerNota() { 
	    	return nota; 
	    	}
	    public String obtenerComentario() { 
	    	return comentario; 
	    	}
	   
	}


