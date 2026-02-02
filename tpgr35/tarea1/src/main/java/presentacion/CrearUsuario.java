package presentacion;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Date;
import java.io.File;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.toedter.calendar.JDateChooser;

import excepciones.ContrasenaInvalidaException;
import excepciones.UsuarioCorreoRepetidoException;
import excepciones.UsuarioRepetidoException;
import logica.IControladorUsuario;



@SuppressWarnings("serial")
public class CrearUsuario extends JInternalFrame {

	private IControladorUsuario ctrlUsuario;
	
    private JLabel lblIngreseNickname;
    private JLabel lblIngreseNombre;
    private JLabel lblIngreseCorreo;
    private JLabel lblIngreseContrasena;
    private JLabel lblIngreseConfirmacionContrasena;
    private JLabel lblIngreseImagenPerfil;
    private JLabel lblIngreseApellido;
    private JLabel lblIngreseFechaNacimiento;
    private JLabel lblIngreseDescripcion;
    private JLabel lblIngreseSitioWeb;
    
    private JTextField textFieldIngresarNickname;
    private JTextField textFieldIngresarNombre;
    private JTextField textFieldIngresarCorreo;
    private JPasswordField passwordFieldContrasena;
    private JPasswordField passwordFieldConfirmacionContrasena;
    private JTextField textFieldRutaImagen;
    private JButton btnSeleccionarImagen;
    private JTextField textFieldIngresarApellido;
    private JDateChooser dateChooserIngresarFechaNacimiento;
    private JTextField textFieldIngresarDescripcion;
    private JTextField textFieldIngresarSitioWeb;
    
    private String imagenSeleccionada;
    
    private JRadioButton rdbtnAsistente;
    private JRadioButton rdbtnOrganizador;
    
    private JButton btnAceptar;
    private JButton btnCancelar;
    
