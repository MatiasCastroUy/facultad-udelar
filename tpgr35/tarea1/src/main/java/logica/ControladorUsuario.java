package logica;

import excepciones.ContrasenaInvalidaException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioCorreoRepetidoException;
import excepciones.UsuarioRepetidoException;
import presentacion.Principal;

import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

public class ControladorUsuario implements IControladorUsuario {

	public ControladorUsuario() {
	}

	public void registrarUsuario(String nickname, String nombre, String correo, boolean esAsistente, String apellido,
			LocalDate fechaNacimiento, String descripcion, String sitioWeb)
			throws UsuarioRepetidoException, UsuarioCorreoRepetidoException {
		ManejadorUsuario manUsr = ManejadorUsuario.getInstancia();
		Usuario usr = manUsr.findUsuario(nickname);
		Usuario usrC = manUsr.findUsuarioCorreo(correo);
		if (usr != null)
			throw new UsuarioRepetidoException("El usuario " + nickname + " ya esta registrado");
		if (usrC != null)
			throw new UsuarioCorreoRepetidoException("El correo " + correo + " ya esta registrado");
		if (esAsistente) {
			Asistente asistente = new Asistente(nickname, nombre, correo, apellido, fechaNacimiento);
			manUsr.addUsuario(asistente);
			manUsr.addUsuarioC(asistente);
		} else {
			Organizador organizador = new Organizador(nickname, nombre, correo, descripcion, sitioWeb);
			manUsr.addUsuario(organizador);
			manUsr.addUsuarioC(organizador);
		}
	}

	// Método nuevo (con contraseña e imagen)
	public void registrarUsuario(String nickname, String nombre, String correo, String contrasena, String confirmacionContrasena, String imagenPerfil, boolean esAsistente, String apellido,
			LocalDate fechaNacimiento, String descripcion, String sitioWeb)
			throws UsuarioRepetidoException, UsuarioCorreoRepetidoException, ContrasenaInvalidaException {
		ManejadorUsuario manUsr = ManejadorUsuario.getInstancia();
		Usuario usr = manUsr.findUsuario(nickname);
		Usuario usrC = manUsr.findUsuarioCorreo(correo);
		
		// Validaciones de usuario existente
		if (usr != null)
			throw new UsuarioRepetidoException("El usuario " + nickname + " ya esta registrado");
		if (usrC != null)
			throw new UsuarioCorreoRepetidoException("El correo " + correo + " ya esta registrado");
			
		// Validaciones básicas de contraseña
		if (contrasena == null || contrasena.trim().isEmpty())
			throw new ContrasenaInvalidaException("La contraseña no puede estar vacía");
		
		// Validar que las contraseñas coincidan
		if (!contrasena.equals(confirmacionContrasena))
			throw new ContrasenaInvalidaException("Las contraseñas no coinciden");
			
		// Validar imagen de perfil (puede estar vacía)
		if (imagenPerfil == null)
			imagenPerfil = "";
			
		if (esAsistente) {
			Asistente asistente = new Asistente(nickname, nombre, correo, contrasena, imagenPerfil, apellido, fechaNacimiento);
			manUsr.addUsuario(asistente);
			manUsr.addUsuarioC(asistente);
		} else {
			Organizador organizador = new Organizador(nickname, nombre, correo, contrasena, imagenPerfil, descripcion, sitioWeb);
			manUsr.addUsuario(organizador);
			manUsr.addUsuarioC(organizador);
		}
	}

