package com.TP6.businessService.service;

import com.TP6.businessService.client.DataServiceClient;
import com.TP6.businessService.dto.ProductoDTO;
import com.TP6.businessService.dto.ProductoRequest;
import com.TP6.businessService.exception.MicroserviceCommunicationException;
import com.TP6.businessService.exception.ProductoNoEncontradoException;
import com.TP6.businessService.exception.ValidacionNegocioException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class ProductoBusinessService {

    private final DataServiceClient dataServiceClient;

    public ProductoBusinessService(DataServiceClient dataServiceClient) {
        this.dataServiceClient = dataServiceClient;
    }

    // Obtener todos los productos
    public List<ProductoDTO> obtenerTodosLosProductos() {
        try {
            return dataServiceClient.obtenerTodosLosProductos();
        } catch (FeignException e) {
            log.error("Error al obtener productos del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Obtener un producto por su ID
    public ProductoDTO obtenerProductoPorId(Long id) {
        try {
            return dataServiceClient.obtenerProductoPorId(id);
        } catch (FeignException.NotFound e) {
            throw new ProductoNoEncontradoException("Producto no encontrado con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al obtener producto del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Obtener un producto por su nombre
    public ProductoDTO obtenerProductoPorNombre(String nombre) {
        try {
            return dataServiceClient.obtenerProductoPorNombre(nombre);
        } catch (FeignException.NotFound e) {
            throw new ProductoNoEncontradoException("Producto no encontrado con nombre: " + nombre);
        } catch (FeignException e) {
            log.error("Error al obtener producto por nombre del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Obtener productos por precio exacto
    public List<ProductoDTO> obtenerProductosPorPrecio(BigDecimal precio) {
        try {
            return dataServiceClient.obtenerProductosPorPrecio(precio);
        } catch (FeignException e) {
            log.error("Error al obtener productos por precio del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Obtener productos por nombre de categoría
    public List<ProductoDTO> obtenerProductosPorCategoria(String nombreCategoria) {
        try {
            return dataServiceClient.obtenerProductosPorCategoria(nombreCategoria);
        } catch (FeignException e) {
            log.error("Error al obtener productos por categoría del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Crear un nuevo producto
    public ProductoDTO crearProducto(ProductoRequest request) {
        validarProducto(request);
        try {
            return dataServiceClient.crearProducto(request);
        } catch (FeignException e) {
            log.error("Error al crear producto en el data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Actualizar un producto existente por su ID
    public ProductoDTO actualizarProducto(Long id, ProductoRequest request) {
        validarProducto(request);
        try {
            return dataServiceClient.actualizarProducto(id, request);
        } catch (FeignException.NotFound e) {
            throw new ProductoNoEncontradoException("Producto no encontrado con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al actualizar producto en el data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Eliminar un producto existente por su ID
    public void eliminarProducto(Long id) {
        try {
            dataServiceClient.eliminarProducto(id);
        } catch (FeignException.NotFound e) {
            throw new ProductoNoEncontradoException("Producto no encontrado con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al eliminar producto en el data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Validaciones de negocio para productos
    private void validarProducto(ProductoRequest request) {
        if (request.getPrecio() == null || request.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacionNegocioException("El precio debe ser mayor a cero");
        }
        if (request.getStock() == null || request.getStock() < 0) {
            throw new ValidacionNegocioException("El stock no puede ser negativo");
        }
    }
}