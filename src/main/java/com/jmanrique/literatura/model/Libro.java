package com.jmanrique.literatura.model;

import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private List<String> idiomas;
    private Double numeroDeDesargas;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "libro_autor", joinColumns = @JoinColumn(name = "libro_id", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "autor_id", referencedColumnName = "id")
    )
    private List<Autor> autores;

    public Libro(){} // Por defecto

    public Libro(DatosLibros d){
        this.titulo = d.titulo();
        this.idiomas = d.idiomas();
        this.numeroDeDesargas = d.numeroDeDescargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public Double getNumeroDeDesargas() {
        return numeroDeDesargas;
    }

    public void setNumeroDeDesargas(Double numeroDeDesargas) {
        this.numeroDeDesargas = numeroDeDesargas;
    }
}
