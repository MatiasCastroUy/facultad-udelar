<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="webservices.WSUsuarioService" %>
<%@ page import="webservices.DtUsuario" %>
<%@ page import="webservices.DtAsistente" %>
<%@ page import="webservices.DtOrganizador" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="webservices.WSUsuario" %>
<!DOCTYPE html>
<html>
<head>
    <title>Modificar Usuario - eventos.uy</title>
    <jsp:include page="/WEB-INF/template/head.jsp"/>
</head>
<body>
    <jsp:include page="/WEB-INF/template/header.jsp"/>
    
    <main>
        <div class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-12 col-md-8 col-lg-6">
                    <div class="card shadow">
                        <div class="card-body">
                            <%
                            // Obtener los datos del usuario desde los atributos del request
                            DtUsuario usuario = (DtUsuario) request.getAttribute("usuario");
                            Boolean esAsistenteObj = (Boolean) request.getAttribute("esAsistente");
                            Boolean esOrganizadorObj = (Boolean) request.getAttribute("esOrganizador");
                            boolean esAsistente = (esAsistenteObj != null && esAsistenteObj.booleanValue());
                            boolean esOrganizador = (esOrganizadorObj != null && esOrganizadorObj.booleanValue());
                            
                            String error = (String) request.getAttribute("error");
                            String mensaje = (String) request.getAttribute("mensaje");
                            
                            if (error != null) {
                            %>
                                <div class="alert alert-danger" role="alert">
                                    <%= error %>
                                </div>
                                <div class="d-grid">
                                    <a href="<%= request.getContextPath() %>/home" class="btn btn-primary">Volver al inicio</a>
                                </div>
                            <%
                            } else if (mensaje != null) {
                            %>
                                <div class="alert alert-success" role="alert">
                                    <%= mensaje %>
                                </div>
                                <div class="d-grid gap-2">
                                    <a href="<%= request.getContextPath() %>/consultaUsr?nickname=<%= usuario.getNickname() %>" 
                                       class="btn btn-primary">Ver perfil</a>
                                    <a href="<%= request.getContextPath() %>/home" class="btn btn-secondary">Volver al inicio</a>
                                </div>
                            <%
                            } else if (usuario != null) {
                                // Construir ruta de imagen
                                String imagenPerfil = usuario.getImagenPerfil();
                                String imagenRuta;
                                if (imagenPerfil != null && !imagenPerfil.trim().isEmpty() && !imagenPerfil.equals("-")) {
                                    if (imagenPerfil.startsWith("recursos/")) {
                                        imagenRuta = imagenPerfil;
                                    } else {
                                        imagenRuta = "recursos/img/" + imagenPerfil;
                                    }
                                } else {
                                    imagenRuta = "recursos/placeholderNoImagen.png";
                                }
                                
                                String tipoUsuario = esAsistente ? "Asistente" : "Organizador";
                            %>
                            
                            <h2 class="mb-4 text-center">Modificar Perfil <%= esAsistente ? "Asistente" : "Organizador" %></h2>
                            
                            <form method="post" enctype="multipart/form-data">
                                <div class="mb-3 text-center">
                                    <img src="<%= request.getContextPath() %>/<%= imagenRuta %>" 
                                         id="fotoPerfil" 
                                         class="rounded-circle border mb-2" 
                                         alt="Foto de perfil" 
                                         style="width: 120px; height: 120px; object-fit: cover;"
                                         onerror="this.src='<%= request.getContextPath() %>/recursos/placeholderNoImagen.png'">
                                    <div>
                                        <label for="fotoInput" class="form-label">Cambiar foto de perfil</label>
                                        <input class="form-control" type="file" id="fotoInput" name="fotoInput" accept="image/*">
                                        <div class="form-text">Seleccione una nueva imagen (JPG, PNG, GIF) o deje vacío para mantener la actual</div>
                                    </div>
                                </div>
                                
                                <!-- Campos no editables -->
                                <div class="mb-3">
                                    <label class="form-label">Nickname</label>
                                    <input type="text" class="form-control" value="<%= usuario.getNickname() %>" disabled>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Correo electrónico</label>
                                    <input type="email" class="form-control" value="<%= usuario.getCorreo() %>" disabled>
                                </div>
                                
                                <!-- Campos editables comunes -->
                                <div class="mb-3">
                                    <label for="nombre" class="form-label">Nombre</label>
                                    <input type="text" class="form-control" id="nombre" name="nombre" 
                                           value="<%= usuario.getNombre() %>">
                                </div>
                                
                                <% if (esAsistente) { 
                                    DtAsistente asistente = (DtAsistente) usuario;
                                %>
                                    <!-- Campos específicos de Asistente -->
                                    <div class="mb-3">
                                        <label for="apellido" class="form-label">Apellido</label>
                                        <input type="text" class="form-control" id="apellido" name="apellido" 
                                               value="<%= asistente.getApellido() %>">
                                    </div>
                                    <div class="mb-3">
                                        <label for="fechaNacimiento" class="form-label">Fecha de nacimiento</label>
                                        <input type="date" class="form-control" id="fechaNacimiento" name="fechaNacimiento" 
                                               value="<%= asistente.getFechaNacimiento().toString() %>">
                                    </div>
                                <% } else if (esOrganizador) { 
                                    DtOrganizador organizador = (DtOrganizador) usuario;
                                %>
                                    <!-- Campos específicos de Organizador -->
                                    <div class="mb-3">
                                        <label for="descripcion" class="form-label">Descripción</label>
                                        <textarea class="form-control" id="descripcion" name="descripcion" rows="2"><%= organizador.getDescripcion() != null ? organizador.getDescripcion() : "" %></textarea>
                                    </div>
                                    <div class="mb-3">
                                        <label for="sitioWeb" class="form-label">Sitio web</label>
                                        <input type="url" class="form-control" id="sitioWeb" name="sitioWeb" 
                                               value="<%= organizador.getSitioWeb() != null ? organizador.getSitioWeb() : "" %>">
                                    </div>
                                <% } %>
                                
                                <hr>
                                
                                <div class="mb-3">
                                    <label for="imagenPerfil" class="form-label">URL de imagen de perfil</label>
                                    <input type="text" class="form-control" id="imagenPerfil" name="imagenPerfil" 
                                           value="<%= usuario.getImagenPerfil() != null ? usuario.getImagenPerfil() : "" %>"
                                           placeholder="URL de la imagen de perfil">
                                    <div class="form-text">Puede dejar vacío para mantener la imagen actual</div>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="password" class="form-label">Nueva contraseña</label>
                                    <input type="password" class="form-control" id="password" name="password" 
                                           placeholder="Nueva contraseña (dejar vacío si no desea cambiar)">
                                    <div class="form-text">Debe tener al menos 8 caracteres</div>
                                </div>
                                <div class="mb-3">
                                    <label for="confirmPassword" class="form-label">Confirmar nueva contraseña</label>
                                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" 
                                           placeholder="Confirmar nueva contraseña">
                                </div>
                                
                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-primary">Guardar cambios</button>
                                    <a href="<%= request.getContextPath() %>/consultaUsuario?nickname=<%= usuario.getNickname() %>" 
                                       class="btn btn-secondary">Cancelar</a>
                                </div>
                            </form>
                            
                            <%
                            } else {
                            %>
                                <div class="alert alert-warning" role="alert">
                                    No se pudieron cargar los datos del usuario.
                                </div>
                                <div class="d-grid">
                                    <a href="<%= request.getContextPath() %>/home" class="btn btn-primary">Volver al inicio</a>
                                </div>
                            <%
                            }
                            %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Scripts para preview de imagen y validación -->
    <script>
        // Preview de la imagen seleccionada
        document.getElementById('fotoInput').addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    document.getElementById('fotoPerfil').src = e.target.result;
                };
                reader.readAsDataURL(file);
            }
        });
        
        // Validación de contraseñas
        document.getElementById('confirmPassword').addEventListener('input', function() {
            const password = document.getElementById('password').value;
            const confirmPassword = this.value;
            
            if (password && confirmPassword && password !== confirmPassword) {
                this.setCustomValidity('Las contraseñas no coinciden');
            } else {
                this.setCustomValidity('');
            }
        });
    </script>
</body>
</html>
