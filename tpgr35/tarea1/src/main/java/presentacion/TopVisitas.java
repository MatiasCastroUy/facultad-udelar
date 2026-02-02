package presentacion;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;

import logica.IControladorEvento;
import logica.DTEvento;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class TopVisitas extends JInternalFrame {

    private static final long serialVersionUID = 1L;
    private IControladorEvento ctrlEvento;
    private JTable tablaEventos;
    private DefaultTableModel modeloTabla;

    public TopVisitas(IControladorEvento ctrlEvento, JPanel parentPanel) {
        this.ctrlEvento = ctrlEvento;

        setIconifiable(true);
        setMaximizable(true);
        setClosable(true);
        setResizable(true);
        setTitle("Top 5 eventos más visitados");
        setBounds(100, 100, 500, 300);
        parentPanel.add(this);
        getContentPane().setLayout(null);

        JLabel lblTitulo = new JLabel("Eventos más visitados:");
        lblTitulo.setBounds(20, 10, 300, 20);
        getContentPane().add(lblTitulo);

        String[] columnas = {"Posición", "Nombre del evento", "Visitas"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEventos = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaEventos);
        scroll.setBounds(20, 40, 450, 180);
        getContentPane().add(scroll);


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                cargarTopEventos();
            }
        });

        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                limpiarTabla();
            }
        });
    }

    private void cargarTopEventos() {
        limpiarTabla();
        List<DTEvento> topEventos = ctrlEvento.obtenerTopEventosVisitados();

        int pos = 1;
        for (DTEvento evt : topEventos) {
            modeloTabla.addRow(new Object[]{
                pos++,
                evt.getNombre(),
                evt.getVisitas()
            });
        }
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }
}
