package com.TP6.dataService.repository;
import com.TP6.dataService.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    // Buscar inventario por el id del producto
    Optional<Inventario> findByProductoId(Long productoId);

    // Buscar inventarios por cantidad exacta
    List<Inventario> findByCantidad(Integer cantidad);

    // Inventarios con stock bajo (cantidad <= stockMinimo)
    @Query("SELECT i FROM Inventario i WHERE i.cantidad <= COALESCE(i.stockMinimo, 0)")
    List<Inventario> findConStockBajo();

    // Inventarios con stock alto (cantidad > stockMinimo)
    @Query("SELECT i FROM Inventario i WHERE i.cantidad > COALESCE(i.stockMinimo, 0)")
    List<Inventario> findConStockAlto();
}
