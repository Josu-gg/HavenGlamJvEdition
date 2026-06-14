package persistencia;
import dominio.Categoria;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CategoriaDAO
{
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public CategoriaDAO() {
        conn = ConnectionManager.getInstance();
    }
    public  Categoria Create(Categoria categoria) throws SQLException{
        Categoria result = null;
        try {
            ps = conn.connect().prepareStatement(
                    "Insert Into Categoria (NombreCategoria, IdEstado) " +
                            "Values (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, categoria.getNombreCategoria());
            ps.setInt(2, categoria.getIdEstado());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    result = getById(idGenerado);
                } else {
                    throw new SQLException("Error al crear la categoria");
                }
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al crear la categoria: "
                    + ex.getMessage(), ex);
        } finally {
            if (ps != null) ps.close();
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return result;
    }
}




