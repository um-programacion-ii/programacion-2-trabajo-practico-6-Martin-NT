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

    // ---------- PRODUCTOS ----------

    // Obtiene todos los productos
    @GetMapping("/data/productos")
    List<ProductoDTO> obtenerTodosLosProductos();

    // Obtiene un producto por su ID
    @GetMapping("/data/productos/id/{id}")
    ProductoDTO obtenerProductoPorId(@PathVariable Long id);

    // Obtiene un producto por su nombre
    @GetMapping("/data/productos/nombre/{nombre}")
    ProductoDTO obtenerProductoPorNombre(@PathVariable String nombre);

    // Obtiene productos con un precio exacto
    @GetMapping("/data/productos/precio/{precio}")
    List<ProductoDTO> obtenerProductosPorPrecio(@PathVariable BigDecimal precio);

    // Obtiene productos filtrados por nombre de categoría
    @GetMapping("/data/productos/categoria/{nombre}")
    List<ProductoDTO> obtenerProductosPorCategoria(@PathVariable String nombre);

    // Crea un nuevo producto
    @PostMapping("/data/productos")
    ProductoDTO crearProducto(@RequestBody ProductoRequest request);

    // Actualiza un producto existente por ID
    @PutMapping("/data/productos/{id}")
    ProductoDTO actualizarProducto(@PathVariable Long id, @RequestBody ProductoRequest request);

    // Elimina un producto por ID
    @DeleteMapping("/data/productos/{id}")
    void eliminarProducto(@PathVariable Long id);

    // Obtiene productos cuyo stock es menor o igual al mínimo
    @GetMapping("/data/productos/stock-bajo")
    List<ProductoDTO> obtenerProductosConStockBajo();


    // ---------- CATEGORÍAS ----------

    // Obtiene todas las categorías
    @GetMapping("/data/categorias")
    List<CategoriaDTO> obtenerTodasLasCategorias();

    // Obtiene una categoría por su ID
    @GetMapping("/data/categorias/id/{id}")
    CategoriaDTO obtenerCategoriaPorId(@PathVariable Long id);

    // Obtiene una categoría por su nombre
    @GetMapping("/data/categorias/nombre/{nombre}")
    CategoriaDTO obtenerCategoriaPorNombre(@PathVariable String nombre);

    // Crea una nueva categoría
    @PostMapping("/data/categorias")
    CategoriaDTO crearCategoria(@RequestBody CategoriaDTO categoriaDTO);

    // Actualiza una categoría existente por ID
    @PutMapping("/data/categorias/{id}")
    CategoriaDTO actualizarCategoria(@PathVariable Long id, @RequestBody CategoriaDTO categoriaDTO);

    // Elimina una categoría por ID
    @DeleteMapping("/data/categorias/{id}")
    void eliminarCategoria(@PathVariable Long id);

    // Obtiene todas las categorías junto con sus productos
    @GetMapping("/data/categorias/con-productos")
    List<CategoriaDTO> obtenerCategoriasConProductos();


    // ---------- INVENTARIOS ----------

    // Obtiene inventarios cuyo stock es menor o igual al mínimo
    @GetMapping("/data/inventario/stock-bajo")
    List<InventarioDTO> obtenerInventariosConStockBajo();

    // Obtiene inventarios cuyo stock es mayor al mínimo
    @GetMapping("/data/inventario/stock-alto")
    List<InventarioDTO> obtenerInventariosConStockAlto();

    // Obtiene un inventario por su ID
    @GetMapping("/data/inventario/{id}")
    InventarioDTO obtenerInventarioPorId(@PathVariable Long id);

    // Obtiene el inventario asociado a un producto
    @GetMapping("/data/inventario/producto/{productoId}")
    InventarioDTO obtenerInventarioPorProducto(@PathVariable Long productoId);

    // Obtiene inventarios que tienen una cantidad exacta
    @GetMapping("/data/inventario/cantidad/{cantidad}")
    List<InventarioDTO> obtenerInventariosPorCantidad(@PathVariable Integer cantidad);

    // Obtiene todos los inventarios registrados
    @GetMapping("/data/inventario")
    List<InventarioDTO> obtenerTodosLosInventarios();

    // Crea un nuevo inventario
    @PostMapping("/data/inventario")
    InventarioDTO crearInventario(@RequestBody InventarioDTO inventarioDTO);

    // Actualiza un inventario existente por ID
    @PutMapping("/data/inventario/{id}")
    InventarioDTO actualizarInventario(@PathVariable Long id, @RequestBody InventarioDTO inventarioDTO);

    // Elimina un inventario por ID
    @DeleteMapping("/data/inventario/{id}")
    void eliminarInventario(@PathVariable Long id);
}