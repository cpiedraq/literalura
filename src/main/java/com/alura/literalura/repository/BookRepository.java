package com.alura.literalura.repository;

import com.alura.literalura.models.Authors;
import com.alura.literalura.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT * FROM books WHERE UPPER(books.title)=:Title", nativeQuery =true)
    Book findBooksByTitle(String Title);

    @Query(value = "SELECT DISTINCT books.languages_books FROM books ORDER BY books.languages_books", nativeQuery = true)
    List<String> findByTypesOfLanguage();

    @Query("SELECT b FROM Book b WHERE b.languagesBooks LIKE :languageInserted")
    List<Book> findByLanguagesBooks(String languageInserted);

    @Query("SELECT b FROM Book b ORDER BY b.downloadCount DESC LIMIT 10")
    List<Book> findTop10Books();
}
