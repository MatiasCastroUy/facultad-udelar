<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Set" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<%@ page import="webservices.DtEvento" %>
<%@ page import="webservices.DtEdicion" %>
<!DOCTYPE html>
<html>
<head>
    <title>Consulta de Evento</title>
    <jsp:include page="/WEB-INF/template/head.jsp" />
</head>

<body>
    <jsp:include page="/WEB-INF/template/header.jsp" />

    <main>
        <div class="container-fluid">
            <div class="row">
                
                <jsp:include page="/WEB-INF/template/panel.jsp" />

                <!-- pagina -->
                <div id="main" class="col-10 p-3">
                    <div class="card p-3 shadow-sm">
                        <div class="row">
                            <%
                                DtEvento evento = (DtEvento) request.getAttribute("evento");
                                String categoriasStr = (String) request.getAttribute("categoriasStr");
                                List<DtEdicion> listaEdiciones = (List<DtEdicion>) request.getAttribute("listaEdiciones");
                                Map<String, String> imagenesEdiciones_b64 = (Map<String, String>) request.getAttribute("imagenesEdiciones_b64");
                                Boolean esOrganizador = (Boolean) request.getAttribute("esOrganizador");
                                String imagenEvento_b64 = (String) request.getAttribute("imagenEvento_b64");
                                String img_src = imagenEvento_b64 != null ? 
                                		"data:image/jpeg;base64," + imagenEvento_b64 
                                		: "recursos/placeholder_evento.png";
                            %>

                          <!-- Imagen -->
<div class="col-md-3">
    <img src="<%= img_src %>" 
         class="img-fluid rounded shadow mb-3" 
         alt="<%= (evento != null) ? evento.getNombre() : "Evento" %>">

    <%
        Boolean finalizado = (Boolean) request.getAttribute("estaFinalizado");
        if (finalizado == null) finalizado = false;
    %>

    <% if (finalizado) { %>
        <div class="alert alert-danger text-center fw-bold mt-2">
            Evento finalizado
        </div>
    <% } else if (Boolean.TRUE.equals(esOrganizador)) { %>
        <form action="consultaEvento" method="post" class="mt-2">
            <input type="hidden" name="accion" value="finalizar">
            <input type="hidden" name="nombre" value="<%= evento.getNombre() %>">
            <button type="submit" class="btn btn-danger w-100">
                Finalizar evento
            </button>
        </form>
    <% } %>
</div>

                            <!-- Datos del evento -->
                            <div class="col-md-9">
                                <h2 class="fw-bold mb-3"><%= (evento != null) ? evento.getNombre() : "Sin nombre" %></h2>
                                <form>
                                    <div class="mb-3">
                                        <label class="form-label">Nombre</label>
                                        <input type="text" class="form-control" disabled value="<%= evento != null ? evento.getNombre() : "" %>">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Descripción</label>
                                        <textarea class="form-control" rows="3" disabled><%= evento != null ? evento.getDescripcion() : "" %></textarea>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Sigla</label>
                                        <input type="text" class="form-control" disabled value="<%= evento != null ? evento.getSigla() : "" %>">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Fecha Alta</label>
                                        <input type="text" class="form-control" disabled value="<%= evento != null ? evento.getFechaAlta() : "" %>">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Categorías</label>
                                        <input type="text" class="form-control" disabled
                                               value="<%= categoriasStr %>" >
                                               
                                    </div>
                                </form>

                                <!-- ediciones -->
                                <h4 class="mt-4 mb-3">Ediciones</h4>

                          <%

                          if (listaEdiciones != null && !listaEdiciones.isEmpty()) {
                          %>
                          <div class="row row-cols-1 row-cols-md-3 g-3">
                             <%
                             for (DtEdicion ed : listaEdiciones) {                       
                             %>
                           <div class="col">
                            <a href="consultaEdicionEvento?edicion=<%= ed.getNombre() %>&evento=<%= evento.getNombre() %>"
                            class="text-decoration-none text-dark">
                             <div class="card h-100 shadow-sm">
                              <img src="data:image/jpeg;base64,<%= imagenesEdiciones_b64.get(ed.getNombre()) %>" class="card-img-top img-fluid" alt="<%= ed.getNombre() %>">
                               <div class="card-body text-center">
                          <h5 class="card-title"><%= ed.getNombre() %></h5>
                               </div>
                            </div>
                           </a>
                          </div>
                   <%
                     }
                   %>
                      </div>
                   <%
                    } else {
                   %>
                    <div class="alert alert-secondary mt-3" role="alert">
                       Este evento aún no tiene ediciones registradas.
                    </div>
             <%
               }
             %>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"
            crossorigin="anonymous"></script>
</body>
</html>