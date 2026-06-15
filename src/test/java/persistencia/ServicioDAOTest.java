package persistencia;

import dominio.Categoria;
import dominio.Servicio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;

class ServicioDAOTest {

    private static final int ID_ESTADO_ACTIVO = 1;

    ServicioDAO servicioDAO;
    CategoriaDAO categoriaDAO;
    ConnectionManager connectionManager;

    @BeforeEach
    void setUp() {
        servicioDAO = new ServicioDAO();
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

    private Categoria crearCategoriaDePrueba() throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setNombreCategoria("CategoriaParaServicio");
        categoria.setIdEstado(ID_ESTADO_ACTIVO);
        return categoriaDAO.create(categoria);
    }

    @Test
    void create() throws SQLException {
        Categoria categoria = crearCategoriaDePrueba();

        Servicio servicio = new Servicio();
        servicio.setNombreServicio("ServicioTest");
        servicio.setDescripcion("Descripcion de prueba");
        servicio.setPrecio(150.0);
        servicio.setDuracionMinutos(Time.valueOf("01:30:00"));
        servicio.setIdCategoria(categoria.getIdCategoria());
        servicio.setIdEstado(ID_ESTADO_ACTIVO);

        Servicio creado = servicioDAO.create(servicio);

        assertNotNull(creado, "El servicio creado no debe ser nulo");
        assertTrue(creado.getIdServicio() > 0);
        assertEquals("ServicioTest", creado.getNombreServicio());
        assertEquals(categoria.getIdCategoria(), creado.getIdCategoria());

        // Limpieza (primero el servicio, luego la categoria por la FK)
        servicioDAO.delete(creado);
        categoriaDAO.delete(categoria);
    }

    @Test
    void update() throws SQLException {
        Categoria categoria = crearCategoriaDePrueba();

        Servicio servicio = new Servicio();
        servicio.setNombreServicio("ServicioParaActualizar");
        servicio.setDescripcion("Original");
        servicio.setPrecio(100.0);
        servicio.setDuracionMinutos(Time.valueOf("00:45:00"));
        servicio.setIdCategoria(categoria.getIdCategoria());
        servicio.setIdEstado(ID_ESTADO_ACTIVO);
        Servicio creado = servicioDAO.create(servicio);

        creado.setNombreServicio("ServicioActualizado");
        creado.setPrecio(200.0);

        boolean actualizado = servicioDAO.update(creado);

        assertTrue(actualizado, "update debe retornar true");

        Servicio recargado = servicioDAO.getById(creado.getIdServicio());
        assertEquals("ServicioActualizado", recargado.getNombreServicio());
        assertEquals(200.0, recargado.getPrecio());

        // Limpieza
        servicioDAO.delete(creado);
        categoriaDAO.delete(categoria);
    }

    @Test
    void delete() throws SQLException {
        Categoria categoria = crearCategoriaDePrueba();

        Servicio servicio = new Servicio();
        servicio.setNombreServicio("ServicioParaEliminar");
        servicio.setDescripcion("Se va a borrar");
        servicio.setPrecio(50.0);
        servicio.setDuracionMinutos(Time.valueOf("00:30:00"));
        servicio.setIdCategoria(categoria.getIdCategoria());
        servicio.setIdEstado(ID_ESTADO_ACTIVO);
        Servicio creado = servicioDAO.create(servicio);

        boolean eliminado = servicioDAO.delete(creado);

        assertTrue(eliminado, "delete debe retornar true");

        Servicio buscado = servicioDAO.getById(creado.getIdServicio());
        assertNull(buscado, "El servicio eliminado no debe encontrarse");

        // Limpieza de la categoria
        categoriaDAO.delete(categoria);
    }

    @Test
    void getById() throws SQLException {
        Categoria categoria = crearCategoriaDePrueba();

        Servicio servicio = new Servicio();
        servicio.setNombreServicio("ServicioParaBuscar");
        servicio.setDescripcion("Buscar por id");
        servicio.setPrecio(75.0);
        servicio.setDuracionMinutos(Time.valueOf("01:00:00"));
        servicio.setIdCategoria(categoria.getIdCategoria());
        servicio.setIdEstado(ID_ESTADO_ACTIVO);
        Servicio creado = servicioDAO.create(servicio);

        Servicio encontrado = servicioDAO.getById(creado.getIdServicio());

        assertNotNull(encontrado, "Debe encontrar el servicio");
        assertEquals(creado.getIdServicio(), encontrado.getIdServicio());
        assertEquals("ServicioParaBuscar", encontrado.getNombreServicio());

        // Limpieza
        servicioDAO.delete(creado);
        categoriaDAO.delete(categoria);
    }
}