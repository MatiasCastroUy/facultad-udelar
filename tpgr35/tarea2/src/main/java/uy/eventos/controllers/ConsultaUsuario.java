package uy.eventos.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import webservices.DtAsistente;
import webservices.DtEdicion;
import webservices.DtOrganizador;
import webservices.DtRegistro;
import webservices.DtUsuario;
import webservices.ListOfString;
import webservices.UsuarioNoExisteException_Exception;
import webservices.WSEvento;
import webservices.WSEventoService;
import webservices.WSUsuario;
import webservices.WSUsuarioService;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@WebServlet("/consultaUsuario")
public class ConsultaUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String nicknameConsultado = request.getParameter("nickname");

		if (nicknameConsultado == null || nicknameConsultado.trim().isEmpty()) {
			request.setAttribute("error", "Debe especificar un usuario para consultar");
			request.getRequestDispatcher("/WEB-INF/ConsultaUsuario/ConsultaUsuario.jsp").forward(request, response);
			return;
		}

		try {
			// Consumir el servicio web WSUsuario
			Properties propiedades = (Properties) getServletContext().getAttribute("propiedades");
			URL url_ws_usuarios = new URL(propiedades.getProperty("url_ws_usuarios"));
			WSUsuarioService service = new WSUsuarioService(url_ws_usuarios);
			WSUsuario portUsuario = service.getWSUsuarioPort();

			// Consumir el servicio web WSEvento
			URL url_ws_eventos = new URL(propiedades.getProperty("url_ws_eventos"));
			WSEventoService serviceEvento = new WSEventoService(url_ws_eventos);
			WSEvento portEvento = serviceEvento.getWSEventoPort();

			// Obtener información del usuario consultado
			DtUsuario usuarioConsultado = portUsuario.getUsuario(nicknameConsultado);

			if (usuarioConsultado == null) {
				request.setAttribute("error", "El usuario consultado no existe: " + nicknameConsultado);
				request.getRequestDispatcher("/WEB-INF/ConsultaUsuario/ConsultaUsuario.jsp").forward(request, response);
				return;
			}

			HttpSession sesion = request.getSession(false);
			String usuarioLogueado = null;
			if (sesion != null) {
				usuarioLogueado = (String) sesion.getAttribute("usuarioLogueado");
			}

			boolean esMismoPerfil = usuarioLogueado != null && usuarioLogueado.equals(nicknameConsultado);
			boolean esAsistente = usuarioConsultado instanceof DtAsistente;
			boolean esOrganizador = usuarioConsultado instanceof DtOrganizador;

			// Procesar lógica específica según el tipo de usuario y el contexto
			if (esAsistente) {
				procesarAsistente(request, portUsuario, portEvento, usuarioConsultado, esMismoPerfil);
			} else if (esOrganizador) {
				procesarOrganizador(request, portEvento, usuarioConsultado, esMismoPerfil);
			}

			// Verificar estado de seguimiento si hay usuario logueado y no es el mismo perfil
			boolean sigueAlUsuario = false;
			if (usuarioLogueado != null && !esMismoPerfil) {
				try {
					sigueAlUsuario = portUsuario.verificarSiSigue(usuarioLogueado, nicknameConsultado);
				} catch (Exception e) {
					sigueAlUsuario = false;
				}
			}

			// Obtener lista de usuarios seguidos si es el mismo perfil
			List<DtUsuario> usuariosSeguidos = new ArrayList<>();
			if (esMismoPerfil) {
				try {
					webservices.SetOfDTUsuario usuariosSeguidosWS = portUsuario.obtenerUsuariosSeguidos(nicknameConsultado);
					if (usuariosSeguidosWS != null && usuariosSeguidosWS.getItem() != null) {
						usuariosSeguidos.addAll(usuariosSeguidosWS.getItem());
					}
				} catch (Exception e) {
					usuariosSeguidos = new ArrayList<>();
				}
			}

			request.setAttribute("usuario", usuarioConsultado);
			request.setAttribute("esMismoPerfil", esMismoPerfil);
			request.setAttribute("nicknameConsultado", nicknameConsultado);
			request.setAttribute("esAsistente", esAsistente);
			request.setAttribute("esOrganizador", esOrganizador);
			request.setAttribute("sigueAlUsuario", sigueAlUsuario);
			request.setAttribute("usuarioLogueado", usuarioLogueado);
			request.setAttribute("usuariosSeguidos", usuariosSeguidos);

			byte[] imagen_bytes = portUsuario.getImagenUsuario(nicknameConsultado);
			String imagen = Base64.getEncoder().encodeToString(imagen_bytes);
			request.setAttribute("imagen_b64", imagen);

			request.getRequestDispatcher("/WEB-INF/ConsultaUsuario/ConsultaUsuario.jsp").forward(request, response);

		} catch (UsuarioNoExisteException_Exception usuarioNoExiste) {
			request.setAttribute("error", "Error al consultar usuario: " + usuarioNoExiste.getMessage());
			request.getRequestDispatcher("/WEB-INF/ConsultaUsuario/ConsultaUsuario.jsp").forward(request, response);
		} catch (ServletException servletException) {
			request.setAttribute("error", "Error de servlet: " + servletException.getMessage());
			request.getRequestDispatcher("/WEB-INF/ConsultaUsuario/ConsultaUsuario.jsp").forward(request, response);
		} catch (IOException ioException) {
			request.setAttribute("error", "Error de entrada/salida: " + ioException.getMessage());
			request.getRequestDispatcher("/WEB-INF/ConsultaUsuario/ConsultaUsuario.jsp").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("error", "Error inesperado: " + e.getMessage());
			request.getRequestDispatcher("/WEB-INF/ConsultaUsuario/ConsultaUsuario.jsp").forward(request, response);
		}
	}

	/**
	 * Procesa la lógica específica para usuarios asistentes
	 */
	private void procesarAsistente(HttpServletRequest request, WSUsuario portUsuario, WSEvento portEvento,
			DtUsuario usuario, boolean esMismoPerfil) throws Exception {

		// Obtener registros del asistente
		ListOfString registrosWS = portUsuario.listarNombresRegistrosDeUsuario(usuario.getNickname());
		List<String> nombresRegistros = new ArrayList<>();
		if (registrosWS != null && registrosWS.getItem() != null) {
			nombresRegistros.addAll(registrosWS.getItem());
		}

		if (esMismoPerfil) {
			// Caso 1: Asistente consultando su propio perfil
			List<Map<String, Object>> registrosConInfo = new ArrayList<>();

			for (String descripcionRegistro : nombresRegistros) {
				try {
					String nombreEdicion = extraerNombreEdicion(descripcionRegistro);

					if (nombreEdicion != null) {
						webservices.DtRegistro registroCompleto = portUsuario.obtenerRegistroDeUsuario(
								usuario.getNickname(), nombreEdicion);

						if (registroCompleto != null) {
							Map<String, Object> registroInfo = new HashMap<>();
							registroInfo.put("descripcion", descripcionRegistro);
							registroInfo.put("edicion", registroCompleto.getEdicion());

							String urlRegistro = request.getContextPath() + "/consultaDeRegistro" +
									"?evento=" + URLEncoder.encode(registroCompleto.getEvento(), "UTF-8") +
									"&edicion=" + URLEncoder.encode(registroCompleto.getEdicion(), "UTF-8") +
									"&tiporeg=" + URLEncoder.encode(registroCompleto.getTipo(), "UTF-8");
							registroInfo.put("url", urlRegistro);

							registrosConInfo.add(registroInfo);
						}
					}
				} catch (Exception e) {
					Map<String, Object> registroInfo = new HashMap<>();
					registroInfo.put("descripcion", descripcionRegistro);
					registroInfo.put("error", true);
					registrosConInfo.add(registroInfo);
				}
			}

			request.setAttribute("registrosConInfo", registrosConInfo);

		} else {
			// Caso 2: Visitante consultando asistente
			List<Map<String, Object>> edicionesRegistrado = new ArrayList<>();

			for (String descripcionRegistro : nombresRegistros) {
				try {
					String nombreEdicion = extraerNombreEdicion(descripcionRegistro);
					if (nombreEdicion == null || nombreEdicion.isBlank()) continue;

					// Cambio Matias
					webservices.DtRegistro reg = portUsuario.obtenerRegistroDeUsuario(
							usuario.getNickname(), nombreEdicion);
					if (reg == null) continue;

					String evento = reg.getEvento();
					// Cambio Matias

					Map<String, Object> edicionInfo = new HashMap<>();
					edicionInfo.put("edicion", nombreEdicion);
					edicionInfo.put("descripcion", descripcionRegistro);

					// Cambio Matias
					String urlEdicion = request.getContextPath()
							+ "/consultaEdicionEvento?evento="
							+ URLEncoder.encode(evento, "UTF-8")
							+ "&edicion=" + URLEncoder.encode(nombreEdicion, "UTF-8");
					edicionInfo.put("url", urlEdicion);
					// Cambio Matias
					
					edicionesRegistrado.add(edicionInfo);
				} catch (Exception ignore) {
					// continuar con el resto
				}
			}

			request.setAttribute("edicionesRegistrado", edicionesRegistrado);
		}
	}

	/**
	 * Procesa la lógica específica para usuarios organizadores
	 */
	private void procesarOrganizador(HttpServletRequest request, WSEvento portEvento,
			DtUsuario usuario, boolean esMismoPerfil) throws Exception {

		// Obtener todos los eventos
		ListOfString eventosWS = portEvento.listarEventos();
		List<String> todosEventos = new ArrayList<>();
		if (eventosWS != null && eventosWS.getItem() != null) {
			todosEventos.addAll(eventosWS.getItem());
		}

		List<Map<String, Object>> edicionesAceptadas = new ArrayList<>();
		List<Map<String, Object>> edicionesIngresadas = new ArrayList<>();
		List<Map<String, Object>> edicionesRechazadas = new ArrayList<>();

		// Recorrer todos los eventos para encontrar ediciones del organizador
		for (String nombreEvento : todosEventos) {
			try {
				ListOfString edicionesWS = portEvento.listarEdiciones(nombreEvento);
				if (edicionesWS != null && edicionesWS.getItem() != null) {
					for (String nombreEdicion : edicionesWS.getItem()) {
						try {
							DtEdicion edicion = portEvento.getDTEdicion(nombreEvento, nombreEdicion);

							if (edicion != null && edicion.getNicknameOrganizador().equals(usuario.getNickname())) {
								Map<String, Object> edicionInfo = crearInfoEdicion(request, nombreEvento, edicion);

								String estado = edicion.getEstado().name();

								if ("Aceptada".equalsIgnoreCase(estado)) {
									edicionesAceptadas.add(edicionInfo);
								} else if ("Ingresada".equalsIgnoreCase(estado)) {
									edicionesIngresadas.add(edicionInfo);
								} else if ("Rechazada".equalsIgnoreCase(estado)) {
									edicionesRechazadas.add(edicionInfo);
								}
							}
						} catch (Exception e) {
							// Ignorar ediciones con errores
						}
					}
				}
			} catch (Exception e) {
				// Ignorar eventos con errores
			}
		}

		request.setAttribute("edicionesAceptadas", edicionesAceptadas);
		if (esMismoPerfil) {
			request.setAttribute("edicionesIngresadas", edicionesIngresadas);
			request.setAttribute("edicionesRechazadas", edicionesRechazadas);
		}
	}

	/**
	 * Extrae el nombre de la edición del string de descripción del registro
	 */
	private String extraerNombreEdicion(String descripcionRegistro) {
		try {
			int startIndex = descripcionRegistro.indexOf("\"") + 1;
			int endIndex = descripcionRegistro.indexOf("\"", startIndex);
			if (startIndex > 0 && endIndex > startIndex) {
				return descripcionRegistro.substring(startIndex, endIndex);
			}
		} catch (Exception e) {
			// Ignorar errores de parsing
		}
		return null;
	}

	/**
	 * Crea un mapa con la información de una edición para el JSP
	 */
	private Map<String, Object> crearInfoEdicion(HttpServletRequest request, String nombreEvento, DtEdicion edicion) {
		Map<String, Object> edicionInfo = new HashMap<>();
		edicionInfo.put("evento", nombreEvento);
		edicionInfo.put("edicion", edicion.getNombre());
		edicionInfo.put("estado", edicion.getEstado());
		edicionInfo.put("fechaInicio", edicion.getFechaInicio());
		edicionInfo.put("fechaFin", edicion.getFechaFin());
		edicionInfo.put("pais", edicion.getPais());
		edicionInfo.put("ciudad", edicion.getCiudad());

		try {
			String urlEdicion = request.getContextPath() + "/consultaEdicionEvento?evento="
					+ URLEncoder.encode(nombreEvento, "UTF-8") + "&edicion="
					+ URLEncoder.encode(edicion.getNombre(), "UTF-8");
			edicionInfo.put("url", urlEdicion);
		} catch (Exception e) {
			edicionInfo.put("url", "#");
		}

		return edicionInfo;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	//
}
