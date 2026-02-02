package uy.eventos.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import webservices.*;

@WebServlet("/seguirUsuario")
public class SeguirUsuario extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigir GET a POST no está permitido para esta acción
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Solo se permiten peticiones POST");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        
        // Debug temporal
        System.out.println("=== SEGUIR USUARIO DEBUG ===");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Context Path: " + request.getContextPath());

        // Verificar que el usuario esté logueado
        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String usuarioSeguidor = (String) sesion.getAttribute("usuarioLogueado");
        String accion = request.getParameter("accion");
        String usuarioSeguido = request.getParameter("usuarioSeguido");
        String redirectUrl = request.getParameter("redirectUrl");

        // Debug temporal
        System.out.println("Usuario Seguidor: " + usuarioSeguidor);
        System.out.println("Accion: " + accion);
        System.out.println("Usuario Seguido: " + usuarioSeguido);
        System.out.println("Redirect URL: " + redirectUrl);

        // Validar parámetros
        if (accion == null || usuarioSeguido == null || usuarioSeguido.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetros faltantes");
            return;
        }

        // URL de redirección por defecto
        if (redirectUrl == null || redirectUrl.trim().isEmpty()) {
            redirectUrl = request.getContextPath() + "/consultaUsuario?nickname=" + usuarioSeguido;
        }

        try {
            // Consumir el servicio web WSUsuario
            Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
            URL url_ws_usuarios = new URL(propiedades.getProperty("url_ws_usuarios"));
            WSUsuarioService service = new WSUsuarioService(url_ws_usuarios);
            WSUsuario portUsuario = service.getWSUsuarioPort();

            // Procesar la acción
            if ("seguir".equals(accion)) {
                portUsuario.seguirUsr(usuarioSeguidor, usuarioSeguido);
            } else if ("dejar_seguir".equals(accion)) {
                portUsuario.dejarDeSeguirUsr(usuarioSeguidor, usuarioSeguido);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida: " + accion);
                return;
            }

            // Redirigir de vuelta a la página de origen
            response.sendRedirect(redirectUrl);

        } catch (UsuarioNoExisteException_Exception e) {
            // Redirigir con mensaje de error
            String errorUrl = redirectUrl + (redirectUrl.contains("?") ? "&" : "?") + 
                            "error=" + java.net.URLEncoder.encode("Usuario no encontrado: " + e.getMessage(), "UTF-8");
            response.sendRedirect(errorUrl);
        } catch (Exception e) {
            // Redirigir con mensaje de error genérico
            String errorUrl = redirectUrl + (redirectUrl.contains("?") ? "&" : "?") + 
                            "error=" + java.net.URLEncoder.encode("Error al procesar la solicitud: " + e.getMessage(), "UTF-8");
            response.sendRedirect(errorUrl);
        }
    }
//
}
