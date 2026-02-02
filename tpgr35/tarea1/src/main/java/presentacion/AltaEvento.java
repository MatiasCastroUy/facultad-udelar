package presentacion;


import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

import excepciones.CategoriaEventoVacia;
import excepciones.EventoRepetido;

import logica.IControladorEvento;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import com.toedter.calendar.JDateChooser;

public class AltaEvento extends JInternalFrame {
	
	private  IControladorEvento ctrlEvento;

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JTextField textField1;
	private JTextField textField2;
	private JList<String> listCategorias = new JList<>(new String[0]);
	private JDateChooser dcFechaAlta;
	private String[] cates2 = new String[0];
	private JTextField txtSinImagen;
	private String imagenselect = null;
	
	private void limpiarFormulario() {
		textField.setText("");
        textField1.setText("");
        textField2.setText("");
        listCategorias.clearSelection();
        Set<String> cates = ctrlEvento.getCategorias();
	    cates2 = cates.toArray(new String[0]);	
	    listCategorias.setListData(cates2);
	    imagenselect = null;
	    txtSinImagen.setText("sin imagen");
	}

	
	

	public AltaEvento(IControladorEvento ice, JPanel parentPanel) {
		
		
		ctrlEvento = ice;
		setIconifiable(true);
		setTitle("Alta de Evento");
		setResizable(true);
		setMaximizable(true);
		setClosable(true);
		setBounds(100, 100, 454, 377);
		parentPanel.add(this);
		getContentPane().setLayout(null);
		
		JLabel lblIngresarDatosDel = new JLabel("Ingresar datos del evento:");
		lblIngresarDatosDel.setBounds(12, 12, 199, 15);
		getContentPane().add(lblIngresarDatosDel);
		
		JLabel lblNewLabel = new JLabel("Nombre:");
		lblNewLabel.setBounds(88, 39, 47, 15);
		getContentPane().add(lblNewLabel);
		
		JLabel lblDescripcion = new JLabel("Descripción:");
		lblDescripcion.setBounds(71, 64, 56, 15);
		getContentPane().add(lblDescripcion);
		
		JLabel lblNewLabel_1 = new JLabel("Sigla:");
		lblNewLabel_1.setBounds(100, 111, 35, 15);
		getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Fecha de Alta:");
		lblNewLabel_2.setBounds(58, 147, 77, 15);
		getContentPane().add(lblNewLabel_2);
		
		JLabel lblCategorias = new JLabel("Categorías:");
		lblCategorias.setBounds(71, 172, 70, 15);
		getContentPane().add(lblCategorias);
		
		textField = new JTextField();
		textField.setBounds(145, 39, 114, 19);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		textField1 = new JTextField();
		textField1.setBounds(145, 64, 273, 35);
		getContentPane().add(textField1);
		textField1.setColumns(10);
		
		dcFechaAlta = new JDateChooser();
		dcFechaAlta.setDateFormatString("dd/MM/yyyy");
		dcFechaAlta.setBounds(145, 140, 114, 20);
		getContentPane().add(dcFechaAlta);
		
		textField2 = new JTextField();
		textField2.setBounds(145, 109, 114, 19);
		getContentPane().add(textField2);
		textField2.setColumns(10);
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aevent) {
				cmdAltaDeUsuario(aevent);
			}
		});
		btnAceptar.setBounds(59, 299, 117, 25);
		getContentPane().add(btnAceptar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				limpiarFormulario();
                setVisible(false);
			}
		});
		btnCancelar.setBounds(264, 299, 117, 25);
		getContentPane().add(btnCancelar);
        //ver carga de datos de categorias
		//String[] categorias = {"Deporte", "Musica", "Cultura", "Tecnologia","Magia"};
		

		listCategorias.setListData(cates2);
		listCategorias.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		
		JScrollPane scrollCategorias = new JScrollPane(listCategorias);
		scrollCategorias.setBounds(145, 170, 124, 73); 
		getContentPane().add(scrollCategorias);
		
		JLabel lblCtrlclickParaSeleccionar = new JLabel("ctrl+click seleccionar");
		lblCtrlclickParaSeleccionar.setBounds(273, 172, 161, 15);
		getContentPane().add(lblCtrlclickParaSeleccionar);
		
		JLabel lblVarios = new JLabel("varios");
		lblVarios.setBounds(273, 188, 70, 15);
		getContentPane().add(lblVarios);
		
		JLabel lblNewLabel_3 = new JLabel("Imagen del evento:");
		lblNewLabel_3.setBounds(29, 252, 89, 12);
		getContentPane().add(lblNewLabel_3);
		
		txtSinImagen = new JTextField();
		txtSinImagen.setEditable(false);
		txtSinImagen.setText("sin imagen");
		txtSinImagen.setBounds(145, 249, 124, 18);
		getContentPane().add(txtSinImagen);
		txtSinImagen.setColumns(10);
		
		JButton btnNewButton = new JButton("Seleccionar");
		btnNewButton.setBounds(281, 248, 100, 20);
		getContentPane().add(btnNewButton);
		
		btnNewButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent evt) {
		        JFileChooser fileChooser = new JFileChooser();
		        FileNameExtensionFilter filter = new FileNameExtensionFilter(
		            "Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif");
		        fileChooser.setFileFilter(filter);

		        int seleccion = fileChooser.showOpenDialog(null);
		        if (seleccion == JFileChooser.APPROVE_OPTION) {
		            File archivo = fileChooser.getSelectedFile();
		            imagenselect = archivo.getAbsolutePath();
		            txtSinImagen.setText(archivo.getName());		            
		        }
		    }
		});
		
		
		limpiarFormulario();
		
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


	protected void cmdAltaDeUsuario(ActionEvent aevent ) {
		String nombreE = this.textField.getText();
        String descE = this.textField1.getText();
        String siglaE = this.textField2.getText();
        

		ZoneId zoneId = ZoneId.systemDefault();
		Date fechaAlDate = this.dcFechaAlta.getDate();
		Instant fechaAltaInstant = fechaAlDate.toInstant();
		ZonedDateTime zonedFechaAlta = fechaAltaInstant.atZone(zoneId);
		LocalDate fechaa = zonedFechaAlta.toLocalDate();
		
        Set<String> cates = new HashSet<>(listCategorias.getSelectedValuesList());

        if (checkFormulario()) {
            try {
                ctrlEvento.altaEvento(nombreE, descE, fechaa, siglaE, cates, imagenselect); // TODO: Sustituir null por imagen
                
                // Muestro éxito de la operación
                JOptionPane.showMessageDialog(this, "El Evento se ha creado con éxito", "Alta de Evento ",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (EventoRepetido exc) {
                // Muestro error de Evento
                JOptionPane.showMessageDialog(this, exc.getMessage(), "Alta de Evento", JOptionPane.ERROR_MESSAGE);
            } catch (CategoriaEventoVacia exc) {
				// TODO Auto-generated catch block
            	JOptionPane.showMessageDialog(this, exc.getMessage(), "Alta de Evento", JOptionPane.ERROR_MESSAGE);
			}

            // Limpio el internal frame antes de cerrar la ventana
            limpiarFormulario();
            setVisible(false);
        }
		
		
	}
	
	

	private boolean checkFormulario() {
        String nombreE = this.textField.getText();
        String descE = this.textField1.getText();
        String siglaE = this.textField2.getText();

        if (nombreE.isEmpty() || descE.isEmpty() || siglaE.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No puede haber campos vacíos", "Alta Evento",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
       
    }
}
