package logica;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import excepciones.TipoRegistroRepetidoExcepcion;
import excepciones.EdicionRepetidaException;
import excepciones.CategoriaEventoVacia;
import excepciones.CategoriaRepetidaExcepcion;
import excepciones.CategoriaVaciaExcepcion;
import excepciones.EventoRepetido;
import excepciones.CupoLlenoExcepcion;
import excepciones.YaRegistradoExcepcion;

public interface IControladorEvento {
	
	public abstract void altaEvento(String nom, String desc, LocalDate fechaa, String sig, Set<String> cats, String imagen) throws EventoRepetido, CategoriaEventoVacia;

	public abstract Set<String> listarEventos();
	
	public abstract Set<String> listarEventosSinFinalizar();
		
	public abstract Set<String> listarEdiciones(String evento);
	
	public Set<String> listarEdicionesIngresadas(String evento);
	public Set<String> listarEdicionesAceptadas(String evento);
	
	public abstract void crearTipoRegistro(String evento, String edicion, String nombre, String desc, float costo, int cupo)
			throws TipoRegistroRepetidoExcepcion;
	
	public abstract Set<String> listarTipoRegistro(String evento, String edicion);
	
	public abstract DTTipoRegistro getDTTipoReg(String evento, String edicion, String tipoReg);
	
	public abstract Set<String> listarCategorias(String evento);
		
	public abstract Set<String> getCategorias();
	
	public abstract DTEvento getDTEvento(String evento);
	
	public abstract void finalizarEvento(String nom);
				
	public abstract void altaEdicionEvento(String evento, String nickOrg, String nombre, 
    		String sigla, String pais, String ciudad, LocalDate fechaIni, LocalDate fechaFin, 
    		LocalDate fechaAlta, String estado, String imagen, String urlVideo) throws EdicionRepetidaException;
	
	public abstract void aceptarEdicionEvento(String evento, String edicion);
	public abstract void rechazarEdicionEvento(String evento, String edicion);
	
	// Agregue la fecha como parametro
	public abstract void altaRegistroCodigo(String evento, String edicion, String tipoRegistro, String nicknameAsistente, LocalDate fechaRegistro, String CodPatrocinio)
	            throws CupoLlenoExcepcion, YaRegistradoExcepcion;
	
	public abstract void altaRegistro(String evento, String edicion, String tipoRegistro, String nicknameAsistente, LocalDate fechaRegistro)
            throws CupoLlenoExcepcion, YaRegistradoExcepcion;

	public abstract void altaCategoria(String cat) throws CategoriaVaciaExcepcion, CategoriaRepetidaExcepcion;

	public abstract DTEdicion getDTEdicion(String evento, String edicion);
	
	public abstract DTRegistro getRegistro(String evento, String edicion, String nicknameAsistente);

	public abstract void sumarVisita(String nombreEvento);
	
	public abstract List<DTEvento> obtenerTopEventosVisitados();

	public abstract void marcarAsistencia(String evento, String edicion, String nicknameAsistente);
	
	public byte[] getImagenEvento(String evento);
	
	public byte[] getImagenEdicion(String evento, String edicion);
	
	public void setImagenEvento(String evento, byte[] imagen);
	
	public void setImagenEdicion(String evento, String edicion, byte[] imagen);
}
