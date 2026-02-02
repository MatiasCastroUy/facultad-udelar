<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<%
  String ctx  = request.getContextPath();
  String nick = (String) session.getAttribute("usuarioLogueado");
  boolean logged = (nick != null);

  String rol = (String) session.getAttribute("rol");
  boolean esOrganizador = "ORGANIZADOR".equals(rol);
  boolean esAsistente   = "ASISTENTE".equals(rol);

  // Para construir "next" si lo necesitás en otros lugares
  String uri  = request.getRequestURI();
  String here = uri.substring(ctx.length());
  String qs   = request.getQueryString();
  String next = (qs == null) ? here : (here + "?" + qs);

  // Categorías desde sesión
  List<String> categorias = null;
  try {
    categorias = (List<String>) session.getAttribute("categorias");
  } catch (Exception ignore) {}
%>

<div class="col-2"></div>
<div id="panel" class="fixed-top col-2 px-2 bg-light" style="height:100vh; padding-top:60px; z-index:1;">
  <h5 class="p-1">Mi perfil</h5>
  <div class="container">
    <% if (!logged) { %>
      <i>Debe iniciar sesión para acceder a su perfil</i>
    <% } else if (esOrganizador) { %>
      <div class="row p-2">
        <a href="<%= ctx %>/altaEvento" class="link-dark link-underline-opacity-25">Alta Evento</a>
      </div>
      <div class="row p-2">
        <a href="<%= ctx %>/altaEdicion" class="link-dark link-underline-opacity-25">Alta Edición</a>
      </div>
    <% } else if (esAsistente) { %>
      <div class="row p-2">
        <a href="<%= ctx %>/registroAEdicionPanel" class="link-dark link-underline-opacity-25">
          Registro a Edición
        </a>
      </div>
      <div class="row p-2">
        <a href="<%= ctx %>/consultaUsuario?nickname=<%= java.net.URLEncoder.encode(nick, "UTF-8") %>"
           class="link-dark link-underline-opacity-25">
          Consulta de Registro
        </a>
      </div>
    <% } %>
  </div>

  <h5>Categorías</h5>
  <div id="categorias" class="container">
    <% if (categorias != null && !categorias.isEmpty()) { %>
      <% for (String cat : categorias) { %>
        <form id="formFiltrarCat_<%= cat %>" action="home" method="get">
          <input type="hidden" name="categoria" value="<%= cat %>">
          <div class="row p-2">
            <button type="submit"
                    class="btn btn-link py-0 text-start link-dark link-underline-opacity-25">
              <%= cat %>
            </button>
          </div>
        </form>
      <% } %>
    <% } else { %>
      <i>No hay ninguna categoría</i>
    <% } %>
  </div>

  <h5 class="mt-4">Usuarios</h5>
  <div id="usuarios_section" class="container">
    <div class="row p-2">
      <a href="<%= ctx %>/listarUsuarios" class="link-dark link-underline-opacity-25">Ver usuarios</a>
    </div>
  </div>
</div>
