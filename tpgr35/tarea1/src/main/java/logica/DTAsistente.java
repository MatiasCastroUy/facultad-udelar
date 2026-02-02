package logica;

import java.time.LocalDate;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DTAsistente extends DTUsuario{
	private String apellido;
	private LocalDate fechaNacimiento;


	public DTAsistente() {
		this.setApellido(new String());
		this.setFechaNacimiento(LocalDate.now());
	}
	
	public DTAsistente(String nickname, String nombre, String correo, String apellido, LocalDate fechaNacimiento) {
		super(nickname, nombre , correo);
		this.setApellido(apellido);
		this.setFechaNacimiento(fechaNacimiento);
	}
	
	// Constructor nuevo (con imagen)
	public DTAsistente(String nickname, String nombre, String correo, String imagenPerfil, String apellido, LocalDate fechaNacimiento) {
		super(nickname, nombre , correo, imagenPerfil);
		this.setApellido(apellido);
		this.setFechaNacimiento(fechaNacimiento);
	}
	
	public String getApellido() {
		return apellido;
	}
	
	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}
	
	private void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	private void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
}
