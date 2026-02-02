package presentacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import excepciones.CargaDatosRepetidaExcepcion;
import excepciones.CategoriaEventoVacia;
import excepciones.CategoriaRepetidaExcepcion;
import excepciones.CategoriaVaciaExcepcion;
import excepciones.ContrasenaInvalidaException;
import excepciones.CupoLlenoExcepcion;
import excepciones.EdicionRepetidaException;
import excepciones.ErroresCargaDatosExcepcion;
import excepciones.EventoRepetido;
import excepciones.TipoRegistroRepetidoExcepcion;
import excepciones.UsuarioRepetidoException;
import excepciones.YaRegistradoExcepcion;
import excepciones.UsuarioCorreoRepetidoException;
import excepciones.UsuarioNoExisteException;
import logica.Fabrica;
import logica.IControladorEvento;
import logica.IControladorUsuario;

public class CargaDatos {
	private static boolean cargados = false;
	private static String pathDatos = null;

	private LocalDate strToDate(String stg) { // crea fecha de string dd/mm/yyyy
		String[] str = stg.split("/");
		int anio = Integer.parseInt(str[2]);
		int mes = Integer.parseInt(str[1]);
		int dia = Integer.parseInt(str[0]);
		return LocalDate.of(anio, mes, dia);
	}

