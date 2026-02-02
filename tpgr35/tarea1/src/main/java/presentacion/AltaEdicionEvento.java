package presentacion;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JSeparator;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import com.toedter.calendar.JDateChooser;

import excepciones.EdicionRepetidaException;
import excepciones.UsuarioNoExisteException;
import logica.DTOrganizador;
import logica.IControladorEvento;
import logica.IControladorUsuario;
import enumerados.EstadoEdicion;


@SuppressWarnings("serial")
public class AltaEdicionEvento extends JInternalFrame{
	
	private IControladorEvento ctrlEvento;
	private IControladorUsuario ctrlUsuario;
	
	// Constantes
	private final String seleccionarLabelConst = "--Seleccionar--";
	
	private final String tituloLabelConst = "Alta de Edición de Evento";
	private final String eventoLabelConst = "Evento";
	private final String organizadorLabelConst = "Organizador";
	private final String nombreLabelConst = "Nombre de Edición";
	private final String siglasLabelConst = "Siglas";
	private final String paisLabelConst = "País";
	private final String ciudadLabelConst = "Ciudad";
	private final String fechaInicioLabelConst = "Fecha de Inicio";
	private final String fechaFinLabelConst = "Fecha de Fin";
	private final String fechaAltaLabelConst = "Fecha de Alta";
	private final String imagenLabelConst = "Imagen";
	private final String videoLabelConst = "Video";
	
	private final String imagenNoSeleccionadaLabelConst = "Sin imagen seleccionada";
	private final String archivoInexistenteLabelConst = "El archivo seleccionado no existe";

	private final String aceptarBtnLabelConst = "Aceptar";
	private final String cancelarBtnLabelConst = "Cancelar";
	private final String seleccionarBtnLabelConst = "Seleccionar";
	
	private final String tituloEdicionRepetidaConst = "Edición ya existente";
	private final String tituloOrganizadoresInexistentesConst = "No hay organizadores existentes";
	private final String tituloFormularioIncompletoConst = "Faltan datos";
	private final String tituloEdicionCreadaConst = "Edición creada";
	private final String errorFormularioIncompletoConst = "Falta completar los siguientes campos: ";
	private final String edicionCreadaConst = "La Edición ha sido creada exitosamente.";
	
	
	// Campos de formulario
	private JComboBox<String> eventoComboBox;
	private JComboBox<String> organizadorComboBox;
	private JTextField nombreTextField;
	private JTextField siglaTextField;
	private JTextField paisTextField;
	private JTextField ciudadTextField;
	private JDateChooser fechaInicioDate;
	private JDateChooser fechaFinDate;
	private JDateChooser fechaAltaDate;
	private JTextField imagenPathTextField;
	private JTextField videoTextField;
	
	
	private List<String> eventosList;
	private List<String> organizadoresList;
	private String imagenSeleccionada;

	
	public AltaEdicionEvento(JPanel parentPanel, IControladorEvento ice, IControladorUsuario icu) {
		this.ctrlEvento = ice;
		this.ctrlUsuario = icu;
		
		inicializar();
		
		setTitle(tituloLabelConst);
		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);
		setSize(500, 522);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		parentPanel.add(this);
		
		
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(null);
		
		
		JLabel eventoLabel = new JLabel(eventoLabelConst + ":");
		eventoLabel.setBounds(23, 15, 131, 14);
		panel.add(eventoLabel);
		
		eventoComboBox = new JComboBox<String>();
		eventoLabel.setLabelFor(eventoComboBox);
		eventoComboBox.setBounds(158, 11, 300, 22);
		eventoComboBox.setModel(new DefaultComboBoxModel<String>(eventosList.toArray(new String[eventosList.size()])));
		panel.add(eventoComboBox);

		
		JLabel organizadorLabel = new JLabel(organizadorLabelConst + ":");
		organizadorLabel.setBounds(23, 55, 131, 14);
		panel.add(organizadorLabel);

		organizadorComboBox = new JComboBox<String>();
		organizadorLabel.setLabelFor(organizadorComboBox);
		organizadorComboBox.setBounds(158, 51, 300, 22);
		organizadorComboBox.setModel(new DefaultComboBoxModel<String>(organizadoresList.toArray(new String[organizadoresList.size()])));
		panel.add(organizadorComboBox);

		
		JSeparator separadorSuperior = new JSeparator();
		separadorSuperior.setBounds(10, 83, 464, 2);
		panel.add(separadorSuperior);
		
		
		JLabel nombreLabel = new JLabel(nombreLabelConst + ":");
		nombreLabel.setBounds(23, 96, 131, 14);
		panel.add(nombreLabel);
		
