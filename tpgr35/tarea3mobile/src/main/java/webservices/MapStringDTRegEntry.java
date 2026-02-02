
package webservices;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para mapStringDTRegEntry complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>{@code
 * <complexType name="mapStringDTRegEntry">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="nomAsist" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="registro" type="{http://webServices/}dtRegistro" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mapStringDTRegEntry", propOrder = {
    "nomAsist",
    "registro"
})
public class MapStringDTRegEntry {

    protected String nomAsist;
    protected DtRegistro registro;

    /**
     * Obtiene el valor de la propiedad nomAsist.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomAsist() {
        return nomAsist;
    }

    /**
     * Define el valor de la propiedad nomAsist.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomAsist(String value) {
        this.nomAsist = value;
    }

    /**
     * Obtiene el valor de la propiedad registro.
     * 
     * @return
     *     possible object is
     *     {@link DtRegistro }
     *     
     */
    public DtRegistro getRegistro() {
        return registro;
    }

    /**
     * Define el valor de la propiedad registro.
     * 
     * @param value
     *     allowed object is
     *     {@link DtRegistro }
     *     
     */
    public void setRegistro(DtRegistro value) {
        this.registro = value;
    }

}
