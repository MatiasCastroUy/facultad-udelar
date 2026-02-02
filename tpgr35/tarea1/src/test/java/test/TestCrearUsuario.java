package test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import excepciones.UsuarioCorreoRepetidoException;
import excepciones.UsuarioRepetidoException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.time.LocalDate;
import java.util.Set;


import logica.DTAsistente;
import logica.DTOrganizador;
import logica.Fabrica;
import logica.IControladorUsuario;

public class TestCrearUsuario {
	private IControladorUsuario ctrlUsuario;
    
    @BeforeEach
    void setUp() {
        ctrlUsuario = Fabrica.getInstance().getIControladorUsuario();   
    }
    
    //Creo un asistente y verifico que este en la lista de asistentes 
    @Test
    void testCrearAsistente() throws Exception {
        String nickname = "asist1";
        String nombre = "Juan";
        String correo = "juan@correo.com";
        String apellido = "Pérez";
        LocalDate fechaNacimiento = LocalDate.of(2000, 1, 1);

        ctrlUsuario.registrarUsuario(nickname, nombre, correo, true, apellido, fechaNacimiento, null, null);

        Set<DTAsistente> asistentes = ctrlUsuario.listarAsistentes();
        boolean encontrado = asistentes.stream().anyMatch(a -> a.getNickname().equals(nickname));
        assertTrue(encontrado, "El asistente debería estar registrado");
    }
    
    //Creo un organizador y verifico que este en la lista de usuarios
    @Test
    void testCrearOrganizador() throws Exception {
        String nickname = "org1";
        String nombre = "Ana";
        String correo = "ana@correo.com";
        String descripcion = "Organizadora de eventos";
        String sitioWeb = "https://eventos.com";

        ctrlUsuario.registrarUsuario(nickname, nombre, correo, false, null, null, descripcion, sitioWeb);

        Set<DTOrganizador> organizadores = ctrlUsuario.listarOrganizadores();
        boolean encontrado = organizadores.stream().anyMatch(u -> u.getNickname().equals(nickname));
        assertTrue(encontrado, "El organizador debería estar registrado");
    }
    
    //Trata de crear dos usuarios con el mismo nickname y espera que salte la excepción
    @Test
    void testUsuarioRepetidoPorNickname() throws Exception {
        String nickname = "asist2";
        String nombre = "Pedro";
        String correo = "pedro@correo.com";
        String apellido = "Gómez";
        LocalDate fechaNacimiento = LocalDate.of(1995, 5, 5);

        ctrlUsuario.registrarUsuario(nickname, nombre, correo, true, apellido, fechaNacimiento, null, null);

        // Intentar registrar otro usuario con el mismo nickname
        assertThrows(UsuarioRepetidoException.class, () -> {
            ctrlUsuario.registrarUsuario(nickname, "Otro", "otro@correo.com", true, "Otro", fechaNacimiento, null, null);
        });
    }
    
    //Trata de crear dos usuarios con el mismo correo y espera la excepción
    @Test
    void testUsuarioRepetidoPorCorreo() throws Exception {
        String nickname = "asist3";
        String nombre = "Pedro";
        String correo = "repetido@correo.com";
        String apellido = "Gómez";
        LocalDate fechaNacimiento = LocalDate.of(1995, 5, 5);

        ctrlUsuario.registrarUsuario(nickname, nombre, correo, true, apellido, fechaNacimiento, null, null);

        // Intentar registrar otro usuario con el mismo correo
        assertThrows(UsuarioCorreoRepetidoException.class, () -> {
            ctrlUsuario.registrarUsuario("otroNick", "Otro", correo, true, "Otro", fechaNacimiento, null, null);
        });
    }

}