	public CrearUsuario(JPanel parentPanel, IControladorUsuario icu) {
		this.ctrlUsuario = icu;
		getContentPane().setEnabled(false);
		
		setResizable(true);
        setIconifiable(true);
        setMaximizable(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setClosable(true);
        setTitle("Registrar un Usuario");
        setBounds(10, 40, 500, 450);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        parentPanel.add(this);
        
        
        JPanel panel = new JPanel();
        getContentPane().add(panel);
        panel.setLayout(gridBagLayout);
        
        lblIngreseNickname = new JLabel("Nickname:");
        GridBagConstraints gbc_lblIngreseNickname = new GridBagConstraints();
        gbc_lblIngreseNickname.insets = new Insets(0, 0, 5, 5);
        gbc_lblIngreseNickname.gridx = 0;
        gbc_lblIngreseNickname.gridy = 1;
        panel.add(lblIngreseNickname, gbc_lblIngreseNickname);
        
        textFieldIngresarNickname = new JTextField();
        GridBagConstraints gbc_textFieldIngresarNickname = new GridBagConstraints();
        gbc_textFieldIngresarNickname.gridwidth = 8;
        gbc_textFieldIngresarNickname.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldIngresarNickname.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldIngresarNickname.gridx = 1;
        gbc_textFieldIngresarNickname.gridy = 1;
        panel.add(textFieldIngresarNickname, gbc_textFieldIngresarNickname);
        textFieldIngresarNickname.setColumns(10);
        
        lblIngreseNombre = new JLabel("Nombre:");
        GridBagConstraints gbc_lblIngreseNombre = new GridBagConstraints();
        gbc_lblIngreseNombre.insets = new Insets(0, 0, 5, 5);
        gbc_lblIngreseNombre.gridx = 0;
        gbc_lblIngreseNombre.gridy = 3;
        panel.add(lblIngreseNombre, gbc_lblIngreseNombre);
        
        textFieldIngresarNombre = new JTextField();
        GridBagConstraints gbc_textFieldIngresarNombre = new GridBagConstraints();
        gbc_textFieldIngresarNombre.gridwidth = 8;
        gbc_textFieldIngresarNombre.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldIngresarNombre.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldIngresarNombre.gridx = 1;
        gbc_textFieldIngresarNombre.gridy = 3;
        panel.add(textFieldIngresarNombre, gbc_textFieldIngresarNombre);
        textFieldIngresarNombre.setColumns(10);
        
        lblIngreseCorreo = new JLabel("Correo:");
        GridBagConstraints gbc_lblIngreseCorreo = new GridBagConstraints();
        gbc_lblIngreseCorreo.insets = new Insets(0, 0, 5, 5);
        gbc_lblIngreseCorreo.gridx = 0;
        gbc_lblIngreseCorreo.gridy = 5;
        panel.add(lblIngreseCorreo, gbc_lblIngreseCorreo);
        
        textFieldIngresarCorreo = new JTextField();
        GridBagConstraints gbc_textFieldIngresarCorreo = new GridBagConstraints();
        gbc_textFieldIngresarCorreo.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldIngresarCorreo.gridwidth = 8;
        gbc_textFieldIngresarCorreo.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldIngresarCorreo.gridx = 1;
        gbc_textFieldIngresarCorreo.gridy = 5;
        panel.add(textFieldIngresarCorreo, gbc_textFieldIngresarCorreo);
        textFieldIngresarCorreo.setColumns(10);
        
        lblIngreseContrasena = new JLabel("Contraseña:");
        GridBagConstraints gbc_lblIngreseContrasena = new GridBagConstraints();
        gbc_lblIngreseContrasena.insets = new Insets(0, 0, 5, 5);
        gbc_lblIngreseContrasena.gridx = 0;
        gbc_lblIngreseContrasena.gridy = 6;
        panel.add(lblIngreseContrasena, gbc_lblIngreseContrasena);
        
        passwordFieldContrasena = new JPasswordField();
        GridBagConstraints gbc_passwordFieldContrasena = new GridBagConstraints();
        gbc_passwordFieldContrasena.gridwidth = 8;
        gbc_passwordFieldContrasena.insets = new Insets(0, 0, 5, 5);
        gbc_passwordFieldContrasena.fill = GridBagConstraints.HORIZONTAL;
        gbc_passwordFieldContrasena.gridx = 1;
        gbc_passwordFieldContrasena.gridy = 6;
        panel.add(passwordFieldContrasena, gbc_passwordFieldContrasena);
        passwordFieldContrasena.setColumns(10);
        
        lblIngreseConfirmacionContrasena = new JLabel("Confirmar Contraseña:");
        GridBagConstraints gbc_lblIngreseConfirmacionContrasena = new GridBagConstraints();
        gbc_lblIngreseConfirmacionContrasena.insets = new Insets(0, 0, 5, 5);
        gbc_lblIngreseConfirmacionContrasena.gridx = 0;
        gbc_lblIngreseConfirmacionContrasena.gridy = 7;
        panel.add(lblIngreseConfirmacionContrasena, gbc_lblIngreseConfirmacionContrasena);
        
        passwordFieldConfirmacionContrasena = new JPasswordField();
        GridBagConstraints gbc_passwordFieldConfirmacionContrasena = new GridBagConstraints();
        gbc_passwordFieldConfirmacionContrasena.gridwidth = 8;
        gbc_passwordFieldConfirmacionContrasena.insets = new Insets(0, 0, 5, 5);
        gbc_passwordFieldConfirmacionContrasena.fill = GridBagConstraints.HORIZONTAL;
        gbc_passwordFieldConfirmacionContrasena.gridx = 1;
        gbc_passwordFieldConfirmacionContrasena.gridy = 7;
        panel.add(passwordFieldConfirmacionContrasena, gbc_passwordFieldConfirmacionContrasena);
        passwordFieldConfirmacionContrasena.setColumns(10);
        
        lblIngreseImagenPerfil = new JLabel("Imagen de Perfil:");
        GridBagConstraints gbc_lblIngreseImagenPerfil = new GridBagConstraints();
        gbc_lblIngreseImagenPerfil.insets = new Insets(0, 0, 5, 5);
        gbc_lblIngreseImagenPerfil.gridx = 0;
        gbc_lblIngreseImagenPerfil.gridy = 8;
        panel.add(lblIngreseImagenPerfil, gbc_lblIngreseImagenPerfil);
        
        textFieldRutaImagen = new JTextField();
        textFieldRutaImagen.setEditable(false);
        textFieldRutaImagen.setText("Sin imagen seleccionada");
        GridBagConstraints gbc_textFieldRutaImagen = new GridBagConstraints();
        gbc_textFieldRutaImagen.gridwidth = 6;
        gbc_textFieldRutaImagen.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldRutaImagen.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldRutaImagen.gridx = 1;
        gbc_textFieldRutaImagen.gridy = 8;
        panel.add(textFieldRutaImagen, gbc_textFieldRutaImagen);
        textFieldRutaImagen.setColumns(10);
        
        btnSeleccionarImagen = new JButton("Seleccionar");
        btnSeleccionarImagen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                cmdSeleccionarImagenActionPerformed(event);
            }
        });
        GridBagConstraints gbc_btnSeleccionarImagen = new GridBagConstraints();
        gbc_btnSeleccionarImagen.gridwidth = 2;
        gbc_btnSeleccionarImagen.insets = new Insets(0, 0, 5, 5);
        gbc_btnSeleccionarImagen.gridx = 7;
        gbc_btnSeleccionarImagen.gridy = 8;
        panel.add(btnSeleccionarImagen, gbc_btnSeleccionarImagen);
        
        ButtonGroup grupoBtn = new ButtonGroup();
        
        rdbtnAsistente =  new JRadioButton("Asistente");
        GridBagConstraints gbc_rdbtnAsistente = new GridBagConstraints();
        gbc_rdbtnAsistente.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnAsistente.gridx = 1;
        gbc_rdbtnAsistente.gridy = 9;
        panel.add(rdbtnAsistente, gbc_rdbtnAsistente);
        grupoBtn.add(rdbtnAsistente);
        
        
        
        rdbtnOrganizador = new JRadioButton("Organizador");
        GridBagConstraints gbc_rdbtnOrganizador = new GridBagConstraints();
        gbc_rdbtnOrganizador.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnOrganizador.gridx = 7;
        gbc_rdbtnOrganizador.gridy = 9;
        panel.add(rdbtnOrganizador, gbc_rdbtnOrganizador);
        grupoBtn.add(rdbtnOrganizador);
        
        
       
        
        lblIngreseApellido = new JLabel("Apellido:");
        GridBagConstraints gbc_lblIngreseApellido = new GridBagConstraints();
        gbc_lblIngreseApellido.insets = new Insets(0, 0, 5, 5);
        gbc_lblIngreseApellido.gridx = 0;
        gbc_lblIngreseApellido.gridy = 10;
        panel.add(lblIngreseApellido, gbc_lblIngreseApellido);
        
        textFieldIngresarApellido = new JTextField();
        GridBagConstraints gbc_textFieldIngresarApellido = new GridBagConstraints();
        gbc_textFieldIngresarApellido.gridwidth = 3;
        gbc_textFieldIngresarApellido.insets = new Insets(0, 0, 5, 5);
        gbc_textFieldIngresarApellido.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldIngresarApellido.gridx = 1;
        gbc_textFieldIngresarApellido.gridy = 10;
        panel.add(textFieldIngresarApellido, gbc_textFieldIngresarApellido);
        textFieldIngresarApellido.setColumns(10);
        
        lblIngreseDescripcion = new JLabel("Descripcion General:");
        GridBagConstraints gbc_lblIngreseDescripcion = new GridBagConstraints();
        gbc_lblIngreseDescripcion.anchor = GridBagConstraints.EAST;
        gbc_lblIngreseDescripcion.insets = new Insets(0, 0, 5, 5);
        gbc_lblIngreseDescripcion.gridx = 6;
        gbc_lblIngreseDescripcion.gridy = 10;
        panel.add(lblIngreseDescripcion, gbc_lblIngreseDescripcion);
        
        textFieldIngresarDescripcion = new JTextField();
        GridBagConstraints gbc_textFieldIngresarDescripcion = new GridBagConstraints();
        gbc_textFieldIngresarDescripcion.gridwidth = 3;
        gbc_textFieldIngresarDescripcion.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldIngresarDescripcion.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldIngresarDescripcion.gridx = 7;
        gbc_textFieldIngresarDescripcion.gridy = 10;
        panel.add(textFieldIngresarDescripcion, gbc_textFieldIngresarDescripcion);
        textFieldIngresarDescripcion.setColumns(10);
        
        lblIngreseFechaNacimiento = new JLabel("Nacimiento:");
        GridBagConstraints gbc_lblIngreseFechaNacimiento = new GridBagConstraints();
        gbc_lblIngreseFechaNacimiento.insets = new Insets(0, 0, 5, 5);
        gbc_lblIngreseFechaNacimiento.gridx = 0;
        gbc_lblIngreseFechaNacimiento.gridy = 12;
        panel.add(lblIngreseFechaNacimiento, gbc_lblIngreseFechaNacimiento);
        
        dateChooserIngresarFechaNacimiento = new JDateChooser();
        GridBagConstraints gbc_dateChooserIngresarFechaNacimiento = new GridBagConstraints();
        gbc_dateChooserIngresarFechaNacimiento.gridwidth = 3;
        gbc_dateChooserIngresarFechaNacimiento.insets = new Insets(0, 0, 5, 5);
        gbc_dateChooserIngresarFechaNacimiento.fill = GridBagConstraints.HORIZONTAL;
        gbc_dateChooserIngresarFechaNacimiento.gridx = 1;
        gbc_dateChooserIngresarFechaNacimiento.gridy = 12;
        panel.add(dateChooserIngresarFechaNacimiento, gbc_dateChooserIngresarFechaNacimiento);
        
        
        lblIngreseSitioWeb = new JLabel("Sitio Web:");
        GridBagConstraints gbc_lblIngreseSitioWeb = new GridBagConstraints();
        gbc_lblIngreseSitioWeb.insets = new Insets(0, 0, 5, 5);
        gbc_lblIngreseSitioWeb.gridx = 6;
        gbc_lblIngreseSitioWeb.gridy = 12;
        panel.add(lblIngreseSitioWeb, gbc_lblIngreseSitioWeb);
        
        textFieldIngresarSitioWeb = new JTextField();
        GridBagConstraints gbc_textFieldIngresarSitioWeb = new GridBagConstraints();
        gbc_textFieldIngresarSitioWeb.gridwidth = 3;
        gbc_textFieldIngresarSitioWeb.insets = new Insets(0, 0, 5, 0);
        gbc_textFieldIngresarSitioWeb.fill = GridBagConstraints.HORIZONTAL;
        gbc_textFieldIngresarSitioWeb.gridx = 7;
        gbc_textFieldIngresarSitioWeb.gridy = 12;
        panel.add(textFieldIngresarSitioWeb, gbc_textFieldIngresarSitioWeb);
        textFieldIngresarSitioWeb.setColumns(10);
        
     // Oculta y deshabilita todos los campos específicos al inicio
        lblIngreseApellido.setVisible(false);
        textFieldIngresarApellido.setVisible(false);
        textFieldIngresarApellido.setEditable(false);

        lblIngreseFechaNacimiento.setVisible(false);
        dateChooserIngresarFechaNacimiento.setVisible(false);
        dateChooserIngresarFechaNacimiento.setEnabled(closable);

        lblIngreseDescripcion.setVisible(false);
        textFieldIngresarDescripcion.setVisible(false);
        textFieldIngresarDescripcion.setEditable(false);

        lblIngreseSitioWeb.setVisible(false);
        textFieldIngresarSitioWeb.setVisible(false);
        textFieldIngresarSitioWeb.setEditable(false);
        
        rdbtnAsistente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                lblIngreseApellido.setVisible(true);
                textFieldIngresarApellido.setVisible(true);
                textFieldIngresarApellido.setEditable(true);

                lblIngreseFechaNacimiento.setVisible(true);
                dateChooserIngresarFechaNacimiento.setVisible(true);
                dateChooserIngresarFechaNacimiento.setEnabled(iconable);

                lblIngreseDescripcion.setVisible(false);
                textFieldIngresarDescripcion.setVisible(false);
                textFieldIngresarDescripcion.setEditable(false);

                lblIngreseSitioWeb.setVisible(false);
                textFieldIngresarSitioWeb.setVisible(false);
                textFieldIngresarSitioWeb.setEditable(false);

                textFieldIngresarDescripcion.setText("");
                textFieldIngresarSitioWeb.setText("");
            }
        });
        
        rdbtnOrganizador.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                lblIngreseDescripcion.setVisible(true);
                textFieldIngresarDescripcion.setVisible(true);
                textFieldIngresarDescripcion.setEditable(true);

                lblIngreseSitioWeb.setVisible(true);
                textFieldIngresarSitioWeb.setVisible(true);
                textFieldIngresarSitioWeb.setEditable(true);

                lblIngreseApellido.setVisible(false);
                textFieldIngresarApellido.setVisible(false);
                textFieldIngresarApellido.setEditable(false);

                lblIngreseFechaNacimiento.setVisible(false);
                dateChooserIngresarFechaNacimiento.setVisible(false);
                dateChooserIngresarFechaNacimiento.setEnabled(closable);

                textFieldIngresarApellido.setText("");
                dateChooserIngresarFechaNacimiento.setDate(null);
            }
        });
        
        
        btnAceptar = new JButton("Aceptar");
        btnAceptar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		cmdRegistroUsuarioActionPerformed(arg0);
        	}
        });
        GridBagConstraints gbc_btnAceptar = new GridBagConstraints();
        gbc_btnAceptar.gridwidth = 2;
        gbc_btnAceptar.insets = new Insets(0, 0, 5, 5);
        gbc_btnAceptar.gridx = 1;
        gbc_btnAceptar.gridy = 14;
        panel.add(btnAceptar, gbc_btnAceptar);
        
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent event) {
        		limpiarFormulario();
        		setVisible(false);
        	}
        });
        GridBagConstraints gbc_btnCancelar = new GridBagConstraints();
        gbc_btnCancelar.insets = new Insets(0, 0, 5, 5);
        gbc_btnCancelar.gridx = 6;
        gbc_btnCancelar.gridy = 14;
        panel.add(btnCancelar, gbc_btnCancelar);
        
	}
	
	protected void cmdSeleccionarImagenActionPerformed(ActionEvent event) {
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
				textFieldRutaImagen.setText(selectedFile.getName());
				
				System.out.println("DEBUG CrearUsuario: Imagen seleccionada: " + imagenSeleccionada);
				System.out.println("DEBUG CrearUsuario: Archivo existe: " + selectedFile.exists());
				
				// Verificar que el archivo existe
				if (!selectedFile.exists()) {
					throw new IllegalArgumentException("El archivo seleccionado no existe");
				}
				
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(this, "Error al seleccionar la imagen: " + ex.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
				imagenSeleccionada = null;
				textFieldRutaImagen.setText("Sin imagen seleccionada");
			}
		}
			
	}
	
	protected void cmdRegistroUsuarioActionPerformed(ActionEvent arg0) {
		String nicknameU = this.textFieldIngresarNickname.getText();
		String correoU = this.textFieldIngresarCorreo.getText();
		String nombreU = this.textFieldIngresarNombre.getText();
		String contrasenaU = new String(this.passwordFieldContrasena.getPassword());
		String confirmacionContrasenaU = new String(this.passwordFieldConfirmacionContrasena.getPassword());
		String apellidoU = this.textFieldIngresarApellido.getText();
		Date date = dateChooserIngresarFechaNacimiento.getDate();
		LocalDate fechaNacimientoU = null;
		if (date != null) {
		    fechaNacimientoU = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
		}
		String descripcionU = this.textFieldIngresarDescripcion.getText();	
		String sitioWebU = this.textFieldIngresarSitioWeb.getText();
		
		if (checkFormulario()) {
			if (apellidoU.isEmpty()) {
				try {
					ctrlUsuario.registrarUsuario(nicknameU, nombreU, correoU, contrasenaU, confirmacionContrasenaU, imagenSeleccionada, false, apellidoU, fechaNacimientoU, descripcionU, sitioWebU);
					JOptionPane.showMessageDialog(this, "El Usuario se ha creado con éxito", "Registrar Usuario", JOptionPane.INFORMATION_MESSAGE);
				}catch (UsuarioRepetidoException e){
					JOptionPane.showMessageDialog(this, e.getMessage(), "Registrar Usuario", JOptionPane.ERROR_MESSAGE);
				} catch (UsuarioCorreoRepetidoException e) {
					JOptionPane.showMessageDialog(this, e.getMessage(), "Registrar Usuario", JOptionPane.ERROR_MESSAGE);
				} catch (ContrasenaInvalidaException e) {
					JOptionPane.showMessageDialog(this, e.getMessage(), "Registrar Usuario", JOptionPane.ERROR_MESSAGE);
				}
			}else {
				try {
					ctrlUsuario.registrarUsuario(nicknameU, nombreU, correoU, contrasenaU, confirmacionContrasenaU, imagenSeleccionada, true, apellidoU, fechaNacimientoU, descripcionU, sitioWebU);
					JOptionPane.showMessageDialog(this, "El Usuario se ha creado con éxito", "Registrar Usuario", JOptionPane.INFORMATION_MESSAGE);
				}catch (UsuarioRepetidoException e){
					JOptionPane.showMessageDialog(this, e.getMessage(), "Registrar Usuario", JOptionPane.ERROR_MESSAGE);
				} catch (UsuarioCorreoRepetidoException e) {
					JOptionPane.showMessageDialog(this, e.getMessage(), "Registrar Usuario", JOptionPane.ERROR_MESSAGE);
				} catch (ContrasenaInvalidaException e) {
					JOptionPane.showMessageDialog(this, e.getMessage(), "Registrar Usuario", JOptionPane.ERROR_MESSAGE);
				}
			}
			limpiarFormulario();
			setVisible(false);
		}
	}

	
	
	private boolean checkFormulario() {
	    String nicknameU = this.textFieldIngresarNickname.getText();
	    String correoU = this.textFieldIngresarCorreo.getText();
	    String nombreU = this.textFieldIngresarNombre.getText();
	    String contrasenaU = new String(this.passwordFieldContrasena.getPassword());
	    String confirmacionContrasenaU = new String(this.passwordFieldConfirmacionContrasena.getPassword());
	    String apellidoU = this.textFieldIngresarApellido.getText();
	    Date date = dateChooserIngresarFechaNacimiento.getDate();
	    LocalDate fechaNacimientoU = null;
	    if (date != null) {
	        fechaNacimientoU = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
	    }
	    String descrpU = this.textFieldIngresarDescripcion.getText();

	    if (nicknameU.isEmpty() || correoU.isEmpty() || nombreU.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "No puede haber campos vacíos", "Registrar Usuario", JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	    
	   
	    if (contrasenaU.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "La contraseña no puede estar vacía", "Registrar Usuario", JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	    
	    if (!contrasenaU.equals(confirmacionContrasenaU)) {
	        JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden", "Registrar Usuario", JOptionPane.ERROR_MESSAGE);
	        return false;
	    }

	    if (rdbtnAsistente.isSelected()) {
	        if (apellidoU.isEmpty() || fechaNacimientoU == null) {
	            JOptionPane.showMessageDialog(this, "No puede haber campos vacíos en Asistente", "Registrar Usuario", JOptionPane.ERROR_MESSAGE);
	            return false;
	        }
	    } else if (rdbtnOrganizador.isSelected()) {
	        if (descrpU.isEmpty()) {
	            JOptionPane.showMessageDialog(this, "No puede estar vacío el campo Descripcion", "Registrar Usuario", JOptionPane.ERROR_MESSAGE);
	            return false;
	        }
	    } else {
	        JOptionPane.showMessageDialog(this, "Debe seleccionar Asistente u Organizador", "Registrar Usuario", JOptionPane.ERROR_MESSAGE);
	        return false;
	    }

	    return true;
	}
	
	private void limpiarFormulario() {
        textFieldIngresarNickname.setText("");
        textFieldIngresarNombre.setText("");
        textFieldIngresarCorreo.setText("");
        passwordFieldContrasena.setText("");
        passwordFieldConfirmacionContrasena.setText("");
        textFieldRutaImagen.setText("Sin imagen seleccionada");
        imagenSeleccionada = null;
        textFieldIngresarApellido.setText("");
        dateChooserIngresarFechaNacimiento.setDate(null);
        textFieldIngresarDescripcion.setText("");
        textFieldIngresarSitioWeb.setText("");
    }

}
