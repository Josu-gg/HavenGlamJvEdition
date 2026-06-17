package presentacion;

import persistencia.UsuarioDAO;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Arrays;

public class LoginForm {

    private JPanel LoginForm;
    private JTextField txtCorreo;
    private JButton btnIngresar;
    private JButton btnSalir;
    private JPasswordField pswContra;

    private UsuarioDAO usuarioDAO;

    public static void main(String[] args) {
        JFrame frame = new JFrame("LoginForm");
        frame.setContentPane(new LoginForm().LoginForm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public LoginForm() {
        usuarioDAO = new UsuarioDAO();

        btnIngresar.addActionListener(e -> ingresar());
        btnSalir.addActionListener(e -> salir());
    }

    private void ingresar() {
        String correo = txtCorreo.getText().trim();
        char[] contraArray = pswContra.getPassword();
        String contra = new String(contraArray);

        if (correo.isEmpty() || contra.isEmpty()) {
            JOptionPane.showMessageDialog(LoginForm,
                    "Por favor completa la informacion en los campos requeridos",
                    "Campos vacios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean loginExitoso = usuarioDAO.validarLogin(correo, contra);

            if (loginExitoso) {
                JOptionPane.showMessageDialog(LoginForm,
                        "Bienvenido",
                        "Login exitoso", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(LoginForm,
                        "Correo o contraseña incorrectos",
                        "Error de autenticacion", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(LoginForm,
                    "Error de conexion con la base de datos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Arrays.fill(contraArray, ' ');
        }
    }

    private void salir() {
        System.exit(0);
    }

    public JPanel getPanelPrincipal() {
        return LoginForm;
    }
}

