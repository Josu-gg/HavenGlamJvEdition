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
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

        // Valor inicial del spinner: 00:30 (30 minutos)
        Calendar calInicial = Calendar.getInstance();
        calInicial.set(Calendar.HOUR_OF_DAY, 0);
        calInicial.set(Calendar.MINUTE, 30);
        calInicial.set(Calendar.SECOND, 0);
        calInicial.set(Calendar.MILLISECOND, 0);

        // El modelo cambia de a MINUTO cuando usas las flechas sobre esa parte
        SpinnerDateModel modeloDuracion = new SpinnerDateModel(
                calInicial.getTime(), null, null, Calendar.MINUTE
        );
        spDuracion.setModel(modeloDuracion);

        // El editor solo MUESTRA hora:minuto, sin segundos
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spDuracion, "HH:mm");
        spDuracion.setEditor(editor);

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

                // Time extiende de Date, así que se puede pasar directo al spinner
                Object valorDuracion = dtgservicio.getValueAt(fila, 4);
                if (valorDuracion instanceof Time) {
                    spDuracion.setValue(new Date(((Time) valorDuracion).getTime()));
                }

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

    // Toma el valor del spinner (Date) y arma un Time con segundos siempre en 00
    private Time obtenerDuracionDelSpinner() {
        Date valorSpinner = (Date) spDuracion.getValue();
        Calendar cal = Calendar.getInstance();
        cal.setTime(valorSpinner);
        int horas = cal.get(Calendar.HOUR_OF_DAY);
        int minutos = cal.get(Calendar.MINUTE);
        return Time.valueOf(String.format("%02d:%02d:00", horas, minutos));
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
            servicio.setDuracionMinutos(obtenerDuracionDelSpinner());

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
            servicio.setDuracionMinutos(obtenerDuracionDelSpinner());

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

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        spDuracion.setValue(cal.getTime());

        if (cmbcategoria.getItemCount() > 0) cmbcategoria.setSelectedIndex(0);
        if (cmbestado.getItemCount() > 0) cmbestado.setSelectedIndex(0);
        idServicioSeleccionado = -1;
        dtgservicio.clearSelection();
    }

    public JPanel getPanel() {
        return Servicio;
    }
}