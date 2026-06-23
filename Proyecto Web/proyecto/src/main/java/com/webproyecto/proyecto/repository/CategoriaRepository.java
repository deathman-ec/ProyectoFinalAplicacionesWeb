package com.webproyecto.proyecto.repository;

import com.webproyecto.proyecto.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
