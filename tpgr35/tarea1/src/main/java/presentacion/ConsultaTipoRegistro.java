package presentacion;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import logica.IControladorEvento;
import logica.DTTipoRegistro;
import logica.Fabrica;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class ConsultaTipoRegistro extends JInternalFrame {
	private IControladorEvento contr = Fabrica.getInstance().getIControladorEvento();
	private JTextField tfCosto;
	private JTextField tfCupo;
	private JTextArea taDescripcion;
	private JComboBox<String> cbEvento;
	private JComboBox<String> cbEdicion;
	private JComboBox<String> cbTipoReg;

	private String evtSeleccionado = "";
	private String edSeleccionada = "";
	private String trSeleccionado = "";

	private boolean datosFijos = false;

	public void limpiarFormulario() {
		if (datosFijos)
			return;
		cbEvento.setModel(new DefaultComboBoxModel<String>(new Vector<String>(contr.listarEventos())));
		cbEvento.setSelectedIndex(-1);
		cbEdicion.setModel(new DefaultComboBoxModel<>());
		cbEdicion.setEnabled(false);
		cbTipoReg.setModel(new DefaultComboBoxModel<>());
		cbTipoReg.setEnabled(false);
		tfCosto.setText("");
		tfCupo.setText("");
		evtSeleccionado = edSeleccionada = "";
	}

	/**
	 * @wbp.parser.constructor
	 */
	public ConsultaTipoRegistro(JPanel parentPanel) {
		parentPanel.add(this);
		setSize(393, 393);
		setTitle("Consulta de Tipo de Registro");
		setToolTipText("");
		setIconifiable(true);
		setMaximizable(true);
		setClosable(true);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblEvento = new JLabel("Evento:");
		lblEvento.setBounds(26, 27, 62, 16);
		panel.add(lblEvento);

		cbEvento = new JComboBox();
		lblEvento.setLabelFor(cbEvento);
		cbEvento.setBounds(138, 22, 211, 26);
		panel.add(cbEvento);

		JLabel lblEdicin = new JLabel("Edición:");
		lblEdicin.setBounds(26, 65, 62, 16);
		panel.add(lblEdicin);

		cbEdicion = new JComboBox();
		cbEdicion.setEnabled(false);
		cbEdicion.setBounds(138, 60, 211, 26);
		panel.add(cbEdicion);

		JLabel lblInformacionDeTipo = new JLabel("Información de Tipo de Registro:");
		lblInformacionDeTipo.setHorizontalAlignment(SwingConstants.CENTER);
		lblInformacionDeTipo.setBounds(70, 158, 242, 16);
		panel.add(lblInformacionDeTipo);

		JLabel lblTipoReg = new JLabel("Tipo de Registro:");
		lblTipoReg.setBounds(26, 99, 107, 16);
		panel.add(lblTipoReg);

		cbTipoReg = new JComboBox();
		cbTipoReg.setEnabled(false);
		cbTipoReg.setBounds(138, 94, 211, 26);
		panel.add(cbTipoReg);

		JLabel lblDescripcin = new JLabel("Descripción:");
		lblDescripcin.setBounds(25, 192, 90, 16);
		panel.add(lblDescripcin);

		JScrollPane scrollDesc = new JScrollPane();
		scrollDesc.setBounds(127, 183, 222, 63);
		panel.add(scrollDesc);

		taDescripcion = new JTextArea();
		taDescripcion.setEditable(false);
		taDescripcion.setLineWrap(true);
		scrollDesc.setViewportView(taDescripcion);

		JLabel lblCosto = new JLabel("Costo:");
		lblCosto.setBounds(26, 275, 62, 16);
		panel.add(lblCosto);

		tfCosto = new JTextField();
		tfCosto.setEditable(false);
		tfCosto.setColumns(10);
		tfCosto.setBounds(130, 269, 107, 28);
		panel.add(tfCosto);

		JLabel lblCupo = new JLabel("Cupo:");
		lblCupo.setBounds(26, 308, 62, 16);
		panel.add(lblCupo);

		tfCupo = new JTextField();
		tfCupo.setEditable(false);
		tfCupo.setColumns(10);
		tfCupo.setBounds(130, 302, 107, 28);
		panel.add(tfCupo);

		// Funcionamiento (action listeners)
		limpiarFormulario();

		cbEvento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (cbEvento.getSelectedIndex() < 0)
					return;
				evtSeleccionado = (String) cbEvento.getSelectedItem();
				Set<String> eds = contr.listarEdiciones(evtSeleccionado);
				cbEdicion.setModel(new DefaultComboBoxModel<String>(new Vector<String>(eds)));
				cbEdicion.setSelectedIndex(-1);
				cbEdicion.setEnabled(true);

				// vacía los campos de texto
				taDescripcion.setText("");
				tfCosto.setText("");
				tfCupo.setText("");

				// vacía el combobox tiporegistro
				cbTipoReg.setModel(new DefaultComboBoxModel<String>());
			}
		});

		cbEdicion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (cbEdicion.getSelectedIndex() < 0)
					return;
				edSeleccionada = (String) cbEdicion.getSelectedItem();
				Set<String> tiposReg = contr.listarTipoRegistro(evtSeleccionado, edSeleccionada);
				cbTipoReg.setModel(new DefaultComboBoxModel<String>(new Vector<String>(tiposReg)));
				cbTipoReg.setSelectedIndex(-1);
				cbTipoReg.setEnabled(true);

				// vacía los campos de texto
				taDescripcion.setText("");
				tfCosto.setText("");
				tfCupo.setText("");

			}
		});

		cbTipoReg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (cbTipoReg.getSelectedIndex() < 0)
					return;
				trSeleccionado = (String) cbTipoReg.getSelectedItem();
				DTTipoRegistro dtTipoReg = contr.getDTTipoReg(evtSeleccionado, edSeleccionada, trSeleccionado);
				taDescripcion.setText(dtTipoReg.getDescripcion());
				tfCosto.setText(Float.toString(dtTipoReg.getCosto()));
				tfCupo.setText(Integer.toString(dtTipoReg.getCupoDisponible()));
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

	// para mostrar datos FIJOS: es independiente de la ventana del CU
	// ConsultaTipoRegistro
	// precond: existe evento, edicion, tipoRegistro
	public ConsultaTipoRegistro(JPanel panel, String evento, String edicion, String tipoRegistro) {
		this(panel);
		datosFijos = true;
		setVisible(true);
		setTitle("Tipo de Registro: " + tipoRegistro);

		cbEvento.setEnabled(true);
		cbEdicion.setEnabled(true);
		cbTipoReg.setEnabled(true);
		cbEvento.setEditable(false);
		cbEdicion.setEditable(false);
		cbTipoReg.setEditable(false);

		String[] evts = { evento };
		cbEvento.setModel(new DefaultComboBoxModel<String>(evts));

		String[] eds = { edicion };
		cbEdicion.setModel(new DefaultComboBoxModel<String>(eds));

		String[] trs = { tipoRegistro };
		cbTipoReg.setModel(new DefaultComboBoxModel<String>(trs));

		taDescripcion.setText(contr.getDTTipoReg(evento, edicion, tipoRegistro).getDescripcion());
		tfCosto.setText(Float.toString(contr.getDTTipoReg(evento, edicion, tipoRegistro).getCosto()));
		tfCupo.setText(Integer.toString(contr.getDTTipoReg(evento, edicion, tipoRegistro).getCupoDisponible()));

	}

//	public void definirParametros(String evento, String edicion, String tipoRegistro) {
//		cbEvento.setSelectedItem(evento);
//		cbEdicion.setSelectedItem(edicion);
//		cbTipoReg.setSelectedItem(tipoRegistro);
//	}
}
