package logica;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


// SINGLETON
public class ManejadorEvento {
	private static ManejadorEvento instancia;
	
	private Map<String, Evento> colEventos; // colección de eventos
	private Set<String> colEdicionesGlobal;
	private Set<Categoria> categorias = new HashSet<>();

	
	
	public static ManejadorEvento getInstancia() {
		if (instancia == null) {
			instancia = new ManejadorEvento();
		}
		return instancia;
	}
	
	private ManejadorEvento() {
		colEventos = new TreeMap<String, Evento>();
		colEdicionesGlobal = new HashSet<String>();
	}

	public Set<String> listarEventos() {
		return colEventos.keySet();
	}
	
	public Set<String> listarEventosSinFinalizar() {
		Set<String> eventos =  colEventos.keySet();
		Set<String> eventosSinFinalizar = new HashSet<String>();
		
		for (String evento : eventos) {
			Evento evt = colEventos.get(evento);
			if (!evt.getFinalizado()) {
				eventosSinFinalizar.add(evento);
			}
		}
		
		return eventosSinFinalizar;		
	}
	
	// precond: existe un Evento de nombre evento en la colección
	public Evento getEvento(String evento) {
		return colEventos.get(evento);

	}
	
	public void addEvento(Evento event) {
        String nom = event.getNombre();
        colEventos.put(nom, event);
    }
	
	public Evento obtenerEvento(String nom) {
		return (Evento) colEventos.get(nom);		
	}
	
	public Set<Categoria> getCategorias() {
		return this.categorias;
	}
	
   public void agregarCategoria(Categoria cat) {
	   this.categorias.add(cat);   	
   }
   
   
   
   public Edicion obtenerEdicion(String evento, String edicion) {
	   Evento evt = (Evento) colEventos.get(evento);
	   if (evt == null) return null;
	   
	   Edicion edition = (Edicion) evt.getEdicion(edicion);
	   return edition;
   }
   
   public void addEdicionCol(String edicion) {
	   colEdicionesGlobal.add(edicion);
   }
   
   public Set<String> getColeccionEdiciones() {
	   return colEdicionesGlobal;
   }
	
   
	public void reiniciarSingleton() {
		colEventos = null;
		categorias = null;
		instancia = null;
	}
	
	public Map<String, Evento> getColEventos() {
        return colEventos;
    }
	
}
