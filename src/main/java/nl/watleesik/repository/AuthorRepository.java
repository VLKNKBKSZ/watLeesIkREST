package nl.watleesik.repository;

import nl.watleesik.domain.Author;
import nl.watleesik.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository <Author,Book> {

    public Author findAuthorByName(String name);
    public Author findAuthorByLastNameAndName(String lastName, String name);
}
