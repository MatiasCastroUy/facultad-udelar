package test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import enumerados.EstadoEdicion;
import enumerados.Nivel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.List;

import logica.IControladorUsuario;
import logica.IControladorEvento;
import logica.ControladorUsuario;
import logica.ControladorEvento;
import logica.ManejadorUsuario;
import logica.ManejadorEvento;
import logica.Evento;
import logica.Usuario;
import logica.Edicion;
import logica.Organizador;
import logica.DTEvento;
import logica.DTEdicion;
import logica.DTOrganizador;
import logica.DTPatrocinador;

import excepciones.UsuarioNoExisteException;


public class TestConsultaEdicionEvento {
	
	private IControladorUsuario ctrlUsuario;
    private IControladorEvento ctrlEvento;

    private List<Evento> eventosLst;
    private List<Usuario> usuariosLst;
    private List<Edicion> edicionesLst;
	
	@BeforeEach
	void setUp(TestInfo testInfo) {
		Boolean saltarCreacionDeSetup = testInfo.getDisplayName().contains("sinSetup");
		
		// Inicializamos las propiedades
		ctrlUsuario = new ControladorUsuario();
		ctrlEvento = new ControladorEvento();

		eventosLst = new Vector<Evento>();
		usuariosLst = new Vector<Usuario>();
		edicionesLst = new Vector<Edicion>();
		
		// Saltamos la creación de datos de prueba para aquellos métodos que requieran no tenerlos.
		if (saltarCreacionDeSetup) return;
		
		
		// Definimos datos iniciales
		for (Integer i = 1; i <= 2; i++) {
			Evento evento = new Evento(
				"Evento_" + i,
				"Desc_" + i,
				LocalDate.of(2025, 8, i),
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
		
		
		// Agregamos las ediciones
		for (Integer i = 1; i <= 2; i++) {
			for (Integer j = 1; j <= i + 1; j++) {
				Edicion edicion = new Edicion(
					eventosLst.get(i - 1),
					"ED_" + i + "_" + j,
					"SIG_" + i + "_" + j,
					LocalDate.of(2025, 9, 1),
					LocalDate.of(2025, 9, 30),
					LocalDate.of(2025, 8, 29),
					"Ciudad_" + j,
					"Pais_" + j,
					(Organizador) usuariosLst.get(i - 1),
					EstadoEdicion.Aceptada,
					null,
					null
				);
		        
				edicionesLst.add(edicion);
				
				eventosLst.get(i - 1).agregarEdicion(edicion);
			}
		}
	}
	
	
	@Test
	void testListarEdicionesPositivo() throws Exception {
		for (Integer i = 1; i <= 2; i++) {
			// Obtenemos las ediciones del evento de la iteración actual
			Set<String> ediciones = ctrlEvento.listarEdiciones(eventosLst.get(i - 1).getNombre());
			List<String> edicionesList = new Vector<String>(ediciones);
		
			// Verificar tamaño de salida
			// Valor esperado: 2, 3
			assertEquals(i + 1, edicionesList.size());
			
			// Verificar que las salidas coincidan
			for (Integer j = 1; j <= edicionesList.size(); j++) {
				// Valores esperados:
				// 		Para el primer evento: ED_1_1, ED_1_2
				//		Para el segundo evento: ED_2_1, ED_2_2, ED_2_3
				assertEquals("ED_" + i + "_" + j, edicionesList.get(j - 1));
			}
		}
	}
	
	
	@Test
	void testGetDTEvento2Positivo() throws Exception {
		// El índice i corresponde al evento.
		for (Integer i = 1; i <= 2; i++) {
			// Obtenemos el evento
			DTEvento evento = ctrlEvento.getDTEvento("Evento_" + i);
			
			// Verificamos que los valores sean los correctos
			
			// Valor esperado: Evento_1, Evento_2
			assertEquals("Evento_" + i, evento.getNombre());
			
			// Valor esperado: Desc_1, Desc_2
			assertEquals("Desc_" + i, evento.getDescripcion());
			
			// Valor esperado: SIG_1, SIG_2
			assertEquals("SIG_" + i, evento.getSigla());
			
			// Valor esperado: LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 2)
			assertEquals(LocalDate.of(2025, 8, i), evento.getFechaAlta());
		}
	}
	
	
	@Test
	void testgetDTEdicionPositivo() throws Exception {
		// El índice i corresponde al evento.
		for (Integer i = 1; i <= 2; i++) {
			// El índice j corresponde a la edición del evento i.
			for (Integer j = 1; j <= i + 1; j++) {
				// Obtenemos el DTEdicion de la edición de la iteración actual
				DTEdicion edicion = ctrlEvento.getDTEdicion("Evento_" + i, "ED_" + i + "_" + j);
				
				// Verificamos que los valores sean los correctos.

				// Valor esperado:
				// 		Primer evento: ED_1_1, ED_1_2
				//		Segundo evento: ED_2_1, ED_2_2, ED_2_3
				assertEquals("ED_" + i + "_" + j, edicion.getNombre());

				// Valor esperado:
				// 		Primer evento: SIG_1_1, SIG_1_2
				//		Segundo evento: SIG_2_1, SIG_2_2, SIG_2_3
				assertEquals("SIG_" + i + "_" + j, edicion.getSigla());

				// Valor esperado:
				// 		Primer evento: Pais_1, Pais_2
				//		Segundo evento: Pais_1, Pais_2, Pais_3
				assertEquals("Pais_" + j, edicion.getPais());
				
				// Valor esperado:
				// 		Primer evento: Ciudad_1, Ciudad_2
				//		Segundo evento: Ciudad_1, Ciudad_2, Ciudad_3
				assertEquals("Ciudad_" + j, edicion.getCiudad());

				// Valor esperado:
				// 		Primer evento: Org_1, Org_1
				//		Segundo evento: Org_2, Org_2, Org_2
				assertEquals("Org_" + i, edicion.getNicknameOrganizador());
				
				// Valor esperado:
				// 		Primer evento: LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 1)
				//		Segundo evento: LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 1)
				assertEquals(LocalDate.of(2025, 9, 1), edicion.getFechaInicio());
				
				// Valor esperado:
				// 		Primer evento: LocalDate.of(2025, 9, 30), LocalDate.of(2025, 9, 30)
				//		Segundo evento: LocalDate.of(2025, 9, 30), LocalDate.of(2025, 9,30), LocalDate.of(2025, 9, 30)
				assertEquals(LocalDate.of(2025, 9, 30), edicion.getFechaFin());
				
				// Valor esperado:
				// 		Primer evento: LocalDate.of(2025, 8, 29), LocalDate.of(2025, 8, 29)
				//		Segundo evento: LocalDate.of(2025, 8, 29), LocalDate.of(2025, 8, 29), LocalDate.of(2025, 8, 29)
				assertEquals(LocalDate.of(2025, 8, 29), edicion.getFechaAlta());
			}
		}
	}
	
	
	@Test
	void testObtenerInfoOrganizadorPositivo() throws Exception{
		// El índice i corresponde al evento.
		for (Integer i = 1; i <= 2; i++) {
			// El índice j corresponde a la edición del evento i.
			for (Integer j = 1; j <= i + 1; j++) {
				// Obtenemos el DTOrganizador del organizador de la edición de la iteración actual
				DTOrganizador organizador = ctrlUsuario.obtenerInfoOrganizador("Org_" + i);
				
				// Verificamos que los valores sean los correctos.

				// Valor esperado:
				// 		Primer evento: Nombre_1, Nombre_1
				//		Segundo evento: Nombre_2, Nombre_2, Nombre_2
				assertEquals("Nombre_" + i, organizador.getNombre());

				// Valor esperado:
				// 		Primer evento: Org_1, Org_1
				//		Segundo evento: Org_2, Org_2, Org_2
				assertEquals("Org_" + i, organizador.getNickname());

				// Valor esperado:
				// 		Primer evento: correo_1@correo.com, correo_1@correo.com
				//		Segundo evento: correo_2@correo.com, correo_2@correo.com, correo_2@correo.com
				assertEquals("correo_" + i + "@correo.com", organizador.getCorreo());
				
				// Valor esperado:
				// 		Primer evento: Desc_1, Desc_1
				//		Segundo evento: Desc_2, Desc_2, Desc_2
				assertEquals("Desc_" + i, organizador.getDescripcion());

				// Valor esperado:
				// 		Primer evento: https://pagina1.uy/, https://pagina1.uy/
				//		Segundo evento: https://pagina2.uy/, https://pagina2.uy/, https://pagina2.uy/
				assertEquals("https://pagina" + i + ".uy/", organizador.getSitioWeb());
			}
		}
	}
	
	
	@Test
	void testObtenerInfoOrganizadorNegativo() throws Exception {
		// Caso donde el usuario no existe
		
		assertThrows(UsuarioNoExisteException.class,
			() -> ctrlUsuario.obtenerInfoOrganizador(""));
	}
	
	
	@Test
	void testListarTipoRegistroPositivo() {
		// El índice i corresponde al evento.
		for (Integer i = 1; i <= 2; i++) {
			// El índice j corresponde a la edición del evento i.
			for (Integer j = 1; j <= i + 1; j++) {
				// Obtenemos la lista de TipoRegistro de la iteración actual
				Set<String> tipoRegistro = ctrlEvento.listarTipoRegistro("Evento_" + i, "ED_" + i + "_" + j);
				assertEquals(0, tipoRegistro.size());
			}
		}
	}

	
	@Test
	void misc() {
		// Métodos relacionados con el caso de uso
		DTOrganizador dtOrg = new DTOrganizador();
		
		// Setters Organizador
		Organizador org = (Organizador) usuariosLst.get(0);
		org.setDescripcion("Prueba");
		org.setSitioWeb("https://eva.fing.edu.uy/");
		org.setCorreo("correo@correo.uy");
		org.setNickname("Prueba");
		org.setNombre("Alejandro");
		
		// Setters DTEvento
		DTEvento evt = new DTEvento("Nombre", "Descripción", LocalDate.of(2025, 1, 1), "Sigla", null, false, 0);
		evt.setNombre("Nombre");
		evt.setDescripcion("Descripción");
		evt.setFechaAlta(LocalDate.of(2025, 1, 1));
		evt.setSigla("Sigla");
		
		// DTPatrocinador
		DTPatrocinador patrocinador = new DTPatrocinador("Patrocinador", 10000.0f, 100, "Codigo", Nivel.Oro);
		String nombrePatrocinador = patrocinador.getNombre();
		Float montoAportado = patrocinador.getMontoAportado();
		Integer cantidadRegistrosGratis = patrocinador.getCantidadRegistrosGratis();
		String codigo = patrocinador.getCodigo();
		Nivel nivel = patrocinador.getNivel();
		
		// Assertions
		assertEquals("", dtOrg.getNickname());
		assertEquals("Patrocinador", nombrePatrocinador);
		assertEquals(10000.0f, montoAportado);
		assertEquals(100, cantidadRegistrosGratis);
		assertEquals("Codigo", codigo);
		assertEquals(Nivel.Oro, nivel);
	}
	
    @AfterEach
    void reiniciarSingletons() {
    	ManejadorUsuario.getInstancia().reiniciarSingleton();
    	ManejadorEvento.getInstancia().reiniciarSingleton();
    }
}
