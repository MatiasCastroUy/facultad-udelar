package presentacion;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import logica.DTUsuario;
import logica.IControladorUsuario;
import logica.Fabrica;
import javax.swing.JOptionPane;
import excepciones.UsuarioNoExisteException;
import excepciones.ContrasenaInvalidaException;

@SuppressWarnings("serial")
public class ModificarUsuario extends JInternalFrame {
	
	private JTextField textField;
	private JTextField textField1;
	private JTextField textField2;
	private JTextField textField3;
	private JTextField textField4;
	private JTextField textField5;
	private JTextField textField6;

	
	public ModificarUsuario(JPanel parentPanel, IControladorUsuario icu) {
		
		setTitle("Modificar ");
		setResizable(true);
		setIconifiable(true);
		setMaximizable(true);
		setClosable(true);
		setBounds(100, 100, 542, 503);
		getContentPane().setLayout(null);
		parentPanel.add(this);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 526, 473);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblUsuariosRegistrados = new JLabel("Modificar Datos");
		lblUsuariosRegistrados.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsuariosRegistrados.setBounds(0, 0, 532, 48);
		panel.add(lblUsuariosRegistrados);
		
		DTUsuario[] usuarios = {
        		new DTUsuario("Pedrinho", "Pedro", "pedro@gmail.com"),
        		new DTUsuario("Anita", "Ana", "ana@gmail.com"),
        		new DTUsuario("Juancin", "Juan", "juan@gmail.com"),
        		new DTUsuario("Lu", "Lucía", "lucia@gmail.com")
        };
        
        JComboBox comboBoxUsuarios = new JComboBox<DTUsuario>(usuarios);
        comboBoxUsuarios.setBounds(10, 50, 518, 47);
        panel.add(comboBoxUsuarios);
        
		JLabel lblNewLabel = new JLabel("Nickname:");
		lblNewLabel.setBounds(20, 106, 66, 53);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel1 = new JLabel("Correo:");
		lblNewLabel1.setBounds(20, 158, 66, 53);
		panel.add(lblNewLabel1);
		
		JLabel lblNewLabel2 = new JLabel("Nombre:");
		lblNewLabel2.setBounds(20, 211, 66, 53);
		panel.add(lblNewLabel2);
		
		JLabel lblNewLabel3 = new JLabel("Apellido:");
		lblNewLabel3.setBounds(20, 263, 66, 53);
		panel.add(lblNewLabel3);
		
		JLabel lblNewLabel4 = new JLabel("Nacimiento:");
		lblNewLabel4.setBounds(20, 315, 66, 53);
		panel.add(lblNewLabel4);
		
		JLabel lblNewLabel5 = new JLabel("Sitio Web:");
		lblNewLabel5.setBounds(20, 418, 66, 53);
		panel.add(lblNewLabel5);
		
		JLabel lblNewLabel6 = new JLabel("Descripcion:");
		lblNewLabel6.setBounds(20, 368, 66, 53);
		panel.add(lblNewLabel6);
		
		textField = new JTextField();
		textField.setBounds(96, 122, 266, 20);
		panel.add(textField);
		textField.setColumns(10);
		
		textField1 = new JTextField();
		textField1.setBounds(96, 174, 266, 20);
		panel.add(textField1);
		textField1.setColumns(10);
		
		textField2 = new JTextField();
		textField2.setBounds(96, 227, 266, 20);
		panel.add(textField2);
		textField2.setColumns(10);
		
		textField3 = new JTextField();
		textField3.setBounds(96, 279, 266, 20);
		panel.add(textField3);
		textField3.setColumns(10);
		
		textField4 = new JTextField();
		textField4.setBounds(96, 331, 266, 20);
		panel.add(textField4);
		textField4.setColumns(10);
		
		textField5 = new JTextField();
		textField5.setBounds(96, 384, 266, 20);
		panel.add(textField5);
		textField5.setColumns(10);
		
		textField6 = new JTextField();
		textField6.setBounds(96, 434, 266, 20);
		panel.add(textField6);
		textField6.setColumns(10);
		
		JButton btnNewButton = new JButton("Editar");
		btnNewButton.setBounds(382, 418, 134, 53);
		panel.add(btnNewButton);

		// Added logic to handle user modification
		JButton btnGuardar = new JButton("Guardar Cambios");
		btnGuardar.setBounds(96, 450, 150, 30);
		panel.add(btnGuardar);

		btnGuardar.addActionListener(e -> {
			String nickname = (String) comboBoxUsuarios.getSelectedItem();
			String nombre = textField.getText();
			String correo = textField1.getText();
			String contrasena = textField2.getText();
			String apellido = textField3.getText();
			String fechaNacimiento = textField4.getText();
			String descripcion = textField5.getText();
			String sitioWeb = textField6.getText();

			try {
				IControladorUsuario controladorUsuario = Fabrica.getInstance().getIControladorUsuario();
				controladorUsuario.modificarUsuario(nickname, nombre, contrasena, LocalDate.parse(fechaNacimiento), descripcion, sitioWeb, "");
				JOptionPane.showMessageDialog(this, "Usuario modificado exitosamente.");
			} catch (UsuarioNoExisteException ex) {
				JOptionPane.showMessageDialog(this, "Error: El usuario no existe.");
			} catch (ContrasenaInvalidaException ex) {
				JOptionPane.showMessageDialog(this, "Error: La contraseña no es válida.");
			} catch (DateTimeParseException ex) {
				JOptionPane.showMessageDialog(this, "Error: La fecha de nacimiento no tiene un formato válido.");
			}
		});
	}
}
