package com.TP6.businessService.client;

import com.TP6.businessService.dto.CategoriaDTO;
import com.TP6.businessService.dto.InventarioDTO;
import com.TP6.businessService.dto.ProductoDTO;
import com.TP6.businessService.dto.ProductoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "data-service", url = "${data.service.url}")
public interface DataServiceClient {

    // Productos
    @GetMapping("/data/productos")
    List<ProductoDTO> obtenerTodosLosProductos();

    @GetMapping("/data/productos/{id}")
    ProductoDTO obtenerProductoPorId(@PathVariable Long id);

    @PostMapping("/data/productos")
    ProductoDTO crearProducto(@RequestBody ProductoRequest request);

    @PutMapping("/data/productos/{id}")
    ProductoDTO actualizarProducto(@PathVariable Long id, @RequestBody ProductoRequest request);

    @DeleteMapping("/data/productos/{id}")
    void eliminarProducto(@PathVariable Long id);

    // Categorias
    @GetMapping("/data/productos/categoria/{nombre}")
    List<ProductoDTO> obtenerProductosPorCategoria(@PathVariable String nombre);

    @GetMapping("/data/categorias")
    List<CategoriaDTO> obtenerTodasLasCategorias();

    // Inventarios
    @GetMapping("/data/inventario/stock-bajo")
    List<InventarioDTO> obtenerProductosConStockBajo();
}