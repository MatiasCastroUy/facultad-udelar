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

@WebServlet("/login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Login() { super(); }

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
        HttpSession sesion = request.getSession(true);

        String inputUsuario = request.getParameter("usuario");     // email o nickname
        String inputPass    = request.getParameter("contrasena");  // contraseña
        String nextParam    = request.getParameter("next");        // destino opcional post-login

        if ("GET".equalsIgnoreCase(request.getMethod())
                && (isBlank(inputUsuario) || isBlank(inputPass))) {
            if (!isBlank(nextParam)) request.setAttribute("next", nextParam);
            request.getRequestDispatcher("/WEB-INF/login/login.jsp").forward(request, response);
            return;
        }

        if (isBlank(inputUsuario) || isBlank(inputPass)) {
            request.setAttribute("error", "Debe ingresar usuario y contraseña.");
            request.setAttribute("identificador", inputUsuario);
            if (!isBlank(nextParam)) request.setAttribute("next", nextParam);
            request.getRequestDispatcher("/WEB-INF/login/login.jsp").forward(request, response);
            return;
        }

        WSUsuario port = getPort();

        try {
            String nick = inputUsuario.contains("@")
                    ? port.obtenerNicknamePorEmail(inputUsuario.trim())
                    : inputUsuario.trim();

            boolean ok = port.verificarContrasena(nick, inputPass);
            if (!ok) {
                request.setAttribute("error", "Contraseña incorrecta.");
                request.setAttribute("identificador", inputUsuario);
                if (!isBlank(nextParam)) request.setAttribute("next", nextParam);
                request.getRequestDispatcher("/WEB-INF/login/login.jsp").forward(request, response);
                return;
            }

            String rol = port.obtenerRol(nick);
            if (rol == null || !rol.equalsIgnoreCase("Asistente")) {
                request.setAttribute("error", "Solo asistentes pueden iniciar sesión desde el dispositivo móvil.");
                request.setAttribute("identificador", inputUsuario);
                if (!isBlank(nextParam)) request.setAttribute("next", nextParam);
                request.getRequestDispatcher("/WEB-INF/login/login.jsp").forward(request, response);
                return;
            }

            sesion.setAttribute("usuarioLogueado", nick);
            sesion.setAttribute("rol", "Asistente");

            if (!isBlank(nextParam)) {
                response.sendRedirect(nextParam);
            } else {
                request.getRequestDispatcher("/index.html").forward(request, response);
            }
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

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
