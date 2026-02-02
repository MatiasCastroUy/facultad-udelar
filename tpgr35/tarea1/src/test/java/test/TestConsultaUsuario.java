package test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import excepciones.UsuarioNoExisteException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.util.Set;
import java.util.Map;
import logica.IControladorUsuario;
import logica.ManejadorUsuario;
import logica.DTAsistente;
import logica.DTOrganizador;
import logica.DTUsuario;
import logica.DTRegistro;
import logica.Fabrica;
import excepciones.UsuarioRepetidoException;
import excepciones.UsuarioCorreoRepetidoException;
import excepciones.ContrasenaInvalidaException;


public class TestConsultaUsuario {
	
	private IControladorUsuario ctrlUsuario;
	
	@BeforeEach
    void setUp() throws Exception {
		
	    ManejadorUsuario musuario = ManejadorUsuario.getInstancia();
	    musuario.limpiar(); // Esto es para que vacie la colección de usuarios
        ctrlUsuario = Fabrica.getInstance().getIControladorUsuario();
        
        // Creo un asistente y un organizador para consultar
        ctrlUsuario.registrarUsuario("asist4", "Lucía", "lucia@correo.com", true, "Fernández", LocalDate.of(1999, 3, 15), null, null);
        ctrlUsuario.registrarUsuario("org2", "Carlos", "carlos@correo.com", false, null, null, "Organizador senior", "https://org.com");
    }
	
	
	//Se consulta el asistente y se verifica todos sus datos
	@Test
    void testVerInfoUsuarioAsistente() throws Exception {
        DTUsuario usuario = ctrlUsuario.verInfoUsuario("asist4", true);
        assertNotNull(usuario);
        assertEquals("asist4", usuario.getNickname());
        assertEquals("Lucía", usuario.getNombre());
        assertEquals("lucia@correo.com", usuario.getCorreo());
        assertTrue(usuario instanceof DTAsistente);
        DTAsistente asis = (DTAsistente) usuario;
        assertEquals("Fernández", asis.getApellido());
        assertEquals(LocalDate.of(1999, 3, 15), asis.getFechaNacimiento());
    }
	
	//Se consulta el organizador y se verifican sus datos
	@Test
	void testVerInfoUsuarioOrganizador() throws Exception {
		DTUsuario usuario = ctrlUsuario.verInfoUsuario("org2", false);
		System.out.println("Usuarios registrados:");
		for (DTUsuario u : ctrlUsuario.listarUsuarios()) {
		    System.out.println(u.getNickname());
		}
		assertNotNull(usuario);
		assertEquals("org2", usuario.getNickname());
		assertEquals("Carlos", usuario.getNombre());
		assertEquals("carlos@correo.com", usuario.getCorreo());
		assertTrue(usuario instanceof DTOrganizador);
		DTOrganizador orga = (DTOrganizador) usuario;
		assertEquals("Organizador senior", orga.getDescripcion());
		assertEquals("https://org.com", orga.getSitioWeb());
	}
	
	//Se intenta consultar un usuario inexistente y se espera la excepcion
	@Test
	void testUsuarioNoExiste() {
		assertThrows(UsuarioNoExisteException.class, () -> {
			ctrlUsuario.verInfoUsuario("usuarioInexistente", true);
		});
	}
	
	//Se intenta consultar un asistente pero el usuario es organizador
	@Test
	void testUsuarioExistePeroTipoIncorrecto() {
		assertThrows(UsuarioNoExisteException.class, () -> {
			// Intentar obtener "org2" como asistente cuando es organizador
			ctrlUsuario.verInfoUsuario("org2", true);
		});
		
		assertThrows(UsuarioNoExisteException.class, () -> {
			// Intentar obtener "asist4" como organizador cuando es asistente
			ctrlUsuario.verInfoUsuario("asist4", false);
		});
	}
	
	//listar usuarios cuando hay usuarios registrados
	@Test
	void testListarUsuarios() throws Exception {
		Set<DTUsuario> usuarios = ctrlUsuario.listarUsuarios();
		assertNotNull(usuarios);
		assertEquals(2, usuarios.size()); // asist4 y org2
		
		// Verificar que ambos usuarios están en la lista
		boolean tieneAsistente = false;
		boolean tieneOrganizador = false;
		for (DTUsuario u : usuarios) {
			if (u.getNickname().equals("asist4")) {
				tieneAsistente = true;
				assertTrue(u instanceof DTAsistente);
			}
			if (u.getNickname().equals("org2")) {
				tieneOrganizador = true;
				assertTrue(u instanceof DTOrganizador);
			}
		}
		assertTrue(tieneAsistente);
		assertTrue(tieneOrganizador);
	}
	
