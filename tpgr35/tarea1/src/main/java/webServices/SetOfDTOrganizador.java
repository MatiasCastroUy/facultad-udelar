package webServices;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import logica.DTOrganizador;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SetOfDTOrganizador {
	
	@XmlElement(nillable = true)
    private Set<DTOrganizador> item = new HashSet<>();
    public Set<DTOrganizador> getItem() { return item; }
    public void setItem(Set<DTOrganizador> newItem) { this.item = newItem; }
	
	public SetOfDTOrganizador() {
		
	}
	
	public SetOfDTOrganizador(Set<DTOrganizador> items) {
		this.item.addAll(items);
	}
	
	public SetOfDTOrganizador(List<DTOrganizador> items) {
		this.item.addAll(items);
	}
    
}