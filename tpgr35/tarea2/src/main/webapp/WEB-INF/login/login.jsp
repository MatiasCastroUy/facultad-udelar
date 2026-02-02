<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="es">
<head>
  <meta charset="utf-8">
  <title>EventosUy - Login</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body, html {
      height: 100%;
      margin: 0;
      font-family: Arial, sans-serif;
    }
    .left-panel {
      background-color: black;
      color: white;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 6rem;
      font-weight: bold;
      letter-spacing: 5px;
      text-align: center;
      padding: 2rem;
    }
    .login-box {
      background: white;
      padding: 2rem;
      border-radius: 12px;
      box-shadow: 0 4px 15px rgba(0,0,0,0.2);
      width: 100%;
      max-width: 450px;
    }
    .register-link {
      color: black;
      text-decoration: none;
    }
    .register-link:hover {
      color: gray;
    }
  </style>
</head>
<body>
  <%
    String ctx   = request.getContextPath();
    String error = (String) request.getAttribute("error");
    String next  = (String) request.getAttribute("next");
  %>

  <div class="container-fluid h-100">
    <div class="row h-100">
      <div class="col-12 col-md-7 d-flex left-panel">
        EVENTOS.UY
      </div>
      <div class="col-12 col-md-5 d-flex flex-column align-items-center justify-content-center p-4">
        <div class="login-box">
          <h2 class="text-center mb-4">Iniciar Sesión</h2>

          <% if (error != null) { %>
            <div class="alert alert-danger" role="alert"><%= error %></div>
          <% } %>

          <!-- MISMO DISEÑO, solo se ajusta action y names -->
          <form action="<%= ctx %>/login" method="post">
            <% if (next != null) { %>
              <input type="hidden" name="next" value="<%= next %>">
            <% } %>

            <div class="mb-3">
              <input type="text" name="usuario" class="form-control form-control-lg" placeholder="Nickname/email" required>
            </div>
            <div class="mb-3">
              <input type="password" name="contrasena" class="form-control form-control-lg" placeholder="Contraseña" required>
            </div>
            <div class="form-check mb-3">
              <input class="form-check-input" type="checkbox" id="rememberMe" disabled>
              <label class="form-check-label" for="rememberMe">Recordarme</label>
            </div>
            <button type="submit" class="btn btn-dark btn-lg w-100">Entrar</button>
          </form>
        </div>
        <p class="mt-3">
          <a href="<%= request.getContextPath() %>/usuario" class="text-decoration-none">¿No tienes cuenta? Regístrate</a>
        </p>
      </div>
    </div>
  </div>
</body>
</html>

