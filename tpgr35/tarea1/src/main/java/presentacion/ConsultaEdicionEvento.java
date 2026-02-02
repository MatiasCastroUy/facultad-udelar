package presentacion;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.border.EtchedBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import excepciones.UsuarioNoExisteException;
import logica.DTEdicion;
import logica.DTEvento;
import logica.DTOrganizador;
import logica.IControladorEvento;
import logica.IControladorUsuario;

import javax.swing.JTabbedPane;


@SuppressWarnings("serial")
public class ConsultaEdicionEvento extends JInternalFrame {

	private IControladorEvento ctrlEvento;
	private IControladorUsuario ctrlUsuario;
	private JPanel jpanel;
	private ConsultaTipoRegistro consultaTipoRegistro;
	
	// Constantes
	private final String seleccionarLabelConst = "--Seleccionar--";
	
	private final String tituloLabelConst = "Consulta de Edición de Evento";
	private final String tituloUsuarioInexistenteConst = "Usuario inexistente";
	
	private final String eventoTabLabelConst = "Evento";
	private final String edicionTabLabelConst = "Edición";
	private final String organizadorTabLabelConst = "Organizador";
	
	private final String eventoDetallesLabelConst = "Detalles del Evento:";
	private final String edicionDetallesLabelConst = "Detalles de la Edición:";
	private final String organizadorDetallesLabelConst = "Detalles del Organizador:";

	private final String eventoImgLabelConst = "Imagen del Evento";
	private final String edicionImgLabelConst = "Imagen de Edición";
	private final String organizadorImgLabelConst = "Organizador";
	private final String sinImagenLabelConst = "Sin imagen";
	private final String imagenInvalidaLabelConst = "Imagen no válida";
	private final String imagenErrorLabelConst = "Error al cargar imagen";
	private final String archivoNoEncontradoLabelConst = "Archivo no encontrado: ";

	private final String eventoLabelConst = "Evento:";
	private final String edicionLabelConst = "Edición:";
	
	private final String nombreLabelConst = "Nombre:";
	private final String siglasLabelConst = "Siglas:";
	private final String fechaAltaLabelConst = "Fecha de Alta:";
	private final String descripcionLabelConst = "Descripción:";
	private final String paisLabelConst = "País:";
	private final String ciudadLabelConst = "Ciudad:";
	private final String fechaInicioLabelConst = "Fecha de Inicio:";
	private final String fechaFinLabelConst = "Fecha de Fin:";
	private final String nicknameLabelConst = "Nickname:";
	private final String correoLabelConst = "Correo:";
	private final String sitioWebLabelConst = "Sitio Web:";
	private final String estadoLabelConst = "Estado:";
	
	private final String tipoRegistroLabelConst = "Tipo de Registro:";
	private final String patrocinioLabelConst = "Patrocinio:";
	
	private final String volverBtnLabelConst = "Volver";
	
	
	// Valores de Evento
	private JTextField nombreEventoTextField;
	private JTextField siglaEventoTextField;
	private JTextField fechaAltaEventoTextField;
	private JTextArea descripcionEventoTextArea;
	
	
	// Valores de Edición
	private JTextField siglaTextField;
	private JTextField paisTextField;
	private JTextField ciudadTextField;
	private JTextField fechaInicioDate;
	private JTextField fechaFinDate;
	private JTextField fechaAltaDate;
	
	
	// Valores de Organizador
	private JTextField nombreOrganizadorTextField;
	private JTextField nicknameTextField;
	private JTextField correoTextField;
	private JTextField sitioWebTextField;
	private JTextArea descripcionOrganizadorTextArea;
	
	
	// Comboboxes
	private JComboBox<String> eventoComboBox;
	private JComboBox<String> edicionComboBox;
	private JComboBox<String> tipoRegistroComboBox;
	private JComboBox<String> patrocinioComboBox;
	
	
	// Imágenes
	private JLabel lblImagenEvento;
	private JLabel lblImagenEdicion;
	private JLabel lblImagenOrganizador;
	
