package nl.watleesik.repository;

import nl.watleesik.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository <Book, Long> {

    public Book findBookByAuthor(String authorName);
    public Book findBookByTitle(String name);
}
