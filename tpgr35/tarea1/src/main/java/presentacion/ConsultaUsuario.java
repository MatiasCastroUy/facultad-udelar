package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.toedter.calendar.JDateChooser;

import java.time.LocalDate;
import java.util.Date;

import excepciones.UsuarioNoExisteException;
import logica.DTOrganizador;
import logica.DTAsistente;
import logica.DTUsuario;
import logica.IControladorEvento;
import logica.IControladorUsuario;

@SuppressWarnings("serial")
public class ConsultaUsuario extends JInternalFrame {

	private IControladorUsuario ctrlUsuario;
	private IControladorEvento ctrlEvento;

	private ConsultaDeRegistro consultadeRegistro;
	private ConsultaEdicionEvento consultaEdicionEvento;

	@SuppressWarnings("unused")
	private JPanel parentPanel;

	private JTextField textFieldNickname;
	private JTextField textFieldCorreo;
	private JTextField textFieldNombre;
	private JTextField textFieldApellido;
	private JDateChooser dateChooserFechaNacimiento;
	private JTextField textFieldDescripcion;
	private JTextField textFieldSitioWeb;

	private JLabel lblUsuario;
	private JLabel lblNickname;
	private JLabel lblCorreo;
	private JLabel lblNombre;
	private JLabel lblApellido;
	private JLabel lblFechaNacimiento;
	private JLabel lblDescripcion;
	private JLabel lblSitioWeb;
	private JLabel lblImagenPerfil;

	private JButton btnEdicionesAsociadas;
	private JButton btnRegistrosAsociados;

	private JComboBox<DTUsuario> comboBoxUsuarios;

