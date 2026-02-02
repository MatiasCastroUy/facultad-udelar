<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>

<%@ page import="webservices.DtEdicion" %>
<%@ page import="webservices.DtTipoRegistro" %>
<%@ page import="webservices.ListOfString" %>

<%
  String ctx = request.getContextPath();

  ListOfString eventosWS   = (ListOfString) request.getAttribute("eventos");
  ListOfString edicionesWS = (ListOfString) request.getAttribute("ediciones");
  ListOfString tiposWS     = (ListOfString) request.getAttribute("tipos");

  List<String> eventos   = (eventosWS   != null && eventosWS.getItem()   != null) ? eventosWS.getItem()   : Collections.emptyList();
  List<String> ediciones = (edicionesWS != null && edicionesWS.getItem() != null) ? edicionesWS.getItem() : Collections.emptyList();
  List<String> tipos     = (tiposWS     != null && tiposWS.getItem()     != null) ? tiposWS.getItem()     : Collections.emptyList();

  String eventoSel  = (String) request.getAttribute("eventoSel");
  String edicionSel = (String) request.getAttribute("edicionSel");
  String tipoSel    = (String) request.getAttribute("tipoSel");

  DtEdicion      dtEd   = (DtEdicion)      request.getAttribute("dtEd");
  DtTipoRegistro dtTipo = (DtTipoRegistro) request.getAttribute("dtTipo");

  String fechaIniStr = (dtEd != null && dtEd.getFechaInicio() != null) ? dtEd.getFechaInicio() : "-";
  String fechaFinStr = (dtEd != null && dtEd.getFechaFin()    != null) ? dtEd.getFechaFin()    : "-";

  String imgEdicion = (dtEd != null && dtEd.getImagen() != null) ? dtEd.getImagen() : "";
  String error = (String) request.getAttribute("error");

  Boolean edicionFinalizada = (Boolean) request.getAttribute("edicionFinalizada");
  boolean bloqueada = (edicionFinalizada != null && edicionFinalizada);

  boolean puedeConfirmar = (eventoSel != null && !eventoSel.isEmpty()
                          && edicionSel != null && !edicionSel.isEmpty()
                          && tipoSel    != null && !tipoSel.isEmpty()
                          && !bloqueada);
%>

<!DOCTYPE html>
<html lang="es">
<head>
  <title>Registro a Edición de Evento</title>
  <jsp:include page="/WEB-INF/template/head.jsp" />
  <style>.maxw { max-width: 960px; }</style>
</head>
<body>
  <jsp:include page="/WEB-INF/template/header.jsp" />
  <div class="container-fluid">
    <div class="row">
      <jsp:include page="/WEB-INF/template/panel.jsp" />

      <div id="main" class="col-12 col-md-9 p-3">
        <h2 class="mb-4">Registro a Edición de Evento</h2>

        <% if (error != null) { %>
          <div class="alert alert-warning"><%= error %></div>
        <% } %>

        <% if (bloqueada) { %>
          <div class="alert alert-warning">Esta edición ya finalizó. No puede registrarse.</div>
        <% } %>

        <form method="get" action="<%= ctx %>/registroAEdicionPanel" class="maxw mb-3">
          <label class="form-label">Seleccione un Evento</label>
          <select name="evento" class="form-select" onchange="this.form.submit()">
            <option value="">-- Elegí un evento --</option>
            <% for (String ev : eventos) { %>
              <option value="<%= ev %>" <%= ev.equals(eventoSel) ? "selected" : "" %>><%= ev %></option>
            <% } %>
          </select>
        </form>

        <form method="get" action="<%= ctx %>/registroAEdicionPanel" class="maxw mb-3">
          <input type="hidden" name="evento" value="<%= eventoSel == null ? "" : eventoSel %>">
          <label class="form-label">Seleccione una Edición</label>
          <select name="edicion" class="form-select" onchange="this.form.submit()" <%= (eventoSel==null||eventoSel.isEmpty()) ? "disabled" : "" %>>
            <option value="">-- Elegí una edición --</option>
            <% for (String ed : ediciones) { %>
              <option value="<%= ed %>" <%= ed.equals(edicionSel) ? "selected" : "" %>><%= ed %></option>
            <% } %>
          </select>
        </form>

        <div class="row g-2 mb-3 maxw">
          <div class="col-12 col-md-6">
            <label class="form-label mb-1">Fecha inicio</label>
            <input class="form-control" type="text" value="<%= fechaIniStr %>" disabled>
          </div>
          <div class="col-12 col-md-6">
            <label class="form-label mb-1">Fecha fin</label>
            <input class="form-control" type="text" value="<%= fechaFinStr %>" disabled>
          </div>
        </div>

        <form method="get" action="<%= ctx %>/registroAEdicionPanel" class="maxw mb-4">
          <input type="hidden" name="evento"  value="<%= eventoSel  == null ? "" : eventoSel  %>">
          <input type="hidden" name="edicion" value="<%= edicionSel == null ? "" : edicionSel %>">
          <label class="form-label">Tipo de Registro</label>
          <select name="tiporeg" class="form-select" onchange="this.form.submit()" <%= (edicionSel==null||edicionSel.isEmpty()) ? "disabled" : "" %>>
            <option value="">-- Elegí un tipo --</option>
            <% for (String tr : tipos) { %>
              <option value="<%= tr %>" <%= tr.equals(tipoSel) ? "selected" : "" %>><%= tr %></option>
            <% } %>
          </select>
        </form>

        <%
          String costoLinea = "Costo: -";
          if (dtTipo != null) {
            Integer cupoDisp = dtTipo.getCupoDisp(); 
            Integer cupoMax  = dtTipo.getCupoMax();
            String cupoStr = (cupoDisp != null) ? ("  —  Cupo: " + cupoDisp + (cupoMax != null ? (" de " + cupoMax) : "")) : "";
            costoLinea = "Costo: $" + dtTipo.getCosto() + cupoStr;
          }
        %>
        <div class="maxw mb-3">
          <input class="form-control" type="text" disabled value="<%= costoLinea %>">
        </div>

        <!-- Confirmar (POST) -->
        <form method="post" action="<%= ctx %>/registroAEdicionPanel" class="maxw">
          <input type="hidden" name="evento"  value="<%= eventoSel  == null ? "" : eventoSel  %>">
          <input type="hidden" name="edicion" value="<%= edicionSel == null ? "" : edicionSel %>">
          <input type="hidden" name="tiporeg" value="<%= tipoSel    == null ? "" : tipoSel    %>">
          <input type="hidden" name="imagen"  value="<%= imgEdicion %>">
          <button type="submit" class="btn btn-primary" <%= puedeConfirmar ? "" : "disabled" %>>
            Confirmar registro
          </button>
          <a href="<%= ctx %>/home" class="btn btn-secondary ms-2">Volver</a>
        </form>

      </div>
    </div>
  </div>
</body>
</html>
