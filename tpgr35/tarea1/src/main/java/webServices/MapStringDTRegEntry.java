package webServices;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import logica.DTRegistro;

@XmlAccessorType(XmlAccessType.FIELD)
public class MapStringDTRegEntry {
	private String nomAsist;
	private DTRegistro registro;
	
	public MapStringDTRegEntry() {
		
	}
	
	public MapStringDTRegEntry(String asist, DTRegistro reg) {
		this.setNomAsist(asist);
		this.setRegistro(reg);
	}
	
	public void setNomAsist(String nom) {
		this.nomAsist = nom;
	}
	
	public void setRegistro(DTRegistro reg) {
		this.registro = reg;
	}
	
	public String getNomAsist() {
		return this.nomAsist;
	}
	
	public DTRegistro getRegistro() {
		return this.registro;
	}
}
