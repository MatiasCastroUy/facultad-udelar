package presentacion;

import java.awt.EventQueue;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ConsultaPatrocinio extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JTextField textField1;
	private JTextField textField2;
	private JTextField textField3;
	private JTextField textField4;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConsultaPatrocinio frame = new ConsultaPatrocinio();
					frame.setVisible(true);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ConsultaPatrocinio() {
		setTitle("Consultar Patrocinio");
		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);
		setBounds(100, 100, 357, 339);
		getContentPane().setLayout(null);
		
		JLabel lblEvento = new JLabel("Evento:");
		lblEvento.setBounds(54, 36, 70, 15);
		getContentPane().add(lblEvento);
		
		JLabel lblEdicin = new JLabel("Edición:");
		lblEdicin.setBounds(54, 63, 70, 15);
		getContentPane().add(lblEdicin);
		
		JLabel lblPatrocinio = new JLabel("Patrocinio:");
		lblPatrocinio.setBounds(32, 90, 92, 15);
		getContentPane().add(lblPatrocinio);
		
		JLabel lblFecha = new JLabel("Información: del patrocinador:");
		lblFecha.setBounds(54, 131, 256, 15);
		getContentPane().add(lblFecha);
		
		JLabel lblFecha1 = new JLabel(" Fecha:");
		lblFecha1.setBounds(80, 168, 70, 15);
		getContentPane().add(lblFecha1);
		
		JLabel lblMontoAportado = new JLabel("Monto aportado:");
		lblMontoAportado.setBounds(12, 195, 125, 15);
		getContentPane().add(lblMontoAportado);
		
		JLabel lblNewLabel = new JLabel(" Registros:");
		lblNewLabel.setBounds(56, 222, 81, 15);
		getContentPane().add(lblNewLabel);
		
		JLabel lblCodigo = new JLabel("   Codigo:");
		lblCodigo.setBounds(67, 247, 70, 15);
		getContentPane().add(lblCodigo);
		
		JLabel lblNivel = new JLabel("Nivel:");
		lblNivel.setBounds(92, 274, 58, 15);
		getContentPane().add(lblNivel);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"--Eventos--"}));
		comboBox.setBounds(120, 31, 106, 20);
		getContentPane().add(comboBox);
		
		JComboBox comboBox1 = new JComboBox();
		comboBox1.setModel(new DefaultComboBoxModel(new String[] {"--Ediciones--"}));
		comboBox1.setBounds(120, 60, 106, 20);
		getContentPane().add(comboBox1);
		
		JComboBox comboBox2 = new JComboBox();
		comboBox2.setModel(new DefaultComboBoxModel(new String[] {"--Patrocinios"}));
		comboBox2.setBounds(120, 90, 108, 20);
		getContentPane().add(comboBox2);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(134, 166, 114, 19);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		textField1 = new JTextField();
		textField1.setEditable(false);
		textField1.setBounds(134, 195, 114, 19);
		getContentPane().add(textField1);
		textField1.setColumns(10);
		
		textField2 = new JTextField();
		textField2.setEditable(false);
		textField2.setBounds(134, 220, 114, 19);
		getContentPane().add(textField2);
		textField2.setColumns(10);
		
		textField3 = new JTextField();
		textField3.setEditable(false);
		textField3.setBounds(134, 245, 114, 19);
		getContentPane().add(textField3);
		textField3.setColumns(10);
		
		textField4 = new JTextField();
		textField4.setEditable(false);
		textField4.setBounds(134, 272, 114, 19);
		getContentPane().add(textField4);
		textField4.setColumns(10);

	}

}
