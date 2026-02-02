package uy.eventos.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import webservices.WSUsuario;
import webservices.WSUsuarioService;
import webservices.UsuarioCorreoRepetidoException_Exception;
import webservices.UsuarioRepetidoException_Exception;
import webservices.ContrasenaInvalidaException_Exception;

@WebServlet("/usuario")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,    // 1 MB
    maxFileSize = 1024 * 1024 * 5,      // 5 MB
    maxRequestSize = 1024 * 1024 * 10   // 10 MB
)
public class AltaDeUsuario extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AltaDeUsuario() { super(); }

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

        request.setCharacterEncoding("UTF-8");
        
        // Get muestra el formulario del registro
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            request.getRequestDispatcher("/WEB-INF/AltaUsuario/AltaUsuario.jsp").forward(request, response);
            return;
        }
        
        // POST va a procesar el registro
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            procesarRegistro(request, response);
            return;
        }
    }
    
    private void procesarRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener parámetros comunes
        String tipoUsuario = request.getParameter("tipoUsuario");
        String nickname = request.getParameter("nickname");
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Procesar imagen de perfil
        String rutaImagen = null;
        Part imagenPart = request.getPart("imagenPerfil");
        byte[] img_bytes = null;
        if (imagenPart != null && imagenPart.getSize() > 0) {
            img_bytes = imagenPart.getInputStream().readAllBytes();
        }
        
        if (isBlank(tipoUsuario) || isBlank(nickname) || isBlank(correo) || 
            isBlank(password) || isBlank(confirmPassword)) {
            request.setAttribute("error", "Todos los campos obligatorios deben estar completos.");
            request.getRequestDispatcher("/WEB-INF/AltaUsuario/AltaUsuario.jsp").forward(request, response);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("/WEB-INF/AltaUsuario/AltaUsuario.jsp").forward(request, response);
            return;
        }
        
        try {
        	
        	Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
    		URL url_ws_usuarios = new URL(propiedades.getProperty("url_ws_usuarios"));
            WSUsuarioService service = new WSUsuarioService(url_ws_usuarios);
            WSUsuario portUsuario = service.getWSUsuarioPort();

            if ("asistente".equals(tipoUsuario)) {
                // Procesar asistente
                String nombreAsis = request.getParameter("nombreAsis");
                String apellidoAsis = request.getParameter("apellidoAsis");
                String fechaNacStr = request.getParameter("fechaNacAsis");
                
                if (isBlank(nombreAsis) || isBlank(apellidoAsis) || isBlank(fechaNacStr)) {
                    request.setAttribute("error", "Todos los campos de asistente son obligatorios.");
                    request.getRequestDispatcher("/WEB-INF/AltaUsuario/AltaUsuario.jsp").forward(request, response);
                    return;
                }
                
                LocalDate fechaNac = LocalDate.parse(fechaNacStr);
                
                // Usar registrarUsuarioConContrasena para incluir la contraseña
                // Asegurar que rutaImagen no sea null
                String imagenPerfil = (rutaImagen != null) ? rutaImagen : "";
                
                // Para asistente: nickname, nombre, correo, contrasena, confirmacionContrasena, imagenPerfil, esAsistente, apellido, fechaNacimiento, descripcion, sitioWeb
                portUsuario.registrarUsuarioConContrasena(nickname, nombreAsis, correo, password, confirmPassword, imagenPerfil, true, apellidoAsis, fechaNac.toString(), "", "");
                    
            } else if ("organizador".equals(tipoUsuario)) {
                // Procesar organizador
                String nombreOrga = request.getParameter("nombreOrga");
                String descripcionOrga = request.getParameter("descripcionOrga");
                String sitioWebOrga = request.getParameter("sitioWebOrga");
                
                if (isBlank(nombreOrga) || isBlank(descripcionOrga)) {
                    request.setAttribute("error", "Nombre y descripción son obligatorios para organizadores.");
                    request.getRequestDispatcher("/WEB-INF/AltaUsuario/AltaUsuario.jsp").forward(request, response);
                    return;
                }
                
                // Usar registrarUsuarioConContrasena para incluir la contraseña
                // Asegurar que rutaImagen no sea null
                String imagenPerfil = (rutaImagen != null) ? rutaImagen : "";
                String sitioWeb = (sitioWebOrga != null) ? sitioWebOrga : "";
                
                // Para organizador: nickname, nombre, correo, contrasena, confirmacionContrasena, imagenPerfil, esAsistente, apellido, fechaNacimiento, descripcion, sitioWeb
                // Organizador no tiene apellido ni fecha de nacimiento, pasar cadenas vacías
                portUsuario.registrarUsuarioConContrasena(nickname, nombreOrga, correo, password, confirmPassword, imagenPerfil, false, "", "", descripcionOrga, sitioWeb);
            }
            
            if (img_bytes!=null) portUsuario.setImagenUsuario(nickname, img_bytes);
            
            request.setAttribute("exito", "Usuario registrado exitosamente. Puede iniciar sesión.");
            request.getRequestDispatcher("/WEB-INF/login/login.jsp").forward(request, response);
            
        } catch (UsuarioRepetidoException_Exception usuarioRepetido) {
            request.setAttribute("error", "El nickname ya está en uso.");
            request.getRequestDispatcher("/WEB-INF/AltaUsuario/AltaUsuario.jsp").forward(request, response);
        } catch (UsuarioCorreoRepetidoException_Exception correoRepetido) {
            request.setAttribute("error", "El correo ya está registrado.");
            request.getRequestDispatcher("/WEB-INF/AltaUsuario/AltaUsuario.jsp").forward(request, response);
        } catch (ContrasenaInvalidaException_Exception contrasenaInvalida) {
            request.setAttribute("error", "Contraseña inválida: " + contrasenaInvalida.getMessage());
            request.getRequestDispatcher("/WEB-INF/AltaUsuario/AltaUsuario.jsp").forward(request, response);
        } catch (DateTimeParseException fechaInvalida) {
            request.setAttribute("error", "Formato de fecha inválido.");
            request.getRequestDispatcher("/WEB-INF/AltaUsuario/AltaUsuario.jsp").forward(request, response);
        } catch (ServletException servletException) {
            request.setAttribute("error", "Error de servlet: " + servletException.getMessage());
            request.getRequestDispatcher("/WEB-INF/AltaUsuario/AltaUsuario.jsp").forward(request, response);
        } catch (IOException ioException) {
            request.setAttribute("error", "Error de entrada/salida: " + ioException.getMessage());
            request.getRequestDispatcher("/WEB-INF/AltaUsuario/AltaUsuario.jsp").forward(request, response);
        }
    }
    
    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}