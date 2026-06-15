package presentacion;
import dominio.Categoria;
import dominio.Estado;
import persistencia.CategoriaDAO;
import persistencia.EstadoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;

public class CategoriaForm {
    private JTextField txtNombre;
    private JComboBox cmbEstado;
    private JTable dtgCategoria;
    private JButton btnGuardar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JLabel lblNombreCategoria;
    private JLabel lblEstado;
    private JPanel JpanelCategoria;

    private CategoriaDAO categoriaDAO;
    private EstadoDAO estadoDAO;
    private int idCategoriaSeleccionada = -1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("CategoriaForm");
        frame.setContentPane(new CategoriaForm().JpanelCategoria);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public CategoriaForm() {
        categoriaDAO = new CategoriaDAO();
        estadoDAO = new EstadoDAO();

        cargarComboEstado();
        cargarTabla();

        btnGuardar.addActionListener(e -> guardar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());

        dtgCategoria.getSelectionModel().addListSelectionListener(e -> {
            int fila = dtgCategoria.getSelectedRow();
            if (fila >= 0) {
                idCategoriaSeleccionada = (int) dtgCategoria.getValueAt(fila, 0);
                txtNombre.setText((String) dtgCategoria.getValueAt(fila, 1));
                String nombreEstado = (String) dtgCategoria.getValueAt(fila, 2);
                seleccionarEstadoEnCombo(nombreEstado);
            }
        });
    }

    private void cargarComboEstado() {
        try {
            cmbEstado.removeAllItems();
            ArrayList<Estado> estados = estadoDAO.getAll();
            for (Estado estado : estados) {
                cmbEstado.addItem(estado);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(JpanelCategoria,
                    "Error al cargar los estados: " + ex.getMessage());
        }
    }

    private void cargarTabla() {
        try {
            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Id");
            modelo.addColumn("Nombre");
            modelo.addColumn("Estado");

            ArrayList<Categoria> categorias = categoriaDAO.getAll();
            for (Categoria categoria : categorias) {
                Estado estado = estadoDAO.getById(categoria.getIdEstado());
                String nombreEstado = (estado != null) ? estado.getNombreEstado() : "";

                modelo.addRow(new Object[]{
                        categoria.getIdCategoria(),
                        categoria.getNombreCategoria(),
                        nombreEstado
                });
            }
            dtgCategoria.setModel(modelo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(JpanelCategoria,
                    "Error al cargar las categorias: " + ex.getMessage());
        }
    }

    private void seleccionarEstadoEnCombo(String nombreEstado) {
        for (int i = 0; i < cmbEstado.getItemCount(); i++) {
            Estado estado = (Estado) cmbEstado.getItemAt(i);
            if (estado.getNombreEstado().equals(nombreEstado)) {
                cmbEstado.setSelectedIndex(i);
                break;
            }
        }
    }

    private void guardar() {
        try {
            if (txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(JpanelCategoria, "El nombre es obligatorio");
                return;
            }

            Categoria categoria = new Categoria();
            categoria.setNombreCategoria(txtNombre.getText().trim());

            Estado estadoSeleccionado = (Estado) cmbEstado.getSelectedItem();
            categoria.setIdEstado(estadoSeleccionado.getIdEstado());

            categoriaDAO.create(categoria);

            JOptionPane.showMessageDialog(JpanelCategoria, "Categoria guardada correctamente");
            limpiarCampos();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(JpanelCategoria, "Error al guardar: " + ex.getMessage());
        }
    }

    private void modificar() {
        try {
            if (idCategoriaSeleccionada == -1) {
                JOptionPane.showMessageDialog(JpanelCategoria, "Selecciona una categoria de la tabla");
                return;
            }
            if (txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(JpanelCategoria, "El nombre es obligatorio");
                return;
            }

            Categoria categoria = new Categoria();
            categoria.setIdCategoria(idCategoriaSeleccionada);
            categoria.setNombreCategoria(txtNombre.getText().trim());

            Estado estadoSeleccionado = (Estado) cmbEstado.getSelectedItem();
            categoria.setIdEstado(estadoSeleccionado.getIdEstado());

            boolean actualizado = categoriaDAO.update(categoria);
            if (actualizado) {
                JOptionPane.showMessageDialog(JpanelCategoria, "Categoria modificada correctamente");
            } else {
                JOptionPane.showMessageDialog(JpanelCategoria, "No se pudo modificar la categoria");
            }

            limpiarCampos();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(JpanelCategoria, "Error al modificar: " + ex.getMessage());
        }
    }

    private void eliminar() {
        try {
            if (idCategoriaSeleccionada == -1) {
                JOptionPane.showMessageDialog(JpanelCategoria, "Selecciona una categoria de la tabla");
                return;
            }

            Categoria categoria = new Categoria();
            categoria.setIdCategoria(idCategoriaSeleccionada);

            boolean eliminado = categoriaDAO.delete(categoria);
            if (eliminado) {
                JOptionPane.showMessageDialog(JpanelCategoria, "Categoria eliminada correctamente");
            } else {
                JOptionPane.showMessageDialog(JpanelCategoria, "No se pudo eliminar la categoria");
            }

            limpiarCampos();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(JpanelCategoria, "Error al eliminar: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        if (cmbEstado.getItemCount() > 0) {
            cmbEstado.setSelectedIndex(0);
        }
        idCategoriaSeleccionada = -1;
        dtgCategoria.clearSelection();
    }

    public JPanel getPanel1() {
        return JpanelCategoria;
    }
}
