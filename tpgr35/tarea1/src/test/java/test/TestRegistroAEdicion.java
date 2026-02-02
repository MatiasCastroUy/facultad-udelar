package test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import enumerados.EstadoEdicion;
import excepciones.CupoLlenoExcepcion;
import excepciones.YaRegistradoExcepcion;
import logica.Asistente;
import logica.ControladorEvento;
import logica.ControladorUsuario;
import logica.DTRegistro;
import logica.Edicion;
import logica.Evento;
import logica.ManejadorEvento;
import logica.ManejadorUsuario;
import logica.Organizador;
import logica.Registro;
import logica.TipoRegistro;

public class TestRegistroAEdicion {

    @Test
    void testAltaRegistroExitoso() throws Exception {
        Evento evento = new Evento("Evento Test", "desc", LocalDate.now(), "EVT", new HashSet<>(), null);
        ManejadorEvento.getInstancia().addEvento(evento);

        Organizador organizador = new Organizador("imm", "Intendencia de Montevideo", "imm@imm.gub.uy",
                "Gobierno Departamental de Montevideo",
                "https://montevideo.gub.uy");

        Edicion edicion = new Edicion(evento, "Edicion Test", "EDT",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                LocalDate.of(2025, 12, 31),
                "Montevideo", "UY", organizador, EstadoEdicion.Aceptada, null, null);
        evento.agregarEdicion(edicion);

        TipoRegistro tipo = new TipoRegistro("General", "Registro general", 100f, 2);
        edicion.agregarTipoRegistro("General", tipo);

        Asistente asistente = new Asistente("nickTest", "Juan", "juan@mail.com", "Perez", LocalDate.of(2000, 1, 1));
        ManejadorUsuario.getInstancia().addUsuario(asistente);

        ControladorEvento ctrlEvento = new ControladorEvento();

        ctrlEvento.altaRegistro("Evento Test", "Edicion Test", "General", "nickTest", LocalDate.of(2025, 1, 1));

        assertTrue(asistente.estaRegistradoEn("Edicion Test"));
        assertEquals(1, tipo.getCupoDisponible());
        
        

    	DTRegistro registro = ctrlEvento.getRegistro("Evento Test", "Edicion Test", "nickTest");
    	assertEquals("Evento Test", registro.getEvento());
    	assertEquals("Edicion Test", registro.getEdicion());
    	assertEquals("General", registro.getTipo());
    	
    	ControladorUsuario ctrlUsuario = new ControladorUsuario();
    	Map<String, DTRegistro> registrosEdicion = ctrlUsuario.listarRegistrosEdicion("Edicion Test");
    	assertEquals(1, registrosEdicion.size());
    }

    @Test
    void testAltaRegistroSinCupo() throws Exception {
        Evento evento = new Evento("Evento Test", "desc", LocalDate.now(), "EVT", new HashSet<>(), null);
        ManejadorEvento.getInstancia().addEvento(evento);

        Organizador organizador = new Organizador("imm", "Intendencia de Montevideo", "imm@imm.gub.uy",
                "Gobierno Departamental de Montevideo",
                "https://montevideo.gub.uy");

        Edicion edicion = new Edicion(evento, "Edicion Test", "EDT",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                LocalDate.of(2025, 12, 31),
                "Montevideo", "UY", organizador, EstadoEdicion.Aceptada, null, null);
        evento.agregarEdicion(edicion);

        TipoRegistro tipo = new TipoRegistro("General", "Registro general", 100f, 0);
        edicion.agregarTipoRegistro("General", tipo);

        Asistente asistente = new Asistente("nickTest", "Juan", "juan@mail.com", "Perez", LocalDate.of(2000, 1, 1));
        ManejadorUsuario.getInstancia().addUsuario(asistente);

        ControladorEvento ctrlEvento = new ControladorEvento();

        assertThrows(CupoLlenoExcepcion.class, () -> {
            ctrlEvento.altaRegistro("Evento Test", "Edicion Test", "General", "nickTest", LocalDate.of(2025, 1, 1));
        });
    }

    @Test
    void testAltaRegistroDuplicado() throws Exception {
        Evento evento = new Evento("Evento Test", "desc", LocalDate.now(), "EVT", new HashSet<>(), null);
        ManejadorEvento.getInstancia().addEvento(evento);

        Organizador organizador = new Organizador("imm", "Intendencia de Montevideo", "imm@imm.gub.uy",
                "Gobierno Departamental de Montevideo",
                "https://montevideo.gub.uy");

        Edicion edicion = new Edicion(evento, "Edicion Test", "EDT",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                LocalDate.of(2025, 12, 31),
                "Montevideo", "UY", organizador, EstadoEdicion.Aceptada, null, null);
        evento.agregarEdicion(edicion);

        TipoRegistro tipo = new TipoRegistro("General", "Registro general", 100f, 2);
        edicion.agregarTipoRegistro("General", tipo);

        Asistente asistente = new Asistente("nickTest", "Juan", "juan@mail.com", "Perez", LocalDate.of(2000, 1, 1));
        ManejadorUsuario.getInstancia().addUsuario(asistente);

        ControladorEvento ctrlEvento = new ControladorEvento();

        ctrlEvento.altaRegistro("Evento Test", "Edicion Test", "General", "nickTest", LocalDate.of(2025, 1, 1));

        assertThrows(YaRegistradoExcepcion.class, () -> {
            ctrlEvento.altaRegistro("Evento Test", "Edicion Test", "General", "nickTest", LocalDate.of(2025, 1, 1));
        });
    }

