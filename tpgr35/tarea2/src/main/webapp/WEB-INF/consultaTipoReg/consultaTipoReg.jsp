<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>         
<%@ page import="webservices.DtTipoRegistro" %>      
<%@ page import="webservices.DtEdicion" %>           
<%@ page import="java.time.format.DateTimeFormatter" %>

<!DOCTYPE html> 
<html> 
<head>
  <title>Tipo de Registro</title> 
  <jsp:include page="/WEB-INF/template/head.jsp" /> 
</head>
<body> 
<%

  String nomEvento = (String) request.getAttribute("nomEvento");
  DtEdicion ed = (DtEdicion) request.getAttribute("edicion");
  DtTipoRegistro tipoReg = (DtTipoRegistro) request.getAttribute("tiporeg");
		  

  String rolUsuario = (String)session.getAttribute("rol");      
  boolean esAsistente = (rolUsuario != null) && rolUsuario.equalsIgnoreCase("ASISTENTE"); 
        

%>

<jsp:include page="/WEB-INF/template/header.jsp"/> 
<div class="container-fluid"> 
  <div class="row"> 
    <jsp:include page="/WEB-INF/template/panel.jsp" /> 

    <div id="main" class="col-10 p-3"> 
      <div class="row"> 
        <div class="col-3 p-2"> 
       	<% if (ed.getImagen() != null) { %>
			<img src=<%= "recursos/img/" + ed.getImagen() %>>
			<% } %>
          <div class="row py-2"><h3><%= ed.getNombre() %></h3></div>
          <div class="row"><span><b>Fecha de inicio: </b><%= (String) ed.getFechaInicio() %></span></div> 
          <div class="row"><span><b>Fecha de fin: </b><%= (String) ed.getFechaFin() %></span></div> 
        </div>

        <div class="col-6 p-2"> 
          <div class="row"> 
            <h2 class="fw-bold ms-2"><small class="text-body-secondary">Tipo de registro</small></h2>
            <form id="form" action="registro-a-edicion" method="get"> 
              <div class="container py-2"> 
                <label for="nombreTipoReg" class="form-label">Nombre</label> 
                <input id="nombreTipoReg" type="text" class="form-control" disabled placeholder="<%= tipoReg.getNombre() %>"> 
              </div>
              <div class="container py-2"> 
                <label for="descTipoReg" class="form-label">Descripción</label> 
                <textarea id="descTipoReg" class="form-control" disabled placeholder="<%= tipoReg.getDescripcion() %>"></textarea> 
              </div>
              <div class="container py-2"> 
                <label for="cupoTipoReg" class="form-label">Cupos disponibles</label> 
                <input id="cupoTipoReg" class="form-control" type="number" min="1" inputmode="numeric" disabled 
                	placeholder="<%= tipoReg.getCupoDisp() %> de <%= tipoReg.getCupoMax() %>"> 
              </div>
              <div class="container py-2"> 
                <label for="costoTipoReg" class="form-label">Costo</label> 
                <input id="costoTipoReg" class="form-control" type="number" min="1" step="0.01" inputmode="numeric" disabled placeholder="<%= tipoReg.getCosto() %>"> 
              </div>
				
				<input type="hidden" name="evento" value="<%= nomEvento %>">
				<input type="hidden" name="edicion" value="<%= ed.getNombre() %>">
				<input type="hidden" name="tiporeg" value="<%= tipoReg.getNombre() %>">
              <button class="btn btn-secondary float-end m-2" formaction="consultaEdicionEvento">Volver</button> 

              <% if (esAsistente) { %> 
                <button class="btn btn-primary float-end m-2" > 
                  Registrarme a esta edición
                </button>
              <% } else { %> 
                <button type="submit" class="btn btn-primary float-end m-2" disabled>Registrarme a esta edición</button>
              <% } %>
            </form>
          </div>

          <div class="row float-end" <% if (esAsistente) { %> style="display:none;" <% } %> > 
            <small><i>Para registrarse a esta edición debe iniciar sesión como asistente</i></small>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
