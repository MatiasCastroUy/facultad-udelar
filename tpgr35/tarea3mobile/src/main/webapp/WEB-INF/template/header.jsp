<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<header class="sticky-top" style="z-index:2;">

<%
    String imagenPerfil = null;
    String ctx  = request.getContextPath();
    String nick = (String) session.getAttribute("usuarioLogueado");
    boolean logged = (nick != null);
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
      cursor: pointer;
    }
  </style>

  <nav class="navbar navbar-expand-lg navbar-dark bg-dark p-2 justify-content-between" style="max-height:54px;">

    <button class="navbar-toggler d-lg-none" type="button" data-bs-toggle="offcanvas" data-bs-target="#panel" aria-controls="sidebar">
      <span class="navbar-toggler-icon"></span>
    </button>

    <span>
      <a class="navbar-brand text-white" href="<%= ctx %>/home"><b>eventos</b>.uy</a>
    </span>

    <% if (logged) { %>
    <div class="dropdown">
      <%
        boolean tieneImagen = (imagenPerfil != null && !imagenPerfil.trim().isEmpty() && !imagenPerfil.equals("-"));
        if (tieneImagen) {
            String imagenRuta;
            if (imagenPerfil.startsWith("recursos/")) {
                imagenRuta = imagenPerfil;
            } else {
                imagenRuta = "recursos/img/" + imagenPerfil;
            }
      %>
          <a href="#" class="d-inline-block" data-bs-toggle="dropdown" aria-expanded="false">
            <img src="<%= ctx %>/<%= imagenRuta %>"
                 class="avatar-image"
                 alt="Foto de perfil"
                 onerror="this.outerHTML='<span class=&quot;avatar-placeholder&quot;></span>'">
          </a>
      <%
        } else {
      %>
          <a href="#" class="d-inline-block" data-bs-toggle="dropdown" aria-expanded="false" aria-label="Abrir menú de usuario">
            <span class="avatar-placeholder"></span>
          </a>
      <%
        }
      %>
      <ul class="dropdown-menu dropdown-menu-end">
        <li>
          <a class="dropdown-item" href="<%= ctx %>/logout">Cerrar sesión</a>
        </li>
      </ul>
    </div>
    <% } %>
  </nav>
</header>
