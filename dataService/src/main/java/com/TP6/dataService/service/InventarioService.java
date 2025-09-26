package com.TP6.dataService.service;

import com.TP6.dataService.entity.Inventario;
import com.TP6.dataService.exception.InventarioNoEncontradoException;
import com.TP6.dataService.repository.InventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class InventarioService {
    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    // Guarda un nuevo inventario en la base de datos
    public Inventario guardar(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    // Busca un inventario por su ID
    // Lanza excepción si no existe
    public Inventario buscarPorId(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() ->
                        new InventarioNoEncontradoException("El Inventario con ID " + id + " no ha sido encontrado"));
    }

    // Busca el inventario asociado a un producto por su ID
    // Lanza excepción si no se encuentra
    public Inventario buscarPorProducto(Long productoId) {
        return inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() ->
                        new InventarioNoEncontradoException("Inventario no encontrado para el producto con ID " + productoId));
    }

    // Devuelve todos los inventarios que tienen una cantidad exacta
    public List<Inventario> buscarPorCantidad(Integer cantidad) {
        return inventarioRepository.findByCantidad(cantidad);
    }

    // Devuelve inventarios con stock menor o igual al stock mínimo configurado
    public List<Inventario> buscarConStockBajo() {
        return inventarioRepository.findConStockBajo();
    }

    // Devuelve inventarios con stock mayor al stock mínimo configurado
    public List<Inventario> buscarConStockAlto() {
        return inventarioRepository.findConStockAlto();
    }

    // Devuelve todos los inventarios registrados
    public List<Inventario> obtenerTodos() {
        return inventarioRepository.findAll();
    }

    // Actualiza un inventario existente
    // Valida que exista por ID y actualiza la fecha de modificación
    public Inventario actualizar(Long id, Inventario inventario) {
        if (!inventarioRepository.existsById(id)) {
            throw new InventarioNoEncontradoException("El Inventario con ID " + id + " no ha sido encontrado");
        }
        inventario.setId(id);
        inventario.setFechaActualizacion(LocalDateTime.now());
        return inventarioRepository.save(inventario);
    }

    // Elimina un inventario existente por su ID
    // Lanza excepción si no existe
    public void eliminar(Long id) {
        if (!inventarioRepository.existsById(id)) {
            throw new InventarioNoEncontradoException("El Inventario con ID " + id + " no existe");
        }
        inventarioRepository.deleteById(id);
    }
}
