package com.TP6.businessService.service;

import com.TP6.businessService.client.DataServiceClient;
import com.TP6.businessService.dto.CategoriaDTO;
import com.TP6.businessService.exception.CategoriaNoEncontradaException;
import com.TP6.businessService.exception.MicroserviceCommunicationException;
import com.TP6.businessService.exception.ValidacionNegocioException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoriaBusinessService {

    private final DataServiceClient dataServiceClient;

    public CategoriaBusinessService(DataServiceClient dataServiceClient) {
        this.dataServiceClient = dataServiceClient;
    }

    // Obtener todas las categorías
    public List<CategoriaDTO> obtenerTodasLasCategorias() {
        try {
            return dataServiceClient.obtenerTodasLasCategorias();
        } catch (FeignException e) {
            log.error("Error al obtener categorías del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Obtener una categoría por su ID
    public CategoriaDTO obtenerCategoriaPorId(Long id) {
        try {
            return dataServiceClient.obtenerCategoriaPorId(id);
        } catch (FeignException.NotFound e) {
            throw new CategoriaNoEncontradaException("Categoría no encontrada con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al obtener categoría del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Crear una nueva categoría
    public CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO) {
        validarCategoria(categoriaDTO);

        try {
            return dataServiceClient.crearCategoria(categoriaDTO);
        } catch (FeignException e) {
            log.error("Error al crear categoría en el data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Actualizar una categoría existente por su ID
    public CategoriaDTO actualizarCategoria(Long id, CategoriaDTO categoriaDTO) {
        validarCategoria(categoriaDTO);

        try {
            return dataServiceClient.actualizarCategoria(id, categoriaDTO);
        } catch (FeignException.NotFound e) {
            throw new CategoriaNoEncontradaException("Categoría no encontrada con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al actualizar categoría en el data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Eliminar una categoría por su ID
    public void eliminarCategoria(Long id) {
        try {
            dataServiceClient.eliminarCategoria(id);
        } catch (FeignException.NotFound e) {
            throw new CategoriaNoEncontradaException("Categoría no encontrada con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al eliminar categoría en el data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Obtener categorías que tengan al menos un producto
    public List<CategoriaDTO> obtenerCategoriasConProductos() {
        try {
            return dataServiceClient.obtenerCategoriasConProductos();
        } catch (FeignException e) {
            log.error("Error al obtener categorías con productos en el data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Obtener una categoría por su nombre
    public CategoriaDTO obtenerCategoriaPorNombre(String nombre) {
        try {
            return dataServiceClient.obtenerCategoriaPorNombre(nombre);
        } catch (FeignException.NotFound e) {
            throw new CategoriaNoEncontradaException("Categoría no encontrada con nombre: " + nombre);
        } catch (FeignException e) {
            log.error("Error al obtener categoría por nombre del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Validaciones de negocio para categorías
    private void validarCategoria(CategoriaDTO categoriaDTO) {
        if (categoriaDTO.getNombre() == null || categoriaDTO.getNombre().isBlank()) {
            throw new ValidacionNegocioException("El nombre de la categoría no puede estar vacío");
        }
    }
}
