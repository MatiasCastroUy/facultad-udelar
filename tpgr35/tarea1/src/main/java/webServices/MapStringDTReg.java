package webServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MapStringDTReg {
	
	@XmlElement(nillable = true)
    private List<MapStringDTRegEntry> item = new ArrayList<>();
    public List<MapStringDTRegEntry> getItem() { return item; }
    public void setItem(List<MapStringDTRegEntry> newItem) { this.item = newItem; }
	
	public MapStringDTReg() {
		
	}
	
	public MapStringDTReg(List<MapStringDTRegEntry> item) {
		this.item.addAll(item);
	}
    
}
