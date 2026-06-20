package presentacion;

import dominio.Categoria;
import dominio.Estado;
import dominio.Producto;
import persistencia.CategoriaDAO;
import persistencia.EstadoDAO;
import persistencia.ProductoDAO;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;

public class ProductoForm {
    private JPanel Producto;
    private JTextField txtNombre;
    private JLabel lblNombreProducto;
    private JTextField txtDescripcion;
    private JLabel lblDescripcion;
    private JTextField txtPrecio;
    private JLabel lblPrecio;
    private JTextField txtStock;
    private JLabel lblStock;
    private JLabel lblCategoria;
    private JComboBox cmbCategoria;
    private JLabel lblEstado;
    private JComboBox cmbEstado;
    private JTable dtgProducto;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnGuardar;
    private JButton btnBuscar;
    private JTextField txtBuscar;

    private ProductoDAO productoDAO;
    private CategoriaDAO categoriaDAO;
    private EstadoDAO estadoDAO;
    private int idProductoSeleccionado = -1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("ProductoForm");
        frame.setContentPane(new ProductoForm().Producto);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public ProductoForm() {
        productoDAO = new ProductoDAO();
        categoriaDAO = new CategoriaDAO();
        estadoDAO = new EstadoDAO();

        cargarComboCategoria();
        cargarComboEstado();
        cargarTabla();

        btnGuardar.addActionListener(e -> guardar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnBuscar.addActionListener(e -> buscar());

        dtgProducto.getSelectionModel().addListSelectionListener(e -> {
            int fila = dtgProducto.getSelectedRow();
            if (fila >= 0) {
                idProductoSeleccionado = (int) dtgProducto.getValueAt(fila, 0);
                txtNombre.setText((String) dtgProducto.getValueAt(fila, 1));
                txtDescripcion.setText((String) dtgProducto.getValueAt(fila, 2));
                txtPrecio.setText(String.valueOf(dtgProducto.getValueAt(fila, 3)));
                txtStock.setText(String.valueOf(dtgProducto.getValueAt(fila, 4)));
                String nombreCategoria = (String) dtgProducto.getValueAt(fila, 5);
                String nombreEstado = (String) dtgProducto.getValueAt(fila, 6);
                seleccionarCategoriaEnCombo(nombreCategoria);
                seleccionarEstadoEnCombo(nombreEstado);
            }
        });
    }

    private void cargarComboCategoria() {
        try {
            cmbCategoria.removeAllItems();
            ArrayList<Categoria> categorias = categoriaDAO.getAll();
            for (Categoria categoria : categorias) {
                cmbCategoria.addItem(categoria);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Producto,
                    "Error al cargar las categorias: " + ex.getMessage());
        }
    }

    private void cargarComboEstado() {
        try {
            cmbEstado.removeAllItems();
            ArrayList<Estado> estados = estadoDAO.getAll();
            for (Estado estado : estados) {
                cmbEstado.addItem(estado);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Producto,
                    "Error al cargar los estados: " + ex.getMessage());
        }
    }

    private void cargarTabla() {
        try {
            ArrayList<Producto> productos = productoDAO.getAll();
            mostrarEnTabla(productos);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Producto,
                    "Error al cargar los productos: " + ex.getMessage());
        }
    }

