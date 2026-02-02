package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import logica.Fabrica;
import logica.IControladorEvento;
import logica.ManejadorEvento;
import logica.ManejadorUsuario;

public class TestFinalizarEvento {

	@BeforeEach @AfterEach
	public void reiniciarSingletons() {
		ManejadorEvento.getInstancia().reiniciarSingleton();
		ManejadorUsuario.getInstancia().reiniciarSingleton();
	}
	
	@Test
	public void finalizarEvento() throws Exception {
		IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
		Set<String> cats = new HashSet<String>();
		cats.add("categoria");
		ctrEv.altaEvento("evento1", "desc", LocalDate.now(), "evt", cats, null);
		
		assertEquals(ctrEv.listarEventosSinFinalizar().size(), 1);
		
		ctrEv.finalizarEvento("evento1");
		
		assertTrue(ctrEv.listarEventosSinFinalizar().isEmpty());
	}
}
