package presentacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logica.IControladorEvento;
import logica.IControladorUsuario;
import logica.DTEvento;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;


public class ConsultaEvento extends JInternalFrame {
	
	private static IControladorEvento ctrlEvento;
	private static IControladorUsuario ctrlUsuario;
	private ConsultaEdicionEvento consultaEdicionEvento;

	private static final long serialVersionUID = 1L;
	private JTextField textFieldNombre;
	private JTextField textFieldSigla;
	private JTextArea textAreaDescripcion;
	private JTextField textFieldFecha;
	private DTEvento evento;
	private JComboBox<String> levent;
	private JComboBox<String> cats;
	private JComboBox<String> eds;
	private String evtSeleccionado = "";
	private String edSelect = "";
	private static JPanel panel;
	private JLabel lblSinImagen;
	private JTextField tfCantVisitas;

	public ConsultaEvento(IControladorUsuario icu, IControladorEvento ice, JPanel parentPane) {
		panel = parentPane;
		
		ctrlEvento = ice;
		ctrlUsuario = icu;
		setTitle("Consultar Evento");
		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);
		setBounds(100, 100, 544, 471);
		getContentPane().setLayout(null);
		parentPane.add(this);
		
		JLabel lblEvento = new JLabel("Evento:");
		lblEvento.setBounds(12, 12, 70, 15);
		getContentPane().add(lblEvento);
		
		cats = new JComboBox();
		cats.setModel(new DefaultComboBoxModel(new String[] {"--Categorias--"}));
		cats.setBounds(115, 259, 212, 24);
		getContentPane().add(cats);
		
		eds= new JComboBox();
		eds.setModel(new DefaultComboBoxModel(new String[] {"--Ediciones--"}));
		eds.setBounds(115, 286, 212, 20);
		getContentPane().add(eds);
		
		
		levent = new JComboBox();
		levent.setModel(new DefaultComboBoxModel(new String[] {"--Eventos--"}));
		levent.setModel(new DefaultComboBoxModel<String>(new Vector<String>(ctrlEvento.listarEventos())));
		levent.setBounds(72, 15, 307, 24);
		getContentPane().add(levent);
		levent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				evtSeleccionado = (String) levent.getSelectedItem();
				Set<String> ctg = ctrlEvento.listarCategorias(evtSeleccionado);
				Set<String> edition = ctrlEvento.listarEdiciones(evtSeleccionado);
				cats.setModel(new DefaultComboBoxModel<String>(new Vector<String>(ctg)));
				eds.setModel(new DefaultComboBoxModel<String>(new Vector<String>(edition)));
				evento = ctrlEvento.getDTEvento(evtSeleccionado);
				textFieldNombre.setText(evento.getNombre());
				textFieldSigla.setText(evento.getSigla());
				textAreaDescripcion.setText(evento.getDescripcion());
				textFieldFecha.setText(evento.getFechaAlta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				tfCantVisitas.setText(Integer.toString(evento.getVisitas()));
				
				// Mostrar imagen 
				String rutaImg = evento.getImagen();
				if (rutaImg != null && !rutaImg.isEmpty()) {
				    ImageIcon icon = new ImageIcon(new ImageIcon(rutaImg)
				            .getImage()
				            .getScaledInstance(lblSinImagen.getWidth(), lblSinImagen.getHeight(), java.awt.Image.SCALE_SMOOTH));
				    lblSinImagen.setIcon(icon);
				    lblSinImagen.setText(""); // borra el "sin imagen"
				} else {
					lblSinImagen.setIcon(null);
					lblSinImagen.setText("sin imagen");
				}
			}
		});
		
		eds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				edSelect = (String) eds.getSelectedItem();
			}
		});
		
		
		
		JLabel lblNewLabel = new JLabel("Informacion del evento:");
		lblNewLabel.setBounds(12, 51, 188, 15);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(42, 78, 70, 15);
		getContentPane().add(lblNombre);
		
		JLabel lblSigla = new JLabel("     Sigla:");
		lblSigla.setBounds(42, 105, 70, 15);
		getContentPane().add(lblSigla);
		
		JLabel lblDesc = new JLabel(" Descripción:");
		lblDesc.setBounds(12, 132, 100, 15);
		getContentPane().add(lblDesc);
		
		JLabel lblFechaAlta = new JLabel("Fecha alta:");
		lblFechaAlta.setBounds(22, 192, 90, 15);
		getContentPane().add(lblFechaAlta);
		
		JLabel lblCategorias = new JLabel("   Categorias:");
		lblCategorias.setBounds(12, 264, 100, 15);
		getContentPane().add(lblCategorias);
		
		JLabel lblEdiciones = new JLabel("  Ediciones:");
		lblEdiciones.setBounds(27, 291, 85, 15);
		getContentPane().add(lblEdiciones);
		
		
		
		JComboBox cbEdiciones = new JComboBox();
		cbEdiciones.setModel(new DefaultComboBoxModel(new String[] {"--Ediciones--"}));
		cbEdiciones.setBounds(115, 286, 115, 20);
		getContentPane().add(cbEdiciones);
		
		JButton btnAceptar = new JButton("Ver Edición");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				 consultaEdicionEvento = new ConsultaEdicionEvento(parentPane, evtSeleccionado, edSelect, ctrlEvento, ctrlUsuario);
				 consultaEdicionEvento.setVisible(true);
			}
			 
		});
		btnAceptar.setBounds(42, 329, 117, 25);
		getContentPane().add(btnAceptar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
			}
		});
		btnCancelar.setBounds(262, 329, 117, 25);
		getContentPane().add(btnCancelar);
		
		textFieldNombre = new JTextField();
		textFieldNombre.setEditable(false);
		textFieldNombre.setBounds(105, 78, 274, 19);
		getContentPane().add(textFieldNombre);
		textFieldNombre.setColumns(10);
		
		textFieldSigla = new JTextField();
		textFieldSigla.setEditable(false);
		textFieldSigla.setBounds(105, 105, 114, 19);
		getContentPane().add(textFieldSigla);
		textFieldSigla.setColumns(10);
		
		textFieldFecha = new JTextField();
		textFieldFecha.setEditable(false);
		textFieldFecha.setBounds(105, 190, 114, 19);
		getContentPane().add(textFieldFecha);
		textFieldFecha.setColumns(10);
		
	    textAreaDescripcion = new JTextArea();
		textAreaDescripcion.setEditable(false);
		textAreaDescripcion.setBounds(105, 132, 374, 53);
		getContentPane().add(textAreaDescripcion);
		
	          
        textAreaDescripcion.setLineWrap(true);               
        textAreaDescripcion.setWrapStyleWord(true);          
        
        JScrollPane scrollPane = new JScrollPane(textAreaDescripcion);
        scrollPane.setBounds(105, 132, 374, 53);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        getContentPane().add(scrollPane);
        
        lblSinImagen = new JLabel("       sin imagen");
        lblSinImagen.setBounds(403, 21, 115, 99);
        getContentPane().add(lblSinImagen);
        
        JLabel lblCantvisitas = new JLabel("Cant. visitas:");
        lblCantvisitas.setBounds(22, 226, 90, 15);
        getContentPane().add(lblCantvisitas);
        
        tfCantVisitas = new JTextField();
        tfCantVisitas.setEditable(false);
        tfCantVisitas.setColumns(10);
        tfCantVisitas.setBounds(105, 228, 114, 19);
        getContentPane().add(tfCantVisitas);
                      
        
 

	}
}
