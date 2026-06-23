package com.webproyecto.proyecto.repository;

import com.webproyecto.proyecto.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
    List<MovimientoInventario> findByProductoIdOrderByFechaDesc(Long productoId);
    List<MovimientoInventario> findAllByOrderByFechaDesc();
}
