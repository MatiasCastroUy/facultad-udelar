<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.URL" %>
<%@ page import="java.util.Properties" %>
<%@ page import="webservices.WSUsuario" %>
<%@ page import="webservices.WSUsuarioService" %>
<%@ page import="webservices.DtUsuario" %>
<%@ page import="webservices.UsuarioNoExisteException_Exception" %>
<%@ page import="java.util.Base64" %>
<!DOCTYPE html>
<header class="sticky-top" style="z-index:2;">
  <%
    String ctx  = request.getContextPath();
    String nick = (String) session.getAttribute("usuarioLogueado");
    boolean logged = (nick != null);
    
    // Variables para la imagen de perfil
    String imagenPerfil_b64 = null;
    if (logged) {
        try {
            // Usar web services en lugar del controlador local
            Properties propiedades = (Properties) application.getAttribute("propiedades");
            URL url_ws_usuarios = new URL(propiedades.getProperty("url_ws_usuarios"));
            WSUsuarioService service = new WSUsuarioService(url_ws_usuarios);
            WSUsuario portUsuario = service.getWSUsuarioPort();
            
            DtUsuario usuario = portUsuario.getUsuario(nick);
            imagenPerfil_b64 = Base64.getEncoder().encodeToString(portUsuario.getImagenUsuario(nick));
        } catch (UsuarioNoExisteException_Exception e) {
            // Si hay error, no mostrar imagen
            imagenPerfil_b64 = null;
        } catch (Exception e) {
            // Si hay cualquier otro error, no mostrar imagen
            imagenPerfil_b64 = null;
        }
    }

    // construir ?next= para volver donde estabas luego del login
    String uri  = request.getRequestURI();              // /tarea2/lo-que-sea
    String here = uri.substring(ctx.length());          // /lo-que-sea
    String qs   = request.getQueryString();             // foo=1&bar=2 o null
    String next = (qs == null) ? here : (here + "?" + qs);
    String loginUrl = ctx + "/login?next=" + java.net.URLEncoder.encode(next, "UTF-8");
  %>

  <style>
    .avatar-placeholder {
      width: 40px; height: 40px;
      border-radius: 50%;          
      background: #ddd;           
      display: inline-block;
      vertical-align: middle;
      margin-left: 8px;
      object-fit: cover;
    }
    .avatar-image {
      width: 40px; height: 40px;
      border-radius: 50%;          
      display: inline-block;
      vertical-align: middle;
      margin-left: 8px;
      object-fit: cover;
      border: 2px solid #fff;
    }
  </style>

  <nav class="navbar navbar-expand-lg navbar-light bg-dark p-2 justify-content-between" style="max-height:54px;">
    <span>
      <a class="navbar-brand text-white" href="<%= ctx %>/home"><b>eventos</b>.uy</a>
    </span>

    <span>
      <% if (!logged) { %>
        <!-- Estado "no logueado" (entrar directo al Home) -->
        <a href="<%= loginUrl %>" class="btn btn-primary">Iniciar sesión</a>
        <a href="<%= ctx %>/usuario" class="btn btn-secondary">Registrarse</a>
      <% } else { %>
        <!-- Estado "logueado" (después del login) -->
        <a href="<%= ctx %>/consultaUsuario?nickname=<%= nick %>" class="btn btn-link"><%= nick %></a>
        <a href="<%= ctx %>/Logout" class="btn btn-link text-primary">Cerrar sesión</a>
        <% 
        if (imagenPerfil_b64 != null && !imagenPerfil_b64.trim().isEmpty()) {
        %>
            <img src="data:image/jpeg;base64,<%= imagenPerfil_b64 %>" 
                 class="avatar-image" 
                 alt="Foto de perfil"
                 onerror="this.outerHTML='<span class=&quot;avatar-placeholder&quot;></span>'">
        <% } else { %>
            <span class="avatar-placeholder"></span>
        <% } %>
      <% } %>
    </span>
  </nav>
</header>
