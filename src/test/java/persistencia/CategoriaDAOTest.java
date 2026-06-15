package persistencia;

import dominio.Categoria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaDAOTest {

    // Cambia este valor si en tu BD "Activo" no es el IdEstado = 1
    private static final int ID_ESTADO_ACTIVO = 1;

    CategoriaDAO categoriaDAO;
    ConnectionManager connectionManager;

    @BeforeEach
    void setUp() {
        categoriaDAO = new CategoriaDAO();
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
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria("CategoriaTest");
        categoria.setIdEstado(ID_ESTADO_ACTIVO);

        Categoria creada = categoriaDAO.create(categoria);

        assertNotNull(creada, "La categoria creada no debe ser nula");
        assertTrue(creada.getIdCategoria() > 0, "El IdCategoria debe ser mayor a 0");
        assertEquals("CategoriaTest", creada.getNombreCategoria());
        assertEquals(ID_ESTADO_ACTIVO, creada.getIdEstado());

        // Limpieza
        categoriaDAO.delete(creada);
    }

    @Test
    void update() throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria("CategoriaParaActualizar");
        categoria.setIdEstado(ID_ESTADO_ACTIVO);
        Categoria creada = categoriaDAO.create(categoria);

        creada.setNombreCategoria("CategoriaActualizada");

        boolean actualizado = categoriaDAO.update(creada);

        assertTrue(actualizado, "update debe retornar true");

        Categoria recargada = categoriaDAO.getById(creada.getIdCategoria());
        assertEquals("CategoriaActualizada", recargada.getNombreCategoria());

        // Limpieza
        categoriaDAO.delete(creada);
    }

    @Test
    void delete() throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria("CategoriaParaEliminar");
        categoria.setIdEstado(ID_ESTADO_ACTIVO);
        Categoria creada = categoriaDAO.create(categoria);

        boolean eliminado = categoriaDAO.delete(creada);

        assertTrue(eliminado, "delete debe retornar true");

        Categoria buscada = categoriaDAO.getById(creada.getIdCategoria());
        assertNull(buscada, "La categoria eliminada no debe encontrarse");
    }

    @Test
    void getById() throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria("CategoriaParaBuscar");
        categoria.setIdEstado(ID_ESTADO_ACTIVO);
        Categoria creada = categoriaDAO.create(categoria);

        Categoria encontrada = categoriaDAO.getById(creada.getIdCategoria());

        assertNotNull(encontrada, "Debe encontrar la categoria");
        assertEquals(creada.getIdCategoria(), encontrada.getIdCategoria());
        assertEquals("CategoriaParaBuscar", encontrada.getNombreCategoria());

        // Limpieza
        categoriaDAO.delete(creada);
    }
}