package com.TP6.dataService.service;

import com.TP6.dataService.entity.Producto;
import com.TP6.dataService.exception.ProductoNoEncontradoException;
import com.TP6.dataService.exception.ProductoYaExisteException;
import com.TP6.dataService.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProductoService {
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Guarda un nuevo producto en la base de datos
    // Valida que no exista previamente por nombre
    public Producto guardar(Producto producto) {
        if (productoRepository.findByNombre(producto.getNombre()).isPresent()) {
            throw new ProductoYaExisteException("El Producto " + producto.getNombre() + " ya existe");
        }
        return productoRepository.save(producto);
    }

    // Busca un producto por su ID
    // Lanza excepción si no existe
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() ->
                        new ProductoNoEncontradoException("El Producto con ID " + id + " no ha sido encontrado"));
    }

    // Busca un producto por su nombre
    // Lanza excepción si no existe
    public Producto buscarPorNombre(String nombre) {
        return productoRepository.findByNombre(nombre)
                .orElseThrow(() ->
                        new ProductoNoEncontradoException("El Producto '" + nombre + "' no ha sido encontrado"));
    }

    // Devuelve todos los productos que tienen un precio exacto
    public List<Producto> buscarPorPrecio(BigDecimal precio) {
        return productoRepository.findByPrecio(precio);
    }

    // Devuelve los productos que pertenecen a una categoría específica
    public List<Producto> buscarPorCategoria(String nombreCategoria) {
        return productoRepository.findByNombreCategoria(nombreCategoria);
    }

    // Devuelve todos los productos registrados en la base de datos
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    // Actualiza los datos de un producto existente
    // Valida que el producto exista por ID
    public Producto actualizar(Long id, Producto producto) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNoEncontradoException("El Producto con ID: " + id + " no ha sido encontrado");
        }
        producto.setId(id);
        return productoRepository.save(producto);
    }

    // Elimina un producto por su ID
    // Lanza excepción si no existe
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNoEncontradoException("El Producto con ID: " + id + " no existe");
        }
        productoRepository.deleteById(id);
    }
}
