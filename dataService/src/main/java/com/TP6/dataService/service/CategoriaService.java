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

    // Guarda una nueva categoría en la base de datos
    // Valida que no exista previamente por nombre
    public Categoria guardar(Categoria categoria) {
        if (categoriaRepository.findByNombre(categoria.getNombre()).isPresent()) {
            throw new CategoriaYaExisteException("La Categoría " + categoria.getNombre() + " ya existe");
        }
        return categoriaRepository.save(categoria);
    }

    // Busca una categoría por su ID
    // Lanza excepción si no la encuentra
    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() ->
                        new CategoriaNoEncontradaException("La Categoría con ID " + id + " no ha sido encontrada"));
    }

    // Busca una categoría por su nombre
    // Lanza excepción si no la encuentra
    public Categoria buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre)
                .orElseThrow(() ->
                        new CategoriaNoEncontradaException("La Categoría '" + nombre + "' no ha sido encontrada"));
    }

    // Obtiene las categorías junto con sus productos asociados
    public List<Categoria> buscarCategoriasConProductos() {
        return categoriaRepository.findCategoriasConProductos();
    }

    // Obtiene todas las categorías de la base de datos
    public List<Categoria> obtenerTodos() {
        return categoriaRepository.findAll();
    }

    // Actualiza los datos de una categoría existente
    // Valida que la categoría exista por ID
    public Categoria actualizar(Long id, Categoria categoria) {
        if (!categoriaRepository.existsById(id)) {
            throw new CategoriaNoEncontradaException("La Categoría con ID: " + id + " no ha sido encontrada");
        }
        categoria.setId(id);
        return categoriaRepository.save(categoria);
    }

    // Elimina una categoría por su ID
    // Lanza excepción si la categoría no existe
    public void eliminar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new CategoriaNoEncontradaException("La Categoría con ID: " + id + " no existe");
        }
        categoriaRepository.deleteById(id);
    }
}
