package logica;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DTTipoRegistro {
	private String nombre;
	private String descripcion;
	private int cupoMax; // cupo máximo
	private int cupoDisp;
	private float costo;
	
	public DTTipoRegistro(TipoRegistro tipoReg) {
		this.setNombre(tipoReg.getNombre());
		this.setDescripcion(tipoReg.getDescripcion());
		this.setCupoMaximo(tipoReg.getCupoMaximo());
		this.setCupoDisponible(tipoReg.getCupoDisponible());
		this.setCosto(tipoReg.getCosto());
	}
	
	public DTTipoRegistro() {
		this.setNombre(null);
		this.setDescripcion(null);
		this.setCupoMaximo(0);
		this.setCupoDisponible(0);
		this.setCosto(0);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getCupoMaximo() {
		return cupoMax;
	}

	public void setCupoMaximo(int cupoMax) {
		this.cupoMax = cupoMax;
	}

	public int getCupoDisponible() {
		return cupoDisp;
	}

	public void setCupoDisponible(int cupoDisp) {
		this.cupoDisp = cupoDisp;
	}

	public float getCosto() {
		return costo;
	}

	public void setCosto(float costo) {
		this.costo = costo;
	}
	

}
