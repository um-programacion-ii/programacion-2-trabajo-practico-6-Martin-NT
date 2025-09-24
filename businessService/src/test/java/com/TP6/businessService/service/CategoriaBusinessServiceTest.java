package com.TP6.businessService.service;

import com.TP6.businessService.client.DataServiceClient;
import com.TP6.businessService.dto.CategoriaDTO;
import com.TP6.businessService.exception.CategoriaNoEncontradaException;
import com.TP6.businessService.exception.MicroserviceCommunicationException;
import com.TP6.businessService.exception.ValidacionNegocioException;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaBusinessServiceTest {

    @Mock
    private DataServiceClient dataServiceClient; // Mock del cliente Feign

    @InjectMocks
    private CategoriaBusinessService categoriaBusinessService; // Service que probamos

    // ------------------- TESTS OBTENER -------------------

    // Caso exitoso: obtiene todas las categorías
    @Test
    void cuandoObtenerTodasLasCategorias_entoncesRetornaLista() {
        // Arrange → simulamos lista de categorías
        List<CategoriaDTO> categoriasEsperadas = Arrays.asList(
                new CategoriaDTO(1L, "Categoría 1", "Descripción 1"),
                new CategoriaDTO(2L, "Categoría 2", "Descripción 2")
        );
        when(dataServiceClient.obtenerTodasLasCategorias()).thenReturn(categoriasEsperadas);

        // Act
        List<CategoriaDTO> resultado = categoriaBusinessService.obtenerTodasLasCategorias();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("Categoría 1", resultado.get(0).getNombre());
        verify(dataServiceClient).obtenerTodasLasCategorias();
    }

    // ------------------- TESTS CREAR -------------------

    // Caso exitoso: crea una categoría válida
    @Test
    void cuandoCrearCategoriaValida_entoncesSeCreaCorrectamente() {
        // Arrange
        CategoriaDTO nueva = new CategoriaDTO(null, "Nueva Categoría", "Descripción nueva");
        CategoriaDTO creada = new CategoriaDTO(10L, "Nueva Categoría", "Descripción nueva");
        when(dataServiceClient.crearCategoria(nueva)).thenReturn(creada);

        // Act
        CategoriaDTO resultado = categoriaBusinessService.crearCategoria(nueva);

        // Assert
        assertNotNull(resultado.getId());
        assertEquals("Nueva Categoría", resultado.getNombre());
        verify(dataServiceClient).crearCategoria(nueva);
    }

    // Caso error: nombre vacío lanza ValidacionNegocioException
    @Test
    void cuandoCrearCategoriaConNombreVacio_entoncesLanzaExcepcion() {
        // Arrange → nombre vacío
        CategoriaDTO categoria = new CategoriaDTO(null, "   ", "Descripción");

        // Act & Assert
        assertThrows(ValidacionNegocioException.class, () -> {
            categoriaBusinessService.crearCategoria(categoria);
        });

        // Verificamos que NO se llame al cliente
        verify(dataServiceClient, never()).crearCategoria(any());
    }

    // ------------------- TESTS BUSCAR -------------------

    // Caso error: categoría por ID inexistente lanza CategoriaNoEncontradaException
    @Test
    void cuandoObtenerCategoriaPorIdInexistente_entoncesLanzaCategoriaNoEncontradaException() {
        // Arrange → simulamos 404 NotFound
        when(dataServiceClient.obtenerCategoriaPorId(99L))
                .thenThrow(FeignException.NotFound.class);

        // Act & Assert
        assertThrows(CategoriaNoEncontradaException.class, () -> {
            categoriaBusinessService.obtenerCategoriaPorId(99L);
        });
    }

    // Caso error: categoría por nombre inexistente lanza CategoriaNoEncontradaException
    @Test
    void cuandoObtenerCategoriaPorNombreInexistente_entoncesLanzaCategoriaNoEncontradaException() {
        // Arrange → simulamos 404 NotFound
        when(dataServiceClient.obtenerCategoriaPorNombre("Inexistente"))
                .thenThrow(FeignException.NotFound.class);

        // Act & Assert
        assertThrows(CategoriaNoEncontradaException.class, () -> {
            categoriaBusinessService.obtenerCategoriaPorNombre("Inexistente");
        });
    }

    // ------------------- TESTS ERRORES GENERALES -------------------

    // Caso error: fallo en comunicación con data-service
    @Test
    void cuandoObtenerCategorias_yDataServiceFalla_entoncesLanzaMicroserviceCommunicationException() {
        // Arrange → simulamos error genérico
        when(dataServiceClient.obtenerTodasLasCategorias())
                .thenThrow(FeignException.class);

        // Act & Assert
        assertThrows(MicroserviceCommunicationException.class, () -> {
            categoriaBusinessService.obtenerTodasLasCategorias();
        });
    }
}