		nombreTextField = new JTextField();
		nombreLabel.setLabelFor(nombreTextField);
		nombreTextField.setBounds(158, 93, 300, 20);
		panel.add(nombreTextField);
		nombreTextField.setColumns(10);
		
		
		JLabel siglaLabel = new JLabel(siglasLabelConst + ":");
		siglaLabel.setBounds(23, 136, 131, 14);
		panel.add(siglaLabel);
		
		siglaTextField = new JTextField();
		siglaLabel.setLabelFor(siglaTextField);
		siglaTextField.setBounds(158, 133, 300, 20);
		panel.add(siglaTextField);
		siglaTextField.setColumns(10);
		
		
		JLabel paisLabel = new JLabel(paisLabelConst + ":");
		paisLabel.setBounds(23, 176, 131, 14);
		panel.add(paisLabel);
		
		paisTextField = new JTextField();
		paisLabel.setLabelFor(paisTextField);
		paisTextField.setBounds(158, 173, 300, 20);
		panel.add(paisTextField);
		paisTextField.setColumns(10);
		
		
		JLabel ciudadLabel = new JLabel(ciudadLabelConst + ":");
		ciudadLabel.setBounds(23, 216, 131, 14);
		panel.add(ciudadLabel);		
		
		ciudadTextField = new JTextField();
		ciudadLabel.setLabelFor(ciudadTextField);
		ciudadTextField.setBounds(158, 213, 300, 20);
		panel.add(ciudadTextField);
		ciudadTextField.setColumns(10);
		
		
		JLabel fechaInicioLabel = new JLabel(fechaInicioLabelConst + ":");
		fechaInicioLabel.setBounds(23, 256, 131, 14);
		panel.add(fechaInicioLabel);
		
		fechaInicioDate = new JDateChooser();
		fechaInicioDate.setDateFormatString("dd/MM/yyyy");
		fechaInicioLabel.setLabelFor(fechaInicioDate);
		fechaInicioDate.setBounds(158, 253, 300, 20);
		panel.add(fechaInicioDate);
		
		
		JLabel fechaFinLabel = new JLabel(fechaFinLabelConst + ":");
		fechaFinLabel.setBounds(23, 296, 131, 14);
		panel.add(fechaFinLabel);
		
		fechaFinDate = new JDateChooser();
		fechaFinDate.setDateFormatString("dd/MM/yyyy");
		fechaFinLabel.setLabelFor(fechaFinDate);
		fechaFinDate.setBounds(158, 293, 300, 20);
		panel.add(fechaFinDate);
		
		
		JLabel fechaAltaLabel = new JLabel(fechaAltaLabelConst + ":");
		fechaAltaLabel.setBounds(23, 337, 131, 14);
		panel.add(fechaAltaLabel);
		
		fechaAltaDate = new JDateChooser();
		fechaAltaDate.setDateFormatString("dd/MM/yyyy");
		fechaAltaLabel.setLabelFor(fechaAltaDate);
		fechaAltaDate.setBounds(158, 334, 300, 20);
		panel.add(fechaAltaDate);
		
		
		JLabel imagenLabel = new JLabel(imagenLabelConst + ":");
		imagenLabel.setBounds(23, 378, 131, 14);
		panel.add(imagenLabel);
		
		imagenPathTextField = new JTextField();
		imagenPathTextField.setEditable(false);
		imagenPathTextField.setBounds(158, 375, 201, 20);
		imagenPathTextField.setText(imagenNoSeleccionadaLabelConst);
		panel.add(imagenPathTextField);
		
		JButton seleccionarBtn = new JButton(seleccionarBtnLabelConst);
		seleccionarBtn.setBounds(369, 374, 89, 23);
		panel.add(seleccionarBtn);

		
		JLabel videoLabel = new JLabel(videoLabelConst + ":");
		videoLabel.setBounds(23, 417, 131, 14);
		panel.add(videoLabel);
		
