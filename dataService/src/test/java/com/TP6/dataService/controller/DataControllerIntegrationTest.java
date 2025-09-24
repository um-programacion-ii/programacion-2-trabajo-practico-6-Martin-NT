package com.TP6.dataService.controller;

import com.TP6.dataService.entity.Categoria;
import com.TP6.dataService.entity.Producto;
import com.TP6.dataService.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
class DataControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate; // Cliente HTTP de prueba

    @Autowired
    private ProductoService productoService; // Acceso directo al service para setup

    // ------------------- TESTS PRODUCTOS -------------------

    // Caso exitoso: crear producto y verificar que se persiste
    @Test
    void cuandoCrearProducto_entoncesSePersisteCorrectamente() {
        // Arrange → construimos producto de prueba
        Producto producto = new Producto();
        producto.setNombre("Producto Test");
        producto.setDescripcion("Descripción de prueba");
        producto.setPrecio(BigDecimal.valueOf(100.50));

        // Act → POST a /data/productos
        ResponseEntity<Producto> response = restTemplate.postForEntity(
                "/data/productos", producto, Producto.class);

        // Assert → verificamos respuesta y persistencia
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Producto Test", response.getBody().getNombre());
    }

    // Caso error: buscar producto inexistente devuelve 404
    @Test
    void cuandoBuscarProductoInexistente_entoncesRetorna404() {
        // Act → GET a un producto inexistente
        ResponseEntity<Producto> response = restTemplate.getForEntity(
                "/data/productos/id/999", Producto.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ------------------- TESTS CATEGORÍAS -------------------

    // Caso error: intentar crear categoría duplicada devuelve 409
    @Test
    void cuandoCrearCategoriaDuplicada_entoncesRetorna409() {
        // Arrange → definimos categoría de prueba
        Categoria categoria = new Categoria();
        categoria.setNombre("Electrónica");
        categoria.setDescripcion("Categoría de prueba");

        // Act → primera vez crea correctamente
        ResponseEntity<Categoria> response1 = restTemplate.postForEntity(
                "/data/categorias", categoria, Categoria.class);
        assertEquals(HttpStatus.CREATED, response1.getStatusCode());

        // Act → segunda vez debe fallar
        ResponseEntity<String> response2 = restTemplate.postForEntity(
                "/data/categorias", categoria, String.class);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());
    }

    // ------------------- TESTS INVENTARIO -------------------

    // Caso exitoso: consultar stock bajo devuelve lista (vacía o con datos)
    @Test
    void cuandoConsultarStockBajo_entoncesRetornaLista() {
        // Act → GET a /data/productos/stock-bajo
        ResponseEntity<Producto[]> response = restTemplate.getForEntity(
                "/data/productos/stock-bajo", Producto[].class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Si la BD está vacía en test, retorna lista vacía
        assertEquals(0, response.getBody().length);
    }
}
