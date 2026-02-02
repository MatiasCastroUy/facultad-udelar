<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="webservices.DtRegistro" %>

<%
  String ctx = request.getContextPath();

  // CAMBIO: ahora recibimos la imagen en Base64 (como en Home)
  String imgEdicion_b64 = (String) request.getAttribute("imagenEdicion_b64");
  // (se mantiene "imagen" por compatibilidad, pero ya no se usa para renderizar)
  String imgEdicion = (String) request.getAttribute("imagen"); // compat

  DtRegistro dt     = (DtRegistro) request.getAttribute("dtRegistro");

  String error = (String) request.getAttribute("error");
  String okMsg = (String) request.getAttribute("ok");
  String warn  = (String) request.getAttribute("warn");

  String evento  = (String) request.getAttribute("evento");
  String edicion = (String) request.getAttribute("edicion");

  Boolean edComAttr = (Boolean) request.getAttribute("edicionComenzada");
  boolean edicionComenzada = (edComAttr != null) ? edComAttr.booleanValue() : false;
%>
<!doctype html>
<html lang="es">
<head>
  <title>Detalle de registro</title>
  <jsp:include page="/WEB-INF/template/head.jsp" />
  <style>
    .img-box { max-height: 360px; }
    .img-box img { max-height: 340px; width: auto; object-fit: contain; display:block; }
    .maxw { max-width: 900px; }
  </style>
</head>
<body>
  <jsp:include page="/WEB-INF/template/header.jsp" />

  <main class="container-fluid py-3">
    <div class="row">
      <jsp:include page="/WEB-INF/template/panel.jsp" />

      <div id="main" class="col-12 col-md-9">
        <h2 class="h5 mb-3">Detalle de registro</h2>

        <% if (okMsg != null) { %>
          <div class="alert alert-success" role="alert"><%= okMsg %></div>
        <% } %>
        <% if (warn != null) { %>
          <div class="alert alert-warning" role="alert"><%= warn %></div>
        <% } %>
        <% if (error != null) { %>
          <div class="alert alert-danger" role="alert"><%= error %></div>
        <% } %>

        <% if (error == null && dt != null) { %>

        <div class="card mb-3 maxw">
          <div class="card-body d-flex justify-content-center align-items-center img-box">
            <% if (imgEdicion_b64 != null && !imgEdicion_b64.isEmpty()) { %>
              <img src="data:image/jpeg;base64,<%= imgEdicion_b64 %>" alt="Imagen de la edición" class="img-fluid">
            <% } else { %>
              <img src="<%= ctx %>/recursos/placeholder_evento.png" alt="Imagen de la edición" class="img-fluid">
            <% } %>
          </div>
        </div>

        <div class="card maxw">
          <div class="card-body">
            <h4 class="card-title"><%= dt.getEvento() %></h4>
            <p class="card-text fs-6">
              <b>Edición:</b> <%= dt.getEdicion() %><br>
              <b>Fecha de Registro:</b> <%= (String) dt.getFecha() %><br>
              <b>Tipo de Registro:</b> <%= dt.getTipo() %><br>
              <b>Costo:</b> $<%= dt.getCosto() %><br>
              <b>Asistencia:</b> <%= (dt.isAsistio() ? "Sí ✅" : "No ❌") %>
            </p>

            <% if (!edicionComenzada && !dt.isAsistio()) { %>
              <div class="alert alert-warning py-2">
                El evento debe comenzar para confirmar la asistencia.
              </div>
            <% } %>

            <div class="d-grid gap-2">
              <% if (!dt.isAsistio()) { %>
                <form method="post" action="<%= ctx %>/detalleReg" class="m-0">
                  <input type="hidden" name="op" value="confirmarAsistencia"/>
                  <input type="hidden" name="evento"  value="<%= evento == null ? "" : evento %>"/>
                  <input type="hidden" name="edicion" value="<%= edicion == null ? "" : edicion %>"/>
                  <button type="submit" class="btn btn-dark w-100"
                          <%= (!edicionComenzada) ? "disabled" : "" %>>
                    Confirmar asistencia
                  </button>
                </form>
              <% } else { %>
                <a class="btn btn-danger w-100"
                   href="<%= ctx %>/descargarConstancia?evento=<%= evento %>&edicion=<%= edicion %>">
                  Descargar constancia (PDF)
                </a>
              <% } %>

              <a href="<%= ctx %>/listaReg" class="btn btn-secondary w-100">Volver</a>
            </div>
          </div>
        </div>

        <% } else if (error == null) { %>
          <div class="alert alert-warning">No se encontró el registro solicitado.</div>
        <% } %>
      </div>
    </div>
  </main>

  <jsp:include page="/WEB-INF/template/scriptImport.jsp" />
</body>
</html>
