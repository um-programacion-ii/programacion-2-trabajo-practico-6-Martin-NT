package com.TP6.businessService.service;

import com.TP6.businessService.client.DataServiceClient;
import com.TP6.businessService.dto.ProductoDTO;
import com.TP6.businessService.dto.ProductoRequest;
import com.TP6.businessService.exception.MicroserviceCommunicationException;
import com.TP6.businessService.exception.ProductoNoEncontradoException;
import com.TP6.businessService.exception.ValidacionNegocioException;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoBusinessServiceTest {

    @Mock
    private DataServiceClient dataServiceClient; // Simulamos el cliente Feign (data-service)

    @InjectMocks
    private ProductoBusinessService productoBusinessService; // Inyectamos el mock en el service

    // ------------------- TESTS OBTENER -------------------

    // Caso exitoso: obtiene todos los productos
    @Test
    void cuandoObtenerTodosLosProductos_entoncesRetornaLista() {
        // Arrange → simulamos lista de productos
        List<ProductoDTO> productosEsperados = Arrays.asList(
                new ProductoDTO(1L, "Producto 1", "Descripción 1", BigDecimal.valueOf(100), "Categoría 1", 10, false),
                new ProductoDTO(2L, "Producto 2", "Descripción 2", BigDecimal.valueOf(200), "Categoría 2", 5, true)
        );
        when(dataServiceClient.obtenerTodosLosProductos()).thenReturn(productosEsperados);

        // Act
        List<ProductoDTO> resultado = productoBusinessService.obtenerTodosLosProductos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Producto 1", resultado.get(0).getNombre());
        verify(dataServiceClient).obtenerTodosLosProductos();
    }

    // Caso exitoso: calcular valor total del inventario
    @Test
    void cuandoCalcularValorTotalInventario_entoncesRetornaSumaCorrecta() {
        // Arrange → dos productos con precio y stock
        List<ProductoDTO> productos = Arrays.asList(
                new ProductoDTO(1L, "Producto 1", "Descripción", BigDecimal.valueOf(100), "Categoría 1", 2, false), // 200
                new ProductoDTO(2L, "Producto 2", "Descripción", BigDecimal.valueOf(50), "Categoría 2", 3, false)   // 150
        );
        when(dataServiceClient.obtenerTodosLosProductos()).thenReturn(productos);

        // Act
        BigDecimal total = productoBusinessService.calcularValorTotalInventario();

        // Assert → 200 + 150 = 350
        assertEquals(BigDecimal.valueOf(350), total);
    }

    // ------------------- TESTS CREAR -------------------

    // Caso error: precio negativo lanza ValidacionNegocioException
    @Test
    void cuandoCrearProductoConPrecioInvalido_entoncesLanzaExcepcion() {
        // Arrange → producto con precio inválido
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Producto Test");
        request.setPrecio(BigDecimal.valueOf(-10));
        request.setStock(5);

        // Act & Assert
        assertThrows(ValidacionNegocioException.class, () -> {
            productoBusinessService.crearProducto(request);
        });

        // Verificamos que NO se llame al cliente
        verify(dataServiceClient, never()).crearProducto(any());
    }

    // Caso error: stock negativo lanza ValidacionNegocioException
    @Test
    void cuandoCrearProductoConStockNegativo_entoncesLanzaExcepcion() {
        // Arrange → producto con stock inválido
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Producto Test");
        request.setPrecio(BigDecimal.valueOf(100));
        request.setStock(-5);

        // Act & Assert
        assertThrows(ValidacionNegocioException.class, () -> {
            productoBusinessService.crearProducto(request);
        });

        verify(dataServiceClient, never()).crearProducto(any());
    }

    // ------------------- TESTS BUSCAR -------------------

    // Caso error: producto por ID inexistente lanza ProductoNoEncontradoException
    @Test
    void cuandoObtenerProductoPorIdInexistente_entoncesLanzaProductoNoEncontradoException() {
        // Arrange → simulamos 404 NotFound
        Long idInexistente = 99L;
        when(dataServiceClient.obtenerProductoPorId(idInexistente))
                .thenThrow(FeignException.NotFound.class);

        // Act & Assert
        assertThrows(ProductoNoEncontradoException.class, () -> {
            productoBusinessService.obtenerProductoPorId(idInexistente);
        });
    }

    // ------------------- TESTS ERRORES GENERALES -------------------

    // Caso error: fallo de comunicación con data-service
    @Test
    void cuandoObtenerTodosLosProductos_yDataServiceFalla_entoncesLanzaMicroserviceCommunicationException() {
        // Arrange → simulamos error de red
        when(dataServiceClient.obtenerTodosLosProductos())
                .thenThrow(FeignException.class);

        // Act & Assert
        assertThrows(MicroserviceCommunicationException.class, () -> {
            productoBusinessService.obtenerTodosLosProductos();
        });
    }
}
