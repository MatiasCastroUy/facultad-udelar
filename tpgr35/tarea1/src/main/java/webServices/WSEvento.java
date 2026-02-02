package webServices;

import java.util.HashSet;
import java.util.Set;
import java.time.LocalDate;

import excepciones.CupoLlenoExcepcion;
import excepciones.EdicionRepetidaException;
import excepciones.TipoRegistroRepetidoExcepcion;
import excepciones.YaRegistradoExcepcion;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.ParameterStyle;
import jakarta.jws.soap.SOAPBinding.Style;
import jakarta.xml.ws.Endpoint;
import logica.DTEdicion;
import logica.DTEvento;
import logica.DTRegistro;
import logica.DTTipoRegistro;
import logica.Fabrica;
import logica.IControladorEvento;

@WebService
@SOAPBinding(style = Style.RPC, parameterStyle = ParameterStyle.WRAPPED)
public class WSEvento {
    private String url = null;

    private Endpoint endpoint = null;

    public WSEvento() {}
    
    public WSEvento(String url) {
        this.url = url;
    }

    @WebMethod(exclude = true)
    public void publish() {
        endpoint = Endpoint.publish(url, this);
    }

    @WebMethod(exclude = true)
    public Endpoint getEndpoint() {
        return endpoint;
    }

    @WebMethod
    public void crearTipoRegistro(String evento, String edicion, String nombre, String desc, float costo, int cupo)
            throws TipoRegistroRepetidoExcepcion {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        ctrEv.crearTipoRegistro(evento, edicion, nombre, desc, costo, cupo);
    }

    @WebMethod
    public ListOfString listarTipoRegistro(String evento, String edicion) {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        return new ListOfString(ctrEv.listarTipoRegistro(evento, edicion));
    }

    @WebMethod
    public DTTipoRegistro getDTTipoReg(String evento, String edicion, String tipoReg) {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        return ctrEv.getDTTipoReg(evento, edicion, tipoReg);
    }

    @WebMethod
    public ListOfString listarEventos() {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        return new ListOfString(ctrEv.listarEventos());
    }
    
    @WebMethod
    public ListOfString listarEventosSinFinalizar() {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        return new ListOfString(ctrEv.listarEventosSinFinalizar());
    }

    @WebMethod
    public ListOfString listarEdiciones(String evento) {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        return new ListOfString(ctrEv.listarEdiciones(evento));
    }
    
    @WebMethod
    public void finalizarEvento(String nom) {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        ctrEv.finalizarEvento(nom);
    }

    @WebMethod
    public ListOfString listarEdicionesAceptadas(String evento) {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        return new ListOfString(ctrEv.listarEdicionesAceptadas(evento));
    }

    @WebMethod
    public DTEdicion getDTEdicion(String evento, String edicion) {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        return ctrEv.getDTEdicion(evento, edicion);
    }

    @WebMethod
    public DTRegistro getRegistro(String evento, String edicion, String nicknameAsistente) {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        return ctrEv.getRegistro(evento, edicion, nicknameAsistente);
    }

    @WebMethod
    public void marcarAsistencia(String evento, String edicion, String nicknameAsistente) {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        ctrEv.marcarAsistencia(evento, edicion, nicknameAsistente);
    }
    
    @WebMethod
    public void altaRegistro(String evento,
                             String edicion,
                             String nombreTipoRegistro,
                             String nicknameAsistente,
                             String fechaISO)
            throws YaRegistradoExcepcion, CupoLlenoExcepcion {

        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();

        LocalDate fechaRegistro;
        if (fechaISO != null && !fechaISO.isBlank()) {
            // espera formato ISO: "yyyy-MM-dd"
            fechaRegistro = LocalDate.parse(fechaISO);
        } else {
            fechaRegistro = LocalDate.now();
        }

        ctrEv.altaRegistro(evento, edicion, nombreTipoRegistro, nicknameAsistente, fechaRegistro);
    }
    
    @WebMethod
    public void altaEvento(String nombre, String descripcion, String sigla, ListOfString categorias, String imagen) throws excepciones.EventoRepetido, excepciones.CategoriaEventoVacia {                           
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        LocalDate fechaAlta = LocalDate.now();
        Set<String> setCategorias = new HashSet<>(categorias.getItem());
        ctrEv.altaEvento(nombre, descripcion, fechaAlta, sigla, setCategorias, imagen);
    }

    @WebMethod
    public ListOfString getCategorias() {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        return new ListOfString(ctrEv.getCategorias());
    }

    @WebMethod
    public DTEvento getDTEvento(String nombre) {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        return ctrEv.getDTEvento(nombre);
    }
    
    @WebMethod
    public ListOfString listarCategorias(String evento) {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        return new ListOfString(ctrEv.listarCategorias(evento));
    }
     
    @WebMethod
    public void altaEdicionEvento(String evento, String nickOrg, String nombre, String sigla, String pais, String ciudad, 
    							  String fechaIni, String fechaFin, String fechaAlta, String estado, 
    							  String imagen, String urlVideo) throws EdicionRepetidaException {
    	
    	LocalDate fechaInicioLD;
    	LocalDate fechaFinLD;
    	LocalDate fechaAltaLD;
    	
        if (fechaIni != null && !fechaIni.isBlank()) {
            // espera formato ISO: "yyyy-MM-dd"
        	fechaInicioLD = LocalDate.parse(fechaIni);
        } else {
        	fechaInicioLD = LocalDate.now();
        }
        
        if (fechaFin != null && !fechaFin.isBlank()) {
        	fechaFinLD = LocalDate.parse(fechaFin);
        } else {
        	fechaFinLD = LocalDate.now();
        }
        
        if (fechaAlta != null && !fechaAlta.isBlank()) {
        	fechaAltaLD = LocalDate.parse(fechaAlta);
        } else {
        	fechaAltaLD = LocalDate.now();
        }
        
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        ctrEv.altaEdicionEvento(evento, nickOrg, nombre, sigla, pais, ciudad,
        		fechaInicioLD, fechaFinLD, fechaAltaLD, estado, imagen, urlVideo);
    }
        
    @WebMethod
    public void sumarVisita(String nombreEvento) {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        ctrEv.sumarVisita(nombreEvento);
    }
    
    @WebMethod
    public byte[] getImagenEvento(String nombreEvento) {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        return ctrEv.getImagenEvento(nombreEvento);
    }
    
    @WebMethod
    public byte[] getImagenEdicion(String evento, String edicion) {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        return ctrEv.getImagenEdicion(evento, edicion);
    }
    
    @WebMethod
    public void setImagenEvento(String evento, byte[] imagen) {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        ctrEv.setImagenEvento(evento, imagen);
    }
    
    @WebMethod
    public void setImagenEdicion(String evento, String edicion, byte[] imagen) {
        IControladorEvento ctrEv = Fabrica.getInstance().getIControladorEvento();
        ctrEv.setImagenEdicion(evento, edicion, imagen);
    }
}
