package nl.watleesik.service;

import nl.watleesik.domain.Author;
import nl.watleesik.domain.Book;
import nl.watleesik.domain.BookCategory;
import nl.watleesik.repository.AuthorRepository;
import nl.watleesik.repository.BookCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private BookCategoryRepository bookCategoryRepository;
    private AuthorRepository authorRepository;

    @Autowired
    public BookService(BookCategoryRepository bookCategoryRepository,
                       AuthorRepository authorRepository) {
        this.bookCategoryRepository = bookCategoryRepository;
        this.authorRepository = authorRepository;
    }

    public Book creatingNewBook(Book book, Author author) {

        Book newBook = new Book();

        BookCategory bookCategoryDB = bookCategoryRepository.findBookCategoryByName(book.getBookCategory().getName());
        newBook.setBookCategory(bookCategoryDB);

        newBook.setAuthor(authorRepository.findAuthorByLastNameAndName(author.getLastName(), author.getName()));

        newBook.setTitle(book.getTitle());
        newBook.setPublicationYear(book.getPublicationYear());
        newBook.setPages(book.getPages());
        newBook.setIsbn(book.getIsbn());

        return newBook;
    }

}
