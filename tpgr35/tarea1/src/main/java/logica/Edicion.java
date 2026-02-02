package logica;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import enumerados.EstadoEdicion;

public class Edicion {
	private String nombre;
	private String sigla;
	private LocalDate fechaIni;
	private LocalDate fechaFin;
	private LocalDate fechaAlta;
	private String ciudad;
	private String pais;
	private Evento evento;
	private Organizador organizador;
	private EstadoEdicion estado = EstadoEdicion.Ingresada; // por defecto al crearse está como Ingresada
	private String imagen = null; // guarda el nombre del archivo
	private String urlVideo = null;
	
	private Map<String, TipoRegistro> colTipoReg;
	
	public Edicion(Evento evento, String nombre, String sigla, 
			LocalDate fechaIni, LocalDate fechaFin, LocalDate fechaAlta, 
			String ciudad, String pais, Organizador organizador, EstadoEdicion estado, String imagen, String urlVideo) {
		this.evento = evento;
		this.nombre = nombre;
		this.sigla = sigla;
		this.fechaIni = fechaIni;
		this.fechaFin = fechaFin;
		this.fechaAlta = fechaAlta;
		this.ciudad = ciudad;
		this.pais = pais;
		this.organizador = organizador;
		this.colTipoReg = new TreeMap<>();
		this.estado = estado;
		this.imagen = imagen;
		this.urlVideo = urlVideo;
	}
	
	public Set<String> listarTipoRegistro() {
		return colTipoReg.keySet();
	}
	
	public void agregarTipoRegistro(String nombre, TipoRegistro nuevo) {
		colTipoReg.put(nombre, nuevo);
	}
	
	public DTTipoRegistro getDTTipoReg(String tipoReg) {
		return new DTTipoRegistro(colTipoReg.get(tipoReg));
	}
	
	public DTEdicion getDTEdicion() {
		return new DTEdicion(this);
	}
	
	// getters
	public Evento getEvento() {
        return evento;
    }
	public String getNombre() {
		return nombre;
	}
	public String getSigla() {
		return sigla;
	}
	public LocalDate getFechaIni() {
		return fechaIni;
	}
	public LocalDate getFechaFin() {
		return fechaFin;
	}
	public LocalDate getFechaAlta() {
		return fechaAlta;
	}
	public String getCiudad() {
		return ciudad;
	}
	public String getPais() {
		return pais;
	};
	public Organizador getOrganizador() {
		return organizador;
	}
	
	public TipoRegistro getTipoRegistro(String nombre) {
	    return colTipoReg.get(nombre);
	}

	public EstadoEdicion getEstado() {
		return estado;
	}
	
	public String getImagen() {
		return imagen;
	}
	
	public String getUrlVideo() {
		return urlVideo;
	}
	
	public void aceptar() {
		estado = EstadoEdicion.Aceptada;
	}
	
	public void rechazar() {
		estado = EstadoEdicion.Rechazada;
	}
	
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
}
