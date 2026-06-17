package presentacion;

import dominio.Categoria;
import dominio.Estado;
import dominio.Servicio;
import persistencia.CategoriaDAO;
import persistencia.EstadoDAO;
import persistencia.ServicioDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServicioForm {
    private JTextField txtNombreServicio;
    private JTextField txtDescripcion;
    private JTextField txtPrecio;
    private JSpinner spDuracion;
    private JComboBox cmbcategoria;
    private JComboBox cmbestado;
    private JTable dtgservicio;
    private JButton btnguardar;
    private JButton btnmodificar;
    private JButton btneliminar;
    private JButton btnlimpiar;
    private JPanel Servicio;


    private ServicioDAO servicioDAO;
    private CategoriaDAO categoriaDAO;
    private EstadoDAO estadoDAO;
    private int idServicioSeleccionado = -1;


    public static void main(String[] args) {
        JFrame frame = new JFrame("ServicioForm");
        frame.setContentPane(new ServicioForm().Servicio);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public ServicioForm() {
        servicioDAO = new ServicioDAO();
        categoriaDAO = new CategoriaDAO();
        estadoDAO = new EstadoDAO();

        SpinnerNumberModel modeloDuracion = new SpinnerNumberModel(30, 1, 480, 5);
        spDuracion.setModel(modeloDuracion);

        cargarComboCategoria();
        cargarComboEstado();
        cargarTabla();

        btnguardar.addActionListener(e -> guardar());
        btnmodificar.addActionListener(e -> modificar());
        btneliminar.addActionListener(e -> eliminar());
        btnlimpiar.addActionListener(e -> limpiarCampos());

        dtgservicio.getSelectionModel().addListSelectionListener(e -> {
            int fila = dtgservicio.getSelectedRow();
            if (fila >= 0) {
                idServicioSeleccionado = (int) dtgservicio.getValueAt(fila, 0);
                txtNombreServicio.setText((String) dtgservicio.getValueAt(fila, 1));
                txtDescripcion.setText((String) dtgservicio.getValueAt(fila, 2));
                txtPrecio.setText(String.valueOf(dtgservicio.getValueAt(fila, 3)));
                spDuracion.setValue(dtgservicio.getValueAt(fila, 4));
                String nombreCategoria = (String) dtgservicio.getValueAt(fila, 5);
                String nombreEstado = (String) dtgservicio.getValueAt(fila, 6);
                seleccionarCategoriaEnCombo(nombreCategoria);
                seleccionarEstadoEnCombo(nombreEstado);
            }
        });
    }

    private void cargarComboCategoria() {
        try {
            cmbcategoria.removeAllItems();
            ArrayList<Categoria> categorias = categoriaDAO.getAll();
            for (Categoria categoria : categorias) {
                cmbcategoria.addItem(categoria);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Servicio,
                    "Error al cargar las categorias: " + ex.getMessage());
        }
    }

    private void cargarComboEstado() {
        try {
            cmbestado.removeAllItems();
            ArrayList<Estado> estados = estadoDAO.getAll();
            for (Estado estado : estados) {
                cmbestado.addItem(estado);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Servicio,
                    "Error al cargar los estados: " + ex.getMessage());
        }
    }

    private void cargarTabla() {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Id");
            modelo.addColumn("Nombre");
            modelo.addColumn("Descripcion");
            modelo.addColumn("Precio");
            modelo.addColumn("Duracion");
            modelo.addColumn("Categoria");
            modelo.addColumn("Estado");

            ArrayList<Servicio> servicios = servicioDAO.getAll();
            for (Servicio servicio : servicios) {
                Categoria categoria = categoriaDAO.getById(servicio.getIdCategoria());
                Estado estado = estadoDAO.getById(servicio.getIdEstado());
                String nombreCategoria = (categoria != null) ? categoria.getNombreCategoria() : "";
                String nombreEstado = (estado != null) ? estado.getNombreEstado() : "";

                modelo.addRow(new Object[]{
                        servicio.getIdServicio(),
                        servicio.getNombreServicio(),
                        servicio.getDescripcion(),
                        servicio.getPrecio(),
                        servicio.getDuracionMinutos(),
                        nombreCategoria,
                        nombreEstado
                });
            }
            dtgservicio.setModel(modelo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Servicio,
                    "Error al cargar los servicios: " + ex.getMessage());
        }
    }

    private void seleccionarCategoriaEnCombo(String nombreCategoria) {
        for (int i = 0; i < cmbcategoria.getItemCount(); i++) {
            Categoria categoria = (Categoria) cmbcategoria.getItemAt(i);
            if (categoria.getNombreCategoria().equals(nombreCategoria)) {
                cmbcategoria.setSelectedIndex(i);
                break;
            }
        }
    }

    private void seleccionarEstadoEnCombo(String nombreEstado) {
        for (int i = 0; i < cmbestado.getItemCount(); i++) {
            Estado estado = (Estado) cmbestado.getItemAt(i);
            if (estado.getNombreEstado().equals(nombreEstado)) {
                cmbestado.setSelectedIndex(i);
                break;
            }
        }
    }

    private void guardar() {
        try {
            if (txtNombreServicio.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(Servicio, "El nombre es obligatorio");
                return;
            }

            Servicio servicio = new Servicio();
            servicio.setNombreServicio(txtNombreServicio.getText().trim());
            servicio.setDescripcion(txtDescripcion.getText().trim());
            servicio.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
            servicio.setDuracionMinutos((Integer) spDuracion.getValue());

            Categoria categoriaSeleccionada = (Categoria) cmbcategoria.getSelectedItem();
            servicio.setIdCategoria(categoriaSeleccionada.getIdCategoria());

            Estado estadoSeleccionado = (Estado) cmbestado.getSelectedItem();
            servicio.setIdEstado(estadoSeleccionado.getIdEstado());

            servicioDAO.create(servicio);
            JOptionPane.showMessageDialog(Servicio, "Servicio guardado correctamente");
            limpiarCampos();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Servicio, "Error al guardar: " + ex.getMessage());
        }
    }

    private void modificar() {
        try {
            if (idServicioSeleccionado == -1) {
                JOptionPane.showMessageDialog(Servicio, "Selecciona un servicio de la tabla");
                return;
            }
            if (txtNombreServicio.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(Servicio, "El nombre es obligatorio");
                return;
            }

            Servicio servicio = new Servicio();
            servicio.setIdServicio(idServicioSeleccionado);
            servicio.setNombreServicio(txtNombreServicio.getText().trim());
            servicio.setDescripcion(txtDescripcion.getText().trim());
            servicio.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
            servicio.setDuracionMinutos((Integer) spDuracion.getValue());

            Categoria categoriaSeleccionada = (Categoria) cmbcategoria.getSelectedItem();
            servicio.setIdCategoria(categoriaSeleccionada.getIdCategoria());

            Estado estadoSeleccionado = (Estado) cmbestado.getSelectedItem();
            servicio.setIdEstado(estadoSeleccionado.getIdEstado());

            boolean actualizado = servicioDAO.update(servicio);
            if (actualizado) {
                JOptionPane.showMessageDialog(Servicio, "Servicio modificado correctamente");
            } else {
                JOptionPane.showMessageDialog(Servicio, "No se pudo modificar el servicio");
            }
            limpiarCampos();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Servicio, "Error al modificar: " + ex.getMessage());
        }
    }

    private void eliminar() {
        try {
            if (idServicioSeleccionado == -1) {
                JOptionPane.showMessageDialog(Servicio, "Selecciona un servicio de la tabla");
                return;
            }

            Servicio servicio = new Servicio();
            servicio.setIdServicio(idServicioSeleccionado);

            boolean eliminado = servicioDAO.delete(servicio);
            if (eliminado) {
                JOptionPane.showMessageDialog(Servicio, "Servicio eliminado correctamente");
            } else {
                JOptionPane.showMessageDialog(Servicio, "No se pudo eliminar el servicio");
            }
            limpiarCampos();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Servicio, "Error al eliminar: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNombreServicio.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        spDuracion.setValue(30);
        if (cmbcategoria.getItemCount() > 0) cmbcategoria.setSelectedIndex(0);
        if (cmbestado.getItemCount() > 0) cmbestado.setSelectedIndex(0);
        idServicioSeleccionado = -1;
        dtgservicio.clearSelection();
    }

    public JPanel getPanel() {
        return Servicio;
    }
}