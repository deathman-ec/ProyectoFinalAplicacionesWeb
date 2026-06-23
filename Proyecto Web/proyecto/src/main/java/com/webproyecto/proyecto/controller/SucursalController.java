package com.webproyecto.proyecto.controller;

import com.webproyecto.proyecto.model.Sucursal;
import com.webproyecto.proyecto.service.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sucursales")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("sucursales", sucursalService.findAll());
        return "sucursales/lista";
    }

    @GetMapping("/nueva")
    public String formularioNueva(Model model) {
        model.addAttribute("sucursal", new Sucursal());
        return "sucursales/formulario";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        return sucursalService.findById(id).map(s -> {
            model.addAttribute("sucursal", s);
            return "sucursales/formulario";
        }).orElse("redirect:/sucursales");
    }

    @PostMapping
    public String guardar(@RequestParam String nombre,
                          @RequestParam(required = false) String direccion,
                          @RequestParam(required = false) Long id,
                          Model model) {
        try {
            Sucursal sucursal = new Sucursal();
            if (id != null) sucursal.setId(id);
            sucursal.setNombre(nombre.trim());
            sucursal.setDireccion(direccion != null ? direccion.trim() : "");
            sucursalService.save(sucursal);
            return "redirect:/sucursales";
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar: " + e.getMessage());
            model.addAttribute("sucursal", new Sucursal());
            return "sucursales/formulario";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        try {
            sucursalService.deleteById(id);
        } catch (Exception e) {
            // Si tiene movimientos asociados no se puede eliminar
        }
        return "redirect:/sucursales";
    }
}
