package test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import enumerados.EstadoEdicion;
import logica.Asistente;
import logica.ControladorUsuario;
import logica.DTAsistente;
import logica.DTRegistro;
import logica.Edicion;
import logica.Evento;
import logica.ManejadorEvento;
import logica.ManejadorUsuario;
import logica.Organizador;
import logica.Registro;
import logica.TipoRegistro;
import excepciones.UsuarioNoExisteException;

public class TestConsultaDeRegistro {

    private ControladorUsuario ctrlUsuario;
    private Asistente asistente;
    private Evento evento;
    private Edicion edicion;
    private Organizador organizador;
    private TipoRegistro tipo;
    private Registro registro;

    @BeforeEach
    void setUp() {
        ctrlUsuario = new ControladorUsuario();

        // Crear escenario mínimo
        evento = new Evento("Conferencia Java", "desc", LocalDate.now(), "CJ", new HashSet<>(), null);

        organizador = new Organizador("imm", "Intendencia de Montevideo", "imm@imm.gub.uy",
                "Gobierno Departamental de Montevideo",
                "https://montevideo.gub.uy");

        edicion = new Edicion(evento, "Edicion 2025", "CJ2025",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                LocalDate.of(2025, 12, 31),
                "Montevideo", "UY", organizador, EstadoEdicion.Aceptada, null, null);

        tipo = new TipoRegistro("General", "desc", 100f, 50);

        asistente = new Asistente("nick", "Juan", "juan@mail.com", "Perez", LocalDate.of(2000, 1, 1));
        registro = new Registro(asistente, tipo, edicion, LocalDate.of(2025, 1, 1)); // ✅ con fecha explícita
        asistente.agregarRegistro(edicion.getNombre(), registro);

        ManejadorUsuario.getInstancia().addUsuario(asistente);
    }

    @Test
    void testListarAsistentes() throws Exception {
        Set<DTAsistente> asistentes = ctrlUsuario.listarAsistentes();
        assertEquals(1, asistentes.size());
        assertEquals("nick", asistentes.iterator().next().getNickname());
    }

    @Test
    void testListarNombresRegistrosDeUsuario() throws Exception {
        Set<String> nombres = ctrlUsuario.listarNombresRegistrosDeUsuario("nick");
        assertTrue(nombres.stream().anyMatch(s -> s.contains("Edicion 2025")));
    }

    @Test
    void testObtenerRegistroDeUsuario() throws Exception {
        DTRegistro dto = ctrlUsuario.obtenerRegistroDeUsuario("nick", "Edicion 2025");
        assertNotNull(dto);
        assertEquals("Conferencia Java", dto.getEvento());
        assertEquals("Edicion 2025", dto.getEdicion());
        assertEquals("General", dto.getTipo());
        assertEquals(100f, dto.getCosto());
    }

    @Test
    void testObtenerRegistroPorEdicionDesdeAsistente() {
        Registro reg = asistente.obtenerRegistroPorEdicion("Edicion 2025");
        assertNotNull(reg);
        assertEquals("General", reg.getTipo().getNombre());
    }

    @Test
    void testUsuarioNoExisteEnListarRegistros() {
        assertThrows(UsuarioNoExisteException.class,
                () -> ctrlUsuario.listarNombresRegistrosDeUsuario("noexiste"));
    }

    @Test
    void testRegistroNoExiste() throws Exception {
        DTRegistro dto = ctrlUsuario.obtenerRegistroDeUsuario("nick", "EdicionInexistente");
        if (dto.getEvento().equalsIgnoreCase("__ERROR__")) { dto = null ; };
        assertNull(dto);
    }

    @Test
    void testUsuarioNoExisteEnListarNombresRegistros() {
        assertThrows(UsuarioNoExisteException.class,
                () -> ctrlUsuario.listarNombresRegistrosDeUsuario("usuarioInexistente"));
    }

    @Test
    void testUsuarioNoExisteEnObtenerRegistro() {
        assertThrows(UsuarioNoExisteException.class,
                () -> ctrlUsuario.obtenerRegistroDeUsuario("usuarioInexistente", "Edicion 2025"));
    }

    @Test
    void testDTRegistroGetFechaYToString() {
        LocalDate fecha = LocalDate.of(2025, 1, 1);
        DTRegistro dto = new DTRegistro("Evento X", "Edicion Y", fecha, "General", 123.45f);

        assertEquals(fecha, dto.getFecha());

        String str = dto.toString();
        assertTrue(str.contains("Evento X"));
        assertTrue(str.contains("Edicion Y"));
    }

    @AfterEach
    void reiniciarSingletons() {
        ManejadorUsuario.getInstancia().reiniciarSingleton();
        ManejadorEvento.getInstancia().reiniciarSingleton();
    }
}
