package uy.eventos.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import webservices.WSEvento;
import webservices.WSEventoService;


@WebFilter("/consultaEvento")
public class Filtro implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request; 

        ServletContext context = request.getServletContext(); 
        Properties propiedades = (Properties) context.getAttribute("propiedades");
        URL url_ws_eventos = new URL(propiedades.getProperty("url_ws_eventos"));

        String nombreEvento = req.getParameter("nombre");
        
        WSEventoService servicio = new WSEventoService(url_ws_eventos);
        WSEvento port = servicio.getWSEventoPort();
        port.sumarVisita(nombreEvento);           
        chain.doFilter(request, response);
    }
}
