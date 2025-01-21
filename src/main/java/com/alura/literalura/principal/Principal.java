package com.alura.literalura.principal;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Datos;
import com.alura.literalura.model.DatosLibros;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;

import java.nio.channels.ScatteringByteChannel;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;
    private Scanner teclado = new Scanner(System.in);
    private List<Libro> libros;
    private List<Autor> autores;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }


    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ****************************************************
                    *********************LITERALURA*********************
                    ****************************************************
                    Escoge alguna de las siguientes opciones:
                    1 - Buscar libro por titulo
                    1 - Listar libros registrados
                    1 - Listar autores registrados  
                    1 - Listar autores registrados en un determinado año 
                    1 - Listar libro por idioma                
                    0 - Salir
                    """;
            try {
                System.out.println(menu);
                opcion = teclado.nextInt();
                teclado.nextLine();
            }catch (Exception e){
                System.out.println("Opción inválida");
            }



            switch (opcion) {
                case 1:
                    buscarlibro();
                    break;
                case 2:
                    listarLibro();
                    break;
                case 3:
                    consultarAutores();
                    break;
                case 4:
                    consultarAutoresPorAno();
                    break;
                case 5:
                    buscarLibroPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private Datos getDatosLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = teclado.nextLine().toLowerCase().replace(" ","+");

        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro);

        System.out.println(json);

        Datos datos= conversor.obtenerDatos(json, Datos.class);
        return datos;
    }




    private void buscarlibro(){
        Datos datosLibros = getDatosLibro();

        try{
            Libro libro = new Libro(datosLibros.resultados().get(0));
            Autor autor = new Autor(datosLibros.resultados().get(0).autor().get(0));
            System.out.println(
                    """
                           ***************************************
                            Resultado del libro buscado:
                            Titulo: %s
                            Autor: %s
                            Idioma: %s
                            descargas: %s         
                            """.formatted(libro.getTitulo(), libro.getAutor(),
                            libro.getIdiomas(), libro.getNumeroDeDescargas().toString()));

        } catch (Exception e) {
            System.out.println("Libro  no encontrado");
        }
    }

    //Listar los libros de la bd
    private void listarLibro(){
        libros = libroRepository.findAll();
        libros.stream().forEach(l -> {
            System.out.println("""
            ***************************************
            Resultado del libro buscado:
            Titulo: %s
            Autor: %s
            Idioma: %s
            descargas: %s        
            """.formatted(l.getTitulo(),
                    l.getAutor(),
                    l.getIdiomas(),
                    l.getNumeroDeDescargas().toString()));
        });
    }

    private void consultarAutores(){
        autores = autorRepository.findAll();
        autores.stream().forEach(a -> {
            System.out.println("""
            ***************************************
            Resultado de autores registrados:
            Autor: %s
            Fecha de nacimiento: %s
            Fecha de fallecimiento: %s        
            """.formatted(a.getNombreAutor(),
                    a.getFechaNacimiento().toString(),
                    a.getFechaFallecimiento().toString()));
        });
    }

    public void consultarAutoresPorAno(){
        System.out.println("introduzca el año para buscar autor:");
        var anioBusqueda = teclado.nextInt();
        teclado.nextLine();

        List<Autor> autores = autorRepository.autorPorAnio(anioBusqueda);
        autores.stream().forEach(a -> {
            System.out.println("""
            ***************************************
            Resultado de autores registrados:
            Autor: %s
            Fecha de nacimiento: %s
            Fecha de fallecimiento: %s      
            """.formatted(a.getNombreAutor(),
                    a.getFechaNacimiento().toString(),
                    a.getFechaFallecimiento().toString()));
        });
    }

    public void buscarLibroPorIdioma(){

        System.out.println("""
            ***************************************
            Ingrese el numero del idioma para buscar el libro:
            1- EN-Ingles
            2- ES-Español     
            """);

        try{

        } catch (Exception e) {
            System.out.println("Opción inválida");
        }

        try {

            var opcion2 = teclado.nextInt();
            teclado.nextLine();

            switch (opcion2) {
                case 1:
                    libros = libroRepository.findByIdiomas("en");
                    break;
                case 2:
                    libros = libroRepository.findByIdiomas("es");
                    break;

                default:
                    System.out.println("Opción inválida, ingrese 1 o 2");
            }

            libros.stream().forEach(l -> {
                System.out.println("""
            ***************************************
            Libros esccritos en el idiomas descrito:
            Titulo: %s
            Autor: %s
            Idioma: %s
            descargas: %s        
            """.formatted(l.getTitulo(),
                        l.getAutor(),
                        l.getIdiomas(),
                        l.getNumeroDeDescargas().toString()));
            });


        } catch (Exception e){
            System.out.println("Ingresa un valor valido");
        }
    }

    }















