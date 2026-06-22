package com.webproyecto.proyecto.controller;

import com.webproyecto.proyecto.model.Producto;
import com.webproyecto.proyecto.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public String listarProductos(Model model) {
        List<Producto> productos = productoRepository.findAll();
        model.addAttribute("productos", productos);
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/formulario";
    }

    @PostMapping
    public String guardarProducto(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Double precio,
            @RequestParam(required = false) Integer cantidad,
            @RequestParam(required = false) Long id,
            Model model) {

        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                model.addAttribute("error", "El nombre del producto es requerido");
                model.addAttribute("producto", new Producto());
                return "productos/formulario";
            }

            if (descripcion == null || descripcion.trim().isEmpty()) {
                model.addAttribute("error", "La descripción es requerida");
                model.addAttribute("producto", new Producto());
                return "productos/formulario";
            }

            if (precio == null || precio <= 0) {
                model.addAttribute("error", "El precio debe ser mayor a 0");
                model.addAttribute("producto", new Producto());
                return "productos/formulario";
            }

            if (cantidad == null || cantidad < 0) {
                model.addAttribute("error", "La cantidad no puede ser negativa");
                model.addAttribute("producto", new Producto());
                return "productos/formulario";
            }

            Producto producto = new Producto();
            if (id != null && id > 0) {
                producto.setId(id);
            }
            producto.setNombre(nombre.trim());
            producto.setDescripcion(descripcion.trim());
            producto.setCategoria(categoria != null ? categoria.trim() : "");
            producto.setPrecio(precio);
            producto.setCantidad(cantidad);

            productoRepository.save(producto);
            return "redirect:/productos";

        } catch (Exception e) {
            System.out.println("Error al guardar producto: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al guardar el producto: " + e.getMessage());
            model.addAttribute("producto", new Producto());
            return "productos/formulario";
        }
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Producto> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            model.addAttribute("producto", producto.get());
            return "productos/formulario";
        }
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        try {
            productoRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
        }
        return "redirect:/productos";
    }
}
