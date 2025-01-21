package com.alura.literalura.repository;

import com.alura.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository {

    @Query("SELECT l FROM Libros l WHERE l.libros ILIKE %:idiomas%")
    List<Libro> findByIdiomas(String idiomas);
}
