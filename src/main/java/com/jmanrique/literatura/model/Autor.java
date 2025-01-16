package com.jmanrique.literatura.model;

import jakarta.persistence.*;

import java.time.format.DateTimeParseException;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer anioNacido;
    @ManyToMany(mappedBy = "autores")
    private List<Libro> libros;

    public Autor(){}

    public Autor(DatosAutor autor){
        this.nombre = autor.nombre();
        try{
            this.anioNacido = Integer.parseInt(autor.anioNacido());
        } catch (DateTimeParseException e){
            this.anioNacido = 0;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAnioNacido() {
        return anioNacido;
    }

    public void setAnioNacido(Integer anioNacido) {
        this.anioNacido = anioNacido;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
}
