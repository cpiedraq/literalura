package com.alura.literalura.principal;

import com.alura.literalura.dataModels.BookData;
import com.alura.literalura.dataModels.ResultsFound;
import com.alura.literalura.models.Authors;
import com.alura.literalura.models.Book;
import com.alura.literalura.repository.AuthorsRepository;
import com.alura.literalura.repository.BookRepository;
import com.alura.literalura.service.ConsumeAPI;
import com.alura.literalura.service.ConvertData;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MainMenu
{
    private Scanner keyInserted = new Scanner(System.in);
    private ConsumeAPI consumeApi = new ConsumeAPI();
    private final String URL_API = "https://gutendex.com/books/";
    private BookRepository bookRepository;
    private AuthorsRepository authorsRepository;

    public MainMenu(BookRepository bookrepository, AuthorsRepository authorsrepository)
    {
        this.bookRepository = bookrepository;
        this.authorsRepository = authorsrepository;
    }

    public void showMenu()
    {
            boolean menuRepetead = true;
            String optionSelected = "";

            while(menuRepetead == true)
            {

                String menu = """
                
                Menú Principal de la LiterAlura
                ------------------------
                Elija la opción atraves de un número
                1.) Buscar libro por titulo.
                2.) Listar libros registrados.
                3.) Listar autores registrados.
                4.) Listar autores vivos en determinado año.
                5.) Listar libros por idioma.
                6.) Top diez de los libros más descargados.
                7.) Buscar autor por nombre
                8.) Estadísticas de las descargas de los libros
                0.) Salir
                ------------------------
                """;

                System.out.println(menu);

                optionSelected = keyInserted.nextLine();
                menuRepetead = optionMenu(optionSelected);
            }
    }

    public boolean optionMenu(String option)
    {
        try
        {
            boolean valueToReturn = true;
            Integer optionNumber = Integer.parseInt(option);

            switch (optionNumber)
            {
                case 1:
                    searchBookByTitle();
                    break;
                case 2:
                    searchAllBooks();
                    break;
                case 3:
                    searchAllAuthors();
                    break;
                case 4:
                    searchAuthorsAreAlive();
                    break;
                case 5:
                    searchByLanguages();
                    break;
                case 6:
                    searchTopTenDownloads();
                    break;
                case 7:
                    searchAuthorByName();
                    break;
                case 8:
                    getStats();
                    break;
                case 0:
                    valueToReturn = false;
                    break;
            }

            return valueToReturn;
        }
        catch (Exception e)
        {
            System.out.println("\n+++++++++++++++++++++++++++++ Ingrese una opción válida ++++++++++++++++++++++++++++\n");

            return true;
        }
    }

    public void searchBookByTitle()
    {
        try
        {
            String dataReturned = "";
            String url = "";
            String titleInserted = "";
            ConvertData convertData = new ConvertData();
            String textWithoutCaracters = "";
            int OptionSelectedFromApi = 0;

            System.out.println("Ingrese el titulo del libro a buscar");
            titleInserted = keyInserted.nextLine();

            textWithoutCaracters = titleInserted.toUpperCase();

            titleInserted = URLEncoder.encode(titleInserted, StandardCharsets.UTF_8);
            titleInserted = titleInserted.toUpperCase();

            url = URL_API + "?search=" + titleInserted;
            System.out.println(dataReturned);

            System.out.println("-------------- Espere un momento mientras realizamos la busqueda... ---------------");

            dataReturned = consumeApi.getDataFromUrl(url);

            ResultsFound dataFound = convertData.getDataConverted(dataReturned, ResultsFound.class);

            if(dataFound != null)
            {
                System.out.println("*********************************************************************");
                System.out.println("Resultados de la búsqueda");
                System.out.println("----------------------------------------------------------------------");

                OptionSelectedFromApi = showBookSearched(dataFound);

                //If this value is zero, the api did not return any value
                if(OptionSelectedFromApi !=0)
                {
                    BookData bookOptional = dataFound.booksReturned().get(OptionSelectedFromApi-1);

                    //Search if the book was founded
                    Book bookFound = bookRepository.findBooksByTitle(bookOptional.title());

                    if(bookFound == null)
                    {
                        saveBookInDatabase(bookOptional);
                    }
                    else
                    {
                        System.out.println("\n------------------- REGISTRADO ENCONTRADO -----------------------");
                        System.out.println("El libro esta almacenado en la base de datos, intente buscar otro libro.");
                        System.out.println("------------------------------------------------------------------");
                    }
                }
                System.out.println("\n\n*********************************************************************");
            }
            else
            {
                System.out.println("No existen libros con los datos ingresados");
            }
        }
        catch(Exception e)
        {
            if(e.getMessage().contains("duplicate key value violates unique constraint"))
            {
                System.out.println("El registro ya se encuentra registrado en la base de datos");
            }
        }
    }

    public int showBookSearched(ResultsFound dataFound)
    {
        int optionOrder = 1;
        int optionSelected = 0;
        String showOption = "";

        if(!dataFound.booksReturned().isEmpty())
        {
            if(dataFound.booksReturned().size() !=1)
            {
                for(BookData data : dataFound.booksReturned())
                {
                    showOption = optionOrder + "). Nombre del Libro: " + data.title();

                    System.out.println(showOption);

                    optionOrder++;
                }

                optionSelected = showMenuBookSelectedFromList(dataFound.booksReturned().size());

                return optionSelected;
            }
            else
            {
                return 1;
            }
        }
        else
        {
            System.out.println("La lista no contiene ningún resultado, intente buscar con otras palabras.");

            return 0;
        }
    }

    public int showMenuBookSelectedFromList(int countOptions)
    {
        try
        {
            Integer optionSelected = 0;
            String optionString = "";

            System.out.println("\nSeleccione el libro de la lista anterior, ingrese el número");
            optionString = keyInserted.nextLine();

            optionSelected = Integer.parseInt(optionString);

            if(optionSelected >= 1 && optionSelected <=countOptions)
            {
                return optionSelected;
            }
            else
            {
                System.out.println("\n++++++++++++++++++++++ La opción ingresada fue incorrecta +++++++++++++++++++++++++++++++\n");

                return showMenuBookSelectedFromList(countOptions);
            }
        }
        catch(Exception e)
        {
            System.out.println("++++++++++++++++++++++ La opción ingresada fue incorrecta +++++++++++++++++++++++++++++++");

            return 0;
        }
    }

    public void saveBookInDatabase(BookData bookOptional)
    {
        try
        {
            Book book = new Book(bookOptional);

            bookRepository.save(book);

            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Se ingresó el libro de forma correcta.");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
        }
        catch (Exception e)
        {
            if(e.getMessage().contains("duplicate key value violates unique constraint"))
            {
                System.out.println("El registro ya se encuentra registrado en la base de datos");
            }
        }
    }

    public void searchAllBooks()
    {
        List<Book> booksReturned =  bookRepository.findAll();
        AtomicInteger numberConsecutive = new AtomicInteger();

        numberConsecutive.getAndIncrement();

        booksReturned
                .stream()
                .forEach(b -> System.out.println("Registro " + numberConsecutive.getAndIncrement() + "\n"  + b.toString()));

        showCountOfRecords(booksReturned.size());
    }

    public void searchAllAuthors()
    {
        List<Authors> authorsReturned =  authorsRepository.findAll();
        AtomicInteger numberConsecutive = new AtomicInteger();

        numberConsecutive.getAndIncrement();

        authorsReturned
                .stream()
                .forEach(b -> System.out.println("Registro " + numberConsecutive.getAndIncrement() + "\n" + b.toString()));

        showCountOfRecords(authorsReturned.size());
    }

    public void searchAuthorsAreAlive()
    {
        try
        {
            Integer yearInserted =0;
            String yearString = "";
            AtomicInteger numberConsecutive = new AtomicInteger();
            numberConsecutive.getAndIncrement();

            System.out.println("Ingrese el año por el cual desea buscar");
            yearString = keyInserted.nextLine();

            yearInserted = Integer.parseInt(yearString);

            List<Authors> authorsReturned =  authorsRepository.findAuthorsAliveBetweenYear(yearInserted);

            if(!authorsReturned.isEmpty())
            {
                System.out.println("=====================================================================");
                System.out.println("Autores encontrados que estuvieron vivos durante el año " + yearString);
                System.out.println("=====================================================================");

                authorsReturned.stream()
                        .forEach(t -> System.out.println("Registro " + numberConsecutive.getAndIncrement() + "\n" + t.toString()));

                showCountOfRecords(authorsReturned.size());

                System.out.println("=====================================================================");
            }
            else
            {
                System.out.println("------- No existen autores que hayan estados vivos durante el año " + yearString + " ------");
            }
        }
        catch (Exception e)
        {
            System.out.println("+++++++++++++++++++++++ El año ingresado no es correcto +++++++++++++++++++++++++++++++++++++++");
        }
    }

    public void searchByLanguages()
    {
        try
        {
            String languageSearched = "";
            int optionSelected = 0;
            List<String> typesOfLanguages = bookRepository.findByTypesOfLanguage();
            AtomicInteger numberConsecutive = new AtomicInteger();
            numberConsecutive.getAndIncrement();

            optionSelected = getTypesLanguage(typesOfLanguages);

            languageSearched = typesOfLanguages.get(optionSelected-1);

            languageSearched =  "%" + languageSearched + "%";
            List<Book> booksListed = bookRepository.findByLanguagesBooks(languageSearched);

            booksListed.stream()
                   .forEach(b -> System.out.println("Registro " + numberConsecutive.getAndIncrement() + "\n" + b.toStringByLanguage()));

            showCountOfRecords(booksListed.size());
        }
        catch(Exception e)
        {
            System.out.println("\n++++++++++++++++++++++ La opción de lenguaje fue incorrecta +++++++++++++++++++++++++++++++\n");
        }
    }

    public int getTypesLanguage(List<String> typesOfLanguages)
    {
        String showOption = "";
        int optionOrder =1;
        int optionSelected = 0;

        for(String data : typesOfLanguages)
        {
            showOption = optionOrder + "). Lenguaje: " + data;

            System.out.println(showOption);

            optionOrder++;
        }

        optionSelected = showLanguageSelection(typesOfLanguages.size());

        return optionSelected;
    }

    public int showLanguageSelection(int countOptions)
    {
        try
        {
            Integer optionSelected = 0;
            String optionString = "";

            System.out.println("\nSeleccione el idioma para realizar la consulta.");
            optionString = keyInserted.nextLine();

            optionSelected = Integer.parseInt(optionString);

            if(optionSelected >= 1 && optionSelected <=countOptions)
            {
                return optionSelected;
            }
            else
            {
                System.out.println("\n++++++++++++++++++++++ La opción de lenguaje fue incorrecta +++++++++++++++++++++++++++++++\n");

                return showMenuBookSelectedFromList(countOptions);
            }
        }
        catch(Exception e)
        {
            System.out.println("++++++++++++++++++++++ La opción de lenguaje fue incorrecta +++++++++++++++++++++++++++++++");

            return 0;
        }
    }

    public void searchTopTenDownloads()
    {
        List<Book> books = bookRepository.findTop10Books();
        AtomicInteger numberConsecutive = new AtomicInteger();
        numberConsecutive.getAndIncrement();

        books.stream()
                .forEach(t -> System.out.println("Registro " + numberConsecutive.getAndIncrement() + "\n" + t.toStringByDownloadCount()));
    }

    public void searchAuthorByName()
    {
        String nameOfTheAuthor = "";
        AtomicInteger numberConsecutive = new AtomicInteger();
        numberConsecutive.getAndIncrement();

        System.out.println("\nIngrese el nombre del autor que desea buscar.");
        nameOfTheAuthor = keyInserted.nextLine();

        nameOfTheAuthor = "%" +  nameOfTheAuthor.toUpperCase() + "%";

        List<Authors> authorsReturned = authorsRepository.findByAuthorsByName(nameOfTheAuthor);

        authorsReturned.stream()
                .forEach(a -> System.out.println("Registro " + numberConsecutive.getAndIncrement() + "\n" + a.toString()));

        showCountOfRecords(authorsReturned.size());
    }

    public void getStats()
    {
        List<Book> books = bookRepository.findAll();
        String dataOfStats = "";

        DoubleSummaryStatistics stats = books.stream()
                .collect(Collectors.summarizingDouble(Book::getDownloadCount));

        System.out.println("\n\n------------- Estadística de los Libros ------------------------");

        dataOfStats = "\nLos libros que se encuentran registrados en la base de datos son: " + stats.getCount() + " libros" +
                "\nEl libro más descargado tiene: " + stats.getMax() + " descargas " +
                "\nEl libro menos descargado tiene: " + stats.getMin() + " descargas " +
                "\nEl total de la suma de las descargas: " + stats.getSum()+ " descargas " +
                "\nEl promedio cada uno de los libros fue descargado: " + stats.getAverage();


        System.out.println(dataOfStats);
    }

    public void showCountOfRecords(int countRecords)
    {
        System.out.println("=============================================================");
        System.out.println("Se encontraron " + countRecords + " registros, al realizar esta búsqueda");
        System.out.println("=============================================================");
    }
}
