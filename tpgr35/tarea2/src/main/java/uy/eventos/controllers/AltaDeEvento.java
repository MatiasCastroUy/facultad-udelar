package uy.eventos.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;


import webservices.CategoriaEventoVacia_Exception;
import webservices.EventoRepetido_Exception;
import webservices.ListOfString;
import webservices.UsuarioNoExisteException_Exception;
import webservices.WSEvento;
import webservices.WSEventoService;
import webservices.WSUsuario;
import webservices.WSUsuarioService;


@WebServlet("/altaEvento")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      
    maxFileSize = 1024 * 1024 * 5,        
    maxRequestSize = 1024 * 1024 * 10     
)
public class AltaDeEvento extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AltaDeEvento() { super(); }

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
    	
    	Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
		URL url_ws_eventos = new URL(propiedades.getProperty("url_ws_eventos"));
		URL url_ws_usuarios = new URL(propiedades.getProperty("url_ws_usuarios"));
    	
		WSUsuarioService servici = new WSUsuarioService(url_ws_usuarios);
        WSUsuario portu = servici.getWSUsuarioPort();
        
    		String nick = (String) request.getSession().getAttribute("usuarioLogueado");
        if (nick == null) {
            // No logueado
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Verificar rol
        String rol = null;
        try {
            rol = portu.obtenerRol(nick);//ver esto bien luego
        } catch (UsuarioNoExisteException_Exception e) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        if (!"ORGANIZADOR".equalsIgnoreCase(rol)) {
            // No es organizador
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
    	
        WSEventoService servicio = new WSEventoService(url_ws_eventos);
        WSEvento port = servicio.getWSEventoPort();
       
        List<String> categoriasDisponibles = port.getCategorias().getItem();
        request.setAttribute("categoriasDisponibles", categoriasDisponibles);
       // Ver que si le dieron a Crear Evento
        boolean formularioEnviado = "POST".equalsIgnoreCase(request.getMethod());

        if (formularioEnviado) {
            
            String nombre = request.getParameter("nombre");
            String desc = request.getParameter("desc");
            String sig = request.getParameter("sigla");
            

            String[] categoriasA = request.getParameterValues("categorias");
            Set<String> categorias = new HashSet<>();
            if (categoriasA != null) {
                categorias.addAll(Arrays.asList(categoriasA));
            }
          // Foto
            Part archivoImagen = request.getPart("imagen");
            String nombreArchivo = null;

            byte[] img_bytes = null;
            if (archivoImagen != null && archivoImagen.getSize() > 0) {
               try {
            	   img_bytes = archivoImagen.getInputStream().readAllBytes();
               } catch (IOException io_exc) {
            	   System.out.println("(Alta de Evento) Error al leer imagen.");
            	   io_exc.printStackTrace();
               }
            }

            // Validaciones
            if (nombre == null || nombre.isBlank()) {
                request.setAttribute("error", "El nombre del evento es obligatorio.");
            } else if (desc == null || desc.isBlank()) {
                request.setAttribute("error", "La descripción es obligatoria.");
            } else if (sig == null || sig.isBlank()) {
                request.setAttribute("error", "La sigla es obligatoria.");
            } else if (categorias.isEmpty()) {
                request.setAttribute("error", "Debe seleccionar al menos una categoría.");
            } else {
            	ListOfString categoriasWs = new ListOfString();
            	categoriasWs.getItem().addAll(categorias);
            	if (nombreArchivo == null) {
            	    nombreArchivo = "";
            	}
                try {
                	port.altaEvento(nombre, desc, sig, categoriasWs, nombreArchivo);
                	if (img_bytes!=null) port.setImagenEvento(nombre, img_bytes);
                    request.setAttribute("mensaje", "Evento creado correctamente");
                    request.setAttribute("limpiarFormulario", true);
                } catch (CategoriaEventoVacia_Exception e) {
                    request.setAttribute("error", "Debe seleccionar alguna categoría para el evento.");
                } catch (EventoRepetido_Exception e) {
                    request.setAttribute("error", "Ya existe un evento con ese nombre.");
                }
            }
        }
        
        
        request.getRequestDispatcher("/WEB-INF/AltaEvento/AltaEvento.jsp").forward(request, response);
    }
}