package com.TP6.dataService.service;

import com.TP6.dataService.entity.Categoria;
import com.TP6.dataService.exception.CategoriaNoEncontradaException;
import com.TP6.dataService.exception.CategoriaYaExisteException;
import com.TP6.dataService.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository; // Simulamos el repositorio

    @InjectMocks
    private CategoriaService categoriaService; // Service bajo prueba

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        // Creamos categoría de prueba
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Bebidas");
        categoria.setDescripcion("Descripción Bebidas");
    }

    // ------------------- GUARDAR -------------------

    // Caso exitoso: guardar categoría válida
    @Test
    void cuandoGuardarCategoriaValida_entoncesPersiste() {
        when(categoriaRepository.findByNombre("Bebidas")).thenReturn(Optional.empty());
        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        Categoria resultado = categoriaService.guardar(categoria);

        assertNotNull(resultado);
        assertEquals("Bebidas", resultado.getNombre());
        verify(categoriaRepository).save(categoria);
    }

    // Caso error: guardar categoría duplicada lanza excepción
    @Test
    void cuandoGuardarCategoriaDuplicada_entoncesLanzaExcepcion() {
        when(categoriaRepository.findByNombre("Bebidas")).thenReturn(Optional.of(categoria));

        assertThrows(CategoriaYaExisteException.class, () -> categoriaService.guardar(categoria));

        verify(categoriaRepository, never()).save(any());
    }

    // ------------------- BUSCAR -------------------

    // Caso exitoso: buscar por ID existente
    @Test
    void cuandoBuscarPorIdExistente_entoncesRetornaCategoria() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Categoria resultado = categoriaService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Bebidas", resultado.getNombre());
    }

    // Caso error: buscar por ID inexistente lanza excepción
    @Test
    void cuandoBuscarPorIdNoExistente_entoncesLanzaExcepcion() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CategoriaNoEncontradaException.class, () -> categoriaService.buscarPorId(99L));
    }

    // Caso exitoso: buscar por nombre existente
    @Test
    void cuandoBuscarPorNombreExistente_entoncesRetornaCategoria() {
        when(categoriaRepository.findByNombre("Bebidas")).thenReturn(Optional.of(categoria));

        Categoria resultado = categoriaService.buscarPorNombre("Bebidas");

        assertNotNull(resultado);
        assertEquals("Bebidas", resultado.getNombre());
    }

    // Caso error: buscar por nombre inexistente lanza excepción
    @Test
    void cuandoBuscarPorNombreNoExistente_entoncesLanzaExcepcion() {
        when(categoriaRepository.findByNombre("Alimentos")).thenReturn(Optional.empty());

        assertThrows(CategoriaNoEncontradaException.class, () -> categoriaService.buscarPorNombre("Alimentos"));
    }

    // Caso exitoso: buscar categorías con productos asociados
    @Test
    void cuandoBuscarCategoriasConProductos_entoncesRetornaLista() {
        List<Categoria> categorias = Arrays.asList(categoria);
        when(categoriaRepository.findCategoriasConProductos()).thenReturn(categorias);

        List<Categoria> resultado = categoriaService.buscarCategoriasConProductos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Bebidas", resultado.get(0).getNombre());
    }

    // ------------------- OBTENER TODOS -------------------

    // Caso exitoso: obtener todas las categorías
    @Test
    void cuandoObtenerTodos_entoncesRetornaLista() {
        List<Categoria> categorias = Arrays.asList(categoria);
        when(categoriaRepository.findAll()).thenReturn(categorias);

        List<Categoria> resultado = categoriaService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Bebidas", resultado.get(0).getNombre());
    }

    // ------------------- ACTUALIZAR -------------------

    // Caso exitoso: actualizar categoría existente
    @Test
    void cuandoActualizarCategoriaExistente_entoncesPersiste() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);
        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        Categoria actualizada = categoriaService.actualizar(1L, categoria);

        assertNotNull(actualizada);
        assertEquals(1L, actualizada.getId());
        verify(categoriaRepository).save(categoria);
    }

    // Caso error: actualizar categoría inexistente lanza excepción
    @Test
    void cuandoActualizarCategoriaInexistente_entoncesLanzaExcepcion() {
        when(categoriaRepository.existsById(99L)).thenReturn(false);

        assertThrows(CategoriaNoEncontradaException.class, () -> categoriaService.actualizar(99L, categoria));
    }

    // ------------------- ELIMINAR -------------------

    // Caso exitoso: eliminar categoría existente
    @Test
    void cuandoEliminarCategoriaExistente_entoncesElimina() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);

        categoriaService.eliminar(1L);

        verify(categoriaRepository).deleteById(1L);
    }

    // Caso error: eliminar categoría inexistente lanza excepción
    @Test
    void cuandoEliminarCategoriaInexistente_entoncesLanzaExcepcion() {
        when(categoriaRepository.existsById(99L)).thenReturn(false);

        assertThrows(CategoriaNoEncontradaException.class, () -> categoriaService.eliminar(99L));
    }
}
