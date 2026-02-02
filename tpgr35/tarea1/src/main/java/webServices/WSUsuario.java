package webServices;

import java.time.LocalDate;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import excepciones.ContrasenaInvalidaException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioCorreoRepetidoException;
import excepciones.UsuarioRepetidoException;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.ParameterStyle;
import jakarta.jws.soap.SOAPBinding.Style;
import jakarta.xml.ws.Endpoint;
import logica.DTUsuario;
import logica.DTOrganizador;
import logica.DTRegistro;
import logica.Fabrica;
import logica.IControladorUsuario;
//import java.util.Map;

@WebService
@SOAPBinding(style = Style.RPC, parameterStyle = ParameterStyle.WRAPPED)
public class WSUsuario {
	private String url = null;

    private Endpoint endpoint = null;

    public WSUsuario() {}
    
    public WSUsuario(String url) {
    	this.url = url;
    }

    @WebMethod(exclude = true)
    public void publish() {
        endpoint = Endpoint.publish(url, this);
    }

    @WebMethod(exclude = true)
    public Endpoint getEndpoint() {
        return endpoint;
    }
    
    @WebMethod
    public String ping() {
        return "WSUsuario activo";
    }
  
    @WebMethod
    public String obtenerNicknamePorEmail(String email) throws UsuarioNoExisteException {
        IControladorUsuario cUsr = Fabrica.getInstance().getIControladorUsuario();
        return cUsr.obtenerNicknamePorEmail(email);
    }

    @WebMethod
    public boolean verificarContrasena(String nick, String contrasena) throws UsuarioNoExisteException {
        IControladorUsuario cUsr = Fabrica.getInstance().getIControladorUsuario();
        return cUsr.verificarContrasena(nick, contrasena);
    }

    @WebMethod
    public String obtenerRol(String nick) throws UsuarioNoExisteException {
        IControladorUsuario cUsr = Fabrica.getInstance().getIControladorUsuario();
        return cUsr.obtenerRol(nick);
    }


    @WebMethod
	public void registrarUsuario(String nickname, String nombre, String correo, boolean esAsistente, String apellido, String fechaNacimientoStr, String descripcion, String sitioWeb) throws UsuarioRepetidoException, UsuarioCorreoRepetidoException {
		IControladorUsuario ctrUsr = Fabrica.getInstance().getIControladorUsuario();

        LocalDate fechaNacimiento = null;
        if (fechaNacimientoStr != null && !fechaNacimientoStr.trim().isEmpty()) {
            fechaNacimiento = LocalDate.parse(fechaNacimientoStr);
        }

        ctrUsr.registrarUsuario(nickname, nombre, correo, esAsistente, apellido, fechaNacimiento, descripcion, sitioWeb);
	}

	@WebMethod
	public void registrarUsuarioConContrasena(String nickname, String nombre, String correo, String contrasena, String confirmacionContrasena, String imagenPerfil, boolean esAsistente, String apellido, String fechaNacimientoStr, String descripcion, String sitioWeb) throws UsuarioRepetidoException, UsuarioCorreoRepetidoException, ContrasenaInvalidaException {
		IControladorUsuario ctrUsr = Fabrica.getInstance().getIControladorUsuario();

        LocalDate fechaNacimiento = null;
        if (fechaNacimientoStr != null && !fechaNacimientoStr.trim().isEmpty()) {
            fechaNacimiento = LocalDate.parse(fechaNacimientoStr);
        }

        ctrUsr.registrarUsuario(nickname, nombre, correo, contrasena, confirmacionContrasena, imagenPerfil, esAsistente, apellido, fechaNacimiento, descripcion, sitioWeb);
	}

	@WebMethod
	public DTUsuario verInfoUsuario(String nickname, boolean esAsistente) throws UsuarioNoExisteException {
		IControladorUsuario ctrUsr = Fabrica.getInstance().getIControladorUsuario();
		return ctrUsr.verInfoUsuario(nickname, esAsistente);
	}

	@WebMethod
	public SetOfDTUsuario listarUsuarios() throws UsuarioNoExisteException {
		IControladorUsuario ctrUsr = Fabrica.getInstance().getIControladorUsuario();
		return new SetOfDTUsuario(ctrUsr.listarUsuarios());
	}

	@WebMethod
	public SetOfDTAsistente listarAsistentes() throws UsuarioNoExisteException {
		IControladorUsuario ctrUsr = Fabrica.getInstance().getIControladorUsuario();
		return new SetOfDTAsistente(ctrUsr.listarAsistentes());
	}

	@WebMethod
	public SetOfDTOrganizador listarOrganizadores() throws UsuarioNoExisteException {
		IControladorUsuario ctrUsr = Fabrica.getInstance().getIControladorUsuario();
		return new SetOfDTOrganizador(ctrUsr.listarOrganizadores());
	}

	@WebMethod
	public ListOfString listarNombresRegistrosDeUsuario(String nickname) throws UsuarioNoExisteException {
		IControladorUsuario ctrUsr = Fabrica.getInstance().getIControladorUsuario();
		return new ListOfString(ctrUsr.listarNombresRegistrosDeUsuario(nickname));
	}

