<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="logica.Fabrica" %>
<%@ page import="logica.IControladorEvento" %>
<!DOCTYPE html>
<html>
<head>
    <title>eventos.uy</title>
    <jsp:include page="/WEB-INF/template/head.jsp"/>
</head>

<body>
	<% 
	
	%>


    <jsp:include page="/WEB-INF/template/header.jsp"/>
	<main>
        <div class="container-fluid">
            <div class="row">
                <!-- Panel izquierdo (fijo) con acciones principales -->
				<jsp:include page="/WEB-INF/template/panel.jsp" />

                <!-- CONTENIDO ACÁ -->
            </div>
        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
</body>
</html>