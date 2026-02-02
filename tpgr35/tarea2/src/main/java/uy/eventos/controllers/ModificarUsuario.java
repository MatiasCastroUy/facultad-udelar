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
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;


import webservices.UsuarioNoExisteException;
import webservices.ContrasenaInvalidaException;
import webservices.WSUsuario;
import webservices.WSUsuarioService;
import webservices.UsuarioNoExisteException_Exception;
import webservices.WSEventoService;
import webservices.ContrasenaInvalidaException_Exception;
import webservices.DtUsuario;
import java.time.format.DateTimeFormatter;

@WebServlet("/modificarUsuario")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5MB max
public class ModificarUsuario extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Verificar que el usuario esté logueado
        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String nickname = (String) sesion.getAttribute("usuarioLogueado");

        try {
            // Consumir el servicio web WSUsuario
        	Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
    		URL url_ws_usuarios = new URL(propiedades.getProperty("url_ws_usuarios"));
            WSUsuarioService service = new WSUsuarioService(url_ws_usuarios);
            WSUsuario portUsuario = service.getWSUsuarioPort();

            // Obtener información del usuario usando getUsuario (más genérico)
            DtUsuario usuario = null;
            boolean esAsistente = false;
            
            try {
                usuario = portUsuario.getUsuario(nickname);
                // Determinar el tipo basado en la instancia
                esAsistente = usuario instanceof webservices.DtAsistente;
            } catch (UsuarioNoExisteException_Exception e) {
                request.setAttribute("error", "Usuario no encontrado: " + nickname);
                request.getRequestDispatcher("/WEB-INF/ModificarUsuario/ModificarUsuario.jsp").forward(request, response);
                return;
            }

            request.setAttribute("usuario", usuario);
            request.setAttribute("esAsistente", esAsistente);
            request.setAttribute("esOrganizador", !esAsistente);

            request.getRequestDispatcher("/WEB-INF/ModificarUsuario/ModificarUsuario.jsp").forward(request, response);

        } catch (ServletException servletException) {
            throw servletException;
        } catch (IOException ioException) {
            throw ioException;
        } catch (Exception e) {
            request.setAttribute("error", "Error inesperado: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/ModificarUsuario/ModificarUsuario.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession sesion = request.getSession(false);
        if (sesion == null || sesion.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String nickname = (String) sesion.getAttribute("usuarioLogueado");

        // Declarar portUsuario fuera del try para que esté disponible en catch
        WSUsuario portUsuario = null;
        
        try {
            // Consumir el servicio web WSUsuario
        	Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
    		URL url_ws_usuarios = new URL(propiedades.getProperty("url_ws_usuarios"));
            WSUsuarioService service = new WSUsuarioService(url_ws_usuarios);
            portUsuario = service.getWSUsuarioPort();

            // Obtener datos del formulario
            String nombre = request.getParameter("nombre");
            String nuevaContrasena = request.getParameter("password");
            String confirmarContrasena = request.getParameter("confirmPassword");
            String fechaNacStr = request.getParameter("fechaNacimiento");
            String descripcion = request.getParameter("descripcion");
            String sitioWeb = request.getParameter("sitioWeb");
            String imagenPerfil = request.getParameter("imagenPerfil");
            String apellido = request.getParameter("apellido");
            
            // Asegurar que no haya parámetros null (reemplazar por cadena vacía)
            if (nombre == null) nombre = "";
            if (nuevaContrasena == null) nuevaContrasena = "";
            if (fechaNacStr == null) fechaNacStr = "";
            if (descripcion == null) descripcion = "";
            if (sitioWeb == null) sitioWeb = "";
            if (imagenPerfil == null) imagenPerfil = "";
            if (apellido == null) apellido = "";
            
            // Procesar imagen de perfil subida
            Part imagenPart = request.getPart("fotoInput");
            if (imagenPart != null && imagenPart.getSize() > 0) {
                String rutaImagen = procesarImagen(imagenPart, request);
                if (rutaImagen != null) {
                    imagenPerfil = rutaImagen;
                } else {
                    request.setAttribute("error", "Error al procesar la imagen. Asegúrese de que sea un archivo válido (JPG, PNG, GIF) y no exceda 5MB.");
                    
                    // Recargar datos del usuario para mostrar el formulario con error
                    DtUsuario usuario = portUsuario.getUsuario(nickname);
                    boolean esAsistente = usuario instanceof webservices.DtAsistente;
                    request.setAttribute("usuario", usuario);
                    request.setAttribute("esAsistente", esAsistente);
                    request.setAttribute("esOrganizador", !esAsistente);
                    
                    request.getRequestDispatcher("/WEB-INF/ModificarUsuario/ModificarUsuario.jsp").forward(request, response);
                    return;
                }
            }

            // Validar contraseñas
            if (nuevaContrasena != null && !nuevaContrasena.trim().isEmpty() && !nuevaContrasena.equals(confirmarContrasena)) {
                request.setAttribute("error", "Las contraseñas no coinciden.");
                
                // Recargar datos del usuario para mostrar el formulario con error
                String rol = portUsuario.obtenerRol(nickname);
                boolean esAsistente = "ASISTENTE".equals(rol);
                DtUsuario usuario = portUsuario.verInfoUsuario(nickname, esAsistente);
                request.setAttribute("usuario", usuario);
                request.setAttribute("esAsistente", esAsistente);
                request.setAttribute("esOrganizador", !esAsistente);
                
                request.getRequestDispatcher("/WEB-INF/ModificarUsuario/ModificarUsuario.jsp").forward(request, response);
                return;
            }

            // Llamar al método del web service con los parámetros correctos
            // arg0=nickname, arg1=nombre, arg2=nuevaContrasena, arg3=fechaNacStr, arg4=descripcion, arg5=sitioWeb, arg6=apellido
            portUsuario.modificarUsuario(nickname, nombre, nuevaContrasena, fechaNacStr, descripcion, sitioWeb, apellido);

            // Si hay imagen de perfil, llamar al método separado
            if (imagenPerfil != null && !imagenPerfil.trim().isEmpty()) {
                portUsuario.modificarImagenPerfil(nickname, imagenPerfil);
            }

            // Redirigir directamente a consultaUsuario después de la modificación exitosa
            response.sendRedirect(request.getContextPath() + "/consultaUsuario?nickname=" + nickname);

        } catch (UsuarioNoExisteException_Exception | ContrasenaInvalidaException_Exception ex) {
            request.setAttribute("error", ex.getMessage());
            
            // Solo intentar recargar datos si portUsuario está disponible
            if (portUsuario != null) {
                try {
                    // Recargar datos del usuario para mostrar el formulario con error
                    DtUsuario usuario = portUsuario.getUsuario(nickname);
                    boolean esAsistente = usuario instanceof webservices.DtAsistente;
                    request.setAttribute("usuario", usuario);
                    request.setAttribute("esAsistente", esAsistente);
                    request.setAttribute("esOrganizador", !esAsistente);
                } catch (Exception e) {
                    // Si no se pueden recargar los datos, al menos mostrar el error
                    request.setAttribute("error", "Error: " + ex.getMessage() + ". No se pudieron recargar los datos del usuario.");
                }
            }
            
            request.getRequestDispatcher("/WEB-INF/ModificarUsuario/ModificarUsuario.jsp").forward(request, response);
        } catch (ServletException servletException) {
            throw servletException;
        } catch (IOException ioException) {
            throw ioException;
        }
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean validarContrasena(String contrasena) {
        // Validación de contraseña: mínimo 6 caracteres, debe contener al menos una letra y un número
        if (contrasena == null || contrasena.length() < 6) {
            return false;
        }

        boolean tieneLetra = false;
        boolean tieneNumero = false;

        for (char c : contrasena.toCharArray()) {
            if (Character.isLetter(c)) {
                tieneLetra = true;
            } else if (Character.isDigit(c)) {
                tieneNumero = true;
            }
        }

        return tieneLetra && tieneNumero;
    }

    private String procesarImagen(Part imagenPart, HttpServletRequest request) {
        try {
            String fileName = imagenPart.getSubmittedFileName();

            if (fileName == null || fileName.trim().isEmpty()) {
                return null;
            }

            String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();

            if (!extension.matches("\\.(jpg|jpeg|png|gif)$")) {
                return null;
            }

            String uploadPath = getServletContext().getRealPath("") + File.separator + "recursos" + File.separator + "img";

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            Path filePath = Paths.get(uploadPath, uniqueFileName);

            Files.copy(imagenPart.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFileName;

        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }
}