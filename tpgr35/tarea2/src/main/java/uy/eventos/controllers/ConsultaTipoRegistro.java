package uy.eventos.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webservices.WSEvento;
import webservices.WSEventoService;

@SuppressWarnings("serial")
@WebServlet ("/consultaTipoReg")
public class ConsultaTipoRegistro extends HttpServlet {
	public ConsultaTipoRegistro() {
		super();
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {		

		Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
		URL url_ws_eventos = new URL(propiedades.getProperty("url_ws_eventos"));
        WSEventoService service = new WSEventoService(url_ws_eventos);
		WSEvento portEvento = service.getWSEventoPort();
		
		String nomEvento = request.getParameter("evento");
		String nomEdicion = request.getParameter("edicion");
		String nomTipoReg = request.getParameter("tiporeg");
		
		if (nomEvento==null || nomEdicion==null || nomTipoReg==null) {
			throw new ServletException("Parámetros incompletos");
		}

		request.setAttribute("nomEvento", nomEvento);
		request.setAttribute("edicion", portEvento.getDTEdicion(nomEvento, nomEdicion));
		request.setAttribute("tiporeg", portEvento.getDTTipoReg(nomEvento, nomEdicion, nomTipoReg));
		
		
		
		request.getRequestDispatcher("/WEB-INF/consultaTipoReg/consultaTipoReg.jsp")
			.forward(request, response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		processRequest(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		processRequest(request, response);
	}
}