	private List<String> eventosList;
	private List<String> edicionesEventoList;
	private List<String> tiposRegistroList;
	private List<String> patrociniosList;
	private JTextField estadoEdicionField;

	
	/**
	 * @wbp.parser.constructor
	 */
	public ConsultaEdicionEvento(JPanel parentPanel, IControladorEvento ice, IControladorUsuario icu) {
		this.ctrlEvento = ice;
		this.ctrlUsuario = icu;
		this.jpanel = parentPanel;
		
		inicializar();
		
		setTitle(tituloLabelConst);
		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);
		setSize(500, 500);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		parentPanel.add(this);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(null);
		
		
		JLabel eventoLabel = new JLabel(eventoLabelConst);
		eventoLabel.setBounds(23, 15, 115, 14);
		panel.add(eventoLabel);
		
		eventoComboBox = new JComboBox<String>();
		eventoLabel.setLabelFor(eventoComboBox);
		eventoComboBox.setBounds(148, 11, 326, 22);
		eventoComboBox.setModel(new DefaultComboBoxModel<String>(eventosList.toArray(new String[eventosList.size()])));
		panel.add(eventoComboBox);

		
		JLabel edicionLabel = new JLabel(edicionLabelConst);
		edicionLabel.setBounds(23, 48, 115, 14);
		panel.add(edicionLabel);

		edicionComboBox = new JComboBox<String>();
		edicionLabel.setLabelFor(edicionComboBox);
		edicionComboBox.setBounds(148, 44, 326, 22);
		panel.add(edicionComboBox);
		
		
		// Panel de Evento
		JPanel eventoPanel = new JPanel();
		eventoPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		eventoPanel.setBounds(263, 283, 264, 227);
		eventoPanel.setLayout(null);
		
		
		JLabel tituloDetallesEventoLabel = new JLabel(eventoDetallesLabelConst);
		tituloDetallesEventoLabel.setBounds(10, 11, 244, 14);
		eventoPanel.add(tituloDetallesEventoLabel);
		
			
		nombreEventoTextField = new JTextField();
		nombreEventoTextField.setBounds(139, 37, 176, 20);
		eventoPanel.add(nombreEventoTextField);
		nombreEventoTextField.setEditable(false);
		nombreEventoTextField.setColumns(10);
		
		JLabel nombreEventoLabel = new JLabel(nombreLabelConst);
		nombreEventoLabel.setBounds(10, 40, 115, 14);
		eventoPanel.add(nombreEventoLabel);
		nombreEventoLabel.setLabelFor(nombreEventoTextField);
		

		siglaEventoTextField = new JTextField();
		siglaEventoTextField.setBounds(139, 68, 176, 20);
		eventoPanel.add(siglaEventoTextField);
		siglaEventoTextField.setEditable(false);
		siglaEventoTextField.setColumns(10);
		
		JLabel siglaEventoLabel = new JLabel(siglasLabelConst);
		siglaEventoLabel.setBounds(10, 71, 115, 14);
		eventoPanel.add(siglaEventoLabel);
		siglaEventoLabel.setLabelFor(siglaEventoTextField);
		

		fechaAltaEventoTextField = new JTextField();
		fechaAltaEventoTextField.setBounds(139, 99, 176, 20);
		eventoPanel.add(fechaAltaEventoTextField);
		fechaAltaEventoTextField.setEditable(false);
		fechaAltaEventoTextField.setColumns(10);
		
		JLabel fechaAltaEventoLabel = new JLabel(fechaAltaLabelConst);
		fechaAltaEventoLabel.setBounds(10, 102, 115, 14);
		eventoPanel.add(fechaAltaEventoLabel);
		siglaEventoLabel.setLabelFor(fechaAltaEventoTextField);
		

		JLabel descripcionEventoLabel = new JLabel(descripcionLabelConst);
		descripcionEventoLabel.setBounds(10, 133, 72, 14);
		eventoPanel.add(descripcionEventoLabel);
		
		descripcionEventoTextArea = new JTextArea();
		JScrollPane descripcionEventoScroll = new JScrollPane(descripcionEventoTextArea);
		descripcionEventoScroll.setBounds(92, 130, 224, 83);
		descripcionEventoScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		descripcionEventoTextArea.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		descripcionEventoTextArea.setBackground(new Color(240, 240, 240));
		descripcionEventoTextArea.setLineWrap(true);
		descripcionEventoTextArea.setEditable(false);
		descripcionEventoTextArea.setBounds(92, 130, 224, 83);
		eventoPanel.add(descripcionEventoScroll);
		
