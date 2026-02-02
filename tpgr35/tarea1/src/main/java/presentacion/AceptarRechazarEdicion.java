package presentacion;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import logica.Fabrica;
import logica.IControladorEvento;
import logica.IControladorUsuario;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class AceptarRechazarEdicion extends JInternalFrame {
	private JComboBox<String> cbEvento;
	private JComboBox<String> cbEdicion;
	private IControladorEvento contrEv;
	private String evtSeleccionado;
	private String edSeleccionada;
	private JPanel parentPanel;
	private JButton verBtn;

	public AceptarRechazarEdicion(JPanel parentPanel, IControladorEvento contrEv) {
		this.contrEv = contrEv;
		setVisible(false);
		setIconifiable(true);
		setMaximizable(true);
		setClosable(true);
		setTitle("Aceptar o Rechazar Edición de Evento");
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		setSize(400, 190);
		this.parentPanel = parentPanel;
		this.parentPanel.add(this);

		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(null);

		cbEvento = new JComboBox<String>();
		cbEvento.setBounds(121, 27, 192, 22);
		panel.add(cbEvento);

		cbEvento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (cbEvento.getSelectedIndex() < 0)
					return;
				evtSeleccionado = (String) cbEvento.getSelectedItem();
				Set<String> edsIngresadas = contrEv.listarEdicionesIngresadas(evtSeleccionado);
				cbEdicion.setModel(new DefaultComboBoxModel<String>(new Vector<String>(edsIngresadas)));
				cbEdicion.setSelectedIndex(-1);
				cbEdicion.setEnabled(true);
				verBtn.setEnabled(false);

			}
		});

		cbEdicion = new JComboBox<String>();
		cbEdicion.setBounds(121, 60, 192, 22);
		cbEdicion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (cbEdicion.getSelectedIndex() < 0)
					return;
				edSeleccionada = (String) cbEdicion.getSelectedItem();
				verBtn.setEnabled(true);
			}
		});
		panel.add(cbEdicion);

		JLabel edicionLabel = new JLabel("Edición:");
		edicionLabel.setBounds(34, 64, 115, 14);
		panel.add(edicionLabel);

		JLabel eventoLabel = new JLabel("Evento:");
		eventoLabel.setBounds(34, 31, 115, 14);
		panel.add(eventoLabel);

		JButton aceptarBtn = new JButton("Aceptar");
		aceptarBtn.setBounds(72, 116, 108, 23);
		aceptarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				contrEv.aceptarEdicionEvento(evtSeleccionado, edSeleccionada);
				limpiarFormulario();
			}
		});
		panel.add(aceptarBtn);

		JButton rechazarBtn = new JButton("Rechazar");
		rechazarBtn.setBounds(190, 116, 108, 23);
		rechazarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				contrEv.rechazarEdicionEvento(evtSeleccionado, edSeleccionada);
				limpiarFormulario();
			}
		});
		panel.add(rechazarBtn);

		verBtn = new JButton("Ver");
		verBtn.setBounds(325, 57, 60, 28);
		verBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				IControladorUsuario contrUs = Fabrica.getInstance().getIControladorUsuario();
				ConsultaEdicionEvento consultaEd = new ConsultaEdicionEvento(parentPanel, evtSeleccionado,
						edSeleccionada, contrEv, contrUs);
				consultaEd.setVisible(true);
			}
		});
		panel.add(verBtn);

		// para que recargue los eventos al abrirse
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent event) {
				limpiarFormulario();
			}
		});
	}

	private void limpiarFormulario() {
		cbEvento.setModel(new DefaultComboBoxModel<String>(new Vector<String>(contrEv.listarEventos())));
		cbEvento.setSelectedIndex(-1);
		cbEdicion.setModel(new DefaultComboBoxModel<>());
		cbEdicion.setEnabled(false);
		verBtn.setEnabled(false);
	}
}
