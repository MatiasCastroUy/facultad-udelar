package test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import excepciones.TipoRegistroRepetidoExcepcion;
import logica.IControladorEvento;
import logica.ManejadorEvento;
import logica.ManejadorUsuario;
import logica.DTTipoRegistro;
import logica.Fabrica;

class TestTipoRegistro {

	@BeforeEach @AfterEach
	void reiniciarSingleton() {
		ManejadorEvento.getInstancia().reiniciarSingleton();
		ManejadorUsuario.getInstancia().reiniciarSingleton();
	}
	
	@Test
	void testsPositivos() throws Exception {
	    Set<String> cats = new HashSet<>();
	       cats.add("Deporte");
	       cats.add("Música");
	       cats.add("Cultura");
		IControladorEvento contrEv = Fabrica.getInstance().getIControladorEvento();
		contrEv.altaEvento("evento1", "desc1", null, null, cats, null);
		contrEv.altaEdicionEvento("evento1", null, "ev1ed1", null, null, null, null, null, null, "Aceptada", null, null);
		
		contrEv.crearTipoRegistro("evento1", "ev1ed1", "tipoReg1", "", 0, 0);
		contrEv.crearTipoRegistro("evento1", "ev1ed1", "tipoReg2", "", 0, 0);
		Set<String> tiposReg = contrEv.listarTipoRegistro("evento1", "ev1ed1");
	
		Set<String> trEsperados = new HashSet<String>();
		trEsperados.add("tipoReg1");
		trEsperados.add("tipoReg2");
		assertEquals(tiposReg.equals(trEsperados), true);
		
		tiposReg = contrEv.listarTipoRegistro("evento1", "ev1ed1");
		// verificamos que no se agrega un duplicado
		assertEquals(tiposReg.size(), 2);

		// funcionamiento getDTTipoRegistro
		contrEv.crearTipoRegistro("evento1", "ev1ed1", "tipoReg3", "desc", 100.0f, 20);
		DTTipoRegistro dtobtenido = contrEv.getDTTipoReg("evento1", "ev1ed1", "tipoReg3");
		assertEquals(100.0f, dtobtenido.getCosto());
		assertEquals(20, dtobtenido.getCupoDisponible());
		assertEquals("tipoReg3", dtobtenido.getNombre());
		assertEquals("desc", dtobtenido.getDescripcion());
		
	}
	
	@Test
	void testsNegativos() throws Exception { // solo nos interesan las excepciones de tipoRegistro
		IControladorEvento contrEv = Fabrica.getInstance().getIControladorEvento();

		// para estos tests solo precisamos el nombre, 
		// en la práctica la capa de presentación restringe los valores null
		Set<String> cats = new HashSet<>();
	       cats.add("Deporte");
	       cats.add("Música");
	       cats.add("Cultura");
	    contrEv.altaEvento("evento2", null, null, null, cats, null);
	    contrEv.altaEdicionEvento("evento2", null, "ev2ed1", null, null, null, null, null, null, "Aceptada", null, null);
		
		
		try {
			contrEv.crearTipoRegistro("evento2", "ev2ed1", "tipoReg1", null, 0, 0);
			contrEv.crearTipoRegistro("evento2", "ev2ed1", "tipoReg1", null, 299.99f, 30); // falla
		} catch (TipoRegistroRepetidoExcepcion e) {
			assertEquals(e.getMessage(), "La edición ev2ed1 del evento evento2 ya tiene un tipo de registro tipoReg1");
		}
		
	}
	

}