		lblImagenEvento = new JLabel();
		lblImagenEvento.setBounds(329, 11, 120, 120);
		lblImagenEvento.setBorder(javax.swing.BorderFactory.createTitledBorder(eventoImgLabelConst));
		lblImagenEvento.setHorizontalAlignment(SwingConstants.CENTER);
		lblImagenEvento.setVerticalAlignment(SwingConstants.CENTER);
		lblImagenEvento.setText(sinImagenLabelConst);
		eventoPanel.add(lblImagenEvento);
		
		
		
		// Panel de Edición
		JPanel edicionPanel = new JPanel();
		edicionPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		edicionPanel.setBounds(263, 283, 264, 227);
		edicionPanel.setLayout(null);
		
		
		JLabel tituloDetallesEdicionLabel = new JLabel(edicionDetallesLabelConst);
		tituloDetallesEdicionLabel.setBounds(10, 11, 244, 14);
		edicionPanel.add(tituloDetallesEdicionLabel);
		
		
		siglaTextField = new JTextField();
		siglaTextField.setBounds(139, 37, 176, 20);
		edicionPanel.add(siglaTextField);
		siglaTextField.setEditable(false);
		siglaTextField.setColumns(10);
		
		JLabel siglaLabel = new JLabel(siglasLabelConst);
		siglaLabel.setBounds(10, 40, 115, 14);
		edicionPanel.add(siglaLabel);
		siglaLabel.setLabelFor(siglaTextField);
		
		
		JLabel paisLabel = new JLabel(paisLabelConst);
		paisLabel.setBounds(10, 71, 115, 14);
		edicionPanel.add(paisLabel);
		
		paisTextField = new JTextField();
		paisTextField.setBounds(139, 68, 176, 20);
		edicionPanel.add(paisTextField);
		paisTextField.setEditable(false);
		paisTextField.setColumns(10);
		paisLabel.setLabelFor(paisTextField);
		
		
		JLabel ciudadLabel = new JLabel(ciudadLabelConst);
		ciudadLabel.setBounds(10, 102, 115, 14);
		edicionPanel.add(ciudadLabel);
		
		ciudadTextField = new JTextField();
		ciudadTextField.setBounds(139, 99, 176, 20);
		edicionPanel.add(ciudadTextField);
		ciudadTextField.setEditable(false);
		ciudadTextField.setColumns(10);
		ciudadLabel.setLabelFor(ciudadTextField);
		

		JLabel fechaInicioLabel = new JLabel(fechaInicioLabelConst);
		fechaInicioLabel.setBounds(10, 133, 115, 14);
		edicionPanel.add(fechaInicioLabel);
		fechaInicioLabel.setLabelFor(fechaInicioDate);
		
		fechaInicioDate = new JTextField();
		fechaInicioDate.setBounds(139, 130, 176, 20);
		edicionPanel.add(fechaInicioDate);
		fechaInicioDate.setEditable(false);
		fechaInicioDate.setColumns(10);
		

		JLabel fechaFinLabel = new JLabel(fechaFinLabelConst);
		fechaFinLabel.setBounds(10, 164, 115, 14);
		edicionPanel.add(fechaFinLabel);
		fechaFinLabel.setLabelFor(fechaFinDate);
		
		fechaFinDate = new JTextField();
		fechaFinDate.setBounds(139, 161, 176, 20);
		edicionPanel.add(fechaFinDate);
		fechaFinDate.setEditable(false);
		fechaFinDate.setColumns(10);
		

		JLabel fechaAltaLabel = new JLabel(fechaAltaLabelConst);
		fechaAltaLabel.setBounds(10, 195, 115, 14);
		edicionPanel.add(fechaAltaLabel);
		fechaAltaLabel.setLabelFor(fechaAltaDate);
		
