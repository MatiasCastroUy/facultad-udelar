package presentacion;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Vector;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import excepciones.TipoRegistroRepetidoExcepcion;
import logica.Fabrica;
import logica.IControladorEvento;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class AltaTipoRegistro extends JInternalFrame {
	// TO-DO: IMPLEMENTAR FÁBRICA PARA OBTENER INTERFAZ DEL CONTROLADOR
	private IControladorEvento contr = Fabrica.getInstance().getIControladorEvento();

	private JTextField tfNombre;
	private JTextField tfCosto;
	private JTextField tfCupo;
	private JTextArea taDescripcion;

	private JComboBox<String> cbEvento;
	private JComboBox<String> cbEdicion;
	private JButton btnAceptar;
	private JButton btnCancelar;

	private String evtSeleccionado;
	private String edSeleccionada;

	private void darDeAlta(IControladorEvento contr, JButton btnAceptar) {
		String nombre = tfNombre.getText();
		String descripcion = taDescripcion.getText();
		String costo_str = tfCosto.getText();
		String cupo_str = tfCupo.getText();
		float costo = 0.0f;
		int cupo = 0;

		if (nombre.isEmpty() || descripcion.isEmpty() || costo_str.isEmpty() || cupo_str.isEmpty()) {
			JOptionPane.showMessageDialog(btnAceptar, "Debe completar todos los campos.");
			return;
		}
		;

		try {
			costo = Float.parseFloat(costo_str);
		} catch (NumberFormatException exc) {
			JOptionPane.showMessageDialog(btnAceptar, "El valor de costo ingresado no es válido.", "Costo inválido",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (costo <= 0) {
			JOptionPane.showMessageDialog(btnAceptar, "El valor de costo debe ser mayor a cero.",
					"Costo nulo o negativo", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			cupo = Integer.parseInt(cupo_str);
		} catch (NumberFormatException exc) {
			JOptionPane.showMessageDialog(btnAceptar, "El valor de cupo ingresado no es válido.", "Cupo inválido",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (cupo <= 0) {
			JOptionPane.showMessageDialog(btnAceptar, "El valor de cupo debe ser un entero positivo.",
					"Cupo nulo o negativo", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			contr.crearTipoRegistro(evtSeleccionado, edSeleccionada, nombre, descripcion, costo, cupo);
		} catch (TipoRegistroRepetidoExcepcion exc) {
			JOptionPane.showMessageDialog(btnAceptar, exc.getMessage(), "Tipo de Registro repetido",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		limpiarFormulario();
		JOptionPane.showMessageDialog(btnAceptar, "El Tipo de Registro " + nombre + " se ha creado con éxito.",
				"Tipo de Registro creado", JOptionPane.PLAIN_MESSAGE);
		;
		setVisible(false);
	}

	// recarga los eventos y resetea el formulario
	private void limpiarFormulario() {
		List<String> eventos = new Vector<>(contr.listarEventos());
		cbEvento.setModel(new DefaultComboBoxModel<>((Vector<String>) eventos));
		cbEvento.setSelectedIndex(-1);
		tfNombre.setText("");
		tfCosto.setText("");
		tfCupo.setText("");
		cbEdicion.setModel(new DefaultComboBoxModel<String>());
		cbEdicion.setEnabled(false);
		btnAceptar.setEnabled(false);
		evtSeleccionado = edSeleccionada = "";
	}

	public AltaTipoRegistro(JPanel parentPanel) {

		// Inicialización de variables

		evtSeleccionado = "";
		edSeleccionada = "";

		// Definición de componentes

		setIconifiable(true);
		setMaximizable(true);
		setClosable(true);
		setTitle("Alta de Tipo de Registro");
		setSize(344, 383);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		parentPanel.add(this);

		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblEvento = new JLabel("Evento:");
		lblEvento.setBounds(23, 33, 62, 16);
		panel.add(lblEvento);

		cbEvento = new JComboBox<String>();
		cbEvento.setBounds(127, 28, 180, 26);
		panel.add(cbEvento);

		JLabel lblEdicin = new JLabel("Edición:");
		lblEdicin.setBounds(23, 71, 62, 16);
		panel.add(lblEdicin);

		cbEdicion = new JComboBox<String>();
		cbEdicion.setEnabled(false);
		cbEdicion.setBounds(127, 66, 180, 26);
		panel.add(cbEdicion);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(23, 104, 62, 16);
		panel.add(lblNombre);

		tfNombre = new JTextField();
		tfNombre.setBounds(127, 98, 180, 28);
		panel.add(tfNombre);
		tfNombre.setColumns(10);

		JLabel lblDescripcin = new JLabel("Descripción:");
		lblDescripcin.setBounds(23, 160, 90, 16);
		panel.add(lblDescripcin);

		JScrollPane scrollDesc = new JScrollPane();
		scrollDesc.setBounds(126, 138, 181, 65);
		panel.add(scrollDesc);

		taDescripcion = new JTextArea();
		taDescripcion.setLineWrap(true);
		scrollDesc.setViewportView(taDescripcion);

		JLabel lblCosto = new JLabel("Costo:");
		lblCosto.setBounds(23, 221, 62, 16);
		panel.add(lblCosto);

		tfCosto = new JTextField();
		tfCosto.setBounds(127, 215, 123, 28);
		panel.add(tfCosto);
		tfCosto.setColumns(10);

		JLabel lblCupo = new JLabel("Cupo:");
		lblCupo.setBounds(23, 254, 62, 16);
		panel.add(lblCupo);

		tfCupo = new JTextField();
		tfCupo.setBounds(127, 248, 123, 28);
		panel.add(tfCupo);
		tfCupo.setColumns(10);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(226, 288, 98, 26);
		panel.add(btnCancelar);

		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(125, 288, 98, 26);
		btnAceptar.setEnabled(false);
		panel.add(btnAceptar);

		// Funcionamiento (action listeners)

		limpiarFormulario();

		cbEvento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JComboBox<String> cbEv = (JComboBox<String>) event.getSource();
				if (cbEv.getSelectedIndex() >= 0) { // al darle a cancelar se dispara este actionlistener, chequear que
													// no sea ese caso
					evtSeleccionado = (String) cbEv.getSelectedItem();
					List<String> ediciones = new Vector<>(contr.listarEdiciones(evtSeleccionado));
					cbEdicion.setModel(new DefaultComboBoxModel<>((Vector<String>) ediciones));
					cbEdicion.setEnabled(true);
					cbEdicion.setSelectedIndex(-1);
				}
			}
		});

		cbEdicion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JComboBox<String> cbEd = (JComboBox<String>) event.getSource();
				edSeleccionada = (String) cbEd.getSelectedItem();
				btnAceptar.setEnabled(true);
			}
		});

		// resetea los campos y oculta la ventana
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				limpiarFormulario();
				setVisible(false);
			}
		});

		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				darDeAlta(contr, btnAceptar);
			}
		});

		addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosed(InternalFrameEvent event) {

			}

			public void internalFrameClosing(InternalFrameEvent event) {
				limpiarFormulario();
			}
		});

		// para que recargue los eventos al abrirse
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent event) {
				limpiarFormulario();
			}
		});

	}
}
