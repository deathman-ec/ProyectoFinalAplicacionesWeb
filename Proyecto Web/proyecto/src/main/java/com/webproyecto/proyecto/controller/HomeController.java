package com.webproyecto.proyecto.controller;

import com.webproyecto.proyecto.service.CategoriaService;
import com.webproyecto.proyecto.service.MovimientoInventarioService;
import com.webproyecto.proyecto.service.ProductoService;
import com.webproyecto.proyecto.service.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private SucursalService sucursalService;

    @Autowired
    private MovimientoInventarioService movimientoService;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalProductos", productoService.findAll().size());
        model.addAttribute("totalCategorias", categoriaService.findAll().size());
        model.addAttribute("totalSucursales", sucursalService.findAll().size());
        model.addAttribute("totalMovimientos", movimientoService.findAll().size());
        model.addAttribute("alertasStock", productoService.findProductosConStockBajo());
        model.addAttribute("ultimosMovimientos", movimientoService.findAll().stream().limit(5).toList());
        return "index";
    }
}