	public DTUsuario verInfoUsuario(String nickname, boolean esAsistente) throws UsuarioNoExisteException {
		ManejadorUsuario manUsr = ManejadorUsuario.getInstancia();
		Usuario usr = manUsr.findUsuario(nickname);
		if (usr != null) {
			if (esAsistente && usr instanceof Asistente) {
				Asistente asis = (Asistente) usr;
				return new DTAsistente(asis.getNickname(), asis.getNombre(), asis.getCorreo(), asis.getImagenPerfil(), asis.getApellido(),
						asis.getFechaNacimiento());
			} else if (!esAsistente && usr instanceof Organizador) {
				Organizador orga = (Organizador) usr;
				return new DTOrganizador(orga.getNickname(), orga.getNombre(), orga.getCorreo(), orga.getImagenPerfil(), orga.getDescripcion(),
						orga.getSitioWeb());
			} else {
				// El usuario existe pero no es del tipo esperado
				throw new UsuarioNoExisteException("El usuario " + nickname + " no es del tipo solicitado");
			}
		} else {
			throw new UsuarioNoExisteException("El usuario " + nickname + " no existe");
		}
	}

	public Set<DTUsuario> listarUsuarios() throws UsuarioNoExisteException {
		ManejadorUsuario manUsr = ManejadorUsuario.getInstancia();
		Set<Usuario> usrs = manUsr.listarUsuarios();

		if (usrs == null || usrs.isEmpty()) {
			throw new UsuarioNoExisteException("No existen usuarios registrados");
		} else {
			Set<DTUsuario> resultado = new HashSet<>();
			for (Usuario u : usrs) {
				if (u instanceof Asistente) {
					Asistente asis = (Asistente) u;
					resultado.add(new DTAsistente(asis.getNickname(), asis.getNombre(), asis.getCorreo(), asis.getImagenPerfil(),
							asis.getApellido(), asis.getFechaNacimiento()));
				} else {
					Organizador orga = (Organizador) u;
					resultado.add(new DTOrganizador(orga.getNickname(), orga.getNombre(), orga.getCorreo(), orga.getImagenPerfil(),
							orga.getDescripcion(), orga.getSitioWeb()));
				}
			}
			return resultado;
		}
	}

	public Set<DTAsistente> listarAsistentes() throws UsuarioNoExisteException {
		ManejadorUsuario manUsr = ManejadorUsuario.getInstancia();
		Set<Usuario> usrs = manUsr.listarUsuarios();

		Set<DTAsistente> resultado = new HashSet<>();
		for (Usuario u : usrs) {
			if (u instanceof Asistente) {
				Asistente asis = (Asistente) u;
				resultado.add(new DTAsistente(asis.getNickname(), asis.getNombre(), asis.getCorreo(), asis.getImagenPerfil(),
						asis.getApellido(), asis.getFechaNacimiento()));
			}
		}
		return resultado;
	}

	public Set<String> listarNombresRegistrosDeUsuario(String nickname) throws UsuarioNoExisteException {
		ManejadorUsuario manUsr = ManejadorUsuario.getInstancia();
		Usuario user = manUsr.findUsuario(nickname);
		if (user == null) {
			throw new UsuarioNoExisteException("No existe " + nickname);
		}

		Set<String> nombres = new HashSet<>();
		if (user instanceof Asistente) {
			Asistente asis = (Asistente) user;
			for (Registro regs : asis.listarRegistros()) {
				String nombreEdicion = regs.getEdicion().getNombre();
				String nombreEvento = regs.getEdicion().getEvento().getNombre();
				nombres.add("Registro a \"" + nombreEdicion + "\" de \"" + nombreEvento + "\"");
			}
		}
		return nombres;
	}

	public DTRegistro obtenerRegistroDeUsuario(String nickname, String nombreEdicion) throws UsuarioNoExisteException {
		ManejadorUsuario manUsr = ManejadorUsuario.getInstancia();
		Usuario user = manUsr.findUsuario(nickname);
		if (user == null) {
			throw new UsuarioNoExisteException("No existe el usuario " + nickname);
		}

		if (user instanceof Asistente) {
			Asistente asis = (Asistente) user;
			Registro reg = asis.obtenerRegistroPorEdicion(nombreEdicion);
			if (reg != null) {
				return new DTRegistro(
						reg.getEdicion().getEvento().getNombre(),
						reg.getEdicion().getNombre(),
						reg.getFechaRegistro(),
						reg.getTipo().getNombre(),
						reg.getCosto(),
						reg.isAsistio()
				);
			}
			return new DTRegistro("__ERROR__", "", LocalDate.now(), "", -1, false); // EVITAR DEVOLVER NULl
		}
		return null;
	}
	
