package com.TP6.businessService.client;

import com.TP6.businessService.dto.CategoriaDTO;
import com.TP6.businessService.dto.InventarioDTO;
import com.TP6.businessService.dto.ProductoDTO;
import com.TP6.businessService.dto.ProductoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@EnableFeignClients(clients = DataServiceClient.class)
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "data.service.url=http://localhost:${wiremock.server.port}",
        "logging.level.feign=DEBUG",
        "feign.client.config.default.loggerLevel=FULL"
})
class DataServiceClientIntegrationTest {

    @Autowired private DataServiceClient dataServiceClient;
    @Autowired private ObjectMapper objectMapper;

    // ------------------- PRODUCTOS -------------------

    // Caso exitoso: obtiene un producto por ID
    @Test
    void obtenerProductoPorId_cuando200_devuelveDTO() throws Exception {
        var dto = new ProductoDTO(1L, "Coca Cola", "Bebida",
                BigDecimal.valueOf(100), "Bebidas", 10, false);
        var json = objectMapper.writeValueAsString(dto);

        // Simula respuesta del data-service
        stubFor(get(urlPathEqualTo("/data/productos/id/1"))
                .willReturn(okJson(json)));

        var res = dataServiceClient.obtenerProductoPorId(1L);

        assertNotNull(res);
        assertEquals("Coca Cola", res.getNombre());
        verify(getRequestedFor(urlPathEqualTo("/data/productos/id/1")));
    }

    // Caso de error: producto no encontrado (404)
    @Test
    void obtenerProductoPorId_cuando404_lanzaFeignNotFound() {
        stubFor(get(urlPathEqualTo("/data/productos/id/999"))
                .willReturn(aResponse().withStatus(404).withBody("Producto no encontrado")));

        var ex = assertThrows(FeignException.NotFound.class,
                () -> dataServiceClient.obtenerProductoPorId(999L));

        assertTrue(ex.contentUTF8().contains("Producto no encontrado"));
        verify(getRequestedFor(urlPathEqualTo("/data/productos/id/999")));
    }

    // Caso exitoso: crear un producto
    @Test
    void crearProducto_enviaJsonYDevuelveDTO() throws Exception {
        var req = new ProductoRequest("Notebook", "14 pulgadas",
                BigDecimal.valueOf(999.99), 5L, 20, 3);
        var resp = new ProductoDTO(100L, req.getNombre(), req.getDescripcion(),
                req.getPrecio(), "Computación", req.getStock(), false);

        var reqJson = objectMapper.writeValueAsString(req);
        var respJson = objectMapper.writeValueAsString(resp);

        stubFor(post(urlPathEqualTo("/data/productos"))
                .withRequestBody(equalToJson(reqJson, true, true))
                .willReturn(okJson(respJson)));

        var creado = dataServiceClient.crearProducto(req);

        assertEquals(100L, creado.getId());
        assertEquals("Notebook", creado.getNombre());
        assertEquals("Computación", creado.getCategoriaNombre());
    }

    // ------------------- CATEGORÍAS -------------------

    // Caso exitoso: obtiene categoría por ID
    @Test
    void obtenerCategoriaPorId_cuando200_devuelveDTO() throws Exception {
        var dto = new CategoriaDTO(1L, "Bebidas", "Líquidos");
        var json = objectMapper.writeValueAsString(dto);

        stubFor(get(urlPathEqualTo("/data/categorias/id/1"))
                .willReturn(okJson(json)));

        var res = dataServiceClient.obtenerCategoriaPorId(1L);

        assertNotNull(res);
        assertEquals("Bebidas", res.getNombre());
        verify(getRequestedFor(urlPathEqualTo("/data/categorias/id/1")));
    }

    // Caso exitoso: crear categoría
    @Test
    void crearCategoria_enviaJsonYDevuelveDTO() throws Exception {
        var req = new CategoriaDTO(null, "Snacks", "Productos secos");
        var resp = new CategoriaDTO(2L, "Snacks", "Productos secos");

        var reqJson = objectMapper.writeValueAsString(req);
        var respJson = objectMapper.writeValueAsString(resp);

        stubFor(post(urlPathEqualTo("/data/categorias"))
                .withRequestBody(equalToJson(reqJson, true, true))
                .willReturn(okJson(respJson)));

        var creado = dataServiceClient.crearCategoria(req);

        assertEquals(2L, creado.getId());
        assertEquals("Snacks", creado.getNombre());
    }

    // ------------------- INVENTARIO -------------------

    // Caso exitoso: obtiene inventario por ID
    @Test
    void obtenerInventarioPorId_cuando200_devuelveDTO() throws Exception {
        var producto = new ProductoDTO(1L, "Coca Cola", "Bebida",
                BigDecimal.valueOf(100), "Bebidas", 10, false);
        var dto = new InventarioDTO(1L, producto, 10, 2, LocalDateTime.now());
        var json = objectMapper.writeValueAsString(dto);

        stubFor(get(urlPathEqualTo("/data/inventario/1"))
                .willReturn(okJson(json)));

        var res = dataServiceClient.obtenerInventarioPorId(1L);

        assertNotNull(res);
        assertEquals(10, res.getCantidad());
        assertEquals("Coca Cola", res.getProducto().getNombre());
    }

    // Caso exitoso: crear inventario
    @Test
    void crearInventario_enviaJsonYDevuelveDTO() throws Exception {
        var producto = new ProductoDTO(1L, "Coca Cola", "Bebida",
                BigDecimal.valueOf(100), "Bebidas", 10, false);
        var req = new InventarioDTO(null, producto, 5, 1, null);
        var resp = new InventarioDTO(99L, producto, 5, 1, LocalDateTime.now());

        var reqJson = objectMapper.writeValueAsString(req);
        var respJson = objectMapper.writeValueAsString(resp);

        stubFor(post(urlPathEqualTo("/data/inventario"))
                .withRequestBody(equalToJson(reqJson, true, true))
                .willReturn(okJson(respJson)));

        var creado = dataServiceClient.crearInventario(req);

        assertEquals(99L, creado.getId());
        assertEquals(5, creado.getCantidad());
    }
}
