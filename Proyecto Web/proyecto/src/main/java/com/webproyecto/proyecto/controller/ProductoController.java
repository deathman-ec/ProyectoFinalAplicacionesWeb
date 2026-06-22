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
    public String guardarProducto(@RequestParam String nombre,
                                   @RequestParam String descripcion,
                                   @RequestParam(required = false) String categoria,
                                   @RequestParam Double precio,
                                   @RequestParam Integer cantidad,
                                   @RequestParam(required = false) Long id,
                                   Model model) {

        System.out.println("=== DATOS RECIBIDOS ===");
        System.out.println("Nombre: " + nombre);
        System.out.println("Descripción: " + descripcion);
        System.out.println("Categoría: " + categoria);
        System.out.println("Precio: " + precio);
        System.out.println("Cantidad: " + cantidad);
        System.out.println("ID: " + id);
        System.out.println("==================");

        try {
            // Validación básica
            if (nombre == null || nombre.trim().isEmpty()) {
                model.addAttribute("error", "El nombre es requerido");
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

            System.out.println("Guardando producto: " + producto.getNombre());
            productoRepository.save(producto);
            System.out.println("Producto guardado exitosamente");

            return "redirect:/productos";

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error: " + e.getMessage());
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
            System.out.println("Error al eliminar: " + e.getMessage());
        }
        return "redirect:/productos";
    }
}
