package logica;

import java.util.HashSet;
import java.util.Set;

public class Usuario {
	private String nickname;
	private String nombre;
	private String correo;
	private String contrasena;
	private String imagenPerfil;
	private Set<String> usuariosSeguidos;
	
	
	// Constructor original (para compatibilidad con carga de datos)
	public Usuario(String nick, String nombr, String corr) {
		this.nickname = nick;
		this.nombre = nombr;
		this.correo = corr;
		// No asignar una contraseña por defecto. Dejar nula para obligar a
		// usar el constructor con contraseña o setContrasena cuando corresponda.
		this.contrasena = null;
		this.imagenPerfil = "";
		this.usuariosSeguidos = new HashSet<>();
	}
	
	public Usuario() {
		this.usuariosSeguidos = new HashSet<>();
    }
	
	// Constructor nuevo (con contraseña e imagen)
	public Usuario(String nick, String nombr, String corr, String contras, String imagen) {
		this.nickname = nick;
		this.nombre = nombr;
		this.correo = corr;
		this.contrasena = contras;
		this.imagenPerfil = imagen;
		this.usuariosSeguidos = new HashSet<>();
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
	
	public String getContrasena() {
		return contrasena;
	}
	
	public String getImagenPerfil() {
		return imagenPerfil;
	}
	
	public void setNickname(String nick) {
		this.nickname = nick;
	}
	
	public void setNombre(String nombr) {
		this.nombre = nombr;
	}
	
	public void setCorreo(String corr) {
		this.correo = corr;
	}
	
	public void setContrasena(String contras) {
		this.contrasena = contras;
	}
	
	public void setImagenPerfil(String imagen) {
		this.imagenPerfil = imagen;
	}
	
	public Set<String> getUsuariosSeguidos() {
		return usuariosSeguidos;
	}
	
	public void setUsuariosSeguidos(Set<String> usuariosSeguidos) {
		this.usuariosSeguidos = usuariosSeguidos;
	}
	
	// Métodos de conveniencia para el manejo de seguimiento
	public void seguirUsuario(String nickname) {
		this.usuariosSeguidos.add(nickname);
	}
	
	public void dejarDeSeguirUsuario(String nickname) {
		this.usuariosSeguidos.remove(nickname);
	}
	
	public boolean sigueA(String nickname) {
		return this.usuariosSeguidos.contains(nickname);
	}
}

