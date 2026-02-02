package webServices;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import logica.DTUsuario;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SetOfDTUsuario {
	
	@XmlElement(nillable = true)
    private Set<DTUsuario> item = new HashSet<>();
    public Set<DTUsuario> getItem() { return item; }
    public void setItem(Set<DTUsuario> newItem) { this.item = newItem; }
	
	public SetOfDTUsuario() {
		
	}
	
	public SetOfDTUsuario(Set<DTUsuario> items) {
		this.item.addAll(items);
	}
	
	public SetOfDTUsuario(List<DTUsuario> items) {
		this.item.addAll(items);
	}
    
}