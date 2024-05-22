package com.alura.literalura.dataModels;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookData
        (@JsonAlias("id") Integer Id,
         @JsonAlias("title") String title,
         @JsonAlias("download_count") Integer downloadCount,
         @JsonAlias("authors") List<AuthorData> authors,
         @JsonAlias("languages") List<String> languagesBook
         )
{
}
