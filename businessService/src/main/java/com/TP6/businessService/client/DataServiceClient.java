package com.TP6.businessService.client;

import com.TP6.businessService.dto.CategoriaDTO;
import com.TP6.businessService.dto.InventarioDTO;
import com.TP6.businessService.dto.ProductoDTO;
import com.TP6.businessService.dto.ProductoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "data-service", url = "${data.service.url}")
public interface DataServiceClient {

    // Productos
    @GetMapping("/data/productos")
    List<ProductoDTO> obtenerTodosLosProductos();

    @GetMapping("/data/productos/id/{id}")
    ProductoDTO obtenerProductoPorId(@PathVariable Long id);

    @GetMapping("/data/productos/nombre/{nombre}")
    ProductoDTO obtenerProductoPorNombre(@PathVariable String nombre);

    @GetMapping("/data/productos/precio/{precio}")
    List<ProductoDTO> obtenerProductosPorPrecio(@PathVariable BigDecimal precio);

    @GetMapping("/data/productos/categoria/{nombre}")
    List<ProductoDTO> obtenerProductosPorCategoria(@PathVariable String nombre);

    @PostMapping("/data/productos")
    ProductoDTO crearProducto(@RequestBody ProductoRequest request);

    @PutMapping("/data/productos/{id}")
    ProductoDTO actualizarProducto(@PathVariable Long id, @RequestBody ProductoRequest request);

    @DeleteMapping("/data/productos/{id}")
    void eliminarProducto(@PathVariable Long id);

    @GetMapping("/data/productos/stock-bajo")
    List<ProductoDTO> obtenerProductosConStockBajo();

    // Categorías
    @GetMapping("/data/categorias")
    List<CategoriaDTO> obtenerTodasLasCategorias();

    @GetMapping("/data/categorias/id/{id}")
    CategoriaDTO obtenerCategoriaPorId(@PathVariable Long id);

    @GetMapping("/data/categorias/nombre/{nombre}")
    CategoriaDTO obtenerCategoriaPorNombre(@PathVariable String nombre);

    @PostMapping("/data/categorias")
    CategoriaDTO crearCategoria(@RequestBody CategoriaDTO categoriaDTO);

    @PutMapping("/data/categorias/{id}")
    CategoriaDTO actualizarCategoria(@PathVariable Long id, @RequestBody CategoriaDTO categoriaDTO);

    @DeleteMapping("/data/categorias/{id}")
    void eliminarCategoria(@PathVariable Long id);

    @GetMapping("/data/categorias/con-productos")
    List<CategoriaDTO> obtenerCategoriasConProductos();

    // Inventarios
    @GetMapping("/data/inventario/stock-bajo")
    List<InventarioDTO> obtenerInventariosConStockBajo();

    @GetMapping("/data/inventario/stock-alto")
    List<InventarioDTO> obtenerInventariosConStockAlto();

    @GetMapping("/data/inventario/{id}")
    InventarioDTO obtenerInventarioPorId(@PathVariable Long id);

    @GetMapping("/data/inventario/producto/{productoId}")
    InventarioDTO obtenerInventarioPorProducto(@PathVariable Long productoId);

    @GetMapping("/data/inventario/cantidad/{cantidad}")
    List<InventarioDTO> obtenerInventariosPorCantidad(@PathVariable Integer cantidad);

    @GetMapping("/data/inventario")
    List<InventarioDTO> obtenerTodosLosInventarios();

    @PostMapping("/data/inventario")
    InventarioDTO crearInventario(@RequestBody InventarioDTO inventarioDTO);

    @PutMapping("/data/inventario/{id}")
    InventarioDTO actualizarInventario(@PathVariable Long id, @RequestBody InventarioDTO inventarioDTO);

    @DeleteMapping("/data/inventario/{id}")
    void eliminarInventario(@PathVariable Long id);

}