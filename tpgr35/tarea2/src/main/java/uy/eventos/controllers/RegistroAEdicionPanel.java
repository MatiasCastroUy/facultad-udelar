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
import webservices.DtEdicion;
import webservices.DtTipoRegistro;
import webservices.ListOfString;

@SuppressWarnings("serial")
@WebServlet("/registroAEdicionPanel")
public class RegistroAEdicionPanel extends HttpServlet {

  private WSEvento port() throws IOException { 
	  Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
	  URL url_ws_eventos = new URL(propiedades.getProperty("url_ws_eventos"));
      WSEventoService service = new WSEventoService(url_ws_eventos);
	  return service.getWSEventoPort(); 
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    req.setCharacterEncoding("UTF-8");

    HttpSession ses = req.getSession(false);
    String rol = (ses == null) ? null : (String) ses.getAttribute("rol");
    if (rol == null || !"ASISTENTE".equalsIgnoreCase(rol)) {
      req.setAttribute("error", "Debe iniciar sesión como asistente para registrarse a ediciones.");
      req.getRequestDispatcher("/WEB-INF/RegistroAEdicion/RegistroAEdicionPanel.jsp").forward(req, resp);
      return;
    }

    String eventoSel  = trim(req.getParameter("evento"));
    String edicionSel = trim(req.getParameter("edicion"));
    String tipoSel    = trim(req.getParameter("tiporeg"));

    ListOfString eventosWS   = null;
    ListOfString edicionesWS = null;
    ListOfString tiposWS     = null;

    DtEdicion      dtEd   = null;
    DtTipoRegistro dtTipo = null;
    Boolean edicionFinalizada = null;

    try {
      eventosWS = port().listarEventos();

      if (!isBlank(eventoSel)) {
        edicionesWS = port().listarEdiciones(eventoSel);
      } else {
        edicionSel = null;
      }

      if (!isBlank(eventoSel) && !isBlank(edicionSel)) {
        dtEd = port().getDTEdicion(eventoSel, edicionSel);

        if (dtEd != null && dtEd.getFechaFin() != null && !dtEd.getFechaFin().isBlank()) {
          edicionFinalizada = LocalDate.parse(dtEd.getFechaFin()).isBefore(LocalDate.now());
        }
      }

      if (dtEd != null) {
        tiposWS = port().listarTipoRegistro(eventoSel, edicionSel);
      } else {
        tipoSel = null;
      }

      if (dtEd != null && !isBlank(tipoSel)) {
        dtTipo = port().getDTTipoReg(eventoSel, edicionSel, tipoSel);
      }

    } catch (Exception e) {
      if (req.getAttribute("error") == null) {
        req.setAttribute("error", "No se pudieron obtener los datos desde el servidor.");
      }
    }

    req.setAttribute("eventos",  eventosWS);
    req.setAttribute("ediciones", edicionesWS);
    req.setAttribute("tipos",     tiposWS);

    req.setAttribute("dtEd",   dtEd);
    req.setAttribute("dtTipo", dtTipo);

    req.setAttribute("eventoSel",  eventoSel);
    req.setAttribute("edicionSel", edicionSel);
    req.setAttribute("tipoSel",    tipoSel);

    req.setAttribute("edicionFinalizada", edicionFinalizada);

    req.getRequestDispatcher("/WEB-INF/RegistroAEdicion/RegistroAEdicionPanel.jsp").forward(req, resp);
  }

  // --- POST: confirmar registro y forward al detalle  ---
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    req.setCharacterEncoding("UTF-8");

    HttpSession ses = req.getSession(false);
    String rol  = (ses == null) ? null : (String) ses.getAttribute("rol");
    String nick = (ses == null) ? null : (String) ses.getAttribute("usuarioLogueado");
    if (rol == null || !"ASISTENTE".equalsIgnoreCase(rol) || nick == null) {
      req.setAttribute("error", "Debe iniciar sesión como asistente para registrarse.");
      doGet(req, resp);
      return;
    }

    String evento  = trim(req.getParameter("evento"));
    String edicion = trim(req.getParameter("edicion"));
    String tiporeg = trim(req.getParameter("tiporeg"));
    String imagen  = trim(req.getParameter("imagen"));

    if (isBlank(evento) || isBlank(edicion) || isBlank(tiporeg)) {
      req.setAttribute("error", "Faltan datos para confirmar el registro.");
      doGet(req, resp);
      return;
    }

    try {
      DtEdicion dt = port().getDTEdicion(evento, edicion);
      if (dt != null && dt.getFechaFin() != null && !dt.getFechaFin().isBlank()) {
        if (LocalDate.parse(dt.getFechaFin()).isBefore(LocalDate.now())) {
          req.setAttribute("error", "Esta edición ya finalizó. No es posible registrarse.");
          req.setAttribute("eventoSel",  evento);
          req.setAttribute("edicionSel", edicion);
          req.setAttribute("tipoSel",    tiporeg);
          doGet(req, resp);
          return;
        }
      }
    } catch (Exception ignore) {
    }


    try {
      port().altaRegistro(evento, edicion, tiporeg, nick, LocalDate.now().toString());

      req.setAttribute("evento",  evento);
      req.setAttribute("edicion", edicion);
      req.setAttribute("tiporeg", tiporeg);
      req.setAttribute("imagen",  imagen);
      getServletContext().getRequestDispatcher("/consultaDeRegistro").forward(req, resp);
      return;

    } catch (Exception e) {
      String msg = e.getMessage();
      req.setAttribute("error", (msg == null) ? "No se pudo registrar." : msg);
      req.setAttribute("eventoSel",  evento);
      req.setAttribute("edicionSel", edicion);
      req.setAttribute("tipoSel",    tiporeg);
      doGet(req, resp);
    }
  }

  // utils
  private static String trim(String s)     { return (s == null) ? null : s.trim(); }
  private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}

