package com.alura.literalura.models;

import com.alura.literalura.dataModels.AuthorData;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="authors")
public class Authors
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer Id;

    String completeName;

    Integer birthYear;

    Integer deathYear;

    @ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> booksOfAuthors;

    public Authors(){}

    public Authors(AuthorData data)
    {
        this.completeName = data.name();
        this.birthYear = data.birthYear();
        this.deathYear = data.deathYear();
    }

    public Authors(String name, Integer birthYear, Integer deathYear)
    {
        this.completeName = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
    }

    @Override
    public String toString() {
        return "--------------------------------------------------------" +
                "\nDatos del Autor:" +
                "\nNombre Completo: " + completeName +
                "\nAño de Nacimiento: " + birthYear +
                "\nAño de defuncion: " + deathYear +
                "\n--------------------------------------------------------";
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getCompleteName() {
        return completeName;
    }

    public void setCompleteName(String completeName) {
        this.completeName = completeName;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    public List<Book> getBooksOfAuthors() {
        return booksOfAuthors;
    }

    public void setBooksOfAuthors(List<Book> booksOfAuthors) {
        this.booksOfAuthors = booksOfAuthors;
    }
}
