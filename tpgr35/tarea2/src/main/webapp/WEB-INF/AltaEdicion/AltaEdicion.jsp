<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>

<%@ page import="webservices.DtEvento" %>
<!DOCTYPE html>
<html>
<head>
    <title>Crear Edición - eventos.uy</title>
    <jsp:include page="/WEB-INF/template/head.jsp"/>
    <%
		String nombreEdicion = (String) request.getAttribute("edicionUrl");
		String nombreEvento = (String) request.getAttribute("eventoUrl");
		Map<String, String> imagenesEvento = (HashMap<String, String>) request.getAttribute("imagenes");
    	String url = "consultaEdicionEvento?edicion=" + nombreEdicion + "&evento=" + nombreEvento;
    	if(nombreEdicion != null && nombreEvento != null) {
    %>
    	<meta http-equiv="refresh" content="0;url=<%= url %>" />
    <% } %>
</head>

<body>	
	<jsp:include page="/WEB-INF/template/header.jsp"/>
	<main>
        <div class="container-fluid">
            <div class="row">
                <!-- Panel izquierdo (fijo) con acciones principales -->
				<jsp:include page="/WEB-INF/template/panel.jsp" />
                <div id="main" class="col-10 p-3">
	                <%
				    	String ctx = request.getContextPath();
				    	String error = (String) request.getAttribute("error");
				    	
						String rolUsuario = (String) session.getAttribute("rol");
						
						if (rolUsuario == null || !rolUsuario.equalsIgnoreCase("organizador")) {	
					%>
						<div class="alert alert-danger">Error: No tienes permisos para esta función</div>	
					<%
						} else {
					%>
					
                	<% if (error != null && !error.isBlank()) { %>
                		<div class="alert alert-danger" role="alert"><%= error %></div>
                	<% } %>
	                <div class="row">
	                	<div class="col-3 p-2">
							<div class="row" id="infoEvento">
								<div class="col">
			                		<img id="imgEvento" src="" style="width: 100%">
			                		<div class="row py-2"><h3><b id="nombreEventoPreview">Seleccione un Evento</b></h3></div>
								</div>				
							</div>
							<div class="row" id="infoEdicion">
								<div class="col">
									<hr />
									<img id="imgEdicion" src="recursos/placeholderNoImagen.png" style="width: 100%">
									<div class="row py-2"><h3 id="nombreEdicionPreview">Nueva Edición</h3></div>	
								</div>
							</div>
	                	</div>
	                	<div class="col-6 p-2">
	                		<h2 class="fw-bold ms-2">Nueva Edición de Evento</h2>
	                		<form action="<%= ctx %>/altaEdicion" method="POST" enctype="multipart/form-data">
								<div class="container py-2">
									<span style="color: red;">* </span><label for="evento" class="form-label">Evento</label>
									<select name="evento" id="evento" class="form-select" onchange="cambiarEvento(event)" required>
										<option value="">--Seleccionar--</option>
										<%
											List<DtEvento> listaEventos = (ArrayList<DtEvento>) request.getAttribute("eventos");
											for (DtEvento evt : listaEventos) {
												String selected = "";
												String evento = (String) request.getAttribute("evento");
												if (evento != null && evento.equals(evt.getNombre())) {
													selected = "selected";
												}
										%>
											<option value="<%= evt.getNombre() %>" <%= selected %>><%= evt.getNombre() %></option>
										<%
											}
										%>
									</select>
								</div>
								<hr />
	                			<div class="container py-2">
	                				<span style="color: red;">* </span><label for="nombreEdicion" class="form-label">Nombre de Edición</label>
	                				<input name="nombreEdicion" id="nombreEdicion" type="text" class="form-control" oninput="cambiarNombreEdicion(event)" value="${nombreEdicion}" required>
	                			</div>
								<div class="container py-2">
									<span style="color: red;">* </span><label for="siglas" class="form-label">Siglas</label>
									<input name="siglas" id="siglas" type="text" class="form-control" value="${siglas}" required>
								</div>
								<div class="container py-2">
									<span style="color: red;">* </span><label for="ciudad" class="form-label">Ciudad</label>
									<input name="ciudad" id="ciudad" type="text" class="form-control" value="${ciudad}" required>
								</div>
								<div class="container py-2">
									<span style="color: red;">* </span><label for="pais" class="form-label">País</label>
									<input name="pais" id="pais" type="text" class="form-control" value="${pais}" required>
								</div>
								<div class="container py-2">
									<span style="color: red;">* </span><label for="fechaIni" class="form-label">Fecha de Inicio</label>
									<input name="fechaIni" id="fechaIni" type="date" class="form-control" onchange="cambiarFechaInicio(event)" value="${fechaIni}" required>
								</div>
								<div class="container py-2">
									<span style="color: red;">* </span><label for="fechaFin" class="form-label">Fecha de Fin</label>
									<input name="fechaFin" id="fechaFin" type="date" class="form-control" onchange="cambiarFechaFin(event)" value="${fechaFin}">
								</div>
								<div class="container py-2">
									<label for="foto" class="form-label">Imagen (.png, .jpg, .jpeg)</label>
									<input name="foto" id="foto" type="file" accept="image/png, image/jpg, image/jpeg" onchange="mostrarImagen(this)" class="form-control">
								</div>
								<div class="container py-2">
									<label for="urlVideo" class="form-label">URL del Video</label>
									<input name="urlVideo" id="urlVideo" type="url" class="form-control" value="${urlVideo}">
								</div>
								<hr />
	                			<a href="<%= request.getContextPath() %>/home" class="btn btn-secondary float-end m-2">Volver</a>
	                			<button class="btn btn-primary float-end m-2" type="submit">Dar de alta edición</button>
	                		</form>
	                	</div>
	                </div>
		           	<%
						}
					%>
	            </div>
	        </div>
	    </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>

	<%
		if (rolUsuario != null && rolUsuario.equalsIgnoreCase("organizador")) {	
	%>
		<script>
			const imgMap = new Map();
			<%
				Map<String, String> mapaImagenes = (HashMap<String, String>) request.getAttribute("imagenes");
				for (String imagen : mapaImagenes.keySet()) {
					if (mapaImagenes.get(imagen) != null) {
						String src = "data:image/jpeg;base64," + mapaImagenes.get(imagen);
			%>
				imgMap.set("<%= imagen %>", "<%= src %>");
			<%
					}
				}
			%>
			
			
			function cambiarEvento(e) {
				document.getElementById("nombreEventoPreview").innerText = e.target.value !== "" ? e.target.value : "Seleccione un Evento";
				document.getElementById("imgEvento").src = e.target.value !== "" && imgMap.get(e.target.value) !== "" && imgMap.get(e.target.value) !== undefined ? imgMap.get(e.target.value) : "";
			}
			
			function cambiarNombreEdicion(e) {
				document.getElementById("nombreEdicionPreview").innerText = e.target.value !== "" ? e.target.value : "Nueva Edición";
			}
			
			function cambiarFechaInicio(e) {
				document.getElementById("fechaFin").setAttribute("min", e.target.value);
			}
			
			function cambiarFechaFin(e) {
				document.getElementById("fechaIni").setAttribute("max", e.target.value);
			}
			
			function mostrarImagen(input) {
				if (input.files && input.files[0]) {
				    var reader = new FileReader();
	
				    reader.onload = function (e) {
				      	document.getElementById("imgEdicion").src = e.target.result;
				    };
	
				    reader.readAsDataURL(input.files[0]);
				} else {
					document.getElementById("imgEdicion").src = "recursos/placeholderNoImagen.png";
				}
			}
			
			document.addEventListener("DOMContentLoaded", () => {
				<% if (request.getAttribute("evento") != null) { %>
					cambiarEvento({target: {value: '<%= (String) request.getAttribute("evento") %>'}});
				<% } %>
				
				<% if (request.getAttribute("nombreEdicion") != null) { %>
					cambiarNombreEdicion({target: {value: '<%= (String) request.getAttribute("nombreEdicion") %>'}});
				<% } %>
				
				<% if (request.getAttribute("fechaIni") != null) { %>
					cambiarFechaInicio({target: {value: '<%= (String) request.getAttribute("fechaIni") %>'}});
				<% } %>
				
				<% if (request.getAttribute("fechaFin") != null) { %>
					cambiarFechaFin({target: {value: '<%= (String) request.getAttribute("fechaFin") %>'}});
				<% } %>
			});
		</script>
	<%
		}
	%>
</body>
</html>