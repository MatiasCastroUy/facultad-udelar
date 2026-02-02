<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>eventos.uy</title>
    <jsp:include page="/WEB-INF/template/head.jsp"/>
</head>
<body>
    <jsp:include page="/WEB-INF/template/header.jsp"/>
    
    <main>
        <div class="container-fluid">
            <div class="row">
               
                <jsp:include page="/WEB-INF/template/panel.jsp" />
             
                <div class="col-10 p-4">
                    <h2 class="fw-bold ms-2">Nuevo Evento</h2>

                    <%
                        String error = (String) request.getAttribute("error");
                        String mensaje = (String) request.getAttribute("mensaje");
                        Boolean limpiar = (Boolean) request.getAttribute("limpiarFormulario");
                        boolean exito = (limpiar != null && limpiar);

                        if (error != null) {
                    %>
                        <div class="alert alert-danger mt-2"><%= error %></div>
                    <%
                        }
                        if (mensaje != null) {
                    %>
                        <div class="alert alert-success mt-2"><%= mensaje %></div>
                    <%
                        }
                    %>

                    <!-- formulario -->
                    <form method="post" action="altaEvento" enctype="multipart/form-data">
                        
                        <div class="mb-3">
                            <label for="nombreEvento" class="form-label">Nombre del evento</label>
                            <input id="nombreEvento" name="nombre" type="text" class="form-control" 
                                   value="<%= !exito && request.getParameter("nombre") != null ? request.getParameter("nombre") : "" %>">
                        </div>
                       
                        <div class="mb-3">
                            <label for="descEvento" class="form-label">Descripción</label>
                            <textarea id="descEvento" name="desc" class="form-control" rows="4"><%= !exito && request.getParameter("desc") != null ? request.getParameter("desc") : "" %></textarea>
                        </div>
              
                        <div class="mb-3">
                            <label for="categoriasEvento" class="form-label">Categorías</label>
                            <select id="categoriasEvento" name="categorias" class="form-select" multiple>
                                <%
                                List<String> categoriasDisponibles = (List<String>) request.getAttribute("categoriasDisponibles");
                                    String[] seleccionadas = (!exito) ? request.getParameterValues("categorias") : null;
                                    if (categoriasDisponibles != null) {
                                        for (String cat : categoriasDisponibles) {
                                            boolean selected = false;
                                            if (seleccionadas != null) {
                                                for (String s : seleccionadas) {
                                                    if (s.equals(cat)) {
                                                        selected = true;
                                                        break;
                                                    }
                                                }
                                            }
                                %>
                                    <option value="<%= cat %>" <%= selected ? "selected" : "" %>><%= cat %></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                            <div class="form-text">Podés seleccionar varias categorías manteniendo presionado Ctrl.</div>
                        </div>
                       
                        <div class="mb-3">
                            <label for="siglaEvento" class="form-label">Sigla</label>
                            <input id="siglaEvento" name="sigla" type="text" class="form-control" 
                                   value="<%= !exito && request.getParameter("sigla") != null ? request.getParameter("sigla") : "" %>">
                        </div>

                        <!-- Imagen -->
                        <div class="mb-3">
                            <label for="imagenEvento" class="form-label">Imagen del evento (opcional)</label>
                            <input id="imagenEvento" name="imagen" type="file" class="form-control" accept="image/*" onchange="previewImage(event)">
                            <div class="form-text">Podés subir una imagen en formato JPG o PNG.</div>
                        </div>

                        <div class="mb-3">
                            <img id="preview" src="#" alt="Previsualización" class="img-fluid rounded shadow d-none" style="max-height:200px;">
                        </div>

                        <button class="btn btn-primary float-end m-2" type="submit">Crear evento</button>
                    </form>

                    <script>
                        function previewImage(event) {
                            const [file] = event.target.files;
                            const preview = document.getElementById("preview");
                            if (file) {
                                preview.src = URL.createObjectURL(file);
                                preview.classList.remove("d-none");
                            } else {
                                preview.src = "#";
                                preview.classList.add("d-none");
                            }
                        }

                        // Si hubo éxito, limpiar el formulario visualmente
                        <% if (exito) { %>
                        document.addEventListener("DOMContentLoaded", () => {
                            const form = document.querySelector("form");
                            if (form) form.reset();
                            const preview = document.getElementById("preview");
                            if (preview) {
                                preview.src = "#";
                                preview.classList.add("d-none");
                            }
                        });
                        <% } %>
                    </script>
                </div>
            </div>
        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
</body>
</html>
