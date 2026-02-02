<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<%
  String ctx  = request.getContextPath();
  String nick = (String) session.getAttribute("usuarioLogueado");
  boolean logged = (nick != null);

  String rol = (String) session.getAttribute("rol");
  boolean esOrganizador = "ORGANIZADOR".equals(rol);
  boolean esAsistente   = "ASISTENTE".equals(rol);

  String uri  = request.getRequestURI();
  String here = uri.substring(ctx.length());
  String qs   = request.getQueryString();
  String next = (qs == null) ? here : (here + "?" + qs);
  String loginUrl = ctx + "/login?next=" + java.net.URLEncoder.encode(next, "UTF-8");
%>

<%-- panel ocultable para pantallas pequeñas y medianas --%>
<div class="offcanvas offcanvas-start bg-light" tabindex="-1" id="panel">
  <div class="offcanvas-header">
    <h5 class="offcanvas-title">Menú</h5>
    <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
  </div>
  <div class="offcanvas-body">
    <a class="col-12 btn btn-dark my-2" href="<%= ctx %>/">Listar eventos</a>
    <a class="col-12 btn btn-dark my-2" href="<%= ctx %>/listaReg">Consulta de registro</a>
    <a class="col-12 btn btn-dark my-2" href="<%= ctx %>/listaReg">Registro de asistencia</a>
  </div>
</div>

<%-- panel fijo visible solo en pantallas grandes --%>
<div class="d-none d-lg-block bg-light" style="width:250px; height:100vh; position:fixed; top:54px; left:0; overflow-y:auto;" tabindex="-1" id="panel">
  <h5 class="offcanvas-title">Menú</h5>
  <div class="offcanvas-body">
    <a class="col-12 btn btn-dark my-2" href="<%= ctx %>/">Listar eventos</a>
    <a class="col-12 btn btn-dark my-2" href="<%= ctx %>/listaReg">Consulta de registro</a>
    <a class="col-12 btn btn-dark my-2" href="<%= ctx %>/listaReg">Registro de asistencia</a>
  </div>
</div>

<div class="d-none d-lg-block" style="width:250px; height:100vh; top:54px; left:0; overflow-y:auto;"></div>
