package webServices;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import logica.DTAsistente;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SetOfDTAsistente {
	
	@XmlElement(nillable = true)
    private Set<DTAsistente> item = new HashSet<>();
    public Set<DTAsistente> getItem() { return item; }
    public void setItem(Set<DTAsistente> newItem) { this.item = newItem; }
	
	public SetOfDTAsistente() {
		
	}
	
	public SetOfDTAsistente(Set<DTAsistente> items) {
		this.item.addAll(items);
	}
	
	public SetOfDTAsistente(List<DTAsistente> items) {
		this.item.addAll(items);
	}
    
}