	public Map<String, DTRegistro> listarRegistrosEdicion(String nombreEdicion) throws UsuarioNoExisteException {
		Map<String, DTRegistro> mapRegistros = new HashMap<>();
		
		ManejadorUsuario manUsr = ManejadorUsuario.getInstancia();
		Set<Usuario> listaUsuarios = manUsr.listarUsuarios();
		for (Usuario usuario : listaUsuarios) {
			if (usuario instanceof Asistente) {
				Registro registro = ((Asistente) usuario).obtenerRegistroPorEdicion(nombreEdicion);
				
				if (registro != null) {
					mapRegistros.put(usuario.getNickname(), new DTRegistro(
						registro.getEdicion().getEvento().getNombre(), 
						registro.getEdicion().getNombre(),
						registro.getFechaRegistro(), 
						registro.getTipo().getNombre(), 
						registro.getCosto(),
						registro.isAsistio()
					));
				}
			}
		}
		
		return mapRegistros;
	}

	public Set<DTOrganizador> listarOrganizadores() throws UsuarioNoExisteException {
		ManejadorUsuario manUsr = ManejadorUsuario.getInstancia();
		Set<Usuario> usrs = manUsr.listarUsuarios();
		Set<DTOrganizador> resultado = new HashSet<>();

		if (usrs == null || usrs.isEmpty()) {
			throw new UsuarioNoExisteException("No existen usuarios registrados.");
		}

		for (Usuario u : usrs) {
			if (u instanceof Organizador) {
				Organizador orga = (Organizador) u;
				resultado.add(new DTOrganizador(orga.getNickname(), orga.getNombre(), orga.getCorreo(), orga.getImagenPerfil(),
						orga.getDescripcion(), orga.getSitioWeb()));
			}
		}

		if (resultado == null || resultado.isEmpty()) {
			throw new UsuarioNoExisteException("No existen organizadores registrados.");
		}

		return resultado;
	}

	public DTOrganizador obtenerInfoOrganizador(String nickname) throws UsuarioNoExisteException {
		ManejadorUsuario manUsr = ManejadorUsuario.getInstancia();

		Usuario user = manUsr.findUsuario(nickname);

		if (user == null)
			throw new UsuarioNoExisteException("No existe el usuario seleccionado.");

		Organizador org = (Organizador) user;

		DTOrganizador dtOrganizador = new DTOrganizador(org.getNickname(), org.getNombre(), org.getCorreo(), org.getImagenPerfil(),
				org.getDescripcion(), org.getSitioWeb());

		return dtOrganizador;
	}
	 public DTUsuario getUsuario(String nickname) throws UsuarioNoExisteException {
		 	ManejadorUsuario manUsr = ManejadorUsuario.getInstancia();
		 	Usuario usr = manUsr.findUsuario(nickname);
		 	if (usr == null) throw new UsuarioNoExisteException("No existe el usuario: " + nickname);

		 	if (usr instanceof Asistente) {
		        Asistente asis = (Asistente) usr;
		        return new DTAsistente(asis.getNickname(), asis.getNombre(), asis.getCorreo(), asis.getImagenPerfil(), asis.getApellido(), asis.getFechaNacimiento());
		    } else if (usr instanceof Organizador) {
		        Organizador orga = (Organizador) usr;
		        return new DTOrganizador(orga.getNickname(), orga.getNombre(), orga.getCorreo(), orga.getImagenPerfil(), orga.getDescripcion(), orga.getSitioWeb());
		    }

		    throw new UsuarioNoExisteException("El usuario no es de un tipo reconocido.");
	    }
	 public DTUsuario getUsuarioPorCorreo(String correo) throws UsuarioNoExisteException {
		 	ManejadorUsuario manUsr = ManejadorUsuario.getInstancia();
		 	Usuario usr = manUsr.findUsuarioCorreo(correo);
		 	if (usr == null) throw new UsuarioNoExisteException("No existe el usuario: " + correo);

		 	if (usr instanceof Asistente) {
		        Asistente asis = (Asistente) usr;
		        return new DTAsistente(asis.getNickname(), asis.getNombre(), asis.getCorreo(), asis.getImagenPerfil(), asis.getApellido(), asis.getFechaNacimiento());
		    } else if (usr instanceof Organizador) {
		        Organizador orga = (Organizador) usr;
		        return new DTOrganizador(orga.getNickname(), orga.getNombre(), orga.getCorreo(), orga.getImagenPerfil(), orga.getDescripcion(), orga.getSitioWeb());
		    }

		    throw new UsuarioNoExisteException("El usuario no es de un tipo reconocido.");
	    }

