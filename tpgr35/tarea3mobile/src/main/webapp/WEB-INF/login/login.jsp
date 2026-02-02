<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="es">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>EventosUy – Login</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body { min-height: 100vh; }
    .signin-wrapper { min-height: 100vh; }
    .brand { font-weight: 800; letter-spacing: .15rem; }
    .card { border-radius: 1rem; box-shadow: 0 10px 30px rgba(0,0,0,.08); }
    .form-control-lg { padding-top: .9rem; padding-bottom: .9rem; }
    .btn-lg { padding-top: .8rem; padding-bottom: .8rem; }
  </style>
</head>
<body class="bg-light">
<%
  String ctx   = request.getContextPath();
  String error = (String) request.getAttribute("error");
  String next  = (String) request.getAttribute("next");
%>

<main class="container signin-wrapper d-flex align-items-center justify-content-center py-5">
  <div class="w-100" style="max-width: 420px;">
    <div class="text-center mb-4">
      <div class="brand h1 m-0">EVENTOS.UY</div>
      <small class="text-muted">Dispositivo móvil</small>
    </div>

    <div class="card p-4">
      <h1 class="h4 text-center mb-3">Iniciar sesión</h1>

      <% if (error != null) { %>
        <div class="alert alert-danger" role="alert"><%= error %></div>
      <% } %>

      <form action="<%= ctx %>/login" method="post" novalidate>
        <% if (next != null) { %>
          <input type="hidden" name="next" value="<%= next %>">
        <% } %>

        <div class="mb-3">
          <input
            type="text"
            id="usuario"
            name="usuario"
            class="form-control form-control-lg"
            placeholder="nickname/email"
            required
            autocomplete="username"
            autocapitalize="off"
            spellcheck="false"
            inputmode="email">
        </div>

        <div class="mb-3">
          <input
            type="password"
            id="contrasena"
            name="contrasena"
            class="form-control form-control-lg"
            placeholder="contraseña"
            required
            autocomplete="current-password">
        </div>

        <button type="submit" class="btn btn-dark btn-lg w-100">Entrar</button>

        <p class="text-center text-muted mt-3 mb-0" style="font-size:.9rem">
          Solo asistentes pueden iniciar sesión desde el dispositivo móvil.
        </p>
      </form>
    </div>
  </div>
</main>
</body>
</html>




