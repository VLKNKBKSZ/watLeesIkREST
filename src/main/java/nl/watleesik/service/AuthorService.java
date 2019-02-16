package nl.watleesik.service;

import nl.watleesik.domain.Author;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    public Author createAuthor(Author author) {

        Author authorNew = new Author();
        authorNew.setName(author.getName());
        authorNew.setMiddleName(author.getMiddleName());
        authorNew.setLastName(author.getLastName());

        return authorNew;
    }
}
