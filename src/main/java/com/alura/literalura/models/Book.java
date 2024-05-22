package com.alura.literalura.models;

import com.alura.literalura.dataModels.BookData;
import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name="books")
public class Book
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;

    @Column(unique = true)
    String title;

    Integer downloadCount;

    @ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    List<Authors> authorsOfBook;

    String languagesBooks;

    public Book(){}

    public Book(BookData data)
    {
        this.title = data.title();
        this.downloadCount = data.downloadCount();
        this.authorsOfBook = data.authors().stream()
                .map(a -> new Authors(a.name(), a.birthYear(), a.deathYear()))
                .collect(Collectors.toList());
        //this.languagesBooks = data.languagesBook();
        this.downloadCount = data.downloadCount();
        this.languagesBooks = data.languagesBook().toString();
    }

    @Override
    public String toString() {
        return "***********************************************" +
                "\n\nLibro: " +
                "\nTitulo=" + title +
                "\nDescargas del Libro=" + downloadCount +
                "\nLenguaje=" + languagesBooks +
                "\nAutores=" + authorsOfBook +
                "\n\n***********************************************";
    }

    public String toStringByLanguage() {
        return "***********************************************" +
                "\n\nLibro: " +
                "\nLenguaje=" + languagesBooks +
                "\nTitulo=" + title +
                "\n\n***********************************************";
    }

    public String toStringByDownloadCount() {
        return "***********************************************" +
                "\n\nLibro: " +
                "\nDescargas del Libro=" + downloadCount +
                "\nTitulo=" + title +
                "\n\n***********************************************";
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public List<Authors> getAuthorsOfBook() {
        return authorsOfBook;
    }

    public void setAuthorsOfBook(List<Authors> authorsOfBook) {
        this.authorsOfBook = authorsOfBook;
    }

    public String getLanguagesBooks() {
        return languagesBooks;
    }

    public void setLanguagesBooks(String languagesBooks) {
        this.languagesBooks = languagesBooks;
    }
}
