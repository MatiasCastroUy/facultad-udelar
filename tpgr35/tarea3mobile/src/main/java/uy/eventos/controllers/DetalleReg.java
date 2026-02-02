package uy.eventos.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Base64;                    
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import webservices.WSUsuario;
import webservices.WSUsuarioService;
import webservices.WSEvento;
import webservices.WSEventoService;
import webservices.DtRegistro;
import webservices.DtEdicion;

@SuppressWarnings("serial")
@WebServlet("/detalleReg")
public class DetalleReg extends HttpServlet {

  private WSUsuario getPortUsuario() throws IOException {
    Properties p = (Properties) getServletContext().getAttribute("propiedades");
    URL url = new URL(p.getProperty("url_ws_usuarios"));
    return new WSUsuarioService(url).getWSUsuarioPort();
  }

  private WSEvento getPortEvento() throws IOException {
    Properties p = (Properties) getServletContext().getAttribute("propiedades");
    URL url = new URL(p.getProperty("url_ws_eventos"));
    return new WSEventoService(url).getWSEventoPort();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String nick = (String) req.getSession().getAttribute("usuarioLogueado");
    String rol  = (String) req.getSession().getAttribute("rol");
    if (nick == null || rol == null || !rol.equalsIgnoreCase("Asistente")) {
      resp.sendRedirect(req.getContextPath() + "/login?next=" + req.getRequestURI());
      return;
    }

    String evento  = trim(req.getParameter("evento"));
    String edicion = trim(req.getParameter("edicion"));
    String tiporeg = trim(req.getParameter("tiporeg"));
    String imagen  = trim(req.getParameter("imagen")); // (se mantiene por compatibilidad)

    if (isBlank(edicion)) {
      resp.sendError(400, "Falta parámetro: edicion");
      return;
    }

    WSUsuario portUsr = getPortUsuario();
    WSEvento  portEvt = getPortEvento();

    Boolean edicionComenzada = null;

    try {
      DtRegistro dt = null;
      if (!isBlank(evento)) {
        dt = portEvt.getRegistro(evento, edicion, nick);
      }
      if (dt == null) {
        DtRegistro dtUsr = portUsr.obtenerRegistroDeUsuario(nick, edicion);
        if (dtUsr != null) {
          evento = (isBlank(evento) ? dtUsr.getEvento() : evento);
          dt = portEvt.getRegistro(evento, edicion, nick);
        }
      }

      if (dt == null) {
        req.setAttribute("error", "No se encontró tu registro para la edición indicada.");
      } else {
        if (isBlank(evento))  evento  = dt.getEvento();
        if (isBlank(tiporeg)) tiporeg = dt.getTipo();
        req.setAttribute("dtRegistro", dt);
      }

      try {
        DtEdicion dtEd = portEvt.getDTEdicion(evento, edicion);

        String imgB64 = null;
        try {
          byte[] bytes = portEvt.getImagenEdicion(evento, edicion);
          if (bytes != null && bytes.length > 0) {
            imgB64 = Base64.getEncoder().encodeToString(bytes);
          }
        } catch (Exception ignore) {}
        req.setAttribute("imagenEdicion_b64", imgB64);

        if (dtEd != null && dtEd.getFechaInicio() != null && !dtEd.getFechaInicio().isBlank()) {
          LocalDate ini = LocalDate.parse(dtEd.getFechaInicio()); // ISO yyyy-MM-dd
          edicionComenzada = !LocalDate.now().isBefore(ini);
        }
      } catch (Exception ignore) {}
    } catch (Exception e) {
      req.setAttribute("error", e.getMessage());
    }

    req.setAttribute("evento",  evento);
    req.setAttribute("edicion", edicion);
    req.setAttribute("tiporeg", tiporeg);
    req.setAttribute("imagen",  imagen);                 
    req.setAttribute("edicionComenzada", edicionComenzada);

    req.getRequestDispatcher("/WEB-INF/ConsultaDeRegistro/Detalle.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession ses = request.getSession(false);
    String nick = (ses == null) ? null : (String) ses.getAttribute("usuarioLogueado");
    String rol  = (ses == null) ? null : (String) ses.getAttribute("rol");

    String evento  = trim(request.getParameter("evento"));
    String edicion = trim(request.getParameter("edicion"));
    String op      = trim(request.getParameter("op"));
    String imagen  = trim(request.getParameter("imagen")); // compat

    if (nick == null || rol == null || !rol.equalsIgnoreCase("Asistente")) {
      response.sendRedirect(request.getContextPath() + "/login?next=" + request.getRequestURI());
      return;
    }

    if (!"confirmarAsistencia".equals(op)) {
      doGet(request, response);
      return;
    }

    if (isBlank(evento) || isBlank(edicion)) {
      response.sendError(400, "Faltan: evento y edicion");
      return;
    }

    try {
      DtEdicion dtEd = getPortEvento().getDTEdicion(evento, edicion);
      if (dtEd != null && dtEd.getFechaInicio() != null && !dtEd.getFechaInicio().isBlank()) {
        LocalDate ini = LocalDate.parse(dtEd.getFechaInicio());
        if (LocalDate.now().isBefore(ini)) {
          request.setAttribute("warn", "El evento debe comenzar para confirmar la asistencia.");
          request.setAttribute("evento",  evento);
          request.setAttribute("edicion", edicion);
          request.setAttribute("imagen",  imagen);
          request.setAttribute("edicionComenzada", false);

          try {
            DtRegistro dt = getPortEvento().getRegistro(evento, edicion, nick);
            request.setAttribute("dtRegistro", dt);
          } catch (Exception ignore) {}

          try {
            byte[] bytes = getPortEvento().getImagenEdicion(evento, edicion);
            if (bytes != null && bytes.length > 0) {
              request.setAttribute("imagenEdicion_b64", Base64.getEncoder().encodeToString(bytes));
            }
          } catch (Exception ignore) {}

          request.getRequestDispatcher("/WEB-INF/ConsultaDeRegistro/Detalle.jsp").forward(request, response);
          return;
        }
      }
    } catch (Exception ignore) {}

    try {
      getPortEvento().marcarAsistencia(evento, edicion, nick);

      DtRegistro dt = getPortEvento().getRegistro(evento, edicion, nick);

      request.setAttribute("dtRegistro", dt);
      request.setAttribute("evento",  evento);
      request.setAttribute("edicion", edicion);
      request.setAttribute("imagen",  imagen);
      request.setAttribute("ok", "Asistencia confirmada.");
      request.setAttribute("edicionComenzada", true);

      try {
        byte[] bytes = getPortEvento().getImagenEdicion(evento, edicion);
        if (bytes != null && bytes.length > 0) {
          request.setAttribute("imagenEdicion_b64", Base64.getEncoder().encodeToString(bytes));
        }
      } catch (Exception ignore) {}

      request.getRequestDispatcher("/WEB-INF/ConsultaDeRegistro/Detalle.jsp").forward(request, response);

    } catch (Exception e) {
      request.setAttribute("error", (e.getMessage() == null) ? "No se pudo confirmar la asistencia." : e.getMessage());
      try {
        DtRegistro dt = getPortEvento().getRegistro(evento, edicion, nick);
        request.setAttribute("dtRegistro", dt);
      } catch (Exception ignore) {}
      request.setAttribute("evento",  evento);
      request.setAttribute("edicion", edicion);
      request.setAttribute("imagen",  imagen);

      try {
        byte[] bytes = getPortEvento().getImagenEdicion(evento, edicion);
        if (bytes != null && bytes.length > 0) {
          request.setAttribute("imagenEdicion_b64", Base64.getEncoder().encodeToString(bytes));
        }
      } catch (Exception ignore) {}

      request.getRequestDispatcher("/WEB-INF/ConsultaDeRegistro/Detalle.jsp").forward(request, response);
    }
  }

  private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
  private static String trim(String s) { return s == null ? null : s.trim(); }
}
