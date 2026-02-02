package uy.eventos.controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import webservices.WSEvento;
import webservices.WSEventoService;
import webservices.DtRegistro;

// iText 5
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@WebServlet("/descargarConstancia")
public class DescargarConstancia extends HttpServlet {

  private WSEvento portEvento(HttpServletRequest req) throws IOException {
    Properties p = (Properties) req.getServletContext().getAttribute("propiedades");
    URL url = new URL(p.getProperty("url_ws_eventos"));
    return new WSEventoService(url).getWSEventoPort();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    HttpSession ses = req.getSession(false);
    String nick = (ses == null) ? null : (String) ses.getAttribute("usuarioLogueado");
    String rol  = (ses == null) ? null : (String) ses.getAttribute("rol");
    if (nick == null || rol == null || !"Asistente".equalsIgnoreCase(rol)) {
      resp.sendError(HttpServletResponse.SC_UNAUTHORIZED); return;
    }

    String evento  = trim(req.getParameter("evento"));
    String edicion = trim(req.getParameter("edicion"));
    if (isBlank(evento) || isBlank(edicion)) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Faltan: evento, edicion"); return;
    }

    DtRegistro reg;
    try { reg = portEvento(req).getRegistro(evento, edicion, nick); }
    catch (Exception ex) { resp.sendError(502, "WS no disponible"); return; }
    if (reg == null || !reg.isAsistio()) {
      resp.sendError(403, "La constancia se habilita tras confirmar asistencia."); return;
    }

    String fileName = ("Constancia_" + evento + "_" + edicion + "_" + nick + ".pdf").replace(' ', '_');
    resp.setContentType("application/pdf");
    resp.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" +
        java.net.URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    resp.setHeader("Cache-Control", "no-store");

    try (OutputStream os = resp.getOutputStream()) {
      Document doc = new Document(PageSize.A4, 50, 50, 60, 50);
      PdfWriter.getInstance(doc, os);
      doc.open();

      Font h1   = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD);
      Font bold = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD);
      Font norm = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL);

      Paragraph title = new Paragraph("Constancia de Asistencia", h1);
      title.setAlignment(Element.ALIGN_CENTER);
      title.setSpacingAfter(20f);
      doc.add(title);

      doc.add(line("Evento: ", reg.getEvento(), bold, norm));
      doc.add(line("Edición: ", reg.getEdicion(), bold, norm));
      doc.add(line("Asistente: ", nick, bold, norm));
      doc.add(line("Tipo de registro: ", reg.getTipo(), bold, norm));
      doc.add(line("Fecha de registro: ", String.valueOf(reg.getFecha()), bold, norm));
      doc.add(line("Estado de asistencia: ", "Confirmada", bold, norm));

      doc.add(Chunk.NEWLINE);
      String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
      Paragraph footer = new Paragraph("Emitido automáticamente por eventos.uy — " + ts,
                                       FontFactory.getFont(FontFactory.HELVETICA, 9));
      footer.setAlignment(Element.ALIGN_RIGHT);
      doc.add(footer);

      doc.close();
    } catch (Exception e) {
      if (!resp.isCommitted()) { resp.reset(); resp.sendError(500, "No se pudo generar el PDF."); }
    }
  }

  private static Paragraph line(String k, String v, Font fk, Font fv) {
    Phrase ph = new Phrase();
    ph.add(new Chunk(k, fk));
    ph.add(new Chunk(v == null ? "-" : v, fv));
    Paragraph p = new Paragraph(ph);
    p.setSpacingAfter(6f);
    return p;
  }
  private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
  private static String trim(String s) { return s == null ? null : s.trim(); }
}
