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

    @Test
    void cuandoObtenerTodosLosProductos_entoncesRetornaLista() {
        // Arrange → preparamos datos falsos que el mock va a devolver
        List<ProductoDTO> productosEsperados = Arrays.asList(
                new ProductoDTO(1L, "Producto 1", "Descripción 1", BigDecimal.valueOf(100), "Categoría 1", 10, false),
                new ProductoDTO(2L, "Producto 2", "Descripción 2", BigDecimal.valueOf(200), "Categoría 2", 5, true)
        );
        when(dataServiceClient.obtenerTodosLosProductos()).thenReturn(productosEsperados);

        // Act → llamamos al metodo real del service
        List<ProductoDTO> resultado = productoBusinessService.obtenerTodosLosProductos();

        // Assert → verificamos resultados
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Producto 1", resultado.get(0).getNombre());
        verify(dataServiceClient).obtenerTodosLosProductos(); // se llamó al cliente
    }

    @Test
    void cuandoCalcularValorTotalInventario_entoncesRetornaSumaCorrecta() {
        // Arrange → simulamos 2 productos con precio y stock
        List<ProductoDTO> productos = Arrays.asList(
                new ProductoDTO(1L, "Producto 1", "Descripción", BigDecimal.valueOf(100), "Categoría 1", 2, false), // 100 * 2 = 200
                new ProductoDTO(2L, "Producto 2", "Descripción", BigDecimal.valueOf(50), "Categoría 2", 3, false)   // 50 * 3 = 150
        );
        when(dataServiceClient.obtenerTodosLosProductos()).thenReturn(productos);

        // Act
        BigDecimal total = productoBusinessService.calcularValorTotalInventario();

        // Assert → 200 + 150 = 350
        assertEquals(BigDecimal.valueOf(350), total);
    }

    @Test
    void cuandoCrearProductoConPrecioInvalido_entoncesLanzaExcepcion() {
        // Arrange → producto con precio negativo
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Producto Test");
        request.setPrecio(BigDecimal.valueOf(-10));
        request.setStock(5);

        // Act & Assert → esperamos excepción
        assertThrows(ValidacionNegocioException.class, () -> {
            productoBusinessService.crearProducto(request);
        });

        // Verificamos que JAMÁS se llamó al cliente
        verify(dataServiceClient, never()).crearProducto(any());
    }

    @Test
    void cuandoCrearProductoConStockNegativo_entoncesLanzaExcepcion() {
        // Arrange → producto con stock negativo
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

    @Test
    void cuandoObtenerProductoPorIdInexistente_entoncesLanzaProductoNoEncontradoException() {
        // Arrange → simulamos que el data-service responde con 404
        Long idInexistente = 99L;
        when(dataServiceClient.obtenerProductoPorId(idInexistente))
                .thenThrow(FeignException.NotFound.class);

        // Act & Assert
        assertThrows(ProductoNoEncontradoException.class, () -> {
            productoBusinessService.obtenerProductoPorId(idInexistente);
        });
    }

    @Test
    void cuandoObtenerTodosLosProductos_yDataServiceFalla_entoncesLanzaMicroserviceCommunicationException() {
        // Arrange → simulamos un error de comunicación
        when(dataServiceClient.obtenerTodosLosProductos())
                .thenThrow(FeignException.class);

        // Act & Assert
        assertThrows(MicroserviceCommunicationException.class, () -> {
            productoBusinessService.obtenerTodosLosProductos();
        });
    }
}
