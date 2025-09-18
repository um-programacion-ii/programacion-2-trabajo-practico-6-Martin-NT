package com.TP6.dataService.service;
import com.TP6.dataService.entity.Categoria;
import com.TP6.dataService.repository.CategoriaRepository;
import com.TP6.dataService.exception.CategoriaNoEncontradaException;
import com.TP6.dataService.exception.CategoriaYaExisteException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
