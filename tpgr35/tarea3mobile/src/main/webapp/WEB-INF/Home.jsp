<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="webservices.DtEvento" %>
<!DOCTYPE html>
<html>
<head>
    <title>eventos.uy</title>
    <jsp:include page="/WEB-INF/template/head.jsp"/>
</head>

<body>
	<% 
	List<DtEvento> listaEventos = (List<DtEvento>)request.getAttribute("listaEventos");
	Map<String, String> imagenes_b64 = (Map<String, String>) request.getAttribute("imagenes_b64");
	%>


    <jsp:include page="/WEB-INF/template/header.jsp"/>
	<main>
        <div class="container-fluid">
            <div class="row">
                <!-- Panel izquierdo (fijo) con acciones principales -->
				<jsp:include page="/WEB-INF/template/panel.jsp" />

                <!-- Div principal actualizable -->
				<div id="main" class="col-12 col-lg-9 p-3">
					<!-- Buscador -->
					<div class="d-flex align-items-center mb-4">
						<input type="text" class="form-control me-2" placeholder="Eventos, Ediciones" style="max-width: 350px;">
						<button class="btn btn-primary">Buscar</button>
					</div>
					
					<% if ((Boolean)request.getAttribute("filtrar")) { %>
					<div class="row"><h4>Categoría: <b><%= request.getAttribute("categoria") %></b></h4></div>
					<% } %>
					
					<!-- Lista de eventos -->
					<% if(listaEventos.isEmpty()) { %>
						<h5>No hay ningún evento.</h5>
					<% 
					} else for(DtEvento evento : listaEventos) { %>
					<div class="card mb-3" style="width: 100%; max-width: 700px;">
						<%
						String imagen_b64 = imagenes_b64.get(evento.getNombre());
						String src_imagen = (imagen_b64==null) ? "recursos/placeholder_evento.png" : "data:image/jpeg;base64," + imagen_b64;
						%>
					
						<img src="<%= src_imagen %>" class="card-img-top" alt="<%= evento.getNombre() %>" style="height: 220px; object-fit: cover;">
						<div class="card-body">
							<h5 class="card-title"><%= evento.getNombre() %></h5>
							<p class="card-text"><%= evento.getDescripcion() %></p>
							<a href="consultaEvento?nombre=<%= evento.getNombre() %>" class="btn btn-dark col-12">Ver ediciones</a>
						</div>
					</div>
					<% }; %>
					
					
                </div>
            </div>
        </div>
    </main>

</body>
</html>