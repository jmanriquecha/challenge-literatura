package com.jmanrique.literatura.repository;

import com.jmanrique.literatura.model.DatosLibros;
import com.jmanrique.literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    DatosLibros findByTitulo(String id);
}
