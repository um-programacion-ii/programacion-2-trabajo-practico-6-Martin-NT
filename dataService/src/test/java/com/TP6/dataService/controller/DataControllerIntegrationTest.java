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
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductoService productoService;

    @Test
    void cuandoCrearProducto_entoncesSePersisteCorrectamente() {
        // Arrange
        Producto producto = new Producto();
        producto.setNombre("Producto Test");
        producto.setDescripcion("Descripción de prueba");
        producto.setPrecio(BigDecimal.valueOf(100.50));

        // Act
        ResponseEntity<Producto> response = restTemplate.postForEntity(
                "/data/productos", producto, Producto.class);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Producto Test", response.getBody().getNombre());
    }

    @Test
    void cuandoBuscarProductoInexistente_entoncesRetorna404() {
        // Act → corregido: endpoint es /productos/id/{id}
        ResponseEntity<Producto> response = restTemplate.getForEntity(
                "/data/productos/id/999", Producto.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void cuandoCrearCategoriaDuplicada_entoncesRetorna409() {
        // Arrange
        Categoria categoria = new Categoria();
        categoria.setNombre("Electrónica");
        categoria.setDescripcion("Categoría de prueba");

        // Act → primera vez crea
        ResponseEntity<Categoria> response1 = restTemplate.postForEntity(
                "/data/categorias", categoria, Categoria.class);
        assertEquals(HttpStatus.CREATED, response1.getStatusCode());

        // Act → segunda vez falla
        ResponseEntity<String> response2 = restTemplate.postForEntity(
                "/data/categorias", categoria, String.class);
        assertEquals(HttpStatus.CONFLICT, response2.getStatusCode());
    }

    @Test
    void cuandoConsultarStockBajo_entoncesRetornaLista() {
        // Act → GET devuelve una lista de productos
        ResponseEntity<Producto[]> response = restTemplate.getForEntity(
                "/data/productos/stock-bajo", Producto[].class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Si la BD está vacía en test, debería retornar lista vacía
        assertEquals(0, response.getBody().length);
    }
}
