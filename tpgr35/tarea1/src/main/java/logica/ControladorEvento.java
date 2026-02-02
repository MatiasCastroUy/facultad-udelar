package logica;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import enumerados.EstadoEdicion;
import excepciones.CategoriaEventoVacia;
import excepciones.CategoriaRepetidaExcepcion;
import excepciones.EdicionRepetidaException;
import excepciones.CupoLlenoExcepcion;
import excepciones.CategoriaVaciaExcepcion;
import excepciones.EventoRepetido;
import excepciones.TipoRegistroRepetidoExcepcion;
import excepciones.YaRegistradoExcepcion;
import presentacion.Principal;

public class ControladorEvento implements IControladorEvento{
	
	private ManejadorEvento manEvento;
	
	public ControladorEvento() {
		manEvento = ManejadorEvento.getInstancia();
	}
	
	public Set<String> listarEventos() {
		return manEvento.listarEventos();
	}

	public Set<String> listarEventosSinFinalizar() {
		return manEvento.listarEventosSinFinalizar();
	}
	
	public Set<String> listarEdiciones(String evento) {
		 manEvento = ManejadorEvento.getInstancia();
		Evento event = manEvento.getEvento(evento);
		return event.listarEdiciones();
	}
	
	public Set<String> listarEdicionesIngresadas(String evento) {
		 manEvento = ManejadorEvento.getInstancia();
		Set<Edicion> ediciones = manEvento.getEvento(evento).getEdiciones();
		Set<String> nomEdicionesIngresadas = new HashSet<String>();
		for (Edicion e : ediciones) {
			if (e.getEstado()==EstadoEdicion.Ingresada)
				nomEdicionesIngresadas.add(e.getNombre());
		}
		return nomEdicionesIngresadas;
	}
	
	public Set<String> listarEdicionesAceptadas(String evento) {
		 manEvento = ManejadorEvento.getInstancia();
		Set<Edicion> ediciones = manEvento.getEvento(evento).getEdiciones();
		Set<String> nomEdicionesAceptadas = new HashSet<String>();
		for (Edicion e : ediciones) {
			if (e.getEstado()==EstadoEdicion.Aceptada)
				nomEdicionesAceptadas.add(e.getNombre());
		}
		return nomEdicionesAceptadas;
	}
	
	public Set<String> listarCategorias(String evento) {
		Evento event = manEvento.getEvento(evento);
		return event.getCategorias();
	}
	
	public void crearTipoRegistro(String evento, String edicion, String nombre, String desc, float costo, int cupo)
			throws TipoRegistroRepetidoExcepcion
	{
		Evento event = manEvento.getEvento(evento);
		Edicion edition = event.getEdicion(edicion);
		
		boolean repetido = false;
		for (String s : edition.listarTipoRegistro()) {
			repetido = s.equalsIgnoreCase(nombre);
			if (repetido) break;
		}
		
		if (repetido)
			throw new TipoRegistroRepetidoExcepcion("La edición " + edicion + " del evento " + evento + " ya tiene un tipo de registro " + nombre);
		TipoRegistro tipoReg = new TipoRegistro(nombre, desc, costo, cupo);
		edition.agregarTipoRegistro(nombre, tipoReg);

	}
		
	public Set<String> listarTipoRegistro(String evento, String edicion) {
		Evento event = manEvento.getEvento(evento);
		return event.getEdicion(edicion).listarTipoRegistro();
	}
	
	public DTTipoRegistro getDTTipoReg(String evento, String edicion, String tipoReg) {
		Evento event = manEvento.getEvento(evento);
		Edicion edition = event.getEdicion(edicion);
		return edition.getDTTipoReg(tipoReg);
	}
	
	public DTEvento getDTEvento(String evento)  {
		Evento event = manEvento.getEvento(evento);
		return event.getDTEvent();
	}
	
