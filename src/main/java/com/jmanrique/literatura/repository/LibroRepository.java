package com.jmanrique.literatura.repository;

import com.jmanrique.literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LibroRepository extends JpaRepository<Libro, Long> {

}