	 @Override
	 public String obtenerNicknamePorEmail(String email) throws UsuarioNoExisteException {
	     if (email == null || (email = email.trim()).isEmpty()) {
	         throw new UsuarioNoExisteException("Email vacío o nulo.");
	     }
	     Usuario usr = ManejadorUsuario.getInstancia().findUsuarioCorreo(email);
	     if (usr == null) {
	         throw new UsuarioNoExisteException("No existe un usuario con ese email.");
	     }
	     return usr.getNickname();
	 }

	 @Override
	 public boolean verificarContrasena(String nickname, String contrasena) throws UsuarioNoExisteException {
	     if (nickname == null || (nickname = nickname.trim()).isEmpty()) {
	         throw new UsuarioNoExisteException("Nickname vacío o nulo.");
	     }
	     if (contrasena == null) return false;

	     Usuario usr = ManejadorUsuario.getInstancia().findUsuario(nickname);
	     if (usr == null) {
	         throw new UsuarioNoExisteException("No existe el usuario: " + nickname);
	     }
	     String guardada = usr.getContrasena();
	     return guardada != null && guardada.equals(contrasena);
	 }
	 
	 public String obtenerRol(String nickname) throws UsuarioNoExisteException {
	     if (nickname == null || (nickname = nickname.trim()).isEmpty()) {
	         throw new UsuarioNoExisteException("Nickname vacío o nulo.");
	     }

	     Usuario usr = ManejadorUsuario.getInstancia().findUsuario(nickname);
	     if (usr == null) {
	         throw new UsuarioNoExisteException("No existe el usuario: " + nickname);
	     }

	     if (usr instanceof Organizador) return "ORGANIZADOR";
	     if (usr instanceof Asistente)   return "ASISTENTE";
	     return "ASISTENTE";
	 }

	 public void modificarUsuario(String nickname, String nombre, String nuevaContrasena, LocalDate fechaNacimiento, String descripcion, String sitioWeb, String imagenPerfil) throws UsuarioNoExisteException, ContrasenaInvalidaException {
		    modificarUsuario(nickname, nombre, nuevaContrasena, fechaNacimiento, descripcion, sitioWeb, imagenPerfil, null);
	 }
	 
