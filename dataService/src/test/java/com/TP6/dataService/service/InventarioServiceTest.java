package com.TP6.dataService.service;

import com.TP6.dataService.entity.Inventario;
import com.TP6.dataService.exception.InventarioNoEncontradoException;
import com.TP6.dataService.repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository; // Simulamos el repositorio

    @InjectMocks
    private InventarioService inventarioService; // Service bajo prueba

    private Inventario inventario;

    @BeforeEach
    void setUp() {
        // Creamos un inventario de prueba
        inventario = new Inventario();
        inventario.setId(1L);
        inventario.setCantidad(10);
        inventario.setStockMinimo(5);
        inventario.setFechaActualizacion(LocalDateTime.now());
    }

    // ------------------- GUARDAR -------------------

    // Caso exitoso: guardar inventario válido
    @Test
    void cuandoGuardarInventarioValido_entoncesPersiste() {
        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        Inventario resultado = inventarioService.guardar(inventario);

        assertNotNull(resultado);
        assertEquals(10, resultado.getCantidad());
        verify(inventarioRepository).save(inventario);
    }

    // ------------------- BUSCAR -------------------

    // Caso exitoso: buscar por ID existente
    @Test
    void cuandoBuscarPorIdExistente_entoncesRetornaInventario() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        Inventario resultado = inventarioService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(10, resultado.getCantidad());
    }

    // Caso error: buscar por ID inexistente lanza excepción
    @Test
    void cuandoBuscarPorIdNoExistente_entoncesLanzaExcepcion() {
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(InventarioNoEncontradoException.class, () -> inventarioService.buscarPorId(99L));
    }

    // Caso exitoso: buscar inventario por producto existente
    @Test
    void cuandoBuscarPorProductoExistente_entoncesRetornaInventario() {
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inventario));

        Inventario resultado = inventarioService.buscarPorProducto(1L);

        assertNotNull(resultado);
        assertEquals(10, resultado.getCantidad());
    }

    // Caso error: buscar inventario por producto inexistente lanza excepción
    @Test
    void cuandoBuscarPorProductoNoExistente_entoncesLanzaExcepcion() {
        when(inventarioRepository.findByProductoId(99L)).thenReturn(Optional.empty());

        assertThrows(InventarioNoEncontradoException.class, () -> inventarioService.buscarPorProducto(99L));
    }

    // Caso exitoso: buscar por cantidad devuelve lista
    @Test
    void cuandoBuscarPorCantidad_entoncesRetornaLista() {
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioRepository.findByCantidad(10)).thenReturn(inventarios);

        List<Inventario> resultado = inventarioService.buscarPorCantidad(10);

        assertEquals(1, resultado.size());
        assertEquals(10, resultado.get(0).getCantidad());
    }

    // Caso exitoso: buscar inventarios con stock bajo
    @Test
    void cuandoBuscarConStockBajo_entoncesRetornaLista() {
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioRepository.findConStockBajo()).thenReturn(inventarios);

        List<Inventario> resultado = inventarioService.buscarConStockBajo();

        assertEquals(1, resultado.size());
    }

    // Caso exitoso: buscar inventarios con stock alto
    @Test
    void cuandoBuscarConStockAlto_entoncesRetornaLista() {
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioRepository.findConStockAlto()).thenReturn(inventarios);

        List<Inventario> resultado = inventarioService.buscarConStockAlto();

        assertEquals(1, resultado.size());
    }

    // Caso exitoso: obtener todos los inventarios
    @Test
    void cuandoObtenerTodos_entoncesRetornaLista() {
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioRepository.findAll()).thenReturn(inventarios);

        List<Inventario> resultado = inventarioService.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals(10, resultado.get(0).getCantidad());
    }

    // ------------------- ACTUALIZAR -------------------

    // Caso exitoso: actualizar inventario existente
    @Test
    void cuandoActualizarInventarioExistente_entoncesPersiste() {
        when(inventarioRepository.existsById(1L)).thenReturn(true);
        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        Inventario resultado = inventarioService.actualizar(1L, inventario);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(inventarioRepository).save(inventario);
    }

    // Caso error: actualizar inventario inexistente lanza excepción
    @Test
    void cuandoActualizarInventarioNoExistente_entoncesLanzaExcepcion() {
        when(inventarioRepository.existsById(99L)).thenReturn(false);

        assertThrows(InventarioNoEncontradoException.class, () -> inventarioService.actualizar(99L, inventario));
    }

    // ------------------- ELIMINAR -------------------

    // Caso exitoso: eliminar inventario existente
    @Test
    void cuandoEliminarInventarioExistente_entoncesElimina() {
        when(inventarioRepository.existsById(1L)).thenReturn(true);

        inventarioService.eliminar(1L);

        verify(inventarioRepository).deleteById(1L);
    }

    // Caso error: eliminar inventario inexistente lanza excepción
    @Test
    void cuandoEliminarInventarioNoExistente_entoncesLanzaExcepcion() {
        when(inventarioRepository.existsById(99L)).thenReturn(false);

        assertThrows(InventarioNoEncontradoException.class, () -> inventarioService.eliminar(99L));
    }
}
