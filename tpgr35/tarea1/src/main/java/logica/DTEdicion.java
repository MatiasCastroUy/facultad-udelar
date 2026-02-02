package logica;

import java.time.LocalDate;

import enumerados.EstadoEdicion;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DTEdicion {
	
	private String nombre;
	private String sigla;
	private String pais;
	private String ciudad;
	private String nicknameOrganizador;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private LocalDate fechaAlta;
	private EstadoEdicion estado;
	private String imagen;
	private String urlVideo;
	
//	public DTEdicion(String nombre, String sigla, String pais, String ciudad, String nicknameOrganizador, LocalDate fechaInicio, LocalDate fechaFin, LocalDate fechaAlta) {
//		this.nombre = nombre;
//		this.sigla = sigla;
//		this.pais = pais;
//		this.ciudad = ciudad;
//		this.nicknameOrganizador = nicknameOrganizador;
//		this.fechaInicio = fechaInicio;
//		this.fechaFin = fechaFin;
//		this.fechaAlta = fechaAlta;
//	}
	
	public DTEdicion(Edicion edition) {
		this.setNombre(edition.getNombre());
		this.setSigla(edition.getSigla());
		this.setPais(edition.getPais());
		this.setCiudad(edition.getCiudad());
		this.setNicknameOrganizador(edition.getOrganizador().getNickname());
		this.setFechaInicio(edition.getFechaIni());
		this.setFechaFin(edition.getFechaFin());
		this.setFechaAlta(edition.getFechaAlta());
		this.setEstado(edition.getEstado());
		this.setImagen(edition.getImagen());
		this.setUrlVideo(edition.getUrlVideo());
	}
	
	public DTEdicion() {
		this.nombre = null;
		this.sigla = null;
		this.pais = null;
		this.ciudad = null;
		this.nicknameOrganizador = null;
		this.fechaInicio = null;
		this.fechaFin = null;
		this.fechaAlta = null;
		this.estado = null;
		this.imagen = null;
		this.urlVideo = null;
	}
	
	// Getters
	
	public String getNombre() {
		return nombre;
	}
	
	public String getSigla() {
		return sigla;
	}
	
	public String getPais() {
		return pais;
	}
	
	public String getCiudad() {
		return ciudad;
	}
	
	public String getNicknameOrganizador() {
		return nicknameOrganizador;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}
	
	public LocalDate getFechaFin() {
		return fechaFin;
	}
	
	public LocalDate getFechaAlta() {
		return fechaAlta;
	}
	
	public String getEstado() {
		return estado.toString();
	}
	
	public String getImagen() {
		return imagen;
	}
	
	public String getUrlVideo() {
		return urlVideo;
	}

	// setters
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public void setNicknameOrganizador(String nicknameOrganizador) {
		this.nicknameOrganizador = nicknameOrganizador;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public void setEstado(EstadoEdicion estado) {
		this.estado = estado;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public void  setUrlVideo(String urlVideo) {
		this.urlVideo = urlVideo;
	}
	

}
