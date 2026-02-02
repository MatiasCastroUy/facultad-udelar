package test;
import org.junit.jupiter.api.Test;

import excepciones.CategoriaEventoVacia;
import excepciones.EventoRepetido;
import logica.ControladorEvento;
import logica.Evento;
import logica.IControladorEvento;
import logica.ManejadorEvento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows ;



import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;



public class TestAltaEvento {
	

	 private IControladorEvento ctrlEvento;
	 private Evento tester;
	 private Evento tester2;
	 private Set<String> cats = new HashSet<>();
	 private Set<String> cats2 = new HashSet<>();
	
	@BeforeEach
	void setUp() {
	       
	       ctrlEvento = new ControladorEvento();
	       cats.add("Deporte");
	       cats.add("Música");
	       cats.add("Cultura");
	       tester = new Evento("ComicCon", "muchos comics", LocalDate.of(2025, 8, 8), "cc", cats, null);
	       tester2 = new Evento("pinguinos", "atun", LocalDate.of(2025, 8, 8), "cc", cats, null);
	}
	
	 @Test
	 void creacionCorrecta() throws Exception{
		 ctrlEvento.altaEvento("ComicCon", "muchos comics", LocalDate.of(2025, 8, 8), "cc", cats, null);
		 Evento evt = ManejadorEvento.getInstancia().getEvento("ComicCon");
		 assertNotNull(evt);
		 assertEquals(tester.getNombre(), evt.getNombre());
		 assertEquals(tester.getDesc(), evt.getDesc());
		 assertEquals(tester.getFechaAlta(), evt.getFechaAlta());
		 assertEquals(tester.getSigla(), evt.getSigla());
		 assertEquals(tester.getCategorias(), evt.getCategorias());		 
	 }
	 
	 @Test
	 void verNickRepetido() throws Exception{
		 ManejadorEvento.getInstancia().addEvento(tester2);
		 assertThrows(EventoRepetido.class, () -> {
			 ctrlEvento.altaEvento("pinguinos", "mapas y tesoros", LocalDate.of(2025, 8, 8), "AA", cats, null);
		 });
	 }
	 
	 @Test
	 void faltaCategotia() throws Exception{
		 assertThrows(CategoriaEventoVacia.class, () -> {
			 ctrlEvento.altaEvento("aaaa", "mapas y tesoros", LocalDate.of(2025, 8, 8), "AA", cats2, null);
		 });
	 }
}
