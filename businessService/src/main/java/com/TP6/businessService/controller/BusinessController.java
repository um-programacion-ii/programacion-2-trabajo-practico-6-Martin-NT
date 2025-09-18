package com.TP6.businessService.controller;

import com.TP6.businessService.dto.CategoriaDTO;
import com.TP6.businessService.dto.InventarioDTO;
import com.TP6.businessService.dto.ProductoDTO;
import com.TP6.businessService.dto.ProductoRequest;
import com.TP6.businessService.service.CategoriaBusinessService;
import com.TP6.businessService.service.InventarioBusinessService;
import com.TP6.businessService.service.ProductoBusinessService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
public class BusinessController {

    private final ProductoBusinessService productoBusinessService;
    private final CategoriaBusinessService categoriaBusinessService;
    private final InventarioBusinessService inventarioBusinessService;

    public BusinessController(ProductoBusinessService productoBusinessService,
                              CategoriaBusinessService categoriaBusinessService,
                              InventarioBusinessService inventarioBusinessService) {
        this.productoBusinessService = productoBusinessService;
        this.categoriaBusinessService = categoriaBusinessService;
        this.inventarioBusinessService = inventarioBusinessService;
    }

    // ------------------- PRODUCTOS -------------------

    // Obtener todos los productos
    @GetMapping("/productos")
    public List<ProductoDTO> obtenerTodosLosProductos() {
        return productoBusinessService.obtenerTodosLosProductos();
    }

    // Obtener producto por ID
    @GetMapping("/productos/id/{id}")
    public ProductoDTO obtenerProductoPorId(@PathVariable Long id) {
        return productoBusinessService.obtenerProductoPorId(id);
    }

    // Obtener producto por nombre
    @GetMapping("/productos/nombre/{nombre}")
    public ProductoDTO obtenerProductoPorNombre(@PathVariable String nombre) {
        return productoBusinessService.obtenerProductoPorNombre(nombre);
    }

    // Obtener productos por precio exacto
    @GetMapping("/productos/precio/{precio}")
    public List<ProductoDTO> obtenerProductoPorPrecio(@PathVariable BigDecimal precio) {
        return productoBusinessService.obtenerProductosPorPrecio(precio);
    }

    // Crear un nuevo producto
    @PostMapping("/productos")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoDTO crearProducto(@Valid @RequestBody ProductoRequest request) {
        return productoBusinessService.crearProducto(request);
    }

    // Actualizar producto por ID
    @PutMapping("/productos/{id}")
    public ProductoDTO actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return productoBusinessService.actualizarProducto(id, request);
    }

    // Eliminar producto por ID
    @DeleteMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarProducto(@PathVariable Long id) {
        productoBusinessService.eliminarProducto(id);
    }

    // Obtener productos por categoría
    @GetMapping("/productos/categoria/{nombre}")
    public List<ProductoDTO> obtenerProductosPorCategoria(@PathVariable String nombre) {
        return productoBusinessService.obtenerProductosPorCategoria(nombre);
    }

// ------------------- CATEGORÍAS -------------------

    // Obtener todas las categorías
    @GetMapping("/categorias")
    public List<CategoriaDTO> obtenerTodasLasCategorias() {
        return categoriaBusinessService.obtenerTodasLasCategorias();
    }

    // Obtener categoría por ID
    @GetMapping("/categorias/{id}")
    public CategoriaDTO obtenerCategoriaPorId(@PathVariable Long id) {
        return categoriaBusinessService.obtenerCategoriaPorId(id);
    }

    // Obtener categoría por nombre
    @GetMapping("/categorias/nombre/{nombre}")
    public CategoriaDTO obtenerCategoriaPorNombre(@PathVariable String nombre) {
        return categoriaBusinessService.obtenerCategoriaPorNombre(nombre);
    }

    // Obtener categorías que tengan productos asociados
    @GetMapping("/categorias/con-productos")
    public List<CategoriaDTO> obtenerCategoriasConProductos() {
        return categoriaBusinessService.obtenerCategoriasConProductos();
    }

    // Crear una nueva categoría
    @PostMapping("/categorias")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaDTO crearCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO) {
        return categoriaBusinessService.crearCategoria(categoriaDTO);
    }

    // Actualizar categoría por ID
    @PutMapping("/categorias/{id}")
    public CategoriaDTO actualizarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaDTO categoriaDTO) {
        return categoriaBusinessService.actualizarCategoria(id, categoriaDTO);
    }

    // Eliminar categoría por ID
    @DeleteMapping("/categorias/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaBusinessService.eliminarCategoria(id);
    }

    // ------------------- INVENTARIO -------------------

    // Obtener todos los inventarios
    @GetMapping("/inventario")
    public List<InventarioDTO> obtenerTodosLosInventarios() {
        return inventarioBusinessService.obtenerTodosLosInventarios();
    }

    // Obtener inventario por ID
    @GetMapping("/inventario/{id}")
    public InventarioDTO obtenerInventarioPorId(@PathVariable Long id) {
        return inventarioBusinessService.obtenerInventarioPorId(id);
    }

    // Obtener inventario asociado a un producto
    @GetMapping("/inventario/producto/{productoId}")
    public InventarioDTO obtenerInventarioPorProducto(@PathVariable Long productoId) {
        return inventarioBusinessService.obtenerInventarioPorProducto(productoId);
    }

    // Obtener inventarios con cantidad exacta
    @GetMapping("/inventario/cantidad/{cantidad}")
    public List<InventarioDTO> obtenerInventariosPorCantidad(@PathVariable Integer cantidad) {
        return inventarioBusinessService.obtenerInventariosPorCantidad(cantidad);
    }

    // Obtener inventarios con stock bajo
    @GetMapping("/inventario/stock-bajo")
    public List<InventarioDTO> obtenerInventariosConStockBajo() {
        return inventarioBusinessService.obtenerInventariosConStockBajo();
    }

    // Obtener inventarios con stock alto
    @GetMapping("/inventario/stock-alto")
    public List<InventarioDTO> obtenerInventariosConStockAlto() {
        return inventarioBusinessService.obtenerInventariosConStockAlto();
    }

    // Crear un inventario
    @PostMapping("/inventario")
    @ResponseStatus(HttpStatus.CREATED)
    public InventarioDTO crearInventario(@Valid @RequestBody InventarioDTO inventarioDTO) {
        return inventarioBusinessService.crearInventario(inventarioDTO);
    }

    // Actualizar inventario
    @PutMapping("/inventario/{id}")
    public InventarioDTO actualizarInventario(@PathVariable Long id, @Valid @RequestBody InventarioDTO inventarioDTO) {
        return inventarioBusinessService.actualizarInventario(id, inventarioDTO);
    }

    // Eliminar inventario
    @DeleteMapping("/inventario/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarInventario(@PathVariable Long id) {
        inventarioBusinessService.eliminarInventario(id);
    }

    // ------------------- REPORTES -------------------

    // Obtener productos con stock bajo
    @GetMapping("/reportes/stock-bajo")
    public List<ProductoDTO> obtenerProductosConStockBajo() {
        return productoBusinessService.obtenerProductosConStockBajo();
    }

    // Obtener valor total del inventario
    @GetMapping("/reportes/valor-inventario")
    public BigDecimal obtenerValorTotalInventario() {
        return productoBusinessService.calcularValorTotalInventario();
    }
}
