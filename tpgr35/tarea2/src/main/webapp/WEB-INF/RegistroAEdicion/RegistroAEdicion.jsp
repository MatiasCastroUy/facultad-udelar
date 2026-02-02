<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="webservices.DtEdicion,webservices.DtTipoRegistro" %>

<%
  String ctx = request.getContextPath();

  String nomEvento = (String) request.getAttribute("nomEvento");
  DtEdicion ed     = (DtEdicion) request.getAttribute("edicion");
  DtTipoRegistro tr= (DtTipoRegistro) request.getAttribute("tiporeg");

  String imgEdicion = (String) request.getAttribute("imagen");
  String error      = (String) request.getAttribute("error");

  Boolean edFinAttr = (Boolean) request.getAttribute("edicionFinalizada");
  boolean edicionFinalizada = (edFinAttr != null) ? edFinAttr.booleanValue() : false;

  String fechaIni = (ed != null && ed.getFechaInicio() != null) ? (String) ed.getFechaInicio() : "-";
  String fechaFin = (ed != null && ed.getFechaFin()    != null) ? (String) ed.getFechaFin()    : "-";

  String rolUsuario  = (String) session.getAttribute("rol");
  boolean esAsistente = (rolUsuario != null) && rolUsuario.equalsIgnoreCase("ASISTENTE");

  String imagenEd_b64 = (String) request.getAttribute("imagenEd_b64");
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <title>Registro a Edición</title>
  <jsp:include page="/WEB-INF/template/head.jsp" />
  <style>
    .img-box { max-height: 420px; }
    .img-box img { max-height: 400px; width: auto; object-fit: contain; display:block; }
    .maxw { max-width: 1100px; }
  </style>
</head>
<body>
  <jsp:include page="/WEB-INF/template/header.jsp"/>

  <div class="container-fluid">
    <div class="row">
      <jsp:include page="/WEB-INF/template/panel.jsp" />

      <div id="main" class="col-12 col-md-9 p-3">
        <h2 class="mb-4">Registro - <%= (nomEvento == null || nomEvento.isEmpty()) ? "Evento" : nomEvento %></h2>

        <% if (error != null) { %>
          <div class="alert alert-danger" role="alert"><%= error %></div>
        <% } %>

        <% if (edicionFinalizada) { %>
          <div class="alert alert-warning">Esta edición ya finalizó. No es posible registrar nuevos asistentes.</div>
        <% } %>

        <div class="card mb-3 maxw">
          <div class="card-body d-flex justify-content-center align-items-center img-box">
            <% if (imagenEd_b64 != null && !imagenEd_b64.isEmpty()) { %>
              <img src="data:image/jpeg;base64,<%= imagenEd_b64 %>" alt="Imagen de la edición" class="img-fluid">
            <% } else { %>
              <span class="text-muted">Sin imagen</span>
            <% } %>
          </div>
        </div>

        <div class="card mb-4 maxw">
          <div class="card-body">
            <h5 class="card-title mb-2"><%= (ed != null ? ed.getNombre() : "Edición") %></h5>

            <div class="row g-2 mb-3">
              <div class="col-12 col-md-6">
                <label class="form-label mb-1">Fecha de inicio</label>
                <input class="form-control" type="text" value="<%= fechaIni %>" disabled>
              </div>
              <div class="col-12 col-md-6">
                <label class="form-label mb-1">Fecha de fin</label>
                <input class="form-control" type="text" value="<%= fechaFin %>" disabled>
              </div>
            </div>

            <form method="post" action="<%= ctx %>/registro-a-edicion">
              <div class="mb-3">
                <label for="tipoRegistro" class="form-label">Tipo de registro</label>
                <input id="tipoRegistro" class="form-control" type="text" disabled
                       placeholder="<%= (tr != null) ? (tr.getNombre() + " - $" + tr.getCosto()) : "Tipo de registro" %>">
                <input type="hidden" name="tipo" value="<%= (tr != null ? tr.getNombre() : "") %>">
              </div>

              <div class="mb-3"><b>Cupo disponible:</b> <%= (tr != null ? tr.getCupoDisp() : 0) %></div>

              <div class="form-check mb-4">
                <input class="form-check-input" type="checkbox" id="chkPatrocinio" disabled>
                <label class="form-check-label" for="chkPatrocinio">Con código de patrocinio</label>
              </div>

              <input type="hidden" name="evento"  value="<%= (nomEvento==null?"":nomEvento) %>">
              <input type="hidden" name="edicion" value="<%= (ed!=null?ed.getNombre():"") %>">
              <input type="hidden" name="tiporeg" value="<%= (tr!=null?tr.getNombre():"") %>">

              <button type="submit" class="btn btn-primary me-2"
                      <%= (edicionFinalizada || !esAsistente) ? "disabled" : "" %>>
                Confirmar registro
              </button>
              <a class="btn btn-secondary" href="<%= ctx %>/home">Volver</a>

              <% if (!esAsistente) { %>
                <div class="mt-2"><small><i>Para registrarse a esta edición debe iniciar sesión como asistente</i></small></div>
              <% } %>
            </form>
          </div>
        </div>

      </div>
    </div>
  </div>
</body>
</html>
