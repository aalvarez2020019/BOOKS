package com.aluracursos.literalura.main;

import com.aluracursos.literalura.models.Author;
import com.aluracursos.literalura.models.LibrosDatos;
import com.aluracursos.literalura.models.Libro;
import com.aluracursos.literalura.repository.RepositorioAutor;
import com.aluracursos.literalura.repository.RepositorioLibro;
import com.aluracursos.literalura.services.ConvierteDatos;
import com.aluracursos.literalura.services.ConsumoAPI;

import java.util.List;
import java.util.Scanner;

public class Main {
    private ConsumoAPI requestAPI = new ConsumoAPI();
    private Scanner scanner = new Scanner(System.in);
    private String urlBase ="https://gutendex.com/books/";
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private RepositorioLibro libroRepository;
    private RepositorioAutor authorRepository;
    private List<Libro> libros;
    private List<Author> autores;

    public Main(RepositorioLibro libroRepository, RepositorioAutor authorRepository) {
        this.libroRepository = libroRepository;
        this.authorRepository = authorRepository;
    }

    public void mostrarMenu()
    {
        var opcion = -1;
        while (opcion != 0){
            var menu ="""
                    **************************************************
                        API DE LITERATURA "Busqueda de libros y autores"
                    **************************************************
                    
                    Bienvenido al programa, seleccione una opción 
                    
                    1 - Desea buscar un libro
                    2 - Desea consultar libros buscados
                    3 - Desea consultar autores de libros
                    4 - Desea Consultar autores de un año especifico
                    
                    0 - Salir del programa           
                    """;

            try {
                System.out.println(menu);
                opcion = scanner.nextInt();
                scanner.nextLine();
            }catch (Exception e){

                System.out.println("Ingresa una opcion valida");
            }

            switch (opcion){
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    consultarLibros();
                    break;
                case 3:
                    consultarAutores();
                    break;
                case 4:
                    consultarAutoresPorAno();
                    break;

                case 0:
                    System.out.println("Hasta luego");
                    break;
                default:
                    System.out.println("Ingresa una opcion valida");
            }
        }
    }

    // TRAER LOS DATOS DEL LIBRO
    private LibrosDatos getDatosLibro() {
        System.out.println("Ingrese el nombre del libro");
        var busqueda = scanner.nextLine().toLowerCase().replace(" ","%20");
        var json = requestAPI.getData(urlBase + "?search=" + busqueda);
        LibrosDatos datosLibro = convierteDatos.obtenerDatos(json, LibrosDatos.class);
        return datosLibro;
    }
    // BUSCAR LIBRO
    private void buscarLibro()
    {
        LibrosDatos datosLibro = getDatosLibro();

        try {
            Libro libro = new Libro(datosLibro.resultados().get(0));
            Author author = new Author(datosLibro.resultados().get(0).autorList().get(0));

            System.out.println("""
                    libro[
                        titulo: %s
                        author: %s
                        lenguaje: %s
                        descargas: %s
                    ]
                    """.formatted(libro.getTitulo(),
                    libro.getAutor(),
                    libro.getLenguaje(),
                    libro.getDescargas().toString()));

            libroRepository.save(libro);
            authorRepository.save(author);

        }catch (Exception e){
            System.out.println("no se encontro ese libro");
        }

    }

    // DATOS DE AUTORES QUE ESTABAN CONSULTADOS
    private void consultarAutores() {
        autores = authorRepository.findAll();
        autores.stream().forEach(a -> {
            System.out.println("""
                        Autor: %s
                        Año de nacimiento: %s
                        Año de defuncion: %s
                    """.formatted(a.getAutor(),
                    a.getNacimiento().toString(),
                    a.getDefuncion().toString()));
        });
    }

    // CONSULTAR LOS LIBROS QUE SE HAN GUARDADO EN POST
    private void consultarLibros() {
        libros = libroRepository.findAll();
        libros.stream().forEach(l -> {
            System.out.println("""    
                        Titulo: %s
                        Author: %s
                        Lenguaje: %s
                        Descargas: %s
                    """.formatted(l.getTitulo(),
                    l.getAutor(),
                    l.getLenguaje(),
                    l.getDescargas().toString()));
        });
    }

    // VER AÑO Y AUTOR
    public void consultarAutoresPorAno()
    {
        System.out.println("Ingresa el año a partir del cual buscar:");
        var anoBusqueda = scanner.nextInt();
        scanner.nextLine();

        List<Author> authors = authorRepository.autorPorFecha(anoBusqueda);
        authors.forEach( a -> {
            System.out.println("""
                    Nombre: %s
                    Fecha de nacimiento: %s
                    Fecha de defuncion: %s
                    """.formatted(a.getAutor(),a.getNacimiento().toString(),a.getDefuncion().toString()));
        });
    }

}