	public ConsultaUsuario(JPanel parentPanel, IControladorUsuario icu, IControladorEvento ice) {
		this.parentPanel = parentPanel;
		this.ctrlUsuario = icu;
		this.ctrlEvento = ice;

		getContentPane().setEnabled(false);
		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);
		setTitle("Datos Usuario");
		setBounds(100, 100, 549, 397);
		getContentPane().setLayout(null);
		parentPanel.add(this);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 533, 367);
		getContentPane().add(panel);
		panel.setLayout(null);

		lblUsuario = new JLabel("Usuario");
		lblUsuario.setBounds(0, 0, 506, 32);
		lblUsuario.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblUsuario);

		comboBoxUsuarios = new JComboBox<DTUsuario>();
		comboBoxUsuarios.setBounds(10, 43, 486, 39);
		panel.add(comboBoxUsuarios);

		comboBoxUsuarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cmdBuscarUsuarioActionPerformed(arg0);
			}
		});
		;

		lblNickname = new JLabel("Nickname:");
		lblNickname.setBounds(20, 93, 66, 39);
		panel.add(lblNickname);

		lblCorreo = new JLabel("Correo:");
		lblCorreo.setBounds(20, 131, 66, 39);
		panel.add(lblCorreo);

		lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(20, 169, 66, 39);
		panel.add(lblNombre);

		lblApellido = new JLabel("Apellido:");
		lblApellido.setBounds(20, 208, 66, 39);
		panel.add(lblApellido);

		lblFechaNacimiento = new JLabel("Nacimiento:");
		lblFechaNacimiento.setBounds(20, 246, 66, 39);
		panel.add(lblFechaNacimiento);

		lblDescripcion = new JLabel("Descripcion:");
		lblDescripcion.setBounds(20, 286, 66, 39);
		panel.add(lblDescripcion);

		lblSitioWeb = new JLabel("Sitio Web:");
		lblSitioWeb.setBounds(20, 325, 66, 39);
		panel.add(lblSitioWeb);

		lblImagenPerfil = new JLabel();
		lblImagenPerfil.setBounds(390, 93, 120, 120);
		lblImagenPerfil.setBorder(javax.swing.BorderFactory.createTitledBorder("Imagen de Perfil"));
		lblImagenPerfil.setHorizontalAlignment(SwingConstants.CENTER);
		lblImagenPerfil.setVerticalAlignment(SwingConstants.CENTER);
		lblImagenPerfil.setText("Sin imagen");
		panel.add(lblImagenPerfil);

		textFieldNickname = new JTextField();
		textFieldNickname.setEditable(false);
		textFieldNickname.setColumns(10);
		textFieldNickname.setBounds(96, 102, 266, 20);
		panel.add(textFieldNickname);

		textFieldCorreo = new JTextField();
		textFieldCorreo.setEditable(false);
		textFieldCorreo.setColumns(10);
		textFieldCorreo.setBounds(96, 140, 266, 20);
		panel.add(textFieldCorreo);

		textFieldNombre = new JTextField();
		textFieldNombre.setEditable(false);
		textFieldNombre.setColumns(10);
		textFieldNombre.setBounds(96, 178, 266, 20);
		panel.add(textFieldNombre);

		textFieldApellido = new JTextField();
		textFieldApellido.setEditable(false);
		textFieldApellido.setColumns(10);
		textFieldApellido.setBounds(96, 217, 266, 20);
		panel.add(textFieldApellido);

		dateChooserFechaNacimiento = new JDateChooser();
		dateChooserFechaNacimiento.setDateFormatString("dd/MM/yyyy");
		dateChooserFechaNacimiento.setEnabled(closable);
		dateChooserFechaNacimiento.setBounds(96, 255, 266, 20);
		panel.add(dateChooserFechaNacimiento);

		textFieldDescripcion = new JTextField();
		textFieldDescripcion.setEditable(false);
		textFieldDescripcion.setColumns(10);
		textFieldDescripcion.setBounds(96, 295, 266, 20);
		panel.add(textFieldDescripcion);

		textFieldSitioWeb = new JTextField();
		textFieldSitioWeb.setEditable(false);
		textFieldSitioWeb.setColumns(10);
		textFieldSitioWeb.setBounds(96, 334, 266, 20);
		panel.add(textFieldSitioWeb);

		btnEdicionesAsociadas = new JButton("Ediciones Asociadas");
		btnEdicionesAsociadas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				DTUsuario usrSeleccionado = (DTUsuario) comboBoxUsuarios.getSelectedItem();
				if (usrSeleccionado instanceof DTOrganizador) {
					consultaEdicionEvento = new ConsultaEdicionEvento(parentPanel, ctrlEvento, ctrlUsuario);
					consultaEdicionEvento.setVisible(true);
				}
			}
		});
		btnEdicionesAsociadas.setEnabled(false);
		btnEdicionesAsociadas.setBounds(372, 295, 151, 59);
		panel.add(btnEdicionesAsociadas);

		btnRegistrosAsociados = new JButton("Registros Asociados");
		btnRegistrosAsociados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				DTUsuario usrSeleccionado = (DTUsuario) comboBoxUsuarios.getSelectedItem();
				if (usrSeleccionado instanceof DTAsistente) {
					consultadeRegistro = new ConsultaDeRegistro(parentPanel, usrSeleccionado.getNickname());
					consultadeRegistro.setVisible(true);
				}
			}
		});
		btnRegistrosAsociados.setEnabled(false);
		btnRegistrosAsociados.setBounds(372, 217, 151, 58);
		panel.add(btnRegistrosAsociados);

		cargarUsuarios();
	}
	
	private void mostrarImagenPerfil(String rutaImagen) {
		if (rutaImagen != null && !rutaImagen.trim().isEmpty()) {
			try {
				System.out.println("DEBUG: Intentando cargar imagen desde: " + rutaImagen);
				File imagenFile = new File(rutaImagen);
				System.out.println("DEBUG: Archivo existe: " + imagenFile.exists());
				System.out.println("DEBUG: Ruta absoluta: " + imagenFile.getAbsolutePath());
				
				if (imagenFile.exists()) {
					Image imagen = ImageIO.read(imagenFile);
					if (imagen != null) {
						Image imagenRedimensionada = imagen.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
						ImageIcon icon = new ImageIcon(imagenRedimensionada);
						lblImagenPerfil.setIcon(icon);
						lblImagenPerfil.setText("");
						System.out.println("DEBUG: Imagen cargada exitosamente");
					} else {
						lblImagenPerfil.setIcon(null);
						lblImagenPerfil.setText("Imagen no válida");
						System.out.println("DEBUG: ImageIO.read devolvió null");
					}
				} else {
					lblImagenPerfil.setIcon(null);
					lblImagenPerfil.setText("Archivo no encontrado: " + rutaImagen);
					System.out.println("DEBUG: Archivo no encontrado");
				}
			} catch (IOException ioException) {
				lblImagenPerfil.setIcon(null);
				lblImagenPerfil.setText("Error al cargar imagen");
				System.out.println("DEBUG: IOException: " + ioException.getMessage());
				ioException.printStackTrace();
			}
		} else {
			lblImagenPerfil.setIcon(null);
			lblImagenPerfil.setText("Sin imagen");
			System.out.println("DEBUG: rutaImagen es null o vacía: '" + rutaImagen + "'");
		}
	}

	protected void cmdBuscarUsuarioActionPerformed(ActionEvent arg0) {
		DTUsuario usrSeleccionado = (DTUsuario) comboBoxUsuarios.getSelectedItem();
		if (usrSeleccionado != null) {
			textFieldNickname.setText(usrSeleccionado.getNickname());
			textFieldCorreo.setText(usrSeleccionado.getCorreo());
			textFieldNombre.setText(usrSeleccionado.getNombre());
			mostrarImagenPerfil(usrSeleccionado.getImagenPerfil());
			if (usrSeleccionado instanceof DTAsistente) {
				DTAsistente asis = (DTAsistente) usrSeleccionado;
				textFieldApellido.setText(asis.getApellido());
				LocalDate fechaNac = asis.getFechaNacimiento();
				if (fechaNac != null) {
					Date date = Date.from(fechaNac.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
					dateChooserFechaNacimiento.setDate(date);
				} else {
					dateChooserFechaNacimiento.setDate(null);
				}
				textFieldDescripcion.setText("");
				textFieldSitioWeb.setText("");
				btnRegistrosAsociados.setEnabled(true);
				btnEdicionesAsociadas.setEnabled(false);
			} else {
				DTOrganizador orga = (DTOrganizador) usrSeleccionado;
				textFieldDescripcion.setText(orga.getDescripcion());
				textFieldSitioWeb.setText(orga.getSitioWeb());
				textFieldApellido.setText("");
				dateChooserFechaNacimiento.setEnabled(false);
				btnRegistrosAsociados.setEnabled(false);
				btnEdicionesAsociadas.setEnabled(true);
			}
		} else {
			// Limpia imagen si no hay usuario seleccionado
			mostrarImagenPerfil(null);
		}
	}

	public void cargarUsuarios() {
		DefaultComboBoxModel<DTUsuario> model;
		try {
			Set<DTUsuario> usrSet = ctrlUsuario.listarUsuarios();
			DTUsuario[] usrArreglo = usrSet.toArray(new DTUsuario[0]);
			model = new DefaultComboBoxModel<>(usrArreglo);
			comboBoxUsuarios.setModel(model);
		} catch (UsuarioNoExisteException usuarioNoExiste) {
			// No se imprime mensaje de error sino que simplemente no se muestra ningún
			// elemento
			comboBoxUsuarios.setModel(new DefaultComboBoxModel<>());
		}

	}

}