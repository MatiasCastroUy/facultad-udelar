package test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals ;
import static org.junit.jupiter.api.Assertions.assertEquals ;
import static org.junit.jupiter.api.Assertions.assertThrows ;



import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import excepciones.CategoriaRepetidaExcepcion;
import excepciones.CategoriaVaciaExcepcion;
import logica.Fabrica;
import logica.IControladorEvento;
import logica.ManejadorEvento;
import logica.ManejadorUsuario;

class TestCategoria {
	
	@BeforeEach @AfterEach
	void reiniciarSingleton() {
		ManejadorEvento.getInstancia().reiniciarSingleton();
		ManejadorUsuario.getInstancia().reiniciarSingleton();
	}

	@Test
	void testCategoria() throws Exception {
		IControladorEvento contrEv = Fabrica.getInstance().getIControladorEvento();

		// listarCategorias(evento)
		Set<String> catsEv1 = new HashSet<String>();
		catsEv1.add("cat1");
		catsEv1.add("cats2");
		
		// el conjunto cats debe estar incluido en las categorías cargadas en el sistema
		// pero esto se restringe mediante la interfaz gráfica
		contrEv.altaEvento("ev1", "desc1", LocalDate.of(2001, 1, 1), "s.i.g.l.a", catsEv1, null);
		
		assertArrayEquals(catsEv1.toArray(), contrEv.listarCategorias("ev1").toArray());
		
		// getCategorias y altaCategoria -- categorías del sistema
		
		assertArrayEquals(contrEv.getCategorias().toArray(), new String[]{}); // inicia sin ninguna cat
		
//		String[] catsEsperadas = {"cat2", "cat3", "cat1"}; // orden
		Set<String> catsEsperadas = new HashSet<>();
		catsEsperadas.add("cat1");
		catsEsperadas.add("cat2");
		catsEsperadas.add("cat3");
		
		contrEv.altaCategoria("cat1");
		contrEv.altaCategoria("cat2");
		contrEv.altaCategoria("cat3");
		
		assertEquals(contrEv.getCategorias().equals(catsEsperadas), true);
		
		// verificamos excepciones
		// 	cat vacia:		
		assertThrows(CategoriaVaciaExcepcion.class, ()-> {
			contrEv.altaCategoria("");
		});
		assertThrows(CategoriaVaciaExcepcion.class, ()-> {
			contrEv.altaCategoria("        ");
		});
		
		// cat repetida:
		assertThrows(CategoriaRepetidaExcepcion.class, () -> {
			contrEv.altaCategoria("cat1");
		});
		assertThrows(CategoriaRepetidaExcepcion.class, () -> {
			contrEv.altaCategoria("CAT1");
		});
	}
	

}
