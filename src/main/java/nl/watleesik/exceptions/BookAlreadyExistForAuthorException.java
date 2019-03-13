package nl.watleesik.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED, reason = "Het boek dat je wil toevoegen bestaat al voor deze auteur")
public class BookAlreadyExistForAuthorException extends Exception {

    private static final long serialVersionUID = 1L;

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
