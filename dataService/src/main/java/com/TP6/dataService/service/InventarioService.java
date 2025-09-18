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
    public Inventario buscarPorId(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() ->
                        new InventarioNoEncontradoException("El Inventario con ID " + id + " no ha sido encontrado"));
    }

    // Busca el inventario asociado a un producto por su ID
    public Inventario buscarPorProducto(Long productoId) {
        return inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() ->
                        new InventarioNoEncontradoException("Inventario no encontrado para el producto con ID " + productoId));
    }

    // Devuelve todos los inventarios con una cantidad exacta
    public List<Inventario> buscarPorCantidad(Integer cantidad) {
        return inventarioRepository.findByCantidad(cantidad);
    }

    // Devuelve inventarios cuyo stock es menor o igual al stock mínimo configurado
    public List<Inventario> buscarConStockBajo() {
        return inventarioRepository.findConStockBajo();
    }

    // Devuelve inventarios cuyo stock es mayor al stock mínimo configurado
    public List<Inventario> buscarConStockAlto() {
        return inventarioRepository.findConStockAlto();
    }

    // Devuelve todos los inventarios registrados
    public List<Inventario> obtenerTodos() {
        return inventarioRepository.findAll();
    }

    // Actualiza un inventario existente (sobrescribe datos con el ID dado)
    public Inventario actualizar(Long id, Inventario inventario) {
        if (!inventarioRepository.existsById(id)) {
            throw new InventarioNoEncontradoException("El Inventario con ID " + id + " no ha sido encontrado");
        }
        inventario.setId(id);
        inventario.setFechaActualizacion(LocalDateTime.now());
        return inventarioRepository.save(inventario);
    }

    // Elimina un inventario existente por su ID
    public void eliminar(Long id) {
        if (!inventarioRepository.existsById(id)) {
            throw new InventarioNoEncontradoException("El Inventario con ID " + id + " no existe");
        }
        inventarioRepository.deleteById(id);
    }
}
