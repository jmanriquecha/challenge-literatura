package com.jmanrique.literatura.service;

import com.jmanrique.literatura.model.*;
import com.jmanrique.literatura.repository.LibroRepository;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository repository;

    public Principal(LibroRepository repository){
        this.repository = repository;
    }

    public void showMenu(){
        var flag = true;
        var mensaje =  """
                Elija una opción a través de su número:
                \t1 - Buscar libro por titúlo
                \t2 - Listar libros registrados
                \t0 - Salir
                """;

        while (flag){
            System.out.println(mensaje);
            int option = Integer.parseInt(scanner.nextLine());

            switch (option){
                case 1:
                    agregaLibrosYAutores();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    System.out.println("Elegiste listar autores registrados");
                    break;
                case 4:
                    System.out.println("Elegiste listar autores vivos en determinado año");
                    break;
                case 5:
                    System.out.println("Elegiste listar libros por idioma");
                case 0:
                    salir();
                    flag = false;
                    break;
                default:
                    System.out.println("La opción seleccionada no es valida!");
                    break;
            }
        }
    }

    public DatosLibros getLibrosPorNombre(){
        System.out.println("Ingrese el nombre del libro que desea buscar");
        var buscarLibro = scanner.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE.concat("?search=").concat(buscarLibro.replace(" ", "%20")));
        var datos = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibros> libros = datos.resultados().stream()
                        .filter(d -> d.titulo().toLowerCase()
                                .contains(buscarLibro.toLowerCase()))
                        .findFirst();

        return libros.orElse(null);
    }

    public void agregaLibrosYAutores(){
        var buscarLibro = getLibrosPorNombre();
        if(buscarLibro != null){
            System.out.printf("El libro %s fue encontrado y agregado a la base de datos ", buscarLibro.titulo());
            Libro libro = new Libro(buscarLibro);
            repository.save(libro);
            List<Autor> autores = buscarLibro.autor().stream()
                    .map(a -> new Autor(a))
                    .collect(Collectors.toList());

            // Agrega autor
            libro.setAutores(autores);
            repository.save(libro);
            System.out.println("Autor se agrego correctamente");

        }else{
            System.out.println("El libro buscado no fue encontrado ó no existe!");
        }
    }

    public void listarLibrosRegistrados(){
        List<Libro> libros = repository.findAll();

        libros.stream().forEach(System.out::println);
    }

    public void salir(){
        System.out.println("Saliendo del sistema...");
    }
}
