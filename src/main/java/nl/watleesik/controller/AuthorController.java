package nl.watleesik.controller;

import nl.watleesik.api.ApiResponse;
import nl.watleesik.domain.Author;
import nl.watleesik.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "author")
@CrossOrigin(origins = "*")
public class AuthorController {

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorController (AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createAuthor(@RequestBody Author author) {

        Author authorDB = authorRepository.findAuthorByLastNameAndName(author.getLastName(), author.getName());

        if (authorDB != null) {

            return new ResponseEntity<>(new ApiResponse(409, "Er bestaat al een auteur in de database met deze naam!", null), HttpStatus.CONFLICT);
        }

        Author authorNew = new Author();
        authorNew.setName(author.getName());
        authorNew.setMiddleName(author.getMiddleName());
        authorNew.setLastName(author.getLastName());
        authorRepository.save(authorNew);
        return new ResponseEntity<>(new ApiResponse(200, "Nieuwe auteur is toegevoegd aan de collectie", authorRepository.save(authorNew)), HttpStatus.CREATED);
    }
}
