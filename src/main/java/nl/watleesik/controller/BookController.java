package nl.watleesik.controller;

import nl.watleesik.api.ApiResponse;
import nl.watleesik.domain.Author;
import nl.watleesik.domain.Book;
import nl.watleesik.domain.BookCategory;
import nl.watleesik.repository.AuthorRepository;
import nl.watleesik.repository.BookCategoryRepository;
import nl.watleesik.repository.BookRepository;
import nl.watleesik.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "book")
@CrossOrigin(origins = "*")
public class BookController {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private BookCategoryRepository bookCategoryRepository;
    private BookService bookService;

    @Autowired
    public BookController(BookRepository bookRepository, AuthorRepository authorRepository, BookCategoryRepository bookCategoryRepository, BookService bookService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookCategoryRepository = bookCategoryRepository;
        this.bookService = bookService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<BookCategory>> getAllBookTypes() {
        return new ResponseEntity<>((bookCategoryRepository.findAll()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createBook(@RequestBody Book book) {

        Author authorDB = authorRepository.findAuthorByLastNameAndName(book.getAuthor().getLastName(), book.getAuthor().getName());

        // check if that author already exist or nog
        if (authorDB == null) {
            return new ResponseEntity<>(new ApiResponse(409, "Maak is een auteur aan met deze naam voordat je een boek wil toevoegen", null), HttpStatus.BAD_REQUEST);
        }

        List<Book> bookDB = bookRepository.findBookByTitle(book.getTitle());

        // loops true the booklist to see if the author already has that book saved
        for (Book bookList : bookDB) {
            if (bookList != null) {
                if (bookList.getAuthor().getLastName().equals(authorDB.getLastName()) && bookList.getAuthor().getName().equals(authorDB.getName())) {
                    return new ResponseEntity<>(new ApiResponse(409, "Het boek dat je wil toevoegen bestaat al voor deze auteur", book), HttpStatus.CONFLICT);
                }
            }
        }

        //makes a new book for an already existing author
        return new ResponseEntity<>(new ApiResponse(200, "Er is een boek aangemaakt met een schrijver die al bestond in de database", bookRepository.save(bookService.creatingNewBook(book, book.getAuthor()))), HttpStatus.CREATED);
    }

}
