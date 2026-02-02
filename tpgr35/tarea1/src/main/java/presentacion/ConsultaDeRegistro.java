package presentacion;

import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Set;

import logica.ControladorUsuario;
import logica.DTAsistente;
import logica.DTRegistro;
import excepciones.UsuarioNoExisteException;

public class ConsultaDeRegistro extends JInternalFrame {

    private static final long serialVersionUID = 1L;
    private JTextField textFieldEvento;
    private JTextField textFieldEdicion;
    private JTextField textFieldFecha;
    private JTextField textFieldTipo;
    private JTextField textFieldCosto;

    private String nicknameSeleccionado = null;
    private final ControladorUsuario ctrlUsuario = new ControladorUsuario();

    public ConsultaDeRegistro(JPanel parentPanel) {
        super("Consulta de Registro", true, true, true, true);
        parentPanel.add(this);
        setBounds(100, 100, 400, 417);
        getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 384, 406);
        getContentPane().add(panel);

        JLabel lblUsuarios = new JLabel("Usuario:");
        lblUsuarios.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblUsuarios.setBounds(15, 20, 80, 20);
        panel.add(lblUsuarios);

        JComboBox<String> cbUsuario = new JComboBox<>();
        cbUsuario.setBounds(120, 20, 200, 25);
        panel.add(cbUsuario);

        JLabel lblRegistros = new JLabel("Registro:");
        lblRegistros.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblRegistros.setBounds(15, 60, 80, 20);
        panel.add(lblRegistros);

        JComboBox<String> cbRegistros = new JComboBox<>();
        cbRegistros.setBounds(120, 60, 200, 25);
        panel.add(cbRegistros);

        JLabel lblDatos = new JLabel("Datos de registros:");
        lblDatos.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblDatos.setBounds(20, 110, 150, 20);
        panel.add(lblDatos);

        JLabel lblEvento = new JLabel("Evento:");
        lblEvento.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblEvento.setBounds(15, 140, 80, 25);
        panel.add(lblEvento);

        JLabel lblEdicion = new JLabel("Edición:");
        lblEdicion.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblEdicion.setBounds(15, 180, 80, 25);
        panel.add(lblEdicion);

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblFecha.setBounds(15, 220, 80, 25);
        panel.add(lblFecha);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblTipo.setBounds(15, 260, 80, 25);
        panel.add(lblTipo);

        JLabel lblCosto = new JLabel("Costo:");
        lblCosto.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblCosto.setBounds(15, 300, 80, 25);
        panel.add(lblCosto);

        textFieldEvento = new JTextField();
        textFieldEvento.setEditable(false);
        textFieldEvento.setBounds(80, 140, 271, 30);
        panel.add(textFieldEvento);

        textFieldEdicion = new JTextField();
        textFieldEdicion.setEditable(false);
        textFieldEdicion.setBounds(80, 180, 271, 30);
        panel.add(textFieldEdicion);

        textFieldFecha = new JTextField();
        textFieldFecha.setEditable(false);
        textFieldFecha.setBounds(80, 220, 271, 30);
        panel.add(textFieldFecha);

        textFieldTipo = new JTextField();
        textFieldTipo.setEditable(false);
        textFieldTipo.setBounds(80, 260, 271, 30);
        panel.add(textFieldTipo);

        textFieldCosto = new JTextField();
        textFieldCosto.setEditable(false);
        textFieldCosto.setBounds(80, 300, 271, 30);
        panel.add(textFieldCosto);

        try {
            cbUsuario.removeAllItems();
            Set<DTAsistente> asistentes = ctrlUsuario.listarAsistentes();
            for (DTAsistente a : asistentes) {
                cbUsuario.addItem(a.getNickname());
            }
        } catch (UsuarioNoExisteException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        cbUsuario.addActionListener(e -> {
            cbRegistros.removeAllItems();
            String nick = (String) cbUsuario.getSelectedItem();
            if (nick != null) {
                try {
                    for (String reg : ctrlUsuario.listarNombresRegistrosDeUsuario(nick)) {
                        cbRegistros.addItem(reg);
                    }
                } catch (UsuarioNoExisteException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cbRegistros.addActionListener(e -> {
            String nick = (String) cbUsuario.getSelectedItem();
            String regNombre = (String) cbRegistros.getSelectedItem();
            if (nick != null && regNombre != null) {
                try {
                    String nombreEdicion = regNombre.split("\"")[1];
                    DTRegistro reg = ctrlUsuario.obtenerRegistroDeUsuario(nick, nombreEdicion);
                    if (reg != null) {
                        textFieldEvento.setText(reg.getEvento());
                        textFieldEdicion.setText(reg.getEdicion());
                        textFieldFecha.setText(reg.getFecha().toString());
                        textFieldTipo.setText(reg.getTipo());
                        textFieldCosto.setText(Float.toString(reg.getCosto()));
                    }
                } catch (UsuarioNoExisteException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent event) {
                try {
                    cbUsuario.removeAllItems();
                    Set<DTAsistente> asistentes = ctrlUsuario.listarAsistentes();
                    for (DTAsistente a : asistentes) {
                        cbUsuario.addItem(a.getNickname());
                    }
                    if (nicknameSeleccionado != null) {
                        cbUsuario.setSelectedItem(nicknameSeleccionado);
                    }
                } catch (UsuarioNoExisteException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    public ConsultaDeRegistro(JPanel parentPanel, String nickname) {
        this(parentPanel);
        this.nicknameSeleccionado = nickname;
        JPanel panel = (JPanel) getContentPane().getComponent(0);
        for (java.awt.Component comp : panel.getComponents()) {
            if (comp instanceof JComboBox) {
                JComboBox<String> cbUsuario = (JComboBox<String>) comp;
                cbUsuario.setSelectedItem(nickname);
                break;
            }
        }
    }
}
