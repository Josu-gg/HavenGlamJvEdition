package persistencia;

import dominio.Estado;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EstadoDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public EstadoDAO() {
        conn = ConnectionManager.getInstance();
    }

    public Estado create(Estado estado) throws SQLException {
        Estado result = null;
        try {
            ps = conn.connect().prepareStatement(
                    "Insert Into Estado (IdEstado, NombreEstado, TipoEstado) " +
                            "Values (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setInt(1, estado.getIdEstado());
            ps.setString(2, estado.getNombreEstado());
            ps.setString(3, estado.getTipoEstado());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    result = getById(idGenerado);
                } else {
                    throw new SQLException("Error al crear el estado");
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el estado: "
                    + ex.getMessage(), ex);
        } finally {
            if (ps != null) ps.close();
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return result;
    }

    public boolean update(Estado estado) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "Update Estado " +
                            "Set NombreEstado = ?, TipoEstado = ? " +
                            "Where IdEstado = ?"
            );
            ps.setString(1, estado.getNombreEstado());
            ps.setString(2, estado.getTipoEstado());
            ps.setInt(3, estado.getIdEstado());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al modificar estado: "
                    + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }
    public ArrayList<Estado> getAll() throws SQLException {
        ArrayList<Estado> estados = new ArrayList<>();
        try {
            ps = conn.connect().prepareStatement(
                    "Select IdEstado, NombreEstado, TipoEstado From Estado"
            );
            rs = ps.executeQuery();

            while (rs.next()) {
                Estado estado = new Estado();
                estado.setIdEstado(rs.getInt(1));
                estado.setNombreEstado(rs.getString(2));
                estado.setTipoEstado(rs.getString(3));
                estados.add(estado);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener los estados: "
                    + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return estados;
    }
    public boolean delete(Estado estado) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "Delete From Estado Where IdEstado = ?"
            );
            ps.setInt(1, estado.getIdEstado());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar un estado: "
                    + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public Estado getById(int pId) throws SQLException {
        Estado estado = new Estado();
        try {
            ps = conn.connect().prepareStatement(
                    "Select IdEstado, NombreEstado, TipoEstado " +
                            "From Estado " +
                            "Where IdEstado = ?"
            );
            ps.setInt(1, pId);
            rs = ps.executeQuery();

            if (rs.next()) {
                estado.setIdEstado(rs.getInt(1));
                estado.setNombreEstado(rs.getString(2));
                estado.setTipoEstado(rs.getString(3));
            } else {
                estado = null;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener un estado por id: "
                    + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return estado;
    }
}