package logica;

import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class ManejadorUsuario {
	private Map<String, Usuario> usuariosNick;
	private Map<String, Usuario> usuariosCorreo;
	private static ManejadorUsuario instancia = null;
	
	private ManejadorUsuario() {
		usuariosNick = new HashMap<String, Usuario>();
		usuariosCorreo = new HashMap<String, Usuario>();
	}
	
	public static ManejadorUsuario getInstancia() {
		if (instancia == null)
			instancia = new ManejadorUsuario();
		return instancia;
	}
	
	public void addUsuario(Usuario usr) {
		String nick = usr.getNickname();
		usuariosNick.put(nick, usr);
	}
	
	public void addUsuarioC(Usuario usr) {
		String correo = usr.getCorreo();
		usuariosCorreo.put(correo, usr);
	}
	
	public Usuario findUsuario(String nick) {
		return (Usuario) usuariosNick.get(nick);
	}
	
	public Usuario findUsuarioCorreo(String correo) {
		return (Usuario) usuariosCorreo.get(correo);
	}
	
	public Set<Usuario> listarUsuarios(){
		Set<Usuario> resultado = new HashSet<>();
		if (usuariosNick.isEmpty())
			return resultado;
		else {
			usuariosNick.forEach((nick, u) -> {
				resultado.add(u);
			});
			return resultado;
		}
	}
	
	
	public void reiniciarSingleton() {
		usuariosNick = null;
		usuariosCorreo = null;
		instancia = null;
	}
	
	public void limpiar() {
	    this.usuariosNick.clear();
	    this.usuariosCorreo.clear();
	}
}


