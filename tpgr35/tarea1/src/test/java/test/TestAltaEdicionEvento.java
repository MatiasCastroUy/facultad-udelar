package test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import logica.IControladorUsuario;
import logica.IControladorEvento;
import logica.ControladorUsuario;
import logica.ControladorEvento;
import logica.ManejadorUsuario;
import logica.ManejadorEvento;
import logica.Asistente;
import logica.Evento;
import logica.Usuario;
import logica.Edicion;
import logica.Organizador;
import logica.DTOrganizador;

import excepciones.UsuarioNoExisteException;
import excepciones.EdicionRepetidaException;

import enumerados.EstadoEdicion;


public class TestAltaEdicionEvento { 
	
    private IControladorUsuario ctrlUsuario;
    private IControladorEvento ctrlEvento;
    
    private List<Evento> eventosLst;
    private List<Usuario> usuariosLst;
	
	@BeforeEach
	void setUp(TestInfo testInfo) {
		Boolean saltarCreacionDeSetup = testInfo.getDisplayName().contains("sinSetup");
		
		// Inicializamos las propiedades
		ctrlUsuario = new ControladorUsuario();
		ctrlEvento = new ControladorEvento();
		
		eventosLst = new Vector<Evento>();
		usuariosLst = new Vector<Usuario>();
		
		// Saltamos la creación de datos de prueba para aquellos métodos que requieran no tenerlos.
		if (saltarCreacionDeSetup) return;
		
		
		// Definimos datos iniciales
		for (Integer i = 1; i <= 2; i++) {
			Evento evento = new Evento(
				"Evento_" + i,
				"Desc_" + i,
				LocalDate.now(),
				"SIG_" + i,
				new HashSet<>(),
				null
			);
	        
			eventosLst.add(evento);
		}
		
		for (Integer i = 1; i <= 3; i++) {
			Organizador organizador = new Organizador(
	    		"Org_" + i,
	    		"Nombre_" + i,
	    		"correo_" + i + "@correo.com",
	    		"Desc_" + i,
	    		"https://pagina" + i + ".uy/"
	        );
	        
			usuariosLst.add(organizador);
		}
        
        
        // Los agregamos a sus respectivas colecciones
		for (Evento e : eventosLst) {
			ManejadorEvento.getInstancia().addEvento(e);
		}
		
		for (Usuario o : usuariosLst) {
			ManejadorUsuario.getInstancia().addUsuario((Organizador) o);
		}
	}
	
	
	@Test
	void testListarEventosPositivo() throws Exception {
		Set<String> eventosSet = ctrlEvento.listarEventos();
		List<String> eventosList = new Vector<String>(eventosSet);
		
		// Verificar tamaño de salida
		// Valor esperado: 2
		assertEquals(2, eventosList.size());
		
		// Verificar que las salidas coincidan
		for (Integer i = 1; i <= eventosList.size(); i++) {
			// Valores esperados: Evento_1, Evento_2
			assertEquals("Evento_" + i, eventosList.get(i - 1));
		}
	}
	
	
	@Test
	void testListarOrganizadoresPositivo() throws Exception {
		// Caso donde puede listar organizadores sin problemas
		
		Set<DTOrganizador> organizadores = ctrlUsuario.listarOrganizadores();
		
		// Creamos una lista ordenada para hacer las comparaciones
		// El caso de uso no exige que los organizadores estén ordenados
		List<DTOrganizador> organizadoresList = new Vector<DTOrganizador>(organizadores);
		Collections.sort(organizadoresList, new Comparator<DTOrganizador>() {
            @Override
            public int compare(DTOrganizador dt1, DTOrganizador dt2) {
                return dt1.getNickname().compareTo(dt2.getNickname());
            }
		});
		
		// Verificar tamaño de salida
		// Valor esperado: 3
		assertEquals(3, organizadoresList.size());
		
		// Verificar que las salidas coincidan
		for (Integer i = 1; i <= organizadoresList.size(); i++) {
			// Valores esperados: Org_1, Org_2, Org_3
			assertEquals("Org_" + i, organizadoresList.get(i - 1).getNickname());
			
			// Valores esperados: Nombre_1, Nombre_2, Nombre_3
			assertEquals("Nombre_" + i, organizadoresList.get(i - 1).getNombre());
			
			// Valores esperados: correo_1@correo.com, correo_2@correo.com, correo_3@correo.com
			assertEquals("correo_" + i + "@correo.com", organizadoresList.get(i - 1).getCorreo());
			
			// Valores esperados: Desc_1, Desc_2, Desc_3
			assertEquals("Desc_" + i, organizadoresList.get(i - 1).getDescripcion());
			
			// Valores esperados: https://pagina1.uy/, https://pagina2.uy/, https://pagina3.uy/
			assertEquals("https://pagina" + i + ".uy/", organizadoresList.get(i - 1).getSitioWeb());
		}
	}

	
	@Test
	void sinSetuptestListarOrganizadoresNegativo1() throws Exception {
		// Caso donde no hay usuarios
		
		// Debería arrojar una excepción		
		assertThrows(UsuarioNoExisteException.class,
			() -> ctrlUsuario.listarOrganizadores());
	}
	
	
	@Test
	void sinSetuptestListarOrganizadoresNegativo2() throws Exception {
		// Caso donde no hay organizadores
		
		// Creamos setup donde hay solo asistentes
		Asistente asistente = new Asistente(
    		"Asis_1",
    		"Nombre_1",
    		"correo_1@correo.com",
    		"Apellido_1",
    		LocalDate.of(1996, 10, 16)
        );
        
		ManejadorUsuario.getInstancia().addUsuario((Asistente) asistente);
		
		// Debería arrojar una excepción		
		assertThrows(UsuarioNoExisteException.class,
			() -> ctrlUsuario.listarOrganizadores());
	}
	
	
	@Test
	void testAltaEdicionEventoPositivo() throws Exception {
		// Verificamos que Evento_1 no tiene ediciones.
		Set<String> ediciones = ctrlEvento.listarEdiciones("Evento_1");
		
		// Valor esperado: 0
		assertEquals(0, ediciones.size());
		
		// Creamos una edición para Evento_1
		ctrlEvento.altaEdicionEvento(
			"Evento_1",
			"Org_1",
			"Edicion_1",
			"SIG_Edicion_1",
			"UY",
			"Memevideo",
			LocalDate.of(2025, 9, 1),
			LocalDate.of(2025, 9, 30),
			LocalDate.of(2025, 8, 29),
			EstadoEdicion.Ingresada.toString(),
			null,
			null
		);
		
		// Verificamos que Evento_1 ahora tiene una edición
		ediciones = ctrlEvento.listarEdiciones("Evento_1");
		
		// Valor esperado: 1
		assertEquals(1, ediciones.size());
		
		// Obtenemos la edición y verificamos que los datos estén correctos
		Evento evento = ManejadorEvento.getInstancia().getEvento("Evento_1");
		Edicion edicion = evento.getEdicion("Edicion_1");
		
		// Valor esperado: Edicion_1
		assertEquals("Edicion_1", edicion.getNombre());
		
		// Valor esperado: SIG_Edicion_1
		assertEquals("SIG_Edicion_1", edicion.getSigla());

		// Valor esperado: LocalDate.of(2025, 9, 1)
		assertEquals(LocalDate.of(2025, 9, 1), edicion.getFechaIni());
		
		// Valor esperado: LocalDate.of(2025, 9, 30)
		assertEquals(LocalDate.of(2025, 9, 30), edicion.getFechaFin());
		
		// Valor esperado: LocalDate.of(2025, 8, 29)
		assertEquals(LocalDate.of(2025, 8, 29), edicion.getFechaAlta());
		
		// Valor esperado: Memevideo
		assertEquals("Memevideo", edicion.getCiudad());
		
		// Valor esperado: UY
		assertEquals("UY", edicion.getPais());
		
		// Comparamos instancias de Evento, para asegurarnos de que apunten al mismo
		assertEquals(evento, edicion.getEvento());
		
		// Comparamos instancias de Organizador
		Organizador org = (Organizador) ManejadorUsuario.getInstancia().findUsuario("Org_1");
		assertEquals(org, edicion.getOrganizador());		
	}
	
	
	@Test
	void testAltaEdicionEventoNegativo1() throws Exception {
		// Caso donde el evento no existe
		
		// Debería arrojar una excepción		
		assertThrows(EdicionRepetidaException.class,
			() -> ctrlEvento.altaEdicionEvento(
					"Evento_4",
					"Org_1",
					"Edicion_1",
					"SIG_Edicion_1",
					"UY",
					"Memevideo",
					LocalDate.of(2025, 9, 1),
					LocalDate.of(2025, 9, 30),
					LocalDate.of(2025, 8, 29),
					EstadoEdicion.Ingresada.toString(),
					null,
					null
				)
		);
	}
	
	
	@Test
	void testAltaEdicionEventoNegativo2() throws Exception {
		// Caso donde el evento existe pero la edición está repetida
		
		// Verificamos que Evento_1 no tiene ediciones.
		Set<String> ediciones = ctrlEvento.listarEdiciones("Evento_1");
		
		// Valor esperado: 0
		assertEquals(0, ediciones.size());
		
		// Creamos una edición para Evento_1
		ctrlEvento.altaEdicionEvento(
			"Evento_1",
			"Org_1",
			"Edicion_1",
			"SIG_Edicion_1",
			"UY",
			"Memevideo",
			LocalDate.of(2025, 9, 1),
			LocalDate.of(2025, 9, 30),
			LocalDate.of(2025, 8, 29),
			EstadoEdicion.Ingresada.toString(),
			null,
			null
		);
		
		// Verificamos que Evento_1 ahora tiene una edición
		ediciones = ctrlEvento.listarEdiciones("Evento_1");
		
		// Valor esperado: 1
		assertEquals(1, ediciones.size());
		
		// Debería arrojar una excepción		
		assertThrows(EdicionRepetidaException.class,
			() -> ctrlEvento.altaEdicionEvento(
					"Evento_1",
					"Org_1",
					"Edicion_1",
					"SIG_Edicion_1",
					"UY",
					"Memevideo",
					LocalDate.of(2025, 9, 1),
					LocalDate.of(2025, 9, 30),
					LocalDate.of(2025, 8, 29),
					EstadoEdicion.Ingresada.toString(),
					null,
					null
				)
		);
		
		// Verificamos que Evento_1 sigue teniendo una edición
		ediciones = ctrlEvento.listarEdiciones("Evento_1");
		
		// Valor esperado: 1
		assertEquals(1, ediciones.size());
	}
	
    @AfterEach
    void reiniciarSingletons() {
    	ManejadorUsuario.getInstancia().reiniciarSingleton();
    	ManejadorEvento.getInstancia().reiniciarSingleton();
    }

}
