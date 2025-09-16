package com.TP6.microservices_system.data_service.repository;
import com.TP6.microservices_system.data_service.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Buscar categoría por nombre exacto
    Optional<Categoria> findByNombre(String nombre);

    // JPQL: categorías que tienen al menos un producto asociado
    @Query("SELECT c FROM Categoria c WHERE SIZE(c.productos) > 0")
    List<Categoria> findCategoriasConProductos();
}
