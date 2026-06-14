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
    public  boolean update(Categoria categoria) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "Update Categoria " +
                            "Set NombreCategoria = ?, IdCategoria = ?" +
                            "Where IdCategoria = ? ");
            ps.setString(1, categoria.getNombreCategoria());
            ps.setInt(2, categoria.getIdEstado());
            ps.setInt(3, categoria.getIdCategoria());
            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        }
        catch (SQLException ex){
            throw new SQLException("Error al modificar categoria: "
            + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;

    }
    public boolean delete(Categoria categoria)throws  SQLException{
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "Delete From Categoria Where IdCategoria = ?"
            );
            ps.setInt(1, categoria.getIdCategoria());
            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        }catch (SQLException ex){
            throw  new SQLException("Error al eliminar una categoria: "+
                    ex.getMessage(), ex);

        }finally {
            ps = null;
            conn.disconnect();
        }

        return  res;
    }
    public Categoria getById(int pId) throws  SQLException {
        Categoria categoria = new Categoria();
        try {
            ps = conn.connect().prepareStatement(
                    "Select IdCategoria, NombreCategoria, IdEstado " +
                            "From Categoria " +
                            "Where IdCategoria = ?"
            );
            ps.setInt(1, pId);
            rs = ps.executeQuery();

            if(rs.next()){
                categoria.setIdCategoria(rs.getInt(1));
                categoria.setNombreCategoria(rs.getString(2));
                categoria.setIdEstado(rs.getInt(3));
            }
            else {
                categoria = null;
            }
            ps.close();
            rs.close();
        }
        catch (SQLException ex){
            throw  new SQLException("Error al obtener una categoria por id: "
            + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return categoria;
    }
}




