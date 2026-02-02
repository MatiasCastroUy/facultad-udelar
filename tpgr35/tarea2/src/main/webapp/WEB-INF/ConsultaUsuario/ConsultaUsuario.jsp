<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="webservices.DtUsuario, webservices.DtAsistente, webservices.DtOrganizador" %>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <title>Consulta Usuario - Eventos.uy</title>
    <jsp:include page="/WEB-INF/template/head.jsp" />
</head>

<body>
    <jsp:include page="/WEB-INF/template/header.jsp" />

    <main>
        <div class="container-fluid">
            <div class="row">
                
                <jsp:include page="/WEB-INF/template/panel.jsp" />

                <div id="main" class="col-10 p-3">
                    <div class="card p-3 shadow-sm">
                        <% 
                        String error = (String) request.getAttribute("error");
                        if (error != null) {
                        %>
                            <div class="alert alert-danger" role="alert">
                                <%= error %>
                            </div>
                        <%
                        } else {
                            DtUsuario usuario = (DtUsuario) request.getAttribute("usuario");
                            Boolean esMismoPerfilObj = (Boolean) request.getAttribute("esMismoPerfil");
                            boolean esMismoPerfil = (esMismoPerfilObj != null && esMismoPerfilObj.booleanValue());
                            String nicknameConsultado = (String) request.getAttribute("nicknameConsultado");
                            Boolean esAsistenteObj = (Boolean) request.getAttribute("esAsistente");
                            Boolean esOrganizadorObj = (Boolean) request.getAttribute("esOrganizador");
                            boolean esAsistente = (esAsistenteObj != null && esAsistenteObj.booleanValue());
                            boolean esOrganizador = (esOrganizadorObj != null && esOrganizadorObj.booleanValue());
                            
                            if (usuario != null) {
                        %>
                        
                        <div class="row">
                            <div class="col-12">
                                <h2 class="mb-4">Datos del <%= esAsistente ? "Asistente" : "Organizador" %></h2>
                            </div>
                        </div>
                        
                        <div class="row mb-4">
                            <div class="col-md-4 d-flex justify-content-center align-items-center">
                                <% 
                                String imagen_b64 = (String) request.getAttribute("imagen_b64");
                                %>
                                <img src="data:image/jpeg;base64,<%= imagen_b64 %>" 
                                     class="rounded-circle border" 
                                     alt="Foto de perfil" 
                                     style="width: 160px; height: 160px; object-fit: cover;"
                                     onerror="this.src='<%= request.getContextPath() %>/recursos/placeholderNoImagen.png'">
                            </div>
                            <div class="col-md-8">
                                <% if (esAsistente) { 
                                    DtAsistente asistente = (DtAsistente) usuario;
                                %>
                                    <h3 class="mb-3">Datos del Asistente</h3>
                                    <ul class="list-group list-group-flush">
                                        <li class="list-group-item"><strong>Nickname:</strong> <%= usuario.getNickname() %></li>
                                        <li class="list-group-item"><strong>Nombre:</strong> <%= usuario.getNombre() %></li>
                                        <li class="list-group-item"><strong>Apellido:</strong> <%= asistente.getApellido() %></li>
                                        <li class="list-group-item"><strong>Correo:</strong> <%= usuario.getCorreo() %></li>
                                        <li class="list-group-item"><strong>Fecha de nacimiento:</strong> 
                                            <%= asistente.getFechaNacimiento() %>
                                        </li>
                                    </ul>
                                <% } else if (esOrganizador) { 
                                    DtOrganizador organizador = (DtOrganizador) usuario;
                                %>
                                    <h3 class="mb-3">Datos del Organizador</h3>
                                    <ul class="list-group list-group-flush">
                                        <li class="list-group-item"><strong>Nickname:</strong> <%= usuario.getNickname() %></li>
                                        <li class="list-group-item"><strong>Nombre:</strong> <%= usuario.getNombre() %></li>
                                        <li class="list-group-item"><strong>Correo:</strong> <%= usuario.getCorreo() %></li>
                                        <li class="list-group-item"><strong>Descripción:</strong> <%= organizador.getDescripcion() %></li>
                                        <li class="list-group-item"><strong>Sitio web:</strong> 
                                            <% if (organizador.getSitioWeb() != null && !organizador.getSitioWeb().trim().isEmpty()) { %>
                                                <a href="<%= organizador.getSitioWeb() %>" target="_blank"><%= organizador.getSitioWeb() %></a>
                                            <% } else { %>
                                                No especificado
                                            <% } %>
                                        </li>
                                    </ul>
                                <% } %>
                            </div>
                        </div>

                        <!-- CASO 1: ASISTENTE consultando SU PROPIO perfil-->
                        <% if (esAsistente && esMismoPerfil) { %>
                            <div class="row">
                                <div class="col-12">
                                    <h4 class="mb-3">Mis Registros a Ediciones de Eventos</h4>
                                    <%
                                    @SuppressWarnings("unchecked")
                                    List<Map<String, Object>> registrosConInfo = (List<Map<String, Object>>) request.getAttribute("registrosConInfo");
                                    %>
                                    
                                    <% if (registrosConInfo != null && !registrosConInfo.isEmpty()) { %>
                                        <ul class="list-group">
                                            <% for (Map<String, Object> registroInfo : registrosConInfo) { %>
                                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                                    <span><i class="fas fa-ticket-alt text-primary"></i> <%= registroInfo.get("descripcion") %></span>
                                                    <% if (registroInfo.get("error") != null && (Boolean) registroInfo.get("error")) { %>
                                                        <span class="btn btn-outline-secondary btn-sm disabled">
                                                            <i class="fas fa-exclamation-triangle"></i> Error
                                                        </span>
                                                    <% } else { %>
                                                        <a href="<%= registroInfo.get("url") %>" class="btn btn-outline-primary btn-sm">
                                                            <i class="fas fa-eye"></i> Ver Registro
                                                        </a>
                                                    <% } %>
                                                </li>
                                            <% } %>
                                        </ul>
                                    <% } else { %>
                                        <div class="alert alert-info" role="alert">
                                            <i class="fas fa-info-circle"></i> No tienes registros a ediciones de eventos aún.
                                        </div>
                                    <% } %>
                                </div>
                            </div>
                        <% } %>
                        
                        <!-- CASO 2: VISITANTE consultando ASISTENTEEE  -->
                        <% if (esAsistente && !esMismoPerfil) { %>
                            <div class="row">
                                <div class="col-12">
                                    <h4 class="mb-3">Ediciones donde <%= usuario.getNickname() %> está registrado</h4>
                                    <%
                                    @SuppressWarnings("unchecked")
                                    List<Map<String, Object>> edicionesRegistrado = (List<Map<String, Object>>) request.getAttribute("edicionesRegistrado");
                                    %>
                                    
                                    <% if (edicionesRegistrado != null && !edicionesRegistrado.isEmpty()) { %>
                                        <div class="row">
                                            <% for (Map<String, Object> edicionInfo : edicionesRegistrado) { %>
                                                <div class="col-12 col-md-6 col-lg-4 mb-3">
                                                    <div class="card border-primary">
                                                        <div class="card-body">
                                                            <h6 class="card-title text-primary">
                                                                <i class="fas fa-ticket-alt"></i> <%= edicionInfo.get("edicion") %>
                                                            </h6>
                                                            <p class="card-text">
                                                                <%= edicionInfo.get("descripcion") %>
                                                            </p>
                                                            <a href="<%= edicionInfo.get("url") %>" 
                                                               class="btn btn-outline-primary btn-sm w-100">
                                                                <i class="fas fa-eye"></i> Ver Edición
                                                            </a>
                                                        </div>
                                                    </div>
                                                </div>
                                            <% } %>
                                        </div>
                                    <% } else { %>
                                        <div class="alert alert-info" role="alert">
                                            <i class="fas fa-info-circle"></i> No hay eventos disponibles.
                                        </div>
                                    <% } %>
                                </div>
                            </div>
                        <% } %>
                        
                        <!-- CASO 3: ORGANIZADOR - mostrar ediciones según el tipo de consulta -->
                        <% if (esOrganizador) { %>
                            <div class="row">
                                <div class="col-12">
                                    <%
                                    @SuppressWarnings("unchecked")
                                    List<Map<String, Object>> edicionesAceptadas = (List<Map<String, Object>>) request.getAttribute("edicionesAceptadas");
                                    @SuppressWarnings("unchecked")
                                    List<Map<String, Object>> edicionesIngresadas = (List<Map<String, Object>>) request.getAttribute("edicionesIngresadas");
                                    @SuppressWarnings("unchecked")
                                    List<Map<String, Object>> edicionesRechazadas = (List<Map<String, Object>>) request.getAttribute("edicionesRechazadas");
                                    %>
                                    
                                    <!-- Mostrar ediciones ACEPTADAS (siempre visible) -->
                                    <% if (edicionesAceptadas != null && !edicionesAceptadas.isEmpty()) { %>
                                        <h4 class="mb-3 text-success">
                                            <i class="fas fa-check-circle"></i> Ediciones Aceptadas
                                        </h4>
                                        <div class="row mb-4">
                                            <% for (Map<String, Object> edicionInfo : edicionesAceptadas) { %>
                                                <div class="col-12 col-md-6 col-lg-4 mb-3">
                                                    <div class="card border-success">
                                                        <div class="card-header bg-success text-white">
                                                            <h6 class="card-title mb-0">
                                                                <i class="fas fa-calendar-check"></i> <%= edicionInfo.get("edicion") %>
                                                            </h6>
                                                        </div>
                                                        <div class="card-body">
                                                            <p class="card-text">
                                                                <strong>Evento:</strong> <%= edicionInfo.get("evento") %><br>
                                                                <strong>Estado:</strong> <span class="badge bg-success">Aceptada</span><br>
                                                                <strong>Lugar:</strong> <%= edicionInfo.get("ciudad") %>, <%= edicionInfo.get("pais") %><br>
                                                                <strong>Período:</strong> <%= java.time.LocalDate.parse((String) edicionInfo.get("fechaInicio")).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) %> - <%= java.time.LocalDate.parse((String) edicionInfo.get("fechaFin")).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) %>
                                                            </p>
                                                            <a href="<%= edicionInfo.get("url") %>" 
                                                               class="btn btn-success btn-sm w-100">
                                                                <i class="fas fa-eye"></i> Ver Edición
                                                            </a>
                                                        </div>
                                                    </div>
                                                </div>
                                            <% } %>
                                        </div>
                                    <% } %>
                                    
                                    <!-- Mostrar ediciones INGRESADAS y RECHAZADAS solo si es el mismo organizador -->
                                    <% if (esMismoPerfil) { %>
                                        <!-- Ediciones INGRESADAS -->
                                        <% if (edicionesIngresadas != null && !edicionesIngresadas.isEmpty()) { %>
                                            <h4 class="mb-3 text-warning">
                                                <i class="fas fa-clock"></i> Ediciones Ingresadas
                                            </h4>
                                            <div class="row mb-4">
                                                <% for (Map<String, Object> edicionInfo : edicionesIngresadas) { %>
                                                    <div class="col-12 col-md-6 col-lg-4 mb-3">
                                                        <div class="card border-warning">
                                                            <div class="card-header bg-warning text-dark">
                                                                <h6 class="card-title mb-0">
                                                                    <i class="fas fa-clock"></i> <%= edicionInfo.get("edicion") %>
                                                                </h6>
                                                            </div>
                                                            <div class="card-body">
                                                                <p class="card-text">
                                                                    <strong>Evento:</strong> <%= edicionInfo.get("evento") %><br>
                                                                    <strong>Estado:</strong> <span class="badge bg-warning text-dark">Ingresada</span><br>
                                                                    <strong>Lugar:</strong> <%= edicionInfo.get("ciudad") %>, <%= edicionInfo.get("pais") %><br>
                                                                	<strong>Período:</strong> <%= java.time.LocalDate.parse((String) edicionInfo.get("fechaInicio")).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) %> - <%= java.time.LocalDate.parse((String) edicionInfo.get("fechaFin")).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) %>
                                                                </p>
                                                                <a href="<%= edicionInfo.get("url") %>" 
                                                                   class="btn btn-warning btn-sm w-100">
                                                                    <i class="fas fa-eye"></i> Ver Edición
                                                                </a>
                                                            </div>
                                                        </div>
                                                    </div>
                                                <% } %>
                                            </div>
                                        <% } %>
                                        
                                        <!-- Ediciones RECHAZADAS -->
                                        <% if (edicionesRechazadas != null && !edicionesRechazadas.isEmpty()) { %>
                                            <h4 class="mb-3 text-danger">
                                                <i class="fas fa-times-circle"></i> Ediciones Rechazadas
                                            </h4>
                                            <div class="row mb-4">
                                                <% for (Map<String, Object> edicionInfo : edicionesRechazadas) { %>
                                                    <div class="col-12 col-md-6 col-lg-4 mb-3">
                                                        <div class="card border-danger">
                                                            <div class="card-header bg-danger text-white">
                                                                <h6 class="card-title mb-0">
                                                                    <i class="fas fa-times-circle"></i> <%= edicionInfo.get("edicion") %>
                                                                </h6>
                                                            </div>
                                                            <div class="card-body">
                                                                <p class="card-text">
                                                                    <strong>Evento:</strong> <%= edicionInfo.get("evento") %><br>
                                                                    <strong>Estado:</strong> <span class="badge bg-danger">Rechazada</span><br>
                                                                    <strong>Lugar:</strong> <%= edicionInfo.get("ciudad") %>, <%= edicionInfo.get("pais") %><br>
                                                                	<strong>Período:</strong> <%= java.time.LocalDate.parse((String) edicionInfo.get("fechaInicio")).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) %> - <%= java.time.LocalDate.parse((String) edicionInfo.get("fechaFin")).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) %>
                                                                </p>
                                                                <a href="<%= edicionInfo.get("url") %>" 
                                                                   class="btn btn-danger btn-sm w-100">
                                                                    <i class="fas fa-eye"></i> Ver Edición
                                                                </a>
                                                            </div>
                                                        </div>
                                                    </div>
                                                <% } %>
                                            </div>
                                        <% } %>
                                    <% } %>
                                    
                                    <% 
                                    boolean sinEdiciones = (edicionesAceptadas == null || edicionesAceptadas.isEmpty()) && 
                                                          (!esMismoPerfil || 
                                                           ((edicionesIngresadas == null || edicionesIngresadas.isEmpty()) && 
                                                            (edicionesRechazadas == null || edicionesRechazadas.isEmpty())));
                                    if (sinEdiciones) { 
                                    %>
                                        <div class="alert alert-info" role="alert">
                                            <i class="fas fa-info-circle"></i> 
                                            <%= esMismoPerfil ? "No tienes ediciones de eventos aún." : "Este organizador no tiene ediciones aceptadas." %>
                                        </div>
                                    <% } %>
                                </div>
                            </div>
                        <% } %>
                        
        <!-- Botón Modificar Usuario para cualquier usuario consultando su propio perfil -->
        <% if (esMismoPerfil) { %>
            <div class="row mt-4">
                <div class="col-12 text-center">
                    <a href="<%= request.getContextPath() %>/modificarUsuario?nickname=<%= usuario.getNickname() %>" 
                       class="btn btn-primary btn-lg">
                        <i class="fas fa-edit"></i> Modificar
                    </a>
                </div>
            </div>
        <% } %>
        
        <!-- Sección de Usuarios Seguidos (solo para el propio perfil) -->
        <% 
        @SuppressWarnings("unchecked")
        List<webservices.DtUsuario> usuariosSeguidos = (List<webservices.DtUsuario>) request.getAttribute("usuariosSeguidos");
        
        if (esMismoPerfil && usuariosSeguidos != null) { %>
            <div class="row mt-5">
                <div class="col-12">
                    <h4 class="mb-3">
                        <i class="fas fa-users"></i> Usuarios que sigues 
                        <span class="badge bg-primary"><%= usuariosSeguidos.size() %></span>
                    </h4>
                    
                    <% if (usuariosSeguidos.isEmpty()) { %>
                        <div class="alert alert-info text-center" role="alert">
                            <i class="fas fa-info-circle"></i> 
                            Aún no sigues a ningún usuario.
                            <br>
                            <small>Puedes seguir usuarios desde la <a href="<%= request.getContextPath() %>/listarUsuarios" class="alert-link">lista de usuarios</a>.</small>
                        </div>
                    <% } else { %>
                        <div class="row g-3">
                            <% for (webservices.DtUsuario usuarioSeguido : usuariosSeguidos) { 
                                // Determinar tipo de usuario y colores
                                String tipoUsuario = "Usuario";
                                String colorClase = "secondary";
                                String borderColor = "border-secondary";
                                
                                if (usuarioSeguido instanceof webservices.DtAsistente) {
                                    tipoUsuario = "Asistente";
                                    colorClase = "primary";
                                    borderColor = "border-primary";
                                } else if (usuarioSeguido instanceof webservices.DtOrganizador) {
                                    tipoUsuario = "Organizador";
                                    colorClase = "success";
                                    borderColor = "border-success";
                                }
                                
                                String imagenPerfilSeguido = usuarioSeguido.getImagenPerfil();
                                String imagenRutaSeguido = (imagenPerfilSeguido != null && !imagenPerfilSeguido.trim().isEmpty() && !imagenPerfilSeguido.equals("-"))
                                    ? (imagenPerfilSeguido.startsWith("recursos/") ? imagenPerfilSeguido : "recursos/img/" + imagenPerfilSeguido)
                                    : "recursos/placeholderNoImagen.png";
                            %>
                            <div class="col-md-6 col-lg-4">
                                <div class="card h-100 <%= borderColor %>" style="max-width: 300px;">
                                    <div class="card-header bg-<%= colorClase %> text-white text-center">
                                        <small><%= tipoUsuario %></small>
                                    </div>
                                    <img src="<%= request.getContextPath() %>/<%= imagenRutaSeguido %>" 
                                         class="card-img-top" 
                                         alt="<%= tipoUsuario %>" 
                                         style="height: 120px; object-fit: cover;"
                                         onerror="this.src='<%= request.getContextPath() %>/recursos/placeholderNoImagen.png'">`
                                    <div class="card-body text-center p-2">
                                        <h6 class="card-title mb-1"><%= usuarioSeguido.getNickname() %></h6>
                                        <p class="card-text small text-muted mb-2"><%= usuarioSeguido.getNombre() %></p>
                                        <a href="<%= request.getContextPath() %>/consultaUsuario?nickname=<%= usuarioSeguido.getNickname() %>" 
                                           class="btn btn-outline-<%= colorClase %> btn-sm">Ver perfil</a>
                                    </div>
                                </div>
                            </div>
                            <% } %>
                        </div>
                    <% } %>
                </div>
            </div>
        <% } %>
        
        <!-- Botón Seguir/Dejar de Seguir para usuarios logueados viendo perfiles ajeno -->
        <% 
        String usuarioLogueado = (String) request.getAttribute("usuarioLogueado");
        Boolean sigueAlUsuarioObj = (Boolean) request.getAttribute("sigueAlUsuario");
        boolean sigueAlUsuario = (sigueAlUsuarioObj != null && sigueAlUsuarioObj.booleanValue());
        
        if (usuarioLogueado != null && !esMismoPerfil) { %>
            <div class="row mt-4">
                <div class="col-12 text-center">
                    <form method="post" action="<%= request.getContextPath() %>/seguirUsuario" style="display: inline;">
                        <input type="hidden" name="usuarioSeguido" value="<%= usuario.getNickname() %>" />
                        <input type="hidden" name="redirectUrl" value="<%= request.getContextPath() %>/consultaUsuario?nickname=<%= usuario.getNickname() %>" />
                        
                        <% if (sigueAlUsuario) { %>
                            <input type="hidden" name="accion" value="dejar_seguir" />
                            <button type="submit" class="btn btn-outline-danger btn-lg">
                                <i class="fas fa-user-minus"></i> Dejar de Seguir
                            </button>
                        <% } else { %>
                            <input type="hidden" name="accion" value="seguir" />
                            <button type="submit" class="btn btn-success btn-lg">
                                <i class="fas fa-user-plus"></i> Seguir
                            </button>
                        <% } %>
                    </form>
                </div>
            </div>
        <% } %>                        <%
                            }
                        }
                        %>
                    </div>
                </div>
            </div>
        </div>
    </main>

</body>
</html>