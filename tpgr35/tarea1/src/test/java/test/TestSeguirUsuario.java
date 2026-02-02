package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import excepciones.UsuarioNoExisteException;
import logica.DTUsuario;
import logica.Fabrica;
import logica.IControladorUsuario;
import logica.ManejadorEvento;
import logica.ManejadorUsuario;

public class TestSeguirUsuario {
	
	@BeforeEach @AfterEach
	public void reiniciarSingletons() {
		ManejadorEvento.getInstancia().reiniciarSingleton();
		ManejadorUsuario.getInstancia().reiniciarSingleton();
	}
	
	@Test
	public void seguirUsuarios() throws Exception {
		IControladorUsuario ctrUs = Fabrica.getInstance().getIControladorUsuario();
		ctrUs.registrarUsuario("us1", "manolo", "man@olo.com", "pass1", "pass1", null, true, "perez", LocalDate.now(), null, null);
		ctrUs.registrarUsuario("us2", "tuseventos", "tus@eventos.com", "pass2", "pass2", null, false, null, null, "desc", "tuseventos.net");

		ctrUs.seguirUsr("us1", "us2");
		Set<DTUsuario> seguidosUs1 = ctrUs.obtenerUsuariosSeguidos("us1");
		Set<DTUsuario> seguidosUs2 = ctrUs.obtenerUsuariosSeguidos("us2");

		assertFalse(seguidosUs1.isEmpty());
		for (DTUsuario us : seguidosUs1) {
			assertTrue(us.getNickname().equals("us2"));
		}
		
		assertTrue(seguidosUs2.isEmpty());
		
		ctrUs.seguirUsr("us2", "us1");
		seguidosUs2 = ctrUs.obtenerUsuariosSeguidos("us2");
		
		assertFalse(seguidosUs2.isEmpty());

		for (DTUsuario us : seguidosUs2) {
			assertTrue(us.getNickname().equals("us1"));
		}
		
		ctrUs.dejarDeSeguirUsr("us1", "us2");
		seguidosUs1 = ctrUs.obtenerUsuariosSeguidos("us1");
		assertTrue(seguidosUs1.isEmpty());

		assertFalse(ctrUs.verificarSiSigue("us1", "us2"));
		assertTrue(ctrUs.verificarSiSigue("us2", "us1"));
		
		assertThrows(UsuarioNoExisteException.class, () -> {ctrUs.verificarSiSigue("us1", " "); });
		
	}
}
