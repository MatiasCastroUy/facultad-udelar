package logica;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DTUsuario {
	private String nickname;
	private String nombre;
	private String correo;
	private String imagenPerfil;
	
	public DTUsuario() {
        this.setNickname(new String());
        this.setNombre(new String());
        this.setCorreo(new String());
        this.setImagenPerfil("");
    }
    
    public DTUsuario(String nickname, String nombre, String correo) {
        this.setNickname(nickname);
        this.setNombre(nombre);
        this.setCorreo(correo);
        this.setImagenPerfil("");
    }
    
    // Constructor nuevo (con imagen)
    public DTUsuario(String nickname, String nombre, String correo, String imagenPerfil) {
        this.setNickname(nickname);
        this.setNombre(nombre);
        this.setCorreo(correo);
        this.setImagenPerfil(imagenPerfil);
    }
	
	public String getNickname() {
        return nickname;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }
    
    public String getImagenPerfil() {
        return imagenPerfil;
    }

    /* Sirve para mostrar textualmente la información del usuario, por ejemplo en un ComboBox
     */
    public String toString() {
        return getNickname() + " (" + getCorreo() + ")";
    }

    private void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private void setCorreo(String correo) {
        this.correo = correo;
    }
    
    private void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }
}

