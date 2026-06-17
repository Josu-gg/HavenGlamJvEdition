package persistencia;
import dominio.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class UsuarioDAO
{

    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public UsuarioDAO() {
        conn = ConnectionManager.getInstance();
    }
    public Usuario create(Usuario usuario) throws SQLException {
        Usuario result = null;
        try {
            String hash = BCrypt.hashpw(usuario.getContra(), BCrypt.gensalt());

            ps = conn.connect().prepareStatement(
                    "Insert Into Usuario (Correo, Contra, IdEstado) " +
                            "Values (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, usuario.getCorreo());
            ps.setString(2, hash);
            ps.setInt(3, usuario.getIdEstado());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    result = getById(idGenerado);
                } else {
                    throw new SQLException("Error al crear el usuario");
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el usuario: "
                    + ex.getMessage(), ex);
        } finally {
            if (ps != null) ps.close();
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return result;
    }

    public Usuario getByCorreo(String correo) throws SQLException {
        Usuario usuario = new Usuario();
        try {
            ps = conn.connect().prepareStatement(
                    "Select IdUsuario, Correo, Contra, IdEstado " +
                            "From Usuario " +
                            "Where Correo = ?"
            );
            ps.setString(1, correo);
            rs = ps.executeQuery();

            if (rs.next()) {
                usuario.setIdUsuario(rs.getInt(1));
                usuario.setCorreo(rs.getString(2));
                usuario.setContra(rs.getString(3));
                usuario.setIdEstado(rs.getInt(4));
            } else {
                usuario = null;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener usuario por correo: "
                    + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return usuario;
    }

    public Usuario getById(int pId) throws SQLException {
        Usuario usuario = new Usuario();
        try {
            ps = conn.connect().prepareStatement(
                    "Select IdUsuario, Correo, Contra, IdEstado " +
                            "From Usuario " +
                            "Where IdUsuario = ?"
            );
            ps.setInt(1, pId);
            rs = ps.executeQuery();

            if (rs.next()) {
                usuario.setIdUsuario(rs.getInt(1));
                usuario.setCorreo(rs.getString(2));
                usuario.setContra(rs.getString(3));
                usuario.setIdEstado(rs.getInt(4));
            } else {
                usuario = null;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener usuario por id: "
                    + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return usuario;
    }

    public boolean validarLogin(String correo, String contraPlana) throws SQLException {
        Usuario usuario = getByCorreo(correo);

        if (usuario == null) {
            return false;
        }

        return BCrypt.checkpw(contraPlana, usuario.getContra());
    }
}
