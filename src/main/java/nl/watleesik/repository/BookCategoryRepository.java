package nl.watleesik.repository;

import nl.watleesik.domain.Book;
import nl.watleesik.domain.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {

    public Book findBookTypeByName(String name);

}
