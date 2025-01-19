package com.aluracursos.challengeLiterAlura.repository;

import com.aluracursos.challengeLiterAlura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository
        extends JpaRepository<Autor, Long> {

    // Método para encontrar un autor por su nombre
    Optional<Autor> findByNombre(String nombre);

    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :año AND (a.fechaFallecimiento IS NULL OR a.fechaFallecimiento >= :año)")
    List<Autor> findAutoresVivosEnAño(@Param("año") int año);

}