	 public void modificarUsuario(String nickname, String nombre, String nuevaContrasena, LocalDate fechaNacimiento, String descripcion, String sitioWeb, String imagenPerfil, String apellido) throws UsuarioNoExisteException, ContrasenaInvalidaException {
		    ManejadorUsuario manUsr = ManejadorUsuario.getInstancia();
		    Usuario usuario = manUsr.findUsuario(nickname);

		    if (usuario == null) {
		        throw new UsuarioNoExisteException("El usuario no existe: " + nickname);
		    }

		    if (nuevaContrasena != null && !nuevaContrasena.trim().isEmpty()) {
		        if (!nuevaContrasena.matches(".*\\d.*") || !nuevaContrasena.matches(".*[a-zA-Z].*")) {
		            throw new ContrasenaInvalidaException("La contraseña debe incluir letras y números.");
		        }
		        usuario.setContrasena(nuevaContrasena);
		    }

		    if (nombre != null && !nombre.trim().isEmpty()) {
		        usuario.setNombre(nombre);
		    }

		    if (fechaNacimiento != null && usuario instanceof Asistente) {
		        ((Asistente) usuario).setFechaNacimiento(fechaNacimiento);
		    }
		    
		    if (apellido != null && !apellido.trim().isEmpty() && usuario instanceof Asistente) {
		        ((Asistente) usuario).setApellido(apellido);
		    }

		    if (usuario instanceof Organizador) {
		        Organizador organizador = (Organizador) usuario;
		        if (descripcion != null && !descripcion.trim().isEmpty()) {
		            organizador.setDescripcion(descripcion);
		        }
		        if (sitioWeb != null && !sitioWeb.trim().isEmpty()) {
		            organizador.setSitioWeb(sitioWeb);
		        }
		    }

		    if (imagenPerfil != null && !imagenPerfil.trim().isEmpty()) {
		        usuario.setImagenPerfil(imagenPerfil);
		    }
		}

	 // Métodos para seguimiento de usuarios
	 public void seguirUsr(String seguidor, String seguido) throws UsuarioNoExisteException {
		 ManejadorUsuario manUsr = ManejadorUsuario.getInstancia();
		 
		 // Validar que ambos usuarios existan
		 Usuario usuarioSeguidor = manUsr.findUsuario(seguidor);
		 Usuario usuarioSeguido = manUsr.findUsuario(seguido);
		 
		 if (usuarioSeguidor == null) {
			 throw new UsuarioNoExisteException("El usuario seguidor no existe: " + seguidor);
		 }
		 
		 if (usuarioSeguido == null) {
			 throw new UsuarioNoExisteException("El usuario a seguir no existe: " + seguido);
		 }
		 
		 // Validar que no se esté siguiendo a sí mismo
		 if (seguidor.equals(seguido)) {
			 throw new IllegalArgumentException("Un usuario no puede seguirse a sí mismo");
		 }
		 
		 // Agregar la relación de seguimiento
		 usuarioSeguidor.seguirUsuario(seguido);
	 }

	 public void dejarDeSeguirUsr(String seguidor, String seguido) throws UsuarioNoExisteException {
		 ManejadorUsuario manUsr = ManejadorUsuario.getInstancia();
		 
		 // Validar que ambos usuarios existan
		 Usuario usuarioSeguidor = manUsr.findUsuario(seguidor);
		 Usuario usuarioSeguido = manUsr.findUsuario(seguido);
		 
		 if (usuarioSeguidor == null) {
			 throw new UsuarioNoExisteException("El usuario seguidor no existe: " + seguidor);
		 }
		 
		 if (usuarioSeguido == null) {
			 throw new UsuarioNoExisteException("El usuario seguido no existe: " + seguido);
		 }
		 
		 // Validar que se esté siguiendo al usuario
		 if (!usuarioSeguidor.sigueA(seguido)) {
			 throw new IllegalArgumentException("El usuario " + seguidor + " no sigue a " + seguido);
		 }
		 
		 // Eliminar la relación de seguimiento
		 usuarioSeguidor.dejarDeSeguirUsuario(seguido);
	 }

	 public boolean verificarSiSigue(String seguidor, String seguido) throws UsuarioNoExisteException {
		 ManejadorUsuario manUsr = ManejadorUsuario.getInstancia();
		 
		 // Validar que ambos usuarios existan
		 Usuario usuarioSeguidor = manUsr.findUsuario(seguidor);
		 Usuario usuarioSeguido = manUsr.findUsuario(seguido);
		 
		 if (usuarioSeguidor == null) {
			 throw new UsuarioNoExisteException("El usuario seguidor no existe: " + seguidor);
		 }
		 
		 if (usuarioSeguido == null) {
			 throw new UsuarioNoExisteException("El usuario seguido no existe: " + seguido);
		 }
		 
		 // Verificar si sigue al usuario
		 return usuarioSeguidor.sigueA(seguido);
	 }

