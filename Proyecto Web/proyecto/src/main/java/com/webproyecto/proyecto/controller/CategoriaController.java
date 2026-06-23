package com.webproyecto.proyecto.controller;

import com.webproyecto.proyecto.model.Categoria;
import com.webproyecto.proyecto.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaService.findAll());
        return "categorias/lista";
    }

    @GetMapping("/nueva")
    public String formularioNueva(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorias/formulario";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        return categoriaService.findById(id).map(c -> {
            model.addAttribute("categoria", c);
            return "categorias/formulario";
        }).orElse("redirect:/categorias");
    }

    @PostMapping
    public String guardar(@RequestParam String nombre,
                          @RequestParam(required = false) String descripcion,
                          @RequestParam(required = false) Long id,
                          Model model) {
        try {
            Categoria categoria = new Categoria();
            if (id != null) categoria.setId(id);
            categoria.setNombre(nombre.trim());
            categoria.setDescripcion(descripcion != null ? descripcion.trim() : "");
            categoriaService.save(categoria);
            return "redirect:/categorias";
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar: " + e.getMessage());
            model.addAttribute("categoria", new Categoria());
            return "categorias/formulario";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        try {
            categoriaService.deleteById(id);
        } catch (Exception e) {
            // Si tiene productos asociados no se puede eliminar
        }
        return "redirect:/categorias";
    }
}
