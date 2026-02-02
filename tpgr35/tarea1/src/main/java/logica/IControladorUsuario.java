package logica;

import excepciones.ContrasenaInvalidaException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioCorreoRepetidoException;
import excepciones.UsuarioRepetidoException;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public interface IControladorUsuario {

	public abstract void registrarUsuario(String nickname, String nombre, String correo, boolean esAsistente, String apellido, LocalDate fechaNacimientoU, String descripcion, String sitioWeb) throws UsuarioRepetidoException, UsuarioCorreoRepetidoException;

	// Método nuevo (con contraseña e imagen)
	public abstract void registrarUsuario(String nickname, String nombre, String correo, String contrasena, String confirmacionContrasena, String imagenPerfil, boolean esAsistente, String apellido, LocalDate fechaNacimientoU, String descripcion, String sitioWeb) throws UsuarioRepetidoException, UsuarioCorreoRepetidoException, ContrasenaInvalidaException;

	public abstract DTUsuario verInfoUsuario(String nickname, boolean esAsistente) throws UsuarioNoExisteException;
	
	public abstract Set<DTUsuario> listarUsuarios() throws UsuarioNoExisteException;
	
	public abstract Set<DTOrganizador> listarOrganizadores() throws UsuarioNoExisteException;
	
	public abstract Set<DTAsistente> listarAsistentes() throws UsuarioNoExisteException;
	
	public abstract Set<String> listarNombresRegistrosDeUsuario(String nickname)
		    throws excepciones.UsuarioNoExisteException;
	
	public abstract DTRegistro obtenerRegistroDeUsuario(String nickname, String nombreEdicion)
		    throws excepciones.UsuarioNoExisteException;
	
	public abstract Map<String, DTRegistro> listarRegistrosEdicion(String nombreEdicion)
			throws UsuarioNoExisteException;
	
	public abstract DTOrganizador obtenerInfoOrganizador(String nickname)
			throws UsuarioNoExisteException;
	
	public abstract DTUsuario getUsuario(String nickname) throws UsuarioNoExisteException;
	
	public abstract DTUsuario getUsuarioPorCorreo(String correo) throws UsuarioNoExisteException;
	
	public abstract String obtenerNicknamePorEmail(String email) throws UsuarioNoExisteException;
	 
	public abstract boolean verificarContrasena(String nickname, String contrasena) throws UsuarioNoExisteException;
	
	public abstract String obtenerRol(String nickname) throws UsuarioNoExisteException;

	public void modificarUsuario(String nickname, String nombre, String nuevaContrasena, LocalDate fechaNacimiento, String descripcion, String sitioWeb, String imagenPerfil) throws UsuarioNoExisteException, ContrasenaInvalidaException;
	
	public void modificarUsuario(String nickname, String nombre, String nuevaContrasena, LocalDate fechaNacimiento, String descripcion, String sitioWeb, String imagenPerfil, String apellido) throws UsuarioNoExisteException, ContrasenaInvalidaException;

	// Métodos para seguimiento de usuarios
	public void seguirUsr(String seguidor, String seguido) throws UsuarioNoExisteException;
	
	public void dejarDeSeguirUsr(String seguidor, String seguido) throws UsuarioNoExisteException;
	
	public boolean verificarSiSigue(String seguidor, String seguido) throws UsuarioNoExisteException;
	
	public Set<DTUsuario> obtenerUsuariosSeguidos(String nickname) throws UsuarioNoExisteException;
	
	
	public byte[] getImagenUsuario(String nickname) throws UsuarioNoExisteException;
	
	public void setImagenUsuario(String nickname, byte[] imagen);
}
