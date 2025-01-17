package com.jmanrique.literatura.repository;

import com.jmanrique.literatura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AutorRepostitory extends JpaRepository<Autor, Long> {
    Autor findByNombre(String nombre);
}
