package test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import excepciones.ContrasenaInvalidaException;
import excepciones.UsuarioNoExisteException;
import logica.DTOrganizador;
import logica.DTUsuario;
import logica.Fabrica;
import logica.IControladorUsuario;
import logica.ManejadorEvento;
import logica.ManejadorUsuario;

public class TestModificarUsuario {

	@BeforeEach @AfterEach
	public void reiniciarSingletons() {
		ManejadorEvento.getInstancia().reiniciarSingleton();
		ManejadorUsuario.getInstancia().reiniciarSingleton();
	}
	
	@Test
	public void modificarUsuario() throws Exception {
		String nickname = "asist1";
        String nombre = "Juan";
        String correo = "juan@correo.com";
        String apellido = "Pérez";
        LocalDate fechaNacimiento = LocalDate.of(2000, 1, 1);

        IControladorUsuario ctrUs = Fabrica.getInstance().getIControladorUsuario();
        ctrUs.registrarUsuario(nickname, nombre, correo, "password", "password", "", true, apellido, fechaNacimiento, null, null);

        DTUsuario original = ctrUs.getUsuario(nickname);
        
        ctrUs.modificarUsuario(nickname, "Roberto", "mypassword99", LocalDate.now(), null, null, null);
        
        DTUsuario nuevo = ctrUs.getUsuario(nickname);
        
        assertTrue(nuevo.getNickname().equals(original.getNickname()));
        assertTrue(nuevo.getCorreo().equals(original.getCorreo()));
        assertTrue(nuevo.getImagenPerfil().equals(original.getImagenPerfil()));
        assertFalse(nuevo.getNombre().equals(original.getNombre()));
        assertTrue(nuevo.getNombre().equals("Roberto"));
        assertTrue(original.getNombre().equals(nombre));
        
        assertThrows(ContrasenaInvalidaException.class, () -> {
        	ctrUs.modificarUsuario(nickname, null, "notvalid", null, null, null, null); });
        
        assertThrows(UsuarioNoExisteException.class, () -> {
        	ctrUs.modificarUsuario("pepito33", null, "O@#masd9", null, null, null, null); });
        
        // organizador
        ctrUs.registrarUsuario("eventos", "tuseventos.com", "hola@yahoo.com", "password", "password", "", 
        		false, null, null, "desc", "http://web.com");
        ctrUs.modificarUsuario("eventos", null, null, null, "Tus eventos.", "facebook.com", "IMG988");
        
        assertTrue(((DTOrganizador) ctrUs.getUsuarioPorCorreo("hola@yahoo.com")).getDescripcion().equals("Tus eventos."));
        
	}
	
}