	public void altaEvento(String nom, String desc, LocalDate fechaa, String sig, Set<String> cats, String imagen) throws EventoRepetido, CategoriaEventoVacia {
        Evento event = manEvento.obtenerEvento(nom);
        for (String s : manEvento.listarEventos()) {
            if (s.equalsIgnoreCase(nom)) {
                throw new EventoRepetido("El evento '" + nom + "' ya está registrado");
            }
        }
        if (cats.isEmpty()) {
        	throw new CategoriaEventoVacia("Debe seleccionar por lo menos una categoria");
        }
        event = new Evento(nom, desc, fechaa, sig, cats, imagen);
        manEvento.addEvento(event); 
	}
	
	public void finalizarEvento(String nom) {
		Evento event = manEvento.obtenerEvento(nom);
		event.finalizar();
	}
	
	public Set<String> getCategorias(){
		Set<String> ret = new HashSet<>();
		Set<Categoria> cates = manEvento.getCategorias();
		
		for (Categoria c : cates)
			ret.add(c.getNombre());
		
		return ret;     
	}
    
	public void altaCategoria(String nombreCat) 
			throws CategoriaVaciaExcepcion, CategoriaRepetidaExcepcion {
		if (nombreCat.isBlank())
			throw new CategoriaVaciaExcepcion("Categoría vacía");
		
		Set<Categoria> cates = manEvento.getCategorias();

		boolean repetida = false;
		for (Categoria c : cates) {
			repetida = c.getNombre().equalsIgnoreCase(nombreCat);
			if (repetida) break;
		}
   
		if (repetida)
			throw new CategoriaRepetidaExcepcion("Ya existe la categoría " + nombreCat);

		Categoria nuevaCat = new Categoria(nombreCat);
		manEvento.agregarCategoria(nuevaCat);
		
	}
	
    public void altaEdicionEvento(String evento, String nickOrg, String nombre, 
    		String sigla, String pais, String ciudad, LocalDate fechaIni, LocalDate fechaFin, 
    		LocalDate fechaAlta, String estado, String imagen, String urlVideo) throws EdicionRepetidaException {
    	Evento evt = manEvento.getEvento(evento);
		if (evt == null)
			throw new EdicionRepetidaException("El evento " + evento + " no existe.");
    	
    	Edicion edition = manEvento.obtenerEdicion(evento, nombre);
    	if (edition != null || manEvento.getColeccionEdiciones().contains(nombre)) throw new EdicionRepetidaException("La edición " + nombre + " ya existe en el evento " + evento + ".");
    	
    	Usuario user = ManejadorUsuario.getInstancia().findUsuario(nickOrg);
    	Organizador organizador = (Organizador) user;
    	
    	EstadoEdicion nuevoEstado = EstadoEdicion.Ingresada; 
    		// cualquier otro string que no diga aceptada ni rechazada se toma como ingresada
    	if (estado.trim().equalsIgnoreCase("aceptada")) {
    		nuevoEstado = EstadoEdicion.Aceptada;
    	} else if (estado.trim().equalsIgnoreCase("rechazada")) {
    		nuevoEstado = EstadoEdicion.Rechazada;
    	}
    	
    	if (imagen == null) imagen = "";
    	
    	edition = new Edicion(
    		evt,
    		nombre,
    		sigla,
    		fechaIni,
    		fechaFin,
    		fechaAlta,
    		ciudad,
    		pais,
    		organizador,
    		nuevoEstado,
    		imagen,
    		urlVideo
    	);
    	evt.addEdicion(edition); 
    	manEvento.addEdicionCol(nombre);
    }

