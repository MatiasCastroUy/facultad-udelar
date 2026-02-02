package uy.eventos.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest; 
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import webservices.DtEvento;
import webservices.DtEdicion;
import webservices.DtOrganizador;
import webservices.DtRegistro;
import webservices.DtTipoRegistro;
import webservices.ListOfString;
import webservices.MapStringDTRegEntry;
import webservices.UsuarioNoExisteException_Exception;
import webservices.WSEvento;
import webservices.WSEventoService;
import webservices.WSUsuario;
import webservices.WSUsuarioService;

@WebServlet("/consultaEdicionEvento")
public class ConsultaEdicionEvento extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private DtEvento dtEvento;
    private DtEdicion dtEdicion;
    private DtOrganizador dtOrganizador;
    private DtRegistro dtRegistro;
    private Map<String, DtRegistro> dtRegistroMap;
    private Set<DtTipoRegistro> dtTipoRegistroLst;
    
    private String error = "";
    
    private Boolean mostrarRegistros = false;
    private Boolean mostrarDetallesRegistro = false;
    
    public ConsultaEdicionEvento() { super(); }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    
    
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	RequestDispatcher dispatcher;
    	request.setCharacterEncoding("UTF-8");
    	
    	error = "";
    	mostrarRegistros = mostrarDetallesRegistro = false;
    	
    	dtEvento = null;
    	dtEdicion = null;
    	dtOrganizador = null;
    	dtRegistro = null;
    	dtRegistroMap = new HashMap<>();
        dtTipoRegistroLst = new HashSet<>();
    	
        // Parámetros esperados: evento, edicion
        String evento = request.getParameter("evento") != null ? request.getParameter("evento") : "";
        String edicion = request.getParameter("edicion") != null ? request.getParameter("edicion") : "";

        request.setAttribute("evento", evento);
        request.setAttribute("edicion", edicion);
        
        
        // Validar request
        if (!validarRequest(evento, edicion)) {
        	request.setAttribute("error", error);
        	dispatcher = request.getRequestDispatcher("/WEB-INF/ConsultaEdicionEvento/ConsultaEdicionEvento.jsp");
        	dispatcher.forward(request, response);
        	return;
        }
        
        
        // Obtener evento
        dtEvento = obtenerEvento(evento);
        if (!error.isBlank()) {
        	request.setAttribute("error", error);
        	dispatcher = request.getRequestDispatcher("/WEB-INF/ConsultaEdicionEvento/ConsultaEdicionEvento.jsp");
        	dispatcher.forward(request, response);
        	return;
        }
        
        // Obtener edición
        dtEdicion = obtenerEdicion(evento, edicion);
        if (!error.isBlank()) {
        	request.setAttribute("error", error);
        	dispatcher = request.getRequestDispatcher("/WEB-INF/ConsultaEdicionEvento/ConsultaEdicionEvento.jsp");
        	dispatcher.forward(request, response);
        	return;
        }
        
        // Obtener organizador
    	dtOrganizador = obtenerOrganizador(dtEdicion);
        if (!error.isBlank()) {
        	request.setAttribute("error", error);
        	dispatcher = request.getRequestDispatcher("/WEB-INF/ConsultaEdicionEvento/ConsultaEdicionEvento.jsp");
        	dispatcher.forward(request, response);
        	return;
        }
        
        
        // Obtener rol de usuario
        HttpSession session = request.getSession(false);
        if (session != null) {
	    	String rolUsuario = (String) session.getAttribute("rol");
	    	String nicknameUsuario = (String) session.getAttribute("usuarioLogueado");
	    	
	    	mostrarRegistros = usuarioEsOrganizador(rolUsuario, nicknameUsuario, dtEdicion);
	    	mostrarDetallesRegistro = usuarioEsAsistenteRegistrado(rolUsuario, nicknameUsuario, evento, edicion);
    	}
        
        
        // Obtener tipos de registro
        dtTipoRegistroLst = obtenerTiposRegistro(evento, edicion);
        
        // obtener imagen edicion y evento
        Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
		URL url_ws_eventos = new URL(propiedades.getProperty("url_ws_eventos"));
        WSEventoService servicio = new WSEventoService(url_ws_eventos);
        WSEvento port = servicio.getWSEventoPort();
        byte[] img_ed_bytes = port.getImagenEdicion(evento, edicion);
        String imagenEdicion_b64 = img_ed_bytes.length > 0 ? Base64.getEncoder().encodeToString(img_ed_bytes) : null;
        
        byte[] img_evt_bytes = port.getImagenEvento(evento);
        String imagenEvento_b64 = img_evt_bytes.length > 0 ? Base64.getEncoder().encodeToString(img_evt_bytes) : null;
        
        // imagen organizador
		URL url_ws_usuarios = new URL(propiedades.getProperty("url_ws_usuarios"));
		WSUsuarioService servicioUsuario = new WSUsuarioService(url_ws_usuarios);
		WSUsuario portUsuario = servicioUsuario.getWSUsuarioPort();
		String img_org_b64;
        try {
			byte[] img_org_bytes = portUsuario.getImagenUsuario(dtOrganizador.getNickname());
			img_org_b64 = img_org_bytes.length > 0 ? Base64.getEncoder().encodeToString(img_org_bytes) : null;
		} catch (UsuarioNoExisteException_Exception e) {
			img_org_b64 = null;
			System.out.println("Organizador " + dtOrganizador.getNickname() + " no encontrado");
		}
        
        // Todo OK
        request.setAttribute("dtEvento", dtEvento);
        request.setAttribute("dtEdicion", dtEdicion);
        request.setAttribute("dtOrganizador", dtOrganizador);
        request.setAttribute("mostrarRegistros", mostrarRegistros);
        request.setAttribute("mostrarDetallesRegistro", mostrarDetallesRegistro);
        request.setAttribute("dtRegistro", dtRegistro);
        request.setAttribute("dtRegistroLst", dtRegistroMap);
        request.setAttribute("dtTipoRegistroLst", dtTipoRegistroLst);
        request.setAttribute("error", error);
        request.setAttribute("imagenEdicion_b64", imagenEdicion_b64);
        request.setAttribute("imagenEvento_b64", imagenEvento_b64);
        request.setAttribute("img_org_b64", img_org_b64);
        
    	dispatcher = request.getRequestDispatcher("/WEB-INF/ConsultaEdicionEvento/ConsultaEdicionEvento.jsp");
    	dispatcher.forward(request, response);
    }
    
    
    private Boolean validarRequest(String evento, String edicion) {
    	
    	if (evento == null || edicion == null || evento.isBlank() || edicion.isBlank()) {
    		error = "Se requiere de un evento y una edición para mostrar la información.";
    		return false;
    	}
    	
    	return true;
    }
    
    
    private DtEvento obtenerEvento(String evento) throws IOException {
        Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
		URL url_ws_eventos = new URL(propiedades.getProperty("url_ws_eventos"));
        WSEventoService servicio = new WSEventoService(url_ws_eventos);
        WSEvento port = servicio.getWSEventoPort();
        
        DtEvento dtEvento = new DtEvento();
        
        try {
        	dtEvento = port.getDTEvento(evento);
        } catch (Exception ex) {
        	error = "No se ha encontrado ningún Evento llamado " + evento + ".";
        }
    	
    	return dtEvento;
    }
    
    
    private DtEdicion obtenerEdicion(String evento, String edicion) throws IOException {
        Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
		URL url_ws_eventos = new URL(propiedades.getProperty("url_ws_eventos"));
        WSEventoService servicio = new WSEventoService(url_ws_eventos);
        WSEvento port = servicio.getWSEventoPort();
        
        DtEdicion dtEdicion = new DtEdicion();
        
        try {
        	dtEdicion = port.getDTEdicion(evento, edicion);
        } catch (Exception ex) {
    		error = "No se ha encontrado ninguna Edición llamada " + edicion + ".";
    	}
    	
    	return dtEdicion;
    }
    
    
    private DtOrganizador obtenerOrganizador(DtEdicion dtEdicion) throws IOException {
    	String nicknameOrganizador = dtEdicion.getNicknameOrganizador();
    	DtOrganizador dtOrganizador = null;
    	
        Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
		URL url_ws_usuarios = new URL(propiedades.getProperty("url_ws_usuarios"));
		WSUsuarioService servici = new WSUsuarioService(url_ws_usuarios);
        WSUsuario portu = servici.getWSUsuarioPort();
    	
    	try {
    		dtOrganizador = (DtOrganizador) portu.obtenerInfoOrganizador(nicknameOrganizador);
    	} catch (UsuarioNoExisteException_Exception exc) {
    		error = exc.getMessage();
    	}
    	
    	return dtOrganizador;
    }
    
    
    private Boolean usuarioEsOrganizador(String rol, String nickname, DtEdicion dtEdicion) throws IOException  {
    	if (rol == null || !rol.equalsIgnoreCase("organizador")) {
    		return false;
    	}
    	
    	// Usuario es organizador. Ver si es organizador de la propia edición.
    	if (nickname == null || !dtEdicion.getNicknameOrganizador().equalsIgnoreCase(nickname)) {
    		return false;
    	}
    	
    	// Obtener listado de registros
        Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
		URL url_ws_usuarios = new URL(propiedades.getProperty("url_ws_usuarios"));
		WSUsuarioService servici = new WSUsuarioService(url_ws_usuarios);
        WSUsuario portu = servici.getWSUsuarioPort();
    	try {
    		List<MapStringDTRegEntry> listaRegs = portu.listarRegistrosEdicion(dtEdicion.getNombre()).getItem();
    		dtRegistroMap = new HashMap<String, DtRegistro>();
    		for (MapStringDTRegEntry entry : listaRegs) {
    			dtRegistroMap.put(entry.getNomAsist(), entry.getRegistro());
    		}
    	} catch (UsuarioNoExisteException_Exception exc) {
    		dtRegistroMap = new HashMap<>();
    	} catch (Exception exc) {
    		dtRegistroMap = new HashMap<>();
    	}
    	
    	return true;
    }
    
    
    private Boolean usuarioEsAsistenteRegistrado(String rol, String nickname, String evento, String edicion) throws IOException {
    	if (rol == null || !rol.equalsIgnoreCase("asistente")) {
    		return false;
    	}
    	
    	// Obtener detalles de registro
        Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
		URL url_ws_eventos = new URL(propiedades.getProperty("url_ws_eventos"));
        WSEventoService servicio = new WSEventoService(url_ws_eventos);
        WSEvento port = servicio.getWSEventoPort();
        
    	dtRegistro = port.getRegistro(evento, edicion, nickname);
    	
    	if (dtRegistro == null || dtRegistro.getEvento()==null ||
    			dtRegistro.getEvento().trim().equalsIgnoreCase("") 
    			|| dtRegistro.getEvento().trim().equalsIgnoreCase("__ERROR__")) { 
    		// chequeo temporal para evitar nulls
    		return false;
    	}
    	
    	
    	return true;
    }
    
    
    private Set<DtTipoRegistro> obtenerTiposRegistro(String evento, String edicion) throws IOException  {
    	Set<DtTipoRegistro> tiposRegistro = new HashSet<>();

        Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
		URL url_ws_eventos = new URL(propiedades.getProperty("url_ws_eventos"));
        WSEventoService servicio = new WSEventoService(url_ws_eventos);
        WSEvento port = servicio.getWSEventoPort();
        
        ListOfString tiposRegistroStr = port.listarTipoRegistro(evento, edicion);
    	
    	for (String tipoReg : tiposRegistroStr.getItem()) {
    		tiposRegistro.add(port.getDTTipoReg(evento, edicion, tipoReg));
    	}
    	
    	return tiposRegistro;
    }
}