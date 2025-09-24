package com.TP6.businessService.controller;

import com.TP6.businessService.dto.CategoriaDTO;
import com.TP6.businessService.dto.InventarioDTO;
import com.TP6.businessService.dto.ProductoDTO;
import com.TP6.businessService.dto.ProductoRequest;
import com.TP6.businessService.exception.MicroserviceCommunicationException;
import com.TP6.businessService.exception.ProductoNoEncontradoException;
import com.TP6.businessService.service.CategoriaBusinessService;
import com.TP6.businessService.service.InventarioBusinessService;
import com.TP6.businessService.service.ProductoBusinessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BusinessController.class)
@ActiveProfiles("test")
class BusinessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductoBusinessService productoBusinessService;

    @MockBean
    private CategoriaBusinessService categoriaBusinessService;

    @MockBean
    private InventarioBusinessService inventarioBusinessService;

    // ------------------- TESTS PRODUCTOS -------------------

    @Test
    void cuandoObtenerTodosLosProductos_entoncesRetornaLista() throws Exception {
        List<ProductoDTO> productos = Arrays.asList(
                new ProductoDTO(1L, "Coca Cola", "Bebida", BigDecimal.valueOf(100), "Bebidas", 10, false),
                new ProductoDTO(2L, "Pepsi", "Bebida", BigDecimal.valueOf(90), "Bebidas", 5, true)
        );

        when(productoBusinessService.obtenerTodosLosProductos()).thenReturn(productos);

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Coca Cola"));
    }

    @Test
    void cuandoCrearProducto_entoncesRetorna201() throws Exception {
        ProductoRequest request = new ProductoRequest("Coca Cola", "Bebida",
                BigDecimal.valueOf(100), 1L, 10, 2);

        ProductoDTO response = new ProductoDTO(1L, "Coca Cola", "Bebida",
                BigDecimal.valueOf(100), "Bebidas", 10, false);

        when(productoBusinessService.crearProducto(any(ProductoRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Coca Cola"))
                .andExpect(jsonPath("$.precio").value(100));
    }

    @Test
    void cuandoBuscarProductoInexistente_entoncesRetorna404() throws Exception {
        when(productoBusinessService.obtenerProductoPorId(999L))
                .thenThrow(new ProductoNoEncontradoException("Producto no encontrado"));

        mockMvc.perform(get("/api/productos/id/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cuandoDataServiceFalla_entoncesRetorna500() throws Exception {
        when(productoBusinessService.obtenerTodosLosProductos())
                .thenThrow(new MicroserviceCommunicationException("Error comunicación"));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isInternalServerError());
    }

    // ------------------- TESTS CATEGORÍAS -------------------

    @Test
    void cuandoObtenerTodasLasCategorias_entoncesRetornaLista() throws Exception {
        List<CategoriaDTO> categorias = Arrays.asList(
                new CategoriaDTO(1L, "Bebidas", "Productos líquidos"),
                new CategoriaDTO(2L, "Snacks", "Productos secos")
        );

        when(categoriaBusinessService.obtenerTodasLasCategorias()).thenReturn(categorias);

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].nombre").value("Snacks"));
    }

    @Test
    void cuandoCrearCategoria_entoncesRetorna201() throws Exception {
        CategoriaDTO categoria = new CategoriaDTO(1L, "Bebidas", "Productos líquidos");

        when(categoriaBusinessService.crearCategoria(any(CategoriaDTO.class))).thenReturn(categoria);

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Bebidas"));
    }

    // ------------------- TESTS INVENTARIOS -------------------

    @Test
    void cuandoObtenerTodosLosInventarios_entoncesRetornaLista() throws Exception {
        ProductoDTO producto = new ProductoDTO(1L, "Coca Cola", "Bebida",
                BigDecimal.valueOf(100), "Bebidas", 10, false);

        InventarioDTO inventario = new InventarioDTO(1L, producto, 10, 2, LocalDateTime.now());
        List<InventarioDTO> inventarios = List.of(inventario);

        when(inventarioBusinessService.obtenerTodosLosInventarios()).thenReturn(inventarios);

        mockMvc.perform(get("/api/inventario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].producto.nombre").value("Coca Cola"));
    }

    @Test
    void cuandoCrearInventario_entoncesRetorna201() throws Exception {
        ProductoDTO producto = new ProductoDTO(1L, "Coca Cola", "Bebida",
                BigDecimal.valueOf(100), "Bebidas", 10, false);

        InventarioDTO inventario = new InventarioDTO(1L, producto, 10, 2, LocalDateTime.now());

        when(inventarioBusinessService.crearInventario(any(InventarioDTO.class)))
                .thenReturn(inventario);

        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cantidad").value(10))
                .andExpect(jsonPath("$.producto.nombre").value("Coca Cola"));
    }

    // ------------------- TESTS REPORTES -------------------

    @Test
    void cuandoObtenerProductosConStockBajo_entoncesRetornaLista() throws Exception {
        List<ProductoDTO> productosBajos = List.of(
                new ProductoDTO(2L, "Pepsi", "Bebida", BigDecimal.valueOf(90), "Bebidas", 5, true)
        );

        when(productoBusinessService.obtenerProductosConStockBajo()).thenReturn(productosBajos);

        mockMvc.perform(get("/api/reportes/stock-bajo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Pepsi"));
    }

    @Test
    void cuandoObtenerValorTotalInventario_entoncesRetornaBigDecimal() throws Exception {
        when(productoBusinessService.calcularValorTotalInventario()).thenReturn(BigDecimal.valueOf(1500));

        mockMvc.perform(get("/api/reportes/valor-inventario"))
                .andExpect(status().isOk())
                .andExpect(content().string("1500"));
    }
}
