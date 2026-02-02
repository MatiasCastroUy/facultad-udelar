package uy.eventos.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webservices.DtEdicion;
import webservices.TipoRegistroRepetidoExcepcion_Exception;
import webservices.WSEvento;
import webservices.WSEventoService;

@WebServlet("/altaTipoReg")
public class AltaTipoRegistro extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AltaTipoRegistro() { super(); }

    // Para llenar el formulario inicialmente
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        crearNuevoTipoReg(request, response);
    }

    // Para procesar el formulario luego de rellenarlo
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        tipoRegCreado(request, response);
    }

    
    protected void crearNuevoTipoReg(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        
        // consumimos web service evento
        Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
		URL url_ws_eventos = new URL(propiedades.getProperty("url_ws_eventos"));
        WSEventoService service = new WSEventoService(url_ws_eventos);
        WSEvento portEvento = service.getWSEventoPort();
        
        String nomEvento = request.getParameter("evento");
        String nomEdicion = request.getParameter("edicion");
		request.setAttribute("nomEvento", nomEvento);
		DtEdicion edicion = portEvento.getDTEdicion(nomEvento, nomEdicion);
		request.setAttribute("edicion", edicion);
		
		String rolUsuario = (String) request.getSession().getAttribute("rol");
		String nickUsuario = (String) request.getSession().getAttribute("usuarioLogueado");
		boolean tienePermisos = rolUsuario.equalsIgnoreCase("organizador") 
				&& nickUsuario.equalsIgnoreCase(edicion.getNicknameOrganizador());
		request.setAttribute("tienePermisos", tienePermisos);
		request.getRequestDispatcher("/WEB-INF/AltaTipoRegistro/AltaTipoReg.jsp")
			.forward(request, response);
        
    }
    
    protected void tipoRegCreado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	// consumimos web service evento
    	Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
		URL url_ws_eventos = new URL(propiedades.getProperty("url_ws_eventos"));
        WSEventoService service = new WSEventoService(url_ws_eventos);
        WSEvento portEvento = service.getWSEventoPort();
        
        String nomEvento = request.getParameter("evento");
    	String nomEdicion = request.getParameter("edicion");
    	String nomTipoReg = request.getParameter("nombreTipoReg");
    	int cupoTipoReg = Integer.parseInt(request.getParameter("cupoTipoReg"));
    	float costoTipoReg = Float.parseFloat(request.getParameter("costoTipoReg"));
    	String descTipoReg = request.getParameter("descTipoReg");
    	
    	request.setAttribute("nomEvento", nomEvento);
    	request.setAttribute("edicion", portEvento.getDTEdicion(nomEvento, nomEdicion));
    	request.setAttribute("nomTipoReg", nomTipoReg);
    	DtEdicion edicion = portEvento.getDTEdicion(nomEvento, nomEdicion);
    	
    	String rolUsuario = (String) request.getSession().getAttribute("rol");
		String nickUsuario = (String) request.getSession().getAttribute("usuarioLogueado");
		boolean tienePermisos = rolUsuario.equalsIgnoreCase("organizador") 
				&& nickUsuario.equalsIgnoreCase(edicion.getNicknameOrganizador());
		request.setAttribute("tienePermisos", tienePermisos);
    	
    	try {
			portEvento.crearTipoRegistro(nomEvento, nomEdicion, nomTipoReg, descTipoReg, costoTipoReg, cupoTipoReg);
			// le dice al JSP que se creó con éxito para permitir consultar el nuevo tipoReg
			request.setAttribute("estado", "exito");
		} catch (TipoRegistroRepetidoExcepcion_Exception e) {
			// si falla, le avisa al JSP
			request.setAttribute("estado", "error");
			request.setAttribute("error", e.getMessage());
		}
    	
    	request.getRequestDispatcher("/WEB-INF/AltaTipoRegistro/AltaTipoReg.jsp").forward(request, response);
    	
    }
}