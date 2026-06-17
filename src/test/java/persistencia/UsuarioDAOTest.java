package persistencia;
import dominio.Usuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioDAOTest {
        private static final int ID_ESTADO_ACTIVO = 1;

        UsuarioDAO usuarioDAO;
        ConnectionManager connectionManager;

        @BeforeEach
        void setUp() {
            usuarioDAO = new UsuarioDAO();
            connectionManager = ConnectionManager.getInstance();
        }

        @AfterEach
        void tearDown() throws SQLException {
            if (connectionManager != null) {
                connectionManager.disconnect();
                connectionManager = null;
            }
        }

        @Test
        void create() throws SQLException {
            Usuario usuario = new Usuario();
            usuario.setCorreo("prueba@correo.com");
            usuario.setContra("1234"); // se guarda como hash, no en texto plano
            usuario.setIdEstado(ID_ESTADO_ACTIVO);

            Usuario creado = usuarioDAO.create(usuario);

            assertNotNull(creado, "El usuario creado no debe ser nulo");
            assertTrue(creado.getIdUsuario() > 0, "El IdUsuario debe ser mayor a 0");
            assertEquals("prueba@correo.com", creado.getCorreo());
            assertNotEquals("1234", creado.getContra(), "La contraseña guardada no debe ser texto plano");
        }

        @Test
        void getByCorreo() throws SQLException {
            Usuario encontrado = usuarioDAO.getByCorreo("prueba@correo.com");

            assertNotNull(encontrado, "Debe encontrar al usuario por correo");
            assertEquals("prueba@correo.com", encontrado.getCorreo());
        }

        @Test
        void validarLogin_correcto() throws SQLException {
            boolean valido = usuarioDAO.validarLogin("prueba@correo.com", "1234");

            assertTrue(valido, "El login debe ser válido con la contraseña correcta");
        }

        @Test
        void validarLogin_incorrecto() throws SQLException {
            boolean valido = usuarioDAO.validarLogin("prueba@correo.com", "contraseñaMala");

            assertFalse(valido, "El login NO debe ser válido con una contraseña incorrecta");
        }
}