	 public Set<DTUsuario> obtenerUsuariosSeguidos(String nickname) throws UsuarioNoExisteException {
		 ManejadorUsuario manUsr = ManejadorUsuario.getInstancia();
		 
		 // Validar que el usuario exista
		 Usuario usuario = manUsr.findUsuario(nickname);
		 if (usuario == null) {
			 throw new UsuarioNoExisteException("El usuario no existe: " + nickname);
		 }
		 
		 // Obtener la lista de nicknames de usuarios seguidos
		 Set<String> nicknamesSeguidos = usuario.getUsuariosSeguidos();
		 Set<DTUsuario> usuariosSeguidos = new HashSet<>();
		 
		 // Para cada nickname seguido, obtener la información completa del usuario
		 for (String nickSeguido : nicknamesSeguidos) {
			 try {
				 Usuario usuarioSeguido = manUsr.findUsuario(nickSeguido);
				 if (usuarioSeguido != null) {
					 // Determinar el tipo y crear el DTO apropiado
					 if (usuarioSeguido instanceof Asistente) {
						 Asistente asistente = (Asistente) usuarioSeguido;
						 DTAsistente dtAsistente = new DTAsistente(
							 asistente.getNickname(), 
							 asistente.getNombre(), 
							 asistente.getCorreo(), 
							 asistente.getImagenPerfil(),
							 asistente.getApellido(), 
							 asistente.getFechaNacimiento()
						 );
						 usuariosSeguidos.add(dtAsistente);
					 } else if (usuarioSeguido instanceof Organizador) {
						 Organizador organizador = (Organizador) usuarioSeguido;
						 DTOrganizador dtOrganizador = new DTOrganizador(
							 organizador.getNickname(), 
							 organizador.getNombre(), 
							 organizador.getCorreo(), 
							 organizador.getImagenPerfil(),
							 organizador.getDescripcion(), 
							 organizador.getSitioWeb()
						 );
						 usuariosSeguidos.add(dtOrganizador);
					 }
				 }
			 } catch (Exception e) {
				 // Si hay error al obtener un usuario seguido, continuar con los demás
				 continue;
			 }
		 }
		 
		 return usuariosSeguidos;
	 }
	 
	 public byte[] getImagenUsuario(String nickname) throws UsuarioNoExisteException {
		 Properties propiedades = Principal.getPropiedades();
		 String path_base = propiedades.getProperty("path_imagenes");
		 
		 if (!path_base.endsWith("/")) {
			 path_base += "/";
		 }
		 
		 String nombre_img = this.getUsuario(nickname).getImagenPerfil();
		 
		 if (nombre_img == null) {
			 return new byte[0];
		 }
		 try {
			 byte[] img = Files.readAllBytes(Paths.get(path_base + nombre_img));
			 System.out.println("Imagen " + nombre_img + " encontrada. Devolviendo " + img.length + " bytes");
			 return img;
		 } catch (IOException io_exc) {
			 System.out.println("Error al buscar imagen " + nombre_img);
			 return new byte[0];
		 }

	 }
	 
	 public void setImagenUsuario(String nickname, byte[] imagen) {
		 
		 
		 Properties propiedades = Principal.getPropiedades();
		 String path_base = propiedades.getProperty("path_imagenes");
		 
		 if (!path_base.endsWith("/")) {
			 path_base += "/";
		 }
		 
		 String nombre_img = "IMG_" + nickname + ".png";
		 ManejadorUsuario manUsuario = ManejadorUsuario.getInstancia();
		 
		 if (imagen==null) {
			 manUsuario.findUsuario(nickname).setImagenPerfil(null);
			 return;
		 }
		 
		 try {
			Files.write(Paths.get(path_base + nombre_img), imagen);
			manUsuario.findUsuario(nickname).setImagenPerfil(nombre_img);
		} catch (IOException e) {
			System.out.println("Error al escribir imagen " + nombre_img);
			e.printStackTrace();
		}
	 }

}
