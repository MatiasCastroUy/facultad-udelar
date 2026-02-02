<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="webservices.DtEdicion" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Alta de Tipo de Registro</title>
	<jsp:include page="/WEB-INF/template/head.jsp"/>
</head>
<body>

	<%
	
	String nomEvento = (String) request.getAttribute("nomEvento");
	DtEdicion ed = (DtEdicion)request.getAttribute("edicion");
	
	%>
	<jsp:include page="/WEB-INF/template/header.jsp"/> 
	<div class="container-fluid"> 
		<div class="row"> 
		    <jsp:include page="/WEB-INF/template/panel.jsp" /> 
		    <div id="main" class="col-10 p-3">
			
			<%	      
			Boolean tienePermisos = (Boolean)request.getAttribute("tienePermisos");
			if (!tienePermisos) {	
			%>
			<div class="alert alert-danger">Error: No tienes permisos para esta función</div>
				
			<%
			} else {
			%>
					                
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
					<h2 class="fw-bold ms-2">Nuevo tipo de registro</h2>
					<form id="formCrear" action="altaTipoReg" method="post">
						<div class="container py-2">
							<label for="nombreTipoReg" class="form-label">Nombre</label>
							<input name="nombreTipoReg" type="text" class="form-control">
						</div>
						<div class="container py-2">
							<label for="descTipoReg" class="form-label">Descripción</label>
							<textarea name="descTipoReg" class="form-control"></textarea>
						</div>
						<div class="container py-2">
							<label for="cupoTipoReg" class="form-label">Cupo máximo</label>
							<input name="cupoTipoReg" class="form-control" type="number" min="1" inputmode="numeric">
						</div>
						<div class="container py-2">
							<label for="costoTipoReg" class="form-label">Costo</label>
							<input name="costoTipoReg" class="form-control" type="number" min="1" step="0.01" inputmode="numeric">
						</div>
						<input type="hidden" name="evento" value="<%= nomEvento %>" >
						<input type="hidden" name="edicion" value="<%= ed.getNombre() %>" >
						<button class="btn btn-secondary float-end m-2" type="submit" formaction="consultaEdicionEvento" formmethod="get">Volver</button>
						<button class="btn btn-primary float-end m-2" type="submit">Crear tipo de registro</button>
					</form>
				</div>
				<div class="col-3"></div><div class="col-3"></div> <!-- para corregir posición de próximo div -->
				<%
				String estado = (String)request.getAttribute("estado");
				if (estado != null && estado.equalsIgnoreCase("exito")) {
					String nomTipoReg = (String)request.getAttribute("nomTipoReg");
				%>
				<div class="col-6">
					<form id="formConsultar" action="consultaTipoReg" method="get">
						<input type="hidden" name="evento" value="<%= nomEvento %>" >
						<input type="hidden" name="edicion" value="<%= ed.getNombre() %>" >
						<input type="hidden" name="tiporeg" value="<%= nomTipoReg %>" >
						<div class="alert alert-success">Tipo de registro creado con éxito. <b><button class="btn" type="submit">Ver</button></b></div>
					</form>
				</div>
				<%} else if (estado != null && estado.equalsIgnoreCase("error")) { %>
				
				<div class="col-6 alert alert-danger"><b>Error:</b> <%= request.getAttribute("error") %></div>
				
				<% }; %>
			</div>
			</div>
  		</div>
	</div>

	<% } %>

</body>
</html>