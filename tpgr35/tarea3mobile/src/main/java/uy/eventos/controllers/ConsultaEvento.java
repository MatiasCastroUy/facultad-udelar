package uy.eventos.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import webservices.DtEdicion;
import webservices.DtEvento;
import webservices.ListOfString;
import webservices.WSEvento;
import webservices.WSEventoService;
import webservices.WSUsuario;
import webservices.WSUsuarioService;


@WebServlet("/consultaEvento")
public class ConsultaEvento extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ConsultaEvento() { super(); }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	String usuarioLogueado = (String) request.getSession().getAttribute("usuarioLogueado");
    	if (usuarioLogueado == null || usuarioLogueado.isBlank()) {
    		response.sendRedirect(request.getContextPath() + "/login");
    		return;
    	}
    	
        String nombreEvento = request.getParameter("nombre");
        
     
        Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
		URL url_ws_eventos = new URL(propiedades.getProperty("url_ws_eventos"));
		URL url_ws_usuarios = new URL(propiedades.getProperty("url_ws_usuarios"));
		WSUsuarioService servici = new WSUsuarioService(url_ws_usuarios);
        WSUsuario portu = servici.getWSUsuarioPort();
        WSEventoService servicio = new WSEventoService(url_ws_eventos);
        WSEvento port = servicio.getWSEventoPort();
        
        
        
       

        DtEvento evento = port.getDTEvento(nombreEvento);    

        // Categorías
        ListOfString categoriasWs = port.listarCategorias(nombreEvento);
        Set<String> categorias = new HashSet<>(categoriasWs.getItem());
        String categoriasStr =  String.join(", ", categorias);

        // Ediciones
        ListOfString edicionesWs = port.listarEdicionesAceptadas(nombreEvento);
        List<DtEdicion> listaEdiciones = new ArrayList<>();
        for (String nomEd : edicionesWs.getItem()) {
            DtEdicion dtEd = port.getDTEdicion(nombreEvento, nomEd);
            listaEdiciones.add(dtEd);
        }
        
     // Preparar imágenes
        byte[] img_evt_bytes = port.getImagenEvento(nombreEvento);
        String imagenEvento_b64 = img_evt_bytes.length > 0 ? Base64.getEncoder().encodeToString(img_evt_bytes) : null;

        Map<String, String> imagenesEdiciones_b64 = new HashMap<>();
        for (DtEdicion edi : listaEdiciones) {
        	byte[] img_bytes;
        	String img_b64;
    		img_bytes = port.getImagenEdicion(nombreEvento, edi.getNombre());
    		            	
        	if (img_bytes == null || img_bytes.length == 0) {
        		img_b64 = null;
        	} else {
        		img_b64 = Base64.getEncoder().encodeToString(img_bytes);
        	}
        	imagenesEdiciones_b64.put(edi.getNombre(), img_b64);
        }
        
        request.setAttribute("evento", evento);
        request.setAttribute("categoriasStr", categoriasStr);
        request.setAttribute("listaEdiciones", listaEdiciones);
        request.setAttribute("imagenEvento_b64", imagenEvento_b64);
        request.setAttribute("imagenesEdiciones_b64", imagenesEdiciones_b64);
        

        request.getRequestDispatcher("/WEB-INF/ConsultaEvento/ConsultaEvento.jsp").forward(request, response);
    }
}
