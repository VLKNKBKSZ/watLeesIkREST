package nl.watleesik.repository;

import nl.watleesik.domain.Book;
import nl.watleesik.domain.BookType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookTypeRepository extends CrudRepository <BookType, Long> {
    public Book findBookTypeByName(String name);
}