	//listar usuarios cuando no hay usuarios (colección vacía)
	@Test
	void testListarUsuariosSinUsuarios() throws Exception {
		// Limpiar todos los usuarios
		ManejadorUsuario musuario = ManejadorUsuario.getInstancia();
		musuario.limpiar();
		
		assertThrows(UsuarioNoExisteException.class, () -> {
			ctrlUsuario.listarUsuarios();
		});
	}
	
	//listar solo asistentes
	@Test
	void testListarAsistentes() throws Exception {
		// Agregar un asistente más para tener variedad
		ctrlUsuario.registrarUsuario("asist2", "María", "maria@correo.com", true, "González", LocalDate.of(1995, 8, 20), null, null);
		
		Set<DTAsistente> asistentes = ctrlUsuario.listarAsistentes();
		assertNotNull(asistentes);
		assertEquals(2, asistentes.size()); // asist4 y asist2
		
		// Verificar que todos son asistentes
		for (DTAsistente a : asistentes) {
			assertTrue(a.getNickname().equals("asist4") || a.getNickname().equals("asist2"));
			assertNotNull(a.getApellido());
			assertNotNull(a.getFechaNacimiento());
		}
	}
	
	//usuario que tiene imagen de perfil
	@Test
	void testVerInfoUsuarioConImagen() throws Exception {
		// Registrar usuario con imagen
		ctrlUsuario.registrarUsuario("usuarioConImg", "Pedro", "pedro@correo.com", "password123", "password123", 
			"foto_perfil.jpg", true, "Martínez", LocalDate.of(1990, 5, 10), null, null);
		
		DTUsuario usuario = ctrlUsuario.verInfoUsuario("usuarioConImg", true);
		assertNotNull(usuario);
		assertEquals("usuarioConImg", usuario.getNickname());
		assertEquals("Pedro", usuario.getNombre());
		assertEquals("pedro@correo.com", usuario.getCorreo());
		assertEquals("foto_perfil.jpg", usuario.getImagenPerfil());
		
		assertTrue(usuario instanceof DTAsistente);
		DTAsistente asis = (DTAsistente) usuario;
		assertEquals("Martínez", asis.getApellido());
		assertEquals(LocalDate.of(1990, 5, 10), asis.getFechaNacimiento());
	}
	
	//organizador que tiene imagen de perfil
	@Test
	void testVerInfoOrganizadorConImagen() throws Exception {
		// Registrar organizador con imagen
		ctrlUsuario.registrarUsuario("orgConImg", "Ana", "ana@correo.com", "password456", "password456", 
			"foto_org.png", false, null, null, "Organizadora profesional", "https://ana.com");
		
		DTUsuario usuario = ctrlUsuario.verInfoUsuario("orgConImg", false);
		assertNotNull(usuario);
		assertEquals("orgConImg", usuario.getNickname());
		assertEquals("Ana", usuario.getNombre());
		assertEquals("ana@correo.com", usuario.getCorreo());
		assertEquals("foto_org.png", usuario.getImagenPerfil());
		
		assertTrue(usuario instanceof DTOrganizador);
		DTOrganizador org = (DTOrganizador) usuario;
		assertEquals("Organizadora profesional", org.getDescripcion());
		assertEquals("https://ana.com", org.getSitioWeb());
	}
	
	//listar solo organizadores
	@Test
	void testListarOrganizadores() throws Exception {
		// Agregar un organizador más para tener variedad
		ctrlUsuario.registrarUsuario("org3", "Luis", "luis@correo.com", false, null, null, "Organizador junior", "https://luis.org");
		
		Set<DTOrganizador> organizadores = ctrlUsuario.listarOrganizadores();
		assertNotNull(organizadores);
		assertEquals(2, organizadores.size()); // org2 y org3
		
		// Verificar que todos son organizadores
		for (DTOrganizador o : organizadores) {
			assertTrue(o.getNickname().equals("org2") || o.getNickname().equals("org3"));
			assertNotNull(o.getDescripcion());
		}
	}
	
