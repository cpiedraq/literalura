package com.alura.literalura.repository;

import com.alura.literalura.models.Authors;
import com.alura.literalura.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorsRepository extends JpaRepository<Authors, Long>
{
    @Query("SELECT a FROM Authors a WHERE birthYear<=:yearInserted AND deathYear>=:yearInserted")
    List<Authors> findAuthorsAliveBetweenYear(int yearInserted);

    @Query("SELECT a FROM Authors a WHERE UPPER(a.completeName) LIKE :authorName")
    List<Authors> findByAuthorsByName(String authorName);
}
