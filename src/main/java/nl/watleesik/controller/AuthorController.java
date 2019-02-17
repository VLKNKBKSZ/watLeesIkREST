package nl.watleesik.controller;

import nl.watleesik.api.ApiResponse;
import nl.watleesik.domain.Author;
import nl.watleesik.repository.AuthorRepository;
import nl.watleesik.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "author")
@CrossOrigin(origins = "*")
public class AuthorController {

    private AuthorRepository authorRepository;
    private AuthorService authorService;

    @Autowired
    public AuthorController(AuthorRepository authorRepository, AuthorService authorService) {
        this.authorRepository = authorRepository;
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createAuthor(@RequestBody Author author) {

        Author authorDB = authorRepository.findAuthorByLastNameAndName(author.getLastName(), author.getName());

        if (authorDB != null && (authorDB.equals(author))) {

            return new ResponseEntity<>(new ApiResponse(409, "Er bestaat al een auteur in de database met deze naam!", null), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new ApiResponse(200, "Nieuwe auteur is toegevoegd aan de collectie", authorRepository.save(authorService.createAuthor(author))), HttpStatus.CREATED);
    }
}
