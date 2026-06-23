package com.webproyecto.proyecto.service;

import com.webproyecto.proyecto.model.MovimientoInventario;
import com.webproyecto.proyecto.model.Producto;
import com.webproyecto.proyecto.model.TipoMovimiento;
import com.webproyecto.proyecto.repository.MovimientoInventarioRepository;
import com.webproyecto.proyecto.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MovimientoInventarioService {

    @Autowired
    private MovimientoInventarioRepository movimientoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<MovimientoInventario> findAll() {
        return movimientoRepository.findAllByOrderByFechaDesc();
    }

    public Optional<MovimientoInventario> findById(Long id) {
        return movimientoRepository.findById(id);
    }

    public List<MovimientoInventario> findByProducto(Long productoId) {
        return movimientoRepository.findByProductoIdOrderByFechaDesc(productoId);
    }

    @Transactional
    public MovimientoInventario registrar(MovimientoInventario movimiento) {
        if (movimiento.getFecha() == null) {
            movimiento.setFecha(LocalDateTime.now());
        }

        Producto producto = movimiento.getProducto();
        int cantidad = movimiento.getCantidad();

        switch (movimiento.getTipoMovimiento()) {
            case ENTRADA:
                producto.setCantidad(producto.getCantidad() + cantidad);
                break;
            case SALIDA:
                if (producto.getCantidad() < cantidad) {
                    throw new IllegalStateException(
                        "Stock insuficiente. Disponible: " + producto.getCantidad() + ", solicitado: " + cantidad);
                }
                producto.setCantidad(producto.getCantidad() - cantidad);
                break;
            case AJUSTE:
                producto.setCantidad(cantidad);
                break;
        }

        productoRepository.save(producto);
        return movimientoRepository.save(movimiento);
    }

    public void deleteById(Long id) {
        movimientoRepository.deleteById(id);
    }
}
