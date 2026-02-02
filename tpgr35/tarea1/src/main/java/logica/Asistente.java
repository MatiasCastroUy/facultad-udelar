package logica;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.time.LocalDate;

public class Asistente extends Usuario{
		private String apellido;
		private LocalDate fechaNacimiento;
		
		private Map<String, Registro> registrosPorEdicion = new HashMap<>();
		
	public Asistente(String nick, String nombr, String corr, String apell, LocalDate fechaN) {
		super(nick, nombr, corr);
		this.apellido = apell;
		this.fechaNacimiento = fechaN;
	}
	
	// Constructor nuevo (con contraseña e imagen)
	public Asistente(String nick, String nombr, String corr, String contras, String imagen, String apell, LocalDate fechaN) {
		super(nick, nombr, corr, contras, imagen);
		this.apellido = apell;
		this.fechaNacimiento = fechaN;
	}		
	
		public String getApellido() {
			return apellido;
		}
		
		public LocalDate getFechaNacimiento() {
			return fechaNacimiento;
		}
		
		public void setApellido(String apell) {
			this.apellido = apell;
		}
		
		public void setFechaNacimiento(LocalDate fechaN) {
			this.fechaNacimiento = fechaN;
		}
		
		public void agregarRegistro(String nombreEdicion, Registro reg) {
	        registrosPorEdicion.put(nombreEdicion, reg);
	    }

	    public boolean estaRegistradoEn(String nombreEdicion) {
	        return registrosPorEdicion.containsKey(nombreEdicion);
	    }

	    public Collection<Registro> listarRegistros() {
	        return registrosPorEdicion.values();
	    }
	    public Registro obtenerRegistroPorEdicion(String nombreEdicion) {
	        return registrosPorEdicion.get(nombreEdicion);
	    }

		
}
