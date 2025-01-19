package com.aluracursos.challengeLiterAlura.principal;

import com.aluracursos.challengeLiterAlura.model.Autor;
import com.aluracursos.challengeLiterAlura.model.Datos;
import com.aluracursos.challengeLiterAlura.model.Idioma;
import com.aluracursos.challengeLiterAlura.model.Libro;
import com.aluracursos.challengeLiterAlura.repository.AutorRepository;
import com.aluracursos.challengeLiterAlura.repository.LibroRepository;
import com.aluracursos.challengeLiterAlura.service.ConsumoAPI;
import com.aluracursos.challengeLiterAlura.service.ConvertirDatos;

import java.util.*;

public class Principal {

    private Scanner lectura = new Scanner(System.in);
    // Instancias de servicios para consumir y convertir datos de una API
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvertirDatos convertirDatos = new ConvertirDatos();
    private final String URL_BASE = "http://gutendex.com/books/?search=";

    private List<Libro> listaDeLibros = new ArrayList<>();
    // Repositorios para interactuar con la base de datos
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository){
        this.libroRepository = libroRepository;
        this.autorRepository= autorRepository;
    }

    public void menuAlUsuario() {
        int opcionElegida = 0;
        boolean continuacion = true;
        String menuGutendex = """
                    =============================
                    |       MENÚ PRINCIPAL      |
                    |  Bienvenidos a LiterAlura |
                    =============================
                ********Selecciona una opción**********
                [1] Buscar Libro por Título
                [2] Listar Libros Registrados 
                [3] Listar Autores Registrados
                [4] Listar Autores vivos en un determinado año
                [5] Listar Libros por Idioma
                [6] SALIR 
                """;
        while (continuacion) {
            try {
                System.out.println(menuGutendex);
                System.out.println("Ingrese una opción: ");
                opcionElegida = lectura.nextInt();
                lectura.nextLine();
                switch (opcionElegida) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibroRegistrado();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresEnUnDeterminadoAño();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 6:
                        System.out.println("""
                                *****Cerrando la aplicación*****
                                """);
                        continuacion = false;
                        break;
                    default:
                        System.out.println("Opción no esta en el menú, intente nuevamente");
                }
            } catch (InputMismatchException e) {
                // Capturar excepciones cuando se ingresan datos no válidos
                System.out.println("Ingrese una opción válida, intente nuevamente ");
                lectura.nextLine();
            }
        }
    }
    // Obtiene datos desde la API a partir de un título proporcionado por el usuario
    public Datos obtenerDatos() {
        System.out.println("Introduce el nombre del libro a buscar:");
        var busqueda = lectura.nextLine().trim();
        busqueda = busqueda.replace(" ", "%20");
        var urlBusqueda = URL_BASE + busqueda;
        // Consume la API y convierte la respuesta JSON en un objeto Datos
        var json = consumoAPI.consumirAPI(urlBusqueda);
        Datos datosObtenidos = convertirDatos.convertirDatos(json, Datos.class);
        return datosObtenidos;
    }
    // Busca un libro en la API, verifica su existencia y lo registra en la base de datos
    private void buscarLibroPorTitulo() {
        Datos datosObtenidos = obtenerDatos();
        // Toma el primer libro de los resultados obtenidos
        Optional<Libro> primerLibro = datosObtenidos.libro().stream()
                .findFirst()
                .map(datosLibro -> {
                    List<Autor> autores = new ArrayList<>();
                    for (var datosAutor : datosLibro.autores()) {
                    // Verifica si el autor ya existe en la base de datos
                        Optional<Autor> autorExistente = autorRepository.findByNombre(datosAutor.nombre());
                        Autor autor = autorExistente.orElseGet(() -> {
                            Autor nuevoAutor = new Autor(datosAutor);
                            autorRepository.save(nuevoAutor); // Guarda al autor si no existe
                            return nuevoAutor;
                        });
                        autores.add(autor);
                    }
                    Autor autorPrincipal = autores.isEmpty() ? null : autores.get(0);
                    return new Libro(datosLibro, autorPrincipal); // Crea un libro con el autor principal
                });
        if (primerLibro.isPresent()) {
            Libro libro = primerLibro.get();
            // Verifica si el libro ya existe en la base de datos
            Optional<Libro> libroExistente = libroRepository.findByTitulo(libro.getTitulo());

            if (libroExistente.isPresent()) {
                System.out.println("El libro ya está registrado en la base de datos!!!\n");
            } else {
                libroRepository.save(libro); // Guarda el libro si no existe
                System.out.println("Libro registrado exitosamente!!!");
                System.out.println(libro);
            }
        } else {
            System.out.println("No se encontraron libros para mostrar.\n");
        }
    }

    private void listarLibroRegistrado() {
        List<Libro> librosRegistrados = libroRepository.findAll();
        if (librosRegistrados.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
            System.out.println();
        } else {
            System.out.println("Lista de Libros Registrados!!!");
            System.out.println("*********************************************");
            for (Libro libro : librosRegistrados) {
                System.out.println("\t- Titulo: \"" + libro.getTitulo() + "\"");
            }
            System.out.println("*********************************************\n");
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autoresRegistrados = autorRepository.findAll();
        if (autoresRegistrados.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos.\n");
        } else {
            System.out.println("Lista de Autores Registrados!!!");
            System.out.println("*********************************************");
            for (Autor autor : autoresRegistrados) {
                System.out.println("\t- Autor: " + autor.getNombre());
            }
            System.out.println("*********************************************\n");
        }
    }

    private void listarAutoresEnUnDeterminadoAño() {

        System.out.println("Introduce el año para listar autores vivos:");
        int año = lectura.nextInt();
        lectura.nextLine();
        List<Autor> autoresVivos = autorRepository.findAutoresVivosEnAño(año);
        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + año + ".\n");
        } else {
            System.out.println("Autores vivos en el año " + año + ":");
            for (Autor autor : autoresVivos) {
                System.out.println("\t- " + autor.getNombre());
            }
        }
        System.out.println();
    }

    private void listarLibrosPorIdioma() {
        System.out.println("Introduce el idioma para filtrar (EN, ES, FR, DE, IT, PT, otro):");
        String entrada = lectura.nextLine().trim();
        Idioma idiomaSeleccionado;
        idiomaSeleccionado = Idioma.fromString(entrada); // Convierte el texto ingresado al tipo Idioma
        if (idiomaSeleccionado == Idioma.OTRO) {
            System.out.println("No se encontraron libros en el idioma seleccionado.");
            return;
        }
        List<Libro> librosFiltrados = libroRepository.findByIdioma(idiomaSeleccionado);
        if (librosFiltrados.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma seleccionado.");
        } else {
            System.out.println("Libros en " + idiomaSeleccionado.getDescripcion() + ":");
            librosFiltrados.forEach(libro -> System.out.println("\t- " + libro.getTitulo()));
        }
        System.out.println();
    }

}
