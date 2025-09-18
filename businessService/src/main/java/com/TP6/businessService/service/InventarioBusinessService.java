package com.TP6.businessService.service;

import com.TP6.businessService.client.DataServiceClient;
import com.TP6.businessService.dto.InventarioDTO;
import com.TP6.businessService.exception.InventarioNoEncontradoException;
import com.TP6.businessService.exception.MicroserviceCommunicationException;
import com.TP6.businessService.exception.ValidacionNegocioException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class InventarioBusinessService {

    private final DataServiceClient dataServiceClient;

    public InventarioBusinessService(DataServiceClient dataServiceClient) {
        this.dataServiceClient = dataServiceClient;
    }

    // Obtener todos los inventarios
    public List<InventarioDTO> obtenerTodosLosInventarios() {
        try {
            return dataServiceClient.obtenerTodosLosInventarios();
        } catch (FeignException e) {
            log.error("Error al obtener inventarios del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Obtener inventario por ID
    public InventarioDTO obtenerInventarioPorId(Long id) {
        try {
            return dataServiceClient.obtenerInventarioPorId(id);
        } catch (FeignException.NotFound e) {
            throw new InventarioNoEncontradoException("Inventario no encontrado con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al obtener inventario del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Obtener inventario por producto
    public InventarioDTO obtenerInventarioPorProducto(Long productoId) {
        try {
            return dataServiceClient.obtenerInventarioPorProducto(productoId);
        } catch (FeignException.NotFound e) {
            throw new InventarioNoEncontradoException("Inventario no encontrado para producto ID: " + productoId);
        } catch (FeignException e) {
            log.error("Error al obtener inventario por producto del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Obtener inventarios por cantidad exacta
    public List<InventarioDTO> obtenerInventariosPorCantidad(Integer cantidad) {
        try {
            return dataServiceClient.obtenerInventariosPorCantidad(cantidad);
        } catch (FeignException e) {
            log.error("Error al obtener inventarios por cantidad del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Obtener inventarios con stock bajo
    public List<InventarioDTO> obtenerInventariosConStockBajo() {
        try {
            return dataServiceClient.obtenerInventariosConStockBajo();
        } catch (FeignException e) {
            log.error("Error al obtener inventarios con stock bajo del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Obtener inventarios con stock alto
    public List<InventarioDTO> obtenerInventariosConStockAlto() {
        try {
            return dataServiceClient.obtenerInventariosConStockAlto();
        } catch (FeignException e) {
            log.error("Error al obtener inventarios con stock alto del data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Crear inventario
    public InventarioDTO crearInventario(InventarioDTO inventarioDTO) {
        validarInventario(inventarioDTO);
        try {
            return dataServiceClient.crearInventario(inventarioDTO);
        } catch (FeignException e) {
            log.error("Error al crear inventario en el data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Actualizar inventario
    public InventarioDTO actualizarInventario(Long id, InventarioDTO inventarioDTO) {
        validarInventario(inventarioDTO);
        try {
            return dataServiceClient.actualizarInventario(id, inventarioDTO);
        } catch (FeignException.NotFound e) {
            throw new InventarioNoEncontradoException("Inventario no encontrado con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al actualizar inventario en el data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Eliminar inventario
    public void eliminarInventario(Long id) {
        try {
            dataServiceClient.eliminarInventario(id);
        } catch (FeignException.NotFound e) {
            throw new InventarioNoEncontradoException("Inventario no encontrado con ID: " + id);
        } catch (FeignException e) {
            log.error("Error al eliminar inventario en el data-service", e);
            throw new MicroserviceCommunicationException("Error de comunicación con el servicio de datos");
        }
    }

    // Validaciones de negocio
    private void validarInventario(InventarioDTO inventarioDTO) {
        if (inventarioDTO.getCantidad() != null && inventarioDTO.getCantidad() < 0) {
            throw new ValidacionNegocioException("La cantidad no puede ser negativa");
        }
        if (inventarioDTO.getStockMinimo() != null && inventarioDTO.getStockMinimo() < 0) {
            throw new ValidacionNegocioException("El stock mínimo no puede ser negativo");
        }
    }
}
