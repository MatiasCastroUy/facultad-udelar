package uy.eventos.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.Base64;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import webservices.DtEvento;
import webservices.EdicionRepetidaException_Exception;
import webservices.ListOfString;
import webservices.WSEvento;
import webservices.WSEventoService;

@WebServlet("/altaEdicion")
@MultipartConfig(
		fileSizeThreshold = 1024 * 1024, 
		maxFileSize = 10 * 1024 * 1024, 
		maxRequestSize = 50 * 1024 * 1024
)
public class AltaDeEdicion extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private String error = "";
	private Map<String, String> imagenesEventos;

	public AltaDeEdicion() { super(); }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		mostrarAltaEdicion(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		crearEdicion(request, response);
	}

	private void mostrarAltaEdicion(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher;
		request.setCharacterEncoding("UTF-8");

		error = "";

		// Obtener lista de eventos
		obtenerEventos(request);

		// Mostrar página
		request.setAttribute("error", error);
		dispatcher = request.getRequestDispatcher("/WEB-INF/AltaEdicion/AltaEdicion.jsp");
		dispatcher.forward(request, response);
	}

	private void crearEdicion(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher;
		request.setCharacterEncoding("UTF-8");

		error = "";

		// Obtener lista de eventos
		obtenerEventos(request);

		if (validarCampos(request)) {
			try {
				// Crear edición
				Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
				URL url_ws_eventos = new URL(propiedades.getProperty("url_ws_eventos"));
		        WSEventoService servicio = new WSEventoService(url_ws_eventos);
		        WSEvento port = servicio.getWSEventoPort();
		        
				HttpSession session = request.getSession(false);
				String nombreEvento = request.getParameter("evento");
				String nickOrganizador = (String) session.getAttribute("usuarioLogueado");
				String nombreEdicion = request.getParameter("nombreEdicion");
				String sigla = request.getParameter("siglas");
				String pais = request.getParameter("pais");
				String ciudad = request.getParameter("ciudad");
				String fechaIni = request.getParameter("fechaIni");
				String fechaFin = request.getParameter("fechaFin");
				String fechaAlta = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String urlVideo = request.getParameter("urlVideo");

				Part filePart = request.getPart("foto");
				byte[] img_bytes = new byte[0];
				if (filePart != null && filePart.getSize() != 0 && !filePart.getSubmittedFileName().isEmpty()) {
					try {
						img_bytes = filePart.getInputStream().readAllBytes();
					} catch (IOException io_exc) {
						System.out.println("(Alta de Edición) Error al leer imagen.");
						io_exc.printStackTrace();
					}
				}
				
				port.altaEdicionEvento(nombreEvento, nickOrganizador, nombreEdicion, sigla, pais, ciudad,
						fechaIni, fechaFin, fechaAlta, "Ingresada", "-", urlVideo);

				port.setImagenEdicion(nombreEvento, nombreEdicion, img_bytes);

				// Redireccionar
				request.setAttribute("error", error);
				request.setAttribute("edicionUrl", nombreEdicion);
				request.setAttribute("eventoUrl", nombreEvento);
				dispatcher = request.getRequestDispatcher("/WEB-INF/AltaEdicion/AltaEdicion.jsp");
				dispatcher.forward(request, response);
			} catch (EdicionRepetidaException_Exception exc) {
				// Mostrar excepción
				request.setAttribute("error", "Ya hay una edición con ese nombre.");
				System.out.println(exc.getMessage());
				dispatcher = request.getRequestDispatcher("/WEB-INF/AltaEdicion/AltaEdicion.jsp");
				dispatcher.forward(request, response);
			}
			return;
		}

		// Ocurrió un error. Mostrar la página.
		request.setAttribute("error", error);
		dispatcher = request.getRequestDispatcher("/WEB-INF/AltaEdicion/AltaEdicion.jsp");
		dispatcher.forward(request, response);
	}

	private Boolean validarCampos(HttpServletRequest request) {
		Boolean camposValidos = true;
		String errorMsg = "Los siguientes campos son requeridos: ";
		List<String> camposFaltantes = new ArrayList<>();

		if (request.getParameter("evento").isBlank()) {
			camposValidos = false;
			camposFaltantes.add("Evento");
		}

		if (request.getParameter("nombreEdicion").isBlank()) {
			camposValidos = false;
			camposFaltantes.add("Nombre de Edición");
		}

		if (request.getParameter("siglas").isBlank()) {
			camposValidos = false;
			camposFaltantes.add("Siglas");
		}

		if (request.getParameter("ciudad").isBlank()) {
			camposValidos = false;
			camposFaltantes.add("Ciudad");
		}

		if (request.getParameter("pais").isBlank()) {
			camposValidos = false;
			camposFaltantes.add("País");
		}

		if (request.getParameter("fechaIni") == null || request.getParameter("fechaIni").isEmpty()) {
			camposValidos = false;
			camposFaltantes.add("Fecha de Inicio");
		}

		if (request.getParameter("fechaFin") == null || request.getParameter("fechaFin").isEmpty()) {
			camposValidos = false;
			camposFaltantes.add("Fecha de Fin");
		}

		if (!camposValidos) {
			error = errorMsg + String.join(", ", camposFaltantes) + ".";

			request.setAttribute("evento", request.getParameter("evento"));
			request.setAttribute("nombreEdicion", request.getParameter("nombreEdicion"));
			request.setAttribute("siglas", request.getParameter("siglas"));
			request.setAttribute("ciudad", request.getParameter("ciudad"));
			request.setAttribute("pais", request.getParameter("pais"));
			request.setAttribute("fechaIni", request.getParameter("fechaIni"));
			request.setAttribute("fechaFin", request.getParameter("fechaFin"));
		}

		return camposValidos;
	}

	private Boolean validarSesion(HttpServletRequest request) {
		Boolean sesionValida = false;
		HttpSession session = request.getSession(false);

		if (session != null) {
			String rolUsuario = (String) session.getAttribute("rol");
			sesionValida = rolUsuario != null && rolUsuario.equalsIgnoreCase("organizador");
		}

		return sesionValida;
	}

	private void obtenerEventos(HttpServletRequest request) throws IOException {
		if (validarSesion(request)) {
			List<DtEvento> evtLst = new ArrayList<>();

			Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
			URL url_ws_eventos = new URL(propiedades.getProperty("url_ws_eventos"));
	        WSEventoService servicio = new WSEventoService(url_ws_eventos);
	        WSEvento port = servicio.getWSEventoPort();
	        ListOfString evtStrLst = port.listarEventosSinFinalizar();

			for (String evento : evtStrLst.getItem()) {
				DtEvento dtEvento = port.getDTEvento(evento);
				evtLst.add(dtEvento);
			}

			request.setAttribute("eventos", evtLst);
			
			// Imágenes
			imagenesEventos = new HashMap<String, String>();
			for (String evento : evtStrLst.getItem()) {
				byte[] img_evt_bytes = port.getImagenEvento(evento);
				String imagenEvento_b64 = img_evt_bytes.length > 0 ? Base64.getEncoder().encodeToString(img_evt_bytes) : null;
				
				imagenesEventos.put(evento, imagenEvento_b64);
			}
			
			request.setAttribute("imagenes", imagenesEventos);
		}
	}
}