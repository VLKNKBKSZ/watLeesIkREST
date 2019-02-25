package nl.watleesik.service;

import lombok.extern.slf4j.Slf4j;
import nl.watleesik.domain.Author;
import nl.watleesik.domain.Book;
import nl.watleesik.domain.BookCategory;
import nl.watleesik.repository.BookCategoryRepository;
import nl.watleesik.repository.BookRepository;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BookService {

    private BookCategoryRepository bookCategoryRepository;
    private AuthorService authorService;
    private BookRepository bookRepository;

    @Autowired
    public BookService(BookCategoryRepository bookCategoryRepository,
                       AuthorService authorService,
                       BookRepository bookRepository) {
        this.authorService = authorService;
        this.bookCategoryRepository = bookCategoryRepository;
        this.bookRepository = bookRepository;
    }

    public Book creatingNewBook(Book book) {

        Book newBook = new Book();

        Author authorDB = authorService.createAuthor(book.getAuthor());
        newBook.setAuthor(authorDB);

        BookCategory bookCategoryDB = creatingNewCategory(book.getBookCategory());
        newBook.setBookCategory(bookCategoryDB);

        List<Book> bookListDB = bookRepository.findBookByTitle(book.getTitle());

        // loops true the booklist to see if the author already has that book saved
        if (!bookListDB.isEmpty()) {
            for (Book bookList : bookListDB) {
                if (bookList.getAuthor().equals(authorDB)) {
                    return null;
                }
            }

        }

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
}
