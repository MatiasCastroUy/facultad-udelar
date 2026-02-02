package test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import logica.ControladorEvento;
import logica.Edicion;
import logica.Evento;
import logica.ManejadorEvento;
import logica.ManejadorUsuario;
import logica.Organizador;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import enumerados.EstadoEdicion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;



public class TestConsultaEvento {

    private ControladorEvento ctrlEvento;

    @BeforeEach
    void setUp() throws Exception {
        ctrlEvento = new ControladorEvento();
        
        ctrlEvento.altaCategoria("CategoriaTest1");
        ctrlEvento.altaCategoria("CategoriaTest2");
        
        Set<String> categorias = new HashSet<>();
        categorias.add("CategoriaTest1");
        categorias.add("CategoriaTest2");
        ctrlEvento.altaEvento(
            "EventoConsulta",
            "Evento para pruebas de consulta",
            LocalDate.of(2025, 9, 1),
            "EVTCONS",
            categorias,
            null
        );
        Organizador orgTest = new Organizador(
                "orgTest",                
                "Organizador de Prueba",  
                "org@test.com",          
                "Organizador para testing", 
                "http://orgtest.com"      
            );
        
        ManejadorUsuario.getInstancia().addUsuario(orgTest);
        
        ctrlEvento.altaEdicionEvento(
                "EventoConsulta",              
                orgTest.getNickname(),             
                "EdicionTest",                 
                "EDTST",                      
                "Uruguay",                    
                "Montevideo",                  
                LocalDate.of(2025, 9, 10),     
                LocalDate.of(2025, 9, 15),     
                LocalDate.of(2025, 9, 1),
    			EstadoEdicion.Ingresada.toString(),
    			null     ,
    			null
            );
    }

    @Test
    void testConsultarEventoExistente() throws Exception {
        Evento evento = ManejadorEvento.getInstancia().obtenerEvento("EventoConsulta");
        assertNotNull(evento);
        assertEquals("EventoConsulta", evento.getNombre());
        assertEquals("Evento para pruebas de consulta", evento.getDesc());
        assertEquals(LocalDate.of(2025, 9, 1), evento.getFechaAlta());
        assertEquals("EVTCONS", evento.getSigla());
        assertTrue(evento.getCategorias().contains("CategoriaTest1"));
        assertTrue(evento.getCategorias().contains("CategoriaTest2"));
    }

    @Test
    void testObtenerEdicion() {
        // Act: pedimos la edición que dimos de alta en el setUp
        Edicion edicion = ManejadorEvento.getInstancia().obtenerEdicion("EventoConsulta", "EdicionTest");

        // Assert: verificamos que no sea null
        assertNotNull(edicion);

        // Chequear datos básicos
        assertEquals("EdicionTest", edicion.getNombre());
        assertEquals("EDTST", edicion.getSigla());
        assertEquals("Montevideo", edicion.getCiudad());
        assertEquals("Uruguay", edicion.getPais());
        assertEquals(LocalDate.of(2025, 9, 10), edicion.getFechaIni());
        assertEquals(LocalDate.of(2025, 9, 15), edicion.getFechaFin());

        // Chequear que referencia al evento correcto
        assertNotNull(edicion.getEvento());
        assertEquals("EventoConsulta", edicion.getEvento().getNombre());

        // Chequear organizador
        assertNotNull(edicion.getOrganizador());
        assertEquals("orgTest", edicion.getOrganizador().getNickname());
    }
    
    
    

    @AfterEach
    void reiniciarSingletons() {
    	ManejadorUsuario.getInstancia().reiniciarSingleton();
    	ManejadorEvento.getInstancia().reiniciarSingleton();
    }
    
    
    
}
