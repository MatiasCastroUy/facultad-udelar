package presentacion;

import excepciones.CargaDatosRepetidaExcepcion;
import excepciones.ErroresCargaDatosExcepcion;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import logica.Fabrica;
import logica.IControladorEvento;
import logica.IControladorUsuario;
import webServices.WSEvento;
import webServices.WSUsuario;


public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	private AltaTipoRegistro altaTipoRegistro;
	private ConsultaTipoRegistro consultaTipoRegistro;
	private AltaEdicionEvento altaEdicionEvento;
	private ConsultaEdicionEvento consultaEdicionEvento;
	private AceptarRechazarEdicion aceptarRechazarEdicion;
	private RegistroAEdicion registroAEdicion;
	private ConsultaDeRegistro consultaDeRegistro;
	private TopVisitas topVisitas;

	private AltaEvento alta;
	private ConsultaEvento consE;

	private CrearUsuario creaUsr;
	private ConsultaUsuario consultaUsr;
	private ModificarUsuario modDatUsr;
	private IControladorEvento ice;
	private IControladorUsuario icu;
	
	private static Properties propiedades;

	public static void main(String[] args) {
		Principal ventana = new Principal();

		ventana.setVisible(true);
		cargarPropiedades();
		levantarWebServices();
	}

	public Principal() {

		Fabrica fabrica = Fabrica.getInstance();
		ice = fabrica.getIControladorEvento();
		icu = fabrica.getIControladorUsuario();

		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(null);
		setTitle("Eventos.uy");

		creaUsr = new CrearUsuario(panel, icu);
		creaUsr.setLocation(40, 40);
		creaUsr.setVisible(false);

		consultaUsr = new ConsultaUsuario(panel, icu, ice);
		consultaUsr.setLocation(40, 40);
		consultaUsr.setVisible(false);

		modDatUsr = new ModificarUsuario(panel, icu);
		modDatUsr.setLocation(40, 40);
		modDatUsr.setVisible(false);

		alta = new AltaEvento(ice, panel);
		alta.setVisible(false);
		
		topVisitas 	= new TopVisitas(ice, panel);
		alta.setVisible(false);


		altaTipoRegistro = new AltaTipoRegistro(panel);
		altaTipoRegistro.setLocation(40, 40);
		altaTipoRegistro.setVisible(false);

		consultaTipoRegistro = new ConsultaTipoRegistro(panel);
		consultaTipoRegistro.setVisible(false);

		aceptarRechazarEdicion = new AceptarRechazarEdicion(panel, ice);
		aceptarRechazarEdicion.setVisible(false);

		registroAEdicion = new RegistroAEdicion(panel);
		registroAEdicion.setVisible(false);

		consultaDeRegistro = new ConsultaDeRegistro(panel);
		registroAEdicion.setVisible(false);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setSize(600, 600);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnSistema = new JMenu("Sistema");
		menuBar.add(mnSistema);

		JMenuItem mntmCargarDatos = new JMenuItem("Cargar datos");
		mntmCargarDatos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					CargaDatos.cargar();
					JOptionPane.showMessageDialog(mntmCargarDatos, "Datos cargados con éxito (" + propiedades.getProperty("path_carga_datos") + ")");
				} catch (CargaDatosRepetidaExcepcion e1) {
					JOptionPane.showMessageDialog(mntmCargarDatos, e1.getMessage(), "Datos ya cargados",
							JOptionPane.ERROR_MESSAGE);
				} catch (ErroresCargaDatosExcepcion e1) {
					JOptionPane.showMessageDialog(mntmCargarDatos, e1.getMessage(), "Errores al cargar",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mnSistema.add(mntmCargarDatos);

		JMenu mnUsuario = new JMenu("Usuario");
		menuBar.add(mnUsuario);

		JMenuItem mntmCrearUsuario = new JMenuItem("Crear Usuario");
		mntmCrearUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				creaUsr.setVisible(true);

			}
		});
		mnUsuario.add(mntmCrearUsuario);

		JMenuItem mntmConsultaUsuario = new JMenuItem("Info Usuario");
		mntmConsultaUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				consultaUsr.cargarUsuarios();
				consultaUsr.setVisible(true);

			}
		});
		mnUsuario.add(mntmConsultaUsuario);

		JMenuItem mntmModificarDatos = new JMenuItem("Modificar Datos");
		mntmModificarDatos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				modDatUsr.setVisible(true);

			}
		});
		mnUsuario.add(mntmModificarDatos);

		JMenuItem mntmAltaDeInstitucin = new JMenuItem("Alta de Institución");
		mnUsuario.add(mntmAltaDeInstitucin);

		JMenuItem mntmRegistroAEdicion = new JMenuItem("Registro a Edición");
		mntmRegistroAEdicion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				registroAEdicion.setVisible(true);
			}
		});
		mnUsuario.add(mntmRegistroAEdicion);

		JMenuItem mntmConsultaDeRegistro = new JMenuItem("Consulta de Registro");
		mntmConsultaDeRegistro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				consultaDeRegistro.setVisible(true);
			}
		});
		mnUsuario.add(mntmConsultaDeRegistro);

		JMenu mnEvento = new JMenu("Evento");
		menuBar.add(mnEvento);

		JMenuItem mntmAltaDeTipo = new JMenuItem("Alta de Tipo de Registro");
		mntmAltaDeTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				altaTipoRegistro.setVisible(true);
			}
		});
		mnEvento.add(mntmAltaDeTipo);

		JMenuItem mntmConsultaDeTipo = new JMenuItem("Consulta de Tipo de Registro");
		mntmConsultaDeTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				consultaTipoRegistro.setVisible(true);
			}
		});
		mnEvento.add(mntmConsultaDeTipo);

		// Alta de Edición de Evento
		JMenuItem mntmAltaEdicionEvento = new JMenuItem("Alta de Edición de Evento");
		mntmAltaEdicionEvento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				altaEdicionEvento = new AltaEdicionEvento(panel, ice, icu);
				altaEdicionEvento.setVisible(true);
			}
		});
		mnEvento.add(mntmAltaEdicionEvento);

		// Consulta de Edición de Evento
		JMenuItem mntmConsultaEdicionEvento = new JMenuItem("Consulta de Edición de Evento");
		mntmConsultaEdicionEvento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				consultaEdicionEvento = new ConsultaEdicionEvento(panel, ice, icu);
				consultaEdicionEvento.setVisible(true);
			}
		});
		mnEvento.add(mntmConsultaEdicionEvento);

		// Aceptar-Rechazar Edición de Evento
		JMenuItem mntmAceptarRechazarEdicion = new JMenuItem("Aceptar o Rechazar Edición de Evento");
		mntmAceptarRechazarEdicion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				aceptarRechazarEdicion.setVisible(true);
			}
		});
		mnEvento.add(mntmAceptarRechazarEdicion);

		JMenuItem mntmAltaDePatrocinio = new JMenuItem("Alta de Patrocinio");
		mnEvento.add(mntmAltaDePatrocinio);

		JMenuItem mntmConsultaDePatrocinio = new JMenuItem("Consulta de Patrocinio");
		mntmConsultaDePatrocinio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ConsultaPatrocinio consP = new ConsultaPatrocinio();
				consP.setVisible(true);
				getContentPane().add(consP);
			}
		});
		mnEvento.add(mntmConsultaDePatrocinio);

		JMenuItem mntmAltaDeCategora = new JMenuItem("Alta de Categoría");
		mnEvento.add(mntmAltaDeCategora);

		JMenuItem mntmAltaDeEdicion = new JMenuItem("Alta de Evento");
		mntmAltaDeEdicion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				alta.setVisible(true);
			}
		});
		mnEvento.add(mntmAltaDeEdicion);
		
		JMenuItem mntmTopVisitas = new JMenuItem("Top Visitas");
		mntmTopVisitas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				topVisitas.setVisible(true);
			}
		});
		mnEvento.add(mntmTopVisitas);


		JMenuItem mntmConsultaDeEvento = new JMenuItem("Consulta de Evento");
		mntmConsultaDeEvento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				consE = new ConsultaEvento(icu, ice, panel);
				consE.setVisible(true);

			}
		});
		mnEvento.add(mntmConsultaDeEvento);

		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
	}
	
	private static void levantarWebServices() {
		
		WSEvento wsEvento = new WSEvento(propiedades.getProperty("url_ws_eventos"));
		wsEvento.publish();
		WSUsuario wsUsuario = new WSUsuario(propiedades.getProperty("url_ws_usuarios"));
		wsUsuario.publish();
	}
	
	public static void cargarPropiedades() {
		// leer propiedades
		propiedades = new Properties();
		String home = System.getProperty("user.home");
		File carpeta = new File(home, ".eventosuy");
		File archivo = new File(carpeta, "servidorcentral.properties");
		if (carpeta.exists() && archivo.exists()) {
			try (FileInputStream input = new FileInputStream(archivo)) {
				propiedades.load(input);
			} catch (IOException e) {
				System.out.println("Error al leer servidorcentral.properties: \n");
				e.printStackTrace();
			}
		} else {
			if (!carpeta.exists()) carpeta.mkdirs();			
		}
		
		if (propiedades.getProperty("url_ws_eventos") == null) propiedades.setProperty("url_ws_eventos", "http://localhost:9128/evtuy/eventos");
		if (propiedades.getProperty("url_ws_usuarios") == null) propiedades.setProperty("url_ws_usuarios", "http://localhost:9128/evtuy/usuarios");
		if (propiedades.getProperty("path_carga_datos") == null) propiedades.setProperty("path_carga_datos", "src/datos/");
		if (propiedades.getProperty("path_imagenes") == null) propiedades.setProperty("path_imagenes", "src/img/");
		
		try (FileOutputStream output = new FileOutputStream(archivo)) {
			propiedades.store(output, "Configuración de web services");
		} catch (FileNotFoundException e) {
			System.out.println("Archivo " + archivo.getName() + " no encontrado");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error al escribir servidorcentral.properties");
			e.printStackTrace();
		}
	}
	
	public static Properties getPropiedades() {
		return propiedades;
	}

}