		fechaAltaDate = new JTextField();
		fechaAltaDate.setBounds(139, 192, 176, 20);
		edicionPanel.add(fechaAltaDate);
		fechaAltaDate.setEditable(false);
		fechaAltaDate.setColumns(10);
		
		lblImagenEdicion = new JLabel();
		lblImagenEdicion.setBounds(329, 11, 120, 120);
		lblImagenEdicion.setBorder(javax.swing.BorderFactory.createTitledBorder(edicionImgLabelConst));
		lblImagenEdicion.setHorizontalAlignment(SwingConstants.CENTER);
		lblImagenEdicion.setVerticalAlignment(SwingConstants.CENTER);
		lblImagenEdicion.setText(sinImagenLabelConst);
		edicionPanel.add(lblImagenEdicion);
		
		
		
		// Panel de Organizador
		JPanel organizadorPanel = new JPanel();
		organizadorPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		organizadorPanel.setLayout(null);
		organizadorPanel.setBounds(465, 283, 264, 227);
		
		
		JLabel tituloDetallesOrganizadorLabel = new JLabel(organizadorDetallesLabelConst);
		tituloDetallesOrganizadorLabel.setBounds(10, 11, 244, 14);
		organizadorPanel.add(tituloDetallesOrganizadorLabel);
		
		
		nombreOrganizadorTextField = new JTextField();
		nombreOrganizadorTextField.setEditable(false);
		nombreOrganizadorTextField.setColumns(10);
		nombreOrganizadorTextField.setBounds(139, 37, 176, 20);
		organizadorPanel.add(nombreOrganizadorTextField);
		
		JLabel nombreOrganizadorLabel = new JLabel(nombreLabelConst);
		nombreOrganizadorLabel.setBounds(10, 40, 115, 14);
		organizadorPanel.add(nombreOrganizadorLabel);
		
		
		JLabel nicknameLabel = new JLabel(nicknameLabelConst);
		nicknameLabel.setBounds(10, 71, 115, 14);
		organizadorPanel.add(nicknameLabel);
		
		nicknameTextField = new JTextField();
		nicknameTextField.setEditable(false);
		nicknameTextField.setColumns(10);
		nicknameTextField.setBounds(139, 68, 176, 20);
		organizadorPanel.add(nicknameTextField);
		
		
		JLabel correoLabel = new JLabel(correoLabelConst);
		correoLabel.setBounds(10, 102, 115, 14);
		organizadorPanel.add(correoLabel);
		
		correoTextField = new JTextField();
		correoTextField.setEditable(false);
		correoTextField.setColumns(10);
		correoTextField.setBounds(139, 99, 176, 20);
		organizadorPanel.add(correoTextField);
		
		
		JLabel descripcionOrganizadorLabel = new JLabel(descripcionLabelConst);
		descripcionOrganizadorLabel.setBounds(10, 133, 72, 14);
		organizadorPanel.add(descripcionOrganizadorLabel);
		
		descripcionOrganizadorTextArea = new JTextArea();
		JScrollPane descripcionOrganizadorScroll = new JScrollPane(descripcionOrganizadorTextArea);
		descripcionOrganizadorScroll.setBounds(92, 130, 224, 52);
		descripcionOrganizadorScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		descripcionOrganizadorTextArea.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		descripcionOrganizadorTextArea.setBackground(new Color(240, 240, 240));
		descripcionOrganizadorTextArea.setLineWrap(true);
		descripcionOrganizadorTextArea.setEditable(false);
		descripcionOrganizadorTextArea.setBounds(92, 130, 224, 52);
		organizadorPanel.add(descripcionOrganizadorScroll);
		
		
		JLabel sitioWebLabel = new JLabel(sitioWebLabelConst);
		sitioWebLabel.setBounds(10, 195, 115, 14);
		organizadorPanel.add(sitioWebLabel);
		
		sitioWebTextField = new JTextField();
		sitioWebTextField.setBounds(139, 192, 176, 20);
		organizadorPanel.add(sitioWebTextField);
		sitioWebTextField.setEditable(false);
		sitioWebTextField.setColumns(10);
		
