<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="webservices.DtRegistro" %>

<%
  String ctx = request.getContextPath();
  List<DtRegistro> registros = (List<DtRegistro>) request.getAttribute("registros");
  String error = (String) request.getAttribute("error");
%>
<!doctype html>
<html lang="es">
<head>
  <title>Consulta de registro</title>
  <jsp:include page="/WEB-INF/template/head.jsp" />
  <style>
    .card-img-top { max-height: 180px; object-fit: cover; }
    .muted { color:#6c757d; }
  </style>
</head>
<body>
  <jsp:include page="/WEB-INF/template/header.jsp" />

  <main class="container-fluid py-3">
    <div class="row">
      <jsp:include page="/WEB-INF/template/panel.jsp" />

      <div id="main" class="col-12 col-md-9">
        <h2 class="h5 mb-3">Mis registros</h2>

        <% if (error != null) { %>
          <div class="alert alert-danger"><%= error %></div>
        <% } %>

        <% if (registros == null || registros.isEmpty()) { %>
          <div class="alert alert-info">Aún no tenés registros.</div>
        <% } else { %>
        <div class="d-grid gap-3">
          <% for (DtRegistro r : registros) {
               String imgKeyB64 = "imgb64:" + r.getEvento() + "|" + r.getEdicion(); 
               String imagenB64 = (String) request.getAttribute(imgKeyB64);         
               boolean tieneImg = (imagenB64 != null && !imagenB64.trim().isEmpty());
          %>
          <div class="card shadow-sm">
            <% if (tieneImg) { %>
              <img class="card-img-top" src="data:image/jpeg;base64,<%= imagenB64 %>" alt="Imagen de la edición">
            <% } else { %>
              <div class="card-img-top d-flex align-items-center justify-content-center bg-secondary text-white" style="height:180px;">
                <span>Edición sin imagen</span>
              </div>
            <% } %>

            <div class="card-body">
              <h5 class="card-title mb-1"><%= r.getEvento() %></h5>
              <p class="card-text muted mb-2"><%= r.getEdicion() %></p>
              <p class="mb-3">
                <small><b>Fecha de registro:</b> <%= (String) r.getFecha() %></small><br>
                <small><b>Tipo:</b> <%= r.getTipo() %></small><br>
                <small><b>Asistencia:</b>
                  <%= (r.isAsistio() ? "Sí ✅" : "No ❌") %>
                </small>
              </p>

              <form action="<%= ctx %>/detalleReg" method="get" class="d-grid">
                <input type="hidden" name="edicion" value="<%= r.getEdicion() %>">
                <input type="hidden" name="evento"  value="<%= r.getEvento() %>">
                <input type="hidden" name="tiporeg" value="<%= r.getTipo() %>">
                <button class="btn btn-dark">Ver registro</button>
              </form>
            </div>
          </div>
          <% } %>
        </div>
        <% } %>
      </div>
    </div>
  </main>
  
  <jsp:include page="/WEB-INF/template/scriptImport.jsp" />
</body>
</html>
