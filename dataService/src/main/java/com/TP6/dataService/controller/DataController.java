package com.TP6.dataService.controller;

import com.TP6.dataService.entity.Categoria;
import com.TP6.dataService.entity.Inventario;
import com.TP6.dataService.entity.Producto;
import com.TP6.dataService.service.CategoriaService;
import com.TP6.dataService.service.InventarioService;
import com.TP6.dataService.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/data")
@Validated
public class DataController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final InventarioService inventarioService;

    public DataController(ProductoService productoService,
                          CategoriaService categoriaService,
                          InventarioService inventarioService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.inventarioService = inventarioService;
    }

    // ------------------- PRODUCTOS -------------------

    // Obtener todos los productos
    @GetMapping("/productos")
    public List<Producto> obtenerTodosLosProductos() {
        return productoService.obtenerTodos();
    }

    // Obtener producto por ID
    @GetMapping("/productos/id/{id}")
    public Producto obtenerProductoPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id);
    }

    // Obtener producto por nombre
    @GetMapping("/productos/nombre/{nombre}")
    public Producto obtenerProductoPorNombre(@PathVariable String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    // Obtener productos por precio exacto
    @GetMapping("/productos/precio/{precio}")
    public List<Producto> obtenerProductoPorPrecio(@PathVariable BigDecimal precio) {
        return productoService.buscarPorPrecio(precio);
    }

    // Obtener productos de una categoría por nombre de categoría
    @GetMapping("/productos/categoria/{nombre}")
    public List<Producto> obtenerProductosPorCategoria(@PathVariable String nombre) {
        return productoService.buscarPorCategoria(nombre);
    }

    // Crear un nuevo producto
    @PostMapping("/productos")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crearProducto(@Valid @RequestBody Producto producto) {
        return productoService.guardar(producto);
    }

    // Actualizar un producto existente por ID
    @PutMapping("/productos/{id}")
    public Producto actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        return productoService.actualizar(id, producto);
    }

    // Eliminar un producto por ID
    @DeleteMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminar(id);
    }

    // Obtener productos con stock bajo (comparación contra stock mínimo)
    @GetMapping("/productos/stock-bajo")
    public List<Producto> obtenerProductosConStockBajo() {
        return productoService.obtenerTodos()
                .stream()
                .filter(p -> p.getInventario() != null
                        && p.getInventario().getCantidad() <= p.getInventario().getStockMinimo())
                .toList();
    }

    // ------------------- CATEGORÍAS -------------------

    // Obtener todas las categorías
    @GetMapping("/categorias")
    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaService.obtenerTodos();
    }

    // Obtener categoría por ID
    @GetMapping("/categorias/id/{id}")
    public Categoria obtenerCategoriaPorId(@PathVariable Long id) {
        return categoriaService.buscarPorId(id);
    }

    // Obtener categoría por nombre
    @GetMapping("/categorias/nombre/{nombre}")
    public Categoria obtenerCategoriaPorNombre(@PathVariable String nombre) {
        return categoriaService.buscarPorNombre(nombre);
    }

    // Obtener categorías que tienen productos asociados
    @GetMapping("/categorias/con-productos")
    public List<Categoria> obtenerCategoriasConProductos() {
        return categoriaService.buscarCategoriasConProductos();
    }

    // Crear una nueva categoría
    @PostMapping("/categorias")
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria crearCategoria(@Valid @RequestBody Categoria categoria) {
        return categoriaService.guardar(categoria);
    }

    // Actualizar una categoría existente por ID
    @PutMapping("/categorias/{id}")
    public Categoria actualizarCategoria(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        return categoriaService.actualizar(id, categoria);
    }

    // Eliminar una categoría por ID
    @DeleteMapping("/categorias/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminar(id);
    }

    // ------------------- INVENTARIO -------------------

    // Obtener todos los inventarios
    @GetMapping("/inventario")
    public List<Inventario> obtenerTodosLosInventarios() {
        return inventarioService.obtenerTodos();
    }

    // Obtener inventario por ID
    @GetMapping("/inventario/{id}")
    public Inventario obtenerInventarioPorId(@PathVariable Long id) {
        return inventarioService.buscarPorId(id);
    }

    // Obtener inventario asociado a un producto por ID de producto
    @GetMapping("/inventario/producto/{productoId}")
    public Inventario obtenerInventarioPorProducto(@PathVariable Long productoId) {
        return inventarioService.buscarPorProducto(productoId);
    }

    // Obtener inventarios con una cantidad exacta
    @GetMapping("/inventario/cantidad/{cantidad}")
    public List<Inventario> obtenerInventariosPorCantidad(@PathVariable Integer cantidad) {
        return inventarioService.buscarPorCantidad(cantidad);
    }

    // Obtener inventarios con stock bajo
    @GetMapping("/inventario/stock-bajo")
    public List<Inventario> obtenerInventariosConStockBajo() {
        return inventarioService.buscarConStockBajo();
    }

    // Obtener inventarios con stock alto
    @GetMapping("/inventario/stock-alto")
    public List<Inventario> obtenerInventariosConStockAlto() {
        return inventarioService.buscarConStockAlto();
    }

    // Crear un nuevo inventario
    @PostMapping("/inventario")
    @ResponseStatus(HttpStatus.CREATED)
    public Inventario crearInventario(@Valid @RequestBody Inventario inventario) {
        return inventarioService.guardar(inventario);
    }

    // Actualizar un inventario existente por ID
    @PutMapping("/inventario/{id}")
    public Inventario actualizarInventario(@PathVariable Long id, @Valid @RequestBody Inventario inventario) {
        return inventarioService.actualizar(id, inventario);
    }

    // Eliminar un inventario por ID
    @DeleteMapping("/inventario/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarInventario(@PathVariable Long id) {
        inventarioService.eliminar(id);
    }
}