		videoTextField = new JTextField();
		videoTextField.setColumns(10);
		videoTextField.setBounds(158, 414, 300, 20);
		panel.add(videoTextField);
		
		
		JSeparator separadorInferior = new JSeparator();
		separadorInferior.setBounds(10, 445, 474, 2);
		panel.add(separadorInferior);
		
		
		JButton aceptarBtn = new JButton(aceptarBtnLabelConst);
		aceptarBtn.setBounds(270, 458, 89, 23);
		panel.add(aceptarBtn);
		
		
		JButton cancelarBtn = new JButton(cancelarBtnLabelConst);
		cancelarBtn.setBounds(369, 458, 89, 23);
		panel.add(cancelarBtn);
		

		fechaInicioDate.addPropertyChangeListener("date", new PropertyChangeListener() {
			@Override
            public void propertyChange(PropertyChangeEvent evt) {
                setFechaInicio();
            }
		});		
		
		fechaFinDate.addPropertyChangeListener("date", new PropertyChangeListener() {
			@Override
            public void propertyChange(PropertyChangeEvent evt) {
                setFechaFin();
            }
		});		

		
		seleccionarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				seleccionarAction(event);
			}
		});
		
		aceptarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				aceptarAction();
			}
		});
		
		cancelarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				cancelarAction();
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
		eventosList = new ArrayList<>();
		eventosList.add(seleccionarLabelConst);
		
		organizadoresList = new ArrayList<>();
		organizadoresList.add(seleccionarLabelConst);
		
		imagenSeleccionada = null;
	}
	
	
	private void cargarDatos() {
		// Cargar eventos
		Set<String> eventosSet = ctrlEvento.listarEventos();
		eventosList.addAll(eventosSet);
		eventoComboBox.setModel(new DefaultComboBoxModel<String>(eventosList.toArray(new String[eventosList.size()])));
		
		// Cargar organizadores
		try {
			Set<DTOrganizador> organizadores = ctrlUsuario.listarOrganizadores();
			List<String> organizadoresStr = new ArrayList<>();
			for (DTOrganizador org : organizadores) {
				organizadoresStr.add(org.getNombre() + " (" + org.getNickname() + ")");;
			}
			organizadoresList.addAll(organizadoresStr);
			organizadorComboBox.setModel(new DefaultComboBoxModel<String>(organizadoresList.toArray(new String[organizadoresList.size()])));
			
		} catch (UsuarioNoExisteException exc) {
			JOptionPane.showMessageDialog(
				this, 
				exc.getMessage(),
				tituloOrganizadoresInexistentesConst,
	            JOptionPane.ERROR_MESSAGE
	        );
			
			// Cerrar ventana si no hay organizadores
			setVisible(false);
			this.dispose();
		}
	}
	
	private Boolean validarFormulario() {
		Boolean esValido = true;
		List<String> camposVacios = new ArrayList<>();
		
		if (eventoComboBox.getSelectedItem().toString().equalsIgnoreCase(seleccionarLabelConst)) {
			camposVacios.add(eventoLabelConst);
		}

		if (organizadorComboBox.getSelectedItem().toString().equalsIgnoreCase(seleccionarLabelConst)) {
			camposVacios.add(organizadorLabelConst);
		}
		
		if (nombreTextField.getText().trim().isEmpty()) {
			camposVacios.add(nombreLabelConst);
		}
		
		if (siglaTextField.getText().trim().isEmpty()) {
			camposVacios.add(siglasLabelConst);
		}
		
		if (paisTextField.getText().trim().isEmpty()) {
			camposVacios.add(paisLabelConst);
		}
		
		if (ciudadTextField.getText().trim().isEmpty()) {
			camposVacios.add(ciudadLabelConst);
		}
		
		if (fechaInicioDate.getDate() == null) {
			camposVacios.add(fechaInicioLabelConst);
		}
		
		if (fechaFinDate.getDate() == null) {
			camposVacios.add(fechaFinLabelConst);
		}

		if (fechaAltaDate.getDate() == null) {
			camposVacios.add(fechaAltaLabelConst);
		}
		
		esValido = camposVacios.size() == 0;
		
		if (!esValido) {
			JOptionPane.showMessageDialog(
				this, 
				errorFormularioIncompletoConst + String.join(", ", camposVacios.toArray(new String[camposVacios.size()])), 
				tituloFormularioIncompletoConst,
	            JOptionPane.ERROR_MESSAGE
	        );
		}
		
		return esValido;
	}
	
	
	private void reiniciarFormulario() {
		nombreTextField.setText("");
		siglaTextField.setText("");
		paisTextField.setText("");
		ciudadTextField.setText("");
		fechaInicioDate.setDate(null);
		fechaInicioDate.setMaxSelectableDate(null);
		fechaFinDate.setDate(null);
		fechaFinDate.setMinSelectableDate(null);
		fechaAltaDate.setDate(null);
		imagenPathTextField.setText(imagenNoSeleccionadaLabelConst);
		videoTextField.setText("");
		
		eventoComboBox.setSelectedIndex(0);
		organizadorComboBox.setSelectedIndex(0);
		imagenSeleccionada = null;
	}
	
	
	private void setFechaInicio() {
		fechaFinDate.setMinSelectableDate(fechaInicioDate.getDate());
	}
	
	private void setFechaFin() {
		fechaInicioDate.setMaxSelectableDate(fechaFinDate.getDate());
	}
	
	
	// Acciones
	private void seleccionarAction(ActionEvent event) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		// Filtro para solo permitir imágenes
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"Imágenes (*.jpg, *.jpeg, *.png, *.gif)", "jpg", "jpeg", "png", "gif");
		fileChooser.setFileFilter(filter);
		
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				// Guardar la ruta completa del archivo
				imagenSeleccionada = selectedFile.getAbsolutePath();
				imagenPathTextField.setText(selectedFile.getName());
				
				// Verificar que el archivo existe
				if (!selectedFile.exists()) {
					throw new IllegalArgumentException(archivoInexistenteLabelConst);
				}
				
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(this, "Error al seleccionar la imagen: " + ex.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
				imagenSeleccionada = null;
				imagenPathTextField.setText(imagenNoSeleccionadaLabelConst);
			}
		}
	}
	
	private void aceptarAction() {
		if (validarFormulario()) {
			try {
				// Procesar Nickname de organizador seleccionado
				String nickname = organizadorComboBox.getSelectedItem().toString();
				Integer startIndex = nickname.indexOf('(');
				Integer endIndex = nickname.indexOf(')');
				nickname = nickname.substring(startIndex + 1, endIndex);
				
				// Procesar fecha de inicio
				ZoneId zoneId = ZoneId.systemDefault();
				
				Date fechaIniDate = fechaInicioDate.getDate();
				Instant fechaIniInstant = fechaIniDate.toInstant();
				ZonedDateTime zonedFechaIni = fechaIniInstant.atZone(zoneId);
				LocalDate fechaIniLocalDate = zonedFechaIni.toLocalDate();
				
				// Procesar fecha de fin
				Date fechaFinalDate = fechaFinDate.getDate();
				Instant fechaFinInstant = fechaFinalDate.toInstant();
				ZonedDateTime zonedFechaFin = fechaFinInstant.atZone(zoneId);
				LocalDate fechaFinLocalDate = zonedFechaFin.toLocalDate();
				
				
				// Procesar fecha de alta
				Date fechaAlDate = fechaAltaDate.getDate();
				Instant fechaAltaInstant = fechaAlDate.toInstant();
				ZonedDateTime zonedFechaAlta = fechaAltaInstant.atZone(zoneId);
				LocalDate fechaAltaLocalDate = zonedFechaAlta.toLocalDate();
								

				// Dar de alta Edición de Evento
				ctrlEvento.altaEdicionEvento(
					eventoComboBox.getSelectedItem().toString(),
					nickname,
					nombreTextField.getText(),
					siglaTextField.getText(),
					paisTextField.getText(),
					ciudadTextField.getText(),
					fechaIniLocalDate,
					fechaFinLocalDate,
					fechaAltaLocalDate,
					EstadoEdicion.Ingresada.toString(),
					imagenSeleccionada,
					videoTextField.getText()
				);
				
				JOptionPane.showMessageDialog(
					this, 
					edicionCreadaConst, 
					tituloEdicionCreadaConst,
	                JOptionPane.INFORMATION_MESSAGE
		        );
				
				reiniciarFormulario();
				setVisible(false);
				this.dispose();
			} catch (EdicionRepetidaException exc) {
				JOptionPane.showMessageDialog(
					this, 
					exc.getMessage(), 
					tituloEdicionRepetidaConst,
	                JOptionPane.ERROR_MESSAGE
		        );
			}
		}
	}
	
	private void cancelarAction() {
		reiniciarFormulario();
		setVisible(false);
		this.dispose();
	}
}
