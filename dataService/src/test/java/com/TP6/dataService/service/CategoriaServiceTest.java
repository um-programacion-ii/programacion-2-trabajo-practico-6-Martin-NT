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
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Bebidas");
        categoria.setDescripcion("Descripción Bebidas");
    }

    // ------------------- GUARDAR -------------------

    @Test
    void cuandoGuardarCategoriaValida_entoncesPersiste() {
        when(categoriaRepository.findByNombre("Bebidas")).thenReturn(Optional.empty());
        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        Categoria resultado = categoriaService.guardar(categoria);

        assertNotNull(resultado);
        assertEquals("Bebidas", resultado.getNombre());
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void cuandoGuardarCategoriaDuplicada_entoncesLanzaExcepcion() {
        when(categoriaRepository.findByNombre("Bebidas")).thenReturn(Optional.of(categoria));

        assertThrows(CategoriaYaExisteException.class, () -> categoriaService.guardar(categoria));

        verify(categoriaRepository, never()).save(any());
    }

    // ------------------- BUSCAR -------------------

    @Test
    void cuandoBuscarPorIdExistente_entoncesRetornaCategoria() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Categoria resultado = categoriaService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Bebidas", resultado.getNombre());
    }

    @Test
    void cuandoBuscarPorIdNoExistente_entoncesLanzaExcepcion() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CategoriaNoEncontradaException.class, () -> categoriaService.buscarPorId(99L));
    }

    @Test
    void cuandoBuscarPorNombreExistente_entoncesRetornaCategoria() {
        when(categoriaRepository.findByNombre("Bebidas")).thenReturn(Optional.of(categoria));

        Categoria resultado = categoriaService.buscarPorNombre("Bebidas");

        assertNotNull(resultado);
        assertEquals("Bebidas", resultado.getNombre());
    }

    @Test
    void cuandoBuscarPorNombreNoExistente_entoncesLanzaExcepcion() {
        when(categoriaRepository.findByNombre("Alimentos")).thenReturn(Optional.empty());

        assertThrows(CategoriaNoEncontradaException.class, () -> categoriaService.buscarPorNombre("Alimentos"));
    }

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

    @Test
    void cuandoActualizarCategoriaExistente_entoncesPersiste() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);
        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        Categoria actualizada = categoriaService.actualizar(1L, categoria);

        assertNotNull(actualizada);
        assertEquals(1L, actualizada.getId());
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void cuandoActualizarCategoriaInexistente_entoncesLanzaExcepcion() {
        when(categoriaRepository.existsById(99L)).thenReturn(false);

        assertThrows(CategoriaNoEncontradaException.class, () -> categoriaService.actualizar(99L, categoria));
    }

    // ------------------- ELIMINAR -------------------

    @Test
    void cuandoEliminarCategoriaExistente_entoncesElimina() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);

        categoriaService.eliminar(1L);

        verify(categoriaRepository).deleteById(1L);
    }

    @Test
    void cuandoEliminarCategoriaInexistente_entoncesLanzaExcepcion() {
        when(categoriaRepository.existsById(99L)).thenReturn(false);

        assertThrows(CategoriaNoEncontradaException.class, () -> categoriaService.eliminar(99L));
    }
}
