package com.webproyecto.proyecto.controller;

import com.webproyecto.proyecto.model.Producto;
import com.webproyecto.proyecto.service.CategoriaService;
import com.webproyecto.proyecto.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("alertasStock", productoService.findProductosConStockBajo());
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.findAll());
        return "productos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        return productoService.findById(id).map(p -> {
            model.addAttribute("producto", p);
            model.addAttribute("categorias", categoriaService.findAll());
            return "productos/formulario";
        }).orElse("redirect:/productos");
    }

    @PostMapping
    public String guardar(@RequestParam String nombre,
                          @RequestParam String descripcion,
                          @RequestParam Double precio,
                          @RequestParam Integer cantidad,
                          @RequestParam(required = false) Integer stockMinimo,
                          @RequestParam(required = false) Long categoriaId,
                          @RequestParam(required = false) Long id,
                          Model model) {
        try {
            Producto producto = new Producto();
            if (id != null) producto.setId(id);
            producto.setNombre(nombre.trim());
            producto.setDescripcion(descripcion.trim());
            producto.setPrecio(precio);
            producto.setCantidad(cantidad);
            producto.setStockMinimo(stockMinimo);

            if (categoriaId != null) {
                categoriaService.findById(categoriaId).ifPresent(producto::setCategoria);
            }

            productoService.save(producto);
            return "redirect:/productos";

        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar: " + e.getMessage());
            model.addAttribute("producto", new Producto());
            model.addAttribute("categorias", categoriaService.findAll());
            return "productos/formulario";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        try {
            productoService.deleteById(id);
        } catch (Exception e) {
            // producto con movimientos asociados no puede eliminarse directamente
        }
        return "redirect:/productos";
    }
}
