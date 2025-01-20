package com.aluracursos.challengeLiterAlura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros") // Nombre de la tabla en la base de datos
public class Libro {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generación de ID
    private Long id;

    @Column(name = "titulo", nullable = false) // Columna "titulo" no puede ser nula
    private String titulo;

    @Column(name = "cantidad_descargas")
    private Integer cantidadDescargas;

    // Relación Many-to-One con la clase Autor
    @ManyToOne // Muchos libros pueden estar relacionados con un solo autor
    @JoinColumn(name = "autor_id", nullable = false) // Columna que referencia al autor
    private Autor autor;

    @Enumerated(EnumType.STRING) // Almacena el enum como texto
    @Column(name = "idioma", nullable = false)
    private Idioma idioma;

    // Constructor vacío requerido por JPA
    public Libro() {}

    public Libro(DatosLibro datosLibro, Autor autor) {
        this.titulo = datosLibro.titulo();
        // Aquí convertimos el valor del idioma en minúsculas
        this.idioma = Idioma.fromString(datosLibro.idiomas().isEmpty() ? "otro" : datosLibro.idiomas().get(0).toLowerCase());
        this.autor = autor;
        this.cantidadDescargas= datosLibro.cantidadDescargas();// Asociamos el único autor
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getCantidadDescargas() {
        return cantidadDescargas;
    }

    public void setCantidadDescargas(Integer cantidadDescargas) {
        this.cantidadDescargas = cantidadDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    @Override
    public String toString() {
        return """
        ******************************************
        \tTitulo: %s
        \tAutor: %s
        \tIdioma: %s
        \tCantidad de Descargas: %d
        ******************************************
        """.formatted(
                titulo,
                autor.getNombre(),
                idioma.getDescripcion(),
                cantidadDescargas
        );
    }
}
