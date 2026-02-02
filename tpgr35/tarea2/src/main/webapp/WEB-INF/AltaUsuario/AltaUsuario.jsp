<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Registro de Usuario - eventos.uy</title>
    <jsp:include page="/WEB-INF/template/head.jsp"/>
</head>
<body>
    <jsp:include page="/WEB-INF/template/header.jsp"/>
    
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-12 col-md-8 col-lg-6">
                <div class="card shadow">
                    <div class="card-body">
                        <h2 class="mb-4 text-center">Registro de Usuario</h2>
                        
                        <!-- Mostrar mensajes de error o éxito -->
                        <% String error = (String) request.getAttribute("error"); %>
                        <% if (error != null) { %>
                            <div class="alert alert-danger" role="alert">
                                <%= error %>
                            </div>
                        <% } %>
                        
                        <% String exito = (String) request.getAttribute("exito"); %>
                        <% if (exito != null) { %>
                            <div class="alert alert-success" role="alert">
                                <%= exito %>
                            </div>
                        <% } %>
                        
                        <form method="post" action="<%= request.getContextPath() %>/usuario" enctype="multipart/form-data">
                            <div class="mb-3">
                                <label for="tipoUsuario" class="form-label">Tipo de usuario</label>
                                <select class="form-select" id="tipoUsuario" name="tipoUsuario" required>
                                    <option value="" selected disabled>Selecciona una opción</option>
                                    <option value="asistente">Asistente</option>
                                    <option value="organizador">Organizador</option>
                                </select>
                            </div>
                            
                            <div id="camposComunes">
                                <div class="mb-3">
                                    <label for="nickname" class="form-label">Nickname</label>
                                    <input type="text" class="form-control" id="nickname" name="nickname" required>
                                </div>
                                <div class="mb-3">
                                    <label for="correo" class="form-label">Correo electrónico</label>
                                    <input type="email" class="form-control" id="correo" name="correo" required>
                                </div>
                                <div class="mb-3">
                                    <label for="password" class="form-label">Contraseña</label>
                                    <input type="password" class="form-control" id="password" name="password" required>
                                </div>
                                <div class="mb-3">
                                    <label for="confirmPassword" class="form-label">Confirmar contraseña</label>
                                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                                </div>
                                <div class="mb-3">
                                    <label for="imagenPerfil" class="form-label">Imagen de perfil (opcional)</label>
                                    <input type="file" class="form-control" id="imagenPerfil" name="imagenPerfil" accept="image/*">
                                    <div class="form-text">Formatos permitidos: JPG, PNG, GIF. Tamaño máximo: 5MB</div>
                                </div>
                            </div>
                            
                            <div id="camposAsistente" style="display:none;">
                                <div class="mb-3">
                                    <label for="nombreAsis" class="form-label">Nombre</label>
                                    <input type="text" class="form-control" id="nombreAsis" name="nombreAsis">
                                </div>
                                <div class="mb-3">
                                    <label for="apellidoAsis" class="form-label">Apellido</label>
                                    <input type="text" class="form-control" id="apellidoAsis" name="apellidoAsis">
                                </div>
                                <div class="mb-3">
                                    <label for="fechaNacAsis" class="form-label">Fecha de nacimiento</label>
                                    <input type="date" class="form-control" id="fechaNacAsis" name="fechaNacAsis">
                                </div>
                            </div>
                            
                            <div id="camposOrganizador" style="display:none;">
                                <div class="mb-3">
                                    <label for="nombreOrga" class="form-label">Nombre</label>
                                    <input type="text" class="form-control" id="nombreOrga" name="nombreOrga">
                                </div>
                                <div class="mb-3">
                                    <label for="descripcionOrga" class="form-label">Descripción</label>
                                    <textarea class="form-control" id="descripcionOrga" name="descripcionOrga" rows="2"></textarea>
                                </div>
                                <div class="mb-3">
                                    <label for="sitioWebOrga" class="form-label">Sitio web</label>
                                    <input type="url" class="form-control" id="sitioWebOrga" name="sitioWebOrga">
                                </div>
                            </div>
                            
                            <div class="d-grid mt-4">
                                <button type="submit" class="btn btn-primary">Registrarse</button>
                            </div>
                            
                            <div class="text-center mt-3">
                                <a href="<%= request.getContextPath() %>/login" class="text-decoration-none">¿Ya tienes cuenta? Inicia sesión</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
    document.addEventListener('DOMContentLoaded', function() {
        const tipoUsuario = document.getElementById('tipoUsuario');
        const camposAsistente = document.getElementById('camposAsistente');
        const camposOrganizador = document.getElementById('camposOrganizador');
        
        tipoUsuario.addEventListener('change', function() {
            if (this.value === 'asistente') {
                camposAsistente.style.display = 'block';
                camposOrganizador.style.display = 'none';
                document.getElementById('nombreAsis').required = true;
                document.getElementById('apellidoAsis').required = true;
                document.getElementById('fechaNacAsis').required = true;
                document.getElementById('nombreOrga').required = false;
                document.getElementById('descripcionOrga').required = false;
            } else if (this.value === 'organizador') {
                camposAsistente.style.display = 'none';
                camposOrganizador.style.display = 'block';
                document.getElementById('nombreOrga').required = true;
                document.getElementById('descripcionOrga').required = true;
                document.getElementById('nombreAsis').required = false;
                document.getElementById('apellidoAsis').required = false;
                document.getElementById('fechaNacAsis').required = false;
            } else {
                camposAsistente.style.display = 'none';
                camposOrganizador.style.display = 'none';
            }
        });
        
        const imagenInput = document.getElementById('imagenPerfil');
        imagenInput.addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (file) {
                if (file.size > 5 * 1024 * 1024) {
                    alert('La imagen no puede exceder 5MB');
                    this.value = '';
                    return;
                }
                
                if (!file.type.match('image.*')) {
                    alert('Por favor seleccione un archivo de imagen válido');
                    this.value = '';
                    return;
                }

                const reader = new FileReader();
                reader.onload = function(e) {
                    // Crear elemento de vista previa si no existe
                    let preview = document.getElementById('imagenPreview');
                    if (!preview) {
                        preview = document.createElement('img');
                        preview.id = 'imagenPreview';
                        preview.style.maxWidth = '150px';
                        preview.style.maxHeight = '150px';
                        preview.style.marginTop = '10px';
                        preview.style.border = '1px solid #ddd';
                        preview.style.borderRadius = '5px';
                        imagenInput.parentNode.appendChild(preview);
                    }
                    preview.src = e.target.result;
                };
                reader.readAsDataURL(file);
            }
        });
    });
    </script>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
</body>
</html>