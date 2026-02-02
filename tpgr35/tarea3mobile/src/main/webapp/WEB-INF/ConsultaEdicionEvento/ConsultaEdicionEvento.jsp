<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.HashSet"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.time.format.DateTimeFormatter"%>

<%@ page import="webservices.DtEvento"%>
<%@ page import="webservices.DtEdicion"%>
<%@ page import="webservices.DtOrganizador"%>
<%@ page import="webservices.DtRegistro"%>
<%@ page import="webservices.DtTipoRegistro"%>
<!DOCTYPE html>
<html>
<head>
<title>Consulta Edición de Evento - Eventos.uy</title>
<jsp:include page="/WEB-INF/template/head.jsp" />
</head>

<body>
	<jsp:include page="/WEB-INF/template/header.jsp" />

	<main>
		<div class="container-fluid">
			<div class="row">
				<jsp:include page="/WEB-INF/template/panel.jsp" />

				<div id="main" class="col-12 col-lg-10 p-3">
					<%
					String ctx = request.getContextPath();
					String error = (String) request.getAttribute("error");
					
					Boolean mostrarDetallesRegistro = (Boolean) request.getAttribute("mostrarDetallesRegistro");

					DtEvento evento = (DtEvento) request.getAttribute("dtEvento");
					DtEdicion edicion = (DtEdicion) request.getAttribute("dtEdicion");
					DtOrganizador organizador = (DtOrganizador) request.getAttribute("dtOrganizador");
					DtRegistro registro = (DtRegistro) request.getAttribute("dtRegistro");
					Map<String, DtRegistro> listaRegistros = (HashMap<String, DtRegistro>) request.getAttribute("dtRegistroLst");
					Set<DtTipoRegistro> listaTiposRegistro = (HashSet<DtTipoRegistro>) request.getAttribute("dtTipoRegistroLst");
					
					String imagenEdicion_b64 = (String) request.getAttribute("imagenEdicion_b64");
					String imagenEvento_b64 = (String) request.getAttribute("imagenEvento_b64");
					String imagen_org_b64 = (String) request.getAttribute("img_org_b64");

					if (error != null && !error.isBlank()) {
					%>
					<div class="alert alert-danger" role="alert"><%=error%></div>
					<%
					} else {
					%>

					<div class="row">
						<!-- Información primaria -->
						<div class="col-12 col-md-9 p-2">
							<div class="row">
								<h2 class="fw-bold ms-2">Consulta de Edición de Evento</h2>
							</div>

							<div class="row">
								<!-- Información de la Edición -->
								<div class="col-12 col-md-4 py-4 px-2">
									<h4 class="mb-4">Edición:</h4>
									<img id="imgEdicion"
										<% String src_ed_img = imagenEdicion_b64 != null ? "data:image/jpeg;base64," + imagenEdicion_b64 
										: ctx + "/recursos/placeholder_evento.png" ; %>
										src="<%= src_ed_img %>"
										width="100%" alt="<%=edicion.getNombre()%>" class="mb-4" /><br />
									<h3 id="nombreEdicion"><%=edicion.getNombre()%></h3>
								</div>

								<div class="col-12 col-md-8 py-4 px-2">
									<p class="px-2">
										<b>Siglas: </b><span id="siglasEdicion"><%=edicion.getSigla()%></span>
									</p>
									<hr />
									<p class="px-2">
										<b>Ciudad: </b><span id="ciudadEdicion"><%=edicion.getCiudad()%></span>
									</p>
									<hr />
									<p class="px-2">
										<b>País: </b><span id="paisEdicion"><%=edicion.getPais()%></span>
									</p>
									<hr />
									<p class="px-2">
										<b>Fecha de Inicio: </b><span id="inicioEdicion"><%= LocalDate.parse(edicion.getFechaInicio()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) %></span>
									</p>
									<hr />
									<p class="px-2">
										<b>Fecha de Fin: </b><span id="finEdicion"><%= LocalDate.parse(edicion.getFechaFin()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) %></span>
									</p>
									<hr />
									<p class="px-2">
										<b>Creado el: </b><span id="altaEdicion"><%= LocalDate.parse(edicion.getFechaAlta()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) %></span>
									</p>
									<%
									if (edicion.getUrlVideo() != null && !edicion.getUrlVideo().isBlank()) {
										String splitStr = "\\?v=";
									%>
										<hr />
										<iframe 
											width="100%" 
											height="300" 
											src="https://www.youtube.com/embed/<%= edicion.getUrlVideo().split(splitStr)[1] %>" 
											title="YouTube video player" 
											frameborder="0" 
											allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" 
											referrerpolicy="strict-origin-when-cross-origin" 
											allowfullscreen
										></iframe>
									<%
									}
									%>
								</div>

							</div>

							<%
							if (mostrarDetallesRegistro) {
							%>
							<hr />
							<%
							if (registro == null) {
							%>
							<div class="row">
								<!-- Información del registro (Asistente - no registrado) -->
								<div class="col-12 py-4 px-2">
									<h4 class="mb-4">Registro:</h4>
									<i>No te has registrado a esta edición.</i>
								</div>
							</div>
							<%
							} else {
							%>

							<div class="row">
								<!-- Información del registro (Asistente - registrado) -->
								<div class="col-12 py-4 px-2">
									<h4 class="mb-4">Registro:</h4>

									<p class="px-2">
										<b>Fecha de Registro: </b><span id="fechaRegistro"><%= LocalDate.parse(registro.getFecha()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) %></span>
									</p>
									<hr />
									<p class="px-2">
										<b>Tipo de Registro: </b><span id="tipoRegistro"><%=registro.getTipo()%></span>
									</p>
									<hr />
									<p class="px-2">
										<b>Costo: </b><span id="costoRegistro">$ <%=registro.getCosto()%></span>
									</p>
									<hr />
									<p class="px-2">
										<b>Has asistido: </b><span id="asistio"><%= registro.isAsistio() ? "Sí" : "No" %></span>
									</p>
									<hr />
									<form action="consultaDeRegistro">
										<input type="hidden" name="evento" value="<%= evento.getNombre() %>">
										<input type="hidden" name="edicion" value="<%= edicion.getNombre() %>">
										<input type="hidden" name="tiporeg" value="<%= registro.getTipo() %>">
										<button class="btn btn-link p-0" type="submit">Ver detalle</button>
									</form>
								</div>
							</div>
							<%
							}
							}
							%>
						</div>


						<!-- Información secundaria -->
						<div class="col-12 col-md-3 py-2">

							<!-- Evento -->
							<div class="row">
								<div class="col-12 py-2">
									<h4 class="mb-2">Evento:</h4>
									<a
										href="<%=ctx%>/consultaEvento?nombre=<%=evento.getNombre()%>">
										<% String img_evt_src = imagenEvento_b64 != null ?
												"data:image/jpeg;base64," + imagenEvento_b64 
												: ctx + "/recursos/placeholder_evento.png" ;%>
										<img src="<%= img_evt_src %>"
											alt="<%=evento.getNombre()%>" width="100%" />
									</a>
								</div>
								<hr />
							</div>

							<!-- Organizador -->
							<div class="row">
								<div class="col-12 py-2">
									<h4 class="mb-2">Organizador:</h4>
									<a
										href="<%=ctx%>/consultaUsuario?nickname=<%=organizador.getNickname()%>">
										<img
										<% String src_org_img = imagen_org_b64 != null ? "data:image/jpeg;base64," + imagen_org_b64 
												: ctx + "/recursos/placeholderNoImagen.png" ; %>
										src="<%= src_org_img %>"
										alt="<%=organizador.getNombre()%>" width="100%" />
									</a>
								</div>
								<hr />
							</div>

							<!-- Tipos de Registro -->
							<div class="row">
								<div class="col-12 py-2">
									<h4 class="mb-2">Tipos de Registro:</h4>
									<%
									if (listaTiposRegistro.size() == 0) {
									%>
									<i>No hay ningún tipo de registro.</i>
									<%
									} else {
									%>
									<ul>
										<%
										for (DtTipoRegistro tipoReg : listaTiposRegistro) {
										%>
										<li><a
											href="<%=ctx%>/consultaTipoReg?evento=<%=evento.getNombre()%>&edicion=<%=edicion.getNombre()%>&tiporeg=<%=tipoReg.getNombre()%>"
											class="link-dark link-underline-opacity-25"><%=tipoReg.getNombre()%></a></li>
										<%
										}
										%>
									</ul>
									<%
									}
									%>
								</div>
								<hr />
							</div>

							<!-- Patrocinios -->
							<div class="row">
								<div class="col-12 py-2">
									<h4 class="mb-2">Patrocinios:</h4>
									<i>No hay ningún patrocinio.</i>
								</div>
							</div>
						</div>
					</div>
					<%
					}
					%>
				</div>
			</div>
		</div>
	</main>

</body>
</html>