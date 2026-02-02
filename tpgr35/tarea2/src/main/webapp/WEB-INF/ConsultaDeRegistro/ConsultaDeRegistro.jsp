<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="webservices.DtRegistro" %>

<%
  String ctx = request.getContextPath();

  // CAMBIO: ahora recibimos Base64 desde el servlet
  String imagenEd_b64 = (String) request.getAttribute("imagenEd_b64");
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <title>Detalle de Registro</title>
  <jsp:include page="/WEB-INF/template/head.jsp" />
  <style>
    .img-box { max-height: 420px; }
    .img-box img { max-height: 400px; width: auto; object-fit: contain; display:block; }
    .maxw { max-width: 1100px; }
  </style>
</head>
<body>
  <jsp:include page="/WEB-INF/template/header.jsp" />

  <main>
    <div class="container-fluid">
      <div class="row">
        <jsp:include page="/WEB-INF/template/panel.jsp" />

        <div id="main" class="col-12 col-md-9 p-3">
        
        <%   if (request.getAttribute("error") == null) {
               DtRegistro dtRegistro = (DtRegistro) request.getAttribute("dtRegistro");
        %>
        
          <h2 class="mb-4">Detalle de Registro</h2>

          <div class="card mb-3 maxw">
            <div class="card-body d-flex justify-content-center align-items-center img-box">
              <% if (imagenEd_b64 != null && !imagenEd_b64.isEmpty()) { %>
                <img src="data:image/jpeg;base64,<%= imagenEd_b64 %>" alt="Imagen de la edición" class="img-fluid">
              <% } else { %>
                <span class="text-muted">Sin imagen</span>
              <% } %>
            </div>
          </div>

          <div class="card mb-3 maxw">
            <div class="card-body p-4">
              <h4 class="card-title"><%= dtRegistro.getEvento() %></h4>
              <p class="card-text fs-5">
                <b>Edición:</b> <%= dtRegistro.getEdicion() %><br>
                <b>Fecha de Registro:</b> <%= (String) dtRegistro.getFecha() %><br>
                <b>Tipo de Registro:</b> <%= dtRegistro.getTipo() %><br>
                <b>Costo:</b> $<%= dtRegistro.getCosto() %>
              </p>
              <a href="<%= ctx %>/home" class="btn btn-secondary">Volver</a>
            </div>
          </div>
        <%
          } else {
        %>
          <div class="col-6 alert alert-danger"><b>Error:</b> <%= request.getAttribute("error") %></div>
        <% } %>
        </div>
      </div>
    </div>
  </main>
</body>
</html>
