package presentacion;

import excepciones.CupoLlenoExcepcion;
import excepciones.UsuarioNoExisteException;
import excepciones.YaRegistradoExcepcion;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.toedter.calendar.JDateChooser;
import logica.DTAsistente;
import logica.Fabrica;
import logica.IControladorEvento;
import logica.IControladorUsuario;

public class RegistroAEdicion extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private JComboBox<String> comboEvento;
    private JComboBox<String> comboEdicion;
    private JComboBox<String> comboTipoRegistro;
    private JComboBox<String> comboAsistentes;

    private JButton botonAceptar;
    private JButton botonCancelar;

    private JLabel etiquetaFechaRegistro;
    private JDateChooser selectorFechaRegistro;

    private final IControladorEvento controladorEvento;
    private final IControladorUsuario controladorUsuario;

    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("dd MM yyyy");

    public RegistroAEdicion(JPanel parentPanel) {
        super();
        parentPanel.add(this);

        this.controladorEvento = Fabrica.getInstance().getIControladorEvento();
        this.controladorUsuario = Fabrica.getInstance().getIControladorUsuario();

        setTitle("Registro a Edición de Evento");
        setClosable(true);
        setResizable(true);
        setMaximizable(true);
        setIconifiable(true);
        setBounds(20, 20, 436, 300);
        getContentPane().setLayout(null);

        JLabel etiquetaEvento = new JLabel("Evento:");
        etiquetaEvento.setFont(new Font("Tahoma", Font.PLAIN, 15));
        etiquetaEvento.setBounds(49, 33, 62, 16);
        getContentPane().add(etiquetaEvento);

        comboEvento = new JComboBox<>();
        comboEvento.setBounds(171, 30, 245, 26);
        getContentPane().add(comboEvento);

        JLabel etiquetaEdicion = new JLabel("Edición:");
        etiquetaEdicion.setFont(new Font("Tahoma", Font.PLAIN, 15));
        etiquetaEdicion.setBounds(49, 70, 62, 16);
        getContentPane().add(etiquetaEdicion);

        comboEdicion = new JComboBox<>();
        comboEdicion.setBounds(171, 67, 245, 26);
        getContentPane().add(comboEdicion);

        JLabel etiquetaTipoRegistro = new JLabel("Tipo de registros:");
        etiquetaTipoRegistro.setFont(new Font("Tahoma", Font.PLAIN, 15));
        etiquetaTipoRegistro.setBounds(49, 103, 122, 25);
        getContentPane().add(etiquetaTipoRegistro);

        comboTipoRegistro = new JComboBox<>();
        comboTipoRegistro.setBounds(171, 104, 245, 26);
        getContentPane().add(comboTipoRegistro);

        JLabel etiquetaAsistentes = new JLabel("Asistentes:");
        etiquetaAsistentes.setFont(new Font("Tahoma", Font.PLAIN, 15));
        etiquetaAsistentes.setBounds(49, 144, 77, 16);
        getContentPane().add(etiquetaAsistentes);

        comboAsistentes = new JComboBox<>();
        comboAsistentes.setBounds(171, 141, 245, 26);
        getContentPane().add(comboAsistentes);

        etiquetaFechaRegistro = new JLabel("Fecha de registro:");
        etiquetaFechaRegistro.setFont(new Font("Tahoma", Font.PLAIN, 15));
        etiquetaFechaRegistro.setBounds(49, 182, 122, 16);
        getContentPane().add(etiquetaFechaRegistro);

        selectorFechaRegistro = new JDateChooser();
        selectorFechaRegistro.setBounds(171, 179, 245, 26);
        selectorFechaRegistro.setDateFormatString("dd MM yyyy");
        selectorFechaRegistro.setDate(null);
        getContentPane().add(selectorFechaRegistro);

        botonAceptar = new JButton("Aceptar");
        botonAceptar.setBounds(61, 220, 98, 26);
        botonAceptar.setEnabled(false);
        getContentPane().add(botonAceptar);

        botonCancelar = new JButton("Cancelar");
        botonCancelar.setBounds(281, 220, 98, 26);
        getContentPane().add(botonCancelar);

        comboEvento.removeAllItems();
        for (String nombreEvento : controladorEvento.listarEventos()) {
            comboEvento.addItem(nombreEvento);
        }

        comboEvento.addActionListener(e -> {
            comboEdicion.removeAllItems();
            comboTipoRegistro.removeAllItems();
            comboAsistentes.removeAllItems();
            botonAceptar.setEnabled(false);

            String eventoSeleccionado = (String) comboEvento.getSelectedItem();
            if (eventoSeleccionado != null) {
                for (String nombreEdicion : controladorEvento.listarEdiciones(eventoSeleccionado)) {
                    comboEdicion.addItem(nombreEdicion);
                }
            }
        });

        comboEdicion.addActionListener(e -> {
            comboTipoRegistro.removeAllItems();
            comboAsistentes.removeAllItems();
            botonAceptar.setEnabled(false);

            String eventoSeleccionado = (String) comboEvento.getSelectedItem();
            String edicionSeleccionada = (String) comboEdicion.getSelectedItem();

            if (eventoSeleccionado != null && edicionSeleccionada != null) {
                for (String nombreTipoRegistro
                        : controladorEvento.listarTipoRegistro(
                                eventoSeleccionado, edicionSeleccionada)) {
                    comboTipoRegistro.addItem(nombreTipoRegistro);
                }

                try {
                    for (DTAsistente asistente : controladorUsuario.listarAsistentes()) {
                        comboAsistentes.addItem(asistente.getNickname());
                    }
                } catch (UsuarioNoExisteException ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "No existen asistentes registrados.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        Runnable habilitarAceptar = new Runnable() {
            @Override
            public void run() {
                boolean todoSeleccionado =
                        comboEvento.getSelectedItem() != null
                        && comboEdicion.getSelectedItem() != null
                        && comboTipoRegistro.getSelectedItem() != null
                        && comboAsistentes.getSelectedItem() != null
                        && selectorFechaRegistro.getDate() != null;

                botonAceptar.setEnabled(todoSeleccionado);
            }
        };

        comboTipoRegistro.addActionListener(e -> habilitarAceptar.run());
        comboAsistentes.addActionListener(e -> habilitarAceptar.run());
        selectorFechaRegistro.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                habilitarAceptar.run();
            }
        });

        botonAceptar.addActionListener(e -> {
            String eventoSeleccionado = (String) comboEvento.getSelectedItem();
            String edicionSeleccionada = (String) comboEdicion.getSelectedItem();
            String tipoSeleccionado = (String) comboTipoRegistro.getSelectedItem();
            String nicknameSeleccionado = (String) comboAsistentes.getSelectedItem();
            Date fechaElegida = selectorFechaRegistro.getDate();

            if (eventoSeleccionado == null
                    || edicionSeleccionada == null
                    || tipoSeleccionado == null
                    || nicknameSeleccionado == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Completá todas las selecciones.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (fechaElegida == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Seleccioná la fecha de registro.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            LocalDate fechaRegistro;
            try {
                fechaRegistro = fechaElegida.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "La fecha de registro no es válida. Formato esperado: "
                                + formatoFecha.toString(),
                        "Error de fecha",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {
                controladorEvento.altaRegistro(
                        eventoSeleccionado,
                        edicionSeleccionada,
                        tipoSeleccionado,
                        nicknameSeleccionado,
                        fechaRegistro
                );
                JOptionPane.showMessageDialog(
                        this,
                        "Registro exitoso."
                );
                dispose();
            } catch (CupoLlenoExcepcion exCupo) {
                JOptionPane.showMessageDialog(
                        this,
                        exCupo.getMessage(),
                        "Error de cupo",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (YaRegistradoExcepcion exDuplicado) {
                JOptionPane.showMessageDialog(
                        this,
                        exDuplicado.getMessage(),
                        "Error de duplicado",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        botonCancelar.addActionListener(e -> dispose());

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent event) {
                comboEvento.removeAllItems();
                for (String nombreEvento : controladorEvento.listarEventos()) {
                    comboEvento.addItem(nombreEvento);
                }
                selectorFechaRegistro.setDate(null);
                botonAceptar.setEnabled(false);
            }
        });
    }
}

