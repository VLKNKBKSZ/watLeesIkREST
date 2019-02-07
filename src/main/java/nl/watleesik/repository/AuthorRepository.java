package nl.watleesik.repository;

import nl.watleesik.domain.Author;
import nl.watleesik.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository <Author,Book> {

    public Author findAuthorByName(String name);
}
