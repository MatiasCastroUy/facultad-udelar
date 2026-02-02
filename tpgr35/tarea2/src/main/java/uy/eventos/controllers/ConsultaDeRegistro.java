package uy.eventos.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import webservices.WSEvento;
import webservices.WSEventoService;
import webservices.DtRegistro;
import webservices.DtEdicion;

import java.util.Base64;

@SuppressWarnings("serial")
@WebServlet("/consultaDeRegistro")
public class ConsultaDeRegistro extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException { process(req, resp); }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException { process(req, resp); }

  private WSEvento getPortEvento() throws IOException {
    Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
    URL url_ws_eventos = new URL(propiedades.getProperty("url_ws_eventos"));
    WSEventoService service = new WSEventoService(url_ws_eventos);
    return service.getWSEventoPort();
  }

  private void process(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String evento  = trimOrNull(objToString(req.getAttribute("evento")));
    String edicion = trimOrNull(objToString(req.getAttribute("edicion")));
    String tiporeg = trimOrNull(objToString(req.getAttribute("tiporeg")));
    String imagen  = trimOrNull(objToString(req.getAttribute("imagen"))); // (no se usa ya)

    if (isBlank(evento))  evento  = trimOrNull(req.getParameter("evento"));
    if (isBlank(edicion)) edicion = trimOrNull(req.getParameter("edicion"));
    if (isBlank(tiporeg)) tiporeg = trimOrNull(req.getParameter("tiporeg"));
    if (isBlank(imagen))  imagen  = trimOrNull(req.getParameter("imagen"));

    if (isBlank(evento) || isBlank(edicion) || isBlank(tiporeg)) {
      resp.sendError(400, "Faltan: evento, edicion, tiporeg");
      return;
    }

    String nickUsuario = (String) req.getSession().getAttribute("usuarioLogueado");

    WSEvento port = getPortEvento();
    try {
      DtRegistro dtRegistro = port.getRegistro(evento, edicion, nickUsuario);
      req.setAttribute("dtRegistro", dtRegistro);

      try {
        byte[] bytes = port.getImagenEdicion(evento, edicion);
        String imgB64 = (bytes != null && bytes.length > 0) ? Base64.getEncoder().encodeToString(bytes) : null;
        req.setAttribute("imagenEd_b64", imgB64);
      } catch (Exception ignore) { req.setAttribute("imagenEd_b64", null); }
    } catch (Exception e) {
      req.setAttribute("error", e.getMessage());
    }

    req.setAttribute("evento",  evento);
    req.setAttribute("edicion", edicion);
    req.setAttribute("tiporeg", tiporeg);

    req.getRequestDispatcher("/WEB-INF/ConsultaDeRegistro/ConsultaDeRegistro.jsp").forward(req, resp);
  }

  private static boolean isBlank(String stg) { return stg == null || stg.trim().isEmpty(); }
  private static String trimOrNull(String stg) { return (stg == null) ? null : stg.trim(); }
  private static String objToString(Object obj) { return (obj == null) ? null : obj.toString(); }
}
