package logica;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.Set;

public class Evento {
	private String nombre;
	private String descripcion;
	private LocalDate fechaAlta;
	private String sigla;
	private String imagen;
	private int visitas;
	private boolean finalizado;
	private Set<String> categorias = new HashSet<>();
	private Map<String, Edicion> colEdiciones; // colección de Ediciones, mapeadas por su nombre
	
	// CONSTRUCTOR DE PRUEBA -- REEMPLAZAR
	public Evento(String nom, String desc, LocalDate fechaa, String sig, Set<String> cats, String imagen){
		this.nombre = nom;
		this.descripcion = desc;
		this.fechaAlta = fechaa;
		this.sigla = sig;
	    this.categorias = cats;
	    this.finalizado = false;
	    this.imagen = imagen;
	    this.visitas = 0;
	    this.colEdiciones = new TreeMap<>();
	}
	
	public void agregarEdicion(Edicion edition) {
	    colEdiciones.put(edition.getNombre(), edition);
	}

	public Edicion getEdicion(String edicion) {
		return colEdiciones.get(edicion);
	}
	
	public Set<String> listarEdiciones() {
		return colEdiciones.keySet();
	}
	
	public Set<Edicion> getEdiciones() {
		return new HashSet<>(colEdiciones.values());
	}
	
	public int getVisitas() {
	    return visitas;
	}

	public void incrementarVisitas() {
	    visitas++;
	}
	
	
	public String getNombre() {
        return nombre;
    }

    public String getDesc() {
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
    
    public boolean getFinalizado() {
    	return finalizado;
    }
    
    public void finalizar() {
    	this.finalizado = true;
    }
    
    public Set<String> getCategorias() {
        return categorias;
    }
    
    public DTEvento getDTEvent() {
    	DTEvento dte = new DTEvento(this.getNombre(), this.getDesc(), this.getFechaAlta(), this.getSigla(), this.getImagen(), this.getFinalizado(), this.getVisitas());
    	return dte;
    }
    
    public void addEdicion(Edicion edicion) {
    	this.colEdiciones.put(edicion.getNombre(), edicion);
	}
    
    public void setImagen(String imagen) {
    	this.imagen = imagen;
    }
}