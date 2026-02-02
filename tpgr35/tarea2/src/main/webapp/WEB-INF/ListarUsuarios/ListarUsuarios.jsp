<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="webservices.DtUsuario" %>
<%@ page import="webservices.SetOfDTUsuario" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <title>Usuarios registrados - eventos.uy</title>
    <jsp:include page="/WEB-INF/template/head.jsp"/>
</head>
<body>
    <jsp:include page="/WEB-INF/template/header.jsp"/>
    
    <main>
        <div class="container-fluid">
            <div class="row">
                <!-- Panel izquierdo -->
                <jsp:include page="/WEB-INF/template/panel.jsp" />

                <!-- Contenido principal -->
                <div id="main" class="col-12 col-md-9 p-3">
                    <h3>Usuarios registrados</h3>
                    
                    <!-- Mostrar mensaje de error si existe -->
                    <% String error = (String) request.getAttribute("error"); %>
                    <% if (error != null) { %>
                        <div class="alert alert-danger" role="alert">
                            <%= error %>
                        </div>
                    <% } %>
                    
                    <div class="row g-4 justify-content-center">
                        <%
                            Set<DtUsuario> usuarios = (Set<DtUsuario>) request.getAttribute("usuarios");
                            Map<String, Boolean> estadoSeguimiento = (Map<String, Boolean>) request.getAttribute("estadoSeguimiento");
                            String usuarioLogueado = (String) request.getAttribute("usuarioLogueado");
                            request.setAttribute("usuarios", usuarios);
                            Map<String, String> imagenes_b64 = (Map<String, String>) request.getAttribute("imagenes_b64");
                        
                        if (usuarios != null && !usuarios.isEmpty()) {
                            for (DtUsuario usuario : usuarios) {
                                // Determinar tipo de usuario y colores
                                String tipoUsuario = "Usuario";
                                String colorClase = "secondary";
                                String borderColor = "border-secondary";
                                
                                if (usuario instanceof webservices.DtAsistente) {
                                    tipoUsuario = "Asistente";
                                    colorClase = "primary";
                                    borderColor = "border-primary";
                                } else if (usuario instanceof webservices.DtOrganizador) {
                                    tipoUsuario = "Organizador";
                                    colorClase = "success";
                                    borderColor = "border-success";
                                }
                                
                                
                                // Verificar estado de seguimiento
                                boolean esElMismoUsuario = usuarioLogueado != null && usuarioLogueado.equals(usuario.getNickname());
                                boolean sigueAlUsuario = estadoSeguimiento != null && estadoSeguimiento.containsKey(usuario.getNickname()) 
                                                        && estadoSeguimiento.get(usuario.getNickname());
                                
                                // obtener imagen de perfil
                                String imagen_b64 = imagenes_b64.get(usuario.getNickname());
                        %>
                        <div class="col-12 col-md-6 d-flex justify-content-center">
                            <div class="card h-100 <%= borderColor %>" style="max-width: 420px; min-width: 260px;">
                                <div class="card-header bg-<%= colorClase %> text-white"><%= tipoUsuario %></div>
                                <img src="data:image/jpeg;base64,<%= imagen_b64 %>" 
                                     class="card-img-top" 
                                     alt="<%= tipoUsuario %>" 
                                     style="height: 200px; object-fit: cover;"
                                     onerror="this.src='<%= request.getContextPath() %>/recursos/placeholderNoImagen.png'">
                                <div class="card-body">
                                    <h5 class="card-title">Nick: <%= usuario.getNickname() %></h5>
                                    <p class="card-text mb-1"><b>Correo:</b> <%= usuario.getCorreo() %></p>
                                    <a href="<%= request.getContextPath() %>/consultaUsuario?nickname=<%= usuario.getNickname() %>" 
                                       class="btn btn-outline-<%= colorClase %> mt-2">Ver perfil</a>
                                    
                                    <!-- Botón Seguir/Dejar de Seguir solo para usuarios logueados viendo otros perfile -->
                                    <% if (usuarioLogueado != null && !esElMismoUsuario) { %>
                                        <form method="post" action="<%= request.getContextPath() %>/seguirUsuario" style="display: inline; margin-top: 8px;">
                                            <input type="hidden" name="usuarioSeguido" value="<%= usuario.getNickname() %>" />
                                            <input type="hidden" name="redirectUrl" value="<%= request.getContextPath() %>/listarUsuarios" />
                                            
                                            <% if (sigueAlUsuario) { %>
                                                <input type="hidden" name="accion" value="dejar_seguir" />
                                                <button type="submit" class="btn btn-outline-danger btn-sm w-100 mt-1">
                                                    <i class="fas fa-user-minus"></i> Dejar de Seguir
                                                </button>
                                            <% } else { %>
                                                <input type="hidden" name="accion" value="seguir" />
                                                <button type="submit" class="btn btn-success btn-sm w-100 mt-1">
                                                    <i class="fas fa-user-plus"></i> Seguir
                                                </button>
                                            <% } %>
                                        </form>
                                    <% } %>
                                </div>
                            </div>
                        </div>
                        <%
                            }
                        } else {
                        %>
                        <div class="col-12">
                            <div class="alert alert-info text-center" role="alert">
                                <h4>No hay usuarios registrados</h4>
                                <p>Aún no se han registrado usuarios en la plataforma.</p>
                                <a href="<%= request.getContextPath() %>/usuario" class="btn btn-primary">Registrarse</a>
                            </div>
                        </div>
                        <%
                        }
                        %>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>