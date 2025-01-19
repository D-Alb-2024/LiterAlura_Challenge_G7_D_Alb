package com.aluracursos.challengeLiterAlura.repository;

import com.aluracursos.challengeLiterAlura.model.Idioma;
import com.aluracursos.challengeLiterAlura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository
        extends JpaRepository<Libro, Long> {

    // Método para buscar un libro por título
    Optional<Libro> findByTitulo(String titulo);

    List<Libro> findByIdioma(Idioma idioma);

}
