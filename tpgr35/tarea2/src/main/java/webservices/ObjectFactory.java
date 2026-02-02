
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

    private final static QName _ContrasenaInvalidaException_QNAME = new QName("http://webServices/", "ContrasenaInvalidaException");
    private final static QName _UsuarioCorreoRepetidoException_QNAME = new QName("http://webServices/", "UsuarioCorreoRepetidoException");
    private final static QName _UsuarioNoExisteException_QNAME = new QName("http://webServices/", "UsuarioNoExisteException");
    private final static QName _UsuarioRepetidoException_QNAME = new QName("http://webServices/", "UsuarioRepetidoException");
    private final static QName _ListOfString_QNAME = new QName("http://webServices/", "listOfString");
    private final static QName _MapStringDTReg_QNAME = new QName("http://webServices/", "mapStringDTReg");
    private final static QName _SetOfDTAsistente_QNAME = new QName("http://webServices/", "setOfDTAsistente");
    private final static QName _SetOfDTOrganizador_QNAME = new QName("http://webServices/", "setOfDTOrganizador");
    private final static QName _SetOfDTUsuario_QNAME = new QName("http://webServices/", "setOfDTUsuario");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: webservices
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ContrasenaInvalidaException }
     * 
     * @return
     *     the new instance of {@link ContrasenaInvalidaException }
     */
    public ContrasenaInvalidaException createContrasenaInvalidaException() {
        return new ContrasenaInvalidaException();
    }

    /**
     * Create an instance of {@link UsuarioCorreoRepetidoException }
     * 
     * @return
     *     the new instance of {@link UsuarioCorreoRepetidoException }
     */
    public UsuarioCorreoRepetidoException createUsuarioCorreoRepetidoException() {
        return new UsuarioCorreoRepetidoException();
    }

    /**
     * Create an instance of {@link UsuarioNoExisteException }
     * 
     * @return
     *     the new instance of {@link UsuarioNoExisteException }
     */
    public UsuarioNoExisteException createUsuarioNoExisteException() {
        return new UsuarioNoExisteException();
    }

    /**
     * Create an instance of {@link UsuarioRepetidoException }
     * 
     * @return
     *     the new instance of {@link UsuarioRepetidoException }
     */
    public UsuarioRepetidoException createUsuarioRepetidoException() {
        return new UsuarioRepetidoException();
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
     * Create an instance of {@link MapStringDTReg }
     * 
     * @return
     *     the new instance of {@link MapStringDTReg }
     */
    public MapStringDTReg createMapStringDTReg() {
        return new MapStringDTReg();
    }

    /**
     * Create an instance of {@link SetOfDTAsistente }
     * 
     * @return
     *     the new instance of {@link SetOfDTAsistente }
     */
    public SetOfDTAsistente createSetOfDTAsistente() {
        return new SetOfDTAsistente();
    }

    /**
     * Create an instance of {@link SetOfDTOrganizador }
     * 
     * @return
     *     the new instance of {@link SetOfDTOrganizador }
     */
    public SetOfDTOrganizador createSetOfDTOrganizador() {
        return new SetOfDTOrganizador();
    }

    /**
     * Create an instance of {@link SetOfDTUsuario }
     * 
     * @return
     *     the new instance of {@link SetOfDTUsuario }
     */
    public SetOfDTUsuario createSetOfDTUsuario() {
        return new SetOfDTUsuario();
    }

    /**
     * Create an instance of {@link DtUsuario }
     * 
     * @return
     *     the new instance of {@link DtUsuario }
     */
    public DtUsuario createDtUsuario() {
        return new DtUsuario();
    }

    /**
     * Create an instance of {@link DtOrganizador }
     * 
     * @return
     *     the new instance of {@link DtOrganizador }
     */
    public DtOrganizador createDtOrganizador() {
        return new DtOrganizador();
    }

    /**
     * Create an instance of {@link DtAsistente }
     * 
     * @return
     *     the new instance of {@link DtAsistente }
     */
    public DtAsistente createDtAsistente() {
        return new DtAsistente();
    }

    /**
     * Create an instance of {@link MapStringDTRegEntry }
     * 
     * @return
     *     the new instance of {@link MapStringDTRegEntry }
     */
    public MapStringDTRegEntry createMapStringDTRegEntry() {
        return new MapStringDTRegEntry();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ContrasenaInvalidaException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ContrasenaInvalidaException }{@code >}
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "ContrasenaInvalidaException")
    public JAXBElement<ContrasenaInvalidaException> createContrasenaInvalidaException(ContrasenaInvalidaException value) {
        return new JAXBElement<>(_ContrasenaInvalidaException_QNAME, ContrasenaInvalidaException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UsuarioCorreoRepetidoException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UsuarioCorreoRepetidoException }{@code >}
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "UsuarioCorreoRepetidoException")
    public JAXBElement<UsuarioCorreoRepetidoException> createUsuarioCorreoRepetidoException(UsuarioCorreoRepetidoException value) {
        return new JAXBElement<>(_UsuarioCorreoRepetidoException_QNAME, UsuarioCorreoRepetidoException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UsuarioNoExisteException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UsuarioNoExisteException }{@code >}
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "UsuarioNoExisteException")
    public JAXBElement<UsuarioNoExisteException> createUsuarioNoExisteException(UsuarioNoExisteException value) {
        return new JAXBElement<>(_UsuarioNoExisteException_QNAME, UsuarioNoExisteException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UsuarioRepetidoException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UsuarioRepetidoException }{@code >}
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "UsuarioRepetidoException")
    public JAXBElement<UsuarioRepetidoException> createUsuarioRepetidoException(UsuarioRepetidoException value) {
        return new JAXBElement<>(_UsuarioRepetidoException_QNAME, UsuarioRepetidoException.class, null, value);
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

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MapStringDTReg }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MapStringDTReg }{@code >}
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "mapStringDTReg")
    public JAXBElement<MapStringDTReg> createMapStringDTReg(MapStringDTReg value) {
        return new JAXBElement<>(_MapStringDTReg_QNAME, MapStringDTReg.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetOfDTAsistente }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SetOfDTAsistente }{@code >}
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "setOfDTAsistente")
    public JAXBElement<SetOfDTAsistente> createSetOfDTAsistente(SetOfDTAsistente value) {
        return new JAXBElement<>(_SetOfDTAsistente_QNAME, SetOfDTAsistente.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetOfDTOrganizador }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SetOfDTOrganizador }{@code >}
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "setOfDTOrganizador")
    public JAXBElement<SetOfDTOrganizador> createSetOfDTOrganizador(SetOfDTOrganizador value) {
        return new JAXBElement<>(_SetOfDTOrganizador_QNAME, SetOfDTOrganizador.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetOfDTUsuario }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SetOfDTUsuario }{@code >}
     */
    @XmlElementDecl(namespace = "http://webServices/", name = "setOfDTUsuario")
    public JAXBElement<SetOfDTUsuario> createSetOfDTUsuario(SetOfDTUsuario value) {
        return new JAXBElement<>(_SetOfDTUsuario_QNAME, SetOfDTUsuario.class, null, value);
    }

}
