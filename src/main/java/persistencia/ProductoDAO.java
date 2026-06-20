package persistencia;

import dominio.Producto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProductoDAO {

    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public ProductoDAO() {
        conn = ConnectionManager.getInstance();
    }

    public Producto create(Producto producto) throws SQLException {
        Producto result = null;
        try {
            ps = conn.connect().prepareStatement(
                    "Insert Into Producto (NombreProducto, Descripcion, Precio, Stock, IdCategoria, IdEstado) " +
                            "Values (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, producto.getNombreProducto());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setInt(5, producto.getIdCategoria());
            ps.setInt(6, producto.getIdEstado());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    result = getById(idGenerado);
                } else {
                    throw new SQLException("Error al crear el producto");
                }
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al crear el producto: "
                    + ex.getMessage(), ex);
        } finally {
            if (ps != null) ps.close();
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return result;
    }

    public boolean update(Producto producto) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "Update Producto " +
                            "Set NombreProducto = ?, Descripcion = ?, Precio = ?, " +
                            "Stock = ?, IdCategoria = ?, IdEstado = ? " +
                            "Where IdProducto = ?"
            );

            ps.setString(1, producto.getNombreProducto());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setInt(5, producto.getIdCategoria());
            ps.setInt(6, producto.getIdEstado());
            ps.setInt(7, producto.getIdProducto());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el producto: "
                    + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public boolean delete(Producto producto) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "Delete From Producto Where IdProducto = ?"
            );

            ps.setInt(1, producto.getIdProducto());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el producto: "
                    + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public ArrayList<Producto> search(String nombre) throws SQLException {
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            ps = conn.connect().prepareStatement(
                    "Select IdProducto, NombreProducto, Descripcion, Precio, Stock, IdCategoria, IdEstado " +
                            "From Producto " +
                            "Where NombreProducto Like ?"
            );

            ps.setString(1, "%" + nombre + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt(1));
                p.setNombreProducto(rs.getString(2));
                p.setDescripcion(rs.getString(3));
                p.setPrecio(rs.getDouble(4));
                p.setStock(rs.getInt(5));
                p.setIdCategoria(rs.getInt(6));
                p.setIdEstado(rs.getInt(7));
                productos.add(p);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar productos: "
                    + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return productos;
    }

    public Producto getById(int pId) throws SQLException {
        Producto producto = new Producto();
        try {
            ps = conn.connect().prepareStatement(
                    "Select IdProducto, NombreProducto, Descripcion, Precio, Stock, IdCategoria, IdEstado " +
                            "From Producto " +
                            "Where IdProducto = ?"
            );

            ps.setInt(1, pId);
            rs = ps.executeQuery();

            if (rs.next()) {
                producto.setIdProducto(rs.getInt(1));
                producto.setNombreProducto(rs.getString(2));
                producto.setDescripcion(rs.getString(3));
                producto.setPrecio(rs.getDouble(4));
                producto.setStock(rs.getInt(5));
                producto.setIdCategoria(rs.getInt(6));
                producto.setIdEstado(rs.getInt(7));
            } else {
                producto = null;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener producto por id: "
                    + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return producto;
    }

    public ArrayList<Producto> getAll() throws SQLException {
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            ps = conn.connect().prepareStatement(
                    "Select IdProducto, NombreProducto, Descripcion, Precio, Stock, IdCategoria, IdEstado " +
                            "From Producto"
            );

            rs = ps.executeQuery();

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt(1));
                p.setNombreProducto(rs.getString(2));
                p.setDescripcion(rs.getString(3));
                p.setPrecio(rs.getDouble(4));
                p.setStock(rs.getInt(5));
                p.setIdCategoria(rs.getInt(6));
                p.setIdEstado(rs.getInt(7));
                productos.add(p);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener los productos: "
                    + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return productos;
    }
}
