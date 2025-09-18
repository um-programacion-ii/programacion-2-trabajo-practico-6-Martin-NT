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

    public Producto guardar(Producto producto) {
        if (productoRepository.findByNombre(producto.getNombre()).isPresent()) {
            throw new ProductoYaExisteException("El Producto " + producto.getNombre() + " ya existe");
        }
        return productoRepository.save(producto);
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() ->
                        new ProductoNoEncontradoException("El Producto con ID " + id + " no ha sido encontrado"));
    }

    public Producto buscarPorNombre(String nombre) {
        return productoRepository.findByNombre(nombre)
                .orElseThrow(() ->
                        new ProductoNoEncontradoException("El Producto '" + nombre + "' no ha sido encontrado"));
    }

    public List<Producto> buscarPorPrecio(BigDecimal precio) {
        return productoRepository.findByPrecio(precio);
    }

    public List<Producto> buscarPorCategoria(String nombreCategoria) {
        return productoRepository.findByNombreCategoria(nombreCategoria);
    }

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Producto actualizar(Long id, Producto producto) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNoEncontradoException("El Producto con ID: " + id + "no ha sido encontrado");
        }
        producto.setId(id);
        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNoEncontradoException("El Producto con ID: " + id + "no existe");
        }
        productoRepository.deleteById(id);
    }
}
