package nl.watleesik.service;

import nl.watleesik.domain.Author;
import nl.watleesik.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // author exist? return dbAuthor else create a new one
    public Author createAuthor(Author author) {

        Author authorDB = authorRepository.findAuthorByLastNameAndName(author.getLastName(), author.getName());

        if (authorDB != null && (authorDB.equals(author))) {

            return authorDB;
        }

        Author authorNew = new Author();
        authorNew.setName(author.getName());
        authorNew.setMiddleName(author.getMiddleName());
        authorNew.setLastName(author.getLastName());

        return authorRepository.save(authorNew);
    }
}
