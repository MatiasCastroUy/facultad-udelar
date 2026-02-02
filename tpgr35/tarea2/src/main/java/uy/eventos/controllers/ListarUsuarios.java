package uy.eventos.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import webservices.WSUsuario;
import webservices.WSUsuarioService;
import webservices.UsuarioNoExisteException_Exception;
import webservices.DtUsuario;
import webservices.IOException_Exception;
import webservices.SetOfDTUsuario;

@WebServlet("/listarUsuarios")
public class ListarUsuarios extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ListarUsuarios() { super(); }

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

        try {
            // Consumir el servicio web WSUsuario
        	Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
    		URL url_ws_usuarios = new URL(propiedades.getProperty("url_ws_usuarios"));
            WSUsuarioService service = new WSUsuarioService(url_ws_usuarios);
            WSUsuario portUsuario = service.getWSUsuarioPort();

            // Obtener todos los usuarios
            SetOfDTUsuario usuariosSet = portUsuario.listarUsuarios();
            Set<DtUsuario> usuarios = new HashSet<>(usuariosSet.getItem());

            // Obtener usuario logueado para verificar estado de seguimiento
            HttpSession sesion = request.getSession(false);
            String usuarioLogueado = null;
            if (sesion != null) {
                usuarioLogueado = (String) sesion.getAttribute("usuarioLogueado");
            }

            // Crear mapa con estado de seguimiento para cada usuario
            Map<String, Boolean> estadoSeguimiento = new HashMap<>();
            if (usuarioLogueado != null) {
                for (DtUsuario usuario : usuarios) {
                    if (!usuario.getNickname().equals(usuarioLogueado)) {
                        try {
                            boolean sigue = portUsuario.verificarSiSigue(usuarioLogueado, usuario.getNickname());
                            estadoSeguimiento.put(usuario.getNickname(), sigue);
                        } catch (Exception e) {
                            // En caso de error, asumir que no sigue
                            estadoSeguimiento.put(usuario.getNickname(), false);
                        }
                    }
                }
            }
            
            // creamos mapa para las imágenes codificadas de cada usuario
            Map<String, String> imagenes_b64 = new HashMap<>();
            for (DtUsuario usuario : usuarios) {
            	byte[] img_bytes;
            	String img_b64;
        		img_bytes = portUsuario.getImagenUsuario(usuario.getNickname());
        		            	
            	if (img_bytes == null || img_bytes.length == 0) {
            		img_b64 = null;
            	} else {
            		img_b64 = Base64.getEncoder().encodeToString(img_bytes);
            	}
            	imagenes_b64.put(usuario.getNickname(), img_b64);
            	
            }
            request.setAttribute("imagenes_b64", imagenes_b64);

            // Pasar usuarios y estado de seguimiento al JSP
            request.setAttribute("usuarios", usuarios);
            request.setAttribute("estadoSeguimiento", estadoSeguimiento);
            request.setAttribute("usuarioLogueado", usuarioLogueado);

            // Forward al JSP
            request.getRequestDispatcher("/WEB-INF/ListarUsuarios/ListarUsuarios.jsp").forward(request, response);

        } catch (UsuarioNoExisteException_Exception usuarioNoExiste) {
            request.setAttribute("error", "Error al cargar los usuarios: " + usuarioNoExiste.getMessage());
            request.getRequestDispatcher("/WEB-INF/ListarUsuarios/ListarUsuarios.jsp").forward(request, response);
        } catch (ServletException servletException) {
            throw servletException;
        } catch (IOException ioException) {
            throw ioException;
        }
    }
    
    //
}
