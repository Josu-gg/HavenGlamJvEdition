package persistencia;

import dominio.Servicio;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ServicioDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public ServicioDAO() {
        conn = ConnectionManager.getInstance();
    }

    public Servicio create(Servicio servicio) throws SQLException {
        Servicio result = null;
        try {
            ps = conn.connect().prepareStatement(
                    "Insert Into Servicio (NombreServicio, Descripcion, Precio, DuracionMinutos, IdCategoria, IdEstado) " +
                            "Values (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, servicio.getNombreServicio());
            ps.setString(2, servicio.getDescripcion());
            ps.setDouble(3, servicio.getPrecio());
            ps.setTime(4, servicio.getDuracionMinutos());
            ps.setInt(5, servicio.getIdCategoria());
            ps.setInt(6, servicio.getIdEstado());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    result = getById(idGenerado);
                } else {
                    throw new SQLException("Error al crear el servicio");
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el servicio: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) ps.close();
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return result;
    }

    public ArrayList<Servicio> getAll() throws SQLException {
        ArrayList<Servicio> servicios = new ArrayList<>();
        try {
            ps = conn.connect().prepareStatement(
                    "Select IdServicio, NombreServicio, Descripcion, Precio, DuracionMinutos, IdCategoria, IdEstado From Servicio"
            );
            rs = ps.executeQuery();

            while (rs.next()) {
                Servicio servicio = new Servicio();
                servicio.setIdServicio(rs.getInt(1));
                servicio.setNombreServicio(rs.getString(2));
                servicio.setDescripcion(rs.getString(3));
                servicio.setPrecio(rs.getDouble(4));
                servicio.setDuracionMinutos(rs.getTime(5));
                servicio.setIdCategoria(rs.getInt(6));
                servicio.setIdEstado(rs.getInt(7));
                servicios.add(servicio);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener los servicios: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return servicios;
    }

    public ArrayList<Servicio> search(String nombre) throws SQLException {
        ArrayList<Servicio> servicios = new ArrayList<>();
        try {
            ps = conn.connect().prepareStatement(
                    "Select IdServicio, NombreServicio, Descripcion, Precio, DuracionMinutos, IdCategoria, IdEstado " +
                            "From Servicio Where NombreServicio Like ?"
            );
            ps.setString(1, "%" + nombre + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Servicio servicio = new Servicio();
                servicio.setIdServicio(rs.getInt(1));
                servicio.setNombreServicio(rs.getString(2));
                servicio.setDescripcion(rs.getString(3));
                servicio.setPrecio(rs.getDouble(4));
                servicio.setDuracionMinutos(rs.getTime(5));
                servicio.setIdCategoria(rs.getInt(6));
                servicio.setIdEstado(rs.getInt(7));
                servicios.add(servicio);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar servicios: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return servicios;
    }
    public boolean update(Servicio servicio) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "Update Servicio " +
                            "Set NombreServicio = ?, Descripcion = ?, Precio = ?, " +
                            "DuracionMinutos = ?, IdCategoria = ?, IdEstado = ? " +
                            "Where IdServicio = ?"
            );
            ps.setString(1, servicio.getNombreServicio());
            ps.setString(2, servicio.getDescripcion());
            ps.setDouble(3, servicio.getPrecio());
            ps.setTime(4, servicio.getDuracionMinutos());
            ps.setInt(5, servicio.getIdCategoria());
            ps.setInt(6, servicio.getIdEstado());
            ps.setInt(7, servicio.getIdServicio());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al modificar servicio: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public boolean delete(Servicio servicio) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "Delete From Servicio Where IdServicio = ?"
            );
            ps.setInt(1, servicio.getIdServicio());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar un servicio: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }


    public Servicio getById(int pId) throws SQLException {
        Servicio servicio = new Servicio();
        try {
            ps = conn.connect().prepareStatement(
                    "Select IdServicio, NombreServicio, Descripcion, Precio, DuracionMinutos, IdCategoria, IdEstado " +
                            "From Servicio " +
                            "Where IdServicio = ?"
            );
            ps.setInt(1, pId);
            rs = ps.executeQuery();

            if (rs.next()) {
                servicio.setIdServicio(rs.getInt(1));
                servicio.setNombreServicio(rs.getString(2));
                servicio.setDescripcion(rs.getString(3));
                servicio.setPrecio(rs.getDouble(4));
                servicio.setDuracionMinutos(rs.getTime(5));
                servicio.setIdCategoria(rs.getInt(6));
                servicio.setIdEstado(rs.getInt(7));
            } else {
                servicio = null;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener un servicio por id: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return servicio;
    }
}