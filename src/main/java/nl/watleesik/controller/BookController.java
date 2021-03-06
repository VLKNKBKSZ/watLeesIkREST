package nl.watleesik.controller;

import lombok.extern.slf4j.Slf4j;
import nl.watleesik.api.ApiResponse;
import nl.watleesik.domain.Book;
import nl.watleesik.domain.BookCategory;
import nl.watleesik.domain.Profile;
import nl.watleesik.domain.ProfileBook;
import nl.watleesik.exceptions.BookAlreadyExistForAuthorException;
import nl.watleesik.exceptions.BookNotDeletedException;
import nl.watleesik.exceptions.RatingNotUpdatedForBookListItemException;
import nl.watleesik.repository.BookCategoryRepository;
import nl.watleesik.repository.BookRepository;
import nl.watleesik.repository.ProfileBookRepository;
import nl.watleesik.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "book")
@Slf4j
public class BookController  implements IApiResponse{

    private BookCategoryRepository bookCategoryRepository;
    private BookService bookService;
    private BookRepository bookRepository;
    private ProfileBookRepository profileBookRepository;

    @Autowired
    public BookController(BookCategoryRepository bookCategoryRepository,
                          BookService bookService,
                          BookRepository bookRepository,
                          ProfileBookRepository profileBookRepository
    ) {
        this.bookCategoryRepository = bookCategoryRepository;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.profileBookRepository = profileBookRepository;

    }

    @DeleteMapping("delete/{isbn}")
    public ApiResponse<?> deleteBook(@PathVariable("isbn") Long isbn) throws BookNotDeletedException{
        boolean bookDelete = bookService.deleteBook(isbn);
        if (bookDelete) {
            return createResponse(200, "Boek is succesvol verwijderd.");
        }
        throw new BookNotDeletedException("Het boek dat je wil verwijderen bestaat niet.");
    }

    @GetMapping("/categories")
    public ResponseEntity<List<BookCategory>> getAllBookTypes() {

        return new ResponseEntity<>((bookCategoryRepository.findAll()), HttpStatus.OK);
    }

    @PostMapping
    public ApiResponse<?> createBook(@RequestBody Book book) throws BookAlreadyExistForAuthorException {

        Book newBook = bookService.creatingNewBook(book);

        if (newBook != null) {
            return createResponse(200, "Boek is opgeslagen.");
        }
       throw new BookAlreadyExistForAuthorException();
    }

    @PostMapping("/booklist")
    public ResponseEntity<ApiResponse> addBookToMyBookList(@RequestBody Book book, Principal principal) {
        boolean saveBookToMyBookList = bookService.addBookToMyBookList(book, principal);
        if (saveBookToMyBookList) {

            return new ResponseEntity<>(new ApiResponse(200, "Boek is toegevoegd aan boekenlijst.", null), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(409, "Het boek dat je wil toevoegen bestaat niet, of is al toegevoegd.", null), HttpStatus.CONFLICT);
    }

    @GetMapping("/mybooklist")
    public ResponseEntity<List<ProfileBook>> getBookList(Principal principal) {
        Profile profile = bookService.getProfileFromPrincipal(principal);
        List<ProfileBook> bookList = profile.getBookList();
        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }

    @PostMapping("delete/booklistitem")
    public ResponseEntity<ApiResponse> deleteBookFromBookList(@RequestBody ProfileBook profileBook) {
       boolean isSuccesfull = bookService.deleteBookFromBookList(profileBook);
       if(isSuccesfull) {
           return new ResponseEntity<>(new ApiResponse(200, "", null), HttpStatus.OK);
       }
        return new ResponseEntity<>(new ApiResponse(409, "Boek is niet verwijderd van boeklijst!", null), HttpStatus.CONFLICT);
    }

    @PutMapping("/mybooklist/addrating")
    public ApiResponse<?> addRatingToProfileBookItem(@RequestBody ProfileBook profileBook) throws RatingNotUpdatedForBookListItemException {
        boolean isSuccesfull = bookService.addRatingToProfileBookItem(profileBook);
        if(isSuccesfull) {
            return createResponse(200 , "Rating is succesvol toegevoegd");
        }
     throw new RatingNotUpdatedForBookListItemException();
    }
}
