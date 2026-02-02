package logica;

public class Organizador extends Usuario{
	private String descripcion;
	private String sitioWeb;
	
	
	public Organizador(String nick, String nombr, String corr, String descrp, String sitioW) {
		super(nick, nombr, corr);
		this.descripcion = descrp;
		this.sitioWeb = sitioW;
	}
	
	// Constructor nuevo (con contraseña e imagen)
	public Organizador(String nick, String nombr, String corr, String contras, String imagen, String descrp, String sitioW) {
		super(nick, nombr, corr, contras, imagen);
		this.descripcion = descrp;
		this.sitioWeb = sitioW;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public String getSitioWeb() {
		return sitioWeb;
	}
	
	public void setDescripcion(String descrp) {
		this.descripcion = descrp;
	}
	
	public void setSitioWeb(String sitioW) {
		this.sitioWeb = sitioW;
	}
}