		lblImagenOrganizador = new JLabel();
		lblImagenOrganizador.setBounds(329, 11, 120, 120);
		lblImagenOrganizador.setBorder(javax.swing.BorderFactory.createTitledBorder(organizadorImgLabelConst));
		lblImagenOrganizador.setHorizontalAlignment(SwingConstants.CENTER);
		lblImagenOrganizador.setVerticalAlignment(SwingConstants.CENTER);
		lblImagenOrganizador.setText(sinImagenLabelConst);
		organizadorPanel.add(lblImagenOrganizador);
		

		// Pestañas
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 77, 464, 278);
		panel.add(tabbedPane);

		tabbedPane.addTab(eventoTabLabelConst, eventoPanel);
		tabbedPane.addTab(edicionTabLabelConst, edicionPanel);
		
		JLabel estadoEdicionLabel = new JLabel(estadoLabelConst);
		estadoEdicionLabel.setBounds(10, 221, 115, 14);
		edicionPanel.add(estadoEdicionLabel);
		
		estadoEdicionField = new JTextField();
		estadoEdicionField.setEditable(false);
		estadoEdicionField.setColumns(10);
		estadoEdicionField.setBounds(139, 222, 176, 20);
		edicionPanel.add(estadoEdicionField);
		tabbedPane.addTab(organizadorTabLabelConst, organizadorPanel);
		
		
		JButton volverBtn = new JButton(volverBtnLabelConst);
		volverBtn.setBounds(385, 436, 89, 23);
		panel.add(volverBtn);
		
		JLabel tipoDeRegistroLabel = new JLabel(tipoRegistroLabelConst);
		tipoDeRegistroLabel.setBounds(23, 374, 137, 14);
		panel.add(tipoDeRegistroLabel);
		
		tipoRegistroComboBox = new JComboBox<String>();
		tipoRegistroComboBox.setBounds(170, 370, 304, 22);
		panel.add(tipoRegistroComboBox);
		
		JLabel patrocinioLabel = new JLabel(patrocinioLabelConst);
		patrocinioLabel.setBounds(23, 407, 137, 14);
		panel.add(patrocinioLabel);
		
		patrocinioComboBox = new JComboBox<String>();
		patrocinioComboBox.setBounds(170, 403, 304, 22);
		panel.add(patrocinioComboBox);
		
		

		
		eventoComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				seleccionarEventoAction(event);
			}
		});
		
		edicionComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				seleccionarEdicionEventoAction(event);
			}
		});
		
		tipoRegistroComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				seleccionarTipoRegistroAction(event);
			}
		});
		
		
		patrocinioComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				seleccionarPatrocinioAction(event);
			}
		});

		volverBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				volverAction();
			}
		});
		
		
		addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosed(InternalFrameEvent event) {
				
			}
			
			public void internalFrameClosing(InternalFrameEvent event) {
				reiniciarFormulario();
			}
		});
		
		
		cargarDatos();
	}
	
	
	private void inicializar() {
		eventosList = new ArrayList<String>();
		eventosList.add(seleccionarLabelConst);
		
		edicionesEventoList = new ArrayList<String>();
		edicionesEventoList.add(seleccionarLabelConst);
		
		tiposRegistroList = new ArrayList<String>();
		tiposRegistroList.add(seleccionarLabelConst);
		
		patrociniosList = new ArrayList<String>();
		patrociniosList.add(seleccionarLabelConst);
	}
	
	
	private void cargarDatos() {
		Set<String> eventos = ctrlEvento.listarEventos();
		eventosList.addAll(eventos);
		eventoComboBox.setModel(new DefaultComboBoxModel<String>(eventosList.toArray(new String[eventosList.size()])));
		
		edicionComboBox.setModel(new DefaultComboBoxModel<String>(edicionesEventoList.toArray(new String[edicionesEventoList.size()])));
		tipoRegistroComboBox.setModel(new DefaultComboBoxModel<String>(tiposRegistroList.toArray(new String[tiposRegistroList.size()])));
		patrocinioComboBox.setModel(new DefaultComboBoxModel<String>(patrociniosList.toArray(new String[patrociniosList.size()])));
	}
	

	private void reiniciarFormulario() {
		nombreEventoTextField.setText("");
		siglaEventoTextField.setText("");
		fechaAltaEventoTextField.setText("");
		descripcionEventoTextArea.setText("");
		siglaTextField.setText("");
		paisTextField.setText("");
		ciudadTextField.setText("");
		fechaInicioDate.setText("");
		fechaFinDate.setText("");
		fechaAltaDate.setText("");
		nombreOrganizadorTextField.setText("");
		nicknameTextField.setText("");
		correoTextField.setText("");
		sitioWebTextField.setText("");
		descripcionOrganizadorTextArea.setText("");
		estadoEdicionField.setText("");

		lblImagenEvento.setIcon(null);
		lblImagenEvento.setText(sinImagenLabelConst);
		lblImagenEdicion.setIcon(null);
		lblImagenEdicion.setText(sinImagenLabelConst);
		lblImagenOrganizador.setIcon(null);
		lblImagenOrganizador.setText(sinImagenLabelConst);
	}
	
	private void reiniciarComboboxes() {
		eventoComboBox.setSelectedIndex(0);
		edicionComboBox.setSelectedIndex(0);
	}
	
	
	private void seleccionarEventoAction(ActionEvent event) {
		JComboBox<String> cbe = (JComboBox<String>) event.getSource();
		String seleccion = (String) cbe.getSelectedItem();
		
		// Reiniciamos el resto de comboboxes
		edicionesEventoList = new ArrayList<String>();
		edicionesEventoList.add(seleccionarLabelConst);
		
		tiposRegistroList = new ArrayList<String>();
		tiposRegistroList.add(seleccionarLabelConst);
		
		patrociniosList = new ArrayList<String>();
		patrociniosList.add(seleccionarLabelConst);
		
		// Cargamos la lista de ediciones de evento, si se seleccionó un evento.
		if (!seleccion.equals(seleccionarLabelConst)) {
			Set<String> ediciones = ctrlEvento.listarEdiciones(seleccion);
			edicionesEventoList.addAll(ediciones);
			
			// Cargamos la información del evento seleccionado
			DTEvento evento = ctrlEvento.getDTEvento(seleccion);
			
			nombreEventoTextField.setText(evento.getNombre());
			siglaEventoTextField.setText(evento.getSigla());
			fechaAltaEventoTextField.setText(evento.getFechaAlta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			descripcionEventoTextArea.setText(evento.getDescripcion());
			
			// Mostramos la imagen del evento
			mostrarImagen(evento.getImagen(), lblImagenEvento);
		} else {
			reiniciarFormulario();
		}
		
		edicionComboBox.setModel(new DefaultComboBoxModel<String>(edicionesEventoList.toArray(new String[edicionesEventoList.size()])));
	
		// Reiniciamos selección de combobox Edición de Evento
		edicionComboBox.setSelectedIndex(0);
	}
	
	
	private void seleccionarEdicionEventoAction(ActionEvent event) {
		JComboBox<String> cbe = (JComboBox<String>) event.getSource();
		String seleccion = (String) cbe.getSelectedItem();
		
		// Cargamos la información, si se seleccionó una edición de evento.
		if (!seleccion.equalsIgnoreCase(seleccionarLabelConst)) {
			try {
				// Información de la edición seleccionada
				DTEdicion dtEd = ctrlEvento.getDTEdicion(eventoComboBox.getSelectedItem().toString(), seleccion);
				
				siglaTextField.setText(dtEd.getSigla());
				paisTextField.setText(dtEd.getPais());
				ciudadTextField.setText(dtEd.getCiudad());
				fechaInicioDate.setText(dtEd.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				fechaFinDate.setText(dtEd.getFechaFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				fechaAltaDate.setText(dtEd.getFechaAlta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				estadoEdicionField.setText(dtEd.getEstado());
				
				// Mostramos la imagen de la edición
				mostrarImagen(dtEd.getImagen(), lblImagenEdicion);
				
				
				// Información del organizador de la edición
				DTOrganizador dtOrg = ctrlUsuario.obtenerInfoOrganizador(dtEd.getNicknameOrganizador());
				
				nombreOrganizadorTextField.setText(dtOrg.getNombre());
				nicknameTextField.setText(dtOrg.getNickname());
				correoTextField.setText(dtOrg.getCorreo());
				sitioWebTextField.setText(dtOrg.getSitioWeb());
				descripcionOrganizadorTextArea.setText(dtOrg.getDescripcion());

				// Mostramos la imagen del organizador
				mostrarImagen(dtOrg.getImagenPerfil(), lblImagenOrganizador);
				
				
				// Llenar lista de tipos de registro
				List<String> tiposDeRegistro = new ArrayList<String>();
				tiposDeRegistro.add(seleccionarLabelConst);
				tiposDeRegistro.addAll(ctrlEvento.listarTipoRegistro(eventoComboBox.getSelectedItem().toString(), seleccion));
				tipoRegistroComboBox.setModel(new DefaultComboBoxModel<String>(tiposDeRegistro.toArray(new String[tiposDeRegistro.size()])));
				
				
				// TODO: Cargar patrocinios
				List<String> patrocinios = new ArrayList<String>();
				patrocinios.add(seleccionarLabelConst);
				patrocinios.addAll(new ArrayList<String>()); // <--- ACÁ
				patrocinioComboBox.setModel(new DefaultComboBoxModel<String>(patrocinios.toArray(new String[patrocinios.size()])));
			} catch (UsuarioNoExisteException exc) {
				JOptionPane.showMessageDialog(
					this, 
					exc.getMessage(), 
					tituloUsuarioInexistenteConst,
	                JOptionPane.ERROR_MESSAGE
		        );
			}
		} else {
			siglaTextField.setText("");
			paisTextField.setText("");
			ciudadTextField.setText("");
			fechaInicioDate.setText("");
			fechaFinDate.setText("");
			fechaAltaDate.setText("");
			nombreOrganizadorTextField.setText("");
			nicknameTextField.setText("");
			correoTextField.setText("");
			sitioWebTextField.setText("");
			descripcionOrganizadorTextArea.setText("");
			estadoEdicionField.setText("");

			lblImagenEdicion.setIcon(null);
			lblImagenEdicion.setText(sinImagenLabelConst);
			lblImagenOrganizador.setIcon(null);
			lblImagenOrganizador.setText(sinImagenLabelConst);
			
			List<String> tiposDeRegistro = new ArrayList<String>();
			tiposDeRegistro.add(seleccionarLabelConst);
			tipoRegistroComboBox.setModel(new DefaultComboBoxModel<String>(tiposDeRegistro.toArray(new String[tiposDeRegistro.size()])));
			tipoRegistroComboBox.setSelectedIndex(0);
			
			List<String> patrocinios = new ArrayList<String>();
			patrocinios.add(seleccionarLabelConst);
			patrocinioComboBox.setModel(new DefaultComboBoxModel<String>(patrocinios.toArray(new String[patrocinios.size()])));
			patrocinioComboBox.setSelectedIndex(0);
		}
	}
	
	
	private void seleccionarTipoRegistroAction(ActionEvent event) {
		JComboBox<String> cbe = (JComboBox<String>) event.getSource();
		String seleccion = (String) cbe.getSelectedItem();
		
		// Cargamos la información, si se seleccionó una edición de evento.
		if (!seleccion.equalsIgnoreCase(seleccionarLabelConst)) {	
			String evento = (String) eventoComboBox.getSelectedItem();
			String edicion = (String) edicionComboBox.getSelectedItem();
			consultaTipoRegistro = new ConsultaTipoRegistro(this.jpanel, evento, edicion, seleccion);
			consultaTipoRegistro.setVisible(true);
			tipoRegistroComboBox.setSelectedIndex(0);
		}
	}
	
	
	private void seleccionarPatrocinioAction(ActionEvent event) {
		// TODO: Abrir ventana (No está presente el caso de uso Consulta de Patrocinio)
		patrocinioComboBox.setSelectedIndex(0);
	}
	
	
	private void volverAction() {
		reiniciarFormulario();
		reiniciarComboboxes();
		setVisible(false);
		this.dispose();
	}
	
	public ConsultaEdicionEvento(JPanel panel, String evento, String edicion, IControladorEvento icE, IControladorUsuario icU)  {
		this(panel, icE, icU);
		
		setVisible(true);
		setTitle("Consulta de Edición" + edicion);
		eventoComboBox.setEnabled(true);
		edicionComboBox.setEnabled(true);
		eventoComboBox.setEditable(false);
		edicionComboBox.setEditable(false);
		String[] evts = {evento};
		eventoComboBox.setModel(new DefaultComboBoxModel<String>(evts));
		
		String[] eds = {edicion};
		edicionComboBox.setModel(new DefaultComboBoxModel<String>(eds));
		
		DTEvento event = ctrlEvento.getDTEvento(evento);
		
		nombreEventoTextField.setText(event.getNombre());
		siglaEventoTextField.setText(event.getSigla());
		fechaAltaEventoTextField.setText(event.getFechaAlta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		descripcionEventoTextArea.setText(event.getDescripcion());
		
		try {
			// Información de la edición seleccionada
			DTEdicion dtEd = ctrlEvento.getDTEdicion(evento, edicion);
			
			siglaTextField.setText(dtEd.getSigla());
			paisTextField.setText(dtEd.getPais());
			ciudadTextField.setText(dtEd.getCiudad());
			fechaInicioDate.setText(dtEd.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			fechaFinDate.setText(dtEd.getFechaFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			fechaAltaDate.setText(dtEd.getFechaAlta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			estadoEdicionField.setText(dtEd.getEstado());
			
			// Información del organizador de la edición
			DTOrganizador dtOrg = ctrlUsuario.obtenerInfoOrganizador(dtEd.getNicknameOrganizador());
			
			nombreOrganizadorTextField.setText(dtOrg.getNombre());
			nicknameTextField.setText(dtOrg.getNickname());
			correoTextField.setText(dtOrg.getCorreo());
			sitioWebTextField.setText(dtOrg.getSitioWeb());
			descripcionOrganizadorTextArea.setText(dtOrg.getDescripcion());
			
			
			// Llenar lista de tipos de registro
			List<String> tiposDeRegistro = new ArrayList<String>();
			tiposDeRegistro.add(seleccionarLabelConst);
			tiposDeRegistro.addAll(ctrlEvento.listarTipoRegistro(evento, edicion));
			tipoRegistroComboBox.setModel(new DefaultComboBoxModel<String>(tiposDeRegistro.toArray(new String[tiposDeRegistro.size()])));
			
			
			// TODO: Cargar patrocinios
			List<String> patrocinios = new ArrayList<String>();
			patrocinios.add(seleccionarLabelConst);
			patrocinios.addAll(new ArrayList<String>()); // <--- ACÁ
			patrocinioComboBox.setModel(new DefaultComboBoxModel<String>(patrocinios.toArray(new String[patrocinios.size()])));
		} catch (UsuarioNoExisteException exc) {
			JOptionPane.showMessageDialog(
				this, 
				exc.getMessage(), 
				tituloUsuarioInexistenteConst,
                JOptionPane.ERROR_MESSAGE
	        );
		}
		
	}
	
	private void mostrarImagen(String rutaImagen, JLabel lblImagen) {
		if (rutaImagen != null && !rutaImagen.trim().isEmpty()) {
			try {
				File imagenFile = new File(rutaImagen);
				
				if (imagenFile.exists()) {
					Image imagen = ImageIO.read(imagenFile);
					if (imagen != null) {
						Image imagenRedimensionada = imagen.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
						ImageIcon icon = new ImageIcon(imagenRedimensionada);
						lblImagen.setIcon(icon);
						lblImagen.setText("");
					} else {
						lblImagen.setIcon(null);
						lblImagen.setText(imagenInvalidaLabelConst);
					}
				} else {
					lblImagen.setIcon(null);
					lblImagen.setText(archivoNoEncontradoLabelConst + rutaImagen);
				}
			} catch (IOException e) {
				lblImagen.setIcon(null);
				lblImagen.setText(imagenErrorLabelConst);
				System.out.println("DEBUG: IOException: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			lblImagen.setIcon(null);
			lblImagen.setText(sinImagenLabelConst);
		}
	}
}
