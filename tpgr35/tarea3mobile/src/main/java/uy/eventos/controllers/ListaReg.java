package uy.eventos.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.Base64;

import webservices.*;

@WebServlet("/listaReg")
public class ListaReg extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private WSUsuario getPortUsuario(HttpServletRequest req) throws IOException {
    Properties p = (Properties) req.getServletContext().getAttribute("propiedades");
    URL url = new URL(p.getProperty("url_ws_usuarios"));
    return new WSUsuarioService(url).getWSUsuarioPort();
  }
  private WSEvento getPortEvento(HttpServletRequest req) throws IOException {
    Properties p = (Properties) req.getServletContext().getAttribute("propiedades");
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

    WSUsuario portUsr = getPortUsuario(req);
    WSEvento  portEvt = getPortEvento(req);

    List<DtRegistro> registros = new ArrayList<>();
    try {
      ListOfString wrap = portUsr.listarNombresRegistrosDeUsuario(nick);
      List<String> descripciones = (wrap != null && wrap.getItem()!=null) ? wrap.getItem() : List.of();

      for (String desc : descripciones) {
        String edicion = extraerEntreComillas(desc);
        if (edicion == null || edicion.isBlank()) continue;

        DtRegistro r = portUsr.obtenerRegistroDeUsuario(nick, edicion);
        if (r != null) {
          try {
            DtRegistro rFull = portEvt.getRegistro(r.getEvento(), r.getEdicion(), nick);
            if (rFull != null) r = rFull; 
          } catch (Exception ignore) { }

          registros.add(r);

          try {
            byte[] bytes = portEvt.getImagenEdicion(r.getEvento(), r.getEdicion());
            if (bytes != null && bytes.length > 0) {
              String b64 = Base64.getEncoder().encodeToString(bytes);
              req.setAttribute(imgKeyB64(r.getEvento(), r.getEdicion()), b64);
            }
          } catch (Exception ignore) {}
        }
      }
    } catch (Exception e) {
      req.setAttribute("error", "No se pudieron obtener tus registros: " + e.getMessage());
    }

    req.setAttribute("registros", registros);
    req.getRequestDispatcher("/WEB-INF/ConsultaDeRegistro/lista.jsp").forward(req, resp);
  }

  // Helpers 
  private static String extraerEntreComillas(String s) {
    int i = (s == null) ? -1 : s.indexOf('"');
    if (i < 0) return null;
    int j = s.indexOf('"', i+1);
    return (j > i+1) ? s.substring(i+1, j) : null;
  }

  private static String imgKeyB64(String ev, String ed) { return "imgb64:" + ev + "|" + ed; }

  @SuppressWarnings("unused")
  private static String imgKey(String ev, String ed) { return "img:" + ev + "|" + ed; }
}
