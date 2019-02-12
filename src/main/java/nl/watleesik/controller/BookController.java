package nl.watleesik.controller;

import nl.watleesik.api.ApiResponse;
import nl.watleesik.domain.Author;
import nl.watleesik.domain.Book;
import nl.watleesik.domain.BookCategory;
import nl.watleesik.repository.AuthorRepository;
import nl.watleesik.repository.BookCategoryRepository;
import nl.watleesik.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping(path = "book")
@CrossOrigin(origins = "*")
public class BookController {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private BookCategoryRepository bookCategoryRepository;

    @Autowired
    public BookController (BookRepository bookRepository, AuthorRepository authorRepository, BookCategoryRepository bookCategoryRepository){
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookCategoryRepository = bookCategoryRepository;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<BookCategory>> getAllBookTypes () {
        return new ResponseEntity<>((bookCategoryRepository.findAll()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createBook(@RequestBody Book book) {

        if (bookRepository.findBookByTitle(book.getTitle()) != null) {
            return new ResponseEntity<>(new ApiResponse(409, "Er bestaat al een boek met deze titel", null), HttpStatus.BAD_REQUEST);
        }

        Author authorDB = authorRepository.findAuthorByLastNameAndName(book.getAuthor().getLastName(), book.getAuthor().getName());

        if (authorDB == null) {

            Author author = new Author();
            author.setName(book.getAuthor().getName());
            author.setMiddleName(book.getAuthor().getMiddleName());
            author.setLastName(book.getAuthor().getLastName());

            Author authorNewDB = authorRepository.save(author);

            return new ResponseEntity<>(new ApiResponse(200, "Boek is toegevoegd aan de collectie met een nieuwe auteur", bookRepository.save(creatingNewBook(book, authorNewDB))), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(new ApiResponse(200, "Boek is toegevoegd aan de collectie met een bestaande auteur", bookRepository.save(creatingNewBook(book, authorDB))), HttpStatus.CREATED);

    }

    public Book creatingNewBook(Book book, Author author) {

        Book newBook = new Book();

        BookCategory bookTypeDB = bookCategoryRepository.findBookCategoryByName(book.getBookCategory().getName());
        newBook.setBookCategory(bookTypeDB);

        newBook.setAuthor(author);

        newBook.setTitle(book.getTitle());
        newBook.setPublicationYear(book.getPublicationYear());
        newBook.setPages(book.getPages());
        newBook.setIsbn(book.getIsbn());

        return newBook;
    }

}
