package com.alura.literalura.dataModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResultsFound(
            @JsonAlias("count") Integer count,
            @JsonAlias("next") String next,
            @JsonAlias("previous") String previous,
            @JsonAlias("results") List<BookData> booksReturned
            )
{
}
