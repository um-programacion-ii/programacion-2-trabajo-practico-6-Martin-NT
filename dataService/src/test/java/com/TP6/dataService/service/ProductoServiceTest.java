package com.TP6.dataService.service;

import com.TP6.dataService.entity.Producto;
import com.TP6.dataService.exception.ProductoNoEncontradoException;
import com.TP6.dataService.exception.ProductoYaExisteException;
import com.TP6.dataService.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository; // Simulamos el repositorio

    @InjectMocks
    private ProductoService productoService; // Service bajo prueba

    private Producto producto;

    @BeforeEach
    void setUp() {
        // Creamos un producto de prueba
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Coca Cola");
        producto.setDescripcion("Bebida gaseosa");
        producto.setPrecio(BigDecimal.valueOf(100));
    }

    // ------------------- GUARDAR -------------------

    // Caso exitoso: guardar producto válido
    @Test
    void cuandoGuardarProductoValido_entoncesPersiste() {
        when(productoRepository.findByNombre("Coca Cola")).thenReturn(Optional.empty());
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto resultado = productoService.guardar(producto);

        assertNotNull(resultado);
        assertEquals("Coca Cola", resultado.getNombre());
        verify(productoRepository).save(producto);
    }

    // Caso error: guardar producto duplicado lanza excepción
    @Test
    void cuandoGuardarProductoDuplicado_entoncesLanzaExcepcion() {
        when(productoRepository.findByNombre("Coca Cola")).thenReturn(Optional.of(producto));

        assertThrows(ProductoYaExisteException.class, () -> productoService.guardar(producto));

        verify(productoRepository, never()).save(any());
    }

    // ------------------- BUSCAR -------------------

    // Caso exitoso: buscar por ID existente
    @Test
    void cuandoBuscarPorIdExistente_entoncesRetornaProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Coca Cola", resultado.getNombre());
    }

    // Caso error: buscar por ID inexistente lanza excepción
    @Test
    void cuandoBuscarPorIdNoExistente_entoncesLanzaExcepcion() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductoNoEncontradoException.class, () -> productoService.buscarPorId(99L));
    }

    // Caso exitoso: buscar por nombre existente
    @Test
    void cuandoBuscarPorNombreExistente_entoncesRetornaProducto() {
        when(productoRepository.findByNombre("Coca Cola")).thenReturn(Optional.of(producto));

        Producto resultado = productoService.buscarPorNombre("Coca Cola");

        assertNotNull(resultado);
        assertEquals("Coca Cola", resultado.getNombre());
    }

    // Caso error: buscar por nombre inexistente lanza excepción
    @Test
    void cuandoBuscarPorNombreNoExistente_entoncesLanzaExcepcion() {
        when(productoRepository.findByNombre("Pepsi")).thenReturn(Optional.empty());

        assertThrows(ProductoNoEncontradoException.class, () -> productoService.buscarPorNombre("Pepsi"));
    }

    // Caso exitoso: buscar por precio devuelve lista
    @Test
    void cuandoBuscarPorPrecio_entoncesRetornaLista() {
        List<Producto> productos = Arrays.asList(producto);
        when(productoRepository.findByPrecio(BigDecimal.valueOf(100))).thenReturn(productos);

        List<Producto> resultado = productoService.buscarPorPrecio(BigDecimal.valueOf(100));

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Coca Cola", resultado.get(0).getNombre());
    }

    // Caso exitoso: buscar por categoría devuelve lista
    @Test
    void cuandoBuscarPorCategoria_entoncesRetornaLista() {
        List<Producto> productos = Arrays.asList(producto);
        when(productoRepository.findByNombreCategoria("Bebidas")).thenReturn(productos);

        List<Producto> resultado = productoService.buscarPorCategoria("Bebidas");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Coca Cola", resultado.get(0).getNombre());
    }

    // ------------------- OBTENER TODOS -------------------

    // Caso exitoso: obtener todos los productos
    @Test
    void cuandoObtenerTodos_entoncesRetornaLista() {
        List<Producto> productos = Arrays.asList(producto);
        when(productoRepository.findAll()).thenReturn(productos);

        List<Producto> resultado = productoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Coca Cola", resultado.get(0).getNombre());
    }

    // ------------------- ACTUALIZAR -------------------

    // Caso exitoso: actualizar producto existente
    @Test
    void cuandoActualizarProductoExistente_entoncesPersiste() {
        when(productoRepository.existsById(1L)).thenReturn(true);
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto actualizado = productoService.actualizar(1L, producto);

        assertNotNull(actualizado);
        assertEquals(1L, actualizado.getId());
        verify(productoRepository).save(producto);
    }

    // Caso error: actualizar producto inexistente lanza excepción
    @Test
    void cuandoActualizarProductoInexistente_entoncesLanzaExcepcion() {
        when(productoRepository.existsById(99L)).thenReturn(false);

        assertThrows(ProductoNoEncontradoException.class, () -> productoService.actualizar(99L, producto));
    }

    // ------------------- ELIMINAR -------------------

    // Caso exitoso: eliminar producto existente
    @Test
    void cuandoEliminarProductoExistente_entoncesElimina() {
        when(productoRepository.existsById(1L)).thenReturn(true);

        productoService.eliminar(1L);

        verify(productoRepository).deleteById(1L);
    }

    // Caso error: eliminar producto inexistente lanza excepción
    @Test
    void cuandoEliminarProductoInexistente_entoncesLanzaExcepcion() {
        when(productoRepository.existsById(99L)).thenReturn(false);

        assertThrows(ProductoNoEncontradoException.class, () -> productoService.eliminar(99L));
    }
}
