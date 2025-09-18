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
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    private Inventario inventario;

    @BeforeEach
    void setUp() {
        inventario = new Inventario();
        inventario.setId(1L);
        inventario.setCantidad(10);
        inventario.setStockMinimo(5);
        inventario.setFechaActualizacion(LocalDateTime.now());
    }

    // ------------------- GUARDAR -------------------

    @Test
    void cuandoGuardarInventarioValido_entoncesPersiste() {
        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        Inventario resultado = inventarioService.guardar(inventario);

        assertNotNull(resultado);
        assertEquals(10, resultado.getCantidad());
        verify(inventarioRepository).save(inventario);
    }

    // ------------------- BUSCAR -------------------

    @Test
    void cuandoBuscarPorIdExistente_entoncesRetornaInventario() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        Inventario resultado = inventarioService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(10, resultado.getCantidad());
    }

    @Test
    void cuandoBuscarPorIdNoExistente_entoncesLanzaExcepcion() {
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(InventarioNoEncontradoException.class, () -> inventarioService.buscarPorId(99L));
    }

    @Test
    void cuandoBuscarPorProductoExistente_entoncesRetornaInventario() {
        when(inventarioRepository.findByProductoId(1L)).thenReturn(Optional.of(inventario));

        Inventario resultado = inventarioService.buscarPorProducto(1L);

        assertNotNull(resultado);
        assertEquals(10, resultado.getCantidad());
    }

    @Test
    void cuandoBuscarPorProductoNoExistente_entoncesLanzaExcepcion() {
        when(inventarioRepository.findByProductoId(99L)).thenReturn(Optional.empty());

        assertThrows(InventarioNoEncontradoException.class, () -> inventarioService.buscarPorProducto(99L));
    }

    @Test
    void cuandoBuscarPorCantidad_entoncesRetornaLista() {
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioRepository.findByCantidad(10)).thenReturn(inventarios);

        List<Inventario> resultado = inventarioService.buscarPorCantidad(10);

        assertEquals(1, resultado.size());
        assertEquals(10, resultado.get(0).getCantidad());
    }

    @Test
    void cuandoBuscarConStockBajo_entoncesRetornaLista() {
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioRepository.findConStockBajo()).thenReturn(inventarios);

        List<Inventario> resultado = inventarioService.buscarConStockBajo();

        assertEquals(1, resultado.size());
    }

    @Test
    void cuandoBuscarConStockAlto_entoncesRetornaLista() {
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioRepository.findConStockAlto()).thenReturn(inventarios);

        List<Inventario> resultado = inventarioService.buscarConStockAlto();

        assertEquals(1, resultado.size());
    }

    @Test
    void cuandoObtenerTodos_entoncesRetornaLista() {
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioRepository.findAll()).thenReturn(inventarios);

        List<Inventario> resultado = inventarioService.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals(10, resultado.get(0).getCantidad());
    }

    // ------------------- ACTUALIZAR -------------------

    @Test
    void cuandoActualizarInventarioExistente_entoncesPersiste() {
        when(inventarioRepository.existsById(1L)).thenReturn(true);
        when(inventarioRepository.save(inventario)).thenReturn(inventario);

        Inventario resultado = inventarioService.actualizar(1L, inventario);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void cuandoActualizarInventarioNoExistente_entoncesLanzaExcepcion() {
        when(inventarioRepository.existsById(99L)).thenReturn(false);

        assertThrows(InventarioNoEncontradoException.class, () -> inventarioService.actualizar(99L, inventario));
    }

    // ------------------- ELIMINAR -------------------

    @Test
    void cuandoEliminarInventarioExistente_entoncesElimina() {
        when(inventarioRepository.existsById(1L)).thenReturn(true);

        inventarioService.eliminar(1L);

        verify(inventarioRepository).deleteById(1L);
    }

    @Test
    void cuandoEliminarInventarioNoExistente_entoncesLanzaExcepcion() {
        when(inventarioRepository.existsById(99L)).thenReturn(false);

        assertThrows(InventarioNoEncontradoException.class, () -> inventarioService.eliminar(99L));
    }
}