	@WebMethod
	public DTRegistro obtenerRegistroDeUsuario(String nickname, String nombreEdicion) throws UsuarioNoExisteException {
		IControladorUsuario ctrUsr = Fabrica.getInstance().getIControladorUsuario();
		return ctrUsr.obtenerRegistroDeUsuario(nickname, nombreEdicion);
	}

	@WebMethod
	public MapStringDTReg listarRegistrosEdicion(String nombreEdicion) throws UsuarioNoExisteException {
		IControladorUsuario ctrUsr = Fabrica.getInstance().getIControladorUsuario();
		Map<String, DTRegistro> mapRegistros = ctrUsr.listarRegistrosEdicion(nombreEdicion);
		List<MapStringDTRegEntry> listaRegistros = new ArrayList<>();
		for (Map.Entry<String, DTRegistro> entry : mapRegistros.entrySet()) {
			MapStringDTRegEntry nuevo_entry = new MapStringDTRegEntry();
			nuevo_entry.setNomAsist(entry.getKey());
			nuevo_entry.setRegistro(entry.getValue());
			listaRegistros.add(nuevo_entry);
		}
		return new MapStringDTReg(listaRegistros);
	}

	@WebMethod
	public DTOrganizador obtenerInfoOrganizador(String nickname) throws UsuarioNoExisteException {
		IControladorUsuario ctrUsr = Fabrica.getInstance().getIControladorUsuario();
		return ctrUsr.obtenerInfoOrganizador(nickname);
	}

	@WebMethod
	public DTUsuario getUsuario(String nickname) throws UsuarioNoExisteException {
		IControladorUsuario ctrUsr = Fabrica.getInstance().getIControladorUsuario();
		return ctrUsr.getUsuario(nickname);
	}

	@WebMethod
	public void modificarUsuario(String nickname, String nombre, String nuevaContrasena, String fechaNacStr, String descripcion, String sitioWeb, String apellido)
			throws UsuarioNoExisteException, ContrasenaInvalidaException {
		
		IControladorUsuario ctrlUsuario = Fabrica.getInstance().getIControladorUsuario();
		
		LocalDate fechaNacimiento = null;
		if (fechaNacStr != null && !fechaNacStr.trim().isEmpty()) {
			fechaNacimiento = LocalDate.parse(fechaNacStr);
		}
		
		// Llamar al método con apellido pero imagenPerfil como cadena vacía (lo manejamos por separado)
		ctrlUsuario.modificarUsuario(nickname, nombre, nuevaContrasena, fechaNacimiento, descripcion, sitioWeb, "", apellido);
	}
	
	@WebMethod
	public void modificarImagenPerfil(String nickname, String imagenPerfil)
			throws UsuarioNoExisteException, ContrasenaInvalidaException {
		
		IControladorUsuario ctrlUsuario = Fabrica.getInstance().getIControladorUsuario();
		
		// Solo modificar la imagen de perfil
		ctrlUsuario.modificarUsuario(nickname, "", "", null, "", "", imagenPerfil, "");
	}
	
	@WebMethod
	public DTUsuario getUsuarioPorCorreo(String correo) throws UsuarioNoExisteException {
		IControladorUsuario ctrUsr = Fabrica.getInstance().getIControladorUsuario();
		return ctrUsr.getUsuarioPorCorreo(correo);
	}
	
	// Métodos para seguimiento de usuarios
	@WebMethod
	public void seguirUsr(String seguidor, String seguido) throws UsuarioNoExisteException {
		IControladorUsuario ctrlUsuario = Fabrica.getInstance().getIControladorUsuario();
		ctrlUsuario.seguirUsr(seguidor, seguido);
	}
	
	@WebMethod
	public void dejarDeSeguirUsr(String seguidor, String seguido) throws UsuarioNoExisteException {
		IControladorUsuario ctrlUsuario = Fabrica.getInstance().getIControladorUsuario();
		ctrlUsuario.dejarDeSeguirUsr(seguidor, seguido);
	}
	
	@WebMethod
	public boolean verificarSiSigue(String seguidor, String seguido) throws UsuarioNoExisteException {
		IControladorUsuario ctrlUsuario = Fabrica.getInstance().getIControladorUsuario();
		return ctrlUsuario.verificarSiSigue(seguidor, seguido);
	}
	
	@WebMethod
	public SetOfDTUsuario obtenerUsuariosSeguidos(String nickname) throws UsuarioNoExisteException {
		IControladorUsuario ctrlUsuario = Fabrica.getInstance().getIControladorUsuario();
		Set<DTUsuario> usuariosSeguidos = ctrlUsuario.obtenerUsuariosSeguidos(nickname);
		
		SetOfDTUsuario resultado = new SetOfDTUsuario();
		resultado.getItem().addAll(usuariosSeguidos);
		return resultado;
	}
	
	@WebMethod
	public byte[] getImagenUsuario(String nickname) throws UsuarioNoExisteException {
		IControladorUsuario ctrUs = Fabrica.getInstance().getIControladorUsuario();
		
		return ctrUs.getImagenUsuario(nickname);
	}
   
	@WebMethod
	public void setImagenUsuario(String nickname, byte[] imagen) {
		IControladorUsuario ctrUs = Fabrica.getInstance().getIControladorUsuario();
		ctrUs.setImagenUsuario(nickname, imagen);
	}

}
