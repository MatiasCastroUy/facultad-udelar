
package webservices;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the webservices package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CategoriaEventoVacia_QNAME = new QName("http://webServices/", "CategoriaEventoVacia");
    private final static QName _CupoLlenoExcepcion_QNAME = new QName("http://webServices/", "CupoLlenoExcepcion");
    private final static QName _EdicionRepetidaException_QNAME = new QName("http://webServices/", "EdicionRepetidaException");
    private final static QName _EventoRepetido_QNAME = new QName("http://webServices/", "EventoRepetido");
    private final static QName _TipoRegistroRepetidoExcepcion_QNAME = new QName("http://webServices/", "TipoRegistroRepetidoExcepcion");
    private final static QName _YaRegistradoExcepcion_QNAME = new QName("http://webServices/", "YaRegistradoExcepcion");
    private final static QName _ListOfString_QNAME = new QName("http://webServices/", "listOfString");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: webservices
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CategoriaEventoVacia }
     * 
     * @return
     *     the new instance of {@link CategoriaEventoVacia }
     */
    public CategoriaEventoVacia createCategoriaEventoVacia() {
        return new CategoriaEventoVacia();
    }

    /**
     * Create an instance of {@link CupoLlenoExcepcion }
     * 
     * @return
     *     the new instance of {@link CupoLlenoExcepcion }
     */
    public CupoLlenoExcepcion createCupoLlenoExcepcion() {
        return new CupoLlenoExcepcion();
    }

    /**
     * Create an instance of {@link EdicionRepetidaException }
     * 
     * @return
     *     the new instance of {@link EdicionRepetidaException }
     */
    public EdicionRepetidaException createEdicionRepetidaException() {
        return new EdicionRepetidaException();
    }

    /**
     * Create an instance of {@link EventoRepetido }
     * 
     * @return
     *     the new instance of {@link EventoRepetido }
     */
    public EventoRepetido createEventoRepetido() {
        return new EventoRepetido();
    }

    /**
     * Create an instance of {@link TipoRegistroRepetidoExcepcion }
     * 
     * @return
     *     the new instance of {@link TipoRegistroRepetidoExcepcion }
     */
    public TipoRegistroRepetidoExcepcion createTipoRegistroRepetidoExcepcion() {
        return new TipoRegistroRepetidoExcepcion();
    }

    /**
     * Create an instance of {@link YaRegistradoExcepcion }
     * 
     * @return
     *     the new instance of {@link YaRegistradoExcepcion }
     */
    public YaRegistradoExcepcion createYaRegistradoExcepcion() {
        return new YaRegistradoExcepcion();
    }

    /**
     * Create an instance of {@link ListOfString }
     * 
     * @return
     *     the new instance of {@link ListOfString }
     */
    public ListOfString createListOfString() {
        return new ListOfString();
    }

    /**
     * Create an instance of {@link DtEvento }
     * 
     * @return
     *     the new instance of {@link DtEvento }
     */
    public DtEvento createDtEvento() {
        return new DtEvento();
    }

    /**
     * Create an instance of {@link DtEdicion }
     * 
     * @return
     *     the new instance of {@link DtEdicion }
     */
    public DtEdicion createDtEdicion() {
        return new DtEdicion();
    }

    /**
     * Create an instance of {@link DtRegistro }
     * 
     * @return
     *     the new instance of {@link DtRegistro }
     */
    public DtRegistro createDtRegistro() {
        return new DtRegistro();
    }

    /**
     * Create an instance of {@link DtTipoRegistro }
     * 
     * @return
     *     the new instance of {@link DtTipoRegistro }
     */
    public DtTipoRegistro createDtTipoRegistro() {
        return new DtTipoRegistro();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CategoriaEventoVacia }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CategoriaEventoVacia }{@code >}
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "CategoriaEventoVacia")
    public JAXBElement<CategoriaEventoVacia> createCategoriaEventoVacia(CategoriaEventoVacia value) {
        return new JAXBElement<>(_CategoriaEventoVacia_QNAME, CategoriaEventoVacia.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CupoLlenoExcepcion }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CupoLlenoExcepcion }{@code >}
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "CupoLlenoExcepcion")
    public JAXBElement<CupoLlenoExcepcion> createCupoLlenoExcepcion(CupoLlenoExcepcion value) {
        return new JAXBElement<>(_CupoLlenoExcepcion_QNAME, CupoLlenoExcepcion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EdicionRepetidaException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EdicionRepetidaException }{@code >}
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "EdicionRepetidaException")
    public JAXBElement<EdicionRepetidaException> createEdicionRepetidaException(EdicionRepetidaException value) {
        return new JAXBElement<>(_EdicionRepetidaException_QNAME, EdicionRepetidaException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EventoRepetido }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EventoRepetido }{@code >}
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "EventoRepetido")
    public JAXBElement<EventoRepetido> createEventoRepetido(EventoRepetido value) {
        return new JAXBElement<>(_EventoRepetido_QNAME, EventoRepetido.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoRegistroRepetidoExcepcion }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link TipoRegistroRepetidoExcepcion }{@code >}
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "TipoRegistroRepetidoExcepcion")
    public JAXBElement<TipoRegistroRepetidoExcepcion> createTipoRegistroRepetidoExcepcion(TipoRegistroRepetidoExcepcion value) {
        return new JAXBElement<>(_TipoRegistroRepetidoExcepcion_QNAME, TipoRegistroRepetidoExcepcion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link YaRegistradoExcepcion }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link YaRegistradoExcepcion }{@code >}
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "YaRegistradoExcepcion")
    public JAXBElement<YaRegistradoExcepcion> createYaRegistradoExcepcion(YaRegistradoExcepcion value) {
        return new JAXBElement<>(_YaRegistradoExcepcion_QNAME, YaRegistradoExcepcion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListOfString }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ListOfString }{@code >}
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "listOfString")
    public JAXBElement<ListOfString> createListOfString(ListOfString value) {
        return new JAXBElement<>(_ListOfString_QNAME, ListOfString.class, null, value);
    }

}