    private void buscar() {
        try {
            String texto = txtBuscar.getText().trim();
            ArrayList<Producto> productos = texto.isEmpty()
                    ? productoDAO.getAll()
                    : productoDAO.search(texto);
            mostrarEnTabla(productos);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Producto, "Error al buscar: " + ex.getMessage());
        }
    }

    private void mostrarEnTabla(ArrayList<Producto> productos) throws SQLException {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Id");
        modelo.addColumn("Nombre");
        modelo.addColumn("Descripcion");
        modelo.addColumn("Precio");
        modelo.addColumn("Stock");
        modelo.addColumn("Categoria");
        modelo.addColumn("Estado");

        for (Producto producto : productos) {
            Categoria categoria = categoriaDAO.getById(producto.getIdCategoria());
            Estado estado = estadoDAO.getById(producto.getIdEstado());

            String nombreCategoria = (categoria != null) ? categoria.getNombreCategoria() : "";
            String nombreEstado = (estado != null) ? estado.getNombreEstado() : "";

            modelo.addRow(new Object[]{
                    producto.getIdProducto(),
                    producto.getNombreProducto(),
                    producto.getDescripcion(),
                    producto.getPrecio(),
                    producto.getStock(),
                    nombreCategoria,
                    nombreEstado
            });
        }
        dtgProducto.setModel(modelo);
    }

    private void seleccionarCategoriaEnCombo(String nombreCategoria) {
        for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
            Categoria categoria = (Categoria) cmbCategoria.getItemAt(i);
            if (categoria.getNombreCategoria().equals(nombreCategoria)) {
                cmbCategoria.setSelectedIndex(i);
                break;
            }
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
                JOptionPane.showMessageDialog(Producto, "El nombre es obligatorio");
                return;
            }
            if (txtPrecio.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(Producto, "El precio es obligatorio");
                return;
            }
            if (txtStock.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(Producto, "El stock es obligatorio");
                return;
            }

            Producto producto = new Producto();
            producto.setNombreProducto(txtNombre.getText().trim());
            producto.setDescripcion(txtDescripcion.getText().trim());
            producto.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
            producto.setStock(Integer.parseInt(txtStock.getText().trim()));

            Categoria categoriaSeleccionada = (Categoria) cmbCategoria.getSelectedItem();
            producto.setIdCategoria(categoriaSeleccionada.getIdCategoria());

            Estado estadoSeleccionado = (Estado) cmbEstado.getSelectedItem();
            producto.setIdEstado(estadoSeleccionado.getIdEstado());

            productoDAO.create(producto);

            JOptionPane.showMessageDialog(Producto, "Producto guardado correctamente");
            limpiarCampos();
            cargarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(Producto, "Precio y Stock deben ser valores numericos");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Producto, "Error al guardar: " + ex.getMessage());
        }
    }

    private void modificar() {
        try {
            if (idProductoSeleccionado == -1) {
                JOptionPane.showMessageDialog(Producto, "Selecciona un producto de la tabla");
                return;
            }
            if (txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(Producto, "El nombre es obligatorio");
                return;
            }
            if (txtPrecio.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(Producto, "El precio es obligatorio");
                return;
            }
            if (txtStock.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(Producto, "El stock es obligatorio");
                return;
            }

            Producto producto = new Producto();
            producto.setIdProducto(idProductoSeleccionado);
            producto.setNombreProducto(txtNombre.getText().trim());
            producto.setDescripcion(txtDescripcion.getText().trim());
            producto.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
            producto.setStock(Integer.parseInt(txtStock.getText().trim()));

            Categoria categoriaSeleccionada = (Categoria) cmbCategoria.getSelectedItem();
            producto.setIdCategoria(categoriaSeleccionada.getIdCategoria());

            Estado estadoSeleccionado = (Estado) cmbEstado.getSelectedItem();
            producto.setIdEstado(estadoSeleccionado.getIdEstado());

            boolean actualizado = productoDAO.update(producto);
            if (actualizado) {
                JOptionPane.showMessageDialog(Producto, "Producto modificado correctamente");
            } else {
                JOptionPane.showMessageDialog(Producto, "No se pudo modificar el producto");
            }

            limpiarCampos();
            cargarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(Producto, "Precio y Stock deben ser valores numericos");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Producto, "Error al modificar: " + ex.getMessage());
        }
    }

    private void eliminar() {
        try {
            if (idProductoSeleccionado == -1) {
                JOptionPane.showMessageDialog(Producto, "Selecciona un producto de la tabla");
                return;
            }

            Producto producto = new Producto();
            producto.setIdProducto(idProductoSeleccionado);

            boolean eliminado = productoDAO.delete(producto);
            if (eliminado) {
                JOptionPane.showMessageDialog(Producto, "Producto eliminado correctamente");
            } else {
                JOptionPane.showMessageDialog(Producto, "No se pudo eliminar el producto");
            }

            limpiarCampos();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(Producto, "Error al eliminar: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtBuscar.setText("");
        if (cmbCategoria.getItemCount() > 0) cmbCategoria.setSelectedIndex(0);
        if (cmbEstado.getItemCount() > 0) cmbEstado.setSelectedIndex(0);
        idProductoSeleccionado = -1;
        dtgProducto.clearSelection();
    }

    public JPanel getJpanelProducto() {
        return Producto;
    }
}