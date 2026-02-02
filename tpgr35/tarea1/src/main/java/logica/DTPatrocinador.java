package logica;

import enumerados.Nivel;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DTPatrocinador {

	private String nombre;
	private Float montoAportado;
	private Integer cantidadRegistrosGratis;
	private String codigo;
	private Nivel nivel;
	
	public DTPatrocinador(String nombre, Float montoAportado, Integer cantidadRegistrosGratis, String codigo, Nivel nivel) {
		this.setNombre(nombre);
		this.setMontoAportado(montoAportado);
		this.setCantidadRegistrosGratis(cantidadRegistrosGratis);
		this.setCodigo(codigo);
		this.setNivel(nivel);
	}
	
	public DTPatrocinador() {
		this.setNombre(null);
		this.setMontoAportado(null);
		this.setCantidadRegistrosGratis(null);
		this.setCodigo(null);
		this.setNivel(null);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Float getMontoAportado() {
		return montoAportado;
	}

	public void setMontoAportado(Float montoAportado) {
		this.montoAportado = montoAportado;
	}

	public Integer getCantidadRegistrosGratis() {
		return cantidadRegistrosGratis;
	}

	public void setCantidadRegistrosGratis(Integer cantidadRegistrosGratis) {
		this.cantidadRegistrosGratis = cantidadRegistrosGratis;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Nivel getNivel() {
		return nivel;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}
	
}