	//obtener información específica de organizador
	@Test
	void testObtenerInfoOrganizador() throws Exception {
		DTOrganizador organizador = ctrlUsuario.obtenerInfoOrganizador("org2");
		assertNotNull(organizador);
		assertEquals("org2", organizador.getNickname());
		assertEquals("Carlos", organizador.getNombre());
		assertEquals("carlos@correo.com", organizador.getCorreo());
		assertEquals("Organizador senior", organizador.getDescripcion());
		assertEquals("https://org.com", organizador.getSitioWeb());
	}
	
	//obtener información de organizador inexistente
	@Test
	void testObtenerInfoOrganizadorInexistente() {
		assertThrows(UsuarioNoExisteException.class, () -> {
			ctrlUsuario.obtenerInfoOrganizador("organizadorInexistente");
		});
	}
	
	//obtener usuario interno (entidad)
	@Test
	void testGetUsuario() throws Exception {
		DTUsuario usuario = ctrlUsuario.getUsuario("asist4");
		assertNotNull(usuario);
		assertEquals("asist4", usuario.getNickname());
	}

	//obtener usuario interno inexistente
	@Test
	void testGetUsuarioInexistente() {
		assertThrows(UsuarioNoExisteException.class, () -> {
			ctrlUsuario.getUsuario("usuarioInexistente");
		});
	}
	
	//obtener usuario por correo
	@Test
	void testGetUsuarioPorCorreo() throws Exception {
		DTUsuario usuario = ctrlUsuario.getUsuarioPorCorreo("lucia@correo.com");
		assertNotNull(usuario);
		assertEquals("lucia@correo.com", usuario.getCorreo());
	}
	
	//obtener usuario por correo inexistente
	@Test
	void testGetUsuarioPorCorreoInexistente() {
		assertThrows(UsuarioNoExisteException.class, () -> {
			ctrlUsuario.getUsuarioPorCorreo("correoInexistente@correo.com");
		});
	}
	
	//obtener nickname por email
	@Test
	void testObtenerNicknamePorEmail() throws Exception {
		String nickname = ctrlUsuario.obtenerNicknamePorEmail("carlos@correo.com");
		assertEquals("org2", nickname);
	}
	
	//obtener nickname por email inexistente
	@Test
	void testObtenerNicknamePorEmailInexistente() {
		assertThrows(UsuarioNoExisteException.class, () -> {
			ctrlUsuario.obtenerNicknamePorEmail("emailInexistente@correo.com");
		});
	}
	
	//obtener rol de asistente
	@Test
	void testObtenerRolAsistente() throws Exception {
		String rol = ctrlUsuario.obtenerRol("asist4");
		assertEquals("ASISTENTE", rol);
	}
	
	//obtener rol de organizador
	@Test
	void testObtenerRolOrganizador() throws Exception {
		String rol = ctrlUsuario.obtenerRol("org2");
		assertEquals("ORGANIZADOR", rol);
	}
	
	@Test
	void testObtenerRolUsuarioInexistente() {
		assertThrows(UsuarioNoExisteException.class, () -> {
			ctrlUsuario.obtenerRol("usuarioInexistente");
		});
	}
	
	//listar registros de edición (método sin testear)
	@Test
	void testListarRegistrosEdicion() throws Exception {
		// Este método necesita datos de eventos y registros para funcionar
		// Como no tenemos registros creados, debería retornar un mapa vacío
		Map<String, DTRegistro> registros = ctrlUsuario.listarRegistrosEdicion("EdicionInexistente");
		assertNotNull(registros);
		assertTrue(registros.isEmpty());
	}
	
	//registrar usuario con nickname repetido
	@Test
	void testRegistrarUsuarioNicknameRepetido() {
		assertThrows(UsuarioRepetidoException.class, () -> {
			// Intentar registrar con nickname que ya existe (asist4)
			ctrlUsuario.registrarUsuario("asist4", "Otro", "otro@correo.com", "password123", 
				"password123", null, true, "Apellido", LocalDate.of(1990, 1, 1), null, null);
		});
	}
	
	//registrar usuario con correo repetido
	@Test
	void testRegistrarUsuarioCorreoRepetido() {
		assertThrows(UsuarioCorreoRepetidoException.class, () -> {
			// Intentar registrar con correo que ya existe (lucia@correo.com)
			ctrlUsuario.registrarUsuario("nuevoNick", "Nuevo", "lucia@correo.com", "password123", 
				"password123", null, true, "Apellido", LocalDate.of(1990, 1, 1), null, null);
		});
	}
	