    @Override
    public void altaRegistroCodigo(String evento, String edicion, String nombreTipo, String nicknameAsistente,
    		LocalDate fechaRegistro, String CodPatrocinio)
            throws CupoLlenoExcepcion, YaRegistradoExcepcion {

        Evento event = manEvento.getEvento(evento);
        Edicion edition = event.getEdicion(edicion);
        TipoRegistro tipoReg = edition.getTipoRegistro(nombreTipo);

        Usuario user = ManejadorUsuario.getInstancia().findUsuario(nicknameAsistente);
        Asistente asistente = (Asistente) user;

        if (asistente.estaRegistradoEn(edition.getNombre())) {
            throw new YaRegistradoExcepcion("El asistente ya está registrado en esta edición.");
        }
        if (!tipoReg.hayCupoDisponible()) {
            throw new CupoLlenoExcepcion("No hay cupos disponibles para el tipo: " + nombreTipo);
        }

        Registro reg = new Registro(asistente, tipoReg, edition, fechaRegistro, CodPatrocinio);

        asistente.agregarRegistro(edition.getNombre(), reg);
        tipoReg.reducirCupo();
    }

    @Override
    public void altaRegistro(String evento, String edicion, String nombreTipo, String nicknameAsistente, LocalDate fechaRegistro)
            throws CupoLlenoExcepcion, YaRegistradoExcepcion {

        Evento event = manEvento.getEvento(evento);
        Edicion edition = event.getEdicion(edicion);
        TipoRegistro tipoReg = edition.getTipoRegistro(nombreTipo);

        Usuario user = ManejadorUsuario.getInstancia().findUsuario(nicknameAsistente);
        Asistente asistente = (Asistente) user;

        if (asistente.estaRegistradoEn(edition.getNombre())) {
            throw new YaRegistradoExcepcion("El asistente ya está registrado en esta edición.");
        }
        if (!tipoReg.hayCupoDisponible()) {
            throw new CupoLlenoExcepcion("No hay cupos disponibles para el tipo: " + nombreTipo);
        }

        Registro reg = new Registro(asistente, tipoReg, edition, fechaRegistro);

        asistente.agregarRegistro(edition.getNombre(), reg);
        tipoReg.reducirCupo();
    }

    public DTEdicion getDTEdicion(String evento, String edicion) {
		Edicion edition = manEvento.obtenerEdicion(evento, edicion);
		return edition.getDTEdicion();
    }

    // debe existir el evento y la edicion (se restringe desde presentacion)
	public void aceptarEdicionEvento(String evento, String edicion) {
		Edicion edition = manEvento.obtenerEdicion(evento, edicion);
		edition.aceptar();
	}

    // debe existir el evento y la edicion (se restringe desde presentacion)
	public void rechazarEdicionEvento(String evento, String edicion) {
		Edicion edition = manEvento.obtenerEdicion(evento, edicion);
		edition.rechazar();
	}
    
	public DTRegistro getRegistro(String evento, String edicion, String nicknameAsistente) {
		DTRegistro dtRegistro = null;
		
		Asistente asistente = (Asistente) ManejadorUsuario.getInstancia().findUsuario(nicknameAsistente);
		Registro registro = asistente.obtenerRegistroPorEdicion(edicion);
		
		if (registro != null) {
			dtRegistro = new DTRegistro(
				evento,
				edicion,
				registro.getFechaRegistro(),
				registro.getTipo().getNombre(),
				registro.getCosto(),
				registro.isAsistio()
			);
		} else {
			dtRegistro = new DTRegistro("__ERROR__", "", LocalDate.now(), "", -1, false); // EVITAR DEVOLVER NULL
		}
		
		return dtRegistro;
	}


	@Override
	public void marcarAsistencia(String evento, String edicion, String nicknameAsistente) {
	    Edicion ed = manEvento.obtenerEdicion(evento, edicion);
	    if (ed == null) return; 

	    LocalDate hoy = LocalDate.now();
	    LocalDate ini = ed.getFechaIni();

	    if (ini != null && hoy.isBefore(ini)) {
	        return;
	    }

	    Asistente asistente = (Asistente) ManejadorUsuario.getInstancia().findUsuario(nicknameAsistente);
	    if (asistente == null) return;

	    Registro registro = asistente.obtenerRegistroPorEdicion(edicion);
	    if (registro == null) return;

	    registro.confirmarAsistencia();
	}

	
	public void sumarVisita(String nombreEvento) {
	    Evento ev = manEvento.getEvento(nombreEvento);	    
	        ev.incrementarVisitas();	    
	}
	
