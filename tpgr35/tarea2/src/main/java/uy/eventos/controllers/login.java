package uy.eventos.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import webservices.WSUsuario;
import webservices.WSUsuarioService;
import webservices.UsuarioNoExisteException_Exception;
import webservices.WSEventoService;

@WebServlet("/login")                 
public class login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public login() { super(); }

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

    private WSUsuario getPort() throws IOException {
    	Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
		URL url_ws_usuarios = new URL(propiedades.getProperty("url_ws_usuarios"));
        WSUsuarioService svc = new WSUsuarioService(url_ws_usuarios);
        return svc.getWSUsuarioPort();
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession sesion = request.getSession(true); // Crea la sesión

        String inputUsuario = request.getParameter("usuario");     // email o nickname
        String inputPass    = request.getParameter("contrasena");  // contraseña
        String nextParam    = request.getParameter("next");        // destino opcional post-login

        if ("GET".equalsIgnoreCase(request.getMethod())
                && (isBlank(inputUsuario) || isBlank(inputPass))) {
            if (!isBlank(nextParam)) request.setAttribute("next", nextParam);
            request.getRequestDispatcher("/WEB-INF/login/login.jsp").forward(request, response);
            return;
        }

        // Validacion minima de los campos obligatorios
        if (isBlank(inputUsuario) || isBlank(inputPass)) {
            request.setAttribute("error", "Debe ingresar usuario y contraseña.");
            if (!isBlank(nextParam)) request.setAttribute("next", nextParam);
            request.getRequestDispatcher("/WEB-INF/login/login.jsp").forward(request, response);
            return;
        }


        WSUsuario port = getPort();

        try {
            // - Si el usuario ingresa un email (contiene "@"), buscamos su nickname.
            // - Si no, asumimos que ya es el nickname.
            String nick;
            if (inputUsuario.contains("@")) {
                nick = port.obtenerNicknamePorEmail(inputUsuario.trim());
            } else {
                nick = inputUsuario.trim();
            }

            boolean contrasenaCorrecta = port.verificarContrasena(nick, inputPass);

            if (!contrasenaCorrecta) {
                request.setAttribute("error", "Contraseña incorrecta.");
                request.setAttribute("identificador", inputUsuario);
                if (!isBlank(nextParam)) request.setAttribute("next", nextParam);
                request.getRequestDispatcher("/WEB-INF/login/login.jsp").forward(request, response);
                return;
            }

            // guardamos en sesión el nickname y setteamos la sesion
            sesion.setAttribute("usuarioLogueado", nick);

            String rol = port.obtenerRol(nick);

            sesion.setAttribute("rol", rol);
            request.getRequestDispatcher("/index.html").forward(request, response);
            return;

        } catch (UsuarioNoExisteException_Exception e) {
            request.setAttribute("error", "No existe un usuario con ese identificador.");
            request.setAttribute("identificador", inputUsuario);
            if (!isBlank(nextParam)) request.setAttribute("next", nextParam);
            request.getRequestDispatcher("/WEB-INF/login/login.jsp").forward(request, response);

        } catch (ServletException | IOException e) {
            request.setAttribute("error", "Error en autenticación: " + e.getMessage());
            if (!isBlank(nextParam)) request.setAttribute("next", nextParam);
            request.getRequestDispatcher("/WEB-INF/login/login.jsp").forward(request, response);
        }
    }

    // true si la cadena es nula o vacía luego de trim()
    private static boolean isBlank(String str) { return str == null || str.trim().isEmpty(); }
}
