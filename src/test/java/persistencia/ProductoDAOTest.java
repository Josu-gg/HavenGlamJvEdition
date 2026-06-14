package persistencia;

import dominio.Producto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProductoDAOTest {

    ProductoDAO productoDAO;

    @BeforeEach
    void setUp() throws SQLException {
        productoDAO = new ProductoDAO();
    }

    @AfterEach
    void tearDown() throws SQLException {
        productoDAO = null;
    }

    @Test
    void create() throws SQLException {
        Producto producto = new Producto();
        producto.setNombreProducto("Mascarilla");
        producto.setDescripcion("Mascarilla hidratante");
        producto.setPrecio(18.99);
        producto.setStock(25);
        producto.setIdCategoria(1);
        producto.setIdEstado(1);

        Producto result = productoDAO.create(producto);

        assertNotNull(result, "El producto creado no debe ser nulo");
        assertTrue(result.getIdProducto() > 0, "Debe tener un ID generado");
        assertEquals("Mascarilla", result.getNombreProducto(), "El nombre debe coincidir");
    }

    @Test
    void update()  throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(1);
        producto.setNombreProducto("Shampoo Editado");
        producto.setDescripcion("Descripcion editada");
        producto.setPrecio(20.00);
        producto.setStock(60);
        producto.setIdCategoria(1);
        producto.setIdEstado(1);

        boolean result = productoDAO.update(producto);

        assertTrue(result, "El update debe retornar true");
    }


    @Test
    void delete() throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(3);

        boolean result = productoDAO.delete(producto);

        assertTrue(result, "El delete debe retornar true");
    }

    @Test
    void search()  throws SQLException {
        Producto producto = new Producto();
        producto.setNombreProducto("Sh");

        ArrayList<Producto> result = productoDAO.search(producto);

        assertNotNull(result, "La lista no debe ser nula");
        assertFalse(result.isEmpty(), "Debe encontrar al menos un producto");
    }
    @Test
    void getById()  throws SQLException {
        Producto result = productoDAO.getById(1);

        assertNotNull(result, "El producto no debe ser nulo");
        assertEquals(1, result.getIdProducto(), "El ID debe ser 1");
    }
}