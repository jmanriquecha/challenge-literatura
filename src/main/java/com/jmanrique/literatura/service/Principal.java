package com.jmanrique.literatura.service;

import com.jmanrique.literatura.dto.DatosLibrosDTO;
import com.jmanrique.literatura.model.*;
import com.jmanrique.literatura.repository.AutorRepostitory;
import com.jmanrique.literatura.repository.LibroRepository;
import org.hibernate.LazyInitializationException;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository repository;
    private AutorRepostitory autorRepostitory;

    public Principal(LibroRepository repository, AutorRepostitory autorRepostitory){
        this.repository = repository;
        this.autorRepostitory = autorRepostitory;
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

        // Valida que la API retorne información
        if(buscarLibro != null){
            // Traemos todos los libros registrados en la base de datos
            List<Libro> libros = repository.findAll();

            // Compara si el libro buscado es igual al registrado en la base de datos
            Optional<Libro> libro = libros.stream()
                    .filter(l->l.getTitulo().toLowerCase()
                            .contains(buscarLibro.titulo().toLowerCase()))
                    .findFirst();

            if(libro.isPresent()){
                System.out.println("Libro ya esta registrado en la base de datos! ");
            }else{

                // Obtiene nombre del autor del libro buscado
                var nombreAutor = buscarLibro.autores().stream().map(DatosAutor::nombre).findFirst().get();


                // Obtener todos los autores de la api
                List<Autor> autoresApi = buscarLibro.autores().stream()
                        .map(Autor::new)
                        .toList();

                //
                List<Libro> buscarAutores = repository.findAll(); // Retorna autores de la db
                Optional<Libro> autorBuscado = libros.stream()
                        .filter(l -> l.getAutores().stream()
                                .anyMatch(autor -> autor.getNombre().toLowerCase().contains(nombreAutor.toLowerCase())))
                        .findFirst();

                // Válida si el autor ya existe en la base de datos
                Libro saveLibro = new Libro(buscarLibro);
                if (autorBuscado.isPresent()){
                    // Autor existe
                    Autor autor = autorRepostitory.findByNombre(nombreAutor);
                    saveLibro.setAutores(new HashSet<>(Collections.singleton(autor)));
                    repository.save(saveLibro);
                }else{
                    // Autor no existe

                    System.out.println("Guardando autor");
                    saveLibro.setAutores(new HashSet<>(autoresApi)); // Asigna los autores al libro
                    repository.save(saveLibro); // Guarda el libro y la relación
                }
            }
        }else{
            System.out.println("El libro no existe!");
        }
    }

    public void listarLibrosRegistrados(){

        try{
            List<Libro> libros = repository.findAll();

            libros.stream().forEach(l ->
                    System.out.println(
                            "\n---LIBRO---"+
                            "\nTitulo: "+ l.getTitulo()+
                            "\nAutor: " + l.getAutores().stream()
                                    .map(a-> new DatosAutor(a.getNombre(), a.getAnioNacido(), a.getAnioMuerte()))
                                    .toList().stream().map(datos -> datos.nombre()).findFirst().get()+
                            "\nIdioma " + l.getIdiomas()+
                            "\nNúmero de descargas: " + l.getNumeroDeDesargas()));

        }catch (LazyInitializationException e){
            System.out.println(e);
        }

    }

    public void salir(){
        System.out.println("Saliendo del sistema...");
    }
}
