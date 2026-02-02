package logica;

public class TipoRegistro {
	private String nombre;
	private String descripcion;
	private float costo;
	private int cupoMaximo;
	private int cupoDisponible;
	
	
	public TipoRegistro(String nombre, String desc, float cost, int cupoMax) {
		this.nombre = nombre;
		this.descripcion = desc;
		this.costo = cost;
		this.cupoMaximo = cupoMax;
		this.cupoDisponible = cupoMax; // precisamos que cupoDisp se pase al constructor??
	}
	
	//getters
	public String getNombre() {
		return nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public float getCosto() {
		return costo;
	}
	public int getCupoMaximo() {
		return cupoMaximo;
	}
	public int getCupoDisponible() {
		return cupoDisponible;
	}
	
	public void reducirCupo() {
	    if (cupoDisponible > 0) {
	        cupoDisponible--;
	    } else {
	        throw new IllegalStateException("No hay cupos disponibles");
	    }
	}
	
	public boolean hayCupoDisponible() {
	    return cupoDisponible > 0;
	}
}
