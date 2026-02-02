package logica;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DTOrganizador extends DTUsuario{
	private String descripcion;
	private String sitioWeb;
	
	
	public DTOrganizador() {
		this.setDescripcion(new String());
		this.setSitioWeb(new String());
	}
	
	public DTOrganizador(String nickname, String nombre, String correo, String descripcion, String sitioWeb) {
		super(nickname, nombre , correo);
		this.setDescripcion(descripcion);
		this.setSitioWeb(sitioWeb);
	}
	
	// Constructor nuevo (con imagen)
	public DTOrganizador(String nickname, String nombre, String correo, String imagenPerfil, String descripcion, String sitioWeb) {
		super(nickname, nombre , correo, imagenPerfil);
		this.setDescripcion(descripcion);
		this.setSitioWeb(sitioWeb);
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public String getSitioWeb() {
		return sitioWeb;
	}
	
	private void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	private void setSitioWeb(String sitioWeb) {
		this.sitioWeb = sitioWeb;
	}
}