	public List<DTEvento> obtenerTopEventosVisitados() {
        List<Evento> eventos = new ArrayList<>(manEvento.getColEventos().values());

        eventos.sort((a, b) -> Integer.compare(b.getVisitas(), a.getVisitas()));

        List<DTEvento> top = new ArrayList<>();
        for (int i = 0; i < Math.min(5, eventos.size()); i++) {
            top.add(eventos.get(i).getDTEvent());
        }
        return top;
    }
	
	public byte[] getImagenEvento(String evento) {
		 Properties propiedades = Principal.getPropiedades();
		 String path_base = propiedades.getProperty("path_imagenes");
		 
		 if (!path_base.endsWith("/")) {
			 path_base += "/";
		 }
		 
		 String nombre_img = this.getDTEvento(evento).getImagen();
		 
		 if (nombre_img == null) {
			 return new byte[0];
		 }
		 try {
			 byte[] img = Files.readAllBytes(Paths.get(path_base + nombre_img));
			 System.out.println("Imagen " + nombre_img + " encontrada. Devolviendo " + img.length + " bytes");
			 return img;
		 } catch (IOException io_exc) {
			 System.out.println("Error al buscar imagen " + nombre_img);
			 return new byte[0];
		 }

	 }
	
	public byte[] getImagenEdicion(String evento, String edicion) {
		Properties propiedades = Principal.getPropiedades();
		 String path_base = propiedades.getProperty("path_imagenes");
		 
		 if (!path_base.endsWith("/")) {
			 path_base += "/";
		 }
		 
		 String nombre_img = this.getDTEdicion(evento, edicion).getImagen();
		 
		 if (nombre_img == null) {
			 return new byte[0];
		 }
		 try {
			 byte[] img = Files.readAllBytes(Paths.get(path_base + nombre_img));
			 System.out.println("Imagen " + nombre_img + " encontrada. Devolviendo " + img.length + " bytes");
			 return img;
		 } catch (IOException io_exc) {
			 System.out.println("Error al buscar imagen " + nombre_img);
			 return new byte[0];
		 }
	}
	
	public void setImagenEvento(String evento, byte[] imagen) {
		 Properties propiedades = Principal.getPropiedades();
		 String path_base = propiedades.getProperty("path_imagenes");
		 
		 if (!path_base.endsWith("/")) {
			 path_base += "/";
		 }
		 
		 String nombre_img = "IMG_" + evento + ".png";
		 ManejadorEvento manEvento = ManejadorEvento.getInstancia();
		 
		 if (imagen==null) {
			 manEvento.getEvento(evento).setImagen(null);
			 return;
		 }
		 
		 try {
			Files.write(Paths.get(path_base + nombre_img), imagen);
			manEvento.getEvento(evento).setImagen(nombre_img);
		} catch (IOException e) {
			System.out.println("Error al escribir imagen " + nombre_img);
			e.printStackTrace();
		}
	 }
	
	public void setImagenEdicion(String evento, String edicion, byte[] imagen) {
		 Properties propiedades = Principal.getPropiedades();
		 String path_base = propiedades.getProperty("path_imagenes");
		 
		 if (!path_base.endsWith("/")) {
			 path_base += "/";
		 }
		 
		 String nombre_img = "IMG_" + edicion + ".png";
		 ManejadorEvento manEvento = ManejadorEvento.getInstancia();
		 
		 if (imagen==null || imagen.length == 0) {
			 manEvento.getEvento(evento).getEdicion(edicion).setImagen(null);
			 return;
		 }
		 
		 try {
			Files.write(Paths.get(path_base + nombre_img), imagen);
			manEvento.getEvento(evento).getEdicion(edicion).setImagen(nombre_img);
		} catch (IOException e) {
			System.out.println("Error al escribir imagen " + nombre_img);
			e.printStackTrace();
		}
	 }

}
