package test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import logica.Fabrica;
import logica.IControladorEvento;
import logica.IControladorUsuario;
import logica.ManejadorEvento;
import logica.ManejadorUsuario;

class TestAceptarRechazarEdicion {

	@BeforeEach @AfterEach
	void reiniciarSingleton() {
		ManejadorEvento.getInstancia().reiniciarSingleton();
		ManejadorUsuario.getInstancia().reiniciarSingleton();
	}
	
	@BeforeEach
	void preCargar() throws Exception {
		IControladorUsuario contrUs = Fabrica.getInstance().getIControladorUsuario();
		String nickname = "org1";
        String nombre = "Ana";
        String correo = "ana@correo.com";
        String descripcion = "Organizadora de eventos";
        String sitioWeb = "https://eventos.com";
        contrUs.registrarUsuario(nickname, nombre, correo, false, null, null, descripcion, sitioWeb);
	}
	
	@Test
	void test() throws Exception {
		IControladorEvento contrEv = Fabrica.getInstance().getIControladorEvento();
		Set<String> cats = new HashSet<String>();
		cats.add("categoria");
		String nickOrg = "org1";
		
		contrEv.altaEvento("evento1", nickOrg, null, "EV1", cats, null);
		// ed ingresada
		contrEv.altaEdicionEvento("evento1", nickOrg, "edicion1", "EV1ED1", "", "", null, null, null, "Ingresada", null, null);
		// ed rechazada
		contrEv.altaEdicionEvento("evento1", nickOrg, "edicion2", "EV1ED2", "", "", null, null, null, "Rechazada", null, null);
		// ed ingresada
		contrEv.altaEdicionEvento("evento1", nickOrg, "edicion3", "EV1ED3", "", "", null, null, null, "Ingresada", null, null);
		
		assertEquals(contrEv.listarEdiciones("evento1").size(), 3);
		assertTrue(contrEv.listarEdicionesAceptadas("evento1").isEmpty());
		assertEquals(contrEv.listarEdicionesIngresadas("evento1").size(), 2);
		
		assertEquals(contrEv.getDTEdicion("evento1", "edicion1").getEstado(), "Ingresada");
		assertEquals(contrEv.getDTEdicion("evento1", "edicion2").getEstado(), "Rechazada");
		assertEquals(contrEv.getDTEdicion("evento1", "edicion3").getEstado(), "Ingresada");
		
		// acepto una edición ingresada
		contrEv.aceptarEdicionEvento("evento1", "edicion1");
		// rechazo otra edición ingresada
		contrEv.rechazarEdicionEvento("evento1", "edicion3");
		
		assertEquals(contrEv.listarEdiciones("evento1").size(), 3);
		assertTrue(contrEv.listarEdicionesIngresadas("evento1").isEmpty());
		assertEquals(contrEv.listarEdicionesAceptadas("evento1").size(), 1);
		
		assertEquals(contrEv.getDTEdicion("evento1", "edicion1").getEstado(), "Aceptada");
		assertEquals(contrEv.getDTEdicion("evento1", "edicion2").getEstado(), "Rechazada");
		assertEquals(contrEv.getDTEdicion("evento1", "edicion3").getEstado(), "Rechazada");
		
	}

}