	//registrar usuario con contraseña vacía
	@Test
	void testRegistrarUsuarioContrasenaVacia() {
		assertThrows(ContrasenaInvalidaException.class, () -> {
			ctrlUsuario.registrarUsuario("nuevoUser", "Nuevo", "nuevo@correo.com", "", 
				"", null, true, "Apellido", LocalDate.of(1990, 1, 1), null, null);
		});
	}
	
	//registrar usuario con contraseña null
	@Test
	void testRegistrarUsuarioContrasenaNull() {
		assertThrows(ContrasenaInvalidaException.class, () -> {
			ctrlUsuario.registrarUsuario("nuevoUser", "Nuevo", "nuevo@correo.com", null, 
				null, null, true, "Apellido", LocalDate.of(1990, 1, 1), null, null);
		});
	}
	
	//registrar usuario con imagen null (debería funcionar)
	@Test
	void testRegistrarUsuarioImagenNull() throws Exception {
		// Esto debería funcionar, la imagen null se convierte en ""
		ctrlUsuario.registrarUsuario("userImgNull", "Usuario", "user@correo.com", "password123", 
			"password123", null, true, "Apellido", LocalDate.of(1990, 1, 1), null, null);
		
		DTUsuario usuario = ctrlUsuario.verInfoUsuario("userImgNull", true);
		assertNotNull(usuario);
		assertEquals("", usuario.getImagenPerfil());
	}
	
	//verificar contraseña correcta
	@Test
	void testVerificarContrasenaCorrecta() throws Exception {
		// Primero registrar un usuario con contraseña
		ctrlUsuario.registrarUsuario("userPass", "Usuario", "userpass@correo.com", "miPassword", 
			"miPassword", null, true, "Apellido", LocalDate.of(1990, 1, 1), null, null);
		
		boolean resultado = ctrlUsuario.verificarContrasena("userPass", "miPassword");
		assertTrue(resultado);
	}
	
	//verificar contraseña incorrecta
	@Test
	void testVerificarContrasenaIncorrecta() throws Exception {
		// Primero registrar un usuario con contraseña
		ctrlUsuario.registrarUsuario("userPass2", "Usuario", "userpass2@correo.com", "miPassword", 
			"miPassword", null, true, "Apellido", LocalDate.of(1990, 1, 1), null, null);
		
		boolean resultado = ctrlUsuario.verificarContrasena("userPass2", "passwordIncorrecta");
		assertFalse(resultado);
	}
	
	//verificar contraseña con nickname null
	@Test
	void testVerificarContrasenaNicknameNull() {
		assertThrows(UsuarioNoExisteException.class, () -> {
			ctrlUsuario.verificarContrasena(null, "password");
		});
	}
	
	//verificar contraseña con nickname vacío
	@Test
	void testVerificarContrasenaNicknameVacio() {
		assertThrows(UsuarioNoExisteException.class, () -> {
			ctrlUsuario.verificarContrasena("", "password");
		});
	}
	
	//verificar contraseña con nickname solo espacios
	@Test
	void testVerificarContrasenaNicknameSoloEspacios() {
		assertThrows(UsuarioNoExisteException.class, () -> {
			ctrlUsuario.verificarContrasena("   ", "password");
		});
	}
	
	//verificar contraseña null
	@Test
	void testVerificarContrasenaNull() throws Exception {
		boolean resultado = ctrlUsuario.verificarContrasena("asist4", null);
		assertFalse(resultado);
	}
	
	//verificar contraseña con usuario inexistente
	@Test
	void testVerificarContrasenaUsuarioInexistente() {
		assertThrows(UsuarioNoExisteException.class, () -> {
			ctrlUsuario.verificarContrasena("usuarioInexistente", "password");
		});
	}
	
	//obtener rol con nickname null
	@Test
	void testObtenerRolNicknameNull() {
		assertThrows(UsuarioNoExisteException.class, () -> {
			ctrlUsuario.obtenerRol(null);
		});
	}
	
	//obtener rol con nickname vacío
	@Test
	void testObtenerRolNicknameVacio() {
		assertThrows(UsuarioNoExisteException.class, () -> {
			ctrlUsuario.obtenerRol("");
		});
	}
	
	//obtener rol con nickname solo espacios
	@Test
	void testObtenerRolNicknameSoloEspacios() {
		assertThrows(UsuarioNoExisteException.class, () -> {
			ctrlUsuario.obtenerRol("   ");
		});
	}
	
	
	
}
