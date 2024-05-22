# LiterAlura
Literalura es un projecto creado en Java Spring. 

Esta aplicación de consola (Java) consume el API de Gutendex (https://gutendex.com/) que retorna una lista de libros y su autor. 
Luego los datos se insertan en la base de datos PostgreSQL, en la cual se crean dos entidades (Libros y autores).
Se realizan diversas consultas a la base de datos con ayuda de la librería Spring Data JPA con fin de retornar información.

El uso de la aplicación es el siguiente:

La aplicación al inicio mostrará un menú de opciones.

Menú Principal de la LiterAlura
------------------------
Elija la opción atraves de un número
1. Buscar libro por titulo = _Busca un libro por el nombre, en el cuál se utiliza el API https://gutendex.com/books?search=NombreLibro, se enlistaran los libros encontrados, luego será necesario seleccionar el libro y el mismo serà almacenado en la base de datos_


2. Listar libros registrados = _Se enlistan todos los libros que se encuentran almacenados en la base de datos_
3. Listar autores registrados = _Se enlistan todos los autores que se encuentran almacenados en la base de datos_
4. Listar autores vivos en determinado año = _Se debe de elegir un año, luego se realizará una búsqueda en los autores que se encontraban vivos en el año seleccionado_
5. Listar libros por idioma = _Se enlistan los libros por el idioma seleccionado_
6. Top diez de los libros más descargados = _Se enlistan los primeros diez libros que han sido descargas_
7. Buscar autor por nombre = _Se busca un autor que se almacenado en la base de datos por nombre_
8. Estadísticas de las descargas de los libros = _Muestra las estadísticas de las descargas realizadas_
0. Salir
------------------------

# Tecnologías Utilizadas.

- JDK 17.
- Maven.
- Spring Boot.
- PostgreSQL.
- Spring Data JPA.
