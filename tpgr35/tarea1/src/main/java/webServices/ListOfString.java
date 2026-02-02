package webServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ListOfString {
	
	@XmlElement(nillable = true)
    private List<String> item = new ArrayList<>();
    public List<String> getItem() { return item; }
    public void setItem(List<String> newItem) { this.item = newItem; }
	
	public ListOfString() {
		
	}
	
	public ListOfString(List<String> items) {
		this.item.addAll(items);
	}
	
	public ListOfString(Set<String> items) {
		this.item.addAll(items);
	}
    
}
