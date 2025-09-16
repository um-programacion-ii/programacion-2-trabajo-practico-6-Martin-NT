package com.TP6.microservices_system.data_service.service;

import com.TP6.microservices_system.data_service.entity.Categoria;
import com.TP6.microservices_system.data_service.exception.CategoriaNoEncontradaException;
import com.TP6.microservices_system.data_service.exception.CategoriaYaExisteException;
import com.TP6.microservices_system.data_service.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria guardar(Categoria categoria) {
        if (categoriaRepository.findByNombre(categoria.getNombre()).isPresent()) {
            throw new CategoriaYaExisteException("La Categoría " + categoria.getNombre() + " ya existe");
        }
        return categoriaRepository.save(categoria);
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() ->
                        new CategoriaNoEncontradaException("La Categoría con ID " + id + " no ha sido encontrada"));
    }

    public Categoria buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre)
                .orElseThrow(() ->
                        new CategoriaNoEncontradaException("La Categoría '" + nombre + "' no ha sido encontrada"));
    }

    public List<Categoria> buscarCategoriasConProductos() {
        return categoriaRepository.findCategoriasConProductos();
    }

    public List<Categoria> obtenerTodos() {
        return categoriaRepository.findAll();
    }

    public Categoria actualizar(Long id, Categoria categoria) {
        if (!categoriaRepository.existsById(id)) {
            throw new CategoriaNoEncontradaException("La Categoría con ID: " + id + "no ha sido encontrada");
        }
        categoria.setId(id);
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new CategoriaNoEncontradaException("La Categoría con ID: " + id + "no existe");
        }
        categoriaRepository.deleteById(id);
    }
}