	private CargaDatos() throws ErroresCargaDatosExcepcion {
		boolean errores = false;
		Fabrica fab = Fabrica.getInstance();
		IControladorEvento ctrEv = fab.getIControladorEvento();
		IControladorUsuario ctrUs = fab.getIControladorUsuario();

		// leer propiedades
		Properties propiedades = Principal.getPropiedades();

		pathDatos = propiedades.getProperty("path_carga_datos");
		if (pathDatos == null) {
			throw new ErroresCargaDatosExcepcion("NO SE CARGÓ LA PROPIEDAD path_carga_datos");
		}
		if (!pathDatos.endsWith("/")) {
			pathDatos += "/";
		}

		// ALTA DE CATEGORIAS
		Path path_cat = Paths.get(pathDatos + "2025Categorias.csv");
		try (BufferedReader buffr = Files.newBufferedReader(path_cat)) {
			buffr.readLine(); // primera fila, no nos interesa
			String linea;
			while ((linea = buffr.readLine()) != null) {
				String[] celdas = linea.split(";"); // devuelve un par [REF, nombreCat]
				if (celdas.length > 1) {
					String nombreCat = celdas[1];
					try {
						ctrEv.altaCategoria(nombreCat);
					} catch (CategoriaRepetidaExcepcion e) {
						errores = true;
						System.out.println(e.getMessage());
					} catch (CategoriaVaciaExcepcion e) {
						errores = true;
						System.out.println(e.getMessage());
					}
				}

			}

		} catch (IOException e) {
			errores = true;
			System.out.println("No se pudo cargar " + path_cat);
		}

		// ALTA DE USUARIOS
		Path path_us = Paths.get(pathDatos + "2025Usuarios.csv");
		Path path_asis = Paths.get(pathDatos + "2025Usuarios-Asistentes.csv");
		Path path_org = Paths.get(pathDatos + "2025Usuarios-Organizadores.csv");

		try (BufferedReader br_us = Files.newBufferedReader(path_us)) {
			br_us.readLine(); // primera fila, no nos interesa

			String lineaUsuario;
			while ((lineaUsuario = br_us.readLine()) != null) {
				String[] celdas_us = lineaUsuario.split(";");
				// formato = [Ref,Tipo,Nickname,Nombre,correo,contraseña,imagen]
				if (celdas_us.length > 1) {
					String ref_us = celdas_us[0];

					boolean esAsistente = celdas_us[1].trim().equals("A");
					String nick = celdas_us[2];
					String nom = celdas_us[3];
					String correo = celdas_us[4];
					String contrasena = celdas_us[5];
					String imagen = celdas_us[6];

					// determinantes (dependen de otro doc)
					String apellido = null;
					String fechaNac = null;
					String desc = null;
					String sitioWeb = null;

					if (esAsistente) { // se buscan detalles en tabla de asistentes
						try (BufferedReader br_asis = Files.newBufferedReader(path_asis)) {
							String lineaAsis;
							br_asis.readLine(); // descartamos primera linea
							// BUSCAMOS LA REF DEL ASISTENTE
							while ((lineaAsis = br_asis.readLine()) != null
									&& !lineaAsis.split(";")[0].trim().equals(ref_us))
								;
							if (lineaAsis != null && lineaAsis.split(";")[0].trim().equals(ref_us)) {
								// formato csv: [ref,apellido,fechanac,(institucion)]
								String[] celdas_as = lineaAsis.split(";");
								apellido = celdas_as[1];
								fechaNac = celdas_as[2];
							} else {
								errores = true;
								System.out.println("ERROR: Asistente " + ref_us + " no encontrado");
							}

						} catch (IOException e) {
							errores = true;
							System.out.println("Error al leer " + path_asis);
						}

					} else { // se buscan detalles en la tabla organizadores
						try (BufferedReader br_org = Files.newBufferedReader(path_org)) {
							String lineaOrg;
							br_org.readLine(); // descartamos primera linea
							// BUSCAMOS LA REF DEL ORGANIZADOR
							while ((lineaOrg = br_org.readLine()) != null
									&& !lineaOrg.split(";")[0].trim().equals(ref_us))
								;
							if (lineaOrg != null && lineaOrg.split(";")[0].equals(ref_us)) {
								// formato csv: [ref,desc,sitioweb]
								String[] celdas_org = lineaOrg.split(";");
								desc = celdas_org[1];
								if (celdas_org.length >= 3)
									sitioWeb = celdas_org[2];
								else
									sitioWeb = "";
							} else {
								errores = true;
								System.out.println("ERROR: Organizador " + ref_us + " no encontrado");
							}

						} catch (IOException e) {
							errores = true;
							System.out.println("Error al leer " + path_org);
						}
					}

					LocalDate fechaNacLocalDate = null;
					if (fechaNac != null && !fechaNac.isEmpty()) {
						fechaNacLocalDate = strToDate(fechaNac);
					}
					try {
						ctrUs.registrarUsuario(nick, nom, correo, contrasena, contrasena, imagen, esAsistente, apellido,
								fechaNacLocalDate, desc, sitioWeb);
					} catch (UsuarioRepetidoException e) {
						errores = true;
						System.out.println(e.getMessage());
					} catch (UsuarioCorreoRepetidoException e) {
						errores = true;
						System.out.println(e.getMessage());
					} catch (ContrasenaInvalidaException e) {
						e.printStackTrace();
					}

				}
			}

		} catch (IOException e) {
			errores = true;
			System.out.println("Error al leer datos. (Usuarios)");
		}

		// USUARIOS SEGUIDOS
		Path path_seg = Paths.get(pathDatos + "2025SeguidoresSeguidos.csv");
		try (BufferedReader br_seg = Files.newBufferedReader(path_seg)) {
			br_seg.readLine();
			String linea_seg;
			while ((linea_seg = br_seg.readLine()) != null) {
				// formato [ref,ref_seguidor,nick_seguidor,reg_seguido,nick_seguido]
				String[] celdas_seg = linea_seg.split(";");
				String nickSeguidor = celdas_seg[2];
				String nickSeguido = celdas_seg[4];

				try {
					ctrUs.seguirUsr(nickSeguidor, nickSeguido);
				} catch (UsuarioNoExisteException exc) {
					System.out.println("Error al seguir usuarios. Seguidor=" + nickSeguidor + " Seguido=" + nickSeguido + " :" + exc.getMessage());
				}
			}
		} catch (IOException e1) {
			errores = true;
			System.out.println("Error al leer datos. (Seguidores/seguidos)");
		}

		// ALTA DE EVENTOS
		Path path_ev = Paths.get(pathDatos + "2025Eventos.csv");
		try (BufferedReader br_us = Files.newBufferedReader(path_ev)) {
			br_us.readLine(); // descartamos primera linea
			String linea_ev;
			while ((linea_ev = br_us.readLine()) != null) {
				// formato [ref,nombre,desc,sigla,fechaalta,categorias,imagen,finalizado,cantvisitas]
				String[] celdas_ev = linea_ev.split(";");
				String nom = celdas_ev[1];
				String desc = celdas_ev[2];
				String sigla = celdas_ev[3];
				String str_fechaAlta = celdas_ev[4];
				String[] ref_cats = celdas_ev[5].split(",");
				String imagen = celdas_ev[6];
				imagen = imagen.trim().equals("-") ? null : imagen;
				boolean finalizado = celdas_ev[7].equalsIgnoreCase("Si");
				int cantVisitas = Integer.parseInt(celdas_ev[8]);

				Set<String> cats = new HashSet<String>();
				for (int i = 0; i < ref_cats.length; i++) {
					try (BufferedReader br_cat = Files.newBufferedReader(path_cat)) {
						br_cat.readLine();
						String linea_cat;
						while ((linea_cat = br_cat.readLine()) != null
								&& !linea_cat.split(";")[0].trim().equals(ref_cats[i].trim()))
							;
						if (linea_cat != null)
							cats.add(linea_cat.split(";")[1]);
						else {
							errores = true;
							System.out.println("ERROR: Categoría " + ref_cats[i] + " no encontrada");
						}

					} catch (IOException e) {
						errores = true;
						System.out.println("Error al leer datos. (Evento -> Categorias)");
					}
				}

				LocalDate fechaAlta = strToDate(str_fechaAlta);

				try {
					ctrEv.altaEvento(nom, desc, fechaAlta, sigla, cats, imagen);
					if (finalizado) ctrEv.finalizarEvento(nom);
					for (int i = 0; i < cantVisitas; i++) ctrEv.sumarVisita(nom);
				} catch (EventoRepetido e) {
					errores = true;
					System.out.println(e.getMessage());
				} catch (CategoriaEventoVacia e) {
					System.out.println(e.getMessage());
				}

			}

		} catch (IOException e) {
			errores = true;
			System.out.println("Error al leer datos. (Eventos)");
		}

		// ALTA DE EDICIONES
		Path path_ed = Paths.get(pathDatos + "2025EdicionesEventos.csv");

		try (BufferedReader br_ed = Files.newBufferedReader(path_ed)) {
			br_ed.readLine(); // descartamos primera linea
			String linea_ed;
			while ((linea_ed = br_ed.readLine()) != null) {
				// formato [ref, ref_evento, org, nombre, sigla, ciudad, pais, fechaInicio,
				// fechaFin, fechaAlta,estado,imagen,urlvideo]
				String[] celdas_ed = linea_ed.split(";");
				String ref_ev2 = celdas_ed[1];
				String ref_org = celdas_ed[2];
				String nom = celdas_ed[3];
				String sig = celdas_ed[4];
				String ciu = celdas_ed[5];
				String pais = celdas_ed[6];
				LocalDate fechaIni = strToDate(celdas_ed[7]);
				LocalDate fechaFin = strToDate(celdas_ed[8]);
				LocalDate fechaAlta = strToDate(celdas_ed[9]);

				String estado = celdas_ed[10];
				String imagen = celdas_ed[11];
				String urlVideo = celdas_ed.length >= 13 ? celdas_ed[12] : null;

				String nombreEvt = null;
				try (BufferedReader br_ev = Files.newBufferedReader(path_ev)) {
					br_ev.readLine(); // descartamos primera linea
					String linea_ev;
					while ((linea_ev = br_ev.readLine()) != null
							&& !linea_ev.split(";")[0].trim().equals(ref_ev2.trim()))
						;
					if (linea_ev != null && linea_ev.split(";")[0].equals(ref_ev2)) {
						nombreEvt = linea_ev.split(";")[1];
					} else {
						errores = true;
						System.out.println("ERROR: No se encontró el evento " + ref_ev2);
					}

				} catch (IOException e) {
					errores = true;
					System.out.println("Error al cargar datos (Edicion -> Eventos");
				}

				String nickOrg = null;
				try (BufferedReader br_us2 = Files.newBufferedReader(path_us)) {
					br_us2.readLine(); // descartamos primera linea
					String linea_us;
					while ((linea_us = br_us2.readLine()) != null
							&& !linea_us.split(";")[0].trim().equals(ref_org.trim()))
						;
					if (linea_us != null && linea_us.split(";")[0].equals(ref_org)) {
						nickOrg = linea_us.split(";")[2];
					} else {
						errores = true;
						System.out.println("ERROR: No se encontró el organizador " + ref_org);
					}

				} catch (IOException e) {
					errores = true;
					System.out.println("Error al cargar datos (Edicion -> Organizador");
				}

				try {
					ctrEv.altaEdicionEvento(nombreEvt, nickOrg, nom, sig, pais, ciu, fechaIni, fechaFin, fechaAlta, estado, imagen, urlVideo);

				} catch (EdicionRepetidaException e) {
					errores = true;
					System.out.println(e.getMessage());
				}
			}
		} catch (IOException e) {
			errores = true;
			System.out.println("Error al leer datos. (Ediciones)");
		}

		// TIPOS DE REGISTRO
		Path path_tr = Paths.get(pathDatos + "2025TipoRegistro.csv");
		try (BufferedReader br_tr = Files.newBufferedReader(path_tr)) {
			br_tr.readLine(); // descartamos primera linea
			String linea_tr;
			while ((linea_tr = br_tr.readLine()) != null) {
				// formato: [ref,ref_ed_ev,nombre,desc,costo,cupo]
				String[] celdas_tr = linea_tr.split(";");
				String ref_ed = celdas_tr[1];
				String nombre = celdas_tr[2];
				String desc = celdas_tr[3];
				float costo = Float.parseFloat(celdas_tr[4]);
				int cupo = Integer.parseInt(celdas_tr[5]);

				// Buscamos en Edicion para hallar nombre de edicion y referencia del evento:
				String edicion = "";
				String ref_evento = "";
				try (BufferedReader br_ed2 = Files.newBufferedReader(path_ed)) {
					br_ed2.readLine();
					String linea_ed2;
					while ((linea_ed2 = br_ed2.readLine()) != null && !linea_ed2.split(";")[0].equals(ref_ed))
						;
					if (linea_ed2 != null && linea_ed2.split(";")[0].equals(ref_ed)) {
						ref_evento = linea_ed2.split(";")[1];
						edicion = linea_ed2.split(";")[3];
					} else {
						errores = true;
						System.out.println("No se encontró la edición " + ref_ed);
					}

				} catch (IOException e) {
					errores = true;
					System.out.println("Error al cargar datos (TipoRegistro -> Edicion");
				}

				// Buscamos en Eventos para hallar el nombre del evento:
				String evento = "";
				try (BufferedReader br_ev2 = Files.newBufferedReader(path_ev)) {
					br_ev2.readLine();
					String linea_ev2;
					while ((linea_ev2 = br_ev2.readLine()) != null && !linea_ev2.split(";")[0].equals(ref_evento))
						;
					if (linea_ev2 != null && linea_ev2.split(";")[0].equals(ref_evento)) {
						evento = linea_ev2.split(";")[1];
					} else {
						errores = true;
						System.out.println("No se encontró el evento " + ref_evento);
					}
				} catch (IOException e) {
					errores = true;
					System.out.println("Error al cargar datos (TipoRegistro -> Evento");
				}

				try {
					ctrEv.crearTipoRegistro(evento, edicion, nombre, desc, costo, cupo);
				} catch (TipoRegistroRepetidoExcepcion e) {
					errores = true;
					System.out.println(e.getMessage());
				}

			}

		} catch (IOException e) {
			errores = true;
			System.out.println("Error al leer datos. (Tipo Registro)");
		}

		// REGISTROS A EDICION EVENTO
		Path path_reg = Paths.get(pathDatos + "2025Registros.csv");
		try (BufferedReader br_reg = Files.newBufferedReader(path_reg)) {
			br_reg.readLine(); // descartamos primera linea
			String linea_reg;
			while ((linea_reg = br_reg.readLine()) != null) {
				// formato: [ref_reg,ref_us,ref_ed,ref_tr,fechaRegistro,costo,patrocinio, asiste]
				String[] celdas_reg = linea_reg.split(";");
				String ref_us3 = celdas_reg[1];
				String ref_ed3 = celdas_reg[2];
				String ref_tr3 = celdas_reg[3];
				LocalDate fechaRegistro = strToDate(celdas_reg[4]);
				String patrocinio = null;
				if (celdas_reg.length >= 7)
					patrocinio = celdas_reg[6];
				boolean asiste = celdas_reg.length >= 8 && celdas_reg[7].trim().equalsIgnoreCase("S");

				// Buscamos el nickname del usuario(asistente)
				String nickUsuario = "";
				try (BufferedReader br_us3 = Files.newBufferedReader(path_us)) {
					br_us3.readLine(); // descartamos primera linea
					String linea_us3;
					while ((linea_us3 = br_us3.readLine()) != null
							&& !linea_us3.split(";")[0].trim().equals(ref_us3.trim()))
						;
					if (linea_us3 != null && linea_us3.split(";")[0].equals(ref_us3)) {
						nickUsuario = linea_us3.split(";")[2];
					} else {
						errores = true;
						System.out.println("ERROR: No se encontró el usuario " + ref_us3);
					}

				} catch (IOException e) {
					errores = true;
					System.out.println("Error al cargar datos (Edicion -> Organizador");
				}

				// Buscamos en Edicion para hallar nombre de edicion y referencia del evento:
				String edicion3 = "";
				String ref_evento3 = "";
				try (BufferedReader br_ed3 = Files.newBufferedReader(path_ed)) {
					br_ed3.readLine();
					String linea_ed3;
					while ((linea_ed3 = br_ed3.readLine()) != null && !linea_ed3.split(";")[0].equals(ref_ed3))
						;
					if (linea_ed3 != null && linea_ed3.split(";")[0].equals(ref_ed3)) {
						ref_evento3 = linea_ed3.split(";")[1];
						edicion3 = linea_ed3.split(";")[3];
					} else {
						errores = true;
						System.out.println("No se encontró la edición " + ref_ed3);
					}

				} catch (IOException e) {
					errores = true;
					System.out.println("Error al cargar datos (RegistroAEdicion -> Edicion");
				}

				// Buscamos el nombre del evento
				String evento3 = "";
				try (BufferedReader br_ev3 = Files.newBufferedReader(path_ev)) {
					br_ev3.readLine();
					String linea_ev3;
					while ((linea_ev3 = br_ev3.readLine()) != null && !linea_ev3.split(";")[0].equals(ref_evento3))
						;
					if (linea_ev3 != null && linea_ev3.split(";")[0].equals(ref_evento3)) {
						evento3 = linea_ev3.split(";")[1];
					} else {
						errores = true;
						System.out.println("No se encontró el evento " + ref_evento3);
					}
				} catch (IOException e) {
					errores = true;
					System.out.println("Error al cargar datos (RegistroAEdicion -> Evento");
				}

				// Buscamos el tipo de registro
				String tipoReg3 = "";
				try (BufferedReader br_tr3 = Files.newBufferedReader(path_tr)) {
					br_tr3.readLine();
					String linea_tr3;
					while ((linea_tr3 = br_tr3.readLine()) != null && !linea_tr3.split(";")[0].equals(ref_tr3))
						;
					if (linea_tr3 != null && linea_tr3.split(";")[0].equals(ref_tr3)) {
						tipoReg3 = linea_tr3.split(";")[2];
					} else {
						errores = true;
						System.out.println("No se encontró el Tipo de Registro " + ref_tr3);
					}
				} catch (IOException e) {
					errores = true;
					System.out.println("Error al cargar datos (RegistroAEdicion -> TipoRegistro");
				}

				try {
					// [SIN CAMBIOS] alta del registro con la fecha del CSV
					ctrEv.altaRegistroCodigo(evento3, edicion3, tipoReg3, nickUsuario, fechaRegistro, patrocinio);

					// ============================================================
					// ===============  [REVERTIDO] IMPORTANTE  ==================
					// Antes: se validaba que la edición hubiera comenzado para marcar asistencia.
					// Ahora: volvemos al comportamiento original: si el CSV trae 'S',
					//        se marca asistencia sin validar fechas aquí.
					//        Las validaciones se harán en ControladorEvento.marcarAsistencia().
					// ============================================================
					if (asiste) {
						ctrEv.marcarAsistencia(evento3, edicion3, nickUsuario); // [REVERTIDO]
					}
					// ============================================================

				} catch (CupoLlenoExcepcion e) {
					errores = true;
					System.out.println(e.getMessage());
				} catch (YaRegistradoExcepcion e) {
					errores = true;
					System.out.println(e.getMessage());
				}
			}

		} catch (IOException e) {
			errores = true;
			System.out.println("Error al leer datos. (Registro a Edicion)");
		}

		if (errores)
			throw new ErroresCargaDatosExcepcion("Hubo errores durante la carga de datos.");
	}

	private void setCargados() {
		cargados = true;
	}

	public static void cargar() throws CargaDatosRepetidaExcepcion, ErroresCargaDatosExcepcion {
		if (!cargados) {
			CargaDatos carga = new CargaDatos();
			carga.setCargados();
		} else
			throw new CargaDatosRepetidaExcepcion("Los datos ya fueron cargados");
	}

	public static void cargar(String nuevoPathDatos) throws CargaDatosRepetidaExcepcion, ErroresCargaDatosExcepcion {
		if (!cargados) {
			pathDatos = nuevoPathDatos;
			CargaDatos carga = new CargaDatos();
			carga.setCargados();
		} else
			throw new CargaDatosRepetidaExcepcion("Los datos ya fueron cargadosOPAKSDOASKD");
	}
}