    @Test
    void testCrearRegistroDirecto() {
        Evento evento = new Evento("Evento Test", "desc", LocalDate.now(), "EVT", new HashSet<>(), null);
        Organizador organizador = new Organizador("imm", "Intendencia de Montevideo", "imm@imm.gub.uy",
                "Gobierno Departamental de Montevideo",
                "https://montevideo.gub.uy");

        Edicion edicion = new Edicion(evento, "Edicion Test", "EDT",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                LocalDate.of(2025, 12, 31),
                "Montevideo", "UY", organizador, EstadoEdicion.Aceptada, null, null);
        TipoRegistro tipo = new TipoRegistro("General", "Registro general", 100f, 5);

        Asistente asistente = new Asistente("nickTest", "Juan", "juan@mail.com", "Perez", LocalDate.of(2000, 1, 1));

        LocalDate fecha = LocalDate.of(2025, 1, 1);
        Registro reg = new Registro(asistente, tipo, edicion, fecha);

        assertEquals(asistente, reg.getAsistente());
        assertEquals(edicion, reg.getEdicion());
        assertEquals("General", reg.getTipo().getNombre());
        assertEquals(100f, reg.getCosto());
        assertEquals(fecha, reg.getFechaRegistro());
    }

    @Test
    void testListarEventosEdicionesyTipoRegistros() throws Exception {
        ControladorEvento ctrlEvento = new ControladorEvento();
        ManejadorEvento manEvento = ManejadorEvento.getInstancia();

        Evento evento = new Evento("Evento Test", "desc", LocalDate.now(), "EVT", new HashSet<>(), null);
        manEvento.addEvento(evento);

        Organizador organizador = new Organizador("imm", "Intendencia de Montevideo", "imm@imm.gub.uy",
                "Gobierno Departamental de Montevideo",
                "https://montevideo.gub.uy");

        Edicion edicion = new Edicion(evento, "Edicion Test", "EDT",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                LocalDate.of(2025, 12, 31),
                "Montevideo", "UY", organizador, EstadoEdicion.Aceptada, null, null);
        evento.agregarEdicion(edicion);

        TipoRegistro tipo = new TipoRegistro("General", "desc", 50f, 5);
        edicion.agregarTipoRegistro("General", tipo);

        Set<String> eventos = ctrlEvento.listarEventos();
        assertTrue(eventos.contains("Evento Test"));

        Set<String> ediciones = evento.listarEdiciones();
        assertTrue(ediciones.contains("Edicion Test"));

        Set<String> tipos = edicion.listarTipoRegistro();
        assertTrue(tipos.contains("General"));
    }

    @Test
    void testGetCupoMaximoDeTipoRegistro() {
        TipoRegistro tipo = new TipoRegistro("VIP", "desc", 200f, 20);
        assertEquals(20, tipo.getCupoMaximo());
    }

    @Test
    void testReducirCupoExitoso() {
        TipoRegistro tipo = new TipoRegistro("VIP", "desc", 200f, 2);
        tipo.reducirCupo();
        assertEquals(1, tipo.getCupoDisponible());
    }

    @Test
    void testReducirCupoSinDisponibles() {
        TipoRegistro tipo = new TipoRegistro("VIP", "desc", 200f, 1);
        tipo.reducirCupo();
        assertThrows(IllegalStateException.class, () -> {
            tipo.reducirCupo();
        });
    }
    
    @Test
    void testRegistroConCodigo() throws Exception {
        Evento evento = new Evento("Evt", "desc", LocalDate.now(), "EVT", new HashSet<>(), null);
        ManejadorEvento.getInstancia().addEvento(evento);

        Organizador org = new Organizador("imm", "IMM", "imm@gub.uy", "Gob", "https://x");
        Edicion edition = new Edicion(evento, "Ed", "EDT",
                LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31), LocalDate.of(2025, 12, 31),
                "Montevideo", "UY", org, EstadoEdicion.Aceptada, null, null);
        evento.agregarEdicion(edition);

        TipoRegistro tipo = new TipoRegistro("General", "desc", 100f, 1);
        edition.agregarTipoRegistro("General", tipo);

        Asistente as1 = new Asistente("nick1", "Juan", "j1@mail.com", "Perez", LocalDate.of(2000, 1, 1));
        Asistente as2 = new Asistente("nick2", "Ana",  "j2@mail.com", "Gomez", LocalDate.of(2000, 2, 2));
        ManejadorUsuario.getInstancia().addUsuario(as1);
        ManejadorUsuario.getInstancia().addUsuario(as2);

        LocalDate fecha = LocalDate.of(2025, 1, 1);
        Registro reg = new Registro(as1, tipo, edition, fecha, "COD-123");
        assertEquals("COD-123", reg.getCodPatrocinio());
        assertEquals(as1, reg.getAsistente());
        assertEquals(edition, reg.getEdicion());
        assertEquals(fecha, reg.getFechaRegistro());

        ControladorEvento ctrl = new ControladorEvento();
        ctrl.altaRegistroCodigo("Evt", "Ed", "General", "nick1", fecha, "COD-123");
        assertTrue(as1.estaRegistradoEn("Ed"));
        assertEquals(0, tipo.getCupoDisponible());       
        DTRegistro dto = ctrl.getRegistro("Evt", "Ed", "nick1");
        assertEquals("Evt", dto.getEvento());
        assertEquals("Ed", dto.getEdicion());
        assertEquals("General", dto.getTipo());

        assertThrows(YaRegistradoExcepcion.class, () ->
            ctrl.altaRegistroCodigo("Evt", "Ed", "General", "nick1", fecha, "COD-123")
        );

        assertThrows(CupoLlenoExcepcion.class, () ->
            ctrl.altaRegistroCodigo("Evt", "Ed", "General", "nick2", fecha, "COD-XYZ")
        );
    }

}

