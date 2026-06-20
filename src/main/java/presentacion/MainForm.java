package presentacion;

import javax.swing.*;
import java.awt.event.MouseAdapter;

public class MainForm extends JFrame {
    private JPanel frmPrincipal;

    public MainForm() {
        setTitle("JvHavenGlam");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setContentPane(frmPrincipal);
        createMenu();
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Menú Producto
        JMenu menuProducto = new JMenu("Producto");
        menuBar.add(menuProducto);
        menuProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                abrirProducto();
            }
        });

        //Menu Categoria
        JMenu menuCategoria = new JMenu("Categoria");
        menuBar.add(menuCategoria);
        menuCategoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                abrirCategoria();
            }
        });

        // Menú Servicio
        JMenu menuServicio = new JMenu("Servicio");
        menuBar.add(menuServicio);
        menuServicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                abrirServicio();
            }
        });

    }


    private void abrirProducto() {
        JFrame frame = new JFrame("Producto");
        frame.setContentPane(new ProductoForm().getJpanelProducto());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ///frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private void abrirServicio() {
        JFrame frame = new JFrame("Servicio");
        frame.setContentPane(new ServicioForm().getPanel());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       // frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
    private void abrirCategoria() {
        JFrame frame = new JFrame("Categoria");
        frame.setContentPane(new CategoriaForm().getPanel1());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        MainForm mainForm = new MainForm();
        mainForm.setVisible(true);
    }
}
