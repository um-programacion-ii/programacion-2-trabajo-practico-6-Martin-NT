package com.TP6.businessService.service;

import com.TP6.businessService.client.DataServiceClient;
import com.TP6.businessService.dto.InventarioDTO;
import com.TP6.businessService.dto.ProductoDTO;
import com.TP6.businessService.exception.InventarioNoEncontradoException;
import com.TP6.businessService.exception.MicroserviceCommunicationException;
import com.TP6.businessService.exception.ValidacionNegocioException;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioBusinessServiceTest {

    @Mock
    private DataServiceClient dataServiceClient; // Mock del cliente Feign

    @InjectMocks
    private InventarioBusinessService inventarioBusinessService; // Service bajo prueba

    // Helper para crear inventarios con datos de prueba
    private InventarioDTO crearInventario(Long id, int cantidad, int stockMinimo) {
        ProductoDTO producto = new ProductoDTO(
                1L, "Producto 1", "Descripción", BigDecimal.valueOf(100), "Categoría", 10, false
        );
        return new InventarioDTO(id, producto, cantidad, stockMinimo, LocalDateTime.now());
    }

    // ------------------- TESTS OBTENER -------------------

    // Caso exitoso: obtener todos los inventarios
    @Test
    void cuandoObtenerTodosLosInventarios_entoncesRetornaLista() {
        // Arrange
        List<InventarioDTO> inventariosEsperados = Arrays.asList(
                crearInventario(1L, 10, 2),
                crearInventario(2L, 5, 1)
        );
        when(dataServiceClient.obtenerTodosLosInventarios()).thenReturn(inventariosEsperados);

        // Act
        List<InventarioDTO> resultado = inventarioBusinessService.obtenerTodosLosInventarios();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals(10, resultado.get(0).getCantidad());
        verify(dataServiceClient).obtenerTodosLosInventarios();
    }

    // ------------------- TESTS CREAR -------------------

    // Caso exitoso: crear inventario válido
    @Test
    void cuandoCrearInventarioValido_entoncesSeCreaCorrectamente() {
        // Arrange
        InventarioDTO nuevo = crearInventario(null, 10, 2);
        InventarioDTO creado = crearInventario(100L, 10, 2);
        when(dataServiceClient.crearInventario(nuevo)).thenReturn(creado);

        // Act
        InventarioDTO resultado = inventarioBusinessService.crearInventario(nuevo);

        // Assert
        assertNotNull(resultado.getId());
        assertEquals(10, resultado.getCantidad());
        verify(dataServiceClient).crearInventario(nuevo);
    }

    // Caso error: cantidad negativa lanza ValidacionNegocioException
    @Test
    void cuandoCrearInventarioConCantidadNegativa_entoncesLanzaExcepcion() {
        // Arrange
        InventarioDTO inventario = crearInventario(null, -5, 2);

        // Act & Assert
        assertThrows(ValidacionNegocioException.class, () -> {
            inventarioBusinessService.crearInventario(inventario);
        });

        // Verificamos que NO se llama al cliente
        verify(dataServiceClient, never()).crearInventario(any());
    }

    // Caso error: stock mínimo negativo lanza ValidacionNegocioException
    @Test
    void cuandoCrearInventarioConStockMinimoNegativo_entoncesLanzaExcepcion() {
        // Arrange
        InventarioDTO inventario = crearInventario(null, 10, -1);

        // Act & Assert
        assertThrows(ValidacionNegocioException.class, () -> {
            inventarioBusinessService.crearInventario(inventario);
        });

        verify(dataServiceClient, never()).crearInventario(any());
    }

    // ------------------- TESTS BUSCAR -------------------

    // Caso error: inventario por ID inexistente lanza InventarioNoEncontradoException
    @Test
    void cuandoObtenerInventarioPorIdInexistente_entoncesLanzaInventarioNoEncontradoException() {
        // Arrange
        when(dataServiceClient.obtenerInventarioPorId(99L))
                .thenThrow(FeignException.NotFound.class);

        // Act & Assert
        assertThrows(InventarioNoEncontradoException.class, () -> {
            inventarioBusinessService.obtenerInventarioPorId(99L);
        });
    }

    // Caso error: inventario por producto inexistente lanza InventarioNoEncontradoException
    @Test
    void cuandoObtenerInventarioPorProductoInexistente_entoncesLanzaInventarioNoEncontradoException() {
        // Arrange
        when(dataServiceClient.obtenerInventarioPorProducto(50L))
                .thenThrow(FeignException.NotFound.class);

        // Act & Assert
        assertThrows(InventarioNoEncontradoException.class, () -> {
            inventarioBusinessService.obtenerInventarioPorProducto(50L);
        });
    }

    // ------------------- TESTS ERRORES GENERALES -------------------

    // Caso error: fallo en comunicación con data-service
    @Test
    void cuandoObtenerInventarios_yDataServiceFalla_entoncesLanzaMicroserviceCommunicationException() {
        // Arrange
        when(dataServiceClient.obtenerTodosLosInventarios())
                .thenThrow(FeignException.class);

        // Act & Assert
        assertThrows(MicroserviceCommunicationException.class, () -> {
            inventarioBusinessService.obtenerTodosLosInventarios();
        });
    }
}
