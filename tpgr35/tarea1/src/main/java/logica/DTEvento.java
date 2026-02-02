package logica;

import java.time.LocalDate;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class DTEvento {
	
	private String nombre;
	private String descripcion;
	@XmlJavaTypeAdapter(AdaptadorLocalDate.class)
	private LocalDate fechaAlta;
	private String sigla;
	private String imagen;
	private int visitas;
	private boolean finalizado;

	
	public DTEvento(String nom, String desc, LocalDate fechaa, String sig, String imagen, boolean estado, int visitas) {
		   this.setNombre(nom);
           this.setDescripcion(desc);
           this.setFechaAlta(fechaa);
           this.setSigla(sig);
           this.setImagen(imagen);
           this.setFinalizado(estado);
           this.setVisita(visitas);
	}
	
	public DTEvento() {
		this.setNombre(null);
        this.setDescripcion(null);
        this.setFechaAlta(null);
        this.setSigla(null);
        this.setImagen(null);
        this.setFinalizado(false);
        this.setVisita(0);
	}
	
	public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getSigla() {
        return sigla;
    }
    
    public LocalDate getFechaAlta() {
        return fechaAlta;
    }
    
    public String getImagen() {
    	return imagen;
    }
    
    public int getVisitas() {
    	return visitas;
    }
    
    public boolean getFinalizado() {
    	return finalizado;
    }

	
	public void setNombre(String nombre) {
        this.nombre = nombre;
    }
	
	public void setDescripcion(String desc) {
        this.descripcion = desc;
    }
	
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public void setFechaAlta(LocalDate fechaAlt) {
        this.fechaAlta = fechaAlt;
    }
	
	public void setSigla(String sig) {
        this.sigla = sig;
    }
	
	public void setVisita(int visits) {
        this.visitas = visits;
    }
	
	public void setFinalizado(boolean estado) {
        this.finalizado = estado;
    }
	
}
