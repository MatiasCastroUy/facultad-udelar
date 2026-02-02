<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
	<% if((Boolean)request.getAttribute("exito")){ %>
	<title>Datos cargados</title>
	<%} else { %>
	<title>Error al cargar datos</title>
	<%} %>
	<jsp:include page="/WEB-INF/template/head.jsp" />
</head>


<body>
	<jsp:include page="/WEB-INF/template/header.jsp"/>
	<div class="container-fluid">
		<div class="row">
			<!-- Panel fijo -->
			<jsp:include page="/WEB-INF/template/panel.jsp" />
			
			<!-- Sección principal -->
			
			<div id="main" class="col-10 p-3">
                <div class="row">
                	<h2 class="p-3">
                	<% if((Boolean)request.getAttribute("exito")){ %>
					Los datos se cargaron con éxito
					<%} else { %>
					Ocurrió un error al intentar cargar los datos
					<% } %>
					</h2>
					<p><a href="home" class="py-2"><i>Volver al inicio</i></a></p>
                </div>
            </div>
		</div>
	</div>
</body>
</html>