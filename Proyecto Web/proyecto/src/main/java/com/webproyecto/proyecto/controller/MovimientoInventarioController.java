package com.webproyecto.proyecto.controller;

import com.webproyecto.proyecto.model.MovimientoInventario;
import com.webproyecto.proyecto.model.TipoMovimiento;
import com.webproyecto.proyecto.service.MovimientoInventarioService;
import com.webproyecto.proyecto.service.ProductoService;
import com.webproyecto.proyecto.service.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/movimientos")
public class MovimientoInventarioController {

    @Autowired
    private MovimientoInventarioService movimientoService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private SucursalService sucursalService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("movimientos", movimientoService.findAll());
        return "movimientos/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("sucursales", sucursalService.findAll());
        model.addAttribute("tipos", TipoMovimiento.values());
        return "movimientos/formulario";
    }

    @PostMapping
    public String registrar(@RequestParam Long productoId,
                            @RequestParam Long sucursalId,
                            @RequestParam String tipoMovimiento,
                            @RequestParam Integer cantidad,
                            @RequestParam(required = false) String observacion,
                            Model model) {
        try {
            MovimientoInventario movimiento = new MovimientoInventario();
            movimiento.setProducto(productoService.findById(productoId)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado")));
            movimiento.setSucursal(sucursalService.findById(sucursalId)
                    .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada")));
            movimiento.setTipoMovimiento(TipoMovimiento.valueOf(tipoMovimiento));
            movimiento.setCantidad(cantidad);
            movimiento.setObservacion(observacion != null ? observacion.trim() : "");

            movimientoService.registrar(movimiento);
            return "redirect:/movimientos";

        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("productos", productoService.findAll());
            model.addAttribute("sucursales", sucursalService.findAll());
            model.addAttribute("tipos", TipoMovimiento.values());
            return "movimientos/formulario";
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar movimiento: " + e.getMessage());
            model.addAttribute("productos", productoService.findAll());
            model.addAttribute("sucursales", sucursalService.findAll());
            model.addAttribute("tipos", TipoMovimiento.values());
            return "movimientos/formulario";
        }
    }
}
