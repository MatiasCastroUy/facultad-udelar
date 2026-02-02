package uy.eventos.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import webservices.WSEvento;
import webservices.WSEventoService;
import webservices.WSUsuarioService;
import webservices.DtEdicion;
import webservices.DtTipoRegistro;

import java.util.Base64;

@SuppressWarnings("serial")
@WebServlet("/registro-a-edicion")
public class RegistroAEdicion extends HttpServlet {

  private WSEvento port() throws IOException { 
    Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
    URL url_ws_eventos = new URL(propiedades.getProperty("url_ws_eventos"));
    WSEventoService service = new WSEventoService(url_ws_eventos);
    return service.getWSEventoPort(); 
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String evento  = trimOrNull(request.getParameter("evento"));
    String edicion = trimOrNull(request.getParameter("edicion"));
    String tiporeg = trimOrNull(request.getParameter("tiporeg"));
    String imagen  = trimOrNull(request.getParameter("imagen"));

    if (isBlank(evento) || isBlank(edicion) || isBlank(tiporeg)) {
      response.sendError(400, "Faltan: evento, edicion, tiporeg");
      return;
    }

    DtEdicion dtEd = null;
    DtTipoRegistro dtTr = null;
    Boolean edicionFinalizada = null;
    try {
      dtEd = port().getDTEdicion(evento, edicion);
      if (dtEd != null && dtEd.getFechaFin() != null && !dtEd.getFechaFin().isBlank()) {
        LocalDate fin = LocalDate.parse(dtEd.getFechaFin());
        edicionFinalizada = fin.isBefore(LocalDate.now());
      }
      dtTr = port().getDTTipoReg(evento, edicion, tiporeg);

      try {
        byte[] bytes = port().getImagenEdicion(evento, edicion);
        String imgB64 = (bytes != null && bytes.length > 0) ? Base64.getEncoder().encodeToString(bytes) : null;
        request.setAttribute("imagenEd_b64", imgB64);
      } catch (Exception ignore) { request.setAttribute("imagenEd_b64", null); }
    } catch (Exception e) {
      request.setAttribute("error", "No se pudieron obtener datos del servicio.");
    }

    request.setAttribute("nomEvento", evento);
    request.setAttribute("edicion", dtEd);
    request.setAttribute("tiporeg", dtTr);
    request.setAttribute("imagen",  imagen); 
    request.setAttribute("edicionFinalizada", edicionFinalizada);

    request.getRequestDispatcher("/WEB-INF/RegistroAEdicion/RegistroAEdicion.jsp")
           .forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession ses = request.getSession(false);
    String nick = (ses == null) ? null : (String) ses.getAttribute("usuarioLogueado");

    String evento  = trimOrNull(request.getParameter("evento"));
    String edicion = trimOrNull(request.getParameter("edicion"));
    String tiporeg = trimOrNull(firstNonBlank(request.getParameter("tipo"),
                                              request.getParameter("tiporeg")));
    String imagen  = trimOrNull(request.getParameter("imagen"));

    if (isBlank(evento) || isBlank(edicion) || isBlank(tiporeg)) {
      response.sendError(400, "Faltan: evento, edicion y tipo/tiporeg");
      return;
    }

    try {
      DtEdicion dtEd = port().getDTEdicion(evento, edicion);
      if (dtEd != null && dtEd.getFechaFin() != null && !dtEd.getFechaFin().isBlank()) {
        LocalDate fin = LocalDate.parse(dtEd.getFechaFin());
        if (fin.isBefore(LocalDate.now())) {
          request.setAttribute("error", "Esta edición ya finalizó. No es posible registrarse.");
          request.setAttribute("nomEvento", evento);
          request.setAttribute("edicion", dtEd);
          request.setAttribute("tiporeg", port().getDTTipoReg(evento, edicion, tiporeg));
          request.setAttribute("imagen",  imagen);
          request.setAttribute("edicionFinalizada", true);

          try {
            byte[] bytes = port().getImagenEdicion(evento, edicion);
            String imgB64 = (bytes != null && bytes.length > 0) ? Base64.getEncoder().encodeToString(bytes) : null;
            request.setAttribute("imagenEd_b64", imgB64);
          } catch (Exception ignore) { request.setAttribute("imagenEd_b64", null); }

          request.getRequestDispatcher("/WEB-INF/RegistroAEdicion/RegistroAEdicion.jsp").forward(request, response);
          return;
        }
      }

      port().altaRegistro(evento, edicion, tiporeg, nick, LocalDate.now().toString());

      request.setAttribute("evento",  evento);
      request.setAttribute("edicion", edicion);
      request.setAttribute("tiporeg", tiporeg);
      request.setAttribute("imagen",  imagen);
      getServletContext().getRequestDispatcher("/consultaDeRegistro").forward(request, response);

    } catch (Exception e) {
      request.setAttribute("error", (e.getMessage() == null) ? "No se pudo registrar." : e.getMessage());
      try {
        request.setAttribute("nomEvento", evento);
        request.setAttribute("edicion", port().getDTEdicion(evento, edicion));
        request.setAttribute("tiporeg", port().getDTTipoReg(evento, edicion, tiporeg));
        try {
          byte[] bytes = port().getImagenEdicion(evento, edicion);
          String imgB64 = (bytes != null && bytes.length > 0) ? Base64.getEncoder().encodeToString(bytes) : null;
          request.setAttribute("imagenEd_b64", imgB64);
        } catch (Exception ignore) { request.setAttribute("imagenEd_b64", null); }
      } catch (Exception ignore) {}
      request.setAttribute("imagen",  imagen);
      request.getRequestDispatcher("/WEB-INF/RegistroAEdicion/RegistroAEdicion.jsp").forward(request, response);
    }
  }

  private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
  private static String trimOrNull(String s) { return (s == null) ? null : s.trim(); }
  private static String firstNonBlank(String a, String b) { return isBlank(a) ? b : a; }
}
