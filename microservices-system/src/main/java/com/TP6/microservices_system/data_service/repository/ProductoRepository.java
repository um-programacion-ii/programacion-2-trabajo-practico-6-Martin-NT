package com.TP6.microservices_system.data_service.repository;
import com.TP6.microservices_system.data_service.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Buscar producto por nombre exacto
    Optional<Producto> findByNombre(String nombre);

    // Buscar productos por precio exacto (puede haber más de uno con el mismo precio)
    List<Producto> findByPrecio(BigDecimal precio);

    // JPQL: Buscar productos por nombre de la categoría
    @Query("SELECT p FROM Producto p WHERE p.categoria.nombre = :nombreCategoria")
    List<Producto> findByNombreCategoria(@Param("nombreCategoria") String nombreCategoria);
}

