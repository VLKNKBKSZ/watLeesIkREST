package nl.watleesik.service;

import lombok.extern.slf4j.Slf4j;
import nl.watleesik.domain.*;
import nl.watleesik.exceptions.RatingNotUpdatedForBookListItem;
import nl.watleesik.repository.*;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class BookService {

    private BookCategoryRepository bookCategoryRepository;
    private AuthorService authorService;
    private BookRepository bookRepository;
    private ProfileRepository profileRepository;
    private AccountRepository accountRepository;
    private ProfileBookRepository profileBookRepository;
    private RatingRepository ratingRepository;

    @Autowired
    public BookService(BookCategoryRepository bookCategoryRepository,
                       AuthorService authorService,
                       BookRepository bookRepository,
                       ProfileRepository profileRepository,
                       AccountRepository accountRepository,
                       ProfileBookRepository profileBookRepository,
                       RatingRepository ratingRepository) {
        this.authorService = authorService;
        this.bookCategoryRepository = bookCategoryRepository;
        this.bookRepository = bookRepository;
        this.profileRepository = profileRepository;
        this.accountRepository = accountRepository;
        this.profileBookRepository = profileBookRepository;
        this.ratingRepository = ratingRepository;
    }

    // loops true the booklist to see if the author already has that book saved
    public boolean checkIfBookExists(Book book) {
        return bookRepository.findBookByTitle(book.getTitle())
                .stream()
                .filter(b -> b.getAuthor().equals(book.getAuthor()))
                .count() != 0;
    }

    public Book creatingNewBook(Book book) {

        if (checkIfBookExists(book)) {
            return null;
        }
        Book newBook = new Book();

        Author authorDB = authorService.createAuthor(book.getAuthor());
        newBook.setAuthor(authorDB);

        BookCategory bookCategoryDB = creatingNewCategory(book.getBookCategory());
        newBook.setBookCategory(bookCategoryDB);

        newBook.setTitle(book.getTitle());
        newBook.setPublicationYear(book.getPublicationYear());
        newBook.setIsbn(book.getIsbn());

        bookRepository.save(newBook);

        return newBook;
    }

    public BookCategory creatingNewCategory(BookCategory bookCategory) {
        BookCategory bookCategoryDB = bookCategoryRepository.findBookCategoryByName(bookCategory.getName());
        if (bookCategoryDB != null) {
            return bookCategoryDB;
        }
        return bookCategoryRepository.save(bookCategory);
    }

    public boolean deleteBook(Long isbn) {
        Book bookDelete = bookRepository.findBookByIsbn(isbn);
        if (bookDelete != null) {
            try {

                bookRepository.delete(bookDelete);
                log.info("book is succesfully deleted");
                return true;
            } catch (HibernateException ex) {
                log.warn(ex.getMessage(), "Something went wrong while deleting a book");
                return false;
            }
        }
        return false;
    }

    public boolean addBookToMyBookList(Book book, Principal principal) {
        Book bookDB = bookRepository.findBookByIsbn(book.getIsbn());

        //Check if book is not null and book is not already in the booklist
        if (bookDB != null && bookIsAlreadyInMyBookList(bookDB, principal)) {
            return true;
        }
        return false;
    }


    public boolean bookIsAlreadyInMyBookList(Book book, Principal principal) {
        Profile profileTemp = getProfileFromPrincipal(principal);
        List<ProfileBook> bookList = profileTemp.getBookList();
        log.debug(bookList.toString());

        // if book already exist in the booklist return false
        for (ProfileBook bookItem : bookList) {
            if (bookItem.getBook().equals(book))
                return false;
        }
        //save book to ProfileBookList

        profileTemp.addProfileBookToBookList(createProfileBook(profileTemp, book));
        profileRepository.save(profileTemp);
        return true;
    }

    public ProfileBook createProfileBook(Profile profile, Book book) {
        ProfileBook profileBookNew = new ProfileBook();
        profileBookNew.setProfile(profile);
        profileBookNew.setBook(book);
        profileBookNew.setAddedOn(LocalDate.now());
        return profileBookRepository.save(profileBookNew);


    }

    public Profile getProfileFromPrincipal(Principal principal) {
        // TODO: Adjust for optional
        Profile profile = accountRepository.findAccountByEmail(principal.getName()).get().getProfile();
        return profile;
    }

    public boolean deleteBookFromBookList(ProfileBook profileBook) {

        try {
            profileBookRepository.delete(profileBookRepository.findById(profileBook.getId()));
            return true;
        } catch (HibernateException ex) {
            log.debug(ex.getMessage());
            return false;
        }
    }

    public boolean addRatingToProfileBookItem(ProfileBook profileBook) {
        ProfileBook profileBook1 = profileBookRepository.findById(profileBook.getId());
        if (profileBook1 != null) {
            profileBook1.setFinishedOn(LocalDate.now());
            profileBook1.setRating(ratingRepository.save(profileBook.getRating()));
            profileBookRepository.save(profileBook1);
            log.info(profileBook1.toString() + " waardering is toegevoegd");
            return true;
        }
        return false;
    }
}
