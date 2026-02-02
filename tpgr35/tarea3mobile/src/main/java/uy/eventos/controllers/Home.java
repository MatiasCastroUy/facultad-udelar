package uy.eventos.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webservices.DtEvento;
import webservices.WSEvento;
import webservices.WSEventoService;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;


/**
 * Servlet implementation class Home
 */
@WebServlet("/home")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Home() {
        super();
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException {
    	
    	String usuarioLogueado = (String) request.getSession().getAttribute("usuarioLogueado");
    	if (usuarioLogueado == null || usuarioLogueado.isBlank()) {
    		response.sendRedirect(request.getContextPath() + "/login");
    		return;
    	}
    	
    	// si tiene el parámetro categoría, filtramos por ella
    	String categoria = request.getParameter("categoria");
    	boolean filtrar = categoria!=null;
    	request.setAttribute("filtrar", filtrar);
    	if (filtrar) request.setAttribute("categoria", categoria);
    	
    	Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
		URL url_ws_eventos = new URL(propiedades.getProperty("url_ws_eventos"));
    	WSEventoService service = new WSEventoService(url_ws_eventos);
		WSEvento portEvento = service.getWSEventoPort();
    	
    	// actualizamos las categorías en la sesión
    	request.getSession()
    		.setAttribute("categorias", 
    				portEvento.getCategorias().getItem());
		
    	List<String> nomEventos = portEvento.listarEventos().getItem();
    	List<DtEvento> listaEventos = new Vector<DtEvento>();
    	for (String nomEvt:nomEventos) {
    		List<String> categorias = portEvento.listarCategorias(nomEvt).getItem();
    		if (!filtrar || categorias.contains(categoria)) {
    			listaEventos.add(portEvento.getDTEvento(nomEvt));
    		}
    	}
    	request.setAttribute("listaEventos", listaEventos);
    	
    	// mapeamos imagenes a eventos
    	Map<String, String> imagenes_b64 = new HashMap<>();
    	for (DtEvento evto : listaEventos) {
    		byte[] img_bytes;
        	String img_b64;
    		img_bytes = portEvento.getImagenEvento(evto.getNombre());
    		            	
        	if (img_bytes == null || img_bytes.length == 0) {
        		img_b64 = null;
        	} else {
        		img_b64 = Base64.getEncoder().encodeToString(img_bytes);
        	}
        	imagenes_b64.put(evto.getNombre(), img_b64);
    	}
    	request.setAttribute("imagenes_b64", imagenes_b64);
    	
    	request.getRequestDispatcher("/WEB-INF/Home.jsp")
    		.forward(request, response);
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
